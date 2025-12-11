package ats.algo.sport.basketball;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ats.algo.genericsupportfunctions.MethodName;
import ats.core.util.json.JsonUtil;

public class BasketballJsonSerialTest {

    @Test
    public void testMatchState() {
        MethodName.log();
        BasketballMatchState basketballMatchState = new BasketballMatchState();
        String json = JsonUtil.marshalJson(basketballMatchState, true);

        // System.out.println("\nSTATE:\n" + json);

        BasketballMatchState voMatchState = JsonUtil.unmarshalJson(json, BasketballMatchState.class);
        assertEquals(basketballMatchState, voMatchState);
    }

    @Test
    public void testMatchParams() {
        MethodName.log();
        BasketballMatchParams basketballMatchParams = new BasketballMatchParams();
        String json = JsonUtil.marshalJson(basketballMatchParams, true);

        // System.out.println("\nPARAMS:\n" + json);

        BasketballMatchParams vpMatchParams = JsonUtil.unmarshalJson(json, BasketballMatchParams.class);
        assertEquals(basketballMatchParams, vpMatchParams);
    }

}
