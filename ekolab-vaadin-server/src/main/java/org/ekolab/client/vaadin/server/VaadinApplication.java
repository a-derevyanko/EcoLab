package org.ekolab.client.vaadin.server;

import de.codecentric.boot.admin.config.EnableAdminServer;
import org.ekolab.server.ServerApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.WebApplicationInitializer;

@SpringBootApplication
@EnableAdminServer
@EnableConfigurationProperties(EkoLabVaadinProperties.class)
public class VaadinApplication extends ServerApplication implements WebApplicationInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(VaadinApplication.class);

    public static void main(String... args) {
        run(new VaadinApplication(), args);
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