package ats.algo.sport.beachvolleyball;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ats.algo.genericsupportfunctions.MethodName;
import ats.core.util.json.JsonUtil;

public class BeachVolleyballJsonTest {

    @Test
    public void testMatchParams() {
        MethodName.log();
        BeachVolleyballMatchParams BeachVolleyballMatchParams = new BeachVolleyballMatchParams();
        String json = JsonUtil.marshalJson(BeachVolleyballMatchParams, true);
        // System.out.println(json);
        BeachVolleyballMatchParams vpMatchParams = JsonUtil.unmarshalJson(json, BeachVolleyballMatchParams.class);
        assertEquals(BeachVolleyballMatchParams, vpMatchParams);
    }

    @Test
    public void testMatchState() {
        MethodName.log();
        BeachVolleyballMatchFormat matchFormat = new BeachVolleyballMatchFormat();
        BeachVolleyballMatchState BeachVolleyballMatchState = new BeachVolleyballMatchState(matchFormat);
        String json = JsonUtil.marshalJson(BeachVolleyballMatchState, true);
        BeachVolleyballMatchState voMatchState = JsonUtil.unmarshalJson(json, BeachVolleyballMatchState.class);
        assertEquals(BeachVolleyballMatchState, voMatchState);

    }

}
