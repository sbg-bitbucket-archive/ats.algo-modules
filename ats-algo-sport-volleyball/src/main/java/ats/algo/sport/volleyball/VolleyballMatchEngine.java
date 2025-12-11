package ats.algo.sport.volleyball;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.montecarloframework.matchengine.MonteCarloMatchEngine;
import ats.algo.sport.volleyball.tradingrules.VolleyballTradingRules;

public class VolleyballMatchEngine extends MonteCarloMatchEngine {

    public VolleyballMatchEngine(MatchFormat matchFormat) {
        super(SupportedSportType.VOLLEYBALL);
        VolleyballMatchFormat volleyballMatchFormat = (VolleyballMatchFormat) matchFormat;
        matchParams = new VolleyballMatchParams();
        matchParams.setDefaultParams(volleyballMatchFormat);
        matchState = new VolleyballMatchState(volleyballMatchFormat);
        match = new VolleyballMatch(volleyballMatchFormat, (VolleyballMatchState) matchState,
                        (VolleyballMatchParams) matchParams);
        initialiseConcurrentCalculationManager();
    }

    @Override
    public ResultedMarkets resultMarkets(Markets markets, MatchState previousMatchState, MatchState currentMatchState) {
        VolleyballMatchResultMarkets resultMarkets = new VolleyballMatchResultMarkets();
        return resultMarkets.resultMarkets(markets, (VolleyballMatchState) previousMatchState,
                        (VolleyballMatchState) currentMatchState, null);
    }

    @Override
    public TradingRules getTradingRuleSet() {
        return new VolleyballTradingRules();
    }

    @Override
    public void calculate() {
        if (super.getCalcRequestCause() == CalcRequestCause.MATCH_INCIDENT && super.getMatchIncidentResult() != null) {
            VolleyballMatchParams matchParams = (VolleyballMatchParams) super.getMatchParams();
            matchParams.updateParamsGivenMatchIncidentResult(super.getMatchIncidentResult());
        }
        super.calculate();
    }
}
