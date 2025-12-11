package ats.algo.sport.outrights.calcengine.core;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import ats.algo.sport.outrights.calcengine.core.Competition;
import ats.algo.sport.outrights.competitionsdata.TestCompetition;

public class StandingSortTest {
    @Test
    public void sortTest() {
        MethodName.log();
        Competition competition = TestCompetition.generate();
        Standings standings = competition.generateStandings();
        Standing[] finishOrder = new Standing[standings.size()];
        int i = 0;
        for (Standing standing : standings.values()) {
            finishOrder[i] = standing;
            i++;
        }
        // // System.out.println("Before");
        for (int j = 0; j < finishOrder.length; j++)
            // // System.out.println(finishOrder[j]);
            Arrays.sort(finishOrder);
        // // System.out.println("After");
        for (int j = 0; j < finishOrder.length; j++)
            // // System.out.println(finishOrder[j]);
            assertEquals("T18", finishOrder[12].getTeamId());

    }
}
