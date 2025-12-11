package ats.algo.sport.rollerhockey;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.montecarloframework.matchengine.MonteCarloMatchEngine;


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
    public ResultedMarkets resultMarkets(Markets markets, MatchState previousMatchState, MatchState currentMatchState) {
        RollerhockeyMatchResultMarkets resultMarkets = new RollerhockeyMatchResultMarkets();
        return resultMarkets.resultMarkets(markets, (RollerhockeyMatchState) previousMatchState,
                        (RollerhockeyMatchState) currentMatchState, null);
    }

}
