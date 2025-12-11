package ats.algo.sport.darts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.core.common.TeamId;

public class DartSimpleMatchState extends SimpleMatchState {

    private static final long serialVersionUID = 1L;
    private int setsA;
    private int setsB;
    private int legsA;
    private int legsB;
    private int currentLegPointsA;
    private int currentLegPointsB;
    private TeamId playerAtOcheAtStartOfLeg;
    private TeamId playerAtOcheNow;
    private int currentThreeDartScore;
    private int n180SoFarA;
    private int n180SoFarB;
    private TeamId first180InCurrentLeg;
    private int noOfDartsThrownInCurrentLegA;
    private int noOfDartsThrownInCurrentLegB;

    @JsonCreator
    public DartSimpleMatchState(@JsonProperty("preMatch") boolean preMatch,
                    @JsonProperty("matchCompleted") boolean matchCompleted, @JsonProperty("setsA") int setsA,
                    @JsonProperty("setsB") int setsB, @JsonProperty("legsA") int legsA,
                    @JsonProperty("legsB") int legsB, @JsonProperty("currentLegPointsA") int currentLegPointsA,
                    @JsonProperty("currentLegPointsB") int currentLegPointsB,
                    @JsonProperty("n180SoFarA") int n180SoFarA, @JsonProperty("n180SoFarB") int n180SoFarB,
                    @JsonProperty("noOfDartsThrownInCurrentLegA") int noOfDartsThrownInCurrentLegA,
                    @JsonProperty("noOfDartsThrownInCurrentLegB") int noOfDartsThrownInCurrentLegB,
                    @JsonProperty("playerAtOcheAtStartOfLeg") TeamId playerAtOcheAtStartOfLeg,
                    @JsonProperty("playerAtOcheNow") TeamId playerAtOcheNow,
                    @JsonProperty("first180InCurrentLeg") TeamId first180InCurrentLeg)

    {
        super(preMatch, matchCompleted);
        this.setsA = setsA;
        this.setsB = setsB;
        this.legsA = legsA;
        this.legsB = legsB;
        this.currentLegPointsA = currentLegPointsA;
        this.currentLegPointsB = currentLegPointsB;
        this.n180SoFarA = n180SoFarA;
        this.n180SoFarB = n180SoFarB;
        this.noOfDartsThrownInCurrentLegA = noOfDartsThrownInCurrentLegA;
        this.noOfDartsThrownInCurrentLegB = noOfDartsThrownInCurrentLegB;
        this.playerAtOcheAtStartOfLeg = playerAtOcheAtStartOfLeg;
        this.playerAtOcheNow = playerAtOcheNow;
        this.first180InCurrentLeg = first180InCurrentLeg;

    }

    public int getSetsA() {
        return setsA;
    }

    public int getSetsB() {
        return setsB;
    }

    public int getLegsA() {
        return legsA;
    }

    public int getLegsB() {
        return legsB;
    }

    public int getCurrentLegPointsA() {
        return currentLegPointsA;
    }

    public int getCurrentLegPointsB() {
        return currentLegPointsB;
    }

    public TeamId getPlayerAtOcheAtStartOfLeg() {
        return playerAtOcheAtStartOfLeg;
    }

    public TeamId getPlayerAtOcheNow() {
        return playerAtOcheNow;
    }

    public int getCurrentThreeDartScore() {
        return currentThreeDartScore;
    }

    public int getN180SoFarA() {
        return n180SoFarA;
    }

    public int getN180SoFarB() {
        return n180SoFarB;
    }

    public TeamId getFirst180InCurrentLeg() {
        return first180InCurrentLeg;
    }

    public int getNoOfDartsThrownInCurrentLegA() {
        return noOfDartsThrownInCurrentLegA;
    }

    public int getNoOfDartsThrownInCurrentLegB() {
        return noOfDartsThrownInCurrentLegB;
    }

    public DartSimpleMatchState() {
        super();
        this.setsA = 0;
        this.setsB = 0;
        this.legsA = 0;
        this.legsB = 0;
        this.currentLegPointsA = 501;
        this.currentLegPointsB = 501;
        this.n180SoFarA = 0;
        this.n180SoFarB = 0;
        this.noOfDartsThrownInCurrentLegA = 0;
        this.noOfDartsThrownInCurrentLegB = 0;
        this.playerAtOcheAtStartOfLeg = TeamId.UNKNOWN;
        this.playerAtOcheNow = TeamId.UNKNOWN;
        this.first180InCurrentLeg = TeamId.UNKNOWN;
    }

