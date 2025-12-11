package ats.algo.loadtester;

import ats.core.AtsBean;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.TreeMap;

import ats.algo.algomanager.AlgoManager;
import ats.algo.algomanager.AlgoManagerPublishable;
import ats.algo.algomanager.EventStateBlob;
import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.core.common.ElapsedTimeMatchIncident;
import ats.algo.core.common.ElapsedTimeMatchIncident.ElapsedTimeMatchIncidentType;
import ats.algo.core.common.TeamId;
import ats.algo.core.comparetomarket.MarketPrice;
import ats.algo.core.comparetomarket.MarketPrices;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.core.comparetomarket.ParamFindResults;
import ats.algo.core.markets.MarketCategory;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.recordplayback.RecordedItem;
import ats.algo.core.timestamp.TimeStamp;
import ats.algo.core.traderalert.TraderAlert;
import ats.algo.genericsupportfunctions.RandomNoGenerator;
import ats.algo.genericsupportfunctions.StopWatch;
import ats.algo.genericsupportfunctions.Time;
import ats.algo.sport.football.FootballMatchIncident;
import ats.algo.sport.football.FootballMatchIncident.FootballMatchIncidentType;
import ats.algo.margining.*;

public class FootballSaturdayLoadSimulator extends AtsBean implements AlgoManagerPublishable {

    private final static int TEST_DURATION = 1800; // time in secs

    enum RequestType {
        MATCH_INCIDENT,
        START_IN_PLAY,
        PARAM_FIND,
        TIMER
    }

    AlgoManager algoManager;

    private Map<Long, TestMatch> testMatches;

    private RequestLog requestLog;

    // TODO update logic given that requestId's no longer returned in published objects
    private static final String requestId = "XXX";

    volatile boolean userRequestedTerminate;
    Map<Long, Markets> marketsMap;

    /*
     * can change these settings for debug purposes
     */
    boolean killTimer = false;
    boolean dontScoreGoals = true; // better to use DANGEROUS_ATTACK since
                                   // scoring lots of goals risks overflowing
                                   // the
                                   // stats counters
    boolean printDetailedLogInfo = false;
    /*
     * end debug settings
     */

    public FootballSaturdayLoadSimulator() {}

    AlgoManager getAlgoManager() {
        return algoManager;
    }

    void setAlgoManager(AlgoManager algoManager) {
        this.algoManager = algoManager;
    }

