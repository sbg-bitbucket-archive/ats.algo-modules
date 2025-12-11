package ats.algo.sport.football;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchIncidentResult;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.ElapsedTimeMatchIncident;
import ats.algo.core.common.TeamId;
import ats.algo.core.common.TeamSheet;
import ats.algo.core.matchresult.MatchResultMap;
import ats.algo.core.common.ElapsedTimeMatchIncident.ElapsedTimeMatchIncidentType;
import ats.algo.core.common.PlayerMatchIncident.PlayerMatchIncidentType;
import ats.algo.core.common.PlayerMatchIncident;
import ats.algo.genericsupportfunctions.JsonSerializer;
import ats.algo.sport.football.FootballMatchIncident.FootballMatchIncidentType;
import ats.core.util.json.JsonUtil;

public class FootballJsonSerialisationTest {

    @Test
    public void testMatchFormat() {
        FootballMatchFormat matchFormat1 = new FootballMatchFormat();
        String json = JsonUtil.marshalJson(matchFormat1, true);
        System.out.println(json);
        FootballMatchFormat matchFormat2 = JsonUtil.unmarshalJson(json, FootballMatchFormat.class);
        assertEquals(matchFormat1, matchFormat2);
    }

    @Test
    public void testMatchResultMap() {
        FootballMatchFormat matchFormat = new FootballMatchFormat();
        MatchResultMap map1 = matchFormat.generateMatchResultProForma();
        String json = JsonUtil.marshalJson(map1, true);
        System.out.println(json);
        MatchResultMap map2 = JsonUtil.unmarshalJson(json, MatchResultMap.class);
        assertEquals(map1, map2);
    }

    @Test
    public void testMatchFormatOptions() {
        FootballMatchFormatOptions o1 = new FootballMatchFormatOptions();
        String json = JsonUtil.marshalJson(o1, true);
        System.out.println(json);
        assertTrue(json.contains("5:5"));
    }
    // /*
    // * this test is no longer relevant - not serializing xxxMatchParams
    // */
    // @Test
    // public void testMatchParams() {
    // FootballMatchParams footballMatchParams = new FootballMatchParams();
    // String json = JsonUtil.marshalJson(footballMatchParams, true);
    // System.out.println(json);
    // FootballMatchParams vpMatchParams = JsonUtil.unmarshalJson(json, FootballMatchParams.class);
    // System.out.println(footballMatchParams.toString());
    // System.out.println(vpMatchParams.toString());
    // boolean x = footballMatchParams.equals(vpMatchParams);
    // assertTrue(x);
    // assertEquals(footballMatchParams, vpMatchParams);
    // }

    @Test
    public void testMatchIncident() {
        FootballMatchIncident o = new FootballMatchIncident();
        o.setIncidentSubType(FootballMatchIncidentType.GOAL);
        o.setElapsedTime(257);
        o.setTeamId(TeamId.A);
        FootballMatchStateFromFeed feedState =
                        new FootballMatchStateFromFeed(4, 3, 2, 1, 4, 5, 6, 7, FootballMatchPeriod.IN_FIRST_HALF);
        o.setFootballMatchStateFromFeed(feedState);
        String json = JsonUtil.marshalJson(o, true);
        System.out.println(json);
        MatchIncident o2 = JsonUtil.unmarshalJson(json, FootballMatchIncident.class);
        assertEquals(o, o2);
    }

    @Test
    public void testMatchIncident2() {
        PlayerMatchIncident o = PlayerMatchIncident.generateMatchIncidentForGoalScorer(1, TeamId.A, "B.Player1");
        o.setIncidentSubType(PlayerMatchIncidentType.GOAL_SCORER);
        o.setEventId(4841013);
        o.setIncidentId("FOOTBALL_4589");
        String json = JsonUtil.marshalJson(o, true);
        System.out.println(json);
        PlayerMatchIncident o2 = JsonUtil.unmarshalJson(json, PlayerMatchIncident.class);
        assertEquals(o, o2);
    }

