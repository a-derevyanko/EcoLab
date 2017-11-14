package org.ekolab.server.dao.impl.content.lab1.random;

import org.ekolab.server.common.Profiles;
import org.ekolab.server.dao.api.content.lab1.random.Lab1RandomDao;
import org.ekolab.server.dao.impl.DaoUtils;
import org.ekolab.server.dao.impl.content.lab1.Lab1DaoImpl;
import org.ekolab.server.model.content.lab1.Lab1Data;
import org.ekolab.server.model.content.lab1.Lab1Variant;
import org.jooq.Record;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.ekolab.server.db.h2.public_.Tables.LAB1DATA;
import static org.ekolab.server.db.h2.public_.Tables.LAB1VARIANT;

/**
 * Created by 777Al on 19.04.2017.
 */
@Service
@Profile({Profiles.DB.H2, Profiles.DB.POSTGRES})
public class Lab1RandomDaoImpl extends Lab1DaoImpl<Lab1Variant> implements Lab1RandomDao {
    private static final Lab1DataMapper<Lab1Variant> RECORD_MAPPER = new Lab1DataMapper<Lab1Variant>() {

        @Override
        public Lab1Data<Lab1Variant> map(Record record) {
            Lab1Data<Lab1Variant> data = super.map(record);
            data.getVariant().setId(record.get(LAB1VARIANT.ID));
            data.getVariant().setName(record.get(LAB1VARIANT.NAME));
            data.getVariant().setOutsideAirTemperature(record.get(LAB1VARIANT.OUTSIDE_AIR_TEMPERATURE));
            data.getVariant().setSteamProductionCapacity(record.get(LAB1VARIANT.STEAM_PRODUCTION_CAPACITY));
            data.getVariant().setOxygenConcentrationPoint(record.get(LAB1VARIANT.OXYGEN_CONCENTRATION_POINT));
            data.getVariant().setFuelConsumerNormalized(record.get(LAB1VARIANT.FUEL_CONSUMER));
            data.getVariant().setStackExitTemperature(record.get(LAB1VARIANT.STACK_EXIT_TEMPERATURE));
            data.getVariant().setFlueGasNOxConcentration(record.get(LAB1VARIANT.FLUE_GAS_NOX_CONCENTRATION));
            return data;
        }

        @Override
        protected Lab1Variant createVariant() {
            return new Lab1Variant();
        }
    };

    @Override
    public Lab1Data<Lab1Variant> getLastLabByUser(String userName, boolean completed) {
        return dsl.select().from(LAB1DATA).join(LAB1VARIANT).on(LAB1VARIANT.ID.eq(LAB1DATA.ID)).
                where(LAB1DATA.USER_ID.eq(DaoUtils.getFindUserIdSelect(dsl, userName))).and(LAB1DATA.COMPLETED.eq(completed)).
                orderBy(LAB1DATA.SAVE_DATE.desc()).limit(1).fetchOne(getLabMapper());
    }

    @Override
    public List<Lab1Data<Lab1Variant>> getAllLabsByUser(String userName) {
        return dsl.select().from(LAB1DATA).join(LAB1VARIANT).on(LAB1VARIANT.ID.eq(LAB1DATA.ID))
                .where(LAB1DATA.USER_ID.eq(DaoUtils.getFindUserIdSelect(dsl, userName))).fetch(getLabMapper());
    }

    @Override
    protected Lab1DataMapper<Lab1Variant> getLabMapper() {
        return RECORD_MAPPER;
    }

    @Override
    protected void saveVariant(Lab1Variant variant) {
        dsl.insertInto(LAB1VARIANT,
                LAB1VARIANT.ID,
                LAB1VARIANT.NAME,
                LAB1VARIANT.OUTSIDE_AIR_TEMPERATURE,
                LAB1VARIANT.STEAM_PRODUCTION_CAPACITY,
                LAB1VARIANT.OXYGEN_CONCENTRATION_POINT,
                LAB1VARIANT.FUEL_CONSUMER,
                LAB1VARIANT.STACK_EXIT_TEMPERATURE,
                LAB1VARIANT.FLUE_GAS_NOX_CONCENTRATION).
                values(
                        variant.getId(),
                        variant.getName(),
                        variant.getOutsideAirTemperature(),
                        variant.getSteamProductionCapacity(),
                        variant.getOxygenConcentrationPoint(),
                        variant.getFuelConsumerNormalized(),
                        variant.getStackExitTemperature(),
                        variant.getFlueGasNOxConcentration()
                ).execute();
    }
}
