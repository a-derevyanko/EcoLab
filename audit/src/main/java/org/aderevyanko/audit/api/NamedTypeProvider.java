package org.aderevyanko.audit.api;

import java.util.Set;

public interface NamedTypeProvider<T extends NamedType> {
    Set<T> getAll();

    T getById(long id);

    long getId(T namedType);
}
