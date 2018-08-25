package org.ecolab.server.service.impl.content.equations;

import org.ecolab.server.service.impl.content.equations.ferrari.EquationFunction;
import org.ecolab.server.service.impl.content.equations.ferrari.QuarticFunction;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by 777Al on 17.05.2017.
 */
public class FunctionsTest {
    @Test
    public void testQuarticFunction1() {
        EquationFunction f = new QuarticFunction(45.1, 17, 12.8, 5, 0.5);
        double[] roots = f.findRealRoots();
        Assert.assertEquals(2, roots.length);
        Assert.assertEquals(Math.round(roots[0] * 1000000), -152063);
        Assert.assertEquals(Math.round(roots[1] * 1000000), -276333);
    }

    @Test
    public void testQuarticFunction2() {
        EquationFunction f = new QuarticFunction(45.1, 17, 12.8, 5, 0.1);
        double[] roots = f.findRealRoots();
        Assert.assertEquals(2, roots.length);
        Assert.assertEquals(Math.round(roots[0] * 1000000), -21111);
        Assert.assertEquals(Math.round(roots[1] * 1000000), -372023);
    }
}