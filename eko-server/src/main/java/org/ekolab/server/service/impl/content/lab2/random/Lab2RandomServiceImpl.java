package org.ekolab.server.service.impl.content.lab2.random;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.ekolab.server.dao.api.content.lab2.random.Lab2RandomDao;
import org.ekolab.server.model.content.lab2.ObjectType;
import org.ekolab.server.model.content.lab2.random.Lab2RandomVariant;
import org.ekolab.server.service.api.ReportService;
import org.ekolab.server.service.api.StudentInfoService;
import org.ekolab.server.service.api.UserInfoService;
import org.ekolab.server.service.api.content.lab2.random.Lab2RandomService;
import org.ekolab.server.service.impl.content.lab2.Lab2ServiceImpl;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by 777Al on 26.04.2017.
 */
@Service
public class Lab2RandomServiceImpl extends Lab2ServiceImpl<Lab2RandomVariant, Lab2RandomDao> implements Lab2RandomService {
    protected Lab2RandomServiceImpl(Lab2RandomDao labDao, UserInfoService userInfoService, ReportService reportService, MessageSource messageSource, StudentInfoService studentInfoService) {
        super(labDao, userInfoService, reportService, messageSource, studentInfoService);
    }


    @Override
    protected Map<String, Object> getInitialDataWithLocalizedValues(Lab2RandomVariant data, Locale locale) {
        Map<String, Object> map = super.getInitialDataWithLocalizedValues(data, locale);
        map.remove("city");

        return map;
    }

    @Override
    protected Lab2RandomVariant generateNewLabVariant() {
        Lab2RandomVariant variant = new Lab2RandomVariant();

        ObjectType object = ObjectType.values()[RandomUtils.nextInt(ObjectType.values().length)];
        variant.setName(object);
        variant.setQuantityOfSingleTypeEquipment(RandomUtils.nextInt(2) + 1);
        variant.setBarometricPressure(700 + RandomUtils.nextInt(21) * 5);
        variant.setIndoorsTemperature(5 + RandomUtils.nextInt(4) * 5);
        switch (variant.getName()) {
            case PT_6_35: {
                variant.setAverageSoundPressure(generateValues(ImmutableList.<Pair<Double, Double>>builder()
                        .add(Pair.of(115.0, 120.0))
                        .add(Pair.of(115.0, 120.0))
                        .add(Pair.of(110.0, 115.0))
                        .add(Pair.of(110.0, 115.0))
                        .add(Pair.of(110.0, 115.0))
                        .add(Pair.of(105.0, 110.0))
                        .add(Pair.of(105.0, 110.0))
                        .add(Pair.of(100.0, 105.0))
                        .add(Pair.of(95.0, 100.0)).build(), 0.2, true));
                break;
            }
            case SUPPLING_PUMP: {
                List<Double> averageSoundPressure = generateValues(ImmutableList.<Pair<Double, Double>>builder()
                        .add(Pair.of(95.0, 105.0))
                        .add(Pair.of(95.0, 105.0))
                        .add(Pair.of(95.0, 105.0))
                        .add(Pair.of(115.0, 125.0)).build(), 0.2, true);
                averageSoundPressure.addAll(generateValues(ImmutableList.<Pair<Double, Double>>builder()
                        .add(Pair.of(110.0, 120.0))
                        .add(Pair.of(110.0, 120.0))
                        .add(Pair.of(105.0, 115.0))
                        .add(Pair.of(90.0, 100.0))
                        .add(Pair.of(90.0, 100.0)).build(), 0.2, false));

                variant.setAverageSoundPressure(averageSoundPressure);
                break;
            }
            case BLOWING_FAN: {
                variant.setAverageSoundPressure(generateValues(ImmutableList.<Pair<Double, Double>>builder()
                        .add(Pair.of(90.0, 95.0))
                        .add(Pair.of(90.0, 95.0))
                        .add(Pair.of(80.0, 85.0))
                        .add(Pair.of(70.0, 75.0))
                        .add(Pair.of(65.0, 70.0))
                        .add(Pair.of(65.0, 70.0))
                        .add(Pair.of(60.0, 65.0))
                        .add(Pair.of(60.0, 65.0))
                        .add(Pair.of(60.0, 65.0)).build(), 0.2, true));
                break;
            }
        }

        return variant;
    }

    private List<Double> generateValues(List<Pair<Double, Double>> bounds, double step, boolean down) {
        List<Double> values = new ArrayList<>(bounds.size());
        double previous = Double.MAX_VALUE;
        for (Pair<Double, Double> entry : bounds) {
            previous = generateNextValue(entry.getKey(), entry.getValue(), step, previous, down);
            values.add(previous);
        }
        return values;
    }

    private double generateNextValue(double firstValue, double lastValue, double step, double previousValue, boolean down) {
        double r = firstValue + RandomUtils.nextInt((int) ((lastValue - firstValue) / step) + 1) * step;
        if (down && r > previousValue || !down && r < previousValue) {
            return generateNextValue(firstValue, lastValue, step, previousValue, down);
        } else {
            return r;
        }
    }
}
