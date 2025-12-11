package ats.algo.algomanager;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.AlgoMatchParams;
import ats.algo.core.baseclasses.GenericMatchParams;
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
import ats.core.util.log.Logger;
import ats.core.util.log.LoggerFactory;

/**
 * Base class for use when writing unit tests for AlgoManager
 * 
 * @author Geoff
 *
 */
public class AlgoManagerSimpleTestBase implements AlgoManagerPublishable {

    protected static final Logger logger = LoggerFactory.getLogger(AlgoManagerSimpleTestBase.class);

    protected AlgoManager algoManager;
    protected AlgoManagerConfiguration algoManagerConfiguration;
    protected volatile AlgoMatchParams publishedMatchParams;
    protected volatile SimpleMatchState publishedMatchState;
    protected volatile Markets publishedMarkets;
    protected volatile ResultedMarkets publishedResultedMarkets;
    protected volatile ParamFindResults publishedParamFinderResults;
    protected volatile MatchResultMap matchResultProforma;
    protected volatile Boolean publishedNotifyEventCompleted;
    protected volatile Set<String> keysForDiscontinuedMarkets;
    protected volatile Recording recording;
    protected volatile TraderAlert traderAlert;
    protected volatile EventStateBlob publishedEventStateBlob;
    protected volatile TimeStamp publishedTimeStamp;
    protected volatile GenericMatchParams publishedUpdatedMatchParams;
    protected volatile Boolean publishedEventLevelShouldSuspend;
    protected volatile Boolean fatalErrorNotified;
    protected volatile Map<String, String> publishedProperties;
    protected volatile Set<TraderAlert> listOfTraderAlerts;
    protected volatile long publishedEventId;

    @Before
    public void beforeEachTest() {
        /*
         * define the various functions that AlgoManager is going to need
         */

        // LogUtil.initConsoleLogging(Level.TRACE);

        algoManagerConfiguration = new SimpleAlgoManagerConfiguration();
        SupportedSportsInitialisation.init();

        /*
         * instantiate AlgoManager and tell it what functions use to calc prices and report the results
         */

        algoManager = new AlgoManager(algoManagerConfiguration, this);
        SetSuspensionStatusTradingRule[] tradingRules = new SetSuspensionStatusTradingRule[0];
        algoManager.setTradingRules(SupportedSportType.TENNIS, tradingRules);
        algoManager.useTraderAlertInsteadOfParamFindAlertDisplay(false);

        /*
         * reset all properties to null
         */
        publishedMatchParams = null;
        publishedMatchState = null;
        publishedMarkets = null;
        publishedResultedMarkets = null;
        publishedParamFinderResults = null;
        matchResultProforma = null;
        publishedNotifyEventCompleted = null;
        keysForDiscontinuedMarkets = null;
        recording = new Recording();
        traderAlert = null;
        publishedTimeStamp = null;
        publishedUpdatedMatchParams = null;
        publishedEventLevelShouldSuspend = null;
        fatalErrorNotified = null;
        publishedProperties = null;
        listOfTraderAlerts = new HashSet<TraderAlert>();
        this.publishedEventId = 0L;
    }

    @After
    public void afterEachTest() {
        algoManager.close();
        algoManager = null;
        algoManagerConfiguration = null;

    }

    @Override
    public void publishMatchParams(long eventId, GenericMatchParams matchParams) {
        this.publishedMatchParams = matchParams.generateXxxMatchParams();
        this.publishedEventId = publishedMatchParams.getEventId();
    }

    @Override
    public void publishMatchState(long eventId, SimpleMatchState matchState) {
        this.publishedMatchState = matchState;
        this.publishedEventId = publishedMatchState.getEventId();
    }

    @Override
    public void publishMarkets(long eventId, Markets markets, Set<String> keysForDiscontinuedMarkets,
                    TimeStamp timeStamp, String sequenceId) {
        this.publishedMarkets = markets;
        this.keysForDiscontinuedMarkets = keysForDiscontinuedMarkets;
        this.publishedTimeStamp = timeStamp;
        this.publishedEventId = eventId;
    }

    @Override
    public void publishResultedMarkets(long eventId, ResultedMarkets resultedMarkets) {
        this.publishedResultedMarkets = resultedMarkets;
        this.publishedEventId = eventId;
    }

    @Override
    public void publishParamFindResults(long eventId, ParamFindResults paramFindResults, GenericMatchParams matchParams,
                    long elapsedTimeMs) {
        this.publishedParamFinderResults = paramFindResults;
        this.publishedUpdatedMatchParams = matchParams;
        this.publishedEventId = eventId;
    }

    @Override
    public void notifyEventCompleted(long eventId, boolean isCompleted) {
        this.publishedNotifyEventCompleted = isCompleted;
        this.publishedEventId = eventId;
    }

    @Override
    public void publishRecordedItem(long eventId, RecordedItem recordedItem) {
        recording.add(recordedItem);
        this.publishedEventId = eventId;
    }

    @Override
    public void publishTraderAlert(long eventId, TraderAlert traderAlert) {
        this.traderAlert = traderAlert;
        this.publishedEventId = eventId;
    }

    @Override
    public void publishEventState(long eventId, EventStateBlob eventStateBlob) {
        this.publishedEventStateBlob = eventStateBlob;
        this.publishedEventId = eventId;
    }

    @Override
    public void publishEventSuspensionStatus(long eventId, boolean shouldSuspend, Set<MarketGroup> marketGroups) {
        this.publishedEventLevelShouldSuspend = shouldSuspend;
        this.publishedEventId = eventId;
    }

    @Override
    public void notifyFatalError(long eventId, String requestId, String errorCause) {
        fatalErrorNotified = true;
        this.publishedEventId = eventId;
    }

    @Override
    public void publishEventProperties(long eventId, Map<String, String> properties) {
        this.publishedProperties = properties;
        this.publishedEventId = eventId;
    }

    protected void setPublishedDataToNull() {
        publishedMatchParams = null;
        publishedMatchState = null;
        publishedMarkets = null;
        publishedResultedMarkets = null;
        publishedParamFinderResults = null;
        publishedNotifyEventCompleted = null;
    }
}
