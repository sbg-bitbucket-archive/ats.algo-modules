package ats.algo.core.comparetomarket;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import ats.algo.core.MarketGroup;
import ats.algo.core.comparetomarket.CompareToMarketMetrics.Stats;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketCategory;
import ats.algo.core.markets.Markets;
import ats.algo.margining.Margining;
import ats.algo.margining.Margining.MarginingAlgo;
import ats.core.AtsBean;
import ats.core.util.log.LoggerFactory;

public class CompareToMarket extends AtsBean {

    private static double paramFindKickoffThreshold = 0.01;
    private static double amberAlertThreshold = 0.035;
    private static double redAlertThreshold = 0.075;
    private static double smartParamFindKickoffThreshold = 0.025; // for use by new rules


    private static MarginingAlgo marginingAlgo = Margining.MARGINING_ALGO_V4A;
    private static boolean applyProbWeights = false;
    private static boolean comparePricesNotProbs = true;


    public static void setMarginingAlgo(MarginingAlgo marginingAlgo) {
        CompareToMarket.marginingAlgo = marginingAlgo;
    }

    public static void setApplyProbWeights(boolean applyEntropyWeights) {
        CompareToMarket.applyProbWeights = applyEntropyWeights;
    }

    public static void setComparePricesNotProbs(boolean comparePricesNotProbs) {
        CompareToMarket.comparePricesNotProbs = comparePricesNotProbs;
    }

    public static double getParamFindKickoffThreshold() {
        return paramFindKickoffThreshold;
    }

    public static double getSmartParamFindKickoffThreshold() {
        return smartParamFindKickoffThreshold;
    }



    public static void setSmartParamFindKickoffThreshold(double smartParamFindKickoffThreshold) {
        CompareToMarket.smartParamFindKickoffThreshold = smartParamFindKickoffThreshold;
    }

    public static void setParamFindKickoffThreshold(double paramFindKickoffThreshold) {
        CompareToMarket.paramFindKickoffThreshold = paramFindKickoffThreshold;
        LoggerFactory.getLogger(CompareToMarket.class).info("paramfindKickoffThreshold changed to: %.3f",
                        paramFindKickoffThreshold);

    }

    public static double getAmberAlertThreshold() {
        return amberAlertThreshold;
    }

    public static void setAmberAlertThreshold(double amberAlertThreshold) {
        CompareToMarket.amberAlertThreshold = amberAlertThreshold;
        LoggerFactory.getLogger(CompareToMarket.class).info("amberAlertThreshold changed to: %.3f",
                        amberAlertThreshold);
    }

    public static double getRedAlertThreshold() {
        return redAlertThreshold;
    }

    public static void setRedAlertThreshold(double redAlertThreshold) {
        CompareToMarket.redAlertThreshold = redAlertThreshold;
        LoggerFactory.getLogger(CompareToMarket.class).info("redAlertThreshold changed to: %.3f", redAlertThreshold);
    }

