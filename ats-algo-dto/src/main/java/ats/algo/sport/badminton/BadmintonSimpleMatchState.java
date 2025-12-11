/**
 * 
 */
package ats.algo.sport.badminton;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Maps;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.core.common.TeamId;
import ats.algo.core.matchresult.MatchResultElement;
import ats.algo.core.matchresult.MatchResultEntryType;
import ats.algo.core.matchresult.MatchResultMap;
import ats.algo.genericsupportfunctions.PairOfIntegers;

/**
 * @author Robert
 *
 */
public class BadmintonSimpleMatchState extends SimpleMatchState {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private int gamesA;
    private int gamesB;
    private TeamId serve;
    private TeamId serveInFirstGame;
    private int pointsA;
    private int pointsB;
    private Map<String, PairOfIntegers> gameScoreInGameN;

    /**
     * 
     * @param preMatch
     * @param matchCompleted
     * @param gamesA
     * @param gamesB
     * @param serve
     * @param serveInFirstGame
     * @param pointsA
     * @param pointsB
     */
    @JsonCreator
    public BadmintonSimpleMatchState(@JsonProperty("preMatch") boolean preMatch,
                    @JsonProperty("matchCompleted") boolean matchCompleted, @JsonProperty("gamesA") int gamesA,
                    @JsonProperty("gamesB") int gamesB, @JsonProperty("pointsA") int pointsA,
                    @JsonProperty("pointsB") int pointsB, @JsonProperty("serve") TeamId serve,
                    @JsonProperty("serveInFirstGame") TeamId serveInFirstGame,
                    @JsonProperty("gameScoreInGameN") Map<String, PairOfIntegers> gameScoreInGameN) {
        super(preMatch, matchCompleted);

        this.gamesA = gamesA;
        this.gamesB = gamesB;
        this.pointsA = pointsA;
        this.pointsB = pointsB;
        this.serve = serve;
        this.serveInFirstGame = serveInFirstGame;
        this.gameScoreInGameN = gameScoreInGameN;
    }

    public BadmintonSimpleMatchState() {
        super();
        serve = TeamId.UNKNOWN;
        serveInFirstGame = TeamId.UNKNOWN;
        gameScoreInGameN = Maps.newHashMap();
    }

    public void setGamesA(int gamesA) {
        this.gamesA = gamesA;
    }

    public void setGamesB(int gamesB) {
        this.gamesB = gamesB;
    }

    public void setServe(TeamId serve) {
        this.serve = serve;
    }

    public void setServeInFirstGame(TeamId serveInFirstGame) {
        this.serveInFirstGame = serveInFirstGame;
    }

    public void setPointsA(int pointsA) {
        this.pointsA = pointsA;
    }

    public void setPointsB(int pointsB) {
        this.pointsB = pointsB;
    }

    public int getGamesA() {
        return gamesA;
    }

    public int getGamesB() {
        return gamesB;
    }

    public TeamId getServe() {
        return serve;
    }

    public TeamId getServeInFirstGame() {
        return serveInFirstGame;
    }

    public int getPointsA() {
        return pointsA;
    }

    public int getPointsB() {
        return pointsB;
    }

    public Map<String, PairOfIntegers> getGameScoreInGameN() {
        return gameScoreInGameN;
    }

    public void setGameScoreInGameN(Map<String, PairOfIntegers> gameScoreInGameN) {
        this.gameScoreInGameN = gameScoreInGameN;
    }

    @Override
    public MatchResultMap generateResultMapFromSimpleMatchState(MatchFormat format) {
        MatchResultMap resultMap = new MatchResultMap();

        int nSets = getGameScoreInGameN().size();
        PairOfIntegers[] setScore = new PairOfIntegers[nSets];
        int j = 0;
        for (PairOfIntegers scores : getGameScoreInGameN().values()) {
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
    public BadmintonSimpleMatchState generateMatchStateFromMatchResultMap(MatchResultMap result,
                    MatchFormat matchFormat) {
        BadmintonMatchState bms = new BadmintonMatchState(matchFormat);
        BadmintonMatchFormat badmintonMatchFormat = (BadmintonMatchFormat) matchFormat;

        Map<String, MatchResultElement> map = result.getMap();
        int nSets = badmintonMatchFormat.getnGamesInMatch();
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
            if (map.get("set3Score") != null)
                setScore[2] = map.get("set3Score").valueAsPairOfIntegers();
            else
                nSets--;
        }

        int gamesA = 0;
        int gamesB = 0;

        for (int i = 0; i < nSets; i++) {
            int nA = setScore[i].A;
            int nB = setScore[i].B;
            bms.setPointScoreInGameN(i, nA, nB);
            if (nA > nB)
                gamesA++;
            else
                gamesB++;
        }
        bms.setGamesA(gamesA);
        bms.setGamesB(gamesB);

        BadmintonSimpleMatchState sms = bms.generateSimpleMatchState();
        return sms;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((gameScoreInGameN == null) ? 0 : gameScoreInGameN.hashCode());
        result = prime * result + gamesA;
        result = prime * result + gamesB;
        result = prime * result + pointsA;
        result = prime * result + pointsB;
        result = prime * result + ((serve == null) ? 0 : serve.hashCode());
        result = prime * result + ((serveInFirstGame == null) ? 0 : serveInFirstGame.hashCode());
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
        BadmintonSimpleMatchState other = (BadmintonSimpleMatchState) obj;
        if (gameScoreInGameN == null) {
            if (other.gameScoreInGameN != null)
                return false;
        } else if (!gameScoreInGameN.equals(other.gameScoreInGameN))
            return false;
        if (gamesA != other.gamesA)
            return false;
        if (gamesB != other.gamesB)
            return false;
        if (pointsA != other.pointsA)
            return false;
        if (pointsB != other.pointsB)
            return false;
        if (serve != other.serve)
            return false;
        if (serveInFirstGame != other.serveInFirstGame)
            return false;
        return true;
    }
}
