package org.ecolab.server.model.content.lab1;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ecolab.server.model.content.LabData;
import org.ecolab.server.model.content.ValidatedBy;
import org.ecolab.server.model.content.lab1.validators.DValidator;
import org.ecolab.server.model.content.lab1.validators.DistanceFromEmissionSourceValidator;
import org.ecolab.server.model.content.lab1.validators.DryGasesFlowRateValidator;
import org.ecolab.server.model.content.lab1.validators.ExcessAirRatioValidator;
import org.ecolab.server.model.content.lab1.validators.ExcessOfNormsValidator;
import org.ecolab.server.model.content.lab1.validators.FValidator;
import org.ecolab.server.model.content.lab1.validators.FlueGasNOxConcentrationNCValidator;
import org.ecolab.server.model.content.lab1.validators.FlueGasesRateValidator;
import org.ecolab.server.model.content.lab1.validators.FlueGasesSpeedValidator;
import org.ecolab.server.model.content.lab1.validators.MValidator;
import org.ecolab.server.model.content.lab1.validators.MassEmissionsValidator;
import org.ecolab.server.model.content.lab1.validators.MaximumSurfaceConcentrationValidator;
import org.ecolab.server.model.content.lab1.validators.NValidator;
import org.ecolab.server.model.content.lab1.validators.UValidator;

import javax.validation.constraints.Digits;

/**
 * Created by Андрей on 24.06.2017.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Lab1Data<V extends Lab1Variant> extends LabData<V> {

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
    @Digits(integer = Integer.MAX_VALUE, fraction = Integer.MAX_VALUE)
    @ValidatedBy(ExcessAirRatioValidator.class)
    private Double excessAirRatio;

    /**
     * Концентрация оксидов азота, приведенная к стандартному коэффициенту избытка воздуха α=1,4
     */
    @Digits(integer = Integer.MAX_VALUE, fraction = Integer.MAX_VALUE)
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
    @Digits(integer = Integer.MAX_VALUE, fraction = Integer.MAX_VALUE)
    @ValidatedBy(FlueGasesRateValidator.class)
    private Double flueGasesRate;

    /**
     * Объемный расход сухих газов
     */
    @Digits(integer = Integer.MAX_VALUE, fraction = Integer.MAX_VALUE)
    @ValidatedBy(DryGasesFlowRateValidator.class)
    private Double dryGasesFlowRate;

    /**
     * Массовые выбросы оксидов азота
     */
    @Digits(integer = 2, fraction = Integer.MAX_VALUE)
    @ValidatedBy(MassEmissionsValidator.class)
    private Double massEmissions;

    /**
     * Скорость дымовых газов на выходе из дымовой трубы
     */
    @Digits(integer = Integer.MAX_VALUE, fraction = Integer.MAX_VALUE)
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
}
