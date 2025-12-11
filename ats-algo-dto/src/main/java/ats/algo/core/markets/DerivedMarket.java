package ats.algo.core.markets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import ats.algo.core.markets.DerivedMarketDetails.DerivedMarketResulter;
import ats.algo.core.markets.DerivedMarketSpec.DerivedMarketGapBetweenLines;

/**
 * static methods to support generation and resulting of derived markets
 * 
 * @author Geoff
 *
 */

public class DerivedMarket {

    /**
     * prevent the class being inadvertently instantiated by declaring a private constructor
     */
    private DerivedMarket() {}

    static List<Market> generateAllDerivedMarkets(Market market,
                    DataToGenerateStaticMarkets dataToGenerateStaticMarkets) {
        ArrayList<Market> mktList = new ArrayList<Market>();
        for (DerivedMarketSpec derivedMarketSpec : market.getDerivedMarketsInfo().getDerivedMarketSpecs()) {
            switch (derivedMarketSpec.getDerivedMarketType()) {
                case RANGE_HCAP:
                    mktList.add(getDerivedDyamicRangeHcapMarket(market, derivedMarketSpec));
                    break;
                case RANGE_TOTAL:
                    mktList.add(getDerivedDyamicRangeTotalMarket(market, derivedMarketSpec));
                    break;
                case LINES_NEAREST_EVENS:
                    mktList.addAll(generateNearestEvensMarkets(market, derivedMarketSpec));
                    break;
                case LINES_REGULAR:
                    Collection<Market> newMkts = generateAllRequiredLinesRegular(market, derivedMarketSpec,
                                    market.getBalancedLineIndex());
                    mktList.addAll(newMkts);
                    break;
                case LINES_STATIC:
                    String s = dataToGenerateStaticMarkets.getMap().get(market.getType());
                    if (s != null) {

                        int staticLineIndex = Integer.valueOf(s);
                        mktList.addAll(generateAllRequiredLinesRegular(market, derivedMarketSpec, staticLineIndex));
                    } else {
                        /*
                         * if null then the staticLineId has not yet been captured - do nothing
                         */
                    }
                    break;
                case LINES_EUROPEAN:
                    mktList.addAll(generateAllRequiredLinesEuropean(market, derivedMarketSpec,
                                    market.getBalancedLineIndex()));
                    break;
                case LINES_ASIAN:
                    mktList.addAll(generateAllRequiredLinesAsian(market, derivedMarketSpec,
                                    market.getBalancedLineIndex()));
                    break;

                default:
                    throw new IllegalArgumentException(String.format("Unsupported derivedMarketType: %s",
                                    derivedMarketSpec.getDerivedMarketType()));
            }
        }
        return mktList;
    }



    /**
     * generates a dynamic range market with the specified properties from the contents of the lineProbs table
     * 
     * @param nRanges
     * @param rangeWidth
     * @return
     */

