package org.ekolab.server.model.content.lab3;

import org.ekolab.server.model.content.Calculated;
import org.ekolab.server.model.content.LabData;

import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Created by 777Al on 06.04.2017.
 */
public class Lab3Data extends LabData<Lab3Variant> {
    /**
     * Мощность ГРЭС
     */
    @Max(3200L)
    @Min(1200L)
    private Integer tppOutput;

    /**
     * Число блоков
     */
    private NumberOfUnits numberOfUnits;

    /**
     * Район расположения ГРЭС (город)
     */
    @Valid
    private City city;

    /**
     * Номинальная паропроизводительность одного котла
     */
    @Max(2450L)
    @Min(580L)
    private Integer steamProductionCapacity;

    /**
     * Число дымовых труб
     */
    private NumberOfStacks numberOfStacks;

    /**
     * Высота дымовой трубы
     */
    @Max(240L)
    @Min(120L)
    private Integer stacksHeight;

    /**
     * Диаметр устья дымовой трубы
     */
    private Double stacksDiameter;

    /**
     * Расчетное направление ветра
     */
    private WindDirection windDirection;

    /**
     * Расчетная скорость ветра
     */
    private Double windSpeed;

    /**
     * Низшая теплота сгорания топлива
     */
    /*@Max(40)
    @Min(28)*/
    @Digits(integer = 2, fraction = 2)
    private Double lowHeatValue;

    /**
     * Расход топлива на 1 блок
     */
    private Integer fuelConsumer;

    /**
     * Потери тепла от механической неполноты сгорания топлива
     */
    /*@Max(1.5)
    @Min(0)*/
    @Digits(integer = 1, fraction = 1)
    private Double carbonInFlyAsh;

    /**
     * Содержание серы в топливе
     */
    private Double sulphurContent;

    /**
     * Зольность топлива
     */
    /*@Max(99)
    @Min(0)*/
    @Digits(integer = 2, fraction = 1)
    private Double ashContent;

    /**
     * Влажность топлива
     */
    private Double waterContent;

    /**
     * Степень улавливания золы
     */
    private Double ashRecyclingFactor;

    /**
     * Концентрация оксидов азота в сухих газах
     */
    @Max(400L)
    @Min(200L)
    private Integer flueGasNOxConcentration;

    /**
     * Температура газов на выходе из дымовой трубы
     */
    @Max(200L)
    @Min(100L)
    private Integer stackExitTemperature;

    /**
     * Температура наружного воздуха
     */
    @Max(100L)
    @Min(-100L)
    private Integer outsideAirTemperature;

    /**
     * Коэффициент избытка воздуха в уходящих газах
     */
    // Всегда == 1,4
    private Double excessAirRatio;

    /**
     * Теоретический объем продуктов сгорания
     */
    @Digits(integer = 100000, fraction = 2)
    private Double combustionProductsVolume;

    /**
     * Теоретический объем паров воды
     */
    @Digits(integer = 100000, fraction = 2)
    private Double waterVaporVolume;

    /**
     * Теоретический объем воздуха
     */
    @Digits(integer = 100000, fraction = 2)
    private Double airVolume;

    /**
     * Фоновая концентрация NO2
     */
    @Digits(integer = 100000, fraction = 3)
    private Double no2BackgroundConcentration;

    /**
     * Фоновая концентрация NO
     */
    @Digits(integer = 100000, fraction = 3)
    private Double noBackgroundConcentration;

    /**
     * Фоновая концентрация SO2
     */
    @Digits(integer = 100000, fraction = 3)
    private Double so2BackgroundConcentration;

    /**
     * Фоновая концентрация золы
     */
    @Digits(integer = 100000, fraction = 3)
    private Double ashBackgroundConcentration;

    /**
     *  Доля оксидов серы, связываемых летучей золой в котле
     */
    private Double sulphurOxidesFractionAssociatedByFlyAsh;

    /**
     * Доля оксидов серы, улавливаемых в мокром золоуловителе попутно с твердыми частицами
     */
    private Double sulphurOxidesFractionAssociatedInWetDustCollector;

    /**
     * Доля оксидов серы, улавливаемых в сероулавливающей установке
     */
    private Double sulphurOxidesFractionAssociatedInDesulphurizationSystem;

    /**
     * Длительность работы сероулавливающей установки
     */
    private Double desulphurizationSystemRunningTime;

    /**
     * Длительность работы котла
     */
    private Double boilerRunningTime;

    /**
     * Доля золы, уносимой газами из котла
     */
    private Double ashProportionEntrainedGases;

    /**
     * Средняя скорость газов на выходе из дымовой трубы
     */
    @Calculated
    private Double stackAverageGasesSpeed;

    /**
     * Суммарное количество NOx поступающего в атмосферу с дымовыми газами
     */
    @Calculated
    private Double noxMassiveInjection;

    /**
     * Суммарное количество NO2, поступающего в атмосферу с дымовыми газами
     */
    @Calculated
    private Double no2MassiveInjection;

    /**
     * Суммарное количество NO, поступающего в атмосферу с дымовыми газами
     */
    @Calculated
    private Double noMassiveInjection;

