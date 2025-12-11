package ats.algo.betstars.tennis;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.betstars.algo.ats.integration.BsTennisMatchParams;

import ats.algo.algomanager.EventStateBlob;
import ats.algo.algomanager.TriggerParamFindTradingRulesResult;
import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.core.common.DatafeedMatchIncident;
import ats.algo.core.common.DatafeedMatchIncident.DatafeedMatchIncidentType;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.common.TeamId;
import ats.algo.core.comparetomarket.MarketPrice;
import ats.algo.core.comparetomarket.MarketPrices;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.core.comparetomarket.ParamFindResults;
import ats.algo.core.markets.MarketCategory;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.markets.SuspensionStatus;
import ats.algo.core.recordplayback.RecordedItem;
import ats.algo.core.recordplayback.Recording;
import ats.algo.core.timestamp.TimeStamp;
import ats.algo.core.traderalert.TraderAlert;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchIncident;
import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;
import ats.algo.springbridge.SpringContextBridge;

public class BetStarsTennisTradingRulesTest extends BsSimpleAlgoManagerTennisTestBase {


    /**
     * Test sequence is as follows: "1 Uses AlgoManager with the Betstars tennis engine and the betstars trading rules 2
     * Creates a new event and issues a MATCH_STARTING incident. 3 Verifies that next game winner market is created but
     * is marked as SUSPEND_UNDISPLAY 4 Plays the first point of the match. 5 Verifies that NGW is still marked as
     * SUSPEND_UNDISPLAY 6. tries to do a param find without next game winner market 7.Verifies that PF fails and that
     * no markets get published 8. Plays the second point of the match 9. Verifies that NGW is still marked as
     * SUSPEND_UNDISPLAY 10. Does a successful param find using match winner and next game winner markets 11. Verifies
     * that NGW is now marked as OPEN. 12. Sends a datafeedIncident with status BET_STOP 13. Verifies that NGW is now
     * marked as SUSPEND_DISPLAY and that "FT:TBIM_M", which was SUSPEND_UNDISPLAY, is still SUSPEND_UNDISPLAY 14. Sends
     * a datafeedIncident with status BET_START 15. Verifies that NGW is now marked as OPEN and that "FT:TBIM_M", is
     * still SUSPEND_UNDISPLAY"
     */
    @Test
    public void test() {
        MethodName.log();
        algoManager.autoSyncWithMatchFeed(false);
        algoManager.onlyPublishMarketsFollowingParamChange(true);
        initSpring();
        long eventId = 1234567L;
        TennisMatchFormat tennisMatchFormat = new TennisMatchFormat();
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, tennisMatchFormat, 3);

