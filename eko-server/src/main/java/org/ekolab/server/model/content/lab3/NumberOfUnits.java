package org.ekolab.server.model.content.lab3;


/**
 * Created by 777Al on 11.04.2017.
 */
public enum NumberOfUnits {
    TWO(2),
    FOUR(4),
    SIX(6),
    EIGHT(8);
    private final int numberOfUnits;

    NumberOfUnits(int numberOfUnits) {
        this.numberOfUnits = numberOfUnits;
    }

    public int value() {
        return numberOfUnits;
    }

    public static NumberOfUnits valueOf(int numberOfUnits) {
        for (NumberOfUnits value : values()) {
            if (value.value() == numberOfUnits) {
                return value;
            }
        }
        throw new IllegalArgumentException("Wrong number of units: " + numberOfUnits);
    }
}
