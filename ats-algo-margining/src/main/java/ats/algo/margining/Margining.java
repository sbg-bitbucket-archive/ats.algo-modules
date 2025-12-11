package ats.algo.margining;


import ats.algo.genericsupportfunctions.Minimiser;
import ats.algo.genericsupportfunctions.Minimiser.FunctionToMinimise;
import ats.algo.genericsupportfunctions.Minimiser.MinimiserResults;
import ats.core.AtsBean;

/**
 * methods to add/remove margin from a list of prices or probs. based on a specified margining algorithm. Prices
 * (Prices[i]) and probabilities (probs[i]) are related by the formula
 * 
 * Prices[i] = 1/(probs[i]+margin[i]
 * 
 * where margin[i] is the margin to be applied to that selection and is required to be function of probs[i] and a
 * constant m: margin[i] = f(probs[i], m)
 * 
 * 
 * The function f may be specified either using the "setMarginingAlgoToXxx" or the generic "setMarginingAlgo" methods
 * 
 * @author Geoff
 * 
 */
public class Margining extends AtsBean {

    /**
     * specifies whether the "m" parameter in the margining algo is to be interpreted as the total margin to be applied
     * across all selections (as in the entropy margining algo) or as a target margin to be applied to each selection
     * (as in the v8 algo)
     * 
     * @author Geoff
     *
     */
    public enum MarginingParameterType {
        isTotalMargin,
        isSelectionTargetMargin
    }

    @FunctionalInterface
    public interface MarginingAlgo {
        public double margin(double p, int n);
    }

    public static MarginingAlgo MARGINING_ALGO_ENTROPY = (p, n) -> entropyMargin(p);
    public static MarginingAlgo MARGINING_ALGO_V4A = (p, n) -> v4AMargin(p);
    public static MarginingAlgo MARGINING_ALGO_V10 = (p, n) -> v10Margin(p, n);

    private MarginingAlgo marginingAlgo;
    private double totalMargin;
    private double targetMargin;
    private int noIterations;
    private int noLoops;
    private double costAtMinimum;

    public Margining() {
        setMarginingAlgoToV10();// set the default margining algorithm
        totalMargin = -1;
        targetMargin = -1;
    }

    /**
     * sets the margining algorithm. Must return a valid number for any p in the range 0<=p,=1
     * 
     * @param marginingAlgo - the margining algorithm
     * @param mtype - whether the "m" parameter in the margining algo is to be interpreted as total margin or target
     *        margin per selection
     */
    public void setMarginingAlgo(MarginingAlgo marginingAlgo) {
        this.marginingAlgo = marginingAlgo;
    }

    /**
     * sets margining to use the entropy method
     */
    public void setMarginingAlgoToEntropy() {
        this.marginingAlgo = MARGINING_ALGO_ENTROPY;
    }


    /**
     * sets margining to use the Amelco v4 algo
     */
    public void setMarginingAlgoToV4A() {
        this.marginingAlgo = MARGINING_ALGO_V4A;
    }

    /**
     * sets margining to use the Amelco v10 algo
     */
    public void setMarginingAlgoToV10() {
        this.marginingAlgo = MARGINING_ALGO_V10;
    }

    /**
     * sets margining to use the Amelco v11 algo
     */
    public void setMarginingAlgoToV11() {
        this.marginingAlgo = (p, n) -> v11Margin(p);
    }

    public void setMarginingAlgoToV12() {
        this.marginingAlgo = (p, n) -> v12Margin(p);
    }

    /**
     * gets the totalMargin added or removed across all selections following a call to the addMargin or removeMargin
     * methods. If not yet calculated then set to -1;
     * 
     * @return -1 if not yet calculated. Otherwise the total margin.
     */
    public double getTotalMargin() {
        return totalMargin;
    }

    /**
     * gets the targetMargin which may then be used in the addMargin method for a single probability to yield the same
     * result as the addMargin method applied to the complete set of probabilities. be added to a single selection
     * 
     * @return -1 if not yet calculated. Otherwise the target margin
     */
    public double getTargetMargin() {
        return targetMargin;
    }

