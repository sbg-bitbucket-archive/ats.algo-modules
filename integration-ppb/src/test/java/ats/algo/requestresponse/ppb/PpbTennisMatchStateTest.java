package ats.algo.requestresponse.ppb;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchState;
import ats.core.util.json.JsonUtil;

public class PpbTennisMatchStateTest {

    @Test
    public void test() {
        MethodName.log();

        TennisMatchFormat format = new TennisMatchFormat();
        TennisMatchState tennisMatchState = new TennisMatchState(format);
        List<PpbTennisPointDetails> points = new ArrayList<PpbTennisPointDetails>();
        points.add(new PpbTennisPointDetails());
        PpbTennisMatchState pbbTennisMatchState =
                        PpbTennisMatchState.generatePpbTennisMatchState(tennisMatchState, points);

        String json = JsonUtil.marshalJson(pbbTennisMatchState, true);
        // System.out.println(json);
        PpbTennisMatchState ppbTennisMatchFormat2 = JsonUtil.unmarshalJson(json, PpbTennisMatchState.class);
        assertEquals(pbbTennisMatchState, ppbTennisMatchFormat2);
    }

}
