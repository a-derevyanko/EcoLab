package org.ecolab.server.dao.api;

public interface SettingsDao {
    <T> T getSetting(String valueName);
}