    public void executeSaturdayLoadTest(int nMatches, int meanGoalScoreTime, int meanParamFindTime) {
        int[] scoreGoalTime = new int[nMatches];
        int[] paramFindTime = new int[nMatches];
        for (int i = 0; i < nMatches; i++) {
            scoreGoalTime[i] = RandomNoGenerator.nextPoisson(meanGoalScoreTime);
            paramFindTime[i] = RandomNoGenerator.nextPoisson(meanParamFindTime);
            // System.out.printf("match %d: goalScore time: %d, paramFindTime
            // %d\n", i, scoreGoalTime[i],
            // paramFindTime[i]);
        }

        if (killTimer)
            algoManager.killTimer();
        testMatches = new TreeMap<Long, TestMatch>();
        marketsMap = new TreeMap<Long, Markets>();
        requestLog = new RequestLog();
        long eventId;
        /*
         * create the matches
         */
        for (int i = 0; i < nMatches; i++) {
            eventId = 1000 + i;
            TestMatch testMatch;
            testMatch = new FootballTestMatch(eventId);
            testMatches.put(eventId, testMatch);
            requestLog.addEvent(eventId);
            String expectedRequestId = String.format("NEW_EVENT_FOR_EVENTID_%d", eventId);
            requestLog.addLogEntry(eventId, RequestType.MATCH_INCIDENT, expectedRequestId);
            requestLog.addLogEntry(eventId, RequestType.TIMER, "TIMER");
            testMatch.recordIncidentIssued();
            algoManager.handleNewEventCreation(testMatch.getSupportedSport(), eventId, testMatch.getMatchFormat());
        }

        /*
         * put all the matches in-play
         */
        for (int i = 0; i < nMatches; i++) {
            ElapsedTimeMatchIncident incident =
                            new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
            eventId = 1000 + i;
            String requestId = "StartMatch_" + eventId;
            incident.setEventId(eventId);
            incident.setIncidentId(requestId);
            requestLog.addLogEntry(eventId, RequestType.MATCH_INCIDENT, requestId);
            algoManager.handleMatchIncident(incident, true);
            sleep(100); // delay 100Ms between each request
        }
        StopWatch sw = new StopWatch();
        sw.start();
        boolean finished = false;
        int tSecs = 0; // approx elapsed time counter
        do {
            sleep(1000);
            String timeStr = Time.getTimeAsString(sw.getElapsedTimeMs());
            String s = String.format("%s %s", timeStr, algoManager.getStatistics().toStringSingleLineSummary());
            info(s);
            FootballMatchIncidentType incidentType;
            if (dontScoreGoals)
                incidentType = FootballMatchIncidentType.DANGEROUS_ATTACK;
            else
                incidentType = FootballMatchIncidentType.GOAL;
            // requestLog.checkExpectedDataReceived();
            for (int i = 0; i < nMatches; i++) {
                if (tSecs == scoreGoalTime[i]) {
                    TeamId teamId;
                    double rnd = RandomNoGenerator.nextDouble();
                    if (rnd > 0.5)
                        teamId = TeamId.A;
                    else
                        teamId = TeamId.B;
                    FootballMatchIncident incident = new FootballMatchIncident(incidentType, tSecs, teamId);
                    eventId = 1000 + i;
                    String requestId = String.format("%s_event:_%s_time:_%d", incidentType, eventId, tSecs);
                    incident.setEventId(eventId);
                    incident.setIncidentId(requestId);
                    System.out.printf("Issue incident %s for eventId %d\n", incidentType.toString(), eventId);
                    requestLog.addLogEntry(eventId, RequestType.MATCH_INCIDENT, requestId);
                    algoManager.handleMatchIncident(incident, true);
                    scoreGoalTime[i] = tSecs + RandomNoGenerator.nextPoisson(meanGoalScoreTime);
                }
                if (tSecs == paramFindTime[i]) {
                    eventId = 1000 + i;
                    MarketPricesList marketPricesList = getTestMarketPricesList(eventId);
                    String requestId = String.format("ParamFind_event:_%s_time:_%d", eventId, tSecs);
                    System.out.printf("Issue param find request for  %d, %s\n", eventId, requestId);
                    requestLog.addLogEntry(eventId, RequestType.PARAM_FIND, requestId);
                    algoManager.handleSupplyMarketPrices(eventId, marketPricesList);
                    paramFindTime[i] = tSecs + RandomNoGenerator.nextPoisson(meanParamFindTime);
                }
            }
            finished = (sw.getElapsedTimeSecs() > TEST_DURATION);
            tSecs++;
        } while (!finished);
        sleep(1000); // give time for any in flight calcs to finish)

        info("** Finished test **");
        int nErrors = 0;
        for (Entry<Long, Map<String, RequestLogEntry>> entry : requestLog.entrySet()) {
            eventId = entry.getKey();
            for (Entry<String, RequestLogEntry> entry2 : entry.getValue().entrySet()) {
                String requestId = entry2.getKey();
                RequestLogEntry logEntry = entry2.getValue();
                if (!logEntry.isMarketsRcvd()) {
                    info("Markets not received for event: %d, requestId: %s", eventId, requestId);
                    nErrors++;
                }
                if (!logEntry.isParamsRcvd()) {
                    info("Params not received for event: %d, requestId: %s", eventId, requestId);
                    nErrors++;
                }
                if (logEntry.getRequestType() == RequestType.PARAM_FIND && !logEntry.isParamfindResultsRcvd()) {
                    info("Param find results not received for event: %d, requestId: %s", eventId, requestId);
                    nErrors++;
                }
            }
        }
        if (nErrors == 0)
            info("Saturday load test: All expected responses received from AlgoManager");
        else
            info("Saturday load test: not all expected responses received from AlgoManager.  # errors: %d\n\n",
                            nErrors);
        /*
         * remove the matches
         */
        for (int i = 0; i < nMatches; i++) {
            eventId = 1000 + i;
            algoManager.handleRemoveEvent(eventId);
        }
    }

