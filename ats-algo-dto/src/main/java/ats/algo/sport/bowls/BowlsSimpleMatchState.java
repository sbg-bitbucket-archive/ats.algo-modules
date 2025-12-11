package ats.algo.sport.bowls;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.PairOfIntegers;

public class BowlsSimpleMatchState extends SimpleMatchState {

    private static final long serialVersionUID = 1L;
    private double setsA;
    private double setsB;
    private int currentEnd;
    private boolean setRace;
    private TeamId serve;
    private TeamId serveIncurrentEnd;
    private int bowlsInEnd;
    private int endScoreForWin;
    private int endsInFinalSet;
    private double setScoreForWin;
    private int setScoreForDraw;
    // private PairOfIntegers[] gameScoreInSetN;
    private int pointsA;
    private int pointsB;
    private boolean extraEnd;
    private boolean finalSet;

    private Map<String, PairOfIntegers> gameScoreInSetN = new LinkedHashMap<String, PairOfIntegers>();


    @JsonCreator
    public BowlsSimpleMatchState(@JsonProperty("preMatch") boolean preMatch,
                    @JsonProperty("matchCompleted") boolean matchCompleted, @JsonProperty("setsA") double setsA,
                    @JsonProperty("setsB") double setsB, @JsonProperty("currentEnd") int currentEnd,
                    @JsonProperty("setRace") boolean setRace, @JsonProperty("serve") TeamId serve,
                    @JsonProperty("serveIncurrentEnd") TeamId serveIncurrentEnd,
                    @JsonProperty("bowlsInEnd") int bowlsInEnd, @JsonProperty("endScoreForWin") int endScoreForWin,
                    @JsonProperty("endsInFinalSet") int endsInFinalSet,
                    @JsonProperty("setScoreForWin") double setScoreForWin,
                    @JsonProperty("setScoreForDraw") int setScoreForDraw, @JsonProperty("pointsA") int pointsA,
                    @JsonProperty("pointsB") int pointsB, @JsonProperty("extraEnd") boolean extraEnd,
                    @JsonProperty("finalSet") boolean finalSet,
                    @JsonProperty("gameScoreInSetN") Map<String, PairOfIntegers> gameScoreInSetN) {
        super(preMatch, matchCompleted);
        this.setsA = setsA;
        this.setsB = setsB;
        this.currentEnd = currentEnd;
        this.setRace = setRace;
        this.serve = serve;
        this.serveIncurrentEnd = serveIncurrentEnd;
        this.bowlsInEnd = bowlsInEnd;
        this.endScoreForWin = endScoreForWin;
        this.endsInFinalSet = endsInFinalSet;
        this.setScoreForWin = setScoreForWin;
        this.setScoreForDraw = setScoreForDraw;
        this.pointsA = pointsA;
        this.pointsB = pointsB;
        this.extraEnd = extraEnd;
        this.finalSet = finalSet;
        this.gameScoreInSetN = gameScoreInSetN;
    }



    public double getSetsA() {
        return setsA;
    }



    public void setSetsA(double setsA) {
        this.setsA = setsA;
    }



    public double getSetsB() {
        return setsB;
    }



    public void setSetsB(double setsB) {
        this.setsB = setsB;
    }



    public int getCurrentEnd() {
        return currentEnd;
    }



    public void setCurrentEnd(int currentEnd) {
        this.currentEnd = currentEnd;
    }



    public boolean isSetRace() {
        return setRace;
    }



    public void setSetRace(boolean setRace) {
        this.setRace = setRace;
    }



    public TeamId getServe() {
        return serve;
    }



    public void setServe(TeamId serve) {
        this.serve = serve;
    }



    public TeamId getServeIncurrentEnd() {
        return serveIncurrentEnd;
    }



    public void setServeIncurrentEnd(TeamId serveIncurrentEnd) {
        this.serveIncurrentEnd = serveIncurrentEnd;
    }



    public int getBowlsInEnd() {
        return bowlsInEnd;
    }



    public void setBowlsInEnd(int bowlsInEnd) {
        this.bowlsInEnd = bowlsInEnd;
    }



