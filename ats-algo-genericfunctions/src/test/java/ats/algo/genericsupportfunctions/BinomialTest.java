package ats.algo.genericsupportfunctions;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BinomialTest {

    @Test
    public void test() {
        Binomial b = new Binomial(20);
        assertEquals(5, b.coeff(5, 1));
        assertEquals(6, b.coeff(4, 2));
        assertEquals(190, b.coeff(20, 2));
    }

}
