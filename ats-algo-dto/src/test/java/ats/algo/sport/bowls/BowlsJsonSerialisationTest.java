package ats.algo.sport.bowls;

import static org.junit.Assert.*;
import org.junit.Test;

import ats.algo.sport.bowls.BowlsMatchFormat;
import ats.algo.sport.bowls.BowlsMatchIncident;
import ats.algo.sport.bowls.BowlsMatchParams;
import ats.algo.sport.bowls.BowlsMatchState;
import ats.core.util.json.JsonUtil;

public class BowlsJsonSerialisationTest {

    @Test
    public void testMatchFormat() {
        BowlsMatchFormat matchFormat1 = new BowlsMatchFormat();
        String json = JsonUtil.marshalJson(matchFormat1, true);
        BowlsMatchFormat matchFormat2 = JsonUtil.unmarshalJson(json, BowlsMatchFormat.class);
        assertEquals(matchFormat1, matchFormat2);
    }

    @Test
    public void testMatchParams() {
        BowlsMatchParams bowlsMatchParams = new BowlsMatchParams();
        String json = JsonUtil.marshalJson(bowlsMatchParams, true);
        BowlsMatchParams vpMatchParams = JsonUtil.unmarshalJson(json, BowlsMatchParams.class);
        assertEquals(bowlsMatchParams, vpMatchParams);
    }

    @Test
    public void testMatchIncident() {
        BowlsMatchIncident bowlsMatchIncident = new BowlsMatchIncident();
        String json = JsonUtil.marshalJson(bowlsMatchIncident, true);
        BowlsMatchIncident bowlsMatchIncident2 = JsonUtil.unmarshalJson(json, BowlsMatchIncident.class);
        assertEquals(bowlsMatchIncident, bowlsMatchIncident2);
    }

    @Test
    public void testMatchFormatOptions() {
        BowlsMatchFormatOptions o1 = new BowlsMatchFormatOptions();
        String json = JsonUtil.marshalJson(o1, true);
        System.out.println(json);
        assertTrue(json.contains("3"));
    }

    @Test
    public void testMatchState() {
        BowlsMatchFormat matchFormat = new BowlsMatchFormat();
        BowlsMatchState bowlsMatchState = new BowlsMatchState(matchFormat);
        String json = JsonUtil.marshalJson(bowlsMatchState, true);
        BowlsMatchState voMatchState = JsonUtil.unmarshalJson(json, BowlsMatchState.class);
        assertEquals(bowlsMatchState, voMatchState);
    }

    @Test
    public void testSimpleMatchState() {
        BowlsMatchFormat matchFormat = new BowlsMatchFormat();
        BowlsSimpleMatchState bowlsSimpleMatchState =
                        (BowlsSimpleMatchState) (new BowlsMatchState(matchFormat)).generateSimpleMatchState();
        bowlsSimpleMatchState.setCurrentEnd(3);
        bowlsSimpleMatchState.setSetsA(1);
        bowlsSimpleMatchState.setSetsB(2);
        String json = JsonUtil.marshalJson(bowlsSimpleMatchState, true);
        System.out.println(json);
        BowlsSimpleMatchState bowlsSimpleMatchState2 = JsonUtil.unmarshalJson(json, BowlsSimpleMatchState.class);
        assertEquals(bowlsSimpleMatchState, bowlsSimpleMatchState2);
    }
}
