package org.ecolab.server;

import org.ecolab.server.service.api.ConfigurationCommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication(scanBasePackageClasses = ServerApplication.class)
@EnableWebSecurity
@EnableWebMvc
@EnableCaching
public class ServerApplication extends SpringBootServletInitializer implements WebApplicationInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerApplication.class);

    /**
     * Конфигурация, которая будет использоваться при запуске из командной строки в Embedded контейнере
     * @param args аргументы запуска
     */
    public static void main(String... args) throws Exception {
        new ServerApplication().run(args);
    }

    protected ApplicationContext run(String... args) throws Exception {
        ApplicationContext ctx = configure(createSpringApplicationBuilder()).run(args); // Запуск приложения
        ctx.getBean(ConfigurationCommonService.class).printApplicationBeanNames(); // Печать имён бинов
        return ctx;
    }

    /**
     * Конфигурация, которая будет использоваться при деплое приложения в контейнер.
     * @param builder билдер
     * @return сконфигурированное приложение
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ServerApplication.class).initializers(initializers()).bannerMode(Banner.Mode.OFF);
    }

    protected ApplicationContextInitializer<?>[] initializers() {
        return new ApplicationContextInitializer[0];
    }
}