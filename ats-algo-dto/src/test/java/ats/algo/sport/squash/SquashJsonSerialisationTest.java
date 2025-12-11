package ats.algo.sport.squash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ats.algo.core.baseclasses.MatchState;
import ats.algo.genericsupportfunctions.JsonSerializer;
import ats.algo.genericsupportfunctions.PairOfIntegers;
import ats.core.util.json.JsonUtil;

public class SquashJsonSerialisationTest {

    @Test
    public void testMatchFormat() {
        SquashMatchFormat matchFormat1 = new SquashMatchFormat();
        String json = JsonUtil.marshalJson(matchFormat1, true);
        SquashMatchFormat matchFormat2 = JsonUtil.unmarshalJson(json, SquashMatchFormat.class);
        assertEquals(matchFormat1, matchFormat2);
    }

    @Test
    public void testMatchParams() {
        SquashMatchParams squashMatchParams = new SquashMatchParams();
        String json = JsonUtil.marshalJson(squashMatchParams, true);
        SquashMatchParams vpMatchParams = JsonUtil.unmarshalJson(json, SquashMatchParams.class);
        assertEquals(squashMatchParams, vpMatchParams);
    }

    @Test
    public void testMatchIncident() {
        SquashMatchIncident squashMatchIncident = new SquashMatchIncident();
        String json = JsonUtil.marshalJson(squashMatchIncident, true);
        SquashMatchIncident squashMatchIncident2 = JsonUtil.unmarshalJson(json, SquashMatchIncident.class);
        assertEquals(squashMatchIncident, squashMatchIncident2);
    }


    @Test
    public void testMatchFormatOptions() {
        SquashMatchFormatOptions o1 = new SquashMatchFormatOptions();
        String json = JsonUtil.marshalJson(o1, true);
        System.out.println(json);
        assertTrue(json.contains("13"));
    }

    @Test
    public void testMatchState() {
        SquashMatchFormat matchFormat = new SquashMatchFormat();
        SquashMatchState squashMatchState = new SquashMatchState(matchFormat);
        String json = JsonUtil.marshalJson(squashMatchState, true);
        SquashMatchState voMatchState = JsonUtil.unmarshalJson(json, SquashMatchState.class);
        assertEquals(squashMatchState, voMatchState);

    }

    @Test
    public void testSimpleMatchState() {
        SquashMatchFormat matchFormat = new SquashMatchFormat();
        SquashMatchState matchState = new SquashMatchState(matchFormat);
        PairOfIntegers[] gameScoreInGameN = {PairOfIntegers.generateFromString("1-21"),
                PairOfIntegers.generateFromString("21-5"), PairOfIntegers.generateFromString("15-21")};;
        matchState.setGameScoreInGameN(gameScoreInGameN);
        MatchState sms = matchState.generateSimpleMatchState();
        String json = JsonSerializer.serialize(sms, true);
        MatchState sms2 = JsonSerializer.deserialize(json, SquashSimpleMatchState.class);
        System.out.println("\nSTATE:\n" + sms2);
        assertEquals(sms, sms2);
    }
}
