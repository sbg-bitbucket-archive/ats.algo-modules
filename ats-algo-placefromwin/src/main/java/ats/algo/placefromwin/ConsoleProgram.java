package ats.algo.placefromwin;

import java.io.IOException;

public class ConsoleProgram {
    public static void main(String[] args) {
        {
            double[] probWin = {.6, .3, .07, .03};
            // double[] probWin = { .85, .1, .03, .02 };
            int nRunners = probWin.length;
            double probFall = 0.1;
            //
            // exercise the Race class 10 times, each of which simulates a
            // single run of the race and output the finishing positions
            //
            Race race = new Race(probWin, probFall);
            System.out.println("Race.run Finishing positions:");
            for (int i = 0; i < 10; i++) {
                int[] result = race.run();
                System.out.printf("%d, %d, %d, %d\n", i, result[0], result[1], result[2], result[3]);
            }
            //
            // exercise the FinishPosnProbs class, which calculates the 2-d
            // array of finish posns for each horse, and output to console
            //
            FinishPosnProbs finishPosnProbs = new FinishPosnProbs(probWin, probFall);
            double[][] fProbs;
            fProbs = finishPosnProbs.calc(100000);
            System.out.println("Finish posn probabilities:");
            System.out.println("no, 1, 2, 3, 4, dnf");
            for (int i = 0; i < nRunners + 1; i++)
                System.out.printf("%d, %.3f, %.3f, %.3f, %.3f\n", i, fProbs[i][1], fProbs[i][2], fProbs[i][3],
                                fProbs[i][4], fProbs[i][0]);
            //
            // exercise the PlaceProbs class for 4 runners
            //
            PlaceProbs placeProbs = new PlaceProbs(probWin, probFall, 2);
            double[] pProbs = placeProbs.calc();
            System.out.println("");
            System.out.println("Place posn probabilities - 4 runners, 2 to place, probFall = 0.1:");
            for (int i = 0; i < nRunners; i++)
                System.out.printf("%d, %.3f\n", i, pProbs[i]);
            //
            // exercise the PlaceProbs class for 12 runners, 3 to place,
            // probFall = 0.1
            //
            double[] probWin2 = {.2042, .1361, .1167, .1021, .0743, .0681, .0628, .0628, .0545, .0481, .0389, .0314};
            probFall = 0.1;
            placeProbs = new PlaceProbs(probWin2, probFall, 3);
            pProbs = placeProbs.calc();
            System.out.println("");
            System.out.println("Place posn probabilities - 12 runners, 3 to place, probFall = 0.1:");
            nRunners = probWin2.length;
            for (int i = 0; i < nRunners; i++)
                System.out.printf("%d, %.3f\n", i, pProbs[i]);
            //
            // exercise the PlaceProbs class for 12 runners, 3 to place,
            // probFall = 0.0
            //
            probFall = 0.0;
            placeProbs = new PlaceProbs(probWin2, probFall, 3);
            pProbs = placeProbs.calc();
            System.out.println("");
            System.out.println("Place posn probabilities - 12 runners, 3 to place, probFall = 0.0:");
            for (int i = 0; i < nRunners; i++)
                System.out.printf("%d, %.3f\n", i, pProbs[i]);
            try {
                System.out.print("Press any key to continue");
                System.in.read();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
