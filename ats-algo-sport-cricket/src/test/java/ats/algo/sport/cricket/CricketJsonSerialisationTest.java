package ats.algo.sport.cricket;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ats.algo.genericsupportfunctions.MethodName;
import ats.core.util.json.JsonUtil;

public class CricketJsonSerialisationTest {

    @Test
    public void testMatchParams() {
        MethodName.log();
        CricketMatchParams CricketMatchParams = new CricketMatchParams();
        String json = JsonUtil.marshalJson(CricketMatchParams, true);
        CricketMatchParams vpMatchParams = JsonUtil.unmarshalJson(json, CricketMatchParams.class);
        assertEquals(CricketMatchParams, vpMatchParams);
    }

    @Test
    public void testMatchState() {
        MethodName.log();
        CricketMatchFormat matchFormat = new CricketMatchFormat();
        CricketMatchState CricketMatchState = new CricketMatchState(matchFormat);
        String json = JsonUtil.marshalJson(CricketMatchState, true);
        CricketMatchState voMatchState = JsonUtil.unmarshalJson(json, CricketMatchState.class);
        assertEquals(CricketMatchState, voMatchState);

    }

}