    /**
     * Суммарное количество SO2, поступающего в атмосферу с дымовыми газами
     */
    @Calculated
    private Double so2MassiveInjection;

    /**
     * Суммарное количество золы, поступающей в атмосферу с дымовыми газами
     */
    @Calculated
    private Double ashMassiveInjection;

    /**
     * Коэффициент, характеризующий температурную стратификацию атмосферы
     */
    //todo ставить ограничение относительно города
    @Max(250L)
    @Min(140L)
    private Double temperatureCoefficient;

    /**
     * Коэффициент, учитывающий влияние рельефа местности
     */
    private Double terrainCoefficient;

    /**
     * Безразмерный коэффициент, учитывающий скорость оседания вредных веществ в атмосферном воздухе
     */
    private Double harmfulSubstancesDepositionCoefficient;

    /**
     * ПДК NO2
     */
    @DecimalMax("0.085")
    @DecimalMin("0.085")
    private Double no2MAC;

    /**
     * ПДК NO
     */
    @DecimalMax("0.4")
    @DecimalMin("0.4")
    private Double noMAC;

    /**
     * ПДК SO2
     */
    @DecimalMax("0.5")
    @DecimalMin("0.5")
    private Double so2MAC;

    /**
     * ПДК золы
     */
    @DecimalMax("0.05")
    @DecimalMin("0.05")
    private Double ashMAC;

    /**
     * Опасная скорость ветра
     */
    @Calculated
    private Double breakdownWindSpeed;

    /**
     * Максимальное значение приземной концентрации NO2 при опасной скорости ветра
     */
    @Calculated
    private Double bwdNo2GroundLevelConcentration;

    /**
     * Максимальное значение приземной концентрации NO при опасной скорости ветра
     */
    @Calculated
    private Double bwdNoGroundLevelConcentration;

    /**
     * Максимальное значение приземной концентрации SO2 при опасной скорости ветра
     */
    @Calculated
    private Double bwdSo2GroundLevelConcentration;

    /**
     * Максимальное значение приземной концентрации золы при опасной скорости ветра
     */
    @Calculated
    private Double bwdAshGroundLevelConcentration;

    /**
     * Безразмерная концентрация веществ, обладающих суммацией вредного действия (NO2 и SO2)
     */
    @Calculated
    private Double no2AndSo2SummationGroup;

    /**
     * Безразмерная концентрация веществ, обладающих суммацией вредного действия (NO и SO2)
     */
    @Calculated
    private Double noAndSo2SummationGroup;

    /**
     * Расстояние от источника выбросов, на котором приземные концентрации загрязняющих веществ достигают максимального значения при опасной скорости ветра
     */
    @Calculated
    private Double bwdMaxGroundLevelConcentrationDistance;

    /**
     * Максимальное значение приземной концентрации NO2 при расчетной скорости ветра
     */
    @Calculated
    private Double windSpeedMaxNo2GroundLevelConcentration;

    /**
     * Максимальное значение приземной концентрации NO при расчетной скорости ветра
     */
    @Calculated
    private Double windSpeedMaxNoGroundLevelConcentration;

    /**
     * Максимальное значение приземной концентрации SO2 при расчетной скорости ветра
     */
    @Calculated
    private Double windSpeedMaxSo2GroundLevelConcentration;

    /**
     * Максимальное значение приземной концентрации золы при расчетной скорости ветра
     */
    @Calculated
    private Double windSpeedMaxAshGroundLevelConcentration;

    /**
     * Расстояние от источника выброса, на котором при скорости ветра u приземная концентрация вредных веществ достигает максимального значения
     */
    @Calculated
    private Double windSpeedMaxGroundLevelConcentrationDistance;

    public Integer getTppOutput() {
        return tppOutput;
    }

    public void setTppOutput(Integer tppOutput) {
        this.tppOutput = tppOutput;
    }

    public NumberOfUnits getNumberOfUnits() {
        return numberOfUnits;
    }

