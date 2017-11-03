package org.ekolab.server.service.impl.content.lab1;

import org.apache.commons.lang.math.RandomUtils;
import org.ekolab.server.dao.api.content.lab1.Lab1Dao;
import org.ekolab.server.model.content.lab1.Lab1Data;
import org.ekolab.server.model.content.lab1.Lab1Variant;
import org.ekolab.server.service.api.content.lab1.Lab1Service;
import org.ekolab.server.service.impl.content.LabServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 777Al on 26.04.2017.
 */
@Service
public class Lab1ServiceImpl extends LabServiceImpl<Lab1Data, Lab1Variant> implements Lab1Service {

    @Autowired
    public Lab1ServiceImpl(Lab1Dao lab1Dao) {
        super(lab1Dao);
    }

    @Override
    public Lab1Data updateCalculatedFields(Lab1Data labData) {
        //todo correctionFactor!!!!
        return null;
    }

    @Override
    public Lab1Data createNewLabData() {
        return new Lab1Data();
    }

    @Override
    public int getLabNumber() {
        return 1;
    }

    @Override
    protected Lab1Variant generateNewLabVariant() {
        Lab1Variant variant = new Lab1Variant();

        variant.setName("");
        variant.setOutsideAirTemperature(-25 + RandomUtils.nextInt(11) * 5);
        variant.setSteamProductionCapacity(30 + RandomUtils.nextInt(5)*5);

        variant.setOxygenConcentrationPoint(5.0 + RandomUtils.nextInt(21) * 0.1);

        variant.setFuelConsumerNormalized(variant.getSteamProductionCapacity() * 80 + 100);
        variant.setStackExitTemperature(120 + RandomUtils.nextInt(26));

        variant.setFlueGasNOxConcentration(120 + RandomUtils.nextInt(17) * 5);
        return variant;
    }

    @Override
    public Lab1Data startNewLabWithEmptyVariant(String userName) {
        Lab1Data lab1Data = createBaseLabData(userName);
        lab1Data.setVariant(new Lab1Variant());
        labDao.saveLab(lab1Data);
        return lab1Data;
    }
}
