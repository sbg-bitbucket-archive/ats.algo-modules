package ats.algo.sport.snooker;

import static org.junit.Assert.*;
import org.junit.Test;

import ats.algo.core.baseclasses.MatchState;
import ats.algo.genericsupportfunctions.JsonSerializer;
import ats.algo.genericsupportfunctions.PairOfIntegers;
import ats.core.util.json.JsonUtil;

public class SnookerJsonSerialisationTest {

    @Test
    public void testMatchFormat() {
        SnookerMatchFormat matchFormat1 = new SnookerMatchFormat();
        String json = JsonUtil.marshalJson(matchFormat1, true);
        SnookerMatchFormat matchFormat2 = JsonUtil.unmarshalJson(json, SnookerMatchFormat.class);
        assertEquals(matchFormat1, matchFormat2);
    }

    @Test
    public void testMatchParams() {
        SnookerMatchParams snookerMatchParams = new SnookerMatchParams();
        String json = JsonUtil.marshalJson(snookerMatchParams, true);
        SnookerMatchParams snookerMatchParams2 = JsonUtil.unmarshalJson(json, SnookerMatchParams.class);
        assertEquals(snookerMatchParams, snookerMatchParams2);

    }

    @Test
    public void testMatchIncident() {
        SnookerMatchIncident snookerMatchIncident = new SnookerMatchIncident();
        String json = JsonUtil.marshalJson(snookerMatchIncident, true);
        SnookerMatchIncident snookerMatchIncident2 = JsonUtil.unmarshalJson(json, SnookerMatchIncident.class);
        assertEquals(snookerMatchIncident, snookerMatchIncident2);

    }

    @Test
    public void testMatchFormatOptions() {
        SnookerMatchFormatOptions o1 = new SnookerMatchFormatOptions();
        String json = JsonUtil.marshalJson(o1, true);
        System.out.println(json);
        assertTrue(json.contains("147"));
    }

    @Test
    public void testMatchState() {
        SnookerMatchFormat matchFormat = new SnookerMatchFormat();
        SnookerMatchState snookerMatchState = new SnookerMatchState(matchFormat);
        snookerMatchState.setFramesA(3);
        snookerMatchState.setFramesB(1);
        String json = JsonUtil.marshalJson(snookerMatchState, true);
        SnookerMatchState snookerMatchState2 = JsonUtil.unmarshalJson(json, SnookerMatchState.class);
        assertEquals(snookerMatchState, snookerMatchState2);

    }

    @Test
    public void testSimpleMatchState() {
        SnookerMatchFormat matchFormat = new SnookerMatchFormat();
        SnookerMatchState matchState = new SnookerMatchState(matchFormat);
        PairOfIntegers[] frameScoreInMatchN = {PairOfIntegers.generateFromString("1-21"),
                PairOfIntegers.generateFromString("21-5"), PairOfIntegers.generateFromString("15-21")};;
        matchState.setFrameScoreInMatchN(frameScoreInMatchN);
        MatchState sms = matchState.generateSimpleMatchState();
        String json = JsonSerializer.serialize(sms, true);
        MatchState sms2 = JsonSerializer.deserialize(json, SnookerSimpleMatchState.class);
        System.out.println("\nSTATE:\n" + sms2);
        assertEquals(sms, sms2);
    }

}
