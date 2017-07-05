package org.ekolab.server.model.content.lab3;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by 777Al on 11.04.2017.
 */
public enum NumberOfUnits {
    TWO {
        @Override
        public int value() {
            return 2;
        }

        @Override
        public List<NumberOfStacks> getStacksCounts() {
            return Arrays.asList(NumberOfStacks.ONE, NumberOfStacks.TWO);
        }
    },
    FOUR {
        @Override
        public int value() {
            return 4;
        }

        @Override
        public List<NumberOfStacks> getStacksCounts() {
            return Collections.singletonList(NumberOfStacks.TWO);
        }
    },
    SIX {
        @Override
        public int value() {
            return 6;
        }

        @Override
        public List<NumberOfStacks> getStacksCounts() {
            return Collections.singletonList(NumberOfStacks.THREE);
        }
    },
    EIGHT {
        @Override
        public int value() {
            return 8;
        }

        @Override
        public List<NumberOfStacks> getStacksCounts() {
            return Arrays.asList(NumberOfStacks.TWO, NumberOfStacks.FOUR);
        }
    };

    public abstract int value();

    public abstract List<NumberOfStacks> getStacksCounts();

    public static NumberOfUnits valueOf(int numberOfUnits) {
        for (NumberOfUnits value : values()) {
            if (value.value() == numberOfUnits) {
                return value;
            }
        }
        throw new IllegalArgumentException("Wrong number of units: " + numberOfUnits);
    }
}
