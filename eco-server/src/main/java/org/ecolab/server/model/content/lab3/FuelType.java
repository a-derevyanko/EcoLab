package org.ecolab.server.model.content.lab3;

import org.apache.commons.lang.math.RandomUtils;

/**
 * Created by 777Al on 11.04.2017.
 */
public enum FuelType {
    DONETSK_COAL {
        @Override
        public double getLowHeatValue() {
            return 19.59;
        }

        @Override
        public double getSulphurContent() {
            return 3.0;
        }

        @Override
        public double getAshContent() {
            return 21.8;
        }

        @Override
        public double getWaterContent() {
            return 13.0;
        }

        @Override
        public int getFlueGasNOxConcentration() {
            return 300 + RandomUtils.nextInt(4) * 50;
        }

        @Override
        public int getStackExitTemperature() {
            return 154 + RandomUtils.nextInt(11);
        }

        @Override
        public double getCarbonContent() {
            return 49.3;
        }

        @Override
        public double getHydrogenContent() {
            return 3.6;
        }

        @Override
        public double getNitrogenContent() {
            return 1.0;
        }

        @Override
        public double getOxygenContent() {
            return 8.3;
        }
    },
    EKIBASTUZ_COAL {
        public double getLowHeatValue() {
            return 16.75;
        }

        @Override
        public double getSulphurContent() {
            return 0.8;
        }

        @Override
        public double getAshContent() {
            return 31.8;
        }

        @Override
        public double getWaterContent() {
            return 7.0;
        }

        @Override
        public int getFlueGasNOxConcentration() {
            return 300 + RandomUtils.nextInt(4) * 50;
        }

        @Override
        public int getStackExitTemperature() {
            return 134 + RandomUtils.nextInt(11);
        }

        @Override
        public double getCarbonContent() {
            return 43.4;
        }

        @Override
        public double getHydrogenContent() {
            return 2.9;
        }

        @Override
        public double getNitrogenContent() {
            return 0.8;
        }

        @Override
        public double getOxygenContent() {
            return 7.0;
        }
    },
    KUZNETSK_COAL {
        public double getLowHeatValue() {
            return 22.82;
        }

        @Override
        public double getSulphurContent() {
            return 0.3;
        }

        @Override
        public double getAshContent() {
            return 13.2;
        }

        @Override
        public double getWaterContent() {
            return 12.0;
        }

        @Override
        public int getFlueGasNOxConcentration() {
            return 300 + RandomUtils.nextInt(4) * 50;
        }

        @Override
        public int getStackExitTemperature() {
            return 116 + RandomUtils.nextInt(11);
        }

        @Override
        public double getCarbonContent() {
            return 58.7;
        }

        @Override
        public double getHydrogenContent() {
            return 4.2;
        }

        @Override
        public double getNitrogenContent() {
            return 1.9;
        }

        @Override
        public double getOxygenContent() {
            return 9.7;
        }
    },
    BEREZOVSKY_COAL_TSU {
        public double getLowHeatValue() {
            return 15.66;
        }

        @Override
        public double getSulphurContent() {
            return 0.3;
        }

        @Override
        public double getAshContent() {
            return 13.2;
        }

        @Override
        public double getWaterContent() {
            return 12.0;
        }

        @Override
        public int getFlueGasNOxConcentration() {
            return 250 + RandomUtils.nextInt(4) * 50;
        }

        @Override
        public int getStackExitTemperature() {
            return 133 + RandomUtils.nextInt(11);
        }

        @Override
        public double getCarbonContent() {
            return 44.3;
        }

        @Override
        public double getHydrogenContent() {
            return 3.0;
        }

        @Override
        public double getNitrogenContent() {
            return 0.4;
        }

        @Override
        public double getOxygenContent() {
            return 14.4;
        }
    },
    BEREZOVSKY_COAL_ZHSHU {
        public double getLowHeatValue() {
            return 15.66;
        }

        @Override
        public double getSulphurContent() {
            return 0.3;
        }

        @Override
        public double getAshContent() {
            return 13.2;
        }

        @Override
        public double getWaterContent() {
            return 12;
        }

        @Override
        public int getFlueGasNOxConcentration() {
            return 300 + RandomUtils.nextInt(4) * 50;
        }

        @Override
        public int getStackExitTemperature() {
            return 133 + RandomUtils.nextInt(11);
        }

        @Override
        public double getCarbonContent() {
            return 44.3;
        }

        @Override
        public double getHydrogenContent() {
            return 3.0;
        }

        @Override
        public double getNitrogenContent() {
            return 0.4;
        }

        @Override
        public double getOxygenContent() {
            return 14.4;
        }
    },
    ESTONIAN_SHALE {
        @Override
        public double getLowHeatValue() {
            return 10.93;
        }

        @Override
        public double getSulphurContent() {
            return 1.6;
        }

        @Override
        public double getAshContent() {
            return 54.4;
        }

        @Override
        public double getWaterContent() {
            return 13.0;
        }

        @Override
        public int getFlueGasNOxConcentration() {
            return 300 + RandomUtils.nextInt(4) * 50;
        }

        @Override
        public int getStackExitTemperature() {
            return 151 + RandomUtils.nextInt(11);
        }

        @Override
        public double getCarbonContent() {
            return 24.1;
        }

        @Override
        public double getHydrogenContent() {
            return 3.1;
        }

        @Override
        public double getNitrogenContent() {
            return 0.1;
        }

        @Override
        public double getOxygenContent() {
            return 3.7;
        }

    },
    LENINGRAD_SHALE {
        @Override
        public double getLowHeatValue() {
            return 5.82;
        }

        @Override
        public double getSulphurContent() {
            return 1.7;
        }

        @Override
        public double getAshContent() {
            return 60.6;
        }

        @Override
        public double getWaterContent() {
            return 11.5;
        }

        @Override
        public int getFlueGasNOxConcentration() {
            return 300 + RandomUtils.nextInt(4) * 50;
        }

        @Override
        public int getStackExitTemperature() {
            return 154 + RandomUtils.nextInt(11);
        }

        @Override
        public double getCarbonContent() {
            return 20.6;
        }

        @Override
        public double getHydrogenContent() {
            return 2.7;
        }

        @Override
        public double getNitrogenContent() {
            return 0.1;
        }

        @Override
        public double getOxygenContent() {
            return 2.8;
        }
    },
    SULFUR_OIL {
        @Override
        public double getLowHeatValue() {
            return 39.73;
        }

        @Override
        public double getSulphurContent() {
            return 1.4;
        }

        @Override
        public double getAshContent() {
            return 0.1;
        }

        @Override
        public double getWaterContent() {
            return 3.0;
        }

        @Override
        public int getFlueGasNOxConcentration() {
            return 200 + RandomUtils.nextInt(4) * 50;
        }

        @Override
        public int getStackExitTemperature() {
            return 137 + RandomUtils.nextInt(11);
        }

        @Override
        public double getCarbonContent() {
            return 83.8;
        }

        @Override
        public double getHydrogenContent() {
            return 11.2;
        }

        @Override
        public double getNitrogenContent() {
            return 0.0;
        }

        @Override
        public double getOxygenContent() {
            return 0.5;
        }
    },
    STABILIZED_OIL {
        @Override
        public double getLowHeatValue() {
            return 39.77;
        }

        @Override
        public double getSulphurContent() {
            return 2.9;
        }

        @Override
        public double getAshContent() {
            return 0.1;
        }

        @Override
        public double getWaterContent() {
            return 3.0;
        }

        @Override
        public int getFlueGasNOxConcentration() {
            return 200 + RandomUtils.nextInt(4) * 50;
        }

        @Override
        public int getStackExitTemperature() {
            return 149 + RandomUtils.nextInt(11);
        }

        @Override
        public double getCarbonContent() {
            return 81.8;
        }

        @Override
        public double getHydrogenContent() {
            return 11.8;
        }

        @Override
        public double getNitrogenContent() {
            return 0.0;
        }

        @Override
        public double getOxygenContent() {
            return 0.4;
        }
    };


    public abstract double getLowHeatValue();


    public abstract double getSulphurContent();

    public abstract double getAshContent();

    public abstract double getWaterContent();

    public abstract int getFlueGasNOxConcentration();

    public abstract int getStackExitTemperature();

    public abstract double getCarbonContent();

    public abstract double getHydrogenContent();

    public abstract double getNitrogenContent();

    public abstract double getOxygenContent();

}

