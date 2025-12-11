package ats.algo.sport.basketball;

import static org.junit.Assert.*;
import org.junit.Test;

import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.ElapsedTimeMatchIncident;
import ats.algo.core.common.TeamId;
import ats.algo.core.common.ElapsedTimeMatchIncident.ElapsedTimeMatchIncidentType;
import ats.algo.genericsupportfunctions.JsonSerializer;
import ats.algo.sport.basketball.BasketballMatchIncident.BasketballMatchIncidentType;
// import ats.algo.sport.basketball.BasketballMatchIncident.BasketballMatchEventType;
import ats.core.util.json.JsonUtil;

public class BasketballJsonSerialisationTest {

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
        BasketballMatchFormat matchFormat1 = new BasketballMatchFormat();
        String json = JsonUtil.marshalJson(matchFormat1, true);

        System.out.println("\nFORMAT:\n" + json);

        BasketballMatchFormat matchFormat2 = JsonUtil.unmarshalJson(json, BasketballMatchFormat.class);
        assertEquals(matchFormat1, matchFormat2);
    }

    @Test
    public void testMatchParams() {
        BasketballMatchParams basketballMatchParams = new BasketballMatchParams();
        String json = JsonUtil.marshalJson(basketballMatchParams, true);

        System.out.println("\nPARAMS:\n" + json);

        BasketballMatchParams vpMatchParams = JsonUtil.unmarshalJson(json, BasketballMatchParams.class);
        assertEquals(basketballMatchParams, vpMatchParams);
    }

    @Test
    public void testMatchIncident() {
        BasketballMatchIncident basketballMatchIncident =
                        new BasketballMatchIncident(BasketballMatchIncidentType.TWO_POINTS_SCORED, 30, TeamId.A);
        // basketballMatchIncident.set(BasketballMatchIncidentType.AWAY_GOAL);
        String json = JsonUtil.marshalJson(basketballMatchIncident, true);

        System.out.println("\nINCIDENT:\n" + json);

        BasketballMatchIncident basketballMatchIncident2 = JsonUtil.unmarshalJson(json, BasketballMatchIncident.class);
        assertEquals(basketballMatchIncident, basketballMatchIncident2);
    }

    @Test
    public void testMatchState() {
        BasketballMatchState basketballMatchState = new BasketballMatchState();
        String json = JsonUtil.marshalJson(basketballMatchState, true);

        System.out.println("\nSTATE:\n" + json);

        BasketballMatchState voMatchState = JsonUtil.unmarshalJson(json, BasketballMatchState.class);
        assertEquals(basketballMatchState, voMatchState);
    }



    @Test
    public void testWrappedFormatJson() {
        MatchDetails resp = new MatchDetails();
        resp.setSport("BASKETBALL");
        BasketballMatchFormat format = new BasketballMatchFormat();
        format.setNormalTimeMinutes(40);
        format.setExtraTimeMinutes(5);
        format.setTwoHalvesFormat(false);
        resp.setFormat(format);
        String json = JsonUtil.marshalJson(resp);

        System.out.println(json);

        @SuppressWarnings("unused")
        MatchDetails matchDetails = JsonUtil.unmarshalJson(json, MatchDetails.class);

        System.out.println(json);
    }

    @Test
    public void testSimpleMatchState() {
        BasketballMatchFormat matchFormat = new BasketballMatchFormat();
        BasketballMatchState matchState = new BasketballMatchState(matchFormat);
        MatchState sms = matchState.generateSimpleMatchState();
        String json = JsonSerializer.serialize(sms, true);
        System.out.println("\nSTATE:\n" + json);
        MatchState sms2 = JsonSerializer.deserialize(json, BasketballSimpleMatchState.class);
        assertEquals(sms, sms2);
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
