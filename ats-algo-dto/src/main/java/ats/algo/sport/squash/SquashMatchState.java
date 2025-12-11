package ats.algo.sport.squash;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ats.algo.core.GamePeriod;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchIncidentPrompt;
import ats.algo.core.baseclasses.MatchIncidentResult;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.PairOfIntegers;
import ats.algo.sport.squash.SquashMatchIncident.SquashMatchIncidentType;
import ats.algo.sport.squash.SquashMatchIncidentResult.SquashMatchIncidentResultType;

public class SquashMatchState extends MatchState {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SquashMatchFormat matchFormat;
    private int gamesA;
    private int gamesB;
    private TeamId serve;
    private TeamId serveInFirstGame;
    private final int pointScoreForWin;
    private final int gameScoreForWin;
    private final int gameScoreForDraw;
    private PairOfIntegers[] gameScoreInGameN;
    private int pointsA;
    private int pointsB;
    @JsonIgnore
    private SquashMatchIncidentResult currentMatchState; // the state
                                                         // following the
                                                         // most recent

    /**
     * Returns PairOfIntegers[i] to display set point score of both teams
     * 
     * @param i game number
     * @return gameScoreInSetN[i]
     */
    public PairOfIntegers getGameScoreInGameN(int i) {
        return gameScoreInGameN[i];
    }

    /**
     *
     * 
     * @param serve sets who is serve now
     * 
     */
    public void setServe(TeamId serve) {
        this.serve = serve;
    }

    /**
     * 
     * 
     * @param serveInFirstGame sets who is serve in first game
     * 
     */
    public void setServeInFirstGame(TeamId serveInFirstGame) {
        this.serveInFirstGame = serveInFirstGame;
    }

    /**
     * Returns who is serve now
     * 
     * @return serve
     */
    public TeamId getServe() {
        return serve;
    }

    /**
     * Returns who is serve in first game now
     * 
     * @return serveInFirstGame
     */
    public TeamId getServeInFirstGame() {
        return serveInFirstGame;
    }

    /**
     * Returns how many points to win in normal set
     * 
     * @return pointScoreForWin
     */
    public int getPointScoreForWin() {
        return pointScoreForWin;
    }

    /**
     * Returns PairOfIntegers to display all the set point scores of both teams
     * 
     * @return gameScoreInGameN
     */
    public PairOfIntegers[] getGameScoreInGameN() {
        return gameScoreInGameN;
    }

    /**
     * set PairOfIntegers
     * 
     * @param gameScoreInGameN the pair of integers to record point score in each set
     * 
     */
    public void setGameScoreInGameN(PairOfIntegers[] gameScoreInGameN) {
        this.gameScoreInGameN = gameScoreInGameN;
    }

    /**
     * Returns how many games in match
     * 
     * @return gameScoreForWin
     */
    public int getGameScoreForWin() {
        return gameScoreForWin;
    }

    /**
     * Returns how many points to draw in normal set
     * 
     * @return gameScoreForDraw
     */
    public int getGameScoreForDraw() {
        return gameScoreForDraw;
    }

    /**
     *
     * 
     * @param pointsA Sets the Team A point score
     * 
     */
    public void setPointsA(int pointsA) {
        this.pointsA = pointsA;
    }

    /**
     *
     * 
     * @param pointsB Sets pointsB
     */
    public void setPointsB(int pointsB) {
        this.pointsB = pointsB;
    }

    /**
     * Returns team A point score
     * 
     * @return pointsA
     */
    public int getPointsA() {
        return pointsA;
    }

    /**
     * Returns team B point score
     * 
     * @return pointsB
     */
    public int getPointsB() {
        return pointsB;
    }

    /**
     * Returns team A game score
     * 
     * @return gamesA
     */
    public int getGamesA() {
        return gamesA;
    }

    /**
     * Returns team B game score
     * 
     * @return gamesB
     */
    public int getGamesB() {
        return gamesB;
    }

    /**
     * 
     * 
     * @param gamesA Sets gamesA
     * 
     */
    public void setGamesA(int gamesA) {
        this.gamesA = gamesA;
    }

