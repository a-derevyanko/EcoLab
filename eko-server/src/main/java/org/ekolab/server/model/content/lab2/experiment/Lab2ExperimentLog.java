package org.ekolab.server.model.content.lab2.experiment;

import org.ekolab.server.model.content.lab2.Lab2Variant;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Lab2ExperimentLog extends Lab2Variant {
    /**
     * Радиус полусферы
     */
    private Double hemisphereRadius;

    /**
     * Объем помещения с исследуемым объектом
     */
    private Integer roomSize;

    private List<List<Double>> soundPressure = new ArrayList<>();

    public Double getHemisphereRadius() {
        return hemisphereRadius;
    }

    public void setHemisphereRadius(Double hemisphereRadius) {
        this.hemisphereRadius = hemisphereRadius;
    }

    public Integer getRoomSize() {
        return roomSize;
    }

    public void setRoomSize(Integer roomSize) {
        this.roomSize = roomSize;
    }

    public List<List<Double>> getSoundPressure() {
        return soundPressure;
    }

    public void setSoundPressure(List<List<Double>> soundPressure) {
        this.soundPressure = soundPressure;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lab2ExperimentLog)) return false;
        if (!super.equals(o)) return false;
        Lab2ExperimentLog that = (Lab2ExperimentLog) o;
        return Objects.equals(hemisphereRadius, that.hemisphereRadius) &&
                Objects.equals(roomSize, that.roomSize) &&
                Objects.equals(soundPressure, that.soundPressure);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), hemisphereRadius, roomSize, soundPressure);
    }
}
