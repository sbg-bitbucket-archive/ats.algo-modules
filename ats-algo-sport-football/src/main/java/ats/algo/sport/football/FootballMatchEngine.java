package ats.algo.sport.football;

import java.util.Set;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.baseclasses.MatchState;
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
    public ResultedMarkets resultMarkets(Markets markets, MatchState previousMatchState, MatchState currentMatchState) {
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
        // FIXME: COMMENTED WITHOUT KNOWING THE AFFECT
        // if (super.getCalcRequestCause() == CalcRequestCause.NEW_MATCH)
        // System.out.println("New Match");
        // super.setMatchEngineSavedState(new FootballMatchEngineSavedState());
        if (super.getCalcRequestCause() == CalcRequestCause.MATCH_INCIDENT) {
            FootballMatchParams footballMatchParams = (FootballMatchParams) super.getMatchParams();
            if (super.getMatchIncident().getIncidentSubType()
                            .equals(FootballMatchIncident.FootballMatchIncidentType.GOAL))
                System.out.print(super.getMatchIncident().toString());
            footballMatchParams.applyMomentumLogic(super.getMatchIncidentResult(), super.getMatchEngineSavedState());
        }
        super.calculate();
    }

    @Override
    public Set<MarketGroup> preprocessParamFind(MatchParams matchParams, MarketPricesList marketPricesList) {
        return FootballParamFindPreProcessor.process(this, matchParams, marketPricesList);
    }

}
