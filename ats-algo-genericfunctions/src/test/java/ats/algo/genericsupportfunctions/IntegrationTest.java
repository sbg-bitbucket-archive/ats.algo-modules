package ats.algo.genericsupportfunctions;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IntegrationTest {

    @Test
    public void test() {
        int n = 4;
        double mean = 7.0;
        double stdDevn = 3.0;
        double[] moments = Integration.normalMoments(n, mean, stdDevn);
        assertEquals(1.0, moments[0], 0.0001);
        assertEquals(mean, moments[1], 0.0001);
        assertEquals(mean * mean + stdDevn * stdDevn, moments[2], 0.0001);
        assertEquals(mean * (mean * mean + 3 * stdDevn * stdDevn), moments[3], 0.0001);

    }

}
