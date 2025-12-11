package ats.algo.sport.darts;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DartMatchParamsCopyTest {

    @Test
    public void test() {
        DartMatchParams dartMatchParams = new DartMatchParams();
        dartMatchParams.setSkillA(1.2, 0.8);
        dartMatchParams.setSkillB(1.3, 0.6);
        dartMatchParams.setTriplesVsDoublesA(0.8, 0.05);
        dartMatchParams.setTriplesVsDoublesA(0.7, 0.04);
        DartMatchParams dartMatchParams2 = (DartMatchParams) dartMatchParams.copy();
        System.out.println(dartMatchParams.toString());
        System.out.println(dartMatchParams2.toString());
        assertEquals(dartMatchParams, dartMatchParams2);
    }

}
