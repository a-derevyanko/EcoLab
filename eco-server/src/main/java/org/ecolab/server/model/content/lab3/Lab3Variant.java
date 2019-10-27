package org.ecolab.server.model.content.lab3;

import org.ecolab.server.model.content.LabVariant;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Objects;

/**
 * Created by 777Al on 06.04.2017.
 */
public class Lab3Variant extends LabVariant {
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
     * Тип топлива
     */
    @Valid
    private FuelType fuelType;

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
     * Господствующее направление ветра
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
    private Double fuelConsumer;

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
    @Digits(integer = 1, fraction = 1)
    private Double excessAirRatio;

    /**
     * Теоретический объем воздуха
     */
    @Digits(integer = 100000, fraction = 2)
    private Double airVolume;

    /**
     * Теоретический объем паров воды
     */
    @Digits(integer = 100000, fraction = 2)
    private Double waterVaporVolume;

    /**
     * Теоретический объем продуктов сгорания
     */
    @Digits(integer = 100000, fraction = 2)
    private Double combustionProductsVolume;

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

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
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

    public Double getFuelConsumer() {
        return fuelConsumer;
    }

    public void setFuelConsumer(Double fuelConsumer) {
        this.fuelConsumer = fuelConsumer;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var variant = (Lab3Variant) o;
        return Objects.equals(tppOutput, variant.tppOutput) &&
                numberOfUnits == variant.numberOfUnits &&
                city == variant.city &&
                fuelType == variant.fuelType &&
                Objects.equals(steamProductionCapacity, variant.steamProductionCapacity) &&
                numberOfStacks == variant.numberOfStacks &&
                Objects.equals(stacksHeight, variant.stacksHeight) &&
                windDirection == variant.windDirection &&
                Objects.equals(windSpeed, variant.windSpeed) &&
                Objects.equals(lowHeatValue, variant.lowHeatValue) &&
                Objects.equals(fuelConsumer, variant.fuelConsumer) &&
                Objects.equals(sulphurContent, variant.sulphurContent) &&
                Objects.equals(ashContent, variant.ashContent) &&
                Objects.equals(waterContent, variant.waterContent) &&
                Objects.equals(ashRecyclingFactor, variant.ashRecyclingFactor) &&
                Objects.equals(flueGasNOxConcentration, variant.flueGasNOxConcentration) &&
                Objects.equals(stackExitTemperature, variant.stackExitTemperature) &&
                Objects.equals(outsideAirTemperature, variant.outsideAirTemperature) &&
                Objects.equals(excessAirRatio, variant.excessAirRatio) &&
                Objects.equals(combustionProductsVolume, variant.combustionProductsVolume) &&
                Objects.equals(waterVaporVolume, variant.waterVaporVolume) &&
                Objects.equals(airVolume, variant.airVolume) &&
                Objects.equals(no2BackgroundConcentration, variant.no2BackgroundConcentration) &&
                Objects.equals(noBackgroundConcentration, variant.noBackgroundConcentration) &&
                Objects.equals(so2BackgroundConcentration, variant.so2BackgroundConcentration) &&
                Objects.equals(ashBackgroundConcentration, variant.ashBackgroundConcentration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tppOutput, numberOfUnits, city, fuelType, steamProductionCapacity, numberOfStacks, stacksHeight, windDirection, windSpeed, lowHeatValue, fuelConsumer, sulphurContent, ashContent, waterContent, ashRecyclingFactor, flueGasNOxConcentration, stackExitTemperature, outsideAirTemperature, excessAirRatio, combustionProductsVolume, waterVaporVolume, airVolume, no2BackgroundConcentration, noBackgroundConcentration, so2BackgroundConcentration, ashBackgroundConcentration);
    }
}
