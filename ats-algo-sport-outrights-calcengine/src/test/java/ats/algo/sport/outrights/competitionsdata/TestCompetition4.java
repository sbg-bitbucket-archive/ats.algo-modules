package ats.algo.sport.outrights.competitionsdata;

import ats.algo.sport.outrights.CompetitionType;
import ats.algo.sport.outrights.calcengine.core.Competition;
import ats.algo.sport.outrights.calcengine.core.Fixture;
import ats.algo.sport.outrights.calcengine.core.FixtureType;
import ats.algo.sport.outrights.calcengine.core.Fixtures;
import ats.algo.sport.outrights.calcengine.core.Team;
import ats.algo.sport.outrights.calcengine.core.Teams;
import ats.algo.sport.outrights.calcengine.leagues.PlayoffsMgr;

/**
 * contains data needed for testing playoffs functionality
 * 
 * @author gicha
 *
 */
public class TestCompetition4 {

    private static Teams initTeams() {
        Teams teams = new Teams();
        teams.put("T1", new Team("T1", "Fulham", "Fulham", 1.4, 1.1));
        teams.put("T2", new Team("T2", "Sheffield Wed.", "Sheffield Wed.", 1.2, 1.6));
        teams.put("T3", new Team("T3", "Preston", "Preston", 1.1, 1.3));
        teams.put("T4", new Team("T4", "Wolverhampton", "Wolverhampton", 1.3, 1.1));
        teams.put("T5", new Team("T5", "Ipswich Town", "Ipswich Town", 0.9, 1.6));
        teams.put("T6", new Team("T6", "Aston Villa", "Aston Villa", 1.3, 1.1));
        teams.put("T7", new Team("T7", "Sheffield United", "Sheffield United", 1.2, 1.3));
        teams.put("T8", new Team("T8", "Hull City", "Hull City", 1.3, 1.6));
        teams.put("T9", new Team("T9", "Sunderland", "Sunderland", 1.1, 1.9));
        teams.put("T10", new Team("T10", "Burton Albion", "Burton Albion", 0.9, 1.8));
        teams.put("T11", new Team("T11", "Cardiff City", "Cardiff City", 1.3, 1.2));
        teams.put("T12", new Team("T12", "Derby County", "Derby County", 1.2, 1.5));
        teams.put("T13", new Team("T13", "Birmingham", "Birmingham", 1.1, 1.6));
        teams.put("T14", new Team("T14", "Norwich City", "Norwich City", 1.1, 1.6));
        teams.put("T15", new Team("T15", "Leeds United", "Leeds United", 1.0, 1.6));
        teams.put("T16", new Team("T16", "Nottm Forest", "Nottm Forest", 1.0, 1.6));
        teams.put("T17", new Team("T17", "Brentford", "Brentford", 1.3, 1.3));
        teams.put("T18", new Team("T18", "QPR", "QPR", 1.2, 1.7));
        teams.put("T19", new Team("T19", "Reading", "Reading", 0.9, 1.9));
        teams.put("T20", new Team("T20", "Middlesbrough", "Middlesbrough", 1.2, 1.2));
        teams.put("T21", new Team("T21", "Bolton", "Bolton", 0.9, 1.8));
        teams.put("T22", new Team("T22", "Millwall", "Millwall", 1.2, 1.3));
        teams.put("T23", new Team("T23", "Barnsley", "Barnsley", 1.0, 1.8));
        teams.put("T24", new Team("T24", "Bristol City", "Bristol City", 1.2, 1.6));
        return teams;
    }

    private static int goals(String id) {
        return Integer.parseInt(id.substring(1));

    }

    private static Fixtures initFixtures(Teams teams) {
        /*
         * start by setting all regular season fixtures to resulted with goals = teamId. should ensure that all markets
         * are resultable at the end of the season
         */
        Fixtures fixtures = new Fixtures();
        int n = 1;
        for (Team teamH : teams.getTeams().values()) {
            for (Team teamA : teams.getTeams().values()) {
                if (teamH.equals(teamA))
                    continue;
                else {
                    String id = "F" + n;
                    fixtures.add(new Fixture("2018-08-03T20:00:00", FixtureType.LEAGUE, id, teamH.getTeamID(),
                                    teamA.getTeamID(), "", false, goals(teamH.getTeamID()), goals(teamA.getTeamID())));
                    n++;
                }
            }
        }
        /*
         * amend one fixture date to be in following year to avoid unnecessary complication with generation of xmas
         * market
         */
        Fixture fixture = fixtures.getByFixtureID("F552");
        fixture.setDate("2019-01-03T20:00:00");
        /*
         * add the set of playoff fixtures
         */
        fixtures.add(new Fixture("2019-05-01T14:30:00", FixtureType.LEAGUE_PLAYOFF_LEG1, PlayoffsMgr.PLAY_OFF1_LEG1,
                        null, null, "6v3", false));
        fixtures.add(new Fixture("2019-05-01T14:30:00", FixtureType.LEAGUE_PLAYOFF_LEG1, PlayoffsMgr.PLAY_OFF2_LEG1,
                        null, null, "5v4", false));
        fixtures.add(Fixture.generateSecondLegFixture("2019-05-01T14:30:00", FixtureType.LEAGUE_PLAYOFF_LEG2,
                        PlayoffsMgr.PLAY_OFF1_LEG2, null, null, "3v6", false, "P1-leg1"));
        fixtures.add(Fixture.generateSecondLegFixture("2019-05-01T14:30:00", FixtureType.LEAGUE_PLAYOFF_LEG2,
                        PlayoffsMgr.PLAY_OFF2_LEG2, null, null, "4v5", false, "P2-leg1"));
        fixtures.add(new Fixture("2019-05-01T14:30:00", FixtureType.LEAGUE_PLAY_OFF_FINAL, PlayoffsMgr.PLAY_OFF_FINAL,
                        null, null, "W1vW2", true));
        return fixtures;
    }

    public static Competition generate() {
        Teams teams = initTeams();
        Fixtures fixtures = initFixtures(teams);
        Competition competition = new Competition(CompetitionType.CHAMPIONSHIP_LEAGUE, "testcompetition2", 888111L,
                        "English Championship League 18/19", "championship", teams, fixtures);
        competition.setReqdInputMarketType("FT:CS");
        return competition;
    }

}
