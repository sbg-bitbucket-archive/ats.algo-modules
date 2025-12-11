package ats.algo.betstars.soccer;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ats.algo.genericsupportfunctions.Gaussian;
import ats.core.util.json.JsonUtil;

public class BsSoccerMatchParamsSerialisationTest {
    @Test
    public void test() {
        MethodName.log();
        BsSoccerMatchParams matchParams1 = new BsSoccerMatchParams();
        matchParams1.getScoreRateTeamA().setGaussian(new Gaussian(23, 6));
        String json = JsonUtil.marshalJson(matchParams1, true);
        // System.out.println(json);
        BsSoccerMatchParams matchParams2 = JsonUtil.unmarshalJson(json, BsSoccerMatchParams.class);

        // for (Entry<String, MatchParam> e : matchParams1.getParamMap().entrySet()) {
        //// MatchParam p1 = e.getValue();
        //// MatchParam p2 = matchParams2.getParamMap().get(e.getKey());
        // // System.out.println(e.getKey());
        // // System.out.println(p1.toString());
        // // System.out.println(p2.toString());
        // }
        // System.out.println(matchParams1.toString());
        assertEquals(matchParams1, matchParams2);
    }

    @Test
    public void testCopy() {
        MethodName.log();
        BsSoccerMatchParams matchParams1 = new BsSoccerMatchParams();
        matchParams1.getScoreRateTeamA().setGaussian(new Gaussian(23, 6));
        BsSoccerMatchParams matchParams2 = (BsSoccerMatchParams) matchParams1.copy();
        // System.out.println(matchParams1);
        // System.out.println(matchParams2);
        assertEquals(matchParams1, matchParams2);;
    }
}
