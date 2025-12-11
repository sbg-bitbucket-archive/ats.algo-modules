package ats.algo.sport.tennisG;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.montecarloframework.matchengine.MonteCarloMatchEngine;
import ats.algo.sport.tennisG.TennisGMatchFormat;
import ats.algo.sport.tennisG.TennisGMatchParams;
import ats.algo.sport.tennisG.TennisGMatchState;

public class TennisGMatchEngine extends MonteCarloMatchEngine {

    public TennisGMatchEngine(MatchFormat matchFormat) {
        super(SupportedSportType.TEST_TENNISG);
        matchParams = new TennisGMatchParams();
        matchParams.setDefaultParams(matchFormat);
        matchState = new TennisGMatchState((TennisGMatchFormat) matchFormat);
        match = new TennisGMatch((TennisGMatchFormat) matchFormat, (TennisGMatchState) matchState,
                        (TennisGMatchParams) matchParams);
        initialiseConcurrentCalculationManager();
    }

    @Override
    public ResultedMarkets resultMarkets(Markets markets, MatchState previousMatchState, MatchState currentMatchState) {
        TennisGMatchResultMarkets resultMarkets = new TennisGMatchResultMarkets();
        return resultMarkets.resultMarkets(markets, (TennisGMatchState) previousMatchState,
                        (TennisGMatchState) currentMatchState, null);
    }

}
