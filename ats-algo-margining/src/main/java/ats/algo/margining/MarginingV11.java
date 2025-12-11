package ats.algo.margining;

public class MarginingV11 {

    /**
     * add margin to a single probability
     * 
     * @param prob probability to be margined
     * @param n the number of selections who's probabilities sum to 1 from which this prob is drawn
     * @param targetMargin the target margin to apply
     * @return the calculated price
     */
    public static double addMargin(double prob, int n, double maxMargin) {
        Margining margining = new Margining();
        margining.setMarginingAlgoToV11();
        double price = margining.addMargin(prob, n, maxMargin);
        return price;
    }

    /**
     * remove margining from the supplied set of prices
     * 
     * @param prices
     * @param expectedProbsTotal the total the probs are expected to sum to.
     * @return array of probs with margin removed
     */
    public static double[] removeMargin(double[] prices, double maxMargin, double expectedProbsTotal) {
        Margining margining = new Margining();
        margining.setMarginingAlgoToV11();
        return margining.removeMargin(prices, expectedProbsTotal);
    }

}
