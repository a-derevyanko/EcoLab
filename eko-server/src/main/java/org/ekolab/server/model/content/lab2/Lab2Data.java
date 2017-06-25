package org.ekolab.server.model.content.lab2;

import org.ekolab.server.model.content.LabData;

import javax.annotation.Nullable;

/**
 * Created by Андрей on 24.06.2017.
 */
public class Lab2Data extends LabData<Lab2Variant> {
    /**
     * Название объекта
     */
    @Nullable
    private String name;

    /**
     * Барометрическое давление
     */
    private Double barometricPressure;

    /**
     * Температура наружного воздуха
     */

    private Double outsideTemperature;

    /**
     * Число дымовых труб
     */
    private Integer numberOfStacks;

    /**
     * Высота дымовой трубы
     */
    private Double stacksHeight;

    /**
     * Паровая нагрузка котла
     */
    private Double steamProductionCapacity;

    /**
     * Содержание кислорода за пароперегревателем
     */
    private Double oxigenConcentration;

    /**
     * Содержание кислорода в сечении газохода, где проводились измерения
     */
    private Double oxigenConcentrationPoint;

    /**
     * Расход природного газа на паровой котел по расходомеру
     */
    private Double fuelConsumer;

    /**
     * Избыточное давление природного газа в магистрали
     */
    private Double excessPressure;

    /**
     * Температура природного газа в магистрали
     */
    private Double gasTemperature;

    /**
     * Температура уходящих дымовых газов
     */
    private Double stackExitTemperature;

    /**
     * Концентрация оксидов азота в дымовых газах по результатам измерений
     */
    private Double flueGasNOxConcentration;

    /**
     * Коэффициент избытка воздуха в точке измерения
     */
    private Double excessAirRatio;

    /**
     * Концентрация оксидов азота, приведенная к стандартному коэффициенту избытка воздуха α=1,4
     */
    private Double flueGasNOxConcentrationNC;

    /**
     * Превышение допустимых норм
     */
    private Boolean excessOfNorms;

    /**
     * Действительное барометрическое давление
     */
    private Double validBarometricPressure;

    /**
     * Действительное абсолютное давление топлива в газопроводе
     */
    private Double validAbsolutePressure;

    /**
     * Поправочный  коэффициент на давление, температуру и плотность природного газа
     */
    private Double correctionFactor;

    /**
     * Расход природного газа на котел с учетом поправок
     */
    private Double fuelConsumerCorrection;

    /**
     * Расход природного газа на котел, приведенный к нормальным условиям
     */
    private Double fuelConsumerNC;

    /**
     * Расход дымовых газов, выбрасываемых в атмосферу
     */
    private Double flueGasesRate;

    /**
     * Объемный расход сухих газов
     */
    private Double dryGasesFlowRate;

    /**
     * Массовые выбросы оксидов азота
     */
    private Double massEmissions;

    /**
     * Скорость дымовых газов на выходе из дымовой трубы
     */
    private Double flueGasesSpeed;

    /**
     * Параметр f
     */
    private Double f;

    /**
     * Коэффициент m учитывающий условия выхода газов из дымовой трубы
     */
    private Double m;

    /**
     * Параметр υ_м
     */
    private Double u;

    /**
     * Коэффициент n учитывающий условия выхода газов из дымовой трубы
     */
    private Double n;

    /**
     * Безразмерный коэффициент d
     */
    private Double d;

    /**
     * Безразмерный коэффициент, учитывающий скорость оседания вредных веществ в атмосферном воздухе
     */
    private Double harmfulSubstancesDepositionCoefficient;

    /**
     * Коэффициент, учитывающий влияние рельефа местности
     */
    private Double terrainCoefficient;

    /**
     * Коэффициент, характеризующий температурную стратификацию атмосферы
     */
    private Double temperatureCoefficient;

    /**
     * Расстояние от источника выбросов, на котором приземные концентрации загрязняющих веществ достигают максимального значения
     */
    private Double distanceFromEmissionSource;

    /**
     * Максимальная приземная концентрация оксидов азота
     */
    private Double maximumSurfaceConcentration;
}
