package ats.algo.scorecast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

import ats.algo.genericsupportfunctions.ConsoleInput;
import ats.algo.scorecast.Selection.Team;

public class ScorecastCalculatorMain {

    /*
     * Test cases
     */

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
            new Selection(Team.NEITHER, "Any Unquoted", 61), new Selection(Team.B, "0-1", 8.5),
            new Selection(Team.B, "0-2", 15), new Selection(Team.B, "1-2", 12), new Selection(Team.B, "0-3", 36),
            new Selection(Team.B, "1-3", 31), new Selection(Team.B, "2-3", 46), new Selection(Team.B, "0-4", 111),
            new Selection(Team.B, "1-4", 91), new Selection(Team.B, "2-4", 126), new Selection(Team.B, "3-4", 236)};


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


    private static Selection[] anytmeToScoreInput1 = {
            /**
             * Man City v Chel
             */
            new Selection(Team.A, "Pells", 3.3), new Selection(Team.A, "Flore", 3.5),
            new Selection(Team.A, "Ingle", 3.8), new Selection(Team.A, "Meggio", 3.8), new Selection(Team.A, "Guzm", 4),
            new Selection(Team.A, "Birsa", 4), new Selection(Team.A, "Jallow", 4.33),
            new Selection(Team.A, "Parig", 4.5), new Selection(Team.A, "Castro", 4.33),
            new Selection(Team.A, "Rigon", 5.5), new Selection(Team.A, "Basti", 6.5), new Selection(Team.A, "Izco", 7),
            new Selection(Team.A, "Sard", 7.5), new Selection(Team.A, "Spol", 9), new Selection(Team.A, "Cesa", 10),
            new Selection(Team.A, "Gobb", 10), new Selection(Team.A, "Hetem", 13), new Selection(Team.A, "Daine", 15),
            new Selection(Team.A, "Costa", 17), new Selection(Team.A, "Radovan", 15),
            new Selection(Team.A, "Gamber", 21), new Selection(Team.A, "Frey", 34),
            new Selection(Team.NEITHER, "No score", 6.5), new Selection(Team.B, "Simeo", 3.25),
            new Selection(Team.B, "Ocam", 3.75), new Selection(Team.B, "Laz", 3.75), new Selection(Team.B, "Gakp", 3.8),
            new Selection(Team.B, "Pand", 3.8), new Selection(Team.B, "Rigoni", 3.8),
            new Selection(Team.B, "Ninko", 4.5), new Selection(Team.B, "Laxal", 5.5), new Selection(Team.B, "Velos", 6),
            new Selection(Team.B, "Nitc", 7), new Selection(Team.B, "Cofie", 9.5), new Selection(Team.B, "Brivi", 9),
            new Selection(Team.B, "Rincon", 10), new Selection(Team.B, "Gent", 13), new Selection(Team.B, "Muno", 13),
            new Selection(Team.B, "Burdis", 12), new Selection(Team.B, "Izzo", 19), new Selection(Team.B, "Biras", 23),
            new Selection(Team.B, "Eden", 23), new Selection(Team.B, "Ciss", 19), new Selection(Team.B, "Flamo", 41)};


    private static Selection[] matchResultInput1 = {
            /**
             * Man City v Chel
             */
            new Selection(Team.A, "Chievo", 2.55), new Selection(Team.NEITHER, "Draw", 3.05),
            new Selection(Team.B, "Genoa", 3)};

    public static void main(String[] args) {
        System.out.print("Scorecast example calculation\n");
        System.out.print("\nScorecast example calculation\n");
        int count = 0;
        double margin = 0;
        do {
            System.out.print("Test cases available: \n");
            System.out.print("  1 - Man City v Chel\n");

            Selection[] correctScoreInput = null;
            Selection[] firstToScoreInput = null;
            Selection[] anytimeToScoreInput = null;
            Selection[] matchResultInput = null;
            int ip = ConsoleInput.readInt("Select test: ", 1, false);
            switch (ip) {
                case 1:
                    correctScoreInput = correctScoreInput1;
                    firstToScoreInput = firstToScoreInput1;
                    anytimeToScoreInput = anytmeToScoreInput1;
                    matchResultInput = matchResultInput1;
                    break;

            }


            ScorecastCalculator calculator = new ScorecastCalculator();
            if (count > 0)
                calculator.setScorecastAnyMarginPerSelection(margin);
            calculator.setOutputDemarginedInputPrices(true);
            Map<String, Selection> correctScorePrices = new LinkedHashMap<String, Selection>();
            for (int i = 0; i < correctScoreInput.length; i++)
                correctScorePrices.put(correctScoreInput[i].getName(), correctScoreInput[i]);
            Map<String, Selection> firstToScorePrices = new LinkedHashMap<String, Selection>();
            for (int i = 0; i < firstToScoreInput.length; i++)
                firstToScorePrices.put(firstToScoreInput[i].getName(), firstToScoreInput[i]);
            Map<String, Selection> anytimeToScorePrices = new LinkedHashMap<String, Selection>();
            for (int i = 0; i < anytimeToScoreInput.length; i++)
                anytimeToScorePrices.put(anytimeToScoreInput[i].getName(), anytimeToScoreInput[i]);
            Map<String, Selection> matchResultPrices = new LinkedHashMap<String, Selection>();
            for (int i = 0; i < matchResultInput.length; i++)
                matchResultPrices.put(matchResultInput[i].getName(), matchResultInput[i]);
            calculator.calculate(correctScorePrices, firstToScorePrices, anytimeToScorePrices, matchResultPrices, true);
            Map<String, Selection> scorecastFirst = calculator.getScorecastFirst();
            Map<String, Selection> wincastFirst = calculator.getWincastFirst();
            Map<String, Selection> scorecastAny = calculator.getScorecastAny();
            Map<String, Selection> wincastAny = calculator.getWincastAny();
            System.out.printf("\nScorecastFirst selections (%d)\n", scorecastFirst.size());
            waitForUser("End display of input data and demargined probs.  ScorecastFirst next");
            for (Selection sel : scorecastFirst.values())
                System.out.println(sel.toString());
            /*
             * if all the data sent to the console in one go we overflow the available buffer space and lose the earlier
             * data so wait to allow data to be looked at or copied.
             */
            waitForUser("End ScorecastFirst. Do it again");
            System.out.printf("\nScorecastAny selections (%d)\n", scorecastAny.size());
            for (Selection sel : scorecastAny.values())
                System.out.println(sel.toString());
            waitForUser("End ScorecastAny.  WincastFirst next");
            System.out.printf("\nWincastFirst selections (%d)\n", wincastFirst.size());
            for (Selection sel : wincastFirst.values())
                System.out.println(sel.toString());
            waitForUser("End WincastFirst.  WincastAny next");
            System.out.printf("\nWincastAny selections (%d)\n", wincastAny.size());
            for (Selection sel : wincastAny.values())
                System.out.println(sel.toString());
            waitForUser("End WincastAny.  Run new test next");

            margin = ConsoleInput.readDouble("New SC Any Margin: ", 0.4, 0, 1);
            calculator.setScorecastAnyMarginPerSelection(margin);
            waitForUser("Ready");
            count++;
        } while (true);
    }

    static void waitForUser() {
        System.out.println("Press enter to continue");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        try {
            in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void waitForUser(String s) {
        System.out.println(s + ".  Press enter to continue");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        try {
            in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