    @Test
    public void testMatchState() {
        // FIXME when MatchState is completed, add the test
        FootballMatchFormat matchFormat = new FootballMatchFormat();
        FootballMatchState matchState = new FootballMatchState(matchFormat);
        String json = JsonUtil.marshalJson(matchState, true);

        System.out.println("\nSTATE:\n" + json);

        MatchState matchState2 = JsonUtil.unmarshalJson(json, FootballMatchState.class);
        assertEquals(matchState, matchState2);

    }

    @Test
    public void testMatchIncidentResult() {
        ElapsedTimeMatchIncident incident =
                        new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
        FootballMatchFormat matchFormat = new FootballMatchFormat();
        FootballMatchState matchState = new FootballMatchState(matchFormat);
        MatchIncidentResult result = matchState.updateStateForIncident(incident, true);
        String json = JsonUtil.marshalJson(result, true);
        System.out.println("\nSTATE:\n" + json);
    }

    @Test
    public void testSimpleMatchState() {
        FootballMatchFormat matchFormat = new FootballMatchFormat();
        FootballMatchState matchState = new FootballMatchState(matchFormat);
        MatchState sms = matchState.generateSimpleMatchState(3);
        String json = JsonSerializer.serialize(sms, MatchState.class, true);
        System.out.println("\nSTATE:\n" + json);
        MatchState sms2 = JsonSerializer.deserialize(json, MatchState.class);
        // System.err.println(sms2);
        assertEquals(sms, sms2);
    }

    @Test
    public void testSimpleMatchState2() {
        Map<String, String> goalScorers = new HashMap<String, String>();
        goalScorers.put("goal1", "A.George Best");
        goalScorers.put("goal2", "B.Wayne Rooney");
        TeamSheet teamSheet = ExampleTeamSheets.getExampleTeamSheet();
        List<FootballMatchIncidentType> shootoutListStatusA = new ArrayList<FootballMatchIncidentType>();
        shootoutListStatusA.add(FootballMatchIncidentType.GOAL);
        shootoutListStatusA.add(FootballMatchIncidentType.SHOOTOUT_MISS);
        shootoutListStatusA.add(FootballMatchIncidentType.GOAL);

        List<FootballMatchIncidentType> shootoutListStatusB = new ArrayList<FootballMatchIncidentType>();
        shootoutListStatusB.add(FootballMatchIncidentType.GOAL);
        shootoutListStatusB.add(FootballMatchIncidentType.GOAL);
        shootoutListStatusB.add(FootballMatchIncidentType.SHOOTOUT_MISS);
        FootballShootoutInfo shootoutInfo =
                        new FootballShootoutInfo(2, 0, TeamId.A, 3, false, shootoutListStatusA, shootoutListStatusB);

        MatchState sms = new FootballSimpleMatchState(false, false, true, FootballMatchPeriod.IN_FIRST_HALF, 374, 1, 0,
                        2, 1, 1, 0, 1, 0, 1, 0, 0, 0, goalScorers, teamSheet, shootoutInfo, false);
        String json = JsonSerializer.serialize(sms, MatchState.class, true);
        System.out.println("\nSTATE:\n" + json);
        MatchState sms2 = JsonSerializer.deserialize(json, MatchState.class);
        System.out.println(sms2);
        assertEquals(sms, sms2);
    }

    @Test
    public void testFootballMatchPeriod() {
        FootballMatchPeriod p = FootballMatchPeriod.AT_FULL_TIME;

        String json = JsonUtil.marshalJson(p);
        System.out.println(json);
        // String json = JsonSerializer.serialize(p, FootballMatchPeriod.class, true);
        FootballMatchIncidentType i = FootballMatchIncidentType.CORNER;
        String json2 = JsonUtil.marshalJson(i);
        System.out.println(json2);
    }


}
