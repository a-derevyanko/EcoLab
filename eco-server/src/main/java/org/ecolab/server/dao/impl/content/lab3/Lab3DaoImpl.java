package org.ecolab.server.dao.impl.content.lab3;

import com.google.common.collect.Sets;
import java.time.LocalDateTime;
import org.ecolab.server.common.Profiles;
import org.ecolab.server.dao.api.content.lab3.Lab3Dao;
import org.ecolab.server.dao.impl.DaoUtils;
import org.ecolab.server.dao.impl.content.LabDaoImpl;
import org.ecolab.server.model.content.lab3.City;
import org.ecolab.server.model.content.lab3.FuelType;
import org.ecolab.server.model.content.lab3.Lab3Data;
import org.ecolab.server.model.content.lab3.Lab3Variant;
import org.ecolab.server.model.content.lab3.NumberOfStacks;
import org.ecolab.server.model.content.lab3.NumberOfUnits;
import org.ecolab.server.model.content.lab3.WindDirection;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.impl.DSL;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;


import static org.ecolab.server.db.h2.public_.Tables.LAB3TEAM;
import static org.ecolab.server.db.h2.public_.Tables.USERS;
import static org.ecolab.server.db.h2.public_.Tables.USER_LAB_ALLOWANCE;
import static org.ecolab.server.db.h2.public_.tables.Lab3data.LAB3DATA;
import static org.ecolab.server.db.h2.public_.tables.Lab3variant.LAB3VARIANT;

/**
 * Created by 777Al on 19.04.2017.
 */
