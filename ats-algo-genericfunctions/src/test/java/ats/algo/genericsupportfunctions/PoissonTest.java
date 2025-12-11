package ats.algo.genericsupportfunctions;

import static org.junit.Assert.*;

import org.junit.Test;

public class PoissonTest {

    @Test
    public void test() {
        assertEquals(.1736, Poisson.probUnder(4.5, 2), 0.0001);
        assertEquals(.7649, Poisson.probUnder(7.6, 9), 0.0001);
        assertEquals(.1798, Poisson.probUnder(50.0, 43), 0.0001);
        assertEquals(.1714, Poisson.probUnder(100.0, 90), 0.001);
    }

}
