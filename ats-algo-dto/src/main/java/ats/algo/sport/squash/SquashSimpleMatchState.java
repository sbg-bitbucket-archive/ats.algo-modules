/**
 * 
 */
package ats.algo.sport.squash;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Maps;

import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.PairOfIntegers;

/**
 * @author Robert
 *
 */
public class SquashSimpleMatchState extends SimpleMatchState {

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
    public SquashSimpleMatchState(@JsonProperty("preMatch") boolean preMatch,
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

    public SquashSimpleMatchState() {
        super();
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
        SquashSimpleMatchState other = (SquashSimpleMatchState) obj;
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
