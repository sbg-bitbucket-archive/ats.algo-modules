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

public class WorldCup2018 {
    private static Teams initTeams() {
        Teams teams = new Teams();
        teams.put("W1", new Team("W1", "Egypt", null, 1.0, 1.0));
        teams.put("W2", new Team("W2", "Russia", null, 1.0, 1.0));
        teams.put("W3", new Team("W3", "Saudi Arabia", null, 1.0, 1.0));
        teams.put("W4", new Team("W4", "Uruguay", null, 1.0, 1.0));
        teams.put("W5", new Team("W5", "Iran", null, 1.0, 1.0));
        teams.put("W6", new Team("W6", "Morocco", null, 1.0, 1.0));
        teams.put("W7", new Team("W7", "Portugal", null, 1.0, 1.0));
        teams.put("W8", new Team("W8", "Spain", null, 1.0, 1.0));
        teams.put("W9", new Team("W9", "Australia", null, 1.0, 1.0));
        teams.put("W10", new Team("W10", "Denmark", null, 1.0, 1.0));
        teams.put("W11", new Team("W11", "France", null, 1.0, 1.0));
        teams.put("W12", new Team("W12", "Peru", null, 1.0, 1.0));
        teams.put("W13", new Team("W13", "Argentina", null, 1.0, 1.0));
        teams.put("W14", new Team("W14", "Croatia", null, 1.0, 1.0));
        teams.put("W15", new Team("W15", "Iceland", null, 1.0, 1.0));
        teams.put("W16", new Team("W16", "Nigeria", null, 1.0, 1.0));
        teams.put("W17", new Team("W17", "Brazil", null, 1.0, 1.0));
        teams.put("W18", new Team("W18", "Costa Rica", null, 1.0, 1.0));
        teams.put("W19", new Team("W19", "Serbia", null, 1.0, 1.0));
        teams.put("W20", new Team("W20", "Switzerland", null, 1.0, 1.0));
        teams.put("W21", new Team("W21", "Germany", null, 1.0, 1.0));
        teams.put("W22", new Team("W22", "Mexico", null, 1.0, 1.0));
        teams.put("W23", new Team("W23", "South Korea", null, 1.0, 1.0));
        teams.put("W24", new Team("W24", "Sweden", null, 1.0, 1.0));
        teams.put("W25", new Team("W25", "Belgium", null, 1.0, 1.0));
        teams.put("W26", new Team("W26", "England", null, 1.0, 1.0));
        teams.put("W27", new Team("W27", "Panama", null, 1.0, 1.0));
        teams.put("W28", new Team("W28", "Tunisia", null, 1.0, 1.0));
        teams.put("W29", new Team("W29", "Colombia", null, 1.0, 1.0));
        teams.put("W30", new Team("W30", "Japan", null, 1.0, 1.0));
        teams.put("W31", new Team("W31", "Poland", null, 1.0, 1.0));
        teams.put("W32", new Team("W32", "Senegal", null, 1.0, 1.0));

        return teams;
    }

