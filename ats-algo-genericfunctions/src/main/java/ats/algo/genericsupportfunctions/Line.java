package ats.algo.genericsupportfunctions;

/*
 * implements y=ax+b
 */
public class Line {

    private double a;
    private double b;

    public Line(double a, double b) {
        this.a = a;
        this.b = b;
    }

    public Line(Vector2D v1, Vector2D v2) {
        if (v1.x == v2.x) {
            a = 0;
            b = (v1.y + v2.y) / 2.0;
        } else {
            a = (v1.y - v2.y) / (v1.x - v2.x);
            b = v1.y - a * v1.x;
        }
    }

    public double calcY(double x) {
        return a * x + b;
    }

    public double calcX(double y) {
        return (y - b) / a;
    }
}
