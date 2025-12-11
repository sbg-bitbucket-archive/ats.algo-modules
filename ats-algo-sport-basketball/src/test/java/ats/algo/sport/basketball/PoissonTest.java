package ats.algo.sport.basketball;

import ats.algo.genericsupportfunctions.MethodName;
import org.junit.Test;

import ats.algo.genericsupportfunctions.RandomNoGenerator;

public class PoissonTest {

    private int tTotal = 3600;
    int tInterval = 2;
    int mcCount = 12500;
    double lambdaA = 85.5;
    double lambdaB = 75;
    double lastScoredWindow = 10;

    @FunctionalInterface
    interface SetParams {
        public void set(int i);
    }

    @FunctionalInterface
    interface AdjustProbs {
        public void adjust();
    }

    @FunctionalInterface
    interface PointsScored {
        public int get();
    }

    private class Results {
        int nPointsA;
        int nPointsB;
    }

    private double alpha;
    private int nIterations;
    int pointsA;
    int pointsB;
    double targetProbA;
    double targetProbB;
    double probA;
    double probB;
    int iterationNo;
    boolean lastToScoreWasA;
    boolean scoreInMatch;
    int timeAtLastScore;
    Results results = new Results();

    private void adjustProbsBaseCaseAlgo() {
        probA = targetProbA;
        probB = targetProbB;
    }

    private void adjustProbsForMeanSeekingAlgo() {
        double expectedPointsA = targetProbA * iterationNo;
        double expectedPointsB = targetProbB * iterationNo;
        double pAdjustA = alpha * (expectedPointsA - ((double) pointsA)) / nIterations;
        double pAdjustB = alpha * (expectedPointsB - ((double) pointsB)) / nIterations;
        probA = targetProbA + pAdjustA;
        probB = targetProbB + pAdjustB;
    }

    private void adjustProbsForPointsDiffAlgo() {
        double expectedPointsDiff = (targetProbA - targetProbB) * iterationNo;
        // // System.out.println(targetProbA +" "+ targetProbB+" "+ iterationNo);
        double actualPointsDiff = pointsA - pointsB;
        double pAdjust = alpha * ((double) (expectedPointsDiff - actualPointsDiff)) / nIterations;
        probA = targetProbA + pAdjust;
        probB = targetProbB - pAdjust;
    }

    private void adjustProbsForPointsVariesByTimeAlgo() {
        double adjFactor = 2.0 * ((double) iterationNo) / (double) (nIterations - 1);
        probA = lambdaA * adjFactor / nIterations;
        probB = lambdaB * adjFactor / nIterations;
    }

    private void adjustProbsForWhoLastScoredAlgo() {
        int timeNow = nIterations * tInterval;
        if (scoreInMatch && (timeNow - timeAtLastScore) <= lastScoredWindow) {
            if (lastToScoreWasA) {
                probA = targetProbA * (1 - alpha);
                probB = targetProbB * (1 + alpha);
            } else {
                probA = targetProbA * (1 + alpha);
                probB = targetProbB * (1 - alpha);
            }
        } else {
            probA = targetProbA;
            probB = targetProbB;
        }
    }

    private int getPointsScoredBaseCase() {
        return 1;
    }

    private static final double[] pointsDistn = {0.0, 0.1, 0.6, 0.3};

    private int getPointsScoredVariableCase() {
        double r = RandomNoGenerator.nextDouble();
        if (r < pointsDistn[1])
            return 1;
        if (r < pointsDistn[1] + pointsDistn[2])
            return 2;
        return 3;
    }

    private void calc(AdjustProbs adjustProbs, PointsScored pointsScored) {
        nIterations = tTotal / tInterval;
        pointsA = 0;
        pointsB = 0;
        targetProbA = lambdaA / nIterations;
        targetProbB = lambdaB / nIterations;
        scoreInMatch = false;
        for (iterationNo = 0; iterationNo < nIterations; iterationNo++) {
            adjustProbs.adjust();
            double r = RandomNoGenerator.nextDouble();
            if (r < probA) {
                pointsA += pointsScored.get();
                scoreInMatch = true;
                lastToScoreWasA = true;
                timeAtLastScore = iterationNo * tInterval;
            } else if (r < probA + probB) {
                pointsB += pointsScored.get();
                scoreInMatch = true;
                lastToScoreWasA = false;
                timeAtLastScore = iterationNo * tInterval;
            }
        }
        results.nPointsA = pointsA;
        results.nPointsB = pointsB;
    }

