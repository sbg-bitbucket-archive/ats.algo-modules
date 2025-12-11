package ats.algo.sport.cricket;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.JsonSerializer;
import ats.algo.sport.cricket.CricketMatchIncident.CricketMatchIncidentType;
import ats.core.util.json.JsonUtil;

public class CricketJsonSerialisationTest {

    @Test
    public void testMatchFormat() {
        CricketMatchFormat matchFormat1 = new CricketMatchFormat();
        String json = JsonUtil.marshalJson(matchFormat1, true);
        CricketMatchFormat matchFormat2 = JsonUtil.unmarshalJson(json, CricketMatchFormat.class);
        assertEquals(matchFormat1, matchFormat2);
    }

    @Test
    public void testMatchParams() {
        CricketMatchParams CricketMatchParams = new CricketMatchParams();
        String json = JsonUtil.marshalJson(CricketMatchParams, true);
        CricketMatchParams vpMatchParams = JsonUtil.unmarshalJson(json, CricketMatchParams.class);
        assertEquals(CricketMatchParams, vpMatchParams);
    }

    @Test
    public void testMatchIncident() {
        CricketMatchIncident matchIncident = new CricketMatchIncident();
        matchIncident.set(CricketMatchIncidentType.RUN1);
        matchIncident.setIncidentId("C:M1:001");
        matchIncident.setTeamId(TeamId.A);
        matchIncident.setElapsedTime(600);
        matchIncident.setSourceSystem("CRICKET_WORLD");
        matchIncident.setExternalEventId("XX123");

        String json = JsonUtil.marshalJson(matchIncident, true);
        CricketMatchIncident CricketMatchIncident2 = JsonUtil.unmarshalJson(json, CricketMatchIncident.class);
        assertEquals(matchIncident, CricketMatchIncident2);
    }

    @Test
    public void testMatchFormatOptions() {
        CricketMatchFormatOptions o1 = new CricketMatchFormatOptions();
        String json = JsonUtil.marshalJson(o1, true);
        System.out.println(json);
        assertTrue(json.contains("Twenty20"));
    }

    @Test
    public void testMatchState() {
        CricketMatchFormat matchFormat = new CricketMatchFormat();
        CricketMatchState CricketMatchState = new CricketMatchState(matchFormat);
        String json = JsonUtil.marshalJson(CricketMatchState, true);
        CricketMatchState voMatchState = JsonUtil.unmarshalJson(json, CricketMatchState.class);
        assertEquals(CricketMatchState, voMatchState);

    }

    @Test
    public void testSimpleMatchState() {
        CricketMatchFormat matchFormat = new CricketMatchFormat();
        CricketMatchState matchState = new CricketMatchState(matchFormat);
        MatchState sms = matchState.generateSimpleMatchState();
        String json = JsonSerializer.serialize(sms, true);
        MatchState sms2 = JsonSerializer.deserialize(json, CricketSimpleMatchState.class);
        System.out.println("\nSTATE:\n" + sms2);
        assertEquals(sms, sms2);
    }
}
