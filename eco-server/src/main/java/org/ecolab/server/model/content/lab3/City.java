package org.ecolab.server.model.content.lab3;

import java.util.Arrays;
import java.util.List;

/**
 * Created by 777Al on 11.04.2017.
 */
public enum City {
    MOSCOW {
        @Override
        public List<FuelType> getFuelTypesForTheCity() {
            return Arrays.asList(FuelType.DONETSK_COAL, FuelType.SULFUR_OIL);
        }

        @Override
        public double getWindSpeed() {
            return 4.0;
        }

        @Override
        public int getOutsideAirTemperature() {
            return 17;
        }
    },
    SAINT_PETERSBURG {
        @Override
        public List<FuelType> getFuelTypesForTheCity() {
            return Arrays.asList(FuelType.SULFUR_OIL, FuelType.LENINGRAD_SHALE, FuelType.ESTONIAN_SHALE);
        }

        @Override
        public double getWindSpeed() {
            return 3.3;
        }

        @Override
        public int getOutsideAirTemperature() {
            return 18;
        }
    },
    NOVOSIBIRSK {
        @Override
        public List<FuelType> getFuelTypesForTheCity() {
            return Arrays.asList(FuelType.EKIBASTUZ_COAL, FuelType.BEREZOVSKY_COAL_TSU, FuelType.BEREZOVSKY_COAL_ZHSHU, FuelType.KUZNETSK_COAL);
        }

        @Override
        public double getWindSpeed() {
            return 3.7;
        }

        @Override
        public int getOutsideAirTemperature() {
            return 17;
        }
    },
    EKATERINBURG {
        @Override
        public List<FuelType> getFuelTypesForTheCity() {
            return Arrays.asList(FuelType.EKIBASTUZ_COAL, FuelType.SULFUR_OIL, FuelType.KUZNETSK_COAL);
        }

        @Override
        public double getWindSpeed() {
            return 4.0;
        }

        @Override
        public int getOutsideAirTemperature() {
            return 19;
        }
    },
    NIZHNIY_NOVGOROD {
        @Override
        public List<FuelType> getFuelTypesForTheCity() {
            return Arrays.asList(FuelType.DONETSK_COAL, FuelType.SULFUR_OIL);
        }

        @Override
        public double getWindSpeed() {
            return 4.7;
        }

        @Override
        public int getOutsideAirTemperature() {
            return 20;
        }
    },
    KRASNOYARSK {
        @Override
        public List<FuelType> getFuelTypesForTheCity() {
            return Arrays.asList(FuelType.EKIBASTUZ_COAL, FuelType.BEREZOVSKY_COAL_TSU, FuelType.BEREZOVSKY_COAL_ZHSHU, FuelType.KUZNETSK_COAL);
        }

        @Override
        public double getWindSpeed() {
            return 3.9;
        }

        @Override
        public int getOutsideAirTemperature() {
            return 16;
        }
    },
    ROSTOV_NA_DONU {
        @Override
        public List<FuelType> getFuelTypesForTheCity() {
            return Arrays.asList(FuelType.DONETSK_COAL, FuelType.SULFUR_OIL);
        }

        @Override
        public double getWindSpeed() {
            return 6.6;
        }

        @Override
        public int getOutsideAirTemperature() {
            return 23;
        }
    },
    KALININGRAD {
        @Override
        public List<FuelType> getFuelTypesForTheCity() {
            return Arrays.asList(FuelType.DONETSK_COAL, FuelType.SULFUR_OIL);
        }

        @Override
        public double getWindSpeed() {
            return 4.7;
        }

        @Override
        public int getOutsideAirTemperature() {
            return 18;
        }
    },
    VLADIVOSTOK {
        @Override
        public List<FuelType> getFuelTypesForTheCity() {
            return Arrays.asList(FuelType.EKIBASTUZ_COAL, FuelType.BEREZOVSKY_COAL_TSU, FuelType.BEREZOVSKY_COAL_ZHSHU, FuelType.KUZNETSK_COAL);
        }

        @Override
        public double getWindSpeed() {
            return 7.1;
        }

        @Override
        public int getOutsideAirTemperature() {
            return 17;
        }
    },
    SURGUT {
        @Override
        public List<FuelType> getFuelTypesForTheCity() {
            return Arrays.asList(FuelType.SULFUR_OIL, FuelType.STABILIZED_OIL);
        }

        @Override
        public double getWindSpeed() {
            return 5.0;
        }

        @Override
        public int getOutsideAirTemperature() {
            return 15;
        }
    };

    public abstract List<FuelType> getFuelTypesForTheCity();

    public abstract double getWindSpeed();

    public abstract int getOutsideAirTemperature();
}
