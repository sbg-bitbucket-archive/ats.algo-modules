package ats.algo.inplayGS;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import ats.algo.inplayGS.Selection.Team;

public class InPlayGSCalculatorTest3 {

    private static Selection[] firstToScore = {new Selection(Team.B, "Carlos Garcia", 34.00),
            new Selection(Team.B, "Abdoulaye Ba", 34.00), new Selection(Team.A, "Aurelian Chedjou", 13.00),
            new Selection(Team.A, "Berk Unsal", 7.00), new Selection(Team.B, "Berkan Emir", 29.00),
            new Selection(Team.A, "Birhan Vatansever", 19.00), new Selection(Team.B, "Birol Parlak", 101.00),
            new Selection(Team.A, "Bruma", 5.50), new Selection(Team.B, "Daniel Candeias", 13.00),
            new Selection(Team.B, "Deniz Vural", 26.00), new Selection(Team.B, "Efecan Karaca", 15.00),
            new Selection(Team.B, "Emre Akbaba", 10.00), new Selection(Team.A, "Eren Derdiyok", 4.500),
            new Selection(Team.B, "Erhan Kartal", 91.00), new Selection(Team.B, "Fabrice NSakala", 151.00),
            new Selection(Team.B, "Gokay Iravul", 21.00), new Selection(Team.A, "Hakan Kadir Balta", 17.00),
            new Selection(Team.A, "Hamit Altintop", 13.00), new Selection(Team.B, "Isaac Sackey", 19.00),
            new Selection(Team.B, "Ismail Aissati", 17.00), new Selection(Team.B, "Jonathan Ayite", 11.00),
            new Selection(Team.A, "Josue", 8.00), new Selection(Team.B, "Kenneth Omeruo", 91.00),
            new Selection(Team.B, "Lamine Gassama", 151.00), new Selection(Team.A, "Lionel Carole", 34.00),
            new Selection(Team.A, "Luis Pedro Cavanda", 67.00), new Selection(Team.A, "Lukas Podolski", 5.00),
            new Selection(Team.A, "Martin Linnes", 15.00), new Selection(Team.A, "Nigel De Jong", 19.00),
            new Selection(Team.B, "Nuru Sulley", 29.00), new Selection(Team.A, "Sabri Sarioglu", 21.00),
            new Selection(Team.B, "Sajjad Shahbazzadeh", 12.00), new Selection(Team.A, "Salih Dursun", 12.00),
            new Selection(Team.B, "Sefa Yilmaz", 12.00), new Selection(Team.A, "Selcuk Inan", 6.500),
            new Selection(Team.A, "Semih Kaya", 26.00), new Selection(Team.A, "Serdar Aziz", 26.00),
            new Selection(Team.A, "Sinan Gumus", 6.00), new Selection(Team.B, "Taha Yalciner", 23.00),
            new Selection(Team.A, "Tolga Cigerci", 15.00), new Selection(Team.B, "Vagner Love", 8.500),
            new Selection(Team.A, "Wesley Sneijder", 6.00), new Selection(Team.B, "Wilde-Donald Guerrier", 11.00),
            new Selection(Team.A, "Yasin Oztekin", 5.500), new Selection(Team.NEITHER, "No Score", 15)};


    private static Selection[] teamToScore = {new Selection(Team.NEITHER, "No Score", 17.0),
            new Selection(Team.A, "Team A", 1.36), new Selection(Team.B, "Team B", 3.0)};

    private static Selection[] correctScore =
                    {new Selection(Team.B, "1-2", 19.000), new Selection(Team.NEITHER, "0-0", 19.000),
                            new Selection(Team.B, "0-1", 23.000), new Selection(Team.B, "0-2", 46.000),
                            new Selection(Team.B, "0-3", 91.000), new Selection(Team.B, "0-4", 126.000),
                            new Selection(Team.B, "0-5", 151.000), new Selection(Team.B, "0-6", 151.000),
                            new Selection(Team.A, "1-0", 9.000), new Selection(Team.NEITHER, "1-1", 8.500),
                            new Selection(Team.B, "1-3", 46.000), new Selection(Team.B, "1-4", 101.000),
                            new Selection(Team.B, "1-5", 151.000), new Selection(Team.A, "2-0", 8.000),
                            new Selection(Team.A, "2-1", 7.500), new Selection(Team.NEITHER, "2-2", 15.000),
                            new Selection(Team.B, "2-3", 41.000), new Selection(Team.B, "2-4", 91.000),
                            new Selection(Team.A, "3-0", 11.000), new Selection(Team.A, "3-1", 11.000),
                            new Selection(Team.A, "3-2", 19.000), new Selection(Team.NEITHER, "3-3", 41.000),
                            new Selection(Team.A, "4-0", 19.000), new Selection(Team.A, "4-1", 19.000),
                            new Selection(Team.A, "4-2", 34.000), new Selection(Team.A, "5-0", 34.000),
                            new Selection(Team.A, "5-1", 34.000), new Selection(Team.A, "6-0", 71.000)};


    private static String[] teamLineup = {"Hakan Kadir Balta", "Efecan Karaca", "Lionel Carole", "Nuru Sulley",
            "Isaac Sackey", "Daniel Candeias", "Berk Unsal", "Ismail Aissati", "Carlos Garcia", "Gokay Iravul",
            "Emre Akbaba", "Lukas Podolski", "Birhan Vatansever", "Erhan Kartal", "Fabrice NSakala", "Lamine Gassama",
            "Nigel De Jong", "Luis Pedro Cavanda", "Josue", "Sabri Sarioglu", "Martin Linnes", "Bruma"};

    @Test
    public void test() {
        // LogUtil.initConsoleLogging(Level.TRACE);
        /*
         * get input into the right format
         */
        Map<String, Selection> teamToScorePrices = new LinkedHashMap<String, Selection>();
        for (int i = 0; i < teamToScore.length; i++)
            teamToScorePrices.put(teamToScore[i].name, teamToScore[i]);
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
        calculator.setReApplyInputMargin(false);
        calculator.calculate(preMatchTeamSheet, firstToScorePrices, correctScorePrices, 60 * 90, 0, null, null, "0-0");
        Map<String, Selection> InPlayGSNext = calculator.getGSForNextGoal();
        Map<String, Selection> InPlayGSAnytime = calculator.getGSForAnytimeGoal();

        System.out.println("Calculated next to score prices:");
        for (Selection sel : InPlayGSNext.values())
            System.out.println(sel.toString());
        System.out.println("Calculated anytime score prices:");
        for (Selection sel : InPlayGSAnytime.values())
            System.out.println(sel.toString());

        assertEquals(.0213, InPlayGSNext.get("Daniel Candeias").prob, 0.0005);
        assertEquals(31, InPlayGSNext.get("Daniel Candeias").price, 0.0005);
        assertEquals(.2181, InPlayGSAnytime.get("Eren Derdiyok").prob, 0.0005);
        assertEquals(1.8, InPlayGSAnytime.get("Eren Derdiyok").price, 3.25);
    }

}
