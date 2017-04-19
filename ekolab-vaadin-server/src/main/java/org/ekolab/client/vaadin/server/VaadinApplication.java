package org.ekolab.client.vaadin.server;

import de.codecentric.boot.admin.config.EnableAdminServer;
import org.ekolab.server.ServerApplication;
import org.ekolab.server.common.Profiles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;

@SpringBootApplication
@EnableAdminServer
@Profile({Profiles.MODE.PROD, Profiles.MODE.DEV})
public class VaadinApplication extends ServerApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(VaadinApplication.class);

    public static void main(String... args) {
        ApplicationContext ctx = run(args);
        LOGGER.info("-------------------------------------------------------");
        LOGGER.info("|              Vaadin Server metrics                  |");
        LOGGER.info("|                  Beans count: {}                   |", ctx.getBeanDefinitionCount());
        LOGGER.info("-------------------------------------------------------");
    }

    protected static ApplicationContext run(String... args) {
        ApplicationContext ctx = SpringApplication.run(VaadinApplication.class, args);

        LOGGER.info("Let's inspect the beans provided by Spring Boot for Vaadin Server:");

        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            LOGGER.info(beanName);
        }
        return ctx;
    }
}