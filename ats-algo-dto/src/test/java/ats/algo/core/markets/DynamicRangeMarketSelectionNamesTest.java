package ats.algo.core.markets;

import static org.junit.Assert.*;
import org.junit.Test;

public class DynamicRangeMarketSelectionNamesTest {

    @Test
    public void testHcapNames() {
        String s = DynamicRangeSelectionNames.hcapName(61, Integer.MAX_VALUE);
        assertEquals("Team A to win by 61+", s);
        s = DynamicRangeSelectionNames.hcapName(-30, -21);
        assertEquals("Team B to win by 21-30", s);
        s = DynamicRangeSelectionNames.hcapName(31, 40);
        assertEquals("Team A to win by 31-40", s);
        DerivedMarketSpec spec = DerivedMarketSpec.getDerivedMarketSpecForDynamicRangeHcap("TEST", "testName",
                        DerivedMarketSpecApplicability.IN_PLAY_ONLY, 6, 10, false);
        s = DynamicRangeSelectionNames.getHcapSelectionNameForOutcome(spec, 70);
        assertEquals("Team A to win by 61+", s);
        s = DynamicRangeSelectionNames.getHcapSelectionNameForOutcome(spec, 61);
        assertEquals("Team A to win by 61+", s);
        s = DynamicRangeSelectionNames.getHcapSelectionNameForOutcome(spec, 31);
        assertEquals("Team A to win by 31-40", s);
        s = DynamicRangeSelectionNames.getHcapSelectionNameForOutcome(spec, 35);
        assertEquals("Team A to win by 31-40", s);
        s = DynamicRangeSelectionNames.getHcapSelectionNameForOutcome(spec, 40);
        assertEquals("Team A to win by 31-40", s);
        s = DynamicRangeSelectionNames.getHcapSelectionNameForOutcome(spec, 41);
        assertEquals("Team A to win by 41-50", s);
        s = DynamicRangeSelectionNames.getHcapSelectionNameForOutcome(spec, -35);
        assertEquals("Team B to win by 31-40", s);
        s = DynamicRangeSelectionNames.getHcapSelectionNameForOutcome(spec, -31);
        assertEquals("Team B to win by 31-40", s);
        s = DynamicRangeSelectionNames.getHcapSelectionNameForOutcome(spec, -40);
        assertEquals("Team B to win by 31-40", s);
        s = DynamicRangeSelectionNames.getHcapSelectionNameForOutcome(spec, -70);
        assertEquals("Team B to win by 61+", s);
        s = DynamicRangeSelectionNames.getHcapSelectionNameForOutcome(spec, -61);
        assertEquals("Team B to win by 61+", s);
    }

    @Test
    public void testTotalNames() {
        String s = DynamicRangeSelectionNames.totalName(235, Integer.MAX_VALUE);
        assertEquals("235 or above", s);
        s = DynamicRangeSelectionNames.totalName(31, 40);
        assertEquals("31-40", s);
        DerivedMarketSpec spec = DerivedMarketSpec.getDerivedMarketSpecForDynamicRangeTotal("TEST", "testName",
                        DerivedMarketSpecApplicability.IN_PLAY_ONLY, 4, 20, 150);
        s = DynamicRangeSelectionNames.getTotalSelectionNameForOutcome(spec, 250);
        assertEquals("231 or above", s);
        s = DynamicRangeSelectionNames.getTotalSelectionNameForOutcome(spec, 231);
        assertEquals("231 or above", s);
        s = DynamicRangeSelectionNames.getTotalSelectionNameForOutcome(spec, 184);
        assertEquals("171-190", s);
        s = DynamicRangeSelectionNames.getTotalSelectionNameForOutcome(spec, 190);
        assertEquals("171-190", s);
        s = DynamicRangeSelectionNames.getTotalSelectionNameForOutcome(spec, 171);
        assertEquals("171-190", s);
        s = DynamicRangeSelectionNames.getTotalSelectionNameForOutcome(spec, 151);
        assertEquals("151-170", s);
        s = DynamicRangeSelectionNames.getTotalSelectionNameForOutcome(spec, 15);
        assertEquals("150 or below", s);


    }

}
