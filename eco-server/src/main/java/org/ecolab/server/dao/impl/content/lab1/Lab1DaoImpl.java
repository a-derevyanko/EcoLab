package org.ecolab.server.dao.impl.content.lab1;

import com.google.common.collect.Sets;
import java.time.LocalDateTime;
import java.util.Arrays;
import org.ecolab.server.common.Profiles;
import org.ecolab.server.dao.api.content.lab1.Lab1Dao;
import org.ecolab.server.dao.impl.DaoUtils;
import org.ecolab.server.dao.impl.content.LabDaoImpl;
import org.ecolab.server.db.h2.public_.tables.records.Lab1dataRecord;
import org.ecolab.server.model.content.lab1.Lab1Data;
import org.ecolab.server.model.content.lab1.Lab1Variant;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.impl.DSL;
import org.springframework.context.annotation.Profile;


import static org.ecolab.server.db.h2.public_.Tables.LAB1DATA;
import static org.ecolab.server.db.h2.public_.Tables.LAB1TEAM;
import static org.ecolab.server.db.h2.public_.Tables.USERS;
import static org.ecolab.server.db.h2.public_.Tables.USER_LAB_ALLOWANCE;

/**
 * Created by 777Al on 19.04.2017.
 */
@Profile({Profiles.DB.H2, Profiles.DB.POSTGRES})
public abstract class Lab1DaoImpl<V extends Lab1Variant> extends LabDaoImpl<Lab1Data<V>> implements Lab1Dao<V> {
    public Lab1DaoImpl(DSLContext dsl) {
        super(dsl);
    }

    protected abstract static class Lab1DataMapper<V extends Lab1Variant>  implements RecordMapper<Record, Lab1Data<V>> {
        @Override
        public Lab1Data<V> map(Record record) {
            var data = new Lab1Data<V>();
            data.setId(record.get(LAB1DATA.ID));
            data.setStartDate(record.get(LAB1DATA.START_DATE));
            data.setSaveDate(record.get(LAB1DATA.SAVE_DATE));
            data.setCompleted(record.get(LAB1DATA.COMPLETED));
            data.setStacksHeight(record.get(LAB1DATA.STACKS_HEIGHT));
            data.setStacksDiameter(record.get(LAB1DATA.STACKS_DIAMETER));
            data.setOutsideAirTemperature(record.get(LAB1DATA.OUTSIDE_AIR_TEMPERATURE));
            data.setSteamProductionCapacity(record.get(LAB1DATA.STEAM_PRODUCTION_CAPACITY));
            data.setOxygenConcentrationPoint(record.get(LAB1DATA.OXYGEN_CONCENTRATION_POINT));
            data.setFuelConsumerNormalized(record.get(LAB1DATA.FUEL_CONSUMER_NORMALIZED));
            data.setStackExitTemperature(record.get(LAB1DATA.STACK_EXIT_TEMPERATURE));
            data.setFlueGasNOxConcentration(record.get(LAB1DATA.FLUE_GAS_NOX_CONCENTRATION));
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

            var variant = createVariant();
            data.setVariant(variant);

            return data;
        }

        protected abstract V createVariant();
    }

