package ats.algo.executeRequests;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import ats.algo.algomanager.SimpleAlgoManagerConfiguration;
import ats.algo.core.request.PriceCalcRequest;
import ats.algo.core.request.PriceCalcResponse;
import ats.algo.genericsupportfunctions.ConsoleInput;
import ats.algo.genericsupportfunctions.Sleep;
import ats.algo.genericsupportfunctions.StopWatch;
import ats.core.util.json.JsonUtil;
import ats.core.util.log.Logger;
import ats.core.util.log.LoggerFactory;

public class ExecutePriceCalcRequest {

    private static final Logger log = LoggerFactory.getLogger(ExecutePriceCalcRequest.class);

    private static String uniqueRequestId = null;
    private static final int NO_REQUESTS = 20;

    public static void main(String[] args) {
        // LogUtil.initConsoleLogging(Level.TRACE);
        System.out.println("Utility to execute priceCalcRequest from file");
        String reqStr = null;

        do {
            String fileName = ConsoleInput.readString("Enter fileName:", "C:\\TestCases\\pricecalc_2017-10-10.json");

            try {
                reqStr = readFile(fileName, Charset.defaultCharset());
            } catch (IOException e) {
                System.out.println("File read error");
                e.printStackTrace();
            }
        } while (reqStr == null);
        PriceCalcRequest request = JsonUtil.unmarshalJson(reqStr, PriceCalcRequest.class);
        log.info("Successfully unmarshalled request");
        SimpleAlgoManagerConfiguration algoConfig = new SimpleAlgoManagerConfiguration();
        algoConfig.setHandlePriceCalcResponse(
                        (long eventId, PriceCalcResponse response) -> handlePriceCalcResponse(eventId, response));
        algoConfig.setHandlePriceCalcError((long eventId, String requestId,
                        String cause) -> handlePriceCalcError(eventId, requestId, cause));
        StopWatch sw = new StopWatch();
        sw.start();
        String lastReqId = null;
        for (int i = 0; i < NO_REQUESTS; i++) {
            lastReqId = "R" + Integer.toString(i);
            request.updateUniqueRequestId(lastReqId);
            algoConfig.schedulePriceCalc(request);
        }
        boolean done = false;
        do {
            if (uniqueRequestId != null)
                done = uniqueRequestId.equals(lastReqId);
            Sleep.sleepMs(100);
        } while (!done);
        sw.stop();
        double t = sw.getElapsedTimeSecs();
        System.out.printf("Finished in %.1f secs.  time per calc: %.2f secs\n", t, t / NO_REQUESTS);
        System.exit(0);
    }

    static void handlePriceCalcResponse(long eventId, PriceCalcResponse response) {
        log.info("Response received:" + response.getUniqueRequestId());
        uniqueRequestId = response.getUniqueRequestId();

    }

    static void handlePriceCalcError(long eventId, String requestId, String cause) {
        log.info("Fatal priceCalcError received: %d, cause: %s", eventId, cause);
    }

    static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

}
