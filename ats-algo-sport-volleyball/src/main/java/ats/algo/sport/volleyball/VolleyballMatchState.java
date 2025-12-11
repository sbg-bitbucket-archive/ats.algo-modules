package ats.algo.sport.volleyball;

import java.util.Arrays;
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
import ats.algo.core.matchresult.MatchResultElement;
import ats.algo.core.matchresult.MatchResultMap;
import ats.algo.genericsupportfunctions.PairOfIntegers;
import ats.algo.sport.volleyball.VolleyballMatchIncident.VolleyballMatchIncidentType;
import ats.algo.sport.volleyball.VolleyballMatchIncidentResult.VolleyballMatchIncidentResultType;

public class VolleyballMatchState extends AlgoMatchState {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private VolleyballMatchFormat matchFormat;
    private int setsA;
    private int setsB;
    private TeamId serve;
    private TeamId serveInFirstSet;
    private final int pointScoreForWin;
    private final int pointScoreForFinalWin;
    private final int setScoreForWin;
    private PairOfIntegers[] gameScoreInSetN;
    private PairOfIntegers totalScoreForMatch;
    private int pointsA;
    private int pointsB;
    private boolean inFinalSet;
    @JsonIgnore
    private VolleyballMatchIncidentResult currentMatchState;

    public VolleyballMatchState() {
        this(new VolleyballMatchFormat());
    }

    public VolleyballMatchState(MatchFormat matchFormat) {
        super();
        this.matchFormat = (VolleyballMatchFormat) matchFormat;
        int n = this.matchFormat.getnSetsInMatch();
        pointScoreForWin = this.matchFormat.getnPointInRegularSet();
        pointScoreForFinalWin = this.matchFormat.getnPointInFinalSet();
        setScoreForWin = n / 2 + 1;
        pointsA = 0;
        pointsB = 0;
        gameScoreInSetN = new PairOfIntegers[n];
        totalScoreForMatch = new PairOfIntegers();
        for (int i = 0; i < n; i++)
            gameScoreInSetN[i] = new PairOfIntegers();
        serve = TeamId.UNKNOWN;
        serveInFirstSet = TeamId.UNKNOWN;
        setScore(0, 0, 0, 0, TeamId.UNKNOWN);
        currentMatchState = new VolleyballMatchIncidentResult(VolleyballMatchIncidentResultType.PREMATCH);
    }

    /**
     * Returns PairOfIntegers[i] to display set point score of both teams
     * 
     * @param i set number
     * @return gameScoreInSetN[i]
     */
    public PairOfIntegers getGameScoreInSetN(int i) {
        return gameScoreInSetN[i];
    }

    /**
     * Returns PairOfIntegers to display all the set point scores of both teams
     * 
     * @return gameScoreInSetN
     */
    public PairOfIntegers[] getGameScoreInSetN() {
        return gameScoreInSetN;
    }

    /**
     * set PairOfIntegers
     * 
     * @param gameScoreInSetN the pair of integers to record point score in each set
     * 
     */
    public void setGameScoreInSetN(PairOfIntegers[] gameScoreInSetN) {
        this.gameScoreInSetN = gameScoreInSetN;
    }

    /**
     * 
     * 
     */
    void setGameScoreInSetN(int setNo, int gamesA, int gamesB) {
        gameScoreInSetN[setNo].A = gamesA;
        gameScoreInSetN[setNo].B = gamesB;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(gameScoreInSetN);
        result = prime * result + (inFinalSet ? 1231 : 1237);
        result = prime * result + ((matchFormat == null) ? 0 : matchFormat.hashCode());
        result = prime * result + pointScoreForFinalWin;
        result = prime * result + pointScoreForWin;
        result = prime * result + pointsA;
        result = prime * result + pointsB;
        result = prime * result + ((serve == null) ? 0 : serve.hashCode());
        result = prime * result + ((serveInFirstSet == null) ? 0 : serveInFirstSet.hashCode());
        result = prime * result + setScoreForWin;
        result = prime * result + setsA;
        result = prime * result + setsB;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        VolleyballMatchState other = (VolleyballMatchState) obj;
        if (gameScoreInSetN.length != other.gameScoreInSetN.length)
            return false;
        for (int i = 0; i < gameScoreInSetN.length; i++) {
            if (gameScoreInSetN[i].A != other.gameScoreInSetN[i].A)
                return false;
            if (gameScoreInSetN[i].B != other.gameScoreInSetN[i].B)
                return false;
        }
        if (inFinalSet != other.inFinalSet)
            return false;
        if (matchFormat == null) {
            if (other.matchFormat != null)
                return false;
        } else if (!matchFormat.equals(other.matchFormat))
            return false;
        if (pointScoreForFinalWin != other.pointScoreForFinalWin)
            return false;
        if (pointScoreForWin != other.pointScoreForWin)
            return false;
        if (pointsA != other.pointsA)
            return false;
        if (pointsB != other.pointsB)
            return false;
        if (serve != other.serve)
            return false;
        if (serveInFirstSet != other.serveInFirstSet)
            return false;
        if (setScoreForWin != other.setScoreForWin)
            return false;
        if (setsA != other.setsA)
            return false;
        if (setsB != other.setsB)
            return false;
        return true;
    }

