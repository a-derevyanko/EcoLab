package org.ekolab.server.model.content.lab1;

import org.ekolab.server.model.content.LabData;
import org.ekolab.server.model.content.Validated;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * Created by Андрей on 24.06.2017.
 */
public class Lab1Data extends LabData<Lab1Variant> {
    /**
     * Название объекта
     */
    @Nullable
    private String name;

    /**
     * Коэффициент избытка воздуха в точке измерения
     */
    @Validated
    private Double excessAirRatio;

    /**
     * Концентрация оксидов азота, приведенная к стандартному коэффициенту избытка воздуха α=1,4
     */
    @Validated
    private Double flueGasNOxConcentrationNC;

    /**
     * Превышение допустимых норм
     */
    @Validated
    private Boolean excessOfNorms;

    /**
     * Расход дымовых газов, выбрасываемых в атмосферу
     */
    @Validated
    private Double flueGasesRate;

    /**
     * Объемный расход сухих газов
     */
    @Validated
    private Double dryGasesFlowRate;

    /**
     * Массовые выбросы оксидов азота
     */
    @Validated
    private Double massEmissions;

    /**
     * Скорость дымовых газов на выходе из дымовой трубы
     */
    @Validated
    private Double flueGasesSpeed;

    /**
     * Параметр f
     */
    @Validated
    private Double f;

    /**
     * Коэффициент m учитывающий условия выхода газов из дымовой трубы
     */
    @Validated
    private Double m;

    /**
     * Параметр υ_м
     */
    @Validated
    private Double u;

    /**
     * Коэффициент n учитывающий условия выхода газов из дымовой трубы
     */
    @Validated
    private Double n;

    /**
     * Безразмерный коэффициент d
     */
    @Validated
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
    @Validated
    private Double distanceFromEmissionSource;

    /**
     * Максимальная приземная концентрация оксидов азота
     */
    @Validated
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lab1Data)) return false;
        if (!super.equals(o)) return false;
        Lab1Data lab1Data = (Lab1Data) o;
        return Objects.equals(name, lab1Data.name) &&
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
        return Objects.hash(super.hashCode(), name, excessAirRatio, flueGasNOxConcentrationNC, excessOfNorms, flueGasesRate,
                dryGasesFlowRate, massEmissions, flueGasesSpeed, f, m, u, n, d, harmfulSubstancesDepositionCoefficient,
                terrainCoefficient, temperatureCoefficient, distanceFromEmissionSource, maximumSurfaceConcentration);
    }
}
