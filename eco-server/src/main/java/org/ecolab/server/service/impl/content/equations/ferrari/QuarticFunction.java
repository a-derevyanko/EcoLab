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
        var depressedQuartic = toDepressed();
        if (depressedQuartic.isBiquadratic()) {
            var depressedRoots = depressedQuartic.solveUsingBiquadraticMethod();
            return reconvertToOriginalRoots(depressedRoots);
        }

        var y = findFerrariY(depressedQuartic);
        var originalRootConversionPart = -b / (4.0 * a);
        var firstPart = Math.sqrt(depressedQuartic.c + 2.0 * y);

        var positiveSecondPart = Math.sqrt(-(3.0 * depressedQuartic.c + 2.0 * y + 2.0 * depressedQuartic.d
                / Math.sqrt(depressedQuartic.c + 2.0 * y)));
        var negativeSecondPart = Math.sqrt(-(3.0 * depressedQuartic.c + 2.0 * y - 2.0 * depressedQuartic.d
                / Math.sqrt(depressedQuartic.c + 2.0 * y)));

        var x1 = originalRootConversionPart + (firstPart + positiveSecondPart) / 2.0;
        var x2 = originalRootConversionPart + (-firstPart + negativeSecondPart) / 2.0;
        var x3 = originalRootConversionPart + (firstPart - positiveSecondPart) / 2.0;
        var x4 = originalRootConversionPart + (-firstPart - negativeSecondPart) / 2.0;

        return Arrays.stream(new double[]{x1, x2, x3, x4}).filter(Double::isFinite).toArray();
    }

    private double[] reconvertToOriginalRoots(double[] depressedRoots) {
        var originalRoots = new double[depressedRoots.length];
        for (var i = 0; i < depressedRoots.length; ++i) {
            originalRoots[i] = depressedRoots[i] - b / (4.0 * a);
        }
        return originalRoots;
    }

    private double findFerrariY(QuarticFunction depressedQuartic) {
        var a3 = 1.0;
        var a2 = 5.0 / 2.0 * depressedQuartic.c;
        var a1 = 2.0 * Math.pow(depressedQuartic.c, 2.0) - depressedQuartic.e;
        var a0 = Math.pow(depressedQuartic.c, 3.0) / 2.0 - depressedQuartic.c * depressedQuartic.e / 2.0
                - Math.pow(depressedQuartic.d, 2.0) / 8.0;

        var cubicFunction = new CubicFunction(a3, a2, a1, a0);
        var roots = cubicFunction.findRealRoots();

        for (var y : roots) {
            if (depressedQuartic.c + 2.0 * y != 0.0) {
                return y;
            }
        }
        throw new IllegalStateException("Ferrari method should have at least one y");
    }

    private double[] solveUsingBiquadraticMethod() {
        var quadraticFunction = new QuadraticFunction(a, c, e);
        var quadraticRoots = quadraticFunction.findRealRoots();

        var roots = new double[0];
        for (var quadraticRoot : quadraticRoots) {
            if (quadraticRoot > 0.0) {
                var sqrt = Math.sqrt(quadraticRoot);
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
        var p = (8.0 * a * c - 3.0 * Math.pow(b, 2.0)) / (8.0 * Math.pow(a, 2.0));
        var q = (Math.pow(b, 3.0) - 4.0 * a * b * c + 8.0 * d * Math.pow(a, 2.0)) / (8.0 * Math.pow(a, 3.0));
        var r = (-3.0 * Math.pow(b, 4.0) + 256.0 * e * Math.pow(a, 3.0) - 64.0 * d * b * Math.pow(a, 2.0) + 16.0 * c
                * a * Math.pow(b, 2.0))
                / (256.0 * Math.pow(a, 4.0));
        return new QuarticFunction(1.0, 0.0, p, q, r);
    }
}