@Service
@Profile({Profiles.DB.H2, Profiles.DB.POSTGRES})
public class Lab3DaoImpl extends LabDaoImpl<Lab3Data> implements Lab3Dao {
    private static final RecordMapper<Record, Lab3Data> LAB3DATA_MAPPER = record -> {
        var data = new Lab3Data();
        data.setId(record.get(LAB3DATA.ID));
        data.setStartDate(record.get(LAB3DATA.START_DATE));
        data.setSaveDate(record.get(LAB3DATA.SAVE_DATE));
        data.setCompleted(record.get(LAB3DATA.COMPLETED));
        data.setTppOutput(record.get(LAB3DATA.TPP_OUTPUT));
        data.setNumberOfUnits(record.get(LAB3DATA.NUMBER_OF_UNITS) == null ? null : NumberOfUnits.valueOf(record.get(LAB3DATA.NUMBER_OF_UNITS)));
        data.setCity(record.get(LAB3DATA.CITY) == null ? null : City.valueOf(record.get(LAB3DATA.CITY)));
        data.setSteamProductionCapacity(record.get(LAB3DATA.STEAM_PRODUCTION_CAPACITY));
        data.setNumberOfStacks(record.get(LAB3DATA.NUMBER_OF_STACKS) == null ? null : NumberOfStacks.valueOf(record.get(LAB3DATA.NUMBER_OF_STACKS)));
        data.setStacksHeight(record.get(LAB3DATA.STACKS_HEIGHT));
        data.setStacksDiameter(record.get(LAB3DATA.STACKS_DIAMETER));
        data.setWindDirection(record.get(LAB3DATA.WIND_DIRECTION) == null ? null : WindDirection.valueOf(record.get(LAB3DATA.WIND_DIRECTION)));
        data.setWindSpeed(record.get(LAB3DATA.WIND_SPEED));
        data.setLowHeatValue(record.get(LAB3DATA.LOW_HEAT_VALUE));
        data.setFuelConsumer(record.get(LAB3DATA.FUEL_CONSUMER));
        data.setCarbonInFlyAsh(record.get(LAB3DATA.CARBON_IN_FLY_ASH));
        data.setSulphurContent(record.get(LAB3DATA.SULPHUR_CONTENT));
        data.setAshContent(record.get(LAB3DATA.ASH_CONTENT));
        data.setWaterContent(record.get(LAB3DATA.WATER_CONTENT));
        data.setAshRecyclingFactor(record.get(LAB3DATA.ASH_RECYCLING_FACTOR));
        data.setFlueGasNOxConcentration(record.get(LAB3DATA.FLUE_GAS_NOX_CONCENTRATION));
        data.setStackExitTemperature(record.get(LAB3DATA.STACK_EXIT_TEMPERATURE));
        data.setOutsideAirTemperature(record.get(LAB3DATA.OUTSIDE_AIR_TEMPERATURE));
        data.setExcessAirRatio(record.get(LAB3DATA.EXCESS_AIR_RATIO));
        data.setCombustionProductsVolume(record.get(LAB3DATA.COMBUSTION_PRODUCT_VOLUME));
        data.setWaterVaporVolume(record.get(LAB3DATA.WATER_VAPOR_VOLUME));
        data.setAirVolume(record.get(LAB3DATA.AIR_VOLUME));
        data.setNo2BackgroundConcentration(record.get(LAB3DATA.NO2_BACKGROUND_CONCENTRATION));
        data.setNoBackgroundConcentration(record.get(LAB3DATA.NO_BACKGROUND_CONCENTRATION));
        data.setSo2BackgroundConcentration(record.get(LAB3DATA.SO2_BACKGROUND_CONCENTRATION));
        data.setAshBackgroundConcentration(record.get(LAB3DATA.ASH_BACKGROUND_CONCENTRATION));
        data.setSulphurOxidesFractionAssociatedByFlyAsh(record.get(LAB3DATA.SULPHUR_OXIDES_FRACTION_ASSOCIATED_BY_FLY_ASH));
        data.setSulphurOxidesFractionAssociatedInWetDustCollector(record.get(LAB3DATA.SULPHUR_OXIDES_FRACTION_ASSOCIATED_IN_WET_DUST_COLLECTOR));
        data.setSulphurOxidesFractionAssociatedInDesulphurizationSystem(record.get(LAB3DATA.SULPHUR_OXIDES_FRACTION_ASSOCIATED_IN_DESULPHURIZATION_SYSTEM));
        data.setDesulphurizationSystemRunningTime(record.get(LAB3DATA.DESULPHURIZATION_SYSTEM_RUNNING_TIME));
        data.setBoilerRunningTime(record.get(LAB3DATA.BOILER_RUNNING_TIME));
        data.setAshProportionEntrainedGases(record.get(LAB3DATA.ASH_PROPORTION_ENTRAINED_GASES));
        data.setTemperatureCoefficient(record.get(LAB3DATA.TEMPERATURE_COEFFICIENT));
        data.setTerrainCoefficient(record.get(LAB3DATA.TERRAIN_COEFFICIENT));
        data.setHarmfulSubstancesDepositionCoefficient(record.get(LAB3DATA.HARMFUL_SUBSTANCES_DEPOSITION_COEFFICIENT));
        data.setNo2MAC(record.get(LAB3DATA.NO2_MAC));
        data.setNoMAC(record.get(LAB3DATA.NO_MAC));
        data.setSo2MAC(record.get(LAB3DATA.SO2_MAC));
        data.setAshMAC(record.get(LAB3DATA.ASH_MAC));

        var variant = new Lab3Variant();
        variant.setTppOutput(record.get(LAB3VARIANT.TPP_OUTPUT));
        variant.setNumberOfUnits(record.get(LAB3VARIANT.NUMBER_OF_UNITS) == null ? null : NumberOfUnits.valueOf(record.get(LAB3VARIANT.NUMBER_OF_UNITS)));
        variant.setFuelType(record.get(LAB3VARIANT.FUEL_TYPE) == null ? null : FuelType.valueOf(record.get(LAB3VARIANT.FUEL_TYPE)));
        variant.setCity(record.get(LAB3VARIANT.CITY) == null ? null : City.valueOf(record.get(LAB3VARIANT.CITY)));
        variant.setSteamProductionCapacity(record.get(LAB3VARIANT.STEAM_PRODUCTION_CAPACITY));
        variant.setNumberOfStacks(record.get(LAB3VARIANT.NUMBER_OF_STACKS) == null ? null : NumberOfStacks.valueOf(record.get(LAB3VARIANT.NUMBER_OF_STACKS)));
        variant.setStacksHeight(record.get(LAB3VARIANT.STACKS_HEIGHT));
        variant.setWindDirection(record.get(LAB3VARIANT.WIND_DIRECTION) == null ? null : WindDirection.valueOf(record.get(LAB3VARIANT.WIND_DIRECTION)));
        variant.setWindSpeed(record.get(LAB3VARIANT.WIND_SPEED));
        variant.setLowHeatValue(record.get(LAB3VARIANT.LOW_HEAT_VALUE));
        variant.setFuelConsumer(record.get(LAB3VARIANT.FUEL_CONSUMER));
        variant.setSulphurContent(record.get(LAB3VARIANT.SULPHUR_CONTENT));
        variant.setAshContent(record.get(LAB3VARIANT.ASH_CONTENT));
        variant.setWaterContent(record.get(LAB3VARIANT.WATER_CONTENT));
        variant.setAshRecyclingFactor(record.get(LAB3VARIANT.ASH_RECYCLING_FACTOR));
        variant.setFlueGasNOxConcentration(record.get(LAB3VARIANT.FLUE_GAS_NOX_CONCENTRATION));
        variant.setStackExitTemperature(record.get(LAB3VARIANT.STACK_EXIT_TEMPERATURE));
        variant.setOutsideAirTemperature(record.get(LAB3VARIANT.OUTSIDE_AIR_TEMPERATURE));
        variant.setExcessAirRatio(record.get(LAB3VARIANT.EXCESS_AIR_RATIO));
        variant.setCombustionProductsVolume(record.get(LAB3VARIANT.COMBUSTION_PRODUCT_VOLUME));
        variant.setWaterVaporVolume(record.get(LAB3VARIANT.WATER_VAPOR_VOLUME));
        variant.setAirVolume(record.get(LAB3VARIANT.AIR_VOLUME));
        variant.setNo2BackgroundConcentration(record.get(LAB3VARIANT.NO2_BACKGROUND_CONCENTRATION));
        variant.setNoBackgroundConcentration(record.get(LAB3VARIANT.NO_BACKGROUND_CONCENTRATION));
        variant.setSo2BackgroundConcentration(record.get(LAB3VARIANT.SO2_BACKGROUND_CONCENTRATION));
        variant.setAshBackgroundConcentration(record.get(LAB3VARIANT.ASH_BACKGROUND_CONCENTRATION));

        data.setVariant(variant);

        return data;
    };

