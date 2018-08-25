package org.ecolab.server.model.content.lab3;

/**
 * Created by 777Al on 11.04.2017.
 */
public enum NumberOfStacks implements Valued<Integer> {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4);
    private final int numberOfStacks;

    NumberOfStacks(int numberOfStacks) {
        this.numberOfStacks = numberOfStacks;
    }

    @Override
    public Integer value() {
        return numberOfStacks;
    }

    public static NumberOfStacks valueOf(int numberOfStacks) {
        for (NumberOfStacks value : values()) {
            if (value.value() == numberOfStacks) {
                return value;
            }
        }
        throw new IllegalArgumentException("Wrong number of stacks: " + numberOfStacks);
    }
}
