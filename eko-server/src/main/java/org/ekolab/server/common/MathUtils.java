package org.ekolab.server.common;

/**
 * Created by 777Al on 12.07.2017.
 */
public abstract class MathUtils {
    private static final double EPS = 0.00001;

    /**
     * Проверяет равенство двух чисел
     * @param a первое число
     * @param b второе число
     * @return признак равенства
     */
    public static boolean checkEquals(double a, double b) {
        return Math.abs(a - b) < EPS;
    }
}
