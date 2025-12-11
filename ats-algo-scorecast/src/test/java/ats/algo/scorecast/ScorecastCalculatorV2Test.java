package ats.algo.scorecast;

import static org.junit.Assert.*;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import ats.algo.scorecast.Selection.Team;

public class ScorecastCalculatorV2Test {

    private static Selection[] correctScoreInput1 = {
            /**
             * Man City v Chel
             */
            new Selection(Team.A, "1-0", 7.5), new Selection(Team.A, "2-0", 12), new Selection(Team.A, "2-1", 11),
            new Selection(Team.A, "3-0", 29), new Selection(Team.A, "3-1", 26), new Selection(Team.A, "3-2", 41),
            new Selection(Team.A, "4-0", 81), new Selection(Team.A, "4-1", 71), new Selection(Team.A, "4-2", 111),
            new Selection(Team.A, "4-3", 221), new Selection(Team.NEITHER, "0-0", 8),
            new Selection(Team.NEITHER, "1-1", 6.5), new Selection(Team.NEITHER, "2-2", 18),
            new Selection(Team.NEITHER, "3-3", 86), new Selection(Team.NEITHER, "4-4", 251),
            new Selection(Team.NEITHER, "5-4", 61), new Selection(Team.B, "0-1", 8.5), new Selection(Team.B, "0-2", 15),
            new Selection(Team.B, "1-2", 12), new Selection(Team.B, "0-3", 36), new Selection(Team.B, "1-3", 31),
            new Selection(Team.B, "2-3", 46), new Selection(Team.B, "0-4", 111), new Selection(Team.B, "1-4", 91),
            new Selection(Team.B, "2-4", 126), new Selection(Team.B, "3-4", 236)};


    private static Selection[] firstToScoreInput1 = {
            /**
             * Man City v Chel
             */
            new Selection(Team.A, "Pells", 7), new Selection(Team.A, "Flore", 7.5), new Selection(Team.A, "Ingle", 8),
            new Selection(Team.A, "Meggio", 8), new Selection(Team.A, "Guzm", 9), new Selection(Team.A, "Birsa", 9),
            new Selection(Team.A, "Jallow", 10), new Selection(Team.A, "Parig", 10),
            new Selection(Team.A, "Castro", 10), new Selection(Team.A, "Rigon", 13), new Selection(Team.A, "Basti", 15),
            new Selection(Team.A, "Izco", 17), new Selection(Team.A, "Sard", 19), new Selection(Team.A, "Spol", 23),
            new Selection(Team.A, "Cesa", 26), new Selection(Team.A, "Gobb", 26), new Selection(Team.A, "Hetem", 34),
            new Selection(Team.A, "Daine", 41), new Selection(Team.A, "Costa", 41),
            new Selection(Team.A, "Radovan", 41), new Selection(Team.A, "Gamber", 51),
            new Selection(Team.A, "Frey", 101), new Selection(Team.NEITHER, "No score", 6.5),
            new Selection(Team.B, "Simeo", 6.5), new Selection(Team.B, "Ocam", 8), new Selection(Team.B, "Laz", 8),
            new Selection(Team.B, "Gakp", 8.5), new Selection(Team.B, "Pand", 8.5),
            new Selection(Team.B, "Rigoni", 8.5), new Selection(Team.B, "Ninko", 10),
            new Selection(Team.B, "Laxal", 13), new Selection(Team.B, "Velos", 15), new Selection(Team.B, "Nitc", 17),
            new Selection(Team.B, "Cofie", 23), new Selection(Team.B, "Brivi", 23), new Selection(Team.B, "Rincon", 26),
            new Selection(Team.B, "Gent", 34), new Selection(Team.B, "Muno", 34), new Selection(Team.B, "Burdis", 34),
            new Selection(Team.B, "Izzo", 51), new Selection(Team.B, "Biras", 51), new Selection(Team.B, "Eden", 51),
            new Selection(Team.B, "Ciss", 51), new Selection(Team.B, "Flamo", 101)};

