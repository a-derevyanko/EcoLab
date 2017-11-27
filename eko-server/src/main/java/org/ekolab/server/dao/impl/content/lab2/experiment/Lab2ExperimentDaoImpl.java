package org.ekolab.server.dao.impl.content.lab2.experiment;

import org.ekolab.server.common.Profiles;
import org.ekolab.server.dao.api.content.lab2.experiment.Lab2ExperimentDao;
import org.ekolab.server.dao.impl.DaoUtils;
import org.ekolab.server.dao.impl.content.lab2.Lab2DaoImpl;
import org.ekolab.server.model.content.lab2.Lab2Data;
import org.ekolab.server.model.content.lab2.Lab2ExperimentLog;
import org.jooq.Record;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.ekolab.server.db.h2.public_.Tables.LAB1DATA;
import static org.ekolab.server.db.h2.public_.Tables.LAB1_EXPERIMENT_LOG;

/**
 * Created by 777Al on 19.04.2017.
 */
@Service
@Profile({Profiles.DB.H2, Profiles.DB.POSTGRES})
public class Lab2ExperimentDaoImpl extends Lab2DaoImpl<Lab2ExperimentLog> implements Lab2ExperimentDao {
    private static final Lab2DataMapper<Lab2ExperimentLog> RECORD_MAPPER = new Lab2DataMapper<Lab2ExperimentLog>() {

        @Override
        public Lab2Data<Lab2ExperimentLog> map(Record record) {
            Lab2Data<Lab2ExperimentLog> data = super.map(record);
            data.getVariant().setId(record.get(LAB1_EXPERIMENT_LOG.ID));
            /*data.getVariant().setName(record.get(LAB1_EXPERIMENT_LOG.NAME));
            data.getVariant().setTime(record.get(LAB1_EXPERIMENT_LOG.TIME));
            data.getVariant().setOutsideAirTemperature(record.get(LAB1_EXPERIMENT_LOG.OUTSIDE_AIR_TEMPERATURE));
            data.getVariant().setSteamProductionCapacity(record.get(LAB1_EXPERIMENT_LOG.STEAM_PRODUCTION_CAPACITY));
            data.getVariant().setOxygenConcentrationPoint(record.get(LAB1_EXPERIMENT_LOG.OXYGEN_CONCENTRATION_POINT));
            data.getVariant().setFuelConsumerNormalized(record.get(LAB1_EXPERIMENT_LOG.FUEL_CONSUMER));
            data.getVariant().setStackExitTemperature(record.get(LAB1_EXPERIMENT_LOG.STACK_EXIT_TEMPERATURE));
            data.getVariant().setFlueGasNOxConcentration(record.get(LAB1_EXPERIMENT_LOG.FLUE_GAS_NOX_CONCENTRATION));
            data.getVariant().setStacksHeight(record.get(LAB1_EXPERIMENT_LOG.STACKS_HEIGHT));
            data.getVariant().setStacksDiameter(record.get(LAB1_EXPERIMENT_LOG.STACKS_DIAMETER));*/
            return data;
        }

        @Override
        protected Lab2ExperimentLog createVariant() {
            return new Lab2ExperimentLog();
        }
    };

    @Override
    public Lab2Data<Lab2ExperimentLog> getLastLabByUser(String userName, boolean completed) {
        return dsl.select().from(LAB1DATA).join(LAB1_EXPERIMENT_LOG).on(LAB1_EXPERIMENT_LOG.ID.eq(LAB1DATA.ID)).
                where(LAB1DATA.USER_ID.eq(DaoUtils.getFindUserIdSelect(dsl, userName))).and(LAB1DATA.COMPLETED.eq(completed)).
                orderBy(LAB1DATA.SAVE_DATE.desc()).limit(1).fetchOne(getLabMapper());
    }

    @Override
    public List<Lab2Data<Lab2ExperimentLog>> getAllLabsByUser(String userName) {
        return dsl.select().from(LAB1DATA).join(LAB1_EXPERIMENT_LOG).on(LAB1_EXPERIMENT_LOG.ID.eq(LAB1DATA.ID))
                .where(LAB1DATA.USER_ID.eq(DaoUtils.getFindUserIdSelect(dsl, userName))).fetch(getLabMapper());
    }

    @Override
    protected Lab2DataMapper<Lab2ExperimentLog> getLabMapper() {
        return RECORD_MAPPER;
    }

    @Override
    protected void saveVariant(Lab2ExperimentLog variant) {
       /* dsl.insertInto(LAB1_EXPERIMENT_LOG,
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
                ).execute();*/
    }

    @Override
    public void updateExperimentJournal(Lab2ExperimentLog experimentJournal) {
       /* dsl.update(LAB1_EXPERIMENT_LOG)
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
                .execute();*/
    }
}
