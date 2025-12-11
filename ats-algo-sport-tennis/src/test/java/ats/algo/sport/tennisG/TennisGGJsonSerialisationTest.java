package ats.algo.sport.tennisG;

import ats.algo.genericsupportfunctions.MethodName;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

import ats.algo.core.common.TeamId;
import ats.algo.sport.tennisG.TennisGMatchFormat.*;
import ats.core.util.json.JsonUtil;

public class TennisGGJsonSerialisationTest {

    @Test
    public void testMatchFormat() {
        MethodName.log();
        TennisGMatchFormat matchFormat1 =
                        new TennisGMatchFormat(Sex.WOMEN, Surface.CLAY, TournamentLevel.CHALLENGER, 3, false);
        String json = JsonUtil.marshalJson(matchFormat1, true);
        TennisGMatchFormat matchFormat2 = JsonUtil.unmarshalJson(json, TennisGMatchFormat.class);
        assertEquals(matchFormat1, matchFormat2);
    }

    @Test
    public void testMatchParams() {
        MethodName.log();
        TennisGMatchParams matchParams1 = new TennisGMatchParams();
        String json = JsonUtil.marshalJson(matchParams1, true);
        TennisGMatchParams matchParams2 = JsonUtil.unmarshalJson(json, TennisGMatchParams.class);

        // String json1 = "{"requestId":"TENS_5765","eventId":4147353,"type":"MATCH_STARTING","
        // serverSideAtStartOfMatch":"A"," serverPlayerAtStartOfMatch ":"A"}";

        assertEquals(matchParams1, matchParams2);
    }

    @Test
    @Ignore
    public void testMatchIncident() {
        MethodName.log();
        TennisGMatchIncident incident1 =
                        new TennisGMatchIncident(0, TennisGMatchIncident.TennisMatchIncidentType.POINT_WON, TeamId.A);
        String json = JsonUtil.marshalJson(incident1, true);
        TennisGMatchIncident incident2 = JsonUtil.unmarshalJson(json, TennisGMatchIncident.class);
        assertEquals(incident1, incident2);
    }

    @Test
    public void testMatchState() {
        MethodName.log();
        TennisGMatchFormat matchFormat =
                        new TennisGMatchFormat(Sex.WOMEN, Surface.CLAY, TournamentLevel.CHALLENGER, 3, false);;
        TennisGMatchState matchState1 = new TennisGMatchState(matchFormat);
        matchState1.setScore(1, 0, 1, 2, TeamId.A);


        String json = JsonUtil.marshalJson(matchState1, true);
        // System.out.println(json);
        TennisGMatchState matchState2 = JsonUtil.unmarshalJson(json, TennisGMatchState.class);
        assertEquals(matchState1, matchState2);
    }
}
