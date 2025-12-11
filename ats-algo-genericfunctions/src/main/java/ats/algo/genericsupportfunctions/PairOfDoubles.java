package ats.algo.genericsupportfunctions;

import java.io.Serializable;

public class PairOfDoubles implements Serializable {
    private static final long serialVersionUID = 1L;
    public double A;
    public double B;

    public PairOfDoubles() {

    }

    public PairOfDoubles(double a, double b) {
        A = a;
        B = b;
    }

    @Override
    public String toString() {
        return "PairOfDoubles [A=" + A + ", B=" + B + "]";
    }


}
