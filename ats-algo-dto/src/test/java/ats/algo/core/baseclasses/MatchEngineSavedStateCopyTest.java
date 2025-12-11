package ats.algo.core.baseclasses;

import static org.junit.Assert.*;
import org.junit.Test;

import ats.algo.genericsupportfunctions.CopySerializableObject;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchIncident;
import ats.algo.sport.tennis.TennisMatchFormat.FinalSetType;
import ats.core.util.json.JsonUtil;

public class MatchEngineSavedStateCopyTest {

    @Test
    public void test() {
        MatchIncident matchIncident = new TennisMatchIncident();
        matchIncident.setEventId(2574L);
        Object x = CopySerializableObject.copy(matchIncident);
        assertEquals(matchIncident, x);

        MatchFormat matchFormat = new TennisMatchFormat(5, FinalSetType.CHAMPIONSHIP_TIE_BREAK, true, false);
        Object y = CopySerializableObject.copy(matchFormat);
        assertEquals(matchFormat, y);

        ExampleMatchEngineSavedState savedState = new ExampleMatchEngineSavedState();
        savedState.setMatchFormat(matchFormat);
        savedState.setMatchIncident(matchIncident);
        Object z = savedState.copy();
        assertEquals(savedState, z);
    }

    @Test
    public void MatchEngineSavedStateSerialisationTest() {
        MatchEngineSavedState state = new MatchEngineSavedState();
        state.setSavedState("hello world");
        String json = JsonUtil.marshalJson(state, true);
        System.out.println(json);
        MatchEngineSavedState state2 = JsonUtil.unmarshalJson(json, MatchEngineSavedState.class);
        assertEquals(state, state2);
    }
}
