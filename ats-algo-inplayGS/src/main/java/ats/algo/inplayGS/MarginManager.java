package ats.algo.inplayGS;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import ats.algo.margining.Margining;
import ats.algo.margining.MarginingV11;

public class MarginManager {


    private final static int zeroProbPrice = 150;
    private final static double zeroProbIn = 1.0e-3; // what we accept as zero prob when adding margin
    private final static double zeroProbOut = 1.0e-6; // what we set as zero prob when removing margin

    /**
     * demargin the supplied prices, updating pricesMap with the calculated probs, using the entropy algorithm
     * 
     * @param pricesMap
     * @param expectedProbsTotal
     * @return
     */
    public static double demargin(Map<String, Selection> pricesMap, double expectedProbsTotal) {

        /*
         * remove from the map of prices to be demargined those selections where price exceeds zeroProbPrice
         */
        Map<String, Selection> pricesMap2 = new HashMap<String, Selection>();
        for (Entry<String, Selection> e : pricesMap.entrySet()) {
            String key = e.getKey();
            Selection selection = e.getValue();
            /*
             * set probs for Prices above zeroProbPrice to close to zero.
             */
            if (selection.price < zeroProbPrice)
                pricesMap2.put(key, selection);
            else
                selection.prob = zeroProbOut;
        }

        double[] prices = new double[pricesMap2.size()];
        int i = 0;
        for (Selection s : pricesMap2.values())
            prices[i++] = s.price;
        Margining margining = new Margining();
        margining.setMarginingAlgoToEntropy();
        double[] probs = margining.removeMargin(prices, expectedProbsTotal);
        i = 0;
        for (Selection s : pricesMap2.values())
            s.prob = probs[i++];
        return margining.getTotalMargin();
        /*
         * output info if required
         */
        // if (printRemarginedPrices) {
        // double[] probs2 = new double[pricesMap.size()];
        // int i2 = 0;
        // for (Selection s : pricesMap.values())
        // probs2[i2++] = s.prob;
        // double[] prices2 = margining.addMargin(probs2, margining.getTotalMargin());
        // int i3 = 0;
        // for (Selection s : pricesMap.values()) {
        // System.out.printf("%s: input price: %.2f, calc prob: %.3f, calc remargined price: %.2f\n",
        // s.name, s.price, s.prob, prices2[i3]);
        // i3++;
        // }
        // System.out.printf("Margining param: %.3f, #iterations: %d, %d,
        // min cost: %.3f, sum probs: %.3f\n\n",
        // margining.getTotalMargin(), margining.getNoIterations(),
        // margining.getNoLoops(),
        // margining.getCostAtMinimum(), sumProbs);
        // }
    }

    /**
     * 
     * @param pricesMap
     */
    static void remargin(Map<String, Selection> pricesMap, double totalMargin, OddsLadder oddsLadder,
                    double zeroProbPrice2) {
        Map<String, Selection> pricesMap2 = new HashMap<String, Selection>();
        for (Entry<String, Selection> e : pricesMap.entrySet()) {
            String key = e.getKey();
            Selection selection = e.getValue();
            /*
             * set Prices for probl below zeroProbIn to zeroProbPrice2
             */
            if (selection.prob > zeroProbIn)
                pricesMap2.put(key, selection);
            else
                selection.price = zeroProbPrice2;
        }
        double[] probs = new double[pricesMap2.size()];
        int i = 0;
        for (Selection s : pricesMap2.values())
            probs[i++] = s.prob;
        Margining margining = new Margining();
        margining.setMarginingAlgoToEntropy();
        double[] prices = margining.addMargin(probs, totalMargin);
        int i2 = 0;
        for (Selection s : pricesMap2.values()) {
            s.price = prices[i2++];
        }
        for (Selection s : pricesMap.values()) {
            s.price = oddsLadder.getPriceFromOddsLadder(s.price);
        }

        // for (Selection s: pricesMap.values()) {

        // i3++;
    }


    /**
     * adds margin to each selection in marketPrices using the v11 algo
     * 
     * @param marketPrices
     * @param marginPerSelection
     * @param oddsLadder
     */
    static void setMarginedPrices(Map<String, Selection> marketPrices, double marginPerSelection,
                    OddsLadder oddsLadder) {

        for (Selection s : marketPrices.values()) {
            double rawPrice = MarginingV11.addMargin(s.prob, marketPrices.size(), marginPerSelection);
            // System.out.println(s.toString() + ", rawPrice: " + rawPrice);
            s.price = oddsLadder.getPriceFromOddsLadder(rawPrice);
            // System.out.println(s.toString());
        }

    }
}