        // System.out.println(publishedMarkets);
        /*
         * shld not publish any markets until param change or param find
         */
        assertTrue(publishedMarkets == null);
        /*
         * note that if the test fails here then it has not been run with the correct properties set
         */
        BsTennisMatchParams matchParams = (BsTennisMatchParams) publishedMatchParams;
        matchParams.getOnServePctA1().setMean(.6);
        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());
        // System.out.println(publishedMarkets);
        assertFalse(publishedMarkets == null);
        /*
         * go in play
         */
        MatchIncident matchIncident = new TennisMatchIncident(10, TennisMatchIncidentType.MATCH_STARTING, TeamId.A, 1);
        matchIncident.setEventId(eventId);
        publishedMarkets = null;
        algoManager.handleMatchIncident(matchIncident, true);
        // System.out.println(publishedMarkets);
        assertEquals(SuspensionStatus.SUSPENDED_UNDISPLAY,
                        publishedMarkets.getMarkets().get("G:ML_S1.2").getMarketStatus().getSuspensionStatus());
        assertEquals("Suspend without INPF",
                        publishedMarkets.getMarkets().get("G:ML_S1.2").getMarketStatus().getSuspensionStatusReason());
        /*
         * play the first point
         */
        MatchIncident matchIncident2 = new TennisMatchIncident(20, TennisMatchIncidentType.POINT_WON, TeamId.A, 1);

        matchIncident2.setEventId(eventId);

        publishedMarkets = null;
        algoManager.handleMatchIncident(matchIncident2, true);
        // System.out.println(publishedMarkets);
        assertEquals(SuspensionStatus.SUSPENDED_UNDISPLAY,
                        publishedMarkets.getMarkets().get("G:ML_S1.2").getMarketStatus().getSuspensionStatus());
        assertEquals("Suspend without INPF",
                        publishedMarkets.getMarkets().get("G:ML_S1.2").getMarketStatus().getSuspensionStatusReason());
        /*
         * do a param find which does not have G:ML
         */
        publishedMarkets = null;
        TriggerParamFindTradingRulesResult result =
                        algoManager.handleSupplyMarketPrices(eventId, getTestMarketPrices(false));
        assertFalse(result.isParamFindScheduled());
        assertTrue(publishedMarkets == null); // no calc done if pf not executed
        /*
         * play the second point
         */
        algoManager.handleMatchIncident(matchIncident2, true);
        // System.out.println(publishedMarkets);
        assertEquals(SuspensionStatus.SUSPENDED_UNDISPLAY,
                        publishedMarkets.getMarkets().get("G:ML_S1.2").getMarketStatus().getSuspensionStatus());
        assertEquals("Suspend without INPF",
                        publishedMarkets.getMarkets().get("G:ML_S1.2").getMarketStatus().getSuspensionStatusReason());

        /*
         * do a param find which includes both required markets - i.e. FT:ML and G:ML for set 1 game2
         */

        publishedMarkets = null;
        algoManager.handleSupplyMarketPrices(eventId, getTestMarketPrices(true));
        // System.out.println(publishedParamFinderResults);
        assertTrue(publishedParamFinderResults.isSuccess());
        // System.out.println(publishedMarkets);
        assertEquals(SuspensionStatus.OPEN,
                        publishedMarkets.getMarkets().get("G:ML_S1.2").getMarketStatus().getSuspensionStatus());
        /*
         * make the datafeed go down. First verify the status of a market that isn't open right now
         */
        assertEquals(SuspensionStatus.SUSPENDED_UNDISPLAY,
                        publishedMarkets.getMarkets().get("FT:SPRD#-0.5_M").getMarketStatus().getSuspensionStatus());
        assertEquals("Not open in play", publishedMarkets.getMarkets().get("FT:SPRD#-0.5_M").getMarketStatus()
                        .getSuspensionStatusReason());
        MatchIncident matchIncident3 = new DatafeedMatchIncident(DatafeedMatchIncidentType.BET_STOP, 30);
        matchIncident3.setEventId(eventId);
        algoManager.handleMatchIncident(matchIncident3, true);
        // System.out.println(publishedMarkets);
        assertEquals(SuspensionStatus.SUSPENDED_DISPLAY,
                        publishedMarkets.getMarkets().get("G:ML_S1.2").getMarketStatus().getSuspensionStatus());
        assertEquals("Datafeed state is: BET_STOP",
                        publishedMarkets.getMarkets().get("G:ML_S1.2").getMarketStatus().getSuspensionStatusReason());
        assertEquals(SuspensionStatus.SUSPENDED_UNDISPLAY,
                        publishedMarkets.getMarkets().get("FT:SPRD#-0.5_M").getMarketStatus().getSuspensionStatus());
        assertEquals("Not open in play", publishedMarkets.getMarkets().get("FT:SPRD#-0.5_M").getMarketStatus()
                        .getSuspensionStatusReason());
        /*
         * bring the datafeed back up and verify that markets revert to their previous state
         */
        MatchIncident matchIncident4 = new DatafeedMatchIncident(DatafeedMatchIncidentType.BET_START, 40);
        matchIncident4.setEventId(eventId);
        algoManager.handleMatchIncident(matchIncident4, true);
        // System.out.println(publishedMarkets);
        assertEquals(SuspensionStatus.OPEN,
                        publishedMarkets.getMarkets().get("G:ML_S1.2").getMarketStatus().getSuspensionStatus());
        assertEquals(SuspensionStatus.SUSPENDED_UNDISPLAY,
                        publishedMarkets.getMarkets().get("FT:SPRD#-0.5_M").getMarketStatus().getSuspensionStatus());
        assertEquals("Not open in play", publishedMarkets.getMarkets().get("FT:SPRD#-0.5_M").getMarketStatus()
                        .getSuspensionStatusReason());

    }

    private MarketPricesList getTestMarketPrices(boolean withNextGameWinner) {
        MarketPrices m = new MarketPrices();
        m.setSourceWeight(1);

        MarketPrice matchWinner = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null);

        matchWinner.put("A", 1.86);
        matchWinner.put("B", 1.88);
        m.addMarketPrice(matchWinner);
        if (withNextGameWinner) {
            MarketPrice nextGameWinner = new MarketPrice("G:ML", "NextGameWinner", MarketCategory.GENERAL, "", "S1.2");
            nextGameWinner.put("A", 1.82);
            nextGameWinner.put("B", 1.92);
            m.addMarketPrice(nextGameWinner);
        }
        MarketPricesList marketPricesList = new MarketPricesList();
        marketPricesList.put("MarathonBet", m);
        marketPricesList.put("Unibet", m); // add second copy of prices to
                                           // satisfy trading rules
        return marketPricesList;
    }

    private static void initSpring() {
        String componentScanPackage = System.getProperty("algomgr.springBridgeComponentScanPackage");
        // System.out.println("algomgr.springBridgeComponentScanPackage = " + componentScanPackage);
        if (componentScanPackage != null) {
            // System.out.println("initialising spring context...");
            String[] packages = componentScanPackage.split(",");
            AnnotationConfigApplicationContext springApplicationContext =
                            new AnnotationConfigApplicationContext(packages);
            SpringContextBridge.newApplicationContext(springApplicationContext);
        }
    }

    @Override
    public void publishMatchParams(long eventId, GenericMatchParams matchParams) {
        this.publishedMatchParams = matchParams.generateXxxMatchParams();
        printResults("matchParams", matchParams);
    }

    @Override
    public void publishMatchState(long eventId, SimpleMatchState simpleMatchState) {
        this.publishedMatchState = simpleMatchState;
        printResults("SimpleMatchState", simpleMatchState);
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
    public void notifyFatalError(long eventId, String requestId, String errorCause) {}

    private void printResults(String resultsDescription, Object results) {
        /*
         * // System.out.printf("Published %s for event: %s \n", resultsDescription); //
         * System.out.print(results.toString()); // System.out.printf("--- Published %s ends ---\n\n",
         * resultsDescription);
         */
    }

    @Override
    public void publishTraderAlert(long eventId, TraderAlert traderAlert) {

    }

    @Override
    public void publishEventState(long eventId, EventStateBlob eventStateBlob) {}

    @Override
    public void publishEventSuspensionStatus(long eventId, boolean suspend, Set<MarketGroup> marketGroups) {

    }

    @Override
    public void publishEventProperties(long eventId, Map<String, String> properties) {

    }

}
