package ats.algo.sport.rugbyleague;

import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.MethodName;
import ats.algo.sport.rugbyleague.RugbyLeagueMatchIncident.RugbyLeagueMatchIncidentType;
import ats.core.util.json.JsonUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RugbyLeagueJsonSerialisationTest {

    @Test
    public void testMatchFormat() {
        MethodName.log();
        RugbyLeagueMatchFormat matchFormat1 = new RugbyLeagueMatchFormat();
        String json = JsonUtil.marshalJson(matchFormat1, true);
        RugbyLeagueMatchFormat matchFormat2 = JsonUtil.unmarshalJson(json, RugbyLeagueMatchFormat.class);
        assertEquals(matchFormat1, matchFormat2);
    }


    @Test
    public void testMatchParams() {
        MethodName.log();
        RugbyLeagueMatchParams footballMatchParams = new RugbyLeagueMatchParams();
        String json = JsonUtil.marshalJson(footballMatchParams, true);
        // System.out.println(json);
        RugbyLeagueMatchParams vpMatchParams = JsonUtil.unmarshalJson(json, RugbyLeagueMatchParams.class);
        // System.out.println(vpMatchParams);
        assertEquals(footballMatchParams, vpMatchParams);
    }

    @Test
    public void testMatchIncident() {
        MethodName.log();
        RugbyLeagueMatchIncident footballMatchIncident = new RugbyLeagueMatchIncident();
        footballMatchIncident.set(RugbyLeagueMatchIncidentType.TRY, 60, TeamId.A);
        String json = JsonUtil.marshalJson(footballMatchIncident, true);
        // System.out.println(json);
        RugbyLeagueMatchIncident RugbyLeagueMatchIncident2 =
                        JsonUtil.unmarshalJson(json, RugbyLeagueMatchIncident.class);
        assertEquals(footballMatchIncident, RugbyLeagueMatchIncident2);
    }

    @Test
    public void testMatchState() {
        MethodName.log();
        RugbyLeagueMatchFormat matchFormat = new RugbyLeagueMatchFormat();
        RugbyLeagueMatchState rugbyLeagueMatchState = new RugbyLeagueMatchState(matchFormat);
        String json = JsonUtil.marshalJson(rugbyLeagueMatchState, true);
        // System.out.println(json);
        RugbyLeagueMatchState voMatchState = JsonUtil.unmarshalJson(json, RugbyLeagueMatchState.class);
        // System.out.println(voMatchState);
        assertEquals(rugbyLeagueMatchState, voMatchState);

    }
}
