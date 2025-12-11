package ats.algo.sport.beachvolleyball;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.core.common.TeamId;
import ats.algo.core.matchresult.MatchResultElement;
import ats.algo.core.matchresult.MatchResultEntryType;
import ats.algo.core.matchresult.MatchResultMap;
import ats.algo.genericsupportfunctions.PairOfIntegers;

public class BeachVolleyballSimpleMatchState extends SimpleMatchState {

    private static final long serialVersionUID = 1L;
    private int setsA;
    private int setsB;
    private int pointsA;
    private int pointsB;
    private TeamId serve;
    private Map<String, PairOfIntegers> setScores = new LinkedHashMap<String, PairOfIntegers>();

    @JsonCreator
    public BeachVolleyballSimpleMatchState(@JsonProperty("preMatch") boolean preMatch,
                    @JsonProperty("matchCompleted") boolean matchCompleted, @JsonProperty("setsA") int setsA,
                    @JsonProperty("setsB") int setsB, @JsonProperty("pointsA") int pointsA,
                    @JsonProperty("pointsB") int pointsB, @JsonProperty("serve") TeamId serve,
                    @JsonProperty("setScores") Map<String, PairOfIntegers> setScores) {
        super(preMatch, matchCompleted);
        this.setsA = setsA;
        this.setsB = setsB;
        this.pointsA = pointsA;
        this.pointsB = pointsB;
        this.setScores = setScores;
        this.serve = serve;
    }

    public BeachVolleyballSimpleMatchState() {
        super();

    }

    public int getSetsA() {
        return setsA;
    }

    public void setSetsA(int setsA) {
        this.setsA = setsA;
    }

    public int getSetsB() {
        return setsB;
    }

    public void setSetsB(int setsB) {
        this.setsB = setsB;
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

    public TeamId getServe() {
        return serve;
    }

    public void setServe(TeamId serve) {
        this.serve = serve;
    }

    public Map<String, PairOfIntegers> getSetScores() {
        return setScores;
    }

    public void setSetScores(Map<String, PairOfIntegers> setScores) {
        this.setScores = setScores;
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
    public BeachVolleyballSimpleMatchState generateMatchStateFromMatchResultMap(MatchResultMap result,
                    MatchFormat matchFormat) {
        BeachVolleyballMatchState bms = new BeachVolleyballMatchState(matchFormat);
        BeachVolleyballMatchFormat badmintonMatchFormat = (BeachVolleyballMatchFormat) matchFormat;

        Map<String, MatchResultElement> map = result.getMap();
        int nSets = badmintonMatchFormat.getnSetsInMatch();
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
        int gamesA = 0;
        int gamesB = 0;

        for (int i = 0; i < nSets; i++) {
            int nA = setScore[i].A;
            int nB = setScore[i].B;
            bms.setGameScoreInSetN(i, nA, nB);
            if (nA > nB)
                gamesA++;
            else
                gamesB++;
        }
        bms.setSetsA(gamesA);
        bms.setSetsB(gamesB);

        BeachVolleyballSimpleMatchState sms = bms.generateSimpleMatchState();
        return sms;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + pointsA;
        result = prime * result + pointsB;
        result = prime * result + ((serve == null) ? 0 : serve.hashCode());
        result = prime * result + ((setScores == null) ? 0 : setScores.hashCode());
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
        BeachVolleyballSimpleMatchState other = (BeachVolleyballSimpleMatchState) obj;
        if (pointsA != other.pointsA)
            return false;
        if (pointsB != other.pointsB)
            return false;
        if (serve != other.serve)
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
        return true;
    }



}

