package ats.algo.core.tradercontrol;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ats.algo.core.baseclasses.MatchParam;
import ats.algo.genericsupportfunctions.JsonSerializer;
import ats.core.util.json.JsonUtil;

public class TraderControlMatchParamSerializationTest {
    @Test
    public void test() {
        TraderControlMatchParam p1 = new TraderControlMatchParam(5, 1, 10);
        String json = JsonSerializer.serialize(p1, MatchParam.class, true);
        System.out.println(json);
        MatchParam p2 = JsonUtil.unmarshalJson(json, MatchParam.class);
        assertEquals(p1, p2);


    }



}
