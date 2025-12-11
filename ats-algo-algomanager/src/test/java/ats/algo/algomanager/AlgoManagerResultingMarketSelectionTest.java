package ats.algo.algomanager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.core.common.ElapsedTimeMatchIncident;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.common.TeamId;
import ats.algo.core.common.ElapsedTimeMatchIncident.ElapsedTimeMatchIncidentType;
import ats.algo.core.comparetomarket.ParamFindResults;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarket;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.matchresult.MatchResultMap;
import ats.algo.core.recordplayback.RecordedItem;
import ats.algo.core.recordplayback.Recording;
import ats.algo.core.timestamp.TimeStamp;
import ats.algo.core.traderalert.TraderAlert;
import ats.algo.core.tradingrules.SetSuspensionStatusTradingRule;
import ats.algo.sport.football.FootballMatchFormat;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchIncident;
import ats.algo.sport.tennis.TennisMatchFormat.FinalSetType;
import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;
import ats.core.util.log.Level;
import ats.core.util.log.LogUtil;

public class AlgoManagerResultingMarketSelectionTest implements AlgoManagerPublishable {

    AlgoManager algoManager;
    MatchParams publishedMatchParams;
    SimpleMatchState publishedMatchState;
    Markets publishedMarkets;
    ResultedMarkets publishedResultedMarkets;
    ParamFindResults publishedParamFinderResults;
    MatchResultMap matchResultProforma;
    Boolean publishedNotifyEventCompleted;
    Set<String> keysForDiscontinuedMarkets;


    public AlgoManagerResultingMarketSelectionTest() {
        /*
         * define the various functions that AlgoManager is going to need
         */

        LogUtil.initConsoleLogging(Level.TRACE);

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
    public void testResultedMarkets() {
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
        assertEquals(0, publishedResultedMarkets.size() - publishedResultedMarkets.getFullyResultedMarkets().size());
        ResultedMarket resultedMarket = publishedResultedMarkets.getResultedMarkets().get("P:CS_S1");
        if (resultedMarket != null)
            assertTrue(!resultedMarket.isFullyResulted());
        assertTrue(publishedResultedMarkets.getResultedMarkets().get("G:ML_S1.1").isFullyResulted());
    }


    @Test
    @Ignore
    public void testFootballResultedMarkets() {


        FootballMatchFormat matchFormat = new FootballMatchFormat();
        long eventId = 789L;

        algoManager.handleNewEventCreation(SupportedSportType.SOCCER, eventId, matchFormat);
        System.out.printf("End of part 1\n");
        ElapsedTimeMatchIncident incidentStart =
                        new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
        incidentStart.setEventId(eventId);
        incidentStart.setIncidentId("R1");
        algoManager.handleMatchIncident(incidentStart, true);
        ElapsedTimeMatchIncident incidentEnd =
                        new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 0);
        incidentEnd.setIncidentId("R2");
        incidentEnd.setEventId(eventId);
        algoManager.handleMatchIncident(incidentEnd, true);
        sleep(6);
        algoManager.handleMatchIncident(incidentStart, true);
        incidentStart.setIncidentId("R3");
        sleep(6);
        algoManager.handleMatchIncident(incidentEnd, true);
        sleep(6);
        incidentEnd.setIncidentId("R4");
        System.out.println(publishedResultedMarkets);
        System.out.print(publishedResultedMarkets.toString());
        ResultedMarket resultedMarket = publishedResultedMarkets.getResultedMarkets().get("FT:20MG_M");
        assertTrue(resultedMarket != null);
        // assertEquals("S1.1.2", resultedMarket.getSequenceId());
        // assertEquals("A", resultedMarket.getWinningSelections().get(0));
        ResultedMarket resultedMarket2 = publishedResultedMarkets.getResultedMarkets().get("FT:60MR_F0");
        assertTrue(resultedMarket2 != null);
        ResultedMarket resultedMarket3 = publishedResultedMarkets.getResultedMarkets().get("FT:60MR_F1");
        assertTrue(resultedMarket3 == null);
        ResultedMarket resultedMarket4 = publishedResultedMarkets.getResultedMarkets().get("FT:75MR_F0");
        assertTrue(resultedMarket4 != null);
    }



    private static void sleep(int nSecs) {
        for (int i = 0; i < nSecs; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.printf("Waiting... %d secs (of %d)\n", i + 1, nSecs);
        }
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

    @Override
    public void notifyFatalError(long eventId, String requestId, String errorCause) {

    }


    @Override
    public void publishTraderAlert(long eventId, TraderAlert traderAlert) {

    }

    @Override
    public void publishEventState(long eventId, EventStateBlob eventStateBlob) {

    }

    @Override
    public void publishEventSuspensionStatus(long eventId, boolean suspend, Set<MarketGroup> marketGroups) {

    }

    Recording recording;

    @Override
    public void publishRecordedItem(long eventId, RecordedItem recordedItem) {
        recording.add(recordedItem);
    }

    private void printResults(String resultsDescription, Object results) {
        /*
         * System.out.printf("Published %s for event: %s \n", resultsDescription); System.out.print(results.toString());
         * System.out.printf("--- Published %s ends ---\n\n", resultsDescription);
         */
    }

    @Override
    public void publishEventProperties(long eventId, Map<String, String> properties) {

    }

}
