package ats.algo.loadtestsport;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.montecarloframework.matchengine.MonteCarloMatchEngine;
import ats.algo.sport.football.FootballMatchFormat;
import ats.algo.sport.football.FootballMatchParams;
import ats.algo.sport.football.FootballMatchState;

public class LoadTestSportMatchEngine extends MonteCarloMatchEngine {

    public LoadTestSportMatchEngine(MatchFormat matchFormat) {
        super(SupportedSportType.SOCCER);
        matchParams = new FootballMatchParams(matchFormat);
        matchState = new FootballMatchState((FootballMatchFormat) matchFormat);
        match = new LoadTestSportMatch((FootballMatchFormat) matchFormat, (FootballMatchState) matchState,
                        (FootballMatchParams) matchParams);
        initialiseConcurrentCalculationManager();
    }

    @Override
    public ResultedMarkets resultMarkets(Markets markets, AlgoMatchState previousMatchState,
                    AlgoMatchState currentMatchState) {
        LoadTestSportResultMarkets resultMarkets = new LoadTestSportResultMarkets();
        return resultMarkets.resultMarkets(markets, (FootballMatchState) previousMatchState,
                        (FootballMatchState) currentMatchState, null);
    }

}
