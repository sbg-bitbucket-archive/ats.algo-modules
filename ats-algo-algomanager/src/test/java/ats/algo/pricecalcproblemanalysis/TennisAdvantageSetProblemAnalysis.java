package ats.algo.pricecalcproblemanalysis;

import static org.junit.Assert.*;

import ats.algo.algomanager.AlgoManagerSimpleTestBase;
import ats.algo.algomanager.EventStateBlob;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.common.TeamId;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchIncident;
import ats.algo.sport.tennis.TennisMatchFormat.FinalSetType;
import ats.algo.sport.tennis.TennisMatchFormat.Sex;
import ats.algo.sport.tennis.TennisMatchFormat.Surface;
import ats.algo.sport.tennis.TennisMatchFormat.TournamentLevel;
import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;
import ats.algo.sport.tennis.TennisMatchParams;
import ats.algo.sport.tennis.TennisSimpleMatchState;
import ats.core.util.log.Level;
import ats.core.util.log.LogUtil;

public class TennisAdvantageSetProblemAnalysis extends AlgoManagerSimpleTestBase {

    long eventId = 1234L;


    public void test() {
        LogUtil.initConsoleLogging(Level.ERROR);
        TennisMatchFormat matchFormat = new TennisMatchFormat(false, Sex.WOMEN, Surface.GRASS,
                        TournamentLevel.GRANDSLAM, 3, FinalSetType.ADVANTAGE_SET, false);

        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, matchFormat, 4);
        TennisMatchParams matchParams = (TennisMatchParams) publishedMatchParams;
        matchParams.setEventId(eventId);
        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());
        TennisMatchIncident matchIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A);
        matchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(matchIncident, true);
        System.out.println(publishedMatchState.generateSimpleMatchState());
        for (int i = 0; i < 6; i++) {
            playGame(TeamId.A);
            System.out.println(publishedMatchState.generateSimpleMatchState());
        }
        for (int i = 0; i < 6; i++) {
            playGame(TeamId.B);
            System.out.println(publishedMatchState.generateSimpleMatchState());
        }
        for (int i = 0; i < 15; i++) {
            TeamId teamId;
            if (isEven(i))
                teamId = TeamId.A;
            else
                teamId = TeamId.B;
            playGame(teamId);
            System.out.println(publishedMatchState.generateSimpleMatchState());
        }
        algoManager.handleMatchIncident(getPointWonIncident(TeamId.A), true);
        System.out.println(publishedMatchState.generateSimpleMatchState());
        for (int i = 0; i < 3; i++) {
            algoManager.handleMatchIncident(getPointWonIncident(TeamId.B), true);
            System.out.println(publishedMatchState.generateSimpleMatchState());
        }
        /**
         * should now be 1-1 8-7 15-40
         */
        System.out.println(this.publishedMatchState.generateSimpleMatchState());
        matchParams = (TennisMatchParams) publishedMatchParams;
        matchParams.getOnServePctA1().setProperties(0.545, 0.0552, 0.0);
        matchParams.getOnServePctB1().setProperties(0.5397, 0.052, 0.0);
        matchParams.setEventId(eventId);
        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());
        System.out.println(this.publishedMatchState.generateSimpleMatchState());
        System.out.println(this.publishedMatchParams);
        assertEquals(7, ((TennisSimpleMatchState) publishedMatchState.generateSimpleMatchState()).getGamesB());
        /*
         * ready to replay the point that failed. should move gamesB up to 8. first capture the blob so we can repeat
         */
        EventStateBlob eventStateBlob = this.publishedEventStateBlob;
        TennisMatchIncident incident = getErroredPointWonIncident();
        System.out.println(incident);
        algoManager.handleMatchIncident(incident, true);
        assertEquals(8, ((TennisSimpleMatchState) publishedMatchState.generateSimpleMatchState()).getGamesB());
        System.out.println(this.publishedMatchState.generateSimpleMatchState());
        /*
         * if we got this far then first attempt succeeded.
         */
        this.publishedMatchState = null;
        algoManager.handleRemoveEvent(eventId);
        for (int i = 0; i < 1000; i++) {
            eventId = 10000 + i;
            System.out.println("iteration: " + i);
            this.publishedMatchState = null;;
            algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, matchFormat, eventStateBlob, null);
            algoManager.handleMatchIncident(getErroredPointWonIncident(), true);
            System.out.println(this.publishedMatchState.generateSimpleMatchState());
            assertEquals(8, ((TennisSimpleMatchState) publishedMatchState.generateSimpleMatchState()).getGamesB());
            algoManager.handleRemoveEvent(eventId);
        }
    }


    public static void main(String[] args) {
        TennisAdvantageSetProblemAnalysis Test = new TennisAdvantageSetProblemAnalysis();
        Test.test();
        System.out.println("Finished");
    }

    private boolean isEven(int i) {
        return 2 * (i / 2) == i;
    }

    public TennisMatchIncident getPointWonIncident(TeamId teamId) {
        TennisMatchIncident matchIncident = new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, teamId);
        matchIncident.setEventId(eventId);
        return matchIncident;
    }

    private TennisMatchIncident getErroredPointWonIncident() {
        TennisMatchIncident incident = getPointWonIncident(TeamId.B);
        incident.setSourceSystem("LSPORTS");
        incident.setServerSideAtStartOfMatch(TeamId.A);
        incident.setTeamId(TeamId.B);
        return incident;
    }

    public void playGame(TeamId id) {
        for (int j = 0; j < 4; j++) {
            algoManager.handleMatchIncident(getPointWonIncident(id), true);
        }
    }

}
