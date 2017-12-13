package org.ekolab.server.service.api;

public interface SettingsService {
    <T> T getSetting(String valueName);
}