package ats.algo.algomanager;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.common.TeamId;
import ats.algo.core.comparetomarket.MarketPrice;
import ats.algo.core.comparetomarket.MarketPrices;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.core.comparetomarket.ParamFindResults;
import ats.algo.core.markets.MarketCategory;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.recordplayback.RecordedItem;
import ats.algo.core.recordplayback.Recording;
import ats.algo.core.timestamp.TimeStamp;
import ats.algo.core.traderalert.TraderAlert;
import ats.algo.core.traderalert.TraderAlert.TraderAlertType;
import ats.algo.core.tradingrules.SetSuspensionStatusTradingRule;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchIncident;
import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;
import ats.algo.sport.tennis.TennisMatchParams;

public class AlgoManagerAlertsTest extends AlgoManagerSimpleTestBase {

    public AlgoManagerAlertsTest() {
        /*
         * define the various functions that AlgoManager is going to need
         */

        // LogUtil.initConsoleLogging(Level.TRACE);

        AlgoManagerConfiguration algoManagerConfiguration = new SimpleAlgoManagerConfiguration();
        SupportedSportsInitialisation.init();
        /*
         * instantiate AlgoManager and tell it what functions use to calc prices and report the results
         */

        algoManager = new AlgoManager(algoManagerConfiguration, this);
        algoManager.onlyPublishMarketsFollowingParamChange(false);
        SetSuspensionStatusTradingRule[] tradingRules = new SetSuspensionStatusTradingRule[0];
        algoManager.setTradingRules(SupportedSportType.TENNIS, tradingRules);
        algoManager.publishResultedMarketsImmediately(true);
        recording = new Recording();
        PublishMarketsManager.publishAllMarkets = true;
    }

    @Test
    public void test() {
        MethodName.log();

        algoManager.transitionToInplayAlert(true);
        long eventId = 12345L;
        TennisMatchFormat tennisMatchFormat = new TennisMatchFormat();
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, tennisMatchFormat);

        TennisMatchParams matchParams = new TennisMatchParams();

        assertNull(traderAlert);

        revertBackToStartParams(matchParams);
        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());

        TennisMatchIncident startingTennisMatchIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A);
        startingTennisMatchIncident.setEventId(eventId);

        algoManager.handleMatchIncident(startingTennisMatchIncident, true);

        assertTrue(traderAlert.getTraderAlertType() == TraderAlertType.EVENT_INPLAY);

        // System.out.println(algoManager.getEventDetails(eventId).getEventState().getMarkets());

        // System.out.println(traderAlert);

        TennisMatchIncident tennisMatchIncident =
                        new TennisMatchIncident(10, TennisMatchIncidentType.SERVING_ORDER, TeamId.A);
        tennisMatchIncident.setEventId(eventId);

        algoManager.handleMatchIncident(tennisMatchIncident, true);

        TennisMatchIncident tennisMatchIncident1 =
                        new TennisMatchIncident(20, TennisMatchIncidentType.POINT_WON, TeamId.A);
        tennisMatchIncident1.setEventId(eventId);

        algoManager.handleMatchIncident(tennisMatchIncident1, true);

        algoManager.handleSupplyMarketPrices(eventId, getPrices(true));

        boolean inplay = false;

        for (TraderAlert alert : listOfTraderAlerts) {
            if (alert.getTraderAlertType().equals(TraderAlertType.FIRST_INPLAY_PF)) {
                inplay = true;
            }
        }

        assertTrue(inplay);

        // System.out.println(traderAlert);

        // System.out.println("TestPass");
        // System.out.println();

    }

    /*
     * Prematch prices for testing
     */

    private MarketPricesList getPrices(boolean includeGame) {

        MarketPrices m = new MarketPrices();
        m.setSourceWeight(1);

        MarketPrice matchWinner = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null);

        matchWinner.put("A", 1.86);
        matchWinner.put("B", 1.88);
        m.addMarketPrice(matchWinner);

        if (includeGame) {
            MarketPrice gameOneWinner = new MarketPrice("G:ML", "Game Winner", MarketCategory.GENERAL, "", "S1.2");

            gameOneWinner.put("A", 2.5);
            gameOneWinner.put("B", 1.5);
            m.addMarketPrice(gameOneWinner);
        }

        MarketPricesList marketPricesList = new MarketPricesList();
        marketPricesList.put("TestSource1", m);

        return marketPricesList;
    }

    private void revertBackToStartParams(TennisMatchParams matchParams) {
        matchParams.getOnServePctA1().setMean(0.6);
        matchParams.getOnServePctB1().setMean(0.65);

    }


    @Override
    public void publishMatchParams(long eventId, GenericMatchParams matchParams) {
        this.publishedMatchParams = matchParams.generateXxxMatchParams();
        printResults("matchParams", publishedMatchParams);
    }

    @Override
    public void publishMatchState(long eventId, SimpleMatchState matchState) {
        this.publishedMatchState = matchState;
        printResults("matchState", matchState);
    }

    @Override
    public void publishMarkets(long eventId, Markets markets, Set<String> keysForDiscontinuedMarkets,
                    TimeStamp timeStamp, String sequenceId) {
        this.publishedMarkets = markets;
        this.keysForDiscontinuedMarkets = keysForDiscontinuedMarkets;
        printResults("markets", markets);
    }

    @Override
    public void publishResultedMarkets(long eventId, ResultedMarkets resultedMarkets) {
        this.publishedResultedMarkets = resultedMarkets;
        printResults("resultedMarkets", resultedMarkets);
    }

    @Override
    public void publishParamFindResults(long eventId, ParamFindResults paramFindResults, GenericMatchParams matchParams,
                    long elapsedTimeMs) {
        this.publishedParamFinderResults = paramFindResults;
        printResults("paramFinderResults", paramFindResults);
    }

    @Override
    public void notifyEventCompleted(long eventId, boolean isCompleted) {
        this.publishedNotifyEventCompleted = isCompleted;
    }

    Recording recording;

    @Override
    public void publishRecordedItem(long eventId, RecordedItem recordedItem) {
        recording.add(recordedItem);
    }

    @Override
    public void notifyFatalError(long eventId, String uniqueRequestId, String errorCause) {}

    private void printResults(String resultsDescription, Object results) {
        /*
         * // System.out.printf("Published %s for event: %s \n", resultsDescription); //
         * System.out.print(results.toString()); // System.out.printf("--- Published %s ends ---\n\n",
         * resultsDescription);
         */
    }

    @Override
    public void publishTraderAlert(long eventId, TraderAlert traderAlert) {
        this.traderAlert = traderAlert;
        // System.out.println("Adding trader alert = " + traderAlert.getTraderAlertType().toString());
        this.listOfTraderAlerts.add(traderAlert);
    }

    @Override
    public void publishEventState(long eventId, EventStateBlob eventStateBlob) {}

    @Override
    public void publishEventSuspensionStatus(long eventId, boolean suspend, Set<MarketGroup> marketGroups) {

    }
}
