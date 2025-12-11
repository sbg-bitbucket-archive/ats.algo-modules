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
    private final int nPromotedUnconditionally;
    private final int nPromotedViaPlayoff;
    private final int nTeamsInPlayoff;
    private List<String> teamsInPlayoff;
    private String playoffFinalistTeamAID;
    private String playoffFinalistTeamBID;
    private String playoffWinnerTeamID;
    private Fixture playoffFixture;
    Map<String, FixtureResult> fixtureScores;

    public PlayoffsMgr(LeagueFormat format) {
        nPromotedUnconditionally = format.getnPromotedUnconditionally();
        nPromotedViaPlayoff = format.getnPromotedViaPlayoff();
        nTeamsInPlayoff = format.getnTeamsInPlayoff();
        teamsInPlayoff = new ArrayList<String>(nTeamsInPlayoff);
        playoffFixture = new Fixture();
        fixtureScores = new HashMap<>(nTeamsInPlayoff / 2);
    }

    public void initialiseforEndOfRegularLeague(Standings standings) {
        teamsInPlayoff.clear();
        fixtureScores.clear();
        playoffFinalistTeamAID = null;
        playoffFinalistTeamBID = null;
        playoffWinnerTeamID = null;
        if (nPromotedViaPlayoff == 0) {
            /*
             * nothing to do if no playoffs
             */
            return;
        }
        List<Standing> finishOrder = standings.finishOrder();
        for (int i = 0; i < nTeamsInPlayoff; i++) {
            teamsInPlayoff.add(finishOrder.get(nPromotedUnconditionally + i).getTeamId());
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
                // System.out.printf("semifinal teamID: %s vs %s, winner %s\n", fixture.getHomeTeamID(),
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
                // System.out.printf("winning teamID: %s vs %s, winner %s\n", fixture.getHomeTeamID(),
                // fixture.getAwayTeamID(),
                // playoffWinnerTeamID);
                break;

            }
            default:
                throw new IllegalArgumentException(
                                "Invalid fixtureType for this competition: " + fixture.getFixtureType());
        }
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
