package org.ecolab.client.vaadin.desktop;

import de.codecentric.boot.admin.config.EnableAdminServer;
import it.sauronsoftware.junique.AlreadyLockedException;
import it.sauronsoftware.junique.JUnique;
import org.apache.commons.lang.UnhandledException;
import org.ecolab.client.vaadin.server.EcoLabVaadinProperties;
import org.ecolab.client.vaadin.server.VaadinApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Desktop;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URI;
import java.util.ResourceBundle;

@SpringBootApplication
@EnableAdminServer
@EnableConfigurationProperties(EcoLabVaadinProperties.class)
public class DesktopVaadinApplication extends VaadinApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopVaadinApplication.class);

    private static final ResourceBundle RES = ResourceBundle.getBundle("messages-vaadin-desktop");

    private ApplicationContext ctx;

    // ---------------------------- Графические компоненты --------------------
    private final MenuItem openItem = new MenuItem(RES.getString("loading"));
    private final MenuItem aboutItem = new MenuItem(RES.getString("about"));
    private final MenuItem exitItem = new MenuItem(RES.getString("exit"));
    private final PopupMenu popup = new PopupMenu();

    public static void main(String... args) throws Exception {
        new DesktopVaadinApplication().run(args);
    }

    @Override
    protected ApplicationContext run(String... args) throws Exception {
        SplashScreen.setVisible(true);
        try {
            JUnique.acquireLock(DesktopVaadinApplication.class.getName(), message -> {
                if (StringUtils.isEmpty(message)) {
                    openEcoLabInBrowser();
                }
                return null;
            });
        } catch (AlreadyLockedException e) {
            JUnique.sendMessage(DesktopVaadinApplication.class.getName(), null);
            for (var arg : args) {
                JUnique.sendMessage(DesktopVaadinApplication.class.getName(), arg);
            }
            exit();
        }

        if (SystemTray.isSupported()) {
            exitItem.addActionListener(e -> exit());
            openItem.addActionListener(e -> openEcoLabInBrowser());
            openItem.setEnabled(false);
            popup.add(openItem);
            popup.addSeparator();
            popup.add(aboutItem);
            popup.addSeparator();
            popup.add(exitItem);
            try (var icon = SplashScreen.class.getResourceAsStream("icon.svg")) {
                var trayIcon = new TrayIcon(ImageIO.read(icon), RES.getString("ecolab"), popup);
                trayIcon.setImageAutoSize(true);
                SystemTray.getSystemTray().add(trayIcon);
                trayIcon.displayMessage(RES.getString("ecolab"), RES.getString("trayInfo"), TrayIcon.MessageType.INFO);
            }
        }
        try {
            ctx = super.run(args);
            openEcoLabInBrowser();
            if (SystemTray.isSupported()) {
                openItem.setLabel(RES.getString("open"));
                openItem.setEnabled(true);
            }
            return ctx;
        } catch (Exception ex) {
            var msg = new JTextArea("Произошла ошибка: " + ex);
            msg.setLineWrap(true);
            msg.setWrapStyleWord(true);
            msg.setColumns(50);

            var scrollPane = new JScrollPane(msg);
            JOptionPane.showMessageDialog(new JFrame(), scrollPane);
            throw ex;
        } finally {
            SplashScreen.setVisible(false);
        }
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

    private void openEcoLabInBrowser() {
        var serverProperties = ctx.getBean(ServerProperties.class);
        var servletContext = ctx.getBean(ServletContext.class);

        try {
            var appUrl = String.format("http://%s:%s%s", serverProperties.getAddress() == null ? InetAddress.getLocalHost().getCanonicalHostName() : serverProperties.getAddress(),  serverProperties.getPort(), servletContext.getContextPath());

            var desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
            if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                desktop.browse(URI.create(appUrl));
            }
        } catch (IOException e) {
            throw new UnhandledException(e);
        }
    }

    private void exit() {
        Runtime.getRuntime().exit(0);
    }
}