    /**
     * generates a set of marketPrices that are reasonably close to our currently generated markets that can be used for
     * a param find
     *
     * @return
     */
    private MarketPricesList getTestMarketPricesList(long eventId) {
        Markets markets = marketsMap.get(eventId);

        MarketPrices m = new MarketPrices();
        m.setSourceWeight(1);

        double pA = markets.get("FT:AXB").get("A");
        double pB = markets.get("FT:AXB").get("B");
        double pX = markets.get("FT:AXB").get("Draw");
        pA *= 1.01; // move it a bit so not param finding on exactly the
                    // previously generated prob
        pB = 1 - pA - pX;
        MarketPrice ab = new MarketPrice("FT:AXB", "Match winner", MarketCategory.GENERAL, null);
        ab.put("A", MarginingV10.addMargin(pA, 2, .2));
        ab.put("B", MarginingV10.addMargin(pB, 2, .2));
        ab.put("Draw", MarginingV10.addMargin(pX, 2, .2));
        m.addMarketPrice(ab);

        String lineId = markets.get("FT:OU").getLineId();
        pA = markets.get("FT:OU").get("Over");
        pA *= 1.01; // move it a bit so not param finding on exactly the
                    // previously generated prob
        if (pA > 1)
            pA = 0.99;
        MarketPrice tmtg = new MarketPrice("FT:OU", "Total games", MarketCategory.OVUN, lineId);
        tmtg.put("Over", MarginingV10.addMargin(pA, 2, .2));
        tmtg.put("Under", MarginingV10.addMargin(1 - pA, 2, .2));
        m.addMarketPrice(tmtg);
        MarketPricesList marketPricesList = new MarketPricesList();
        marketPricesList.put("TEST", m);
        return marketPricesList;
    }

    private void sleep(int n) {
        try {
            Thread.sleep(n);
        } catch (InterruptedException e) {
        }
    }

    @Override
    public void publishMarkets(long eventId, Markets markets, Set<String> keysForDiscontinuedMarkets,
                    TimeStamp timeStamp, String sequenceId) {
        marketsMap.put(eventId, markets);

        requestLog.recordMarketsReceived(eventId, requestId);
        if (printDetailedLogInfo)
            System.out.printf("Markets published for eventId: %d, requestId: %s\n", eventId, requestId);

    }

    @Override
    public void notifyEventCompleted(long eventId, boolean isCompleted) {
        System.out.printf("Match %d completed\n", eventId);

    }

    @Override
    public void publishParamFindResults(long eventId, ParamFindResults paramFindResults, GenericMatchParams matchParams,
                    long elapsedTimeMs) {
        requestLog.recordParamFindResultsReceived(eventId, requestId);
        if (printDetailedLogInfo)
            System.out.printf("ParamFind results published for eventId: %d, requestId: %s\n", eventId, requestId);
    }

    @Override
    public void publishMatchParams(long eventId, GenericMatchParams matchParams) {
        requestLog.recordParamsReceived(eventId, requestId);
    }

    @Override
    public void publishMatchState(long eventId, SimpleMatchState matchState) {

    }

    @Override
    public void publishResultedMarkets(long eventId, ResultedMarkets ResultedMarkets) {

    }

    @Override
    public void notifyFatalError(long eventId, String requestId, String errorCause) {

    }

    @Override
    public void publishRecordedItem(long eventId, RecordedItem recordedItem) {

    }

    @Override
    public void publishTraderAlert(long eventId, TraderAlert traderAlert) {}

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
