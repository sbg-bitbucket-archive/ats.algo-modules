package ats.algo.sport.expectedGoals;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.genericsupportfunctions.PairOfDoubles;
import ats.algo.sport.expectedgoals.ExpectedGoals;

public class ExpectedGoalsTest {

    @Test
    public void test() {
        MethodName.log();
        // LogUtil.initConsoleLogging(Level.TRACE);
        ExpectedGoals expectedGoals = new ExpectedGoals();
        PairOfDoubles x = expectedGoals.calcExpectedGoals(2.65, 3.1, 3.2);
        assertEquals(1.0, x.A, 0.05);
        assertEquals(0.9, x.B, 0.05);
        //
        PairOfDoubles y = expectedGoals.calcExpectedGoals(1.37, 5.23, 6.24);
        assertEquals(3.6, y.A, 0.05);
        assertEquals(1.9, y.B, 0.05);
        //
        // PairOfDoubles z = expectedGoals.calcExpectedGoals(7.5, 4.5, 1.4);
        // System.out.println(z);

    }

}
