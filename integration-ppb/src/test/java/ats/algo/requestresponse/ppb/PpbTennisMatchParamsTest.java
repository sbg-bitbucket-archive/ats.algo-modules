package ats.algo.requestresponse.ppb;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchFormat.FinalSetType;
import ats.algo.sport.tennis.TennisMatchFormat.Sex;
import ats.algo.sport.tennis.TennisMatchFormat.Surface;
import ats.algo.sport.tennis.TennisMatchFormat.TournamentLevel;
import ats.core.util.json.JsonUtil;

public class PpbTennisMatchParamsTest {

    @Test
    public void test() {
        MethodName.log();
        TennisMatchFormat matchFormat = new TennisMatchFormat();
        GenericMatchParams matchParams =
                        PpbTennisMatchParams.generatePpbGenericMatchParams(new GenericMatchParams(), matchFormat);
        String json = JsonUtil.marshalJson(matchParams, true);
        // System.out.println(json);
        GenericMatchParams matchParams2 = JsonUtil.unmarshalJson(json, GenericMatchParams.class);
        assertEquals(matchParams, matchParams2);
        PpbTennisMatchParams ppbTennisMatchParams = PpbTennisMatchParams.generate(matchParams, matchFormat);
        String json2 = JsonUtil.marshalJson(ppbTennisMatchParams, true);
        // System.out.println(json2);
        PpbTennisMatchParams ppbTennisMatchParams2 = JsonUtil.unmarshalJson(json2, PpbTennisMatchParams.class);
        assertEquals(ppbTennisMatchParams, ppbTennisMatchParams2);
        assertTrue(ppbTennisMatchParams.isUsePlayerParameters());
    }

    @Test
    public void testParamsOnCreation() {
        MethodName.log();
        TennisMatchFormat matchFormat = new TennisMatchFormat(false, Sex.MEN, Surface.HARD, TournamentLevel.ITF, 3, 6,
                        FinalSetType.NORMAL_WITH_TIE_BREAK, false);
        GenericMatchParams matchParams =
                        PpbTennisMatchParams.generatePpbGenericMatchParams(new GenericMatchParams(), matchFormat);
        String json = JsonUtil.marshalJson(matchParams, true);
        // System.out.println(json);
        GenericMatchParams matchParams2 = JsonUtil.unmarshalJson(json, GenericMatchParams.class);
        assertEquals(matchParams, matchParams2);
        PpbTennisMatchParams ppbTennisMatchParams = PpbTennisMatchParams.generate(matchParams, matchFormat);
        String json2 = JsonUtil.marshalJson(ppbTennisMatchParams, true);
        // System.out.println(json2);
        PpbTennisMatchParams ppbTennisMatchParams2 = JsonUtil.unmarshalJson(json2, PpbTennisMatchParams.class);
        assertEquals(ppbTennisMatchParams, ppbTennisMatchParams2);
        assertFalse(ppbTennisMatchParams.isUsePlayerParameters());
    }

}
