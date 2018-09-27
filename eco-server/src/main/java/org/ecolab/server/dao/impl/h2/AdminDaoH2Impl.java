package org.ecolab.server.dao.impl.h2;

import org.ecolab.server.common.Profiles;
import org.ecolab.server.dao.api.AdminDao;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile({Profiles.DB.H2})
public class AdminDaoH2Impl implements AdminDao {

    private final DSLContext dsl;

    public AdminDaoH2Impl(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public void backupTo(String fileName) {
        dsl.execute("BACKUP TO '" + fileName + "'");
    }
}
