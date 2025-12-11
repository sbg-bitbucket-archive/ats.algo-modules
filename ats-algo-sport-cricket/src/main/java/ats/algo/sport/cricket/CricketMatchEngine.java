package ats.algo.sport.cricket;

import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.montecarloframework.matchengine.MonteCarloMatchEngine;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.common.SupportedSportType;

public class CricketMatchEngine extends MonteCarloMatchEngine {

    public CricketMatchEngine(MatchFormat matchFormat) {
        super(SupportedSportType.CRICKET);
        CricketMatchFormat cricketMatchFormat = (CricketMatchFormat) matchFormat;
        matchParams = new CricketMatchParams();
        matchParams.setDefaultParams(cricketMatchFormat);
        matchState = new CricketMatchState(cricketMatchFormat);
        match = new CricketMatch(cricketMatchFormat, (CricketMatchState) matchState, (CricketMatchParams) matchParams);
        initialiseConcurrentCalculationManager();
    }

    @Override
    public ResultedMarkets resultMarkets(Markets markets, MatchState previousMatchState, MatchState currentMatchState) {
        CricketMatchResultMarkets resultMarkets = new CricketMatchResultMarkets();
        return resultMarkets.resultMarkets(markets, (CricketMatchState) previousMatchState,
                        (CricketMatchState) currentMatchState, null);
    }

    @Override
    public void calculate() {
        if (super.getCalcRequestCause() == CalcRequestCause.MATCH_INCIDENT && super.getMatchIncidentResult() != null) {
            CricketMatchParams matchParams = (CricketMatchParams) super.getMatchParams();
            matchParams.updateParamsGivenMatchIncidentResult(super.getMatchIncidentResult());
        }
        super.calculate();
    }
}
