package ats.algo.benchmarks.outrights;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ats.algo.algomanager.AlgoManagerConfiguration;
import ats.algo.algomanager.JmsAlgoManagerConfiguration;
import ats.algo.benchmarks.ResponseStat;
import ats.algo.benchmarks.RunBenchmark;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.AlgoMatchParams;
import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.request.AbstractAlgoRequest;
import ats.algo.core.request.AbstractAlgoResponse;
import ats.algo.core.request.PriceCalcRequest;
import ats.algo.core.request.PriceCalcResponse;
import ats.algo.genericsupportfunctions.ConsoleInput;
import ats.algo.genericsupportfunctions.Sleep;
import ats.algo.genericsupportfunctions.StopWatch;
import ats.algo.sport.outrights.OutrightsMatchFormat;
import ats.algo.sport.outrights.OutrightsMatchIncident;
import ats.algo.sport.outrights.OutrightsMatchParams;

import ats.core.util.log.Logger;
import ats.core.util.log.LoggerFactory;

public class OutrightsSoakTest {

    private static final int NO_RUNS = 3;

    private static final Logger log = LoggerFactory.getLogger(RunBenchmark.class);

    private static volatile Map<String, Long> requestTimes;
    private static volatile Map<String, Long> responseTimes;
    private static volatile Map<String, Long> requestReceivedTimes;
    private static volatile Map<String, Long> responseIssuedTimes;
    private static AlgoManagerConfiguration algoConfig;
    private static StopWatch sw;
    private static boolean done;
    private static List<ResponseStat> responseStats = new ArrayList<>();

    public static final long EVENT_ID = 999222;
    private static final String MQ_URL_LOCAL_HOST =
                    "tcp://localhost:61616?connectionTimeout=0&wireFormat.maxInactivityDuration=0";

    private static final boolean compressMessages = false;

    public static void main(String[] args) {
        requestTimes = new HashMap<>();
        responseTimes = new HashMap<>();
        requestReceivedTimes = new HashMap<>();
        responseIssuedTimes = new HashMap<>();
        responseStats = new ArrayList<>();
        // LogUtil.initConsoleLogging(Level.WARN);
        System.out.println("Utility to run priceCalcRequest soak test on Outrights server");
        String url = null;
        int n = 1;
        Map<String, String> urls = new HashMap<>();
        urls.put("local server", MQ_URL_LOCAL_HOST);
        Map<Integer, String> noToKey = new HashMap<>();
        String prompt = "Connect to ";
        for (String key : urls.keySet()) {
            noToKey.put(n, key);
            prompt += key + "(" + Integer.toString(n) + ")";
            n++;
            if (n <= urls.size())
                prompt += ", ";
        }
        int nUrl = 1;
        if (urls.size() > 1)
            nUrl = ConsoleInput.readInt(prompt, 1, false);
        String key = noToKey.get(nUrl);
        url = urls.get(key);

        // int maxN = ConsoleInput.readInt("Enter no of simultaneous requests (1-1500)",
        // 1, compressMessages);
        int maxN = 10;
        System.out.println("Connecting to MQ broker: " + url);

        algoConfig = new JmsAlgoManagerConfiguration(url, false);
        algoConfig.establishExternalModelConnection(url);
        algoConfig.setHandlePriceCalcResponse(
                        (long eventId, PriceCalcResponse response) -> handleResponse(eventId, response));
        algoConfig.setHandlePriceCalcError(
                        (long eventId, String incidentId, String cause) -> handleError(eventId, incidentId, cause));
        AlgoMatchParams matchParams = new OutrightsMatchParams();
        OutrightsMatchIncident incident = new OutrightsMatchIncident();
        MatchFormat matchFormat = new OutrightsMatchFormat();
        PriceCalcRequest request = new PriceCalcRequest(EVENT_ID, null, null, CalcRequestCause.MATCH_INCIDENT,
                        matchFormat, null, matchParams.generateGenericMatchParams(), incident, null, null, 0);
        for (int j = 0; j < NO_RUNS; j++) {
            System.out.printf("\nExecuting run %d of %d for %d simultaneous requests. Compressing messages: %b\n",
                            j + 1, NO_RUNS, maxN, compressMessages);
            runBenchmarkTest(maxN, request);
            if (j < NO_RUNS - 1)
                Sleep.sleep(2);
        }

        System.out.println("\nSUMMARY OF RESULTS");
        System.out.println(
                        "nRequests,uniqueRequestId,latency,calcEngineTime,inFlightTime,inFlightSendTimeMs,inFlightRcvTimeMs");
        for (ResponseStat r : responseStats) {
            System.out.printf("%d,%s,%d,%d,%d,%d,%d\n", r.getnIterations(), r.getUniqueRequestId(), r.latency(),
                            r.calcEngineTime(), r.inFlightTime(), r.inFlightSendTimeMs(), r.inFlightRcvTimeMs());
        }
        System.exit(0);
    }

    private static void runBenchmarkTest(int nIterations, AbstractAlgoRequest request) {
        requestTimes.clear();
        responseTimes.clear();
        sw = new StopWatch();
        sw.start();
        String uniqueRequestId = null;
        for (int i = 1; i <= nIterations; i++) {
            System.out.println("Sending request no: " + Integer.toString(i) + " of: " + nIterations);
            uniqueRequestId = AbstractAlgoRequest.createUniqueRequestId() + "_" + Integer.toString(i);
            request.updateUniqueRequestId(uniqueRequestId);
            synchronized (requestTimes) {
                requestTimes.put(uniqueRequestId, System.currentTimeMillis());
                algoConfig.scheduleExternalPriceCalc((PriceCalcRequest) request);
            }
        }
        done = false;
        int nReps = 0;
        do {
            Sleep.sleepMs(100);
            nReps++;
            int secs = nReps / 10;
            if (10 * (secs) == nReps)
                System.out.printf("%d secs elapsed so far...\n", secs);

        } while (!done);
        double t = sw.getElapsedTimeSecs();
        double avg = t / nIterations;

        System.out.printf("Finished run for nIterations: %d in %.1f secs.  Average processing time: %.2f\n",
                        nIterations, t, avg);

        for (Entry<String, Long> e : requestTimes.entrySet()) {
            long requestTime = e.getValue();
            String reqId = e.getKey();
            long responseTime = responseTimes.get(reqId);

            responseStats.add(new ResponseStat(nIterations, reqId, requestTime, responseTime,
                            requestReceivedTimes.get(reqId), responseIssuedTimes.get(reqId)));
        }
    }

    static void handleResponse(long eventId, AbstractAlgoResponse response) {
        synchronized (responseTimes) {
            String uniqueRequestId = response.getUniqueRequestId();
            responseTimes.put(uniqueRequestId, System.currentTimeMillis());
            if (response instanceof PriceCalcResponse) {
                requestReceivedTimes.put(uniqueRequestId,
                                ((PriceCalcResponse) response).getTimePriceCalcRequestReceived());
                responseIssuedTimes.put(uniqueRequestId,
                                ((PriceCalcResponse) response).getTimePriceCalcResponseIssued());
            } else {
                requestReceivedTimes.put(uniqueRequestId, (long) 0);
                responseIssuedTimes.put(uniqueRequestId, (long) 0);
            }
            done = requestTimes.size() == responseTimes.size();
            if (done)
                sw.stop();
            System.out.printf("Response received: %s (%d of %d)\n", uniqueRequestId, responseTimes.size(),
                            requestTimes.size());
        }
    }

    static void handleError(long eventId, String uniqueRequestId, String cause) {
        log.error("Fatal error received for %s, cause %s", uniqueRequestId, cause);
    }
}
