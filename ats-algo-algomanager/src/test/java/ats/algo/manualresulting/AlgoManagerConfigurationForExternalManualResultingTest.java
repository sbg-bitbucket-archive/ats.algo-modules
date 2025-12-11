package ats.algo.manualresulting;

import ats.algo.algomanager.AbstractParamFinder;
import ats.algo.algomanager.AbstractPriceCalculator;
import ats.algo.algomanager.AlgoManager;
import ats.algo.algomanager.AlgoManagerConfiguration;

import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketCategory;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarket;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.request.ParamFindRequest;
import ats.algo.core.request.PriceCalcRequest;
import ats.algo.core.request.PriceCalcResponse;

/**
 * A simple single threaded configuration which directly wires the inputs and outputs between AlgoManager AlgoCalculator
 * and AlgoPAramFinder
 * 
 * @author Geoff
 *
 */

public class AlgoManagerConfigurationForExternalManualResultingTest extends AlgoManagerConfiguration {

    AlgoManager algoManager;

    public AlgoManagerConfigurationForExternalManualResultingTest() {
        super();
    }

    @Override
    public void schedulePriceCalc(PriceCalcRequest request) {
        scheduleExternalPriceCalc(request);
    }

    @Override
    public boolean establishExternalModelConnection(String url) {
        return false;
    }

    @Override
    public void scheduleExternalPriceCalc(PriceCalcRequest request) {
        PriceCalcResponse response;
        if (request.getCalcRequestCause().equals(CalcRequestCause.MATCH_RESULT)) {
            response = new PriceCalcResponse(request.getUniqueRequestId(), null, null, null,
                            generateTestResultedMarkets());
        } else
            response = new PriceCalcResponse(request.getUniqueRequestId(), generateTestMarkets(), null, null, null);
        algoHandlePriceCalcResponse.handle(request.getEventId(), response);
    }

    @Override
    public void scheduleParamFind(ParamFindRequest request) {

    }

    @Override
    public void scheduleExternalParamFind(ParamFindRequest request) {

    }

    @Override
    public void schedulePriceCalc(PriceCalcRequest request, Class<? extends AbstractPriceCalculator> clazz) {}

    @Override
    public void scheduleParamFind(ParamFindRequest request, Class<? extends AbstractParamFinder> clazz) {}

    @Override
    public int getPriceCalcQueueSize() {
        return 0;
    }

    @Override
    public int getParamFindQueueSize() {
        return 0;
    }

    @Override
    public int getNoAlgoCalculators() {
        return 1;
    }

    @Override
    public int getNoAlgoParamFinders() {
        return 1;
    }

    @Override
    public void abandonPriceCalc(long eventId, String uniqueRequestId) {
        /*
         * do nothing
         */
    }

    @Override
    public void abandonParamFind(long eventId, String uniqueRequestId) {
        /*
         * do nothing
         */
    }

    private ResultedMarkets generateTestResultedMarkets() {
        ResultedMarkets resultedMarkets = new ResultedMarkets();
        ResultedMarket resultedMarket =
                        new ResultedMarket("FT:ML", null, MarketCategory.GENERAL, "M", false, "Match Winner", "A", 0);
        resultedMarkets.addMarket(resultedMarket);
        return resultedMarkets;
    }

    private Markets generateTestMarkets() {
        Markets markets = new Markets();
        Market market = new Market(MarketCategory.GENERAL, "FT:ML", "M", "Match Winner");
        market.setLineId(null);
        market.setIsValid(true);
        market.setSequenceId("M");
        market.put("A", 0.4);
        market.put("B", 0.6);
        markets.addMarketWithShortKey(market);
        return markets;
    }

}
