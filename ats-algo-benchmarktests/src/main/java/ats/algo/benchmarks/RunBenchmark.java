package ats.algo.benchmarks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ats.algo.algomanager.AlgoManagerConfiguration;
import ats.algo.algomanager.JmsAlgoManagerConfiguration;
import ats.algo.core.request.AbstractAlgoRequest;
import ats.algo.core.request.AbstractAlgoResponse;
import ats.algo.core.request.ParamFindRequest;
import ats.algo.core.request.ParamFindResponse;
import ats.algo.core.request.PriceCalcRequest;
import ats.algo.core.request.PriceCalcResponse;
import ats.algo.genericsupportfunctions.ConsoleInput;
import ats.algo.genericsupportfunctions.ReadResourceFile;
import ats.algo.genericsupportfunctions.Sleep;
import ats.algo.genericsupportfunctions.StopWatch;
import ats.core.util.log.Level;
import ats.core.util.log.LogUtil;
import ats.core.util.log.Logger;
import ats.core.util.log.LoggerFactory;

public class RunBenchmark {

    private static final int NO_RUNS = 3;

    private static final Logger log = LoggerFactory.getLogger(RunBenchmark.class);

    private volatile Map<String, Long> requestTimes;
    private volatile Map<String, Long> responseTimes;
    private volatile Map<String, Long> requestReceivedTimes;
    private volatile Map<String, Long> responseIssuedTimes;
    private AlgoManagerConfiguration algoConfig;
    private StopWatch sw;
    private boolean done;
    private List<ResponseStat> responseStats = new ArrayList<>();
    private boolean compressMessages;

    public RunBenchmark() {

    }

    public void runTest(LinkedHashMap<String, String> urls, String priceCalcFileName, String paramFindFileName,
                    boolean externalModel) {
        requestTimes = new HashMap<>();
        responseTimes = new HashMap<>();
        requestReceivedTimes = new HashMap<>();
        responseIssuedTimes = new HashMap<>();
        responseStats = new ArrayList<>();
        LogUtil.initConsoleLogging(Level.WARN);
        System.out.println("Utility to run priceCalcRequest benchmark test");
        String url = null;
        int n = 1;
        Map<Integer, String> noToKey = new HashMap<>();
        String prompt = "Connect to ";
        for (String key : urls.keySet()) {
            noToKey.put(n, key);
            prompt += key + "(" + Integer.toString(n) + ")";
            n++;
            if (n <= urls.size())
                prompt += ", ";
        }
        int nUrl = ConsoleInput.readInt(prompt, 1, false);
        String key = noToKey.get(nUrl);
        url = urls.get(key);

        int maxN = ConsoleInput.readInt("Enter no of simultaneous requests (1-1500)", 1, false);
        compressMessages = ConsoleInput.readBoolean("Compress messages (yes, no)?", false);
        int nPriceCalc = ConsoleInput.readInt("PriceCalc(1) or ParamFind(2)", 1, false);
        boolean priceCalc = true;
        if (nPriceCalc == 2)
            priceCalc = false;
        AbstractAlgoRequest request;
        if (priceCalc)
            request = ReadResourceFile.readObject(priceCalcFileName, RunBenchmark.class, PriceCalcRequest.class);
        else
            request = ReadResourceFile.readObject(paramFindFileName, RunBenchmark.class, ParamFindRequest.class);
        // Sleep.sleep(18000);
        System.out.println("Connecting to MQ broker: " + url);
        try {
            algoConfig = new JmsAlgoManagerConfiguration(url, compressMessages);
            if (externalModel)
                algoConfig.establishExternalModelConnection(url);

            algoConfig.setHandlePriceCalcResponse(
                            (long eventId, PriceCalcResponse response) -> handleResponse(eventId, response));
            algoConfig.setHandlePriceCalcError(
                            (long eventId, String incidentId, String cause) -> handleError(eventId, incidentId, cause));
            algoConfig.setHandleParamFindResponse(
                            (long eventId, ParamFindResponse response) -> handleResponse(eventId, response));
            algoConfig.setHandleParamFindError(
                            (long eventId, String incidentId, String cause) -> handleError(eventId, incidentId, cause));

            for (int j = 0; j < NO_RUNS; j++) {
                System.out.printf("\nExecuting run %d of %d for %d simultaneous requests. Compressing messages: %b\n",
                                j + 1, NO_RUNS, maxN, compressMessages);
                runBenchmarkTest(maxN, priceCalc, request, externalModel);
                if (j < NO_RUNS - 1)
                    Sleep.sleep(2);
            }

        } catch (Exception e) {
            System.out.println("Problem connecting to MQ Server");
            System.out.println(e);
            System.exit(1);
        }
        System.out.println("\nSUMMARY OF RESULTS");
        System.out.println(
                        "nRequests,uniqueRequestId,latency,calcEngineTime,inFlightTime,inFlightSendTimeMs,inFlightRcvTimeMs");
        for (ResponseStat r : responseStats) {
            System.out.printf("%d,%s,%d,%d,%d,%d,%d\n", r.nIterations, r.uniqueRequestId, r.latency(),
                            r.calcEngineTime(), r.inFlightTime(), r.inFlightSendTimeMs(), r.inFlightRcvTimeMs());
        }
        System.exit(0);
    }

    private void runBenchmarkTest(int nIterations, boolean priceCalc, AbstractAlgoRequest request,
                    boolean externalModel) {
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
                if (priceCalc) {
                    if (externalModel)
                        algoConfig.scheduleExternalPriceCalc((PriceCalcRequest) request);
                    else
                        algoConfig.schedulePriceCalc((PriceCalcRequest) request);
                } else {
                    if (externalModel)
                        algoConfig.scheduleExternalParamFind((ParamFindRequest) request);
                    else
                        algoConfig.scheduleParamFind((ParamFindRequest) request);
                }
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

    void handleResponse(long eventId, AbstractAlgoResponse response) {
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
