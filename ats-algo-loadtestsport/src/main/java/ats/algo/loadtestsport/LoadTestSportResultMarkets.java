package ats.algo.loadtestsport;

import ats.algo.core.baseclasses.MatchResultMarkets;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.markets.Market;

public class LoadTestSportResultMarkets extends MatchResultMarkets {

    @Override
    protected CheckMarketResultedOutcome checkMarketResulted(Market market, MatchState previousMatchState,
                    MatchState currentMatchState) {
        return null;
    }
}
