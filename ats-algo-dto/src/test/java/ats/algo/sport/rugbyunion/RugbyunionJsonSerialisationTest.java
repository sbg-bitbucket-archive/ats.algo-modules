package ats.algo.sport.rugbyunion;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ats.algo.core.baseclasses.MatchState;
import ats.algo.genericsupportfunctions.JsonSerializer;
import ats.core.util.json.JsonUtil;

public class RugbyunionJsonSerialisationTest {

    @Test
    public void testMatchFormat() {
        RugbyUnionMatchFormat matchFormat1 = new RugbyUnionMatchFormat();
        String json = JsonUtil.marshalJson(matchFormat1, true);
        RugbyUnionMatchFormat matchFormat2 = JsonUtil.unmarshalJson(json, RugbyUnionMatchFormat.class);
        assertEquals(matchFormat1, matchFormat2);
    }


    @Test
    public void testMatchParams() {
        RugbyUnionMatchParams footballMatchParams = new RugbyUnionMatchParams();
        String json = JsonUtil.marshalJson(footballMatchParams, true);
        System.out.println(json);
        RugbyUnionMatchParams vpMatchParams = JsonUtil.unmarshalJson(json, RugbyUnionMatchParams.class);
        System.out.println(vpMatchParams);
        assertEquals(footballMatchParams, vpMatchParams);
    }

    @Test
    public void testMatchIncident() {
        RugbyUnionMatchIncident footballMatchIncident = new RugbyUnionMatchIncident();
        String json = JsonUtil.marshalJson(footballMatchIncident, true);
        RugbyUnionMatchIncident RugbyUnionMatchIncident2 = JsonUtil.unmarshalJson(json, RugbyUnionMatchIncident.class);
        assertEquals(footballMatchIncident, RugbyUnionMatchIncident2);
    }

    @Test
    public void testMatchState() {
        RugbyUnionMatchFormat matchFormat = new RugbyUnionMatchFormat();
        RugbyUnionMatchState rugbyunionMatchState = new RugbyUnionMatchState(matchFormat);
        String json = JsonUtil.marshalJson(rugbyunionMatchState, true);
        System.out.println(json);
        RugbyUnionMatchState voMatchState = JsonUtil.unmarshalJson(json, RugbyUnionMatchState.class);
        System.out.println(voMatchState);
        assertEquals(rugbyunionMatchState, voMatchState);

    }

    @Test
    public void testSimpleMatchState() {
        RugbyUnionMatchFormat matchFormat = new RugbyUnionMatchFormat();
        RugbyUnionMatchState matchState = new RugbyUnionMatchState(matchFormat);
        MatchState sms = matchState.generateSimpleMatchState();
        String json = JsonSerializer.serialize(sms, true);
        System.out.println("\nSTATE:\n" + json);
        MatchState sms2 = JsonSerializer.deserialize(json, RugbyUnionSimpleMatchState.class);
        assertEquals(sms, sms2);
    }
}
