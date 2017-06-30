package org.ekolab.server.dao.impl.content.lab1;

import org.ekolab.server.common.Profiles;
import org.ekolab.server.dao.api.content.lab1.Lab1Dao;
import org.ekolab.server.dao.impl.content.LabDaoImpl;
import org.ekolab.server.model.content.lab1.Lab1Data;
import org.ekolab.server.model.content.lab1.Lab1Variant;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static org.ekolab.server.db.h2.public_.Tables.LAB1DATA;
import static org.ekolab.server.db.h2.public_.Tables.LAB1VARIANT;

/**
 * Created by 777Al on 19.04.2017.
 */
@Service
@Profile({Profiles.DB.H2, Profiles.DB.POSTGRES})
public class Lab1DaoImpl extends LabDaoImpl<Lab1Data> implements Lab1Dao {
    private static final RecordMapper<Record, Lab1Data> LAB1DATA_MAPPER = record -> {
        Lab1Data data = new Lab1Data();
        data.setStartDate(record.get(LAB1DATA.START_DATE));
        data.setSaveDate(record.get(LAB1DATA.SAVE_DATE));
        data.setCompleted(record.get(LAB1DATA.COMPLETED));
        data.setName(record.get(LAB1DATA.NAME));
        data.setBarometricPressure(record.get(LAB1DATA.BAROMETRIC_PRESSURE));
        data.setOutsideAirTemperature(record.get(LAB1DATA.OUTSIDE_AIR_TEMPERATURE));
        data.setStacksHeight(record.get(LAB1DATA.STACKS_HEIGHT));
        data.setStacksDiameter(record.get(LAB1DATA.STACKS_DIAMETER));
        data.setSteamProductionCapacity(record.get(LAB1DATA.STEAM_PRODUCTION_CAPACITY));
        data.setOxygenConcentration(record.get(LAB1DATA.OXYGEN_CONCENTRATION));
        data.setOxygenConcentrationPoint(record.get(LAB1DATA.OXYGEN_CONCENTRATION_POINT));
        data.setFuelConsumer(record.get(LAB1DATA.FUEL_CONSUMER));
        data.setExcessPressure(record.get(LAB1DATA.EXCESS_PRESSURE));
        data.setGasTemperature(record.get(LAB1DATA.GAS_TEMPERATURE));
        data.setStackExitTemperature(record.get(LAB1DATA.STACK_EXIT_TEMPERATURE));
        data.setFlueGasNOxConcentration(record.get(LAB1DATA.FLUE_GAS_NOX_CONCENTRATION));
        data.setExcessAirRatio(record.get(LAB1DATA.EXCESS_AIR_RATIO));
        data.setFlueGasNOxConcentrationNC(record.get(LAB1DATA.FLUE_GAS_NOX_CONCENTRATION_NC));
        data.setExcessOfNorms(record.get(LAB1DATA.EXCESS_OF_NORMS));
        data.setValidBarometricPressure(record.get(LAB1DATA.VALID_BAROMETRIC_PRESSURE));
        data.setValidAbsolutePressure(record.get(LAB1DATA.VALID_ABSOLUTE_PRESSURE));
        data.setCorrectionFactor(record.get(LAB1DATA.CORRECTION_FACTOR));
        data.setFuelConsumerCorrection(record.get(LAB1DATA.FUEL_CONSUMER_CORRECTION));
        data.setFuelConsumerNC(record.get(LAB1DATA.FUEL_CONSUMER_NC));
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

        Lab1Variant variant = new Lab1Variant();
        variant.setName(record.get(LAB1VARIANT.NAME));
        variant.setBarometricPressure(record.get(LAB1VARIANT.BAROMETRIC_PRESSURE));
        variant.setOutsideAirTemperature(record.get(LAB1VARIANT.OUTSIDE_AIR_TEMPERATURE));
        variant.setStacksHeight(record.get(LAB1VARIANT.STACKS_HEIGHT));
        variant.setStacksDiameter(record.get(LAB1VARIANT.STACKS_DIAMETER));
        variant.setSteamProductionCapacity(record.get(LAB1VARIANT.STEAM_PRODUCTION_CAPACITY));
        variant.setOxygenConcentration(record.get(LAB1VARIANT.OXYGEN_CONCENTRATION));
        variant.setOxygenConcentrationPoint(record.get(LAB1VARIANT.OXYGEN_CONCENTRATION_POINT));
        variant.setFuelConsumer(record.get(LAB1VARIANT.FUEL_CONSUMER));
        variant.setExcessPressure(record.get(LAB1VARIANT.EXCESS_PRESSURE));
        variant.setGasTemperature(record.get(LAB1VARIANT.GAS_TEMPERATURE));
        variant.setStackExitTemperature(record.get(LAB1VARIANT.STACK_EXIT_TEMPERATURE));
        variant.setFlueGasNOxConcentration(record.get(LAB1VARIANT.FLUE_GAS_NOX_CONCENTRATION));

        data.setVariant(variant);

        return data;
    };

