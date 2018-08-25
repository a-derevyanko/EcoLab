package org.ecolab.server.dao.impl.h2;

import org.apache.commons.lang.UnhandledException;
import org.ecolab.server.common.Profiles;
import org.ecolab.server.dao.api.AdminDao;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
@Profile({Profiles.DB.H2})
public class AdminDaoH2Impl implements AdminDao {
    private static final String BACKUP_FILE_NAME = "backup.zip";

    private final DSLContext dsl;

    public AdminDaoH2Impl(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public byte[] getBackup() {
        dsl.execute("BACKUP TO '" + BACKUP_FILE_NAME + "'");
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
}
