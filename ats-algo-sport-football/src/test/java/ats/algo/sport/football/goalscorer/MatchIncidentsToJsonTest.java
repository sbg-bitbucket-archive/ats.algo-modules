package ats.algo.sport.football.goalscorer;

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
        TeamSheetMatchIncident incident = TeamSheetMatchIncident
                        .generateMatchIncidentForInitialTeamSheet(ExampleTeamSheets.getExampleTeamSheet());
        String json = JsonUtil.marshalJson(incident, true);
        System.out.println(json);
    }

    @Test
    public void test2() {
        TeamSheetMatchIncident incident =
                        TeamSheetMatchIncident.generateMatchIncidentForUpdateTeamSheet(testTeamSheetChange());
        String json = JsonUtil.marshalJson(incident, true);
        System.out.println(json);
    }

    @Test
    public void testFootballMatchIncident() {
        FootballMatchIncident incident = new FootballMatchIncident(FootballMatchIncidentType.CORNER, 0, TeamId.A);
        String json = JsonUtil.marshalJson(incident, true);
        System.out.println(json);
    }

    @Test
    public void test3() {
        PlayerMatchIncident incident =
                        PlayerMatchIncident.generateMatchIncidentForGoalScorer(1, TeamId.A, "George Best");
        String json = JsonUtil.marshalJson(incident, true);
        System.out.println(json);
    }


}
