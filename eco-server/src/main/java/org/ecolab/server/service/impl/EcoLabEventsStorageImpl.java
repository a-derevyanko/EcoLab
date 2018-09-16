package org.ecolab.server.service.impl;

import org.aderevyanko.audit.api.AuditEventAttribute;
import org.aderevyanko.audit.api.AuditEventAttributeProvider;
import org.aderevyanko.audit.api.AuditEventFilter;
import org.aderevyanko.audit.api.AuditEventTypeProvider;
import org.aderevyanko.audit.api.generic.GenericEventsStorage;
import org.ecolab.server.model.EcoLabAuditEvent;
import org.ecolab.server.model.EcoLabAuditEventHeader;
import org.jooq.BatchBindStep;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.ecolab.server.db.h2.public_.Tables.AUDIT;
import static org.ecolab.server.db.h2.public_.tables.AuditAttributes.AUDIT_ATTRIBUTES;
import static org.ecolab.server.db.h2.public_.tables.Users.USERS;

@Service
public class EcoLabEventsStorageImpl implements GenericEventsStorage<EcoLabAuditEventHeader, EcoLabAuditEvent, AuditEventFilter> {
    private static final Logger LOGGER = LoggerFactory.getLogger(EcoLabEventsStorageImpl.class);

    private final AuditEventTypeProvider eventTypeProvider;
    private final AuditEventAttributeProvider attributeProvider;

    private final DSLContext dsl;

    public EcoLabEventsStorageImpl(DSLContext dsl, AuditEventTypeProvider eventTypeProvider, AuditEventAttributeProvider attributeProvider) {
        this.dsl = dsl;
        this.eventTypeProvider = eventTypeProvider;
        this.attributeProvider = attributeProvider;
    }

    @Override
    @Async
    public void saveEvents(Collection<EcoLabAuditEvent> events) {
        try {
            dsl.transaction(configuration -> {
                //todo не удаётся сохранить batch, jooq пока этого не умеет https://github.com/jOOQ/jOOQ/issues/3327
                BatchBindStep attributesBatch = null;
                for (EcoLabAuditEvent event : events) {
                    long id = dsl.insertInto(AUDIT, AUDIT.CREATE_DATE, AUDIT.EVENT_TYPE, AUDIT.USER_ID)
                            .values(event.getEventDate(),
                                    eventTypeProvider.getId(event.getEventType()),
                                    event.getUserId())
                            .returning(AUDIT.ID).fetchOne().getId();

                    for (Map.Entry<AuditEventAttribute, String> entry : event.getAttributes().entrySet()) {
                        if (attributesBatch == null) {
                            attributesBatch = dsl.batch(dsl.insertInto(AUDIT_ATTRIBUTES, AUDIT_ATTRIBUTES.AUDIT_ID,
                                    AUDIT_ATTRIBUTES.AUDIT_ATTRIBUTE_ID, AUDIT_ATTRIBUTES.VALUE).
                                    values((Long) null, null, null));
                        }
                        attributesBatch = attributesBatch.bind(id, attributeProvider.getId(entry.getKey()), entry.getValue());
                    }
                }
                if (attributesBatch != null) {
                    attributesBatch.execute();
                }
            });
        } catch (Exception ex) {
            LOGGER.error(ex.getLocalizedMessage(), ex);
        }
    }

    @Override
    public Set<EcoLabAuditEventHeader> getHeaders(AuditEventFilter filter) {
        Collection<Condition> conditions = new HashSet<>();
        conditions.add(AUDIT.CREATE_DATE.between(filter.getStartDate(), filter.getEndDate()));

        if (!CollectionUtils.isEmpty(filter.getEventTypes())) {
            conditions.add(AUDIT.EVENT_TYPE.in(filter.getEventTypes().stream().map(eventTypeProvider::getId).collect(Collectors.toList())));
        }

        return new HashSet<>(dsl.select(USERS.LOGIN, AUDIT.EVENT_TYPE, AUDIT.ID, AUDIT.CREATE_DATE).
                from(AUDIT.join(USERS).on(AUDIT.USER_ID.eq(USERS.ID))).
                where(conditions).fetch(record -> {
                    EcoLabAuditEventHeader header = new EcoLabAuditEventHeader();
                    header.setId(record.get(AUDIT.ID));
                    header.setEventDate(record.get(AUDIT.CREATE_DATE));
                    header.setEventType(eventTypeProvider.getById(record.get(AUDIT.EVENT_TYPE)));
                    header.setInitiator(record.get(USERS.LOGIN));
                    return header;
                }));
    }


    @Override
    public Set<EcoLabAuditEvent> getEvents(AuditEventFilter filter) {
        return null;
    }

    @Override
    public EcoLabAuditEvent getEvent(long id) {
        return null;
    }
}
