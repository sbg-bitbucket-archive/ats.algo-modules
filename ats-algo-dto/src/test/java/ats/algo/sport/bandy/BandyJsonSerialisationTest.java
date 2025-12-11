package ats.algo.sport.bandy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.ElapsedTimeMatchIncident;
import ats.algo.core.common.ElapsedTimeMatchIncident.ElapsedTimeMatchIncidentType;
import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.JsonSerializer;
import ats.algo.sport.bandy.BandyMatchIncident.BandyMatchIncidentType;
// import ats.algo.sport.bandy.BandyMatchIncident.BandyMatchEventType;
import ats.core.util.json.JsonUtil;

public class BandyJsonSerialisationTest {

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
    public void testMatchFormatOptions() {
        BandyMatchFormatOptions o1 = new BandyMatchFormatOptions();
        String json = JsonUtil.marshalJson(o1, true);
        System.out.println(json);
        assertTrue(json.contains("normalTimeMinutes"));
    }

    @Test
    public void testMatchFormat() {
        BandyMatchFormat matchFormat1 = new BandyMatchFormat();
        String json = JsonUtil.marshalJson(matchFormat1, true);

        System.out.println("\nFORMAT:\n" + json);

        BandyMatchFormat matchFormat2 = JsonUtil.unmarshalJson(json, BandyMatchFormat.class);
        assertEquals(matchFormat1, matchFormat2);
    }

    @Test
    public void testMatchParams() {
        BandyMatchParams bandyMatchParams = new BandyMatchParams();
        String json = JsonUtil.marshalJson(bandyMatchParams, true);

        System.out.println("\nPARAMS:\n" + json);

        BandyMatchParams vpMatchParams = JsonUtil.unmarshalJson(json, BandyMatchParams.class);
        assertEquals(bandyMatchParams, vpMatchParams);
    }

    @Test
    public void testMatchIncident() {
        BandyMatchIncident bandyMatchIncident = new BandyMatchIncident(BandyMatchIncidentType.GOAL, 30, TeamId.A);
        // bandyMatchIncident.set(BandyMatchIncidentType.AWAY_GOAL);
        String json = JsonUtil.marshalJson(bandyMatchIncident, true);

        System.out.println("\nINCIDENT:\n" + json);

        BandyMatchIncident bandyMatchIncident2 = JsonUtil.unmarshalJson(json, BandyMatchIncident.class);
        assertEquals(bandyMatchIncident, bandyMatchIncident2);
    }

    @Test
    public void testMatchState() {
        BandyMatchState bandyMatchState = new BandyMatchState();
        String json = JsonUtil.marshalJson(bandyMatchState, true);

        System.out.println("\nSTATE:\n" + json);

        BandyMatchState voMatchState = JsonUtil.unmarshalJson(json, BandyMatchState.class);
        assertEquals(bandyMatchState, voMatchState);

    }

    @Test
    public void testSimpleMatchState() {
        BandyMatchFormat matchFormat = new BandyMatchFormat();
        BandyMatchState matchState = new BandyMatchState(matchFormat);
        MatchState sms = matchState.generateSimpleMatchState(3);
        String json = JsonSerializer.serialize(sms, MatchState.class, true);
        System.out.println("\nSTATE:\n" + json);
        MatchState sms2 = JsonSerializer.deserialize(json, MatchState.class);
        assertEquals(sms, sms2);
    }

    @SuppressWarnings("unused")
    @Test
    public void testWrappedFormatJson() {
        MatchDetails resp = new MatchDetails();
        resp.setSport("BANDY");
        BandyMatchFormat format = new BandyMatchFormat();
        format.setNormalTimeMinutes(90);
        format.setPenaltiesPossible(true);
        resp.setFormat(format);
        String json = JsonUtil.marshalJson(resp);

        System.out.println(json);

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
