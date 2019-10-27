package org.ecolab.server.service.impl;

import org.apache.commons.lang3.time.FastDateFormat;
import org.ecolab.server.dao.api.AdminDao;
import org.ecolab.server.service.api.AdminService;
import org.ecolab.server.service.api.SettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

@Service
public class AdminServiceImpl implements AdminService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminServiceImpl.class);

    private final SettingsService settingsService;

    private final AdminDao adminDao;

    public AdminServiceImpl(SettingsService settingsService, AdminDao adminDao) {
        this.settingsService = settingsService;
        this.adminDao = adminDao;
    }

    @Override
    public byte[] getBackup() {
        return adminDao.getBackup();
    }

    @Override
    @Scheduled(fixedRateString = "${ecolab.server.backupSaveRate:#{18000000}}", initialDelayString = "${ecolab.server.backupSaveRate:#{60000}}")
    public void autoBackupDataBase() {
        String folder = settingsService.getSetting("server.backupPath");

        var path = new File(System.getProperty("user.dir") + File.separator + folder);
        try {
            if (!path.exists() && !path.mkdir()) {
                throw new IllegalStateException("Can`t create folder:" + path);
            }

            var files = path.listFiles();

            int maxBackupFileCount = settingsService.getSetting("server.backupCount");
            if (files != null && files.length > maxBackupFileCount) {
                Arrays.stream(files).sorted(Comparator.comparingLong(File::lastModified)).
                        limit(files.length - maxBackupFileCount - 1).forEach(File::delete);
            }

            Files.write(Paths.get(folder, "backup_" + FastDateFormat.getInstance("dd_MM_yyyy_HH_mm_ss").format(new Date()) + ".zip"), getBackup());
        } catch (IOException e) {
            LOGGER.error(e.getLocalizedMessage(), e);
        }
    }
}
