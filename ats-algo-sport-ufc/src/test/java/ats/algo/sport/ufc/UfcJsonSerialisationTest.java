package ats.algo.sport.ufc;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ats.core.util.json.JsonUtil;

public class UfcJsonSerialisationTest {

    @Test
    public void testMatchFormat() {
        MethodName.log();
        UfcMatchFormat matchFormat1 = new UfcMatchFormat();
        String json = JsonUtil.marshalJson(matchFormat1, true);
        UfcMatchFormat matchFormat2 = JsonUtil.unmarshalJson(json, UfcMatchFormat.class);
        assertEquals(matchFormat1, matchFormat2);
    }

    @Test
    public void testMatchParams() {
        MethodName.log();
        UfcMatchParams ufcMatchParams = new UfcMatchParams();
        String json = JsonUtil.marshalJson(ufcMatchParams, true);
        UfcMatchParams vpMatchParams = JsonUtil.unmarshalJson(json, UfcMatchParams.class);
        assertEquals(ufcMatchParams, vpMatchParams);
    }

    @Test
    public void testMatchIncident() {
        MethodName.log();
        UfcMatchIncident ufcMatchIncident = new UfcMatchIncident();
        String json = JsonUtil.marshalJson(ufcMatchIncident, true);
        UfcMatchIncident UfcMatchIncident2 = JsonUtil.unmarshalJson(json, UfcMatchIncident.class);
        assertEquals(ufcMatchIncident, UfcMatchIncident2);
    }


    @Test
    public void testMatchFormatOptions() {
        MethodName.log();
        UfcMatchFormatOptions o1 = new UfcMatchFormatOptions();
        String json = JsonUtil.marshalJson(o1, true);
        assertTrue(json.contains("5"));
    }

    @Test
    public void testMatchState() {
        MethodName.log();
        UfcMatchFormat matchFormat = new UfcMatchFormat();
        UfcMatchState ufcMatchState = new UfcMatchState(matchFormat);
        String json = JsonUtil.marshalJson(ufcMatchState, true);
        UfcMatchState voMatchState = JsonUtil.unmarshalJson(json, UfcMatchState.class);
        assertEquals(ufcMatchState, voMatchState);

    }
}
