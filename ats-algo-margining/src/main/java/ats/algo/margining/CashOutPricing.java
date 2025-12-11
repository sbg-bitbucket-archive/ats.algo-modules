package ats.algo.margining;

public class CashOutPricing {

    /**
     * Calculates a price for cashing in a bet.
     * 
     * @param Price the current Price including margin for taking out a bet on this selection
     * @param prob the current unbiased prob for taking out a bet on this selection, so Price = 1/(prob+margin)
     * @return the cashout price, which will be >1
     */
    public static double getCashoutPrice(double Price, double prob) {
        return 0.0;
    }

}
