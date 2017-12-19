package org.ekolab.server.model.content.lab2;

import org.ekolab.server.model.content.lab3.Valued;

public enum Frequency implements Valued<Double> {
    F_315(31.5),
    F_63(63.0),
    F_125(125.0),
    F_250(250.0),
    F_500(500.0),
    F_1000(1000.0),
    F_2000(2000.0),
    F_4000(4000.0),
    F_8000(8000.0);

    private final double value;

    Frequency(double value) {
        this.value = value;
    }

    @Override
    public Double value() {
        return value;
    }

    public static Frequency valueOf(double value) {
        for (Frequency e : values()) {
            if (e.value == value) {
                return e;
            }
        }
        throw new IllegalArgumentException("Wrong value: " + value);
    }
}
