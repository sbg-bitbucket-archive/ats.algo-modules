package ats.algo.inplayGS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import ats.algo.inplayGS.Selection.Team;
import ats.core.util.log.Level;
import ats.core.util.log.LogUtil;
import ats.algo.inplayGS.Selection;

public class InPlayGSCalculatorMain {

    /**
     * FC Port vs Desp
     */

    private static Selection[] teamToScoreInput1 = {new Selection(Team.NEITHER, "No Score", 15),
            new Selection(Team.B, "Desportivo Chaves", 5), new Selection(Team.A, "FC Porto", 1.17)};

    /**
     * FC Port vs Desp
     */

    private static Selection[] firstToScoreInput1 = {new Selection(Team.A, "Andre Silva", 3.4),
            new Selection(Team.A, "Otavio", 9.5), new Selection(Team.A, "Hector Herrera", 2001),
            new Selection(Team.A, "Danilo Pereira", 15), new Selection(Team.A, "Yacine Brahimi", 2001),
            new Selection(Team.A, "Jesus Corona", 2001), new Selection(Team.A, "Ivan Marcano", 26),
            new Selection(Team.A, "Diogo Jota", 2001), new Selection(Team.A, "Willy Boly", 51),
            new Selection(Team.A, "Ruben Neves", 2001), new Selection(Team.A, "Oliver Torres", 8),
            new Selection(Team.A, "Rui Pedro", 2001), new Selection(Team.A, "Felipe", 29),
            new Selection(Team.A, "Evandro Goebel", 2001), new Selection(Team.A, "Andre Andre", 12),
            new Selection(Team.A, "Alex Telles", 29), new Selection(Team.A, "Maximiliano Pereira", 29),
            new Selection(Team.A, "Joao Carlos Teixeira", 2001), new Selection(Team.A, "Miguel Layun", 9),
            new Selection(Team.B, "William", 2001), new Selection(Team.B, "Rafael Lopes", 2001),
            new Selection(Team.B, "Pedro Queiros", 101), new Selection(Team.B, "Fabio Martins", 29),
            new Selection(Team.B, "Rodrigo Battaglia", 2001), new Selection(Team.B, "Bruno Braga", 2001),
            new Selection(Team.B, "Felix Mathaus", 101), new Selection(Team.B, "Leandro Freire", 67),
            new Selection(Team.B, "Rafael Assis", 2001), new Selection(Team.B, "Gustavo Souza", 26),
            new Selection(Team.B, "Joao Patrao", 26), new Selection(Team.B, "Fabio Santos", 91),
            new Selection(Team.B, "Carlos Ponck", 51), new Selection(Team.B, "Alioune Fall", 15),
            new Selection(Team.B, "Perdigao", 2001), new Selection(Team.B, "Paulinho", 101),
            new Selection(Team.B, "Nelson Lenho", 91), new Selection(Team.B, "Simon Vukcevic", 2001),
            new Selection(Team.B, "Nemanja Petrovic", 2001), new Selection(Team.NEITHER, "No Score", 13)};

    /**
     * FC Port vs Desp
     */

    private static String[] initialTeam = {"Joao Patrao", "Danilo Pereira", "Gustavo Souza", "Fabio Martins",
            "Leandro Freire", "Ivan Marcano", "Pedro Queiros", "Paulinho", "Carlos Ponck", "Miguel Layun", "Willy Boly",
            "Andre Andre", "Fabio Santos", "Nelson Lenho", "Felipe", "Alex Telles", "Felix Mathaus", "Oliver Torres",
            "Alioune Fall", "Maximiliano Pereira", "Andre Silva", "Otavio"};



    /**
     * FC Port vs Desp
     */

    private static Selection[] correctScoreInput1 = {new Selection(Team.A, "1-0", 7), new Selection(Team.A, "2-0", 5),
            new Selection(Team.A, "2-1", 10), new Selection(Team.A, "3-0", 6.5), new Selection(Team.A, "3-1", 11),
            new Selection(Team.A, "3-2", 34), new Selection(Team.A, "4-0", 10), new Selection(Team.A, "4-1", 17),
            new Selection(Team.A, "4-2", 46), new Selection(Team.A, "5-0", 19), new Selection(Team.A, "5-1", 29),
            new Selection(Team.A, "6-0", 34), new Selection(Team.B, "0-1", 41), new Selection(Team.B, "0-2", 91),
            new Selection(Team.B, "1-2", 46), new Selection(Team.B, "0-3", 151), new Selection(Team.B, "1-3", 101),
            new Selection(Team.B, "2-3", 91), new Selection(Team.B, "0-4", 151), new Selection(Team.B, "1-4", 151),
            new Selection(Team.B, "2-4", 151), new Selection(Team.B, "0-5", 151), new Selection(Team.B, "1-5", 151),
            new Selection(Team.B, "0-6", 151), new Selection(Team.NEITHER, "0-0", 17),
            new Selection(Team.NEITHER, "1-1", 13), new Selection(Team.NEITHER, "2-2", 29),
            new Selection(Team.NEITHER, "3-3", 91)};



    public static void main(String[] args) {
        LogUtil.initConsoleLogging(Level.TRACE);
        System.out.print("In Play GS example calculation\n");
        do {
            System.out.print("Test cases available: \n");
            System.out.print("  1 - Test\n");

            Selection[] teamToScore = null;
            Selection[] firstToScore = null;
            String[] teamLineup = null;
            Selection[] correctScore = null;

            teamLineup = initialTeam;
            teamToScore = teamToScoreInput1;
            firstToScore = firstToScoreInput1;
            correctScore = correctScoreInput1;



            InPlayGSCalculator calculator = new InPlayGSCalculator();
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


            calculator.setNextGoalScorerMarginPerSelection(0.12);
            calculator.setAnytimeGoalScorerMarginPerSelection(0.12);

            // public void calculate(preMatchTeamSheet,
            // preMatchFirstToScore, preMatchCorrectScore,
            // matchDurationSecs, matchClockSecs, currentInPlayTeamSheet,
            // currentInPlayCorrectScore, currentInPlayScore)

            calculator.calculate(preMatchTeamSheet, firstToScorePrices, correctScorePrices, 60 * 90, 0, null, null,
                            "0-0");
            Map<String, Selection> InPlayGSNext = calculator.getGSForNextGoal();
            Map<String, Selection> InPlayGSAnytime = calculator.getGSForAnytimeGoal();

            waitForUser("End display of input data and demargined probs. IP GS next");
            for (Selection sel : InPlayGSNext.values())
                System.out.println(sel.toString());
            waitForUser("InPlayGSAnytime next.");
            for (Selection sel : InPlayGSAnytime.values())
                System.out.println(sel.toString());
            /*
             * if all the data sent to the console in one go we overflow the available buffer space and lose the earlier
             * data so wait to allow data to be looked at or copied.
             */
            waitForUser("InPlayGS.  Run new test next");
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
