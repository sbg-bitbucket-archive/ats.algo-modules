package ats.algo.sport.rollerhockey;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.montecarloframework.matchengine.MonteCarloMatchEngine;
import ats.algo.sport.rollerhockey.tradingrules.RollerhockeyTradingRules;


public class RollerhockeyMatchEngine extends MonteCarloMatchEngine {

    public RollerhockeyMatchEngine(MatchFormat matchFormat) {
        super(SupportedSportType.ROLLER_HOCKEY);
        matchParams = new RollerhockeyMatchParams();
        matchParams.setDefaultParams(matchFormat);
        matchState = new RollerhockeyMatchState(matchFormat);
        match = new RollerhockeyMatch((RollerhockeyMatchFormat) matchFormat, (RollerhockeyMatchState) matchState,
                        (RollerhockeyMatchParams) matchParams);
        initialiseConcurrentCalculationManager();
    }

    @Override
    public ResultedMarkets resultMarkets(Markets markets, AlgoMatchState previousMatchState,
                    AlgoMatchState currentMatchState) {
        RollerhockeyMatchResultMarkets resultMarkets = new RollerhockeyMatchResultMarkets();
        return resultMarkets.resultMarkets(markets, (RollerhockeyMatchState) previousMatchState,
                        (RollerhockeyMatchState) currentMatchState, null);
    }

    @Override
    public TradingRules getTradingRuleSet() {
        return new RollerhockeyTradingRules();
    }

}
