package ats.algo.genericsupportfunctions;

import java.io.Serializable;

/**
 * Implements a 2-d point (x,y)
 * 
 * @author Geoff
 *
 */
public class Point implements Comparable<Point>, Serializable {
    private static final long serialVersionUID = 1L;

    public double x;
    public double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * makes a copy of itself
     * 
     * @return
     */
    public Point copy() {
        return new Point(this.x, this.y);
    }

    /**
     * standard comparison function required by Comparable interface to sort points by their x coordinate
     */

    public int compareTo(Point arg0) {
        int n;
        if (x > arg0.x)
            n = 1;
        else if (x == arg0.x)
            n = 0;
        else
            n = -1;
        return n;
    }
}
