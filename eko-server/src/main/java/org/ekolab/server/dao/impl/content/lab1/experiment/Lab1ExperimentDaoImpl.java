package org.ekolab.server.dao.impl.content.lab1.experiment;

import org.ekolab.server.common.Profiles;
import org.ekolab.server.dao.api.content.lab1.experiment.Lab1ExperimentDao;
import org.ekolab.server.dao.impl.content.lab1.Lab1DaoImpl;
import org.ekolab.server.model.content.lab1.Lab1ExperimentLog;
import org.ekolab.server.model.content.lab1.experiment.Lab1ExperimentData;
import org.jooq.Record;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import static org.ekolab.server.db.h2.public_.Tables.LAB1VARIANT;

/**
 * Created by 777Al on 19.04.2017.
 */
@Service
@Profile({Profiles.DB.H2, Profiles.DB.POSTGRES})
public class Lab1ExperimentDaoImpl extends Lab1DaoImpl<Lab1ExperimentData, Lab1ExperimentLog> implements Lab1ExperimentDao {
    private static final Lab1DataMapper<Lab1ExperimentData, Lab1ExperimentLog> RECORD_MAPPER = new Lab1DataMapper<Lab1ExperimentData, Lab1ExperimentLog>() {

        @Override
        public Lab1ExperimentData map(Record record) {
            Lab1ExperimentData data = super.map(record);
            data.getVariant().setStacksHeight(record.get(LAB1VARIANT.STACKS_HEIGHT));
            data.getVariant().setStacksDiameter(record.get(LAB1VARIANT.STACKS_DIAMETER));
            return data;
        }

        @Override
        protected Lab1ExperimentLog createVariant() {
            return new Lab1ExperimentLog();
        }

        @Override
        protected Lab1ExperimentData createData() {
            return new Lab1ExperimentData();
        }
    };

    @Override
    protected Lab1DataMapper<Lab1ExperimentData, Lab1ExperimentLog> getLabMapper() {
        return RECORD_MAPPER;
    }

    @Override
    protected void saveVariant(long id, Lab1ExperimentLog variant) {
        dsl.insertInto(LAB1VARIANT,
                LAB1VARIANT.ID,
                LAB1VARIANT.NAME,
                LAB1VARIANT.OUTSIDE_AIR_TEMPERATURE,
                LAB1VARIANT.STACKS_HEIGHT,
                LAB1VARIANT.STACKS_DIAMETER,
                LAB1VARIANT.STEAM_PRODUCTION_CAPACITY,
                LAB1VARIANT.OXYGEN_CONCENTRATION_POINT,
                LAB1VARIANT.FUEL_CONSUMER,
                LAB1VARIANT.STACK_EXIT_TEMPERATURE,
                LAB1VARIANT.FLUE_GAS_NOX_CONCENTRATION).
                values(
                        id,
                        variant.getName(),
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
}
