package ats.algo.margining;

public class SamDemarginAlgo {

    public static double reverseMargin(double impliedProbs, double maxMargin, double minMargin) {
        double proportionAdded = 0.0;
        // aim is to estimate the proportion of maxMargin applied at a given implied price, e.g. 0.530 returns 1.000

        // we use x^6 quadratic for all implied probs less than 0.97 & greater than 0.04 and x^2 quad for anything else
        if (impliedProbs < 0.970 && impliedProbs > 0.04) {
            // quadratic coefficients
            double b6 = -50.32558, b5 = 144.50682, b4 = -174.13856, b3 = 115.44933, b2 = -45.89300, b1 = 10.61372,
                            b0 = -0.10890;

            proportionAdded = b6 * Math.pow(impliedProbs, 6) + b5 * Math.pow(impliedProbs, 5)
                            + b4 * Math.pow(impliedProbs, 4) + b3 * Math.pow(impliedProbs, 3)
                            + b2 * Math.pow(impliedProbs, 2) + b1 * impliedProbs + b0;
        } else if (impliedProbs < 0.04) // tail one
        {
            double b2 = -18.614760, b1 = 7.700970, b0 = 0.00004;
            proportionAdded = b2 * Math.pow(impliedProbs, 2) + b1 * impliedProbs + b0;
        } else // tail two
        {
            double b2 = -138.74723, b1 = 261.15628, b0 = -122.40835;
            proportionAdded = b2 * Math.pow(impliedProbs, 2) + b1 * impliedProbs + b0;
        }

        // proportionAdded represents the proportion of maxMargin added to seln

        Math.min(proportionAdded, 1.0); // can't have >100% of max
        double marginAdded = Math.max(minMargin, maxMargin * proportionAdded); // can't have < minMargin
        double probability = impliedProbs - marginAdded;
        return probability;
    }

    public static double[] removeMargin(double[] prices) {
        double[] impliedProbs = new double[prices.length];
        for (int i = 0; i < prices.length; i++)
            impliedProbs[i] = 1 / prices[i];
        double[] toReturn = new double[impliedProbs.length];
        double initMargin = 0.015; // min margin to start
        double marginStep = 0.005; // step in 0.5%
        double max = 0.06; // max margin to end
        double min = 0.002; // min margin applied to curve
        double[] thisWorking = new double[impliedProbs.length]; // working set
        double thisTot = 0.0; // working set total
        double retTot = 0.0; // return total
        double thresholdDiff = 0.01; // max allowed diff between solution and 100%

        for (double m = initMargin; m < max && Math.abs(retTot - 1.0) > thresholdDiff; m += marginStep) {
            thisTot = 0;
            for (int selIdx = 0; selIdx < impliedProbs.length; selIdx++) {
                thisWorking[selIdx] = reverseMargin(impliedProbs[selIdx], m, min);
                thisTot += thisWorking[selIdx];
            }

            if (Math.abs(thisTot - 1.0) < Math.abs(retTot - 1.0)) {
                retTot = thisTot;
                toReturn = new double[thisWorking.length];
                for (int i = 0; i < thisWorking.length; i++)
                    toReturn[i] = thisWorking[i];
            }
        }

        for (int selIdx = 0; selIdx < impliedProbs.length; selIdx++)
            toReturn[selIdx] = thisWorking[selIdx] / thisTot;

        return toReturn;
    }


}

