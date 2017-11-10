package org.ekolab.server.model.content.lab1;

import org.ekolab.server.model.content.LabData;
import org.ekolab.server.model.content.ValidatedBy;
import org.ekolab.server.model.content.lab1.validators.DValidator;
import org.ekolab.server.model.content.lab1.validators.DistanceFromEmissionSourceValidator;
import org.ekolab.server.model.content.lab1.validators.DryGasesFlowRateValidator;
import org.ekolab.server.model.content.lab1.validators.ExcessAirRatioValidator;
import org.ekolab.server.model.content.lab1.validators.ExcessOfNormsValidator;
import org.ekolab.server.model.content.lab1.validators.FValidator;
import org.ekolab.server.model.content.lab1.validators.FlueGasNOxConcentrationNCValidator;
import org.ekolab.server.model.content.lab1.validators.FlueGasesRateValidator;
import org.ekolab.server.model.content.lab1.validators.FlueGasesSpeedValidator;
import org.ekolab.server.model.content.lab1.validators.MValidator;
import org.ekolab.server.model.content.lab1.validators.MassEmissionsValidator;
import org.ekolab.server.model.content.lab1.validators.MaximumSurfaceConcentrationValidator;
import org.ekolab.server.model.content.lab1.validators.NValidator;
import org.ekolab.server.model.content.lab1.validators.UValidator;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Created by Андрей on 24.06.2017.
 */
public class Lab1Data<V extends Lab1Variant> extends LabData<V> {
    /**
     * Название объекта
     */
    @Nullable
    private String name;

    /**
     * Температура наружного воздуха
     */
    private Integer outsideAirTemperature;

    /**
     * Высота дымовой трубы
     */
    private Double stacksHeight;

    /**
     * Диаметр дымовой трубы
     */
    private Double stacksDiameter;

    /**
     * Время проведения измерений
     */
    private LocalDateTime time;

    /**
     * Паровая нагрузка котла
     */
    private Integer steamProductionCapacity;

    /**
     * Содержание кислорода в сечении газохода, где проводились измерения
     */
    private Double oxygenConcentrationPoint;


    /**
     * Расход природного газа на котел, приведенный к нормальным условиям
     */
    private Integer fuelConsumerNormalized;

    /**
     * Температура уходящих дымовых газов
     */
    private Integer stackExitTemperature;

    /**
     * Концентрация оксидов азота в дымовых газах по результатам измерений
     */
    private Integer flueGasNOxConcentration;

    /**
     * Коэффициент избытка воздуха в точке измерения
     */
    @ValidatedBy(ExcessAirRatioValidator.class)
    private Double excessAirRatio;

    /**
     * Концентрация оксидов азота, приведенная к стандартному коэффициенту избытка воздуха α=1,4
     */
    @ValidatedBy(FlueGasNOxConcentrationNCValidator.class)
    private Double flueGasNOxConcentrationNC;

    /**
     * Превышение допустимых норм
     */
    @ValidatedBy(ExcessOfNormsValidator.class)
    private Boolean excessOfNorms;

    /**
     * Расход дымовых газов, выбрасываемых в атмосферу
     */
    @ValidatedBy(FlueGasesRateValidator.class)
    private Double flueGasesRate;

    /**
     * Объемный расход сухих газов
     */
    @ValidatedBy(DryGasesFlowRateValidator.class)
    private Double dryGasesFlowRate;

    /**
     * Массовые выбросы оксидов азота
     */
    @ValidatedBy(MassEmissionsValidator.class)
    private Double massEmissions;

    /**
     * Скорость дымовых газов на выходе из дымовой трубы
     */
    @ValidatedBy(FlueGasesSpeedValidator.class)
    private Double flueGasesSpeed;

    /**
     * Параметр f
     */
    @ValidatedBy(FValidator.class)
    private Double f;

    /**
     * Коэффициент m учитывающий условия выхода газов из дымовой трубы
     */
    @ValidatedBy(MValidator.class)
    private Double m;

    /**
     * Параметр υ_м
     */
    @ValidatedBy(UValidator.class)
    private Double u;

    /**
     * Коэффициент n учитывающий условия выхода газов из дымовой трубы
     */
    @ValidatedBy(NValidator.class)
    private Double n;

    /**
     * Безразмерный коэффициент d
     */
    @ValidatedBy(DValidator.class)
    private Double d;

    /**
     * Безразмерный коэффициент, учитывающий скорость оседания вредных веществ в атмосферном воздухе
     */
    private Double harmfulSubstancesDepositionCoefficient;

