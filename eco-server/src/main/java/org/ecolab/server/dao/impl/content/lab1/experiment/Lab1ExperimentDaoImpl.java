package org.ecolab.server.dao.impl.content.lab1.experiment;

import org.ecolab.server.common.Profiles;
import org.ecolab.server.dao.api.content.lab1.experiment.Lab1ExperimentDao;
import org.ecolab.server.dao.impl.DaoUtils;
import org.ecolab.server.dao.impl.content.lab1.Lab1DaoImpl;
import org.ecolab.server.model.content.lab1.Lab1Data;
import org.ecolab.server.model.content.lab1.experiment.Lab1ExperimentLog;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import static org.ecolab.server.db.h2.public_.Tables.LAB1DATA;
import static org.ecolab.server.db.h2.public_.Tables.LAB1TEAM;
import static org.ecolab.server.db.h2.public_.Tables.LAB1_EXPERIMENT_LOG;

/**
 * Created by 777Al on 19.04.2017.
 */
@Service
@Profile({Profiles.DB.H2, Profiles.DB.POSTGRES})
public class Lab1ExperimentDaoImpl extends Lab1DaoImpl<Lab1ExperimentLog> implements Lab1ExperimentDao {
    private static final Lab1DataMapper<Lab1ExperimentLog> RECORD_MAPPER = new Lab1DataMapper<Lab1ExperimentLog>() {

        @Override
        public Lab1Data<Lab1ExperimentLog> map(Record record) {
            Lab1Data<Lab1ExperimentLog> data = super.map(record);
            data.getVariant().setId(record.get(LAB1_EXPERIMENT_LOG.ID));
            data.getVariant().setName(record.get(LAB1_EXPERIMENT_LOG.NAME));
            data.getVariant().setTime(record.get(LAB1_EXPERIMENT_LOG.TIME));
            data.getVariant().setOutsideAirTemperature(record.get(LAB1_EXPERIMENT_LOG.OUTSIDE_AIR_TEMPERATURE));
            data.getVariant().setSteamProductionCapacity(record.get(LAB1_EXPERIMENT_LOG.STEAM_PRODUCTION_CAPACITY));
            data.getVariant().setOxygenConcentrationPoint(record.get(LAB1_EXPERIMENT_LOG.OXYGEN_CONCENTRATION_POINT));
            data.getVariant().setFuelConsumerNormalized(record.get(LAB1_EXPERIMENT_LOG.FUEL_CONSUMER));
            data.getVariant().setStackExitTemperature(record.get(LAB1_EXPERIMENT_LOG.STACK_EXIT_TEMPERATURE));
            data.getVariant().setFlueGasNOxConcentration(record.get(LAB1_EXPERIMENT_LOG.FLUE_GAS_NOX_CONCENTRATION));
            data.getVariant().setStacksHeight(record.get(LAB1_EXPERIMENT_LOG.STACKS_HEIGHT));
            data.getVariant().setStacksDiameter(record.get(LAB1_EXPERIMENT_LOG.STACKS_DIAMETER));
            return data;
        }

        @Override
        protected Lab1ExperimentLog createVariant() {
            return new Lab1ExperimentLog();
        }
    };

    public Lab1ExperimentDaoImpl(DSLContext dsl) {
        super(dsl);
    }

    @Override
    public Lab1Data<Lab1ExperimentLog> getLastLabByUser(String userName, boolean completed) {
        Lab1Data<Lab1ExperimentLog> data = dsl.select().from(LAB1DATA).join(LAB1_EXPERIMENT_LOG).on(LAB1_EXPERIMENT_LOG.ID.eq(LAB1DATA.ID)).
                where(LAB1DATA.ID.eq(dsl.select(LAB1TEAM.ID).from(LAB1TEAM).where(LAB1TEAM.USER_ID.eq(DaoUtils.getFindUserIdSelect(dsl, userName)))))
                        .and(LAB1DATA.COMPLETED.eq(completed)).
                orderBy(LAB1DATA.SAVE_DATE.desc()).limit(1).fetchOne(getLabMapper());
        if (data != null) {
            fillLabUsers(data);
        }

        return data;
    }

    @Override
    protected Lab1DataMapper<Lab1ExperimentLog> getLabMapper() {
        return RECORD_MAPPER;
    }

    @Override
    protected void saveVariant(Lab1ExperimentLog variant) {
        dsl.insertInto(LAB1_EXPERIMENT_LOG,
                LAB1_EXPERIMENT_LOG.ID,
                LAB1_EXPERIMENT_LOG.NAME,
                LAB1_EXPERIMENT_LOG.TIME,
                LAB1_EXPERIMENT_LOG.OUTSIDE_AIR_TEMPERATURE,
                LAB1_EXPERIMENT_LOG.STACKS_HEIGHT,
                LAB1_EXPERIMENT_LOG.STACKS_DIAMETER,
                LAB1_EXPERIMENT_LOG.STEAM_PRODUCTION_CAPACITY,
                LAB1_EXPERIMENT_LOG.OXYGEN_CONCENTRATION_POINT,
                LAB1_EXPERIMENT_LOG.FUEL_CONSUMER,
                LAB1_EXPERIMENT_LOG.STACK_EXIT_TEMPERATURE,
                LAB1_EXPERIMENT_LOG.FLUE_GAS_NOX_CONCENTRATION).
                values(
                        variant.getId(),
                        variant.getName(),
                        variant.getTime(),
                        variant.getOutsideAirTemperature(),
                        variant.getStacksHeight(),
                        variant.getStacksDiameter(),
                        variant.getSteamProductionCapacity(),
                        variant.getOxygenConcentrationPoint(),
                        variant.getFuelConsumerNormalized(),
                        variant.getStackExitTemperature(),
                        variant.getFlueGasNOxConcentration()
                ).execute();
    }

    @Override
    public void updateExperimentJournal(Lab1ExperimentLog experimentJournal) {
        dsl.update(LAB1_EXPERIMENT_LOG)
                .set(LAB1_EXPERIMENT_LOG.NAME, experimentJournal.getName())
                .set(LAB1_EXPERIMENT_LOG.TIME, experimentJournal.getTime())
                .set(LAB1_EXPERIMENT_LOG.OUTSIDE_AIR_TEMPERATURE, experimentJournal.getOutsideAirTemperature())
                .set(LAB1_EXPERIMENT_LOG.STACKS_HEIGHT, experimentJournal.getStacksHeight())
                .set(LAB1_EXPERIMENT_LOG.STACKS_DIAMETER, experimentJournal.getStacksDiameter())
                .set(LAB1_EXPERIMENT_LOG.STEAM_PRODUCTION_CAPACITY, experimentJournal.getSteamProductionCapacity())
                .set(LAB1_EXPERIMENT_LOG.OXYGEN_CONCENTRATION_POINT, experimentJournal.getOxygenConcentrationPoint())
                .set(LAB1_EXPERIMENT_LOG.FUEL_CONSUMER, experimentJournal.getFuelConsumerNormalized())
                .set(LAB1_EXPERIMENT_LOG.STACK_EXIT_TEMPERATURE, experimentJournal.getStackExitTemperature())
                .set(LAB1_EXPERIMENT_LOG.FLUE_GAS_NOX_CONCENTRATION, experimentJournal.getFlueGasNOxConcentration())
                .where(LAB1_EXPERIMENT_LOG.ID.eq(experimentJournal.getId()))
                .execute();
    }
}
