package org.ekolab.server.model.content;

public class LabTestHomeWorkQuestion extends LabTestQuestion {
    private Class<?> valueType;
    private String formulae;

    public Class<?> getValueType() {
        return valueType;
    }

    public void setValueType(Class<?> valueType) {
        this.valueType = valueType;
    }

    public String getFormulae() {
        return formulae;
    }

    public void setFormulae(String formulae) {
        this.formulae = formulae;
    }
}
