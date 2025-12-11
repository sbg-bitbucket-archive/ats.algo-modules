package ats.algo.sport.badminton;

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
import ats.algo.core.common.AbandonMatchIncident;
import ats.algo.core.common.AbandonMatchIncident.AbandonMatchIncidentType;
import ats.algo.core.common.TeamId;
import ats.algo.core.matchresult.MatchResultElement;
import ats.algo.core.matchresult.MatchResultMap;
import ats.algo.genericsupportfunctions.PairOfIntegers;
import ats.algo.sport.badminton.BadmintonMatchIncident.BadmintonMatchIncidentType;
import ats.algo.sport.badminton.BadmintonMatchIncidentResult.BadmintonMatchIncidentResultType;

public class BadmintonMatchState extends AlgoMatchState {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private BadmintonMatchFormat matchFormat;
    private int gamesA;
    private int gamesB;
    private TeamId serve;
    private TeamId serveInFirstGame;
    private final int pointScoreForWin;
    private final int maxPointScoreForWin;
    private final int gameScoreForWin;
    private final int gameScoreForDraw;
    private PairOfIntegers[] gameScoreInGameN;
    private int pointsA;
    private int pointsB;
    @JsonIgnore
    private BadmintonMatchIncidentResult mostRecentMatchIncidentResult; // the state
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
     * set PairOfIntegers
     * 
     * @param gameScoreInGameN the pair of integers to record point score in each set
     * 
     */
    public void setPointScoreInGameN(int gameNo, int pointsA, int pointsB) {
        this.gameScoreInGameN[gameNo].A = gamesA;
        this.gameScoreInGameN[gameNo].B = gamesB;
    }

    /**
     * Returns max points in the game
     * 
     * @return maxPointScoreForWin
     */
    public int getMaxPointScoreForWin() {
        return maxPointScoreForWin;
    }

