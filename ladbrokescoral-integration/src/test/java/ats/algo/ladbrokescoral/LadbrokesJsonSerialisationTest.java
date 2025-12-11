package ats.algo.ladbrokescoral;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.ladbrokescoral.trading.algo.aussierules.AussieRulesMatchParams;

import ats.algo.sport.afl.AflMatchFormat;
import ats.algo.sport.afl.AflMatchState;
import ats.core.util.json.JsonUtil;

public class LadbrokesJsonSerialisationTest {
    @Test
    public void testMatchParams() {
        MethodName.log();
        AussieRulesMatchParams matchParams1 = new AussieRulesMatchParams();
        matchParams1.setEventId(1111);
        String json = JsonUtil.marshalJson(matchParams1, true);
        // System.out.println("AFL json representation: ");
        // System.out.println(json);
        AussieRulesMatchParams matchParams2 = JsonUtil.unmarshalJson(json, AussieRulesMatchParams.class);
        // System.out.println(matchParams2.toString());
        assertEquals(matchParams1, matchParams2);
    }

    /*
     * TODO commented out until AussieRulesMatchPArams gets rebuilt with latest dto and is derived from AlgoMatchParams
     */
    // @Test
    // public void testCopy() {
    // MethodName.log();
    // AussieRulesMatchParams matchParams1 = new AussieRulesMatchParams();
    // matchParams1.setEventId(1111);
    // AussieRulesMatchParams matchParams2 = (AussieRulesMatchParams) matchParams1.copy();
    // assertEquals(matchParams1, matchParams2);
    // }

    @Test
    public void testMatchState() {
        MethodName.log();
        AflMatchFormat matchFormat = new AflMatchFormat();
        AflMatchState AflMatchState = new AflMatchState(matchFormat);
        String json = JsonUtil.marshalJson(AflMatchState, true);
        // System.out.println(json);
        AflMatchState voMatchState = JsonUtil.unmarshalJson(json, AflMatchState.class);
        // System.out.println(voMatchState);
        assertEquals(AflMatchState, voMatchState);
    }
}
