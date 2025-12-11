package ats.algo.sport.afl;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.montecarloframework.matchengine.MonteCarloMatchEngine;
import ats.algo.sport.afl.AflMatchFormat;
import ats.algo.sport.afl.AflMatchParams;
import ats.algo.sport.afl.AflMatchState;
import ats.algo.sport.afl.tradingrules.AflTradingRules;

public class AflMatchEngine extends MonteCarloMatchEngine {

    public AflMatchEngine(MatchFormat matchFormat) {
        super(SupportedSportType.AUSSIE_RULES);
        matchParams = new AflMatchParams();
        matchParams.setDefaultParams(matchFormat);
        matchState = new AflMatchState(matchFormat);
        match = new AflMatch((AflMatchFormat) matchFormat, (AflMatchState) matchState, (AflMatchParams) matchParams);
        initialiseConcurrentCalculationManager();
    }

    @Override
    public ResultedMarkets resultMarkets(Markets markets, MatchState previousMatchState, MatchState currentMatchState) {
        AflMatchResultMarkets resultMarkets = new AflMatchResultMarkets();
        return resultMarkets.resultMarkets(markets, (AflMatchState) previousMatchState,
                        (AflMatchState) currentMatchState, null);
    }

    @Override
    public TradingRules getTradingRuleSet() {
        return new AflTradingRules();
    }

}
