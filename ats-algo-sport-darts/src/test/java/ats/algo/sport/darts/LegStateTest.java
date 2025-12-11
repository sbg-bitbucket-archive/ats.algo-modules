package ats.algo.sport.darts;

import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.core.common.TeamId;
import ats.algo.sport.darts.DartTarget;
import ats.algo.sport.darts.LegState;
import ats.algo.sport.darts.LegThrowResult;

public class LegStateTest {

    @Test
    public void test1() {
        LegState ls = new LegState(true);
        ls.startNewLeg(TeamId.A);
        DartTarget dt = new DartTarget(1, 20);
        ls.updateScore(dt);
        assertEquals(501, ls.playerA.Points);
        ls.updateScore(dt);
        ls.updateScore(dt);
        assertEquals(501, ls.playerA.Points);
        assertEquals(TeamId.B, ls.playerAtOche);
        ls.updateScore(dt);
        dt = new DartTarget(2, 20);
        ls.updateScore(dt);
        assertEquals(501, ls.playerA.Points);
        assertEquals(461, ls.playerB.Points);
        assertEquals(2, ls.threeDartSet.noDartsThrown);
    }

    @Test
    public void test2() {
        // test going bust
        LegState ls = new LegState(true);
        ls.setPartLegScore(10, 40, 7, 10, 0, 0, TeamId.A);
        ls.playerA.Points = 10;
        ls.playerB.Points = 40;
        assertEquals(10, ls.playerA.Points);
        assertEquals(40, ls.playerB.Points);
        DartTarget dt = new DartTarget(1, 20);
        LegThrowResult result = ls.updateScore(dt);
        assertEquals(LegThrowResult.NEXTDARTFROMOTHERPLAYER, result);
        assertEquals(10, ls.playerA.Points);
        assertEquals(40, ls.playerB.Points);
        assertEquals(TeamId.B, ls.playerAtOche);
        //
        // test copy
        //
        LegState cc = ls.copy();
        assertEquals(10, cc.playerA.Points);
        assertEquals(40, cc.playerB.Points);
        assertEquals(TeamId.B, cc.playerAtOche);

    }

}