    public int getEndScoreForWin() {
        return endScoreForWin;
    }



    public void setEndScoreForWin(int endScoreForWin) {
        this.endScoreForWin = endScoreForWin;
    }



    public int getEndsInFinalSet() {
        return endsInFinalSet;
    }



    public void setEndsInFinalSet(int endsInFinalSet) {
        this.endsInFinalSet = endsInFinalSet;
    }



    public double getSetScoreForWin() {
        return setScoreForWin;
    }



    public void setSetScoreForWin(double setScoreForWin) {
        this.setScoreForWin = setScoreForWin;
    }



    public int getSetScoreForDraw() {
        return setScoreForDraw;
    }



    public void setSetScoreForDraw(int setScoreForDraw) {
        this.setScoreForDraw = setScoreForDraw;
    }



    public int getPointsA() {
        return pointsA;
    }



    public void setPointsA(int pointsA) {
        this.pointsA = pointsA;
    }



    public int getPointsB() {
        return pointsB;
    }



    public void setPointsB(int pointsB) {
        this.pointsB = pointsB;
    }



    public boolean isExtraEnd() {
        return extraEnd;
    }



    public void setExtraEnd(boolean extraEnd) {
        this.extraEnd = extraEnd;
    }



    public boolean isFinalSet() {
        return finalSet;
    }



    public void setFinalSet(boolean finalSet) {
        this.finalSet = finalSet;
    }



    public Map<String, PairOfIntegers> getGameScoreInSetN() {
        return gameScoreInSetN;
    }



    public void setGameScoreInSetN(Map<String, PairOfIntegers> gameScoreInSetN) {
        this.gameScoreInSetN = gameScoreInSetN;
    }



    public BowlsSimpleMatchState() {
        super();
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + bowlsInEnd;
        result = prime * result + currentEnd;
        result = prime * result + endScoreForWin;
        result = prime * result + endsInFinalSet;
        result = prime * result + (extraEnd ? 1231 : 1237);
        result = prime * result + (finalSet ? 1231 : 1237);
        result = prime * result + ((gameScoreInSetN == null) ? 0 : gameScoreInSetN.hashCode());
        result = prime * result + pointsA;
        result = prime * result + pointsB;
        result = prime * result + ((serve == null) ? 0 : serve.hashCode());
        result = prime * result + ((serveIncurrentEnd == null) ? 0 : serveIncurrentEnd.hashCode());
        result = prime * result + (setRace ? 1231 : 1237);
        result = prime * result + setScoreForDraw;
        long temp;
        temp = Double.doubleToLongBits(setScoreForWin);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(setsA);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(setsB);
        result = prime * result + (int) (temp ^ (temp >>> 32));
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
        BowlsSimpleMatchState other = (BowlsSimpleMatchState) obj;
        if (bowlsInEnd != other.bowlsInEnd)
            return false;
        if (currentEnd != other.currentEnd)
            return false;
        if (endScoreForWin != other.endScoreForWin)
            return false;
        if (endsInFinalSet != other.endsInFinalSet)
            return false;
        if (extraEnd != other.extraEnd)
            return false;
        if (finalSet != other.finalSet)
            return false;
        if (gameScoreInSetN == null) {
            if (other.gameScoreInSetN != null)
                return false;
        } else if (!gameScoreInSetN.equals(other.gameScoreInSetN))
            return false;
        if (pointsA != other.pointsA)
            return false;
        if (pointsB != other.pointsB)
            return false;
        if (serve != other.serve)
            return false;
        if (serveIncurrentEnd != other.serveIncurrentEnd)
            return false;
        if (setRace != other.setRace)
            return false;
        if (setScoreForDraw != other.setScoreForDraw)
            return false;
        if (Double.doubleToLongBits(setScoreForWin) != Double.doubleToLongBits(other.setScoreForWin))
            return false;
        if (Double.doubleToLongBits(setsA) != Double.doubleToLongBits(other.setsA))
            return false;
        if (Double.doubleToLongBits(setsB) != Double.doubleToLongBits(other.setsB))
            return false;
        return true;
    }



}