    @SuppressWarnings("unused")
    private void runTest(SetParams setParams, AdjustProbs adjustProbs, PointsScored pointsScored) {
        // System.out.printf("i, meanA, varA, meanB, varB, pAwins\n");
        for (int j = 0; j < 10; j++) {
            setParams.set(j);
            long sumPointsA = 0;
            long sumPointsA2 = 0;
            long sumPointsB = 0;
            long sumPointsB2 = 0;
            int sumAWins = 0;
            for (int i = 0; i < mcCount; i++) {
                calc(adjustProbs, pointsScored);
                sumPointsA += results.nPointsA;
                sumPointsA2 += results.nPointsA * results.nPointsA;
                sumPointsB += results.nPointsB;
                sumPointsB2 += results.nPointsB * results.nPointsB;
                if (results.nPointsA > results.nPointsB)
                    sumAWins++;
            }
            double meanA = ((double) sumPointsA) / ((double) mcCount);
            double varA = ((double) sumPointsA2) / ((double) mcCount) - meanA * meanA;
            double meanB = ((double) sumPointsB) / ((double) mcCount);
            double varB = ((double) sumPointsB2) / ((double) mcCount) - meanB * meanB;
            double pAWins = ((double) sumAWins) / mcCount;
            // System.out.printf("%d, %.2f, %.2f, %.2f, %.2f, %.2f\n", j, meanA, varA, meanB, varB, pAWins);
        }
    }

    private void setParamsForAlgoTests(int i) {
        alpha = (double) i / 10;
        tInterval = 2;

    }

    @Test
    public void test() {
        MethodName.log();
        /*
         * test of effect of interval size
         */
        // System.out.printf("Test effect of varying interval size. tInterval = 1 2 ...10 secs\n");
        SetParams setParams = (i) -> {
            tInterval = i + 1;
        };
        AdjustProbs adjustProbs = () -> adjustProbsBaseCaseAlgo();
        PointsScored pointsScored = () -> getPointsScoredBaseCase();
        runTest(setParams, adjustProbs, pointsScored);
        /*
         * test targetseekingalgo
         */
        // System.out.printf("\nMean seeking Algo. Vary alpha = 0.0 0.1...0.9\n");
        setParams = (i) -> setParamsForAlgoTests(i);
        adjustProbs = () -> adjustProbsForMeanSeekingAlgo();
        pointsScored = () -> getPointsScoredBaseCase();
        runTest(setParams, adjustProbs, pointsScored);
        /*
         * test points difference algo
         */
        // System.out.printf("\nPoints difference algo. Vary alpha = 0.0 0.1...0.9\n");
        setParams = (i) -> setParamsForAlgoTests(i);
        adjustProbs = () -> adjustProbsForPointsDiffAlgo();
        pointsScored = () -> getPointsScoredBaseCase();
        runTest(setParams, adjustProbs, pointsScored);
        /*
         * = test points target varies by time algo
         */
        // // System.out.printf("\nPoints target varies by time algo. Vary alpha = 0.0 0.1...0.9\n");
        setParams = (i) -> setParamsForAlgoTests(i);
        adjustProbs = () -> adjustProbsForPointsVariesByTimeAlgo();
        pointsScored = () -> getPointsScoredBaseCase();
        // runTest(setParams, adjustProbs, pointsScored);
        /*
         * = test who last scored algo
         */
        // // System.out.printf("\nwho last scored algo. Vary alpha = 0.0 0.1...0.9\n");
        setParams = (i) -> setParamsForAlgoTests(i);
        adjustProbs = () -> adjustProbsForWhoLastScoredAlgo();
        pointsScored = () -> getPointsScoredBaseCase();
        // runTest(setParams, adjustProbs, pointsScored);
        /*
         * = variable points algo
         */
        // // System.out.printf("\nVariable points algo. Vary alpha = 0.0 0.1...0.9\n");
        setParams = (i) -> setParamsForAlgoTests(i);
        adjustProbs = () -> adjustProbsForPointsDiffAlgo();
        pointsScored = () -> getPointsScoredVariableCase();
        double avgPointsPerScore = 0;
        for (int i = 0; i < pointsDistn.length; i++)
            avgPointsPerScore += i * pointsDistn[i];
        lambdaA /= avgPointsPerScore;
        lambdaB /= avgPointsPerScore;
        // runTest(setParams, adjustProbs, pointsScored);

    }
}
