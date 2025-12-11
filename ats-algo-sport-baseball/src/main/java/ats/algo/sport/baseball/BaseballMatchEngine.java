package ats.algo.sport.baseball;

import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.montecarloframework.matchengine.MonteCarloMatchEngine;
import ats.algo.sport.baseball.tradingrules.BaseballTradingRules;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.SupportedSportType;

public class BaseballMatchEngine extends MonteCarloMatchEngine {

    public BaseballMatchEngine(MatchFormat matchFormat) {
        super(SupportedSportType.BASEBALL);
        BaseballMatchFormat baseballMatchFormat = (BaseballMatchFormat) matchFormat;
        matchParams = new BaseballMatchParams();
        matchParams.setDefaultParams(baseballMatchFormat);
        matchState = new BaseballMatchState(baseballMatchFormat);
        match = new BaseballMatch(baseballMatchFormat, (BaseballMatchState) matchState,
                        (BaseballMatchParams) matchParams);
        initialiseConcurrentCalculationManager();
    }

    @Override
    public ResultedMarkets resultMarkets(Markets markets, MatchState previousMatchState, MatchState currentMatchState) {
        BaseballMatchResultMarkets resultMarkets = new BaseballMatchResultMarkets();
        return resultMarkets.resultMarkets(markets, (BaseballMatchState) previousMatchState,
                        (BaseballMatchState) currentMatchState, null);
    }

    @Override
    public TradingRules getTradingRuleSet() {
        return new BaseballTradingRules();
    }
}
