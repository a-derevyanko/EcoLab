package org.ekolab.server.model.content;

import org.ekolab.server.model.DomainModel;

@FunctionalInterface
public interface FieldValidator<O, M extends DomainModel> {
    FieldValidationResult validate(O value, M data);
}