    /**
     * calculates the cost metric for the difference between the probs we generate for our market selections and the
     * competitor probs
     *
     * @param markets the set of markets we have generated
     * @param matchedMarkets the inner map is a set of MarketProbs from each source. The outer map has one entry per
     *        Market
     * @return the calculated cost metrics
     */
    public static CompareToMarketMetrics calculateCostMetrics(Markets markets,
                    Map<String, Map<String, MarketProbs>> matchedMarkets) {

        CompareToMarketMetrics costMetrics = new CompareToMarketMetrics(matchedMarkets);
        Map<MarketGroup, GroupMetrics> groupMetricsMap = costMetrics.getGroupMetricsMap();
        double cumCostForMarkets = 0.0;
        double cumMinPossibleCostForMarkets = 0.0;
        double cumWeightForMarkets = 0.0;

        for (Entry<String, Map<String, MarketProbs>> marketEntry : matchedMarkets.entrySet()) {
            /*
             * iterate over the set of markets for which marketProbs are available
             */
            double cumCostForMarket = 0.0;
            double cumMinCostForMarket = 0.0;
            double cumWeightForMarket = 0.0;
            String marketKey = marketEntry.getKey();
            MarketGroup marketGroup = null;
            double marketWeight = 0;
            for (Entry<String, MarketProbs> marketProbsEntry : marketEntry.getValue().entrySet()) {

                /*
                 * iterate over the available sources for the specified market
                 */
                MarketProbs marketProbs = marketProbsEntry.getValue();
                String sourceKey = marketProbsEntry.getKey();
                double cumCostForMarketForSource = 0.0;
                double cumMinCostForMarketForSource = 0.0;
                double cumWeightForMarketForSource = 0.0;
                marketWeight = marketProbs.getMarketWeight(); // should be the
                                                              // same across
                                                              // all sources

                double sourceWeight = marketProbs.getSourceWeight();
                Market market = markets.get(marketProbs.getMarketType(), marketProbs.getMarketSequenceId());
                marketGroup = market.getMarketGroup();
                if (market.getCategory() != marketProbs.getMarketCategory())
                    throw new IllegalArgumentException("market types don't match " + market.getCategory() + " vs "
                                    + marketProbs.getMarketCategory());
                if ((marketProbs.getMarketCategory() != MarketCategory.GENERAL)
                                && !(market.getLineId().equals(marketProbs.getLineId()))) {
                    String ahcpLineId = marketProbs.getLineId();
                    /*
                     * Updated the AHCP line to match sprd market
                     */
                    if (market.getFullKey().contains("AHCP")) {

                        String sequenceId = market.getSequenceId();
                        String score;
                        if (sequenceId.contains("H"))
                            score = sequenceId.substring(2, sequenceId.length());
                        else
                            score = sequenceId.substring(1, sequenceId.length());
                        String[] index = score.split("-");
                        int ahcp = Integer.parseInt(index[0]) - Integer.parseInt(index[1]);
                        Double ahcpD = Double.parseDouble(ahcpLineId);
                        ahcpD -= ahcp;
                        ahcpLineId = String.valueOf(ahcpD);
                    }
                    /*
                     * the line nos are different for an OVUN or HCAP mkt so get the right line
                     */
                    market = market.getMarketForLineId(ahcpLineId);
                }
                for (Entry<String, Double> selection : marketProbs.getProbs().entrySet()) {
                    /*
                     * iterate over the selections from a particular source for the specified market
                     */
                    String selectionKey = selection.getKey();
                    Double mktProb = selection.getValue();
                    Double ourProb = market.get(selectionKey);

                    CompareToMarketMetricsItemInfo info = costMetrics.getMarketsCostDescriptionInfo().get(marketKey)
                                    .get(sourceKey).get(selectionKey);
                    info.ourProb = ourProb;
                    String selectionName = selectionKey;
                    if ((market.getCategory() != MarketCategory.GENERAL)) {
                        switch (selectionKey) {
                            case "A":
                            case "Over":
                                selectionName = market.getSelectionNameOverOrA();
                                break;
                            case "B":
                            case "Under":
                                selectionName = market.getSelectionNameUnderOrB();
                                break;
                            case "Draw":
                                selectionName = market.getSelectionNameDrawOrD();
                                break;
                            case "AH":
                                selectionName = market.getSelectionNameOverOrA();
                                break;
                            case "BH":
                                selectionName = market.getSelectionNameUnderOrB();
                                break;
                            default:
                                /*
                                 * do nothing
                                 *
                                 */
                                break;
                        }
                    }
                    info.selectionName = selectionName;
                    Stats stats = costMetrics.getMarketsProbsStats().get(marketKey).get(selectionKey);
                    Double meanProb = stats.getMean();
                    double probWeight;
                    if (CompareToMarket.applyProbWeights) {
                        if (mktProb > 0)
                            probWeight = 100.0 / (mktProb * mktProb);
                        else
                            probWeight = 0;
                        // if (mktProb > 0 && mktProb < 1) // set to zero if = 0 or 1
                        // entropyWeight = -(mktProb * Math.log(mktProb) + (1 - mktProb) * (Math.log(1 - mktProb)))
                        // / nSelections;
                        // else
                        // entropyWeight = 0.0;
                    } else {
                        probWeight = 1.0;
                    }
                    double probDiff;
                    double meanProbDiff;
                    if (CompareToMarket.comparePricesNotProbs) {
                        // probDiff = (1 / mktProb - 1 / ourProb);
                        // meanProbDiff = (1 / mktProb - 1 / meanProb);
                        probDiff = 0.5 * (mktProb - ourProb) + 0.2 * (mktProb - ourProb) / (mktProb + ourProb);
                        meanProbDiff = 0.5 * (mktProb - meanProb) + 0.2 * (mktProb - meanProb) / (mktProb + meanProb);

                    } else {
                        probDiff = mktProb - ourProb;
                        meanProbDiff = mktProb - meanProb;
                    }

                    double diffSq = probDiff * probDiff;
                    double minPossibleDiffSq = meanProbDiff * meanProbDiff;
                    double cost = probWeight * diffSq;
                    double minPossibleCost = probWeight * minPossibleDiffSq;
                    cumCostForMarketForSource += cost;
                    cumMinCostForMarketForSource += minPossibleCost;
                    cumWeightForMarketForSource += probWeight;
                    costMetrics.getMarketsCostDetails().get(marketKey).get(sourceKey).put(selectionKey, cost);
                    costMetrics.getMinPossibleCostDetails().get(marketKey).get(sourceKey).put(selectionKey,
                                    minPossibleCost);
                }

                cumCostForMarketForSource *= sourceWeight;
                cumMinCostForMarketForSource *= sourceWeight;
                cumWeightForMarketForSource *= sourceWeight;
                cumCostForMarket += cumCostForMarketForSource;
                cumMinCostForMarket += cumMinCostForMarketForSource;
                cumWeightForMarket += cumWeightForMarketForSource;


            }
            double costforMarket = cumCostForMarket / cumWeightForMarket;
            double minPossibleCostforMarket = cumMinCostForMarket / cumWeightForMarket;
            cumCostForMarkets += costforMarket * marketWeight;
            cumMinPossibleCostForMarkets += minPossibleCostforMarket * marketWeight;
            cumWeightForMarkets += marketWeight;
            GroupMetrics groupMetrics = groupMetricsMap.get(marketGroup);
            groupMetrics.cumCost += costforMarket * marketWeight;
            groupMetrics.cumMinPossibleCost += minPossibleCostforMarket * marketWeight;
            groupMetrics.cumWeight += marketWeight;
        }
        if (cumWeightForMarkets == 0)
            throw new IllegalArgumentException(" Weights for at least one set of prices must be non zero");
        cumCostForMarkets /= cumWeightForMarkets;
        cumMinPossibleCostForMarkets /= cumWeightForMarkets;
        costMetrics.setMarketsCost(Math.sqrt(cumCostForMarkets));
        costMetrics.setMinPossibleCost(Math.sqrt(cumMinPossibleCostForMarkets));
        return costMetrics;

    }

