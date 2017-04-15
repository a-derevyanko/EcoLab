package org.ekolab.client.vaadin.server;

import com.vaadin.server.VaadinServlet;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.vaadin.spring.security.config.VaadinSharedSecurityConfiguration;

/**
 * Created by Андрей on 11.09.2016.
 */
@SpringBootConfiguration
@Import(VaadinSharedSecurityConfiguration.class)
public class VaadinUIConfiguration {
    /**
     * Инициализация сервлета Vaadin Servlet.
     */
    @Bean
    public VaadinServlet vaadinServlet(MessageSource messageSource) {
        return new CustomizedVaadinServlet(messageSource);
    }
}
