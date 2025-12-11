package ats.algo.sport.outrights.calcengine.core;

import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.core.common.TeamId;
import ats.algo.sport.outrights.calcengine.core.MatchProbs;
import ats.algo.sport.outrights.calcengine.core.Team;

public class MatchProbsTest {

    @Test
    public void calculateFromExpectedGoalsTest() {
        FullMatchProbs probs = new FullMatchProbs(3.6, 2.4);
        System.out.println(probs);
        assertEquals(.610, probs.getProbHomeWin(), 0.001);
        assertEquals(probs.getGoalsHcap(), 1);
        assertEquals(.446, probs.getProbHomeWinsHcap(), 0.001);
    }

    @Test
    public void calculateFromRatingsTest() {
        Team teamA = new Team("T1", "Team1", "538Name", 2.4, 1.2);
        Team teamB = new Team("T2", "Team2", "538Name", 2.4, 1.2);
        RatingsFactors f = RatingsFactors.defaultFiveThirtyEightRatingsFactors();
        FullMatchProbs probs = new FullMatchProbs(teamA, teamB, f, false);
        System.out.println(probs);
        assertEquals(.464, probs.getProbHomeWin(), 0.005);
        assertEquals(probs.getGoalsHcap(), 0);
        assertEquals(.561, probs.getProbHomeWinsHcap(), 0.005);
    }

    @Test
    public void simulateGoalsTest() {
        double[] goalsProbs = {.1, .2, .3, .5};
        assertEquals(1, MatchProbs.simulateGoals(.15, goalsProbs));
        assertEquals(0, MatchProbs.simulateGoals(.09, goalsProbs));
        assertEquals(3, MatchProbs.simulateGoals(.82, goalsProbs));
    }

    @Test
    public void simulateMatchTest1() {
        MatchProbs matchProbs = new MatchProbs();
        matchProbs.set(0, 0, 0, 0.16);
        matchProbs.set(1, 0, 1, 0.24);
        matchProbs.set(2, 1, 0, 0.24);
        matchProbs.set(3, 1, 1, 0.36);
        matchProbs.setProbsSize(4);
        int nIterations = 1000000;
        int n00 = 0;
        int n01 = 0;
        int n10 = 0;
        int n11 = 0;
        int nH = 0;
        int nA = 0;
        int nD = 0;
        for (int i = 0; i < nIterations; i++) {
            FixtureResult result = matchProbs.simulateMatch(false);
            int n = 10 * result.getScoreA() + result.getScoreB();
            switch (n) {
                case 0:
                    n00++;
                    break;
                case 01:
                    n01++;
                    break;
                case 10:
                    n10++;
                    break;
                case 11:
                    n11++;
                    break;
                default:
                    throw new IllegalArgumentException("wtf");
            }
            if (result.getScoreA() > result.getScoreB())
                nH++;
            else if (result.getScoreA() < result.getScoreB())
                nA++;
            else
                nD++;
        }
        double xIterations = (double) nIterations;
        double p00 = ((double) n00) / xIterations;
        double p01 = ((double) n01) / xIterations;
        double p10 = ((double) n10) / xIterations;
        double p11 = ((double) n11) / xIterations;
        double pH = ((double) nH) / xIterations;
        double pA = ((double) nA) / xIterations;
        double pD = ((double) nD) / xIterations;
        System.out.printf("pH: %.3f, pA: %.3f, pD: %.3f\n", pH, pA, pD);
        System.out.printf("p00: %.3f, p01: %.3f, p10: %.3f, p11: %.3f\n", p00, p01, p10, p11);
        assertEquals(0.16, p00, 0.005);
        assertEquals(0.24, p01, 0.005);
        assertEquals(0.24, p10, 0.005);
        assertEquals(0.36, p11, 0.005);
    }

    @Test
    public void simulateMatchTest() {
        MatchProbs probs = new MatchProbs(2.4, 1.6);
        System.out.printf("pH: %.3f, pA: %.3f, pD: %.3f\n", probs.getProbHomeWin(), probs.getProbAwayWin(),
                        probs.getProbDraw());
        /*
         * gives pH= .555, pA=.252, pDraw=.193
         */
        int nIterations = 1000000;
        int nH = 0;
        int nA = 0;
        int nD = 0;
        for (int i = 0; i < nIterations; i++) {
            FixtureResult result = probs.simulateMatch(false);
            if (result.getScoreA() > result.getScoreB())
                nH++;
            else if (result.getScoreA() < result.getScoreB())
                nA++;
            else
                nD++;
            if (result.getWinningTeamID() != null)
                throw new IllegalArgumentException("winning teamID set - should be null");
        }
        double xIterations = (double) nIterations;
        double pH = ((double) nH) / xIterations;
        double pA = ((double) nA) / xIterations;
        double pD = ((double) nD) / xIterations;
        System.out.printf("pH: %.4f, pA: %.4f, pD: %.4f", pH, pA, pD);
        assertEquals(0.555, pH, 0.005);
        assertEquals(0.252, pA, 0.005);
        assertEquals(0.193, pD, 0.005);
    }

    @Test
    public void simulateMatchTestWithMustHaveWinner() {
        MatchProbs probs = new MatchProbs(2.4, 1.6);
        System.out.printf("pH: %.3f, pA: %.3f, pD: %.3f\n", probs.getProbHomeWin(), probs.getProbAwayWin(),
                        probs.getProbDraw());
        /*
         * gives pH= .555, pA=.252, pDraw=.193
         */
        int nIterations = 1000000;
        int nH = 0;
        int nA = 0;
        int nD = 0;
        int nAWins = 0;
        int nBWins = 0;
        for (int i = 0; i < nIterations; i++) {
            FixtureResult result = probs.simulateMatch(true);
            if (result.getScoreA() > result.getScoreB())
                nH++;
            else if (result.getScoreA() < result.getScoreB())
                nA++;
            else
                nD++;
            if (result.getWinningTeamID() == TeamId.A)
                nAWins++;
            else if (result.getWinningTeamID() == TeamId.B)
                nBWins++;
            else
                throw new IllegalArgumentException("teamID is not set");
        }
        double xIterations = (double) nIterations;
        double pH = ((double) nH) / xIterations;
        double pA = ((double) nA) / xIterations;
        double pD = ((double) nD) / xIterations;
        double pAWins = ((double) nAWins) / xIterations;
        double pBWins = ((double) nBWins) / xIterations;
        System.out.printf("pH: %.4f, pA: %.4f, pD: %.4f,, pAWins: %.4f, pBWins: %.4f", pH, pA, pD, pAWins, pBWins);
        assertEquals(0.555, pH, 0.005);
        assertEquals(0.252, pA, 0.005);
        assertEquals(0.193, pD, 0.005);
        assertEquals(0.688, pAWins, 0.005);
        assertEquals(0.312, pBWins, 0.005);
    }
}
