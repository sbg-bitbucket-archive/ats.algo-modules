package ats.algo.inplayGS;

import static org.junit.Assert.*;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import ats.algo.inplayGS.Selection.Team;
import ats.core.util.json.JsonUtil;
import ats.core.util.log.Level;
import ats.core.util.log.LogUtil;

public class InPlayGSCalculatorTest {


    Map<String, Selection> preMatchFirstToScorePrices;
    Map<String, Selection> preMatchCorrectScorePrices;
    Map<String, PlayerStatus> preMatchTeamSheet;
    Map<String, PlayerStatus> inPlayTeamSheet;
    Map<String, Selection> currentCorrectScorePrices;

    private void initData() {
        /*
         * get test input into the right format
         */
        Selection[] firstToScore = {new Selection(Team.NEITHER, "No Score", 6.94),
                new Selection(Team.A, "Player A1", 12.71), new Selection(Team.A, "Player A2", 5.53),
                new Selection(Team.A, "Player A3", 3.67), new Selection(Team.B, "Player B1", 2.96),
                new Selection(Team.B, "Player B2", 7.60), new Selection(Team.B, "Player B3", 4.89)};

        Selection[] preMatchCorrectScore = {new Selection(Team.NEITHER, "0-0", 7.05),
                new Selection(Team.B, "0-1", 4.96), new Selection(Team.B, "0-2", 2.74),
                new Selection(Team.B, "1-2", 3.20), new Selection(Team.B, "2-3", 3.87)};

        Selection[] currentCorrectScore = {new Selection(Team.B, "0-2", 2.02), new Selection(Team.B, "1-2", 2.25),
                new Selection(Team.B, "2-3", 2.94)};

        preMatchTeamSheet = new LinkedHashMap<String, PlayerStatus>();
        preMatchTeamSheet.put("Player A1", PlayerStatus.ON_THE_BENCH);
        preMatchTeamSheet.put("Player A2", PlayerStatus.PLAYING);
        preMatchTeamSheet.put("Player A3", PlayerStatus.PLAYING);
        preMatchTeamSheet.put("Player B1", PlayerStatus.PLAYING);
        preMatchTeamSheet.put("Player B2", PlayerStatus.ON_THE_BENCH);
        preMatchTeamSheet.put("Player B3", PlayerStatus.PLAYING);

        inPlayTeamSheet = new LinkedHashMap<String, PlayerStatus>();
        inPlayTeamSheet.put("Player A1", PlayerStatus.PLAYING);
        inPlayTeamSheet.put("Player A2", PlayerStatus.ON_THE_BENCH);
        inPlayTeamSheet.put("Player A3", PlayerStatus.PLAYING);
        inPlayTeamSheet.put("Player B1", PlayerStatus.PLAYING);
        inPlayTeamSheet.put("Player B2", PlayerStatus.PLAYING);
        inPlayTeamSheet.put("Player B3", PlayerStatus.ON_THE_BENCH);

        preMatchFirstToScorePrices = new LinkedHashMap<String, Selection>();
        preMatchCorrectScorePrices = new LinkedHashMap<String, Selection>();
        currentCorrectScorePrices = new LinkedHashMap<String, Selection>();

        for (int i = 0; i < firstToScore.length; i++)
            preMatchFirstToScorePrices.put(firstToScore[i].name, firstToScore[i]);
        for (int i = 0; i < preMatchCorrectScore.length; i++)
            preMatchCorrectScorePrices.put(preMatchCorrectScore[i].name, preMatchCorrectScore[i]);
        for (int i = 0; i < currentCorrectScore.length; i++)
            currentCorrectScorePrices.put(currentCorrectScore[i].name, currentCorrectScore[i]);
    }

    @Test
    public void test() {
        LogUtil.initConsoleLogging(Level.TRACE);


        initData();
        InPlayGSCalculator calculator = new InPlayGSCalculator();
        calculator.setNextGoalScorerMarginPerSelection(0.12);
        calculator.setAnytimeGoalScorerMarginPerSelection(0.12);
        calculator.setReApplyInputMargin(false);
        int matchDurationSecs = 60 * 90;
        int matchClockSecs = 0;

        calculator.calculate(preMatchTeamSheet, preMatchFirstToScorePrices, preMatchCorrectScorePrices,
                        matchDurationSecs, matchClockSecs, null, null, "0-0");
        Map<String, Selection> InPlayGSNext = calculator.getGSForNextGoal();
        Map<String, Selection> InPlayGSAnytime = calculator.getGSForAnytimeGoal();

        System.out.println("Calculated next to score prices:");
        for (Selection sel : InPlayGSNext.values())
            System.out.println(sel.toString());
        System.out.println("Calculated anytime score prices:");
        for (Selection sel : InPlayGSAnytime.values())
            System.out.println(sel.toString());
        assertEquals(.0546, InPlayGSNext.get("Player A2").prob, 0.0005);
        assertEquals(.4498, InPlayGSAnytime.get("Player B3").prob, 0.0005);
        assertEquals(12, InPlayGSNext.get("Player A2").price, 0.0005);
        assertEquals(1.8, InPlayGSAnytime.get("Player B3").price, 0.0005);
        /*
         * check captured data is serializing ok
         */

        AllInputOutputData x = calculator.getAllInputOutputData();
        String json = calculator.getAllInputOutputDataAsJson();
        System.out.println(json);
        AllInputOutputData y = JsonUtil.unmarshalJson(json, AllInputOutputData.class);
        assertEquals(x, y);

    }

