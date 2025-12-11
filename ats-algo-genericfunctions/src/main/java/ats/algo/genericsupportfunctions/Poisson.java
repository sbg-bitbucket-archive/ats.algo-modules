package ats.algo.genericsupportfunctions;

public class Poisson {

    private static final int EXACT_CALC_THRESHOLD = 50;


    /**
     * for the poisson distribution with mean lambda returns the prob that the outcome of a trial is between 0 and n
     * inclusive
     * 
     * @param lambda
     * @param n
     */
    public static double probUnder(double lambda, int n) {
        double cumProb = 0.0;
        if (lambda <= EXACT_CALC_THRESHOLD) {
            double probI = Math.exp(-lambda);
            cumProb = probI;
            for (int i = 1; i <= n; i++) {
                probI *= lambda / ((double) i);
                cumProb += probI;
            }
        } else {
            cumProb = NormalDistribution.cumNormal((double) n + 0.5, lambda, Math.sqrt(lambda));
        }
        return cumProb;
    }

}
