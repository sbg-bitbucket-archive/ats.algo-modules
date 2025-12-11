package ats.algo.sport.darts;

import ats.algo.core.common.TeamId;

/**
 * holds the result of a single leg together with the various calculated stats
 * 
 * @author Geoff
 *
 */
public class LegResult {
    public TeamId legWinner;
    public int NDartsThrownA;
    public int NDartsThrownB;
    public Colour checkoutColour;
    public int checkoutScore;
    public int N180sThrownA;
    public int N180sThrownB;
    public TeamId first180InLeg;

    public LegResult() {}

    public LegResult(TeamId legWinner, int nDartsA, int nDartsB, Colour checkoutColour, int checkoutScore, int no180sA,
                    int no180sB, TeamId first180InLeg) {
        this.legWinner = legWinner;
        this.NDartsThrownA = nDartsA;
        this.NDartsThrownB = nDartsB;
        this.checkoutColour = checkoutColour;
        this.checkoutScore = checkoutScore;
        this.N180sThrownA = no180sA;
        this.N180sThrownB = no180sB;
        this.first180InLeg = first180InLeg;
    }
}

