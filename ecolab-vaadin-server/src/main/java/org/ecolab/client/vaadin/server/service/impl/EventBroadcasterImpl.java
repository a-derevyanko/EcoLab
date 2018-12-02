package org.ecolab.client.vaadin.server.service.impl;

import com.vaadin.navigator.View;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.ecolab.client.vaadin.server.service.api.EventBroadcaster;
import org.ecolab.client.vaadin.server.service.api.UIEventListener;
import org.ecolab.client.vaadin.server.ui.VaadinUI;
import org.ecolab.server.model.EcoLabAuditEvent;
import org.ecolab.server.service.api.EcoLabAuditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EventBroadcasterImpl implements EventBroadcaster {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventBroadcasterImpl.class);

    private final Set<VaadinUI> listeners = Collections.newSetFromMap(new ConcurrentHashMap<>());

    /** Кэш методов подписчиков в представлениях */
    private final ConcurrentMap<View, List<Method>> viewSubscribersCache = new ConcurrentHashMap<>();

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public EventBroadcasterImpl(EcoLabAuditService auditService) {
        auditService.addEventSubscriber(this::publishInternal);
    }

    @Override
    public void subscribe(VaadinUI ui) {
        listeners.add(ui);
    }

    @Override
    public void unSubscribe(VaadinUI ui) {
        listeners.remove(ui);
    }

    private void publishInternal(EcoLabAuditEvent event) {
        executorService.submit(() -> listeners.stream().filter(ui ->
                event.getEventConsumerId() == null ||
                        ui.getCurrentUserInfo() != null && event.getEventConsumerId().equals(ui.getCurrentUserInfo().getId()))
                .forEach(ui -> executorService.submit(() -> {
                    View currentView = ui.getNavigator().getCurrentView();
                    if (currentView != null) {
                        List<Method> methods = viewSubscribersCache.computeIfAbsent(currentView, view -> {
                            List<Method> subscribers = new ArrayList<>();
                            for (Method m : currentView.getClass().getMethods()) {
                                if (m.isAnnotationPresent(UIEventListener.class)) {
                                    if (m.getParameterTypes().length == 1 && EcoLabAuditEvent.class.isAssignableFrom(m.getParameterTypes()[0])) {
                                        LOGGER.debug("Found listener method [{}] in listener [{}]", m.getName(), currentView.getClass());
                                        subscribers.add(m);
                                    } else {
                                        throw new IllegalArgumentException("Listener method " + m.getName() + " does not have the required signature");
                                    }
                                }
                            }
                            return subscribers;
                        });

                        for (Method m : methods) {
                            ui.access(() -> {
                                try {
                                    m.invoke(currentView, event);
                                } catch (ReflectiveOperationException e) {
                                    LOGGER.error(e.getLocalizedMessage(), e);
                                }
                            });
                        }
                    }
                })));
    }
}