package org.ecolab.client.vaadin.server.security;

import org.ecolab.server.common.EcoLabAuditEventType;
import org.ecolab.server.model.EcoLabAuditEvent;
import org.ecolab.server.service.api.EcoLabAuditService;
import org.vaadin.spring.security.shared.VaadinRedirectLogoutHandler;
import org.vaadin.spring.security.web.VaadinRedirectStrategy;

public class EcoLabLogoutHandler extends VaadinRedirectLogoutHandler {
    private final EcoLabAuditService auditService;

    public EcoLabLogoutHandler(EcoLabAuditService auditService, VaadinRedirectStrategy redirectStrategy) {
        super(redirectStrategy);
        this.auditService = auditService;
    }

    @Override
    public void onLogout() {
        EcoLabAuditEvent logoutAudit = EcoLabAuditEvent.ofType(EcoLabAuditEventType.LOGOUT);
        super.onLogout();
        auditService.log(logoutAudit);
    }
}
