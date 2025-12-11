package ats.algo.genericsupportfunctions;

public class Plane {

    private double a;
    private double b;
    private double c;
    private double z0;
    private boolean invalidInput;

    /**
     * initialises for the 3 points that define the plane
     * 
     * @param x1
     * @param x2
     * @param x3
     */
    public Plane(Vector3D x1, Vector3D x2, Vector3D x3, double z0) {
        /*
         * calculate the coefficients for the eqn of a plane passing through all three points: a.x+b.y+c.z=1. and sets
         * the coefficients for the straight line that lies in the plane and passes through z0: a.x + b.y = 1-c.z0
         */
        double q12 = x1.x * x2.z - x2.x * x1.z;
        double q13 = x1.x * x3.z - x3.x * x1.z;
        double r12 = x1.y * x2.z - x2.y * x1.z;
        double r13 = x1.y * x3.z - x3.y * x1.z;
        double s12 = x2.z - x1.z;
        double s13 = x3.z - x1.z;
        invalidInput = false;
        try {
            a = (s12 * r13 - s13 * r12) / (q12 * r13 - q13 * r12);
            b = (s13 - a * q13) / r13;
            c = (1 - a * x1.x - b * x1.y) / x1.z;
        } catch (ArithmeticException e) {
            /*
             * all three points lie on a straight line
             */
            invalidInput = true;
        }
        this.z0 = z0;
    }

    /**
     * returns the value of z in the plan that corresponds to x and y
     * 
     * @param x
     * @param y
     * @return
     */
    public Double calcZ(double x, double y) {
        if (invalidInput)
            return null;
        else
            return (1 - a * x - b * y) / c;
    }

    /**
     * Sets the params for the 2-D line in the plane that equates to z=z0;
     * 
     * @param z0
     */
    public void setLine(double z0) {
        this.z0 = z0;
    }

    /**
     * finds the intersection of two straight lines
     * 
     * @param l1
     * @param l2
     * @return
     */
    public static PairOfDoubles findIntersection(Plane l1, Plane l2) {
        if (l1.invalidInput || l2.invalidInput)
            return null;
        double gA = l1.a;
        double gB = l1.b;
        double gC = 1 - l1.c * l1.z0;
        double gD = l2.a;
        double gE = l2.b;
        double gF = 1 - l2.c * l2.z0;
        PairOfDoubles p = new PairOfDoubles();
        try {
            p.A = (gC * gE - gB * gF) / (gA * gE - gD * gB);
            p.B = (gC - gA * p.A) / gB;
        } catch (ArithmeticException e) {
            /*
             * lines are parallel
             */
            p = null;
        }
        return p;
    }
}
