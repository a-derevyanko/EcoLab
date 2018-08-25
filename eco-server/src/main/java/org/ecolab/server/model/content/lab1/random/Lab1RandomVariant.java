package org.ecolab.server.model.content.lab1.random;

import org.ecolab.server.model.content.lab1.City;
import org.ecolab.server.model.content.lab1.Lab1Variant;

import java.util.Objects;

public class Lab1RandomVariant extends Lab1Variant {

    /**
     * Город
     */
    private City city;

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lab1RandomVariant)) return false;
        if (!super.equals(o)) return false;
        Lab1RandomVariant that = (Lab1RandomVariant) o;
        return city == that.city;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), city);
    }
}
