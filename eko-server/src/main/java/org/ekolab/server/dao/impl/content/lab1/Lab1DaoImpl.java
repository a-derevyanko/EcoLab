package org.ekolab.server.dao.impl.content.lab1;

import org.ekolab.server.common.Profiles;
import org.ekolab.server.dao.api.content.lab1.Lab1Dao;
import org.ekolab.server.dao.impl.DaoUtils;
import org.ekolab.server.dao.impl.content.LabDaoImpl;
import org.ekolab.server.model.content.lab1.Lab1Data;
import org.ekolab.server.model.content.lab1.Lab1Variant;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.ekolab.server.db.h2.public_.Tables.LAB1DATA;
import static org.ekolab.server.db.h2.public_.Tables.LAB1VARIANT;

/**
 * Created by 777Al on 19.04.2017.
 */
@Profile({Profiles.DB.H2, Profiles.DB.POSTGRES})
public abstract class Lab1DaoImpl<V extends Lab1Variant> extends LabDaoImpl<Lab1Data<V>> implements Lab1Dao<Lab1Data<V>> {
    protected abstract static class Lab1DataMapper<V extends Lab1Variant>  implements RecordMapper<Record, Lab1Data<V>> {
        @Override
        public Lab1Data<V> map(Record record) {
            Lab1Data<V> data = new Lab1Data<V>();
            data.setStartDate(record.get(LAB1DATA.START_DATE));
            data.setSaveDate(record.get(LAB1DATA.SAVE_DATE));
            data.setCompleted(record.get(LAB1DATA.COMPLETED));
            data.setName(record.get(LAB1DATA.NAME));
            data.setExcessAirRatio(record.get(LAB1DATA.EXCESS_AIR_RATIO));
            data.setFlueGasNOxConcentrationNC(record.get(LAB1DATA.FLUE_GAS_NOX_CONCENTRATION_NC));
            data.setExcessOfNorms(record.get(LAB1DATA.EXCESS_OF_NORMS));
            data.setFlueGasesRate(record.get(LAB1DATA.FLUE_GASES_RATE));
            data.setDryGasesFlowRate(record.get(LAB1DATA.DRY_GASES_FLOW_RATE));
            data.setMassEmissions(record.get(LAB1DATA.MASS_EMISSIONS));
            data.setFlueGasesSpeed(record.get(LAB1DATA.FLUE_GASES_SPEED));
            data.setF(record.get(LAB1DATA.F));
            data.setM(record.get(LAB1DATA.M));
            data.setU(record.get(LAB1DATA.U));
            data.setN(record.get(LAB1DATA.N));
            data.setD(record.get(LAB1DATA.D));
            data.setHarmfulSubstancesDepositionCoefficient(record.get(LAB1DATA.HARMFUL_SUBSTANCES_DEPOSITION_COEFFICIENT));
            data.setTerrainCoefficient(record.get(LAB1DATA.TERRAIN_COEFFICIENT));
            data.setTemperatureCoefficient(record.get(LAB1DATA.TEMPERATURE_COEFFICIENT));
            data.setDistanceFromEmissionSource(record.get(LAB1DATA.DISTANCE_FROM_EMISSION_SOURCE));
            data.setMaximumSurfaceConcentration(record.get(LAB1DATA.MAXIMUM_SURFACE_CONCENTRATION));

            V variant = createVariant();
            variant.setName(record.get(LAB1VARIANT.NAME));
            variant.setTime(record.get(LAB1VARIANT.TIME));
            variant.setOutsideAirTemperature(record.get(LAB1VARIANT.OUTSIDE_AIR_TEMPERATURE));
            variant.setSteamProductionCapacity(record.get(LAB1VARIANT.STEAM_PRODUCTION_CAPACITY));
            variant.setOxygenConcentrationPoint(record.get(LAB1VARIANT.OXYGEN_CONCENTRATION_POINT));
            variant.setFuelConsumerNormalized(record.get(LAB1VARIANT.FUEL_CONSUMER));
            variant.setStackExitTemperature(record.get(LAB1VARIANT.STACK_EXIT_TEMPERATURE));
            variant.setFlueGasNOxConcentration(record.get(LAB1VARIANT.FLUE_GAS_NOX_CONCENTRATION));

            data.setVariant(variant);

            return data;
        }

        protected abstract V createVariant();
    }

    @Override
    public Lab1Data<V> getLastLabByUser(String userName, boolean completed) {
        return dsl.select().from(LAB1DATA).join(LAB1VARIANT).on(LAB1VARIANT.ID.eq(LAB1DATA.ID)).
                where(LAB1DATA.USER_ID.eq(DaoUtils.getFindUserIdSelect(dsl, userName))).and(LAB1DATA.COMPLETED.eq(completed)).
                orderBy(LAB1DATA.SAVE_DATE.desc()).limit(1).fetchOne(getLabMapper());
    }

    @Override
    public List<Lab1Data<V>> getAllLabsByUser(String userName) {
        return dsl.select().from(LAB1DATA).join(LAB1VARIANT).on(LAB1VARIANT.ID.eq(LAB1DATA.ID))
                .where(LAB1DATA.USER_ID.eq(DaoUtils.getFindUserIdSelect(dsl, userName))).fetch(getLabMapper());
    }

