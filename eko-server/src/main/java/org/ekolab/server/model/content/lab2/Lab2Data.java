package org.ekolab.server.model.content.lab2;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ekolab.server.model.content.Calculated;
import org.ekolab.server.model.content.LabData;
import org.ekolab.server.model.content.ValidatedBy;
import org.ekolab.server.model.content.lab2.validators.AverageSoundPressureControlPointValidator;
import org.ekolab.server.model.content.lab2.validators.CorrectionFactorValidator;
import org.ekolab.server.model.content.lab2.validators.FrequencyCoefficientValidator;
import org.ekolab.server.model.content.lab2.validators.MeasuringFactorValidator;
import org.ekolab.server.model.content.lab2.validators.QuantityOfSingleTypeEquipmentValidator;
import org.ekolab.server.model.content.lab2.validators.ReflectedSoundPowerValidator;
import org.ekolab.server.model.content.lab2.validators.RoomConstant1000Validator;
import org.ekolab.server.model.content.lab2.validators.RoomConstantValidator;
import org.ekolab.server.model.content.lab2.validators.SoundPowerLevelValidator;

import javax.validation.constraints.Size;
import java.util.List;
import java.util.SortedMap;

@Data
@EqualsAndHashCode(callSuper = true)
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
    @ValidatedBy(QuantityOfSingleTypeEquipmentValidator.class)
    private Integer quantityOfSingleTypeEquipment;

    /**
     * Радиус полусферы
     */
    private Double hemisphereRadius;

    /**
     * Средний уровень звукового давления
     */
    @Size(min = 9, max = 9)
    @ValidatedBy(AverageSoundPressureControlPointValidator.class)
    private List<Double> averageSoundPressure;

    /**
     * Корректирующая поправка
     */
    @ValidatedBy(CorrectionFactorValidator.class)
    private Double correctionFactor;

    /**
     * Уровень звукового давления на измерительной поверхности
     */
    private Double soundPressureMeasuringSurface;

    /**
     * Поверхность полусферы
     */
    @Calculated
    private Double hemisphereSurface;

    /**
     * Показатель измерительной поверхности
     */
    @ValidatedBy(MeasuringFactorValidator.class)
    private Double measuringFactor;

    /**
     * Уровень звуковой мощности
     */
    @ValidatedBy(SoundPowerLevelValidator.class)
    private Double soundPowerLevel;

    /**
     * Постоянная помещения на среднегеометрической частоте 1000 Гц
     */
    @ValidatedBy(RoomConstant1000Validator.class)
    private Double roomConstant1000;

    /**
     * Частотный множитель
     */
    @ValidatedBy(FrequencyCoefficientValidator.class)
    private Double frequencyCoefficient;

    /**
     * Постоянная помещения
     */
    @ValidatedBy(RoomConstantValidator.class)
    private Double roomConstant;

    /**
     * Уровень звукового давления в зоне отраженного звука
     */
    @ValidatedBy(ReflectedSoundPowerValidator.class)
    private Double reflectedSoundPower;

    /**
     * Результаты расчета
     */
    @Calculated
    private SortedMap<CalculationResultType, List<Double>> calculationResult;

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

    public Double getSoundPressureMeasuringSurface() {
        return soundPressureMeasuringSurface;
    }

    public void setSoundPressureMeasuringSurface(Double soundPressureMeasuringSurface) {
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

    public Double getSoundPowerLevel() {
        return soundPowerLevel;
    }

    public void setSoundPowerLevel(Double soundPowerLevel) {
        this.soundPowerLevel = soundPowerLevel;
    }

    public Double getRoomConstant1000() {
        return roomConstant1000;
    }

    public void setRoomConstant1000(Double roomConstant1000) {
        this.roomConstant1000 = roomConstant1000;
    }

    public Double getFrequencyCoefficient() {
        return frequencyCoefficient;
    }

    public void setFrequencyCoefficient(Double frequencyCoefficient) {
        this.frequencyCoefficient = frequencyCoefficient;
    }

    public Double getRoomConstant() {
        return roomConstant;
    }

    public void setRoomConstant(Double roomConstant) {
        this.roomConstant = roomConstant;
    }

    public Double getReflectedSoundPower() {
        return reflectedSoundPower;
    }

    public void setReflectedSoundPower(Double reflectedSoundPower) {
        this.reflectedSoundPower = reflectedSoundPower;
    }

    public SortedMap<CalculationResultType, List<Double>> getCalculationResult() {
        return calculationResult;
    }

    public void setCalculationResult(SortedMap<CalculationResultType, List<Double>> calculationResult) {
        this.calculationResult = calculationResult;
    }
}
