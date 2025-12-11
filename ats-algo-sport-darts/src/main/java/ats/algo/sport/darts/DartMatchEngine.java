package ats.algo.sport.darts;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.montecarloframework.matchengine.MonteCarloMatchEngine;
import ats.algo.sport.darts.tradingrules.DartTradingRules;
import ats.algo.sport.darts.DartProbs.BaseProbSet;

public class DartMatchEngine extends MonteCarloMatchEngine {

    public DartMatchEngine(MatchFormat matchFormat) {
        super(SupportedSportType.DARTS);
        matchParams = new DartMatchParams(matchFormat);
        matchParams.setDefaultParams(matchFormat);
        matchState = new DartMatchState(matchFormat);
        match = new DartMatch((DartMatchFormat) matchFormat, (DartMatchState) matchState,
                        (DartMatchParams) matchParams);
        initialiseConcurrentCalculationManager();
        LegProbTables lpt = new LegProbTables();
        lpt.loadTables();
    }

    @Override
    public ResultedMarkets resultMarkets(Markets markets, AlgoMatchState previousMatchState,
                    AlgoMatchState currentMatchState) {
        DartMatchResultMarkets resultMarkets = new DartMatchResultMarkets();
        return resultMarkets.resultMarkets(markets, (DartMatchState) previousMatchState,
                        (DartMatchState) currentMatchState, null);
    }

    /**
     * sets the base probability table from which player skill probabilities are derived. Only used to support unit
     * testing. The default is BaseProbSet.LIVE
     * 
     * @param probSet
     */
    void setProbSet(BaseProbSet probSet) {
        ((DartMatch) match).setProbSet(probSet);
    }

    @Override
    public TradingRules getTradingRuleSet() {
        return new DartTradingRules();
    }

    @Override
    public void calculate() {
        if (super.getCalcRequestCause() == CalcRequestCause.MATCH_INCIDENT && super.getMatchIncidentResult() != null) {
            DartMatchParams matchParams = (DartMatchParams) super.getMatchParams();
            matchParams.updateParamsGivenMatchIncidentResult(super.getMatchIncidentResult());
        }
        super.calculate();
    }
}
