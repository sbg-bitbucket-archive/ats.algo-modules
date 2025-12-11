package ats.algo.sport.tennis;

import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.core.baseclasses.MatchFormatOptions;
import ats.core.util.json.JsonUtil;

public class TennisMatchFormatOptionsTest {
    @Test
    public void test() {
        TennisMatchFormat matchFormat = new TennisMatchFormat();
        MatchFormatOptions x = matchFormat.matchFormatOptions();
        String json = JsonUtil.marshalJson(x, true);
        System.out.println(json);
        assertTrue(json.contains("ITF"));
    }

}
