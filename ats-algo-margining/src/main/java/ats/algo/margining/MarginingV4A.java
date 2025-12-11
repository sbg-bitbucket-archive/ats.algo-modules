package ats.algo.margining;

/**
 * This is a variant of the ATS margining v4 algo, designed to be more friendly for demargining purposes. the original
 * v5 algo is included as a static method
 * 
 * @author gicha
 *
 */
public class MarginingV4A {
    /**
     * apply margining to the supplied set of probabilities
     * 
     * @param probs - should sum to 1
     * @param totalMargin - total margin to be applied across all selections
     * @return the calculated set of prices
     */
    public static double[] addMargin(double[] probs, double totalMargin) {
        Margining margining = new Margining();
        margining.setMarginingAlgoToV4A();
        return margining.addMargin(probs, totalMargin);
    }

    /**
     * add margin to a single probability
     * 
     * @param prob probability to be margined
     * @param n the number of selections who's probabilities sum to 1 from which this prob is drawn
     * @param targetMargin the target margin to apply
     * @return the calculated price
     */
    public static double addMargin(double prob, int n, double targetMargin) {
        Margining margining = new Margining();
        margining.setMarginingAlgoToV4A();
        double price = margining.addMargin(prob, n, targetMargin);
        return price;
    }

    /**
     * remove margining from the supplied set of prices
     * 
     * @param prices
     * @param expectedProbsTotal the total the calculated probs are expected to sum to
     * @return array of probs with margin removed
     */
    public static double[] removeMargin(double[] prices, double expectedProbsTotal) {
        Margining margining = new Margining();
        margining.setMarginingAlgoToV4A();
        return margining.removeMargin(prices, expectedProbsTotal);
    }

    /*
     * this is the code as copied from ATS
     */
    public static double calculateV4Margin(double outcomeProb, double minMargin, double maxMargin,
                    boolean eachWayModifications) {
        double margin;
        double factor = 0.66;
        if (outcomeProb > 0.5) {
            margin = maxMargin - (Math.pow(((1.0 - outcomeProb) * factor) - 0.5, 8))
                            * ((maxMargin - minMargin) / Math.pow(0.5, 8));
        } else {
            margin = maxMargin - (Math.pow((outcomeProb * factor) - 0.5, 8))
                            * ((maxMargin - minMargin) / Math.pow(0.5, 8));
        }

        if (eachWayModifications) {
            if (outcomeProb >= 0.01 && outcomeProb <= 0.12) {
                margin = margin + (outcomeProb * (maxMargin / -0.72)) + (maxMargin / 6.0);
            }

            if (outcomeProb < 0.01) {
                margin = margin + (outcomeProb * (maxMargin / 0.6));
            }
        }
        return margin;
    }


}
