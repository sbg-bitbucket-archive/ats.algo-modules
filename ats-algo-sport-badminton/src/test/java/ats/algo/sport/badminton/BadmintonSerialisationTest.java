package ats.algo.sport.badminton;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.sport.badminton.BadmintonMatchFormat;
import ats.algo.sport.badminton.BadmintonMatchParams;
import ats.algo.sport.badminton.BadmintonMatchState;
import ats.core.util.json.JsonUtil;

public class BadmintonSerialisationTest {


    @Test
    public void testMatchParams() {
        MethodName.log();
        BadmintonMatchParams BadmintonMatchParams = new BadmintonMatchParams();
        String json = JsonUtil.marshalJson(BadmintonMatchParams, true);
        BadmintonMatchParams vpMatchParams = JsonUtil.unmarshalJson(json, BadmintonMatchParams.class);
        assertEquals(BadmintonMatchParams, vpMatchParams);
    }


    @Test
    public void testMatchState() {
        MethodName.log();
        BadmintonMatchFormat matchFormat = new BadmintonMatchFormat();
        BadmintonMatchState BadmintonMatchState = new BadmintonMatchState(matchFormat);
        String json = JsonUtil.marshalJson(BadmintonMatchState, true);
        BadmintonMatchState voMatchState = JsonUtil.unmarshalJson(json, BadmintonMatchState.class);
        // System.out.println(voMatchState);
        assertEquals(BadmintonMatchState, voMatchState);
    }

}