    /**
     * calc prices for 0-2
     */
    @Test
    public void test2() {
        LogUtil.initConsoleLogging(Level.TRACE);

        initData();
        InPlayGSCalculator calculator = new InPlayGSCalculator();
        calculator.setNextGoalScorerMarginPerSelection(0.12);
        calculator.setAnytimeGoalScorerMarginPerSelection(0.12);
        calculator.setReApplyInputMargin(false);
        calculator.calculate(preMatchTeamSheet, preMatchFirstToScorePrices, preMatchCorrectScorePrices, 60 * 90, 400,
                        inPlayTeamSheet, currentCorrectScorePrices, "0-2");
        Map<String, Selection> InPlayGSNext = calculator.getGSForNextGoal();
        Map<String, Selection> InPlayGSAnytime = calculator.getGSForAnytimeGoal();

        System.out.println("End display of input data and demargined probs. IP GS next");
        for (Selection sel : InPlayGSNext.values())
            System.out.println(sel.toString());
        System.out.println("InPlayGSAnytime next.");
        for (Selection sel : InPlayGSAnytime.values())
            System.out.println(sel.toString());
        assertEquals(.3556, InPlayGSNext.get("Player A1").prob, 0.0005);
        assertEquals(.1303, InPlayGSAnytime.get("Player B2").prob, 0.0005);
        assertTrue(InPlayGSAnytime.get("Player A2") == null);


    }

    /**
     * add custom odds ladder and margins
     */
    @Test
    public void test3() {
        LogUtil.initConsoleLogging(Level.TRACE);

        /*
         * calculate prices for "0-0"
         */
        initData();
        InPlayGSCalculator calculator = new InPlayGSCalculator();
        calculator.setNextGoalScorerMarginPerSelection(0.08);
        calculator.setAnytimeGoalScorerMarginPerSelection(0.16);
        calculator.setOddsLadder(customOddsLadder());
        calculator.setReApplyInputMargin(false);
        calculator.calculate(preMatchTeamSheet, preMatchFirstToScorePrices, preMatchCorrectScorePrices, 90 * 60, 0,
                        null, null, "0-0");
        Map<String, Selection> InPlayGSNext = calculator.getGSForNextGoal();
        Map<String, Selection> InPlayGSAnytime = calculator.getGSForAnytimeGoal();

        System.out.println("Calculated next to score prices:");
        for (Selection sel : InPlayGSNext.values())
            System.out.println(sel.toString());
        System.out.println("Calculated anytime score prices:");
        for (Selection sel : InPlayGSAnytime.values())
            System.out.println(sel.toString());
        assertEquals(.0546, InPlayGSNext.get("Player A2").prob, 0.0005);
        assertEquals(.4498, InPlayGSAnytime.get("Player B3").prob, 0.0005);
        assertEquals(14, InPlayGSNext.get("Player A2").price, 0.0005);
        assertEquals(1.65, InPlayGSAnytime.get("Player B3").price, 0.0005);

    }

    private double[] customOddsLadder() {
        double[] ladder = {1, 1.001, 1.002, 1.004, 1.005, 1.007, 1.01, 1.02, 1.03, 1.04, 1.05, 1.06, 1.07, 1.08, 1.09,
                1.1, 1.11, 1.12, 1.14, 1.15, 1.16, 1.18, 1.2, 1.22, 1.25, 1.28, 1.3, 1.33, 1.35, 1.36, 1.4, 1.44, 1.47,
                1.5, 1.53, 1.55, 1.57, 1.6, 1.62, 1.65, 1.67, 1.7, 1.72, 1.73, 1.75, 1.8, 1.83, 1.85, 1.9, 1.91, 1.95,
                2, 2.05, 2.1, 2.15, 2.2, 2.25, 2.3, 2.35, 2.38, 2.4, 2.45, 2.5, 2.55, 2.6, 2.63, 2.65, 2.7, 2.75, 2.8,
                2.85, 2.88, 2.9, 2.95, 3, 3.05, 3.1, 3.15, 3.2, 3.25, 3.3, 3.35, 3.4, 3.45, 3.5, 3.6, 3.7, 3.75, 3.8,
                3.9, 4, 4.1, 4.2, 4.33, 4.4, 4.5, 4.6, 4.7, 4.75, 4.8, 5, 5.25, 5.5, 5.75, 6, 6.5, 7, 7.5, 8, 8.5, 9,
                9.5, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 21, 23, 26, 29, 31, 34, 36, 41, 46, 51, 56, 61, 67, 71, 76,
                81, 86, 91, 96, 101, 106, 111, 116, 121, 126, 131, 136, 141, 146, 151, 156, 161, 166, 171, 176, 181,
                186, 191, 196, 201, 206, 211, 216, 221, 226, 231, 236, 241, 246, 251, 256, 261, 266, 271, 276, 281, 286,
                291, 296, 301, 306, 311, 316, 321, 326, 331, 336, 341, 346, 351, 356, 361, 366, 371, 376, 381, 386, 391,
                396, 401, 406, 411, 416, 421, 426, 431, 436, 441, 446, 451, 456, 461, 466, 471, 476, 481, 486, 491, 496,
                501};
        return ladder;
    }


}
