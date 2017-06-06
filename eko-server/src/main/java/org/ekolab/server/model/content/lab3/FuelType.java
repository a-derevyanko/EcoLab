package org.ekolab.server.model.content.lab3;

import org.apache.commons.lang.math.RandomUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 777Al on 11.04.2017.
 */
public enum FuelType {
    DONETSK_COAL {
        public double getLowHeatValue(){return 19.59;}
        public double getCarbonInFlyAsh() {
            List<Double> values;
            values = Arrays.asList(1.0 , 1.5);
            return values.get(RandomUtils.nextInt(values.size()));
        }
    },
    EKIBASTUZ_COAL {
        public double getLowHeatValue(){return 16.75;}
        public double getCarbonInFlyAsh() {
            List<Double> values;
            values = Arrays.asList(1.0 , 1.5);
            return values.get(RandomUtils.nextInt(values.size()));
        }
    },
    KUZNETSK_COAL {
        public double getLowHeatValue(){return 22.82;}
        public double getCarbonInFlyAsh() {
            List<Double> values;
            values = Arrays.asList(1.0 , 1.5);
            return values.get(RandomUtils.nextInt(values.size()));
        }
    },
    BEREZOVSKY_COAL_TSU {
        public double getLowHeatValue(){return 15.66;}
        public double getCarbonInFlyAsh() {
            List<Double> values;
            values = Arrays.asList(0.5, 1.0);
            return values.get(RandomUtils.nextInt(values.size()));
        }
    },
    BEREZOVSKY_COAL_ZHSHU {
        public double getLowHeatValue(){return 15.66;}
        public double getCarbonInFlyAsh() {
            List<Double> values;
            values = Arrays.asList(0.5);
            return values.get(RandomUtils.nextInt(values.size()));
        }
    },
    ESTONIAN_SHALE {
        public double getLowHeatValue(){return 10.93;}
        public double getCarbonInFlyAsh() {
            List<Double> values;
            values = Arrays.asList(0.5, 1.0);
            return values.get(RandomUtils.nextInt(values.size()));
        }
    },
    LENINGRAD_SHALE {
        public double getLowHeatValue(){return 5.82;}
        public double getCarbonInFlyAsh() {
            List<Double> values;
            values = Arrays.asList(0.5, 1.0);
            return values.get(RandomUtils.nextInt(values.size()));
        }
    },
    SULFUR_OIL {
        public double getLowHeatValue(){return 39.73;}
        public double getCarbonInFlyAsh() {
            List<Double> values;
            values = Arrays.asList(0.0);
            return values.get(RandomUtils.nextInt(values.size()));
        }
    },
    STABILIZED_OIL {
        public double getLowHeatValue(){return 39.77;}
        public double getCarbonInFlyAsh() {
            List<Double> values;
            values = Arrays.asList(0.0);
            return values.get(RandomUtils.nextInt(values.size()));
        }
    };


    public abstract double getLowHeatValue();
}

