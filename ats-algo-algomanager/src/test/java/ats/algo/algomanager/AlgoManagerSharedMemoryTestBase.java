package ats.algo.algomanager;

import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.comparetomarket.ParamFindResults;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.matchresult.MatchResultMap;
import ats.algo.core.recordplayback.RecordedItem;
import ats.algo.core.recordplayback.Recording;
import ats.algo.core.timestamp.TimeStamp;
import ats.algo.core.traderalert.TraderAlert;
import ats.algo.core.tradingrules.SetSuspensionStatusTradingRule;
import ats.core.util.log.Level;
import ats.core.util.log.LogUtil;

/**
 * Base class for use when writing unit tests for AlgoManager
 * 
 * @author Geoff
 *
 */
public class AlgoManagerSharedMemoryTestBase implements AlgoManagerPublishable {

    protected AlgoManager algoManager;
    protected MatchParams publishedMatchParams;
    protected SimpleMatchState publishedMatchState;
    protected Markets publishedMarkets;
    protected ResultedMarkets publishedResultedMarkets;
    protected ParamFindResults publishedParamFinderResults;
    protected MatchResultMap matchResultProforma;
    protected Boolean publishedNotifyEventCompleted;
    protected Set<String> keysForDiscontinuedMarkets;
    protected Recording recording;
    protected TraderAlert traderAlert;
    protected EventStateBlob publishedEventStateBlob;
    protected Long publishedNotifyFatalError;

    @Before
    public void beforeEachTest() {
        /*
         * define the various functions that AlgoManager is going to need
         */

        LogUtil.initConsoleLogging(Level.TRACE);

        AlgoManagerConfiguration algoManagerConfiguration = new SharedMemoryEnvironmentAlgoManagerConfiguration();
        SupportedSportsInitialisation.init();
        /*
         * instantiate AlgoManager and tell it what functions use to calc prices and report the results
         */

        algoManager = new AlgoManager(algoManagerConfiguration, this);
        algoManager.onlyPublishMarketsFollowingParamChange(false);
        SetSuspensionStatusTradingRule[] tradingRules = new SetSuspensionStatusTradingRule[0];
        algoManager.setTradingRules(SupportedSportType.TENNIS, tradingRules);
        recording = new Recording();
    }

    @After
    public void afterEachTest() {
        algoManager.close();
    }



    @Override
    public void publishMatchParams(long eventId, GenericMatchParams matchParams) {
        this.publishedMatchParams = matchParams.generateXxxMatchParams();
    }

    @Override
    public void publishMatchState(long eventId, SimpleMatchState matchState) {
        this.publishedMatchState = matchState;
    }

    @Override
    public void publishMarkets(long eventId, Markets markets, Set<String> keysForDiscontinuedMarkets,
                    TimeStamp timeStamp, String sequenceId) {
        this.publishedMarkets = markets;
        this.keysForDiscontinuedMarkets = keysForDiscontinuedMarkets;
    }

    @Override
    public void publishResultedMarkets(long eventId, ResultedMarkets resultedMarkets) {
        this.publishedResultedMarkets = resultedMarkets;
    }

    @Override
    public void publishParamFindResults(long eventId, ParamFindResults paramFindResults, GenericMatchParams matchParams,
                    long elapsedTimeMs) {
        this.publishedParamFinderResults = paramFindResults;
    }

    @Override
    public void notifyEventCompleted(long eventId, boolean isCompleted) {
        this.publishedNotifyEventCompleted = isCompleted;
    }


    @Override
    public void publishRecordedItem(long eventId, RecordedItem recordedItem) {
        recording.add(recordedItem);
    }

    @Override
    public void notifyFatalError(long eventId, String requestId, String errorCause) {}

    @Override
    public void publishTraderAlert(long eventId, TraderAlert traderAlert) {
        this.traderAlert = traderAlert;
    }

    @Override
    public void publishEventState(long eventId, EventStateBlob eventStateBlob) {
        this.publishedEventStateBlob = eventStateBlob;
    }

    @Override
    public void publishEventSuspensionStatus(long eventId, boolean suspend, Set<MarketGroup> marketGroups) {

    }

    protected void setPublishedDataToNull() {
        publishedMatchParams = null;
        publishedMatchState = null;
        publishedMarkets = null;
        publishedResultedMarkets = null;
        publishedParamFinderResults = null;
        publishedNotifyEventCompleted = null;
    }

    protected void waitOnPublishedMatchParams() {
        while (publishedMatchParams == null) {
            try {
                // System.out.println("main thread waiting...");
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    protected void waitOnPublishedMarkets() {
        while (publishedMarkets == null) {
            try {
                // System.out.println("main thread waiting...");
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected void waitOnPublishedParamFindResults() {
        while (publishedParamFinderResults == null) {
            try {
                // System.out.println("main thread waiting...");
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected void waitOnPublishedResultedMarkets() {
        while (publishedResultedMarkets == null) {
            try {
                // System.out.println("main thread waiting...");
                Thread.sleep(50);
            } catch (InterruptedException e) {
            }
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }
    }

    @Override
    public void publishEventProperties(long eventId, Map<String, String> properties) {

    }


}
