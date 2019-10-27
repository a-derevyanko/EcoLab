package org.ecolab.client.vaadin.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;
import org.vaadin.spring.security.annotation.EnableVaadinSharedSecurity;
import org.vaadin.spring.security.config.VaadinSharedSecurityConfiguration;
import org.vaadin.spring.security.shared.VaadinSessionClosingLogoutHandler;

/**
 * Created by Андрей on 11.09.2016.
 */
@SpringBootConfiguration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)
@EnableVaadinSharedSecurity
@Import(VaadinSharedSecurityConfiguration.class)
public class VaadinServerSecurityContext extends WebSecurityConfigurerAdapter {
    private final AuthenticationProvider authenticationProvider;
    private final RememberMeServices rememberMeServices;
    private final PersistentTokenRepository persistentTokenRepository;
    private final JdbcUserDetailsManager userDetailsManager;


    @Autowired
    public VaadinServerSecurityContext(AuthenticationProvider authenticationProvider,
                                       RememberMeServices rememberMeServices,
                                       PersistentTokenRepository persistentTokenRepository,
                                       JdbcUserDetailsManager userDetailsManager) {
        this.authenticationProvider = authenticationProvider;
        this.rememberMeServices = rememberMeServices;
        this.persistentTokenRepository = persistentTokenRepository;
        this.userDetailsManager = userDetailsManager;
    }
/*
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
        auth.authenticationProvider(authenticationProvider);
    }*/

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
        http.headers().frameOptions().disable()
                .and()
                .authorizeRequests().anyRequest().permitAll()
                .and()
                .logout().permitAll().addLogoutHandler(new VaadinSessionClosingLogoutHandler()).logoutSuccessUrl("/")
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
        var authenticationManager = super.authenticationManagerBean();
        userDetailsManager.setAuthenticationManager(authenticationManager);
        return authenticationManager;
    }

    /**
     * The {@link SessionAuthenticationStrategy} must be available as a Spring bean for Vaadin4Spring.
     */
    @Bean
    public SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new SessionFixationProtectionStrategy();
    }
}
