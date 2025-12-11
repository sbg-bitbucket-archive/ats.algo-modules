package ats.algo.betstars;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ats.algo.algomanager.AlgoManager;
import ats.algo.algomanager.AlgoManagerConfiguration;
import ats.algo.algomanager.AlgoManagerPublishable;
import ats.algo.algomanager.EventStateBlob;
import ats.algo.algomanager.SimpleAlgoManagerConfiguration;
import ats.algo.algomanager.SupportedSportsInitialisation;
import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.common.TeamId;
import ats.algo.core.comparetomarket.ParamFindResults;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.matchresult.MatchResultMap;
import ats.algo.core.recordplayback.RecordedItem;
import ats.algo.core.recordplayback.Recording;
import ats.algo.core.timestamp.TimeStamp;
import ats.algo.core.traderalert.TraderAlert;
import ats.algo.core.traderalert.TraderAlert.TraderAlertType;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchIncident;
import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;
import ats.algo.sport.tennis.TennisMatchStateFromFeed;
import ats.algo.springbridge.SpringContextBridge;
import ats.core.util.log.Level;
import ats.core.util.log.LogUtil;

public class FeedStateMismatchClearingTest implements AlgoManagerPublishable {
    AlgoManager algoManager;
    MatchParams publishedMatchParams;
    SimpleMatchState publishedMatchState;
    Markets publishedMarkets;
    ResultedMarkets publishedResultedMarkets;
    ParamFindResults publishedParamFinderResults;
    MatchResultMap matchResultProforma;
    Boolean publishedNotifyEventCompleted;
    TraderAlert traderAlert;
    Set<String> keysForDiscontinuedMarkets;
    Recording recording;

    static {
        BetstarsSportInitialisation.initTennis();
    }

    public FeedStateMismatchClearingTest() {
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
        recording = new Recording();
    }

    @Test
    public void testForClearingAlert() {
        initSpring();
        long eventId = 1234567L;
        TennisMatchFormat tennisMatchFormat = new TennisMatchFormat();
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, tennisMatchFormat);

