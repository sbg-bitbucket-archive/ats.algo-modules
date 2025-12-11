package ats.algo.sport.football;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.base.Stopwatch;

public class FootballGoalDistributionTest {
    @Test
    /**
     * not really a test, but lets the playMatch method get exercised once, to allow any loops etc to be debugged
     */
    public void test() {
        MethodName.log();
        FootballMatchFormat matchFormat = new FootballMatchFormat();
        FootballMatchState simMatchState = new FootballMatchState(matchFormat);
        FootballMatchParams matchParams = new FootballMatchParams(matchFormat);
        matchParams.setGoalTotal(4, 0);
        matchParams.setGoalSupremacy(1, 0);
        matchParams.setHomeLoseBoost(0.5, 0);
        matchParams.setAwayLoseBoost(0.5, 0);
        int v3ColIndex = 2;
        double timeBasedFactor = 0.0;
        double timeBasedFactorCorner = 0.0;
        double timeBasedFactorCard = 0.0;
        Stopwatch stopwatch = Stopwatch.createStarted();
        // for (int i = 0; i < 1000000; i++) {
        for (int i = 0; i < 1000000; i++)
            timeBasedFactor = GoalDistribution.getGoalDistribution(simMatchState.getMatchPeriod(),
                            simMatchState.getElapsedTimeSecs(), simMatchState.getInjuryTimeSecs(), v3ColIndex, true);
        stopwatch.stop();
        // // System.out.println("running a match simulation cost time is :" +
        // // stopwatch);
        stopwatch = Stopwatch.createStarted();
        for (int i = 0; i < 1000000; i++)
            timeBasedFactorCorner = GoalDistribution.getCornerDistribution(false, simMatchState.getMatchPeriod(),
                            simMatchState.getElapsedTimeSecs(), simMatchState.getInjuryTimeSecs());
        stopwatch.stop();

        stopwatch = Stopwatch.createStarted();
        for (int i = 0; i < 1000000; i++)
            timeBasedFactorCard = GoalDistribution.getCardDistribution(false, simMatchState.getMatchPeriod(),
                            simMatchState.getElapsedTimeSecs(), simMatchState.getInjuryTimeSecs());
        // }
        stopwatch.stop();
        // // System.out.println("running a match simulation cost time is :" +
        // // stopwatch);
        // // System.out.println("running a match simulation cost time is :" +
        // // stopwatch);
        // // System.out.println(
        // // "Running 100,000 times getGoalDistribution and
        // // getCornerDistribution,getCardDistribution " + stopwatch);
        assertEquals(timeBasedFactor, 0.0014, 0.00005);
        assertEquals(timeBasedFactorCorner, 0.00185185, 0.00005);
        assertEquals(timeBasedFactorCard, 0.00122, 0.00005);
        FootballMatch match = new FootballMatch(matchFormat, simMatchState, matchParams, false);
        for (int m = 0; m < 10; m++) {
            stopwatch = Stopwatch.createStarted();
            for (int i = 0; i < 62500; i++)
                match.playMatch();
            stopwatch.stop();
            // System.out.println("running a match simulation cost time is :" + stopwatch);
        }

    }
}
