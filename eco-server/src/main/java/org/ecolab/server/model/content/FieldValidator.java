package org.ecolab.server.model.content;

import org.ecolab.server.model.DomainModel;

@FunctionalInterface
public interface FieldValidator<O, M extends DomainModel> {
    FieldValidationResult validate(O value, M data);
}
