package ats.algo.core.baseclasses;

import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.core.MarketGroup;
import ats.core.util.json.JsonUtil;

public class MatchParamSerialisationTest {

    @Test
    public void test() {
        MatchParam matchParam = new MatchParam();
        matchParam.updateGaussian(6, 0.6);
        matchParam.getGaussian().setBias(.1);
        matchParam.setMarketGroup(MarketGroup.GOALS);
        matchParam.setMatchParameterType(MatchParamType.A);
        matchParam.setMinAllowedParamValue(4);
        matchParam.setMaxAllowedParamValue(8);
        String json = JsonUtil.marshalJson(matchParam, true);
        System.out.print(json);
        MatchParam matchParam2 = JsonUtil.unmarshalJson(json, MatchParam.class);
        System.out.print(matchParam + "\n");
        System.out.print(matchParam2 + "\n");
        assertEquals(matchParam, matchParam2);
    }

    @Test
    public void testCopy() {
        MatchParam matchParam = new MatchParam();
        matchParam.updateGaussian(6, 0.6);
        matchParam.getGaussian().setBias(.1);
        matchParam.getGaussian().setApplyBias(true);
        matchParam.setMarketGroup(MarketGroup.GOALS);
        matchParam.setMatchParameterType(MatchParamType.A);
        matchParam.setMinAllowedParamValue(4);
        matchParam.setMaxAllowedParamValue(8);
        MatchParam matchParam2 = matchParam.copy();
        System.out.print(matchParam + "\n");
        System.out.print(matchParam2 + "\n");
        assertEquals(matchParam, matchParam2);
    }

}
