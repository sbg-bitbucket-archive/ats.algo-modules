package ats.algo.genericsupportfunctions;

import ats.core.util.log.Logger;
import ats.core.util.log.LoggerFactory;

/**
 * multidimensional minimisation via the downhill simplex Neider and Mead algorithm. c.f. Numerical recipes 3rd edition
 * section 10.5
 * 
 * @author Geoff
 * 
 */
public class Minimiser {

    private static final Logger log = LoggerFactory.getLogger(Minimiser.class);

    /**
     * specifies the function used as input by Amoeba
     * 
     * @author Geoff
     * 
     */
    @FunctionalInterface
    public interface FunctionToMinimise {
        public double calculate(double x[]);
    }

    private boolean detailedLogRequired;
    private int maxIterations;
    private boolean logIterations;

    private static final int logIterationFrequency = 10;

    public Minimiser() {
        /*
         * set defaults
         */
        detailedLogRequired = false;
        logIterations = false;
        maxIterations = 150;
    }

    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    /**
     * if set to true then more detailed logs are output
     * 
     * @param detailedLogRequired
     */
    public void setDetailedLogRequired(boolean detailedLogRequired) {
        this.detailedLogRequired = detailedLogRequired;
    }

    /**
     * set to true to output to logger updates on progress
     * 
     * @param logIterations
     */
    public void setLogIterations(boolean logIterations) {
        this.logIterations = logIterations;
    }



    /**
     * 
     * @author Geoff
     *
     */
    public class MinimiserResults {
        public boolean success;
        public double[] bestX;
        public int nIterations;
        public double functionValueAtMinimum;
        public String detailedLog;
        int i = 1;

        public MinimiserResults() {
            detailedLog = "";
        }

    }


    /**
     * Multidimensional minimisation of the function AmoebaFunction.function (x), where x is a vector of dimension nDim
     * the initial simplex is defined by point (also of dimension nDim) + constant displacement del in each dimension
     * 
     * @param point vector of dimension nDim - starting point for the process
     * @param del % displacement of each starting point to be used to construct initial simplex
     * @param fTol the convergence tolerance for the optimisation to succeed
     * @param func the class containing the function to be minimised
     * @return the nDim vector which minimises the function
     */
    public MinimiserResults minimise(double[] point, double del, double fTol, FunctionToMinimise func) {
        int nDim = point.length;
        double[] dels = new double[nDim];

        @SuppressWarnings("unused")
        String s = "\nStarting point. ";
        for (int i = 0; i < nDim; i++) {
            if (Math.abs(point[i]) < 0.01) // put a lower limit on the initial
                                           // delta
                dels[i] = 0.01 * del;
            else
                dels[i] = point[i] * del;
            s += String.format("param[%d]: %.4f  ", i, point[i]);
        }
        return minimise(point, dels, fTol, func);
    }

    /**
     * Multidimensional minimisation of the function AmoebaFunction.function (x), where x is a vector of dimension nDim
     * the initial simplex is defined by point (also of dimension nDim) + varying displacements del in each dimension
     * 
     * @param pointvector of dimension nDim - starting point for the process
     * @param dels the set of displacements to be used to construct initial simplex
     * @param fTol the convergence tolerance for the optimisation to succeed
     * @param func the class containing the function to be minimised
     * @return
     */
    public MinimiserResults minimise(double[] point, double[] dels, double fTol, FunctionToMinimise func) {
        int nDim = point.length;
        double[][] pp = new double[nDim + 1][nDim];
        for (int i = 0; i < nDim + 1; i++) {
            for (int j = 0; j < nDim; j++)
                pp[i][j] = point[j];
            if (i != 0)
                pp[i][i - 1] += dels[i - 1];
        }
        double[][] qq = new double[nDim + 1][nDim];
        for (int i = 0; i < nDim + 1; i++) {
            for (int j = 0; j < nDim; j++)
                qq[i][j] = point[j];
            if (i != 0)
                qq[i][i - 1] -= dels[i - 1];
        }
        return minimise(pp, qq, fTol, func);
    }

