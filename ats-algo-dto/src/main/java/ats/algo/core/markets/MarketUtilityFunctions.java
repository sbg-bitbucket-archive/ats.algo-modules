package ats.algo.core.markets;

import ats.algo.genericsupportfunctions.GCMath;

/**
 * static methods for manipulating market related entities
 * 
 * @author Geoff
 *
 */
public class MarketUtilityFunctions {

    /**
     * converts the supplied id string e.g. "4.5" to integer e.g. "4"
     * 
     * @param lineIdString
     * @return
     */
    public static int convertLineIdStringToLineIndex(String lineIdString) {
        double r = Double.parseDouble(lineIdString);
        r -= 0.5;
        return (int) GCMath.round(r, 0);
    }

    /**
     * converts the supplied id string e.g. "4.5" to integer e.g. "4"
     * 
     * @param lineIdString
     * @return
     */
    public static double convertLineIdStringToLineIndex2(String lineIdString) {
        double r = Double.parseDouble(lineIdString);

        return r;
    }

    /**
     * converts the supplied integer to a lineId string. e.g. -4 -> -3.5
     * 
     * @param id
     * @return
     */
    public static String convertLineIndexToLineIdString(int id) {
        double r = id + 0.5;
        return String.format("%.1f", r);
    }

    /**
     * determines the winning outcome for an overunder market, given the actual result
     * 
     * @param actualOutcome - e.g. 20
     * @param lineId - e.g. "21.5" or "21"
     * @return Either "Under" or "Over" or null if actualOutcome exactly matches lineId
     */
    public static String getWinningSelectionOvunMarket(int actualOutcome, String lineId) {
        if (actualOutComeEqualsLineId(actualOutcome, lineId))
            return null;
        int n = convertLineIdStringToLineIndex(lineId);
        String winningSelection;
        if (actualOutcome <= n)
            winningSelection = "Under";
        else
            winningSelection = "Over";
        return winningSelection;
    }

    /*
     * returns true if the int and string are the same value, to within a small margin for dealing with rounding issues
     */
    private static boolean actualOutComeEqualsLineId(int actualOutcome, String lineId) {
        double x = Double.valueOf(lineId);
        return Math.abs(x - (double) actualOutcome) < 1.0e-6;
    }

    /**
     * Determines the winning outcome for a handicap market, given the actual result
     * 
     * @param actualOutcome - e.g. -4
     * @param lineId - e.g. "2.5" or "2"
     * @return Either "A" or "B" or null if actual
     */
    public static String getWinningSelectionHcapMarket(int actualOutcome, String lineId) {
        if (actualOutComeEqualsLineId(actualOutcome, lineId))
            return null;
        double lineIdAsDouble = convertLineIdStringToLineIndex2(lineId);
        String winningSelection;
        if (lineIdAsDouble + (double) actualOutcome > 0)
            winningSelection = "AH";
        else
            winningSelection = "BH";
        return winningSelection;
    }

    /**
     * Determines the winning outcome for a handicap market, given the actual result
     * 
     * @param actualOutcome - e.g. -4
     * @param lineId - e.g. "2.5" or "2"
     * @return Either "A" or "B" or null if actual
     */
    public static String getWinningSelectionAhcpMarket(int actualOutcome, String lineId) {
        double lineIdAsDouble = convertLineIdStringToLineIndex2(lineId);
        String winningSelection = "";
        if (lineIdAsDouble + (double) actualOutcome > 0 && (lineIdAsDouble + actualOutcome) >= 0.5)
            winningSelection = "AH";
        else if (lineIdAsDouble + (double) actualOutcome > 0
                        && (Math.abs((lineIdAsDouble + actualOutcome)) * 4 % 4 == 1))
            winningSelection = "halfWonA";
        else if (lineIdAsDouble + (double) actualOutcome == 0 && (lineIdAsDouble + actualOutcome) == 0.0)
            winningSelection = "stakeBack";
        else if (lineIdAsDouble + (double) actualOutcome == 0 && (lineIdAsDouble + actualOutcome) < 0.0)
            winningSelection = "BH";
        else if (lineIdAsDouble + (double) actualOutcome == 0 && (lineIdAsDouble + actualOutcome) > 0.0)
            winningSelection = "AH";
        else if (lineIdAsDouble + (double) actualOutcome < 0 && (lineIdAsDouble + actualOutcome) <= -0.5)
            winningSelection = "BH";
        else if (lineIdAsDouble + (double) actualOutcome < 0
                        && (Math.abs((lineIdAsDouble + actualOutcome)) * 4 % 4 == 1))
            winningSelection = "halfLostA";
        else
            winningSelection = "error selection pleasae check with Rob" + lineIdAsDouble + "--" + actualOutcome;
        return winningSelection;
    }

    /**
     * Determines the winning outcome for a handicap market, given the actual result
     * 
     * @param actualOutcome - e.g. -4
     * @param lineId - e.g. "2.5" or "2"
     * @return Either "A" or "B" or null if actual
     */
    public static String getWinningSelectionAouMarket(int actualOutcome, String lineId) {
        double lineIdAsDouble = convertLineIdStringToLineIndex2(lineId);
        String winningSelection = "";
        double s = (double) actualOutcome - lineIdAsDouble;
        if (s >= 0.5)
            winningSelection = "O";
        else if (s == 0.25)
            winningSelection = "halfWonO";
        else if (s == 0.0)
            winningSelection = "stakeBack";
        else if (s <= -0.5)
            winningSelection = "U";
        else if (s == -0.25)
            winningSelection = "halfLostO";
        else
            winningSelection = "error selection pleasae check with Rob" + lineIdAsDouble + "--" + actualOutcome;
        return winningSelection;
    }

    /**
     * Determines the winning outcome for a handicap market, given the actual result
     * 
     * @param actualOutcome - e.g. -4
     * @param lineId - e.g. "2"
     * @return Either "A" "B" or "D"
     */
    public static String getWinningSelectionEuroHcapMarket(int actualOutcome, String lineId) {
        int n = convertLineIdStringToLineIndex(lineId);
        String winningSelection;
        if (actualOutcome < n)
            winningSelection = "AH";
        else if (actualOutcome == n)
            winningSelection = "DH";
        else
            winningSelection = "BH";
        return winningSelection;
    }
}
