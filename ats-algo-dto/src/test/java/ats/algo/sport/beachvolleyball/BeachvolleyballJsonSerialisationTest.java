package ats.algo.sport.beachvolleyball;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.JsonSerializer;
import ats.core.util.json.JsonUtil;

public class BeachvolleyballJsonSerialisationTest {
    @Test
    public void testMatchFormat() {
        BeachVolleyballMatchFormat matchFormat1 = new BeachVolleyballMatchFormat();
        String json = JsonUtil.marshalJson(matchFormat1, true);
        BeachVolleyballMatchFormat matchFormat2 = JsonUtil.unmarshalJson(json, BeachVolleyballMatchFormat.class);
        assertEquals(matchFormat1, matchFormat2);
    }

    @Test
    public void testMatchParams() {
        BeachVolleyballMatchParams BeachVolleyballMatchParams = new BeachVolleyballMatchParams();
        String json = JsonUtil.marshalJson(BeachVolleyballMatchParams, true);
        System.out.println(json);
        BeachVolleyballMatchParams vpMatchParams = JsonUtil.unmarshalJson(json, BeachVolleyballMatchParams.class);
        assertEquals(BeachVolleyballMatchParams, vpMatchParams);
    }

    @Test
    public void testMatchIncident() {
        BeachVolleyballMatchIncident BeachVolleyballMatchIncident = new BeachVolleyballMatchIncident();
        String json = JsonUtil.marshalJson(BeachVolleyballMatchIncident, true);
        BeachVolleyballMatchIncident BeachVolleyballMatchIncident2 =
                        JsonUtil.unmarshalJson(json, BeachVolleyballMatchIncident.class);
        assertEquals(BeachVolleyballMatchIncident, BeachVolleyballMatchIncident2);
    }

    @Test
    public void testMatchFormatOptions() {
        BeachvolleyballMatchFormatOptions o1 = new BeachvolleyballMatchFormatOptions();
        String json = JsonUtil.marshalJson(o1, true);
        System.out.println(json);
        assertTrue(json.contains("3"));
    }

    @Test
    public void testMatchState() {
        BeachVolleyballMatchFormat matchFormat = new BeachVolleyballMatchFormat();
        BeachVolleyballMatchState BeachVolleyballMatchState = new BeachVolleyballMatchState(matchFormat);
        String json = JsonUtil.marshalJson(BeachVolleyballMatchState, true);
        BeachVolleyballMatchState voMatchState = JsonUtil.unmarshalJson(json, BeachVolleyballMatchState.class);
        assertEquals(BeachVolleyballMatchState, voMatchState);

    }

    @Test
    public void testSimpleMatchState() {
        BeachVolleyballMatchFormat matchFormat = new BeachVolleyballMatchFormat();
        BeachVolleyballMatchState matchState = new BeachVolleyballMatchState(matchFormat);
        String json = JsonSerializer.serialize(matchState.generateSimpleMatchState(), true);
        System.out.println(json);
        matchState.setScore(1, 0, 1, 2, TeamId.A);
        matchState.setGameScoreInSetN(0, 6, 2);
        System.out.println((matchState).getGameScoreInSetN(0).A + "-" + (matchState).getGameScoreInSetN(0).B);

        MatchState sms = matchState.generateSimpleMatchState();
        json = JsonSerializer.serialize(sms, true);
        System.out.println(json);
        MatchState sms2 = JsonSerializer.deserialize(json, BeachVolleyballSimpleMatchState.class);
        assertEquals(sms, sms2);
    }
}
