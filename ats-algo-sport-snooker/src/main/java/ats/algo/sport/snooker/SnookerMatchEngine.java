package ats.algo.sport.snooker;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.montecarloframework.matchengine.MonteCarloMatchEngine;
import ats.algo.sport.snooker.tradingrules.SnookerTradingRules;

public class SnookerMatchEngine extends MonteCarloMatchEngine {

    public SnookerMatchEngine(MatchFormat matchFormat) {
        super(SupportedSportType.SNOOKER);
        SnookerMatchFormat snookerMatchFormat = (SnookerMatchFormat) matchFormat;
        matchParams = new SnookerMatchParams();
        matchParams.setDefaultParams(snookerMatchFormat);
        matchState = new SnookerMatchState(snookerMatchFormat);
        match = new SnookerMatch(snookerMatchFormat, (SnookerMatchState) matchState, (SnookerMatchParams) matchParams);
        initialiseConcurrentCalculationManager();
    }

    @Override
    public ResultedMarkets resultMarkets(Markets markets, MatchState previousMatchState, MatchState currentMatchState) {
        SnookerMatchResultMarkets resultMarkets = new SnookerMatchResultMarkets();
        return resultMarkets.resultMarkets(markets, (SnookerMatchState) previousMatchState,
                        (SnookerMatchState) currentMatchState, null);
    }

    @Override
    public TradingRules getTradingRuleSet() {
        return new SnookerTradingRules();
    }

    @Override
    public void calculate() {
        if (super.getCalcRequestCause() == CalcRequestCause.MATCH_INCIDENT && super.getMatchIncidentResult() != null) {
            SnookerMatchParams matchParams = (SnookerMatchParams) super.getMatchParams();
            matchParams.updateParamsGivenMatchIncidentResult(super.getMatchIncidentResult());
        }
        super.calculate();
    }
}
