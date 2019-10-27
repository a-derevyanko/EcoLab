package org.ecolab.server.model.content.lab2.experiment;

import org.ecolab.server.model.content.lab2.Lab2Variant;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Lab2ExperimentLog extends Lab2Variant {
    /**
     * Название объекта
     */
    @Nullable
    private String name;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lab2ExperimentLog)) return false;
        if (!super.equals(o)) return false;
        var that = (Lab2ExperimentLog) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(hemisphereRadius, that.hemisphereRadius) &&
                Objects.equals(roomSize, that.roomSize) &&
                Objects.equals(soundPressure, that.soundPressure);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, hemisphereRadius, roomSize, soundPressure);
    }
}
