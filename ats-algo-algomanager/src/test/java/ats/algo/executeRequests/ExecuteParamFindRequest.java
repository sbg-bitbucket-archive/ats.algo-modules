package ats.algo.executeRequests;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import ats.algo.algomanager.SimpleAlgoManagerConfiguration;
import ats.algo.algomanager.AlgoManagerConfiguration.HandleParamFindResponse;
import ats.algo.core.request.ParamFindRequest;
import ats.algo.core.request.ParamFindResponse;
import ats.algo.genericsupportfunctions.ConsoleInput;
import ats.algo.sport.football.FootballMatchEngine;
import ats.core.util.json.JsonUtil;
import ats.core.util.log.Logger;
import ats.core.util.log.LoggerFactory;

public class ExecuteParamFindRequest {

    static HandleParamFindResponse algoHandleParamFinderResults;

    private static final Logger log = LoggerFactory.getLogger(ExecuteParamFindRequest.class);



    public static void main(String[] args) {
        // LogUtil.initConsoleLogging(Level.TRACE);
        System.out.println("Utility to execute paramfindRequest from file");
        String reqStr = null;

        do {
            String fileName = ConsoleInput.readString("Enter fileName:", "C:\\AAtmp\\request.json");

            try {
                reqStr = readFile(fileName, Charset.defaultCharset());
            } catch (IOException e) {
                System.out.println("File read error");
                e.printStackTrace();
            }
        } while (reqStr == null);
        ParamFindRequest request = JsonUtil.unmarshalJson(reqStr, ParamFindRequest.class);
        request.setMatchEngineClassName(FootballMatchEngine.class.getName());
        log.info("Successfully unmarshalled request");
        /*
         * uncomment following line(s) to remove a particular price or patch a param
         */
        // request.getMarketPricesList().getMarketPricesList().get("Bet365").getMarketPrices().remove("FT:AXB_M");
        // MatchParam matchParam = request.getMatchParams().getParamMap().get("goalSupremacy");
        // request.getMatchParams().getParamMap().get("goalSupremacy").getGaussian().setStdDevn(0.22);
        // request.getMatchParams().getParamMap().get("goalTotal").getGaussian().setStdDevn(0.22);
        // request.getMatchParams().getParamMap().get("homeLoseBoost").getGaussian().setStdDevn(0.22);
        // request.getMatchParams().getParamMap().get("awayLoseBoost").getGaussian().setStdDevn(0.22);

        System.out.println(request.getMarketPricesList());

        SimpleAlgoManagerConfiguration algoConfig = new SimpleAlgoManagerConfiguration();
        algoConfig.setHandleParamFindResponse(
                        (long eventId, ParamFindResponse response) -> handleParamFindResult(eventId, response));

        algoConfig.scheduleParamFind(request);
        System.exit(0);
    }

    static void handleParamFindResult(long eventId, ParamFindResponse response) {
        log.info("Response received:" + eventId);
        log.info(response);
    }

    static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
