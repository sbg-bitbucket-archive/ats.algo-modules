package ats.algo.sport.football;


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
import ats.algo.core.markets.Markets;
import ats.algo.genericsupportfunctions.GCMath;
import ats.algo.genericsupportfunctions.Gaussian;
import ats.algo.genericsupportfunctions.Line;
import ats.algo.genericsupportfunctions.Vector2D;
import ats.core.util.log.Logger;
import ats.core.util.log.LoggerFactory;

public class FootballParamFindPreProcessor {

    private static final Logger logger = LoggerFactory.getLogger(FootballParamFindPreProcessor.class);

    public static Set<MarketGroup> process(FootballMatchEngine matchEngine, MatchParams matchParams,
                    MarketPricesList marketPricesList) {
        Set<MarketGroup> set = new HashSet<>(1);
        Map<String, FootballParamFindPreProcessorTargetPrice> targetPrices = getTargetPrices(marketPricesList);
        FootballMatchState matchState = (FootballMatchState) matchEngine.getMatchState();
        FootballMatchParams footballParams = (FootballMatchParams) matchParams;
        if (matchState.preMatch() && goalParamsAreSetToDefault(footballParams))
            setInitialGoalParams(footballParams, targetPrices);
        if (haveCornerMarkets(targetPrices)) {
            setCornerParams(matchEngine, footballParams, targetPrices, matchState.getCornersA(),
                            matchState.getCornersB());
        }
        set.add(MarketGroup.CORNERS);

        if (haveBookingMarkets(targetPrices)) {
            setBookingParams(matchEngine, footballParams, targetPrices, matchState.getYellowCardsA(),
                            matchState.getYellowCardsB(), matchState.getRedCardsA(), matchState.getRedCardsB());
        }
        set.add(MarketGroup.BOOKINGS);
        return set;
    }

    private static boolean goalParamsAreSetToDefault(FootballMatchParams footballParams) {
        if (footballParams.getGoalTotal().getMean() != FootballMatchParams.GOAL_TOTAL_DEFAULT_VALUE)
            return false;
        if (footballParams.getGoalSupremacy().getMean() != FootballMatchParams.GOAL_SUPREMACY_DEFAULT_VALUE)
            return false;
        return true;
    }

    /**
     * set some sensible values for starting the param find
     * 
     * @param footballParams
     * @param targetPrices
     */
    private static void setInitialGoalParams(FootballMatchParams footballParams,
                    Map<String, FootballParamFindPreProcessorTargetPrice> targetPrices) {
        /*
         * set the starting values for the goal related param find
         */
        FootballParamFindPreProcessorTargetPrice goalOuPrice = targetPrices.get("FT:OU");
        if (goalOuPrice != null) {
            logger.info("Setting starting value of goalTotal param to: %.2f", goalOuPrice.dLineId);
            footballParams.getGoalTotal().setMean(goalOuPrice.dLineId);
        }
        /*
         * set the supremacy param from AHCP if available else AXB
         */
        FootballParamFindPreProcessorTargetPrice goalAhcpPrice = targetPrices.get("FT:AHCP");

        if (goalAhcpPrice != null) {
            logger.info("Setting starting value of goalSupremacy param to: %.2f", -goalAhcpPrice.dLineId);
            footballParams.getGoalSupremacy().setMean(-goalAhcpPrice.dLineId);
        } else {
            FootballParamFindPreProcessorTargetPrice goalAxbPrice = targetPrices.get("FT:AXB");
            double newValue;
            if (goalAxbPrice != null) {
                if (goalAxbPrice.isFavourite)
                    newValue = Math.abs(FootballMatchParams.GOAL_SUPREMACY_DEFAULT_VALUE);
                else
                    newValue = -Math.abs(FootballMatchParams.GOAL_SUPREMACY_DEFAULT_VALUE);
                footballParams.getGoalSupremacy().setMean(newValue);
                logger.info("Setting starting value of goalSupremacy param to: %.2f", newValue);
            }
        }
    }

