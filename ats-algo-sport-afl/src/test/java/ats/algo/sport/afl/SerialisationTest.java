package ats.algo.sport.afl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ats.algo.genericsupportfunctions.MethodName;
import ats.core.util.json.JsonUtil;

public class SerialisationTest {
    @Test
    public void testMatchState() {
        MethodName.log();
        AflMatchFormat matchFormat = new AflMatchFormat();
        AflMatchState AflMatchState = new AflMatchState(matchFormat);
        String json = JsonUtil.marshalJson(AflMatchState, true);
        // System.out.println(json);
        AflMatchState voMatchState = JsonUtil.unmarshalJson(json, AflMatchState.class);
        // System.out.println(voMatchState);
        assertEquals(AflMatchState, voMatchState);
    }

    @Test
    public void testMatchParams() {
        MethodName.log();
        AflMatchParams aflMatchParams = new AflMatchParams();
        String json = JsonUtil.marshalJson(aflMatchParams, true);
        // System.out.println(json);
        AflMatchParams vpMatchParams = JsonUtil.unmarshalJson(json, AflMatchParams.class);
        // System.out.println(vpMatchParams);
        assertEquals(aflMatchParams, vpMatchParams);
    }
}
