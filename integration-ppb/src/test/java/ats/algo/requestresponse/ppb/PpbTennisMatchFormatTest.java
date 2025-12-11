package ats.algo.requestresponse.ppb;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.sport.tennis.TennisMatchFormat;
import ats.core.util.json.JsonUtil;

public class PpbTennisMatchFormatTest {

    @Test
    public void test() {
        MethodName.log();
        PpbTennisMatchFormat ppbTennisMatchFormat =
                        PpbTennisMatchFormat.generatePbbTennisMatchFormat(new TennisMatchFormat());
        String json = JsonUtil.marshalJson(ppbTennisMatchFormat, true);
        // System.out.println(json);
        PpbTennisMatchFormat ppbTennisMatchFormat2 = JsonUtil.unmarshalJson(json, PpbTennisMatchFormat.class);
        assertEquals(ppbTennisMatchFormat, ppbTennisMatchFormat2);
    }

}
