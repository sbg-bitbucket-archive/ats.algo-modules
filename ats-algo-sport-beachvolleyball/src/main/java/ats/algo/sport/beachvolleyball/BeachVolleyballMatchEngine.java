package ats.algo.sport.beachvolleyball;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.montecarloframework.matchengine.MonteCarloMatchEngine;
import ats.algo.sport.beachvolleyball.tradingrules.BeachVolleyballTradingRules;

public class BeachVolleyballMatchEngine extends MonteCarloMatchEngine {

    public BeachVolleyballMatchEngine(MatchFormat matchFormat) {
        super(SupportedSportType.BEACH_VOLLEYBALL);
        BeachVolleyballMatchFormat beachVolleyballMatchFormat = (BeachVolleyballMatchFormat) matchFormat;
        matchParams = new BeachVolleyballMatchParams();
        matchParams.setDefaultParams(beachVolleyballMatchFormat);
        matchState = new BeachVolleyballMatchState(beachVolleyballMatchFormat);
        match = new BeachVolleyballMatch(beachVolleyballMatchFormat, (BeachVolleyballMatchState) matchState,
                        (BeachVolleyballMatchParams) matchParams);
        initialiseConcurrentCalculationManager();
    }

    @Override
    public ResultedMarkets resultMarkets(Markets markets, MatchState previousMatchState, MatchState currentMatchState) {
        BeachVolleyballMatchResultMarkets resultMarkets = new BeachVolleyballMatchResultMarkets();
        return resultMarkets.resultMarkets(markets, (BeachVolleyballMatchState) previousMatchState,
                        (BeachVolleyballMatchState) currentMatchState, null);
    }

    @Override
    public TradingRules getTradingRuleSet() {
        return new BeachVolleyballTradingRules();
    }

    @Override
    public void calculate() {
        if (super.getCalcRequestCause() == CalcRequestCause.MATCH_INCIDENT && super.getMatchIncidentResult() != null) {
            BeachVolleyballMatchParams matchParams = (BeachVolleyballMatchParams) super.getMatchParams();
            matchParams.updateParamsGivenMatchIncidentResult(super.getMatchIncidentResult());
        }
        super.calculate();
    }
}
