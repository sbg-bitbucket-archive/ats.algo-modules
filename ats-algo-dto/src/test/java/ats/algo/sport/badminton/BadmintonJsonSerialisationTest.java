package ats.algo.sport.badminton;

import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.core.baseclasses.MatchState;
import ats.algo.genericsupportfunctions.JsonSerializer;
import ats.algo.genericsupportfunctions.PairOfIntegers;
import ats.algo.sport.badminton.BadmintonMatchFormat;
import ats.algo.sport.badminton.BadmintonMatchIncident;
import ats.algo.sport.badminton.BadmintonMatchParams;
import ats.algo.sport.badminton.BadmintonMatchState;
import ats.core.util.json.JsonUtil;

public class BadmintonJsonSerialisationTest {

    @Test
    public void testMatchFormat() {
        BadmintonMatchFormat matchFormat1 = new BadmintonMatchFormat();
        String json = JsonUtil.marshalJson(matchFormat1, true);
        BadmintonMatchFormat matchFormat2 = JsonUtil.unmarshalJson(json, BadmintonMatchFormat.class);
        assertEquals(matchFormat1, matchFormat2);
    }

    @Test
    public void testMatchParams() {
        BadmintonMatchParams BadmintonMatchParams = new BadmintonMatchParams();
        String json = JsonUtil.marshalJson(BadmintonMatchParams, true);
        BadmintonMatchParams vpMatchParams = JsonUtil.unmarshalJson(json, BadmintonMatchParams.class);
        assertEquals(BadmintonMatchParams, vpMatchParams);
    }

    @Test
    public void testMatchIncident() {
        BadmintonMatchIncident BadmintonMatchIncident = new BadmintonMatchIncident();
        String json = JsonUtil.marshalJson(BadmintonMatchIncident, true);
        BadmintonMatchIncident BadmintonMatchIncident2 = JsonUtil.unmarshalJson(json, BadmintonMatchIncident.class);
        assertEquals(BadmintonMatchIncident, BadmintonMatchIncident2);
    }

    @Test
    public void testMatchFormatOptions() {
        BadmintonMatchFormatOptions o1 = new BadmintonMatchFormatOptions();
        String json = JsonUtil.marshalJson(o1, true);
        System.out.println(json);
        assertTrue(json.contains("30"));
    }

    @Test
    public void testMatchState() {
        BadmintonMatchFormat matchFormat = new BadmintonMatchFormat();
        BadmintonMatchState BadmintonMatchState = new BadmintonMatchState(matchFormat);
        String json = JsonUtil.marshalJson(BadmintonMatchState, true);
        BadmintonMatchState voMatchState = JsonUtil.unmarshalJson(json, BadmintonMatchState.class);
        System.out.println(voMatchState);
        assertEquals(BadmintonMatchState, voMatchState);

    }

    @Test
    public void testSimpleMatchState() {
        BadmintonMatchFormat matchFormat = new BadmintonMatchFormat();
        BadmintonMatchState matchState = new BadmintonMatchState(matchFormat);
        PairOfIntegers[] gameScoreInGameN = {PairOfIntegers.generateFromString("1-21"),
                PairOfIntegers.generateFromString("21-5"), PairOfIntegers.generateFromString("15-21")};;
        matchState.setGameScoreInGameN(gameScoreInGameN);
        MatchState sms = matchState.generateSimpleMatchState();
        String json = JsonSerializer.serialize(sms, true);
        MatchState sms2 = JsonSerializer.deserialize(json, BadmintonSimpleMatchState.class);
        System.out.println("\nSTATE:\n" + sms2);
        assertEquals(sms, sms2);
    }
}
