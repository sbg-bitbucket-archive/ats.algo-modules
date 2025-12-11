package ats.algo.margining;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MarginingTest {

    @Test
    public void testAddMargin() {
        Margining margining = new Margining();
        /*
         * test entropy algo
         */
        margining.setMarginingAlgoToEntropy();
        double[] probs = {.1, .3, .6};
        double[] prices = margining.addMargin(probs, .09);
        assertEquals(8.1249, prices[0], 0.0001);
        assertEquals(2.9744, prices[1], 0.0001);
        assertEquals(1.5854, prices[2], 0.0001);
        assertEquals(.09, margining.getTotalMargin(), 0.0001);
        // check targetMargin calcs
        double targetMargin = margining.getTargetMargin();
        assertEquals(.03474, targetMargin, 0.0001);
        double singlePrice = margining.addMargin(probs[0], 3, targetMargin);
        assertEquals(8.2141, singlePrice, 0.0001);
        // call to addMargin for single prob should reset total and target margin to -1
        assertEquals(-1, margining.getTotalMargin(), 0.0001);
        assertEquals(-1, margining.getTargetMargin(), 0.0001);
        /*
         * test set custom algo
         */
        margining.setMarginingAlgo((p, n) -> p);
        double[] probs2 = {.1, .3, .6};
        double[] prices2 = margining.addMargin(probs2, .09);
        assertEquals(9.1743, prices2[0], 0.0001);
        assertEquals(3.0581, prices2[1], 0.0001);
        assertEquals(1.5291, prices2[2], 0.0001);
        assertEquals(.09, margining.getTotalMargin(), 0.001);
        // check targetMargin calcs
        double targetMargin2 = margining.getTargetMargin();
        assertEquals(.0450, targetMargin2, 0.0001);
        double singlePrice2 = margining.addMargin(probs[0], 3, targetMargin2);
        assertEquals(9.5693, singlePrice2, 0.0001);

        /*
         * test v10 algo
         */
        margining.setMarginingAlgoToV10();
        double[] probs4 = {.1, .3, .6};
        double[] prices4 = margining.addMargin(probs4, 0.09);
        assertEquals(8.3802, prices4[0], 0.0001); // = 1/.1305
        assertEquals(2.9884, prices4[1], 0.0001); // = 1/.7332
        assertEquals(1.5722, prices4[2], 0.0001); // = 1/.7332
        assertEquals(.09, margining.getTotalMargin(), 0.001);
        double targetMargin3 = margining.getTargetMargin();
        assertEquals(.0361, targetMargin3, 0.0001);
        double singlePrice3 = margining.addMargin(probs[0], 3, targetMargin3);
        assertEquals(8.3801, singlePrice3, 0.0001);
        // call to addMargin for single prob should reset total and target margin to -1
        assertEquals(-1, margining.getTotalMargin(), 0.0001);
        assertEquals(-1, margining.getTargetMargin(), 0.0001);

        /*
         * test v11 algo
         */
        // margining.setMarginingAlgoToV11();
        // double[] probs5 = { .1, .3, .6 };
        // double[] prices5 = margining.addMargin(probs5, 0.04);
        // assertEquals(9.0359, prices5[0], 0.0001); // 1/.1107
        // assertEquals(3.1789, prices5[1], 0.0001); // 1/.3146
        // assertEquals(1.6267, prices5[2], 0.0001); // 1/.6148
        // double targetMargin4 = margining.getTargetMargin();
        // assertEquals(.0148, targetMargin4, 0.0001);
        // double singlePrice4 = margining.addMargin(probs[0], 3, targetMargin4);
        // assertEquals(9.0359, singlePrice4, 0.0001);
        // // call to addMargin for single prob should reset total and target margin to -1
        // assertEquals (-1, margining.getTotalMargin(), 0.0001);
        // assertEquals (-1, margining.getTargetMargin(), 0.0001);

    }

    @Test
    public void testAddMargin2() {
        Margining margining = new Margining();
        margining.setMarginingAlgoToV10();
        double price1 = margining.addMargin(.58188, 2, .035);
        double price2 = margining.addMargin(.41812, 2, .035);
        System.out.printf("%.3f, %.3f", price1, price2);
        System.out.println("");
    }

    @Test
    public void testAddMargin3() {
        Margining margining = new Margining();
        margining.setMarginingAlgoToV11();
        double price1 = margining.addMargin(.58188, 2, .04);
        double price2 = margining.addMargin(.41812, 2, .04);
        System.out.printf("%.3f, %.3f", price1, price2);
        System.out.println("");
    }

    @Test
    public void testAddMargin4() {
        Margining margining = new Margining();
        margining.setMarginingAlgoToV10();
        double[] probs = {0.0, 1.0};
        double[] prices = margining.addMargin(probs, .1);
        System.out.printf("%.3f, %.3f\n", prices[0], prices[1]);
    }

    @Test
    public void testAddMargin5() {
        Margining margining = new Margining();
        margining.setMarginingAlgoToV4A();
        double[] probs = {0.05, 0.15, 0.8};
        double[] prices = margining.addMargin(probs, .1);
        System.out.printf("%.3f, %.3f, %.3f\n", prices[0], prices[1], prices[2]);
        assertEquals(13.309, prices[0], 0.006);
        assertEquals(5.369, prices[1], 0.006);
        assertEquals(1.192, prices[2], 0.006);
    }

    @Test
    public void testAddMargin6() {
        double margin = Margining.v4AMargin(0.95);
        System.out.printf("%.4f\n", margin);
        assertEquals(0.0184, margin, 0.0006);
        margin = Margining.v4AMargin(0.75);
        System.out.printf("%.4f\n", margin);
        assertEquals(0.0292, margin, 0.0006);

    }


    @Test
    public void testRemoveMargin() {
        Margining margining = new Margining();
        /*
         * demargin entropy algo
         */
        margining.setMarginingAlgoToEntropy();
        double[] prices = {8.1249, 2.9744, 1.5854};
        double[] probs = margining.removeMargin(prices, 1);
        assertEquals(.1, probs[0], 0.005);
        assertEquals(.3, probs[1], 0.005);
        assertEquals(.6, probs[2], 0.005);
        assertEquals(.09, margining.getTotalMargin(), 0.001);
        assertEquals(.035, margining.getTargetMargin(), 0.001);
        /*
         * demargin custom algo
         */
        margining.setMarginingAlgo((p, n) -> p);
        double[] prices2 = {9.1743, 3.0581, 1.5291};
        double[] probs2 = margining.removeMargin(prices2, 1);
        assertEquals(.1, probs2[0], 0.005);
        assertEquals(.3, probs2[1], 0.005);
        assertEquals(.6, probs2[2], 0.005);
        assertEquals(.09, margining.getTotalMargin(), 0.001);
        assertEquals(.045, margining.getTargetMargin(), 0.001);

        /*
         * demargin v10 algo
         */
        margining.setMarginingAlgoToV10();
        double[] prices4 = {8.3801, 2.9884, 1.5722};
        double[] probs4 = margining.removeMargin(prices4, 1);
        assertEquals(.1, probs4[0], 0.005);
        assertEquals(.3, probs4[1], 0.005);
        assertEquals(.6, probs4[2], 0.005);
        assertEquals(.09, margining.getTotalMargin(), 0.001);
        assertEquals(.0362, margining.getTargetMargin(), 0.001);

        /*
         * demargin v11 algo
         */
        margining.setMarginingAlgoToV11();
        double[] prices5 = {9.0359, 3.1789, 1.6267};
        double[] probs5 = margining.removeMargin(prices5, 1);
        assertEquals(.1, probs5[0], 0.005);
        assertEquals(.3, probs5[1], 0.005);
        assertEquals(.6, probs5[2], 0.005);
    }

    @Test
    public void testRemoveMargin2() {

        double[] prices = {5.5, 6.5, 7, 7, 7.5, 7.5, 8, 10, 10, 13, 17, 17, 19, 19, 21, 23, 34, 41, 51, 51, 9, 10, 10,
                11, 12, 12, 13, 15, 17, 19, 21, 26, 26, 34, 34, 41, 41, 41, 67, 7};
        Margining margining = new Margining();
        /**
         * remove margin using entropy algo
         */
        System.out.print("demargin using Entropy Algo\n");
        margining.setMarginingAlgoToEntropy();
        runDemarginingTest(margining, prices, .034);

        /**
         * remove margin using v10 algo
         */
        System.out.print("demargin using v10 Algo\n");
        margining.setMarginingAlgoToV10();
        runDemarginingTest(margining, prices, .034);

        /**
         * remove margin using v11 algo
         */
        // System.out.print("demargin using v11 Algo\n");
        // margining.setMarginingAlgoToV11();
        // runDemarginingTest(margining, prices, .033);

        /**
         * remove margin using custom algo margin proportional to p*(1-p)
         */
        System.out.print("demargin using margin proportional to p*(1-p) \n");
        margining.setMarginingAlgo((p, n) -> p * (1 - p));
        runDemarginingTest(margining, prices, .034);

        /**
         * remove margin using custom algo margin proportional to p
         */
        System.out.print("demargin using margin proportional to p \n");
        margining.setMarginingAlgo((p, n) -> p);
        runDemarginingTest(margining, prices, .034);
    }

    private void runDemarginingTest(Margining margining, double[] prices, double expectedProb8) {
        double[] probs = margining.removeMargin(prices, 1);
        double[] prices2 = margining.addMargin(probs, margining.getTotalMargin());
        double sumProbs = 0;
        for (int i = 0; i < prices.length; i++) {
            System.out.printf("%d, %.2f, %.3f, %.2f\n", i, prices[i], probs[i], prices2[i]);
            sumProbs += probs[i];
        }
        System.out.printf("Margining param: %.3f, #iterations: %d, %d, min cost: %.3f, sum probs: %.3f\n\n",
                        margining.getTotalMargin(), margining.getNoIterations(), margining.getNoLoops(),
                        margining.getCostAtMinimum(), sumProbs);
        assertEquals(expectedProb8, probs[8], 0.001);
    }

    /**
     * demargin when probs don't sum to 1
     */
    @Test
    public void testRemoveMargin3() {
        Margining margining = new Margining();
        margining.setMarginingAlgoToEntropy();
        double[] prices = {1.8, 1.8, 1.8};
        double[] probs = margining.removeMargin(prices, 1.5);
        assertEquals(.5, probs[0], 0.005);
        assertEquals(.5, probs[1], 0.005);
        assertEquals(.5, probs[2], 0.005);
        assertEquals(.166, margining.getTotalMargin(), 0.001);
        assertEquals(.055, margining.getTargetMargin(), 0.001);
        /*
         * 
         */
        Margining margining2 = new Margining();
        margining2.setMarginingAlgoToEntropy();
        double[] prices2 = {8.1249, 2.9744, 1.5854, 8.1249, 2.9744, 1.5854};
        double[] probs2 = margining2.removeMargin(prices2, 2);
        assertEquals(.1, probs2[0], 0.005);
        assertEquals(.3, probs2[1], 0.005);
        assertEquals(.6, probs2[2], 0.005);
        assertEquals(.18, margining2.getTotalMargin(), 0.001);
        assertEquals(.035, margining2.getTargetMargin(), 0.001);
    }

    @Test
    public void testRemoveMargin4() {
        Margining margining = new Margining();
        margining.setMarginingAlgoToEntropy();
        double[] prices = {1.02, 35.02, 12.8};
        double[] probs = margining.removeMargin(prices, 1.0);
        System.out.println("Margining test");
        for (int i = 0; i < prices.length; i++) {
            System.out.printf("input price: %.2f, calculated output prob: %.3f\n", prices[i], probs[i]);
        }
    }

    @Test
    public void testRemoveMargin5() {
        Margining margining = new Margining();
        margining.setMarginingAlgoToEntropy();
        double[] prices = {1.00, 12};
        double[] probs = margining.removeMargin(prices, 1.0);
        System.out.println("Margining test");
        for (int i = 0; i < prices.length; i++) {
            System.out.printf("input price: %.2f, calculated output prob: %.3f\n", prices[i], probs[i]);
        }
    }

    // @Test
    public void testRemoveMargin6() {
        Margining margining = new Margining();
        margining.setMarginingAlgoToV4A();
        double[] prices = {51, 51, 1.002};
        double[] probs = margining.removeMargin(prices, 1.0);
        System.out.println("Margining test");
        for (int i = 0; i < prices.length; i++) {
            System.out.printf("input price: %.2f, calculated output prob: %.4f\n", prices[i], probs[i]);
        }
    }

    @Test
    public void testRemoveMargin7() {
        Margining margining = new Margining();
        margining.setMarginingAlgoToV4A();
        double[] prices = {7, 5.5, 7.5, 15, 41, 81, 9, 6.5, 9, 19, 41, 101, 21, 15, 21, 41, 67, 201, 51, 41, 51, 81,
                201, 151, 126, 151, 351};
        double[] probs = margining.removeMargin(prices, 1.0);
        System.out.println("Margining test");
        for (int i = 0; i < prices.length; i++) {
            System.out.printf("input price: %.2f, calculated output prob: %.4f\n", prices[i], probs[i]);
        }
    }

}
