package org.ecolab.server.common;

import org.apache.commons.math3.util.Precision;

/**
 * Created by 777Al on 12.07.2017.
 */
public abstract class MathUtils {
    private static final double EPS = 0.001;

    /**
     * Проверяет равенство двух чисел
     * @param a первое число
     * @param b второе число
     * @return признак равенства
     */
    public static boolean checkEquals(double a, double b) {
        return checkEquals(a, b, EPS);
    }

    /**
     * Проверяет равенство двух чисел
     * @param a первое число
     * @param b второе число
     * @return признак равенства
     */
    public static boolean roundedCheckEquals(double a, double b, int precision) {
        return checkEquals(Precision.round(a, precision), Precision.round(b, precision), EPS);
    }

    /**
     * Проверяет равенство двух чисел
     * @param a первое число
     * @param b второе число
     * @param eps погрешность
     * @return признак равенства
     */
    public static boolean checkEquals(double a, double b, double eps) {
        return Math.abs(a - b) < eps;
    }
}
