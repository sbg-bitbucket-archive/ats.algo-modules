package ats.algo.sport.futsal;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.montecarloframework.matchengine.MonteCarloMatchEngine;
import ats.algo.sport.futsal.tradingrules.FutsalTradingRules;


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
    public TradingRules getTradingRuleSet() {
        return new FutsalTradingRules();
    }

    @Override
    public ResultedMarkets resultMarkets(Markets markets, AlgoMatchState previousMatchState,
                    AlgoMatchState currentMatchState) {
        FutsalMatchResultMarkets resultMarkets = new FutsalMatchResultMarkets();
        return resultMarkets.resultMarkets(markets, (FutsalMatchState) previousMatchState,
                        (FutsalMatchState) currentMatchState, null);
    }

}