    public void setSetsA(int setsA) {
        this.setsA = setsA;
    }

    public void setSetsB(int setsB) {
        this.setsB = setsB;
    }

    public void setLegsA(int legsA) {
        this.legsA = legsA;
    }

    public void setLegsB(int legsB) {
        this.legsB = legsB;
    }

    public void setCurrentLegPointsA(int currentLegPointsA) {
        this.currentLegPointsA = currentLegPointsA;
    }

    public void setCurrentLegPointsB(int currentLegPointsB) {
        this.currentLegPointsB = currentLegPointsB;
    }

    public void setPlayerAtOcheAtStartOfLeg(TeamId playerAtOcheAtStartOfLeg) {
        this.playerAtOcheAtStartOfLeg = playerAtOcheAtStartOfLeg;
    }

    public void setPlayerAtOcheNow(TeamId playerAtOcheNow) {
        this.playerAtOcheNow = playerAtOcheNow;
    }

    public void setCurrentThreeDartScore(int currentThreeDartScore) {
        this.currentThreeDartScore = currentThreeDartScore;
    }

    public void setN180SoFarA(int n180SoFarA) {
        this.n180SoFarA = n180SoFarA;
    }

    public void setN180SoFarB(int n180SoFarB) {
        this.n180SoFarB = n180SoFarB;
    }

    public void setFirst180InCurrentLeg(TeamId first180InCurrentLeg) {
        this.first180InCurrentLeg = first180InCurrentLeg;
    }

    public void setNoOfDartsThrownInCurrentLegA(int noOfDartsThrownInCurrentLegA) {
        this.noOfDartsThrownInCurrentLegA = noOfDartsThrownInCurrentLegA;
    }

    public void setNoOfDartsThrownInCurrentLegB(int noOfDartsThrownInCurrentLegB) {
        this.noOfDartsThrownInCurrentLegB = noOfDartsThrownInCurrentLegB;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + currentLegPointsA;
        result = prime * result + currentLegPointsB;
        result = prime * result + currentThreeDartScore;
        result = prime * result + ((first180InCurrentLeg == null) ? 0 : first180InCurrentLeg.hashCode());
        result = prime * result + legsA;
        result = prime * result + legsB;
        result = prime * result + n180SoFarA;
        result = prime * result + n180SoFarB;
        result = prime * result + noOfDartsThrownInCurrentLegA;
        result = prime * result + noOfDartsThrownInCurrentLegB;
        result = prime * result + ((playerAtOcheAtStartOfLeg == null) ? 0 : playerAtOcheAtStartOfLeg.hashCode());
        result = prime * result + ((playerAtOcheNow == null) ? 0 : playerAtOcheNow.hashCode());
        result = prime * result + setsA;
        result = prime * result + setsB;
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
        DartSimpleMatchState other = (DartSimpleMatchState) obj;
        if (currentLegPointsA != other.currentLegPointsA)
            return false;
        if (currentLegPointsB != other.currentLegPointsB)
            return false;
        if (currentThreeDartScore != other.currentThreeDartScore)
            return false;
        if (first180InCurrentLeg != other.first180InCurrentLeg)
            return false;
        if (legsA != other.legsA)
            return false;
        if (legsB != other.legsB)
            return false;
        if (n180SoFarA != other.n180SoFarA)
            return false;
        if (n180SoFarB != other.n180SoFarB)
            return false;
        if (noOfDartsThrownInCurrentLegA != other.noOfDartsThrownInCurrentLegA)
            return false;
        if (noOfDartsThrownInCurrentLegB != other.noOfDartsThrownInCurrentLegB)
            return false;
        if (playerAtOcheAtStartOfLeg != other.playerAtOcheAtStartOfLeg)
            return false;
        if (playerAtOcheNow != other.playerAtOcheNow)
            return false;
        if (setsA != other.setsA)
            return false;
        if (setsB != other.setsB)
            return false;
        return true;
    }

}
