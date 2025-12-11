package ats.algo.genericsupportfunctions;

import java.io.Serializable;

public class Odds implements Serializable {
    private static final long serialVersionUID = 1L;
    public int LineIdentifier;
    public double Price1;
    public double Price2;
    public double Price3;
    public double Prob1;
    public double Prob2;
    public double Prob3;
    public double overround;
    public boolean IsValid;


    /**
     * set odds for a two way market - .e.g match win/lose
     * 
     * @param price1
     * @param price2
     */
    public Odds(double price1, double price2) {

        Price1 = price1;
        Price2 = price2;
        Price3 = 0;
        LineIdentifier = -1;
        IsValid = price1 > 0 && price2 > 0;
        if (IsValid) {
            overround = 1 / Price1 + 1 / Price2 - 1;
            Prob1 = 1 / (Price1 * (1 + overround));
            Prob2 = 1 / (Price2 * (1 + overround));
            Prob3 = 0;
            IsValid = true; // overround >= 0; remove the check on overround
        } else {
            overround = 0;
            Prob1 = 0;
            Prob2 = 0;
            Prob3 = 0;
        }
    }

    /**
     * set odds for a 3-way market - e.g. win/lose/draw
     * 
     * @param price1
     * @param price2
     * @param price3
     */
    public Odds(double price1, double price2, double price3) {
        IsValid = price1 > 0 && price2 > 0 && price3 > 0;
        Price1 = price1;
        Price2 = price2;
        Price3 = price3;
        LineIdentifier = -1;
        IsValid = price1 > 0 && price2 > 0 && price3 > 0;
        if (IsValid) {
            overround = 1 / Price1 + 1 / Price2 + 1 / Price3 - 1;
            Prob1 = 1 / (Price1 * (1 + overround));
            Prob2 = 1 / (Price2 * (1 + overround));
            Prob3 = 1 / (Price3 * (1 + overround));
            IsValid = true; // overround >= 0;
        } else {
            overround = 0;
            Prob1 = 0;
            Prob2 = 0;
            Prob3 = 0;
        }
    }

    /**
     * set odds for a handicap or over/under market
     * 
     * @param lineIdentifier
     * @param price1
     * @param price2
     */
    public Odds(int lineIdentifier, double price1, double price2) {

        Price1 = price1;
        Price2 = price2;
        Price3 = 0;
        this.LineIdentifier = lineIdentifier;
        IsValid = price1 > 0 && price2 > 0;
        if (IsValid) {
            overround = 1 / Price1 + 1 / Price2 - 1;
            Prob1 = 1 / (Price1 * (1 + overround));
            Prob2 = 1 / (Price2 * (1 + overround));
            Prob3 = 0;
            IsValid = true; // overround >= 0;
        } else {
            overround = 0;
            Prob1 = 0;
            Prob2 = 0;
            Prob3 = 0;
        }
    }

    /**
     * set the probabilities directly
     * 
     * @param lineId
     * @param prob1
     * @param prob2
     * @param prob3
     */
    public void setProbs(int lineId, double prob1, double prob2, double prob3) {
        this.IsValid = Math.round((prob1 + prob2 + prob3) * 10000) == 10000;
        this.Prob1 = prob1;
        this.Prob2 = prob2;
        this.Prob3 = prob3;
        this.LineIdentifier = lineId;

    }


}
