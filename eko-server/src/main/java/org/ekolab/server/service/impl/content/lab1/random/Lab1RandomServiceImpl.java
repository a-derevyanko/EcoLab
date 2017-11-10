package org.ekolab.server.service.impl.content.lab1.random;

import org.apache.commons.lang.math.RandomUtils;
import org.ekolab.server.dao.api.content.lab1.random.Lab1RandomDao;
import org.ekolab.server.model.content.lab1.Lab1Variant;
import org.ekolab.server.model.content.lab1.random.Lab1RandomData;
import org.ekolab.server.service.impl.content.lab1.Lab1ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Locale;

/**
 * Created by 777Al on 26.04.2017.
 */
@Service
public class Lab1RandomServiceImpl extends Lab1ServiceImpl<Lab1RandomData, Lab1Variant> {

    @Autowired
    public Lab1RandomServiceImpl(Lab1RandomDao lab1Dao) {
        super(lab1Dao);
    }

    @Override
    public Lab1RandomData createNewLabData() {
        return new Lab1RandomData();
    }

    @Override
    protected Lab1Variant generateNewLabVariant() {
        Lab1Variant variant = new Lab1Variant();

        variant.setName(messageSource.getMessage("lab1.random-object-name", null, Locale.getDefault()));
        variant.setTime(LocalDateTime.now());
        variant.setOutsideAirTemperature(-25 + RandomUtils.nextInt(11) * 5);
        variant.setSteamProductionCapacity(30 + RandomUtils.nextInt(5)*5);

        variant.setOxygenConcentrationPoint(5.0 + RandomUtils.nextInt(21) * 0.1);

        variant.setFuelConsumerNormalized(variant.getSteamProductionCapacity() * 80 + 100);
        variant.setStackExitTemperature(120 + RandomUtils.nextInt(26));

        variant.setFlueGasNOxConcentration(120 + RandomUtils.nextInt(17) * 5);
        return variant;
    }

}
