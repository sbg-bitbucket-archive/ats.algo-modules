package ats.algo.genericsupportfunctions;

import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.genericsupportfunctions.Minimiser;
import ats.algo.genericsupportfunctions.Minimiser.FunctionToMinimise;
import ats.algo.genericsupportfunctions.Minimiser.MinimiserResults;

public class MinimiserTest {

    private double testFunction1(double[] x) {
        double cost = 0;
        for (int i = 0; i < x.length; i++)
            cost += (x[i] - i) * (x[i] - i);
        return cost + 1;
    }

    /**
     * minimised when x[i]= pi, min value is 1.
     * 
     * @param x
     * @return
     */
    private double testFunction2(double[] x) {

        double cost = 1;
        for (int i = 0; i < x.length; i++) {
            double z = Math.cos(x[i]);
            cost += z * z;
        }
        return cost;
    }

    @Test
    public void test() {
        double[] x = new double[2];
        x[0] = 1;
        x[1] = 0;
        FunctionToMinimise fn = (double[] z) -> testFunction1(z);
        Minimiser nelderMead = new Minimiser();
        MinimiserResults results = nelderMead.minimise(x, 0.5, 0.0001, fn);
        System.out.printf("success: %b, nIteration: %d, f(x): %.3f, x[0]: %.3f, x[1]: %.3f\n", results.success,
                        results.nIterations, results.functionValueAtMinimum, results.bestX[0], results.bestX[1]);
        assertEquals(0, results.bestX[0], 0.008);
        assertEquals(1, results.bestX[1], 0.008);
    }

    @Test
    public void test2() {
        double[] x = new double[4];
        x[0] = 1;
        x[1] = 1;
        x[2] = -1;
        x[3] = 1;
        System.out.println(testFunction2(x));
        FunctionToMinimise fn = (double[] z) -> testFunction2(z);
        Minimiser nelderMead = new Minimiser();
        MinimiserResults results = nelderMead.minimise(x, 0.1, 0.0001, fn);
        System.out.printf("success: %b, nIteration: %d, f(x): %.3f, x[0]: %.3f, x[1]: %.3f, x[2]: %.3f, x[3]: %.3f\n",
                        results.success, results.nIterations, results.functionValueAtMinimum, results.bestX[0],
                        results.bestX[1], results.bestX[2], results.bestX[2]);
        double halfPi = 3.142 / 2;
        assertEquals(halfPi, results.bestX[0], 0.008);
        assertEquals(halfPi, results.bestX[1], 0.008);
        assertEquals(-halfPi, results.bestX[2], 0.008);
        assertEquals(halfPi, results.bestX[3], 0.008);
    }
}
