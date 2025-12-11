package ats.algo.momentum;

import ats.algo.genericsupportfunctions.Gaussian;

/**
 * provides methods for updating estimates of mean and standard deviation of model parameters given that an event
 * occurs. Supplied priors should be Gaussian. The posteriors are approximated as Gaussians. provides support for one,
 * two or three parameters. c.f. separate paper for details of methodology
 * 
 * @author Geoff
 * 
 */
public class Bayes {

    private Gaussian[] priorParams;
    private Gaussian[] posteriorParams;
    private JointProbabilityDistribution jointPd;
    private int nParams;

    @FunctionalInterface
    /**
     * defines the probability that the event occurred as a function of the list of skills parameters x[].
     * 
     * @author Geoff
     * 
     */
    public interface JointProbabilityDistribution {
        public double probability(double x[]);
    }

    /**
     * 
     * @param nParams - the number of parameters - currently must be 1,2 or 3
     */
    public Bayes(int nParams, JointProbabilityDistribution fn) {
        if ((nParams < 1) || (nParams > 3)) {
            throw new IllegalArgumentException("nParams must be 1,2 or 3");
        }
        jointPd = fn;
        priorParams = new Gaussian[nParams];
        posteriorParams = new Gaussian[nParams];
        for (int i = 0; i < nParams; i++) {
            posteriorParams[i] = new Gaussian(1, 0);
        }
        this.nParams = nParams;
    }

    public void setPriorParams(Gaussian[] params) {
        for (int i = 0; i < params.length; i++) {
            this.priorParams[i] = params[i].copy();
        }
    }

    public Gaussian[] getPosteriorParams() {
        return posteriorParams;
    }

    final static double lBound = -4; // no stdDevns
    final static double uBound = +4;
    double interval; // integration interval
    double mx0; // counters to hold the accumulating sums mxn[i] is the sum for x[i]^n
    double[] mx1;
    double[] mx2;
    double dxdy; // the volume of each element in the integrand
    double x[];
    double a[];
    double b[];
    double xDash[];
    boolean eventOccurred;

    /**
     * recursive function to calculate the nParams-dimensional integral keeps call itself until the innermost loop is
     * reached (n=0) when it does the sums
     * 
     * @param n
     */
    private void calcIntegral(int n) {
        for (a[n] = lBound; a[n] <= uBound; a[n] += interval) {
            b[n] = a[n] + interval;
            if (n == 0) {
                double expFactor = 0;
                for (int i = 0; i < nParams; i++) {
                    x[i] = (b[i] + a[i]) / 2;
                    xDash[i] = priorParams[i].getMean() + x[i] * priorParams[i].getStdDevn();
                    expFactor += x[i] * x[i];
                }
                double commonFactor = Math.exp(-expFactor / 2) * dxdy;
                if (eventOccurred)
                    commonFactor *= jointPd.probability(xDash);
                else
                    commonFactor *= 1 - jointPd.probability(xDash);
                mx0 += commonFactor;
                for (int i = 0; i < nParams; i++) {
                    mx1[i] += xDash[i] * commonFactor;
                    mx2[i] += xDash[i] * xDash[i] * commonFactor;
                }
            } else {
                calcIntegral(n - 1);
            }
        }
    }

    /**
     * updates the skills estimates given that the event for which the probability is defined by the supplied function
     * occurred (or didn't)
     */
    public void updateSkills(boolean eventOccurred) {
        /*
         * need to evaluate 5 nParams dimensiona integrals to calculate the first two moments of x and y perform the
         * integration over the interval plus or minus 4 sd's either side of the mean for each variable
         */
        this.eventOccurred = eventOccurred;
        interval = 0.05; // integration interval
        mx0 = 0; // counters to hold the accumulating sums mxn[i] is the sum for x[i]^n
        mx1 = new double[nParams];
        mx2 = new double[nParams];
        dxdy = 1; // the volume of each element in the integrand
        for (int i = 0; i < nParams; i++) {
            mx1[i] = 0;
            mx2[i] = 0;
            dxdy *= interval;
        }
        x = new double[nParams];
        a = new double[nParams];
        b = new double[nParams];
        xDash = new double[nParams];
        calcIntegral(nParams - 1);
        double pi = 3.1415926535;
        mx0 = mx0 / (2 * pi);
        for (int i = 0; i < nParams; i++) {
            mx1[i] = mx1[i] / (2 * pi);
            mx2[i] = mx2[i] / (2 * pi);
        }
        /*
         * now we have the moments, calculate the updated mean and variance for each distn
         */
        for (int i = 0; i < nParams; i++) {
            posteriorParams[i].setMean(mx1[i] / mx0);
            posteriorParams[i].setStdDevn(
                            Math.sqrt(mx2[i] / mx0 - posteriorParams[i].getMean() * posteriorParams[i].getMean()));
        }
    }

}
