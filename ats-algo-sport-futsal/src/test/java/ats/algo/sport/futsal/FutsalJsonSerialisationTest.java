package ats.algo.sport.futsal;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ats.algo.core.common.ElapsedTimeMatchIncident;
import ats.algo.core.common.ElapsedTimeMatchIncident.ElapsedTimeMatchIncidentType;
import ats.core.util.json.JsonUtil;

public class FutsalJsonSerialisationTest {

    @Test
    public void testElapsedTime() {
        MethodName.log();
        ElapsedTimeMatchIncident clock1 =
                        new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_MATCH_CLOCK, 30);
        clock1.setIncidentId("CLK-01");
        String json = JsonUtil.marshalJson(clock1, true);
        // System.out.println("\nCLOCK:\n" + json);

        ElapsedTimeMatchIncident clock2 = JsonUtil.unmarshalJson(json, ElapsedTimeMatchIncident.class);
        assertEquals(clock1, clock2);
    }

    @Test
    public void testMatchParams() {
        MethodName.log();
        FutsalMatchParams futsalMatchParams = new FutsalMatchParams();
        String json = JsonUtil.marshalJson(futsalMatchParams, true);

        // System.out.println("\nPARAMS:\n" + json);

        FutsalMatchParams vpMatchParams = JsonUtil.unmarshalJson(json, FutsalMatchParams.class);
        assertEquals(futsalMatchParams, vpMatchParams);
    }

    @Test
    public void testMatchState() {
        MethodName.log();
        FutsalMatchState futsalMatchState = new FutsalMatchState();
        String json = JsonUtil.marshalJson(futsalMatchState, true);

        // System.out.println("\nSTATE:\n" + json);

        FutsalMatchState voMatchState = JsonUtil.unmarshalJson(json, FutsalMatchState.class);
        assertEquals(futsalMatchState, voMatchState);

    }

    @Test
    public void testWrappedFormatJson() {
        MethodName.log();
        MatchDetails resp = new MatchDetails();
        resp.setSport("FUTSAL");
        FutsalMatchFormat format = new FutsalMatchFormat();
        format.setNormalTimeMinutes(40);
        format.setExtraTimeMinutes(5);
        format.setPenaltiesPossible(true);
        resp.setFormat(format);
        String json = JsonUtil.marshalJson(resp);

        // System.out.println(json);

        @SuppressWarnings("unused")
        MatchDetails matchDetails = JsonUtil.unmarshalJson(json, MatchDetails.class);

        // System.out.println(json);
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
