package org.aderevyanko.audit.impl;

import org.aderevyanko.audit.api.AuditEventType;
import org.aderevyanko.audit.api.AuditEventTypeProvider;
import org.aderevyanko.audit.api.generic.AuditConfigStorage;

public class EventTypeProviderImpl extends NamedTypeProviderImpl<AuditEventType>
        implements AuditEventTypeProvider {
    protected EventTypeProviderImpl(Class<? extends Enum<?>> enumOfTypesClass, AuditConfigStorage storage) {
        super(enumOfTypesClass, storage.getEventTypes());
    }

    @Override
    protected AuditEventType createStoredType(String name) {
        return new AuditEventAttributeTypeImpl(name);
    }

    private static class AuditEventAttributeTypeImpl implements AuditEventType {
        private final String name;

        private AuditEventAttributeTypeImpl(String name) {
            this.name = name;
        }
        @Override
        public String getSystemName() {
            return name;
        }
    }
}
