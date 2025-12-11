package ats.algo.sport.basketball;

import java.util.Set;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.montecarloframework.matchengine.MonteCarloMatchEngine;
import ats.algo.sport.basketball.tradingrules.BasketballTradingRules;

@SuppressWarnings("unused")
public class BasketballMatchEngine extends MonteCarloMatchEngine {

    public BasketballMatchEngine(MatchFormat format) {
        super(SupportedSportType.BASKETBALL);
        BasketballMatchFormat matchFormat = (BasketballMatchFormat) format;
        matchParams = new BasketballMatchParams();
        matchParams.setDefaultParams(matchFormat);
        matchState = new BasketballMatchState(matchFormat);
        match = new BasketballMatch(matchFormat, (BasketballMatchState) matchState,
                        (BasketballMatchParams) matchParams);
        initialiseConcurrentCalculationManager();
    }

    @Override
    public ResultedMarkets resultMarkets(Markets markets, AlgoMatchState previousMatchState,
                    AlgoMatchState currentMatchState) {
        BasketballMatchResultMarkets resultMarkets = new BasketballMatchResultMarkets();
        return resultMarkets.resultMarkets(markets, (BasketballMatchState) previousMatchState,
                        (BasketballMatchState) currentMatchState, null);
    }

    @Override
    public TradingRules getTradingRuleSet() {
        return new BasketballTradingRules();
    }

    // @Override
    // public Set<MarketGroup> preprocessParamFind(MatchParams matchParams, MarketPricesList marketPricesList) {
    // return BasketballParamFindPreProcessor.process(this, matchParams, marketPricesList);
    // }

}
