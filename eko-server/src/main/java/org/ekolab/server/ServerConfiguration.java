package org.ekolab.server;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.StandardChartTheme;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import java.awt.*;

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
    private void createDefaultChartTheme() {
        StandardChartTheme chartTheme = new StandardChartTheme("EkoLab");

        chartTheme.setChartBackgroundPaint(Color.WHITE);
        chartTheme.setPlotBackgroundPaint(Color.WHITE);
        chartTheme.setPlotOutlinePaint(Color.WHITE);
        chartTheme.setDomainGridlinePaint(Color.DARK_GRAY);
        chartTheme.setRangeGridlinePaint(Color.DARK_GRAY);

        ChartFactory.setChartTheme(chartTheme);
    }

    @EventListener(ApplicationReadyEvent.class)
    private void installAuthenticationManager() {
        userDetailsManager.setAuthenticationManager(authenticationManager);
    }

}
