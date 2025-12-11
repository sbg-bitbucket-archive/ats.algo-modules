package ats.algo.inplayGS;

import static org.junit.Assert.*;

import org.junit.Test;

public class OddsLadderTest {

    @Test
    public void test() {
        double[] testOddsLadder = {1, 1.2, 1.5, 6, 50};
        OddsLadder oddsLadder = new OddsLadder(testOddsLadder);
        assertEquals(1.0, oddsLadder.getPriceFromOddsLadder(1.0), 0.0001);
        assertEquals(1.2, oddsLadder.getPriceFromOddsLadder(1.1), 0.0001);
        assertEquals(1.5, oddsLadder.getPriceFromOddsLadder(1.5), 0.0001);
        assertEquals(6, oddsLadder.getPriceFromOddsLadder(1.5 + 1.0E-10), 0.0001);
        assertEquals(50, oddsLadder.getPriceFromOddsLadder(8), 0.0001);
        assertEquals(50, oddsLadder.getPriceFromOddsLadder(1000), 0.0001);
        boolean exceptionGenerated;
        try {
            oddsLadder.getPriceFromOddsLadder(0.9);
            exceptionGenerated = false;
        } catch (IllegalArgumentException e) {
            exceptionGenerated = true;
        }
        assertTrue(exceptionGenerated);

    }

}
