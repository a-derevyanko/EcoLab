package org.ecolab.server.service.impl;

import org.ecolab.server.model.ClientContext;
import org.ecolab.server.model.EcoLabUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EcoLabDaoAuthenticationProvider extends DaoAuthenticationProvider {
    public EcoLabDaoAuthenticationProvider(UserCache userCache,
                                           PasswordEncoder passwordEncoder,
                                           UserDetailsService userDetailsService) {
        setUserDetailsService(userDetailsService);
        setPasswordEncoder(passwordEncoder);
        setUserCache(userCache);
    }

    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        UsernamePasswordAuthenticationToken result = (UsernamePasswordAuthenticationToken) super.createSuccessAuthentication(principal, authentication, user);
        result.setDetails(new ClientContext(((EcoLabUserDetails)user).getId(), ((EcoLabUserDetails)user).getLocale()));
        return result;
    }
}
