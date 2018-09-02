package org.ecolab.server.service.api;

public interface AdminService {
    byte[] getBackup();

    void autoBackupDataBase();
}
