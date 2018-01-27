package org.ekolab.server.service.impl.content.lab2;


import org.apache.commons.math3.util.Precision;
import org.ekolab.server.dao.api.content.lab2.Lab2Dao;
import org.ekolab.server.model.content.lab2.CalculationResultType;
import org.ekolab.server.model.content.lab2.Frequency;
import org.ekolab.server.model.content.lab2.Lab2Data;
import org.ekolab.server.model.content.lab2.Lab2Variant;
import org.ekolab.server.service.api.ReportService;
import org.ekolab.server.service.api.StudentInfoService;
import org.ekolab.server.service.api.UserInfoService;
import org.ekolab.server.service.api.content.lab2.Lab2Service;
import org.ekolab.server.service.impl.content.LabServiceImpl;
import org.springframework.context.MessageSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by 777Al on 26.04.2017.
 */
public abstract class Lab2ServiceImpl<V extends Lab2Variant, D extends Lab2Dao<V>> extends LabServiceImpl<Lab2Data<V>, V, D> implements Lab2Service<V> {

    protected Lab2ServiceImpl(D labDao, UserInfoService userInfoService, ReportService reportService, MessageSource messageSource, StudentInfoService studentInfoService) {
        super(labDao, userInfoService, reportService, messageSource, studentInfoService);
    }

    @Override
    protected Lab2Data<V> createNewLabData() {
        return new Lab2Data<>();
    }

    @Override
    public void updateCalculatedFields(Lab2Data<V> labData) {
        if (labData.getHemisphereRadius() != null) {
            labData.setHemisphereSurface(2 * Math.PI * Math.pow(labData.getHemisphereRadius(), 2));
        }

        SortedMap<CalculationResultType, List<Double>> calculationResult = new TreeMap<>();
        if (labData.getAverageSoundPressure() != null) {
            calculationResult.put(CalculationResultType.AVERAGE_SOUND_PRESSURE, labData.getAverageSoundPressure());

            if (labData.getCorrectionFactor() != null) {
                calculationResult.put(CalculationResultType.CORRECTION_FACTOR, Collections.nCopies(labData.getAverageSoundPressure().size(), labData.getCorrectionFactor()));


                List<Double> soundPressureMeasuringSurface = labData.getAverageSoundPressure().stream().
                                map(a -> a + labData.getCorrectionFactor()).collect(Collectors.toList());

                if (labData.getMeasuringFactor() != null) {
                    calculationResult.put(CalculationResultType.MEASURING_FACTOR, Collections.nCopies(labData.getAverageSoundPressure().size(), labData.getMeasuringFactor()));

                    if (labData.getRoomSize() != null) {
                        calculationResult.put(CalculationResultType.FREQUENCY_COEFFICIENT,
                                Arrays.stream(Frequency.values()).map(f -> Lab2CalcUtils.getMu(labData.getRoomSize(), f)).collect(Collectors.toList()));


                        calculationResult.put(CalculationResultType.SOUND_POWER_LEVEL,
                                soundPressureMeasuringSurface.stream().
                                        map(a -> a + labData.getMeasuringFactor()).collect(Collectors.toList()));

                        if (labData.getRoomConstant1000() != null && labData.getRoomConstant() != null) {
                            calculationResult.put(CalculationResultType.ROOM_CONSTANT,
                                    calculationResult.get(CalculationResultType.FREQUENCY_COEFFICIENT).stream().
                                            map(f -> f * labData.getRoomConstant1000()).collect(Collectors.toList()));

                            if (labData.getRoomConstant() != null) {
                                calculationResult.put(CalculationResultType._10_lgB, Collections.nCopies(labData.getAverageSoundPressure().size(), labData.getRoomConstant()));

                                if (labData.getQuantityOfSingleTypeEquipment() != null) {
                                    calculationResult.put(CalculationResultType._10_lgn, Collections.nCopies(labData.getAverageSoundPressure().size(), Double.valueOf(labData.getQuantityOfSingleTypeEquipment())));

                                    if (labData.getReflectedSoundPower() != null) {
                                        List<Double> reflectedSoundPower =
                                                calculationResult.get(CalculationResultType.SOUND_POWER_LEVEL).stream().
                                                        map(a -> Precision.round(a + labData.getSoundPowerLevel() + 10 *
                                                                Math.log10(labData.getQuantityOfSingleTypeEquipment()) - 10 * Math.log10(labData.getRoomConstant()) + 6, 3)).collect(Collectors.toList());

                                        calculationResult.put(CalculationResultType.REFLECTED_SOUND_POWER, reflectedSoundPower);


                                        List<Double> allowedSoundPowerLevels = Arrays.stream(Frequency.values()).map(Lab2CalcUtils::getSoundPowerLevelForWorkplace).map(Double::valueOf).collect(Collectors.toList());

                                        calculationResult.put(CalculationResultType.ALLOWED_SOUND_POWER_LEVELS, allowedSoundPowerLevels);

                                        calculationResult.put(CalculationResultType.EXCESS,
                                                IntStream.range(0, reflectedSoundPower.size()).mapToObj(i -> reflectedSoundPower.get(i) > allowedSoundPowerLevels.get(i) ?
                                                        Precision.round(reflectedSoundPower.get(i) - allowedSoundPowerLevels.get(i), 3) : null).collect(Collectors.toList()));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        labData.setCalculationResult(calculationResult);
    }

    @Override
    public int getLabNumber() {
        return 2;
    }
}
