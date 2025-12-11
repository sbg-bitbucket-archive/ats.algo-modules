package ats.algo.sport.tennis;

import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.PairOfIntegers;

public class ConvertSequenceIdToAlgoMgrStdTest {

    @Test
    public void test() {
        TennisMatchState matchState = new TennisMatchState(new TennisMatchFormat());
        String newSequenceId = matchState.convertSequenceIdToAlgoMgrStd("S1.1");
        assertEquals(null, newSequenceId);
        newSequenceId = matchState.convertSequenceIdToAlgoMgrStd("G2");
        assertEquals("S1.2", newSequenceId);
        newSequenceId = matchState.convertSequenceIdToAlgoMgrStd("G15");
        assertEquals("S1.15", newSequenceId);

        matchState.setScore(1, 0, 2, 1, TeamId.A, 1, 2);
        PairOfIntegers setScore = matchState.getGameScoreInSetN(0);
        setScore.A = 6;
        setScore.B = 4;
        newSequenceId = matchState.convertSequenceIdToAlgoMgrStd("G15");
        assertEquals("S2.5", newSequenceId);
        setScore.A = 7;
        setScore.B = 6;
        newSequenceId = matchState.convertSequenceIdToAlgoMgrStd("G15");
        assertEquals("S2.2", newSequenceId);


    }

}
