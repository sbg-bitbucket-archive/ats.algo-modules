package ats.algo.sport.tennis;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.core.common.TeamId;
import ats.algo.core.matchresult.MatchResultElement;
import ats.algo.core.matchresult.MatchResultEntryType;
import ats.algo.core.matchresult.MatchResultMap;
import ats.algo.genericsupportfunctions.PairOfIntegers;

@JsonPropertyOrder({"preMatch", "matchCompleted", "setsA", "setsB", "gamesA", "gamesB", "pointsA", "pointsB",
        "onServeNow", "currentSetNo", "tieBreak", "superTieBreak", "setScores"})
public class TennisSimpleMatchState extends SimpleMatchState {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private int setsA;
    private int setsB;
    private int gamesA;
    private int gamesB;
    private int pointsA;
    private int pointsB;
    private TeamId onServeNow;
    private int currentSetNo;
    private boolean tieBreak;
    private boolean superTieBreak;
    private Map<String, PairOfIntegers> setScores = new LinkedHashMap<String, PairOfIntegers>();

    @JsonCreator
    public TennisSimpleMatchState(@JsonProperty("preMatch") boolean preMatch,
                    @JsonProperty("matchCompleted") boolean matchCompleted, @JsonProperty("setsA") int setsA,
                    @JsonProperty("setsB") int setsB, @JsonProperty("gamesA") int gamesA,
                    @JsonProperty("gamesB") int gamesB, @JsonProperty("pointsA") int pointsA,
                    @JsonProperty("pointsB") int pointsB, @JsonProperty("onServeNow") TeamId onServeNow,
                    @JsonProperty("currentSetNo") int currentSetNo, @JsonProperty("inTieBreak") boolean tieBreak,
                    @JsonProperty("inSuperTieBreak") boolean superTieBreak,
                    @JsonProperty("setScores") Map<String, PairOfIntegers> setScores) {
        super(preMatch, matchCompleted);
        this.setsA = setsA;
        this.setsB = setsB;
        this.gamesA = gamesA;
        this.gamesB = gamesB;
        this.pointsA = pointsA;
        this.pointsB = pointsB;
        this.currentSetNo = currentSetNo;
        this.onServeNow = onServeNow;
        this.tieBreak = tieBreak;
        this.superTieBreak = superTieBreak;
        this.setScores = setScores;
    }

    public TennisSimpleMatchState() {
        super();
        onServeNow = TeamId.UNKNOWN;
    }

    public void setSetsA(int setsA) {
        this.setsA = setsA;
    }

    public void setSetsB(int setsB) {
        this.setsB = setsB;
    }

    public void setGamesA(int gamesA) {
        this.gamesA = gamesA;
    }

    public void setGamesB(int gamesB) {
        this.gamesB = gamesB;
    }

    public void setPointsA(int pointsA) {
        this.pointsA = pointsA;
    }

    public void setPointsB(int pointsB) {
        this.pointsB = pointsB;
    }

    public void setOnServeNow(TeamId onServeNow) {
        this.onServeNow = onServeNow;
    }

    public void setCurrentSetNo(int currentSetNo) {
        this.currentSetNo = currentSetNo;
    }

    public void setTieBreak(boolean tieBreak) {
        this.tieBreak = tieBreak;
    }

    public void setSuperTieBreak(boolean superTieBreak) {
        this.superTieBreak = superTieBreak;
    }

    public void setSetScores(Map<String, PairOfIntegers> setScores) {
        this.setScores = setScores;
    }

    public int getSetsA() {
        return setsA;
    }

    public int getSetsB() {
        return setsB;
    }

    public int getGamesA() {
        return gamesA;
    }

    public int getGamesB() {
        return gamesB;
    }

    public int getPointsA() {
        return pointsA;
    }

    public int getPointsB() {
        return pointsB;
    }

    public TeamId getOnServeNow() {
        return onServeNow;
    }

    public int getCurrentSetNo() {
        return currentSetNo;
    }

    public boolean isTieBreak() {
        return tieBreak;
    }

    public boolean isSuperTieBreak() {
        return superTieBreak;
    }

    public Map<String, PairOfIntegers> getSetScores() {
        return setScores;
    }