    /**
     * 
     * 
     * @param gamesB Sets gamesB
     * 
     */
    public void setGamesB(int gamesB) {
        this.gamesB = gamesB;
    }

    /**
     * sets the starting score. Starting points assumed to be zero
     * 
     * @param gamesA Sets gamesA
     * @param gamesB Sets gamesB
     * @param pointA Sets pointA
     * @param pointB Sets pointB
     * @param onServeNow may be A,B or unknown
     */
    public void setScore(int gamesA, int gamesB, int pointA, int pointB, TeamId onServeNow) {
        this.gamesA = gamesA;
        this.gamesB = gamesB;
        this.pointsA = pointA;
        this.pointsB = pointB;
        this.serve = onServeNow;

    }

    /**
     * Returns the current match state as squash match incident result
     * 
     * @return currentMatchState
     */
    protected SquashMatchIncidentResult getCurrentMatchState() {
        return currentMatchState;
    }

    /**
     * 
     * 
     * @param currentMatchState sets current squash match state
     * 
     */
    protected void setCurrentMatchState(SquashMatchIncidentResult currentMatchState) {
        this.currentMatchState = currentMatchState;
    }

    public SquashMatchState() {
        this(new SquashMatchFormat());
    }

    public SquashMatchState(MatchFormat matchFormat) {
        super();
        this.matchFormat = (SquashMatchFormat) matchFormat;
        int n = this.matchFormat.getnGamesInMatch();
        pointScoreForWin = ((SquashMatchFormat) matchFormat).getnPointInRegularGame();
        gameScoreForWin = n / 2 + 1;
        if (2 * (n / 2) == n) // i.e. if n is even
            gameScoreForDraw = n / 2;
        else
            gameScoreForDraw = n; // set high enough so it doesn't ever get hit
        pointsA = 0;
        pointsB = 0;
        currentMatchState = new SquashMatchIncidentResult(SquashMatchIncidentResultType.PREMATCH);
        gameScoreInGameN = new PairOfIntegers[n];
        for (int i = 0; i < n; i++)
            gameScoreInGameN[i] = new PairOfIntegers();
        serve = TeamId.UNKNOWN;
        serveInFirstGame = TeamId.UNKNOWN;
        setScore(0, 0, 0, 0, TeamId.UNKNOWN);
    }

    /**
     * Return SquashMatchIncident Result after the match incident updated
     * 
     * @return matchEventResult
     */
    @Override
    public MatchIncidentResult updateStateForIncident(MatchIncident matchIncident,
                    boolean autosyncMatchStateToFeedOnMismatch) {
        SquashMatchIncidentResult matchEventResult = null;
        if (!(matchIncident instanceof SquashMatchIncident))
            return super.updateStateForIncident(matchIncident, autosyncMatchStateToFeedOnMismatch);
        else {
            SquashMatchIncident mI = (SquashMatchIncident) matchIncident;
            TeamId onServeNow;
            boolean serveA = serve == TeamId.A;
            TeamId teamId = mI.getTeamId();
            switch (mI.getIncidentSubType()) {
                case SERVEFIRST:
                    matchEventResult = new SquashMatchIncidentResult(SquashMatchIncidentResultType.SERVEFIRST);
                    serveInFirstGame = teamId;
                    serve = serveInFirstGame;
                    break;
                case POINTWON:
                    if (TeamId.A == teamId) {
                        matchEventResult = new SquashMatchIncidentResult(SquashMatchIncidentResultType.POINTWON);
                        onServeNow = teamId;
                        pointsA++;
                        serve = onServeNow;
                        if (isSetWinningGameScore(pointsA, pointsB)) {
                            matchEventResult = new SquashMatchIncidentResult(SquashMatchIncidentResultType.GAMEWON);
                            gameScoreInGameN[gamesA + gamesB].A = pointsA;
                            gameScoreInGameN[gamesA + gamesB].B = pointsB;
                            pointsA = 0;
                            pointsB = 0;
                            gamesA++;
                            if (gamesA == gameScoreForWin) {
                                matchEventResult =
                                                new SquashMatchIncidentResult(SquashMatchIncidentResultType.MATCHWON);
                            }
                        }
                    } else {
                        matchEventResult = new SquashMatchIncidentResult(SquashMatchIncidentResultType.POINTWON);
                        onServeNow = TeamId.B;
                        pointsB++;
                        serve = onServeNow;

                        if (isSetWinningGameScore(pointsB, pointsA)) {
                            matchEventResult = new SquashMatchIncidentResult(SquashMatchIncidentResultType.GAMEWON);
                            gameScoreInGameN[gamesA + gamesB].A = pointsA;
                            gameScoreInGameN[gamesA + gamesB].B = pointsB;
                            gamesB++;
                            pointsA = 0;
                            pointsB = 0;
                            if (gamesB == gameScoreForWin) {
                                matchEventResult =
                                                new SquashMatchIncidentResult(SquashMatchIncidentResultType.MATCHWON);
                            }
                        }
                    }

                    matchEventResult.setPlayerAServedPoint(serveA);
                    matchEventResult.setTeamId(teamId);
                    break;
                default:
                    break;
            }
        }
        currentMatchState = matchEventResult;
        return matchEventResult;
    }

