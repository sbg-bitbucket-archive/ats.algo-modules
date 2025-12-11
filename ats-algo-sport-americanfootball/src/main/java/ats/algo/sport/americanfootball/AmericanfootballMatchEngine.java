package ats.algo.sport.americanfootball;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.montecarloframework.matchengine.MonteCarloMatchEngine;
import ats.algo.sport.americanfootball.AmericanfootballMatchFormat;
import ats.algo.sport.americanfootball.AmericanfootballMatchParams;
import ats.algo.sport.americanfootball.AmericanfootballMatchState;
import ats.algo.sport.americanfootball.tradingrules.AmericanFootballTradingRules;


public class AmericanfootballMatchEngine extends MonteCarloMatchEngine {

    public AmericanfootballMatchEngine(MatchFormat matchFormat) {
        super(SupportedSportType.AMERICAN_FOOTBALL);
        matchParams = new AmericanfootballMatchParams();
        matchParams.setDefaultParams(matchFormat);
        matchState = new AmericanfootballMatchState(matchFormat);
        match = new AmericanfootballMatch((AmericanfootballMatchFormat) matchFormat,
                        (AmericanfootballMatchState) matchState, (AmericanfootballMatchParams) matchParams);
        initialiseConcurrentCalculationManager();
    }

    @Override
    public ResultedMarkets resultMarkets(Markets markets, MatchState previousMatchState, MatchState currentMatchState) {
        AmericanfootballMatchResultMarkets resultMarkets = new AmericanfootballMatchResultMarkets();
        return resultMarkets.resultMarkets(markets, (AmericanfootballMatchState) previousMatchState,
                        (AmericanfootballMatchState) currentMatchState, null);
    }

    @Override
    public TradingRules getTradingRuleSet() {
        return new AmericanFootballTradingRules();
    }

}