    /**
     * Converts prices to probs, using demargining algo
     *
     * @param market
     * @param marketPrice
     * @param weight
     * @param d
     * @param d
     * @return
     */
    private static MarketProbs getMarketProbs(Market market, MarketPrice marketPrice, double sourceWeight,
                    double marketWeight) {
        MarketProbs p = new MarketProbs();
        p.setMarketCategory(market.getCategory());
        p.setMarketType(market.getType());
        p.setMarketGroup(market.getMarketGroup());
        p.setMarketSequenceId(marketPrice.getSequenceId());
        p.setLineId(marketPrice.getLineId());
        p.setSourceWeight(sourceWeight);
        p.setMarketWeight(marketWeight);
        double[] prices = new double[marketPrice.size()];
        String[] key = new String[marketPrice.size()];
        int i = 0;
        for (Entry<String, Double> entry : marketPrice.entrySet()) {
            prices[i] = entry.getValue();
            if (prices[i] == 0.0)
                throw new IllegalArgumentException("Price with value zero supplied as input");
            key[i] = entry.getKey();
            i++;
        }
        Margining margining = new Margining();
        margining.setMarginingAlgo(marginingAlgo);
        double[] probs = margining.removeMargin(prices, 1.0);
        if (probs != null) {
            for (int j = 0; j < probs.length; j++) {
                p.getProbs().put(key[j], probs[j]);
            }
        } else {
            String pricesStr = "{";
            for (int k = 0; k < prices.length; k++) {
                pricesStr += prices[k];
                if (k < prices.length - 1)
                    pricesStr += ", ";
            }
            pricesStr += "}";
            String errMsg = String.format("Demargining error for prices = %s.  Can't continue with param find.",
                            pricesStr);
            throw new IllegalArgumentException(errMsg);
        }
        return p;
    }

