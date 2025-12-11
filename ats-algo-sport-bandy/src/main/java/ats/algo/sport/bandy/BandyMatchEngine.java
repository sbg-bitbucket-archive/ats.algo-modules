package ats.algo.sport.bandy;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.montecarloframework.matchengine.MonteCarloMatchEngine;

public class BandyMatchEngine extends MonteCarloMatchEngine {

    public BandyMatchEngine(MatchFormat matchFormat) {
        super(SupportedSportType.BANDY);
        matchParams = new BandyMatchParams();
        matchParams.setDefaultParams(matchFormat);
        matchState = new BandyMatchState(matchFormat);
        match = new BandyMatch((BandyMatchFormat) matchFormat, (BandyMatchState) matchState,
                        (BandyMatchParams) matchParams);
        initialiseConcurrentCalculationManager();
    }

    @Override
    public ResultedMarkets resultMarkets(Markets markets, MatchState previousMatchState, MatchState currentMatchState) {
        BandyMatchResultMarkets resultMarkets = new BandyMatchResultMarkets();
        return resultMarkets.resultMarkets(markets, (BandyMatchState) previousMatchState,
                        (BandyMatchState) currentMatchState, null);
    }

}
