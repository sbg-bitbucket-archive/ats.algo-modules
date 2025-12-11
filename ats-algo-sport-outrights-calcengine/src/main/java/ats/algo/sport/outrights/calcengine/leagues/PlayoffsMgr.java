package ats.algo.sport.outrights.calcengine.leagues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ats.algo.core.common.TeamId;
import ats.algo.sport.outrights.calcengine.core.Fixture;
import ats.algo.sport.outrights.calcengine.core.FixtureResult;
import ats.algo.sport.outrights.calcengine.core.Standing;
import ats.algo.sport.outrights.calcengine.core.Standings;
import ats.algo.sport.outrights.calcengine.core.TagMetaData;

/**
 * encapsulates all the logic around managing league playoff matches
 * 
 * @author gicha
 *
 */
public class PlayoffsMgr {

    public static final String PLAY_OFF1_LEG1 = "P1-leg1";
    public static final String PLAY_OFF2_LEG1 = "P2-leg1";
    public static final String PLAY_OFF1_LEG2 = "P1-leg2";
    public static final String PLAY_OFF2_LEG2 = "P2-leg2";
    public static final String PLAY_OFF_FINAL = "P3";

    private final int nPromotedUnconditionally;
    private final int nTeamsInPlayoff;

    private List<String> teamsInPlayoff;
    private boolean teamsInPlayoffKnown;
    private String playoffFinalistTeamAID;
    private String playoffFinalistTeamBID;
    private String playoffWinnerTeamID;
    Map<String, FixtureResult> fixtureScores;

    /*
     * playOffFixture is used to avoid creating a new object on every simulation run.
     */
    private Fixture playoffFixture;


    public PlayoffsMgr(LeagueFormat format, Standings standings, List<Fixture> completedPlayoffFixtures) {
        nPromotedUnconditionally = format.getnPromotedUnconditionally();
        nTeamsInPlayoff = format.getnTeamsInPlayoff();
        playoffFixture = new Fixture();
        teamsInPlayoff = new ArrayList<String>(nTeamsInPlayoff);
        fixtureScores = new HashMap<>(nTeamsInPlayoff / 2);
        teamsInPlayoffKnown = false;
        if (completedPlayoffFixtures.size() > 0) {
            /*
             * at least one playoff fixture has already been completed before the start of the simulation run
             */
            for (Fixture fixture : completedPlayoffFixtures) {
                updateStateForFixtureResult(fixture, new FixtureResult(fixture.getGoalsHome(), fixture.getGoalsAway()));
            }
            initialiseforStartOfSimulation(standings);
            teamsInPlayoffKnown = true;
        }
    }

    public void initialiseforStartOfSimulation(Standings standings) {
        if (teamsInPlayoffKnown) {
            /*
             * do nothing - teamsInPlayoff will already have been initialised
             */
        } else {
            List<Standing> finishOrder = standings.finishOrder();
            for (int i = 0; i < nTeamsInPlayoff; i++) {
                teamsInPlayoff.add(finishOrder.get(nPromotedUnconditionally + i).getTeamId());
            }
        }
    }

    /**
     * initialise the play off fixture object with the id's of the two teams. If the input fixture has id's already set
     * those are used in preference.
     * 
     * @param inputFixture
     */
    private void initialisePlayoffFixture(Fixture outputFixture, Fixture inputFixture) {
        TagMetaData tagMetaData = inputFixture.getTagMetaData();
        outputFixture.setEqualTo(inputFixture);
        if (outputFixture.getHomeTeamID() == null)
            outputFixture.setHomeTeamID(teamsInPlayoff
                            .get(convertPlayOffPosntoIndex(tagMetaData.getTeamARegularLeagueFinishPosition())));
        if (outputFixture.getAwayTeamID() == null)
            outputFixture.setAwayTeamID(teamsInPlayoff
                            .get(convertPlayOffPosntoIndex(tagMetaData.getTeamBRegularLeagueFinishPosition())));
    }

    /*
     * converts a playoff posn (e.g. in
     */
    private int convertPlayOffPosntoIndex(int leagueFinishPosn) {
        return leagueFinishPosn - nPromotedUnconditionally - 1;

    }

