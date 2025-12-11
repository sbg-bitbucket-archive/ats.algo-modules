package ats.algo.core.common;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestSequenceId {

    @Test
    public void test() {
        String s = SequenceId.getOffsetSequenceId("S2.3.1", 1);
        assertEquals("S2.3.2", s);
        s = SequenceId.getOffsetSequenceId("S2.3", 3);
        assertEquals("S2.6", s);
        s = SequenceId.getOffsetSequenceId("G2", 3);
        assertEquals("G5", s);
        s = SequenceId.getOffsetSequenceId("M", 3);
        assertEquals("M", s);
        assertEquals("S", SequenceId.getSequenceIdPrefix("S2.3.1"));
        assertEquals(3, SequenceId.getSequenceIdElements("S2.3.1").length);
        assertEquals(1, SequenceId.getSequenceIdElements("S2.3.1")[2]);
        assertEquals(203, SequenceId.getSequenceIdComparator("S2.3"));
        assertEquals(20301, SequenceId.getSequenceIdComparator("S2.3.1"));
        assertTrue(SequenceId.compare("S0.1.2", "S1.3.5") < 0);
        assertTrue(SequenceId.compare("S1.3.4", "S1.3.5") < 0);
        assertTrue(SequenceId.compare("S1.3.5", "S1.3.5") == 0);
        assertTrue(SequenceId.compare("S1.3.6", "S1.3.5") > 0);

    }
}
