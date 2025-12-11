package ats.algo.sport.americanfootball;

import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.JsonSerializer;
import ats.algo.sport.americanfootball.AmericanfootballMatchIncident.AmericanfootballMatchIncidentType;
import ats.core.util.json.JsonUtil;

public class AmericanfootballJsonSerialisationTest {

    @Test
    public void testMatchFormat() {
        AmericanfootballMatchFormat matchFormat1 = new AmericanfootballMatchFormat();
        String json = JsonUtil.marshalJson(matchFormat1, true);
        AmericanfootballMatchFormat matchFormat2 = JsonUtil.unmarshalJson(json, AmericanfootballMatchFormat.class);
        assertEquals(matchFormat1, matchFormat2);
    }

    @Test
    public void testMatchFormatOptions() {
        AmericanfootballMatchFormatOptions o1 = new AmericanfootballMatchFormatOptions();
        String json = JsonUtil.marshalJson(o1, true);
        System.out.println(json);
        assertTrue(json.contains("normalTimeMinutes"));
    }

    @Test
    public void testMatchParams() {
        AmericanfootballMatchParams footballMatchParams = new AmericanfootballMatchParams();
        String json = JsonUtil.marshalJson(footballMatchParams, true);
        System.out.println(json);
        AmericanfootballMatchParams vpMatchParams = JsonUtil.unmarshalJson(json, AmericanfootballMatchParams.class);
        System.out.println(vpMatchParams);
        assertEquals(footballMatchParams, vpMatchParams);
    }

    @Test
    public void testMatchIncident() {
        AmericanfootballMatchIncident footballMatchIncident = new AmericanfootballMatchIncident();
        String json = JsonUtil.marshalJson(footballMatchIncident, true);
        AmericanfootballMatchIncident AmericanfootballMatchIncident2 =
                        JsonUtil.unmarshalJson(json, AmericanfootballMatchIncident.class);
        assertEquals(footballMatchIncident, AmericanfootballMatchIncident2);

    }


    @Test
    public void testMatchIncidentCombo() {
        AmericanfootballMatchIncident incident1 = new AmericanfootballMatchIncident(TeamId.A.toString(),
                        TeamId.B.toString(), 2, 2, AmericanfootballMatchIncidentType.COMBO_SETTING, 0);
        String json = JsonUtil.marshalJson(incident1, true);
        System.out.println(json);
        AmericanfootballMatchIncident incident2 = JsonUtil.unmarshalJson(json, AmericanfootballMatchIncident.class);
        assertEquals(incident1, incident2);
    }

    @Test
    public void testMatchState() {
        AmericanfootballMatchFormat matchFormat = new AmericanfootballMatchFormat();
        AmericanfootballMatchState AmericanfootballMatchState = new AmericanfootballMatchState(matchFormat);
        String json = JsonUtil.marshalJson(AmericanfootballMatchState, true);
        System.out.println(json);
        AmericanfootballMatchState voMatchState = JsonUtil.unmarshalJson(json, AmericanfootballMatchState.class);
        System.out.println(voMatchState);
        assertEquals(AmericanfootballMatchState, voMatchState);

    }

    @Test
    public void testSimpleMatchState() {
        AmericanfootballMatchFormat matchFormat = new AmericanfootballMatchFormat();
        AmericanfootballMatchState matchState = new AmericanfootballMatchState(matchFormat);
        MatchState sms = matchState.generateSimpleMatchState();
        String json = JsonSerializer.serialize(sms, true);
        System.out.println("\nSTATE:\n" + json);
        MatchState sms2 = JsonSerializer.deserialize(json, AmericanfootballSimpleMatchState.class);
        assertEquals(sms, sms2);
    }
}
