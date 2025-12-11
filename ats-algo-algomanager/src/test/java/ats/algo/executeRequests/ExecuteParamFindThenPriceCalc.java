package ats.algo.executeRequests;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import ats.algo.algomanager.SimpleAlgoManagerConfiguration;
import ats.algo.algomanager.AlgoManagerConfiguration.HandleParamFindResponse;
import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.comparetomarket.CompareToMarket;
import ats.algo.core.comparetomarket.MarketPrice;
import ats.algo.core.comparetomarket.MarketPrices;
import ats.algo.core.comparetomarket.ParamFindResultsStatus;
import ats.algo.core.markets.Market;
import ats.algo.core.request.ParamFindRequest;
import ats.algo.core.request.ParamFindResponse;
import ats.algo.core.request.PriceCalcRequest;
import ats.algo.core.request.PriceCalcResponse;
import ats.algo.genericsupportfunctions.StopWatch;
import ats.algo.margining.Margining;
import ats.algo.margining.Margining.MarginingAlgo;
import ats.algo.sport.football.FootballMatchEngine;
import ats.algo.sport.football.FootballMatchParams;
import ats.core.util.json.JsonUtil;
import ats.core.util.log.Level;
import ats.core.util.log.LogUtil;
import ats.core.util.log.Logger;
import ats.core.util.log.LoggerFactory;

public class ExecuteParamFindThenPriceCalc {
    static HandleParamFindResponse algoHandleParamFinderResults;
    private static final Logger log = LoggerFactory.getLogger("PF TEST");

    private static ParamFindResponse pfResponse;
    private static PriceCalcResponse pcResponse;

    private static MarginingAlgo marginingAlgo = Margining.MARGINING_ALGO_V4A;
    private static String algoString = "MARGINING_ALGO_V4A";
    private static boolean applyProbWeights = false;
    private static boolean comparePrices = true;

    public static void main(String[] args) {
        LogUtil.initConsoleLogging(Level.TRACE);
        CompareToMarket.setMarginingAlgo(marginingAlgo);
        CompareToMarket.setApplyProbWeights(applyProbWeights);
        CompareToMarket.setComparePricesNotProbs(comparePrices);

        SimpleAlgoManagerConfiguration algoConfig = new SimpleAlgoManagerConfiguration();
        algoConfig.setHandleParamFindResponse((eventId, response) -> handleParamFindResponse(eventId, response));
        algoConfig.setHandlePriceCalcResponse((eventId, response) -> handlePriceCalcResponse(eventId, response));
        /*
         * read the pf request from file
         */
        System.out.println("Execute pf then price calc");
        String reqStr = null;

        String fileName = "football_2017-09-04.json";
        // String fileName = "football_2017-09-13.json";
        // String fileName = "football_2017-09-14.json";
        // String fileName = "football_2017-10-09.json"; // Georgia Wales
        // correct score investigation
        // String fileName = "football_2017-10-10.json"; // pf that was
        // exhausting heap
        // String fileName = "football_2017-10-11.json"; // pf that is pfing on
        // 16 params

        try {
            reqStr = readFile("C:\\TestCases\\" + fileName, Charset.defaultCharset());
        } catch (IOException e) {
            System.out.println("File read error");
            e.printStackTrace();
            System.exit(1);
        }

        ParamFindRequest pfRequest = JsonUtil.unmarshalJson(reqStr, ParamFindRequest.class);
        pfRequest.setMatchEngineClassName(FootballMatchEngine.class.getName());
        log.info("Successfully unmarshalled request");
        MarketPrices marketPrices = pfRequest.getMarketPricesList().getMarketPricesList().get("Bet365");
        Set<String> mktKeys = marketPrices.getMarketPrices().keySet();
        String ahcpKey = null;
        for (String key : mktKeys)
            if (key.contains("FT:AHCP"))
                ahcpKey = key;
        MarketPrice priceAxb = marketPrices.get("FT:AXB_M");
        MarketPrice priceOu = marketPrices.get("FT:OU_M");
        MarketPrice priceBts = marketPrices.get("FT:BTS_M");
        MarketPrice priceAhcp = marketPrices.get(ahcpKey);
        /*
         * set market weights as per current trading rules
         */
        priceAxb.setMarketWeight(2.0);
        priceOu.setMarketWeight(1.0);
        /*
         * remove any unwanted prices - comment out as required
         */
        // marketPrices.removeMarketPrice("FT:OU_M");
        // marketPrices.removeMarketPrice(ahcpKey);
        /*
         * set the match params to default values
         */
        FootballMatchParams footballMatchParams = new FootballMatchParams(pfRequest.getMatchFormat());
        pfRequest.setMatchParams(footballMatchParams.generateGenericMatchParams());

        // LinkedHashMap<String, MatchParam> paramMap =
        // pfRequest.getMatchParams().getParamMap();
        // paramMap.get("goalTotal").getGaussian().setStdDevn(1.0);
        // paramMap.get("goalSupremacy").getGaussian().setStdDevn(1.0);
        /*
         * execute the pf request and then a price calc using the updated params
         */
        StopWatch sw = new StopWatch();
        sw.start();
        algoConfig.scheduleParamFind(pfRequest);
        if (pfResponse.getParamFindResults().getParamFindResultsStatus() != ParamFindResultsStatus.RED) {
            PriceCalcRequest pcRequest = new PriceCalcRequest(123L, pfRequest.getEventSettings(),
                            FootballMatchEngine.class.getName(), CalcRequestCause.PARAMS_CHANGED_FOLLOWING_PARAM_FIND,
                            pfRequest.getMatchFormat(), pfRequest.getMatchState(), pfResponse.getGenericMatchParams(),
                            null, null, null, System.currentTimeMillis());
            algoConfig.schedulePriceCalc(pcRequest);
            sw.stop();
            /*
             * collect and print summary results
             */

            System.out.println("Compare target and generated probs and prices\n");
            System.out.printf("file: %s; marginingAlgo: %s; applyProbWeights: %b; comparePrices: %b.\n", fileName,
                            algoString, applyProbWeights, comparePrices);
            Map<String, Market> markets = pcResponse.getMarkets().getMarkets();
            printSummaryHeader();
            printSummaryData(priceAxb, markets.get("FT:AXB_M"));
            printSummaryData(priceOu, markets.get("FT:OU_M"));
            printSummaryData(priceBts, markets.get("FT:BTS_M"));
            printSummaryData(priceAhcp, markets.get(ahcpKey));

        } else {
            System.out.println("Failed pf");
        }
        String y = pfResponse.getParamFindResults().getTraderParamFindResultsDescription().getResultSummary()
                        .toString();
        System.out.println();
        System.out.println(y);
        System.out.printf("Elapsed time %.1f secs.\n\n", sw.getElapsedTimeSecs());
        System.out.println("goalTotal: " + pfResponse.getGenericMatchParams().getParamMap().get("goalTotal"));
        System.out.println("goalSupremacy: " + pfResponse.getGenericMatchParams().getParamMap().get("goalSupremacy"));
        System.out.println("homeLoseBoost: " + pfResponse.getGenericMatchParams().getParamMap().get("homeLoseBoost"));
        System.out.println("awayLoseBoost: " + pfResponse.getGenericMatchParams().getParamMap().get("awayLoseBoost"));
        System.out.println(
                        "targetGoalBoost: " + pfResponse.getGenericMatchParams().getParamMap().get("targetGoalBoost"));
        System.out.println("goalsDevn: " + pfResponse.getGenericMatchParams().getParamMap().get("goalsDevn"));

        System.exit(0);
    }

