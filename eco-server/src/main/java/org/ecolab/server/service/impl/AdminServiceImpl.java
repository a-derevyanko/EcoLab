package org.ecolab.server.service.impl;

import org.apache.commons.lang.UnhandledException;
import org.apache.commons.lang3.SystemUtils;
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

    private static final String BACKUP_FILE_NAME = "backup.zip";

    private final SettingsService settingsService;

    private final AdminDao adminDao;

    public AdminServiceImpl(SettingsService settingsService, AdminDao adminDao) {
        this.settingsService = settingsService;
        this.adminDao = adminDao;
    }

    @Override
    public byte[] getBackup() {
        adminDao.backupTo(BACKUP_FILE_NAME);
        File backup = new File(BACKUP_FILE_NAME);
        if (backup.exists()) {
            try {
                byte[] file = Files.readAllBytes(backup.toPath());
                if (!backup.delete()) {
                    throw new IllegalStateException("Backup not deleted!");
                }
                return file;
            } catch (IOException e) {
                throw new UnhandledException(e);
            }
        } else {
            throw new IllegalStateException("Backup not exists!");
        }
    }

    @Override
    @Scheduled(fixedRateString = "${ecolab.server.backupSaveRate:#{18000000}}", initialDelayString = "${ecolab.server.backupSaveRate:#{60000}}")
    public void autoBackupDataBase() {
        String folder = settingsService.getSetting("server.backupPath");

        File path = new File(SystemUtils.getUserDir().getPath() + File.separator + folder);
        if (!path.exists() && !path.mkdir()) {
            throw new IllegalStateException("Can`t create folder:" + path);
        }

        File[] files = path.listFiles();

        int maxBackupFileCount = settingsService.getSetting("server.backupCount");
        if (files != null && files.length > maxBackupFileCount) {
            Arrays.stream(files).sorted(Comparator.comparingLong(File::lastModified)).
                    limit(files.length - maxBackupFileCount - 1).forEach(File::delete);
        }

        adminDao.backupTo(Paths.get(folder, "backup_" + FastDateFormat.getInstance("dd_MM_yyyy_HH_mm_ss").format(new Date()) + ".zip").toString());
    }
}
