package ats.algo.sport.darts;

import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.sport.darts.DartTarget;
import ats.algo.sport.darts.ThreeDartSet;

public class ThreeDartSetTest {

    @Test
    public void test() {
        ThreeDartSet ds = new ThreeDartSet();
        DartTarget dt = new DartTarget(3, 20);
        ds.setActual(1, dt);
        ds.setActual(2, dt);
        assertTrue(ds.hitT20(2));
        ds = new ThreeDartSet();
        dt.no = 19;
        ds.setActual(1, dt);
        dt.no = 20;
        ds.setActual(2, dt);
        assertFalse(ds.hitT20(1));
        ThreeDartSet dCopy = ds.copy();
        assertTrue(dCopy.hitT20(2));
        assertFalse(dCopy.hitT20(1));
        dt = dCopy.getActual(1);
        assertEquals(19, dt.no);
        assertEquals(3, dt.multiplier);
    }

}
