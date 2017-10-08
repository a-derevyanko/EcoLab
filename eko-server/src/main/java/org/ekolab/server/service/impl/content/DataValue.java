package org.ekolab.server.service.impl.content;

import org.ekolab.server.model.DomainModel;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class DataValue implements DomainModel {
    @NotNull
    private String name;

    private Object value;

    private String sign;

    private String dimension;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataValue)) return false;
        DataValue dataValue = (DataValue) o;
        return Objects.equals(name, dataValue.name) &&
                Objects.equals(value, dataValue.value) &&
                Objects.equals(sign, dataValue.sign) &&
                Objects.equals(dimension, dataValue.dimension);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value, sign, dimension);
    }
}
