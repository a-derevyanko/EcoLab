package org.ekolab.server.model.content.lab2;

import org.ekolab.server.model.content.LabData;

import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;


public class Lab2Data<V extends Lab2Variant> extends LabData<V> {
    /**
     * Барометрическое давление
     */
    private Integer barometricPressure;

    /**
     * Температура воздуха в помещении
     */

    private Integer indoorsTemperature;

    /**
     * Объем помещения с исследуемым объектом
     */

    private Integer roomSize;
    /**
     * Количество однотипного оборудования
     */

    private Integer quantityOfSingleTypeEquipment;

    /**
     * Радиус полусферы
     */
    private Double hemisphereRadius;

    /**
     * Средний уровень звукового давления
     */
    @Size(min = 9, max = 9)
    private List<Double> averageSoundPressure;

    /**
     * Корректирующая поправка
     */
    private Double correctionFactor;

    /**
     * Уровень звукового давления на измерительной поверхности
     */
    @Size(min = 9, max = 9)
    private List<Double> soundPressureMeasuringSurface;

    /**
     * Поверхность полусферы
     */
    private Double hemisphereSurface;

    /**
     * Показатель измерительной поверхности
     */
    private Double measuringFactor;

    /**
     * Уровень звуковой мощности
     */
    @Size(min = 9, max = 9)
    private List<Double> soundPowerLevel;

    /**
     * Постоянная помещения на среднегеометрической частоте 1000 Гц
     */
    private Double roomConstant1000;

    /**
     * Частотный множитель
     */
    @Size(min = 9, max = 9)
    private List<Double> frequencyCoefficient;

    /**
     * Постоянная помещения
     */
    private Double roomConstant;

    /**
     * Уровни звукового давления в зоне отраженного звука
     */
    @Size(min = 9, max = 9)
    private List<Double> reflectedSoundPower;

    public Integer getBarometricPressure() {
        return barometricPressure;
    }

    public void setBarometricPressure(Integer barometricPressure) {
        this.barometricPressure = barometricPressure;
    }

    public Integer getIndoorsTemperature() {
        return indoorsTemperature;
    }

    public void setIndoorsTemperature(Integer indoorsTemperature) {
        this.indoorsTemperature = indoorsTemperature;
    }

    public Integer getRoomSize() {
        return roomSize;
    }

    public void setRoomSize(Integer roomSize) {
        this.roomSize = roomSize;
    }

    public Integer getQuantityOfSingleTypeEquipment() {
        return quantityOfSingleTypeEquipment;
    }

    public void setQuantityOfSingleTypeEquipment(Integer quantityOfSingleTypeEquipment) {
        this.quantityOfSingleTypeEquipment = quantityOfSingleTypeEquipment;
    }

    public Double getHemisphereRadius() {
        return hemisphereRadius;
    }

    public void setHemisphereRadius(Double hemisphereRadius) {
        this.hemisphereRadius = hemisphereRadius;
    }

    public List<Double> getAverageSoundPressure() {
        return averageSoundPressure;
    }

    public void setAverageSoundPressure(List<Double> averageSoundPressure) {
        this.averageSoundPressure = averageSoundPressure;
    }

    public Double getCorrectionFactor() {
        return correctionFactor;
    }

    public void setCorrectionFactor(Double correctionFactor) {
        this.correctionFactor = correctionFactor;
    }

    public List<Double> getSoundPressureMeasuringSurface() {
        return soundPressureMeasuringSurface;
    }

    public void setSoundPressureMeasuringSurface(List<Double> soundPressureMeasuringSurface) {
        this.soundPressureMeasuringSurface = soundPressureMeasuringSurface;
    }

    public Double getHemisphereSurface() {
        return hemisphereSurface;
    }

    public void setHemisphereSurface(Double hemisphereSurface) {
        this.hemisphereSurface = hemisphereSurface;
    }

    public Double getMeasuringFactor() {
        return measuringFactor;
    }

    public void setMeasuringFactor(Double measuringFactor) {
        this.measuringFactor = measuringFactor;
    }

    public List<Double> getSoundPowerLevel() {
        return soundPowerLevel;
    }

    public void setSoundPowerLevel(List<Double> soundPowerLevel) {
        this.soundPowerLevel = soundPowerLevel;
    }

    public Double getRoomConstant1000() {
        return roomConstant1000;
    }

    public void setRoomConstant1000(Double roomConstant1000) {
        this.roomConstant1000 = roomConstant1000;
    }

    public List<Double> getFrequencyCoefficient() {
        return frequencyCoefficient;
    }

    public void setFrequencyCoefficient(List<Double> frequencyCoefficient) {
        this.frequencyCoefficient = frequencyCoefficient;
    }

    public Double getRoomConstant() {
        return roomConstant;
    }

    public void setRoomConstant(Double roomConstant) {
        this.roomConstant = roomConstant;
    }

    public List<Double> getReflectedSoundPower() {
        return reflectedSoundPower;
    }

    public void setReflectedSoundPower(List<Double> reflectedSoundPower) {
        this.reflectedSoundPower = reflectedSoundPower;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lab2Data)) return false;
        if (!super.equals(o)) return false;
        Lab2Data<?> lab2Data = (Lab2Data<?>) o;
        return Objects.equals(barometricPressure, lab2Data.barometricPressure) &&
                Objects.equals(indoorsTemperature, lab2Data.indoorsTemperature) &&
                Objects.equals(roomSize, lab2Data.roomSize) &&
                Objects.equals(quantityOfSingleTypeEquipment, lab2Data.quantityOfSingleTypeEquipment) &&
                Objects.equals(hemisphereRadius, lab2Data.hemisphereRadius) &&
                Objects.equals(averageSoundPressure, lab2Data.averageSoundPressure) &&
                Objects.equals(correctionFactor, lab2Data.correctionFactor) &&
                Objects.equals(soundPressureMeasuringSurface, lab2Data.soundPressureMeasuringSurface) &&
                Objects.equals(hemisphereSurface, lab2Data.hemisphereSurface) &&
                Objects.equals(measuringFactor, lab2Data.measuringFactor) &&
                Objects.equals(soundPowerLevel, lab2Data.soundPowerLevel) &&
                Objects.equals(roomConstant1000, lab2Data.roomConstant1000) &&
                Objects.equals(frequencyCoefficient, lab2Data.frequencyCoefficient) &&
                Objects.equals(roomConstant, lab2Data.roomConstant) &&
                Objects.equals(reflectedSoundPower, lab2Data.reflectedSoundPower);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), barometricPressure, indoorsTemperature, roomSize, quantityOfSingleTypeEquipment, hemisphereRadius, averageSoundPressure, correctionFactor, soundPressureMeasuringSurface, hemisphereSurface, measuringFactor, soundPowerLevel, roomConstant1000, frequencyCoefficient, roomConstant, reflectedSoundPower);
    }
}
