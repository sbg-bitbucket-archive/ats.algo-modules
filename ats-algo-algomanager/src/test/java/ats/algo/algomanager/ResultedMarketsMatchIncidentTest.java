package ats.algo.algomanager;

import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.ElapsedTimeMatchIncident;
import ats.algo.core.common.ResultedMarketsMatchIncident;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.common.ElapsedTimeMatchIncident.ElapsedTimeMatchIncidentType;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.sport.football.FootballMatchFormat;
import ats.algo.sport.football.FootballSimpleMatchState;

public class ResultedMarketsMatchIncidentTest extends AlgoManagerSimpleTestBase {

    @Test
    public void test() {
        long eventId = 12345L;
        algoManager.handleNewEventCreation(SupportedSportType.SOCCER, eventId, new FootballMatchFormat());

        MatchIncident incident1 = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
        incident1.setIncidentId("F1");
        incident1.setEventId(eventId);
        algoManager.handleMatchIncident(incident1, true);
        FootballSimpleMatchState sms1 = (FootballSimpleMatchState) publishedMatchState.copy();
        ResultedMarkets resultedMarkets = new ResultedMarkets();
        MatchIncident incident2 = new ResultedMarketsMatchIncident(resultedMarkets);
        incident2.setIncidentId("R1");
        incident2.setEventId(eventId);
        algoManager.handleMatchIncident(incident2, true);
        System.out.println(sms1);
        System.out.println(publishedMatchState);
        /*
         * check matchState same apart from elapsed time and incidentID
         */
        sms1.setElapsedTimeSeconds(((FootballSimpleMatchState) publishedMatchState).getElapsedTimeSeconds());
        sms1.setIncidentId(((FootballSimpleMatchState) publishedMatchState).getIncidentId());

        assertEquals(sms1, publishedMatchState);
    }

}
