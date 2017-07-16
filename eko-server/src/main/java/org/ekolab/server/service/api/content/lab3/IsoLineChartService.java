package org.ekolab.server.service.api.content.lab3;

import org.jfree.chart.JFreeChart;

import java.util.Locale;

/**
 * Created by 777Al on 24.04.2017.
 */
public interface IsoLineChartService {
    JFreeChart createIsoLineChart(double windSpeedMaxGroundLevelConcentrationDistance,
                                  double harmfulSubstancesDepositionCoefficient, double bwdNo2GroundLevelConcentration,
                                  double no2BackgroundConcentration, double windSpeed, double mac, Locale locale);
}
