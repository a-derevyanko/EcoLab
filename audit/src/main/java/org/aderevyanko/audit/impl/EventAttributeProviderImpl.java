package org.aderevyanko.audit.impl;

import org.aderevyanko.audit.api.AuditEventAttribute;
import org.aderevyanko.audit.api.AuditEventAttributeProvider;
import org.aderevyanko.audit.api.generic.AuditConfigStorage;

public class EventAttributeProviderImpl extends NamedTypeProviderImpl<AuditEventAttribute>
        implements AuditEventAttributeProvider {
    protected EventAttributeProviderImpl(Class<? extends Enum<?>> enumOfTypesClass, AuditConfigStorage storage) {
        super(enumOfTypesClass, storage.getEventAttributeTypes());
    }

    @Override
    protected AuditEventAttribute createStoredType(String name) {
        return new AuditEventAttributeImpl(name);
    }

    private static class AuditEventAttributeImpl implements AuditEventAttribute {
        private final String name;

        private AuditEventAttributeImpl(String name) {
            this.name = name;
        }
        @Override
        public String getSystemName() {
            return name;
        }
    }
}
