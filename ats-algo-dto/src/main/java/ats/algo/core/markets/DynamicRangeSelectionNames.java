package ats.algo.core.markets;

/**
 * non instantiable class providing helper methods for generating and resulting dynamic range markets
 * 
 * @author Geoff
 *
 */
public class DynamicRangeSelectionNames {

    /**
     * private constructor prevents inadvertent instantiation
     */
    private DynamicRangeSelectionNames() {

    }

    /**
     * generates the selection name from the input params
     * 
     * @param low low end of the range.
     * @param high
     * @return A string in one of the following forms: Team B to win by 31+ Team B to win by 20-30 Draw Team A to win by
     *         25+ Team A to win by 10-20
     */
    static String hcapName(int low, int high) {
        if (low == 0 && high == 0)
            return "Draw";
        if (low == Integer.MIN_VALUE)
            return "Team B to win by " + String.valueOf(-high) + "+";
        if (high == Integer.MAX_VALUE)
            return "Team A to win by " + String.valueOf(low) + "+";
        if (low > 0)
            return "Team A to win by " + String.valueOf(low) + "-" + String.valueOf(high);
        return "Team B to win by " + String.valueOf(-high) + "-" + String.valueOf(-low);
    }

    static String getHcapSelectionNameForOutcome(DerivedMarketSpec spec, int actualOutcome) {
        int nRanges = spec.getnRanges();
        int rangeWidth = spec.getRangeWidth();
        int lowLine = -nRanges * rangeWidth - 1;
        int highLine = nRanges * rangeWidth + 1;
        if (actualOutcome == 0)
            return hcapName(0, 0);
        if (actualOutcome <= lowLine)
            return hcapName(Integer.MIN_VALUE, lowLine);
        if (actualOutcome >= highLine)
            return hcapName(highLine, Integer.MAX_VALUE);
        int absOutcome = Math.abs(actualOutcome);
        if (spec.isInnerRangesShorter()) {
            highLine = rangeWidth * (1 + absOutcome / rangeWidth) - 1;
            lowLine = highLine - rangeWidth + 1;
        } else {
            highLine = rangeWidth * (1 + ((absOutcome - 1) / rangeWidth));
            lowLine = highLine - rangeWidth + 1;
        }
        if (actualOutcome > 0)
            return hcapName(lowLine == 0 ? 1 : lowLine, highLine);
        else
            return hcapName(-highLine, lowLine == 0 ? -1 : -lowLine);
    }

    static String totalName(int low, int high) {
        if (low == Integer.MIN_VALUE)
            return String.valueOf(high) + " or below";
        if (high == Integer.MAX_VALUE)
            return String.valueOf(low) + " or above";
        return String.valueOf(low) + "-" + String.valueOf(high);
    }

    static String getTotalSelectionNameForOutcome(DerivedMarketSpec spec, int actualOutcome) {
        int lowLine = spec.getTotalMarketRangeStart();
        int nRanges = spec.getnRanges();
        int rangeWidth = spec.getRangeWidth();
        int highLine = lowLine + nRanges * rangeWidth + 1;
        if (actualOutcome <= lowLine)
            return totalName(Integer.MIN_VALUE, lowLine);
        if (actualOutcome >= highLine)
            return totalName(highLine, Integer.MAX_VALUE);
        highLine = lowLine + rangeWidth * (1 + ((actualOutcome - 1 - lowLine) / rangeWidth));
        lowLine = highLine - rangeWidth + 1;
        return totalName(lowLine, highLine);

    }

}
