package ats.algo.sport.volleyball;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.JsonSerializer;
import ats.core.util.json.JsonUtil;

public class VolleyballJsonSerialisationTest {
    @Test
    public void testMatchFormat() {
        VolleyballMatchFormat matchFormat1 = new VolleyballMatchFormat();
        String json = JsonUtil.marshalJson(matchFormat1, true);
        VolleyballMatchFormat matchFormat2 = JsonUtil.unmarshalJson(json, VolleyballMatchFormat.class);
        assertEquals(matchFormat1, matchFormat2);
    }

    @Test
    public void testMatchFormatOptions() {
        VolleyballMatchFormatOptions o1 = new VolleyballMatchFormatOptions();
        String json = JsonUtil.marshalJson(o1, true);
        System.out.println(json);
        assertTrue(json.contains("25"));
    }

    @Test
    public void testMatchParams() {
        VolleyballMatchParams volleyballMatchParams = new VolleyballMatchParams();
        String json = JsonUtil.marshalJson(volleyballMatchParams, true);
        VolleyballMatchParams vpMatchParams = JsonUtil.unmarshalJson(json, VolleyballMatchParams.class);
        assertEquals(volleyballMatchParams, vpMatchParams);
    }

    @Test
    public void testMatchIncident() {
        VolleyballMatchIncident volleyballMatchIncident = new VolleyballMatchIncident();
        String json = JsonUtil.marshalJson(volleyballMatchIncident, true);
        VolleyballMatchIncident volleyballMatchIncident2 = JsonUtil.unmarshalJson(json, VolleyballMatchIncident.class);
        assertEquals(volleyballMatchIncident, volleyballMatchIncident2);
    }

    @Test
    public void testMatchState() {
        VolleyballMatchFormat matchFormat = new VolleyballMatchFormat();
        VolleyballMatchState volleyballMatchState = new VolleyballMatchState(matchFormat);
        String json = JsonUtil.marshalJson(volleyballMatchState, true);
        VolleyballMatchState voMatchState = JsonUtil.unmarshalJson(json, VolleyballMatchState.class);
        assertEquals(volleyballMatchState, voMatchState);

    }

    @Test
    public void testSimpleMatchState() {
        VolleyballMatchFormat matchFormat = new VolleyballMatchFormat();
        VolleyballMatchState matchState = new VolleyballMatchState(matchFormat);
        String json = JsonSerializer.serialize(matchState.generateSimpleMatchState(), true);
        System.out.println(json);
        matchState.setScore(1, 0, 1, 2, TeamId.A);
        matchState.setGameScoreInSetN(0, 6, 2);
        System.out.println((matchState).getGameScoreInSetN(0).A + "-" + (matchState).getGameScoreInSetN(0).B);

        MatchState sms = matchState.generateSimpleMatchState();
        json = JsonSerializer.serialize(sms, true);
        System.out.println(json);
        MatchState sms2 = JsonSerializer.deserialize(json, VolleyballSimpleMatchState.class);
        assertEquals(sms, sms2);
    }
}
