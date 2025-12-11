package ats.algo.margining;

import static org.junit.Assert.*;

import org.junit.Test;

public class MarginingEntropyTest {

    @Test
    public void testAddMargin() {
        double[] probs = {.1, .3, .6};
        double[] prices = MarginingEntropy.addMargin(probs, .09);
        assertEquals(8.12, prices[0], 0.005);
        assertEquals(2.97, prices[1], 0.005);
        assertEquals(1.59, prices[2], 0.005);

    }

    @Test
    public void testRemoveMargin() {
        double[] prices = {8.12, 2.97, 1.59};
        double[] probs = MarginingEntropy.removeMargin(prices);
        assertEquals(.1, probs[0], 0.005);
        assertEquals(.3, probs[1], 0.005);
        assertEquals(.6, probs[2], 0.005);
        /*
         * test using generic algo
         */
        Margining margining = new Margining();
        margining.setMarginingAlgoToEntropy();
        probs = margining.removeMargin(prices, 1.0);
        assertEquals(.1, probs[0], 0.005);
        assertEquals(.3, probs[1], 0.005);
        assertEquals(.6, probs[2], 0.005);
    }



    @Test
    public void testRemoveMargin3() {
        double[] prices = {1.02, 11};
        double[] probs = MarginingEntropy.removeMargin(prices);
        for (int i = 0; i < prices.length; i++) {
            System.out.print(String.format("%d  %.2f %.4f\n", i, prices[i], probs[i]));
        }
        assertEquals(.9643, probs[0], 0.005);
        assertEquals(.0357, probs[1], 0.005);
        /*
         * test using generic algo
         */
        Margining margining = new Margining();
        margining.setMarginingAlgoToEntropy();
        probs = margining.removeMargin(prices, 1.0);
    }

    @Test
    public void testRemoveMarginOldAlgo() {
        double[] prices = {1.02, 13, 51};
        try {
            @SuppressWarnings("unused")
            double[] probs = MarginingEntropy.removeMargin(prices);
            fail(); // expect exception to occur
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testRemoveMarginNewAlgo() {
        double[] prices = {1.02, 13, 51};
        Margining margining = new Margining();
        margining.setMarginingAlgoToEntropy();
        double[] probs = margining.removeMargin(prices, 1.0);
        for (int i = 0; i < prices.length; i++) {
            System.out.print(String.format("%d  %.2f %.4f\n", i, prices[i], probs[i]));
        }
    }

}
