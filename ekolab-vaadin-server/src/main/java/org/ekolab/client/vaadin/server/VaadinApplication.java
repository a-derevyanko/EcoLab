package org.ekolab.client.vaadin.server;

import de.codecentric.boot.admin.config.EnableAdminServer;
import org.ekolab.client.vaadin.server.ui.development.DevUtils;
import org.ekolab.server.ServerApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableAdminServer
@EnableConfigurationProperties(EkoLabVaadinProperties.class)
public class VaadinApplication extends ServerApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(VaadinApplication.class);

    public static void main(String... args) throws Exception {
        new VaadinApplication().run(args);
    }

    @Override
    protected ApplicationContext run(String... args) throws Exception {
        if (true) throw new RuntimeException();
        ApplicationContext ctx = super.run(args);
        DevUtils.setBuildProperties(ctx.getBean(BuildProperties.class));
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