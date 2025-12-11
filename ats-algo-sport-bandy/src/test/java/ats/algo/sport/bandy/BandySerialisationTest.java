package ats.algo.sport.bandy;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ats.core.util.json.JsonUtil;

public class BandySerialisationTest {



    @Test
    public void testMatchParams() {
        MethodName.log();
        BandyMatchParams bandyMatchParams = new BandyMatchParams();
        String json = JsonUtil.marshalJson(bandyMatchParams, true);

        // System.out.println("\nPARAMS:\n" + json);

        BandyMatchParams vpMatchParams = JsonUtil.unmarshalJson(json, BandyMatchParams.class);
        assertEquals(bandyMatchParams, vpMatchParams);
    }


    @Test
    public void testMatchState() {
        MethodName.log();
        BandyMatchState bandyMatchState = new BandyMatchState();
        String json = JsonUtil.marshalJson(bandyMatchState, true);

        // System.out.println("\nSTATE:\n" + json);

        BandyMatchState voMatchState = JsonUtil.unmarshalJson(json, BandyMatchState.class);
        assertEquals(bandyMatchState, voMatchState);
    }

}
