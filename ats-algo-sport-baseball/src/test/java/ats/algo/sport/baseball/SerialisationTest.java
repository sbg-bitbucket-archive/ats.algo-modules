package ats.algo.sport.baseball;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ats.core.util.json.JsonUtil;

public class SerialisationTest {

    @Test
    public void testMatchParams() {
        MethodName.log();
        BaseballMatchParams baseballMatchParams = new BaseballMatchParams();
        String json = JsonUtil.marshalJson(baseballMatchParams, true);
        {
        } // System.out.println("-----------------------------------------");
        {
        } // System.out.println(json);
        BaseballMatchParams vpMatchParams = JsonUtil.unmarshalJson(json, BaseballMatchParams.class);
        {
        } // System.out.println("-----------------------------------------");
        {
        } // System.out.println(vpMatchParams);
        assertEquals(baseballMatchParams, vpMatchParams);
    }

    @Test
    public void testMatchState() {
        MethodName.log();
        BaseballMatchFormat matchFormat = new BaseballMatchFormat();
        BaseballMatchState BaseballMatchState = new BaseballMatchState(matchFormat);
        String json = JsonUtil.marshalJson(BaseballMatchState, true);
        BaseballMatchState voMatchState = JsonUtil.unmarshalJson(json, BaseballMatchState.class);
        {
        } // System.out.println(json);
        assertEquals(BaseballMatchState, voMatchState);

    }

}
