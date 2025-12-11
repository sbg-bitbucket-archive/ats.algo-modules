package ats.algo.sport.outrights;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.baseclasses.MatchParams;

public class OutrightsMatchParamsTest {

    @Test
    public void test() {
        MatchParams x = new OutrightsMatchParams();
        GenericMatchParams y = x.generateGenericMatchParams();
        assertTrue(y != null);
    }
}
