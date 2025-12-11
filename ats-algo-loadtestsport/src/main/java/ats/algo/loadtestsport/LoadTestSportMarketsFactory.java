package ats.algo.loadtestsport;

import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketCategory;
import ats.algo.core.markets.Markets;
import ats.algo.montecarloframework.MarketsFactory;
import ats.algo.sport.football.FootballMatchState;

public class LoadTestSportMarketsFactory extends MarketsFactory {

    /**
     * holds any facts about a simulation of a match that has just been played (via the Match class) that are need in
     * the calculation of the required statistics and that are not captured by the matchState object for Football if we
     * need to know who won leg N+2, where N is the current leg, then we need to collect that "fact" in this class
     * 
     * @author Geoff
     *
     */

    LoadTestSportMarketsFactory(FootballMatchState matchState) {

    }

    @Override
    public void addDerivedMarkets(Markets markets, MatchState matchState, MatchParams matchParams) {
        Market market = new Market(MarketCategory.GENERAL, "AB", "M", "Test market");
        market.setIsValid(true);
        market.setLineId("");
        String requestId = matchState.getIncidentId();
        double prob;
        if (requestId == null)
            prob = 0.6;
        else {
            double h = Math.abs(requestId.hashCode());
            prob = h / ((double) Integer.MAX_VALUE);
        }
        market.put("A", prob);
        market.put("B", 1 - prob);
        markets.addMarketWithShortKey(market);
        market = new Market(MarketCategory.OVUN, "GT", "M", "Test total market");
        market.setIsValid(true);
        market.setLineId("20.5");
        market.setLineBase(20);
        double[] probs = {.5, .5};
        market.setLineProbs(probs);
        market.put("Over", prob);
        market.put("Under", 1 - prob);
        markets.addMarketWithShortKey(market);
        try {
            Thread.sleep(500); // wait a bit
        } catch (InterruptedException e) {
        }
    }

}
