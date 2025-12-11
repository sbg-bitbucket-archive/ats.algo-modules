package ats.algo.sport.snooker;

import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.MethodName;
import ats.algo.sport.snooker.SnookerMatchIncident.SnookerMatchIncidentType;
import ats.core.util.json.JsonUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SnookerJsonSerialisationTest {

    @Test
    public void testMatchFormat() {
        MethodName.log();
        SnookerMatchFormat matchFormat1 = new SnookerMatchFormat();
        String json = JsonUtil.marshalJson(matchFormat1, true);
        SnookerMatchFormat matchFormat2 = JsonUtil.unmarshalJson(json, SnookerMatchFormat.class);
        assertEquals(matchFormat1, matchFormat2);
    }

    @Test
    public void testMatchParams() {
        MethodName.log();
        SnookerMatchParams snookerMatchParams = new SnookerMatchParams();
        String json = JsonUtil.marshalJson(snookerMatchParams, true);
        SnookerMatchParams snookerMatchParams2 = JsonUtil.unmarshalJson(json, SnookerMatchParams.class);
        assertEquals(snookerMatchParams, snookerMatchParams2);

    }

    @Test
    public void testMatchIncident() {
        MethodName.log();
        SnookerMatchIncident snookerMatchIncident = new SnookerMatchIncident();
        snookerMatchIncident.set(SnookerMatchIncidentType.BLACKPOT, TeamId.A);
        String json = JsonUtil.marshalJson(snookerMatchIncident, true);
        // System.out.println(json);
        SnookerMatchIncident snookerMatchIncident2 = JsonUtil.unmarshalJson(json, SnookerMatchIncident.class);
        assertEquals(snookerMatchIncident, snookerMatchIncident2);

    }

    @Test
    public void testMatchFormatOptions() {
        MethodName.log();
        SnookerMatchFormatOptions o1 = new SnookerMatchFormatOptions();
        String json = JsonUtil.marshalJson(o1, true);
        // System.out.println(json);
        assertTrue(json.contains("147"));
    }

    @Test
    public void testMatchState() {
        MethodName.log();
        SnookerMatchFormat matchFormat = new SnookerMatchFormat();
        SnookerMatchState snookerMatchState = new SnookerMatchState(matchFormat);
        snookerMatchState.setFramesA(3);
        snookerMatchState.setFramesB(1);
        String json = JsonUtil.marshalJson(snookerMatchState, true);
        SnookerMatchState snookerMatchState2 = JsonUtil.unmarshalJson(json, SnookerMatchState.class);
        assertEquals(snookerMatchState, snookerMatchState2);

    }
}
