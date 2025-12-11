package ats.algo.sport.darts.mainprograms;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import ats.algo.genericsupportfunctions.ConsoleInput;

/**
 * generates the table of probabilities associated with a single leg
 * 
 * @author Geoff
 * 
 */
public class GenerateLegWinnerTable {

    public static void main(String[] args) {
        int nIterations = ConsoleInput.readInt("# iterations", 1000000, false);
        String fname = "c:\\aatmp\\tableNoDouble.txt";
        writeTable(fname, nIterations, false);
        fname = "c:\\aatmp\\tableDoubleReqd.txt";
        writeTable(fname, nIterations, true);
    }

    static void writeTable(String fName, int nIterations, boolean doubleReqdToStart) {
        /*
         * DartLegEngine e = new DartLegEngine(BaseProbSet.LIVE, false); LegState legScore = new
         * LegState(doubleReqdToStart);
         * 
         * e.setMcCount(nIterations); FileWriter file; try { file = new FileWriter(fName, true); writeLine(
         * "ratingA, ratingB, pAWins, pCheckoutRed, pCS170,pCSOver40,pNo180sA0, pno180sA1,pNo180sA2, pNo180sB0, pno180sB1,pNo180sB2,pFirst180IsA,pFirst180IsB, pFirst180IsNeither"
         * , file); for (double ratingA = 0.7; ratingA <= 1.3; ratingA += 0.01) for (double ratingB = 0.7; ratingB <=
         * 1.3; ratingB += 0.01) { e.setSkillA(ratingA); e.setSkillB(ratingB); legScore.startNewLeg(TeamId.A); LegProbs
         * lp = e.execute(legScore); double pAWins = prob(lp.ProbAWins); double pCheckoutRed = prob(lp.ProbCheckoutRed);
         * double pCS170 = prob(lp.ProbCheckoutScoreIs170); double pCSOver40 = prob(lp.ProbCheckoutScoreOver40); double
         * pNo180sA0 = prob(lp.ProbNo180sA[0]); double pno180sA1 = prob(lp.ProbNo180sA[1]); double pNo180sA2 =
         * prob(lp.ProbNo180sA[2]); double pNo180sB0 = prob(lp.ProbNo180sB[0]); double pno180sB1 =
         * prob(lp.ProbNo180sB[1]); double pNo180sB2 = prob(lp.ProbNo180sB[2]); double pFirst180IsA =
         * prob(lp.ProbFirst180IsA); double pFirst180IsB = prob(lp.ProbFirst180IsB); double pFirst180IsNeither =
         * prob(lp.ProbFirst180IsNeither);
         * 
         * String opStr = String.format("[%f,%f,%f,%f,%f,", ratingA, ratingB, pAWins, pCheckoutRed, pCS170); opStr =
         * opStr + String.format("%f,%f,%f,%f,%f,", pCSOver40, pNo180sA0, pno180sA1, pNo180sA2, pNo180sB0); opStr =
         * opStr + String.format("%f,%f,%f,%f,%f]", pno180sB1, pNo180sB2, pFirst180IsA, pFirst180IsB,
         * pFirst180IsNeither); writeLine(opStr, file); } file.flush(); file.close(); } catch (IOException e1) {
         * System.out.println("can't access: " + fName); e1.printStackTrace(); }
         */
    }

    static void writeLine(String s, FileWriter file) {
        Date date = new Date();
        System.out.println(date.toString() + " " + s);
        try {
            file.write(s + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static double prob(double p) {
        return p;
    }
}
