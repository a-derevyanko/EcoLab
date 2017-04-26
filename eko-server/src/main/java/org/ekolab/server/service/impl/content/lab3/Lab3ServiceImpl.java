package org.ekolab.server.service.impl.content.lab3;

import org.ekolab.server.dao.api.Lab3Dao;
import org.ekolab.server.model.content.lab3.Lab3Data;
import org.ekolab.server.service.impl.content.LabServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 777Al on 26.04.2017.
 */
@Service
public class Lab3ServiceImpl extends LabServiceImpl<Lab3Data> {
    @Autowired
    public Lab3ServiceImpl(Lab3Dao lab3Dao) {
        super(lab3Dao);
    }

    @Override
    protected Lab3Data loadCalculatedFields(Lab3Data labData) {
        return null;
    }
}
