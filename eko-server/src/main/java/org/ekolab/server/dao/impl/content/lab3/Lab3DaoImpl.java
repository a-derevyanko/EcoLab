package org.ekolab.server.dao.impl.content.lab3;

import org.ekolab.server.common.Profiles;
import org.ekolab.server.dao.api.content.lab3.Lab3Dao;
import org.ekolab.server.dao.impl.content.LabDaoImpl;
import org.ekolab.server.model.content.lab3.Lab3Data;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by 777Al on 19.04.2017.
 */
@Service
@Profile({Profiles.DB.H2, Profiles.DB.POSTGRES})
public class Lab3DaoImpl extends LabDaoImpl<Lab3Data> implements Lab3Dao {
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
    public int removeLabsByUser(String userName) {
        return 0;
    }

    @Override
    public int removeOldLabs(LocalDateTime lastSaveDate) {
        return 0;
    }
}
