package org.ekolab.server.model.content;

@FunctionalInterface
public interface FieldValidator<O, V extends LabVariant, T extends LabData<V>> {
    FieldValidationResult validate(O value, T labData);
}