    /**
     * Gets the set of matching probabilities between the Markets we generate and the set of competitor MarketPrices
     *
     * @param markets The list of our markets (i.e. the ones that our model has calculated)
     * @param marketPricesList The list of competitor market prices, one entry for each source
     * @param marketPricesStatus Updated on exit to contains any error messages that are generated during the comparison
     *        process
     * @return inner map has one MarketProbs object per Source. Outer map has one entry per Market
     */
    public static Map<String, Map<String, MarketProbs>> getMatchedMarkets(Markets markets,
                    MarketPricesList marketPricesList, MarketPricesStatus marketPricesStatus) {
        Map<String, MarketPrices> list = marketPricesList.getMarketPricesList();
        int nMarkets = 0;
        boolean markedIsNotValid = false;
        Map<String, Map<String, MarketProbs>> matchedMarkets = new TreeMap<String, Map<String, MarketProbs>>();
        for (Entry<String, MarketPrices> entry : list.entrySet()) {
            /*
             * Iterate over each source of market prices
             */
            String source = entry.getKey();
            MarketPrices marketPrices = entry.getValue();
            for (MarketPrice marketPrice : marketPrices) {
                /*
                 * iterate over each market price from a single source
                 */
                if (marketPrice.isValid()) {
                    String marketType = marketPrice.getType();
                    String sequenceId = marketPrice.getSequenceId();
                    String lineId = marketPrice.getLineId();
                    // if (marketType.contains("FT:AHCP")) {
                    // LoggerFactory.getLogger(CompareToMarket.class).debug("Market = " + marketType + ", SeqId = " +
                    // sequenceId+ ", LineID = " + marketPrice.getLineId());
                    // for (Market market : markets) {
                    // if (market.getType().contains("FT:AHCP"))
                    // LoggerFactory.getLogger(CompareToMarket.class).debug("Market = " + market + ", SeqId = " +
                    // sequenceId+ ", LineID = " + market.getLineId());
                    // }
                    // }
                    Market market = markets.get(marketType, sequenceId);
                    if (market != null && lineId != null) {
                        if (!lineId.equals("")) {
                            market = market.getMarketForLineId(lineId);
                        }
                    }

                    boolean marketPriceOk;
                    if (market == null) {
                        String s = String.format(
                                        "MarketPrice %s_%s from source %s not matched with markets we generate. Ignored when calculating best fit",
                                        marketType, sequenceId, source);
                        marketPricesStatus.addWarningMsg(s);
                    } else {
                        /*
                         * check that no of selections are same in both market and marketprice
                         */
                        if (market.isValid() == false) {
                            markedIsNotValid = true;
                            // marketPricesStatus.addWarningMsg(
                            // String.format("market %s_%s from source %s is marked is not valid. Ignored when
                            // calculating best fit",
                            // marketType, sequenceId, source));
                            marketPriceOk = false;
                        } else if (market.size() != marketPrice.size()) {
                            marketPricesStatus.addWarningMsg(String.format(
                                            "No of selections don't match for market %s_%s from source %s.  Ignored when calculating best fit",
                                            marketType, sequenceId, source));
                            marketPriceOk = false;
                        } else {
                            /*
                             * check that all selection names match
                             */
                            marketPriceOk = true;
                            for (String selectionName : market.getSelections().keySet()) {
                                marketPriceOk = marketPriceOk && (marketPrice.get(selectionName) != null);
                            }
                            if (!marketPriceOk) {
                                marketPricesStatus.addWarningMsg(String.format(
                                                "Selection keys don't match for market %s_%s from source %s.  Ignored when calculating best fit",
                                                marketType, sequenceId, source));
                            }
                        }
                        if (marketPriceOk) {
                            /*
                             * found at least one matching market so demargin the prices.
                             *
                             */
                            nMarkets++;
                            String marketKey = String.format("%s_%s", marketType, sequenceId);
                            if (market.getLineId() != "" && market.getLineId() != null) {
                                marketKey = String.format("%s#%s_%s", marketType, lineId, sequenceId);
                            }
                            MarketProbs marketProbs = getMarketProbs(market, marketPrice,
                                            marketPrices.getSourceWeight(), marketPrice.getMarketWeight());
                            Map<String, MarketProbs> matchedPricesForMarket = matchedMarkets.get(marketKey);
                            if (matchedPricesForMarket == null) {
                                matchedPricesForMarket = new TreeMap<String, MarketProbs>();
                                matchedMarkets.put(marketKey, matchedPricesForMarket);
                            }
                            matchedPricesForMarket.put(source, marketProbs);

                        }
                    }
                } else {
                    /*
                     * marketPrice is not valid
                     */
                    marketPricesStatus.addWarningMsg(String.format(
                                    "marketPrice %s_%s from source %s for lineId %s is marked is not valid.  Ignored when calculating best fit",
                                    marketPrice.getType(), marketPrice.getSequenceId(), source,
                                    marketPrice.getLineId()));
                }
            }
        }

        if (markedIsNotValid) {
            marketPricesStatus.addWarningMsg(String
                            .format("market from source is marked is not valid.  Ignored when calculating best fit"));
        }

        if (matchedMarkets.size() == 0) {
            marketPricesStatus.addWarningMsg("Error.  No matching prices found for any market");
            marketPricesStatus.setPricesOk(false);
        } else
            marketPricesStatus.setPricesOk(true);
        marketPricesStatus.setnSources(list.size());
        marketPricesStatus.setnMarkets(nMarkets);
        return matchedMarkets;
    }

