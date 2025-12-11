package ats.algo.sport.icehockey;

import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.ElapsedTimeMatchIncident;
import ats.algo.core.common.TeamId;
import ats.algo.core.common.ElapsedTimeMatchIncident.ElapsedTimeMatchIncidentType;
import ats.algo.genericsupportfunctions.JsonSerializer;
import ats.algo.sport.icehockey.IcehockeyMatchIncident.IcehockeyMatchIncidentType;
// import ats.algo.sport.icehockey.IcehockeyMatchIncident.IcehockeyMatchEventType;
import ats.core.util.json.JsonUtil;

public class IcehockeyJsonSerialisationTest {

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
        IcehockeyMatchFormat matchFormat1 = new IcehockeyMatchFormat();
        String json = JsonUtil.marshalJson(matchFormat1, true);

        System.out.println("\nFORMAT:\n" + json);

        IcehockeyMatchFormat matchFormat2 = JsonUtil.unmarshalJson(json, IcehockeyMatchFormat.class);
        assertEquals(matchFormat1, matchFormat2);
    }

    @Test
    public void testMatchParams() {
        IcehockeyMatchParams icehockeyMatchParams = new IcehockeyMatchParams();
        String json = JsonUtil.marshalJson(icehockeyMatchParams, true);

        System.out.println("\nPARAMS:\n" + json);

        IcehockeyMatchParams vpMatchParams = JsonUtil.unmarshalJson(json, IcehockeyMatchParams.class);
        assertEquals(icehockeyMatchParams, vpMatchParams);
    }

    @Test
    public void testMatchIncident() {
        IcehockeyMatchIncident icehockeyMatchIncident =
                        new IcehockeyMatchIncident(IcehockeyMatchIncidentType.GOAL, 30, TeamId.A);
        // icehockeyMatchIncident.set(IcehockeyMatchIncidentType.AWAY_GOAL);
        String json = JsonUtil.marshalJson(icehockeyMatchIncident, true);

        System.out.println("\nINCIDENT:\n" + json);

        IcehockeyMatchIncident icehockeyMatchIncident2 = JsonUtil.unmarshalJson(json, IcehockeyMatchIncident.class);
        assertEquals(icehockeyMatchIncident, icehockeyMatchIncident2);
    }

    @Test
    public void testMatchState() {
        IcehockeyMatchState icehockeyMatchState = new IcehockeyMatchState();
        String json = JsonUtil.marshalJson(icehockeyMatchState, true);

        System.out.println("\nSTATE:\n" + json);

        IcehockeyMatchState voMatchState = JsonUtil.unmarshalJson(json, IcehockeyMatchState.class);
        assertEquals(icehockeyMatchState, voMatchState);

    }

    @Test
    public void testSimpleMatchState() {
        IcehockeyMatchFormat matchFormat = new IcehockeyMatchFormat();
        IcehockeyMatchState matchState = new IcehockeyMatchState(matchFormat);
        MatchState sms = matchState.generateSimpleMatchState();
        String json = JsonSerializer.serialize(sms, true);
        System.out.println("\nSTATE:\n" + json);
        MatchState sms2 = JsonSerializer.deserialize(json, IcehockeySimpleMatchState.class);
        assertEquals(sms, sms2);
    }


    @Test
    public void testWrappedFormatJson() {
        MatchDetails resp = new MatchDetails();
        resp.setSport("ICE_HOCKEY");
        IcehockeyMatchFormat format = new IcehockeyMatchFormat();
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
