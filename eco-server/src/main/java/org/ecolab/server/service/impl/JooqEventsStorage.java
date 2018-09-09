package org.ecolab.server.service.impl;

import com.google.common.collect.Sets;
import org.aderevyanko.audit.api.AuditEvent;
import org.aderevyanko.audit.api.AuditEventAttribute;
import org.aderevyanko.audit.api.EventsStorage;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static org.ecolab.server.db.h2.public_.Tables.AUDIT;
import static org.ecolab.server.db.h2.public_.Tables.AUDIT_EVENT_ATTRIBUTE;
import static org.ecolab.server.db.h2.public_.Tables.AUDIT_EVENT_TYPE;
import static org.ecolab.server.db.h2.public_.tables.AuditAttributes.AUDIT_ATTRIBUTES;

@Service
public class JooqEventsStorage implements EventsStorage {
    private static final Logger LOGGER = LoggerFactory.getLogger(JooqEventsStorage.class);

    private final DSLContext dsl;

    public JooqEventsStorage(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    @Async
    public void saveEvents(Collection<AuditEvent> events) {
        try {
            dsl.transaction(configuration -> {
                //todo не удаётся сохранить batch, jooq пока этого не умеет https://github.com/jOOQ/jOOQ/issues/3327
                BatchBindStep attributesBatch = dsl.batch(dsl.insertInto(AUDIT_ATTRIBUTES, AUDIT_ATTRIBUTES.AUDIT_ID,
                        AUDIT_ATTRIBUTES.AUDIT_ATTRIBUTE_ID, AUDIT_ATTRIBUTES.VALUE).
                        values((Long) null, null, null));
                for (AuditEvent event : events) {
                    long id = dsl.insertInto(AUDIT, AUDIT.CREATE_DATE, AUDIT.EVENT_TYPE)
                            .values(event.getEventDate(), event.getEventType().getId())
                            .returning(AUDIT.ID).fetchOne().getId();

                    for (Map.Entry<AuditEventAttribute, String> entry : event.getAttributes().entrySet()) {
                        attributesBatch = attributesBatch.bind(id, entry.getKey().getId(), entry.getValue());
                    }
                }
                attributesBatch.execute();
            });
        } catch (Exception ex) {
            LOGGER.error(ex.getLocalizedMessage(), ex);
        }
    }

    @Override
    public Map<Long, String> getEventTypes() {
        return dsl.selectFrom(AUDIT_EVENT_TYPE).fetchMap(AUDIT_EVENT_TYPE.ID, AUDIT_EVENT_TYPE.SYSTEMNAME);
    }

    @Override
    public Map<Long, String> getEventAttributeTypes() {
        return dsl.selectFrom(AUDIT_EVENT_ATTRIBUTE).fetchMap(AUDIT_EVENT_ATTRIBUTE.ID, AUDIT_EVENT_ATTRIBUTE.SYSTEMNAME);
    }

    @Override
    @Cacheable("AUDIT_EVENTS")
    public Set<Long> getLoggableEvents() {
        return Sets.newHashSet(dsl.select(AUDIT_EVENT_TYPE.ID).from(AUDIT_EVENT_TYPE)
                .where(AUDIT_EVENT_TYPE.IS_LOGGABLE.isTrue()).fetchInto(Long.class));
    }
}
