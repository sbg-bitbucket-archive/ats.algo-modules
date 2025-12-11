package ats.algo.genericsupportfunctions;

import static org.junit.Assert.*;

import org.junit.Test;

public class DateFunctionsTest {

    @Test
    /**
     * nb this test needs to be run from a pc set to UK time
     */
    public void test() {
        String d = DateFunctions.millisToString(1480347015005L, "yyyy-MM-dd HH:mm:ss.SSS");
        System.out.println(d);
        assertEquals("2016-11-28 15:30:15.005", d);
    }
}
