package ats.algo.sport.tennis;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import ats.algo.core.baseclasses.AlgoMatchParams;
import ats.algo.core.baseclasses.GenericMatchParams;

public class MatchParamsTest {

    @Test
    public void matchParamsClassNameTest() {
        AlgoMatchParams matchParams = new TennisMatchParams();
        assertTrue(matchParams.getClass().getName().contains("TennisMatchParams"));
        GenericMatchParams genericMatchParams = matchParams.generateGenericMatchParams();
        boolean paramsMatch = genericMatchParams.getClass().getName().contains("TennisMatchParams");
        System.out.println(paramsMatch);
        assertFalse(paramsMatch);
    }
}
