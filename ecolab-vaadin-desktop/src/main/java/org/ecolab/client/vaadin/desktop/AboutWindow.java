package org.ecolab.client.vaadin.desktop;

import org.jfree.ui.tabbedui.VerticalLayout;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.MessageSource;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.HeadlessException;
import java.lang.management.ManagementFactory;

public class AboutWindow extends JFrame {
    private final JLabel jvmVersion = new JLabel();
    private final JLabel appVersion = new JLabel();
    private final JLabel appVersion1 = new JLabel();
    private final JLabel appVersion2 = new JLabel();
    private final JLabel appVersion3 = new JLabel();
    private final JLabel appVersion4 = new JLabel();
    private final JPanel content = new JPanel(new VerticalLayout());

    public AboutWindow(BuildProperties buildProperties, MessageSource messageSource) throws HeadlessException {
        jvmVersion.setText("Версия JVM: " + ManagementFactory.getRuntimeMXBean().getVmVersion());
        appVersion1.setText("Версия 1: " + buildProperties.getArtifact());
        appVersion2.setText("Версия 2: " + buildProperties.getGroup());
        appVersion3.setText("Версия 3: " + buildProperties.getName());
        appVersion4.setText("Версия 4: " + buildProperties.getTime());
        appVersion.setText("Версия 5: " + buildProperties.getVersion());

        content.add(jvmVersion);
        content.add(appVersion);
        content.add(appVersion1);
        content.add(appVersion2);
        content.add(appVersion3);
        content.add(appVersion4);
        setContentPane(content);
        setLocationRelativeTo(null);
        pack();
    }
}
