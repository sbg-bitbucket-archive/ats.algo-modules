package ats.algo.sport.tennis;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

import ats.algo.algomanager.AlgoManagerSimpleTestBase;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.Sleep;
import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;
import ats.algo.sport.tennis.TennisMatchIncident.TennisPointResult;


public class TennisMarketResultingTests extends AlgoManagerSimpleTestBase {

    long eventId = 123;
    int inc = 1;

    @Test
    public void testTotalAceAndPlayerMostAceMarket() {
        MethodName.log();
        System.setProperty("clientMarkets", "van");
        algoManager.publishResultedMarketsImmediately(true);

        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, new TennisMatchFormat(), 2);
        publishedMatchParams.setEventId(eventId);
        TennisMatchParams matchParams = (TennisMatchParams) algoManager.getEventDetails(eventId).getEventState()
                        .getMatchParams().generateXxxMatchParams();
        matchParams.getOnServePctA1().setMean(0.6);
        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());

        TennisMatchIncident matchIncident =
                        new TennisMatchIncident(1, TennisMatchIncidentType.MATCH_STARTING, TeamId.B, 1);
        matchIncident.setEventId(eventId);
        matchIncident.setIncidentId(Integer.toString(inc));
        inc++;
        algoManager.handleMatchIncident(matchIncident, true);

        algoManager.handleMatchIncident(getPointWonIncident(TeamId.A), true);
        algoManager.handleMatchIncident(getPointWonIncidentAsAce(TeamId.A), true);
        algoManager.handleMatchIncident(getPointWonIncidentAsAce(TeamId.A), true);
        algoManager.handleMatchIncident(getPointWonIncidentAsAce(TeamId.A), true);

        for (int i = 0; i < 20; i++) {
            algoManager.handleMatchIncident(getPointWonIncident(TeamId.A), true);
        }
        for (int j = 0; j < 24; j++) {
            algoManager.handleMatchIncident(getPointWonIncident(TeamId.A), true);
        }
        Sleep.sleep(3);
        assertEquals(publishedResultedMarkets.get("FT:AML").getWinningSelections().get(0), "Player A");
        assertEquals(publishedResultedMarkets.get("FT:AOU").getWinningSelections().get(0), "Under");
        assertEquals(publishedResultedMarkets.get("FT:AOU").getActualOutcome(), 3);
        System.clearProperty("clientMarkets");
    }

    @Test
    public void testTiebreakPointWinnerMarket() {
        MethodName.log();
        algoManager.publishResultedMarketsImmediately(true);

        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, new TennisMatchFormat(), 2);
        publishedMatchParams.setEventId(eventId);
        TennisMatchParams matchParams = (TennisMatchParams) algoManager.getEventDetails(eventId).getEventState()
                        .getMatchParams().generateXxxMatchParams();
        matchParams.setEventId(eventId);
        matchParams.getOnServePctA1().setMean(0.6);
        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());

        TennisMatchIncident matchIncident =
                        new TennisMatchIncident(1, TennisMatchIncidentType.MATCH_STARTING, TeamId.B, 1);
        matchIncident.setEventId(eventId);
        matchIncident.setIncidentId(Integer.toString(inc));
        inc++;
        algoManager.handleMatchIncident(matchIncident, true);

        algoManager.handleMatchIncident(getPointWonIncident(TeamId.A), true);
        algoManager.handleMatchIncident(getPointWonIncidentAsAce(TeamId.A), true);
        algoManager.handleMatchIncident(getPointWonIncidentAsAce(TeamId.A), true);
        algoManager.handleMatchIncident(getPointWonIncidentAsAce(TeamId.A), true);

        for (int i = 0; i < 16; i++) {
            algoManager.handleMatchIncident(getPointWonIncident(TeamId.A), true);
        }
        for (int j = 0; j < 20; j++) {
            algoManager.handleMatchIncident(getPointWonIncident(TeamId.B), true);
        }
        for (int k = 0; k < 4; k++) {
            algoManager.handleMatchIncident(getPointWonIncident(TeamId.B), true);
        }
        for (int l = 0; l < 4; l++) {
            algoManager.handleMatchIncident(getPointWonIncident(TeamId.A), true);
            // System.out.println(publishedMarkets);
            // System.out.println(publishedMarkets);
        }

        algoManager.handleMatchIncident(getPointWonIncident(TeamId.A), true);
        algoManager.handleMatchIncident(getPointWonIncident(TeamId.A), true);
        algoManager.handleMatchIncident(getPointWonIncident(TeamId.A), true);

        Sleep.sleep(3);
        assertEquals(publishedResultedMarkets.get("G:TBPW", "S1.13.3").getWinningSelections().get(0), "Player A");
    }

    @Ignore // Ignore until Jin merges that market.
    @Test
    public void testRaceToCertainGamesResulting() {
        MethodName.log();
        inc = 1;
        algoManager.publishResultedMarketsImmediately(true);

        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, new TennisMatchFormat(), 2);
        publishedMatchParams.setEventId(eventId);
        TennisMatchParams matchParams = (TennisMatchParams) algoManager.getEventDetails(eventId).getEventState()
                        .getMatchParams().generateXxxMatchParams();
        matchParams.setEventId(eventId);
        matchParams.getOnServePctA1().setMean(0.6);
        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());

        TennisMatchIncident matchIncident =
                        new TennisMatchIncident(1, TennisMatchIncidentType.MATCH_STARTING, TeamId.B, 1);
        matchIncident.setEventId(eventId);
        matchIncident.setIncidentId(Integer.toString(inc));
        inc++;
        algoManager.handleMatchIncident(matchIncident, true);

        algoManager.handleMatchIncident(getPointWonIncident(TeamId.A), true);
        algoManager.handleMatchIncident(getPointWonIncidentAsAce(TeamId.A), true);
        algoManager.handleMatchIncident(getPointWonIncidentAsAce(TeamId.A), true);
        algoManager.handleMatchIncident(getPointWonIncidentAsAce(TeamId.A), true);
        // System.out.println(algoManager.getEventDetails(eventId).getEventState().getMarketsAwaitingResult());

        for (int i = 0; i < 8; i++) {
            algoManager.handleMatchIncident(getPointWonIncident(TeamId.B), true);
        }

        assertEquals(publishedResultedMarkets.get("P:RTSG", "S1.2").getWinningSelections().get(0), "Player B");
    }

    TennisMatchIncident getPointWonIncident(TeamId teamId) {
        TennisMatchIncident matchIncident = new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, teamId);
        matchIncident.setEventId(eventId);
        matchIncident.setIncidentId(Integer.toString(inc));
        inc++;
        return matchIncident;
    }

    TennisMatchIncident getPointWonIncidentAsAce(TeamId teamId) {
        TennisMatchIncident matchIncident = new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, teamId);
        matchIncident.setPointResult(TennisPointResult.ACE);
        matchIncident.setEventId(eventId);
        matchIncident.setIncidentId(Integer.toString(inc));
        inc++;
        return matchIncident;
    }

}
