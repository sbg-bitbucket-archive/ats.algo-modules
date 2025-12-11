package ats.algo.sport.volleyball;

import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.JsonSerializer;
import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ats.core.util.json.JsonUtil;

public class SerialisationTest {

    @Test
    public void testMatchParams() {
        MethodName.log();
        VolleyballMatchParams volleyballMatchParams = new VolleyballMatchParams();
        String json = JsonUtil.marshalJson(volleyballMatchParams, true);
        VolleyballMatchParams vpMatchParams = JsonUtil.unmarshalJson(json, VolleyballMatchParams.class);
        assertEquals(volleyballMatchParams, vpMatchParams);
    }


    @Test
    public void testMatchState() {
        MethodName.log();
        VolleyballMatchFormat matchFormat = new VolleyballMatchFormat();
        VolleyballMatchState volleyballMatchState = new VolleyballMatchState(matchFormat);
        String json = JsonUtil.marshalJson(volleyballMatchState, true);
        VolleyballMatchState voMatchState = JsonUtil.unmarshalJson(json, VolleyballMatchState.class);
        assertEquals(volleyballMatchState, voMatchState);

    }

    @Test
    public void testSimpleMatchState() {
        MethodName.log();
        VolleyballMatchFormat matchFormat = new VolleyballMatchFormat();
        VolleyballMatchState matchState = new VolleyballMatchState(matchFormat);
        String json = JsonSerializer.serialize(matchState.generateSimpleMatchState(), true);
        matchState.setScore(1, 0, 1, 2, TeamId.A);
        matchState.setGameScoreInSetN(0, 6, 2);

        MatchState sms = matchState.generateSimpleMatchState();
        json = JsonSerializer.serialize(sms, true);
        MatchState sms2 = JsonSerializer.deserialize(json, VolleyballSimpleMatchState.class);
        assertEquals(sms, sms2);
    }
}
