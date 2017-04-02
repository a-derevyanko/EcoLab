package org.ekolab.client.vaadin.server;

import de.codecentric.boot.admin.config.EnableAdminServer;
import org.ekolab.server.ServerApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.vaadin.spring.security.config.VaadinSharedSecurityConfiguration;

import java.util.Arrays;

@SpringBootApplication
@Import(VaadinSharedSecurityConfiguration.class)
@EnableAdminServer
public class VaadinApplication extends ServerApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(VaadinApplication.class);

    public static void main(String... args) {
        ApplicationContext ctx = SpringApplication.run(VaadinApplication.class, args);

        LOGGER.info("Let's inspect the beans provided by Spring Boot for Vaadin Client:");

        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            LOGGER.info(beanName);
        }

        LOGGER.info("-------------------------------------------------------");
        LOGGER.info("|              Vaadin Server metrics                  |", beanNames.length);
        LOGGER.info("|                  Beans count: {}                    |", beanNames.length);
        LOGGER.info("-------------------------------------------------------");
    }
}