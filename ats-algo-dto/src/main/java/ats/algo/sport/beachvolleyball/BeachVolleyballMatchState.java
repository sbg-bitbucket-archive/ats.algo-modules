package ats.algo.sport.beachvolleyball;

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
import ats.algo.sport.beachvolleyball.BeachVolleyballMatchIncident.BeachVolleyballMatchIncidentType;
import ats.algo.sport.beachvolleyball.BeachVolleyballMatchIncidentResult.BeachVolleyballMatchIncidentResultType;

public class BeachVolleyballMatchState extends MatchState {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private BeachVolleyballMatchFormat matchFormat;
    private int setsA;
    private int setsB;
    private TeamId serve;
    private TeamId serveInFirstSet;
    private final int pointScoreForWin;
    private final int pointScoreForFinalWin;
    private final int setScoreForWin;
    private final int setScoreForDraw;
    private PairOfIntegers[] gameScoreInSetN;
    private int pointsA;
    private int pointsB;
    private boolean inFinalSet;
    @JsonIgnore
    private BeachVolleyballMatchIncidentResult currentMatchState; // the state
                                                                  // following
                                                                  // the
                                                                  // most
                                                                  // recent

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
     * @return setScoreForDraw
     */
    public int getSetScoreForDraw() {
        return setScoreForDraw;
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
     * @param pointsA Sets pointsA
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
     *
     * 
     * @return pointsA Sets the Team A point score
     * 
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
     * Returns team A set score
     * 
     * @return setsA
     */
    public int getSetsA() {
        return setsA;
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
     * @param setsA Sets setsA
     * 
     */
    public void setSetsA(int setsA) {
        this.setsA = setsA;
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
        inFinalSet = setsA == (setScoreForWin - 1) && setsB == (setScoreForWin - 1);
        this.serve = onServeNow;

    }

    /**
     * Returns the current match state as volleyball match incident result
     * 
     * @return currentMatchState
     */
    protected BeachVolleyballMatchIncidentResult getCurrentMatchState() {
        return currentMatchState;
    }

    /**
     * 
     * 
     * @param currentMatchState sets current volleyball match state
     * 
     */
    protected void setCurrentMatchState(BeachVolleyballMatchIncidentResult currentMatchState) {
        this.currentMatchState = currentMatchState;
    }

    public BeachVolleyballMatchState() {
        this(new BeachVolleyballMatchFormat());
    }

    public BeachVolleyballMatchState(MatchFormat matchFormat) {
        super();
        this.matchFormat = (BeachVolleyballMatchFormat) matchFormat;
        int n = this.matchFormat.getnSetsInMatch();
        pointScoreForWin = ((BeachVolleyballMatchFormat) matchFormat).getnPointInRegularSet();
        pointScoreForFinalWin = ((BeachVolleyballMatchFormat) matchFormat).getnPointInFinalSet();
        setScoreForWin = n / 2 + 1;
        if (2 * (n / 2) == n) // i.e. if n is even
            setScoreForDraw = n / 2;
        else
            setScoreForDraw = n; // set high enough so it doesn't ever get hit
        pointsA = 0;
        pointsB = 0;
        currentMatchState = new BeachVolleyballMatchIncidentResult(BeachVolleyballMatchIncidentResultType.PREMATCH);
        gameScoreInSetN = new PairOfIntegers[n];
        for (int i = 0; i < n; i++)
            gameScoreInSetN[i] = new PairOfIntegers();
        serve = TeamId.UNKNOWN;
        serveInFirstSet = TeamId.UNKNOWN;
        setScore(0, 0, 0, 0, TeamId.UNKNOWN);
    }

    /**
     * Return BeachVolleyballMatchIncident Result after the match incident updated
     * 
     * @return matchEventResult
     */
    @Override
    public MatchIncidentResult updateStateForIncident(MatchIncident matchIncident,
                    boolean autosyncMatchStateToFeedOnMismatch) {
        BeachVolleyballMatchIncidentResult matchEventResult = null;
        TeamId onServeNow;
        boolean serveA = serve == TeamId.A;
        if (!(matchIncident instanceof BeachVolleyballMatchIncident))
            return super.updateStateForIncident(matchIncident, autosyncMatchStateToFeedOnMismatch);
        else {
            BeachVolleyballMatchIncident mI = (BeachVolleyballMatchIncident) matchIncident;
            TeamId teamId = mI.getTeamId();
            switch (mI.getIncidentSubType()) {
                case SERVEFIRST:
                    matchEventResult = new BeachVolleyballMatchIncidentResult(
                                    BeachVolleyballMatchIncidentResultType.SERVEFIRST);
                    serveInFirstSet = teamId;
                    serve = serveInFirstSet;
                    break;
                case POINTWON:
                    if (TeamId.A == teamId) {
                        matchEventResult = new BeachVolleyballMatchIncidentResult(
                                        BeachVolleyballMatchIncidentResultType.POINTWON);
                        onServeNow = teamId;
                        pointsA++;
                        serve = onServeNow;
                        if (isSetWinningGameScore(pointsA, pointsB)) {
                            matchEventResult = new BeachVolleyballMatchIncidentResult(
                                            BeachVolleyballMatchIncidentResultType.SETWON);
                            gameScoreInSetN[setsA + setsB].A = pointsA;
                            gameScoreInSetN[setsA + setsB].B = pointsB;
                            pointsA = 0;
                            pointsB = 0;
                            setsA++;
                            if ((setsB == setScoreForWin - 1) && (setsA == setScoreForWin - 1))
                                inFinalSet = true;
                            if (isEven(setsA + setsB))
                                onServeNow = switchServer(serveInFirstSet);
                            else
                                onServeNow = serveInFirstSet;
                            serve = onServeNow;
                            if (setsA == setScoreForWin) {
                                matchEventResult = new BeachVolleyballMatchIncidentResult(
                                                BeachVolleyballMatchIncidentResultType.MATCHWON);
                            }
                        }
                    } else {
                        matchEventResult = new BeachVolleyballMatchIncidentResult(
                                        BeachVolleyballMatchIncidentResultType.POINTWON);
                        onServeNow = teamId;
                        pointsB++;
                        serve = onServeNow;

                        if (isSetWinningGameScore(pointsB, pointsA)) {
                            matchEventResult = new BeachVolleyballMatchIncidentResult(
                                            BeachVolleyballMatchIncidentResultType.SETWON);
                            gameScoreInSetN[setsA + setsB].A = pointsA;
                            gameScoreInSetN[setsA + setsB].B = pointsB;
                            setsB++;
                            pointsA = 0;
                            pointsB = 0;
                            if (isEven(setsA + setsB))
                                onServeNow = switchServer(serveInFirstSet);
                            else
                                onServeNow = serveInFirstSet;
                            if ((setsB == setScoreForWin - 1) && (setsA == setScoreForWin - 1))
                                inFinalSet = true;
                            serve = onServeNow;
                            if (setsB == setScoreForWin) {
                                matchEventResult = new BeachVolleyballMatchIncidentResult(
                                                BeachVolleyballMatchIncidentResultType.MATCHWON);
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

        if (inFinalSet)
            return (gamesWinner >= pointScoreForFinalWin) && (gamesWinner - gamesLoser >= 2);
        else
            return (gamesWinner >= pointScoreForWin) && (gamesWinner - gamesLoser >= 2);
    }

    @Override
    public MatchState copy() {
        BeachVolleyballMatchState cc = new BeachVolleyballMatchState(this.matchFormat);
        cc.setEqualTo(this);
        return cc;
    }

    @Override
    public void setEqualTo(MatchState matchState) {
        BeachVolleyballMatchState vs = (BeachVolleyballMatchState) matchState;
        this.setSetsA(((BeachVolleyballMatchState) matchState).getSetsA());
        this.setSetsB(((BeachVolleyballMatchState) matchState).getSetsB());
        this.setPointsA(((BeachVolleyballMatchState) matchState).getPointsA());
        this.setPointsB(((BeachVolleyballMatchState) matchState).getPointsB());
        this.setServe(vs.getServe());
        this.setServeInFirstSet(vs.getServeInFirstSet());
        this.setInFinalSet(((BeachVolleyballMatchState) matchState).isInFinalSet());
        this.setScore(vs.getSetsA(), vs.getSetsB(), vs.getPointsA(), vs.getPointsB(), vs.getServe());
        for (int i = 0; i < matchFormat.getnSetsInMatch(); i++) {
            PairOfIntegers s = vs.getGameScoreInSetN(i);
            this.gameScoreInSetN[i].A = s.A;
            this.gameScoreInSetN[i].B = s.B;
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
        switch (currentMatchState.getVolleyballMatchIncidentResultType()) {
            case PREMATCH:
                matchIncidentPrompt = new MatchIncidentPrompt("Enter SA/SB as who serve first", "SA");
                break;
            case SETWON:
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
        BeachVolleyballMatchIncident matchEvent = new BeachVolleyballMatchIncident();
        TeamId teamId;
        switch (response.toUpperCase()) {
            case "PA":
                teamId = TeamId.A;
                matchEvent.set(BeachVolleyballMatchIncidentType.POINTWON, teamId);
                break;
            case "PB":
                teamId = TeamId.B;
                matchEvent.set(BeachVolleyballMatchIncidentType.POINTWON, teamId);
                break;
            case "SA":
                teamId = TeamId.A;
                matchEvent.set(BeachVolleyballMatchIncidentType.SERVEFIRST, teamId);
                break;
            case "SB":
                teamId = TeamId.B;
                matchEvent.set(BeachVolleyballMatchIncidentType.SERVEFIRST, teamId);
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
     * @return ture or false
     */
    @JsonIgnore
    @Override
    public boolean isMatchCompleted() {

        return (setsB == setScoreForWin) || (setsA == setScoreForWin);
    }

    /**
     * Returns the server based on the current server
     * 
     * @return newServer
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
     * @param setNo =0 for first set
     * @param gamesA
     * @param gamesB
     */
    void setGameScoreInSetN(int setNo, int gamesA, int gamesB) {
        gameScoreInSetN[setNo].A = gamesA;
        gameScoreInSetN[setNo].B = gamesB;
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
    public void setMatchFormat(BeachVolleyballMatchFormat matchFormat) {
        this.matchFormat = matchFormat;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (inFinalSet ? 1231 : 1237);
        result = prime * result + ((matchFormat == null) ? 0 : matchFormat.hashCode());
        result = prime * result + pointScoreForFinalWin;
        result = prime * result + pointScoreForWin;
        result = prime * result + pointsA;
        result = prime * result + pointsB;
        result = prime * result + ((serve == null) ? 0 : serve.hashCode());
        result = prime * result + ((serveInFirstSet == null) ? 0 : serveInFirstSet.hashCode());
        result = prime * result + setScoreForDraw;
        result = prime * result + setScoreForWin;
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
        BeachVolleyballMatchState other = (BeachVolleyballMatchState) obj;
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
        if (setScoreForDraw != other.setScoreForDraw)
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
     * Returns current game period
     * 
     * @return gamePeriod
     */
    @Override
    public GamePeriod getGamePeriod() {
        GamePeriod gamePeriod = null;
        switch (currentMatchState.getBeachVolleyballMatchIncidentResultType()) {
            case PREMATCH:
                gamePeriod = GamePeriod.PREMATCH;
                break;
            case MATCHWON:
                gamePeriod = GamePeriod.POSTMATCH;
                break;
            case SERVEFIRST:
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
        int pointNo = this.getPointsA() + this.getPointsB() + pointOffset;
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
     * Returns true if at pre match
     * 
     * @return true or false
     */
    @Override
    public boolean preMatch() {
        return getServeInFirstSet() == TeamId.UNKNOWN;
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
    public BeachVolleyballSimpleMatchState generateSimpleMatchState() {
        BeachVolleyballSimpleMatchState simpleMatchState =
                        new BeachVolleyballSimpleMatchState(preMatch(), isMatchCompleted(), getSetsA(), getSetsB(),
                                        getPointsA(), getPointsB(), getServe(), getGamesScoreMap());
        return simpleMatchState;
    }

}
