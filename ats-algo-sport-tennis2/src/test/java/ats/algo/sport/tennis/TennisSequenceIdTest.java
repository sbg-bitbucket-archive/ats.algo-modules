package ats.algo.sport.tennis;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.*;

import org.junit.Test;

public class TennisSequenceIdTest {

    @Test
    public void test() {
        MethodName.log();
        TennisMatchState matchState = new TennisMatchState(new TennisMatchFormat());
        assertEquals("S1.1.2", matchState.getSequenceId("S3.4.2", 1));
        assertEquals("S1.3", matchState.getSequenceId("S3.4", 2));
        assertEquals("M", matchState.getSequenceId("M", 2));
    }

}
