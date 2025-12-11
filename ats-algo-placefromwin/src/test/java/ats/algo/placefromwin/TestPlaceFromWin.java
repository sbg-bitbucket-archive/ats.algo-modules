package ats.algo.placefromwin;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.*;

import org.junit.Test;

public class TestPlaceFromWin {
    @Test
    public void testFinishPosnProbs() {
        MethodName.log();
        double[] probWin = {.6, .3, .07, .03};
        double probFall = 0.1;
        FinishPosnProbs calcFinishPosnProbs = new FinishPosnProbs(probWin, probFall);
        double[][] p;
        p = calcFinishPosnProbs.calc(100000);
        assertEquals(.606, p[0][1], 0.008);
        assertEquals(.249, p[0][2], 0.008);
        assertEquals(.140, p[1][3], 0.008);
    }

    @Test
    public void testPlacePosnProbs1() {
        MethodName.log();
        double[] probWin = {.6, .3, .07, .03};
        double probFall = 0.1;
        PlaceProbs calcPlaceProbs = new PlaceProbs(probWin, probFall, 2);
        double[] p;
        p = calcPlaceProbs.calc();
        assertTrue(p[0] > 0.846 && p[0] < 0.856);
        assertTrue(p[2] > 0.262 && p[2] < 0.272);
    }

    @Test
    public void testPlacePosnProbs2() {
        MethodName.log();
        double[] probWin = {.2042, .1361, .1167, .1021, .0743, .0681, .0628, .0628, .0545, .0481, .0389, .0314};
        double probFall = 0.1;
        PlaceProbs calcPlaceProbs = new PlaceProbs(probWin, probFall, 3);
        double[] p;
        p = calcPlaceProbs.calc();
        assertTrue(p[5] > 0.212 && p[5] < 0.222);
    }
}
