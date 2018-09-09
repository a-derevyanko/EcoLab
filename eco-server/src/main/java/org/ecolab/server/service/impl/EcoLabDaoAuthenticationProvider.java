package org.ecolab.server.service.impl;

import org.aderevyanko.audit.api.AuditEvent;
import org.aderevyanko.audit.api.AuditService;
import org.ecolab.server.common.EcoLabAuditEventAttribute;
import org.ecolab.server.common.EcoLabAuditEventType;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EcoLabDaoAuthenticationProvider extends DaoAuthenticationProvider {
    private final AuditService auditService;

    public EcoLabDaoAuthenticationProvider(UserCache userCache,
                                           PasswordEncoder passwordEncoder,
                                           UserDetailsService userDetailsService, AuditService auditService) {
        this.auditService = auditService;
        setUserDetailsService(userDetailsService);
        setPasswordEncoder(passwordEncoder);
        setUserCache(userCache);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication successfulAuthentication = super.authenticate(authentication);
        auditService.log(AuditEvent.forType(EcoLabAuditEventType.LOGIN)
                .setAttribute(EcoLabAuditEventAttribute.USER_NAME, authentication.getName()));
        return successfulAuthentication;
    }
}
