package org.ecolab.client.vaadin.server.dataprovider;

import com.vaadin.data.provider.AbstractBackEndDataProvider;
import com.vaadin.data.provider.Query;
import java.util.stream.Stream;
import org.aderevyanko.audit.api.AuditEventFilter;
import org.ecolab.server.model.EcoLabAuditEventHeader;
import org.ecolab.server.service.api.EcoLabEventsStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AuditDataProvider extends AbstractBackEndDataProvider<EcoLabAuditEventHeader, AuditEventFilter> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuditDataProvider.class);

    private final EcoLabEventsStorage eventsStorage;

    protected AuditDataProvider(EcoLabEventsStorage eventsStorage) {
        this.eventsStorage = eventsStorage;
    }

    @Override
    public Long getId(EcoLabAuditEventHeader item) {
        return item.getId();
    }

    @Override
    protected Stream<EcoLabAuditEventHeader> fetchFromBackEnd(Query<EcoLabAuditEventHeader, AuditEventFilter> query) {
        return eventsStorage.getHeaders(query.getFilter().orElseThrow(IllegalArgumentException::new)).stream();
    }

    @Override
    protected int sizeInBackEnd(Query<EcoLabAuditEventHeader, AuditEventFilter> query) {
        return eventsStorage.getHeadersSize(query.getFilter().orElseThrow(IllegalArgumentException::new));
    }
}
