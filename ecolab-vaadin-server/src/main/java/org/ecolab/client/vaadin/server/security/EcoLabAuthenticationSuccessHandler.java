package org.ecolab.client.vaadin.server.security;

import org.ecolab.server.common.EcoLabAuditEventAttribute;
import org.ecolab.server.common.EcoLabAuditEventType;
import org.ecolab.server.model.EcoLabAuditEvent;
import org.ecolab.server.service.api.EcoLabAuditService;
import org.springframework.security.core.Authentication;
import org.vaadin.spring.http.HttpService;
import org.vaadin.spring.security.shared.SavedRequestAwareVaadinAuthenticationSuccessHandler;
import org.vaadin.spring.security.web.VaadinRedirectStrategy;

public class EcoLabAuthenticationSuccessHandler extends SavedRequestAwareVaadinAuthenticationSuccessHandler {
    private final EcoLabAuditService auditService;

    public EcoLabAuthenticationSuccessHandler(HttpService http,
                                              VaadinRedirectStrategy redirectStrategy, String defaultTargetUrl,
                                              EcoLabAuditService auditService) {
        super(http, redirectStrategy, defaultTargetUrl);
        this.auditService = auditService;
    }

    @Override
    public void onAuthenticationSuccess(Authentication authentication) throws Exception {
        super.onAuthenticationSuccess(authentication);
        auditService.log(EcoLabAuditEvent.ofType(EcoLabAuditEventType.LOGIN)
                .attribute(EcoLabAuditEventAttribute.USER_NAME, authentication.getName()));
    }
}
