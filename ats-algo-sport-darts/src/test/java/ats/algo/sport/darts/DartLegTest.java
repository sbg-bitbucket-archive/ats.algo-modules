package ats.algo.sport.darts;

import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.core.common.TeamId;
import ats.algo.sport.darts.DartProbs;
import ats.algo.sport.darts.DartTarget;
import ats.algo.sport.darts.LegResult;
import ats.algo.sport.darts.LegState;
import ats.algo.sport.darts.PlayLeg;
import ats.algo.sport.darts.DartProbs.BaseProbSet;

public class DartLegTest {

    private final int nTrials = 62500;
    private final double stDev = 0.5 / Math.sqrt(nTrials);
    private final double stDevsFromMean = 2.56; // means tests should pass at least 99% of the time


    @Test
    public void test1() {
        PlayLeg dl = new PlayLeg();
        DartProbs playerA = new DartProbs(BaseProbSet.UNITTESTSET1, 1.0, 0);
        DartProbs playerB = new DartProbs(BaseProbSet.UNITTESTSET1, 1.0, 0);
        dl.setPlayers(playerA, playerB);
        LegState ls = new LegState(false);
        //
        // A throws first
        //
        double nwinsA = 0;
        ls.startNewLeg(TeamId.A);
        for (int i = 0; i < nTrials; i++) {
            LegResult result = dl.play(ls);
            if (result.legWinner == TeamId.A)
                nwinsA += 1;
        }
        double probAWins = nwinsA / nTrials;
        assertEquals(.618, probAWins, stDevsFromMean * stDev); // don't know for certain this is the correct answer

        //
        // B throws first
        //
        nwinsA = 0;
        ls.startNewLeg(TeamId.B);

        for (int i = 0; i < nTrials; i++) {
            LegResult result = dl.play(ls);
            if (result.legWinner == TeamId.A)
                nwinsA += 1;
        }
        probAWins = nwinsA / nTrials;
        assertEquals(.383, probAWins, .02); // don't know for certain this is the correct answer
        ls.startNewLeg(TeamId.A);
        playerA = new DartProbs(BaseProbSet.UNITTESTSET1, 1.05, 0);
        playerB = new DartProbs(BaseProbSet.UNITTESTSET1, 0.95, 0);
        dl.setPlayers(playerA, playerB);
        nwinsA = 0;
        for (int i = 0; i < nTrials; i++) {
            LegResult result = dl.play(ls);
            if (result.legWinner == TeamId.A)
                nwinsA += 1;
        }
        probAWins = nwinsA / nTrials;
        assertEquals(.695, probAWins, stDevsFromMean * stDev); //// don't know for certain this is the correct answer
    }

    @Test
    public void test2() {
        PlayLeg dl = new PlayLeg();
        DartProbs playerA = new DartProbs(BaseProbSet.LIVE, 1.0, 0);
        DartProbs playerB = new DartProbs(BaseProbSet.LIVE, 1.0, 0);
        dl.setPlayers(playerA, playerB);
        LegState ls = new LegState(false);
        ls.startNewLeg(TeamId.A);
        // executeTest2(dl, ls, 9);
        DartTarget dt = new DartTarget(1, 2);
        ls.updateScore(dt);
        ls.updateScore(dt);
        ls.updateScore(dt);
        ls.updateScore(dt);
        ls.updateScore(dt);
        ls.updateScore(dt);
        // both players now scored total of 6 with three darts
        executeTest2(dl, ls, 12);
    }

    void executeTest2(PlayLeg dl, LegState ls, int nDartsExpected) {
        int minDartsA = 100;
        int minDartsB = 100;
        for (int i = 0; i < nTrials; i++) {
            LegResult result = dl.play(ls);
            if (result.legWinner == TeamId.A && result.NDartsThrownA < minDartsA)
                minDartsA = result.NDartsThrownA;
            if (result.legWinner == TeamId.B && result.NDartsThrownB < minDartsB)
                minDartsB = result.NDartsThrownB;
        }
        assertTrue(minDartsA >= nDartsExpected && minDartsB >= nDartsExpected);

    }


}
