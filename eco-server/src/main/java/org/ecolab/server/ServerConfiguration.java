package org.ecolab.server;

import java.awt.Color;
import org.aderevyanko.audit.api.AuditEventFilter;
import org.aderevyanko.audit.api.generic.AuditConfigStorage;
import org.aderevyanko.audit.api.generic.GenericEventsStorage;
import org.ecolab.server.model.EcoLabAuditEvent;
import org.ecolab.server.model.EcoLabAuditEventHeader;
import org.ecolab.server.service.api.EcoLabAuditService;
import org.ecolab.server.service.impl.EcoLabAuditServiceImpl;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.StandardChartTheme;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

/**
 * Created by 777Al on 24.04.2017.
 */
@SpringBootConfiguration
public class ServerConfiguration {

    private final JdbcUserDetailsManager userDetailsManager;

    private final AuthenticationManager authenticationManager;

    public ServerConfiguration(JdbcUserDetailsManager userDetailsManager, AuthenticationManager authenticationManager) {
        this.userDetailsManager = userDetailsManager;
        this.authenticationManager = authenticationManager;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void createDefaultChartTheme() {
        StandardChartTheme chartTheme = new StandardChartTheme("EcoLab");

        chartTheme.setChartBackgroundPaint(Color.WHITE);
        chartTheme.setPlotBackgroundPaint(Color.WHITE);
        chartTheme.setPlotOutlinePaint(Color.WHITE);
        chartTheme.setDomainGridlinePaint(Color.DARK_GRAY);
        chartTheme.setRangeGridlinePaint(Color.DARK_GRAY);

        ChartFactory.setChartTheme(chartTheme);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void installAuthenticationManager() {
        userDetailsManager.setAuthenticationManager(authenticationManager);
    }

    @Bean
    public EcoLabAuditService ecoLabAuditService(GenericEventsStorage<EcoLabAuditEventHeader,
            EcoLabAuditEvent, AuditEventFilter> storage,
                                                 AuditConfigStorage configStorage) {
        return new EcoLabAuditServiceImpl(storage, configStorage);
    }
}