    /**
     * * Multidimensional minimisation of the function AmoebaFunction.function (x), where x is a vector of dimension
     * nDim the initial simplex is defined by the matrix pp, which must be of dimension [nDim+1, nDim]
     * 
     * @param pp1 set of points making up the initial simplex, of dimension [nDim+1, nDim] 2@param pp1 set of points
     *        making up the alternative initial simplex, of dimension [nDim+1, nDim]
     * @param func
     * @return
     */
    private MinimiserResults minimise(double[][] pp1, double[][] pp2, double fTol, FunctionToMinimise fn) {
        MinimiserResults results = new MinimiserResults();
        results.success = false;
        results.nIterations = 0;
        int nDim = pp1[0].length; // dimension of the x vector;
        int nVertices = pp1.length; // no of elements of the simplex = nDim+1
        double[] y1 = new double[nVertices]; // function values at the vertices
        double[] y2 = new double[nVertices];
        double minY1 = Double.MAX_VALUE;
        double minY2 = Double.MAX_VALUE;

        for (int i = 0; i < nVertices; i++) {
            double[] x1 = new double[nDim];
            double[] x2 = new double[nDim];
            for (int j = 0; j < nDim; j++) {
                x1[j] = pp1[i][j];
                x2[j] = pp2[i][j];
            }
            y1[i] = fn.calculate(x1);
            y2[i] = fn.calculate(x2);

            if (y1[i] < minY1)
                minY1 = y1[i];
            if (y2[i] < minY2)
                minY2 = y2[i];
        }
        /*
         * choose whichever of pp1 and pp2 is given the lowest min value of the cost function
         */
        double[][] p;
        double[] y;
        if (minY1 < minY2) {
            p = pp1;
            y = y1;
        } else {
            p = pp2;
            y = y2;
        }

        double[] psum = new double[nDim];
        double[] pmin = new double[nDim];
        results.nIterations = 0;
        psum = get_psum(p);
        do {
            /*
             * determine which point in the simplex gives the highest (worst), next highest and lowest function values
             */
            int ilo = 0; // the index of the lowest scoring point
            int ihi; // index of the highest scoring point
            int inhi; // index of the next highest scoring point
            if (y[0] > y[1]) {
                ihi = 0;
                inhi = 1;
            } else {
                ihi = 1;
                inhi = 0;
            }
            for (int i = 0; i < nVertices; i++) {
                if (y[i] <= y[ilo])
                    ilo = i;
                if (y[i] > y[ihi]) {
                    inhi = ihi;
                    ihi = i;
                } else if (y[i] > y[inhi] && i != ihi)
                    inhi = i;
            }
            double rTol = Math.abs(y[ihi] - y[ilo]);

            if (logIterations && (results.nIterations % logIterationFrequency) == 0)
                log.debug("In progress.  IterationNo: %d.  Cost fn: %.3f, rTol: %.3f(%.3f)", results.nIterations,
                                y[ilo], rTol, fTol);

            if (detailedLogRequired) {

                String s = String.format("IterationNo: %d.  Cost fn: %.3f, rTol: %.3f(%.3f)", results.nIterations,
                                y[ilo], rTol, fTol);
                for (int m = 0; m < nDim; m++)
                    s += String.format("  param[%d]: %.4f", m, p[ilo][m]);
                for (int l = 0; l < nVertices; l++) {
                    s += "\n     ";
                    for (int m = 0; m < nDim; m++)
                        s += String.format("  p[%d][%d]: %.4f", l, m, p[l][m]);
                    s += String.format("   fn[l]: %.4f", y[l]);
                }
                results.detailedLog += s + "\n";
            }
            /*
             * compute the fractional range from highest to lowest and finish the calc if satisfactory
             */
            if (rTol < fTol) {
                /*
                 * found acceptable minimum so put the best point and value in slot 0 and into fmin
                 */
                swapPoints(nDim, p, y, pmin, ilo);
                if (logIterations)
                    log.debug("Finished.  rtol below threshold.  IterationNo: %d.  Cost fn: %.3f, rTol: %.3f(%.3f)",
                                    results.nIterations, y[ilo], rTol, fTol);
                results.success = true;
                results.functionValueAtMinimum = y[0];
                results.bestX = pmin;

                return results;
            }
            if (results.nIterations >= maxIterations) {
                swapPoints(nDim, p, y, pmin, ilo);
                if (logIterations)
                    log.debug("Failure.  Max iterations exceeded.  IterationNo: %d.  Cost fn: %.3f, rTol: %.3f(%.3f)",
                                    results.nIterations, y[ilo], rTol, fTol);
                results.success = false;
                results.functionValueAtMinimum = y[0];
                results.bestX = pmin;
                return results;
            }
            results.nIterations += 2;
            /*
             * Begin new iteration. First try reflection through the face of the simplex of the highest scoring point
             */
            double ytry = amotry(p, y, psum, ihi, -1.0, fn);
            if (ytry <= y[ilo])
                /*
                 * gives result better than best point, so try additional extrapolation by factor of 2
                 */
                ytry = amotry(p, y, psum, ihi, 2.0, fn);
            else if (ytry >= y[inhi]) {
                /*
                 * reflected point is worse that the second highest so look for an intermediate lower point - i.e.
                 * one-dimensional contraction
                 */
                double ysave = y[ihi];
                ytry = amotry(p, y, psum, ihi, 0.5, fn);
                if (ytry >= ysave) {
                    /*
                     * try contraction around the lowest scoring point
                     */
                    for (int i = 0; i < nVertices; i++) {
                        if (i != ilo) {
                            for (int j = 0; j < nDim; j++)
                                p[i][j] = psum[j] = 0.5 * (p[i][j] + p[ilo][j]);
                            y[i] = fn.calculate(psum);
                        }
                    }
                    results.nIterations += nDim;
                    psum = get_psum(p);
                }
            } else
                --results.nIterations; // correct the evaluation count
        } while (true);
    }

