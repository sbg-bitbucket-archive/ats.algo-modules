package ats.algo.sport.americanfootball;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.*;

import org.junit.Test;

import ats.core.util.json.JsonUtil;

public class JsonSerialisationTest {

    @Test
    public void testMatchStateSerialisation() {
        MethodName.log();
        AmericanfootballMatchFormat matchFormat = new AmericanfootballMatchFormat();
        AmericanfootballMatchState AmericanfootballMatchState = new AmericanfootballMatchState(matchFormat);
        String json = JsonUtil.marshalJson(AmericanfootballMatchState, true);
        // System.out.println(json);
        AmericanfootballMatchState voMatchState = JsonUtil.unmarshalJson(json, AmericanfootballMatchState.class);
        // System.out.println(voMatchState);
        assertEquals(AmericanfootballMatchState, voMatchState);
    }

}
