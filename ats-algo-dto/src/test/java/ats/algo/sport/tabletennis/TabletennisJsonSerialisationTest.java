package ats.algo.sport.tabletennis;

import static org.junit.Assert.*;
import org.junit.Test;

import ats.algo.core.baseclasses.MatchState;
import ats.algo.genericsupportfunctions.JsonSerializer;
import ats.algo.genericsupportfunctions.PairOfIntegers;
import ats.algo.sport.tabletennis.TabletennisMatchFormat;
import ats.algo.sport.tabletennis.TabletennisMatchIncident;
import ats.algo.sport.tabletennis.TabletennisMatchParams;
import ats.algo.sport.tabletennis.TabletennisMatchState;
import ats.core.util.json.JsonUtil;

public class TabletennisJsonSerialisationTest {

    @Test
    public void testMatchFormat() {
        TabletennisMatchFormat matchFormat1 = new TabletennisMatchFormat();
        String json = JsonUtil.marshalJson(matchFormat1, true);
        TabletennisMatchFormat matchFormat2 = JsonUtil.unmarshalJson(json, TabletennisMatchFormat.class);
        assertEquals(matchFormat1, matchFormat2);
    }

    @Test
    public void testMatchParams() {
        TabletennisMatchParams TabletennisMatchParams = new TabletennisMatchParams();
        String json = JsonUtil.marshalJson(TabletennisMatchParams, true);
        TabletennisMatchParams vpMatchParams = JsonUtil.unmarshalJson(json, TabletennisMatchParams.class);
        assertEquals(TabletennisMatchParams, vpMatchParams);
    }

    @Test
    public void testMatchIncident() {
        TabletennisMatchIncident TabletennisMatchIncident = new TabletennisMatchIncident();
        String json = JsonUtil.marshalJson(TabletennisMatchIncident, true);
        TabletennisMatchIncident TabletennisMatchIncident2 =
                        JsonUtil.unmarshalJson(json, TabletennisMatchIncident.class);
        assertEquals(TabletennisMatchIncident, TabletennisMatchIncident2);
    }

    @Test
    public void testMatchFormatOptions() {
        TabletennisMatchFormatOptions o1 = new TabletennisMatchFormatOptions();
        String json = JsonUtil.marshalJson(o1, true);
        System.out.println(json);
        assertTrue(json.contains("11"));
    }

    @Test
    public void testMatchState() {
        TabletennisMatchFormat matchFormat = new TabletennisMatchFormat();
        TabletennisMatchState TabletennisMatchState = new TabletennisMatchState(matchFormat);
        String json = JsonUtil.marshalJson(TabletennisMatchState, true);
        TabletennisMatchState voMatchState = JsonUtil.unmarshalJson(json, TabletennisMatchState.class);
        assertEquals(TabletennisMatchState, voMatchState);

    }

    @Test
    public void testSimpleMatchState() {
        TabletennisMatchFormat matchFormat = new TabletennisMatchFormat();
        TabletennisMatchState matchState = new TabletennisMatchState(matchFormat);
        PairOfIntegers[] gameScoreInGameN = {PairOfIntegers.generateFromString("1-11"),
                PairOfIntegers.generateFromString("11-5"), PairOfIntegers.generateFromString("15-13")};;
        matchState.setGameScoreInGameN(gameScoreInGameN);
        MatchState sms = matchState.generateSimpleMatchState();
        String json = JsonSerializer.serialize(sms, true);
        MatchState sms2 = JsonSerializer.deserialize(json, TabletennisSimpleMatchState.class);
        System.out.println("\nSTATE:\n" + sms2);
        assertEquals(sms, sms2);
    }
}
