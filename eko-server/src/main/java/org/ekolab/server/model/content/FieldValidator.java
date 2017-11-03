package org.ekolab.server.model.content;

@FunctionalInterface
public interface FieldValidator<V extends LabVariant, T extends LabData<V>> {
    boolean validate(Object value, T labData);
}
