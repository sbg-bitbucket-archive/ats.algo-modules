package ats.algo.genericsupportfunctions;

import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.genericsupportfunctions.RandomNoGenerator;

public class RandomNoGeneratorTest {

    @Test
    public void TestBasicRandomNos() {
        double mean = 0;
        int N = 1000000;
        for (int i = 0; i < N; i++)
            mean += RandomNoGenerator.nextDouble();
        mean = mean / N;
        assertEquals(.5, mean, 0.001);
        for (int i = 0; i < N; i++) {
            int n = RandomNoGenerator.nextInt(20, 81);
            assertTrue(n >= 20);
            assertTrue(n < 81);
            mean += n;
        }
        mean = mean / N;
        assertEquals(50, mean, 0.1);
    }


    @Test
    public void TestNextPoisson() {
        double mean = calcMean(6, 20000);
        assertEquals(6, mean, 0.05);
        mean = calcMean(150, 20000);
        assertEquals(150, mean, 1);
        mean = calcMean(2000, 20000);
        assertEquals(2000, mean, 5);
    }

    double calcMean(int lambda, int N) {
        double mean = 0;
        for (int i = 0; i < N; i++) {
            int nextI = RandomNoGenerator.nextPoisson(lambda);
            mean += nextI;
        }
        mean = mean / N;
        return mean;
    }

    //
    @Test
    public void TestNextIntWithStatedMean() {
        double mean = 0;
        int N = 50000;
        for (int i = 0; i < N; i++) {
            int nextI = RandomNoGenerator.nextIntWithStatedMean(7, 12);
            mean += nextI;
            assertTrue(nextI >= 0);
            assertTrue(nextI <= 12);
        }
        mean = mean / N;
        assertEquals(7, mean, 0.05);
        for (int i = 0; i < N; i++) {
            int nextI = RandomNoGenerator.nextIntWithStatedMean(15, 35);
            mean += nextI;
            assertTrue(nextI >= 0);
            assertTrue(nextI <= 35);
        }
        mean = mean / N;
        assertEquals(15, mean, 0.1);
        for (int i = 0; i < N; i++) {
            int nextI = RandomNoGenerator.nextIntWithStatedMean(2, 3);
            mean += nextI;
            assertTrue(nextI >= 0);
            assertTrue(nextI <= 3);
        }
        mean = mean / N;
        assertEquals(2, mean, 0.1);
    }

    @Test
    public void testNormal() {
        int N = 5000000;
        int n0 = 0;
        int n1 = 0;
        int n2 = 0;
        for (int i = 0; i < N; i++) {
            double rnd = RandomNoGenerator.nextNormal();
            if (rnd < 0)
                n0++;
            if (rnd < 1)
                n1++;
            if (rnd < 2)
                n2++;
        }
        double p0 = ((double) n0) / N;
        double p1 = ((double) n1) / N;
        double p2 = ((double) n2) / N;
        assertEquals(.5, p0, .0005);
        assertEquals(.8413, p1, .001);
        assertEquals(.9777, p2, .001);

    }
}
