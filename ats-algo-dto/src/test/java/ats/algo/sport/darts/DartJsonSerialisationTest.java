package ats.algo.sport.darts;

import static org.junit.Assert.*;
import java.util.LinkedHashMap;

import org.junit.Test;

import ats.core.util.json.JsonUtil;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.genericsupportfunctions.JsonSerializer;
import ats.algo.sport.darts.DartMatchState;
import ats.algo.sport.darts.DartMatchIncident.DartMatchIncidentType;

public class DartJsonSerialisationTest {

    @Test
    public void testMatchFormat() {
        DartMatchFormat matchFormat1 = new DartMatchFormat();
        String json = JsonUtil.marshalJson(matchFormat1, true);
        DartMatchFormat matchFormat2 = JsonUtil.unmarshalJson(json, DartMatchFormat.class);
        assertEquals(matchFormat1, matchFormat2);
        LinkedHashMap<String, String> asMap = matchFormat2.getAsMap();
        matchFormat1.setFromMap(asMap);
        System.out.println(json);
        assertEquals(matchFormat1, matchFormat2);
    }

    @Test
    public void testMatchParams() {
        DartMatchParams dartMatchParams = new DartMatchParams();
        String json = JsonUtil.marshalJson(dartMatchParams, true);
        System.out.println(json);
        DartMatchParams dartMatchParams2 = JsonUtil.unmarshalJson(json, DartMatchParams.class);
        System.out.println(json);
        assertEquals(dartMatchParams, dartMatchParams2);
    }

    @Test
    public void testMatchFormatOptions() {
        DartMatchFormatOptions o1 = new DartMatchFormatOptions();
        String json = JsonUtil.marshalJson(o1, true);
        System.out.println(json);
        assertTrue(json.contains("LOCAL"));
    }

    @Test
    public void testMatchIncident() {
        DartMatchIncident matchIncident = new DartMatchIncident();
        matchIncident.setIncidentSubType(DartMatchIncidentType.SET_PLAYER_STARTING_MATCH_AT_OCHE);
        String json = JsonUtil.marshalJson(matchIncident, true);
        DartMatchIncident DartMatchIncident2 = JsonUtil.unmarshalJson(json, DartMatchIncident.class);
        System.out.println(json);
        assertEquals(matchIncident, DartMatchIncident2);
    }

    @Test
    public void testMatchState() {
        DartMatchFormat matchFormat = new DartMatchFormat();
        DartMatchState dartMatchState = new DartMatchState(matchFormat);
        String json = JsonUtil.marshalJson(dartMatchState, true);
        DartMatchState voMatchState = JsonUtil.unmarshalJson(json, DartMatchState.class);
        System.out.println(json);
        assertEquals(dartMatchState, voMatchState);
    }

    @Test
    public void testSimpleMatchState() {
        DartMatchFormat matchFormat = new DartMatchFormat();
        DartMatchState matchState = new DartMatchState(matchFormat);
        MatchState sms = matchState.generateSimpleMatchState();
        String json = JsonSerializer.serialize(sms, true);
        System.out.println(json);
        MatchState sms2 = JsonSerializer.deserialize(json, DartSimpleMatchState.class);
        assertEquals(sms, sms2);
    }

}
