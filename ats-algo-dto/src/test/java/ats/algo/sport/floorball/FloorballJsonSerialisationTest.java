package ats.algo.sport.floorball;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.ElapsedTimeMatchIncident;
import ats.algo.core.common.ElapsedTimeMatchIncident.ElapsedTimeMatchIncidentType;
import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.JsonSerializer;
import ats.algo.sport.floorball.FloorballMatchIncident.FloorballMatchIncidentType;
// import ats.algo.sport.floorball.FloorballMatchIncident.FloorballMatchEventType;
import ats.core.util.json.JsonUtil;

public class FloorballJsonSerialisationTest {

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
        FloorballMatchFormat matchFormat1 = new FloorballMatchFormat();
        String json = JsonUtil.marshalJson(matchFormat1, true);

        System.out.println("\nFORMAT:\n" + json);

        FloorballMatchFormat matchFormat2 = JsonUtil.unmarshalJson(json, FloorballMatchFormat.class);
        assertEquals(matchFormat1, matchFormat2);
    }

    @Test
    public void testMatchParams() {
        FloorballMatchParams floorballMatchParams = new FloorballMatchParams();
        String json = JsonUtil.marshalJson(floorballMatchParams, true);

        System.out.println("\nPARAMS:\n" + json);

        FloorballMatchParams vpMatchParams = JsonUtil.unmarshalJson(json, FloorballMatchParams.class);
        assertEquals(floorballMatchParams, vpMatchParams);
    }

    @Test
    public void testMatchIncident() {
        FloorballMatchIncident floorballMatchIncident =
                        new FloorballMatchIncident(FloorballMatchIncidentType.GOAL, 30, TeamId.A);
        floorballMatchIncident.setSourceSystem("FLOORBALL_WORLD");
        floorballMatchIncident.setExternalEventId("FB-1:111");

        // floorballMatchIncident.set(FloorballMatchIncidentType.AWAY_GOAL);
        String json = JsonUtil.marshalJson(floorballMatchIncident, true);

        System.out.println("\nINCIDENT:\n" + json);

        FloorballMatchIncident floorballMatchIncident2 = JsonUtil.unmarshalJson(json, FloorballMatchIncident.class);
        assertEquals(floorballMatchIncident, floorballMatchIncident2);
    }

    @Test
    public void testMatchState() {
        FloorballMatchState floorballMatchState = new FloorballMatchState();
        String json = JsonUtil.marshalJson(floorballMatchState, true);

        System.out.println("\nSTATE:\n" + json);

        FloorballMatchState voMatchState = JsonUtil.unmarshalJson(json, FloorballMatchState.class);
        assertEquals(floorballMatchState, voMatchState);

    }

    @Test
    public void testSimpleMatchState() {
        FloorballMatchFormat matchFormat = new FloorballMatchFormat();
        FloorballMatchState matchState = new FloorballMatchState(matchFormat);
        MatchState sms = matchState.generateSimpleMatchState(3);
        String json = JsonSerializer.serialize(sms, MatchState.class, true);
        System.out.println("\nSTATE:\n" + json);
        MatchState sms2 = JsonSerializer.deserialize(json, MatchState.class);
        assertEquals(sms, sms2);
    }

    @Test
    public void testWrappedFormatJson() {
        MatchDetails resp = new MatchDetails();
        resp.setSport("FLOORBALL");
        FloorballMatchFormat format = new FloorballMatchFormat();
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