    /**
     * 
     * 
     * @param matchFormat sets the badminton match format
     * 
     */
    public void setMatchFormat(BadmintonMatchFormat matchFormat) {
        this.matchFormat = matchFormat;
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
     * Returns how many points to draw in normal set
     * 
     * @return gameScoreForDraw
     */
    public int getGameScoreForDraw() {
        return gameScoreForDraw;
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
     * Returns the current match state as badminton match incident result
     * 
     * @return currentMatchState
     */
    public BadmintonMatchIncidentResult getMostRecentMatchIncidentResult() {
        return mostRecentMatchIncidentResult;
    }

    /**
     * 
     * 
     * @param currentMatchState sets current badminton match state
     * 
     */
    public void setMostRecentMatchIncidentResult(BadmintonMatchIncidentResult currentMatchState) {
        this.mostRecentMatchIncidentResult = currentMatchState;
    }

    public BadmintonMatchState() {
        this(new BadmintonMatchFormat());
    }

    public BadmintonMatchState(MatchFormat matchFormat) {
        super();
        this.matchFormat = (BadmintonMatchFormat) matchFormat;
        int n = this.matchFormat.getnGamesInMatch();
        pointScoreForWin = ((BadmintonMatchFormat) matchFormat).getnPointInRegularGame();
        maxPointScoreForWin = ((BadmintonMatchFormat) matchFormat).getMaxPointInFinalGame();
        gameScoreForWin = n / 2 + 1;
        if (2 * (n / 2) == n) // i.e. if n is even
            gameScoreForDraw = n / 2;
        else
            gameScoreForDraw = n; // set high enough so it doesn't ever get hit
        pointsA = 0;
        pointsB = 0;
        mostRecentMatchIncidentResult = new BadmintonMatchIncidentResult(BadmintonMatchIncidentResultType.PREMATCH);
        gameScoreInGameN = new PairOfIntegers[n];
        for (int i = 0; i < n; i++)
            gameScoreInGameN[i] = new PairOfIntegers();
        serve = TeamId.UNKNOWN;
        serveInFirstGame = TeamId.UNKNOWN;
        setScore(0, 0, 0, 0, TeamId.UNKNOWN);
    }

    /**
     * Return BadmintonMatchIncident Result after the match incident updated
     * 
     * @return matchEventResult
     */
    @Override
    public MatchIncidentResult updateStateForIncident(MatchIncident matchIncident,
                    boolean autosyncMatchStateToFeedOnMismatch) {
        BadmintonMatchIncidentResult matchEventResult = null;
        if (matchIncident.getClass() == AbandonMatchIncident.class) {

            matchAbandonedWonTeam = matchIncident.getTeamId();

            mostRecentMatchIncidentResult = matchEventResult;

            abandonedMatchWinner();
            matchEventResult = new BadmintonMatchIncidentResult(BadmintonMatchIncidentResultType.MATCHABANDONED);

            matchEventResult.setTeamId(matchAbandonedWonTeam);

        } else if (!(matchIncident instanceof BadmintonMatchIncident)) {
            return super.updateStateForIncident(matchIncident, autosyncMatchStateToFeedOnMismatch);
        } else if (matchIncident.getClass() == BadmintonMatchIncident.class) {

            BadmintonMatchIncident mI = (BadmintonMatchIncident) matchIncident;

            TeamId onServeNow;
            boolean serveA = serve == TeamId.A;
            TeamId teamId = mI.getTeamId();
            switch (mI.getIncidentSubType()) {
                case SERVEFIRST:
                    matchEventResult = new BadmintonMatchIncidentResult(BadmintonMatchIncidentResultType.SERVEFIRST);
                    serveInFirstGame = teamId;
                    serve = serveInFirstGame;
                    break;
                case POINTWON:
                    if (TeamId.A == teamId) {
                        matchEventResult = new BadmintonMatchIncidentResult(BadmintonMatchIncidentResultType.POINTWON);
                        onServeNow = teamId;
                        pointsA++;
                        serve = onServeNow;
                        if (isSetWinningGameScore(pointsA, pointsB)) {
                            matchEventResult =
                                            new BadmintonMatchIncidentResult(BadmintonMatchIncidentResultType.GAMEWON);
                            gameScoreInGameN[gamesA + gamesB].A = pointsA;
                            gameScoreInGameN[gamesA + gamesB].B = pointsB;
                            pointsA = 0;
                            pointsB = 0;
                            gamesA++;
                            if (gamesA == gameScoreForWin) {
                                matchEventResult = new BadmintonMatchIncidentResult(
                                                BadmintonMatchIncidentResultType.MATCHWON);
                            }
                        }
                    } else {
                        matchEventResult = new BadmintonMatchIncidentResult(BadmintonMatchIncidentResultType.POINTWON);
                        onServeNow = teamId;
                        pointsB++;
                        serve = onServeNow;
                        if (isSetWinningGameScore(pointsB, pointsA)) {
                            matchEventResult =
                                            new BadmintonMatchIncidentResult(BadmintonMatchIncidentResultType.GAMEWON);
                            gameScoreInGameN[gamesA + gamesB].A = pointsA;
                            gameScoreInGameN[gamesA + gamesB].B = pointsB;
                            gamesB++;
                            pointsA = 0;
                            pointsB = 0;
                            if (gamesB == gameScoreForWin) {
                                matchEventResult = new BadmintonMatchIncidentResult(
                                                BadmintonMatchIncidentResultType.MATCHWON);

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


        mostRecentMatchIncidentResult = matchEventResult;
        return matchEventResult;
    }


    public TeamId abandonedMatchWinner() {
        if (gamesA + gamesB > 0)
            return this.matchAbandonedWonTeam;
        else
            return TeamId.UNKNOWN;
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

        if (gamesWinner == maxPointScoreForWin)
            return true;
        else
            return (gamesWinner >= pointScoreForWin) && (gamesWinner - gamesLoser >= 2);
    }

    @Override
    public AlgoMatchState copy() {
        BadmintonMatchState cc = new BadmintonMatchState(this.matchFormat);
        cc.setEqualTo(this);
        return cc;
    }

    @Override
    public void setEqualTo(AlgoMatchState matchState) {
        BadmintonMatchState vs = (BadmintonMatchState) matchState;
        this.setGamesA(((BadmintonMatchState) matchState).getGamesA());
        this.setGamesB(((BadmintonMatchState) matchState).getGamesB());
        this.setPointsA(((BadmintonMatchState) matchState).getPointsA());
        this.setPointsB(((BadmintonMatchState) matchState).getPointsB());
        this.setServe(vs.getServe());
        this.setServeInFirstGame(vs.getServeInFirstGame());
        this.setScore(vs.getGamesA(), vs.getGamesB(), vs.getPointsA(), vs.getPointsB(), vs.getServe());
        this.setMostRecentMatchIncidentResult(vs.getMostRecentMatchIncidentResult());
        this.setMatchAbandonedWonTeam(vs.getMatchAbandonedWonTeam());
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
        TeamId teamId = mostRecentMatchIncidentResult.getTeamId();
        switch (mostRecentMatchIncidentResult.getBadmintonMatchIncidentResultType()) {
            case PREMATCH:
                matchIncidentPrompt = new MatchIncidentPrompt("Enter SA/SB as who serve first", "SA");
                break;
            case MATCHABANDONED:
                matchIncidentPrompt = new MatchIncidentPrompt("Game Abandoned", "Null");
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
        BadmintonMatchIncident matchEvent = new BadmintonMatchIncident();
        TeamId teamId;
        switch (response.toUpperCase()) {
            case "PA":
                teamId = TeamId.A;
                matchEvent.set(BadmintonMatchIncidentType.POINTWON, teamId);
                break;
            case "PB":
                teamId = TeamId.B;
                matchEvent.set(BadmintonMatchIncidentType.POINTWON, teamId);
                break;
            case "SA":
                teamId = TeamId.A;
                matchEvent.set(BadmintonMatchIncidentType.SERVEFIRST, teamId);
                break;
            case "SB":
                teamId = TeamId.B;
                matchEvent.set(BadmintonMatchIncidentType.SERVEFIRST, teamId);
                break;
            case "ABA":
                teamId = TeamId.A;
                return new AbandonMatchIncident(AbandonMatchIncidentType.WALKOVER, teamId);
            case "ABB":
                teamId = TeamId.B;
                return new AbandonMatchIncident(AbandonMatchIncidentType.WALKOVER, teamId);
            default:
                return null; // invalid input so return null
        }
        return matchEvent;
    }

    private static final String poScoreKey = "point score";
    private static final String seScoreKey = "game score";
    private static final String onServeKey = "serve now";
    private static final String serverFirstSetKey = "first game serve";

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

        return (gamesA == gameScoreForWin) || (gamesB == gameScoreForWin);
    }

    /**
     * Returns if it is after match point
     * 
     * @return true or false
     */
    @JsonIgnore
    public boolean isMatchPoint() {

        return (gamesA == (gameScoreForWin - 1) && pointsA >= (pointScoreForWin - 1))
                        || (gamesB == (gameScoreForWin - 1) && pointsB >= (pointScoreForWin - 1));
    }

    /**
     * Returns if it is match point
     * 
     * @return true or false
     */
    @JsonIgnore
    public boolean isGamePoint() {

        return (pointsA >= (pointScoreForWin - 1) && pointsA > pointsB)
                        || (pointsB >= (pointScoreForWin - 1) && pointsB > pointsA);
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
        result = prime * result + ((matchFormat == null) ? 0 : matchFormat.hashCode());
        result = prime * result + maxPointScoreForWin;
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
        BadmintonMatchState other = (BadmintonMatchState) obj;
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
        if (maxPointScoreForWin != other.maxPointScoreForWin)
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
        switch (mostRecentMatchIncidentResult.getBadmintonMatchIncidentResultType()) {
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
        int pointNo = this.getPointsA() + this.getPointsB() + pointOffset;
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
    public BadmintonSimpleMatchState generateSimpleMatchState() {
        Map<String, PairOfIntegers> pair = new HashMap<String, PairOfIntegers>();
        for (int i = 1; i <= gameScoreInGameN.length; i++)
            pair.put("scoreInSet " + i, gameScoreInGameN[i - 1]);
        BadmintonSimpleMatchState simpleMatchState = new BadmintonSimpleMatchState(preMatch(), isMatchCompleted(),
                        getGamesA(), getGamesB(), getPointsA(), getPointsB(), getServe(), getServeInFirstGame(), pair);
        return simpleMatchState;
    }

    @Override
    public AlgoMatchState generateMatchStateForMatchResult(MatchResultMap matchManualResult) {
        Map<String, MatchResultElement> map = matchManualResult.getMap();
        int nSets = ((BadmintonMatchFormat) this.getMatchFormat()).getnGamesInMatch();
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
        }
        int setsA = 0;
        int setsB = 0;
        int gamesA = 0;
        int gamesB = 0;
        BadmintonMatchState endMatchState = (BadmintonMatchState) this.copy();
        for (int i = 0; i < nSets; i++) {
            int nA = setScore[i].A;
            int nB = setScore[i].B;
            endMatchState.setPointScoreInGameN(i, nA, nB);
            if (nA > nB)
                setsA++;
            else
                setsB++;
            gamesA += nA;
            gamesB += nB;
        }
        PairOfIntegers[] gameScoreInGameN = new PairOfIntegers[nSets];
        for (int i = 0; i < nSets; i++) {
            gameScoreInGameN[i] = new PairOfIntegers();
            gameScoreInGameN[i].A = setScore[i].A;
            gameScoreInGameN[i].B = setScore[i].B;
        }
        endMatchState.setGameScoreInGameN(gameScoreInGameN);
        endMatchState.setGamesA(setsA);
        endMatchState.setGamesB(setsB);;
        endMatchState.setPointsA(gamesA);
        endMatchState.setPointsB(gamesB);
        return endMatchState;
    }
}
