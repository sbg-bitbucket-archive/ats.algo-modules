package ats.algo.genericsupportfunctions;

import static org.junit.Assert.*;

import org.junit.Test;

public class LineTest {

    @Test
    public void test() {
        Line line = new Line(3.0, 2.0);
        assertEquals(8.0, line.calcY(2.0), 0.0001);
        assertEquals(2.0, line.calcX(8.0), 0.0001);
        Line line2 = new Line(0.0, 2.0);
        assertEquals(2.0, line2.calcY(2.0), 0.0001);
        assertEquals(Double.POSITIVE_INFINITY, line2.calcX(8.0), 0.0001);
    }

    @Test
    public void test2() {
        Line line = new Line(new Vector2D(2.0, 8.0), new Vector2D(3.0, 11.0));
        assertEquals(2.0, line.calcY(0.0), 0.0001);
        assertEquals(2.0, line.calcX(8.0), 0.0001);

    }
}
