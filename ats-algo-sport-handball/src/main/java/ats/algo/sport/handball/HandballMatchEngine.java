package ats.algo.sport.handball;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.montecarloframework.matchengine.MonteCarloMatchEngine;
import ats.algo.sport.handball.tradingrules.HandballTradingRules;


public class HandballMatchEngine extends MonteCarloMatchEngine {

    public HandballMatchEngine(MatchFormat matchFormat) {
        super(SupportedSportType.HANDBALL);
        matchParams = new HandballMatchParams();
        matchParams.setDefaultParams(matchFormat);
        matchState = new HandballMatchState((HandballMatchFormat) matchFormat);
        match = new HandballMatch((HandballMatchFormat) matchFormat, (HandballMatchState) matchState,
                        (HandballMatchParams) matchParams);
        initialiseConcurrentCalculationManager();
    }

    @Override
    public TradingRules getTradingRuleSet() {
        return new HandballTradingRules();
    }

    @Override
    public ResultedMarkets resultMarkets(Markets markets, AlgoMatchState previousMatchState,
                    AlgoMatchState currentMatchState) {
        HandballMatchResultMarkets resultMarkets = new HandballMatchResultMarkets();
        return resultMarkets.resultMarkets(markets, (HandballMatchState) previousMatchState,
                        (HandballMatchState) currentMatchState, null);
    }

}
