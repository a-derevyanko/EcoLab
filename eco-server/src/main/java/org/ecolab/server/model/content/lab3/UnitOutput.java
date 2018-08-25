package org.ecolab.server.model.content.lab3;


import java.util.Arrays;
import java.util.List;

/**
 * Created by 777Al on 11.04.2017.
 */
public enum UnitOutput {
    O_200 {
        @Override
        public int getUnitOutput() {
            return 200;
        }

        @Override
        public List<NumberOfUnits> getNumberOfUnits() {
            return Arrays.asList(NumberOfUnits.SIX, NumberOfUnits.EIGHT);
        }

        @Override
        public List<Integer> getStacksHeights() {
            return Arrays.asList(120, 150, 180);
        }

        @Override
        public int getSteamProductionCapacity() {
            return 630;
        }

        @Override
        public int getBy(boolean isOil) {
            return isOil ? 342 : 364;
        }
    },
    O_300 {
        @Override
        public int getUnitOutput() {
            return 300;
        }

        @Override
        public List<NumberOfUnits> getNumberOfUnits() {
            return Arrays.asList(NumberOfUnits.FOUR, NumberOfUnits.SIX, NumberOfUnits.EIGHT);
        }

        @Override
        public List<Integer> getStacksHeights() {
            return Arrays.asList(120, 150, 180);
        }

        @Override
        public int getSteamProductionCapacity() {
            return 1050;
        }

        @Override
        public int getBy(boolean isOil) {
            return isOil ? 326 : 373;
        }
    },
    O_500 {
        @Override
        public int getUnitOutput() {
            return 500;
        }

        @Override
        public List<NumberOfUnits> getNumberOfUnits() {
            return Arrays.asList(NumberOfUnits.FOUR, NumberOfUnits.SIX);
        }

        @Override
        public List<Integer> getStacksHeights() {
            return Arrays.asList(150, 180, 210);
        }

        @Override
        public int getSteamProductionCapacity() {
            return 1650;
        }

        @Override
        public int getBy(boolean isOil) {
            return isOil ? 319 : 343;
        }
    },
    O_800 {
        @Override
        public int getUnitOutput() {
            return 800;
        }

        @Override
        public List<NumberOfUnits> getNumberOfUnits() {
            return Arrays.asList(NumberOfUnits.TWO, NumberOfUnits.FOUR);
        }

        @Override
        public List<Integer> getStacksHeights() {
            return Arrays.asList(180, 210, 240);
        }

        @Override
        public int getSteamProductionCapacity() {
            return 2450;
        }

        @Override
        public int getBy(boolean isOil) {
            return isOil ? 338 : 314;
        }
    };


    public abstract int getUnitOutput();

    public abstract List<NumberOfUnits> getNumberOfUnits();

    public abstract List<Integer> getStacksHeights();

    public abstract int getSteamProductionCapacity();

    public abstract int getBy(boolean isOil);
}
