package ats.algo.sport.outrights.calcengine.leagues;

import ats.algo.sport.outrights.CompetitionType;
import ats.algo.sport.outrights.calcengine.core.AbstractFormat;

public class LeagueFormat extends AbstractFormat {


    private static final long serialVersionUID = 1L;

    CompetitionType competitionType;
    int nPromotedUnconditionally;
    int nPromotedViaPlayoff;
    int nTeamsInPlayoff;
    int nRelegated;


    /**
     * 
     * @param competitionType
     * @param nPromotedUnconditionally
     * @param nPromotedViaPlayoff
     * @param nTeamsInPlayoff
     * @param nRelegated
     */
    public LeagueFormat(CompetitionType competitionType, int nPromotedUnconditionally, int nPromotedViaPlayoff,
                    int nTeamsInPlayoff, int nRelegated) {
        super(competitionType);
        this.nPromotedUnconditionally = nPromotedUnconditionally;
        this.nPromotedViaPlayoff = nPromotedViaPlayoff;
        this.nTeamsInPlayoff = nTeamsInPlayoff;
        this.nRelegated = nRelegated;
    }

    public CompetitionType getCompetitionType() {
        return competitionType;
    }

    public void setCompetitionType(CompetitionType competitionType) {
        this.competitionType = competitionType;
    }

    public int getnPromotedUnconditionally() {
        return nPromotedUnconditionally;
    }

    public void setnPromotedUnconditionally(int nPromotedUnconditionally) {
        this.nPromotedUnconditionally = nPromotedUnconditionally;
    }

    public int getnPromotedViaPlayoff() {
        return nPromotedViaPlayoff;
    }

    public void setnPromotedViaPlayoff(int nPromotedViaPlayoff) {
        this.nPromotedViaPlayoff = nPromotedViaPlayoff;
    }

    public int getnTeamsInPlayoff() {
        return nTeamsInPlayoff;
    }

    public void setnTeamsInPlayoff(int nTeamsInPlayoff) {
        this.nTeamsInPlayoff = nTeamsInPlayoff;
    }

    public int getnRelegated() {
        return nRelegated;
    }

    public void setnRelegated(int nRelegated) {
        this.nRelegated = nRelegated;
    }

}
