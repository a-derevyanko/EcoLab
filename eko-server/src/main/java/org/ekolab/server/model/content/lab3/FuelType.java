package org.ekolab.server.model.content.lab3;

/**
 * Created by 777Al on 11.04.2017.
 */
public enum FuelType {
    DONETSK_COAL {
        public double getLowHeatValue(){return 19.59;}
    },
    EKIBASTUZ_COAL {
        public double getLowHeatValue(){return 16.75;}
    },
    KUZNETSK_COAL {
        public double getLowHeatValue(){return 22.82;}
    },
    BEREZOVSKY_COAL_TSU {
        public double getLowHeatValue(){return 15.66;}
    },
    BEREZOVSKY_COAL_ZHSHU {
        public double getLowHeatValue(){return 15.66;}
    },
    ESTONIAN_SHALE {
        public double getLowHeatValue(){return 10.93;}
    },
    LENINGRAD_SHALE {
        public double getLowHeatValue(){return 5.82;}
    },
    SULFUR_OIL {
        public double getLowHeatValue(){return 39.73;}
    },
    STABILIZED_OIL{
        public double getLowHeatValue(){return 39.77;}
    };


    public abstract double getLowHeatValue();
}

