package ats.algo.algomanager.outrights;

import ats.algo.algomanager.AbstractParamFinder;
import ats.algo.algomanager.AbstractPriceCalculator;
import ats.algo.algomanager.AlgoManager;
import ats.algo.algomanager.AlgoManagerConfiguration;
import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchEngineSavedState;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketCategory;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarket;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.request.ParamFindRequest;
import ats.algo.core.request.PriceCalcRequest;
import ats.algo.core.request.PriceCalcResponse;
import ats.algo.sport.outrights.OutrightsMatchIncident;
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
    OutrightsMatchIncident outrightsMatchIncident;

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
        long eventId = request.getEventId();
        // System.out.println("Received request for: " + eventId);

        if (eventId == OutrightsTest.football11L || eventId == OutrightsTest.football12L) {
            /*
             * respond to requests for football price calc
             */
            response = new PriceCalcResponse(request.getUniqueRequestId(), sampleFootballMarkets(eventId),
                            request.getMatchParams(), null, sampleResultedMarkets());
            outrightsMatchIncident = null;

        } else if (eventId == OutrightsTest.outrightsEventID) {
            /*
             * respond to requests for outrights price calc
             */

            response = new PriceCalcResponse(request.getUniqueRequestId(), sampleOutrightMarkets(),
                            request.getMatchParams(), testWatchList(), null);
            outrightsMatchIncident = (OutrightsMatchIncident) request.getMatchIncident();
        } else {
            throw new IllegalArgumentException("invalid eventId");
        }
        algoHandlePriceCalcResponse.handle(request.getEventId(), response);
    }

    private long extraEventId = 0;

    private MatchEngineSavedState testWatchList() {
        OutrightsWatchList watchList = new OutrightsWatchList();
        watchList.put(OutrightsTest.football11L, "FT:CS");
        if (extraEventId != 0) {
            watchList.put(extraEventId, "FT:MW");
        }
        return watchList;
    }

    public void addToWatchList(long id) {
        extraEventId = id;
    }

    private long resultEventId = 0;

    public void resultEvent(long eventId) {
        this.resultEventId = eventId;

    }

    private ResultedMarkets sampleResultedMarkets() {
        if (resultEventId == 0)
            return null;
        /*
         * add FT:CS if resultEventId non 0
         */
        ResultedMarkets rms = new ResultedMarkets();
        ResultedMarket rm = new ResultedMarket("FT:CS", null, MarketCategory.GENERAL, "M", false, "Correct Score",
                        "1-0", 0);
        rms.addMarket(rm);
        return rms;

    }

    private Markets sampleFootballMarkets(long eventId) {
        Markets markets = new Markets();
        if (eventId != resultEventId) {
            /*
             * only add FT:CS if market is not resulted for this eventId
             */
            Market market = new Market(MarketCategory.GENERAL, "FT:CS", "M", "Correct Score");
            market.setLineId(null);
            market.setIsValid(true);
            market.setSequenceId("M");
            market.setMarketGroup(MarketGroup.GOALS);
            market.put("0-1", 0.4);
            market.put("1-0", 0.6);
            markets.addMarketWithShortKey(market);
        }
        Market market2 = new Market(MarketCategory.GENERAL, "FT:MW", "M", "Match Winner");
        market2.setLineId(null);
        market2.setIsValid(true);
        market2.setSequenceId("M");
        market2.setMarketGroup(MarketGroup.GOALS);
        market2.put("A", 0.3);
        market2.put("B", 0.5);
        market2.put("Draw", 0.1);
        markets.addMarketWithShortKey(market2);
        return markets;
    }

    private Markets sampleOutrightMarkets() {
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
