package org.ekolab.server.model.content.lab3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 777Al on 11.04.2017.
 */
public enum City {
    MOSCOW{
        public List<FuelType> getFuelTypesForTheCity(){
            ArrayList<FuelType> FuelTypesForTheCity = new ArrayList<>();
            FuelTypesForTheCity.add(FuelType.DONETSK_COAL);
            FuelTypesForTheCity.add(FuelType.SULFUR_OIL);
            return FuelTypesForTheCity;
        }
        public double getWindSpeed(){return 4;}
    },
    SAINT_PETERSBURG{
        public List<FuelType> getFuelTypesForTheCity(){
            ArrayList<FuelType> FuelTypesForTheCity = new ArrayList<>();
            FuelTypesForTheCity.add(FuelType.SULFUR_OIL);
            FuelTypesForTheCity.add(FuelType.LENINGRAD_SHALE);
            FuelTypesForTheCity.add(FuelType.ESTONIAN_SHALE);
            return FuelTypesForTheCity;
        }
        public double getWindSpeed(){return 3.3;}
    },
    NOVOSIBIRSK{
        public List<FuelType> getFuelTypesForTheCity(){
            ArrayList<FuelType> FuelTypesForTheCity = new ArrayList<>();
            FuelTypesForTheCity.add(FuelType.EKIBASTUZ_COAL);
            FuelTypesForTheCity.add(FuelType.BEREZOVSKY_COAL_TSU);
            FuelTypesForTheCity.add(FuelType.BEREZOVSKY_COAL_ZHSHU);
            FuelTypesForTheCity.add(FuelType.KUZNETSK_COAL);
            return FuelTypesForTheCity;
        }
        public double getWindSpeed(){return 3.7;}
    },
    EKATERINBURG{
        public List<FuelType> getFuelTypesForTheCity(){
            ArrayList<FuelType> FuelTypesForTheCity = new ArrayList<>();
            FuelTypesForTheCity.add(FuelType.EKIBASTUZ_COAL);
            FuelTypesForTheCity.add(FuelType.SULFUR_OIL);
            FuelTypesForTheCity.add(FuelType.KUZNETSK_COAL);
            return FuelTypesForTheCity;
        }
        public double getWindSpeed(){return 4;}
    },
    NIZHNIY_NOVGOROD{
        public List<FuelType> getFuelTypesForTheCity(){
            ArrayList<FuelType> FuelTypesForTheCity = new ArrayList<>();
            FuelTypesForTheCity.add(FuelType.DONETSK_COAL);
            FuelTypesForTheCity.add(FuelType.SULFUR_OIL);
            return FuelTypesForTheCity;
        }
        public double getWindSpeed(){return 4.7;}
    },
    KRASNOYARSK{
        public List<FuelType> getFuelTypesForTheCity(){
            ArrayList<FuelType> FuelTypesForTheCity = new ArrayList<>();
            FuelTypesForTheCity.add(FuelType.EKIBASTUZ_COAL);
            FuelTypesForTheCity.add(FuelType.BEREZOVSKY_COAL_TSU);
            FuelTypesForTheCity.add(FuelType.BEREZOVSKY_COAL_ZHSHU);
            FuelTypesForTheCity.add(FuelType.KUZNETSK_COAL);
            return FuelTypesForTheCity;
        }
        public double getWindSpeed(){return 3.9;}
    },
    ROSTOV_NA_DONU{
        public List<FuelType> getFuelTypesForTheCity(){
            ArrayList<FuelType> FuelTypesForTheCity = new ArrayList<>();
            FuelTypesForTheCity.add(FuelType.DONETSK_COAL);
            FuelTypesForTheCity.add(FuelType.SULFUR_OIL);
            return FuelTypesForTheCity;
        }
        public double getWindSpeed(){return 6.6;}
    },
    KALININGRAD{
        public List<FuelType> getFuelTypesForTheCity(){
            ArrayList<FuelType> FuelTypesForTheCity = new ArrayList<>();
            FuelTypesForTheCity.add(FuelType.DONETSK_COAL);
            FuelTypesForTheCity.add(FuelType.SULFUR_OIL);
            return FuelTypesForTheCity;
        }
        public double getWindSpeed(){return 4.7;}
    },
    VLADIVOSTOK{
        public List<FuelType> getFuelTypesForTheCity(){
            ArrayList<FuelType> FuelTypesForTheCity = new ArrayList<>();
            FuelTypesForTheCity.add(FuelType.EKIBASTUZ_COAL);
            FuelTypesForTheCity.add(FuelType.BEREZOVSKY_COAL_TSU);
            FuelTypesForTheCity.add(FuelType.BEREZOVSKY_COAL_ZHSHU);
            FuelTypesForTheCity.add(FuelType.KUZNETSK_COAL);
            return FuelTypesForTheCity;
        }
        public double getWindSpeed(){return 7.1;}
    },
    SURGUT{
        public List<FuelType> getFuelTypesForTheCity(){
            ArrayList<FuelType> FuelTypesForTheCity = new ArrayList<>();
            FuelTypesForTheCity.add(FuelType.SULFUR_OIL);
            FuelTypesForTheCity.add(FuelType.STABILIZED_OIL);
            return FuelTypesForTheCity;
        }
        public double getWindSpeed(){return 5;}
    };
    public abstract List<FuelType> getFuelTypesForTheCity();
    public abstract double getWindSpeed();
}
