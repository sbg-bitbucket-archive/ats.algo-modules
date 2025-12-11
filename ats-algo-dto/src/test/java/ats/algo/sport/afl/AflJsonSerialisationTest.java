package ats.algo.sport.afl;


import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.core.baseclasses.MatchState;
import ats.algo.genericsupportfunctions.JsonSerializer;
import ats.algo.sport.afl.AflMatchFormatOptions;
import ats.algo.sport.afl.AflMatchFormat;
import ats.algo.sport.afl.AflMatchState;
import ats.core.util.json.JsonUtil;

public class AflJsonSerialisationTest {

    @Test
    public void testMatchFormat() {
        AflMatchFormat matchFormat1 = new AflMatchFormat();
        String json = JsonUtil.marshalJson(matchFormat1, true);
        AflMatchFormat matchFormat2 = JsonUtil.unmarshalJson(json, AflMatchFormat.class);
        assertEquals(matchFormat1, matchFormat2);
    }


    @Test
    public void testMatchParams() {
        AflMatchParams aflMatchParams = new AflMatchParams();
        String json = JsonUtil.marshalJson(aflMatchParams, true);
        System.out.println(json);
        AflMatchParams vpMatchParams = JsonUtil.unmarshalJson(json, AflMatchParams.class);
        System.out.println(vpMatchParams);
        assertEquals(aflMatchParams, vpMatchParams);
    }

    @Test
    public void testMatchIncident() {
        AflMatchIncident aflMatchIncident = new AflMatchIncident();
        String json = JsonUtil.marshalJson(aflMatchIncident, true);
        AflMatchIncident AflMatchIncident2 = JsonUtil.unmarshalJson(json, AflMatchIncident.class);
        assertEquals(aflMatchIncident, AflMatchIncident2);
    }

    @Test
    public void testMatchFormatOptions() {
        AflMatchFormatOptions o1 = new AflMatchFormatOptions();
        String json = JsonUtil.marshalJson(o1, true);
        System.out.println(json);
        assertTrue(json.contains("normalTimeMinutes"));
    }

    @Test
    public void testMatchState() {
        AflMatchFormat matchFormat = new AflMatchFormat();
        AflMatchState AflMatchState = new AflMatchState(matchFormat);
        String json = JsonUtil.marshalJson(AflMatchState, true);
        System.out.println(json);
        AflMatchState voMatchState = JsonUtil.unmarshalJson(json, AflMatchState.class);
        System.out.println(voMatchState);
        assertEquals(AflMatchState, voMatchState);

    }

    @Test
    public void testSimpleMatchState() {
        AflMatchFormat matchFormat = new AflMatchFormat();
        AflMatchState matchState = new AflMatchState(matchFormat);
        MatchState sms = matchState.generateSimpleMatchState();
        String json = JsonSerializer.serialize(sms, true);
        System.out.println("\nSTATE:\n" + json);
        System.out.println(json);
        MatchState sms2 = JsonSerializer.deserialize(json, AflSimpleMatchState.class);
        assertEquals(sms, sms2);


    }
}
