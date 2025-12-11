package ats.algo.algomanager;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ats.algo.core.common.SupportedSportType;
import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.Gaussian;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchIncident;
import ats.algo.sport.tennis.TennisMatchParams;
import ats.algo.sport.tennis.TennisSimpleMatchState;
import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;

public class ResetEventTest extends AlgoManagerSimpleTestBase {

    @Test
    public void testResetEvent() {
        MethodName.log();
        TennisMatchFormat tennisMatchFormat = new TennisMatchFormat();
        /*
         * create match and get first set of published markets
         */
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, 13579L, tennisMatchFormat);
        TennisMatchParams defaultMatchParams = (TennisMatchParams) this.publishedMatchParams;
        defaultMatchParams.setEventId(11L);
        Gaussian g = defaultMatchParams.getOnServePctA1();
        g.setMean(g.getMean() + 0.000001); // make a small change the default params
        algoManager.handleSetMatchParams(defaultMatchParams.generateGenericMatchParams());
        /*
         * send in MatchIncident and then immediately a reset event. Should see in the logs a) msg saying abandon
         * priceCalcRequest then b) msg saying pricecalcResponse recevied for unexpected uniqueRequestId
         */
        TennisMatchIncident tennisMatchIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A);
        tennisMatchIncident.setEventId(13579L);
        tennisMatchIncident.setIncidentId("REQUEST_ID_235");
        algoManager.handleMatchIncident(tennisMatchIncident, true);
        super.publishedMarkets = null;
        algoManager.handleResetEvent(13579L, tennisMatchFormat, 3);
        defaultMatchParams = (TennisMatchParams) this.publishedMatchParams;
        defaultMatchParams.setEventId(11L);
        g = defaultMatchParams.getOnServePctA1();
        g.setMean(g.getMean() + 0.000001); // make a small change the default params
        algoManager.handleSetMatchParams(defaultMatchParams.generateGenericMatchParams());
        TennisSimpleMatchState simpleMatchState = (TennisSimpleMatchState) publishedMatchState;
        assertEquals(TeamId.UNKNOWN, simpleMatchState.getOnServeNow());
    }
}
