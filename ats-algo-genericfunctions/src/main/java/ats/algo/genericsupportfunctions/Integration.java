package ats.algo.genericsupportfunctions;

public class Integration {

    /**
     * generates the integrals of the normal prob density fn * x^r, for r = 0...n. The integrand is:
     * 
     * (1/sqrt(2*pi))*x^r*exp(-(x-mean)^2/stdDevn^2)
     * 
     * @param n
     * @param mean
     * @param stdDevn
     * @return the set of calculated integrals with y[r] being the r'th moment
     */
    public static double[] normalMoments(int n, double mean, double stdDevn) {
        double[] y = new double[n + 1];
        y[0] = 1;
        y[1] = mean;
        for (int i = 2; i <= n; i++) {
            y[i] = mean * y[i - 1] + (i - 1) * stdDevn * stdDevn * y[i - 2];
        }
        return y;
    }

}
