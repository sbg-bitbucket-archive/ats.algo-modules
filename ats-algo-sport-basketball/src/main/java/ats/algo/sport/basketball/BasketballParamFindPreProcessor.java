package ats.algo.sport.basketball;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.comparetomarket.MarketPrice;
import ats.algo.core.comparetomarket.MarketPrices;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.genericsupportfunctions.GCMath;
import ats.core.util.log.Logger;
import ats.core.util.log.LoggerFactory;

public class BasketballParamFindPreProcessor {

    private static final Logger logger = LoggerFactory.getLogger(BasketballParamFindPreProcessor.class);

    public static Set<MarketGroup> process(BasketballMatchEngine matchEngine, MatchParams matchParams,
                    MarketPricesList marketPricesList) {
        Set<MarketGroup> set = new HashSet<>(1);
        Map<String, BasketballParamFindPreProcessorTargetPrice> targetPrices = getTargetPrices(marketPricesList);
        BasketballMatchState matchState = (BasketballMatchState) matchEngine.getMatchState();
        BasketballMatchParams basketballParams = (BasketballMatchParams) matchParams;
        if (matchState.preMatch() && paramsAreSetToDefault(basketballParams))
            setInitialGoalParams(basketballParams, targetPrices);
        return set;
    }

    private static boolean paramsAreSetToDefault(BasketballMatchParams basketballParams) {
        if (basketballParams.getPace().getMean() != BasketballMatchParams.PACE_DEFAULT_VALUE)
            return false;
        if (basketballParams.getAdv().getMean() != BasketballMatchParams.ADV_DEFAULT_VALUE)
            return false;
        return true;
    }

    /**
     * set some sensible values for starting the param find
     * 
     * @param basketballParams
     * @param targetPrices
     */
    private static void setInitialGoalParams(BasketballMatchParams basketballParams,
                    Map<String, BasketballParamFindPreProcessorTargetPrice> targetPrices) {
        /*
         * set the starting values for the pace related param find
         */
        BasketballParamFindPreProcessorTargetPrice goalOuPrice = targetPrices.get("FTOT:OU");
        if (goalOuPrice != null) {
            logger.info("Setting starting value of pace param to: %.2f", goalOuPrice.dLineId);
            basketballParams.getPace().setMean(goalOuPrice.dLineId);
        }
        /*
         * set the supremacy param from SPRD if available else FTOT:ML
         */
        BasketballParamFindPreProcessorTargetPrice goalAhcpPrice = targetPrices.get("FT:SPRD");

        if (goalAhcpPrice != null) {
            logger.info("Setting starting value of adv param to: %.2f", -goalAhcpPrice.dLineId);
            basketballParams.getAdv().setMean(-goalAhcpPrice.dLineId);
        } else {
            BasketballParamFindPreProcessorTargetPrice goalAxbPrice = targetPrices.get("FTOT:ML");
            double newValue;
            if (goalAxbPrice != null) {
                if (goalAxbPrice.isFavourite)
                    newValue = Math.abs(BasketballMatchParams.ADV_DEFAULT_VALUE);
                else
                    newValue = -Math.abs(BasketballMatchParams.ADV_DEFAULT_VALUE);
                basketballParams.getAdv().setMean(newValue);
                logger.info("Setting starting value of adv param to: %.2f", newValue);
            }
        }
    }

    private static Map<String, BasketballParamFindPreProcessorTargetPrice> getTargetPrices(
                    MarketPricesList marketPricesList) {
        Map<String, BasketballParamFindPreProcessorTargetPrice> targetPrices = new HashMap<>();
        /*
         * only going to use one source, so pick the source with a) the most prices and b) both corners markets, if any
         */
        MarketPrices marketPrices;
        MarketPrices maxCountMarketPrices = null;
        int nMaxCountMarketPrices = 0;
        for (MarketPrices m : marketPricesList.values()) {
            if (m.size() > nMaxCountMarketPrices) {
                nMaxCountMarketPrices = m.size();
                maxCountMarketPrices = m;
            }
        }
        marketPrices = maxCountMarketPrices;
        /*
         * chosen the set of marketPrices to use
         */
        if (marketPrices != null) {
            for (Entry<String, MarketPrice> e : marketPrices.getMarketPrices().entrySet()) {
                MarketPrice mp = e.getValue();
                String key = mp.getType();
                Map<String, Double> selections = mp.getSelections();
                double p = -9999.0;
                boolean isFavourite = false;
                switch (key) {
                    case "FTOT:ML":
                        isFavourite = selections.get("A") < selections.get("B");
                        p = -1000.0;
                        break;

                    case "FTOT:OU":
                        p = convertToProb(selections.get("Over"), selections.get("Under"));
                        isFavourite = p > 0.5;
                        break;
                    case "FT:SPRD":
                        p = convertToProb(selections.get("AH"), selections.get("BH"));
                        isFavourite = p > 0.5;
                        break;
                    default:
                        break;
                }
                if (p != -9999.0)
                    targetPrices.put(key,
                                    new BasketballParamFindPreProcessorTargetPrice(mp.getLineId(), p, isFavourite));
            }
        } else {
            /*
             * no market prices for Bet365 so return null
             */
        }
        return targetPrices;
    }

    /**
     * Can do a simple conversio since if they are going to be used probs should be fairly close to 0.5
     * 
     * @param p1
     * @param p2
     * @return
     */
    private static double convertToProb(Double p1, Double p2) {
        double q1 = 1 / p1;
        double q2 = 1 / p2;
        double margin = (q1 + q2 - 1) / 2.0;
        return GCMath.round(q1 - margin, 4);
    }

}
