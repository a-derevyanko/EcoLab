package org.ekolab.server.dao.impl.content.lab1.random;

import org.ekolab.server.common.Profiles;
import org.ekolab.server.dao.api.content.lab1.random.Lab1RandomDao;
import org.ekolab.server.dao.impl.content.lab1.Lab1DaoImpl;
import org.ekolab.server.model.content.lab1.Lab1Variant;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import static org.ekolab.server.db.h2.public_.Tables.LAB1VARIANT;

/**
 * Created by 777Al on 19.04.2017.
 */
@Service
@Profile({Profiles.DB.H2, Profiles.DB.POSTGRES})
public class Lab1RandomDaoImpl extends Lab1DaoImpl<Lab1Variant> implements Lab1RandomDao {
    private static final Lab1DataMapper<Lab1Variant> RECORD_MAPPER = new Lab1DataMapper<Lab1Variant>() {

        @Override
        protected Lab1Variant createVariant() {
            return new Lab1Variant();
        }
    };

    @Override
    protected Lab1DataMapper<Lab1Variant> getLabMapper() {
        return RECORD_MAPPER;
    }

    @Override
    protected void saveVariant(long id, Lab1Variant variant) {
        dsl.insertInto(LAB1VARIANT,
                LAB1VARIANT.ID,
                LAB1VARIANT.NAME,
                LAB1VARIANT.TIME,
                LAB1VARIANT.OUTSIDE_AIR_TEMPERATURE,
                LAB1VARIANT.STEAM_PRODUCTION_CAPACITY,
                LAB1VARIANT.OXYGEN_CONCENTRATION_POINT,
                LAB1VARIANT.FUEL_CONSUMER,
                LAB1VARIANT.STACK_EXIT_TEMPERATURE,
                LAB1VARIANT.FLUE_GAS_NOX_CONCENTRATION).
                values(
                        id,
                        variant.getName(),
                        variant.getTime(),
                        variant.getOutsideAirTemperature(),
                        variant.getSteamProductionCapacity(),
                        variant.getOxygenConcentrationPoint(),
                        variant.getFuelConsumerNormalized(),
                        variant.getStackExitTemperature(),
                        variant.getFlueGasNOxConcentration()
                ).execute();
    }
}
