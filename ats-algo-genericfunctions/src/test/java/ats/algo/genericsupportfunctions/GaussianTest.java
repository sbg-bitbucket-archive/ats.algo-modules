package ats.algo.genericsupportfunctions;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ats.algo.genericsupportfunctions.Gaussian;

public class GaussianTest {

    @Test
    public void test() {
        Gaussian g = new Gaussian(2, 1);
        double z = g.pdf(1);
        assertEquals(.2419, z, 0.0001);
        z = g.cdf(4);
        assertEquals(.97725, z, 0.0001);
    }

    @Test
    public void testNextRandom() {
        Gaussian g = new Gaussian(0, 1);
        int N = 500000;
        int n0 = 0;
        int n1 = 0;
        int n2 = 0;
        for (int i = 0; i < N; i++) {
            double rnd = g.nextRandom();
            if (rnd < 0)
                n0++;
            if (rnd < 1)
                n1++;
            if (rnd < 2)
                n2++;
        }
        double p0 = ((double) n0) / N;
        double p1 = ((double) n1) / N;
        double p2 = ((double) n2) / N;
        assertEquals(.5, p0, .005);
        assertEquals(.8413, p1, .005);
        assertEquals(.9777, p2, .005);
    }

    @Test
    public void testCopy() {
        Gaussian g = new Gaussian(0, 1);
        g.setBias(.2);
        g.setApplyBias(true);
        Gaussian g2 = g.copy();
        assertEquals(g, g2);
    }

}
