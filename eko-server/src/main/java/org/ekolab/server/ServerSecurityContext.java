package org.ekolab.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

/**
 * Created by Андрей on 19.11.2016.
 */
@Configuration
public class ServerSecurityContext {
    @Bean
    public AuthenticationProvider authenticationProvider(UserCache userCache,
                                                         PasswordEncoder passwordEncoder,
                                                         UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        authenticationProvider.setUserCache(userCache);
        return authenticationProvider;
    }

    @Bean
    public RememberMeServices rememberMeServices(UserDetailsService userDetailsService,
                                                 PersistentTokenRepository tokenRepository) {
        return new PersistentTokenBasedRememberMeServices(AbstractRememberMeServices.DEFAULT_PARAMETER,
                userDetailsService, tokenRepository);
    }
}
