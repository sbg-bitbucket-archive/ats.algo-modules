package ats.algo.genericsupportfunctions;

public class CurveFunctions {
    /**
     * returns the square of the distance between two points
     */
    private static double distance(Point p1, Point p2) {
        return ((p1.x - p2.x) * (p1.x - p2.x)) + ((p1.y - p2.y) * (p1.y - p2.y));
    }

    /**
     * find the closest point of intersection of two curves. assumes the curves are well behaved
     * 
     * @param curve1
     * @param curve2
     * @return
     */
    public static Point findIntersection(Point[] curve1, Point[] curve2) {
        int n1 = curve1.length - 1;
        int m1 = curve2.length - 1;
        int iBest = 0;
        double minDist = distance(curve1[0], curve2[0]) + 1.0; // initialise so gets set in first iteration
        for (int i = 0; i <= n1; i++)
            for (int j = 0; j <= m1; j++) {
                double dist = distance(curve1[i], curve2[j]);
                if (dist < minDist) {
                    iBest = i;
                    minDist = dist;
                }
            }


        return curve1[iBest];
    }
}
