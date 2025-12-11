package ats.algo.genericsupportfunctions;

import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionTest {

    @Test
    public void test() {
        assertEquals(0.5, NormalDistribution.cumNormal(0.0), 0.0001);
        assertEquals(0.0, NormalDistribution.cumNormal(-4.0), 0.0001);
        assertEquals(1.0, NormalDistribution.cumNormal(4.0), 0.0001);
        assertEquals(0.6915, NormalDistribution.cumNormal(0.5), 0.0001);
        assertEquals(0.8413, NormalDistribution.cumNormal(1.0), 0.0001);
        assertEquals(0.9772, NormalDistribution.cumNormal(2.0), 0.0001);
        assertEquals(0.9987, NormalDistribution.cumNormal(3.0), 0.0001);
        assertEquals(0.3446, NormalDistribution.cumNormal(0.8, 1.0, 0.5), 0.0001);
    }

}
