package ats.algo.sport.tennis;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import ats.algo.core.abandonIncident.tradingrules.AbandonIncidentTradingRule;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.MarketsAwaitingResult;
import ats.algo.core.markets.ResultedMarket;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.tradingrules.AbstractTradingRule;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.montecarloframework.matchengine.MonteCarloMatchEngine;
import ats.algo.sport.tennis.tradingrules.TennisTradingRules;

public class TennisMatchEngine extends MonteCarloMatchEngine {
    TradingRules tradingRules = null;

    public TennisMatchEngine(MatchFormat matchFormat) {
        super(SupportedSportType.TENNIS);
        super.setParamFindStdDevns(true);
        matchParams = new TennisMatchParams();
        matchParams.setDefaultParams(matchFormat);
        matchState = new TennisMatchState((TennisMatchFormat) matchFormat);
        match = new TennisMatch((TennisMatchFormat) matchFormat, (TennisMatchState) matchState,
                        (TennisMatchParams) matchParams);

        initialiseConcurrentCalculationManager();
    }

    @Override
    public ResultedMarkets resultMarkets(Markets markets, MatchState previousMatchState, MatchState currentMatchState) {
        TennisMatchResultMarkets resultMarkets = new TennisMatchResultMarkets();
        TradingRules ruleSets = (TradingRules) this.getTradingRuleSet();
        List<String> list = null;
        for (AbstractTradingRule rule : ruleSets.getTradingRules()) {
            if (rule instanceof AbandonIncidentTradingRule)
                list = ((AbandonIncidentTradingRule) rule).getSuspendList();
        }
        // if (list != null)
        // LoggerFactory.getLogger(TennisMatchEngine.class).info("List is " + list.toString());
        // else
        // LoggerFactory.getLogger(TennisMatchEngine.class).info("List is empty!");

        return resultMarkets.resultMarkets(markets, (TennisMatchState) previousMatchState,
                        (TennisMatchState) currentMatchState, list);
    }

    @Override
    public ResultedMarkets resultMarketsForAbandonedEvent(Markets markets, MatchState mostRecentMatchState) {
        ResultedMarkets resultedMarkets = new ResultedMarkets();
        List<String> nonVoidList = null;
        TradingRules ruleSets = (TradingRules) this.getTradingRuleSet();
        for (AbstractTradingRule rule : ruleSets.getTradingRules()) {
            if (rule instanceof AbandonIncidentTradingRule)
                nonVoidList = ((AbandonIncidentTradingRule) rule).getSuspendList();
        }
        boolean listExist = nonVoidList != null;
        for (Market market : markets) {
            if (listExist) {
                if (nonVoidList.contains(market.getType()))
                    continue;
            }
            ResultedMarket resultedMarket = voidMarket(market);
            resultedMarkets.addMarket(resultedMarket);
        }
        return resultedMarkets;
    }

    @Override
    public TradingRules getTradingRuleSet() {

        if (tradingRules == null) {
            String tradingRuleClass = System.getProperty("algo.tennis.tradingrules.class");
            if (tradingRuleClass == null) {
                tradingRules = new TennisTradingRules();
                return tradingRules;
            } else {
                try {
                    Class<?> clazz = Class.forName(tradingRuleClass);
                    Constructor<?> ctor = clazz.getConstructor();
                    Object object = ctor.newInstance();
                    tradingRules = (TradingRules) object;
                    return tradingRules;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                return new TennisTradingRules();
            }

        } else {
            return tradingRules;
        }
    }

    @Override
    public void calculate() {
        if (super.getCalcRequestCause() == CalcRequestCause.NEW_MATCH)
            super.setMatchEngineSavedState(new TennisMatchEngineSavedState());
        if (super.getCalcRequestCause() == CalcRequestCause.MATCH_INCIDENT) {
            TennisMatchParams tennisMatchParams = (TennisMatchParams) super.getMatchParams();
            if (super.getMatchIncidentResult() != null)
                tennisMatchParams.applyMomentumLogic(this.matchState, super.getMatchIncidentResult(),
                                super.getMatchEngineSavedState());
        }
        super.calculate();
    }

    // FIXME: renaming to result losing selections, this method is cleaning up the selections just resulted
    @Override
    public void cleanUpMarketAwaitingResultSelections(MarketsAwaitingResult marketsAwaitingResult,
                    ResultedMarket resultedMarket) {
        ArrayList<String> resultedPartialLosings = (ArrayList<String>) resultedMarket.getLosingSelections();
        ArrayList<String> resultedPartialWinnings = (ArrayList<String>) resultedMarket.getLosingSelections();
        ArrayList<String> resultedPartialVoideds = (ArrayList<String>) resultedMarket.getLosingSelections();
        String baseMarketFullKey = resultedMarket.getFullKey();


        for (String resultedSelection : resultedPartialLosings)
            marketsAwaitingResult.updateMarketsAwaitingResultSelections(baseMarketFullKey, resultedSelection);
        for (String resultedSelection : resultedPartialWinnings)
            marketsAwaitingResult.updateMarketsAwaitingResultSelections(baseMarketFullKey, resultedSelection);
        for (String resultedSelection : resultedPartialVoideds)
            marketsAwaitingResult.updateMarketsAwaitingResultSelections(baseMarketFullKey, resultedSelection);
    }

}
