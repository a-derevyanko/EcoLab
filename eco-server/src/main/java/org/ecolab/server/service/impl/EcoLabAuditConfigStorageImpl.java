package org.ecolab.server.service.impl;

import com.google.common.collect.Sets;
import org.aderevyanko.audit.api.generic.AuditConfigStorage;
import org.jooq.DSLContext;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

import static org.ecolab.server.db.h2.public_.Tables.AUDIT_EVENT_ATTRIBUTE;
import static org.ecolab.server.db.h2.public_.Tables.AUDIT_EVENT_TYPE;

@Service
public class EcoLabAuditConfigStorageImpl implements AuditConfigStorage {

    private final DSLContext dsl;

    public EcoLabAuditConfigStorageImpl(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    @Cacheable("EVENT_NAMES")
    public Map<String, String> getEventNames() {
        return dsl.selectFrom(AUDIT_EVENT_TYPE).fetchMap(AUDIT_EVENT_TYPE.SYSTEMNAME, AUDIT_EVENT_TYPE.NAME);
    }
    @Override
    @Cacheable("EVENT_TYPES")
    public Map<Long, String> getEventTypes() {
        return dsl.selectFrom(AUDIT_EVENT_TYPE).fetchMap(AUDIT_EVENT_TYPE.ID, AUDIT_EVENT_TYPE.SYSTEMNAME);
    }

    @Override
    @Cacheable("EVENT_ATTRIBUTE_NAMES")
    public Map<String, String> getEventAttributeNames() {
        return dsl.selectFrom(AUDIT_EVENT_ATTRIBUTE).fetchMap(AUDIT_EVENT_ATTRIBUTE.SYSTEMNAME, AUDIT_EVENT_ATTRIBUTE.NAME);
    }

    @Override
    @Cacheable("EVENT_ATTRIBUTES")
    public Map<Long, String> getEventAttributeTypes() {
        return dsl.selectFrom(AUDIT_EVENT_ATTRIBUTE).fetchMap(AUDIT_EVENT_ATTRIBUTE.ID, AUDIT_EVENT_ATTRIBUTE.SYSTEMNAME);
    }

    @Override
    @Cacheable("AUDIT_EVENTS")
    public Set<String> getLoggableEvents() {
        return Sets.newHashSet(dsl.select(AUDIT_EVENT_TYPE.SYSTEMNAME).from(AUDIT_EVENT_TYPE)
                .where(AUDIT_EVENT_TYPE.IS_LOGGABLE.isTrue()).fetchInto(String.class));
    }
}
