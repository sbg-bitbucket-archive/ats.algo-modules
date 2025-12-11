package ats.algo.sport.football;

import java.util.Set;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.AlgoMatchParams;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.montecarloframework.matchengine.MonteCarloMatchEngine;
import ats.algo.sport.football.tradingrules.FootballTradingRules;

public class FootballMatchEngine extends MonteCarloMatchEngine {

    /**
     * 
     * @param matchFormat
     * @param isforParamFinder
     */
    public FootballMatchEngine(MatchFormat matchFormat, boolean isforParamFinder) {
        super(SupportedSportType.SOCCER);
        super.setParamFindStdDevns(false);
        matchParams = createMatchParams(matchFormat);
        matchState = new FootballMatchState((FootballMatchFormat) matchFormat);
        match = new FootballMatch((FootballMatchFormat) matchFormat, (FootballMatchState) matchState,
                        (FootballMatchParams) matchParams, isforParamFinder);
        initialiseConcurrentCalculationManager();
    }

    public FootballMatchEngine(MatchFormat matchFormat) {
        this(matchFormat, false);
    }

    protected FootballMatchParams createMatchParams(MatchFormat matchFormat) {
        return new FootballMatchParams(matchFormat);
    }

    @Override
    public ResultedMarkets resultMarkets(Markets markets, AlgoMatchState previousMatchState,
                    AlgoMatchState currentMatchState) {
        FootballMatchResultMarkets resultMarkets = new FootballMatchResultMarkets();
        return resultMarkets.resultMarkets(markets, (FootballMatchState) previousMatchState,
                        (FootballMatchState) currentMatchState, null);
    }

    @Override
    public TradingRules getTradingRuleSet() {
        return new FootballTradingRules();
    }

    /** Jin added for football momentum **/
    @Override
    public void calculate() {
        if (super.getCalcRequestCause() == CalcRequestCause.MATCH_INCIDENT) {
            FootballMatchParams footballMatchParams = (FootballMatchParams) super.getMatchParams();
            footballMatchParams.applyMomentumLogic(super.getMatchIncidentResult(), super.getMatchEngineSavedState());
        }
        super.calculate();
    }

    @Override
    public Set<MarketGroup> preprocessParamFind(AlgoMatchParams matchParams, MarketPricesList marketPricesList) {
        return FootballParamFindPreProcessor.process(this, matchParams, marketPricesList);
    }

}
