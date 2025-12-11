package ats.algo.sport.rugbyunion;

import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.MethodName;
import ats.algo.sport.rugbyunion.RugbyUnionMatchIncident.RugbyUnionMatchIncidentType;
import ats.core.util.json.JsonUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RugbyunionJsonSerialisationTest {

    @Test
    public void testMatchFormat() {
        MethodName.log();
        RugbyUnionMatchFormat matchFormat1 = new RugbyUnionMatchFormat();
        String json = JsonUtil.marshalJson(matchFormat1, true);
        RugbyUnionMatchFormat matchFormat2 = JsonUtil.unmarshalJson(json, RugbyUnionMatchFormat.class);
        assertEquals(matchFormat1, matchFormat2);
    }


    @Test
    public void testMatchParams() {
        MethodName.log();
        RugbyUnionMatchParams footballMatchParams = new RugbyUnionMatchParams();
        String json = JsonUtil.marshalJson(footballMatchParams, true);
        // System.out.println(json);
        RugbyUnionMatchParams vpMatchParams = JsonUtil.unmarshalJson(json, RugbyUnionMatchParams.class);
        // System.out.println(vpMatchParams);
        assertEquals(footballMatchParams, vpMatchParams);
    }

    @Test
    public void testMatchIncident() {
        MethodName.log();
        RugbyUnionMatchIncident footballMatchIncident = new RugbyUnionMatchIncident();
        footballMatchIncident.set(RugbyUnionMatchIncidentType.TRY, 60, TeamId.A);
        String json = JsonUtil.marshalJson(footballMatchIncident, true);
        // System.out.println(json);
        RugbyUnionMatchIncident RugbyUnionMatchIncident2 = JsonUtil.unmarshalJson(json, RugbyUnionMatchIncident.class);
        assertEquals(footballMatchIncident, RugbyUnionMatchIncident2);
    }

    @Test
    public void testMatchState() {
        MethodName.log();
        RugbyUnionMatchFormat matchFormat = new RugbyUnionMatchFormat();
        RugbyUnionMatchState rugbyunionMatchState = new RugbyUnionMatchState(matchFormat);
        String json = JsonUtil.marshalJson(rugbyunionMatchState, true);
        // System.out.println(json);
        RugbyUnionMatchState voMatchState = JsonUtil.unmarshalJson(json, RugbyUnionMatchState.class);
        // System.out.println(voMatchState);
        assertEquals(rugbyunionMatchState, voMatchState);

    }
}
