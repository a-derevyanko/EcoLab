package org.ekolab.client.vaadin.server;

import de.codecentric.boot.admin.config.EnableAdminServer;
import org.ekolab.server.ServerApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.vaadin.spring.security.config.VaadinSharedSecurityConfiguration;

@SpringBootApplication/*(scanBasePackageClasses = ServerApplication.class)*/
@Import(VaadinSharedSecurityConfiguration.class)
@EnableAdminServer
public class VaadinApplication extends ServerApplication {
    public static void main(String... args) {
        ApplicationContext ctx = SpringApplication.run(VaadinApplication.class, args);
    }
}