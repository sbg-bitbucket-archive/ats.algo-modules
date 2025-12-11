package ats.algo.genericsupportfunctions;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FactorialTest {

    @Test
    public void test() {
        assertEquals(24, Factorial.calc(4));
        assertEquals(1, Factorial.calc(1));
        assertEquals(1, Factorial.calc(0));
    }
}
