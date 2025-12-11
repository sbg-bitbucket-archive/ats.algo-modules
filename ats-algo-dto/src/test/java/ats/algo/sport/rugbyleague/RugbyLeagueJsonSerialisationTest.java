package ats.algo.sport.rugbyleague;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ats.algo.core.baseclasses.MatchState;
import ats.algo.genericsupportfunctions.JsonSerializer;
import ats.core.util.json.JsonUtil;

public class RugbyLeagueJsonSerialisationTest {

    @Test
    public void testMatchFormat() {
        RugbyLeagueMatchFormat matchFormat1 = new RugbyLeagueMatchFormat();
        String json = JsonUtil.marshalJson(matchFormat1, true);
        RugbyLeagueMatchFormat matchFormat2 = JsonUtil.unmarshalJson(json, RugbyLeagueMatchFormat.class);
        assertEquals(matchFormat1, matchFormat2);
    }


    @Test
    public void testMatchParams() {
        RugbyLeagueMatchParams footballMatchParams = new RugbyLeagueMatchParams();
        String json = JsonUtil.marshalJson(footballMatchParams, true);
        System.out.println(json);
        RugbyLeagueMatchParams vpMatchParams = JsonUtil.unmarshalJson(json, RugbyLeagueMatchParams.class);
        System.out.println(vpMatchParams);
        assertEquals(footballMatchParams, vpMatchParams);
    }

    @Test
    public void testMatchIncident() {
        RugbyLeagueMatchIncident footballMatchIncident = new RugbyLeagueMatchIncident();
        String json = JsonUtil.marshalJson(footballMatchIncident, true);
        RugbyLeagueMatchIncident RugbyLeagueMatchIncident2 =
                        JsonUtil.unmarshalJson(json, RugbyLeagueMatchIncident.class);
        assertEquals(footballMatchIncident, RugbyLeagueMatchIncident2);
    }

    @Test
    public void testMatchState() {
        RugbyLeagueMatchFormat matchFormat = new RugbyLeagueMatchFormat();
        RugbyLeagueMatchState rugbyLeagueMatchState = new RugbyLeagueMatchState(matchFormat);
        String json = JsonUtil.marshalJson(rugbyLeagueMatchState, true);
        System.out.println(json);
        RugbyLeagueMatchState voMatchState = JsonUtil.unmarshalJson(json, RugbyLeagueMatchState.class);
        System.out.println(voMatchState);
        assertEquals(rugbyLeagueMatchState, voMatchState);

    }

    @Test
    public void testSimpleMatchState() {
        RugbyLeagueMatchFormat matchFormat = new RugbyLeagueMatchFormat();
        RugbyLeagueMatchState matchState = new RugbyLeagueMatchState(matchFormat);
        MatchState sms = matchState.generateSimpleMatchState();
        String json = JsonSerializer.serialize(sms, true);
        System.out.println("\nSTATE:\n" + json);
        MatchState sms2 = JsonSerializer.deserialize(json, RugbyLeagueSimpleMatchState.class);
        assertEquals(sms, sms2);
    }
}
