package ats.algo.sport.testcricket;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ats.algo.core.baseclasses.MatchState;
import ats.algo.genericsupportfunctions.JsonSerializer;
import ats.core.util.json.JsonUtil;

public class TestCricketJsonSerialisationTest {
    @Test
    public void testMatchFormat() {

        TestCricketMatchFormat matchFormat1 = new TestCricketMatchFormat();
        String json = JsonUtil.marshalJson(matchFormat1, true);

        TestCricketMatchFormat matchFormat2 = JsonUtil.unmarshalJson(json, TestCricketMatchFormat.class);
        System.out.println(matchFormat2 + "--" + json);
        assertEquals(matchFormat1, matchFormat2);
    }

    @Test
    public void testMatchParams() {
        TestCricketMatchParams testCricketMatchParams = new TestCricketMatchParams();
        String json = JsonUtil.marshalJson(testCricketMatchParams, true);
        TestCricketMatchParams vpMatchParams = JsonUtil.unmarshalJson(json, TestCricketMatchParams.class);
        assertEquals(testCricketMatchParams, vpMatchParams);
    }

    @Test
    public void testMatchIncident() {

        TestCricketMatchIncident matchIncident = new TestCricketMatchIncident();
        String json = JsonUtil.marshalJson(matchIncident, true);
        TestCricketMatchIncident testCricketMatchIncident2 =
                        JsonUtil.unmarshalJson(json, TestCricketMatchIncident.class);
        assertEquals(matchIncident, testCricketMatchIncident2);

    }

    @Test
    public void testMatchFormatOptions() {
        TestCricketMatchFormatOptions o1 = new TestCricketMatchFormatOptions();
        String json = JsonUtil.marshalJson(o1, true);
        System.out.println(json);
        assertTrue(json.contains("11"));
    }

    @Test
    public void testMatchState() {
        TestCricketMatchFormat matchFormat = new TestCricketMatchFormat();
        TestCricketMatchState testCricketMatchState = new TestCricketMatchState(matchFormat);
        String json = JsonUtil.marshalJson(testCricketMatchState, true);
        TestCricketMatchState voMatchState = JsonUtil.unmarshalJson(json, TestCricketMatchState.class);
        assertEquals(testCricketMatchState, voMatchState);
    }

    @Test
    public void testSimpleMatchState() {
        TestCricketMatchFormat matchFormat = new TestCricketMatchFormat();
        TestCricketMatchState matchState = new TestCricketMatchState(matchFormat);
        MatchState sms = matchState.generateSimpleMatchState();
        String json = JsonSerializer.serialize(sms, true);
        MatchState sms2 = JsonSerializer.deserialize(json, TestCricketSimpleMatchState.class);
        System.out.println("\nSTATE:\n" + sms2);
        assertEquals(sms, sms2);
    }
}
