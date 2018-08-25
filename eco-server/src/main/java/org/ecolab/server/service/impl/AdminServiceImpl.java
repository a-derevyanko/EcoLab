package org.ecolab.server.service.impl;

import org.ecolab.server.dao.api.AdminDao;
import org.ecolab.server.service.api.AdminService;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    private final AdminDao adminDao;

    public AdminServiceImpl(AdminDao adminDao) {
        this.adminDao = adminDao;
    }

    @Override
    public byte[] getBackup() {
        return adminDao.getBackup();
    }
}
