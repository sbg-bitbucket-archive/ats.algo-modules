package ats.algo.betstars.tennis;


import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.DatafeedMatchIncident;
import ats.algo.core.common.DatafeedMatchIncident.DatafeedMatchIncidentType;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.common.TeamId;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.genericsupportfunctions.MethodName;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchFormat.FinalSetType;
import ats.algo.sport.tennis.TennisMatchIncident;
import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;
import ats.algo.sport.tennis.TennisMatchState;
import ats.algo.sport.tennis.tradingrules.bs.BsTennisTradingRules;

public class BsTennisUndoProblemTest extends BsSimpleAlgoManagerTennisTestBase {



    long eventId;

    @Test
    public void testUndoLastMatchIncident() {
        MethodName.log();
        TennisMatchFormat tennisMatchFormat =
                        new TennisMatchFormat(3, FinalSetType.NORMAL_WITH_TIE_BREAK, false, false);
        eventId = 1234L;
        TradingRules tradingRules = new BsTennisTradingRules();
        algoManager.setTradingRules(SupportedSportType.TENNIS, tradingRules);
        algoManager.onlyPublishMarketsFollowingParamChange(false);
        algoManager.autoSyncWithMatchFeed(false);
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, tennisMatchFormat);
        algoManager.handleMatchIncident(getIncident("R1", TennisMatchIncidentType.MATCH_STARTING, TeamId.A), true);
        algoManager.handleMatchIncident(getIncident("R2", TennisMatchIncidentType.POINT_WON, TeamId.B), true);
        algoManager.handleMatchIncident(getIncident("R3", TennisMatchIncidentType.POINT_WON, TeamId.B), true);
        algoManager.handleMatchIncident(getIncident("R4", TennisMatchIncidentType.POINT_WON, TeamId.B), true);
        algoManager.handleMatchIncident(getIncident("R5", TennisMatchIncidentType.POINT_WON, TeamId.A), true);
        algoManager.handleMatchIncident(getDatafeedIncident("D1", DatafeedMatchIncidentType.BET_STOP), true);
        algoManager.handleMatchIncident(getIncident("R6", TennisMatchIncidentType.POINT_WON, TeamId.A), true);
        algoManager.handleUndoLastMatchIncident(eventId);
        algoManager.handleMatchIncident(getIncident("R7", TennisMatchIncidentType.POINT_WON, TeamId.A), true);
        boolean finished = false;
        do {
            if ((TennisMatchState) algoManager.getEventDetails(eventId).getEventState().getMatchState() != null) {
                TennisMatchState matchState =
                                (TennisMatchState) algoManager.getEventDetails(eventId).getEventState().getMatchState();
                // System.out.println(matchState);
                if (matchState.getLastIncidentDetails().getLastIncident() != null) {
                    finished = matchState.getLastIncidentDetails().getLastIncident().getIncidentId().equals("R7");
                }
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } while (!finished);
        assertTrue(finished);
    }


    private TennisMatchIncident getIncident(String requestId, TennisMatchIncidentType type, TeamId teamId) {
        TennisMatchIncident matchIncident = new TennisMatchIncident(0, type, teamId);
        matchIncident.setEventId(eventId);
        matchIncident.setIncidentId(requestId);
        return matchIncident;
    }

    private MatchIncident getDatafeedIncident(String requestId, DatafeedMatchIncidentType status) {
        DatafeedMatchIncident incident = new DatafeedMatchIncident();
        incident.setIncidentId(requestId);
        incident.setEventId(eventId);
        incident.setIncidentSubType(status);
        return incident;

    }



}