    public Lab3DaoImpl(DSLContext dsl) {
        super(dsl);
    }

    @Override
    public Lab3Data getLastLabByUser(String userName, boolean completed) {
        var data = dsl.select().from(LAB3DATA).join(LAB3VARIANT).on(LAB3VARIANT.ID.eq(LAB3DATA.ID)).
                where(LAB3DATA.ID.eq(dsl.select(LAB3TEAM.ID).from(LAB3TEAM).where(LAB3TEAM.USER_ID.eq(DaoUtils.getFindUserIdSelect(dsl, userName)))))
                        .and(LAB3DATA.COMPLETED.eq(completed)).
                orderBy(LAB3DATA.SAVE_DATE.desc()).limit(1).fetchOne(LAB3DATA_MAPPER);

        if (data != null) {
            fillLabUsers(data);
        }

        return data;
    }

    @Override
    public void saveLab(Lab3Data data) {
        var record = dsl.insertInto(LAB3DATA,
                LAB3DATA.COMPLETED,
                LAB3DATA.TPP_OUTPUT,
                LAB3DATA.NUMBER_OF_UNITS,
                LAB3DATA.CITY,
                LAB3DATA.STEAM_PRODUCTION_CAPACITY,
                LAB3DATA.NUMBER_OF_STACKS,
                LAB3DATA.STACKS_HEIGHT,
                LAB3DATA.STACKS_DIAMETER,
                LAB3DATA.WIND_DIRECTION,
                LAB3DATA.WIND_SPEED,
                LAB3DATA.LOW_HEAT_VALUE,
                LAB3DATA.FUEL_CONSUMER,
                LAB3DATA.CARBON_IN_FLY_ASH,
                LAB3DATA.SULPHUR_CONTENT,
                LAB3DATA.ASH_CONTENT,
                LAB3DATA.WATER_CONTENT,
                LAB3DATA.ASH_RECYCLING_FACTOR,
                LAB3DATA.FLUE_GAS_NOX_CONCENTRATION,
                LAB3DATA.STACK_EXIT_TEMPERATURE,
                LAB3DATA.OUTSIDE_AIR_TEMPERATURE,
                LAB3DATA.EXCESS_AIR_RATIO,
                LAB3DATA.COMBUSTION_PRODUCT_VOLUME,
                LAB3DATA.WATER_VAPOR_VOLUME,
                LAB3DATA.AIR_VOLUME,
                LAB3DATA.NO2_BACKGROUND_CONCENTRATION,
                LAB3DATA.NO_BACKGROUND_CONCENTRATION,
                LAB3DATA.SO2_BACKGROUND_CONCENTRATION,
                LAB3DATA.ASH_BACKGROUND_CONCENTRATION,
                LAB3DATA.SULPHUR_OXIDES_FRACTION_ASSOCIATED_BY_FLY_ASH,
                LAB3DATA.SULPHUR_OXIDES_FRACTION_ASSOCIATED_IN_WET_DUST_COLLECTOR,
                LAB3DATA.SULPHUR_OXIDES_FRACTION_ASSOCIATED_IN_DESULPHURIZATION_SYSTEM,
                LAB3DATA.DESULPHURIZATION_SYSTEM_RUNNING_TIME,
                LAB3DATA.BOILER_RUNNING_TIME,
                LAB3DATA.ASH_PROPORTION_ENTRAINED_GASES,
                LAB3DATA.TEMPERATURE_COEFFICIENT,
                LAB3DATA.TERRAIN_COEFFICIENT,
                LAB3DATA.HARMFUL_SUBSTANCES_DEPOSITION_COEFFICIENT,
                LAB3DATA.NO2_MAC,
                LAB3DATA.NO_MAC,
                LAB3DATA.SO2_MAC,
                LAB3DATA.ASH_MAC).
                values(
                        data.isCompleted(),
                        data.getTppOutput(),
                        data.getNumberOfUnits() == null ? null : data.getNumberOfUnits().value(),
                        data.getCity() == null ? null : data.getCity().name(),
                        data.getSteamProductionCapacity(),
                        data.getNumberOfStacks() == null ? null : data.getNumberOfStacks().value(),
                        data.getStacksHeight(),
                        data.getStacksDiameter(),
                        data.getWindDirection() == null ? null : data.getWindDirection().name(),
                        data.getWindSpeed(),
                        data.getLowHeatValue(),
                        data.getFuelConsumer(),
                        data.getCarbonInFlyAsh(),
                        data.getSulphurContent(),
                        data.getAshContent(),
                        data.getWaterContent(),
                        data.getAshRecyclingFactor(),
                        data.getFlueGasNOxConcentration(),
                        data.getStackExitTemperature(),
                        data.getOutsideAirTemperature(),
                        data.getExcessAirRatio(),
                        data.getCombustionProductsVolume(),
                        data.getWaterVaporVolume(),
                        data.getAirVolume(),
                        data.getNo2BackgroundConcentration(),
                        data.getNoBackgroundConcentration(),
                        data.getSo2BackgroundConcentration(),
                        data.getAshBackgroundConcentration(),
                        data.getSulphurOxidesFractionAssociatedByFlyAsh(),
                        data.getSulphurOxidesFractionAssociatedInWetDustCollector(),
                        data.getSulphurOxidesFractionAssociatedInDesulphurizationSystem(),
                        data.getDesulphurizationSystemRunningTime(),
                        data.getBoilerRunningTime(),
                        data.getAshProportionEntrainedGases(),
                        data.getTemperatureCoefficient(),
                        data.getTerrainCoefficient(),
                        data.getHarmfulSubstancesDepositionCoefficient(),
                        data.getNo2MAC(),
                        data.getNoMAC(),
                        data.getSo2MAC(),
                        data.getAshMAC()
                ).returning(LAB3DATA.ID, LAB3DATA.START_DATE, LAB3DATA.SAVE_DATE).fetchOne();
        data.setId(record.getId());
        data.setSaveDate(record.getSaveDate());
        data.setStartDate(record.getStartDate());

        saveLabUsers(data);

        var variant = data.getVariant();
        dsl.insertInto(LAB3VARIANT,
                LAB3VARIANT.ID,
                LAB3VARIANT.TPP_OUTPUT,
                LAB3VARIANT.NUMBER_OF_UNITS,
                LAB3VARIANT.FUEL_TYPE,
                LAB3VARIANT.CITY,
                LAB3VARIANT.STEAM_PRODUCTION_CAPACITY,
                LAB3VARIANT.NUMBER_OF_STACKS,
                LAB3VARIANT.STACKS_HEIGHT,
                LAB3VARIANT.WIND_DIRECTION,
                LAB3VARIANT.WIND_SPEED,
                LAB3VARIANT.LOW_HEAT_VALUE,
                LAB3VARIANT.FUEL_CONSUMER,
                LAB3VARIANT.SULPHUR_CONTENT,
                LAB3VARIANT.ASH_CONTENT,
                LAB3VARIANT.WATER_CONTENT,
                LAB3VARIANT.ASH_RECYCLING_FACTOR,
                LAB3VARIANT.FLUE_GAS_NOX_CONCENTRATION,
                LAB3VARIANT.STACK_EXIT_TEMPERATURE,
                LAB3VARIANT.OUTSIDE_AIR_TEMPERATURE,
                LAB3VARIANT.EXCESS_AIR_RATIO,
                LAB3VARIANT.COMBUSTION_PRODUCT_VOLUME,
                LAB3VARIANT.WATER_VAPOR_VOLUME,
                LAB3VARIANT.AIR_VOLUME,
                LAB3VARIANT.NO2_BACKGROUND_CONCENTRATION,
                LAB3VARIANT.NO_BACKGROUND_CONCENTRATION,
                LAB3VARIANT.SO2_BACKGROUND_CONCENTRATION,
                LAB3VARIANT.ASH_BACKGROUND_CONCENTRATION).
                values(
                        data.getId(),
                        variant.getTppOutput(),
                        variant.getNumberOfUnits() == null ? null : variant.getNumberOfUnits().value(),
                        variant.getFuelType() == null ? null : variant.getFuelType().name(),
                        variant.getCity() == null ? null : variant.getCity().name(),
                        variant.getSteamProductionCapacity(),
                        variant.getNumberOfStacks() == null ? null : variant.getNumberOfStacks().value(),
                        variant.getStacksHeight(),
                        variant.getWindDirection() == null ? null : variant.getWindDirection().name(),
                        variant.getWindSpeed(),
                        variant.getLowHeatValue(),
                        variant.getFuelConsumer(),
                        variant.getSulphurContent(),
                        variant.getAshContent(),
                        variant.getWaterContent(),
                        variant.getAshRecyclingFactor(),
                        variant.getFlueGasNOxConcentration(),
                        variant.getStackExitTemperature(),
                        variant.getOutsideAirTemperature(),
                        variant.getExcessAirRatio(),
                        variant.getCombustionProductsVolume(),
                        variant.getWaterVaporVolume(),
                        variant.getAirVolume(),
                        variant.getNo2BackgroundConcentration(),
                        variant.getNoBackgroundConcentration(),
                        variant.getSo2BackgroundConcentration(),
                        variant.getAshBackgroundConcentration()
                ).execute();
    }

