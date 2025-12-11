package ats.algo.sport.squash;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ats.algo.genericsupportfunctions.MethodName;
import ats.core.util.json.JsonUtil;

public class SquashJsonSerialisationtest {

    @Test
    public void testMatchParams() {
        MethodName.log();
        SquashMatchParams squashMatchParams = new SquashMatchParams();
        String json = JsonUtil.marshalJson(squashMatchParams, true);
        SquashMatchParams vpMatchParams = JsonUtil.unmarshalJson(json, SquashMatchParams.class);
        assertEquals(squashMatchParams, vpMatchParams);
    }

    @Test
    public void testMatchState() {
        MethodName.log();
        SquashMatchFormat matchFormat = new SquashMatchFormat();
        SquashMatchState squashMatchState = new SquashMatchState(matchFormat);
        String json = JsonUtil.marshalJson(squashMatchState, true);
        SquashMatchState voMatchState = JsonUtil.unmarshalJson(json, SquashMatchState.class);
        assertEquals(squashMatchState, voMatchState);

    }

}
