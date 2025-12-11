package ats.algo.genericsupportfunctions;

import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.genericsupportfunctions.LinearInterpolation;

public class LinearInterpolationTest {

    @Test
    public void test() throws Exception {
        {

            double[] xVec = new double[6];
            double[] yVec = new double[9];
            double[][] table = new double[6][9];
            for (int i = 0; i < 6; i++) // start with simple function z = 10*x+y, for x in [0,5], y in [10,18]
            {
                xVec[i] = i;
                for (int j = 0; j < 9; j++) {
                    yVec[j] = j + 10;
                    table[i][j] = i * 10 + j + 10;
                }
            }
            double x = 1.5;
            double y = 15.5;
            double z = LinearInterpolation.interpolate(x, y, xVec, yVec, table);
            assertEquals(30.5, z, .0001);
            x = 0; // test edge conditions
            y = 18;
            z = LinearInterpolation.interpolate(x, y, xVec, yVec, table);
            assertEquals(18.0, z, .0001);

            x = 5; // test more edge conditions
            y = 10;
            z = LinearInterpolation.interpolate(x, y, xVec, yVec, table);
            assertEquals(60.0, z, .0001);
            for (int i = 0; i < 6; i++) // now a non linear example z = (x^2 + (y)^2)^0.5
            {
                xVec[i] = i;
                for (int j = 0; j < 9; j++) {
                    yVec[j] = j + 10;
                    table[i][j] = Math.pow((Math.pow((double) i, 2) + Math.pow((double) (j + 10), 2)), 0.5);
                }
            }
            x = 3.65;
            y = 12.42;
            z = LinearInterpolation.interpolate(x, y, xVec, yVec, table);
            double s = Math.pow((Math.pow(x, 2) + Math.pow(y, 2)), 0.5);
            assertEquals(s, z, 0.02);
        }
    }

}