    /**
     * Коэффициент, учитывающий влияние рельефа местности
     */
    private Double terrainCoefficient;

    /**
     * Коэффициент, характеризующий температурную стратификацию атмосферы
     */
    private Double temperatureCoefficient;

    /**
     * Расстояние от источника выбросов, на котором приземные концентрации загрязняющих веществ достигают максимального значения
     */
    @ValidatedBy(DistanceFromEmissionSourceValidator.class)
    private Double distanceFromEmissionSource;

    /**
     * Максимальная приземная концентрация оксидов азота
     */
    @ValidatedBy(MaximumSurfaceConcentrationValidator.class)
    private Double maximumSurfaceConcentration;

    @Nullable
    public String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    public Double getExcessAirRatio() {
        return excessAirRatio;
    }

    public void setExcessAirRatio(Double excessAirRatio) {
        this.excessAirRatio = excessAirRatio;
    }

    public Double getFlueGasNOxConcentrationNC() {
        return flueGasNOxConcentrationNC;
    }

    public void setFlueGasNOxConcentrationNC(Double flueGasNOxConcentrationNC) {
        this.flueGasNOxConcentrationNC = flueGasNOxConcentrationNC;
    }

    public Boolean getExcessOfNorms() {
        return excessOfNorms;
    }

    public void setExcessOfNorms(Boolean excessOfNorms) {
        this.excessOfNorms = excessOfNorms;
    }

    public Double getFlueGasesRate() {
        return flueGasesRate;
    }

    public void setFlueGasesRate(Double flueGasesRate) {
        this.flueGasesRate = flueGasesRate;
    }

    public Double getDryGasesFlowRate() {
        return dryGasesFlowRate;
    }

    public void setDryGasesFlowRate(Double dryGasesFlowRate) {
        this.dryGasesFlowRate = dryGasesFlowRate;
    }

    public Double getMassEmissions() {
        return massEmissions;
    }

    public void setMassEmissions(Double massEmissions) {
        this.massEmissions = massEmissions;
    }

    public Double getFlueGasesSpeed() {
        return flueGasesSpeed;
    }

    public void setFlueGasesSpeed(Double flueGasesSpeed) {
        this.flueGasesSpeed = flueGasesSpeed;
    }

    public Double getF() {
        return f;
    }

    public void setF(Double f) {
        this.f = f;
    }

    public Double getM() {
        return m;
    }

    public void setM(Double m) {
        this.m = m;
    }

    public Double getU() {
        return u;
    }

    public void setU(Double u) {
        this.u = u;
    }

    public Double getN() {
        return n;
    }

    public void setN(Double n) {
        this.n = n;
    }

    public Double getD() {
        return d;
    }

    public void setD(Double d) {
        this.d = d;
    }

    public Double getHarmfulSubstancesDepositionCoefficient() {
        return harmfulSubstancesDepositionCoefficient;
    }

    public void setHarmfulSubstancesDepositionCoefficient(Double harmfulSubstancesDepositionCoefficient) {
        this.harmfulSubstancesDepositionCoefficient = harmfulSubstancesDepositionCoefficient;
    }

    public Double getTerrainCoefficient() {
        return terrainCoefficient;
    }

    public void setTerrainCoefficient(Double terrainCoefficient) {
        this.terrainCoefficient = terrainCoefficient;
    }

    public Double getTemperatureCoefficient() {
        return temperatureCoefficient;
    }

    public void setTemperatureCoefficient(Double temperatureCoefficient) {
        this.temperatureCoefficient = temperatureCoefficient;
    }

    public Double getDistanceFromEmissionSource() {
        return distanceFromEmissionSource;
    }

    public void setDistanceFromEmissionSource(Double distanceFromEmissionSource) {
        this.distanceFromEmissionSource = distanceFromEmissionSource;
    }

    public Double getMaximumSurfaceConcentration() {
        return maximumSurfaceConcentration;
    }

    public void setMaximumSurfaceConcentration(Double maximumSurfaceConcentration) {
        this.maximumSurfaceConcentration = maximumSurfaceConcentration;
    }

    public Integer getOutsideAirTemperature() {
        return outsideAirTemperature;
    }

    public void setOutsideAirTemperature(Integer outsideAirTemperature) {
        this.outsideAirTemperature = outsideAirTemperature;
    }

    public Double getStacksHeight() {
        return stacksHeight;
    }

