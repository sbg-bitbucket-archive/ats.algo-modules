package ats.algo.sport.badminton;

import java.util.List;

import ats.algo.core.abandonIncident.tradingrules.AbandonIncidentTradingRule;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarket;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.tradingrules.AbstractTradingRule;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.montecarloframework.matchengine.MonteCarloMatchEngine;
import ats.algo.sport.badminton.tradingrules.BadmintonTradingRules;

public class BadmintonMatchEngine extends MonteCarloMatchEngine {

    public BadmintonMatchEngine(MatchFormat matchFormat) {
        super(SupportedSportType.BADMINTON);
        super.setParamFindStdDevns(true);
        BadmintonMatchFormat badmintonMatchFormat = (BadmintonMatchFormat) matchFormat;
        matchParams = new BadmintonMatchParams();
        matchParams.setDefaultParams(badmintonMatchFormat);
        matchState = new BadmintonMatchState(badmintonMatchFormat);
        match = new BadmintonMatch(badmintonMatchFormat, (BadmintonMatchState) matchState,
                        (BadmintonMatchParams) matchParams);
        initialiseConcurrentCalculationManager();
    }

    @Override
    public ResultedMarkets resultMarkets(Markets markets, MatchState previousMatchState, MatchState currentMatchState) {
        BadmintonTradingRules ruleSets = (BadmintonTradingRules) this.getTradingRuleSet();
        List<String> list = null;
        for (AbstractTradingRule rule : ruleSets.getTradingRules()) {
            if (rule instanceof AbandonIncidentTradingRule)
                list = ((AbandonIncidentTradingRule) rule).getSuspendList();
        }

        BadmintonResultMarkets resultMarkets = new BadmintonResultMarkets();
        // System.out.println(tradingRules.getTradingRules().toString());
        return resultMarkets.resultMarkets(markets, (BadmintonMatchState) previousMatchState,
                        (BadmintonMatchState) currentMatchState, list);
    }

    @Override
    public void calculate() {
        if (super.getCalcRequestCause() == CalcRequestCause.MATCH_INCIDENT && super.getMatchIncidentResult() != null) {
            BadmintonMatchParams matchParams = (BadmintonMatchParams) super.getMatchParams();
            int pointA = ((BadmintonMatchState) matchState).getPointsA();
            int pointB = ((BadmintonMatchState) matchState).getPointsB();
            /*
             * every 10 points apply the momentum logic
             */
            if ((pointA + pointB) % 10 == 0) {
                System.out.println("Apply momentum logic by points" + (pointA - pointB));
                matchParams.updateParamsGivenMatchIncidentResult(pointA - pointB);
            }
        }
        super.calculate();
    }

    @Override
    public ResultedMarkets resultMarketsForAbandonedEvent(Markets markets, MatchState mostRecentMatchState) {
        ResultedMarkets resultedMarkets = new ResultedMarkets();
        List<String> nonVoidList = null;

        BadmintonTradingRules ruleSets = (BadmintonTradingRules) this.getTradingRuleSet();
        for (AbstractTradingRule rule : ruleSets.getTradingRules()) {
            if (rule instanceof AbandonIncidentTradingRule)
                nonVoidList = ((AbandonIncidentTradingRule) rule).getSuspendList();
        }
        boolean listExist = nonVoidList != null;
        // void all markets
        for (Market market : markets) {
            if (listExist) {
                if (nonVoidList.contains(market.getType()))
                    continue;
            }
            // System.out.println(tradingRules.getTradingRules()); // resulting
            // abandon by trading rule!!
            // ResultedMarket resultedMarket = voidMarket(market);
            ResultedMarket resultedMarket = voidMarket(market);
            resultedMarkets.addMarket(resultedMarket);
        }
        // result few selected markets, overwrite previous result
        if (resultWinnerMarkets((BadmintonMatchState) mostRecentMatchState)) {
            ResultedMarkets resultedMarkets2 = this.resultMarkets(markets, null, mostRecentMatchState);// need
                                                                                                       // teamId
                                                                                                       // for
                                                                                                       // the
                                                                                                       // abandoned
                                                                                                       // match
            for (ResultedMarket resultedMarket : resultedMarkets2.getResultedMarkets().values()) {
                resultedMarkets.addMarket(resultedMarket); // should overwrite
                                                           // the previou void
                                                           // effect
            }
        }
        return resultedMarkets;
    }

    private boolean resultWinnerMarkets(BadmintonMatchState matchStatelocal) {
        return ((matchStatelocal.getGamesA() + matchStatelocal.getGamesB() > 0)
                        && matchStatelocal.getPointsB() + matchStatelocal.getPointsA() > 0);//
    }

    /* void market logic spefic desgined for badmintion */
    @Override
    protected ResultedMarket voidMarket(Market market) {
        ResultedMarket resultedMarket = new ResultedMarket(market.getType(), market.getATSSubType(),
                        market.getCategory(), market.getSequenceId(), true, market.getMarketDescription(), "VOID", 0);
        return resultedMarket;
    }

    @Override
    public TradingRules getTradingRuleSet() {
        return new BadmintonTradingRules();
    }
}
