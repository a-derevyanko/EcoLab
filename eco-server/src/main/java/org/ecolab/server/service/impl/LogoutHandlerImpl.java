package org.ecolab.server.service.impl;

import org.ecolab.server.common.EcoLabAuditEventType;
import org.ecolab.server.model.EcoLabAuditEvent;
import org.ecolab.server.service.api.EcoLabAuditService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class LogoutHandlerImpl extends SimpleUrlLogoutSuccessHandler {
    private final EcoLabAuditService auditService;

    public LogoutHandlerImpl(EcoLabAuditService auditService) {
        this.auditService = auditService;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        auditService.log(EcoLabAuditEvent.ofType(EcoLabAuditEventType.LOGOUT));
        super.onLogoutSuccess(request, response, authentication);
    }
}