    @Override
    public void saveLab(Lab1Data<V> data) {
        var record = dsl.insertInto(LAB1DATA,
                LAB1DATA.COMPLETED,
                LAB1DATA.STACKS_DIAMETER,
                LAB1DATA.STACKS_HEIGHT,
                LAB1DATA.OUTSIDE_AIR_TEMPERATURE,
                LAB1DATA.STEAM_PRODUCTION_CAPACITY,
                LAB1DATA.OXYGEN_CONCENTRATION_POINT,
                LAB1DATA.FUEL_CONSUMER_NORMALIZED,
                LAB1DATA.STACK_EXIT_TEMPERATURE,
                LAB1DATA.FLUE_GAS_NOX_CONCENTRATION,
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
                        Arrays.asList(
                                data.isCompleted(),
                                data.getStacksDiameter(),
                                data.getStacksHeight(),
                                data.getOutsideAirTemperature(),
                                data.getSteamProductionCapacity(),
                                data.getOxygenConcentrationPoint(),
                                data.getFuelConsumerNormalized(),
                                data.getStackExitTemperature(),
                                data.getFlueGasNOxConcentration(),
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
                ).returning(LAB1DATA.ID, LAB1DATA.START_DATE, LAB1DATA.SAVE_DATE)
                .fetchOne();

        data.setId(record.getId());
        data.setSaveDate(record.getSaveDate());
        data.setStartDate(record.getStartDate());

        data.getVariant().setId(data.getId());
        saveVariant(data.getVariant());
        saveLabUsers(data);
    }

    @Override
    public int updateLab(Lab1Data<V> data) {
        return dsl.update(LAB1DATA)
                .set(LAB1DATA.START_DATE, data.getStartDate())
                .set(LAB1DATA.SAVE_DATE, data.getSaveDate())
                .set(LAB1DATA.COMPLETED, data.isCompleted())
                .set(LAB1DATA.STACKS_HEIGHT, data.getStacksHeight())
                .set(LAB1DATA.STACKS_DIAMETER, data.getStacksDiameter())
                .set(LAB1DATA.OUTSIDE_AIR_TEMPERATURE, data.getOutsideAirTemperature())
                .set(LAB1DATA.STEAM_PRODUCTION_CAPACITY, data.getSteamProductionCapacity())
                .set(LAB1DATA.OXYGEN_CONCENTRATION_POINT, data.getOxygenConcentrationPoint())
                .set(LAB1DATA.FUEL_CONSUMER_NORMALIZED, data.getFuelConsumerNormalized())
                .set(LAB1DATA.STACK_EXIT_TEMPERATURE, data.getStackExitTemperature())
                .set(LAB1DATA.FLUE_GAS_NOX_CONCENTRATION, data.getFlueGasNOxConcentration())
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
                .where(LAB1DATA.ID.eq(data.getId()))
                .execute();
    }

    @Override
    public int removeLabsByUser(String userName) {
        return dsl.deleteFrom(LAB1DATA).where(LAB1DATA.ID.in(dsl.select(LAB1TEAM.ID).from(LAB1TEAM).where(LAB1TEAM.USER_ID.eq(DaoUtils.getFindUserIdSelect(dsl, userName))))).execute();
    }

    @Override
    public int removeOldLabs(LocalDateTime lastSaveDate) {
        return dsl.deleteFrom(LAB1DATA).where(LAB1DATA.SAVE_DATE.greaterThan(lastSaveDate)).execute();
    }

    @Override
    protected int getLabNumber() {
        return 1;
    }

    @Override
    public void setLabCompleted(Lab1Data<V> data) {
        dsl.update(LAB1DATA).set(LAB1DATA.COMPLETED, true).where(LAB1DATA.ID.eq(data.getId())).execute();
        dsl.deleteFrom(USER_LAB_ALLOWANCE).where(USER_LAB_ALLOWANCE.LAB_NUMBER.eq(getLabNumber())
                .and(USER_LAB_ALLOWANCE.USER_ID.in(dsl.select(LAB1TEAM.USER_ID).from(LAB1TEAM).where(LAB1TEAM.ID.eq(data.getId()))))).execute();
    }

    protected abstract Lab1DataMapper<V> getLabMapper();

    protected abstract void saveVariant(V variant);

  @Override
  protected void fillLabUsers(Lab1Data<V> data) {
    data.setUsers(Sets.newHashSet(dsl.select(USERS.LOGIN).from(USERS).join(LAB1TEAM).on(USERS.ID.eq(LAB1TEAM.USER_ID)).
            where(LAB1TEAM.ID.eq(data.getId())).fetchInto(String.class)));
  }

  @Override
  protected void saveLabUsers(Lab1Data<V> data) {
    dsl.insertInto(LAB1TEAM, LAB1TEAM.ID, LAB1TEAM.USER_ID).
            select(dsl.select(DSL.val(data.getId()), USERS.ID).from(USERS).where(USERS.LOGIN.in(data.getUsers()))).execute();
  }
}
