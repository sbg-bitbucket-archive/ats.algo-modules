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

public class PremierLeague1819 {

    private static Teams initTeams() {
        Teams teams = new Teams();
        teams.put("T1", new Team("T1", "Manchester City", "Man. City", 1.0, 1.0));
        teams.put("T2", new Team("T2", "Manchester United", "Man. United", 1.0, 1.0));
        teams.put("T3", new Team("T3", "Tottenham", "Tottenham", 1.0, 1.0));
        teams.put("T4", new Team("T4", "Liverpool", "Liverpool", 1.0, 1.0));
        teams.put("T5", new Team("T5", "Chelsea", "Chelsea", 1.0, 1.0));
        teams.put("T6", new Team("T6", "Arsenal", "Arsenal", 1.0, 1.0));
        teams.put("T7", new Team("T7", "Burnley", "Burnley", 1.0, 1.0));
        teams.put("T8", new Team("T8", "Everton", "Everton", 1.0, 1.0));
        teams.put("T9", new Team("T9", "Leicester City", "Leicester City", 1.0, 1.0));
        teams.put("T10", new Team("T10", "Crystal Palace", "Crystal Palace", 1.0, 1.0));
        teams.put("T11", new Team("T11", "Newcastle", "Newcastle", 1.0, 1.0));
        teams.put("T12", new Team("T12", "Bournemouth", "Bournemouth", 1.0, 1.0));
        teams.put("T13", new Team("T13", "West Ham", "West Ham", 1.0, 1.0));
        teams.put("T14", new Team("T14", "Watford", "Watford", 1.0, 1.0));
        teams.put("T15", new Team("T15", "Brighton", "Brighton", 1.0, 1.0));
        teams.put("T16", new Team("T16", "Huddersfield", "Huddersfield", 1.0, 1.0));
        teams.put("T17", new Team("T17", "Southampton", "Southampton", 1.0, 1.0));
        teams.put("T18", new Team("T18", "Wolverhampton Wanderers", "tbd", 1.7, 1.0));
        teams.put("T19", new Team("T19", "Cardiff City", "Cardiff City", 1.3, 1.1));
        teams.put("T20", new Team("T20", "Fulham/Villa", "tbd", 1.4, 1.1));
        return teams;
    }

    private static Fixtures initFixtures() {
        Fixtures fixtures = new Fixtures();
        initialiseRoundMatchMap(fixtures, 20);
        return fixtures;
    }

    private static void initialiseRoundMatchMap(Fixtures fixtures, int noOfTeams) {
        /*
         * temporary set up of fixtures until the actual dates are known
         */
        int index = 1;
        for (int j = 1; j <= noOfTeams; j++)
            for (int k = 1; k <= noOfTeams; k++)
                if (j != k) {
                    String id = "F" + Integer.toString(index);
                    fixtures.add(new Fixture("2018-09-01T14:30:00", FixtureType.LEAGUE, id, "T" + j, "T" + k, "",
                                    false));
                    index++;
                }
    }

    private static Standings initStandings(Teams teams) {
        Standings standings = new Standings();
        teams.getTeams().forEach((k, v) -> {
            standings.put(v.getTeamID(), new Standing(v.getTeamID(), 0, 0, 0, 0, 0, 0));
        });
        return standings;
    }

    public static final long EVENT_ID = 456789L;

    public static Competition generate() {
        Teams teams = initTeams();
        Fixtures fixtures = initFixtures();
        Standings standings = initStandings(teams);
        Competition competition = new Competition(CompetitionType.PREMIER_LEAGUE, EVENT_ID, "Premier league 18/19",
                        "premier-league", teams, fixtures, standings);
        competition.setReqdInputMarketType("FT:CS");
        return competition;
    }

}
