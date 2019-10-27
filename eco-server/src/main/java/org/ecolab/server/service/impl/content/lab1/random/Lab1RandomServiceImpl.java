package org.ecolab.server.service.impl.content.lab1.random;

import org.apache.commons.lang.math.RandomUtils;
import org.ecolab.server.dao.api.content.lab1.random.Lab1RandomDao;
import org.ecolab.server.model.content.DataValue;
import org.ecolab.server.model.content.lab1.City;
import org.ecolab.server.model.content.lab1.random.Lab1RandomVariant;
import org.ecolab.server.service.api.ReportService;
import org.ecolab.server.service.api.StudentInfoService;
import org.ecolab.server.service.api.UserInfoService;
import org.ecolab.server.service.api.content.lab1.Lab1ResourceService;
import org.ecolab.server.service.api.content.lab1.random.Lab1RandomService;
import org.ecolab.server.service.impl.content.lab1.Lab1ServiceImpl;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Created by 777Al on 26.04.2017.
 */
@Service
public class Lab1RandomServiceImpl extends Lab1ServiceImpl<Lab1RandomVariant, Lab1RandomDao> implements Lab1RandomService {
    protected final Lab1ResourceService labResourceService;

    protected Lab1RandomServiceImpl(Lab1RandomDao labDao, UserInfoService userInfoService, ReportService reportService, MessageSource messageSource, StudentInfoService studentInfoService, Lab1ResourceService labResourceService) {
        super(labDao, userInfoService, reportService, messageSource, studentInfoService);
        this.labResourceService = labResourceService;
    }

    @Override
    public Set<DataValue> getInitialDataValues(Lab1RandomVariant data, Locale locale) {
        var values = super.getInitialDataValues(data, locale);

        var imageValue = new DataValue();
        imageValue.setName(messageSource.getMessage("lab1.initial-data.boiler", null, locale));
        imageValue.setValue(labResourceService.getBoiler());
        values.add(imageValue);

        var cityValue = new DataValue();
        cityValue.setName(messageSource.getMessage("lab1.city", new Object[]{getFieldValueForPrint(data.getCity(), locale)}, locale));
        cityValue.setValue(labResourceService.getCoatOfArms(data.getCity().name()));
        values.add(cityValue);

        return values;
    }

    @Override
    protected Map<String, Object> getInitialDataWithLocalizedValues(Lab1RandomVariant data, Locale locale) {
        var map = super.getInitialDataWithLocalizedValues(data, locale);
        map.remove("city");

        return map;
    }

    @Override
    protected Lab1RandomVariant generateNewLabVariant() {
        var variant = new Lab1RandomVariant();

        variant.setCity(City.values()[RandomUtils.nextInt(City.values().length)]);
        variant.setOutsideAirTemperature(-25 + RandomUtils.nextInt(11) * 5);
        variant.setSteamProductionCapacity(30 + RandomUtils.nextInt(5)*5);

        variant.setOxygenConcentrationPoint(5.0 + RandomUtils.nextInt(21) * 0.1);

        variant.setFuelConsumerNormalized(variant.getSteamProductionCapacity() * 80 + 100);
        variant.setStackExitTemperature(120 + RandomUtils.nextInt(26));

        variant.setFlueGasNOxConcentration(120 + RandomUtils.nextInt(17) * 5);
        return variant;
    }

}