    static Market getDerivedDyamicRangeHcapMarket(Market srcMarket, DerivedMarketSpec spec) {

        Market market = new Market(MarketCategory.GENERAL, srcMarket.getMarketGroup(), spec.getDerivedMarketAtsType(),
                        srcMarket.getSequenceId(), spec.getDerivedMarketName(srcMarket.getSequenceId()));
        market.setValid(true);
        DerivedMarketResulter resulter =
                        (Market derivedMarket, ResultedMarket resultedBaseMarket) -> resultDerivedDyamicRangeHcapMarket(
                                        derivedMarket, resultedBaseMarket);
        DerivedMarketDetails derivedMarketDetails = new DerivedMarketDetails(spec, resulter);
        market.setDerivedMarketDetails(derivedMarketDetails);
        market.setLineId("");
        /*
         * generate the probabilities that outcome = lineId rather than <= lineId which is what is in lineProbs
         */
        double[] lineProbs = srcMarket.getLineProbs();
        int lineBase = srcMarket.getLineBase();
        int n = lineProbs.length;;
        double[] probs = new double[n];
        probs[0] = lineProbs[0];
        for (int i = 1; i < n; i++) {
            probs[i] = lineProbs[i] - lineProbs[i - 1];
        }
        /*
         * Start from the top and work down
         */
        boolean innerRangesShorter = spec.isInnerRangesShorter();
        int lineAdjustmentForShorterRange = 0;
        if (innerRangesShorter)
            lineAdjustmentForShorterRange = 1;
        String selectionName;
        int rangeWidth = spec.getRangeWidth();
        int nRanges = spec.getnRanges();
        int highLine = spec.getnRanges() * rangeWidth + 1 - lineAdjustmentForShorterRange;
        selectionName = DynamicRangeSelectionNames.hcapName(highLine, Integer.MAX_VALUE);
        market.put(selectionName, probForRange(highLine, Integer.MAX_VALUE, lineBase, probs));
        for (int i = nRanges; i > 0; i--) {
            int nextHigh = i * rangeWidth - lineAdjustmentForShorterRange;
            int nextLow = nextHigh - rangeWidth + 1;
            if (i == 1)
                nextLow += lineAdjustmentForShorterRange;
            selectionName = DynamicRangeSelectionNames.hcapName(nextLow, nextHigh);
            market.put(selectionName, probForRange(nextLow, nextHigh, lineBase, probs));
        }
        market.put("Draw", probForRange(0, 0, lineBase, probs));
        for (int i = 1; i <= nRanges; i++) {
            int nextLow = -i * rangeWidth + lineAdjustmentForShorterRange;
            int nextHigh = nextLow + rangeWidth - 1;
            if (i == 1)
                nextHigh -= lineAdjustmentForShorterRange;
            selectionName = DynamicRangeSelectionNames.hcapName(nextLow, nextHigh);
            market.put(selectionName, probForRange(nextLow, nextHigh, lineBase, probs));
        }
        int lowLine = -nRanges * rangeWidth - 1 + lineAdjustmentForShorterRange;
        selectionName = DynamicRangeSelectionNames.hcapName(Integer.MIN_VALUE, lowLine);
        market.put(selectionName, probForRange(Integer.MIN_VALUE, lowLine, lineBase, probs));
        return market;
    }

    /*
     * generates the resulted market object for this derived market
     */
    static ResultedMarket resultDerivedDyamicRangeHcapMarket(Market derivedMarket, ResultedMarket resultedBaseMarket) {
        DerivedMarketSpec derivedMarketSpec = derivedMarket.getDerivedMarketDetails().getDerivedMarketSpec();
        String winningSelection = DynamicRangeSelectionNames.getHcapSelectionNameForOutcome(derivedMarketSpec,
                        resultedBaseMarket.getActualOutcome());

        ResultedMarket resultedMarket = new ResultedMarket(derivedMarket.getType(), derivedMarket.getLineId(),
                        derivedMarket.getCategory(), derivedMarket.getSequenceId(), resultedBaseMarket.isMarketVoided(),
                        derivedMarket.getMarketDescription(), winningSelection, resultedBaseMarket.getActualOutcome());
        return resultedMarket;
    }

    /**
     * generates a dynamic range market with the specified properties from the contents of the lineProbs table
     * 
     * @param nRanges
     * @param rangeWidth
     * @return
     */

