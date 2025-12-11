package ats.algo.algomanager.outrights;

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
import ats.algo.core.common.TeamId;
import ats.algo.core.comparetomarket.ParamFindResults;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.recordplayback.RecordedItem;
import ats.algo.core.timestamp.TimeStamp;
import ats.algo.core.traderalert.TraderAlert;
import ats.algo.sport.football.FootballMatchFormat;
import ats.algo.sport.football.FootballMatchIncident;
import ats.algo.sport.football.FootballMatchParams;
import ats.algo.sport.football.FootballMatchIncident.FootballMatchIncidentType;
import ats.algo.sport.outrights.OutrightsMatchFormat;
import ats.core.util.log.Level;
import ats.core.util.log.LogUtil;

public class OutrightsTest implements AlgoManagerPublishable {

    AlgoManager algoManager;

    ResultedMarkets publishedResultedMarkets;

    public OutrightsTest() {
        LogUtil.initConsoleLogging(Level.TRACE);
    }

    private void initialiseAlgoManager() {
        AlgoManagerConfiguration algoManagerConfiguration = new AlgoManagerConfigurationForOutrightsTest();
        SupportedSportsInitialisation.init();
        algoManager = new AlgoManager(algoManagerConfiguration, this);
    }

    static final long matchEventID = 11L;
    static final long outrightsEventID = 22L;

    private boolean outrightsMarketPublished = false;

    @Test
    public void outrightsTest() {
        /*
         * start by running the standard internal resulting logic
         */
        initialiseAlgoManager();
        algoManager.handleNewEventCreation(SupportedSportType.SOCCER, matchEventID, new FootballMatchFormat());
        /*
         * create the outrights event. It should add 11L to it's watchList
         */
        algoManager.handleNewEventCreation(SupportedSportType.OUTRIGHTS, outrightsEventID, new OutrightsMatchFormat());
        FootballMatchParams params = new FootballMatchParams();
        params.setEventId(matchEventID);
        params.setGoalSupremacy(3.8, 0.17);
        algoManager.handleSetMatchParams(params.generateGenericMatchParams());
        FootballMatchIncident incident = new FootballMatchIncident(FootballMatchIncidentType.GOAL, 0, TeamId.A);
        incident.setEventId(matchEventID);
        /*
         * issuing goal incident should trigger Outrights recalc. the recalc should include the FT:CS market from the
         * goal incident
         */
        algoManager.handleMatchIncident(incident, true);
        assertTrue(outrightsMarketPublished);
        algoManager.close();

    }

    @Override
    public void publishMatchParams(long eventId, GenericMatchParams matchParams) {}

    @Override
    public void publishMatchState(long eventId, SimpleMatchState matchState) {}

    @Override
    public void publishMarkets(long eventId, Markets markets, Set<String> keysForDiscontinuedMarkets,
                    TimeStamp timeStamp, String sequenceId) {
        System.out.println(markets);
        if (eventId == outrightsEventID)
            outrightsMarketPublished = true;
    }

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
