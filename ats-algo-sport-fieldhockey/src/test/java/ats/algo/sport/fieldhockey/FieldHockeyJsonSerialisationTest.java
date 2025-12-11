package ats.algo.sport.fieldhockey;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ats.algo.genericsupportfunctions.MethodName;
import ats.core.util.json.JsonUtil;

public class FieldHockeyJsonSerialisationTest {

    @Test
    public void testMatchParams() {
        MethodName.log();
        FieldhockeyMatchParams fieldhockeyMatchParams = new FieldhockeyMatchParams();
        String json = JsonUtil.marshalJson(fieldhockeyMatchParams, true);

        // System.out.println("\nPARAMS:\n" + json);

        FieldhockeyMatchParams vpMatchParams = JsonUtil.unmarshalJson(json, FieldhockeyMatchParams.class);
        assertEquals(fieldhockeyMatchParams, vpMatchParams);
    }

    @Test
    public void testMatchState() {
        MethodName.log();
        FieldhockeyMatchState fieldhockeyMatchState = new FieldhockeyMatchState();
        String json = JsonUtil.marshalJson(fieldhockeyMatchState, true);

        // System.out.println("\nSTATE:\n" + json);

        FieldhockeyMatchState voMatchState = JsonUtil.unmarshalJson(json, FieldhockeyMatchState.class);
        assertEquals(fieldhockeyMatchState, voMatchState);

    }

}
