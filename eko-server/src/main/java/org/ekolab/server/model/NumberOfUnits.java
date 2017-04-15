package org.ekolab.server.model;

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

    public int getNumberOfUnits() {
        return numberOfUnits;
    }
}
