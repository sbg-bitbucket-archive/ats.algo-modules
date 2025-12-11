package ats.algo.sport.fieldhockey;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.montecarloframework.matchengine.MonteCarloMatchEngine;


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
    public ResultedMarkets resultMarkets(Markets markets, MatchState previousMatchState, MatchState currentMatchState) {
        FieldhockeyMatchResultMarkets resultMarkets = new FieldhockeyMatchResultMarkets();
        return resultMarkets.resultMarkets(markets, (FieldhockeyMatchState) previousMatchState,
                        (FieldhockeyMatchState) currentMatchState, null);
    }

}
