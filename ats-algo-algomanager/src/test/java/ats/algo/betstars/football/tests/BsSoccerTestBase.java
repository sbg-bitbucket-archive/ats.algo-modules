package ats.algo.betstars.football.tests;

import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;

import ats.algo.algomanager.AlgoManager;
import ats.algo.algomanager.AlgoManagerConfiguration;
import ats.algo.algomanager.AlgoManagerPublishable;
import ats.algo.algomanager.EventStateBlob;
import ats.algo.algomanager.JmsAlgoManagerConfiguration;
import ats.algo.algomanager.SupportedSportsInitialisation;
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

/**
 * Base class for use when writing unit tests for AlgoManager
 * 
 * NOTE - NOT YET WORKING PROPERLY WITH JMSALGOMANAGERCONFIGURATION
 * 
 * @author Geoff
 *
 */
public class BsSoccerTestBase implements AlgoManagerPublishable {

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
    protected TimeStamp publishedTimeStamp;
    protected GenericMatchParams publishedUpdatedMatchParams;

    @Before
    public void setProperties() {
        System.setProperty("algo.soccer.externalModel", "true");
        System.setProperty("algo.soccer.clientTradingRules", "true");
        System.setProperty("algo.soccer.clientResultingOnly", "true");
        System.setProperty("algo.soccer.clientParamFinding", "true");
        System.setProperty("algo.soccer.tradingrules.class",
                        "ats.algo.sport.football.bs.tradingrules.BsFootballTradingRules");
    }

    @After
    public void clearProperties() {
        System.clearProperty("algo.soccer.externalModel");
        System.clearProperty("algo.soccer.clientTradingRules");
        System.clearProperty("algo.soccer.clientResultingOnly");
        System.clearProperty("algo.soccer.clientParamFinding");
        System.clearProperty("algo.soccer.tradingrules.class");
    }

    public BsSoccerTestBase() {
        // LogUtil.initConsoleLogging(Level.TRACE);
        // System.setProperty("urlForExternalModelsMqBroker",
        // "tcp://iomsampss01.amelco.lan:61616?connectionTimeout=0&wireFormat.maxInactivityDuration=0");


        AlgoManagerConfiguration algoManagerConfiguration = new JmsAlgoManagerConfiguration(
                        "tcp://iomsampss01.amelco.lan:61616?connectionTimeout=0&wireFormat.maxInactivityDuration=0",
                        false);

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



    @Override
    public void publishMatchParams(long eventId, GenericMatchParams matchParams) {
        this.publishedMatchParams = matchParams.generateXxxMatchParams();
    }

    @Override
    public void publishMatchState(long eventId, SimpleMatchState simpleMatchState) {
        this.publishedMatchState = simpleMatchState;
    }

    @Override
    public void publishMarkets(long eventId, Markets markets, Set<String> keysForDiscontinuedMarkets,
                    TimeStamp timeStamp, String sequenceId) {
        this.publishedMarkets = markets;
        this.keysForDiscontinuedMarkets = keysForDiscontinuedMarkets;
        this.publishedTimeStamp = timeStamp;
    }

    @Override
    public void publishResultedMarkets(long eventId, ResultedMarkets resultedMarkets) {
        this.publishedResultedMarkets = resultedMarkets;
    }

    @Override
    public void publishParamFindResults(long eventId, ParamFindResults paramFindResults, GenericMatchParams matchParams,
                    long elapsedTimeMs) {
        this.publishedParamFinderResults = paramFindResults;
        this.publishedUpdatedMatchParams = matchParams;
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



    @Override
    public void publishEventProperties(long eventId, Map<String, String> properties) {

    }

}
