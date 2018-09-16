package org.ecolab.client.vaadin.server;

import org.ecolab.client.vaadin.server.security.EcoLabAuthenticationSuccessHandler;
import org.ecolab.client.vaadin.server.security.EcoLabLogoutHandler;
import org.ecolab.server.service.api.EcoLabAuditService;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.vaadin.spring.http.HttpService;
import org.vaadin.spring.security.config.VaadinSharedSecurityConfiguration;
import org.vaadin.spring.security.shared.VaadinAuthenticationSuccessHandler;
import org.vaadin.spring.security.shared.VaadinLogoutHandler;
import org.vaadin.spring.security.web.VaadinRedirectStrategy;

@SpringBootConfiguration
public class EcoLabVaadinSharedSecurityConfiguration extends VaadinSharedSecurityConfiguration {

    @Bean(name = VAADIN_LOGOUT_HANDLER_BEAN)
    public VaadinLogoutHandler vaadinLogoutHandler(EcoLabAuditService auditService, VaadinRedirectStrategy vaadinRedirectStrategy) {
        return new EcoLabLogoutHandler(auditService, vaadinRedirectStrategy);
    }

    @Bean(name = VAADIN_AUTHENTICATION_SUCCESS_HANDLER_BEAN)
    public VaadinAuthenticationSuccessHandler vaadinAuthenticationSuccessHandler(EcoLabAuditService auditService,
                                                                                 HttpService httpService,
                                                                                 VaadinRedirectStrategy vaadinRedirectStrategy) {
        return new EcoLabAuthenticationSuccessHandler(httpService, vaadinRedirectStrategy, "/", auditService);
    }
}