    @Override
    public long saveLab(Lab1Data<V> data) {
        long id = dsl.insertInto(LAB1DATA,
                LAB1DATA.USER_ID,
                LAB1DATA.START_DATE,
                LAB1DATA.SAVE_DATE,
                LAB1DATA.COMPLETED,
                LAB1DATA.NAME,
                LAB1DATA.EXCESS_AIR_RATIO,
                LAB1DATA.FLUE_GAS_NOX_CONCENTRATION_NC,
                LAB1DATA.EXCESS_OF_NORMS,
                LAB1DATA.FLUE_GASES_RATE,
                LAB1DATA.DRY_GASES_FLOW_RATE,
                LAB1DATA.MASS_EMISSIONS,
                LAB1DATA.FLUE_GASES_SPEED,
                LAB1DATA.F,
                LAB1DATA.M,
                LAB1DATA.U,
                LAB1DATA.N,
                LAB1DATA.D,
                LAB1DATA.HARMFUL_SUBSTANCES_DEPOSITION_COEFFICIENT,
                LAB1DATA.TERRAIN_COEFFICIENT,
                LAB1DATA.TEMPERATURE_COEFFICIENT,
                LAB1DATA.DISTANCE_FROM_EMISSION_SOURCE,
                LAB1DATA.MAXIMUM_SURFACE_CONCENTRATION).
                values(
                        Arrays.asList(DaoUtils.getFindUserIdSelect(dsl, data.getUserLogin()),
                                data.getStartDate(),
                                data.getSaveDate(),
                                data.isCompleted(),
                                data.getName(),
                                data.getExcessAirRatio(),
                                data.getFlueGasNOxConcentrationNC(),
                                data.getExcessOfNorms(),
                                data.getFlueGasesRate(),
                                data.getDryGasesFlowRate(),
                                data.getMassEmissions(),
                                data.getFlueGasesSpeed(),
                                data.getF(),
                                data.getM(),
                                data.getU(),
                                data.getN(),
                                data.getD(),
                                data.getHarmfulSubstancesDepositionCoefficient(),
                                data.getTerrainCoefficient(),
                                data.getTemperatureCoefficient(),
                                data.getDistanceFromEmissionSource(),
                                data.getMaximumSurfaceConcentration())
                ).returning(LAB1DATA.ID).fetchOne().getId();

        saveVariant(id, data.getVariant());
        return id;
    }

    @Override
    public int updateLab(Lab1Data<V> data) {
        return dsl.update(LAB1DATA)
                .set(LAB1DATA.START_DATE, data.getStartDate())
                .set(LAB1DATA.SAVE_DATE, data.getSaveDate())
                .set(LAB1DATA.NAME, data.getName())
                .set(LAB1DATA.COMPLETED, data.isCompleted())
                .set(LAB1DATA.EXCESS_AIR_RATIO, data.getExcessAirRatio())
                .set(LAB1DATA.FLUE_GAS_NOX_CONCENTRATION_NC, data.getFlueGasNOxConcentrationNC())
                .set(LAB1DATA.EXCESS_OF_NORMS, data.getExcessOfNorms())
                .set(LAB1DATA.FLUE_GASES_RATE, data.getFlueGasesRate())
                .set(LAB1DATA.DRY_GASES_FLOW_RATE, data.getDryGasesFlowRate())
                .set(LAB1DATA.MASS_EMISSIONS, data.getMassEmissions())
                .set(LAB1DATA.FLUE_GASES_SPEED, data.getFlueGasesSpeed())
                .set(LAB1DATA.F, data.getF())
                .set(LAB1DATA.M, data.getM())
                .set(LAB1DATA.U, data.getU())
                .set(LAB1DATA.N, data.getN())
                .set(LAB1DATA.D, data.getD())
                .set(LAB1DATA.HARMFUL_SUBSTANCES_DEPOSITION_COEFFICIENT, data.getHarmfulSubstancesDepositionCoefficient())
                .set(LAB1DATA.TERRAIN_COEFFICIENT, data.getTerrainCoefficient())
                .set(LAB1DATA.TEMPERATURE_COEFFICIENT, data.getTemperatureCoefficient())
                .set(LAB1DATA.DISTANCE_FROM_EMISSION_SOURCE, data.getDistanceFromEmissionSource())
                .set(LAB1DATA.MAXIMUM_SURFACE_CONCENTRATION, data.getMaximumSurfaceConcentration())
                .where(LAB1DATA.USER_ID.eq(DaoUtils.getFindUserIdSelect(dsl, data.getUserLogin())).and(LAB1DATA.START_DATE.eq(data.getStartDate())))
                .execute();
    }

    @Override
    public int removeLabsByUser(String userName) {
        return dsl.deleteFrom(LAB1DATA).where(LAB1DATA.USER_ID.eq(DaoUtils.getFindUserIdSelect(dsl, userName))).execute();
    }

    @Override
    public int removeOldLabs(LocalDateTime lastSaveDate) {
        return dsl.deleteFrom(LAB1DATA).where(LAB1DATA.SAVE_DATE.greaterThan(lastSaveDate)).execute();
    }

    @Override
    protected int getLabNumber() {
        return 1;
    }

    protected abstract Lab1DataMapper<V> getLabMapper();

    protected abstract void saveVariant(long id, V variant);
}
