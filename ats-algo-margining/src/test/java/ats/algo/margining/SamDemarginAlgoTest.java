package ats.algo.margining;

import org.junit.Test;

public class SamDemarginAlgoTest {

    @Test
    public void testRemoveMargin6() {
        // double[] prices = {51,51,1.002};
        double[] prices = {41, 34, 1.005};
        double[] probs = SamDemarginAlgo.removeMargin(prices);
        System.out.println("Margining test");
        for (int i = 0; i < prices.length; i++) {
            System.out.printf("input price: %.2f, calculated output prob: %.4f\n", prices[i], probs[i]);
        }
    }


}
