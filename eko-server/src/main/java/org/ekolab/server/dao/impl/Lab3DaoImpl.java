package org.ekolab.server.dao.impl;

import org.ekolab.server.common.Profiles;
import org.ekolab.server.dao.api.Lab3Dao;
import org.ekolab.server.model.LabData;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Created by 777Al on 19.04.2017.
 */
@Service
@Profile({Profiles.DB.H2, Profiles.DB.POSTGRES})
public class Lab3DaoImpl implements Lab3Dao {
    @Autowired
    private DSLContext dsl;

    @Override
    public LabData getLabByUser(Long userId) {
        return null;
    }

    @Override
    public LabData saveLab(LabData labData) {
        return null;
    }

    @Override
    public void updateLab(LabData labData) {

    }
}