    static Market getDerivedDyamicRangeTotalMarket(Market srcMarket, DerivedMarketSpec spec) {

        Market market = new Market(MarketCategory.GENERAL, srcMarket.getMarketGroup(), spec.getDerivedMarketAtsType(),
                        srcMarket.getSequenceId(), spec.getDerivedMarketName(srcMarket.getSequenceId()));
        market.setValid(true);
        DerivedMarketResulter resulter = (Market derivedMarket,
                        ResultedMarket resultedBaseMarket) -> resultDerivedDyamicRangeTotalMarket(derivedMarket,
                                        resultedBaseMarket);
        DerivedMarketDetails derivedMarketDetails = new DerivedMarketDetails(spec, resulter);
        market.setDerivedMarketDetails(derivedMarketDetails);
        market.setLineId("");

        /*
         * generate the probabilities that outcome = lineId rather than <= lineId which is what is in lineProbs
         */
        double[] lineProbs = srcMarket.getLineProbs();
        int lineBase = srcMarket.getLineBase();
        int n = lineProbs.length;;
        double[] probs = new double[n];
        probs[0] = lineProbs[0];
        for (int i = 1; i < n; i++) {
            probs[i] = lineProbs[i] - lineProbs[i - 1];
        }
        /*
         * Start from the bottom and work up
         */
        String selectionName;
        int rangeWidth = spec.getRangeWidth();
        int nRanges = spec.getnRanges();
        int rangeStart = spec.getTotalMarketRangeStart();
        selectionName = DynamicRangeSelectionNames.totalName(Integer.MIN_VALUE, rangeStart);
        market.put(selectionName, probForRange(Integer.MIN_VALUE, rangeStart, lineBase, probs));

        for (int i = 1; i <= nRanges; i++) {
            int nextHigh = rangeStart + i * rangeWidth;
            int nextLow = nextHigh - rangeWidth + 1;
            selectionName = DynamicRangeSelectionNames.totalName(nextLow, nextHigh);
            market.put(selectionName, probForRange(nextLow, nextHigh, lineBase, probs));
        }
        int rangeEnd = rangeStart + nRanges * rangeWidth + 1;
        selectionName = DynamicRangeSelectionNames.totalName(rangeEnd, Integer.MAX_VALUE);
        market.put(selectionName, probForRange(rangeEnd, Integer.MAX_VALUE, lineBase, probs));
        return market;
    }

    static ResultedMarket resultDerivedDyamicRangeTotalMarket(Market derivedMarket, ResultedMarket resultedBaseMarket) {
        DerivedMarketSpec derivedMarketSpec = derivedMarket.getDerivedMarketDetails().getDerivedMarketSpec();
        int actualOutcome = resultedBaseMarket.getActualOutcome();
        String winningSelection =
                        DynamicRangeSelectionNames.getTotalSelectionNameForOutcome(derivedMarketSpec, actualOutcome);

        ResultedMarket resultedMarket = new ResultedMarket(derivedMarket.getType(), derivedMarket.getLineId(),
                        derivedMarket.getCategory(), derivedMarket.getSequenceId(), resultedBaseMarket.isMarketVoided(),
                        derivedMarket.getMarketDescription(), winningSelection, resultedBaseMarket.getActualOutcome());
        return resultedMarket;
    }

    private static double probForRange(int nLow, int nHigh, int lineBase, double[] probs) {
        int iLow = Math.max(nLow, lineBase);
        int iHigh = Math.min(nHigh, lineBase + probs.length - 1);
        double p = 0;
        for (int i = iLow; i <= iHigh; i++) {
            p += probs[i - lineBase];
        }
        return p;
    }

