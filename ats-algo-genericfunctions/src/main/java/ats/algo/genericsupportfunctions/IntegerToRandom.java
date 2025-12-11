package ats.algo.genericsupportfunctions;

import java.util.Random;

public class IntegerToRandom {

    /**
     * Converts an integer to an evenly distributed random no in the range [min,max]. Calling the function repeatedly
     * for the same integer wil produce the same result
     * 
     * @param n
     * @param min
     * @param max
     */
    public static int convert(int n, int min, int max) {
        Random random = new Random(n);
        int r = min + random.nextInt(max - min + 1);
        return r;

    }

}
