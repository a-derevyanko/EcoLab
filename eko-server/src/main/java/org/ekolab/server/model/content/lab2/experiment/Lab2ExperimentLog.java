package org.ekolab.server.model.content.lab2.experiment;

import org.ekolab.server.model.content.lab2.Lab2Variant;

import java.util.Objects;

public class Lab2ExperimentLog extends Lab2Variant {
    /**
     * Радиус полусферы
     */
    private Double hemisphereRadius;

    public Double getHemisphereRadius() {
        return hemisphereRadius;
    }

    public void setHemisphereRadius(Double hemisphereRadius) {
        this.hemisphereRadius = hemisphereRadius;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lab2ExperimentLog)) return false;
        if (!super.equals(o)) return false;
        Lab2ExperimentLog that = (Lab2ExperimentLog) o;
        return Objects.equals(hemisphereRadius, that.hemisphereRadius);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), hemisphereRadius);
    }
}
