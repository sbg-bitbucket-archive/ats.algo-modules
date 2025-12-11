package ats.algo.sport.darts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
// import org.junit.Test;

import ats.algo.genericsupportfunctions.CurveFunctions;
import ats.algo.genericsupportfunctions.PairOfDoubles;
import ats.algo.genericsupportfunctions.Point;

/**
 * Holds the lookup table for probabilities associated with each leg, plus assorted methods for retrieving data
 * 
 * @author Geoff
 *
 */
public final class LegProbTables {
    private static double[][] probTableWhenDoubleNotReqdToStart;
    private static double[][] probTableWhenDoubleIsReqdToStart;

    /**
     * reads in the tables from file
     */
    // @Test
    public void loadTables() {
        probTableWhenDoubleNotReqdToStart = loadTable("probTableNoDoubleReqd.json");
        probTableWhenDoubleIsReqdToStart = loadTable("probTableDoubleIsReqd.json");
    }

    private double[][] loadTable(String fileName) {
        ClassLoader classLoader = this.getClass().getClassLoader();

        InputStream in = classLoader.getResourceAsStream(fileName);
        // System.out.println("T0");
        if (in == null) {
            in = classLoader.getResourceAsStream("resources/" + fileName);
            // System.out.println("T1");
        }
        if (in == null)
            throw new IllegalArgumentException("Can't find json files");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = null;
        try {
            jsonArray = (JSONArray) parser.parse(reader);
            // probTableWhenDoubleNotReqdToStart =(double[][])parser.parse(reader);
        } catch (IOException | ParseException e) {
            throw new IllegalArgumentException();
        }
        int nRows = jsonArray.size();
        int nCols = ((JSONArray) jsonArray.get(0)).size();
        double[][] table = new double[nRows][nCols];
        int i = 0;
        for (Object row : jsonArray) {
            JSONArray jRow = (JSONArray) row;
            int j = 0;
            for (Object element : jRow) {
                table[i][j] = (double) element;
                j++;
            }
            i++;
        }
        return table;
    }

    // these constants need to match the dimensions of probTable
    public static int tableLow = 70;
    public static int tableHigh = 130;
    public static double interval = 0.01;
    public static double minSkill = ((double) (tableLow + 10)) * interval;
    public static double maxSkill = ((double) (tableHigh - 10)) * interval;


    /**
     * Uses the lookup to infer the skills for each player from the supplied input probabilities
     * 
     * @param probAA prob that A wins a leg given that A throws first
     * @param probAB prob that A wins a leg given that B throws first
     * @param doubleReqdToStartLeg
     * @return p.x is skillA, p.y is skillB
     */
    public static PairOfDoubles inferSkills(double probAA, double probAB, boolean doubleReqdToStartLeg) {
        Point[] curve1 = generateCurve(probAA, false, doubleReqdToStartLeg);
        Point[] curve2 = generateCurve(1 - probAB, true, doubleReqdToStartLeg);
        Point p1 = CurveFunctions.findIntersection(curve1, curve2);
        double probBA = 1 - probAA;
        double probBB = 1 - probAB;
        Point[] curve3 = generateCurve(probBB, false, doubleReqdToStartLeg);
        Point[] curve4 = generateCurve(1 - probBA, true, doubleReqdToStartLeg);
        Point p2 = CurveFunctions.findIntersection(curve3, curve4);
        PairOfDoubles pd = new PairOfDoubles();
        pd.A = ((double) Math.round(10000 * (p1.x + p2.y) / 2)) / 10000;
        pd.B = ((double) Math.round(10000 * (p1.y + p2.x) / 2)) / 10000;

        return pd;
    }

    /**
     * returns the probabilities s of various events for a 501-501 game with playerA starting at the Oche
     * 
     * @param ratingA
     * @param ratingB
     * @param doubleReqdToStartLeg
     * @return
     */
    public static LegProbTablesRow getLegProbs(double ratingA, double ratingB, boolean doubleReqdToStartLeg) {
        // find the four rows that bracket the target ratings
        int rA = (int) (ratingA / interval);
        int rB = (int) (ratingB / interval);
        int rowLL = (rA - tableLow) * (tableHigh - tableLow) + (rB - tableLow);
        int rowLH = rowLL + 1;
        int rowHL = (rA - tableLow + 1) * (tableHigh - tableLow) + (rB - tableLow);
        int rowHH = rowHL + 1;
        double[][] probTable;
        if (doubleReqdToStartLeg)
            probTable = probTableWhenDoubleIsReqdToStart;
        else
            probTable = probTableWhenDoubleNotReqdToStart;
        double pAWins = getProb(probTable, ratingA, ratingB, rowLL, rowLH, rowHL, rowHH, 2);
        double pCheckoutRed = getProb(probTable, ratingA, ratingB, rowLL, rowLH, rowHL, rowHH, 3);
        double pCS170 = getProb(probTable, ratingA, ratingB, rowLL, rowLH, rowHL, rowHH, 4);
        double pCSOver40 = getProb(probTable, ratingA, ratingB, rowLL, rowLH, rowHL, rowHH, 5);
        double pNo180sA0 = getProb(probTable, ratingA, ratingB, rowLL, rowLH, rowHL, rowHH, 6);
        double pno180sA1 = getProb(probTable, ratingA, ratingB, rowLL, rowLH, rowHL, rowHH, 7);
        double pNo180sA2 = getProb(probTable, ratingA, ratingB, rowLL, rowLH, rowHL, rowHH, 8);
        double pNo180sB0 = getProb(probTable, ratingA, ratingB, rowLL, rowLH, rowHL, rowHH, 9);
        double pno180sB1 = getProb(probTable, ratingA, ratingB, rowLL, rowLH, rowHL, rowHH, 10);
        double pNo180sB2 = getProb(probTable, ratingA, ratingB, rowLL, rowLH, rowHL, rowHH, 11);
        double pFirst180IsA = getProb(probTable, ratingA, ratingB, rowLL, rowLH, rowHL, rowHH, 12);
        double pFirst180IsB = getProb(probTable, ratingA, ratingB, rowLL, rowLH, rowHL, rowHH, 13);
        double pFirst180IsNeither = getProb(probTable, ratingA, ratingB, rowLL, rowLH, rowHL, rowHH, 14);
        LegProbTablesRow probs = new LegProbTablesRow(ratingA, ratingB, pAWins, pCheckoutRed, pCS170, pCSOver40,
                        pNo180sA0, pno180sA1, pNo180sA2, pNo180sB0, pno180sB1, pNo180sB2, pFirst180IsA, pFirst180IsB,
                        pFirst180IsNeither);
        return probs;
    }