    private void swapPoints(int nDim, double[][] p, double[] y, double[] pmin, int ilo) {
        double tmp = y[ilo]; // swap the points
        y[ilo] = y[0];
        y[0] = y[ilo];

        for (int i = 0; i < nDim; i++) {
            tmp = p[ilo][i]; // swap the points
            p[ilo][i] = p[0][i];
            p[0][i] = tmp;
            pmin[i] = p[0][i];
        }
    }

    /**
     * 
     * @param p
     * @return
     */
    private double[] get_psum(double[][] p) {
        int nDim = p[0].length;
        int mpts = nDim + 1;
        double psum[] = new double[nDim];
        for (int j = 0; j < nDim; j++) {
            double sum = 0.0;
            for (int i = 0; i < mpts; i++)
                sum += p[i][j];
            psum[j] = sum;
        }
        return psum;
    }

    /**
     * Extrapolates by a factor fac through the face of the simplex across from the high point, tries it and replaces
     * the high point if the new point is better
     * 
     * @param p the points making up the simplex
     * @param y function values at each point in the simplex
     * @param psum
     * @param ihi
     * @param fac the factor by which to stretch the point being moved
     * @param func the class containing the function being evaluated
     * @return
     */
    private double amotry(double[][] p, double[] y, double[] psum, final int ihi, final double fac,
                    FunctionToMinimise func) {
        int nDim = p[0].length;
        double[] ptry = new double[nDim];
        double fac1 = (1.0 - fac) / nDim;
        double fac2 = fac1 - fac;
        for (int j = 0; j < nDim; j++)
            ptry[j] = psum[j] * fac1 - p[ihi][j] * fac2;
        double ytry = func.calculate(ptry);
        if (ytry < y[ihi]) {
            y[ihi] = ytry;
            for (int j = 0; j < nDim; j++) {
                psum[j] += ptry[j] - p[ihi][j];
                p[ihi][j] = ptry[j];
            }
        }
        return ytry;
    }
}
