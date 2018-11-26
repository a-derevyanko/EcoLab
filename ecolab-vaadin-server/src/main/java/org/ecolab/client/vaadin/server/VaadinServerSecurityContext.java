package org.ecolab.client.vaadin.server;

import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.ecolab.client.vaadin.server.security.VaadinSessionClientContextProvider;
import org.ecolab.server.common.CurrentUser;
import org.ecolab.server.model.ClientContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;
import org.vaadin.spring.security.config.VaadinSharedSecurityConfiguration;
import org.vaadin.spring.security.shared.VaadinSessionClosingLogoutHandler;

/**
 * Created by Андрей on 11.09.2016.
 */
@SpringBootConfiguration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)
@Import(VaadinSharedSecurityConfiguration.class)
public class VaadinServerSecurityContext extends WebSecurityConfigurerAdapter {
    private final AuthenticationProvider authenticationProvider;
    private final RememberMeServices rememberMeServices;
    private final PersistentTokenRepository persistentTokenRepository;

    @Autowired
    public VaadinServerSecurityContext(AuthenticationProvider authenticationProvider, RememberMeServices rememberMeServices,
                                       PersistentTokenRepository persistentTokenRepository) {
        this.authenticationProvider = authenticationProvider;
        this.rememberMeServices = rememberMeServices;
        this.persistentTokenRepository = persistentTokenRepository;
    }
/*
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
        auth.authenticationProvider(authenticationProvider);
    }*/

    private class EcoLabAnonymousFilter extends AnonymousAuthenticationFilter {

        public EcoLabAnonymousFilter(String key) {
            super(key);
        }

        public EcoLabAnonymousFilter(String key, Object principal, List<GrantedAuthority> authorities) {
            super(key, principal, authorities);
        }

        @Override
        protected Authentication createAuthentication(HttpServletRequest request) {
            AnonymousAuthenticationToken authentication = (AnonymousAuthenticationToken) super.createAuthentication(request);
            authentication.setDetails(new ClientContext(null, request.getLocale()));
            return authentication;
        }
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
        CurrentUser.setProvider(VaadinSessionClientContextProvider.INSTANCE);
        http.anonymous().authenticationFilter(new EcoLabAnonymousFilter(UUID.randomUUID().toString())).and().
                headers().frameOptions().disable()
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
