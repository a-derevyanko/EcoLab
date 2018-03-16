package org.ekolab.server.model.content.lab2.random;

import org.ekolab.server.model.content.lab2.Lab2Variant;
import org.ekolab.server.model.content.lab2.ObjectType;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class Lab2RandomVariant extends Lab2Variant {
    /**
     * Название объекта
     */
    @NotNull
    private ObjectType name;

    public ObjectType getName() {
        return name;
    }

    public void setName(ObjectType name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lab2RandomVariant)) return false;
        if (!super.equals(o)) return false;
        Lab2RandomVariant that = (Lab2RandomVariant) o;
        return name == that.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }
}