    @Override
    public Lab1Data getLastUncompletedLabByUser(String userName) {
        return dsl.select().from(LAB1DATA).join(LAB1VARIANT).on(LAB1VARIANT.ID.eq(LAB1DATA.ID)).
                where(LAB1DATA.USER_ID.eq(getFindUserIdSelect(userName))).and(LAB1DATA.COMPLETED.isFalse()).
                orderBy(LAB1DATA.SAVE_DATE.desc()).limit(1).fetchOne(LAB1DATA_MAPPER);
    }

    @Override
    public List<Lab1Data> getAllLabsByUser(String userName) {
        return dsl.select().from(LAB1DATA).join(LAB1VARIANT).on(LAB1VARIANT.ID.eq(LAB1DATA.ID))
                .where(LAB1DATA.USER_ID.eq(getFindUserIdSelect(userName))).fetch(LAB1DATA_MAPPER);
    }

    @Override
    public long saveLab(Lab1Data data) {
        long id = dsl.insertInto(LAB1DATA,
                LAB1DATA.USER_ID,
                LAB1DATA.START_DATE,
                LAB1DATA.SAVE_DATE,
                LAB1DATA.COMPLETED,
                LAB1DATA.NAME,
                LAB1DATA.BAROMETRIC_PRESSURE,
                LAB1DATA.OUTSIDE_AIR_TEMPERATURE,
                LAB1DATA.STACKS_HEIGHT,
                LAB1DATA.STACKS_DIAMETER,
                LAB1DATA.STEAM_PRODUCTION_CAPACITY,
                LAB1DATA.OXYGEN_CONCENTRATION,
                LAB1DATA.OXYGEN_CONCENTRATION_POINT,
                LAB1DATA.FUEL_CONSUMER,
                LAB1DATA.EXCESS_PRESSURE,
                LAB1DATA.GAS_TEMPERATURE,
                LAB1DATA.STACK_EXIT_TEMPERATURE,
                LAB1DATA.FLUE_GAS_NOX_CONCENTRATION,
                LAB1DATA.EXCESS_AIR_RATIO,
                LAB1DATA.FLUE_GAS_NOX_CONCENTRATION_NC,
                LAB1DATA.EXCESS_OF_NORMS,
                LAB1DATA.VALID_BAROMETRIC_PRESSURE,
                LAB1DATA.VALID_ABSOLUTE_PRESSURE,
                LAB1DATA.CORRECTION_FACTOR,
                LAB1DATA.FUEL_CONSUMER_CORRECTION,
                LAB1DATA.FUEL_CONSUMER_NC,
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
                        getFindUserIdSelect(data.getUserLogin()),
                        data.getStartDate(),
                        data.getSaveDate(),
                        data.isCompleted(),
                        data.getName(),
                        data.getBarometricPressure(),
                        data.getOutsideAirTemperature(),
                        data.getStacksHeight(),
                        data.getStacksDiameter(),
                        data.getSteamProductionCapacity(),
                        data.getOxygenConcentration(),
                        data.getOxygenConcentrationPoint(),
                        data.getFuelConsumer(),
                        data.getExcessPressure(),
                        data.getGasTemperature(),
                        data.getStackExitTemperature(),
                        data.getFlueGasNOxConcentration(),
                        data.getExcessAirRatio(),
                        data.getFlueGasNOxConcentrationNC(),
                        data.getExcessOfNorms(),
                        data.getValidBarometricPressure(),
                        data.getValidAbsolutePressure(),
                        data.getCorrectionFactor(),
                        data.getFuelConsumerCorrection(),
                        data.getFuelConsumerNC(),
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
                        data.getMaximumSurfaceConcentration()
                ).returning(LAB1DATA.ID).fetchOne().getId();

        Lab1Variant variant = data.getVariant();
        dsl.insertInto(LAB1VARIANT,
                LAB1VARIANT.ID,
                LAB1VARIANT.NAME,
                LAB1VARIANT.BAROMETRIC_PRESSURE,
                LAB1VARIANT.OUTSIDE_AIR_TEMPERATURE,
                LAB1VARIANT.STACKS_HEIGHT,
                LAB1VARIANT.STACKS_DIAMETER,
                LAB1VARIANT.STEAM_PRODUCTION_CAPACITY,
                LAB1VARIANT.OXYGEN_CONCENTRATION,
                LAB1VARIANT.OXYGEN_CONCENTRATION_POINT,
                LAB1VARIANT.FUEL_CONSUMER,
                LAB1VARIANT.EXCESS_PRESSURE,
                LAB1VARIANT.GAS_TEMPERATURE,
                LAB1VARIANT.STACK_EXIT_TEMPERATURE,
                LAB1VARIANT.FLUE_GAS_NOX_CONCENTRATION).
                values(
                        id,
                        variant.getName(),
                        variant.getBarometricPressure(),
                        variant.getOutsideAirTemperature(),
                        variant.getStacksHeight(),
                        variant.getStacksDiameter(),
                        variant.getSteamProductionCapacity(),
                        variant.getOxygenConcentration(),
                        variant.getOxygenConcentrationPoint(),
                        variant.getFuelConsumer(),
                        variant.getExcessPressure(),
                        variant.getGasTemperature(),
                        variant.getStackExitTemperature(),
                        variant.getFlueGasNOxConcentration()
                ).execute();
        return id;
    }