    public Fixture nextFixture(Fixture fixture) {
        switch (fixture.getFixtureType()) {
            case LEAGUE_PLAYOFF_LEG1:
                initialisePlayoffFixture(playoffFixture, fixture);
                return playoffFixture;
            case LEAGUE_PLAYOFF_LEG2:
                initialisePlayoffFixture(playoffFixture, fixture);
                String firstLegFixtureID = playoffFixture.getFirstLegFixtureID();
                /*
                 * check first if the first leg was already completed
                 */

                FixtureResult result = fixtureScores.get(firstLegFixtureID);
                playoffFixture.setFirstLegScoreA(result.getScoreA());
                playoffFixture.setFirstLegScoreB(result.getScoreB());
                playoffFixture.setMustHaveAWinner(true);
                return playoffFixture;
            case LEAGUE_PLAY_OFF_FINAL: {
                playoffFixture.setEqualTo(fixture);
                if (playoffFixture.getHomeTeamID() == null)
                    playoffFixture.setHomeTeamID(this.playoffFinalistTeamAID);
                if (playoffFixture.getAwayTeamID() == null)
                    playoffFixture.setAwayTeamID(this.playoffFinalistTeamBID);
                playoffFixture.setMustHaveAWinner(true);
                return playoffFixture;
            }
            default:
                throw new IllegalArgumentException(
                                "Invalid fixtureType for this competition: " + fixture.getFixtureType());
        }
    }

    public void updateStateForFixtureResult(Fixture fixture, FixtureResult result) {

        switch (fixture.getFixtureType()) {
            case LEAGUE_PLAYOFF_LEG1:
                fixtureScores.put(fixture.getFixtureID(), result);
                break;
            case LEAGUE_PLAYOFF_LEG2:
                /**
                 * assign the first semifinal to result to teamA in the final
                 */
                String id = idToID(fixture, result.getWinningTeamID());
                // GC DEBUG
                // System.out.printf("semifinal teamID: %s vs %s, winner %s\n",
                // fixture.getHomeTeamID(),
                // fixture.getAwayTeamID(), id);
                // if (id.equals("T19"))
                // System.out.println(id);

                if (playoffFinalistTeamAID == null)
                    playoffFinalistTeamAID = id;
                else
                    playoffFinalistTeamBID = id;
                break;
            case LEAGUE_PLAY_OFF_FINAL: {
                playoffWinnerTeamID = idToID(fixture, result.getWinningTeamID());
                // GC DEBUG
                // System.out.printf("winning teamID: %s vs %s, winner %s\n",
                // fixture.getHomeTeamID(),
                // fixture.getAwayTeamID(),
                // playoffWinnerTeamID);
                break;

            }
            default:
                throw new IllegalArgumentException(
                                "Invalid fixtureType for this competition: " + fixture.getFixtureType());
        }
    }

    public void setEqualTo(PlayoffsMgr other) {
        this.teamsInPlayoff.clear();
        other.teamsInPlayoff.forEach(t -> this.teamsInPlayoff.add(t));
        this.teamsInPlayoffKnown = other.teamsInPlayoffKnown;
        this.playoffFinalistTeamAID = other.playoffFinalistTeamAID;
        this.playoffFinalistTeamBID = other.playoffFinalistTeamBID;
        this.playoffWinnerTeamID = other.playoffWinnerTeamID;
        this.fixtureScores.clear();
        other.fixtureScores.forEach((k, t) -> this.fixtureScores.put(k, t));
    }

    private String idToID(Fixture fixture, TeamId teamID) {
        if (teamID == TeamId.A)
            return fixture.getHomeTeamID();
        else
            return fixture.getAwayTeamID();
    }

    public String getPlayoffFinalistTeamAID() {
        return playoffFinalistTeamAID;
    }

    public String getPlayoffFinalistTeamBID() {
        return playoffFinalistTeamBID;
    }

    public String getPlayoffWinnerTeamID() {
        return playoffWinnerTeamID;
    }

    public List<String> getTeamsInPlayoff() {
        return teamsInPlayoff;
    }

    public boolean isPromoted(String teamId) {
        return playoffWinnerTeamID.equals(teamId);
    }

    public boolean isInPlayoffs(String teamId) {
        for (String id : teamsInPlayoff)
            if (id.equals(teamId))
                return true;
        return false;
    }

}
