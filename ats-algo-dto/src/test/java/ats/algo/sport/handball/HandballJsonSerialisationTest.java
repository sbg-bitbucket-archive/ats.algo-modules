package ats.algo.sport.handball;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ats.algo.core.baseclasses.MatchState;
import ats.algo.genericsupportfunctions.JsonSerializer;
import ats.core.util.json.JsonUtil;

public class HandballJsonSerialisationTest {

    @Test
    public void testMatchFormat() {
        HandballMatchFormat matchFormat1 = new HandballMatchFormat();
        String json = JsonUtil.marshalJson(matchFormat1, true);
        HandballMatchFormat matchFormat2 = JsonUtil.unmarshalJson(json, HandballMatchFormat.class);
        assertEquals(matchFormat1, matchFormat2);
    }


    @Test
    public void testMatchParams() {
        HandballMatchParams HandballMatchParams = new HandballMatchParams();
        String json = JsonUtil.marshalJson(HandballMatchParams, true);
        System.out.println(json);
        HandballMatchParams vpMatchParams = JsonUtil.unmarshalJson(json, HandballMatchParams.class);
        assertEquals(HandballMatchParams, vpMatchParams);
    }

    @Test
    public void testMatchIncident() {
        HandballMatchIncident HandballMatchIncident = new HandballMatchIncident();
        String json = JsonUtil.marshalJson(HandballMatchIncident, true);
        HandballMatchIncident HandballMatchIncident2 = JsonUtil.unmarshalJson(json, HandballMatchIncident.class);
        assertEquals(HandballMatchIncident, HandballMatchIncident2);
    }

    @Test
    public void testMatchState() {
        HandballMatchState HandballMatchState = new HandballMatchState();
        String json = JsonUtil.marshalJson(HandballMatchState, true);
        HandballMatchState voMatchState = JsonUtil.unmarshalJson(json, HandballMatchState.class);
        assertEquals(HandballMatchState, voMatchState);

    }

    @Test
    public void testSimpleMatchState() {
        HandballMatchFormat matchFormat = new HandballMatchFormat();
        HandballMatchState matchState = new HandballMatchState(matchFormat);
        MatchState sms = matchState.generateSimpleMatchState(3);
        String json = JsonSerializer.serialize(sms, MatchState.class, true);
        System.out.println("\nSTATE:\n" + json);
        MatchState sms2 = JsonSerializer.deserialize(json, MatchState.class);
        assertEquals(sms, sms2);
    }
}
