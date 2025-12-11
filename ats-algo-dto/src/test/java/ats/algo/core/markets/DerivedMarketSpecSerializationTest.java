package ats.algo.core.markets;

import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.genericsupportfunctions.JsonSerializer;

public class DerivedMarketSpecSerializationTest {

    @Test
    public void test() {
        DerivedMarketSpec spec = DerivedMarketSpec.getDerivedMarketSpecForDynamicRangeHcap("Test", "Test name",
                        DerivedMarketSpecApplicability.IN_PLAY_ONLY, 27, 12, false);
        String json = JsonSerializer.serialize(spec, true);
        DerivedMarketSpec spec2 = JsonSerializer.deserialize(json, DerivedMarketSpec.class);
        assertEquals(spec, spec2);
    }
}
