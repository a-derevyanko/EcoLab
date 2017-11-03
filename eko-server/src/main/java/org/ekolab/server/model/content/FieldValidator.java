package org.ekolab.server.model.content;

@FunctionalInterface
public interface FieldValidator<O extends Object, V extends LabVariant, T extends LabData<V>> {
    boolean validate(O value, T labData);
}
