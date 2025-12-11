package ats.algo.sport.football.goalscorer;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.fail;

import org.junit.Test;

import ats.algo.core.common.PlayerMatchIncident;
import ats.algo.core.common.PlayerStatus;
import ats.algo.core.common.TeamId;
import ats.algo.core.common.TeamSheet;
import ats.algo.core.common.TeamSheetMatchIncident;
import ats.algo.sport.football.ExampleTeamSheets;
import ats.algo.sport.football.FootballMatchIncident;
import ats.algo.sport.football.FootballMatchIncident.FootballMatchIncidentType;
import ats.core.util.json.JsonUtil;


public class MatchIncidentsToJsonTest {


    private TeamSheet testTeamSheetChange() {
        TeamSheet teamSheet = new TeamSheet();
        teamSheet.addPlayer(TeamId.A, "Aiden O'Neill", PlayerStatus.SUBSTITUTED);
        teamSheet.addPlayer(TeamId.B, "Steven Defour", PlayerStatus.PLAYING);
        return teamSheet;
    }


    @Test
    public void test() {
        MethodName.log();
        TeamSheetMatchIncident incident = TeamSheetMatchIncident
                        .generateMatchIncidentForInitialTeamSheet(ExampleTeamSheets.getExampleTeamSheet());
        try {
            @SuppressWarnings("unused")
            String json = JsonUtil.marshalJson(incident, true);
            // System.out.println(json);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void test2() {
        MethodName.log();
        TeamSheetMatchIncident incident =
                        TeamSheetMatchIncident.generateMatchIncidentForUpdateTeamSheet(testTeamSheetChange());
        try {
            @SuppressWarnings("unused")
            String json = JsonUtil.marshalJson(incident, true);
            // System.out.println(json);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testFootballMatchIncident() {
        MethodName.log();
        FootballMatchIncident incident = new FootballMatchIncident(FootballMatchIncidentType.CORNER, 0, TeamId.A);
        try {
            @SuppressWarnings("unused")
            String json = JsonUtil.marshalJson(incident, true);
            // System.out.println(json);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void test3() {
        MethodName.log();
        PlayerMatchIncident incident =
                        PlayerMatchIncident.generateMatchIncidentForGoalScorer(1, TeamId.A, "George Best");
        try {
            @SuppressWarnings("unused")
            String json = JsonUtil.marshalJson(incident, true);
            // System.out.println(json);
        } catch (Exception e) {
            fail();
        }
    }


}
