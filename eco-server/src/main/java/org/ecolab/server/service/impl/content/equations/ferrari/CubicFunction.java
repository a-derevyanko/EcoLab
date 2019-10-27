package org.ecolab.server.service.impl.content.equations.ferrari;

public class CubicFunction implements EquationFunction {
    private double a;
    private double b;
    private double c;
    private double d;

    /**
     * Construct a new Cubic object.
     *
     * @param a Coefficient of <I>x</I><SUP>3</SUP>.
     * @param b Coefficient of <I>x</I><SUP>2</SUP>.
     * @param c Coefficient of <I>x</I>.
     * @param d Constant coefficient.
     */
    public CubicFunction(double a, double b, double c, double d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;

        // Verify preconditions.
        if (this.a == 0.0) {
            throw new IllegalArgumentException("Cubic.solve(): a = 0");
        }

        // Normalize coefficients.
        var denom = this.a;

        this.a = this.b / denom;
        this.b = this.c / denom;
        this.c = this.d / denom;
    }

    /**
     * Solve the cubic equation with the given coefficients.
     */
    @Override
    public double[] findRealRoots() {
        int nRoots;

        double x1;
        double x2;
        double x3;


        // Commence solution.
        var a_over_3 = a / 3.0;
        var Q = (3 * b - a * a) / 9.0;
        var Q_CUBE = Q * Q * Q;
        var R = (9 * a * b - 27 * c - 2 * a * a * a) / 54.0;
        var R_SQR = R * R;
        var D = Q_CUBE + R_SQR;

        if (D < 0.0) {
            // Three unequal real roots.
            nRoots = 3;
            var theta = Math.acos(R / Math.sqrt(-Q_CUBE));
            var SQRT_Q = Math.sqrt(-Q);
            x1 = 2.0 * SQRT_Q * Math.cos(theta / 3.0) - a_over_3;
            x2 = 2.0 * SQRT_Q * Math.cos((theta + 2.0 * Math.PI) / 3.0) - a_over_3;
            x3 = 2.0 * SQRT_Q * Math.cos((theta + 4.0 * Math.PI) / 3.0) - a_over_3;
        } else if (D > 0.0) {
            // One real root.
            nRoots = 1;
            var SQRT_D = Math.sqrt(D);
            var S = Math.cbrt(R + SQRT_D);
            var T = Math.cbrt(R - SQRT_D);
            x1 = (S + T) - a_over_3;
            x2 = Double.NaN;
            x3 = Double.NaN;
        } else {
            // Three real roots, at least two equal.
            nRoots = 3;
            var CBRT_R = Math.cbrt(R);
            x1 = 2 * CBRT_R - a_over_3;
            x2 = x3 = CBRT_R - a_over_3;
        }

        if (x1 < x2) {
            var tmp = x1;
            x1 = x2;
            x2 = tmp;
        }
        if (x2 < x3) {
            var tmp = x2;
            x2 = x3;
            x3 = tmp;
        }
        if (x1 < x2) {
            var tmp = x1;
            x1 = x2;
            x2 = tmp;
        }

        return nRoots == 1 ? new double[]{x1} : new double[]{x1, x2, x3};
    }
}
