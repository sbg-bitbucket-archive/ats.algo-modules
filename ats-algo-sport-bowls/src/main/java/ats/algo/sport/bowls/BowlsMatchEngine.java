package ats.algo.sport.bowls;

import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.montecarloframework.matchengine.MonteCarloMatchEngine;
import ats.algo.sport.bowls.tradingrules.BowlsTradingRules;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.common.SupportedSportType;

public class BowlsMatchEngine extends MonteCarloMatchEngine {

    public BowlsMatchEngine(MatchFormat matchFormat) {
        super(SupportedSportType.BOWLS);
        BowlsMatchFormat bowlsMatchFormat = (BowlsMatchFormat) matchFormat;
        matchParams = new BowlsMatchParams();
        matchParams.setDefaultParams(bowlsMatchFormat);
        matchState = new BowlsMatchState(bowlsMatchFormat);
        match = new BowlsMatch(bowlsMatchFormat, (BowlsMatchState) matchState, (BowlsMatchParams) matchParams);
        initialiseConcurrentCalculationManager();
    }

    @Override
    public ResultedMarkets resultMarkets(Markets markets, AlgoMatchState previousMatchState,
                    AlgoMatchState currentMatchState) {
        BowlsMatchResultMarkets resultMarkets = new BowlsMatchResultMarkets();
        return resultMarkets.resultMarkets(markets, (BowlsMatchState) previousMatchState,
                        (BowlsMatchState) currentMatchState, null);
    }

    @Override
    public void calculate() {
        if (super.getCalcRequestCause() == CalcRequestCause.MATCH_INCIDENT && super.getMatchIncidentResult() != null) {
            BowlsMatchParams matchParams = (BowlsMatchParams) super.getMatchParams();
            matchParams.updateParamsGivenMatchIncidentResult(super.getMatchIncidentResult());
        }
        super.calculate();
    }


    @Override
    public TradingRules getTradingRuleSet() {
        return new BowlsTradingRules();
    }
}