        algoManager.autoSyncWithMatchFeed(false); // Setting as PS
        TennisMatchIncident startingTennisMatchIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A);
        startingTennisMatchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(startingTennisMatchIncident, true);

        algoManager.handleMatchIncident(getPointWonIncident(eventId, "R0", TeamId.A), true);
        assertNull(traderAlert);
        assertFalse(publishedMatchState.isDatafeedStateMismatch());

        /*
         * Score should be 15-0, send in incident that takes score to 30-0 with TennisMatchStateFromFeed in line
         */

        TennisMatchStateFromFeed tennisMatchStateFromFeed1 = new TennisMatchStateFromFeed(0, 0, 0, 0, 2, 0, TeamId.A);
        TennisMatchIncident tennisMatchIncident1 = getPointWonIncident(eventId, "R1", TeamId.A);
        tennisMatchIncident1.setTennisMatchStateFromFeed(tennisMatchStateFromFeed1);
        System.out.println(publishedMarkets);

        publishedMarkets = null;
        algoManager.handleMatchIncident(tennisMatchIncident1, true);
        // miss match setted to false in the TennisMatchState
        assertFalse(publishedMatchState.isDatafeedStateMismatch());

        assertNull(traderAlert);

        /*
         * All good to here
         */

        /*
         * Set TennisMatchStateFromFeed to 30-30 but incident only takes us 30-15
         */
        System.out.println(publishedMatchState);
        TennisMatchStateFromFeed tennisMatchStateFromFeed2 = new TennisMatchStateFromFeed(0, 0, 0, 0, 2, 2, TeamId.A);
        TennisMatchIncident tennisMatchIncident2 = getPointWonIncident(eventId, "R2", TeamId.B);
        tennisMatchIncident2.setTennisMatchStateFromFeed(tennisMatchStateFromFeed2);
        System.out.println(publishedMarkets);

        publishedMarkets = null;
        algoManager.handleMatchIncident(tennisMatchIncident2, true);
        System.out.println("Trader alert = " + traderAlert);
        System.out.println("JUST CHECKING");
        System.out.println("JUST CHECKING");

        /*
         * Now event must be out of sync
         */

        assertTrue(publishedMatchState.isDatafeedStateMismatch());

        assertTrue(traderAlert.getTraderAlertType().equals(TraderAlertType.MATCH_STATE_MISMATCH));
        System.out.println("Trader alert = " + traderAlert);

        System.out.println("Checking trader alert");

        /*
         * All good to here
         */

        /*
         * Undo last Incident - should start the clearing messages.
         */

        System.out.println("Handling undo");
        algoManager.handleUndoLastMatchIncident(eventId);
        assertTrue(traderAlert.getTraderAlertType().equals(TraderAlertType.MATCH_STATE_MISMATCH_CLEARING));
        System.out.println(publishedMatchState);
        System.out.println("super.publishedMatchState.isDatafeedStateMismatch() = "
                        + publishedMatchState.isDatafeedStateMismatch());
        System.out.println("Trader alert = " + traderAlert);
        System.out.println("Check");

        /*
         * All good to here To summarise, 2 incidents have come in with feed agreeing. 3rd incident has come in (30-15)
         * but feed believes it to be (30-30) Out of sync alert generated We have then undone the incident and we have
         * generated a clearing alert. Next is to pass a trader incident (so no tennisMatchStateFromFeed) thus not
         * generating an alert.
         */
        traderAlert = null;
        TennisMatchIncident tennisMatchIncident3 = getPointWonIncident(eventId, "R3", TeamId.B);
        algoManager.handleMatchIncident(tennisMatchIncident3, true);

        System.out.println(traderAlert);
        assertNull(traderAlert);


        /*
         * Score is now 30-15. We now pass another incident in for Player B. Takes score to 30-30 with the feed
         * agreeing. Creates a cleared alert.
         */
        TennisMatchStateFromFeed tennisMatchStateFromFeed4 = new TennisMatchStateFromFeed(0, 0, 0, 0, 2, 2, TeamId.A);
        TennisMatchIncident tennisMatchIncident4 = getPointWonIncident(eventId, "R4", TeamId.B);
        tennisMatchIncident4.setTennisMatchStateFromFeed(tennisMatchStateFromFeed4);

        algoManager.handleMatchIncident(tennisMatchIncident4, true);

        assertTrue(traderAlert.getTraderAlertType().equals(TraderAlertType.MATCH_STATE_MISMATCH_CLEARED));

        /*
         * Last thing to do is to test another incident coming in and not generate an alert at all.
         */
        traderAlert = null;
        TennisMatchStateFromFeed tennisMatchStateFromFeed5 = new TennisMatchStateFromFeed(0, 0, 0, 0, 3, 2, TeamId.A);
        TennisMatchIncident tennisMatchIncident5 = getPointWonIncident(eventId, "R5", TeamId.A);
        tennisMatchIncident5.setTennisMatchStateFromFeed(tennisMatchStateFromFeed5);

        algoManager.handleMatchIncident(tennisMatchIncident5, true);

        assertNull(traderAlert);

        System.out.println("Finished and passed");
        System.out.println("");

    }

    @Test
    public void testForClearingAlert2() {
        initSpring();
        long eventId = 1234567L;
        TennisMatchFormat tennisMatchFormat = new TennisMatchFormat();
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, tennisMatchFormat);

        algoManager.autoSyncWithMatchFeed(false);
        TennisMatchIncident startingTennisMatchIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A);
        startingTennisMatchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(startingTennisMatchIncident, true);

        algoManager.handleMatchIncident(getPointWonIncident(eventId, "R0", TeamId.A), true);
        assertNull(traderAlert);
        assertFalse(publishedMatchState.isDatafeedStateMismatch());

        /*
         * Score should be 15-0, send in incident that takes score to 30-0 with TennisMatchStateFromFeed inline
         */

        TennisMatchStateFromFeed tennisMatchStateFromFeed1 = new TennisMatchStateFromFeed(0, 0, 0, 0, 2, 0, TeamId.A);
        TennisMatchIncident tennisMatchIncident1 = getPointWonIncident(eventId, "R1", TeamId.A);
        tennisMatchIncident1.setTennisMatchStateFromFeed(tennisMatchStateFromFeed1);
        System.out.println(publishedMarkets);

        publishedMarkets = null;
        algoManager.handleMatchIncident(tennisMatchIncident1, true);
        // miss match setted to false in the TennisMatchState
        assertFalse(publishedMatchState.isDatafeedStateMismatch());

        assertNull(traderAlert);

        /*
         * All good to here
         */

        /*
         * Set TennisMatchStateFromFeed to 1-0, 15-0 but incident only takes us 30-15
         */

        TennisMatchStateFromFeed tennisMatchStateFromFeed2 = new TennisMatchStateFromFeed(0, 0, 1, 0, 1, 0, TeamId.A);
        TennisMatchIncident tennisMatchIncident2 = getPointWonIncident(eventId, "R2", TeamId.B);
        tennisMatchIncident2.setTennisMatchStateFromFeed(tennisMatchStateFromFeed2);

        algoManager.handleMatchIncident(tennisMatchIncident2, true);
        System.out.println("Trader alert = " + traderAlert);

        /*
         * Now event must be out of sync
         */

        assertTrue(publishedMatchState.isDatafeedStateMismatch());

        assertTrue(traderAlert.getTraderAlertType().equals(TraderAlertType.MATCH_STATE_MISMATCH));

        /*
         * All good to here
         */

        /*
         * Trader starts inserting incidents - no state from feed. We generate trader alert.
         */

        System.out.println("Insert incident to generate alert");
        TennisMatchIncident tennisMatchIncident3 = getPointWonIncident(eventId, "R3", TeamId.A);
        algoManager.handleMatchIncident(tennisMatchIncident3, true);

        assertTrue(traderAlert.getTraderAlertType().equals(TraderAlertType.MATCH_STATE_MISMATCH_CLEARING));
        System.out.println(publishedMatchState);
        System.out.println("super.publishedMatchState.isDatafeedStateMismatch() = "
                        + publishedMatchState.isDatafeedStateMismatch());
        System.out.println("Trader alert = " + traderAlert);
        System.out.println("Check");

        /*
         * All good to here To summarise, 2 incidents have come in with feed agreeing. 3rd incident has come in (30-15)
         * but feed believes it to be (1-0, 15-0) Out of sync alert generated We have then insert another incident to
         * take us to 40-15 Next is to pass more incidents without any more alerts. First one is game won by team A.
         */

        traderAlert = null;
        TennisMatchIncident tennisMatchIncident4 = getPointWonIncident(eventId, "R4", TeamId.A);
        algoManager.handleMatchIncident(tennisMatchIncident4, true);

        System.out.println(traderAlert);
        assertNull(traderAlert);

        /*
         * All good - no alert generated. Current score is 1-0, 0-0. Now taking event to 1-0, 30-15 with 2 incidents,
         * hopefully generating no alert.
         */

        TennisMatchIncident tennisMatchIncident5 = getPointWonIncident(eventId, "R5", TeamId.A);
        algoManager.handleMatchIncident(tennisMatchIncident5, true);

        /*
         * 1-0, 15-0
         */

        TennisMatchIncident tennisMatchIncident6 = getPointWonIncident(eventId, "R6", TeamId.A);
        algoManager.handleMatchIncident(tennisMatchIncident6, true);

        /*
         * 1-0, 30-0
         */

        System.out.println(traderAlert);
        assertNull(traderAlert);

        /*
         * Score is now 1-0, 30-0. We now pass another incident in for Player B. Takes score to 1-0, 30-15 with feed
         * agreeing. Creates a cleared alert.
         */
        TennisMatchStateFromFeed tennisMatchStateFromFeed8 = new TennisMatchStateFromFeed(0, 0, 1, 0, 2, 1, TeamId.B);
        TennisMatchIncident tennisMatchIncident8 = getPointWonIncident(eventId, "R8", TeamId.B);
        tennisMatchIncident8.setTennisMatchStateFromFeed(tennisMatchStateFromFeed8);

        algoManager.handleMatchIncident(tennisMatchIncident8, true);

        assertTrue(traderAlert.getTraderAlertType().equals(TraderAlertType.MATCH_STATE_MISMATCH_CLEARED));

        /*
         * Last thing to do is to test another incident coming in and not generate an alert at all. Final score after
         * test is 1-0 30-30 with Team B serving
         */
        traderAlert = null;
        TennisMatchStateFromFeed tennisMatchStateFromFeed9 = new TennisMatchStateFromFeed(0, 0, 1, 0, 2, 2, TeamId.B);
        TennisMatchIncident tennisMatchIncident9 = getPointWonIncident(eventId, "R9", TeamId.B);
        tennisMatchIncident9.setTennisMatchStateFromFeed(tennisMatchStateFromFeed9);

        algoManager.handleMatchIncident(tennisMatchIncident9, true);

        assertNull(traderAlert);

        System.out.println("Finished and passed");
        System.out.println("");

    }

    private static void initSpring() {
        String componentScanPackage = System.getProperty("algomgr.springBridgeComponentScanPackage");
        System.out.println("algomgr.springBridgeComponentScanPackage = " + componentScanPackage);
        if (componentScanPackage != null) {
            System.out.println("initialising spring context...");
            String[] packages = componentScanPackage.split(",");
            AnnotationConfigApplicationContext springApplicationContext =
                            new AnnotationConfigApplicationContext(packages);
            SpringContextBridge.newApplicationContext(springApplicationContext);
        }
    }

    TennisMatchIncident getPointWonIncident(long eventId, String requestId, TeamId teamId) {
        TennisMatchIncident matchIncident = new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, teamId);
        matchIncident.setEventId(eventId);
        matchIncident.setIncidentId(requestId);
        return matchIncident;
    }

    @Override
    public void publishTraderAlert(long eventId, TraderAlert traderAlert) {
        this.traderAlert = traderAlert;
    }


    @Override
    public void publishEventState(long eventId, EventStateBlob eventStateBlob) {
        // this.eventStateBlob = eventStateBlob;
    }


    @Override
    public void publishEventSuspensionStatus(long eventId, boolean suspend, Set<MarketGroup> marketGroups) {
        // this.suspend = suspend;
    }

    @Override
    public void publishMatchParams(long eventId, GenericMatchParams matchParams) {
        this.publishedMatchParams = matchParams;

    }

    @Override
    public void publishMatchState(long eventId, SimpleMatchState matchState) {
        this.publishedMatchState = matchState;

    }

    @Override
    public void publishMarkets(long eventId, Markets markets, Set<String> keysForDiscontinuedMarkets,
                    TimeStamp timeStamp, String sequenceId) {
        // this.traderAlert = traderAlert;

    }

    @Override
    public void publishResultedMarkets(long eventId, ResultedMarkets ResultedMarkets) {
        this.publishedResultedMarkets = ResultedMarkets;

    }

    @Override
    public void publishParamFindResults(long eventId, ParamFindResults paramFindResults,
                    GenericMatchParams genericMatchParams, long elapsedTimeMs) {
        this.publishedParamFinderResults = paramFindResults;

    }

    @Override
    public void notifyEventCompleted(long eventId, boolean isCompleted) {
        // this.traderAlert = traderAlert;

    }

    @Override
    public void notifyFatalError(long eventId, String requestId, String errorCause) {
        // this.traderAlert = traderAlert;

    }

    @Override
    public void publishRecordedItem(long eventId, RecordedItem recordedItem) {
        // this.traderAlert = traderAlert;

    }

    @Override
    public void publishEventProperties(long eventId, Map<String, String> properties) {

    }



}
