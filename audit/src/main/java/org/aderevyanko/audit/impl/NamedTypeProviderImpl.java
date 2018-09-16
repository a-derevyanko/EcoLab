package org.aderevyanko.audit.impl;

import org.aderevyanko.audit.api.NamedType;
import org.aderevyanko.audit.api.NamedTypeProvider;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

abstract class NamedTypeProviderImpl<T extends NamedType> implements NamedTypeProvider<T> {
    private final Map<Long, T> eventTypes = new HashMap<>();

    NamedTypeProviderImpl(Class<? extends Enum<?>> enumOfTypesClass, Map<Long, String> storedEventTypes) {
        if (!NamedType.class.isAssignableFrom(enumOfTypesClass)) {
            throw new IllegalArgumentException("Enum must be instance of " + NamedType.class.getSimpleName());
        }

        Set<String> applicationEventTypes = Arrays.stream(enumOfTypesClass.getEnumConstants()).map(Enum::name).collect(Collectors.toSet());

        for (String s : applicationEventTypes) {
            if (!storedEventTypes.containsValue(s)) {
                throw new IllegalArgumentException("Event type '" + s + "' not registered in storage!");
            }
        }

        for (Map.Entry<Long, String> entry : storedEventTypes.entrySet().stream().filter(e -> applicationEventTypes.contains(e.getValue())).collect(Collectors.toList())) {
            eventTypes.put(entry.getKey(), createStoredType(entry.getValue()));
        }
    }

    protected abstract T createStoredType(String name);

    @Override
    public Set<T> getAll() {
        return Collections.unmodifiableSet(new HashSet<>(eventTypes.values()));
    }

    @Override
    public T getById(long id) {
        return eventTypes.get(id);
    }

    @Override
    public long getId(NamedType namedType) {
        for (Map.Entry<Long, T> entry : eventTypes.entrySet()) {
            if (entry.getValue().getSystemName().equals(namedType.getSystemName())) {
              return entry.getKey();
            }
        }
        throw new IllegalArgumentException("Type not exists: " + namedType);
    }

    private static void checkEnumIdUnique(Collection<? extends NamedType> values) {
        values.stream().collect(Collectors.groupingBy(
                Function.identity(), Collectors.counting()))
                .forEach((key, value) -> {
                    if (value > 1) {
                        throw new IllegalArgumentException("Duplicate type: " +
                                key.getClass() + ": " + key.getSystemName());
                    }
                });
    }
}
