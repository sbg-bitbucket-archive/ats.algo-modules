package ats.algo.sport.darts;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.core.common.TeamId;
import ats.algo.sport.darts.DartTarget;
import ats.algo.sport.darts.LegState;
import ats.algo.sport.darts.LegThrowResult;

public class LegStateTest {

    @Test
    public void test1() {
        MethodName.log();
        LegState ls = new LegState(true);
        ls.startNewLeg(TeamId.A);
        DartTarget dt = new DartTarget(1, 20);
        ls.updateScore(dt);
        assertEquals(501, ls.playerA.getPoints());
        ls.updateScore(dt);
        ls.updateScore(dt);
        assertEquals(501, ls.playerA.getPoints());
        assertEquals(TeamId.B, ls.playerAtOche);
        ls.updateScore(dt);
        dt = new DartTarget(2, 20);
        ls.updateScore(dt);
        assertEquals(501, ls.playerA.getPoints());
        assertEquals(461, ls.playerB.getPoints());
        assertEquals(2, ls.getThreeDartSet().noDartsThrown);
    }

    @Test
    public void test2() {
        MethodName.log();
        // test going bust
        LegState ls = new LegState(true);
        ls.setPartLegScore(10, 40, 7, 10, 0, 0, TeamId.A);
        ls.playerA.setPoints(10);
        ls.playerB.setPoints(40);
        assertEquals(10, ls.playerA.getPoints());
        assertEquals(40, ls.playerB.getPoints());
        DartTarget dt = new DartTarget(1, 20);
        LegThrowResult result = ls.updateScore(dt);
        assertEquals(LegThrowResult.NEXTDARTFROMOTHERPLAYER, result);
        assertEquals(10, ls.playerA.getPoints());
        assertEquals(40, ls.playerB.getPoints());
        assertEquals(TeamId.B, ls.playerAtOche);
        //
        // test copy
        //
        LegState cc = ls.copy();
        assertEquals(10, cc.playerA.getPoints());
        assertEquals(40, cc.playerB.getPoints());
        assertEquals(TeamId.B, cc.playerAtOche);

    }

}
