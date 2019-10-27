package org.ecolab.server.service.impl.content.equations.ferrari;

public class QuadraticFunction implements EquationFunction {
    private double a;
    private double b;
    private double c;

    /**
     * Construct a new Cubic object.
     *
     * @param a Coefficient of <I>x</I><SUP>2</SUP>.
     * @param b Coefficient of <I>x</I>.
     * @param c Constant coefficient.
     */
    public QuadraticFunction(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    /**
     * Solve the quadratic equation with the given coefficients.
     */
    @Override
    public double[] findRealRoots() {
        var discriminant = Math.sqrt(b * b - 4 * a * c);

        if (discriminant < 0) {
            return new double[0];
        } else {
            var x1 = (-b + discriminant) / (2 * a);
            var x2 = (-b - discriminant) / (2 * a);
            return new double[]{x1, x2};
        }
    }
}
