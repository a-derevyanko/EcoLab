package org.ekolab.server.dao.impl.content.lab2;

import org.ekolab.server.common.Profiles;
import org.ekolab.server.dao.api.content.lab2.Lab2Dao;
import org.ekolab.server.dao.impl.content.LabDaoImpl;
import org.ekolab.server.model.content.lab2.Lab2Data;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by 777Al on 19.04.2017.
 */
@Service
@Profile({Profiles.DB.H2, Profiles.DB.POSTGRES})
public class Lab2DaoImpl extends LabDaoImpl<Lab2Data> implements Lab2Dao {


    @Override
    protected int getLabNumber() {
        return 2;
    }

    @Override
    public Lab2Data getLastLabByUser(String userName, boolean completed) {
        return null;
    }

    @Override
    public List<Lab2Data> getAllLabsByUser(String userName) {
        return null;
    }

    @Override
    public void saveLab(Lab2Data labData) {
    }

    @Override
    public int updateLab(Lab2Data labData) {
        return 0;
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