    /**
     * Returns true if at the final set
     * 
     * @return inFinalSet
     */
    public boolean isInFinalSet() {
        return inFinalSet;
    }

    /**
     * 
     * 
     * @param inFinalSet to check set if is in final set
     * 
     */
    public void setInFinalSet(boolean inFinalSet) {
        this.inFinalSet = inFinalSet;
    }

    /**
     * 
     * 
     * @param matchFormat sets the volleyball match format
     * 
     */
    public void setMatchFormat(VolleyballMatchFormat matchFormat) {
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
     * @param serveInFirstSet sets who is serve in first set
     * 
     */
    public void setServeInFirstSet(TeamId serveInFirstSet) {
        this.serveInFirstSet = serveInFirstSet;
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
     * Returns who is serve in first set now
     * 
     * @return serveInFirstSet
     */
    public TeamId getServeInFirstSet() {
        return serveInFirstSet;
    }

    /**
     * Returns how many points in final set
     * 
     * @return pointScoreForFinalWin
     */
    public int getPointScoreForFinalWin() {
        return pointScoreForFinalWin;
    }

    /**
     * Returns how many points in normal set
     * 
     * @return pointScoreForWin
     */
    public int getPointScoreForWin() {
        return pointScoreForWin;
    }

    /**
     * Returns how many sets in match
     * 
     * @return setScoreForWin
     */
    public int getSetScoreForWin() {
        return setScoreForWin;
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
     *
     * 
     * @param pointsA Sets the Team A point score
     * 
     */
    public void setPointsA(int pointsA) {
        this.pointsA = pointsA;
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
     *
     * 
     * @param pointsB Sets pointsB
     */
    public void setPointsB(int pointsB) {
        this.pointsB = pointsB;
    }

    /**
     * Returns team A set score
     * 
     * @return setsA
     */
    public int getSetsA() {
        return setsA;
    }

    /**
     * 
     * 
     * @param setsA Sets setsA
     * 
     */
    public void setSetsA(int setsA) {
        this.setsA = setsA;
    }

    /**
     * Returns team B set score
     * 
     * @return setsB
     */
    public int getSetsB() {
        return setsB;
    }

    /**
     *
     * 
     * @param setsB Sets setsB
     */
    public void setSetsB(int setsB) {
        this.setsB = setsB;
    }

    /**
     * sets the starting score. Starting points assumed to be zero
     * 
     * @param setsA Sets setsA
     * @param setsB Sets setsB
     * @param pointA Sets pointA
     * @param pointB Sets pointB
     * @param onServeNow may be A,B or unknown
     */
    public void setScore(int setsA, int setsB, int pointA, int pointB, TeamId onServeNow) {
        this.setsA = setsA;
        this.setsB = setsB;
        this.pointsA = pointA;
        this.pointsB = pointB;
        inFinalSet = (setsA == setScoreForWin - 1) && (setsB == setScoreForWin - 1);
        this.serve = onServeNow;

    }

    /**
     * Return VolleyballMatchIncident Result after the match incident updated
     * 
     * @return matchEventResult
     */
    @Override
    public MatchIncidentResult updateStateForIncident(MatchIncident matchIncident,
                    boolean autosyncMatchStateToFeedOnMismatch) {
        VolleyballMatchIncidentResult matchEventResult = null;
        if (!(matchIncident instanceof VolleyballMatchIncident))
            return super.updateStateForIncident(matchIncident, autosyncMatchStateToFeedOnMismatch);
        else {
            VolleyballMatchIncident mI = (VolleyballMatchIncident) matchIncident;

            TeamId onServeNow;
            boolean serveA = serve == TeamId.A;
            TeamId teamId = mI.getTeamId();
            switch (mI.getIncidentSubType()) {
                case SERVEFIRST:
                    matchEventResult = new VolleyballMatchIncidentResult(VolleyballMatchIncidentResultType.SERVE);
                    matchEventResult.setTeamId(teamId);
                    serveInFirstSet = teamId;
                    serve = serveInFirstSet;
                    break;
                case SERVEFINALSET:
                    matchEventResult =
                                    new VolleyballMatchIncidentResult(VolleyballMatchIncidentResultType.SERVEFINALSET);
                    matchEventResult.setTeamId(teamId);
                    serve = teamId;
                    break;
                case POINTWON:

                    matchEventResult = new VolleyballMatchIncidentResult(VolleyballMatchIncidentResultType.POINTWON);
                    matchEventResult.setTeamId(teamId);
                    matchEventResult.setPlayerAServedPoint(serveA);
                    onServeNow = teamId;
                    serve = onServeNow;
                    if (TeamId.A == teamId) {
                        pointsA++;
                        gameScoreInSetN[setsA + setsB].A = pointsA;
                        gameScoreInSetN[setsA + setsB].B = pointsB;
                        if (isSetWinningGameScore(pointsA, pointsB)) {
                            matchEventResult =
                                            new VolleyballMatchIncidentResult(VolleyballMatchIncidentResultType.SETWON);
                            matchEventResult.setTeamId(teamId);
                            matchEventResult.setPlayerAServedPoint(serveA);

                            pointsA = 0;
                            pointsB = 0;
                            setsA++;
                            if (isEven(setsA + setsB))
                                onServeNow = switchServer(serveInFirstSet);
                            else
                                onServeNow = serveInFirstSet;
                            if ((setsB == setScoreForWin - 1) && (setsA == setScoreForWin - 1)) {
                                inFinalSet = true;
                                onServeNow = TeamId.UNKNOWN;
                            }
                            serve = onServeNow;
                            if (setsA == setScoreForWin) {
                                matchEventResult = new VolleyballMatchIncidentResult(
                                                VolleyballMatchIncidentResultType.MATCHWON);
                                matchEventResult.setTeamId(teamId);
                                matchEventResult.setPlayerAServedPoint(serveA);
                            }
                        }
                    } else {
                        pointsB++;
                        gameScoreInSetN[setsA + setsB].A = pointsA;
                        gameScoreInSetN[setsA + setsB].B = pointsB;
                        if (isSetWinningGameScore(pointsB, pointsA)) {
                            matchEventResult =
                                            new VolleyballMatchIncidentResult(VolleyballMatchIncidentResultType.SETWON);
                            matchEventResult.setTeamId(teamId);
                            matchEventResult.setPlayerAServedPoint(serveA);

                            setsB++;
                            pointsA = 0;
                            pointsB = 0;
                            if (isEven(setsA + setsB))
                                onServeNow = switchServer(serveInFirstSet);
                            else
                                onServeNow = serveInFirstSet;
                            if ((setsB == setScoreForWin - 1) && (setsA == setScoreForWin - 1)) {
                                inFinalSet = true;
                                onServeNow = TeamId.UNKNOWN;
                            }
                            serve = onServeNow;
                            if (setsB == setScoreForWin) {
                                matchEventResult = new VolleyballMatchIncidentResult(
                                                VolleyballMatchIncidentResultType.MATCHWON);
                                matchEventResult.setTeamId(teamId);
                                matchEventResult.setPlayerAServedPoint(serveA);
                            }

                        }
                        break;
                    }

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

        if (inFinalSet)
            return (gamesWinner >= pointScoreForFinalWin) && (gamesWinner - gamesLoser >= 2);
        else
            return (gamesWinner >= pointScoreForWin) && (gamesWinner - gamesLoser >= 2);
    }

    @Override
    public AlgoMatchState copy() {
        VolleyballMatchState cc = new VolleyballMatchState(this.matchFormat);
        cc.setEqualTo(this);
        return cc;
    }

    @Override
    public void setEqualTo(AlgoMatchState matchState) {
        VolleyballMatchState vs = (VolleyballMatchState) matchState;
        this.setSetsA(((VolleyballMatchState) matchState).getSetsA());
        this.setSetsB(((VolleyballMatchState) matchState).getSetsB());
        this.setPointsA(((VolleyballMatchState) matchState).getPointsA());
        this.setPointsB(((VolleyballMatchState) matchState).getPointsB());
        this.setServe(vs.getServe());
        this.setServeInFirstSet(vs.getServeInFirstSet());
        this.setScore(vs.getSetsA(), vs.getSetsB(), vs.getPointsA(), vs.getPointsB(), vs.getServe());
        for (int i = 0; i < matchFormat.getnSetsInMatch(); i++) {
            PairOfIntegers s = vs.getGameScoreInSetN(i);
            this.gameScoreInSetN[i].A = s.A;
            this.gameScoreInSetN[i].B = s.B;
        }
        this.setCurrentMatchState(((VolleyballMatchState) matchState).getCurrentMatchState());
    }

    /**
     * 
     * 
     * @param currentMatchState sets current volleyball match state
     * 
     */
    public void setCurrentMatchState(VolleyballMatchIncidentResult currentMatchState) {
        this.currentMatchState = currentMatchState;
    }

    /**
     * Returns the current match state as volleyball match incident result
     * 
     * @return currentMatchState
     */
    public VolleyballMatchIncidentResult getCurrentMatchState() {
        return currentMatchState;
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
        TeamId teamId = currentMatchState.getTeamId();
        switch (currentMatchState.getVolleyballMatchIncidentResultType()) {
            case PREMATCH:
                matchIncidentPrompt = new MatchIncidentPrompt("Enter SA/SB as who serve first", "SA");
                break;
            case SERVE:
            case SERVEFINALSET:
            case POINTWON:
            case SETWON:
                if (TeamId.A == teamId)
                    lastLegWinner = "PA";
                else
                    lastLegWinner = "PB";
                matchIncidentPrompt = new MatchIncidentPrompt("Enter winner of next point (PA/PB)", lastLegWinner);
                break;
            case MATCHWON:
                if (TeamId.A == teamId)
                    lastLegWinner = "A";
                else
                    lastLegWinner = "B";
                matchIncidentPrompt =
                                new MatchIncidentPrompt(String.format("Match is finished - won by %s", lastLegWinner));
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
        VolleyballMatchIncident matchEvent = new VolleyballMatchIncident();
        TeamId teamId;
        switch (response.toUpperCase()) {
            case "PA":
                teamId = TeamId.A;
                matchEvent.set(VolleyballMatchIncidentType.POINTWON, teamId);
                break;
            case "PB":
                teamId = TeamId.B;
                matchEvent.set(VolleyballMatchIncidentType.POINTWON, teamId);
                break;
            case "SB":
                teamId = TeamId.B;
                matchEvent.set(VolleyballMatchIncidentType.SERVEFIRST, teamId);
                break;
            case "SA":
                teamId = TeamId.A;
                matchEvent.set(VolleyballMatchIncidentType.SERVEFIRST, teamId);
                break;
            case "FSA":
                teamId = TeamId.A;
                matchEvent.set(VolleyballMatchIncidentType.SERVEFINALSET, teamId);
                break;
            case "FSB":
                teamId = TeamId.B;
                matchEvent.set(VolleyballMatchIncidentType.SERVEFINALSET, teamId);
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
        map.put(seScoreKey, String.format("%d-%d", setsA, setsB));
        map.put(onServeKey, String.format("%s", serve));
        map.put(serverFirstSetKey, String.format("%s", serveInFirstSet));
        return map;
    }

    /**
     * 
     * 
     * @param map sets null
     * 
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

        return (setsA == setScoreForWin) || (setsB == setScoreForWin);
    }

    /**
     * Returns the server based on the current server
     * 
     * @param currentServer current serve
     * @return newServer
     * 
     */
    @JsonIgnore
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
     * Returns true if at pre match
     * 
     * @return true or false
     */
    @JsonIgnore
    public boolean isPreMatch() {
        return getOnServeNow() == TeamId.UNKNOWN;
    }

    /**
     * Returns who is serve now
     * 
     * @return serve
     */
    @JsonIgnore
    public TeamId getOnServeNow() {
        return serve;
    }

    /**
     * Returns current game period
     * 
     * @return gamePeriod
     */
    @Override
    public GamePeriod getGamePeriod() {
        GamePeriod gamePeriod = null;
        switch (currentMatchState.getVolleyballMatchIncidentResultType()) {
            case PREMATCH:
                gamePeriod = GamePeriod.PREMATCH;
                break;
            case MATCHWON:
                gamePeriod = GamePeriod.POSTMATCH;
                break;
            case SERVE:
            case SERVEFINALSET:
            case POINTWON:
            case SETWON:
                switch (this.getSetsA() + this.getSetsB() + 1) {
                    case 1:
                        gamePeriod = GamePeriod.FIRST_SET;
                        break;
                    case 2:
                        gamePeriod = GamePeriod.SECOND_SET;
                        break;
                    case 3:
                        gamePeriod = GamePeriod.THIRD_SET;
                        break;
                    case 4:
                        gamePeriod = GamePeriod.FOURTH_SET;
                        break;
                    case 5:
                        gamePeriod = GamePeriod.FIFTH_SET;
                        break;
                }
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
    public String getSequenceIdforSet(int setOffset) {
        int setNo = this.getSetsA() + this.getSetsB() + setOffset + 1;
        if (setNo > matchFormat.getnSetsInMatch())
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
        int pointNo = this.getPointsA() + this.getPointsB() + pointOffset + 1;
        if (this.isPointMayBePlayed(pointNo))
            return String.format("S%d.%d", this.getSetNo(), pointNo);
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
        return n >= getPointNo();
    }

    /**
     * Returns set no in range 1-5
     * 
     * @return setNo
     */
    @JsonIgnore
    public int getSetNo() {
        return setsA + setsB + 1;
    }

    /**
     * Returns point no in range 0 - infinite for specific set
     * 
     * @return totalPointNo
     */
    @JsonIgnore
    public PairOfIntegers getTotalPointsForMatch() {
        int setNo = getSetNo();
        totalScoreForMatch.A = 0;
        totalScoreForMatch.B = 0;
        for (int i = 0; i < setNo; i++) {
            totalScoreForMatch.A += getGameScoreInSetN(i).A;
            totalScoreForMatch.B += getGameScoreInSetN(i).B;
        }

        return totalScoreForMatch;
    }

    /**
     * Returns point no starting at 1 for first point of game or tie break
     * 
     * @return pointNo
     */
    @JsonIgnore
    public int getPointNo() {
        int pointNo = pointsA + pointsB + 1;
        return pointNo;
    }

    /**
     * Returns true if in preMatch
     * 
     * @return true or false
     */
    @Override
    public boolean preMatch() {
        return (currentMatchState.getVolleyballMatchIncidentResultType() == VolleyballMatchIncidentResultType.PREMATCH);
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

    @JsonIgnore
    private LinkedHashMap<String, PairOfIntegers> getGamesScoreMap() {
        LinkedHashMap<String, PairOfIntegers> scoreMap = new LinkedHashMap<String, PairOfIntegers>();
        for (int i = 1; i < getSetNo(); i++) {
            scoreMap.put("scoreInSet" + Integer.toString(i), gameScoreInSetN[i - 1]);
        }
        return scoreMap;
    }

    @Override
    public VolleyballSimpleMatchState generateSimpleMatchState() {
        VolleyballSimpleMatchState simpleMatchState = new VolleyballSimpleMatchState(isPreMatch(), isMatchCompleted(),
                        getSetsA(), getSetsB(), getPointsA(), getPointsB(), getOnServeNow(), getGamesScoreMap());
        return simpleMatchState;
    }

    @Override
    public AlgoMatchState generateMatchStateForMatchResult(MatchResultMap matchManualResult) {

        Map<String, MatchResultElement> map = matchManualResult.getMap();
        int nSets = ((VolleyballMatchFormat) this.getMatchFormat()).getnSetsInMatch();
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
        VolleyballMatchState endMatchState = (VolleyballMatchState) this.copy();
        for (int i = 0; i < nSets; i++) {
            int nA = setScore[i].A;
            int nB = setScore[i].B;
            endMatchState.setGameScoreInSetN(i, nA, nB);
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
        endMatchState.setGameScoreInSetN(gameScoreInGameN);
        endMatchState.setSetsA(setsA);
        endMatchState.setSetsB(setsB);
        endMatchState.setPointsA(gamesA);
        endMatchState.setPointsB(gamesB);
        return endMatchState;
    }
}
