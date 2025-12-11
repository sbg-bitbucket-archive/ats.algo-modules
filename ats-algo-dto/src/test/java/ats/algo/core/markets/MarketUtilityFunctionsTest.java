package ats.algo.core.markets;

import static org.junit.Assert.*;

import org.junit.Test;

public class MarketUtilityFunctionsTest {
    @Test
    public void test() {
        String winningSelection;
        winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(21, "21.5");
        assertTrue(winningSelection.equals("Under"));
        winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(22, "21.5");
        assertTrue(winningSelection.equals("Over"));
        winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(5, "4.5");
        assertTrue(winningSelection.equals("AH"));
        winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(5, "5.5");
        assertTrue(winningSelection.equals("AH"));
        winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(5, "-4.5");
        assertTrue(winningSelection.equals("AH"));
        winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(5, "-5.5");
        assertTrue(winningSelection.equals("BH"));
        winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(-2, "1.5");
        assertTrue(winningSelection.equals("BH"));
        winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(-2, "2.5");
        assertTrue(winningSelection.equals("AH"));
        winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(-2, "2.5");
        assertTrue(winningSelection.equals("AH"));
        winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(1, "-2.5");
        assertTrue(winningSelection.equals("BH"));
        winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(1, "-1.5");
        assertTrue(winningSelection.equals("BH"));
        winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(1, "-0.5");
        assertTrue(winningSelection.equals("AH"));
        winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(1, "0.5");
        assertTrue(winningSelection.equals("AH"));

        for (int i = -10; i < 10; i++) {
            String lineId = MarketUtilityFunctions.convertLineIndexToLineIdString(i);
            winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(i, "-5.5");
            System.out.printf("outcome: %d, lineId: %s, winningSelection: %s\n", i, lineId, winningSelection);
        }
    }

    @Test
    public void test2() {
        String winningSelection;
        winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(1, "-2.5");
        assertTrue(winningSelection.equals("BH"));
        winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(1, "-1.5");
        assertTrue(winningSelection.equals("BH"));
        winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(1, "-0.5");
        assertTrue(winningSelection.equals("AH"));
        winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(1, "0.5");
        assertTrue(winningSelection.equals("AH"));
    }

}
