package ats.algo.sport.handball;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.montecarloframework.matchengine.MonteCarloMatchEngine;


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
    public ResultedMarkets resultMarkets(Markets markets, MatchState previousMatchState, MatchState currentMatchState) {
        HandballMatchResultMarkets resultMarkets = new HandballMatchResultMarkets();
        return resultMarkets.resultMarkets(markets, (HandballMatchState) previousMatchState,
                        (HandballMatchState) currentMatchState, null);
    }

}
