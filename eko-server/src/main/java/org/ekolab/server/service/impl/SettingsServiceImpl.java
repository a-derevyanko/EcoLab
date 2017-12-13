package org.ekolab.server.service.impl;

import org.ekolab.server.dao.api.SettingsDao;
import org.ekolab.server.service.api.SettingsService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SettingsServiceImpl implements SettingsService {
    private final SettingsDao settingsDao;

    public SettingsServiceImpl(SettingsDao settingsDao) {
        this.settingsDao = settingsDao;
    }

    @Override
    @Cacheable("SETTINGS_CACHE")
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public <T> T getSetting(String valueName) {
        return settingsDao.getSetting(valueName);
    }

}