    @Test
    public void v2Test1() {
        Selection[] correctScoreInput = null;
        Selection[] firstToScoreInput = null;

        correctScoreInput = correctScoreInput1;
        firstToScoreInput = firstToScoreInput1;

        Map<String, Selection> correctScorePrices = new LinkedHashMap<String, Selection>();
        for (int i = 0; i < correctScoreInput.length; i++)
            correctScorePrices.put(correctScoreInput[i].getName(), correctScoreInput[i]);
        Map<String, Selection> firstToScorePrices = new LinkedHashMap<String, Selection>();
        for (int i = 0; i < firstToScoreInput.length; i++)
            firstToScorePrices.put(firstToScoreInput[i].getName(), firstToScoreInput[i]);

        Map<String, Map<String, Selection>> results =
                        ScorecastCalculatorV2.calculate(correctScorePrices, firstToScorePrices, true, true);
        Map<String, Selection> scorecastFirst = results.get("scorecast");
        Map<String, Selection> wincastFirst = results.get("wincast");

        assertEquals(0.0053, scorecastFirst.get("Pells and 2-1").getProb(), 0.001);
        assertEquals(0.0014, scorecastFirst.get("Cesa and 1-1").getProb(), 0.001);
        assertEquals(0.0285, wincastFirst.get("Meggio and TeamA wins").getProb(), 0.001);
    }

    private static Selection[] correctScoreInput2 = {
            /*
             * c.f spreadsheet scorecast unit test v2
             */
            new Selection(Team.NEITHER, "0:0", 5.0), new Selection(Team.NEITHER, "0:1", 10.0),
            new Selection(Team.NEITHER, "1-0", 3.3333), new Selection(Team.NEITHER, "1:1", 6.6667),
            new Selection(Team.NEITHER, "2-1", 4.0)};

    private static Selection[] firstToScoreInput2 = {
            /*
             * c.f spreadsheet scorecast unit test v2
             */
            new Selection(Team.A, "Fred", 8.3333), new Selection(Team.A, "John", 5.5556),
            new Selection(Team.B, "Paul", 4.1667), new Selection(Team.B, "George", 3.8462)};


    @Test
    public void v2Test2() {
        // LogUtil.initConsoleLogging(Level.TRACE);
        Map<String, Selection> correctScorePrices = new LinkedHashMap<String, Selection>();
        for (int i = 0; i < correctScoreInput2.length; i++)
            correctScorePrices.put(correctScoreInput2[i].getName(), correctScoreInput2[i]);
        Map<String, Selection> firstToScorePrices = new LinkedHashMap<String, Selection>();
        for (int i = 0; i < firstToScoreInput2.length; i++)
            firstToScorePrices.put(firstToScoreInput2[i].getName(), firstToScoreInput2[i]);

        Map<String, Map<String, Selection>> results =
                        ScorecastCalculatorV2.calculate(correctScorePrices, firstToScorePrices, false, false);
        Map<String, Selection> scorecastFirst = results.get("scorecast");
        Map<String, Selection> wincastFirst = results.get("wincast");

        assertEquals(0.0450, scorecastFirst.get("John and 1-1").getProb(), 0.0001);
        assertEquals(0.04335, scorecastFirst.get("George and 2-1").getProb(), 0.0001);
        assertEquals(0.2800, wincastFirst.get("John and TeamA wins").getProb(), 0.0001);
        /*
         * add in a no score selection. Shoulld leave probs unchanged
         */
        Selection selection = new Selection(Team.NEITHER, "No score", 5.0);
        firstToScorePrices.put(selection.getName(), selection);
        Map<String, Map<String, Selection>> results2 =
                        ScorecastCalculatorV2.calculate(correctScorePrices, firstToScorePrices, false, false);
        Map<String, Selection> scorecastFirst2 = results2.get("scorecast");
        Map<String, Selection> wincastFirst2 = results2.get("wincast");

        assertEquals(0.0450, scorecastFirst2.get("John and 1-1").getProb(), 0.0001);
        assertEquals(0.04335, scorecastFirst2.get("George and 2-1").getProb(), 0.0001);
        assertEquals(0.2800, wincastFirst2.get("John and TeamA wins").getProb(), 0.0001);
        /*
         * add in a selection with an invalid selection name. Should return null and log an error
         */
        Selection selection2 = new Selection(Team.NEITHER, "Bad name", 5.0);
        correctScorePrices.put(selection2.getName(), selection2);
        Map<String, Map<String, Selection>> results3 =
                        ScorecastCalculatorV2.calculate(correctScorePrices, firstToScorePrices, false, false);
        assertEquals(null, results3);
    }

}
