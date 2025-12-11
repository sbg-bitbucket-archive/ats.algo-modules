package ats.algo.sport.testcricket;

import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.montecarloframework.matchengine.MonteCarloMatchEngine;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.SupportedSportType;

public class TestCricketMatchEngine extends MonteCarloMatchEngine {

    public TestCricketMatchEngine(MatchFormat matchFormat) {
        super(SupportedSportType.TESTCRICKET);
        TestCricketMatchFormat cricketMatchFormat = (TestCricketMatchFormat) matchFormat;
        matchParams = new TestCricketMatchParams();
        matchParams.setDefaultParams(cricketMatchFormat);
        matchState = new TestCricketMatchState(cricketMatchFormat);
        match = new TestCricketMatch(cricketMatchFormat, (TestCricketMatchState) matchState,
                        (TestCricketMatchParams) matchParams);
        initialiseConcurrentCalculationManager();
    }

    @Override
    public ResultedMarkets resultMarkets(Markets markets, MatchState previousMatchState, MatchState currentMatchState) {
        TestCricketMatchResultMarkets resultMarkets = new TestCricketMatchResultMarkets();
        return resultMarkets.resultMarkets(markets, (TestCricketMatchState) previousMatchState,
                        (TestCricketMatchState) currentMatchState, null);
    }
}
