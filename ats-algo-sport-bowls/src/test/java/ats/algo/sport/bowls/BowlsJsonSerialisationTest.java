package ats.algo.sport.bowls;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ats.algo.genericsupportfunctions.MethodName;
import ats.core.util.json.JsonUtil;

public class BowlsJsonSerialisationTest {

    @Test
    public void testMatchParams() {
        MethodName.log();
        BowlsMatchParams bowlsMatchParams = new BowlsMatchParams();
        String json = JsonUtil.marshalJson(bowlsMatchParams, true);
        BowlsMatchParams vpMatchParams = JsonUtil.unmarshalJson(json, BowlsMatchParams.class);
        assertEquals(bowlsMatchParams, vpMatchParams);
    }


    @Test
    public void testMatchState() {
        MethodName.log();
        BowlsMatchFormat matchFormat = new BowlsMatchFormat();
        BowlsMatchState bowlsMatchState = new BowlsMatchState(matchFormat);
        String json = JsonUtil.marshalJson(bowlsMatchState, true);
        BowlsMatchState voMatchState = JsonUtil.unmarshalJson(json, BowlsMatchState.class);
        assertEquals(bowlsMatchState, voMatchState);
    }

}
