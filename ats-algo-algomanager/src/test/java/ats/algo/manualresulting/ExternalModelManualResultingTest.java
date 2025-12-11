package ats.algo.manualresulting;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.Set;

import org.junit.Test;

import ats.algo.algomanager.AlgoManager;
import ats.algo.algomanager.AlgoManagerConfiguration;
import ats.algo.algomanager.AlgoManagerPublishable;
import ats.algo.algomanager.EventStateBlob;
import ats.algo.algomanager.SupportedSportsInitialisation;
import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.comparetomarket.ParamFindResults;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarket;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.matchresult.MatchResultMap;
import ats.algo.core.recordplayback.RecordedItem;
import ats.algo.core.timestamp.TimeStamp;
import ats.algo.core.traderalert.TraderAlert;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.core.util.log.Level;
import ats.core.util.log.LogUtil;

public class ExternalModelManualResultingTest implements AlgoManagerPublishable {

    AlgoManager algoManager;

    ResultedMarkets publishedResultedMarkets;


    public ExternalModelManualResultingTest() {
        LogUtil.initConsoleLogging(Level.TRACE);
    }

    private void initialiseAlgoManager() {
        AlgoManagerConfiguration algoManagerConfiguration =
                        new AlgoManagerConfigurationForExternalManualResultingTest();
        SupportedSportsInitialisation.init();
        algoManager = new AlgoManager(algoManagerConfiguration, this);
    }

    @Test
    public void testExternalModelResulting() {
        /*
         * start by running the standard internal resulting logic
         */
        initialiseAlgoManager();
        TennisMatchFormat tennisMatchFormat = new TennisMatchFormat();
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, 11L, tennisMatchFormat);
        MatchResultMap matchResultMap = tennisMatchFormat.generateMatchResultProForma();
        matchResultMap.getMap().get("set1Score").setValue("7-6");
        matchResultMap.getMap().get("set2Score").setValue("3-6");
        matchResultMap.getMap().get("set3Score").setValue("5-7");
        matchResultMap.setEventId(11L);

        algoManager.handleManualResultMarkets(matchResultMap);
        System.out.println(publishedResultedMarkets);
        ResultedMarket resultedMarket = publishedResultedMarkets.getResultedMarkets().get("FT:ML_M");
        assertEquals("B", resultedMarket.getWinningSelections().get(0));
        algoManager.close();
        /*
         * set up to use external model
         */
        System.setProperty("algo.tennis.externalModel", "true");
        initialiseAlgoManager();
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, 11L, tennisMatchFormat);
        algoManager.handleManualResultMarkets(matchResultMap);
        System.out.println(publishedResultedMarkets);
        resultedMarket = publishedResultedMarkets.getResultedMarkets().get("FT:ML_M");
        assertEquals("A", resultedMarket.getWinningSelections().get(0));
        algoManager.close();
        System.clearProperty("algo.tennis.externalModel");
    }

    @Override
    public void publishMatchParams(long eventId, GenericMatchParams matchParams) {}

    @Override
    public void publishMatchState(long eventId, SimpleMatchState matchState) {}

    @Override
    public void publishMarkets(long eventId, Markets markets, Set<String> keysForDiscontinuedMarkets,
                    TimeStamp timeStamp, String sequenceId) {}

    @Override
    public void publishResultedMarkets(long eventId, ResultedMarkets resultedMarkets) {
        this.publishedResultedMarkets = resultedMarkets;

    }

    @Override
    public void publishParamFindResults(long eventId, ParamFindResults paramFindResults,
                    GenericMatchParams genericMatchParams, long elapsedTimeMs) {


    }

    @Override
    public void notifyEventCompleted(long eventId, boolean isCompleted) {


    }

    @Override
    public void notifyFatalError(long eventId, String uniqueRequestId, String errorCause) {


    }

    @Override
    public void publishRecordedItem(long eventId, RecordedItem recordedItem) {


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

    @Override
    public void publishEventProperties(long eventId, Map<String, String> properties) {

    }



}
