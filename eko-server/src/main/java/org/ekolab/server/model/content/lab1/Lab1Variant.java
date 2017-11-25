package org.ekolab.server.model.content.lab1;

import org.ekolab.server.model.content.LabVariant;

import java.util.Objects;

/**
 * Created by Андрей on 24.06.2017.
 */
public abstract class Lab1Variant extends LabVariant {

    /**
     * Температура наружного воздуха
     */
    private Integer outsideAirTemperature;

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

    public Integer getOutsideAirTemperature() {
        return outsideAirTemperature;
    }

    public void setOutsideAirTemperature(Integer outsideAirTemperature) {
        this.outsideAirTemperature = outsideAirTemperature;
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
        if (!(o instanceof Lab1Variant)) return false;
        Lab1Variant that = (Lab1Variant) o;
        return Objects.equals(outsideAirTemperature, that.outsideAirTemperature) &&
                Objects.equals(steamProductionCapacity, that.steamProductionCapacity) &&
                Objects.equals(oxygenConcentrationPoint, that.oxygenConcentrationPoint) &&
                Objects.equals(fuelConsumerNormalized, that.fuelConsumerNormalized) &&
                Objects.equals(stackExitTemperature, that.stackExitTemperature) &&
                Objects.equals(flueGasNOxConcentration, that.flueGasNOxConcentration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(outsideAirTemperature, steamProductionCapacity, oxygenConcentrationPoint, fuelConsumerNormalized, stackExitTemperature, flueGasNOxConcentration);
    }
}
