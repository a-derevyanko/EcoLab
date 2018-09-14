package org.ecolab.server.service.impl;

import org.ecolab.server.common.EcoLabAuditEventAttribute;
import org.ecolab.server.common.EcoLabAuditEventType;
import org.ecolab.server.model.ClientContext;
import org.ecolab.server.model.EcoLabAuditEvent;
import org.ecolab.server.model.EcoLabUserDetails;
import org.ecolab.server.service.api.EcoLabAuditService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EcoLabDaoAuthenticationProvider extends DaoAuthenticationProvider {
    private final EcoLabAuditService auditService;

    public EcoLabDaoAuthenticationProvider(UserCache userCache,
                                           PasswordEncoder passwordEncoder,
                                           UserDetailsService userDetailsService, EcoLabAuditService auditService) {
        this.auditService = auditService;
        setUserDetailsService(userDetailsService);
        setPasswordEncoder(passwordEncoder);
        setUserCache(userCache);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication successfulAuthentication = super.authenticate(authentication);
        auditService.log(EcoLabAuditEvent.ofType(EcoLabAuditEventType.LOGIN)
                .attribute(EcoLabAuditEventAttribute.USER_NAME, authentication.getName()));
        return successfulAuthentication;
    }

    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        UsernamePasswordAuthenticationToken result = (UsernamePasswordAuthenticationToken) super.createSuccessAuthentication(principal, authentication, user);
        result.setDetails(new ClientContext(((EcoLabUserDetails)user).getId(), ((EcoLabUserDetails)user).getLocale()));
        return result;
    }
}
