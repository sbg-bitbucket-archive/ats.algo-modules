package ats.algo.sport.darts;

import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.sport.darts.DartMatchFormat;
import ats.algo.sport.darts.DartMatchParams;
import ats.algo.sport.darts.DartMatchState;

public class DartMatchMapTests {

    @Test
    public void test() {
        try {
            DartMatchFormat matchFormat = new DartMatchFormat();
            matchFormat.setDoubleReqdToStart(false);
            matchFormat.setnLegsPerSet(3);
            matchFormat.setnLegsOrSetsInMatch(7);
            System.out.print(matchFormat.toString());
            DartMatchState matchState = new DartMatchState(matchFormat);
            System.out.print(matchState.toString());
            DartMatchParams matchParams = new DartMatchParams(matchFormat);
            System.out.print(matchParams.toString());
        } catch (Exception e) {
            fail();
        }
    }
}