    private static Map<String, FootballParamFindPreProcessorTargetPrice> getTargetPrices(
                    MarketPricesList marketPricesList) {
        Map<String, FootballParamFindPreProcessorTargetPrice> targetPrices = new HashMap<>();
        /*
         * only going to use one source, so pick the source with a) the most prices and b) both corners markets, if any
         */
        MarketPrices marketPrices;
        MarketPrices maxCountMarketPrices = null;
        MarketPrices maxCountWithCornersMarketPrices = null;
        MarketPrices maxCountWithBookingsMarketPrices = null;
        MarketPrices maxCountWithCornersBookingsMarketPrices = null;
        int nMaxCountMarketPrices = 0;
        int nMaxCountWithCornersMarketPrices = 0;
        int nMaxCountWithBookingsMarketPrices = 0;
        int nMaxCountWithCornersBookingsMarketPrices = 0;
        for (MarketPrices m : marketPricesList.values()) {
            if (m.size() > nMaxCountMarketPrices) {
                nMaxCountMarketPrices = m.size();
                maxCountMarketPrices = m;
            }
            if (m.size() > nMaxCountWithCornersMarketPrices && hasCornersPrices(m)) {
                nMaxCountWithCornersMarketPrices = m.size();
                maxCountWithCornersMarketPrices = m;
            }
            if (m.size() > nMaxCountWithBookingsMarketPrices && hasBookingsPrices(m)) {
                nMaxCountWithBookingsMarketPrices = m.size();
                maxCountWithBookingsMarketPrices = m;
            }
            if (m.size() > nMaxCountWithCornersBookingsMarketPrices && hasCornersPrices(m) && hasBookingsPrices(m)) {
                nMaxCountWithCornersBookingsMarketPrices = m.size();
                maxCountWithCornersBookingsMarketPrices = m;
            }
        }
        if (maxCountWithCornersBookingsMarketPrices != null)
            marketPrices = maxCountWithCornersBookingsMarketPrices;
        else if (maxCountWithCornersMarketPrices != null)
            marketPrices = maxCountWithCornersMarketPrices;
        else if (maxCountWithBookingsMarketPrices != null)
            marketPrices = maxCountWithBookingsMarketPrices;
        else
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
                    case "FT:AXB":
                        isFavourite = selections.get("A") < selections.get("B");
                        p = -1000.0;
                        break;

                    case "FT:OU":
                    case "FT:COU":
                    case "FT:BCOU":
                        p = convertToProb(selections.get("Over"), selections.get("Under"));
                        isFavourite = p > 0.5;
                        break;
                    case "FT:AHCP":
                    case "FT:CHCP":
                    case "FT:BCSPRD":
                        p = convertToProb(selections.get("AH"), selections.get("BH"));
                        isFavourite = p > 0.5;
                        break;
                    default:
                        break;
                }
                if (p != -9999.0)
                    targetPrices.put(key, new FootballParamFindPreProcessorTargetPrice(mp.getLineId(), p, isFavourite));
            }
        } else {
            /*
             * no market prices for Bet365 so return null
             */
        }
        return targetPrices;
    }

    private static boolean hasCornersPrices(MarketPrices m) {
        return m.contains("FT:COU") && m.contains("FT:CHCP");
    }

    private static boolean hasBookingsPrices(MarketPrices m) {
        return m.contains("FT:BCOU") && m.contains("FT:BCSPRD");
    }

    private static boolean haveCornerMarkets(Map<String, FootballParamFindPreProcessorTargetPrice> targetPrices) {
        if (targetPrices.get("FT:COU") == null)
            return false;
        if (targetPrices.get("FT:CHCP") == null)
            return false;
        return true;
    }

    private static boolean haveBookingMarkets(Map<String, FootballParamFindPreProcessorTargetPrice> targetPrices) {
        if (targetPrices.get("FT:BCOU") == null)
            return false;
        if (targetPrices.get("FT:BCSPRD") == null)
            return false;
        return true;
    }

    private static void setCornerParams(FootballMatchEngine matchEngine, FootballMatchParams footballParams,
                    Map<String, FootballParamFindPreProcessorTargetPrice> targetPrices, int cornersA, int cornersB) {
        FootballParamFindPreProcessorTargetPrice cou = targetPrices.get("FT:COU");
        FootballParamFindPreProcessorTargetPrice csprd = targetPrices.get("FT:CHCP");
        Gaussian cornersTotal = footballParams.getCornerTotal();
        Gaussian cornersSupremacy = footballParams.getCornerSupremacy();
        double totalMean = cou.dLineId - (cornersA + cornersB);
        double supremacyMean = -csprd.dLineId - (cornersA - cornersB);
        Vector2D[] v1 = calculateCornerVectors(matchEngine, footballParams, cornersTotal, cornersSupremacy, totalMean,
                        supremacyMean, cou.lineId, csprd.lineId);
        /*
         * calculate the deltas
         */
        double deltaOu = GCMath.round(getDelta(cou.targetProb, v1[0].y, 0.1, 0.01), 4);
        double deltaSprd = GCMath.round(getDelta(csprd.targetProb, v1[1].y, 0.1, 0.01), 4);
        /*
         * calc the other two vectors
         */
        Vector2D[] v2 = calculateCornerVectors(matchEngine, footballParams, cornersTotal, cornersSupremacy,
                        totalMean + deltaOu, supremacyMean, cou.lineId, csprd.lineId);
        Vector2D[] v3 = calculateCornerVectors(matchEngine, footballParams, cornersTotal, cornersSupremacy, totalMean,
                        supremacyMean + deltaSprd, cou.lineId, csprd.lineId);
        /*
         * create the lines and calculate their intersection
         */
        double adjTotal;
        if (v1[0].x == v2[0].x) {
            adjTotal = v1[0].x;
        } else {
            Line lou = new Line(v1[0], v2[0]);
            adjTotal = lou.calcX(cou.targetProb);
        }
        double adjSupremacy;
        if (v1[1].x == v3[1].x) {
            adjSupremacy = v1[1].x;
        } else {
            Line lsprd = new Line(v1[1], v3[1]);
            adjSupremacy = lsprd.calcX(csprd.targetProb);
        }

        logger.info("Setting value of cornerTotal param to: %.2f", adjTotal);
        logger.info("Setting value of cornerSupremacy param to: %.2f", adjSupremacy);
        cornersTotal.setMean(adjTotal);
        cornersSupremacy.setMean(adjSupremacy);
    }

    private static void setBookingParams(FootballMatchEngine matchEngine, FootballMatchParams footballParams,
                    Map<String, FootballParamFindPreProcessorTargetPrice> targetPrices, int yellowCardsA,
                    int yellowCardsB, int redCardsA, int redCardsB) {
        FootballParamFindPreProcessorTargetPrice bou = targetPrices.get("FT:BCOU");
        FootballParamFindPreProcessorTargetPrice bsprd = targetPrices.get("FT:BCSPRD");
        Gaussian bookingsTotal = footballParams.getCardTotal();
        Gaussian bookingsSupremacy = footballParams.getCardSupremacy();
        double totalMean = bou.dLineId - (yellowCardsA + yellowCardsB) - (redCardsA + redCardsB);
        double supremacyMean = -bsprd.dLineId - (yellowCardsA - yellowCardsB) - (redCardsA - redCardsB);
        Vector2D[] v1 = calculateBookingVectors(matchEngine, footballParams, bookingsTotal, bookingsSupremacy,
                        totalMean, supremacyMean, bou.lineId, bsprd.lineId);
        /*
         * calculate the deltas
         */
        double deltaOu = GCMath.round(getDelta(bou.targetProb, v1[0].y, 0.1, 0.01), 4);
        double deltaSprd = GCMath.round(getDelta(bsprd.targetProb, v1[1].y, 0.1, 0.01), 4);
        /*
         * calc the other two vectors
         */
        Vector2D[] v2 = calculateBookingVectors(matchEngine, footballParams, bookingsTotal, bookingsSupremacy,
                        totalMean + deltaOu, supremacyMean, bou.lineId, bsprd.lineId);
        Vector2D[] v3 = calculateBookingVectors(matchEngine, footballParams, bookingsTotal, bookingsSupremacy,
                        totalMean, supremacyMean + deltaSprd, bou.lineId, bsprd.lineId);
        double adjTotal;
        if (v1[0].x == v2[0].x) {
            adjTotal = v1[0].x;
        } else {
            Line lou = new Line(v1[0], v2[0]);
            adjTotal = lou.calcX(bou.targetProb);
        }
        double adjSupremacy;
        if (v1[1].x == v3[1].x) {
            adjSupremacy = v1[1].x;
        } else {
            Line lsprd = new Line(v1[1], v3[1]);
            adjSupremacy = lsprd.calcX(bsprd.targetProb);
        }

        logger.info("Setting value of cardTotal param to: %.2f", adjTotal);
        logger.info("Setting value of cardSupremacy param to: %.2f", adjSupremacy);
        bookingsTotal.setMean(adjTotal);
        bookingsSupremacy.setMean(adjSupremacy);
    }

    private static Vector2D[] calculateCornerVectors(FootballMatchEngine matchEngine,
                    FootballMatchParams footballParams, Gaussian totalParam, Gaussian supremacyParam, double totalMean,
                    double supremacyMean, String ouLineId, String sprdLineId) {
        Vector2D[] vectors = new Vector2D[2];
        totalParam.setMean(totalMean);
        supremacyParam.setMean(supremacyMean);
        matchEngine.setMatchParams(footballParams);
        matchEngine.calculate();
        Markets markets = matchEngine.getCalculatedMarkets();
        double ourProbOu = GCMath
                        .round(markets.getMarketForLineId("FT:COU", "M", ouLineId).getSelectionsProbs().get("Over"), 4);
        double ourProbSprd = GCMath.round(
                        markets.getMarketForLineId("FT:CHCP", "M", sprdLineId).getSelectionsProbs().get("AH"), 4);
        vectors[0] = new Vector2D(totalMean, ourProbOu);
        vectors[1] = new Vector2D(supremacyMean, ourProbSprd);
        return vectors;
    }

    private static Vector2D[] calculateBookingVectors(FootballMatchEngine matchEngine,
                    FootballMatchParams footballParams, Gaussian totalParam, Gaussian supremacyParam, double totalMean,
                    double supremacyMean, String ouLineId, String sprdLineId) {
        Vector2D[] vectors = new Vector2D[2];
        totalParam.setMean(totalMean);
        supremacyParam.setMean(supremacyMean);
        matchEngine.setMatchParams(footballParams);
        matchEngine.calculate();
        Markets markets = matchEngine.getCalculatedMarkets();
        double ourProbOu = GCMath.round(
                        markets.getMarketForLineId("FT:BCOU", "M", ouLineId).getSelectionsProbs().get("Over"), 4);
        double ourProbSprd = GCMath.round(
                        markets.getMarketForLineId("FT:BCSPRD", "M", sprdLineId).getSelectionsProbs().get("AH"), 4);
        vectors[0] = new Vector2D(totalMean, ourProbOu);
        vectors[1] = new Vector2D(supremacyMean, ourProbSprd);
        return vectors;
    }

    /**
     * gets the paramDelta based on 1 paramDiff for each probDiff between targetProb and ourProb and subject to a
     * minimum of paramDiff
     * 
     * @param targetProb
     * @param ourProb
     * @param probDiff
     * @param paramDiff
     * @return
     */
    private static double getDelta(double targetProb, double ourProb, double probDiff, double paramDiff) {
        double delta = (targetProb - ourProb) * probDiff / paramDiff;
        if (delta > 0 && delta < paramDiff)
            delta = paramDiff;
        if (delta < 0 && delta > -paramDiff)
            delta = -paramDiff;
        return delta;
    }

    static void removeCornersPricesFromList(MarketPricesList marketPricesList) {
        Set<String> keysToRemove = new HashSet<>();
        for (MarketPrices marketPrices : marketPricesList.getMarketPricesList().values()) {
            keysToRemove.clear();
            Map<String, MarketPrice> marketPricesMap = marketPrices.getMarketPrices();
            for (Entry<String, MarketPrice> e2 : marketPricesMap.entrySet()) {
                String type = e2.getValue().getType();
                if (type.equals("FT:COU") || type.equals("FT:CHCP"))
                    keysToRemove.add(e2.getKey());
            }
            for (String key : keysToRemove) {
                logger.info("Removing corner market %s from prices to be used for param find", key);
                marketPricesMap.remove(key);
            }
        }

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
