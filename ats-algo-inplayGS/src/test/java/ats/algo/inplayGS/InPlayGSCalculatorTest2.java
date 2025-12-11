package ats.algo.inplayGS;


import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import ats.algo.inplayGS.Selection.Team;
import ats.core.util.log.Level;
import ats.core.util.log.LogUtil;

public class InPlayGSCalculatorTest2 {

    private static Selection[] firstToScore = {new Selection(Team.A, "Ashley Barnes", 8.500),
            new Selection(Team.A, "Steven Defour", 15.000), new Selection(Team.A, "George Boyd", 15.000),
            new Selection(Team.A, "No Score", 7.500), new Selection(Team.B, "Jose Fonte", 29.000),
            new Selection(Team.B, "Maya Yoshida", 17.000), new Selection(Team.B, "Steven Davis", 12.000),
            new Selection(Team.B, "Pierre Hojbjerg", 9.500), new Selection(Team.A, "Stephen Ward", 41.000),
            new Selection(Team.A, "Jeff Hendrick", 9.500), new Selection(Team.B, "Josh Sims", 7.500),
            new Selection(Team.B, "James Ward-Prowse", 9.500), new Selection(Team.B, "Cuco Martina", 41.000),
            new Selection(Team.B, "Jordy Clasie", 17.000), new Selection(Team.A, "Scott Arfield", 13.000),
            new Selection(Team.A, "John Flanagan", 29.000), new Selection(Team.B, "Jay Rodriguez", 6.000),
            new Selection(Team.A, "Sam Vokes", 8.500), new Selection(Team.A, "James Tarkowski", 23.000),
            new Selection(Team.A, "Aiden O'Neill", 21.000), new Selection(Team.A, "Matthew Lowton", 34.000),
            new Selection(Team.A, "Joey Barton", 12.000), new Selection(Team.A, "Michael Keane", 26.000),
            new Selection(Team.B, "Florin Gardos", 34.000), new Selection(Team.B, "Harrison Reed", 19.000),
            new Selection(Team.B, "Oriol Romeu", 12.000), new Selection(Team.A, "Patrick Bamford", 8.000),
            new Selection(Team.A, "Johann Gudmundsson", 10.000), new Selection(Team.B, "Nathan Redmond", 6.500),
            new Selection(Team.B, "Sam McQueen", 15.000), new Selection(Team.B, "Shane Long", 6.000),
            new Selection(Team.A, "Andre Gray", 7.000), new Selection(Team.B, "Virgil Van Dijk", 13.000),
            new Selection(Team.A, "Michael Kightly", 15.000), new Selection(Team.B, "Ryan Bertrand", 15.000),
            new Selection(Team.A, "Dean Marney", 23.000), new Selection(Team.A, "Tendayi Darikwa", 34.000),
            new Selection(Team.A, "Ben Mee", 34.000), new Selection(Team.B, "Dusan Tadic", 7.500),
            new Selection(Team.B, "Olufela Olomola", 9.000), new Selection(Team.B, "Jack Stephens", 41.000),
            new Selection(Team.B, "Cedric Soares", 41.000)};



