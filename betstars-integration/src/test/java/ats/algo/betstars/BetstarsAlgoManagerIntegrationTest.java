package ats.algo.betstars;

import org.junit.BeforeClass;

import com.betstars.algo.ats.integration.BsTennisMatchParams;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.Set;

import ats.algo.algomanager.AlgoManager;
import ats.algo.algomanager.AlgoManagerConfiguration;
import ats.algo.algomanager.AlgoManagerPublishable;
import ats.algo.algomanager.EventStateBlob;
import ats.algo.algomanager.SimpleAlgoManagerConfiguration;
import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.comparetomarket.ParamFindResults;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.recordplayback.RecordedItem;
import ats.algo.core.timestamp.TimeStamp;
import ats.algo.core.traderalert.TraderAlert;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchFormat.FinalSetType;

/*
 * tests functionality using the single threaded SimpleAlgoManagerConfiguration
 */
public class BetstarsAlgoManagerIntegrationTest implements AlgoManagerPublishable {

    @BeforeClass
    public static void onceExecutedBeforeAll() {
        /*
         * configure to use the betstars match engine
         */
        System.setProperty("algo.tennis.engine.class", "com.betstars.algo.ats.integration.tennis.BsTennisMatchEngine");
    }

    AlgoManager algoManager;
    MatchParams publishedMatchParams;
    SimpleMatchState publishedMatchState;
    Markets publishedMarkets;
    ResultedMarkets publishedResultedMarkets;
    ParamFindResults publishedParamFinderResults;
    Boolean publishedNotifyEventCompleted;

    public BetstarsAlgoManagerIntegrationTest() {
        /*
         * define the various functions that AlgoManager is going to need
         */

        AlgoManagerConfiguration algoManagerConfiguration = new SimpleAlgoManagerConfiguration();
        /*
         * instantiate AlgoManager and tell it what functions use to calc prices and report the results
         */
        algoManager = new AlgoManager(algoManagerConfiguration, this);
        algoManager.onlyPublishMarketsFollowingParamChange(false);
    }

    // @Test
    /*
     * need to sort out setting the spring context before trying to run this test
     */
    public void test() {
        TennisMatchFormat tennisMatchFormat =
                        new TennisMatchFormat(3, FinalSetType.NORMAL_WITH_TIE_BREAK, false, false);
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, 11L, tennisMatchFormat);
        assertEquals(BsTennisMatchParams.class, publishedMatchParams.getClass());
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
        printResults("markets", markets);
    }

    @Override
    public void publishResultedMarkets(long eventId, ResultedMarkets resultedMarkets) {
        this.publishedResultedMarkets = resultedMarkets;
        printResults("resultedMarkets", resultedMarkets);
    }

    @Override
    public void publishParamFindResults(long eventId, ParamFindResults paramFinderResults,
                    GenericMatchParams matchParams, long elapsedTimeMs) {
        this.publishedParamFinderResults = paramFinderResults;
        printResults("paramFinderResults", paramFinderResults);
    }

    @Override
    public void notifyEventCompleted(long eventId, boolean isCompleted) {
        this.publishedNotifyEventCompleted = isCompleted;
    }

    @Override
    public void notifyFatalError(long eventId, String requestId, String errorCause) {}

    @Override
    public void publishRecordedItem(long eventId, RecordedItem recordedItem) {}

    private void printResults(String resultsDescription, Object results) {
        /*
         * System.out.printf("Published %s for event: %s \n", resultsDescription); System.out.print(results.toString());
         * System.out.printf("--- Published %s ends ---\n\n", resultsDescription);
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
