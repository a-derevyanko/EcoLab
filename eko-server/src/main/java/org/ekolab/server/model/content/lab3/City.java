package org.ekolab.server.model.content.lab3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 777Al on 11.04.2017.
 */
public enum City {
    MOSCOW{
        @Override
        public List<FuelType> getFuelTypesForTheCity(){
            ArrayList<FuelType> FuelTypesForTheCity = new ArrayList<>();
            FuelTypesForTheCity.add(FuelType.DONETSK_COAL);
            FuelTypesForTheCity.add(FuelType.SULFUR_OIL);
            return FuelTypesForTheCity;
        }
    },
    SAINT_PETERSBURG{
        @Override
        public List<FuelType> getFuelTypesForTheCity(){
            ArrayList<FuelType> FuelTypesForTheCity = new ArrayList<>();
            FuelTypesForTheCity.add(FuelType.SULFUR_OIL);
            FuelTypesForTheCity.add(FuelType.LENINGRAD_SHALE);
            FuelTypesForTheCity.add(FuelType.ESTONIAN_SHALE);
            return FuelTypesForTheCity;
        }
    },
    NOVOSIBIRSK{
        @Override
        public List<FuelType> getFuelTypesForTheCity(){
            ArrayList<FuelType> FuelTypesForTheCity = new ArrayList<>();
            FuelTypesForTheCity.add(FuelType.EKIBASTUZ_COAL);
            FuelTypesForTheCity.add(FuelType.BEREZOVSKY_COAL_TSU);
            FuelTypesForTheCity.add(FuelType.BEREZOVSKY_COAL_ZHSHU);
            FuelTypesForTheCity.add(FuelType.KUZNETSK_COAL);
            return FuelTypesForTheCity;
        }
    },
    EKATERINBURG{
        @Override
        public List<FuelType> getFuelTypesForTheCity(){
            ArrayList<FuelType> FuelTypesForTheCity = new ArrayList<>();
            FuelTypesForTheCity.add(FuelType.EKIBASTUZ_COAL);
            FuelTypesForTheCity.add(FuelType.SULFUR_OIL);
            FuelTypesForTheCity.add(FuelType.KUZNETSK_COAL);
            return FuelTypesForTheCity;
        }
    },
    NIZHNIY_NOVGOROD{
        @Override
        public List<FuelType> getFuelTypesForTheCity(){
            ArrayList<FuelType> FuelTypesForTheCity = new ArrayList<>();
            FuelTypesForTheCity.add(FuelType.DONETSK_COAL);
            FuelTypesForTheCity.add(FuelType.SULFUR_OIL);
            return FuelTypesForTheCity;
        }
    },
    KRASNOYARSK{
        @Override
        public List<FuelType> getFuelTypesForTheCity(){
            ArrayList<FuelType> FuelTypesForTheCity = new ArrayList<>();
            FuelTypesForTheCity.add(FuelType.EKIBASTUZ_COAL);
            FuelTypesForTheCity.add(FuelType.BEREZOVSKY_COAL_TSU);
            FuelTypesForTheCity.add(FuelType.BEREZOVSKY_COAL_ZHSHU);
            FuelTypesForTheCity.add(FuelType.KUZNETSK_COAL);
            return FuelTypesForTheCity;
        }
    },
    ROSTOV_NA_DONU{
        @Override
        public List<FuelType> getFuelTypesForTheCity(){
            ArrayList<FuelType> FuelTypesForTheCity = new ArrayList<>();
            FuelTypesForTheCity.add(FuelType.DONETSK_COAL);
            FuelTypesForTheCity.add(FuelType.SULFUR_OIL);
            return FuelTypesForTheCity;
        }
    },
    KALININGRAD{
        @Override
        public List<FuelType> getFuelTypesForTheCity(){
            ArrayList<FuelType> FuelTypesForTheCity = new ArrayList<>();
            FuelTypesForTheCity.add(FuelType.DONETSK_COAL);
            FuelTypesForTheCity.add(FuelType.SULFUR_OIL);
            return FuelTypesForTheCity;
        }
    },
    VLADIVOSTOK{
        @Override
        public List<FuelType> getFuelTypesForTheCity(){
            ArrayList<FuelType> FuelTypesForTheCity = new ArrayList<>();
            FuelTypesForTheCity.add(FuelType.EKIBASTUZ_COAL);
            FuelTypesForTheCity.add(FuelType.BEREZOVSKY_COAL_TSU);
            FuelTypesForTheCity.add(FuelType.BEREZOVSKY_COAL_ZHSHU);
            FuelTypesForTheCity.add(FuelType.KUZNETSK_COAL);
            return FuelTypesForTheCity;
        }
    },
    SURGUT{
        @Override
        public List<FuelType> getFuelTypesForTheCity(){
            ArrayList<FuelType> FuelTypesForTheCity = new ArrayList<>();
            FuelTypesForTheCity.add(FuelType.SULFUR_OIL);
            FuelTypesForTheCity.add(FuelType.STABILIZED_OIL);
            return FuelTypesForTheCity;
        }
    };
    public List<FuelType> getFuelTypesForTheCity(){
        ArrayList<FuelType> FuelTypesForTheCity = new ArrayList<>();
        return FuelTypesForTheCity;
    }
}
