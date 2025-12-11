package ats.algo.sport.testcricket;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ats.algo.core.common.TeamId;
import ats.algo.sport.testcricket.TestCricketMatchIncident.CricketMatchIncidentType;
import ats.core.util.json.JsonUtil;

public class TestCricketJsonSerialisationTest {
    @Test
    public void testMatchFormat() {
        MethodName.log();

        TestCricketMatchFormat matchFormat1 = new TestCricketMatchFormat();
        String json = JsonUtil.marshalJson(matchFormat1, true);

        TestCricketMatchFormat matchFormat2 = JsonUtil.unmarshalJson(json, TestCricketMatchFormat.class);
        // System.out.println(matchFormat2 + "--" + json);
        assertEquals(matchFormat1, matchFormat2);
    }

    @Test
    public void testMatchParams() {
        MethodName.log();
        TestCricketMatchParams testCricketMatchParams = new TestCricketMatchParams();
        String json = JsonUtil.marshalJson(testCricketMatchParams, true);
        TestCricketMatchParams vpMatchParams = JsonUtil.unmarshalJson(json, TestCricketMatchParams.class);
        assertEquals(testCricketMatchParams, vpMatchParams);
    }

    @Test
    public void testMatchIncident() {
        MethodName.log();

        TestCricketMatchIncident matchIncident = new TestCricketMatchIncident();
        matchIncident.set(CricketMatchIncidentType.RUN1, TeamId.A);
        String json = JsonUtil.marshalJson(matchIncident, true);
        // System.out.println(json);
        TestCricketMatchIncident testCricketMatchIncident2 =
                        JsonUtil.unmarshalJson(json, TestCricketMatchIncident.class);
        assertEquals(matchIncident, testCricketMatchIncident2);

    }

    @Test
    public void testMatchFormatOptions() {
        MethodName.log();
        TestCricketMatchFormatOptions o1 = new TestCricketMatchFormatOptions();
        String json = JsonUtil.marshalJson(o1, true);
        // System.out.println(json);
        assertTrue(json.contains("11"));
    }

    @Test
    public void testMatchState() {
        MethodName.log();
        TestCricketMatchFormat matchFormat = new TestCricketMatchFormat();
        TestCricketMatchState testCricketMatchState = new TestCricketMatchState(matchFormat);
        String json = JsonUtil.marshalJson(testCricketMatchState, true);
        TestCricketMatchState voMatchState = JsonUtil.unmarshalJson(json, TestCricketMatchState.class);
        assertEquals(testCricketMatchState, voMatchState);
    }
}
