package ats.algo.genericsupportfunctions;

import java.io.Serializable;

/**
 * holds pair of integers
 * 
 * @author Geoff
 *
 */
public class PairOfIntegers implements Serializable {
    private static final long serialVersionUID = 1L;
    public int A;
    public int B;

    public PairOfIntegers() {}

    public PairOfIntegers(int a, int b) {
        this.A = a;
        this.B = b;
    }

    /**
     * returns a new Instance initialised from a String of the form n-m. Returns null if can't parse
     * 
     * @param value
     */
    public static PairOfIntegers generateFromString(String value) {
        PairOfIntegers p = new PairOfIntegers();
        String[] vBits = value.split("-");

        try {
            p.A = Integer.valueOf(vBits[0]);
            p.B = Integer.valueOf(vBits[1]);
        } catch (NumberFormatException ex) {
            p = null;
        }
        return p;
    }

    /**
     * returns a new Instance initialised from a String of the form n?m, where ? is a separator. Returns null if can't
     * parse
     *
     * @param value
     * @param separator
     * @return
     */
    public static PairOfIntegers generateFromString(String value, String separator) {
        PairOfIntegers p = new PairOfIntegers();
        String[] vBits = value.split(separator);

        try {
            p.A = Integer.valueOf(vBits[0]);
            p.B = Integer.valueOf(vBits[1]);
        } catch (NumberFormatException ex) {
            p = null;
        }
        return p;
    }

    /**
     * returns a new Instance initialised from a String of the form n-m. Returns null if can't parse
     * 
     * @param value
     */
    public static PairOfIntegers generateFromStringCheckingNegatives(String value) {
        PairOfIntegers p = new PairOfIntegers();
        if (value.startsWith("-")) {
            return null;
        }
        String[] vBits = value.split("-");

        try {
            p.A = Integer.valueOf(vBits[0]);
            p.B = Integer.valueOf(vBits[1]);
        } catch (NumberFormatException ex) {
            p = null;
        }
        return p;
    }

    /**
     * generates a string of the form "n-m"
     * 
     * @return
     */
    public String toScoreString() {
        return Integer.toString(A) + "-" + Integer.toString(B);

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + A;
        result = prime * result + B;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PairOfIntegers other = (PairOfIntegers) obj;
        if (A != other.A)
            return false;
        if (B != other.B)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PairOfIntegers [A=" + A + ", B=" + B + "]";
    }

}
