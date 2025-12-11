package ats.algo.sport.tennis;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TennisMatchParamsCopyTest {
    @Test
    public void testMatchParams() {
        TennisMatchParams matchParams1 = new TennisMatchParams();
        matchParams1.setDoublesMatch(true);
        matchParams1.setEventId(1111);
        matchParams1.setOnServePctA1(.3, .03);
        matchParams1.setOnServePctA2(.4, .04);
        matchParams1.setOnServePctB1(.5, .05);
        matchParams1.setOnServePctB2(.6, .06);
        TennisMatchParams matchParams2 = (TennisMatchParams) matchParams1.copy();
        System.out.print(matchParams1.toString() + "\n");
        System.out.print(matchParams2.toString() + "\n");
        assertEquals(matchParams1, matchParams2);

        TennisMatchParams matchParams3 = new TennisMatchParams();
        matchParams3.setDoublesMatch(false);
        matchParams3.setOnServePctA1(.3, .03);
        matchParams3.setOnServePctB1(.7, .07);
        TennisMatchParams matchParams4 = (TennisMatchParams) matchParams3.copy();
        assertEquals(matchParams3, matchParams4);

    }
}