    static Collection<Market> generateAllRequiredLinesRegular(Market srcMarket, DerivedMarketSpec derivedMarketSpec,
                    int centralLineIndex) {

        Collection<Market> mktList = new ArrayList<Market>();
        double[] fixLines = derivedMarketSpec.getFixedLines();
        int nLines = derivedMarketSpec.getnMkts();
        int spacing = derivedMarketSpec.getLineSpacing();
        int reverseGap = 1;
        DerivedMarketGapBetweenLines x = derivedMarketSpec.getDerivedMarketGapBetweenLines();
        if (x == null)
            throw new IllegalArgumentException("null derivedMarketSpec");
        switch (derivedMarketSpec.getDerivedMarketGapBetweenLines()) {
            case FULL_LINE:
                reverseGap = 1;
                break;
            case HALF_LINE:
                reverseGap = 2;
                break;
            case QUARTER_LINE:
                throw new IllegalArgumentException("Quarter line spacing not allowed for regular line markets");
        }
        MarketCategory category = srcMarket.getCategory();
        double balancedLine;
        String line;



        //// jin added markets creation for the fixed lines creation.
        boolean fixLinesMarkets = false;
        if (fixLines != null)
            if (fixLines.length > 0)
                fixLinesMarkets = true;

        boolean useProbBoundaries = false;
        if (derivedMarketSpec.getMinProbBoundary() > 0) {
            useProbBoundaries = true;
        }

        if (fixLinesMarkets) {
            srcMarket.setMarketStatus(new MarketStatus(SuspensionStatus.SUSPENDED_UNDISPLAY, "Fixed Line Rule",
                            "Overwritting Most Balanced Line"));
            switch (srcMarket.getCategory()) {
                case HCAP:
                    balancedLine = srcMarket.getAhcpLineId(category, centralLineIndex, reverseGap);
                    for (int i = 0; i < fixLines.length; i++) {
                        line = String.format("%.2f", fixLines[i]);
                        Market newMarket = generateNewLineMarket(srcMarket, derivedMarketSpec, line);
                        newMarket.setBalancedLine(balancedLine);
                        mktList.add(newMarket);
                    }
                    break;
                case OVUN:
                    balancedLine = srcMarket.getOvunLineId(category, centralLineIndex, reverseGap);
                    for (int i = 0; i < fixLines.length; i++) {
                        line = String.format("%.1f", fixLines[i]);
                        Market newMarket = generateNewLineMarket(srcMarket, derivedMarketSpec, line);
                        newMarket.setBalancedLine(balancedLine);
                        if (useProbBoundaries) {
                            double minProbBoundary = derivedMarketSpec.getMinProbBoundary();
                            double maxProbBoundary = derivedMarketSpec.getMaxProbBoundary();

                            for (double prob : newMarket.getSelectionsProbs().values()) {
                                if (!(prob < minProbBoundary || prob > maxProbBoundary)) {
                                    mktList.add(newMarket);
                                }
                            }
                        } else {
                            mktList.add(newMarket);
                        }
                    }
                    break;
                case HCAPEU:
                    for (int i = 0; i < fixLines.length; i++) {
                        line = String.format("%.2f", fixLines[i]);
                        Market newMarket = generateNewLineMarket(srcMarket, derivedMarketSpec, line);
                        mktList.add(newMarket);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Invalid market category: " + srcMarket.getCategory());
            }
            //// markets creation for fixed lines ends
        } else {

            switch (srcMarket.getCategory()) {
                case HCAP:
                    balancedLine = srcMarket.getAhcpLineId(category, centralLineIndex, reverseGap);
                    for (int i = -nLines; i <= nLines; i++) {
                        line = String.format("%.2f", -(balancedLine + (double) i * spacing / reverseGap));
                        Market newMarket = generateNewLineMarket(srcMarket, derivedMarketSpec, line);
                        newMarket.setBalancedLine(balancedLine);
                        mktList.add(newMarket);
                    }
                    break;
                case OVUN:
                    balancedLine = srcMarket.getOvunLineId(category, centralLineIndex, reverseGap);
                    for (int i = -nLines; i <= nLines; i++) {

                        line = String.format("%.2f", balancedLine + (double) i * spacing / reverseGap);
                        Market newMarket = generateNewLineMarket(srcMarket, derivedMarketSpec, line);
                        newMarket.setBalancedLine(balancedLine);
                        mktList.add(newMarket);
                    }
                    break;
                case HCAPEU:
                    for (int i = -nLines; i <= nLines; i++) {
                        int lineIndex = (int) (centralLineIndex + i * spacing);
                        line = srcMarket.getLineId(category, lineIndex);
                        mktList.add(generateNewLineMarket(srcMarket, derivedMarketSpec, line));
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Invalid market category: " + srcMarket.getCategory());
            }

        }
        return mktList;
    }

    private static Collection<? extends Market> generateAllRequiredLinesAsian(Market srcMarket,
                    DerivedMarketSpec derivedMarketSpec, int centralLineIndex) {
        Collection<Market> mktList = new ArrayList<Market>();
        int nLines = derivedMarketSpec.getnMkts();
        int spacing = derivedMarketSpec.getLineSpacing();
        int reverseGap = 4;
        MarketCategory category = srcMarket.getCategory();
        double balancedLine;
        String line;
        switch (srcMarket.getCategory()) {
            case HCAP:
                balancedLine = srcMarket.getAhcpLineId(category, centralLineIndex, reverseGap);
                /*
                 * Added the support to AHCP market here
                 */
                if (srcMarket.getType().contains("AHCP")) {
                    String sequenceId = srcMarket.getSequenceId();
                    String score;
                    if (sequenceId.contains("H"))
                        score = sequenceId.substring(2, sequenceId.length());
                    else
                        score = sequenceId.substring(1, sequenceId.length());
                    String[] index = score.split("-");
                    int ahcp = Integer.parseInt(index[0]) - Integer.parseInt(index[1]);
                    balancedLine -= ahcp;
                    srcMarket.setLineBase(srcMarket.getLineBase() - ahcp);
                }
                for (int i = -nLines; i <= nLines; i++) {
                    line = String.format("%.2f", -(balancedLine + (double) i * spacing / reverseGap));
                    if (line.equals("0.00") || line.equals("0.0")) {
                        line = "-0.00";
                    }
                    Market newMarket = generateNewLineMarket(srcMarket, derivedMarketSpec, line);
                    newMarket.setBalancedLine(balancedLine);

                    boolean useProbBoundaries = false;
                    if (derivedMarketSpec.getMinProbBoundary() > 0) {
                        useProbBoundaries = true;
                    }
                    if (useProbBoundaries) {
                        double minProbBoundary = derivedMarketSpec.getMinProbBoundary();
                        double maxProbBoundary = derivedMarketSpec.getMaxProbBoundary();

                        for (double prob : newMarket.getSelectionsProbs().values()) {
                            if (!(prob < minProbBoundary || prob > maxProbBoundary)) {
                                mktList.add(newMarket);
                            }
                        }
                    } else {
                        mktList.add(newMarket);
                    }
                }
                break;
            case OVUN:
                balancedLine = srcMarket.getOvunLineId(category, centralLineIndex, reverseGap);
                for (int i = -nLines; i <= nLines; i++) {

                    line = String.format("%.2f", balancedLine + (double) i * spacing / reverseGap);
                    Market newMarket = generateNewLineMarket(srcMarket, derivedMarketSpec, line);
                    newMarket.setBalancedLine(balancedLine);
                    mktList.add(newMarket);
                }
                break;
            case HCAPEU:
                for (int i = -nLines; i <= nLines; i++) {
                    int lineIndex = (int) (centralLineIndex + i * spacing);
                    line = srcMarket.getLineId(category, lineIndex);
                    mktList.add(generateNewLineMarket(srcMarket, derivedMarketSpec, line));
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid market category: " + srcMarket.getCategory());
        }
        return mktList;

    }

    private static Collection<? extends Market> generateAllRequiredLinesEuropean(Market srcMarket,
                    DerivedMarketSpec derivedMarketSpec, int centralLineIndex) {
        // TODO Auto-generated method stub
        return null;

    }


    /**
     * generate the nMkts closest to .5 prob.
     * 
     * @param srcMarket
     * @param derivedMarketSpec
     * @return
     */
    static Collection<Market> generateNearestEvensMarkets(Market srcMarket, DerivedMarketSpec derivedMarketSpec) {
        Collection<Market> mktList = new ArrayList<Market>();
        int nMkts = derivedMarketSpec.getnMkts();
        double[] lineProbs = srcMarket.getLineProbs();
        /*
         * sort the lineProbs by distance from 0.5 by adding to a TreeMap
         */
        TreeMap<Double, Integer> map = new TreeMap<Double, Integer>();
        for (int i = 0; i < lineProbs.length; i++) {
            /*
             * add a bit of random noise to the key to avoid possibility of two keys having exactly the same value
             */
            double key = Math.abs((lineProbs[i] - 0.5)) + 1.0E-6 * Math.random();
            map.put(key, i);
        }
        /*
         * extract the indices we want and sort into order
         */
        ArrayList<Integer> indexList = new ArrayList<Integer>();
        for (Integer i : map.values()) {
            if (indexList.size() == nMkts)
                break;
            indexList.add(i);
        }
        Collections.sort(indexList);

        int lineBase = srcMarket.getLineBase();
        for (Integer i : indexList) {
            String line;
            switch (srcMarket.getCategory()) {

                case HCAP:
                case HCAPEU:
                    line = String.format("%.2f", -((double) (lineBase + i)) + 0.5);
                    break;
                case OVUN:
                    line = String.format("%.2f", ((double) (lineBase + i)) + 0.5);
                    break;
                case GENERAL:
                default:
                    throw new IllegalArgumentException("Invalid market category: " + srcMarket.getCategory());

            }

            mktList.add(generateNewLineMarket(srcMarket, derivedMarketSpec, line));
        }
        return mktList;
    }

    /**
     * generates a new market for the specified line
     * 
     * @param srcMarket
     * @param derivedMarketSpec
     * @param line
     * @return
     */
    private static Market generateNewLineMarket(Market srcMarket, DerivedMarketSpec derivedMarketSpec, String line) {
        Market newMarket = srcMarket.getMarketForLineId(line);
        /*
         * set type if specified in derivedMarketSpec, else use the same as in the srcMarket
         */
        if (derivedMarketSpec.getDerivedMarketAtsType() != null)
            newMarket.setType(derivedMarketSpec.getDerivedMarketAtsType());
        /*
         * set description if specified in derivedMarketSpec, else use the same as in the srcMarket
         */
        if (derivedMarketSpec.getDerivedMarketNames() != null)
            newMarket.setMarketDescription(derivedMarketSpec.getDerivedMarketName(srcMarket.getSequenceId()));
        DerivedMarketResulter resulter =
                        (Market derivedMarket, ResultedMarket resultedBaseMarket) -> resultDerivedLineMarket(
                                        derivedMarket, resultedBaseMarket);
        DerivedMarketDetails derivedMarketDetails = new DerivedMarketDetails(derivedMarketSpec, resulter);
        newMarket.setDerivedMarketDetails(derivedMarketDetails);
        return newMarket;
    }

    private static ResultedMarket resultDerivedLineMarket(Market derivedMarket, ResultedMarket resultedBaseMarket) {
        int actualOutcome = resultedBaseMarket.getActualOutcome();

        String lineId = derivedMarket.getLineId();
        String winningSelection;
        /* Jin add, 423 - 427, commented 446-449 */
        boolean voidMarket = false;
        if (resultedBaseMarket.isMarketVoided()) {
            voidMarket = true;
            winningSelection = "VOID";
        } else {

            switch (derivedMarket.getCategory()) {
                case HCAP:
                    winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(actualOutcome, lineId);
                    break;
                case HCAPEU:
                    winningSelection = MarketUtilityFunctions.getWinningSelectionEuroHcapMarket(actualOutcome, lineId);
                    break;
                case OVUN:
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(actualOutcome, lineId);
                    break;
                case GENERAL:
                default:
                    throw new IllegalArgumentException(
                                    "Trying to result a non line market: " + derivedMarket.getType());
            }

            if (winningSelection == null) {
                voidMarket = true;
                winningSelection = "VOID";
            }
        }

        // if (winningSelection == null) {
        // voidMarket = true;
        // winningSelection = "VOID";
        // }
        if (derivedMarket.getType().contains("AHCP")) {
            winningSelection = MarketUtilityFunctions.getWinningSelectionAhcpMarket(actualOutcome, lineId);
        }

        if (derivedMarket.getType().contains("ATG") || derivedMarket.getType().contains("ACOU")) {
            winningSelection = MarketUtilityFunctions.getWinningSelectionAouMarket(actualOutcome, lineId);
        }
        ResultedMarket resultedMarket = new ResultedMarket(derivedMarket.getType(), lineId, derivedMarket.getCategory(),
                        derivedMarket.getSequenceId(), voidMarket, derivedMarket.getMarketDescription(),
                        winningSelection, actualOutcome);
        return resultedMarket;
    }

}
