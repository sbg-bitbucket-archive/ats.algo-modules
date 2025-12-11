package ats.algo.algomanager;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ats.algo.core.common.SupportedSportType;
import ats.algo.core.common.TeamId;

import ats.algo.core.markets.ResultedMarket;

import ats.algo.core.tradingrules.SetSuspensionStatusTradingRule;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchIncident;
import ats.algo.sport.tennis.TennisMatchFormat.FinalSetType;
import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;

public class AlgoManagerResultingMarketSelectionTest extends AlgoManagerSimpleTestBase {


    public AlgoManagerResultingMarketSelectionTest() {
        PublishMarketsManager.publishAllMarkets = true;
    }

    @Test
    public void testResultedMarkets() {
        MethodName.log();
        algoManager.onlyPublishMarketsFollowingParamChange(false);
        SetSuspensionStatusTradingRule[] tradingRules = new SetSuspensionStatusTradingRule[0];
        algoManager.setTradingRules(SupportedSportType.TENNIS, tradingRules);
        algoManager.publishResultedMarketsImmediately(true);
        TennisMatchFormat tennisMatchFormat =
                        new TennisMatchFormat(3, FinalSetType.NORMAL_WITH_TIE_BREAK, false, false);
        long eventId = 987654321L;
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, tennisMatchFormat);
        TennisMatchIncident tennisMatchIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A);
        tennisMatchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(tennisMatchIncident, true);
        tennisMatchIncident.setTennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A, 1);
        algoManager.handleMatchIncident(tennisMatchIncident, true);
        tennisMatchIncident.setTennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A, 1);
        algoManager.handleMatchIncident(tennisMatchIncident, true);
        tennisMatchIncident.setTennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A, 1);
        algoManager.handleMatchIncident(tennisMatchIncident, true);
        tennisMatchIncident.setTennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A, 1);
        algoManager.handleMatchIncident(tennisMatchIncident, true);

        // System.out.print(publishedResultedMarkets.toString());
        // System.out.print(publishedResultedMarkets.getFullyResultedMarkets().toString());
        /*
         * expect one partially resulted market: P:CS_S1
         */
        assertEquals(1, publishedResultedMarkets.size() - publishedResultedMarkets.getFullyResultedMarkets().size());
        ResultedMarket resultedMarket = publishedResultedMarkets.getResultedMarkets().get("P:CS_S1");
        if (resultedMarket != null)
            assertTrue(!resultedMarket.isFullyResulted());
        assertTrue(publishedResultedMarkets.getResultedMarkets().get("G:ML_S1.1").isFullyResulted());
    }


}
