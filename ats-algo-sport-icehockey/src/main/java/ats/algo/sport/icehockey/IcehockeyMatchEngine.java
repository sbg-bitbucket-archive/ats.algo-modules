package ats.algo.sport.icehockey;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.montecarloframework.matchengine.MonteCarloMatchEngine;
import ats.algo.sport.icehockey.tradingrules.IceHockeyTradingRules;

public class IcehockeyMatchEngine extends MonteCarloMatchEngine {

    public IcehockeyMatchEngine(MatchFormat matchFormat) {
        super(SupportedSportType.ICE_HOCKEY);
        matchParams = new IcehockeyMatchParams();
        matchParams.setDefaultParams(matchFormat);
        matchState = new IcehockeyMatchState(matchFormat);
        match = new IcehockeyMatch((IcehockeyMatchFormat) matchFormat, (IcehockeyMatchState) matchState,
                        (IcehockeyMatchParams) matchParams);
        initialiseConcurrentCalculationManager();
    }

    @Override
    public ResultedMarkets resultMarkets(Markets markets, AlgoMatchState previousMatchState,
                    AlgoMatchState currentMatchState) {
        IcehockeyMatchResultMarkets resultMarkets = new IcehockeyMatchResultMarkets();
        return resultMarkets.resultMarkets(markets, (IcehockeyMatchState) previousMatchState,
                        (IcehockeyMatchState) currentMatchState, null);
    }

    @Override
    public TradingRules getTradingRuleSet() {
        return new IceHockeyTradingRules();
    }

}
