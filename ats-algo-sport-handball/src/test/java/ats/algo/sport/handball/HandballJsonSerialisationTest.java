package ats.algo.sport.handball;

import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.MethodName;
import ats.algo.sport.handball.HandballMatchIncident.HandballMatchIncidentType;
import ats.core.util.json.JsonUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HandballJsonSerialisationTest {

    @Test
    public void testMatchFormat() {
        MethodName.log();
        HandballMatchFormat matchFormat1 = new HandballMatchFormat();
        String json = JsonUtil.marshalJson(matchFormat1, true);
        HandballMatchFormat matchFormat2 = JsonUtil.unmarshalJson(json, HandballMatchFormat.class);
        assertEquals(matchFormat1, matchFormat2);
    }


    @Test
    public void testMatchParams() {
        MethodName.log();
        HandballMatchParams HandballMatchParams = new HandballMatchParams();
        String json = JsonUtil.marshalJson(HandballMatchParams, true);
        // System.out.println(json);
        HandballMatchParams vpMatchParams = JsonUtil.unmarshalJson(json, HandballMatchParams.class);
        assertEquals(HandballMatchParams, vpMatchParams);
    }

    @Test
    public void testMatchIncident() {
        MethodName.log();
        HandballMatchIncident HandballMatchIncident = new HandballMatchIncident();
        HandballMatchIncident.set(HandballMatchIncidentType.GOAL, 60, TeamId.A);
        String json = JsonUtil.marshalJson(HandballMatchIncident, true);
        HandballMatchIncident HandballMatchIncident2 = JsonUtil.unmarshalJson(json, HandballMatchIncident.class);
        // System.out.println(json);
        assertEquals(HandballMatchIncident, HandballMatchIncident2);
    }

    @Test
    public void testMatchState() {
        MethodName.log();
        HandballMatchState HandballMatchState = new HandballMatchState();
        String json = JsonUtil.marshalJson(HandballMatchState, true);
        HandballMatchState voMatchState = JsonUtil.unmarshalJson(json, HandballMatchState.class);
        assertEquals(HandballMatchState, voMatchState);

    }
}
