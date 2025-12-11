package ats.algo.sport.tabletennis;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ats.algo.genericsupportfunctions.MethodName;
import ats.core.util.json.JsonUtil;

public class TabletennisJsonSerialisationTest {

    @Test
    public void testMatchParams() {
        MethodName.log();
        TabletennisMatchParams TabletennisMatchParams = new TabletennisMatchParams();
        String json = JsonUtil.marshalJson(TabletennisMatchParams, true);
        TabletennisMatchParams vpMatchParams = JsonUtil.unmarshalJson(json, TabletennisMatchParams.class);
        assertEquals(TabletennisMatchParams, vpMatchParams);
    }

    @Test
    public void testMatchState() {
        MethodName.log();
        TabletennisMatchFormat matchFormat = new TabletennisMatchFormat();
        TabletennisMatchState TabletennisMatchState = new TabletennisMatchState(matchFormat);
        String json = JsonUtil.marshalJson(TabletennisMatchState, true);
        TabletennisMatchState voMatchState = JsonUtil.unmarshalJson(json, TabletennisMatchState.class);
        assertEquals(TabletennisMatchState, voMatchState);

    }

}
