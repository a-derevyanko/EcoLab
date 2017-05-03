package org.ekolab.client.vaadin.server;

import de.codecentric.boot.admin.config.EnableAdminServer;
import org.ekolab.server.ServerApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.WebApplicationInitializer;

import java.util.Arrays;

@SpringBootApplication
@EnableAdminServer
public class VaadinApplication extends ServerApplication implements WebApplicationInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(VaadinApplication.class);

    public static void main(String... args) {
        ApplicationContext ctx = run(args);
        LOGGER.info("-------------------------------------------------------");
        LOGGER.info("|              Vaadin Server metrics                  |");
        LOGGER.info("|                  Beans count: {}                   |", ctx.getBeanDefinitionCount());
        LOGGER.info("-------------------------------------------------------");
    }

    protected static ApplicationContext run(String... args) {
        VaadinApplication vaadinApplication = new VaadinApplication();
        ApplicationContext ctx = vaadinApplication.configure(new SpringApplicationBuilder()).run(args);

        LOGGER.info("Let's inspect the beans provided by Spring Boot for Vaadin Server:");

        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            LOGGER.info(beanName);
        }
        return ctx;
    }

    /**
     * Конфигурация, которая будет использоваться при деплое приложения в контейнер.
     * @param builder билдер
     * @return сконфигурированное приложение
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(VaadinApplication.class).initializers(initializers()).bannerMode(Banner.Mode.OFF);
    }
}