    private static Selection[] correctScore =
                    {new Selection(Team.A, "4-1", 91.000), new Selection(Team.A, "4-0", 101.000),
                            new Selection(Team.B, "1-2", 9.500), new Selection(Team.B, "0-2", 9.500),
                            new Selection(Team.B, "0-1", 6.500), new Selection(Team.B, "5-5", 201.000),
                            new Selection(Team.B, "4-4", 201.000), new Selection(Team.B, "3-3", 81.000),
                            new Selection(Team.B, "2-2", 17.000), new Selection(Team.B, "1-1", 6.500),
                            new Selection(Team.B, "0-10", 201.000), new Selection(Team.B, "0-9", 201.000),
                            new Selection(Team.B, "1-8", 201.000), new Selection(Team.B, "0-8", 201.000),
                            new Selection(Team.A, "3-2", 46.000), new Selection(Team.A, "3-1", 34.000),
                            new Selection(Team.A, "3-0", 41.000), new Selection(Team.A, "0-0", 8.000),
                            new Selection(Team.A, "10-0", 201.000), new Selection(Team.A, "9-0", 201.000),
                            new Selection(Team.A, "8-1", 201.000), new Selection(Team.A, "8-0", 201.000),
                            new Selection(Team.B, "1-7", 201.000), new Selection(Team.B, "0-7", 201.000),
                            new Selection(Team.B, "3-6", 201.000), new Selection(Team.B, "2-6", 201.000),
                            new Selection(Team.B, "1-6", 151.000), new Selection(Team.B, "0-6", 151.000),
                            new Selection(Team.A, "2-0", 17.000), new Selection(Team.A, "1-0", 8.500),
                            new Selection(Team.A, "7-1", 201.000), new Selection(Team.A, "7-0", 201.000),
                            new Selection(Team.A, "6-3", 201.000), new Selection(Team.A, "6-2", 201.000),
                            new Selection(Team.A, "6-1", 201.000), new Selection(Team.B, "4-5", 201.000),
                            new Selection(Team.B, "3-5", 201.000), new Selection(Team.B, "2-5", 151.000),
                            new Selection(Team.B, "1-5", 126.000), new Selection(Team.B, "0-5", 126.000),
                            new Selection(Team.B, "3-4", 126.000), new Selection(Team.A, "6-0", 201.000),
                            new Selection(Team.A, "5-4", 201.000), new Selection(Team.A, "5-3", 201.000),
                            new Selection(Team.A, "5-2", 201.000), new Selection(Team.A, "5-1", 151.000),
                            new Selection(Team.A, "5-0", 151.000), new Selection(Team.A, "2-1", 12.000),
                            new Selection(Team.A, "4-3", 151.000), new Selection(Team.A, "4-2", 101.000),
                            new Selection(Team.B, "2-4", 81.000), new Selection(Team.B, "1-4", 51.000),
                            new Selection(Team.B, "0-4", 51.000), new Selection(Team.B, "2-3", 34.000),
                            new Selection(Team.B, "1-3", 21.000), new Selection(Team.B, "0-3", 19.000)};


    private static String[] teamLineup = {"Aiden O'Neill", "Andre Gray", "Ashley Barnes", "Ben Mee", "Dean Marney",
            "George Boyd", "Michael Keane", "Michael Kightly", "Sam Vokes", "Scott Arfield", "Stephen Ward",
            "Cuco Martina", "Cedric Soares", "Dusan Tadic", "Florin Gardos", "Harrison Reed", "Jack Stephens",
            "Jay Rodriguez", "Jordy Clasie", "Jose Fonte", "Shane Long", "Steven Davis",};

    @Test
    public void test() {
        LogUtil.initConsoleLogging(Level.TRACE);
        /*
         * get input into the right format
         */

        Map<String, Selection> firstToScorePrices = new LinkedHashMap<String, Selection>();
        for (int i = 0; i < firstToScore.length; i++)
            firstToScorePrices.put(firstToScore[i].name, firstToScore[i]);
        Map<String, Selection> correctScorePrices = new LinkedHashMap<String, Selection>();
        for (int i = 0; i < correctScore.length; i++)
            correctScorePrices.put(correctScore[i].name, correctScore[i]);

        Map<String, PlayerStatus> preMatchTeamSheet = new HashMap<String, PlayerStatus>();
        for (String player : firstToScorePrices.keySet())
            preMatchTeamSheet.put(player, PlayerStatus.ON_THE_BENCH);
        for (String player : teamLineup)
            preMatchTeamSheet.put(player, PlayerStatus.PLAYING);
        /*
         * call the calculator
         */
        InPlayGSCalculator calculator = new InPlayGSCalculator();
        calculator.setNextGoalScorerMarginPerSelection(0.12);
        calculator.setAnytimeGoalScorerMarginPerSelection(0.12);
        calculator.calculate(preMatchTeamSheet, firstToScorePrices, correctScorePrices, 60 * 90, 0, null, null, "0-0");
        Map<String, Selection> InPlayGSNext = calculator.getGSForNextGoal();
        Map<String, Selection> InPlayGSAnytime = calculator.getGSForAnytimeGoal();

        System.out.println("Calculated next to score prices:");
        for (Selection sel : InPlayGSNext.values())
            System.out.println(sel.toString());
        System.out.println("Calculated anytime score prices:");
        for (Selection sel : InPlayGSAnytime.values())
            System.out.println(sel.toString());

        /*
         * assertEquals(.0213, InPlayGSNext.get("Daniel Candeias").prob, 0.0005); assertEquals(31,
         * InPlayGSNext.get("Daniel Candeias").price, 0.0005); assertEquals(.2181,
         * InPlayGSAnytime.get("Eren Derdiyok").prob, 0.0005); assertEquals(1.8,
         * InPlayGSAnytime.get("Eren Derdiyok").price, 3.25);
         */
    }

}
