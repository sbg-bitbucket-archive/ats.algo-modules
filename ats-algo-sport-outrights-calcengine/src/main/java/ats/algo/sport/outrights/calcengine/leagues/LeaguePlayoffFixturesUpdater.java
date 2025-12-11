package ats.algo.sport.outrights.calcengine.leagues;

import ats.algo.genericsupportfunctions.PairOfIntegers;
import ats.algo.sport.outrights.OutrightsFixtureStatus;
import ats.algo.sport.outrights.calcengine.core.Fixture;
import ats.algo.sport.outrights.calcengine.core.Fixtures;

public class LeaguePlayoffFixturesUpdater {

    /**
     * updates any derived fixtures from the results of fixtures completed to date
     * 
     * @param fixtures
     * @param resulter
     */
    public static void update(Fixtures fixtures, LeagueMarketsResulter resulter) {
        for (Fixture fixture : fixtures) {
            if (fixture.getStatus() == OutrightsFixtureStatus.COMPLETED)
                continue;
            switch (fixture.getFixtureType()) {
                case LEAGUE:
                    break;
                case LEAGUE_PLAYOFF_LEG1:
                case LEAGUE_PLAYOFF_LEG2:
                    checkPlayoffsTeamsIdentified(fixtures, resulter, fixture);
                    break;
                case LEAGUE_PLAY_OFF_FINAL:
                    checkFinalsTeamsIdentified(fixtures, fixture);
                    break;
                default:
                    break;

            }
        }

    }

    private static void checkPlayoffsTeamsIdentified(Fixtures fixtures, LeagueMarketsResulter resulter,
                    Fixture fixture) {
        PairOfIntegers p = parsePlayOffTag(fixture.getTag());
        fixture.setHomeTeamID(resulter.getTeamFinishingNth(p.A));
        fixture.setAwayTeamID(resulter.getTeamFinishingNth(p.B));
    }

    private static PairOfIntegers parsePlayOffTag(String tag) {
        return PairOfIntegers.generateFromString(tag, "v");
    }

    private static void checkFinalsTeamsIdentified(Fixtures fixtures, Fixture fixture) {
        String teamIdA = checkFinalsTeamIdentified(fixtures, 1);
        fixture.setHomeTeamID(teamIdA);
        String teamIdB = checkFinalsTeamIdentified(fixtures, 2);
        fixture.setAwayTeamID(teamIdB);
    }

    private static String checkFinalsTeamIdentified(Fixtures fixtures, int n) {
        String winningTeamId = null;
        Fixture l1 = fixtures.getByFixtureID("P" + n + "-leg1");
        Fixture l2 = fixtures.getByFixtureID("P" + n + "-leg2");
        if (l1.getStatus().equals(OutrightsFixtureStatus.COMPLETED)
                        && (l2.getStatus().equals(OutrightsFixtureStatus.COMPLETED))) {
            int goalsA = l1.getGoalsHome() + l2.getGoalsAway();
            int goalsB = l1.getGoalsAway() + l2.getGoalsHome();
            int goalDiff = goalsA - goalsB;
            if (goalDiff > 0)
                winningTeamId = l1.getHomeTeamID();
            else if (goalDiff < 0)
                winningTeamId = l1.getAwayTeamID();
        }
        return winningTeamId;
    }

    /**
     * checks that a fixtureTag and fixtureId meet the syntax requirements
     * 
     * @param fixture
     * @return
     */
    public static String validateFixture(Fixture fixture) {
        String tag = fixture.getTag();
        String fixtureId = fixture.getFixtureID();
        String response = null;
        int playoffLegNo = 2;
        switch (fixture.getFixtureType()) {
            case LEAGUE:
                if (!tag.equals(""))
                    return "Tag for fixture of type LEAGUE should be \"\" but is: " + tag;
                break;
            case LEAGUE_PLAYOFF_LEG1:
                playoffLegNo = 1;
                // fall through to Leg2 case - no break expected
            case LEAGUE_PLAYOFF_LEG2:
                String s = checkPlayoffTag(tag);
                if (s != null)
                    return null;
                s = checkPlayoffFixtureId(fixtureId, playoffLegNo);
                if (s != null)
                    return null;
                break;
            case LEAGUE_PLAY_OFF_FINAL:
                if (!fixtureId.equals("P3"))
                    return "fixtureId for league playoff final should be P3 but is: " + fixtureId;
                /*
                 * don't use the finals tag so allow anything
                 */
                break;
            default:
                throw new IllegalArgumentException("fixture type not supported: " + fixture.getFixtureType());
        }
        return response;
    }

    static String checkPlayoffTag(String tag) {
        PairOfIntegers p = parsePlayOffTag(tag);
        if (p == null)
            return "Invalid tag format for fixture.  Should be of form \"NvM\" but is: " + tag;
        return null;
    }

    /**
     * Verifies that fixtureId is f the format "P<m>-leg<n>" where m = 1 or 2
     * 
     * @param fixtureId
     * @param n
     * @return null if ok, else err msg
     */
    static String checkPlayoffFixtureId(String fixtureId, int n) {
        try {
            String mStr = fixtureId.substring(1, 2);
            int m = Integer.parseInt(mStr);
            if (m < 1 || m > 2)
                return "Invalid fixture id.  Should be of format \"P<m>-leg<n>\" where m = 1 or 2 but is: " + fixtureId;
            String nStr = fixtureId.substring(6, 7);
            int nParsed = Integer.parseInt(nStr);
            if (nParsed != n)
                return String.format("Invalid fixture id.  Should be of format \"P<m>-leg<n>\" where n = %d but is: %s",
                                n, fixtureId);
        } catch (NumberFormatException e) {
            return String.format("Invalid fixture id.  Should be of format \"P<m>-leg<n>\" where n = %d but is: %s", n,
                            fixtureId);
        }

        return null;
    }

}
