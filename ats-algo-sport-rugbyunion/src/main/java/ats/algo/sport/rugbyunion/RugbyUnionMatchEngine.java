package ats.algo.sport.rugbyunion;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.montecarloframework.matchengine.MonteCarloMatchEngine;
import ats.algo.sport.rugbyunion.RugbyUnionMatchFormat;
import ats.algo.sport.rugbyunion.RugbyUnionMatchParams;
import ats.algo.sport.rugbyunion.RugbyUnionMatchState;
import ats.algo.sport.rugbyunion.tradingrules.RugbyUnionTradingRules;


public class RugbyUnionMatchEngine extends MonteCarloMatchEngine {

    public RugbyUnionMatchEngine(MatchFormat matchFormat) {
        super(SupportedSportType.RUGBY_UNION);
        matchParams = new RugbyUnionMatchParams();
        matchParams.setDefaultParams(matchFormat);
        matchState = new RugbyUnionMatchState(matchFormat);
        match = new RugbyUnionMatch((RugbyUnionMatchFormat) matchFormat, (RugbyUnionMatchState) matchState,
                        (RugbyUnionMatchParams) matchParams);
        initialiseConcurrentCalculationManager();
    }

    @Override
    public ResultedMarkets resultMarkets(Markets markets, AlgoMatchState previousMatchState,
                    AlgoMatchState currentMatchState) {
        RugbyUnionMatchResultMarkets resultMarkets = new RugbyUnionMatchResultMarkets();
        return resultMarkets.resultMarkets(markets, (RugbyUnionMatchState) previousMatchState,
                        (RugbyUnionMatchState) currentMatchState, null);
    }

    @Override
    public TradingRules getTradingRuleSet() {
        return new RugbyUnionTradingRules();
    }

}
