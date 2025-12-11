package ats.algo.sport.tabletennis;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ats.algo.core.GamePeriod;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchIncidentPrompt;
import ats.algo.core.baseclasses.MatchIncidentResult;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.common.TeamId;
import ats.algo.core.matchresult.MatchResultMap;
import ats.algo.genericsupportfunctions.PairOfIntegers;
import ats.algo.sport.tabletennis.TabletennisMatchIncident.TabletennisMatchIncidentType;
import ats.algo.sport.tabletennis.TabletennisMatchIncidentResult.TabletennisMatchIncidentResultType;

public class TabletennisMatchState extends AlgoMatchState {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private TabletennisMatchFormat matchFormat;
    private int gamesA;
    private int gamesB;
    private TeamId serve;
    private TeamId serveNext;
    private TeamId firstServeinGame;
    private final int pointScoreForWin;
    private final int gameScoreForWin;
    private PairOfIntegers[] gameScoreInGameN;
    private int pointsA;
    private int pointsB;
    @JsonIgnore
    private TabletennisMatchIncidentResult currentMatchState; // the state
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
     * @param serveNext sets who is serve next in first game
     * 
     */
    public void setServeNext(TeamId serveNext) {
        this.serveNext = serveNext;
    }

    /**
     * 
     * 
     * @param serveInFirstGame sets who is serve in first game
     * 
     */
    public void setServeInFirstGame(TeamId serveInFirstGame) {
        this.firstServeinGame = serveInFirstGame;
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
        return firstServeinGame;
    }

