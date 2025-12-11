package ats.algo.sport.darts;

import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.sport.darts.Colour;
import ats.algo.sport.darts.DartBoard;

public class DartBoardTest {

    @Test
    public void test() {
        assertEquals(DartBoard.leftNo(6), 13);
        assertEquals(DartBoard.rightNo(20), 1);
        assertEquals(DartBoard.leftNo(15), 10);
        assertEquals(DartBoard.rightNo(15), 2);
        assertEquals(DartBoard.colourOfNumber(7), Colour.RED);
        assertEquals(DartBoard.colourOfNumber(19), Colour.GREEN);
    }

}
