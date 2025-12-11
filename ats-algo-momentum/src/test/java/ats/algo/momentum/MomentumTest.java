package ats.algo.momentum;

import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.genericsupportfunctions.Gaussian;

public class MomentumTest {

    private double testFunction1(double[] x) {
        return 1;
    }

    /*
     * prob A wins point depending who is on serve
     */
    private boolean onServeA;

    private double testFunction2(double[] x) {
        if (onServeA) {
            if (x[0] < 0)
                return 0;
            if (x[0] > 1)
                return 1;
            return x[0];
        } else {
            if (x[1] < 0)
                return 1;
            if (x[1] > 1)
                return 0;
            return 1 - x[1];
        }
    }

    @Test
    public void test() {
        /*
         * start with flat distn
         */
        Bayes bayes = new Bayes(2, (double[] z) -> testFunction1(z));
        Gaussian[] params = new Gaussian[2];
        params[0] = (new Gaussian(3, 1));
        params[1] = (new Gaussian(0, 1));
        bayes.setPriorParams(params);
        bayes.updateSkills(true);
        Gaussian[] adjParams = bayes.getPosteriorParams();
        assertEquals(3, adjParams[0].getMean(), 0.001);
        assertEquals(1, adjParams[0].getStdDevn(), 0.001);
        /*
         * pdf 2 emulates prob player winning point in tennis
         */
        bayes = new Bayes(2, (double[] z) -> testFunction2(z));
        /*
         * A on serve, wins point scenario
         */
        onServeA = true;
        params = new Gaussian[2];
        params[0] = new Gaussian(0.6, 0.1);
        params[1] = (new Gaussian(0.4, 0.2));
        bayes.setPriorParams(params);
        bayes.updateSkills(true);
        adjParams = bayes.getPosteriorParams();

        /*
         * expect skills A to increase, skillsB to stay same
         */
        double adjMuA = params[0].getMean() + (params[0].getStdDevn() * params[0].getStdDevn()) / params[0].getMean();
        double adjSigmaA = params[0].getStdDevn() * Math.sqrt(1 - params[0].getStdDevn() * params[0].getStdDevn()
                        / (params[0].getMean() * params[0].getMean()));
        assertEquals(adjMuA, adjParams[0].getMean(), 0.001);
        assertEquals(adjSigmaA, adjParams[0].getStdDevn(), 0.001);
        assertEquals(0.4, adjParams[1].getMean(), 0.001);
        assertEquals(0.2, adjParams[1].getStdDevn(), 0.001);
        /*
         * A on serve, A loses point scenario
         */
        params[0] = new Gaussian(0.6, 0.1);
        params[1] = new Gaussian(0.4, 0.2);
        bayes.setPriorParams(params);
        bayes.updateSkills(false);
        adjParams = bayes.getPosteriorParams();
        adjMuA = params[0].getMean() - (params[0].getStdDevn() * params[0].getStdDevn()) / (1 - params[0].getMean());
        adjSigmaA = params[0].getStdDevn() * Math.sqrt(1 - params[0].getStdDevn() * params[0].getStdDevn()
                        / ((1 - params[0].getMean()) * (1 - params[0].getMean())));
        assertEquals(adjMuA, adjParams[0].getMean(), 0.001);
        assertEquals(adjSigmaA, adjParams[0].getStdDevn(), 0.001);
        assertEquals(0.4, adjParams[1].getMean(), 0.001);
        assertEquals(0.2, adjParams[1].getStdDevn(), 0.001);
        /*
         * B on serve, A wins point
         */
        onServeA = false;
        params[0] = new Gaussian(0.6, 0.1);
        params[1] = new Gaussian(0.5, 0.1);
        bayes.setPriorParams(params);
        bayes.updateSkills(true);
        adjParams = bayes.getPosteriorParams();
        double adjMuB = params[1].getMean()
                        - (params[1].getStdDevn() * params[1].getStdDevn()) / (1 - params[1].getMean());
        double adjSigmaB = params[1].getStdDevn() * Math.sqrt(1 - params[1].getStdDevn() * params[1].getStdDevn()
                        / (params[1].getMean() * params[1].getMean()));
        assertEquals(.6, adjParams[0].getMean(), 0.001);
        assertEquals(.1, adjParams[0].getStdDevn(), 0.001);
        assertEquals(adjMuB, adjParams[1].getMean(), 0.001);
        assertEquals(adjSigmaB, adjParams[1].getStdDevn(), 0.001);
        /*
         * B on serve, B wins point
         */
        onServeA = false;
        params[0] = new Gaussian(0.6, 0.1);
        params[1] = new Gaussian(0.5, 0.1);
        bayes.setPriorParams(params);
        bayes.updateSkills(false);
        adjParams = bayes.getPosteriorParams();
        adjMuB = params[1].getMean() + (params[1].getStdDevn() * params[1].getStdDevn()) / (params[1].getMean());
        adjSigmaB = params[1].getStdDevn() * Math.sqrt(1 - params[1].getStdDevn() * params[1].getStdDevn()
                        / (params[1].getMean() * params[1].getMean()));
        assertEquals(.6, adjParams[0].getMean(), 0.001);
        assertEquals(.1, adjParams[0].getStdDevn(), 0.001);
        assertEquals(adjMuB, adjParams[1].getMean(), 0.001);
        assertEquals(adjSigmaB, adjParams[1].getStdDevn(), 0.001);
    }

    public double testFunction3(double[] x) {
        double z = 1 / (1 + Math.exp(-(x[0] - x[1]) / 100));
        return z;
    }

    /*
     * test proper joint distn
     * 
     */
    @Test
    public void test2() {

        Bayes bayes = new Bayes(2, (double[] z) -> testFunction3(z));
        Gaussian[] params = new Gaussian[2];
        params[0] = new Gaussian(200, 20);
        params[1] = (new Gaussian(200, 20));
        bayes.setPriorParams(params);
        bayes.updateSkills(true);
        Gaussian[] adjParams = bayes.getPosteriorParams();
        assertEquals(201.96, adjParams[0].getMean(), 0.001);
        assertEquals(19.894, adjParams[0].getStdDevn(), 0.001);
        assertEquals(198.04, adjParams[1].getMean(), 0.001);
        assertEquals(19.894, adjParams[1].getStdDevn(), 0.001);
    }
}
