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

public class TestCompetition {
    private static Teams initTeams() {
        Teams teams = new Teams();
        teams.put("T1", new Team("T1", "Arsenal", "538Name", 2.8, 0.9));
        teams.put("T2", new Team("T2", "Bournemouth", "538Name", 1.7, 1.3));
        teams.put("T3", new Team("T3", "Brighton", "538Name", 1.4, 1));
        teams.put("T4", new Team("T4", "Burnley", "538Name", 1.5, 0.9));
        teams.put("T5", new Team("T5", "Chelsea", "538Name", 2.4, 0.6));
        teams.put("T6", new Team("T6", "Crystal Palace", "538Name", 1.9, 1));
        teams.put("T7", new Team("T7", "Everton", "538Name", 1.7, 1));
        teams.put("T8", new Team("T8", "Huddersfield", "538Name", 1.2, 1));
        teams.put("T9", new Team("T9", "Leicester City", "538Name", 1.7, 1));
        teams.put("T10", new Team("T10", "Liverpool", "538Name", 2.9, 0.6));
        teams.put("T11", new Team("T11", "Man. City", "538Name", 3, 0.5));
        teams.put("T12", new Team("T12", "Man. United", "538Name", 2.2, 0.7));
        teams.put("T13", new Team("T13", "Newcastle", "538Name", 1.5, 1));
        teams.put("T14", new Team("T14", "Southampton", "538Name", 1.6, 1));
        teams.put("T15", new Team("T15", "Stoke City", "538Name", 1.4, 1.1));
        teams.put("T16", new Team("T16", "Swansea City", "538Name", 1.2, 1.1));
        teams.put("T17", new Team("T17", "Tottenham", "538Name", 2.5, 0.6));
        teams.put("T18", new Team("T18", "Watford", "538Name", 1.6, 1.1));
        teams.put("T19", new Team("T19", "West Brom", "538Name", 1.5, 1));
        teams.put("T20", new Team("T20", "West Ham", "538Name", 1.7, 1.2));
        return teams;
    }

    private static Fixtures initFixtures() {
        Fixtures fixtures = new Fixtures();
        fixtures.add(new Fixture("2018-05-09T14:30:00", FixtureType.LEAGUE, "F1", "T17", "T13", "", false));
        fixtures.add(new Fixture("2018-05-13T14:30:00", FixtureType.LEAGUE, "F2", "T13", "T5", "", false));
        fixtures.add(new Fixture("2018-05-13T14:30:00", FixtureType.LEAGUE, "F3", "T12", "T18", "", false));
        fixtures.add(new Fixture("2018-05-13T14:30:00", FixtureType.LEAGUE, "F4", "T10", "T3", "", false));
        fixtures.add(new Fixture("2018-05-13T14:30:00", FixtureType.LEAGUE, "F5", "T8", "T1", "", false));
        fixtures.add(new Fixture("2018-05-13T14:30:00", FixtureType.LEAGUE, "F6", "T6", "T19", "", false));
        fixtures.add(new Fixture("2018-05-13T14:30:00", FixtureType.LEAGUE, "F7", "T4", "T2", "", false));
        fixtures.add(new Fixture("2018-05-09T14:30:00", FixtureType.LEAGUE, "F8", "T11", "T3", "", false));
        fixtures.add(new Fixture("2018-05-09T14:30:00", FixtureType.LEAGUE, "F9", "T9", "T1", "", false));
        fixtures.add(new Fixture("2018-05-09T14:30:00", FixtureType.LEAGUE, "F10", "T5", "T8", "", false));
        fixtures.add(new Fixture("2018-05-10T14:30:00", FixtureType.LEAGUE, "F11", "T20", "T12", "", false));
        fixtures.add(new Fixture("2018-05-13T14:30:00", FixtureType.LEAGUE, "F12", "T20", "T7", "", false));
        fixtures.add(new Fixture("2018-05-13T14:30:00", FixtureType.LEAGUE, "F13", "T17", "T9", "", false));
        fixtures.add(new Fixture("2018-05-13T14:30:00", FixtureType.LEAGUE, "F14", "T16", "T15", "", false));
        fixtures.add(new Fixture("2018-05-13T14:30:00", FixtureType.LEAGUE, "F15", "T14", "T11", "", false));
        return fixtures;
    }

    private static Standings initStandings() {
        Standings standings = new Standings();
        standings.put("T1", new Standing("T1", 36, 18, 6, 72, 48, 60));
        standings.put("T2", new Standing("T2", 37, 10, 11, 43, 60, 41));
        standings.put("T3", new Standing("T3", 36, 9, 13, 33, 47, 40));
        standings.put("T4", new Standing("T4", 37, 14, 12, 35, 37, 54));
        standings.put("T5", new Standing("T5", 36, 21, 6, 61, 34, 69));
        standings.put("T6", new Standing("T6", 37, 10, 11, 43, 55, 41));
        standings.put("T7", new Standing("T7", 37, 13, 10, 43, 55, 49));
        standings.put("T8", new Standing("T8", 36, 9, 9, 27, 56, 36));
        standings.put("T9", new Standing("T9", 36, 11, 11, 49, 54, 44));
        standings.put("T10", new Standing("T10", 37, 20, 12, 80, 38, 72));
        standings.put("T11", new Standing("T11", 36, 30, 4, 102, 26, 94));
        standings.put("T12", new Standing("T12", 36, 24, 5, 67, 28, 77));
        standings.put("T13", new Standing("T13", 36, 11, 8, 36, 46, 41));
        standings.put("T14", new Standing("T14", 37, 7, 15, 37, 55, 36));
        standings.put("T15", new Standing("T15", 37, 6, 12, 33, 67, 30));
        standings.put("T16", new Standing("T16", 37, 8, 9, 27, 54, 33));
        standings.put("T17", new Standing("T17", 36, 21, 8, 68, 32, 71));
        standings.put("T18", new Standing("T18", 37, 11, 8, 44, 63, 41));
        standings.put("T19", new Standing("T19", 37, 6, 13, 31, 54, 31));
        standings.put("T20", new Standing("T20", 36, 9, 11, 45, 67, 38));
        return standings;
    }

    public static Competition generate() {
        Teams teams = initTeams();
        Fixtures fixtures = initFixtures();
        Standings standings = initStandings();
        Competition competition = new Competition(CompetitionType.PREMIER_LEAGUE, 123456L, "Premier league 17/18",
                        "premier-league", teams, fixtures, standings);
        competition.setReqdInputMarketType("FT:CS");
        return competition;
    }

}
