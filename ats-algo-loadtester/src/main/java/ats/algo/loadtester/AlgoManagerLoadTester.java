package ats.algo.loadtester;

import ats.core.AtsBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.TreeMap;

import ats.algo.algomanager.AlgoManager;
import ats.algo.algomanager.AlgoManagerPublishable;
import ats.algo.algomanager.EventStateBlob;
import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.core.common.ElapsedTimeMatchIncident;
import ats.algo.core.common.ElapsedTimeMatchIncident.ElapsedTimeMatchIncidentType;
import ats.algo.core.common.TeamId;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.core.comparetomarket.ParamFindResults;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.recordplayback.RecordedItem;
import ats.algo.core.timestamp.TimeStamp;
import ats.algo.core.traderalert.TraderAlert;
import ats.algo.genericsupportfunctions.RandomNoGenerator;
import ats.algo.genericsupportfunctions.StopWatch;
import ats.algo.sport.football.FootballMatchIncident;
import ats.algo.sport.football.FootballMatchIncident.FootballMatchIncidentType;
import ats.algo.sport.tennis.TennisMatchIncident;
import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;
import ats.algo.sport.tennis.TennisMatchParams;

// TODO - fix for fact that requestId's no longer returned by pbulsihed items
public class AlgoManagerLoadTester extends AtsBean implements AlgoManagerPublishable {

    private static SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

    private AlgoManager algoManager;
    private Map<Long, TestMatch> testMatches;
    RecordedItem recordedItem;

    public AlgoManagerLoadTester() {}

    AlgoManager getAlgoManager() {
        return algoManager;
    }

    void setAlgoManager(AlgoManager algoManager) {
        this.algoManager = algoManager;
    }

    @FunctionalInterface
    public interface NextIncident {
        /**
         * pushes out the current set of matchParams to any interested downstream listeners
         *
         * @param eventId The id supplied by ATS when the event was created
         *
         * @param paramMap
         */
        public MatchIncident get();
    }

    @FunctionalInterface
    public interface TestCompleted {
        /**
         * pushes out the current set of matchParams to any interested downstream listeners
         *
         * @param eventId The id supplied by ATS when the event was created
         *
         * @param paramMap
         */
        public boolean completed();
    }

