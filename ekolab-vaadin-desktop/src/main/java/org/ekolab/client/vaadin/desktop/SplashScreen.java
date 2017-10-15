package org.ekolab.client.vaadin.desktop;

import org.apache.commons.lang.UnhandledException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

public class SplashScreen {
    private static final JFrame INSTANCE = new JFrame();

    static {
        try (InputStream logo = SplashScreen.class.getResourceAsStream("splashscreen.svg"); InputStream icon = SplashScreen.class.getResourceAsStream("icon.svg")) {
            JLabel label = new JLabel();
            label.setIcon(new ImageIcon(ImageIO.read(logo)));
            INSTANCE.setPreferredSize(new Dimension(500, 200));
            INSTANCE.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            INSTANCE.setUndecorated(true);
            INSTANCE.pack();
            INSTANCE.setLocationRelativeTo(null);
            INSTANCE.setContentPane(label);
            INSTANCE.setIconImage(ImageIO.read(icon));
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        } catch (IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException | ClassNotFoundException ex) {
            throw new UnhandledException(ex);
        }
    }

    public static void setVisible(boolean visible) {
        INSTANCE.setVisible(visible);
    }
}