    public void setStacksHeight(Double stacksHeight) {
        this.stacksHeight = stacksHeight;
    }

    public Double getStacksDiameter() {
        return stacksDiameter;
    }

    public void setStacksDiameter(Double stacksDiameter) {
        this.stacksDiameter = stacksDiameter;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Integer getSteamProductionCapacity() {
        return steamProductionCapacity;
    }

    public void setSteamProductionCapacity(Integer steamProductionCapacity) {
        this.steamProductionCapacity = steamProductionCapacity;
    }

    public Double getOxygenConcentrationPoint() {
        return oxygenConcentrationPoint;
    }

    public void setOxygenConcentrationPoint(Double oxygenConcentrationPoint) {
        this.oxygenConcentrationPoint = oxygenConcentrationPoint;
    }

    public Integer getFuelConsumerNormalized() {
        return fuelConsumerNormalized;
    }

    public void setFuelConsumerNormalized(Integer fuelConsumerNormalized) {
        this.fuelConsumerNormalized = fuelConsumerNormalized;
    }

    public Integer getStackExitTemperature() {
        return stackExitTemperature;
    }

    public void setStackExitTemperature(Integer stackExitTemperature) {
        this.stackExitTemperature = stackExitTemperature;
    }

    public Integer getFlueGasNOxConcentration() {
        return flueGasNOxConcentration;
    }

    public void setFlueGasNOxConcentration(Integer flueGasNOxConcentration) {
        this.flueGasNOxConcentration = flueGasNOxConcentration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lab1Data)) return false;
        if (!super.equals(o)) return false;
        Lab1Data lab1Data = (Lab1Data) o;
        return Objects.equals(name, lab1Data.name) &&
                Objects.equals(outsideAirTemperature, lab1Data.outsideAirTemperature) &&
                Objects.equals(stacksHeight, lab1Data.stacksHeight) &&
                Objects.equals(stacksDiameter, lab1Data.stacksDiameter) &&
                Objects.equals(time, lab1Data.time) &&
                Objects.equals(steamProductionCapacity, lab1Data.steamProductionCapacity) &&
                Objects.equals(oxygenConcentrationPoint, lab1Data.oxygenConcentrationPoint) &&
                Objects.equals(fuelConsumerNormalized, lab1Data.fuelConsumerNormalized) &&
                Objects.equals(stackExitTemperature, lab1Data.stackExitTemperature) &&
                Objects.equals(flueGasNOxConcentration, lab1Data.flueGasNOxConcentration) &&
                Objects.equals(excessAirRatio, lab1Data.excessAirRatio) &&
                Objects.equals(flueGasNOxConcentrationNC, lab1Data.flueGasNOxConcentrationNC) &&
                Objects.equals(excessOfNorms, lab1Data.excessOfNorms) &&
                Objects.equals(flueGasesRate, lab1Data.flueGasesRate) &&
                Objects.equals(dryGasesFlowRate, lab1Data.dryGasesFlowRate) &&
                Objects.equals(massEmissions, lab1Data.massEmissions) &&
                Objects.equals(flueGasesSpeed, lab1Data.flueGasesSpeed) &&
                Objects.equals(f, lab1Data.f) &&
                Objects.equals(m, lab1Data.m) &&
                Objects.equals(u, lab1Data.u) &&
                Objects.equals(n, lab1Data.n) &&
                Objects.equals(d, lab1Data.d) &&
                Objects.equals(harmfulSubstancesDepositionCoefficient, lab1Data.harmfulSubstancesDepositionCoefficient) &&
                Objects.equals(terrainCoefficient, lab1Data.terrainCoefficient) &&
                Objects.equals(temperatureCoefficient, lab1Data.temperatureCoefficient) &&
                Objects.equals(distanceFromEmissionSource, lab1Data.distanceFromEmissionSource) &&
                Objects.equals(maximumSurfaceConcentration, lab1Data.maximumSurfaceConcentration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, outsideAirTemperature, stacksHeight, stacksDiameter, time, steamProductionCapacity, oxygenConcentrationPoint, fuelConsumerNormalized, stackExitTemperature, flueGasNOxConcentration, excessAirRatio, flueGasNOxConcentrationNC, excessOfNorms, flueGasesRate, dryGasesFlowRate, massEmissions, flueGasesSpeed, f, m, u, n, d, harmfulSubstancesDepositionCoefficient, terrainCoefficient, temperatureCoefficient, distanceFromEmissionSource, maximumSurfaceConcentration);
    }
}