    private MatchIncident nextTennisIncident() {
        return new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A);
    }

    private MatchIncident nextFootballIncident() {
        // return new
        // ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_MATCH_CLOCK,
        // 10);
        TeamId teamId;
        double rnd = RandomNoGenerator.nextDouble();
        if (rnd > 0.5)
            teamId = TeamId.A;
        else
            teamId = TeamId.B;
        // teamId = TeamId.A; //DEBUG
        return new FootballMatchIncident(FootballMatchIncidentType.DANGEROUS_ATTACK, 10, teamId);
    }

    void executeRandomTennisMatchesLoadTest(int noMatches) {
        executeRandomMatchesLoadTest(noMatches, new TennisTestMatch(0L));
    }

    public void executeRandomVolleyballMatchesLoadTest(int noMatches) {
        executeRandomMatchesLoadTest(noMatches, new VolleyballTestMatch(0L));

    }

    void executeRandomMatchesLoadTest(int noMatches, TestMatch testMatchExample) {

        testMatches = new TreeMap<Long, TestMatch>();
        long eventId;
        /*
         * create the matches
         */
        for (int i = 0; i < noMatches; i++) {
            eventId = 1000 + i;
            TestMatch testMatch;
            testMatch = testMatchExample.newInstance(eventId);
            testMatches.put(eventId, testMatch);
            testMatch.recordIncidentIssued();
            algoManager.handleNewEventCreation(testMatch.getSupportedSport(), eventId, testMatch.getMatchFormat());
        }

        loadTestInfo(String.format("%d matches created.  Waiting for all pre-match markets to be published...",
                        noMatches));
        waitForAllMarketsToPublish();
        loadTestInfo("Starting in play test");
        for (Entry<Long, TestMatch> entry : testMatches.entrySet()) {
            entry.getValue().resetNoIncidentsGenerated();
        }

        StopWatch sw = new StopWatch();
        sw.start();
        int nFinished;
        do {
            nFinished = 0;
            for (Entry<Long, TestMatch> entry : testMatches.entrySet()) {
                eventId = entry.getKey();
                TestMatch testMatch = entry.getValue();
                if (testMatch.isMatchCompleted()) {
                    loadTestInfo(String.format("MatchCompleted for %s %s", testMatch.eventId,
                                    testMatch.getLastRequestId()));

                    nFinished++;
                } else if (testMatch.isMarketsPublished()) {
                    loadTestInfo(String.format("Published markets received for %s %s", testMatch.eventId,
                                    testMatch.getLastRequestId()));
                    testMatch.recordIncidentIssued();
                    algoManager.handleMatchIncident(testMatch.getNextIncident(), true);
                }
            }
            if (nFinished < noMatches) {
                sleep(200);
            }
            checkWhetherToLogStatus();
        } while (nFinished < noMatches);
        sw.stop();
        double elapsedTime = sw.getElapsedTimeSecs();
        int nIncidents = getTotalNoOfIncidents();
        double averageTimePerIncident = elapsedTime / nIncidents;
        loadTestInfo(String.format(
                        "Finished in %.1f seconds.  Total no of incidents: %d,  Average time per incident: %.3f seconds",
                        elapsedTime, nIncidents, averageTimePerIncident));
        loadTestInfo("-\n---Summary for each event----");
        for (Entry<Long, TestMatch> entry : testMatches.entrySet()) {
            eventId = entry.getKey();
            TestMatch testMatch = entry.getValue();
            loadTestInfo(String.format("  Event %d (%s): %s", eventId, testMatch.getMatchDescription(),
                            testMatch.getSummaryStats()));
        }
        loadTestInfo("----Summary ends----\n");
    }

    static int nConcurrentEventsForTest = 48;

    void executeConcurrentEventsTestTennis() {
        long eventId = 1000L;
        TestMatch testMatch = new TennisTestMatch(eventId);
        MatchIncident startingIncident = new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A);
        NextIncident nextIncident = () -> nextTennisIncident();
        TestCompleted testCompleted = () -> testMatch.isMatchCompleted();
        executeConcurrentEventsTest(eventId, testMatch, startingIncident, nextIncident, testCompleted);
    }

    void executeConcurrentEventsTestFootball() {
        long eventId = 2000L;
        TestMatch testMatch = new FootballTestMatch(eventId);
        NextIncident nextIncident = () -> nextFootballIncident();
        TestCompleted testCompleted = () -> nPublishedMarkets >= nConcurrentEventsForTest + 1;
        MatchIncident startingIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
        executeConcurrentEventsTest(eventId, testMatch, startingIncident, nextIncident, testCompleted);
    }

    private void executeConcurrentEventsTest(long eventId, TestMatch testMatch, MatchIncident startingIncident,
                    NextIncident nextIncident, TestCompleted testCompleted) {

        testMatches = new TreeMap<Long, TestMatch>();
        testMatches.put(eventId, testMatch);
        algoManager.handleNewEventCreation(testMatch.getSupportedSport(), eventId, testMatch.getMatchFormat());
        loadTestInfo("Match created.  Waiting for pre-match markets to be published...");
        waitForAllMarketsToPublish();
        StopWatch sw = new StopWatch();
        /*
         * fire off all of the incidents at once
         */
        nPublishedMarkets = 0;
        startingIncident.setEventId(eventId);
        String requestId = String.format("MatchStarting_for_eventId_%d", eventId);
        startingIncident.setIncidentId(requestId);
        loadTestInfo(String.format("Incident issued for %s", requestId));
        algoManager.handleMatchIncident(startingIncident, true);
        for (int i = 0; i < nConcurrentEventsForTest; i++) {
            MatchIncident eventIncident = nextIncident.get();
            eventIncident.setEventId(eventId);
            requestId = String.format("Event_%d_Request_%d", eventId, i + 1);
            eventIncident.setIncidentId(requestId);
            loadTestInfo(String.format("Incident issued for %s", requestId));
            algoManager.handleMatchIncident(eventIncident, true);
        }
        sw.start();
        boolean finished = false;
        while (!finished) {
            checkWhetherToLogStatus();
            synchronized (testMatches) {
                finished = testCompleted.completed();
            }
            sleep(200);
        }
        sw.stop();
        loadTestInfo(String.format("** Test finished in %.1f seconds **", sw.getElapsedTimeSecs()));
    }

    void executeFootballCalcsTest(int noMatches) {

        testMatches = new TreeMap<Long, TestMatch>();
        long eventId;
        /*
         * create the matches
         */
        for (int i = 0; i < noMatches; i++) {
            eventId = 1000 + i;
            TestMatch testMatch = new FootballTestMatch(eventId);
            testMatches.put(eventId, testMatch);
            testMatch.recordIncidentIssued();
            algoManager.handleNewEventCreation(testMatch.getSupportedSport(), eventId, testMatch.getMatchFormat());
        }
        loadTestInfo(String.format("%d matches created.  Waiting for all pre-match markets to be published...",
                        noMatches));
        waitForAllMarketsToPublish();
        for (Entry<Long, TestMatch> entry : testMatches.entrySet()) {
            entry.getValue().resetNoIncidentsGenerated();
        }
        loadTestInfo("Starting calc test");

        StopWatch sw = new StopWatch();
        sw.start();
        int nFinished = 0;
        do {
            MatchParams matchParams = null;
            synchronized (testMatches) {
                for (Entry<Long, TestMatch> entry : testMatches.entrySet()) {
                    eventId = entry.getKey();
                    TestMatch testMatch = entry.getValue();
                    if (testMatch.isMatchCompleted()) {
                        loadTestInfo(String.format("Match completed for %s", testMatch.getLastRequestId()));
                        nFinished++;
                    } else if (testMatch.isMarketsPublished()) {
                        loadTestInfo(String.format("Published markets received for %s", testMatch.getLastRequestId()));
                        testMatch.recordIncidentIssued();
                        matchParams = testMatch.getNextMatchParams();
                    }
                }
            }
            if (matchParams != null)
                algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());
            if (nFinished < noMatches) {
                sleep(200);
            }
            checkWhetherToLogStatus();
        } while (nFinished < noMatches);
        sw.stop();
        double elapsedTime = sw.getElapsedTimeSecs();
        int nIncidents = getTotalNoOfIncidents();
        double averageTimePerIncident = elapsedTime / nIncidents;
        loadTestInfo(String.format(
                        "Finished in %.1f seconds.  Total no of incidents: %d,  Average time per incident: %.3f seconds\n",
                        elapsedTime, nIncidents, averageTimePerIncident));
    }

    private void loadTestInfo(String s) {
        Date date = new Date();
        String dateStr = formatter.format(date);
        System.out.print(dateStr + " LOADTESTER: " + s + "\n");
    }

    private void sleep(int mSecs) {
        try {
            Thread.sleep(mSecs);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void executeFootballParamFindTest(int noMatches, int nParamFinds) {

        testMatches = new TreeMap<Long, TestMatch>();
        long eventId;
        /*
         * create the matches
         */
        for (int i = 0; i < noMatches; i++) {
            eventId = 1000 + i;
            FootballTestMatch testMatch = new FootballTestMatch(eventId);
            synchronized (testMatches) {
                testMatches.put(eventId, testMatch);
                testMatch.recordIncidentIssued();
            }
            algoManager.handleNewEventCreation(testMatch.getSupportedSport(), eventId, testMatch.getMatchFormat());
        }
        loadTestInfo(String.format("%d matches created.  Waiting for all pre-match markets to be published...",
                        noMatches));
        waitForAllMarketsToPublish();
        for (Entry<Long, TestMatch> entry : testMatches.entrySet()) {
            FootballTestMatch testMatch = (FootballTestMatch) entry.getValue();
            testMatch.resetNoIncidentsGenerated();
            testMatch.setParamFindResultsPublished(true);
            testMatch.setnParamFindResultsPublished(0);
        }
        loadTestInfo("Starting param find test");
        StopWatch sw = new StopWatch();
        sw.start();
        int nMatchesFinished = 0;
        int totalNoParamFinds = noMatches * nParamFinds;
        do {
            int nParamFindsFinished = 0;
            synchronized (testMatches) {
                for (Entry<Long, TestMatch> entry : testMatches.entrySet()) {
                    eventId = entry.getKey();
                    TestMatch testMatch = entry.getValue();
                    if (testMatch.isParamFindResultsPublished()) {
                        loadTestInfo(String.format("%d of %d param finds completed for match %d",
                                        testMatch.getnParamFindResultsPublished(), nParamFinds, eventId));
                        if (testMatch.getnParamFindResultsPublished() >= nParamFinds) {
                            nMatchesFinished++;
                            testMatch.setMatchCompleted(true);
                        } else {
                            testMatch.setParamFindResultsPublished(false);
                            testMatch.recordIncidentIssued();
                            MarketPricesList marketPricesList = testMatch.getNextMarketPricesList();
                            algoManager.handleSupplyMarketPrices(eventId, marketPricesList);
                        }
                    }
                    nParamFindsFinished += testMatch.getnParamFindResultsPublished();
                }
            }
            if (nMatchesFinished < noMatches) {
                sleep(1000);
            }
            loadTestInfo(String.format("%d of %d param finds completed across all matches", nParamFindsFinished,
                            totalNoParamFinds));
            checkWhetherToLogStatus();
        } while (nMatchesFinished < noMatches);
        sw.stop();
        double elapsedTime = sw.getElapsedTimeSecs();
        int nIncidents = getTotalNoOfIncidents();
        double averageTimePerIncident = elapsedTime / nIncidents;
        loadTestInfo(String.format(
                        "Finished in %.1f seconds.  Total no of param finds: %d,  Average time per param find: %.3f seconds\n",
                        elapsedTime, nIncidents, averageTimePerIncident));
        loadTestInfo("-\n---Summary for each event----\n");
        for (Entry<Long, TestMatch> entry : testMatches.entrySet()) {
            eventId = entry.getKey();
            TestMatch testMatch = entry.getValue();
            loadTestInfo(String.format("  Event %d (%s): %s\n", eventId, testMatch.getMatchDescription(),
                            testMatch.getSummaryStats()));
        }
        info("----Summary ends----\n\n");
    }

    public void executeFatalErrorTest() {
        testMatches = new TreeMap<Long, TestMatch>();
        long eventId;
        /*
         * create the match
         */
        eventId = 1001L;
        TestMatch testMatch;
        testMatch = new TennisTestMatch(eventId);
        testMatches.put(eventId, testMatch);
        testMatch.recordIncidentIssued();
        algoManager.handleNewEventCreation(testMatch.getSupportedSport(), eventId, testMatch.getMatchFormat());
        loadTestInfo("match created.  Waiting for pre-match markets to be published...");
        waitForAllMarketsToPublish();
        loadTestInfo("Starting error test");
        testMatch.resetNoIncidentsGenerated();
        boolean badMatchParamsIssued = false;
        int n = 0;
        do {
            if (testMatch.isMarketsPublished() || testMatch.isFatalErrorNotified()) {
                if (testMatch.isMarketsPublished()) {
                    loadTestInfo(String.format("Published markets received for %s %s", testMatch.eventId,
                                    testMatch.getLastRequestId()));
                    if (badMatchParamsIssued)
                        throw new IllegalArgumentException("Not expecting markets here - badMatchParams was issued");
                }
                if (testMatch.isFatalErrorNotified()) {
                    if (!badMatchParamsIssued)
                        throw new IllegalArgumentException("Not expecting fatal error here - normal incident issued");
                }
                testMatch.setFatalErrorNotified(false);
                testMatch.recordIncidentIssued();
                if (n != 3) {
                    badMatchParamsIssued = false;
                    TennisMatchParams matchParams = new TennisMatchParams();
                    matchParams.setEventId(1001L);
                    algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());
                } else {
                    /*
                     * cause an exception to occur in AlgoCalculator by sending an out of range matchParam
                     */
                    TennisMatchParams matchParams = new TennisMatchParams();
                    matchParams.setOnServePctA1(-100, 0.1);
                    matchParams.setEventId(1001L);
                    badMatchParamsIssued = true;
                    algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());
                }
                n++;

            }
            sleep(200);
        } while (n < 10);

    }

    private long lastStatusUpdateTime;

    private void checkWhetherToLogStatus() {
        long timeNow = System.currentTimeMillis();
        if (timeNow - lastStatusUpdateTime > 1000) {
            loadTestInfo(algoManager.getStatistics().toStringSingleLineSummary());
            lastStatusUpdateTime = timeNow;
        }
    }

    private int getTotalNoOfIncidents() {
        int nIncidents = 0;
        for (Entry<Long, TestMatch> entry : testMatches.entrySet())
            nIncidents += entry.getValue().getNoIncidentsGenerated();
        return nIncidents;
    }

    private void waitForAllMarketsToPublish() {
        boolean allPublished;
        do {
            allPublished = true;
            synchronized (testMatches) {
                for (Entry<Long, TestMatch> entry : testMatches.entrySet()) {
                    allPublished = allPublished && entry.getValue().isMarketsPublished();
                }
                if (allPublished)
                    return;
                else {

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new IllegalArgumentException("Unexpected Interruped execution");
                    }
                }
            }
        } while (!allPublished);
    }

    int nPublishedMarkets;

    @Override
    public void publishMarkets(long eventId, Markets markets, Set<String> keysForDiscontinuedMarkets,
                    TimeStamp timeStamp, String sequenceId) {
        synchronized (testMatches) {
            nPublishedMarkets++;
            loadTestInfo("published markets received ");
            // TODO - fix load tester given no longer support requestIds in
            // published items
            testMatches.get(eventId).recordPublishedMarketsReceived("XXX");
            testMatches.notify();
        }
    }

    @Override
    public void notifyEventCompleted(long eventId, boolean isCompleted) {
        loadTestInfo(String.format("Notification received that match %d completed", eventId));
        synchronized (testMatches) {
            testMatches.get(eventId).setMatchCompleted(isCompleted);
            testMatches.notify();
        }

    }

    @Override
    public void publishMatchParams(long eventId, GenericMatchParams matchParams) {
        synchronized (testMatches) {
            loadTestInfo("matchParams received");
            testMatches.get(eventId).recordPublishedParamsReceived("XXX");
            testMatches.notify();
        }
    }

    @Override
    public void publishParamFindResults(long eventId, ParamFindResults paramFindResults, GenericMatchParams matchParams,
                    long elapsedTimeMs) {
        synchronized (testMatches) {
            loadTestInfo("paramFindResults received");
            testMatches.get(eventId).recordParamFindResultsReceived("XXX");
            testMatches.notify();
        }
    }

    @Override
    public void notifyFatalError(long eventId, String requestId, String errorCause) {
        synchronized (testMatches) {
            loadTestInfo(String.format("Fatal error notification received for %d, %s", eventId, requestId));
            testMatches.get(eventId).setFatalErrorNotified(true);
            if (requestId.equals("RequestId_2"))
                requestId = "RequestId_2";
            testMatches.notify();
        }
    }

    @Override
    public void publishRecordedItem(long eventId, RecordedItem recordedItem) {
        this.recordedItem = recordedItem;

    }

    @Override
    public void publishMatchState(long eventId, SimpleMatchState matchState) {}

    @Override
    public void publishResultedMarkets(long eventId, ResultedMarkets ResultedMarkets) {}

    @Override
    public void publishTraderAlert(long eventId, TraderAlert traderAlert) {}

    @Override
    public void publishEventState(long eventId, EventStateBlob eventStateBlob) {}

    @Override
    public void publishEventSuspensionStatus(long eventId, boolean suspend, Set<MarketGroup> marketGroups) {

    }

    @Override
    public void publishEventProperties(long eventId, Map<String, String> properties) {

    }

}
