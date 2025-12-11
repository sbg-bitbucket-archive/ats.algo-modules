package ats.algo.sport.darts;

import ats.algo.genericsupportfunctions.MethodName;
import ats.core.util.json.JsonUtil;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DartJsonSerialisationTest {

    @Test
    public void testMatchParams() {
        MethodName.log();
        DartMatchParams dartMatchParams = new DartMatchParams();
        String json = JsonUtil.marshalJson(dartMatchParams, true);
        // System.out.println(json);
        DartMatchParams dartMatchParams2 = JsonUtil.unmarshalJson(json, DartMatchParams.class);
        // System.out.println(json);
        assertEquals(dartMatchParams, dartMatchParams2);
    }

    @Test
    public void testMatchState() {
        MethodName.log();
        DartMatchFormat matchFormat = new DartMatchFormat();
        DartMatchState dartMatchState = new DartMatchState(matchFormat);
        String json = JsonUtil.marshalJson(dartMatchState, true);
        DartMatchState voMatchState = JsonUtil.unmarshalJson(json, DartMatchState.class);
        // System.out.println(json);
        assertEquals(dartMatchState, voMatchState);
    }
}