    @Override
    public int updateLab(Lab3Data data) {
        return dsl.update(LAB3DATA)
                .set(LAB3DATA.START_DATE, data.getStartDate())
                .set(LAB3DATA.SAVE_DATE, data.getSaveDate())
                .set(LAB3DATA.COMPLETED, data.isCompleted())
                .set(LAB3DATA.TPP_OUTPUT, data.getTppOutput())
                .set(LAB3DATA.NUMBER_OF_UNITS, data.getNumberOfUnits() == null ? null : data.getNumberOfUnits().value())
                .set(LAB3DATA.CITY, data.getCity() == null ? null : data.getCity().name())
                .set(LAB3DATA.STEAM_PRODUCTION_CAPACITY, data.getSteamProductionCapacity())
                .set(LAB3DATA.NUMBER_OF_STACKS, data.getNumberOfStacks() == null ? null : data.getNumberOfStacks().value())
                .set(LAB3DATA.STACKS_HEIGHT, data.getStacksHeight())
                .set(LAB3DATA.STACKS_DIAMETER, data.getStacksDiameter())
                .set(LAB3DATA.WIND_DIRECTION, data.getWindDirection() == null ? null : data.getWindDirection().name())
                .set(LAB3DATA.WIND_SPEED, data.getWindSpeed())
                .set(LAB3DATA.LOW_HEAT_VALUE, data.getLowHeatValue())
                .set(LAB3DATA.FUEL_CONSUMER, data.getFuelConsumer())
                .set(LAB3DATA.CARBON_IN_FLY_ASH, data.getCarbonInFlyAsh())
                .set(LAB3DATA.SULPHUR_CONTENT, data.getSulphurContent())
                .set(LAB3DATA.ASH_CONTENT, data.getAshContent())
                .set(LAB3DATA.WATER_CONTENT, data.getWaterContent())
                .set(LAB3DATA.ASH_RECYCLING_FACTOR, data.getAshRecyclingFactor())
                .set(LAB3DATA.FLUE_GAS_NOX_CONCENTRATION, data.getFlueGasNOxConcentration())
                .set(LAB3DATA.STACK_EXIT_TEMPERATURE, data.getStackExitTemperature())
                .set(LAB3DATA.OUTSIDE_AIR_TEMPERATURE, data.getOutsideAirTemperature())
                .set(LAB3DATA.EXCESS_AIR_RATIO, data.getExcessAirRatio())
                .set(LAB3DATA.COMBUSTION_PRODUCT_VOLUME, data.getCombustionProductsVolume())
                .set(LAB3DATA.WATER_VAPOR_VOLUME, data.getWaterVaporVolume())
                .set(LAB3DATA.AIR_VOLUME, data.getAirVolume())
                .set(LAB3DATA.NO2_BACKGROUND_CONCENTRATION, data.getNo2BackgroundConcentration())
                .set(LAB3DATA.NO_BACKGROUND_CONCENTRATION, data.getNoBackgroundConcentration())
                .set(LAB3DATA.SO2_BACKGROUND_CONCENTRATION, data.getSo2BackgroundConcentration())
                .set(LAB3DATA.ASH_BACKGROUND_CONCENTRATION, data.getAshBackgroundConcentration())
                .set(LAB3DATA.SULPHUR_OXIDES_FRACTION_ASSOCIATED_BY_FLY_ASH, data.getSulphurOxidesFractionAssociatedByFlyAsh())
                .set(LAB3DATA.SULPHUR_OXIDES_FRACTION_ASSOCIATED_IN_WET_DUST_COLLECTOR, data.getSulphurOxidesFractionAssociatedInWetDustCollector())
                .set(LAB3DATA.SULPHUR_OXIDES_FRACTION_ASSOCIATED_IN_DESULPHURIZATION_SYSTEM, data.getSulphurOxidesFractionAssociatedInDesulphurizationSystem())
                .set(LAB3DATA.DESULPHURIZATION_SYSTEM_RUNNING_TIME, data.getDesulphurizationSystemRunningTime())
                .set(LAB3DATA.BOILER_RUNNING_TIME, data.getBoilerRunningTime())
                .set(LAB3DATA.ASH_PROPORTION_ENTRAINED_GASES, data.getAshProportionEntrainedGases())
                .set(LAB3DATA.TEMPERATURE_COEFFICIENT, data.getTemperatureCoefficient())
                .set(LAB3DATA.TERRAIN_COEFFICIENT, data.getTerrainCoefficient())
                .set(LAB3DATA.HARMFUL_SUBSTANCES_DEPOSITION_COEFFICIENT, data.getHarmfulSubstancesDepositionCoefficient())
                .set(LAB3DATA.NO2_MAC, data.getNo2MAC())
                .set(LAB3DATA.NO_MAC, data.getNoMAC())
                .set(LAB3DATA.SO2_MAC, data.getSo2MAC())
                .set(LAB3DATA.ASH_MAC, data.getAshMAC())
                .where(LAB3DATA.ID.eq(data.getId()))
                .execute();
    }