    public int getNoIterations() {
        return noIterations;
    }

    public int getNoLoops() {
        return noLoops;
    }

    public double getCostAtMinimum() {
        return costAtMinimum;
    }

    private static final double entropyNormalisingConst = Math.exp(-1);

    private static double entropyMargin(double p) {
        double marginPct;
        if (p == 0)
            marginPct = 0;
        else
            marginPct = -p * Math.log(p) / entropyNormalisingConst;
        return marginPct;
    }

    static boolean eachWayModifications = false;
    static double minMargin = 0.01;
    static double maxMargin = 0.03;

    /**
     * note that this is not quite the same as the v4 algo used in ATS
     * 
     * @param probability
     * @return
     */
    public static double v4AMargin(double probability) {
        return v4AMargin(probability, minMargin, minMargin, maxMargin);

    }

    public static double v4AMargin(double prob, double minMarginLow, double minMarginHigh, double maxMargin) {
        double margin;
        double factor = 1.32;
        if (prob < 0.5)
            margin = maxMargin - (maxMargin - minMarginLow) * (Math.pow(((prob) * factor) - 1.0, 8));
        else
            margin = maxMargin - (maxMargin - minMarginHigh) * (Math.pow(((1 - prob) * factor) - 1.0, 8));
        if (eachWayModifications) {
            if (prob >= 0.01 && prob <= 0.12) {
                margin = margin + (prob * (maxMargin / -0.72)) + (maxMargin / 6.0);
            }

            if (prob < 0.01) {
                margin = margin + (prob * (maxMargin / 0.6));
            }
        }
        return margin;
    }



    /**
     * Takes probability input --> outputs margin
     * 
     * @param probability
     * @param nEvents
     * @return
     */
    private static double v10Margin(double probability, int nEvents) {
        double x = probability;
        double k = (5.0 / nEvents + 2.0) * 14.0 / 15.0;
        double marginPct = 1 - Math.pow(Math.abs(x - 0.5), k) / Math.pow(0.5, k);
        return marginPct;

    }

    private double v11Margin(double probability) {
        return v10Margin(probability, 6);
    }

    private double v12Margin(double probability) {
        return 0;
    }

    /**
     * calculates margined prices using the specified marginingAlgo
     * 
     * @param probs the probs to which margin is to be applied, must be in the range 0<= p[i] <= 1 and sum to 1
     * @param totalMargin the total margin to be allocated across all selections, e.g. 0.12
     * @return the calculated prices
     */
    public double[] addMargin(double[] probs, double totalMargin) {
        int n = probs.length;
        double[] prices = new double[n];
        double[] e = new double[n];
        double sumE = 0;
        for (int i = 0; i < n; i++) {
            if (probs[i] < 0 || probs[i] > 1)
                throw new IllegalArgumentException("probs outside range 0 to 1");
            e[i] = marginingAlgo.margin(probs[i], n);
            sumE += e[i];
        }
        for (int i = 0; i < n; i++)
            if (probs[i] == 0.0)
                prices[i] = 1000;
            else if (probs[i] == 1.0)
                prices[i] = 1;
            else
                prices[i] = 1 / (probs[i] + e[i] * totalMargin / sumE);
        this.totalMargin = totalMargin;
        this.targetMargin = totalMargin * marginingAlgo.margin(0.5, n) / sumE;
        return prices;
    }

