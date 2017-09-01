package org.ekolab.server.model.content;

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
}
