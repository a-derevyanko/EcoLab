package org.ekolab.server.service.impl.content.lab1;

import org.ekolab.server.dao.api.content.lab1.Lab1Dao;
import org.ekolab.server.model.content.lab1.Lab1Data;
import org.ekolab.server.model.content.lab1.Lab1Variant;
import org.ekolab.server.service.api.content.lab1.Lab1Service;
import org.ekolab.server.service.impl.content.LabServiceImpl;

/**
 * Created by 777Al on 26.04.2017.
 */
public abstract class Lab1ServiceImpl<T extends Lab1Data<V>, V extends Lab1Variant> extends LabServiceImpl<T, V> implements Lab1Service<T, V> {
    protected Lab1ServiceImpl(Lab1Dao<T> labDao) {
        super(labDao);
    }

    @Override
    public void updateCalculatedFields(Lab1Data labData) {
        //todo correctionFactor!!!!
    }


    @Override
    public int getLabNumber() {
        return 1;
    }
}