    /**
     * adds margin to a single probability. The totalMargin and targetMargin properties are reset to -1, since any
     * previously calculated values may no longer be valid
     * 
     * @param prob
     * @param n total no of selections making up the set of probs that sum to 1
     * @param targetMargin the targetMargin to be applied. The actual margin applied will be equal to the target margin
     *        if prob = 0.5. For other values of prob the actual margin will be the target margin scaled up or down in
     *        line with the curve f(p), where f is the margining algo. The relationship between targetMargin and
     *        totalMargin for a set of probabilities p[] is targetMargin = totalMargin/sum(f(p[i])
     * @return
     */
    public double addMargin(double prob, int n, double targetMargin) {
        double e = marginingAlgo.margin(prob, n);
        double price;
        if (prob == 0.0)
            price = 1000;
        else if (prob == 1.0)
            price = 1;
        else
            price = 1 / (prob + e * targetMargin);
        /*
         * reset total and target margin to -1 since results of any previous calc may no longer be valid
         */
        this.totalMargin = -1;
        this.targetMargin = -1;
        return price;
    }

    /*
     * variables used in the cost fn calc
     */
    private double[] targetPrices;
    private double penaltyFactor;
    private double expectedProbsTotal;
    private int n;

    private double costFunction(double[] z) {
        double cost = 0;
        double sumP = 0;
        double p[] = new double[n];
        for (int i = 0; i < n; i++) {
            p[i] = 1 / (1 + Math.exp(-z[i]));
            sumP += p[i];
        }

        double[] prices = addMargin(p, totalMargin);
        for (int i = 0; i < n; i++) {
            double x = prices[i] - targetPrices[i];
            cost += x * x;
        }
        cost = Math.sqrt(cost / n) + Math.abs(sumP - expectedProbsTotal) * penaltyFactor;
        return cost;
    }

    /**
     * remove margining from the supplied set of prices
     * 
     * @param prices
     * @param expectedProbsTotal the total the probs are expected to sum to
     * @return array of probs with margin removed
     */
    public double[] removeMargin(double[] prices, double expectedProbsTotal) {
        this.targetPrices = prices;
        this.expectedProbsTotal = expectedProbsTotal;
        this.n = prices.length;

        double[] p = new double[n];
        double sumP = 0;
        double sumE = 0;

        /*
         * calculate an initial guess at the p[i] using p*(1-p) as a rough margin allocation algo
         */

        for (int i = 0; i < n; i++) {
            if (prices[i] <= 1.0)
                p[i] = 0.9999;
            else
                p[i] = 1 / prices[i];
            sumP += p[i];
            sumE += p[i] * (1 - p[i]);
        }


        totalMargin = sumP - expectedProbsTotal;
        double[] z = new double[n];
        for (int i = 0; i < n; i++) {
            p[i] -= totalMargin * p[i] * (1 - p[i]) / sumE;
            if (p[i] < 0)
                p[i] = 0.00001;
            if (p[i] > 1)
                p[i] = 0.9999;
            z[i] = Math.log(p[i] / (1 - p[i]));
        }
        FunctionToMinimise fn = (double[] x) -> costFunction(x);
        Minimiser minimiser = new Minimiser();
        minimiser.setMaxIterations(10000);
        double tolerance = 0.001;
        double err;
        penaltyFactor = 1;
        MinimiserResults results;
        /*
         * iterate until converge on solution where sum of probs is sufficiently close to 1
         */
        noIterations = 0;
        noLoops = 0;
        do {
            results = minimiser.minimise(z, .5, 0.002, fn);
            if (!results.success) {
                error("Minimiser can't find solution");
                return null;
            }
            noLoops++;
            noIterations += results.nIterations;
            sumP = 0;
            for (int i = 0; i < n; i++) {
                z[i] = results.bestX[i];
                p[i] = 1 / (1 + Math.exp(-z[i]));
                sumP += p[i];
            }
            err = Math.abs(sumP - expectedProbsTotal);
            penaltyFactor *= 10;
        } while (err > tolerance);
        costAtMinimum = results.functionValueAtMinimum;
        /*
         * calculate the targetMargin
         */
        double sumE2 = 0;
        for (int i = 0; i < n; i++) {
            sumE2 += marginingAlgo.margin(p[i], n);
        }
        targetMargin = totalMargin * marginingAlgo.margin(0.5, n) / sumE2;
        return p;
    }

}
