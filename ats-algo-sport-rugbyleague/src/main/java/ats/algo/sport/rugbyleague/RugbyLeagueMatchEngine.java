package ats.algo.sport.rugbyleague;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.montecarloframework.matchengine.MonteCarloMatchEngine;
import ats.algo.sport.rugbyleague.RugbyLeagueMatch;


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
    public ResultedMarkets resultMarkets(Markets markets, MatchState previousMatchState, MatchState currentMatchState) {
        RugbyLeagueMatchResultMarkets resultMarkets = new RugbyLeagueMatchResultMarkets();
        return resultMarkets.resultMarkets(markets, (RugbyLeagueMatchState) previousMatchState,
                        (RugbyLeagueMatchState) currentMatchState, null);
    }

}
