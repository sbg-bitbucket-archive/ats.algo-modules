package ats.algo.sport.darts;

public class LegProbTablesRow {
    public double ProbAWins;
    public double ProbCheckoutRed;
    public double ProbCS170;
    public double ProbCSOver40;
    public double ProbNo180sA0;
    public double ProbNo180sA1;
    public double ProbNo180sA2;
    public double ProbNo180sB0;
    public double ProbNo180sB1;
    public double ProbNo180sB2;
    public double ProbFirst180IsA;
    public double ProbFirst180IsB;
    public double ProbFirst180IsNeither;

    public LegProbTablesRow(double ratingA, double ratingB, double pAWins, double pCheckoutRed, double pCS170,
                    double pCSOver40, double pNo180sA0, double pNo180sA1, double pNo180sA2, double pNo180sB0,
                    double pNo180sB1, double pNo180sB2, double pFirst180IsA, double pFirst180IsB,
                    double pFirst180IsNeither) {
        this.ProbAWins = pAWins;
        this.ProbCheckoutRed = pCheckoutRed;
        this.ProbCS170 = pCS170;
        this.ProbCSOver40 = pCSOver40;
        this.ProbNo180sA0 = pNo180sA0;
        this.ProbNo180sA1 = pNo180sA1;
        this.ProbNo180sA2 = pNo180sA2;
        this.ProbNo180sB0 = pNo180sB0;
        this.ProbNo180sB1 = pNo180sB1;
        this.ProbNo180sB2 = pNo180sB2;
        this.ProbFirst180IsA = pFirst180IsA;
        this.ProbFirst180IsB = pFirst180IsB;
        this.ProbFirst180IsNeither = pFirst180IsNeither;
    }

    /**
     * Switches the probabilities for playerA to those for playerB and vice versa;
     * 
     */
    public void switchProbsAToB() {
        ProbAWins = 1 - ProbAWins;
        double z;

        z = ProbNo180sA0;
        ProbNo180sA0 = ProbNo180sB0;
        ProbNo180sB0 = z;

        z = ProbNo180sA1;
        ProbNo180sA1 = ProbNo180sB1;
        ProbNo180sB1 = z;

        z = ProbNo180sA2;
        ProbNo180sA2 = ProbNo180sB2;
        ProbNo180sB2 = z;

        z = ProbFirst180IsA;
        ProbFirst180IsA = ProbFirst180IsB;
        ProbFirst180IsB = z;
    }
}
