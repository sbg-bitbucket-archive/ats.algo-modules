package ats.algo.algomanager.outrights;

import java.util.HashSet;
import java.util.Set;

import ats.algo.algomanager.AbstractParamFinder;
import ats.algo.algomanager.AbstractPriceCalculator;
import ats.algo.algomanager.AlgoManager;
import ats.algo.algomanager.AlgoManagerConfiguration;
import ats.algo.core.baseclasses.MatchEngineSavedState;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketCategory;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.request.ParamFindRequest;
import ats.algo.core.request.PriceCalcRequest;
import ats.algo.core.request.PriceCalcResponse;
import ats.algo.sport.outrights.OutrightsWatchList;

/**
 * A simple single threaded configuration which directly wires the inputs and outputs between AlgoManager AlgoCalculator
 * and AlgoPAramFinder
 * 
 * @author Geoff
 *
 */

public class AlgoManagerConfigurationForOutrightsTest extends AlgoManagerConfiguration {

    AlgoManager algoManager;

    public AlgoManagerConfigurationForOutrightsTest() {
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
        PriceCalcResponse response = null;
        if (request.getEventId() == OutrightsTest.matchEventID) {
            response = new PriceCalcResponse(request.getUniqueRequestId(), testFootballMarkets(),
                            request.getMatchParams(), null, testResultedMarkets());
        } else if (request.getEventId() == OutrightsTest.outrightsEventID) {
            response = new PriceCalcResponse(request.getUniqueRequestId(), testOutrightMarkets(),
                            request.getMatchParams(), testWatchList(), null);
        } else {
            throw new IllegalArgumentException("invalid eventId");
        }
        algoHandlePriceCalcResponse.handle(request.getEventId(), response);
    }

    private MatchEngineSavedState testWatchList() {
        OutrightsWatchList watchList = new OutrightsWatchList();
        watchList.put(OutrightsTest.matchEventID, "FT:CS");
        return watchList;
    }

    private ResultedMarkets testResultedMarkets() {
        return null;
    }

    private Markets testFootballMarkets() {
        Markets markets = new Markets();
        Market market = new Market(MarketCategory.GENERAL, "FT:CS", "M", "Match Winner");
        market.setLineId(null);
        market.setIsValid(true);
        market.setSequenceId("M");
        market.put("0-1", 0.4);
        market.put("1-0", 0.6);
        markets.addMarketWithShortKey(market);
        return markets;
    }

    private Markets testOutrightMarkets() {
        Markets markets = new Markets();
        Market market = new Market(MarketCategory.GENERAL, "FT:XX", "M", "Outrights winner");
        market.setLineId(null);
        market.setIsValid(true);
        market.setSequenceId("M");
        market.put("Man Utd", 0.4);
        market.put("Chelsea", 0.6);
        markets.addMarketWithShortKey(market);
        return markets;
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

}
