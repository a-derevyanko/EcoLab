package org.ekolab.server.model.content.lab3;

import java.util.Arrays;
import java.util.List;

/**
 * Created by 777Al on 11.04.2017.
 */
public enum City {
    MOSCOW {
        public List<FuelType> getFuelTypesForTheCity() {
            return Arrays.asList(FuelType.DONETSK_COAL, FuelType.SULFUR_OIL);
        }
        public double getWindSpeed(){return 4;}
    },
    SAINT_PETERSBURG {
        public List<FuelType> getFuelTypesForTheCity() {
            return Arrays.asList(FuelType.SULFUR_OIL, FuelType.LENINGRAD_SHALE, FuelType.ESTONIAN_SHALE);
        }
        public double getWindSpeed(){return 3.3;}
    },
    NOVOSIBIRSK {
        public List<FuelType> getFuelTypesForTheCity() {
            return Arrays.asList(FuelType.EKIBASTUZ_COAL, FuelType.BEREZOVSKY_COAL_TSU, FuelType.BEREZOVSKY_COAL_ZHSHU, FuelType.KUZNETSK_COAL);
        }
        public double getWindSpeed(){return 3.7;}
    },
    EKATERINBURG {
        public List<FuelType> getFuelTypesForTheCity() {
            return Arrays.asList(FuelType.EKIBASTUZ_COAL, FuelType.SULFUR_OIL, FuelType.KUZNETSK_COAL);
        }
        public double getWindSpeed(){return 4;}
    },
    NIZHNIY_NOVGOROD {
        public List<FuelType> getFuelTypesForTheCity() {
            return Arrays.asList(FuelType.DONETSK_COAL, FuelType.SULFUR_OIL);
        }
        public double getWindSpeed(){return 4.7;}
    },
    KRASNOYARSK {
        public List<FuelType> getFuelTypesForTheCity() {
            return Arrays.asList(FuelType.EKIBASTUZ_COAL, FuelType.BEREZOVSKY_COAL_TSU, FuelType.BEREZOVSKY_COAL_ZHSHU, FuelType.KUZNETSK_COAL);
        }
        public double getWindSpeed(){return 3.9;}
    },
    ROSTOV_NA_DONU {
        public List<FuelType> getFuelTypesForTheCity() {
            return Arrays.asList(FuelType.DONETSK_COAL, FuelType.SULFUR_OIL);
        }
        public double getWindSpeed(){return 6.6;}
    },
    KALININGRAD {
        public List<FuelType> getFuelTypesForTheCity() {
            return Arrays.asList(FuelType.DONETSK_COAL, FuelType.SULFUR_OIL);
        }
        public double getWindSpeed(){return 4.7;}
    },
    VLADIVOSTOK {
        public List<FuelType> getFuelTypesForTheCity() {
            return Arrays.asList(FuelType.EKIBASTUZ_COAL, FuelType.BEREZOVSKY_COAL_TSU, FuelType.BEREZOVSKY_COAL_ZHSHU, FuelType.KUZNETSK_COAL);
        }
        public double getWindSpeed(){return 7.1;}
    },
    SURGUT {
        public List<FuelType> getFuelTypesForTheCity() {
            return Arrays.asList(FuelType.SULFUR_OIL, FuelType.STABILIZED_OIL);
        }
        public double getWindSpeed(){return 5;}
    };

    public abstract List<FuelType> getFuelTypesForTheCity();
    public abstract double getWindSpeed();
}
