package org.ecolab.server.service.impl.content.lab2.random;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.ecolab.server.dao.api.content.lab2.random.Lab2RandomDao;
import org.ecolab.server.model.content.lab2.Frequency;
import org.ecolab.server.model.content.lab2.ObjectType;
import org.ecolab.server.model.content.lab2.random.Lab2RandomVariant;
import org.ecolab.server.service.api.ReportService;
import org.ecolab.server.service.api.StudentInfoService;
import org.ecolab.server.service.api.UserInfoService;
import org.ecolab.server.service.api.content.lab2.random.Lab2RandomService;
import org.ecolab.server.service.impl.content.lab2.Lab2ServiceImpl;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

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
        variant.setEstimatedGeometricMeanFrequency(Arrays.stream(Frequency.values())
                .filter(frequency -> frequency != Frequency.F_315).sorted((o1, o2) ->
                        ThreadLocalRandom.current().nextInt(-1, 2))
                .findAny()
                .orElseThrow(IllegalStateException::new));
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
                        .add(Pair.of(95.0, 100.0)).build(), 0.2));
                break;
            }
            case SUPPLING_PUMP: {
                List<Double> averageSoundPressure = Lists.reverse(generateValues(ImmutableList.<Pair<Double, Double>>builder()
                        .add(Pair.of(115.0, 125.0))
                        .add(Pair.of(95.0, 105.0))
                        .add(Pair.of(95.0, 105.0))
                        .add(Pair.of(95.0, 105.0)).build(), 0.2));
                averageSoundPressure.addAll(generateValues(ImmutableList.<Pair<Double, Double>>builder()
                        .add(Pair.of(110.0, 120.0))
                        .add(Pair.of(110.0, 120.0))
                        .add(Pair.of(105.0, 115.0))
                        .add(Pair.of(90.0, 100.0))
                        .add(Pair.of(90.0, 100.0)).build(), 0.2));

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
                        .add(Pair.of(60.0, 65.0)).build(), 0.2));
                break;
            }
        }

        return variant;
    }

    private List<Double> generateValues(List<Pair<Double, Double>> bounds, double step) {
        List<Double> values = new ArrayList<>(bounds.size());
        double previous = Double.MAX_VALUE;
        for (Pair<Double, Double> entry : bounds) {
            previous = generateNextValue(entry.getKey(), entry.getValue(), step, previous);
            values.add(previous);
        }
        return values;
    }

    /**
     * Вычисление следующего значения Дб
     * @param firstValue левая граница интервала, в котором находится значение
     * @param lastValue правая граница интервала, в котором находится значение
     * @param step шаг
     * @param previousValue предыдущее значение
     * @return сгенерированное значение
     */
    private double generateNextValue(double firstValue, double lastValue, double step, double previousValue) {
        if (firstValue >= lastValue) {
            return lastValue;
        }
        double r = firstValue + RandomUtils.nextInt((int) ((lastValue - firstValue) / step) + 1) * step;
        if (r > previousValue) {
            return generateNextValue(firstValue, lastValue, step, previousValue);
        } else {
            return r;
        }
    }
}
