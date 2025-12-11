package ats.algo.sport.outrights;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketCategory;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.matchengineframework.MatchEngine;

public class OutrightsMatchEngine extends MatchEngine {


    public OutrightsMatchEngine() {
        super(SupportedSportType.OUTRIGHTS);
        matchParams = new OutrightsMatchParams();
        matchState = new OutrightsMatchState();

    }

    public OutrightsMatchEngine(MatchFormat matchformat) {
        this();
    }

    @Override
    public void calculate() {
        /*
         * generate something to display while testing. In normal use will be using the external outrights server
         */
        super.calculatedMarkets = outrightMarkets();

    }

    @Override
    public ResultedMarkets resultMarkets(Markets arg0, MatchState arg1, MatchState arg2) {
        return new ResultedMarkets();
    }

    private Markets outrightMarkets() {
        Markets markets = new Markets();
        Market market = new Market(MarketCategory.GENERAL, "FT:XX", "M", "placeholder market");
        market.put("win", 0.6);
        market.put("lose", 0.4);
        markets.addMarketWithFullKey(market);
        return markets;
    }

}
