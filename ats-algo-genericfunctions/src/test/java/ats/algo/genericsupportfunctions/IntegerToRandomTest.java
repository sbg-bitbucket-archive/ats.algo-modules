package ats.algo.genericsupportfunctions;

import static org.junit.Assert.*;

import org.junit.Test;

public class IntegerToRandomTest {

    private static int START_NO = 100000;
    private static int N_COUNT = 1500;
    private static int N_BUCKETS = 30;

    @Test
    public void test() {
        int n = IntegerToRandom.convert(27, 3, 6);
        System.out.println(n);
        int m = IntegerToRandom.convert(27, 3, 6);
        assertEquals(n, m);
        System.out.println("i,r");
        int[] buckets = new int[N_BUCKETS];
        for (int i = START_NO; i < START_NO + N_COUNT; i++) {
            int r = IntegerToRandom.convert(i, 1, N_BUCKETS);
            buckets[r - 1]++;
            System.out.printf("%d,%d\n", i, r);
        }

        double chi2 = 0.0;
        double expectedCount = ((double) N_COUNT) / ((double) N_BUCKETS);
        for (int j = 0; j < N_BUCKETS; j++) {
            double x = ((double) buckets[j]) - expectedCount;
            chi2 += x * x / expectedCount;
        }
        System.out.printf("Chi-squared value is : %.2f for %d degrees of freedom\n", chi2, N_BUCKETS - 1);

    }
}
