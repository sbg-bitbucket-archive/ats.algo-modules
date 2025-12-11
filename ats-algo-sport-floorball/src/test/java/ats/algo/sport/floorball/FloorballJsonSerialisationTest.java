package ats.algo.sport.floorball;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ats.algo.genericsupportfunctions.MethodName;
import ats.core.util.json.JsonUtil;

public class FloorballJsonSerialisationTest {

    @Test
    public void testMatchParams() {
        MethodName.log();
        FloorballMatchParams floorballMatchParams = new FloorballMatchParams();
        String json = JsonUtil.marshalJson(floorballMatchParams, true);

        // System.out.println("\nPARAMS:\n" + json);

        FloorballMatchParams vpMatchParams = JsonUtil.unmarshalJson(json, FloorballMatchParams.class);
        assertEquals(floorballMatchParams, vpMatchParams);
    }

    @Test
    public void testMatchState() {
        MethodName.log();
        FloorballMatchState floorballMatchState = new FloorballMatchState();
        String json = JsonUtil.marshalJson(floorballMatchState, true);

        // System.out.println("\nSTATE:\n" + json);

        FloorballMatchState voMatchState = JsonUtil.unmarshalJson(json, FloorballMatchState.class);
        assertEquals(floorballMatchState, voMatchState);

    }

}
