package ats.algo.genericsupportfunctions;

public final class LinearInterpolation {
    /**
     * General purpose 2-D linear interpolation algorithm. Dimensions of xVec, YVec and table must be consistent:
     * xVec(n), yVec(m), table(n,m) xVec and YVec must be ordered, so xVec(i-1)< xVec(i) for 0<=i<=n. x must satisfy
     * xVec(0) <= x <= xVec(n), similarly y must be between yVec(0) and yVec(n) .
     * 
     * @param x
     * @param y
     * @param xVec
     * @param yVec
     * @return the value of y that best fits
     * @throws Exception
     */
    public static double interpolate(double x, double y, double[] xVec, double[] yVec, double[][] table)
                    throws Exception {
        int n = xVec.length - 1;
        int m = yVec.length - 1;
        if (!(table.length - 1 == n) && (table.length - 1 == m))
            throw new Exception("upper bounds don't match in linearInterpolate params");
        if (!((x >= xVec[0]) && (x <= xVec[n])))
            throw new Exception("upper bounds don't match in linearInterpolate params");
        boolean found = false;
        int nH = -1;
        while (!found) {
            nH += 1;
            found = (x <= xVec[nH]);
        }
        if (nH == 0)
            nH = 1; // only possible if x=xVec(0)
        found = false;
        int mH = -1;
        while (!found) {
            mH += 1;
            found = (x <= yVec[mH]);
        }
        if (mH == 0)
            mH = 1; // only possible if y=yVec(0)
        double xL = xVec[nH - 1];
        double xH = xVec[nH];
        double yL = yVec[mH - 1];
        double yH = yVec[mH];
        double zLL = table[nH - 1][mH - 1];
        double zLH = table[nH - 1][mH];
        double zHL = table[nH][mH - 1];
        double zHH = table[nH][mH];
        double aL = (zLL - zHL) / (xL - xH);
        double bL = zLL - aL * xL;
        double wL = aL * x + bL;
        double aH = (zLH - zHH) / (xL - xH);
        double bH = zLH - aH * xL;
        double wH = aH * x + bH;
        double aF = (wL - wH) / (yL - yH);
        double bF = wL - aF * yL;
        return aF * y + bF;
    }
}
