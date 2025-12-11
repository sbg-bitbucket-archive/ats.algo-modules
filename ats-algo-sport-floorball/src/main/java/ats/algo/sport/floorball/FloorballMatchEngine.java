package ats.algo.sport.floorball;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.montecarloframework.matchengine.MonteCarloMatchEngine;

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
    public ResultedMarkets resultMarkets(Markets markets, MatchState previousMatchState, MatchState currentMatchState) {
        FloorballMatchResultMarkets resultMarkets = new FloorballMatchResultMarkets();
        return resultMarkets.resultMarkets(markets, (FloorballMatchState) previousMatchState,
                        (FloorballMatchState) currentMatchState, null);
    }

}
