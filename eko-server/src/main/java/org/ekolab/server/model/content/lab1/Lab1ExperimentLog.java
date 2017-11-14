package org.ekolab.server.model.content.lab1;

import java.time.LocalDateTime;
import java.util.Objects;

public class Lab1ExperimentLog extends Lab1Variant {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lab1ExperimentLog)) return false;
        Lab1ExperimentLog that = (Lab1ExperimentLog) o;
        return Objects.equals(stacksHeight, that.stacksHeight) &&
                Objects.equals(time, that.time) &&
                Objects.equals(stacksDiameter, that.stacksDiameter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stacksHeight, stacksDiameter, time);
    }
}
