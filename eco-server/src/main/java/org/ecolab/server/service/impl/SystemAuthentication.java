package org.ecolab.server.service.impl;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.security.Principal;

final class SystemAuthentication extends PreAuthenticatedAuthenticationToken {
    static final SystemAuthentication INSTANCE = new SystemAuthentication();
    static final SecurityContext SECURITY_CONTEXT_INSTANCE = new SecurityContextImpl();

    static {
        SECURITY_CONTEXT_INSTANCE.setAuthentication(INSTANCE);
    }

    private SystemAuthentication() {
        super((Principal) () -> "system", null);
        setAuthenticated(true);
    }
}
