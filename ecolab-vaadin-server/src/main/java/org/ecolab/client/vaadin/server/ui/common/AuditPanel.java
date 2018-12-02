package org.ecolab.client.vaadin.server.ui.common;

import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.event.selection.SelectionListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateTimeField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.LocalDateTimeRenderer;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.stream.Collectors;
import org.aderevyanko.audit.api.AuditEventFilter;
import org.aderevyanko.audit.api.generic.AuditConfigStorage;
import org.ecolab.client.vaadin.server.dataprovider.AuditDataProvider;
import org.ecolab.client.vaadin.server.service.impl.I18N;
import org.ecolab.client.vaadin.server.ui.styles.EcoLabTheme;
import org.ecolab.client.vaadin.server.ui.view.api.View;
import org.ecolab.server.common.EcoLabAuditEventType;
import org.ecolab.server.model.EcoLabAuditEvent;
import org.ecolab.server.model.EcoLabAuditEventHeader;
import org.ecolab.server.service.api.EcoLabEventsStorage;

@SpringView(name = AuditPanel.NAME)
public class AuditPanel extends VerticalLayout implements View {
    public static final String NAME = "audit";

    private final ConfigurableFilterDataProvider<EcoLabAuditEventHeader, Void, AuditEventFilter> auditDataProvider;

    private final EcoLabEventsStorage eventsStorage;

    private final AuditConfigStorage auditConfig;

    private final I18N i18N;

    // ---------------------------- Графические компоненты --------------------
    protected final Grid<EcoLabAuditEventHeader> events = new Grid<>();
    protected final TextArea eventData = new TextArea();
    protected final ComboBox<EcoLabAuditEventType> eventTypeComboBox = new ComboBox<>();
    protected final DateTimeField fromDateField = new DateTimeField();
    protected final DateTimeField toDateField = new DateTimeField();
    protected final Button clearFind = new Button(VaadinIcons.CLOSE);
    protected final Button find = new Button(VaadinIcons.SEARCH);
    protected final HorizontalLayout eventsPanel = new HorizontalLayout(events, eventData);
    protected final HorizontalLayout findPanel = new HorizontalLayout(eventTypeComboBox, fromDateField, toDateField, find, clearFind);

    public AuditPanel(AuditDataProvider auditDataProvider, EcoLabEventsStorage eventsStorage, AuditConfigStorage auditConfig, I18N i18N) {
        this.auditDataProvider = auditDataProvider.withConfigurableFilter();
        this.eventsStorage = eventsStorage;
        this.auditConfig = auditConfig;
        this.i18N = i18N;
    }


    @Override
    public void init() throws Exception {
        View.super.init();
        setMargin(true);
        setSpacing(true);
        setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        addComponents(findPanel, eventsPanel);

        eventsPanel.setSizeFull();
        eventsPanel.setExpandRatio(events, 3.0F);
        eventsPanel.setExpandRatio(eventData, 1.0F);

        events.setCaption(i18N.get("admin-manage.system.audit.events"));
        events.setSizeFull();
        eventData.setCaption(i18N.get("admin-manage.system.audit.data"));
        eventData.setSizeFull();
        eventData.setEnabled(false);

        findPanel.setComponentAlignment(eventTypeComboBox, Alignment.MIDDLE_CENTER);
        findPanel.setComponentAlignment(fromDateField, Alignment.MIDDLE_CENTER);
        findPanel.setComponentAlignment(toDateField, Alignment.MIDDLE_CENTER);
        findPanel.setComponentAlignment(find, Alignment.BOTTOM_CENTER);
        findPanel.setComponentAlignment(clearFind, Alignment.BOTTOM_CENTER);

        eventTypeComboBox.setCaption(i18N.get("admin-manage.system.audit.type"));
        fromDateField.setCaption(i18N.get("admin-manage.system.audit.date-from"));
        toDateField.setCaption(i18N.get("admin-manage.system.audit.date-to"));

        fromDateField.setTextFieldEnabled(false);
        toDateField.setTextFieldEnabled(false);

        eventTypeComboBox.setItems(EcoLabAuditEventType.values());
        eventTypeComboBox.setItemCaptionGenerator(item -> auditConfig.getEventNames().get(item.getSystemName()));

        find.setDescription(i18N.get("admin-manage.users.find"));
        find.setStyleName(EcoLabTheme.BUTTON_PRIMARY);
        find.addClickListener((Button.ClickListener) event -> {
            searchEvents();
        });

        clearFind.setDescription(i18N.get("admin-manage.users.clear-find"));
        clearFind.setStyleName(EcoLabTheme.BUTTON_PRIMARY);
        clearFind.addClickListener((Button.ClickListener) event -> {
            clearFilter();
            searchEvents();
        });

        findPanel.setSpacing(true);
        findPanel.setMargin(true);

        eventsPanel.setSizeFull();
        events.setSelectionMode(Grid.SelectionMode.SINGLE);

        clearFilter();
        setFilter();
        events.setDataProvider(auditDataProvider);

        events.addSelectionListener((SelectionListener<EcoLabAuditEventHeader>) event -> {
            boolean hasSelection = event.getFirstSelectedItem().isPresent();
            if (hasSelection) {
                EcoLabAuditEvent e = eventsStorage.getEvent(event.getFirstSelectedItem().orElseThrow(IllegalStateException::new).getId());
                eventData.setValue(e.getAttributes().entrySet().stream().map(e1 ->
                        auditConfig.getEventAttributeNames().get(e1.getKey().getSystemName())
                                + " = " + e1.getValue()).
                        collect(Collectors.joining("\n")));
            } else {
                eventData.clear();
            }
        });

        events.addColumn(ecoLabAuditEventHeader -> auditConfig.getEventNames().get(ecoLabAuditEventHeader.getEventType().getSystemName())).setCaption(i18N.get("admin-manage.system.audit.type")).setExpandRatio(1);
        events.addColumn(EcoLabAuditEventHeader::getEventDate, new LocalDateTimeRenderer()).setCaption(i18N.get("admin-manage.system.audit.date")).setExpandRatio(1);
/*
        printUserLogins.setCaption(i18N.get("admin-manage.users.print-table"));
        printUserLogins.setStyleName(EcoLabTheme.BUTTON_PRIMARY);
        printUserLogins.setWidth(300.0F, Unit.PIXELS);

        dataProvider.setFilter(filter);

        new BrowserWindowOpener(new DownloadStreamResource(
                () -> userInfoService.printUsersData(dataProvider.fetch(new Query<>(null))),
                "users.pdf")).extend(printUserLogins);*/
    }

    private void setFilter() {
        AuditEventFilter filter = AuditEventFilter.create(fromDateField.getValue(), toDateField.getValue());
        auditDataProvider.setFilter(filter);
    }
    private void clearFilter() {
        fromDateField.setValue(LocalDateTime.now().minus(Period.ofDays(1)));
        toDateField.setValue(LocalDateTime.now());
    }


    private void searchEvents() {
        if (!toDateField.getValue().isAfter(fromDateField.getValue())) {
            Notification.show(i18N.get("admin-manage.system.audit.wrong-date"), Notification.Type.ERROR_MESSAGE);
        }
        setFilter();
        events.getDataProvider().refreshAll();
    }
}
