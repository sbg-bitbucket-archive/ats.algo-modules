package ats.algo.genericsupportfunctions;

import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.genericsupportfunctions.Odds;

public class OddsTest {

    @Test

    public void TestPrices() {
        Odds prices = new Odds(2, 2);
        assertEquals(prices.overround, 0, 0.00001);
        assertEquals(prices.Prob1, 0.5, 0.00001);
        assertEquals(prices.Prob2, 0.5, 0.00001);
        assertEquals(prices.Prob3, 0.0, 0.00001);
        assertTrue(prices.IsValid);
        prices = new Odds(2.1, 2.1);
        prices = new Odds(3, 1.4);
        assertTrue(prices.IsValid);
        assertEquals(prices.overround, 0.048, 0.001);
        assertEquals(prices.Prob1, 0.318, 0.001);
        assertEquals(prices.Prob2, 0.682, 0.001);
        assertEquals(prices.Prob3, 0.0, 0.001);
        prices = new Odds(4, 3.0, 1.4);
        assertTrue(prices.IsValid);
        assertEquals(prices.overround, 0.048, 0.001);
        assertEquals(prices.Prob1, 0.318, 0.001);
        assertEquals(prices.Prob2, 0.682, 0.001);
        assertEquals(prices.Prob3, 0.0, 0.001);
        assertEquals(4, prices.LineIdentifier);
        prices = new Odds(3.0, 2.5, 2.1);
        assertTrue(prices.IsValid);
        assertEquals(prices.overround, 0.210, 0.001);
        assertEquals(prices.Prob1, 0.276, 0.001);
        assertEquals(prices.Prob2, 0.331, 0.001);
        assertEquals(prices.Prob3, 0.394, 0.001);
        assertEquals(-1, prices.LineIdentifier);
    }
}