    /**
     * returns the probability for the element determined by rowIndex by table lookup using linear interpolation;
     * 
     * @param probTable
     * @param x
     * @param y
     * @param rowLL
     * @param rowLH
     * @param rowHL
     * @param rowHH
     * @param rowIndex
     * @return
     */
    private static double getProb(double[][] probTable, double x, double y, int rowLL, int rowLH, int rowHL, int rowHH,
                    int rowIndex) {
        double xL = probTable[rowLL][0];
        double xH = probTable[rowHL][0];
        double yL = probTable[rowLL][1];
        double yH = probTable[rowLH][1];
        double zLL = probTable[rowLL][rowIndex];
        double zLH = probTable[rowLH][rowIndex];
        double zHL = probTable[rowHL][rowIndex];
        double zHH = probTable[rowHH][rowIndex];
        double aL = (zLL - zHL) / (xL - xH);
        double bL = zLL - aL * xL;
        double wL = aL * x + bL;
        double aH = (zLH - zHH) / (xL - xH);
        double bH = zLH - aH * xL;
        double wH = aH * x + bH;
        double aF = (wL - wH) / (yL - yH);
        double bF = wL - aF * yL;
        return aF * y + bF;
    }

    /**
     * generates the set of data points x,y s.that f(x,y) = targetProb, where f() is defined by the table Each row of
     * probTable must contain xi,yi,targetProb as the first three elements
     * 
     * @param targetProb the target we are trying to match
     * @param reverseXY if true then the X and Y components of the array that is returned are swapped
     * @param doubleReqdToStart
     * @return an (M,2) array containing the set of data points that match the targetProb
     */
    public static Point[] generateCurve(double targetProb, boolean reverseXY, boolean doubleReqdToStart) {
        double[][] table;
        if (doubleReqdToStart)
            table = probTableWhenDoubleIsReqdToStart;
        else
            table = probTableWhenDoubleNotReqdToStart;

        return generateCurve(targetProb, table, reverseXY);
    }

    /**
     * this version allows table to be explicitly specified - only needed to support unit testing
     * 
     * @param targetProb
     * @param table
     * @param reverseXY
     * @return
     */
    public static Point[] generateCurve(double targetProb, double[][] table, boolean reverseXY) {
        List<Point> curve = new ArrayList<Point>(100);
        for (int i = 0; i < tableHigh - tableLow; i++)
            for (int j = 0; j < tableHigh - tableLow - 1; j++) {
                int rowL = i * (tableHigh - tableLow) + j;
                int rowH = rowL + 1;
                double x = table[rowL][0];
                double yL = table[rowL][1];
                double pL = table[rowL][2];
                double yH = table[rowH][1];
                double pH = table[rowH][2];
                if (((pH >= targetProb) && (pL <= targetProb)) || (pH <= targetProb) && (pL >= targetProb)) {
                    Point point = getPoint(targetProb, x, yL, yH, pL, pH, reverseXY);
                    curve.add(point);
                }

                rowL = j * (tableHigh - tableLow) + i;
                rowH = (j + 1) * (tableHigh - tableLow) + i;
                double xL = table[rowL][0];
                double y = table[rowL][1];
                pL = table[rowL][2];
                double xH = table[rowH][0];
                pH = table[rowH][2];
                if (((pH >= targetProb) && (pL <= targetProb)) || (pH <= targetProb) && (pL >= targetProb)) {
                    Point point = getPoint(targetProb, y, xL, xH, pL, pH, !reverseXY);
                    curve.add(point);
                }

            }

        //
        // sort the List by x and generate output array
        //
        Collections.sort(curve);
        int len = curve.size();
        Point[] xyCurve = new Point[len];
        int k = 0;
        for (Point point : curve) {
            xyCurve[k] = point;
            k++;
        }
        return xyCurve;
    }

    static boolean comparebyX(Point p1, Point p2) {
        return p1.x >= p2.x;
    }

    private static Point getPoint(double targetProb, double x, double yL, double yH, double pL, double pH,
                    boolean reverseXY) {
        double a, b, yT;
        a = (yH - yL) / (pH - pL);
        b = yH - a * pH;
        yT = a * targetProb + b;
        Point point;
        if (reverseXY)
            point = new Point(yT, x);
        else
            point = new Point(x, yT);
        return point;
    }
}
