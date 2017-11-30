package org.ekolab.server.model.content.lab2.experiment;

import org.ekolab.server.model.content.lab2.Lab2Variant;

import java.util.Objects;

public class Lab2ExperimentLog extends Lab2Variant {
    /**
     * Название объекта
     */
    private String name;

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
        Lab2ExperimentLog that = (Lab2ExperimentLog) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
