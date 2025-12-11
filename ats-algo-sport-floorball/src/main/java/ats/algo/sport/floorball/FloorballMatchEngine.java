package ats.algo.sport.floorball;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.montecarloframework.matchengine.MonteCarloMatchEngine;
import ats.algo.sport.floorball.tradingrules.FloorballTradingRules;

public class FloorballMatchEngine extends MonteCarloMatchEngine {

    public FloorballMatchEngine(MatchFormat matchFormat) {
        super(SupportedSportType.FLOORBALL);
        matchParams = new FloorballMatchParams();
        matchParams.setDefaultParams(matchFormat);
        matchState = new FloorballMatchState(matchFormat);
        match = new FloorballMatch((FloorballMatchFormat) matchFormat, (FloorballMatchState) matchState,
                        (FloorballMatchParams) matchParams);
        initialiseConcurrentCalculationManager();
    }

    @Override
    public ResultedMarkets resultMarkets(Markets markets, AlgoMatchState previousMatchState,
                    AlgoMatchState currentMatchState) {
        FloorballMatchResultMarkets resultMarkets = new FloorballMatchResultMarkets();
        return resultMarkets.resultMarkets(markets, (FloorballMatchState) previousMatchState,
                        (FloorballMatchState) currentMatchState, null);
    }

    @Override
    public TradingRules getTradingRuleSet() {
        return new FloorballTradingRules();
    }

}
