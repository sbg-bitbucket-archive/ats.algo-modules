package ats.algo.sport.squash;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.montecarloframework.matchengine.MonteCarloMatchEngine;
import ats.algo.sport.squash.tradingrules.SquashTradingRules;

public class SquashMatchEngine extends MonteCarloMatchEngine {

    public SquashMatchEngine(MatchFormat matchFormat) {
        super(SupportedSportType.SQUASH);
        super.setParamFindStdDevns(true);
        SquashMatchFormat squashMatchFormat = (SquashMatchFormat) matchFormat;
        matchParams = new SquashMatchParams();
        matchParams.setDefaultParams(squashMatchFormat);
        matchState = new SquashMatchState(squashMatchFormat);
        match = new SquashMatch(squashMatchFormat, (SquashMatchState) matchState, (SquashMatchParams) matchParams);
        initialiseConcurrentCalculationManager();
    }

    @Override
    public ResultedMarkets resultMarkets(Markets markets, MatchState previousMatchState, MatchState currentMatchState) {
        SquashMatchResultMarkets resultMarkets = new SquashMatchResultMarkets();
        return resultMarkets.resultMarkets(markets, (SquashMatchState) previousMatchState,
                        (SquashMatchState) currentMatchState, null);
    }

    @Override
    public void calculate() {
        if (super.getCalcRequestCause() == CalcRequestCause.MATCH_INCIDENT && super.getMatchIncidentResult() != null) {
            SquashMatchParams matchParams = (SquashMatchParams) super.getMatchParams();
            matchParams.updateParamsGivenMatchIncidentResult(super.getMatchIncidentResult());
        }
        super.calculate();
    }

    @Override
    public TradingRules getTradingRuleSet() {
        return new SquashTradingRules();
    }
}
