package ats.algo.sport.tennis;

import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class TennisMatchParamsCopyTest {
    @Test
    public void testMatchParams() {
        MethodName.log();
        TennisMatchParams matchParams1 = new TennisMatchParams();
        matchParams1.setDoublesMatch(true);
        matchParams1.setEventId(1111);
        matchParams1.setOnServePctA1(.3, .03);
        matchParams1.setOnServePctA2(.4, .04);
        matchParams1.setOnServePctB1(.5, .05);
        matchParams1.setOnServePctB2(.6, .06);
        TennisMatchParams matchParams2 = (TennisMatchParams) matchParams1.copy();
        // System.out.print(matchParams1.toString() + "\n");
        // System.out.print(matchParams2.toString() + "\n");
        assertEquals(matchParams1, matchParams2);

        TennisMatchParams matchParams3 = new TennisMatchParams();
        matchParams3.setDoublesMatch(false);
        matchParams3.setOnServePctA1(.3, .03);
        matchParams3.setOnServePctB1(.7, .07);
        TennisMatchParams matchParams4 = (TennisMatchParams) matchParams3.copy();
        assertEquals(matchParams3, matchParams4);

    }

    @Test
    public void test() {
        MethodName.log();
        TennisMatchParams matchParams = new TennisMatchParams();
        GenericMatchParams genericParams = matchParams.generateGenericMatchParams();
        GenericMatchParams genericParams2 = genericParams.copy();
        matchParams.getOnServePctA1().setMean(.5);
        genericParams.getParamMap().get("onServePctA1").getGaussian().setMean(0.5);
        // System.out.println("P1: \n" + genericParams);
        // System.out.println("P2: \n" + genericParams2);
        assertFalse(genericParams2.getParamMap().get("onServePctA1").getGaussian().getMean() == 0.5);
    }
}