    private static Fixtures initFixtures() {
        Fixtures fixtures = new Fixtures();
        fixtures.add(new Fixture("2018-06-14T16:00:00", FixtureType.GROUP_STAGE, "F1", "W2", "W3", "Group A", false));
        fixtures.add(new Fixture("2018-06-15T13:00:00", FixtureType.GROUP_STAGE, "F2", "W1", "W4", "Group A", false));
        fixtures.add(new Fixture("2018-06-15T16:00:00", FixtureType.GROUP_STAGE, "F3", "W6", "W5", "Group B", false));
        fixtures.add(new Fixture("2018-06-15T19:00:00", FixtureType.GROUP_STAGE, "F4", "W7", "W8", "Group B", false));
        fixtures.add(new Fixture("2018-06-16T11:00:00", FixtureType.GROUP_STAGE, "F5", "W11", "W9", "Group C", false));
        fixtures.add(new Fixture("2018-06-16T14:00:00", FixtureType.GROUP_STAGE, "F6", "W13", "W15", "Group D", false));
        fixtures.add(new Fixture("2018-06-16T17:00:00", FixtureType.GROUP_STAGE, "F7", "W12", "W10", "Group C", false));
        fixtures.add(new Fixture("2018-06-16T20:00:00", FixtureType.GROUP_STAGE, "F8", "W14", "W16", "Group D", false));
        fixtures.add(new Fixture("2018-06-17T13:00:00", FixtureType.GROUP_STAGE, "F9", "W18", "W19", "Group E", false));
        fixtures.add(new Fixture("2018-06-17T16:00:00", FixtureType.GROUP_STAGE, "F10", "W21", "W22", "Group F",
                        false));
        fixtures.add(new Fixture("2018-06-17T19:00:00", FixtureType.GROUP_STAGE, "F11", "W17", "W20", "Group E",
                        false));
        fixtures.add(new Fixture("2018-06-18T13:00:00", FixtureType.GROUP_STAGE, "F12", "W24", "W23", "Group F",
                        false));
        fixtures.add(new Fixture("2018-06-18T16:00:00", FixtureType.GROUP_STAGE, "F13", "W25", "W27", "Group G",
                        false));
        fixtures.add(new Fixture("2018-06-18T19:00:00", FixtureType.GROUP_STAGE, "F14", "W28", "W26", "Group G",
                        false));
        fixtures.add(new Fixture("2018-06-19T13:00:00", FixtureType.GROUP_STAGE, "F15", "W29", "W30", "Group H",
                        false));
        fixtures.add(new Fixture("2018-06-19T16:00:00", FixtureType.GROUP_STAGE, "F16", "W31", "W32", "Group H",
                        false));
        fixtures.add(new Fixture("2018-06-19T19:00:00", FixtureType.GROUP_STAGE, "F17", "W2", "W1", "Group A", false));
        fixtures.add(new Fixture("2018-06-20T13:00:00", FixtureType.GROUP_STAGE, "F18", "W7", "W6", "Group B", false));
        fixtures.add(new Fixture("2018-06-20T16:00:00", FixtureType.GROUP_STAGE, "F19", "W4", "W3", "Group A", false));
        fixtures.add(new Fixture("2018-06-20T19:00:00", FixtureType.GROUP_STAGE, "F20", "W5", "W8", "Group B", false));
        fixtures.add(new Fixture("2018-06-21T13:00:00", FixtureType.GROUP_STAGE, "F21", "W10", "W9", "Group C", false));
        fixtures.add(new Fixture("2018-06-21T16:00:00", FixtureType.GROUP_STAGE, "F22", "W11", "W12", "Group C",
                        false));
        fixtures.add(new Fixture("2018-06-21T19:00:00", FixtureType.GROUP_STAGE, "F23", "W13", "W14", "Group D",
                        false));
        fixtures.add(new Fixture("2018-06-22T13:00:00", FixtureType.GROUP_STAGE, "F24", "W17", "W18", "Group E",
                        false));
        fixtures.add(new Fixture("2018-06-22T16:00:00", FixtureType.GROUP_STAGE, "F25", "W16", "W15", "Group D",
                        false));
        fixtures.add(new Fixture("2018-06-22T19:00:00", FixtureType.GROUP_STAGE, "F26", "W19", "W20", "Group E",
                        false));
        fixtures.add(new Fixture("2018-06-23T13:00:00", FixtureType.GROUP_STAGE, "F27", "W25", "W28", "Group G",
                        false));
        fixtures.add(new Fixture("2018-06-23T16:00:00", FixtureType.GROUP_STAGE, "F28", "W23", "W22", "Group F",
                        false));
        fixtures.add(new Fixture("2018-06-23T19:00:00", FixtureType.GROUP_STAGE, "F29", "W21", "W24", "Group F",
                        false));
        fixtures.add(new Fixture("2018-06-24T13:00:00", FixtureType.GROUP_STAGE, "F30", "W26", "W27", "Group G",
                        false));
        fixtures.add(new Fixture("2018-06-24T16:00:00", FixtureType.GROUP_STAGE, "F31", "W30", "W32", "Group H",
                        false));
        fixtures.add(new Fixture("2018-06-24T19:00:00", FixtureType.GROUP_STAGE, "F32", "W31", "W29", "Group H",
                        false));
        fixtures.add(new Fixture("2018-06-25T15:00:00", FixtureType.GROUP_STAGE, "F33", "W3", "W1", "Group A", false));
        fixtures.add(new Fixture("2018-06-25T15:00:00", FixtureType.GROUP_STAGE, "F34", "W4", "W2", "Group A", false));
        fixtures.add(new Fixture("2018-06-25T19:00:00", FixtureType.GROUP_STAGE, "F35", "W5", "W7", "Group B", false));
        fixtures.add(new Fixture("2018-06-25T19:00:00", FixtureType.GROUP_STAGE, "F36", "W8", "W6", "Group B", false));
        fixtures.add(new Fixture("2018-06-26T15:00:00", FixtureType.GROUP_STAGE, "F37", "W9", "W12", "Group C", false));
        fixtures.add(new Fixture("2018-06-26T15:00:00", FixtureType.GROUP_STAGE, "F38", "W10", "W11", "Group C",
                        false));
        fixtures.add(new Fixture("2018-06-26T19:00:00", FixtureType.GROUP_STAGE, "F39", "W15", "W14", "Group D",
                        false));
        fixtures.add(new Fixture("2018-06-26T19:00:00", FixtureType.GROUP_STAGE, "F40", "W16", "W13", "Group D",
                        false));
        fixtures.add(new Fixture("2018-06-27T15:00:00", FixtureType.GROUP_STAGE, "F41", "W22", "W24", "Group F",
                        false));
        fixtures.add(new Fixture("2018-06-27T15:00:00", FixtureType.GROUP_STAGE, "F42", "W23", "W21", "Group F",
                        false));
        fixtures.add(new Fixture("2018-06-27T19:00:00", FixtureType.GROUP_STAGE, "F43", "W19", "W17", "Group E",
                        false));
        fixtures.add(new Fixture("2018-06-27T19:00:00", FixtureType.GROUP_STAGE, "F44", "W20", "W18", "Group E",
                        false));
        fixtures.add(new Fixture("2018-06-28T15:00:00", FixtureType.GROUP_STAGE, "F45", "W30", "W31", "Group H",
                        false));
        fixtures.add(new Fixture("2018-06-28T15:00:00", FixtureType.GROUP_STAGE, "F46", "W32", "W29", "Group H",
                        false));
        fixtures.add(new Fixture("2018-06-28T19:00:00", FixtureType.GROUP_STAGE, "F47", "W26", "W25", "Group G",
                        false));
        fixtures.add(new Fixture("2018-06-28T19:00:00", FixtureType.GROUP_STAGE, "F48", "W27", "W28", "Group G",
                        false));
        fixtures.add(new Fixture("2018-06-30T15:00:00", FixtureType.KNOCKOUT_R16, "F50", "", "", "R16-1Cv2D", false));
        fixtures.add(new Fixture("2018-06-30T19:00:00", FixtureType.KNOCKOUT_R16, "F49", "", "", "R16-1Av2B", false));
        fixtures.add(new Fixture("2018-07-01T15:00:00", FixtureType.KNOCKOUT_R16, "F51", "", "", "R16-1Bv2A", false));
        fixtures.add(new Fixture("2018-07-01T19:00:00", FixtureType.KNOCKOUT_R16, "F52", "", "", "R16-1Dv2C", false));
        fixtures.add(new Fixture("2018-07-02T15:00:00", FixtureType.KNOCKOUT_R16, "F53", "", "", "R16-1Ev2F", false));
        fixtures.add(new Fixture("2018-07-02T19:00:00", FixtureType.KNOCKOUT_R16, "F54", "", "", "R16-1Gv2H", false));
        fixtures.add(new Fixture("2018-07-03T15:00:00", FixtureType.KNOCKOUT_R16, "F55", "", "", "R16-1Fv2E", false));
        fixtures.add(new Fixture("2018-07-03T19:00:00", FixtureType.KNOCKOUT_R16, "F56", "", "", "R16-1Hv2G", false));
        fixtures.add(new Fixture("2018-07-06T15:00:00", FixtureType.KNOCKOUT_QF, "F57", "", "", "R8-W49vW50", false));
        fixtures.add(new Fixture("2018-07-06T19:00:00", FixtureType.KNOCKOUT_QF, "F58", "", "", "R8-W53vW54", false));
        fixtures.add(new Fixture("2018-07-07T15:00:00", FixtureType.KNOCKOUT_QF, "F60", "", "", "R8-W55vW56", false));
        fixtures.add(new Fixture("2018-07-07T19:00:00", FixtureType.KNOCKOUT_QF, "F59", "", "", "R8-W51vW52", false));
        fixtures.add(new Fixture("2018-07-10T19:00:00", FixtureType.KNOCKOUT_SF, "F61", "", "", "SF-W57vW58", false));
        fixtures.add(new Fixture("2018-07-11T19:00:00", FixtureType.KNOCKOUT_SF, "F62", "", "", "SF-W59vW60", false));
        fixtures.add(new Fixture("2018-07-14T15:00:00", FixtureType.KNOCKOUT_THIRD_PLACE, "F63", "", "", "P-L61vL62",
                        false));
        fixtures.add(new Fixture("2018-07-15T16:00:00", FixtureType.KNOCKOUT_FINAL, "F64", "", "", "F-W61vW62", false));
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

    public static Competition generateBasicSample() {
        Teams teams = initTeams();
        Fixtures fixtures = initFixtures();
        @SuppressWarnings("unused")
        Standings standings = initStandings();
        Competition competition = new Competition(CompetitionType.PREMIER_LEAGUE, "TEST", 123456L, "World Cup",
                        "premier-league", teams, fixtures);
        competition.setReqdInputMarketType("FT:CS");
        return competition;
    }



}
