package ats.algo.gateway.ppb;


import java.util.Map;

import ats.algo.algomanager.AbstractParamFinder;
import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.comparetomarket.CompareToMarket;
import ats.algo.core.comparetomarket.CompareToMarketMetrics;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.core.comparetomarket.MarketPricesStatus;
import ats.algo.core.comparetomarket.MarketProbs;
import ats.algo.core.comparetomarket.ParamFindResults;
import ats.algo.core.comparetomarket.ParamFindResultsStatus;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketCategory;
import ats.algo.core.markets.Markets;
import ats.algo.core.request.ParamFindRequest;
import ats.algo.core.request.ParamFindResponse;
import ats.algo.genericsupportfunctions.GCMath;

public class PpbParamFinder extends AbstractParamFinder {

    private static final String PARAM_NAME = "teamAPreMatchLine";

    @Override
    public ParamFindResponse calculate(ParamFindRequest request) {
        String uniqueRequestId = request.getUniqueRequestId();
        MarketPricesList marketPricesList = request.getMarketPricesList();
        GenericMatchParams matchParams = (GenericMatchParams) request.getMatchParams();

        /*
         * create a default FT:ML market - the only one we are interested in.
         */

        Markets markets = new Markets();
        Market market = new Market(MarketCategory.GENERAL, "FT:ML", "M", "Match winner");
        market.setIsValid(true);
        market.put("A", 0.5);
        market.put("B", 0.5);
        markets.addMarketWithShortKey(market);

        MarketPricesStatus marketPricesStatus = CompareToMarket.getMarketPricesStatus(markets, marketPricesList);
        /*
         * create the results object
         */
        ParamFindResults results = new ParamFindResults();
        results.setnIterations(0);
        results.addResultDetailRows(marketPricesStatus.getCompareToMarketMetrics().getMarketsCostDescriptionInfo());
        /*
         * check minAchievable within thresholds
         */
        double minCostAchievable = GCMath.round(marketPricesStatus.getMinCostPossibleWithTheseMarketsPrices(),
                        ParamFindResults.PF_DECIMAL_PLACES);
        if (minCostAchievable > CompareToMarket.getRedAlertThreshold()) {
            String summaryText = String.format(
                            "Inconsistent inputs, check prices against market. Param find not attempted.  Cost metric: %.3f.  Min achievable cost %.3f",
                            marketPricesStatus.getActualCostForOurMarkets(),
                            marketPricesStatus.getMinCostPossibleWithTheseMarketsPrices());

            results.addResultSummaryInfo(false, ParamFindResultsStatus.RED, summaryText);

        } else {
            Map<String, Map<String, MarketProbs>> matchedMarkets =
                            CompareToMarket.getMatchedMarkets(markets, marketPricesList, marketPricesStatus);
            CompareToMarketMetrics costMetrics = CompareToMarket.calculateCostMetrics(markets, matchedMarkets);

            double probAWinsMatch = costMetrics.getMarketsProbsStats().get("FT:ML_M").get("A").getMean();
            matchParams.getParamMap().get(PARAM_NAME).getGaussian().setMean(probAWinsMatch);
            results.addResultSummaryInfo(true, ParamFindResultsStatus.GREEN, "Param find succeeded");
        }
        ParamFindResponse response = new ParamFindResponse(uniqueRequestId, results, matchParams);
        return response;
    }
}
