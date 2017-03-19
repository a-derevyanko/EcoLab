package org.ekolab.client.vaadin.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;
import org.vaadin.spring.security.annotation.EnableVaadinSharedSecurity;
import org.vaadin.spring.security.shared.VaadinSessionClosingLogoutHandler;

/**
 * Created by Андрей on 11.09.2016.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, proxyTargetClass = true)
@EnableVaadinSharedSecurity
public class VaadinServerSecurityContext extends WebSecurityConfigurerAdapter {
    private final AuthenticationProvider authenticationProvider;
    private final RememberMeServices rememberMeServices;
    private final PersistentTokenRepository persistentTokenRepository;

    @Autowired
    public VaadinServerSecurityContext(AuthenticationProvider authenticationProvider,
                                       RememberMeServices rememberMeServices,
                                       PersistentTokenRepository persistentTokenRepository) {
        this.authenticationProvider = authenticationProvider;
        this.rememberMeServices = rememberMeServices;
        this.persistentTokenRepository = persistentTokenRepository;
    }

    /*@Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
        auth.authenticationProvider(authenticationProvider);
    }*/

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable()
                .and()
                .authorizeRequests().anyRequest().permitAll()
                .and()
                .logout().permitAll().addLogoutHandler(new VaadinSessionClosingLogoutHandler())
                .and()
                .csrf().disable().exceptionHandling()
                .and()
                .authenticationProvider(authenticationProvider).rememberMe().tokenRepository(persistentTokenRepository).rememberMeServices(rememberMeServices);
    }

    /**
     * The {@link AuthenticationManager} must be available as a Spring bean for Vaadin4Spring.
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * The {@link SessionAuthenticationStrategy} must be available as a Spring bean for Vaadin4Spring.
     */
    @Bean
    public SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new SessionFixationProtectionStrategy();
    }
}
