package ats.algo.sport.darts;

import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.sport.darts.DartProbs;
import ats.algo.sport.darts.DartTarget;
import ats.algo.sport.darts.DartProbs.BaseProbSet;

public class DartProbsTest {
    DartProbs dartProbs;

    @Test
    public void test() {
        /*
         * start with base case
         */
        dartProbs = new DartProbs(BaseProbSet.LIVE, 1, 1);
        runTest(.47, .41);
        /*
         * increase skill factor
         */
        double alphaDouble = calcAlpha(1.1, 0.47);
        double alphaTriple = calcAlpha(1.1, 0.41);
        dartProbs.updateProbs(1.1, 1);
        runTest(.47 * alphaDouble, .41 * alphaTriple);
        /*
         * change triplesvsDoubles
         */
        alphaDouble = calcAlpha(0.8, 0.47);
        alphaTriple = calcAlpha(1.1, 0.41);
        dartProbs.updateProbs(1.0, 1.2);
        runTest(.47 * alphaDouble, .41 * alphaTriple);
        /*
         * change triplesvsDoubles to 0
         */
        alphaDouble = calcAlpha(2, 0.47);
        alphaTriple = calcAlpha(0.5, 0.41);
        dartProbs.updateProbs(1.0, 0);
        runTest(.47 * alphaDouble, .41 * alphaTriple);
        /*
         * change to 2
         */
        alphaDouble = calcAlpha(0, 0.47);
        alphaTriple = calcAlpha(1.5, 0.41);
        dartProbs.updateProbs(1.0, 2);
        runTest(.47 * alphaDouble, .41 * alphaTriple);
    }

    private double calcAlpha(double skill, double p) {
        double alpha;
        if (skill > 1)
            alpha = (1 - (1 - p) / skill) / p;
        else
            alpha = skill;
        return alpha;
    }

    private void runTest(double expProbDouble, double expProbTriple) {
        int nIterations = 100000;
        int nDouble = 0;
        int nTriple = 0;
        double p = dartProbs.getProb(DartProbs.aimedAtDouble);
        assertEquals(expProbDouble, p, 0.0001);
        p = dartProbs.getProb(DartProbs.aimedAtTriple);
        assertEquals(expProbTriple, p, 0.0001);
        for (int i = 0; i < nIterations; i++) {
            DartTarget result = dartProbs.getActual(DartProbs.aimedAtDouble, 15);
            if (result.multiplier == 2 && result.no == 15)
                nDouble++;
            result = dartProbs.getActual(DartProbs.aimedAtTriple, 20);
            if (result.multiplier == 3 && result.no == 20)
                nTriple++;
        }
        double probHitDouble = ((double) nDouble) / nIterations;
        double probHitTriple = ((double) nTriple) / nIterations;
        assertEquals(expProbDouble, probHitDouble, 0.02);
        assertEquals(expProbTriple, probHitTriple, 0.02);
    }

}
