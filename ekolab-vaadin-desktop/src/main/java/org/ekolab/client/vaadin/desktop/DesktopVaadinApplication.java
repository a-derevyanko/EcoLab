package org.ekolab.client.vaadin.desktop;

import de.codecentric.boot.admin.config.EnableAdminServer;
import org.ekolab.client.vaadin.server.EkoLabVaadinProperties;
import org.ekolab.client.vaadin.server.VaadinApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;

import javax.servlet.ServletContext;
import java.awt.*;
import java.net.InetAddress;
import java.net.URI;

@SpringBootApplication
@EnableAdminServer
@EnableConfigurationProperties(EkoLabVaadinProperties.class)
public class DesktopVaadinApplication extends VaadinApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopVaadinApplication.class);

    public static void main(String... args) throws Exception {
        new DesktopVaadinApplication().run(args);
    }

    @Override
    protected ApplicationContext run(String... args) throws Exception {
        SplashScreen.setVisible(true);
        ApplicationContext ctx = super.run(args);
        ServerProperties serverProperties = ctx.getBean(ServerProperties.class);
        ServletContext servletContext = ctx.getBean(ServletContext.class);

        String appUrl = String.format("http://%s:%s%s", serverProperties.getAddress() == null ? InetAddress.getLocalHost().getCanonicalHostName() : serverProperties.getAddress(),  serverProperties.getPort(), servletContext.getContextPath());

        SplashScreen.setVisible(false);
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            desktop.browse(URI.create(appUrl));
        }
        return ctx;
    }


    @Override
    protected SpringApplicationBuilder createSpringApplicationBuilder() {
        return super.createSpringApplicationBuilder().headless(false);
    }

    /**
     * Конфигурация, которая будет использоваться при деплое приложения в контейнер.
     * @param builder билдер
     * @return сконфигурированное приложение
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(DesktopVaadinApplication.class).initializers(initializers()).bannerMode(Banner.Mode.OFF);
    }


    private static void renderSplashFrame(Graphics2D g, int frame) {
        final String[] comps = {"foo", "bar", "baz"};
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(120,140,200,40);
        g.setPaintMode();
        g.setColor(Color.BLACK);
        g.drawString("Loading "+comps[(frame/5)%3]+"...", 120, 150);
    }
}