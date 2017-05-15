package org.ekolab.server.model.content.lab3;

import org.ekolab.server.model.content.LabVariant;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * Created by 777Al on 06.04.2017.
 */
public class Lab3Variant extends LabVariant {
    /**
     * Мощность ГРЭС
     */
    @Max(3200L)
    @Min(1200L)
    private Integer tppOutput;

    /**
     * Число блоков
     */
    private NumberOfUnits numberOfUnits;

    /**
     * Район расположения ГРЭС (город)
     */
    @Valid
    private City city;

    /**
     * Номинальная паропроизводительность одного котла
     */
    @Max(2450L)
    @Min(580L)
    private Integer steamProductionCapacity;

    /**
     * Число дымовых труб
     */
    private NumberOfStacks numberOfStacks;

    /**
     * Высота дымовой трубы
     */
    @Max(240L)
    @Min(120L)
    private Integer stacksHeight;

    /**
     * Расчетное направление ветра
     */
    private WindDirection windDirection;

    /**
     * Расчетная скорость ветра
     */
    private Double windSpeed;

    /**
     * Низшая теплота сгорания топлива
     */
    /*@Max(40)
    @Min(28)*/
    @Digits(integer = 2, fraction = 2)
    private Double lowHeatValue;

    /**
     * Расход топлива на 1 блок
     */
    private Double fuelConsumer;

    /**
     * Потери тепла от механической неполноты сгорания топлива
     */
    /*@Max(1.5)
    @Min(0)*/
    @Digits(integer = 1, fraction = 1)
    private Double carbonInFlyAsh;

    /**
     * Содержание серы в топливе
     */
    private Double sulphurContent;

    /**
     * Зольность топлива
     */
    /*@Max(99)
    @Min(0)*/
    @Digits(integer = 2, fraction = 1)
    private Double ashContent;

    /**
     * Влажность топлива
     */
    private Double waterContent;

    /**
     * Степень улавливания золы
     */
    private Double ashRecyclingFactor;

    /**
     * Концентрация оксидов азота в сухих газах
     */
    private Double flueGasNOxConcentration;

    /**
     * Температура газов на выходе из дымовой трубы
     */
    @Max(200L)
    @Min(100L)
    private Integer stackExitTemperature;

    /**
     * Температура наружного воздуха
     */
    @Max(100L)
    @Min(-100L)
    private Integer outsideAirTemperature;

    /**
     * Коэффициент избытка воздуха в уходящих газах
     */
    // Всегда == 1,4
    @Digits(integer = 1, fraction = 1)
    private Double excessAirRatio;

    /**
     * Теоретический объем продуктов сгорания
     */
    @Digits(integer = 100000, fraction = 2)
    private Double combustionProductsVolume;

    /**
     * Теоретический объем паров воды
     */
    @Digits(integer = 100000, fraction = 2)
    private Double waterVaporVolume;

    /**
     * Теоретический объем воздуха
     */
    @Digits(integer = 100000, fraction = 2)
    private Double airVolume;

    /**
     * Фоновая концентрация NO2
     */
    @Digits(integer = 100000, fraction = 3)
    private Double no2BackgroundConcentration;

    /**
     * Фоновая концентрация NO
     */
    @Digits(integer = 100000, fraction = 3)
    private Double noBackgroundConcentration;

    /**
     * Фоновая концентрация SO2
     */
    @Digits(integer = 100000, fraction = 3)
    private Double so2BackgroundConcentration;

    /**
     * Фоновая концентрация золы
     */
    @Digits(integer = 100000, fraction = 3)
    private Double ashBackgroundConcentration;
}
