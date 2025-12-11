package ats.algo.sport.darts;

import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.core.common.TeamId;
import ats.algo.sport.darts.DartProbs;
import ats.algo.sport.darts.DartTarget;
import ats.algo.sport.darts.LegState;
import ats.algo.sport.darts.PlayDart;
import ats.algo.sport.darts.DartProbs.BaseProbSet;

public class DartPlayTest {

    @Test
    public void test1() {
        PlayDart dp = new PlayDart();
        DartProbs playerA = new DartProbs(BaseProbSet.UNITTESTSET1, 1.0, 1.0);
        LegState ls = new LegState(false);
        ls.startNewLeg(TeamId.A);
        double totalScore = 0;
        DartTarget target = new DartTarget(0, 0);
        for (int i = 0; i < 10000; i++) {
            DartTarget dartResult = dp.throwDart(playerA, ls, target);
            totalScore += dartResult.multiplier * dartResult.no;
        }
        double avgScore = totalScore / 10000;
        assertTrue(avgScore > 41.1 || avgScore < 41.3); // 41.2 expected

        //
        // emulate 2xT20 already thrown
        //
        DartTarget dt = new DartTarget(3, 20);
        totalScore = 0;
        ls.startNewLeg(TeamId.A);
        ls.threeDartSet.noDartsThrown = 2;
        ls.threeDartSet.setActual(1, dt);
        ls.threeDartSet.setActual(2, dt);
        for (int i = 0; i < 10000; i++) {
            DartTarget dartResult = dp.throwDart(playerA, ls, target);
            totalScore += dartResult.multiplier * dartResult.no;
        }
        avgScore = totalScore / 10000;
        assertTrue(avgScore > 40.5 || avgScore < 40.7); // 40.6 expected
        //
        // emulate throwing for bull
        //
        ls.startNewLeg(TeamId.A);
        ls.playerA.Points = 50;
        ls.playerB.Points = 30;
        totalScore = 0;
        for (int i = 0; i < 10000; i++) {
            DartTarget dartResult = dp.throwDart(playerA, ls, target);
            totalScore += dartResult.multiplier * dartResult.no;
        }
        avgScore = totalScore / 10000;
        assertTrue(target.multiplier == 2 && target.no == 25);
        assertTrue(avgScore > 20.675 || avgScore < 20.875); // 21.775 expected
        //
        // emulate going for a standard checkout score
        //
        ls.startNewLeg(TeamId.A);
        ls.playerA.Points = 137;
        ls.playerB.Points = 30;
        totalScore = 0;
        for (int i = 0; i < 10000; i++) {
            DartTarget dartResult = dp.throwDart(playerA, ls, target);
            totalScore += dartResult.multiplier * dartResult.no;
        }
        avgScore = totalScore / 10000;
        assertTrue(target.multiplier == 3 && target.no == 17);
        assertTrue(avgScore > 34.9 || avgScore < 35.1); // 35.0 expected
        //
        // emulate going for a standard checkout score when bull is not better
        //
        ls.startNewLeg(TeamId.A);
        ls.playerA.Points = 95;
        ls.playerB.Points = 200;
        totalScore = 0;
        for (int i = 0; i < 10000; i++) {
            DartTarget dartResult = dp.throwDart(playerA, ls, target);
            totalScore += dartResult.multiplier * dartResult.no;
        }
        avgScore = totalScore / 10000;
        assertTrue(target.multiplier == 3 && target.no == 19);
        assertTrue(avgScore > 39.9 || avgScore < 40.1); // 35.0 expected
        //
        // emulate going for a standard checkout score when bull IS better
        //
        ls.startNewLeg(TeamId.A);
        ls.playerA.Points = 95;
        ls.playerB.Points = 30;
        totalScore = 0;
        for (int i = 0; i < 10000; i++) {
            DartTarget dartResult = dp.throwDart(playerA, ls, target);
            totalScore += dartResult.multiplier * dartResult.no;
        }
        avgScore = totalScore / 10000;
        assertTrue(target.multiplier == 3 && target.no == 15);
        assertTrue(avgScore > 32.3 || avgScore < 32.5); // 35.0 expected
        //
        // test the score avoidance logic
        //
        ls.startNewLeg(TeamId.A);
        ls.playerA.Points = 219;
        ls.playerB.Points = 501;
        @SuppressWarnings("unused")
        DartTarget tmp = dp.throwDart(playerA, ls, target);
        assertTrue(target.no == 16 && target.multiplier == 3);
        ls.playerA.Points = 238;
        ls.playerB.Points = 501;
        tmp = dp.throwDart(playerA, ls, target);
        assertTrue(target.no == 20 && target.multiplier == 3);
    }

}

