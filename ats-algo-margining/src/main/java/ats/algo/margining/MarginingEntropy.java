package ats.algo.margining;

/**
 * utility methods to add/remove margin from a list of prices or probs. based on the entropy margining algorithm
 * 
 * @author Geoff
 * 
 */
public class MarginingEntropy {
    /**
     * apply margining to the supplied set of probabilities
     * 
     * @param probs - should sum to 1
     * @param maxMargin - max margin to be applied to each selection
     * @return
     */
    public static double[] addMargin(double[] probs, double margin) {
        double[] prices = new double[probs.length];
        double totalProb = 0;
        for (int i = 0; i < probs.length; i++)
            totalProb += probs[i];
        if (Math.abs(totalProb - 1) > 0.005)
            throw new IllegalArgumentException("probs don't add up to 1");
        if (margin < 0 || margin > 0.1)
            throw new IllegalArgumentException("invalid totalMargin");
        double[] e = new double[probs.length];
        double sumE = 0;
        for (int i = 0; i < probs.length; i++) {
            e[i] = entropy(probs[i]);
            sumE += e[i];
        }
        for (int i = 0; i < probs.length; i++) {
            prices[i] = 1 / (probs[i] + e[i] * margin / sumE);
        }
        return prices;
    }

    private static final int MAX_ITERATIONS = 10000;

    /**
     * remove margining from the supplied set of prices.
     * 
     * @param prices
     * @return array of probs with margin removed
     */
    public static double[] removeMargin(double[] prices) {
        double epsilon = 0.005;
        double[] q = new double[prices.length];
        double[] p = new double[prices.length];
        double sumQ = 0;
        for (int i = 0; i < prices.length; i++) {
            q[i] = 1 / prices[i];
            p[i] = q[i];
            sumQ += q[i];
        }
        double margin = sumQ - 1;
        double[] e = new double[prices.length];
        int nIterations = 0;
        double err;
        do {
            double sumE = 0;
            err = 0;
            for (int i = 0; i < prices.length; i++) {
                e[i] = entropy(p[i]);
                sumE += e[i];
            }
            for (int i = 0; i < prices.length; i++) {
                double x;
                x = q[i] - e[i] * margin / sumE;
                double y = Math.abs(x - p[i]);
                if (y > err)
                    err = y;
                p[i] = x;
            }
            nIterations++;
            if (nIterations > MAX_ITERATIONS) {
                String pricesStr = "{";
                for (int i = 0; i < prices.length; i++) {
                    pricesStr += prices[i];
                    if (i <= prices.length - 1)
                        pricesStr += ", ";
                }
                pricesStr += "}";
                String errMsg = String.format(
                                "Demargining error for prices = %s.  No solution found in %d iterations.  Error: %.4f(%,4f)",
                                pricesStr, nIterations, err, epsilon);
                throw new IllegalArgumentException(errMsg);
            }
        } while (err > epsilon);
        return p;
    }

    private static double entropy(double p) {
        double e = 0;
        if (p > 0)
            e = p * Math.log(p);
        else if (p < 0)
            e = p * Math.log(-p);
        return e;
    }
}
