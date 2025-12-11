package ats.algo.sport.rugbyleague;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.montecarloframework.matchengine.MonteCarloMatchEngine;
import ats.algo.sport.rugbyleague.RugbyLeagueMatch;
import ats.algo.sport.rugbyleague.tradingrules.RugbyLeagueTradingRules;


public class RugbyLeagueMatchEngine extends MonteCarloMatchEngine {

    public RugbyLeagueMatchEngine(MatchFormat matchFormat) {
        super(SupportedSportType.RUGBY_LEAGUE);
        matchParams = new RugbyLeagueMatchParams();
        matchParams.setDefaultParams(matchFormat);
        matchState = new RugbyLeagueMatchState(matchFormat);
        match = new RugbyLeagueMatch((RugbyLeagueMatchFormat) matchFormat, (RugbyLeagueMatchState) matchState,
                        (RugbyLeagueMatchParams) matchParams);
        initialiseConcurrentCalculationManager();
    }

    @Override
    public ResultedMarkets resultMarkets(Markets markets, AlgoMatchState previousMatchState,
                    AlgoMatchState currentMatchState) {
        RugbyLeagueMatchResultMarkets resultMarkets = new RugbyLeagueMatchResultMarkets();
        return resultMarkets.resultMarkets(markets, (RugbyLeagueMatchState) previousMatchState,
                        (RugbyLeagueMatchState) currentMatchState, null);
    }

    @Override
    public TradingRules getTradingRuleSet() {
        return new RugbyLeagueTradingRules();
    }

}