    @Override
    public int removeLabsByUser(String userName) {
        return dsl.deleteFrom(LAB3DATA).where(LAB3DATA.ID.in(dsl.select(LAB3TEAM.ID).from(LAB3TEAM).where(LAB3TEAM.USER_ID.eq(DaoUtils.getFindUserIdSelect(dsl, userName))))).execute();
    }

    @Override
    public int removeOldLabs(LocalDateTime lastSaveDate) {
        return dsl.deleteFrom(LAB3DATA).where(LAB3DATA.SAVE_DATE.greaterThan(lastSaveDate)).execute();
    }

    @Override
    protected int getLabNumber() {
        return 3;
    }

    @Override
    public void setLabCompleted(Lab3Data data) {
        dsl.update(LAB3DATA).set(LAB3DATA.COMPLETED, true).where(LAB3DATA.ID.eq(data.getId())).execute();
        dsl.deleteFrom(USER_LAB_ALLOWANCE).where(USER_LAB_ALLOWANCE.LAB_NUMBER.eq(getLabNumber())
                .and(USER_LAB_ALLOWANCE.USER_ID.in(dsl.select(LAB3TEAM.USER_ID).from(LAB3TEAM).where(LAB3TEAM.ID.eq(data.getId()))))).execute();
    }

    @Override
    protected void fillLabUsers(Lab3Data data) {
        data.setUsers(Sets.newHashSet(dsl.select(USERS.LOGIN).from(USERS).join(LAB3TEAM).on(USERS.ID.eq(LAB3TEAM.USER_ID)).
                where(LAB3TEAM.ID.eq(data.getId())).fetchInto(String.class)));
    }

    @Override
    protected void saveLabUsers(Lab3Data data) {
        dsl.insertInto(LAB3TEAM, LAB3TEAM.ID, LAB3TEAM.USER_ID).
                select(dsl.select(DSL.val(data.getId()), USERS.ID).from(USERS).where(USERS.LOGIN.in(data.getUsers()))).execute();
    }
}