    /**
     * calculates the cost metrics for the supplied marketPrices, generates warnings or errors if any quality issues and
     * determines whether a param find should be scheduled
     *
     * @param markets
     * @param marketPricesList
     *
     * @return MarketPricesStatus Container for all the info generated
     */
    public static MarketPricesStatus getMarketPricesStatus(Markets markets, MarketPricesList marketPricesList) {
        MarketPricesStatus marketPricesStatus = new MarketPricesStatus();
        Map<String, Map<String, MarketProbs>> matchedMarkets =
                        getMatchedMarkets(markets, marketPricesList, marketPricesStatus);

        // System.out.println (toStringMatchedMarkets(matchedMarkets));
        if (marketPricesStatus.isPricesOk()) {


            CompareToMarketMetrics costMetrics = calculateCostMetrics(markets, matchedMarkets);
            // logger.debug(costMetrics);
            reviewMetricsForErrors(costMetrics, matchedMarkets, marketPricesStatus);
            double marketCost = costMetrics.getMarketsCost();
            double minPossibleCost = costMetrics.getMinPossibleCost();
            marketPricesStatus.setActualCostForOurMarkets(marketCost);
            marketPricesStatus.setMinCostPossibleWithTheseMarketsPrices(minPossibleCost);
            marketPricesStatus.setGroupMetricsMap(costMetrics.getGroupMetricsMap());
            marketPricesStatus.setParamFindRequired(marketCost - minPossibleCost > paramFindKickoffThreshold);
            marketPricesStatus.setCompareToMarketMetrics(costMetrics);
        }
        return marketPricesStatus;
    }

