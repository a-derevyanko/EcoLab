package org.ekolab.server.model;

/**
 * Created by 777Al on 11.04.2017.
 */
public enum NumberOfStacks {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4);
    private final int numberOfStacks;

    NumberOfStacks(int numberOfStacks) {
        this.numberOfStacks = numberOfStacks;
    }

    public int getNumberOfStacks() {
        return numberOfStacks;
    }
}
