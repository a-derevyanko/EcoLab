package org.ecolab.server.model.content.lab1.experiment;

import org.ecolab.server.model.content.lab1.Lab1Variant;

import java.time.LocalDateTime;
import java.util.Objects;

public class Lab1ExperimentLog extends Lab1Variant {
    /**
     * Название объекта
     */
    private String name;

    /**
     * Время проведения измерений
     */
    private LocalDateTime time;

    /**
     * Высота дымовой трубы
     */
    private Double stacksHeight;

    /**
     * Диаметр дымовой трубы
     */
    private Double stacksDiameter;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lab1ExperimentLog)) return false;
        if (!super.equals(o)) return false;
        var that = (Lab1ExperimentLog) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(time, that.time) &&
                Objects.equals(stacksHeight, that.stacksHeight) &&
                Objects.equals(stacksDiameter, that.stacksDiameter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, time, stacksHeight, stacksDiameter);
    }
}