    public static String toStringMatchedMarkets(Map<String, Map<String, MarketProbs>> matchedMarkets) {
        String s = "";
        for (Entry<String, Map<String, MarketProbs>> e : matchedMarkets.entrySet()) {
            s += String.format("\nMatched markets for market: %s\n", e.getKey());
            for (Entry<String, MarketProbs> e2 : e.getValue().entrySet()) {
                s += String.format("MarketProbs for source: %s\n", e2.getKey());
                MarketProbs marketProbs = e2.getValue();
                s += marketProbs.toString();
            }
        }
        return s;
    }


    /**
     * Compares the supplied marketPricesList and markets and calculates the distance metric
     * 
     * @param marketPricesList
     * @param markets
     * @return true if param find should be scheduled
     */
    public static double getPricesDistance(MarketPricesList marketPricesList, Markets markets) {
        MarketPricesStatus marketPricesStatus = new MarketPricesStatus();
        Map<String, Map<String, MarketProbs>> matchedMarkets =
                        getMatchedMarkets(markets, marketPricesList, marketPricesStatus);
        if (!marketPricesStatus.isPricesOk()) {
            /*
             * can't schedule a param find if there are no markets which match
             */
            return 0;
        }
        CompareToMarketMetrics metrics = calculateCostMetrics(markets, matchedMarkets);
        return metrics.getMarketsCost();
    }



    /**
     * looks through the generated costMetrics and highlights any anomalies
     *
     * @param costMetrics
     *
     * @param matchedMarkets
     * @param marketPricesStatus - object updated with error messages
     */
    private static void reviewMetricsForErrors(CompareToMarketMetrics costMetrics,
                    Map<String, Map<String, MarketProbs>> matchedMarkets, MarketPricesStatus marketPricesStatus) {
        marketPricesStatus.setPricesOk(costMetrics.getMinPossibleCost() < amberAlertThreshold);
        for (Entry<String, Map<String, Stats>> entry : costMetrics.getMarketsProbsStats().entrySet()) {
            /*
             * iterate over markets
             */
            String marketKey = entry.getKey();
            for (Entry<String, Stats> entry1 : entry.getValue().entrySet()) {
                /*
                 * iterate over selections on market
                 */
                String selectionKey = entry1.getKey();
                Stats stats = entry1.getValue();
                double mean = stats.getMean();

                /*
                 * examine the individual source probs for this selection to find any that look wrong
                 */
                Map<String, MarketProbs> x = matchedMarkets.get(marketKey);
                for (Entry<String, MarketProbs> entry3 : x.entrySet()) {
                    String sourceKey = entry3.getKey();
                    MarketProbs marketProbs = entry3.getValue();
                    double prob = marketProbs.getProbForSelection(selectionKey);
                    if (Math.abs(prob - mean) > amberAlertThreshold) {
                        String s2 = String.format(
                                        "  %s market %s, selection %s is suspect.  Prob for this source is %.2f vs weighted mean for all sources of %.2f",
                                        sourceKey, marketKey, selectionKey, prob, mean);
                        marketPricesStatus.addWarningMsg(s2);
                    }
                }
            }
        }
    }

    public static String matchedMarketsToString(Map<String, Map<String, MarketProbs>> matchedMarkets) {
        String s = "MatchedMarkets: \n";
        for (Entry<String, Map<String, MarketProbs>> marketEntry : matchedMarkets.entrySet()) {
            s += "  Market: " + marketEntry.getKey() + "\n";
            /*
             * iterate over the set of markets for which marketProbs are available
             */
            for (Entry<String, MarketProbs> marketProbsEntry : marketEntry.getValue().entrySet()) {
                s += "    Source: " + marketProbsEntry.getKey() + "\n";
                /*
                 * iterate over the available sources of marketPrices for the specified market
                 */
                MarketProbs marketProbs = marketProbsEntry.getValue();
                for (Entry<String, Double> selection : marketProbs.getProbs().entrySet()) {
                    /*
                     * iterate over the selections from a particular source for the specified market
                     */
                    s += String.format("      %s: %.3f\n", selection.getKey(), selection.getValue());
                }
            }
        }
        s += "\n";
        return s;
    }
}