    @Override
    public MatchResultMap generateResultMapFromSimpleMatchState(MatchFormat format) {
        MatchResultMap resultMap = new MatchResultMap();

        int nSets = getSetScores().size();
        PairOfIntegers[] setScore = new PairOfIntegers[nSets];
        int j = 0;
        for (PairOfIntegers scores : getSetScores().values()) {
            setScore[j] = scores;
            j++;
        }

        for (int i = 0; i < setScore.length; i++) {
            if (setScore[i].A > (-1) && setScore[i].B > (-1)) {
                String scoreA = "";
                String scoreB = "";
                switch (i + 1) {
                    case 1:
                        scoreA = Integer.toString(setScore[i].A);
                        scoreB = Integer.toString(setScore[i].B);
                        resultMap.put("set1Score", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50,
                                        scoreA + "-" + scoreB));
                        break;
                    case 2:
                        scoreA = Integer.toString(setScore[i].A);
                        scoreB = Integer.toString(setScore[i].B);
                        resultMap.put("set2Score", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50,
                                        scoreA + "-" + scoreB));
                        break;
                    case 3:
                        scoreA = Integer.toString(setScore[i].A);
                        scoreB = Integer.toString(setScore[i].B);
                        resultMap.put("set3Score", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50,
                                        scoreA + "-" + scoreB));
                        break;
                    case 4:
                        scoreA = Integer.toString(setScore[i].A);
                        scoreB = Integer.toString(setScore[i].B);
                        resultMap.put("set4Score", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50,
                                        scoreA + "-" + scoreB));
                        break;
                    case 5:
                        scoreA = Integer.toString(setScore[i].A);
                        scoreB = Integer.toString(setScore[i].B);
                        resultMap.put("set5Score", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50,
                                        scoreA + "-" + scoreB));
                        break;
                }
            }
        }

        return resultMap;
    }

    @Override
    public TennisSimpleMatchState generateMatchStateFromMatchResultMap(MatchResultMap result, MatchFormat matchFormat) {
        TennisMatchState tms = new TennisMatchState(matchFormat);
        TennisMatchFormat tennisMatchFormat = (TennisMatchFormat) matchFormat;

        Map<String, MatchResultElement> map = result.getMap();
        int nSets = tennisMatchFormat.getSetsPerMatch();
        PairOfIntegers[] setScore = new PairOfIntegers[nSets];
        setScore[0] = map.get("set1Score").valueAsPairOfIntegers();
        setScore[1] = map.get("set2Score").valueAsPairOfIntegers();
        if (nSets == 3) {
            if (map.get("set3Score") != null)
                setScore[2] = map.get("set3Score").valueAsPairOfIntegers();
            else
                nSets--;
        }
        if (nSets == 5) {
            if (map.get("set5Score") != null)
                setScore[4] = map.get("set5Score").valueAsPairOfIntegers();
            else
                nSets--;
            if (map.get("set4Score") != null)
                setScore[3] = map.get("set4Score").valueAsPairOfIntegers();
            else
                nSets--;
            setScore[2] = map.get("set3Score").valueAsPairOfIntegers();
        }
        int setsA = 0;
        int setsB = 0;

        for (int i = 0; i < nSets; i++) {
            int nA = setScore[i].A;
            int nB = setScore[i].B;
            tms.setGameScoreInSetN(i, nA, nB);
            if (nA > nB)
                setsA++;
            else
                setsB++;
        }
        tms.setSetsA(setsA);
        tms.setSetsB(setsB);
        tms.setGamesA(0);
        tms.setGamesB(0);

        TennisSimpleMatchState sms = tms.generateSimpleMatchState();
        return sms;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + currentSetNo;
        result = prime * result + gamesA;
        result = prime * result + gamesB;
        result = prime * result + ((onServeNow == null) ? 0 : onServeNow.hashCode());
        result = prime * result + pointsA;
        result = prime * result + pointsB;
        result = prime * result + ((setScores == null) ? 0 : setScores.hashCode());
        result = prime * result + setsA;
        result = prime * result + setsB;
        result = prime * result + (superTieBreak ? 1231 : 1237);
        result = prime * result + (tieBreak ? 1231 : 1237);
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
        TennisSimpleMatchState other = (TennisSimpleMatchState) obj;
        if (currentSetNo != other.currentSetNo)
            return false;
        if (gamesA != other.gamesA)
            return false;
        if (gamesB != other.gamesB)
            return false;
        if (onServeNow != other.onServeNow)
            return false;
        if (pointsA != other.pointsA)
            return false;
        if (pointsB != other.pointsB)
            return false;
        if (setScores == null) {
            if (other.setScores != null)
                return false;
        } else if (!setScores.equals(other.setScores))
            return false;
        if (setsA != other.setsA)
            return false;
        if (setsB != other.setsB)
            return false;
        if (superTieBreak != other.superTieBreak)
            return false;
        if (tieBreak != other.tieBreak)
            return false;
        return true;
    }

}
