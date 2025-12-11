package ats.algo.sport.futsal;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.montecarloframework.matchengine.MonteCarloMatchEngine;


public class FutsalMatchEngine extends MonteCarloMatchEngine {

    public FutsalMatchEngine(MatchFormat matchFormat) {
        super(SupportedSportType.FUTSAL);
        matchParams = new FutsalMatchParams();
        matchParams.setDefaultParams(matchFormat);
        matchState = new FutsalMatchState(matchFormat);
        match = new FutsalMatch((FutsalMatchFormat) matchFormat, (FutsalMatchState) matchState,
                        (FutsalMatchParams) matchParams);
        initialiseConcurrentCalculationManager();
    }

    @Override
    public ResultedMarkets resultMarkets(Markets markets, MatchState previousMatchState, MatchState currentMatchState) {
        FutsalMatchResultMarkets resultMarkets = new FutsalMatchResultMarkets();
        return resultMarkets.resultMarkets(markets, (FutsalMatchState) previousMatchState,
                        (FutsalMatchState) currentMatchState, null);
    }

}