    public void setNumberOfUnits(NumberOfUnits numberOfUnits) {
        this.numberOfUnits = numberOfUnits;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Integer getSteamProductionCapacity() {
        return steamProductionCapacity;
    }

    public void setSteamProductionCapacity(Integer steamProductionCapacity) {
        this.steamProductionCapacity = steamProductionCapacity;
    }

    public NumberOfStacks getNumberOfStacks() {
        return numberOfStacks;
    }

    public void setNumberOfStacks(NumberOfStacks numberOfStacks) {
        this.numberOfStacks = numberOfStacks;
    }

    public Integer getStacksHeight() {
        return stacksHeight;
    }

    public void setStacksHeight(Integer stacksHeight) {
        this.stacksHeight = stacksHeight;
    }

    public Double getStacksDiameter() {
        return stacksDiameter;
    }

    public void setStacksDiameter(Double stacksDiameter) {
        this.stacksDiameter = stacksDiameter;
    }

    public WindDirection getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(WindDirection windDirection) {
        this.windDirection = windDirection;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Double getLowHeatValue() {
        return lowHeatValue;
    }

    public void setLowHeatValue(Double lowHeatValue) {
        this.lowHeatValue = lowHeatValue;
    }

    public Integer getFuelConsumer() {
        return fuelConsumer;
    }

    public void setFuelConsumer(Integer fuelConsumer) {
        this.fuelConsumer = fuelConsumer;
    }

    public Double getCarbonInFlyAsh() {
        return carbonInFlyAsh;
    }

    public void setCarbonInFlyAsh(Double carbonInFlyAsh) {
        this.carbonInFlyAsh = carbonInFlyAsh;
    }

    public Double getSulphurContent() {
        return sulphurContent;
    }

    public void setSulphurContent(Double sulphurContent) {
        this.sulphurContent = sulphurContent;
    }

    public Double getAshContent() {
        return ashContent;
    }

    public void setAshContent(Double ashContent) {
        this.ashContent = ashContent;
    }

    public Double getWaterContent() {
        return waterContent;
    }

    public void setWaterContent(Double waterContent) {
        this.waterContent = waterContent;
    }

    public Double getAshRecyclingFactor() {
        return ashRecyclingFactor;
    }

    public void setAshRecyclingFactor(Double ashRecyclingFactor) {
        this.ashRecyclingFactor = ashRecyclingFactor;
    }

    public Integer getFlueGasNOxConcentration() {
        return flueGasNOxConcentration;
    }

    public void setFlueGasNOxConcentration(Integer flueGasNOxConcentration) {
        this.flueGasNOxConcentration = flueGasNOxConcentration;
    }

    public Integer getStackExitTemperature() {
        return stackExitTemperature;
    }

    public void setStackExitTemperature(Integer stackExitTemperature) {
        this.stackExitTemperature = stackExitTemperature;
    }

    public Integer getOutsideAirTemperature() {
        return outsideAirTemperature;
    }

    public void setOutsideAirTemperature(Integer outsideAirTemperature) {
        this.outsideAirTemperature = outsideAirTemperature;
    }

    public Double getExcessAirRatio() {
        return excessAirRatio;
    }

    public void setExcessAirRatio(Double excessAirRatio) {
        this.excessAirRatio = excessAirRatio;
    }

    public Double getCombustionProductsVolume() {
        return combustionProductsVolume;
    }

    public void setCombustionProductsVolume(Double combustionProductsVolume) {
        this.combustionProductsVolume = combustionProductsVolume;
    }

    public Double getWaterVaporVolume() {
        return waterVaporVolume;
    }

    public void setWaterVaporVolume(Double waterVaporVolume) {
        this.waterVaporVolume = waterVaporVolume;
    }

    public Double getAirVolume() {
        return airVolume;
    }

    public void setAirVolume(Double airVolume) {
        this.airVolume = airVolume;
    }

    public Double getNo2BackgroundConcentration() {
        return no2BackgroundConcentration;
    }

    public void setNo2BackgroundConcentration(Double no2BackgroundConcentration) {
        this.no2BackgroundConcentration = no2BackgroundConcentration;
    }

    public Double getNoBackgroundConcentration() {
        return noBackgroundConcentration;
    }

    public void setNoBackgroundConcentration(Double noBackgroundConcentration) {
        this.noBackgroundConcentration = noBackgroundConcentration;
    }

    public Double getSo2BackgroundConcentration() {
        return so2BackgroundConcentration;
    }

    public void setSo2BackgroundConcentration(Double so2BackgroundConcentration) {
        this.so2BackgroundConcentration = so2BackgroundConcentration;
    }

    public Double getAshBackgroundConcentration() {
        return ashBackgroundConcentration;
    }

    public void setAshBackgroundConcentration(Double ashBackgroundConcentration) {
        this.ashBackgroundConcentration = ashBackgroundConcentration;
    }

    public Double getSulphurOxidesFractionAssociatedByFlyAsh() {
        return sulphurOxidesFractionAssociatedByFlyAsh;
    }

    public void setSulphurOxidesFractionAssociatedByFlyAsh(Double sulphurOxidesFractionAssociatedByFlyAsh) {
        this.sulphurOxidesFractionAssociatedByFlyAsh = sulphurOxidesFractionAssociatedByFlyAsh;
    }

    public Double getSulphurOxidesFractionAssociatedInWetDustCollector() {
        return sulphurOxidesFractionAssociatedInWetDustCollector;
    }

    public void setSulphurOxidesFractionAssociatedInWetDustCollector(Double sulphurOxidesFractionAssociatedInWetDustCollector) {
        this.sulphurOxidesFractionAssociatedInWetDustCollector = sulphurOxidesFractionAssociatedInWetDustCollector;
    }

    public Double getSulphurOxidesFractionAssociatedInDesulphurizationSystem() {
        return sulphurOxidesFractionAssociatedInDesulphurizationSystem;
    }

    public void setSulphurOxidesFractionAssociatedInDesulphurizationSystem(Double sulphurOxidesFractionAssociatedInDesulphurizationSystem) {
        this.sulphurOxidesFractionAssociatedInDesulphurizationSystem = sulphurOxidesFractionAssociatedInDesulphurizationSystem;
    }

    public Double getDesulphurizationSystemRunningTime() {
        return desulphurizationSystemRunningTime;
    }

    public void setDesulphurizationSystemRunningTime(Double desulphurizationSystemRunningTime) {
        this.desulphurizationSystemRunningTime = desulphurizationSystemRunningTime;
    }

    public Double getBoilerRunningTime() {
        return boilerRunningTime;
    }

    public void setBoilerRunningTime(Double boilerRunningTime) {
        this.boilerRunningTime = boilerRunningTime;
    }

    public Double getAshProportionEntrainedGases() {
        return ashProportionEntrainedGases;
    }

    public void setAshProportionEntrainedGases(Double ashProportionEntrainedGases) {
        this.ashProportionEntrainedGases = ashProportionEntrainedGases;
    }

    public Double getStackAverageGasesSpeed() {
        return stackAverageGasesSpeed;
    }

    public void setStackAverageGasesSpeed(Double stackAverageGasesSpeed) {
        this.stackAverageGasesSpeed = stackAverageGasesSpeed;
    }

    public Double getNoxMassiveInjection() {
        return noxMassiveInjection;
    }

    public void setNoxMassiveInjection(Double noxMassiveInjection) {
        this.noxMassiveInjection = noxMassiveInjection;
    }

    public Double getNo2MassiveInjection() {
        return no2MassiveInjection;
    }

    public void setNo2MassiveInjection(Double no2MassiveInjection) {
        this.no2MassiveInjection = no2MassiveInjection;
    }

    public Double getNoMassiveInjection() {
        return noMassiveInjection;
    }

    public void setNoMassiveInjection(Double noMassiveInjection) {
        this.noMassiveInjection = noMassiveInjection;
    }

    public Double getSo2MassiveInjection() {
        return so2MassiveInjection;
    }

    public void setSo2MassiveInjection(Double so2MassiveInjection) {
        this.so2MassiveInjection = so2MassiveInjection;
    }

    public Double getAshMassiveInjection() {
        return ashMassiveInjection;
    }

    public void setAshMassiveInjection(Double ashMassiveInjection) {
        this.ashMassiveInjection = ashMassiveInjection;
    }

    public Double getTemperatureCoefficient() {
        return temperatureCoefficient;
    }

    public void setTemperatureCoefficient(Double temperatureCoefficient) {
        this.temperatureCoefficient = temperatureCoefficient;
    }

    public Double getTerrainCoefficient() {
        return terrainCoefficient;
    }

    public void setTerrainCoefficient(Double terrainCoefficient) {
        this.terrainCoefficient = terrainCoefficient;
    }

    public Double getHarmfulSubstancesDepositionCoefficient() {
        return harmfulSubstancesDepositionCoefficient;
    }

    public void setHarmfulSubstancesDepositionCoefficient(Double harmfulSubstancesDepositionCoefficient) {
        this.harmfulSubstancesDepositionCoefficient = harmfulSubstancesDepositionCoefficient;
    }

    public Double getNo2MAC() {
        return no2MAC;
    }

    public void setNo2MAC(Double no2MAC) {
        this.no2MAC = no2MAC;
    }

    public Double getNoMAC() {
        return noMAC;
    }

    public void setNoMAC(Double noMAC) {
        this.noMAC = noMAC;
    }

    public Double getSo2MAC() {
        return so2MAC;
    }

    public void setSo2MAC(Double so2MAC) {
        this.so2MAC = so2MAC;
    }

    public Double getAshMAC() {
        return ashMAC;
    }

    public void setAshMAC(Double ashMAC) {
        this.ashMAC = ashMAC;
    }

    public Double getBreakdownWindSpeed() {
        return breakdownWindSpeed;
    }

    public void setBreakdownWindSpeed(Double breakdownWindSpeed) {
        this.breakdownWindSpeed = breakdownWindSpeed;
    }

    public Double getBwdNo2GroundLevelConcentration() {
        return bwdNo2GroundLevelConcentration;
    }

    public void setBwdNo2GroundLevelConcentration(Double bwdNo2GroundLevelConcentration) {
        this.bwdNo2GroundLevelConcentration = bwdNo2GroundLevelConcentration;
    }

    public Double getBwdNoGroundLevelConcentration() {
        return bwdNoGroundLevelConcentration;
    }

    public void setBwdNoGroundLevelConcentration(Double bwdNoGroundLevelConcentration) {
        this.bwdNoGroundLevelConcentration = bwdNoGroundLevelConcentration;
    }

    public Double getBwdSo2GroundLevelConcentration() {
        return bwdSo2GroundLevelConcentration;
    }

    public void setBwdSo2GroundLevelConcentration(Double bwdSo2GroundLevelConcentration) {
        this.bwdSo2GroundLevelConcentration = bwdSo2GroundLevelConcentration;
    }

    public Double getBwdAshGroundLevelConcentration() {
        return bwdAshGroundLevelConcentration;
    }

    public void setBwdAshGroundLevelConcentration(Double bwdAshGroundLevelConcentration) {
        this.bwdAshGroundLevelConcentration = bwdAshGroundLevelConcentration;
    }

    public Double getNo2AndSo2SummationGroup() {
        return no2AndSo2SummationGroup;
    }

    public void setNo2AndSo2SummationGroup(Double no2AndSo2SummationGroup) {
        this.no2AndSo2SummationGroup = no2AndSo2SummationGroup;
    }

    public Double getNoAndSo2SummationGroup() {
        return noAndSo2SummationGroup;
    }

    public void setNoAndSo2SummationGroup(Double noAndSo2SummationGroup) {
        this.noAndSo2SummationGroup = noAndSo2SummationGroup;
    }

    public Double getBwdMaxGroundLevelConcentrationDistance() {
        return bwdMaxGroundLevelConcentrationDistance;
    }

    public void setBwdMaxGroundLevelConcentrationDistance(Double bwdMaxGroundLevelConcentrationDistance) {
        this.bwdMaxGroundLevelConcentrationDistance = bwdMaxGroundLevelConcentrationDistance;
    }

    public Double getWindSpeedMaxNo2GroundLevelConcentration() {
        return windSpeedMaxNo2GroundLevelConcentration;
    }

    public void setWindSpeedMaxNo2GroundLevelConcentration(Double windSpeedMaxNo2GroundLevelConcentration) {
        this.windSpeedMaxNo2GroundLevelConcentration = windSpeedMaxNo2GroundLevelConcentration;
    }

    public Double getWindSpeedMaxNoGroundLevelConcentration() {
        return windSpeedMaxNoGroundLevelConcentration;
    }

    public void setWindSpeedMaxNoGroundLevelConcentration(Double windSpeedMaxNoGroundLevelConcentration) {
        this.windSpeedMaxNoGroundLevelConcentration = windSpeedMaxNoGroundLevelConcentration;
    }

    public Double getWindSpeedMaxSo2GroundLevelConcentration() {
        return windSpeedMaxSo2GroundLevelConcentration;
    }

    public void setWindSpeedMaxSo2GroundLevelConcentration(Double windSpeedMaxSo2GroundLevelConcentration) {
        this.windSpeedMaxSo2GroundLevelConcentration = windSpeedMaxSo2GroundLevelConcentration;
    }

    public Double getWindSpeedMaxAshGroundLevelConcentration() {
        return windSpeedMaxAshGroundLevelConcentration;
    }

    public void setWindSpeedMaxAshGroundLevelConcentration(Double windSpeedMaxAshGroundLevelConcentration) {
        this.windSpeedMaxAshGroundLevelConcentration = windSpeedMaxAshGroundLevelConcentration;
    }

    public Double getWindSpeedMaxGroundLevelConcentrationDistance() {
        return windSpeedMaxGroundLevelConcentrationDistance;
    }

    public void setWindSpeedMaxGroundLevelConcentrationDistance(Double windSpeedMaxGroundLevelConcentrationDistance) {
        this.windSpeedMaxGroundLevelConcentrationDistance = windSpeedMaxGroundLevelConcentrationDistance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Lab3Data lab3Data = (Lab3Data) o;

        if (tppOutput != null ? !tppOutput.equals(lab3Data.tppOutput) : lab3Data.tppOutput != null) return false;
        if (numberOfUnits != lab3Data.numberOfUnits) return false;
        if (city != lab3Data.city) return false;
        if (steamProductionCapacity != null ? !steamProductionCapacity.equals(lab3Data.steamProductionCapacity) : lab3Data.steamProductionCapacity != null)
            return false;
        if (numberOfStacks != lab3Data.numberOfStacks) return false;
        if (stacksHeight != null ? !stacksHeight.equals(lab3Data.stacksHeight) : lab3Data.stacksHeight != null)
            return false;
        if (stacksDiameter != null ? !stacksDiameter.equals(lab3Data.stacksDiameter) : lab3Data.stacksDiameter != null)
            return false;
        if (windDirection != lab3Data.windDirection) return false;
        if (windSpeed != null ? !windSpeed.equals(lab3Data.windSpeed) : lab3Data.windSpeed != null) return false;
        if (lowHeatValue != null ? !lowHeatValue.equals(lab3Data.lowHeatValue) : lab3Data.lowHeatValue != null)
            return false;
        if (fuelConsumer != null ? !fuelConsumer.equals(lab3Data.fuelConsumer) : lab3Data.fuelConsumer != null)
            return false;
        if (carbonInFlyAsh != null ? !carbonInFlyAsh.equals(lab3Data.carbonInFlyAsh) : lab3Data.carbonInFlyAsh != null)
            return false;
        if (sulphurContent != null ? !sulphurContent.equals(lab3Data.sulphurContent) : lab3Data.sulphurContent != null)
            return false;
        if (ashContent != null ? !ashContent.equals(lab3Data.ashContent) : lab3Data.ashContent != null) return false;
        if (waterContent != null ? !waterContent.equals(lab3Data.waterContent) : lab3Data.waterContent != null)
            return false;
        if (ashRecyclingFactor != null ? !ashRecyclingFactor.equals(lab3Data.ashRecyclingFactor) : lab3Data.ashRecyclingFactor != null)
            return false;
        if (flueGasNOxConcentration != null ? !flueGasNOxConcentration.equals(lab3Data.flueGasNOxConcentration) : lab3Data.flueGasNOxConcentration != null)
            return false;
        if (stackExitTemperature != null ? !stackExitTemperature.equals(lab3Data.stackExitTemperature) : lab3Data.stackExitTemperature != null)
            return false;
        if (outsideAirTemperature != null ? !outsideAirTemperature.equals(lab3Data.outsideAirTemperature) : lab3Data.outsideAirTemperature != null)
            return false;
        if (excessAirRatio != null ? !excessAirRatio.equals(lab3Data.excessAirRatio) : lab3Data.excessAirRatio != null)
            return false;
        if (combustionProductsVolume != null ? !combustionProductsVolume.equals(lab3Data.combustionProductsVolume) : lab3Data.combustionProductsVolume != null)
            return false;
        if (waterVaporVolume != null ? !waterVaporVolume.equals(lab3Data.waterVaporVolume) : lab3Data.waterVaporVolume != null)
            return false;
        if (airVolume != null ? !airVolume.equals(lab3Data.airVolume) : lab3Data.airVolume != null) return false;
        if (no2BackgroundConcentration != null ? !no2BackgroundConcentration.equals(lab3Data.no2BackgroundConcentration) : lab3Data.no2BackgroundConcentration != null)
            return false;
        if (noBackgroundConcentration != null ? !noBackgroundConcentration.equals(lab3Data.noBackgroundConcentration) : lab3Data.noBackgroundConcentration != null)
            return false;
        if (so2BackgroundConcentration != null ? !so2BackgroundConcentration.equals(lab3Data.so2BackgroundConcentration) : lab3Data.so2BackgroundConcentration != null)
            return false;
        if (ashBackgroundConcentration != null ? !ashBackgroundConcentration.equals(lab3Data.ashBackgroundConcentration) : lab3Data.ashBackgroundConcentration != null)
            return false;
        if (sulphurOxidesFractionAssociatedByFlyAsh != null ? !sulphurOxidesFractionAssociatedByFlyAsh.equals(lab3Data.sulphurOxidesFractionAssociatedByFlyAsh) : lab3Data.sulphurOxidesFractionAssociatedByFlyAsh != null)
            return false;
        if (sulphurOxidesFractionAssociatedInWetDustCollector != null ? !sulphurOxidesFractionAssociatedInWetDustCollector.equals(lab3Data.sulphurOxidesFractionAssociatedInWetDustCollector) : lab3Data.sulphurOxidesFractionAssociatedInWetDustCollector != null)
            return false;
        if (sulphurOxidesFractionAssociatedInDesulphurizationSystem != null ? !sulphurOxidesFractionAssociatedInDesulphurizationSystem.equals(lab3Data.sulphurOxidesFractionAssociatedInDesulphurizationSystem) : lab3Data.sulphurOxidesFractionAssociatedInDesulphurizationSystem != null)
            return false;
        if (desulphurizationSystemRunningTime != null ? !desulphurizationSystemRunningTime.equals(lab3Data.desulphurizationSystemRunningTime) : lab3Data.desulphurizationSystemRunningTime != null)
            return false;
        if (boilerRunningTime != null ? !boilerRunningTime.equals(lab3Data.boilerRunningTime) : lab3Data.boilerRunningTime != null)
            return false;
        if (ashProportionEntrainedGases != null ? !ashProportionEntrainedGases.equals(lab3Data.ashProportionEntrainedGases) : lab3Data.ashProportionEntrainedGases != null)
            return false;
        if (stackAverageGasesSpeed != null ? !stackAverageGasesSpeed.equals(lab3Data.stackAverageGasesSpeed) : lab3Data.stackAverageGasesSpeed != null)
            return false;
        if (noxMassiveInjection != null ? !noxMassiveInjection.equals(lab3Data.noxMassiveInjection) : lab3Data.noxMassiveInjection != null)
            return false;
        if (no2MassiveInjection != null ? !no2MassiveInjection.equals(lab3Data.no2MassiveInjection) : lab3Data.no2MassiveInjection != null)
            return false;
        if (noMassiveInjection != null ? !noMassiveInjection.equals(lab3Data.noMassiveInjection) : lab3Data.noMassiveInjection != null)
            return false;
        if (so2MassiveInjection != null ? !so2MassiveInjection.equals(lab3Data.so2MassiveInjection) : lab3Data.so2MassiveInjection != null)
            return false;
        if (ashMassiveInjection != null ? !ashMassiveInjection.equals(lab3Data.ashMassiveInjection) : lab3Data.ashMassiveInjection != null)
            return false;
        if (temperatureCoefficient != null ? !temperatureCoefficient.equals(lab3Data.temperatureCoefficient) : lab3Data.temperatureCoefficient != null)
            return false;
        if (terrainCoefficient != null ? !terrainCoefficient.equals(lab3Data.terrainCoefficient) : lab3Data.terrainCoefficient != null)
            return false;
        if (harmfulSubstancesDepositionCoefficient != null ? !harmfulSubstancesDepositionCoefficient.equals(lab3Data.harmfulSubstancesDepositionCoefficient) : lab3Data.harmfulSubstancesDepositionCoefficient != null)
            return false;
        if (no2MAC != null ? !no2MAC.equals(lab3Data.no2MAC) : lab3Data.no2MAC != null) return false;
        if (noMAC != null ? !noMAC.equals(lab3Data.noMAC) : lab3Data.noMAC != null) return false;
        if (so2MAC != null ? !so2MAC.equals(lab3Data.so2MAC) : lab3Data.so2MAC != null) return false;
        if (ashMAC != null ? !ashMAC.equals(lab3Data.ashMAC) : lab3Data.ashMAC != null) return false;
        if (breakdownWindSpeed != null ? !breakdownWindSpeed.equals(lab3Data.breakdownWindSpeed) : lab3Data.breakdownWindSpeed != null)
            return false;
        if (bwdNo2GroundLevelConcentration != null ? !bwdNo2GroundLevelConcentration.equals(lab3Data.bwdNo2GroundLevelConcentration) : lab3Data.bwdNo2GroundLevelConcentration != null)
            return false;
        if (bwdNoGroundLevelConcentration != null ? !bwdNoGroundLevelConcentration.equals(lab3Data.bwdNoGroundLevelConcentration) : lab3Data.bwdNoGroundLevelConcentration != null)
            return false;
        if (bwdSo2GroundLevelConcentration != null ? !bwdSo2GroundLevelConcentration.equals(lab3Data.bwdSo2GroundLevelConcentration) : lab3Data.bwdSo2GroundLevelConcentration != null)
            return false;
        if (bwdAshGroundLevelConcentration != null ? !bwdAshGroundLevelConcentration.equals(lab3Data.bwdAshGroundLevelConcentration) : lab3Data.bwdAshGroundLevelConcentration != null)
            return false;
        if (no2AndSo2SummationGroup != null ? !no2AndSo2SummationGroup.equals(lab3Data.no2AndSo2SummationGroup) : lab3Data.no2AndSo2SummationGroup != null)
            return false;
        if (noAndSo2SummationGroup != null ? !noAndSo2SummationGroup.equals(lab3Data.noAndSo2SummationGroup) : lab3Data.noAndSo2SummationGroup != null)
            return false;
        if (bwdMaxGroundLevelConcentrationDistance != null ? !bwdMaxGroundLevelConcentrationDistance.equals(lab3Data.bwdMaxGroundLevelConcentrationDistance) : lab3Data.bwdMaxGroundLevelConcentrationDistance != null)
            return false;
        if (windSpeedMaxNo2GroundLevelConcentration != null ? !windSpeedMaxNo2GroundLevelConcentration.equals(lab3Data.windSpeedMaxNo2GroundLevelConcentration) : lab3Data.windSpeedMaxNo2GroundLevelConcentration != null)
            return false;
        if (windSpeedMaxNoGroundLevelConcentration != null ? !windSpeedMaxNoGroundLevelConcentration.equals(lab3Data.windSpeedMaxNoGroundLevelConcentration) : lab3Data.windSpeedMaxNoGroundLevelConcentration != null)
            return false;
        if (windSpeedMaxSo2GroundLevelConcentration != null ? !windSpeedMaxSo2GroundLevelConcentration.equals(lab3Data.windSpeedMaxSo2GroundLevelConcentration) : lab3Data.windSpeedMaxSo2GroundLevelConcentration != null)
            return false;
        if (windSpeedMaxAshGroundLevelConcentration != null ? !windSpeedMaxAshGroundLevelConcentration.equals(lab3Data.windSpeedMaxAshGroundLevelConcentration) : lab3Data.windSpeedMaxAshGroundLevelConcentration != null)
            return false;
        return windSpeedMaxGroundLevelConcentrationDistance != null ? windSpeedMaxGroundLevelConcentrationDistance.equals(lab3Data.windSpeedMaxGroundLevelConcentrationDistance) : lab3Data.windSpeedMaxGroundLevelConcentrationDistance == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (tppOutput != null ? tppOutput.hashCode() : 0);
        result = 31 * result + (numberOfUnits != null ? numberOfUnits.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (steamProductionCapacity != null ? steamProductionCapacity.hashCode() : 0);
        result = 31 * result + (numberOfStacks != null ? numberOfStacks.hashCode() : 0);
        result = 31 * result + (stacksHeight != null ? stacksHeight.hashCode() : 0);
        result = 31 * result + (stacksDiameter != null ? stacksDiameter.hashCode() : 0);
        result = 31 * result + (windDirection != null ? windDirection.hashCode() : 0);
        result = 31 * result + (windSpeed != null ? windSpeed.hashCode() : 0);
        result = 31 * result + (lowHeatValue != null ? lowHeatValue.hashCode() : 0);
        result = 31 * result + (fuelConsumer != null ? fuelConsumer.hashCode() : 0);
        result = 31 * result + (carbonInFlyAsh != null ? carbonInFlyAsh.hashCode() : 0);
        result = 31 * result + (sulphurContent != null ? sulphurContent.hashCode() : 0);
        result = 31 * result + (ashContent != null ? ashContent.hashCode() : 0);
        result = 31 * result + (waterContent != null ? waterContent.hashCode() : 0);
        result = 31 * result + (ashRecyclingFactor != null ? ashRecyclingFactor.hashCode() : 0);
        result = 31 * result + (flueGasNOxConcentration != null ? flueGasNOxConcentration.hashCode() : 0);
        result = 31 * result + (stackExitTemperature != null ? stackExitTemperature.hashCode() : 0);
        result = 31 * result + (outsideAirTemperature != null ? outsideAirTemperature.hashCode() : 0);
        result = 31 * result + (excessAirRatio != null ? excessAirRatio.hashCode() : 0);
        result = 31 * result + (combustionProductsVolume != null ? combustionProductsVolume.hashCode() : 0);
        result = 31 * result + (waterVaporVolume != null ? waterVaporVolume.hashCode() : 0);
        result = 31 * result + (airVolume != null ? airVolume.hashCode() : 0);
        result = 31 * result + (no2BackgroundConcentration != null ? no2BackgroundConcentration.hashCode() : 0);
        result = 31 * result + (noBackgroundConcentration != null ? noBackgroundConcentration.hashCode() : 0);
        result = 31 * result + (so2BackgroundConcentration != null ? so2BackgroundConcentration.hashCode() : 0);
        result = 31 * result + (ashBackgroundConcentration != null ? ashBackgroundConcentration.hashCode() : 0);
        result = 31 * result + (sulphurOxidesFractionAssociatedByFlyAsh != null ? sulphurOxidesFractionAssociatedByFlyAsh.hashCode() : 0);
        result = 31 * result + (sulphurOxidesFractionAssociatedInWetDustCollector != null ? sulphurOxidesFractionAssociatedInWetDustCollector.hashCode() : 0);
        result = 31 * result + (sulphurOxidesFractionAssociatedInDesulphurizationSystem != null ? sulphurOxidesFractionAssociatedInDesulphurizationSystem.hashCode() : 0);
        result = 31 * result + (desulphurizationSystemRunningTime != null ? desulphurizationSystemRunningTime.hashCode() : 0);
        result = 31 * result + (boilerRunningTime != null ? boilerRunningTime.hashCode() : 0);
        result = 31 * result + (ashProportionEntrainedGases != null ? ashProportionEntrainedGases.hashCode() : 0);
        result = 31 * result + (stackAverageGasesSpeed != null ? stackAverageGasesSpeed.hashCode() : 0);
        result = 31 * result + (noxMassiveInjection != null ? noxMassiveInjection.hashCode() : 0);
        result = 31 * result + (no2MassiveInjection != null ? no2MassiveInjection.hashCode() : 0);
        result = 31 * result + (noMassiveInjection != null ? noMassiveInjection.hashCode() : 0);
        result = 31 * result + (so2MassiveInjection != null ? so2MassiveInjection.hashCode() : 0);
        result = 31 * result + (ashMassiveInjection != null ? ashMassiveInjection.hashCode() : 0);
        result = 31 * result + (temperatureCoefficient != null ? temperatureCoefficient.hashCode() : 0);
        result = 31 * result + (terrainCoefficient != null ? terrainCoefficient.hashCode() : 0);
        result = 31 * result + (harmfulSubstancesDepositionCoefficient != null ? harmfulSubstancesDepositionCoefficient.hashCode() : 0);
        result = 31 * result + (no2MAC != null ? no2MAC.hashCode() : 0);
        result = 31 * result + (noMAC != null ? noMAC.hashCode() : 0);
        result = 31 * result + (so2MAC != null ? so2MAC.hashCode() : 0);
        result = 31 * result + (ashMAC != null ? ashMAC.hashCode() : 0);
        result = 31 * result + (breakdownWindSpeed != null ? breakdownWindSpeed.hashCode() : 0);
        result = 31 * result + (bwdNo2GroundLevelConcentration != null ? bwdNo2GroundLevelConcentration.hashCode() : 0);
        result = 31 * result + (bwdNoGroundLevelConcentration != null ? bwdNoGroundLevelConcentration.hashCode() : 0);
        result = 31 * result + (bwdSo2GroundLevelConcentration != null ? bwdSo2GroundLevelConcentration.hashCode() : 0);
        result = 31 * result + (bwdAshGroundLevelConcentration != null ? bwdAshGroundLevelConcentration.hashCode() : 0);
        result = 31 * result + (no2AndSo2SummationGroup != null ? no2AndSo2SummationGroup.hashCode() : 0);
        result = 31 * result + (noAndSo2SummationGroup != null ? noAndSo2SummationGroup.hashCode() : 0);
        result = 31 * result + (bwdMaxGroundLevelConcentrationDistance != null ? bwdMaxGroundLevelConcentrationDistance.hashCode() : 0);
        result = 31 * result + (windSpeedMaxNo2GroundLevelConcentration != null ? windSpeedMaxNo2GroundLevelConcentration.hashCode() : 0);
        result = 31 * result + (windSpeedMaxNoGroundLevelConcentration != null ? windSpeedMaxNoGroundLevelConcentration.hashCode() : 0);
        result = 31 * result + (windSpeedMaxSo2GroundLevelConcentration != null ? windSpeedMaxSo2GroundLevelConcentration.hashCode() : 0);
        result = 31 * result + (windSpeedMaxAshGroundLevelConcentration != null ? windSpeedMaxAshGroundLevelConcentration.hashCode() : 0);
        result = 31 * result + (windSpeedMaxGroundLevelConcentrationDistance != null ? windSpeedMaxGroundLevelConcentrationDistance.hashCode() : 0);
        return result;
    }
}
