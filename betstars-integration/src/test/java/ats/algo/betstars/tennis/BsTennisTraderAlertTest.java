package ats.algo.betstars.tennis;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ats.algo.betstars.BsSimpleAlgoManagerTestBase;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.DatafeedMatchIncident;
import ats.algo.core.common.DatafeedMatchIncident.DatafeedMatchIncidentType;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.common.TeamId;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchFormat.FinalSetType;
import ats.algo.sport.tennis.TennisMatchIncident;
import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;
import ats.algo.sport.tennis.TennisMatchStateFromFeed;

public class BsTennisTraderAlertTest extends BsSimpleAlgoManagerTestBase {



    long eventId;

    @Test
    public void test() {
        TennisMatchFormat tennisMatchFormat = new TennisMatchFormat(3, FinalSetType.ADVANTAGE_SET, false, false);
        eventId = 1234L;
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
        System.out.println(publishedMatchState.isDatafeedStateMismatch());
        TennisMatchStateFromFeed feedState = new TennisMatchStateFromFeed(1, 1, 2, 0, 2, 3, TeamId.A);
        // feedState = new TennisMatchStateFromFeed(0,0,0,1,3,0,TeamId.A);
        TennisMatchIncident tempMatchIncident =
                        new TennisMatchIncident(1, TennisMatchIncidentType.POINT_WON, TeamId.A, 1);
        tempMatchIncident.setTennisMatchStateFromFeed(feedState);
        tempMatchIncident.setEventId(eventId);
        tempMatchIncident.setIncidentId("R7");
        assertEquals(this.traderAlert, null);
        this.publishedMarkets = null;


        algoManager.handleMatchIncident(tempMatchIncident, true);
        System.out.println(publishedMatchState.isDatafeedStateMismatch());
        System.out.println(this.traderAlert);
        System.out.println(this.publishedMarkets);
        // assertTrue(this.publishedMarkets==null);
        // assertTrue(matchState.isDatafeedStateMismatch());


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
