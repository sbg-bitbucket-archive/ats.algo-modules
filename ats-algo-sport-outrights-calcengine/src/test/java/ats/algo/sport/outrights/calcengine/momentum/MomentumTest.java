package ats.algo.sport.outrights.calcengine.momentum;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ats.algo.genericsupportfunctions.Gaussian;
import ats.algo.sport.outrights.calcengine.core.RatingsFactors;

public class MomentumTest {

    @Test
    public void momentumTest() {
        MethodName.log();
        Rating teamA = new Rating(new Gaussian(3.0, 0.5), new Gaussian(2.0, 0.1));
        Rating teamB = new Rating(new Gaussian(4.0, 0.2), new Gaussian(2.0, 0.4));
        Rating teamAb = teamA.copy();
        Rating teamBb = teamB.copy();

        Momentum momentum = new Momentum(new RatingsFactors(1.0, -1.0, 0.0, 0.0, 0.0, 0.1));
        momentum.UPDATE_STD_DEVNS = true;

        momentum.updateRatings(teamA, teamB, 0, 0, false);
        // System.out.printf("scoreA: %d, scoreB: %d\n", 0, 0);
        // System.out.println("Befor: A" + teamAb);
        // System.out.println("After: A" + teamA);
        // System.out.println("Befor: B" + teamBb);
        // System.out.println("After: B" + teamB);
        assertEquals(2.7500, teamA.getRatingAttack().getMean(), 0.0001);
        assertEquals(0.5000, teamA.getRatingAttack().getStdDevn(), 0.0001);
        assertEquals(2.1600, teamB.getRatingDefense().getMean(), 0.0001);
        assertEquals(0.4000, teamB.getRatingDefense().getStdDevn(), 0.0001);
        assertEquals(3.9600, teamB.getRatingAttack().getMean(), 0.0001);
        assertEquals(0.2000, teamB.getRatingAttack().getStdDevn(), 0.0001);
        assertEquals(2.0100, teamA.getRatingDefense().getMean(), 0.0001);
        assertEquals(0.1000, teamA.getRatingDefense().getStdDevn(), 0.0001);
        teamA = teamAb.copy();
        teamB = teamBb.copy();
        momentum.updateRatings(teamA, teamB, 1, 1, false);
        // System.out.printf("scoreA: %d, scoreB: %d\n", 1, 1);
        // System.out.println("Befor: A" + teamAb);
        // System.out.println("After: A" + teamA);
        // System.out.println("Befor: B" + teamBb);
        // System.out.println("After: B" + teamB);

        assertEquals(3.1737, teamA.getRatingAttack().getMean(), 0.0001);
        assertEquals(0.2654, teamA.getRatingAttack().getStdDevn(), 0.0001);
        assertEquals(1.8888, teamB.getRatingDefense().getMean(), 0.0001);
        assertEquals(0.2940, teamB.getRatingDefense().getStdDevn(), 0.0001);

        assertEquals(3.9805, teamB.getRatingAttack().getMean(), 0.0001);
        assertEquals(0.1989, teamB.getRatingAttack().getStdDevn(), 0.0001);
        assertEquals(2.0049, teamA.getRatingDefense().getMean(), 0.0001);
        assertEquals(0.0999, teamA.getRatingDefense().getStdDevn(), 0.0001);
        for (int i = 0; i <= 1; i++) {
            teamAb.setRatingAttack(new Gaussian(3.0, 0.1 * (i + 1)));
            teamBb.setRatingDefense(new Gaussian(1.0, 0.1));
            // System.out.printf("Prior , rAttack: %.3f, rDefense: %.3f, stdDevn: %.3f, %.3f\n",
            // teamAb.getRatingAttack().getMean(), teamBb.getRatingDefense().getMean(),
            // teamAb.getRatingAttack().getStdDevn(), teamBb.getRatingDefense().getStdDevn());
            for (int n = 0; n <= 6; n++) {
                teamA = teamAb.copy();
                teamB = teamBb.copy();
                momentum.updateRatings(teamA, teamB, n, n, false);
                // System.out.printf("score: %d, rAttack: %.3f, rDefense: %.3f, stdDevn: %.3f, %.3f\n", n,
                // teamA.getRatingAttack().getMean(), teamB.getRatingDefense().getMean(),
                // teamA.getRatingAttack().getStdDevn(), teamB.getRatingDefense().getStdDevn());
            }

        }
    }

    // @Test
    public void momentumTest2() {
        MethodName.log();
        Rating teamA = new Rating(new Gaussian(1.1236670749653113, 0.09915245903068708),
                        new Gaussian(0.5630796015454568, .09673103876736208));
        Rating teamB = new Rating(new Gaussian(1.1236670749653113, 0.09915245903068708),
                        new Gaussian(0.5630796015454568, .09673103876736208));
        @SuppressWarnings("unused")
        Rating teamAb = teamA.copy();
        @SuppressWarnings("unused")
        Rating teamBb = teamB.copy();
        Momentum momentum = new Momentum(new RatingsFactors(1.0, 1.0, 0.337 - 1.605, -1.605, -1.605, 0.1));
        // System.out.printf("Prior , rAttack: %.3f, rDefense: %.3f, stdDevn: %.3f, %.3f\n",
        // teamAb.getRatingAttack().getMean(), teamBb.getRatingDefense().getMean(),
        // teamAb.getRatingAttack().getStdDevn(), teamBb.getRatingDefense().getStdDevn());
        momentum.updateRatings(teamA, teamB, 1, 1, false);
        // System.out.printf("score: %d, rAttack: %.3f, rDefense: %.3f, stdDevn: %.3f, %.3f\n", 1,
        // teamA.getRatingAttack().getMean(), teamB.getRatingDefense().getMean(),
        // teamA.getRatingAttack().getStdDevn(), teamB.getRatingDefense().getStdDevn());
    }

}