    private static void printSummaryHeader() {
        System.out.printf("mkt,selection,sourcePrice,sourceProb,calcProb,calcPrice\n");
    }

    private static void printSummaryData(MarketPrice marketPrice, Market srcMarket) {
        String mktName = srcMarket.getShortKey();
        if (marketPrice == null) {
            for (Entry<String, Double> me : srcMarket.getSelectionsProbs().entrySet())
                System.out.printf("%s,%s, , , , \n", mktName, me.getKey());
            return;
        } else {
            Market market;
            String lineId = marketPrice.getLineId();
            if (lineId != null && !lineId.equals(""))
                market = srcMarket.getMarketForLineId(lineId);
            else
                market = srcMarket;

            int n = marketPrice.getSelections().size();
            double[] sourcePrices = new double[n];
            double[] calcProbs = new double[n];
            String[] selection = new String[n];
            int i = 0;
            for (Entry<String, Double> e : marketPrice.getSelections().entrySet()) {
                String selName = e.getKey();
                sourcePrices[i] = e.getValue();
                calcProbs[i] = market.getSelectionsProbs().get(selName);
                selection[i] = selName;
                i++;
            }
            Margining margining = new Margining();
            margining.setMarginingAlgo(marginingAlgo);
            double[] sourceProbs = margining.removeMargin(sourcePrices, 1.0);
            double[] calcPrices = new double[calcProbs.length];
            for (int j = 0; j < calcProbs.length; j++) {
                double margin = Margining.v4AMargin(calcProbs[j], 0.01, 0.05, 0.03);
                calcPrices[j] = 1 / (calcProbs[j] + margin);
            }
            for (int j = 0; j < n; j++)
                System.out.printf("%s,%s,%.3f,%.3f,%.3f,%.3f\n", mktName, selection[j], sourcePrices[j], sourceProbs[j],
                                calcProbs[j], calcPrices[j]);
        }
    }

    static void handlePriceCalcResponse(long eventId, PriceCalcResponse response) {
        pcResponse = response;
        log.info("pcResponse received:" + eventId);
        log.info(response);
    }

    static void handleParamFindResponse(long eventId, ParamFindResponse response) {
        pfResponse = response;
        log.info("pfResponse received:" + eventId);
        log.info(response);
    }

    static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
