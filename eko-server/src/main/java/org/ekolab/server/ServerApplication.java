package org.ekolab.server;

import org.ekolab.server.service.api.ConfigurationCommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@ComponentScan(basePackageClasses = {ServerApplication.class})
@EnableWebSecurity
@EnableWebMvc
@EnableCaching
public class ServerApplication extends SpringBootServletInitializer implements WebApplicationInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerApplication.class);

    /**
     * Конфигурация, которая будет использоваться при запуске из командной строки в Embedded контейнере
     * @param args аргументы запуска
     */
    public static void main(String... args) {
        run(new ServerApplication(), args);
    }

    protected static void run(ServerApplication application, String... args) {
        application.configure(new SpringApplicationBuilder()). // Конфигурация
                run(args). // Запуск приложения
                getBean(ConfigurationCommonService.class).printApplicationBeanNames(); // Печать имён бинов
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

    protected static ApplicationContextInitializer<?>[] initializers() {
        return new ApplicationContextInitializer[0];
    }
}