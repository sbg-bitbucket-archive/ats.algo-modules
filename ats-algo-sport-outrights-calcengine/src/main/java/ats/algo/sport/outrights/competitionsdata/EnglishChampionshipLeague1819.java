package ats.algo.sport.outrights.competitionsdata;

import ats.algo.sport.outrights.CompetitionType;
import ats.algo.sport.outrights.calcengine.core.Competition;
import ats.algo.sport.outrights.calcengine.core.Fixture;
import ats.algo.sport.outrights.calcengine.core.FixtureType;
import ats.algo.sport.outrights.calcengine.core.Fixtures;
import ats.algo.sport.outrights.calcengine.core.Standing;
import ats.algo.sport.outrights.calcengine.core.Standings;
import ats.algo.sport.outrights.calcengine.core.Team;
import ats.algo.sport.outrights.calcengine.core.Teams;

public class EnglishChampionshipLeague1819 {

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

    private static Fixtures initFixtures(int nTeams) {
        Fixtures fixtures = new Fixtures();
        int index = 1;
        for (int j = 1; j <= nTeams; j++) {
            for (int k = 1; k <= nTeams; k++) {
                if (j != k) {
                    String id = "F" + Integer.toString(index);
                    fixtures.add(new Fixture("2018-09-01T14:30:00", FixtureType.LEAGUE, id, "T" + j, "T" + k,
                                    "empty_tag", false));
                    index++;
                }
            }
        }
        /*
         * add the playoffs
         * 
         */
        fixtures.add(new Fixture("2019-05-01T14:30:00", FixtureType.LEAGUE_PLAYOFF_LEG1, "P1-leg1", null, null, "6v3",
                        false));
        fixtures.add(new Fixture("2019-05-01T14:30:00", FixtureType.LEAGUE_PLAYOFF_LEG1, "P2-leg1", null, null, "5v4",
                        false));
        fixtures.add(Fixture.generateSecondLegFixture("2019-05-01T14:30:00", FixtureType.LEAGUE_PLAYOFF_LEG2, "P1-leg2",
                        null, null, "3v6", false, "P1-leg1"));
        fixtures.add(Fixture.generateSecondLegFixture("2019-05-01T14:30:00", FixtureType.LEAGUE_PLAYOFF_LEG2, "P2-leg2",
                        null, null, "4v5", false, "P2-leg1"));
        fixtures.add(new Fixture("2019-05-01T14:30:00", FixtureType.LEAGUE_PLAY_OFF_FINAL, "P3", null, null, "W1vW2",
                        true));
        return fixtures;
    }

    private static Standings initStandings(Teams teams) {
        Standings standings = new Standings();
        teams.getTeams().forEach((k, v) -> {
            standings.put(v.getTeamID(), new Standing(v.getTeamID(), 0, 0, 0, 0, 0, 0));
        });
        return standings;
    }

    public static Competition generate() {
        Teams teams = initTeams();
        Fixtures fixtures = initFixtures(teams.size());
        Standings standings = initStandings(teams);
        Competition competition = new Competition(CompetitionType.CHAMPIONSHIP_LEAGUE, 567890L,
                        "English Championship League 18/19", "championship", teams, fixtures, standings);
        competition.setReqdInputMarketType("FT:CS");
        return competition;
    }

}
