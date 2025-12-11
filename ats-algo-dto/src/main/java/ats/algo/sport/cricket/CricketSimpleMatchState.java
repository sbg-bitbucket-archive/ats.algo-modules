package ats.algo.sport.cricket;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.core.common.TeamId;

public class CricketSimpleMatchState extends SimpleMatchState {

    private static final long serialVersionUID = 1L;
    private int oversA;
    private int oversB;
    private int ballsA;
    private int ballsB;
    private int ball;
    private int wicket;
    private int inning;
    private TeamId bat;
    private int runsA;
    private int runsB;
    private int wicketsA;
    private int wicketsB;
    private int extrasA;
    private int extrasB;
    private int extra;

    /**
     * 
     * @param preMatch
     * @param matchCompleted
     * @param ball
     * @param ballsA
     * @param ballsB
     * @param extra
     * @param extrasA
     * @param extrasB
     * @param oversA
     * @param oversB
     * @param runsA
     * @param runsB
     * @param wicket
     * @param wicketsA
     * @param wicketsB
     * @param inning
     * @param bat
     */

    @JsonCreator
    public CricketSimpleMatchState(@JsonProperty("preMatch") boolean preMatch,
                    @JsonProperty("matchCompleted") boolean matchCompleted, @JsonProperty("ball") int ball,
                    @JsonProperty("ballsA") int ballsA, @JsonProperty("ballsB") int ballsB,
                    @JsonProperty("extra") int extra, @JsonProperty("extrasA") int extrasA,
                    @JsonProperty("extrasB") int extrasB, @JsonProperty("oversA") int oversA,
                    @JsonProperty("oversB") int oversB, @JsonProperty("runsA") int runsA,
                    @JsonProperty("runsB") int runsB, @JsonProperty("wicket") int wicket,
                    @JsonProperty("wicketsA") int wicketsA, @JsonProperty("wicketsB") int wicketsB,
                    @JsonProperty("inning") int inning, @JsonProperty("bat") TeamId bat)

    {
        super(preMatch, matchCompleted);

        this.ball = ball;
        this.ballsA = ballsA;
        this.ballsB = ballsB;
        this.bat = bat;
        this.extra = extra;
        this.extrasA = extrasA;
        this.extrasB = extrasB;
        this.oversA = oversA;
        this.oversB = oversB;
        this.runsA = runsA;
        this.runsB = runsB;
        this.wicket = wicket;
        this.wicketsA = wicketsA;
        this.wicketsB = wicketsB;
        this.inning = inning;


        // info = "simpleMatchState not yet implemented for" + this.getClass().getSimpleName();
        // // TODO - replace info property by the properties needed for this sport.
        // // c.f. e.g. TennisSimpleMatchState
    }

    public CricketSimpleMatchState() {
        super();
        bat = TeamId.UNKNOWN;
    }


    public void setOversA(int oversA) {
        this.oversA = oversA;
    }

    public void setOversB(int oversB) {
        this.oversB = oversB;
    }

    public void setBallsA(int ballsA) {
        this.ballsA = ballsA;
    }

    public void setBallsB(int ballsB) {
        this.ballsB = ballsB;
    }

    public void setBall(int ball) {
        this.ball = ball;
    }

    public void setWicket(int wicket) {
        this.wicket = wicket;
    }

    public void setInning(int inning) {
        this.inning = inning;
    }

    public void setBat(TeamId bat) {
        this.bat = bat;
    }

    public void setRunsA(int runsA) {
        this.runsA = runsA;
    }

    public void setRunsB(int runsB) {
        this.runsB = runsB;
    }

    public void setWicketsA(int wicketsA) {
        this.wicketsA = wicketsA;
    }

    public void setWicketsB(int wicketsB) {
        this.wicketsB = wicketsB;
    }

    public void setExtrasA(int extrasA) {
        this.extrasA = extrasA;
    }

    public void setExtrasB(int extrasB) {
        this.extrasB = extrasB;
    }

    public void setExtra(int extra) {
        this.extra = extra;
    }

    public int getInning() {
        return inning;
    }

    public int getWicketsA() {
        return wicketsA;
    }

    public int getWicketsB() {
        return wicketsB;
    }

    public int getWicket() {
        return wicket;
    }

    public int getBall() {
        return ball;
    }

    public int getOversA() {
        return oversA;
    }

    public int getOversB() {
        return oversB;
    }

    public int getBallsA() {
        return ballsA;
    }

    public int getBallsB() {
        return ballsB;
    }

    public int getRunsA() {
        return runsA;
    }

    public int getRunsB() {
        return runsB;
    }

    public TeamId getBat() {
        return bat;
    }

    public int getExtra() {
        return extra;
    }

    public int getExtrasA() {
        return extrasA;
    }

    public int getExtrasB() {
        return extrasB;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ball;
        result = prime * result + ballsA;
        result = prime * result + ballsB;
        result = prime * result + ((bat == null) ? 0 : bat.hashCode());
        result = prime * result + extra;
        result = prime * result + extrasA;
        result = prime * result + extrasB;
        result = prime * result + inning;
        result = prime * result + oversA;
        result = prime * result + oversB;
        result = prime * result + runsA;
        result = prime * result + runsB;
        result = prime * result + wicket;
        result = prime * result + wicketsA;
        result = prime * result + wicketsB;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        CricketSimpleMatchState other = (CricketSimpleMatchState) obj;
        if (ball != other.ball)
            return false;
        if (ballsA != other.ballsA)
            return false;
        if (ballsB != other.ballsB)
            return false;
        if (bat != other.bat)
            return false;
        if (extra != other.extra)
            return false;
        if (extrasA != other.extrasA)
            return false;
        if (extrasB != other.extrasB)
            return false;
        if (inning != other.inning)
            return false;
        if (oversA != other.oversA)
            return false;
        if (oversB != other.oversB)
            return false;
        if (runsA != other.runsA)
            return false;
        if (runsB != other.runsB)
            return false;
        if (wicket != other.wicket)
            return false;
        if (wicketsA != other.wicketsA)
            return false;
        if (wicketsB != other.wicketsB)
            return false;
        return true;
    }
}
