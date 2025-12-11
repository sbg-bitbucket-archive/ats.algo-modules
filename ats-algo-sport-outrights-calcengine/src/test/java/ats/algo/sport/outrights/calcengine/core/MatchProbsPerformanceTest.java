package ats.algo.sport.outrights.calcengine.core;

import ats.algo.genericsupportfunctions.ConsoleInput;
import ats.algo.genericsupportfunctions.StopWatch;
import ats.algo.sport.outrights.calcengine.core.MatchProbs;

public class MatchProbsPerformanceTest {



    public static void main(String[] args) {
        RatingsFactors f = RatingsFactors.defaultFiveThirtyEightRatingsFactors();
        MatchProbs probs = new MatchProbs();
        double homeAttack = 2.4;
        double homeDefense = 1.2;
        double awayAttack = 2.4;
        double awayDefense = 1.2;
        int n = 10000000;
        StopWatch sw = new StopWatch();
        ConsoleInput.readString("matchProbs performance test.  Press any key to start", "");
        sw.start();
        for (int i = 0; i < n; i++)
            probs.initialise(homeAttack, homeDefense, awayAttack, awayDefense, f, false);
        sw.stop();
        long el = sw.getElapsedTimeMs();
        double t = ((double) (el * 1000)) / (double) n;
        System.out.printf("Finished in %d mS. time per invocation %.3f us\n", el, t);

    }
}
