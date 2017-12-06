package org.ekolab.server.model.content.lab2;

import org.ekolab.server.model.content.LabVariant;
import org.ekolab.server.model.content.ValidatedBy;
import org.ekolab.server.model.content.lab2.validators.AverageSoundPressureControlPointValidator;

import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

/**
 * Created by Андрей on 24.06.2017.
 */
public abstract class Lab2Variant extends LabVariant {
    /**
     * Барометрическое давление
     */
    private Double barometricPressure;

    /**
     * Температура воздуха в помещении
     */

    private Double indoorsTemperature;

    /**
     * Объем помещения с исследуемым объектом
     */

    private Double roomSize;
    /**
     * Количество однотипного оборудования
     */

    private Double quantityOfSingleTypeEquipment;

    /**
     * Радиус полусферы
     */
    private Double hemisphereRadius;

    /**
     * Результаты измерений уровня звукового давления, дБ, для каждой точки
     */
    @Size(min = 9, max = 9)
    @ValidatedBy(AverageSoundPressureControlPointValidator.class)
    private List<Double> averageSoundPressureControlPoint;

    /**
     * Средний уровень звукового давления
     */
    @Size(min = 9, max = 9)
    private List<Double> averageSoundPressure;

    public Double getBarometricPressure() {
        return barometricPressure;
    }

    public void setBarometricPressure(Double barometricPressure) {
        this.barometricPressure = barometricPressure;
    }

    public Double getIndoorsTemperature() {
        return indoorsTemperature;
    }

    public void setIndoorsTemperature(Double indoorsTemperature) {
        this.indoorsTemperature = indoorsTemperature;
    }

    public Double getRoomSize() {
        return roomSize;
    }

    public void setRoomSize(Double roomSize) {
        this.roomSize = roomSize;
    }

    public Double getQuantityOfSingleTypeEquipment() {
        return quantityOfSingleTypeEquipment;
    }

    public void setQuantityOfSingleTypeEquipment(Double quantityOfSingleTypeEquipment) {
        this.quantityOfSingleTypeEquipment = quantityOfSingleTypeEquipment;
    }

    public Double getHemisphereRadius() {
        return hemisphereRadius;
    }

    public void setHemisphereRadius(Double hemisphereRadius) {
        this.hemisphereRadius = hemisphereRadius;
    }

    public List<Double> getAverageSoundPressureControlPoint() {
        return averageSoundPressureControlPoint;
    }

    public void setAverageSoundPressureControlPoint(List<Double> averageSoundPressureControlPoint) {
        this.averageSoundPressureControlPoint = averageSoundPressureControlPoint;
    }

    public List<Double> getAverageSoundPressure() {
        return averageSoundPressure;
    }

    public void setAverageSoundPressure(List<Double> averageSoundPressure) {
        this.averageSoundPressure = averageSoundPressure;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lab2Variant)) return false;
        Lab2Variant that = (Lab2Variant) o;
        return Objects.equals(barometricPressure, that.barometricPressure) &&
                Objects.equals(indoorsTemperature, that.indoorsTemperature) &&
                Objects.equals(roomSize, that.roomSize) &&
                Objects.equals(quantityOfSingleTypeEquipment, that.quantityOfSingleTypeEquipment) &&
                Objects.equals(hemisphereRadius, that.hemisphereRadius) &&
                Objects.equals(averageSoundPressureControlPoint, that.averageSoundPressureControlPoint) &&
                Objects.equals(averageSoundPressure, that.averageSoundPressure);
    }

    @Override
    public int hashCode() {
        return Objects.hash(barometricPressure, indoorsTemperature, roomSize, quantityOfSingleTypeEquipment, hemisphereRadius, averageSoundPressureControlPoint, averageSoundPressure);
    }
}
