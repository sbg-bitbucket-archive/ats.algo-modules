package ats.algo.genericsupportfunctions;

public final class GCMath {
    /**
     * rounds the supplied double to n places
     * 
     * @param x
     * @param n
     * @return
     */
    public static double round(double x, int n) {
        double mult = Math.pow(10.0, (double) n);
        double y = ((double) Math.round(x * mult)) / mult;
        return y;
    }

    /**
     * returns true if n odd
     * 
     * @param n
     * @return
     */
    public static boolean isOdd(int n) {
        return !((n / 2) * 2 == n);
    }
}
