package ats.algo.core.baseclasses;

import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.sport.tennis.TennisMatchParams;

public class MatchParamsCopyTest {
    @Test
    public void test() {
        TennisMatchParams matchParams = new TennisMatchParams();
        MatchParams genericParams = matchParams.generateGenericMatchParams();
        MatchParams genericParams2 = genericParams.copy();
        matchParams.getOnServePctA1().setMean(.5);
        genericParams.getParamMap().get("onServePctA1").getGaussian().setMean(0.5);
        System.out.println("P1: \n" + genericParams);
        System.out.println("P2: \n" + genericParams2);
        assertFalse(genericParams2.getParamMap().get("onServePctA1").getGaussian().getMean() == 0.5);
    }

}
