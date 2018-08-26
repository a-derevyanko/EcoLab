package org.ecolab.server.service.impl.content.equations.ferrari;

import org.apache.commons.lang3.ArrayUtils;
import org.ecolab.server.dev.LogExecutionTime;

import java.util.Arrays;

public class QuarticFunction implements EquationFunction {

    private static final double NEAR_ZERO = 0.000001;

    private final double a;
    private final double b;
    private final double c;
    private final double d;
    private final double e;

    public QuarticFunction(double a, double b, double c, double d, double e) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
    }

    @Override
    @LogExecutionTime(500)
    public final double[] findRealRoots() {
        if (Math.abs(a) < NEAR_ZERO) {
            return new CubicFunction(b, c, d, e).findRealRoots();
        }

        if (isBiquadratic()) {
            return solveUsingBiquadraticMethod();
        }

        return solveUsingFerrariMethod();
    }


    private boolean isBiquadratic() {
        return Double.compare(b, 0) == 0 && Double.compare(d, 0) == 0;
    }

    /**
     * @see <a href="http://en.wikipedia.org/wiki/Quartic_function#Ferrari.27s_solution">Ferrari solution</a>
     * @return корни уравнения
     */
    private double[] solveUsingFerrariMethod() {
        QuarticFunction depressedQuartic = toDepressed();
        if (depressedQuartic.isBiquadratic()) {
            double[] depressedRoots = depressedQuartic.solveUsingBiquadraticMethod();
            return reconvertToOriginalRoots(depressedRoots);
        }

        double y = findFerrariY(depressedQuartic);
        double originalRootConversionPart = -b / (4.0 * a);
        double firstPart = Math.sqrt(depressedQuartic.c + 2.0 * y);

        double positiveSecondPart = Math.sqrt(-(3.0 * depressedQuartic.c + 2.0 * y + 2.0 * depressedQuartic.d
                / Math.sqrt(depressedQuartic.c + 2.0 * y)));
        double negativeSecondPart = Math.sqrt(-(3.0 * depressedQuartic.c + 2.0 * y - 2.0 * depressedQuartic.d
                / Math.sqrt(depressedQuartic.c + 2.0 * y)));

        double x1 = originalRootConversionPart + (firstPart + positiveSecondPart) / 2.0;
        double x2 = originalRootConversionPart + (-firstPart + negativeSecondPart) / 2.0;
        double x3 = originalRootConversionPart + (firstPart - positiveSecondPart) / 2.0;
        double x4 = originalRootConversionPart + (-firstPart - negativeSecondPart) / 2.0;

        return Arrays.stream(new double[]{x1, x2, x3, x4}).filter(Double::isFinite).toArray();
    }

    private double[] reconvertToOriginalRoots(double[] depressedRoots) {
        double[] originalRoots = new double[depressedRoots.length];
        for (int i = 0; i < depressedRoots.length; ++i) {
            originalRoots[i] = depressedRoots[i] - b / (4.0 * a);
        }
        return originalRoots;
    }

    private double findFerrariY(QuarticFunction depressedQuartic) {
        double a3 = 1.0;
        double a2 = 5.0 / 2.0 * depressedQuartic.c;
        double a1 = 2.0 * Math.pow(depressedQuartic.c, 2.0) - depressedQuartic.e;
        double a0 = Math.pow(depressedQuartic.c, 3.0) / 2.0 - depressedQuartic.c * depressedQuartic.e / 2.0
                - Math.pow(depressedQuartic.d, 2.0) / 8.0;

        CubicFunction cubicFunction = new CubicFunction(a3, a2, a1, a0);
        double[] roots = cubicFunction.findRealRoots();

        for (double y : roots) {
            if (depressedQuartic.c + 2.0 * y != 0.0) {
                return y;
            }
        }
        throw new IllegalStateException("Ferrari method should have at least one y");
    }

    private double[] solveUsingBiquadraticMethod() {
        QuadraticFunction quadraticFunction = new QuadraticFunction(a, c, e);
        double[] quadraticRoots = quadraticFunction.findRealRoots();

        double[] roots = new double[0];
        for (double quadraticRoot : quadraticRoots) {
            if (quadraticRoot > 0.0) {
                double sqrt = Math.sqrt(quadraticRoot);
                roots = ArrayUtils.addAll(roots, sqrt, -sqrt);
            } else if (quadraticRoot == 0.0 && !ArrayUtils.contains(roots, quadraticRoot)) {
                roots = ArrayUtils.add(roots, quadraticRoot);
            }
        }

        return roots;
    }

    /**
     * @see <a href="http://en.wikipedia.org/wiki/Quartic_function#Converting_to_a_depressed_quartic">Converting to a depressed quartic</a>
     * @return конвертированная функция
     */
    private QuarticFunction toDepressed() {
        double p = (8.0 * a * c - 3.0 * Math.pow(b, 2.0)) / (8.0 * Math.pow(a, 2.0));
        double q = (Math.pow(b, 3.0) - 4.0 * a * b * c + 8.0 * d * Math.pow(a, 2.0)) / (8.0 * Math.pow(a, 3.0));
        double r = (-3.0 * Math.pow(b, 4.0) + 256.0 * e * Math.pow(a, 3.0) - 64.0 * d * b * Math.pow(a, 2.0) + 16.0 * c
                * a * Math.pow(b, 2.0))
                / (256.0 * Math.pow(a, 4.0));
        return new QuarticFunction(1.0, 0.0, p, q, r);
    }
}