    @Override
    public int updateLab(Lab1Data data) {
        return dsl.update(LAB1DATA)
                .set(LAB1DATA.START_DATE, data.getStartDate())
                .set(LAB1DATA.SAVE_DATE, data.getSaveDate())
                .set(LAB1DATA.NAME, data.getName())
                .set(LAB1DATA.BAROMETRIC_PRESSURE, data.getBarometricPressure())
                .set(LAB1DATA.OUTSIDE_AIR_TEMPERATURE, data.getOutsideAirTemperature())
                .set(LAB1DATA.STACKS_HEIGHT, data.getStacksHeight())
                .set(LAB1DATA.STACKS_DIAMETER, data.getStacksDiameter())
                .set(LAB1DATA.STEAM_PRODUCTION_CAPACITY, data.getSteamProductionCapacity())
                .set(LAB1DATA.OXYGEN_CONCENTRATION, data.getOxygenConcentration())
                .set(LAB1DATA.OXYGEN_CONCENTRATION_POINT, data.getOxygenConcentrationPoint())
                .set(LAB1DATA.FUEL_CONSUMER, data.getFuelConsumer())
                .set(LAB1DATA.EXCESS_PRESSURE, data.getExcessPressure())
                .set(LAB1DATA.GAS_TEMPERATURE, data.getGasTemperature())
                .set(LAB1DATA.STACK_EXIT_TEMPERATURE, data.getStackExitTemperature())
                .set(LAB1DATA.FLUE_GAS_NOX_CONCENTRATION, data.getFlueGasNOxConcentration())
                .set(LAB1DATA.EXCESS_AIR_RATIO, data.getExcessAirRatio())
                .set(LAB1DATA.FLUE_GAS_NOX_CONCENTRATION_NC, data.getFlueGasNOxConcentrationNC())
                .set(LAB1DATA.EXCESS_OF_NORMS, data.getExcessOfNorms())
                .set(LAB1DATA.VALID_BAROMETRIC_PRESSURE, data.getValidBarometricPressure())
                .set(LAB1DATA.VALID_ABSOLUTE_PRESSURE, data.getValidAbsolutePressure())
                .set(LAB1DATA.CORRECTION_FACTOR, data.getCorrectionFactor())
                .set(LAB1DATA.FUEL_CONSUMER_CORRECTION, data.getFuelConsumerCorrection())
                .set(LAB1DATA.FUEL_CONSUMER_NC, data.getFuelConsumerNC())
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
                .where(LAB1DATA.USER_ID.eq(getFindUserIdSelect(data.getUserLogin())).and(LAB1DATA.START_DATE.eq(data.getStartDate())))
                .execute();
    }

    @Override
    public int removeLabsByUser(String userName) {
        return dsl.deleteFrom(LAB1DATA).where(LAB1DATA.USER_ID.eq(getFindUserIdSelect(userName))).execute();
    }

    @Override
    public int removeOldLabs(LocalDateTime lastSaveDate) {
        return dsl.deleteFrom(LAB1DATA).where(LAB1DATA.SAVE_DATE.greaterThan(lastSaveDate)).execute();
    }
}