    /**
     * Return true if x is even
     * 
     * @param x input numbers
     * @return true or false
     */
    @JsonIgnore
    public boolean isEven(int x) {
        if ((x % 2) == 0)
            return false;
        else
            return true;

    }

    /**
     * Returns true if the games score is a set winning score
     * 
     * @param gamesWinner input numbers as gamesWinner
     * @param gamesLoser input numbers as gamesLoser
     * @return true or false
     */
    private boolean isSetWinningGameScore(int gamesWinner, int gamesLoser) {

        return (gamesWinner >= pointScoreForWin) && (gamesWinner - gamesLoser >= 2);
    }

    @Override
    public MatchState copy() {
        SquashMatchState cc = new SquashMatchState(this.matchFormat);
        cc.setEqualTo(this);
        return cc;
    }

    @Override
    public void setEqualTo(MatchState matchState) {
        SquashMatchState vs = (SquashMatchState) matchState;
        this.setGamesA(((SquashMatchState) matchState).getGamesA());
        this.setGamesB(((SquashMatchState) matchState).getGamesB());
        this.setPointsA(((SquashMatchState) matchState).getPointsA());
        this.setPointsB(((SquashMatchState) matchState).getPointsB());
        this.setServe(vs.getServe());
        this.setServeInFirstGame(vs.getServeInFirstGame());
        this.setScore(vs.getGamesA(), vs.getGamesB(), vs.getPointsA(), vs.getPointsB(), vs.getServe());
        for (int i = 0; i < matchFormat.getnGamesInMatch(); i++) {
            PairOfIntegers s = vs.getGameScoreInGameN(i);
            this.gameScoreInGameN[i].A = s.A;
            this.gameScoreInGameN[i].B = s.B;
        }
    }

    /**
     * Returns next matchIncident Prompt
     * 
     * @return matchIncidentPrompt
     */
    @JsonIgnore
    @Override
    public MatchIncidentPrompt getNextPrompt() {
        MatchIncidentPrompt matchIncidentPrompt = null;
        String lastLegWinner = "PA"; // set default prompt to be whoever on the
                                     // last leg
        String matchWinner = "A";
        TeamId teamId = currentMatchState.getTeamId();
        switch (currentMatchState.getSquashMatchIncidentResultType()) {
            case PREMATCH:
                matchIncidentPrompt = new MatchIncidentPrompt("Enter SA/SB as who serve first", "SA");
                break;
            case GAMEWON:
            case SERVEFIRST:
            case POINTWON:
                if (TeamId.A == teamId)
                    lastLegWinner = "PA";
                else
                    lastLegWinner = "PB";
                matchIncidentPrompt = new MatchIncidentPrompt("Enter winner of next point (PA/PB)", lastLegWinner);
                break;
            case MATCHWON:
                if (TeamId.A == teamId)
                    matchWinner = "A";
                else
                    matchWinner = "B";
                matchIncidentPrompt =
                                new MatchIncidentPrompt(String.format("Match is finished - won by %s", matchWinner));
                break;
            default:
                break;
        }

        return matchIncidentPrompt;
    }

