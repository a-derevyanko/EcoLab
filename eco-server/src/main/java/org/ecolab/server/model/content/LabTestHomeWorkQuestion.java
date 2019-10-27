package org.ecolab.server.model.content;

import java.util.Objects;

public class LabTestHomeWorkQuestion extends LabTestQuestionVariant {
    private Class<?> valueType;
    private String dimension;
    private String formulae;

    public Class<?> getValueType() {
        return valueType;
    }

    public void setValueType(Class<?> valueType) {
        this.valueType = valueType;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getFormulae() {
        return formulae;
    }

    public void setFormulae(String formulae) {
        this.formulae = formulae;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LabTestHomeWorkQuestion)) return false;
        if (!super.equals(o)) return false;
        var question = (LabTestHomeWorkQuestion) o;
        return Objects.equals(valueType, question.valueType) &&
                Objects.equals(dimension, question.dimension) &&
                Objects.equals(formulae, question.formulae);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), valueType, dimension, formulae);
    }
}
