package ats.algo.sport.fieldhockey;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.ElapsedTimeMatchIncident;
import ats.algo.core.common.ElapsedTimeMatchIncident.ElapsedTimeMatchIncidentType;
import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.JsonSerializer;
import ats.algo.sport.fieldhockey.FieldhockeyMatchIncident.FieldhockeyMatchIncidentType;
// import ats.algo.sport.fieldhockey.FieldhockeyMatchIncident.FieldhockeyMatchEventType;
import ats.core.util.json.JsonUtil;

public class FieldhockeyJsonSerialisationTest {

    @Test
    public void testElapsedTime() {
        ElapsedTimeMatchIncident clock1 =
                        new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_MATCH_CLOCK, 30);
        clock1.setIncidentId("CLK-01");
        String json = JsonUtil.marshalJson(clock1, true);
        System.out.println("\nCLOCK:\n" + json);

        ElapsedTimeMatchIncident clock2 = JsonUtil.unmarshalJson(json, ElapsedTimeMatchIncident.class);
        assertEquals(clock1, clock2);
    }

    @Test
    public void testMatchFormat() {
        FieldhockeyMatchFormat matchFormat1 = new FieldhockeyMatchFormat();
        String json = JsonUtil.marshalJson(matchFormat1, true);

        System.out.println("\nFORMAT:\n" + json);

        FieldhockeyMatchFormat matchFormat2 = JsonUtil.unmarshalJson(json, FieldhockeyMatchFormat.class);
        assertEquals(matchFormat1, matchFormat2);
    }

    @Test
    public void testMatchParams() {
        FieldhockeyMatchParams fieldhockeyMatchParams = new FieldhockeyMatchParams();
        String json = JsonUtil.marshalJson(fieldhockeyMatchParams, true);

        System.out.println("\nPARAMS:\n" + json);

        FieldhockeyMatchParams vpMatchParams = JsonUtil.unmarshalJson(json, FieldhockeyMatchParams.class);
        assertEquals(fieldhockeyMatchParams, vpMatchParams);
    }

    @Test
    public void testMatchIncident() {
        FieldhockeyMatchIncident fieldhockeyMatchIncident =
                        new FieldhockeyMatchIncident(FieldhockeyMatchIncidentType.GOAL, 30, TeamId.A);
        // fieldhockeyMatchIncident.set(FieldhockeyMatchIncidentType.AWAY_GOAL);
        String json = JsonUtil.marshalJson(fieldhockeyMatchIncident, true);

        System.out.println("\nINCIDENT:\n" + json);

        FieldhockeyMatchIncident fieldhockeyMatchIncident2 =
                        JsonUtil.unmarshalJson(json, FieldhockeyMatchIncident.class);
        assertEquals(fieldhockeyMatchIncident, fieldhockeyMatchIncident2);
    }

    @Test
    public void testMatchState() {
        FieldhockeyMatchState fieldhockeyMatchState = new FieldhockeyMatchState();
        String json = JsonUtil.marshalJson(fieldhockeyMatchState, true);

        System.out.println("\nSTATE:\n" + json);

        FieldhockeyMatchState voMatchState = JsonUtil.unmarshalJson(json, FieldhockeyMatchState.class);
        assertEquals(fieldhockeyMatchState, voMatchState);

    }

    @Test
    public void testSimpleMatchState() {
        FieldhockeyMatchFormat matchFormat = new FieldhockeyMatchFormat();
        FieldhockeyMatchState matchState = new FieldhockeyMatchState(matchFormat);
        MatchState sms = matchState.generateSimpleMatchState();
        String json = JsonSerializer.serialize(sms, true);
        System.out.println("\nSTATE:\n" + json);
        MatchState sms2 = JsonSerializer.deserialize(json, FieldhockeySimpleMatchState.class);
        assertEquals(sms, sms2);
    }

    @Test
    public void testWrappedFormatJson() {
        MatchDetails resp = new MatchDetails();
        resp.setSport("ICE_HOCKEY");
        FieldhockeyMatchFormat format = new FieldhockeyMatchFormat();
        format.setNormalTimeMinutes(90);
        format.setPenaltiesPossible(true);
        resp.setFormat(format);
        String json = JsonUtil.marshalJson(resp);

        System.out.println(json);

        @SuppressWarnings("unused")
        MatchDetails matchDetails = JsonUtil.unmarshalJson(json, MatchDetails.class);

        System.out.println(json);
    }


    private static class MatchDetails {
        private String sport;
        private Object format;

        @SuppressWarnings("unused")
        public String getSport() {
            return sport;
        }

        public void setSport(String sport) {
            this.sport = sport;
        }

        @SuppressWarnings("unused")
        public Object getFormat() {
            return format;
        }

        public void setFormat(Object format) {
            this.format = format;
        }
    }
}