    /**
     * Returns match incident
     * 
     * @return matchEvent
     */
    @Override
    public MatchIncident getMatchIncident(String response) {
        SquashMatchIncident matchEvent = new SquashMatchIncident();
        TeamId teamId;
        switch (response.toUpperCase()) {
            case "PA":
                teamId = TeamId.A;
                matchEvent.set(SquashMatchIncidentType.POINTWON, teamId);
                break;
            case "PB":
                teamId = TeamId.B;
                matchEvent.set(SquashMatchIncidentType.POINTWON, teamId);
                break;
            case "SA":
                teamId = TeamId.A;
                matchEvent.set(SquashMatchIncidentType.SERVEFIRST, teamId);
                break;
            case "SB":
                teamId = TeamId.B;
                matchEvent.set(SquashMatchIncidentType.SERVEFIRST, teamId);
                break;
            default:
                return null; // invalid input so return null
        }
        return matchEvent;
    }

    private static final String poScoreKey = "point score";
    private static final String seScoreKey = "set score";
    private static final String onServeKey = "serve now";
    private static final String serverFirstSetKey = "first set serve";

    /**
     * Returns map
     * 
     * @return map
     */
    @Override
    public LinkedHashMap<String, String> getAsMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

        map.put(poScoreKey, String.format("%d-%d", pointsA, pointsB));
        map.put(seScoreKey, String.format("%d-%d", gamesA, gamesB));
        map.put(onServeKey, String.format("%s", serve));
        map.put(serverFirstSetKey, String.format("%s", serveInFirstGame));
        return map;
    }

    /**
     * @param map sets null
     */
    @Override
    public String setFromMap(Map<String, String> map) {
        /*
         * do nothing - not allowing the user to manually update the score
         */
        return null;
    }

    /**
     * Returns if the match is completed
     * 
     * @return true or false
     */
    @JsonIgnore
    @Override
    public boolean isMatchCompleted() {

        return (gamesB == gameScoreForWin) || (gamesA == gameScoreForWin);
    }

    /**
     * Returns match format
     * 
     * @return matchFormat
     */
    @JsonIgnore
    @Override
    public MatchFormat getMatchFormat() {
        return matchFormat;
    }

    /**
     * Returns PairOfIntegers[i] to display set point score of both teams
     * 
     * @return gameScoreInSetN[i]
     */
    public PairOfIntegers[] getGameScoreInSetN() {
        return gameScoreInGameN;
    }

    /**
     * set PairOfIntegers
     * 
     * @param gameScoreInSetN the pair of integers to record point score in each set
     * 
     */
    public void setGameScoreInSetN(PairOfIntegers[] gameScoreInSetN) {
        this.gameScoreInGameN = gameScoreInSetN;
    }

    /**
     * 
     * 
     * @param matchFormat sets the squash match format
     * 
     */
    public void setMatchFormat(SquashMatchFormat matchFormat) {
        this.matchFormat = matchFormat;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((matchFormat == null) ? 0 : matchFormat.hashCode());
        result = prime * result + pointScoreForWin;
        result = prime * result + pointsA;
        result = prime * result + pointsB;
        result = prime * result + ((serve == null) ? 0 : serve.hashCode());
        result = prime * result + ((serveInFirstGame == null) ? 0 : serveInFirstGame.hashCode());
        result = prime * result + gameScoreForDraw;
        result = prime * result + gameScoreForWin;
        result = prime * result + gamesA;
        result = prime * result + gamesB;
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
        SquashMatchState other = (SquashMatchState) obj;
        for (int i = 0; i < gameScoreInGameN.length; i++) {
            if (gameScoreInGameN[i].A != other.gameScoreInGameN[i].A)
                return false;
            if (gameScoreInGameN[i].B != other.gameScoreInGameN[i].B)
                return false;
        }
        if (matchFormat == null) {
            if (other.matchFormat != null)
                return false;
        } else if (!matchFormat.equals(other.matchFormat))
            return false;
        if (pointScoreForWin != other.pointScoreForWin)
            return false;
        if (pointsA != other.pointsA)
            return false;
        if (pointsB != other.pointsB)
            return false;
        if (serve != other.serve)
            return false;
        if (serveInFirstGame != other.serveInFirstGame)
            return false;
        if (gameScoreForDraw != other.gameScoreForDraw)
            return false;
        if (gameScoreForWin != other.gameScoreForWin)
            return false;
        if (gamesA != other.gamesA)
            return false;
        if (gamesB != other.gamesB)
            return false;
        return true;
    }

    /**
     * Returns current game period
     * 
     * @return gamePeriod
     */
    @Override
    public GamePeriod getGamePeriod() {
        GamePeriod gamePeriod = null;
        switch (currentMatchState.getSquashMatchIncidentResultType()) {
            case PREMATCH:
                gamePeriod = GamePeriod.PREMATCH;
                break;
            case MATCHWON:
                gamePeriod = GamePeriod.POSTMATCH;
                break;
            case SERVEFIRST:
            case POINTWON:
            case GAMEWON:
                gamePeriod = GamePeriod.PERIOD1;
                break;
            default:
                break;
        }
        return gamePeriod;
    }

    /**
     * Returns the sequence id to use for match based markets
     * 
     * @return "M"
     */
    @JsonIgnore
    public String getSequenceIdForMatch() {
        return "M";
    }

    /**
     * Returns the sequence id to use for set based markets
     * 
     * @param setOffset 0 = current set, 1 = next set etc
     * @return null if specified set can't occur, else the sequence id
     */
    @JsonIgnore
    public String getSequenceIdforGame(int setOffset) {
        int setNo = this.getGamesA() + this.getGamesB() + setOffset + 1;
        if (setNo > matchFormat.getnGamesInMatch())
            return null;
        else
            return String.format("G%d", setNo);
    }

    /**
     * Returns the sequence id to use for point based markets (within the currently active game or tie break)
     * 
     * @param pointOffset 0 = current game, 1 = next game etc
     * @return null if specified point can't occur, else the sequence id
     */
    @JsonIgnore
    public String getSequenceIdForPoint(int pointOffset) {
        int pointNo = this.getPointsA() + this.getPointsB() + pointOffset;
        if (this.isPointMayBePlayed(pointNo))
            return String.format("G%d.%d", this.getGameNo(1), pointNo);
        else
            return null;
    }

    /**
     * Returns true if within a game and point no n (starting at 1 for first point in game) might be played
     * 
     * @param n point No
     * @return true or false
     */
    @JsonIgnore
    boolean isPointMayBePlayed(int n) {
        return n >= getPointNo(1);
    }

    /**
     * Returns set no in range 1-5
     * 
     * @param pointOffset Game point No
     * @return gameNo
     */
    @JsonIgnore
    public int getGameNo(int pointOffset) {
        return gamesA + gamesB + pointOffset;
    }

    /**
     * Returns point no starting at 1 for first point of game or tie break
     * 
     * @param pointOffset point in game No
     * @return pointNo
     */
    @JsonIgnore
    public int getPointNo(int pointOffset) {
        int pointNo = pointsA + pointsB + pointOffset;
        return pointNo;
    }

    /**
     * Returns true if in preMatch
     * 
     * @return true or false
     */
    @Override
    public boolean preMatch() {
        return getServeInFirstGame() == TeamId.UNKNOWN;
    }

    /**
     * Returns no of secs to go within current period. Returns zero if pre Match or at half time etc.
     * 
     * @return secs
     */
    @Override
    public int secsLeftInCurrentPeriod() {
        int secs = -1;
        return secs;

    }

    @Override
    public MatchState generateSimpleMatchState() {
        Map<String, PairOfIntegers> pair = new HashMap<String, PairOfIntegers>();
        for (int i = 1; i <= gameScoreInGameN.length; i++)
            pair.put("scoreInSet" + i, gameScoreInGameN[i - 1]);
        SquashSimpleMatchState simpleMatchState = new SquashSimpleMatchState(preMatch(), isMatchCompleted(),
                        getGamesA(), getGamesB(), getPointsA(), getPointsB(), getServe(), getServeInFirstGame(), pair);
        return simpleMatchState;
    }
}
