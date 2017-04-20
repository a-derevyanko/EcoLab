package org.ekolab.server.dao.impl;

import org.ekolab.server.common.Profiles;
import org.ekolab.server.dao.api.Lab3Dao;
import org.ekolab.server.model.Lab3Data;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by 777Al on 19.04.2017.
 */
@Service
@Profile({Profiles.DB.H2, Profiles.DB.POSTGRES})
public class Lab3DaoImpl implements Lab3Dao {
    @Override
    public Lab3Data getLastLabByUser(String userName) {
        return null;
    }

    @Override
    public List<Lab3Data> getAllLabsByUser(String userName) {
        return null;
    }

    @Override
    public Lab3Data saveLab(Lab3Data labData) {
        return null;
    }

    @Override
    public Lab3Data updateLab(Lab3Data labData) {
        return null;
    }

    @Override
    public void removeLabsByUser(String userName) {

    }

    @Override
    public void removeOldLabs(LocalDateTime lastSaveDate) {

    }
}
