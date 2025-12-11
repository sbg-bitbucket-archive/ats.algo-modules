package ats.algo.sport.darts;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.sport.darts.DartMatchFormat;

public class DartMatchMapTests {

    @Test
    public void test() {
        MethodName.log();
        try {
            DartMatchFormat matchFormat = new DartMatchFormat();
            matchFormat.setDoubleReqdToStart(false);
            matchFormat.setnLegsPerSet(3);
            matchFormat.setnLegsOrSetsInMatch(7);
            // System.out.print(matchFormat.toString());
            // DartMatchState matchState = new DartMatchState(matchFormat);
            // // System.out.print(matchState.toString());
            // DartMatchParams matchParams = new DartMatchParams(matchFormat);
            // // System.out.print(matchParams.toString());
        } catch (Exception e) {
            fail();
        }
    }
}