    /**
     * Returns who is serve next
     * 
     * @return serveNext
     */
    public TeamId getServeNext() {
        return serveNext;
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
     * Returns how many games in match
     * 
     * @return gameScoreForWin
     */
    public int getGameScoreForWin() {
        return gameScoreForWin;
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
     * 
     * 
     * @param gamesA Sets gamesA
     * 
     */
    public void setGamesA(int gamesA) {
        this.gamesA = gamesA;
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
     * @param onServeNext may be A,B or unknown
     */
    public void setScore(int gamesA, int gamesB, int pointA, int pointB, TeamId onServeNow, TeamId onServeNext) {
        this.gamesA = gamesA;
        this.gamesB = gamesB;
        this.pointsA = pointA;
        this.pointsB = pointB;
        this.serve = onServeNow;
        this.serveNext = onServeNext;

    }

    /**
     * Returns the current match state as table tennis match incident result
     * 
     * @return currentMatchState
     */
    protected TabletennisMatchIncidentResult getCurrentMatchState() {
        return currentMatchState;
    }

    /**
     * 
     * 
     * @param currentMatchState sets current table tennis match state
     * 
     */
    protected void setCurrentMatchState(TabletennisMatchIncidentResult currentMatchState) {
        this.currentMatchState = currentMatchState;
    }

    public TabletennisMatchState() {
        this(new TabletennisMatchFormat());
    }

    public TabletennisMatchState(MatchFormat matchFormat) {
        super();
        this.matchFormat = (TabletennisMatchFormat) matchFormat;
        int n = this.matchFormat.getnGamesInMatch();
        pointScoreForWin = ((TabletennisMatchFormat) matchFormat).getnPointInRegularGame();
        gameScoreForWin = n / 2 + 1;
        pointsA = 0;
        pointsB = 0;
        currentMatchState = new TabletennisMatchIncidentResult(TabletennisMatchIncidentResultType.PREMATCH);
        gameScoreInGameN = new PairOfIntegers[n];
        for (int i = 0; i < n; i++)
            gameScoreInGameN[i] = new PairOfIntegers();
        serve = TeamId.UNKNOWN;
        firstServeinGame = TeamId.UNKNOWN;
        serveNext = TeamId.UNKNOWN;
        setScore(0, 0, 0, 0, TeamId.UNKNOWN, TeamId.UNKNOWN);
    }

    /**
     * Return SquashMatchIncident Result after the match incident updated
     * 
     * @return matchEventResult
     */
    @Override
    public MatchIncidentResult updateStateForIncident(MatchIncident matchIncident,
                    boolean autosyncMatchStateToFeedOnMismatch) {
        TabletennisMatchIncidentResult matchEventResult = null;
        if (!(matchIncident instanceof TabletennisMatchIncident))
            return super.updateStateForIncident(matchIncident, autosyncMatchStateToFeedOnMismatch);
        else {
            TabletennisMatchIncident mI = (TabletennisMatchIncident) matchIncident;

            boolean serveA = serve == TeamId.A;
            if ((serve == serveNext)) {
                serveNext = switchServer(serve);
            } else {
                serve = serveNext;
                serveNext = serve;
            }
            TeamId teamId = mI.getTeamId();
            switch (mI.getIncidentSubType()) {
                case SERVEFIRST:
                    matchEventResult =
                                    new TabletennisMatchIncidentResult(TabletennisMatchIncidentResultType.SERVEFIRST);
                    matchEventResult.setTeamId(teamId);
                    firstServeinGame = teamId;
                    serve = firstServeinGame;
                    serveNext = serve;
                    break;
                case POINTWON:
                    if (TeamId.A == teamId) {
                        matchEventResult =
                                        new TabletennisMatchIncidentResult(TabletennisMatchIncidentResultType.POINTWON);
                        matchEventResult.setPlayerAwonPoint(true);
                        pointsA++;
                        if ((pointsA + pointsB) >= ((pointScoreForWin - 1) * 2)) {
                            serve = firstServeinGame;
                            serveNext = switchServer(firstServeinGame);
                        }
                        if (isSetWinningGameScore(pointsA, pointsB)) {
                            matchEventResult = new TabletennisMatchIncidentResult(
                                            TabletennisMatchIncidentResultType.GAMEWON);
                            matchEventResult.setPlayerAwonPoint(true);
                            gameScoreInGameN[gamesA + gamesB].A = pointsA;
                            gameScoreInGameN[gamesA + gamesB].B = pointsB;
                            pointsA = 0;
                            pointsB = 0;
                            gamesA++;
                            firstServeinGame = switchServer(firstServeinGame);
                            serve = firstServeinGame;
                            serveNext = serve;
                            if (gamesA == gameScoreForWin) {
                                matchEventResult = new TabletennisMatchIncidentResult(
                                                TabletennisMatchIncidentResultType.MATCHWON);
                            }
                        }
                    } else {
                        matchEventResult =
                                        new TabletennisMatchIncidentResult(TabletennisMatchIncidentResultType.POINTWON);
                        matchEventResult.setPlayerAwonPoint(false);
                        pointsB++;
                        if ((pointsA + pointsB) >= ((pointScoreForWin - 1) * 2)) {
                            serve = firstServeinGame;
                            serveNext = switchServer(firstServeinGame);
                        }
                        if (isSetWinningGameScore(pointsB, pointsA)) {
                            matchEventResult = new TabletennisMatchIncidentResult(
                                            TabletennisMatchIncidentResultType.GAMEWON);
                            matchEventResult.setPlayerAwonPoint(false);
                            gameScoreInGameN[gamesA + gamesB].A = pointsA;
                            gameScoreInGameN[gamesA + gamesB].B = pointsB;
                            gamesB++;
                            pointsA = 0;
                            pointsB = 0;
                            firstServeinGame = switchServer(firstServeinGame);
                            serve = firstServeinGame;
                            serveNext = serve;
                            if (gamesB == gameScoreForWin) {
                                matchEventResult = new TabletennisMatchIncidentResult(
                                                TabletennisMatchIncidentResultType.MATCHWON);
                            }
                        }
                    }
                    matchEventResult.setTeamId(teamId);
                    matchEventResult.setPlayerAServedPoint(serveA);
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
    public AlgoMatchState copy() {
        TabletennisMatchState cc = new TabletennisMatchState(this.matchFormat);
        cc.setEqualTo(this);
        return cc;
    }

    @Override
    public void setEqualTo(AlgoMatchState matchState) {
        TabletennisMatchState vs = (TabletennisMatchState) matchState;
        this.setGamesA(((TabletennisMatchState) matchState).getGamesA());
        this.setGamesB(((TabletennisMatchState) matchState).getGamesB());
        this.setPointsA(((TabletennisMatchState) matchState).getPointsA());
        this.setPointsB(((TabletennisMatchState) matchState).getPointsB());
        this.setServe(vs.getServe());
        this.setServeInFirstGame(vs.getServeInFirstGame());
        this.setScore(vs.getGamesA(), vs.getGamesB(), vs.getPointsA(), vs.getPointsB(), vs.getServe(),
                        vs.getServeNext());
        for (int i = 0; i < matchFormat.getnGamesInMatch(); i++) {
            PairOfIntegers s = vs.getGameScoreInGameN(i);
            this.gameScoreInGameN[i].A = s.A;
            this.gameScoreInGameN[i].B = s.B;
        }
        // this.setCurrentMatchState(((tabletennisMatchState) matchState)
        // .getCurrentMatchState());
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
        switch (currentMatchState.getTabletennisMatchIncidentResultType()) {
            case PREMATCH:
                matchIncidentPrompt = new MatchIncidentPrompt("Enter SA/SB as who serve first", "SA");
                break;
            case SERVEFIRST:
            case GAMEWON:
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
        TabletennisMatchIncident matchEvent = new TabletennisMatchIncident();
        TeamId teamId;
        switch (response.toUpperCase()) {
            case "PA":
                teamId = TeamId.A;
                matchEvent.set(TabletennisMatchIncidentType.POINTWON, teamId);
                break;
            case "PB":
                teamId = TeamId.B;
                matchEvent.set(TabletennisMatchIncidentType.POINTWON, teamId);
                break;
            case "SA":
                teamId = TeamId.A;
                matchEvent.set(TabletennisMatchIncidentType.SERVEFIRST, teamId);
                break;
            case "SB":
                teamId = TeamId.B;
                matchEvent.set(TabletennisMatchIncidentType.SERVEFIRST, teamId);
                break;
            default:
                return null; // invalid input so return null
        }
        return matchEvent;
    }

    private static final String poScoreKey = "point score";
    private static final String seScoreKey = "game score";
    private static final String onServeKey = "serve now";
    private static final String onServeNextKey = "serve next";
    private static final String serverFirstSetKey = "serve first in current set";

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
        map.put(onServeNextKey, String.format("%s", serveNext));
        map.put(serverFirstSetKey, String.format("%s", firstServeinGame));
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
     * Returns the server based on the current server
     * 
     * @param currentServer current serve
     * @return newServer
     * 
     */
    private TeamId switchServer(TeamId currentServer) {
        TeamId newServer = null;
        switch (currentServer) {
            case A:
                newServer = TeamId.B;
                break;
            case B:
                newServer = TeamId.A;
                break;
            case UNKNOWN:
                newServer = TeamId.UNKNOWN;
        }
        return newServer;
    }

    /**
     * Returns who is first serve in game
     * 
     * @return firstServeinGame
     */
    public TeamId getFirstServeinGame() {
        return firstServeinGame;
    }

    /**
     *
     * 
     * @param firstServeinGame Sets who serve first in game
     * 
     */
    public void setFirstServeinGame(TeamId firstServeinGame) {
        this.firstServeinGame = firstServeinGame;
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
     *
     * @param setNo =0 for first set
     * @param gamesA
     * @param gamesB
     */
    void setGameScoreInSetN(int setNo, int gamesA, int gamesB) {
        gameScoreInGameN[setNo].A = gamesA;
        gameScoreInGameN[setNo].B = gamesB;
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
     * 
     * 
     * @param matchFormat sets the table tennis match format
     * 
     */
    public void setMatchFormat(TabletennisMatchFormat matchFormat) {
        this.matchFormat = matchFormat;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((currentMatchState == null) ? 0 : currentMatchState.hashCode());
        result = prime * result + ((firstServeinGame == null) ? 0 : firstServeinGame.hashCode());
        result = prime * result + Arrays.hashCode(gameScoreInGameN);
        result = prime * result + ((matchFormat == null) ? 0 : matchFormat.hashCode());
        result = prime * result + pointScoreForWin;
        result = prime * result + pointsA;
        result = prime * result + pointsB;
        result = prime * result + ((serve == null) ? 0 : serve.hashCode());
        result = prime * result + ((serveNext == null) ? 0 : serveNext.hashCode());
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
        TabletennisMatchState other = (TabletennisMatchState) obj;
        if (firstServeinGame != other.firstServeinGame)
            return false;
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
        if (serveNext != other.serveNext)
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
        switch (currentMatchState.getTabletennisMatchIncidentResultType()) {
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
        int setNo = getGameNo(setOffset) + 1;
        if (setNo > matchFormat.getnGamesInMatch())
            return null;
        else
            return String.format("S%d", setNo);
    }

    /**
     * Returns the sequence id to use for point based markets (within the currently active game or tie break)
     * 
     * @param pointOffset 0 = current game, 1 = next game etc
     * @return null if specified point can't occur, else the sequence id
     */
    @JsonIgnore
    public String getSequenceIdForPoint(int pointOffset) {
        int pointNo = getPointNo(pointOffset);
        if (this.isPointMayBePlayed(pointNo))
            return String.format("S%d.%d", this.getGameNo(1), pointNo);
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
        return n >= getPointNo(0);
    }

    /**
     * Returns game no in range 1-7
     * 
     * @param pointOffset 0 = current game, 1 = next game etc
     * @return gameNo
     */
    @JsonIgnore
    public int getGameNo(int pointOffset) {
        return gamesA + gamesB + pointOffset;
    }

    /**
     * Returns point no starting at 1 for first point of game or tie break
     * 
     * @param pointOffset 0 = current point, 1 = next point etc
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
    public TabletennisSimpleMatchState generateSimpleMatchState() {
        Map<String, PairOfIntegers> pair = new HashMap<String, PairOfIntegers>();
        for (int i = 1; i <= gameScoreInGameN.length; i++)
            pair.put("scoreInSet" + i, gameScoreInGameN[i - 1]);
        TabletennisSimpleMatchState simpleMatchState = new TabletennisSimpleMatchState(preMatch(), isMatchCompleted(),
                        getGamesA(), getGamesB(), getPointsA(), getPointsB(), getServe(), getServeInFirstGame(), pair);
        return simpleMatchState;
    }

    public boolean isGamePoint() {
        int pointsToEndGame = this.matchFormat.getnPointInRegularGame();
        if ((this.pointsA >= pointsToEndGame - 1 && this.pointsA - this.pointsB > 1)
                        || (this.pointsB >= pointsToEndGame - 1 && this.pointsB - this.pointsA > 1))
            return true;
        else
            return false;
    }

    public boolean isPossibleFinalGame() {
        return (gamesB + 1 == gameScoreForWin) || (gamesA + 1 == gameScoreForWin);
    }

    public boolean isTieBreak() {
        return this.pointsA >= 10 && this.pointsB >= 10;
    }

    public TeamId getRaceToGameNWinner(int gameNo) {
        int a = 0;
        int b = 0;
        int counter = 0;
        for (PairOfIntegers game : gameScoreInGameN) {
            if (game != null) {
                if (game.A > game.B)
                    ++a;
                else {
                    ++b;
                }
                ++counter;
            }
            if (counter == gameNo)
                if (a > b)
                    return TeamId.A;
                else {
                    return TeamId.B;
                }
        }

        return TeamId.UNKNOWN;
    }

    public boolean isFinalGamePoint(int i) {
        if (this.isPossibleFinalGame()) {
            if (pointsA + pointsB >= i)
                return true;
        }
        return false;
    }

    @Override
    public AlgoMatchState generateMatchStateForMatchResult(MatchResultMap matchResultMap) {
        /*
         * not yet supported for this sport
         */
        return null;
    }
}
