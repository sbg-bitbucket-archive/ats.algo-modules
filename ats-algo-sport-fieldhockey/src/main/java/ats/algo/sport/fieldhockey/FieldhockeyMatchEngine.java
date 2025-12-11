package ats.algo.sport.fieldhockey;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.montecarloframework.matchengine.MonteCarloMatchEngine;
import ats.algo.sport.fieldhockey.tradingrules.FieldhockeyTradingRules;


public class FieldhockeyMatchEngine extends MonteCarloMatchEngine {

    public FieldhockeyMatchEngine(MatchFormat matchFormat) {
        super(SupportedSportType.FIELD_HOCKEY);
        matchParams = new FieldhockeyMatchParams();
        matchParams.setDefaultParams(matchFormat);
        matchState = new FieldhockeyMatchState(matchFormat);
        match = new FieldhockeyMatch((FieldhockeyMatchFormat) matchFormat, (FieldhockeyMatchState) matchState,
                        (FieldhockeyMatchParams) matchParams);
        initialiseConcurrentCalculationManager();
    }

    @Override
    public ResultedMarkets resultMarkets(Markets markets, AlgoMatchState previousMatchState,
                    AlgoMatchState currentMatchState) {
        FieldhockeyMatchResultMarkets resultMarkets = new FieldhockeyMatchResultMarkets();
        return resultMarkets.resultMarkets(markets, (FieldhockeyMatchState) previousMatchState,
                        (FieldhockeyMatchState) currentMatchState, null);
    }

    @Override
    public TradingRules getTradingRuleSet() {
        return new FieldhockeyTradingRules();
    }

}
