package org.ecolab.client.vaadin.server;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ecolab")
public class EcoLabVaadinProperties {
    private Lab lab = new Lab();

    public Lab getLab() {
        return lab;
    }

    public void setLab(Lab lab) {
        this.lab = lab;
    }

    public class Lab {
        private int autoSaveRate;

        public int getAutoSaveRate() {
            return autoSaveRate;
        }

        public void setAutoSaveRate(int autoSaveRate) {
            this.autoSaveRate = autoSaveRate;
        }
    }
}
