package ats.algo.sport.tabletennis;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.montecarloframework.matchengine.MonteCarloMatchEngine;
import ats.algo.sport.tabletennis.tradingrules.TabletennisTradingRules;

public class TabletennisMatchEngine extends MonteCarloMatchEngine {

    public TabletennisMatchEngine(MatchFormat matchFormat) {
        super(SupportedSportType.TABLE_TENNIS);
        TabletennisMatchFormat tabletennisMatchFormat = (TabletennisMatchFormat) matchFormat;
        matchParams = new TabletennisMatchParams();
        matchParams.setDefaultParams(tabletennisMatchFormat);
        matchState = new TabletennisMatchState(tabletennisMatchFormat);
        match = new TabletennisMatch(tabletennisMatchFormat, (TabletennisMatchState) matchState,
                        (TabletennisMatchParams) matchParams);
        initialiseConcurrentCalculationManager();
    }

    @Override
    public ResultedMarkets resultMarkets(Markets markets, AlgoMatchState previousMatchState,
                    AlgoMatchState currentMatchState) {
        TabletennisResultMarkets resultMarkets = new TabletennisResultMarkets();
        return resultMarkets.resultMarkets(markets, (TabletennisMatchState) previousMatchState,
                        (TabletennisMatchState) currentMatchState, null);
    }

    @Override
    public void calculate() {
        if (super.getCalcRequestCause() == CalcRequestCause.MATCH_INCIDENT && super.getMatchIncidentResult() != null) {
            TabletennisMatchParams matchParams = (TabletennisMatchParams) super.getMatchParams();
            matchParams.updateParamsGivenMatchIncidentResult(super.getMatchIncidentResult());
        }
        super.calculate();
    }

    @Override
    public TradingRules getTradingRuleSet() {
        return new TabletennisTradingRules();
    }
}
