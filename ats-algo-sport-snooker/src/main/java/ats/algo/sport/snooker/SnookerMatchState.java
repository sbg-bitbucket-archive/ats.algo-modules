package ats.algo.sport.snooker;

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
import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.common.TeamId;
import ats.algo.core.matchresult.MatchResultElement;
import ats.algo.core.matchresult.MatchResultMap;
import ats.algo.genericsupportfunctions.PairOfIntegers;
import ats.algo.sport.snooker.SnookerMatchIncident.SnookerMatchIncidentType;
import ats.algo.sport.snooker.SnookerMatchIncidentResult.SnookerMatchIncidentResultType;

public class SnookerMatchState extends AlgoMatchState {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SnookerMatchFormat matchFormat;
    private int framesA;
    private int framesB;
    private final int race = 4;
    private TeamId serve;
    private TeamId serveInFirstSet;
    private final int pointScoreForWin;
    private final int setScoreForWin;
    private PairOfIntegers[] frameScoreInMatchN;
    private PairOfIntegers[] raceScore;
    private PairOfIntegers[] frameBreakScore;
    private int pointsA;
    private int pointsB;
    private int highestBreakInCurrentFrameA;
    private int highestBreakInCurrentFrameB;

    @JsonIgnore
    private SnookerMatchIncidentResult currentMatchState; // the state
                                                          // following the
                                                          // most recent
    private TeamId potFirstBall = TeamId.UNKNOWN;
    private String potFirstColor = null;

    /**
     * Returns PairOfIntegers[i] to display frame score of both teams
     * 
     * @param i set number
     * @return frameScoreInMatchN[i]
     */
    public PairOfIntegers getFrameScoreInMatchN(int i) {
        return frameScoreInMatchN[i];
    }

    /**
     * Returns race No
     * 
     * @return race
     */
    public int getRace() {
        return race;
    }

    /**
     * set PairOfIntegers
     * 
     * @param raceScore the pair of integers to record race score in each frame
     * 
     */
    public void setRaceScore(PairOfIntegers[] raceScore) {
        this.raceScore = raceScore;
    }

    /**
     * Returns PairOfIntegers[i] to display race point score of both teams
     * 
     * @param i frame number
     * @return raceScore[i]
     */
    public PairOfIntegers getRaceScore(int i) {
        return raceScore[i];
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
     * sets the starting score. Starting points assumed to be zero
     * 
     * @param framesA Sets framesA
     * @param frameB Sets frameB
     * @param pointA Sets pointA
     * @param pointB Sets pointB
     * @param onServeNow may be A,B or unknown
     */
    public void setScore(int framesA, int frameB, int pointA, int pointB, TeamId onServeNow) {
        this.framesA = framesA;
        this.framesB = frameB;
        this.pointsA = pointA;
        this.pointsB = pointB;
        this.serve = onServeNow;

    }

    /**
     * Returns the current match state as snooker match incident result
     * 
     * @return currentMatchState
     */
    protected SnookerMatchIncidentResult getCurrentMatchState() {
        return currentMatchState;
    }

    /**
     * 
     * 
     * @param currentMatchState sets current snooker match state
     * 
     */
    protected void setCurrentMatchState(SnookerMatchIncidentResult currentMatchState) {
        this.currentMatchState = currentMatchState;
    }

    public TeamId getPotFirstBall() {
        return potFirstBall;
    }

    public void setPotFirstBall(TeamId potFirstBall) {
        this.potFirstBall = potFirstBall;
    }

    public String getPotFirstColor() {
        return potFirstColor;
    }

    public void setPotFirstColor(String potFirstColor) {
        this.potFirstColor = potFirstColor;
    }

    public int getHighestBreakInCurrentFrameA() {
        return highestBreakInCurrentFrameA;
    }

    public void setHighestBreakInCurrentFrameA(int highestBreakInCurrentFrameA) {
        this.highestBreakInCurrentFrameA = highestBreakInCurrentFrameA;
    }

    public int getHighestBreakInCurrentFrameB() {
        return highestBreakInCurrentFrameB;
    }

    public void setHighestBreakInCurrentFrameB(int highestBreakInCurrentFrameB) {
        this.highestBreakInCurrentFrameB = highestBreakInCurrentFrameB;
    }

    public PairOfIntegers[] getFrameBreakScore() {
        return frameBreakScore;
    }

    public void setFrameBreakScore(PairOfIntegers[] frameBreakScore) {
        this.frameBreakScore = frameBreakScore;
    }

    public SnookerMatchState(MatchFormat matchFormat) {
        super();
        this.matchFormat = (SnookerMatchFormat) matchFormat;
        int n = this.matchFormat.getnFramesInMatch();
        pointScoreForWin = ((SnookerMatchFormat) matchFormat).getnPointInRegularFrame();
        int raceNo = n;
        setScoreForWin = n / 2 + 1;
        pointsA = 0;
        pointsB = 0;
        highestBreakInCurrentFrameA = 0;
        highestBreakInCurrentFrameB = 0;
        currentMatchState = new SnookerMatchIncidentResult(SnookerMatchIncidentResultType.PREMATCH);
        frameScoreInMatchN = new PairOfIntegers[n];
        frameBreakScore = new PairOfIntegers[n];
        raceScore = new PairOfIntegers[raceNo];
        for (int i = 0; i < n; i++) {
            frameScoreInMatchN[i] = new PairOfIntegers();
            frameBreakScore[i] = new PairOfIntegers();
        }
        for (int i = 0; i < raceNo; i++)
            raceScore[i] = new PairOfIntegers();
        serve = TeamId.UNKNOWN;
        serveInFirstSet = TeamId.UNKNOWN;
        setScore(0, 0, 0, 0, TeamId.UNKNOWN);
    }

    public SnookerMatchState() {
        this(new SnookerMatchFormat());
    }

    /**
     * Return snooker Result after the match incident updated
     * 
     * @return matchEventResult
     */
    @Override
    public MatchIncidentResult updateStateForIncident(MatchIncident matchIncident,
                    boolean autosyncMatchStateToFeedOnMismatch) {
        SnookerMatchIncidentResult matchEventResult = null;
        if (!(matchIncident instanceof SnookerMatchIncident))
            return super.updateStateForIncident(matchIncident, autosyncMatchStateToFeedOnMismatch);
        else {
            SnookerMatchIncident mI = (SnookerMatchIncident) matchIncident;

            boolean serveA = serveInFirstSet == TeamId.A;
            TeamId teamId = mI.getTeamId();
            switch (mI.getIncidentSubType()) {
                case SERVEFIRST:
                    matchEventResult = new SnookerMatchIncidentResult(SnookerMatchIncidentResultType.SERVEFIRST);
                    matchEventResult.setTeamId(teamId);
                    serveInFirstSet = teamId;
                    serve = serveInFirstSet;
                    break;
                case WHOISATTABLE:

                    matchEventResult = new SnookerMatchIncidentResult(SnookerMatchIncidentResultType.WHOISATTABLE);
                    matchEventResult.setTeamId(teamId);
                    serve = teamId;
                    highestBreakInCurrentFrameA = 0;
                    highestBreakInCurrentFrameB = 0;
                    break;
                case REDPOT:
                    matchEventResult = new SnookerMatchIncidentResult(SnookerMatchIncidentResultType.REDPOT);
                    matchEventResult.setTeamId(teamId);
                    if (serve == teamId) {
                        if (teamId == TeamId.A)
                            highestBreakInCurrentFrameA++;
                        if (teamId == TeamId.B)
                            highestBreakInCurrentFrameB++;
                        if (highestBreakInCurrentFrameA > frameBreakScore[framesA + framesB].A)
                            frameBreakScore[framesA + framesB].A = highestBreakInCurrentFrameA;
                        if (highestBreakInCurrentFrameB > frameBreakScore[framesA + framesB].B)
                            frameBreakScore[framesA + framesB].B = highestBreakInCurrentFrameB;
                    }
                    if (potFirstBall == TeamId.UNKNOWN)
                        potFirstBall = teamId;
                    if (TeamId.A == teamId)
                        pointsA = pointsA + 1;
                    else
                        pointsB = pointsB + 1;
                    break;
                case YELLOWPOT:
                    matchEventResult = new SnookerMatchIncidentResult(SnookerMatchIncidentResultType.YELLOWPOT);
                    matchEventResult.setTeamId(teamId);
                    if (serve == teamId) {
                        if (teamId == TeamId.A)
                            highestBreakInCurrentFrameA += 2;
                        if (teamId == TeamId.B)
                            highestBreakInCurrentFrameB += 2;
                        if (highestBreakInCurrentFrameA > frameBreakScore[framesA + framesB].A)
                            frameBreakScore[framesA + framesB].A = highestBreakInCurrentFrameA;
                        if (highestBreakInCurrentFrameB > frameBreakScore[framesA + framesB].B)
                            frameBreakScore[framesA + framesB].B = highestBreakInCurrentFrameB;
                    }
                    if (potFirstColor == null)
                        potFirstColor = "YELLOW";
                    if (TeamId.A == teamId)
                        pointsA = pointsA + 2;
                    else
                        pointsB = pointsB + 2;
                    break;
                case GREENPOT:
                    matchEventResult = new SnookerMatchIncidentResult(SnookerMatchIncidentResultType.GREENPOT);
                    matchEventResult.setTeamId(teamId);
                    if (serve == teamId) {
                        if (teamId == TeamId.A)
                            highestBreakInCurrentFrameA += 3;
                        if (teamId == TeamId.B)
                            highestBreakInCurrentFrameB += 3;
                        if (highestBreakInCurrentFrameA > frameBreakScore[framesA + framesB].A)
                            frameBreakScore[framesA + framesB].A = highestBreakInCurrentFrameA;
                        if (highestBreakInCurrentFrameB > frameBreakScore[framesA + framesB].B)
                            frameBreakScore[framesA + framesB].B = highestBreakInCurrentFrameB;
                    }
                    if (potFirstColor == null)
                        potFirstColor = "GREEN";
                    if (TeamId.A == teamId)
                        pointsA = pointsA + 3;
                    else
                        pointsB = pointsB + 3;
                    break;
                case BROWNPOT:
                    matchEventResult = new SnookerMatchIncidentResult(SnookerMatchIncidentResultType.BROWNPOT);
                    matchEventResult.setTeamId(teamId);
                    if (serve == teamId) {
                        if (teamId == TeamId.A)
                            highestBreakInCurrentFrameA += 4;
                        if (teamId == TeamId.B)
                            highestBreakInCurrentFrameB += 4;
                        if (highestBreakInCurrentFrameA > frameBreakScore[framesA + framesB].A)
                            frameBreakScore[framesA + framesB].A = highestBreakInCurrentFrameA;
                        if (highestBreakInCurrentFrameB > frameBreakScore[framesA + framesB].B)
                            frameBreakScore[framesA + framesB].B = highestBreakInCurrentFrameB;
                    }
                    if (potFirstColor == null)
                        potFirstColor = "BROWN";

                    if (TeamId.A == teamId)
                        pointsA = pointsA + 4;
                    else
                        pointsB = pointsB + 4;
                    break;
                case BLUEPOT:
                    matchEventResult = new SnookerMatchIncidentResult(SnookerMatchIncidentResultType.BLUEPOT);
                    matchEventResult.setTeamId(teamId);
                    if (serve == teamId) {
                        if (teamId == TeamId.A)
                            highestBreakInCurrentFrameA += 5;
                        if (teamId == TeamId.B)
                            highestBreakInCurrentFrameB += 5;
                        if (highestBreakInCurrentFrameA > frameBreakScore[framesA + framesB].A)
                            frameBreakScore[framesA + framesB].A = highestBreakInCurrentFrameA;
                        if (highestBreakInCurrentFrameB > frameBreakScore[framesA + framesB].B)
                            frameBreakScore[framesA + framesB].B = highestBreakInCurrentFrameB;
                    }
                    if (potFirstColor == null)
                        potFirstColor = "BLUE";

                    if (TeamId.A == teamId)
                        pointsA = pointsA + 5;
                    else
                        pointsB = pointsB + 5;
                    break;
                case PINKPOT:
                    matchEventResult = new SnookerMatchIncidentResult(SnookerMatchIncidentResultType.PINKPOT);
                    matchEventResult.setTeamId(teamId);
                    if (serve == teamId) {
                        if (teamId == TeamId.A)
                            highestBreakInCurrentFrameA += 6;
                        if (teamId == TeamId.B)
                            highestBreakInCurrentFrameB += 6;
                        if (highestBreakInCurrentFrameA > frameBreakScore[framesA + framesB].A)
                            frameBreakScore[framesA + framesB].A = highestBreakInCurrentFrameA;
                        if (highestBreakInCurrentFrameB > frameBreakScore[framesA + framesB].B)
                            frameBreakScore[framesA + framesB].B = highestBreakInCurrentFrameB;
                    }
                    if (potFirstColor == null)
                        potFirstColor = "PINK";
                    if (TeamId.A == teamId)
                        pointsA = pointsA + 6;
                    else
                        pointsB = pointsB + 6;
                    break;
                case BLACKPOT:
                    matchEventResult = new SnookerMatchIncidentResult(SnookerMatchIncidentResultType.BLACKPOT);
                    matchEventResult.setTeamId(teamId);
                    if (serve == teamId) {
                        if (teamId == TeamId.A)
                            highestBreakInCurrentFrameA += 7;
                        if (teamId == TeamId.B)
                            highestBreakInCurrentFrameB += 7;
                        if (highestBreakInCurrentFrameA > frameBreakScore[framesA + framesB].A)
                            frameBreakScore[framesA + framesB].A = highestBreakInCurrentFrameA;
                        if (highestBreakInCurrentFrameB > frameBreakScore[framesA + framesB].B)
                            frameBreakScore[framesA + framesB].B = highestBreakInCurrentFrameB;
                    }
                    if (potFirstColor == null)
                        potFirstColor = "BLACK";
                    if (TeamId.A == teamId)
                        pointsA = pointsA + 7;
                    else
                        pointsB = pointsB + 7;
                    break;
                case FRAMEWON:
                    matchEventResult = new SnookerMatchIncidentResult(SnookerMatchIncidentResultType.FRAMEWON);
                    matchEventResult.setTeamId(teamId);
                    matchEventResult.setPlayerAServedPoint(serveA);
                    frameScoreInMatchN[framesA + framesB].A = pointsA;
                    frameScoreInMatchN[framesA + framesB].B = pointsB;
                    if (TeamId.A == teamId) {
                        framesA++;
                        if (framesA == race && framesB < race) {
                            matchEventResult.setPlayerAwonFrame4(true);
                        }
                    } else {
                        framesB++;
                        if (framesB == race && framesA < race) {
                            matchEventResult.setPlayerAwonFrame4(false);
                        }
                    }
                    pointsA = 0;
                    pointsB = 0;
                    potFirstBall = TeamId.UNKNOWN;
                    potFirstColor = null;
                    if ((framesA + framesB) % race == 0) {
                        raceScore[(framesA + framesB) / race].A = framesA;
                        raceScore[(framesA + framesB) / race].B = framesB;
                    }

                    serveInFirstSet = switchServer(serveInFirstSet);
                    serve = serveInFirstSet;
                    if (framesA == setScoreForWin) {
                        matchEventResult = new SnookerMatchIncidentResult(SnookerMatchIncidentResultType.MATCHWON);
                        matchEventResult.setTeamId(TeamId.A);
                        if (framesA == race) {
                            matchEventResult.setPlayerAwonFrame4(true);
                        }
                    }

                    if (framesB == setScoreForWin) {
                        matchEventResult = new SnookerMatchIncidentResult(SnookerMatchIncidentResultType.MATCHWON);
                        matchEventResult.setTeamId(TeamId.B);
                        if (framesB == race) {
                            matchEventResult.setPlayerAwonFrame4(false);
                        }
                    }
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

    @Override
    public AlgoMatchState copy() {
        SnookerMatchState cc = new SnookerMatchState(this.matchFormat);
        cc.setEqualTo(this);
        return cc;
    }

    @Override
    public void setEqualTo(AlgoMatchState matchState) {
        SnookerMatchState vs = (SnookerMatchState) matchState;
        this.setFramesA(((SnookerMatchState) matchState).getFramesA());
        this.setFramesB(((SnookerMatchState) matchState).getFramesB());
        this.setPointsA(((SnookerMatchState) matchState).getPointsA());
        this.setPointsB(((SnookerMatchState) matchState).getPointsB());
        this.setServe(vs.getServe());
        this.setServeInFirstSet(vs.getServeInFirstSet());
        this.setScore(vs.getFramesA(), vs.getFramesB(), vs.getPointsA(), vs.getPointsB(), vs.getServe());
        for (int i = 0; i < matchFormat.getnFramesInMatch(); i++) {
            PairOfIntegers s = vs.getFrameScoreInMatchN(i);
            this.frameScoreInMatchN[i].A = s.A;
            this.frameScoreInMatchN[i].B = s.B;
        }

        for (int i = 0; i < matchFormat.getnFramesInMatch(); i++) {
            PairOfIntegers[] s = vs.getFrameBreakScore();
            this.frameBreakScore[i].A = s[i].A;
            this.frameBreakScore[i].B = s[i].B;
        }
        for (int i = 0; i < matchFormat.getnFramesInMatch() / 4; i++) {
            PairOfIntegers s = vs.getRaceScore(i);
            this.raceScore[i].A = s.A;
            this.raceScore[i].B = s.B;
        }
        this.setCurrentMatchState(((SnookerMatchState) matchState).getCurrentMatchState());
        this.setPotFirstBall(vs.getPotFirstBall());
        this.setPotFirstColor(vs.getPotFirstColor());
        this.setHighestBreakInCurrentFrameA(vs.getHighestBreakInCurrentFrameA());
        this.setHighestBreakInCurrentFrameB(vs.getHighestBreakInCurrentFrameB());
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
        String lastLegWinner = "SA"; // set default prompt to be whoever on the
                                     // last leg
        String matchWinner = "A";
        TeamId teamId = currentMatchState.getTeamId();
        switch (currentMatchState.getSnookerMatchIncidentResultType()) {
            case PREMATCH:
                matchIncidentPrompt = new MatchIncidentPrompt("Enter SA/SB as who serve first", lastLegWinner);
                break;
            case SERVEFIRST:
                if (TeamId.A == teamId)
                    lastLegWinner = "A";
                else
                    lastLegWinner = "B";
                matchIncidentPrompt = new MatchIncidentPrompt("who is at table (A/B)", lastLegWinner);
                break;

            case WHOISATTABLE:
                if (TeamId.A == teamId)
                    lastLegWinner = "PA1";
                else
                    lastLegWinner = "PB1";
                matchIncidentPrompt =
                                new MatchIncidentPrompt("which ball has been potted (PA1/2/3/4/5/6/7)", lastLegWinner);
                break;
            case REDPOT:
                if (TeamId.A == teamId)
                    lastLegWinner = "PA1";
                else
                    lastLegWinner = "PB1";
                matchIncidentPrompt = new MatchIncidentPrompt(
                                "Enter next point (PA/PB/1/2/3/4/5/6/7) Enter (FA/FB) to win frame", lastLegWinner);
                break;
            case YELLOWPOT:
                if (TeamId.A == teamId)
                    lastLegWinner = "PA1";
                else
                    lastLegWinner = "PB1";
                matchIncidentPrompt = new MatchIncidentPrompt(
                                "Enter next point (PA/PB/1/2/3/4/5/6/7) Enter (FA/FB) to win frame", lastLegWinner);
                break;
            case GREENPOT:
                if (TeamId.A == teamId)
                    lastLegWinner = "PA1";
                else
                    lastLegWinner = "PB1";
                matchIncidentPrompt = new MatchIncidentPrompt(
                                "Enter next point (PA/PB/1/2/3/4/5/6/7) Enter (FA/FB) to win frame", lastLegWinner);
                break;
            case BROWNPOT:
                if (TeamId.A == teamId)
                    lastLegWinner = "PA1";
                else
                    lastLegWinner = "PB1";
                matchIncidentPrompt = new MatchIncidentPrompt(
                                "Enter next point (PA/PB/1/2/3/4/5/6/7) Enter (FA/FB) to win frame", lastLegWinner);
                break;
            case BLUEPOT:
                if (TeamId.A == teamId)
                    lastLegWinner = "PA1";
                else
                    lastLegWinner = "PB1";
                matchIncidentPrompt = new MatchIncidentPrompt(
                                "Enter next point (PA/PB/1/2/3/4/5/6/7) Enter (FA/FB) to win frame", lastLegWinner);
                break;
            case PINKPOT:
                if (TeamId.A == teamId)
                    lastLegWinner = "PA1";
                else
                    lastLegWinner = "PB1";
                matchIncidentPrompt = new MatchIncidentPrompt(
                                "Enter next point (PA/PB/1/2/3/4/5/6/7) Enter (FA/FB) to win frame", lastLegWinner);
                break;
            case BLACKPOT:
                if (TeamId.A == teamId)
                    lastLegWinner = "PA1";
                else
                    lastLegWinner = "PB1";
                matchIncidentPrompt = new MatchIncidentPrompt(
                                "Enter next point (PA/PB/1/2/3/4/5/6/7) Enter (FA/FB) to win frame", lastLegWinner);
                break;
            case FRAMEWON:
                if (TeamId.A == teamId)
                    lastLegWinner = "FA";
                else
                    lastLegWinner = "FB";
                matchIncidentPrompt = new MatchIncidentPrompt("Enter winner of next Frame (FA/FB)", lastLegWinner);
                break;
            case DRAW:
            case MATCHWON:
                if (TeamId.A == teamId)
                    lastLegWinner = "A";
                else
                    lastLegWinner = "B";
                matchIncidentPrompt =
                                new MatchIncidentPrompt(String.format("Match is finished - won by %s", matchWinner));

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
        SnookerMatchIncident matchEvent = new SnookerMatchIncident();

        switch (response.toUpperCase()) {
            case "FA":
                matchEvent.set(SnookerMatchIncidentType.FRAMEWON, TeamId.A);
                break;
            case "FB":
                matchEvent.set(SnookerMatchIncidentType.FRAMEWON, TeamId.B);
                break;
            case "PA":
            case "PA1":
                matchEvent.set(SnookerMatchIncidentType.REDPOT, TeamId.A);
                break;
            case "PB":
            case "PB1":
                matchEvent.set(SnookerMatchIncidentType.REDPOT, TeamId.B);
                break;
            case "PA2":
                matchEvent.set(SnookerMatchIncidentType.YELLOWPOT, TeamId.A);
                break;
            case "PB2":
                matchEvent.set(SnookerMatchIncidentType.YELLOWPOT, TeamId.B);
                break;
            case "PA3":
                matchEvent.set(SnookerMatchIncidentType.GREENPOT, TeamId.A);
                break;
            case "PB3":
                matchEvent.set(SnookerMatchIncidentType.GREENPOT, TeamId.B);
                break;
            case "PA4":
                matchEvent.set(SnookerMatchIncidentType.BROWNPOT, TeamId.A);
                break;
            case "PB4":
                matchEvent.set(SnookerMatchIncidentType.BROWNPOT, TeamId.B);
                break;
            case "PA5":
                matchEvent.set(SnookerMatchIncidentType.BLUEPOT, TeamId.A);
                break;
            case "PB5":
                matchEvent.set(SnookerMatchIncidentType.BLUEPOT, TeamId.B);
                break;
            case "PA6":
                matchEvent.set(SnookerMatchIncidentType.PINKPOT, TeamId.A);
                break;
            case "PB6":
                matchEvent.set(SnookerMatchIncidentType.PINKPOT, TeamId.B);
                break;
            case "PA7":
                matchEvent.set(SnookerMatchIncidentType.BLACKPOT, TeamId.A);
                break;
            case "PB7":
                matchEvent.set(SnookerMatchIncidentType.BLACKPOT, TeamId.B);
                break;
            case "SA":
                matchEvent.set(SnookerMatchIncidentType.SERVEFIRST, TeamId.A);

                break;
            case "SB":
                matchEvent.set(SnookerMatchIncidentType.SERVEFIRST, TeamId.B);
                break;
            case "A":
                matchEvent.set(SnookerMatchIncidentType.WHOISATTABLE, TeamId.A);
                break;
            case "B":
                matchEvent.set(SnookerMatchIncidentType.WHOISATTABLE, TeamId.B);
                break;
            default:
                return null; // invalid input so return null
        }
        return matchEvent;
    }

    private static final String seScoreKey = "frame score";
    private static final String poScoreKey = "point score";
    private static final String onServeKey = "who is at table";
    private static final String serverFirstSetKey = "first pot in current frame";

    /**
     * Returns map
     * 
     * @return map
     */
    @Override
    public LinkedHashMap<String, String> getAsMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put(seScoreKey, String.format("%d-%d", framesA, framesB));
        map.put(poScoreKey, String.format("%d-%d", pointsA, pointsB));
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
        return (framesA == setScoreForWin) || (framesB == setScoreForWin);
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
     * Returns player A frame score
     * 
     * @return framesA
     */
    public int getFramesA() {
        return framesA;
    }

    /**
     * @param framesA sets player A frame score
     */
    public void setFramesA(int framesA) {
        this.framesA = framesA;
    }

    /**
     * Returns player B frame score
     * 
     * @return framesB
     */
    public int getFramesB() {
        return framesB;
    }

    /**
     * @param framesB sets player B frame score
     */
    public void setFramesB(int framesB) {
        this.framesB = framesB;
    }

    /**
     * Returns PairOfIntegers to display all the frame point scores of both teams
     * 
     * @return frameScoreInMatchN
     */
    public PairOfIntegers[] getFrameScoreInMatchN() {
        return frameScoreInMatchN;
    }

    /**
     * set PairOfIntegers
     * 
     * @param frameScoreInMatchN the pair of integers to record frame point score in each set
     * 
     */
    public void setFrameScoreInMatchN(PairOfIntegers[] frameScoreInMatchN) {
        this.frameScoreInMatchN = frameScoreInMatchN;
    }

    /**
     * Returns PairOfIntegers to display all the race point scores of both teams
     * 
     * @return raceScore
     */
    public PairOfIntegers[] getRaceScore() {
        return raceScore;
    }

    /**
     * 
     * 
     * @param matchFormat sets the snooker match format
     * 
     */
    public void setMatchFormat(SnookerMatchFormat matchFormat) {
        this.matchFormat = matchFormat;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((currentMatchState == null) ? 0 : currentMatchState.hashCode());
        result = prime * result + Arrays.hashCode(frameBreakScore);
        result = prime * result + Arrays.hashCode(frameScoreInMatchN);
        result = prime * result + framesA;
        result = prime * result + framesB;
        result = prime * result + highestBreakInCurrentFrameA;
        result = prime * result + highestBreakInCurrentFrameB;
        result = prime * result + ((matchFormat == null) ? 0 : matchFormat.hashCode());
        result = prime * result + pointScoreForWin;
        result = prime * result + pointsA;
        result = prime * result + pointsB;
        result = prime * result + ((potFirstBall == null) ? 0 : potFirstBall.hashCode());
        result = prime * result + ((potFirstColor == null) ? 0 : potFirstColor.hashCode());
        result = prime * result + race;
        result = prime * result + Arrays.hashCode(raceScore);
        result = prime * result + ((serve == null) ? 0 : serve.hashCode());
        result = prime * result + ((serveInFirstSet == null) ? 0 : serveInFirstSet.hashCode());
        result = prime * result + setScoreForWin;
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
        SnookerMatchState other = (SnookerMatchState) obj;
        if (currentMatchState == null) {
            if (other.currentMatchState != null)
                return false;
        } else if (!currentMatchState.equals(other.currentMatchState))
            return false;
        if (!Arrays.equals(frameBreakScore, other.frameBreakScore))
            return false;
        if (!Arrays.equals(frameScoreInMatchN, other.frameScoreInMatchN))
            return false;
        if (framesA != other.framesA)
            return false;
        if (framesB != other.framesB)
            return false;
        if (highestBreakInCurrentFrameA != other.highestBreakInCurrentFrameA)
            return false;
        if (highestBreakInCurrentFrameB != other.highestBreakInCurrentFrameB)
            return false;
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
        if (potFirstBall != other.potFirstBall)
            return false;
        if (potFirstColor == null) {
            if (other.potFirstColor != null)
                return false;
        } else if (!potFirstColor.equals(other.potFirstColor))
            return false;
        if (race != other.race)
            return false;
        if (!Arrays.equals(raceScore, other.raceScore))
            return false;
        if (serve != other.serve)
            return false;
        if (serveInFirstSet != other.serveInFirstSet)
            return false;
        if (setScoreForWin != other.setScoreForWin)
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
        switch (currentMatchState.getSnookerMatchIncidentResultType()) {
            case PREMATCH:
                gamePeriod = GamePeriod.PREMATCH;
                break;
            case MATCHWON:
                gamePeriod = GamePeriod.POSTMATCH;
                break;
            case SERVEFIRST:
            case WHOISATTABLE:
            case REDPOT:
            case YELLOWPOT:
            case GREENPOT:
            case BROWNPOT:
            case BLUEPOT:
            case PINKPOT:
            case BLACKPOT:
            case FRAMEWON:

                switch (this.getFramesA() + this.getFramesB() + 1) {
                    case 1:
                        gamePeriod = GamePeriod.FRAME1;
                        break;
                    case 2:
                        gamePeriod = GamePeriod.FRAME2;
                        break;
                    case 3:
                        gamePeriod = GamePeriod.FRAME3;
                        break;
                    case 4:
                        gamePeriod = GamePeriod.FRAME4;
                        break;
                    case 5:
                        gamePeriod = GamePeriod.FRAME5;
                        break;
                    case 6:
                        gamePeriod = GamePeriod.FRAME6;
                        break;
                    case 7:
                        gamePeriod = GamePeriod.FRAME7;
                        break;
                    case 8:
                        gamePeriod = GamePeriod.FRAME8;
                        break;
                    case 9:
                        gamePeriod = GamePeriod.FRAME9;
                        break;
                    case 10:
                        gamePeriod = GamePeriod.FRAME10;
                        break;
                    case 11:
                        gamePeriod = GamePeriod.FRAME11;
                        break;
                    case 12:
                        gamePeriod = GamePeriod.FRAME12;
                        break;
                    case 13:
                        gamePeriod = GamePeriod.FRAME13;
                        break;
                    case 14:
                        gamePeriod = GamePeriod.FRAME14;
                        break;
                    case 15:
                        gamePeriod = GamePeriod.FRAME15;
                        break;
                    case 16:
                        gamePeriod = GamePeriod.FRAME16;
                        break;
                    case 17:
                        gamePeriod = GamePeriod.FRAME17;
                        break;
                    case 18:
                        gamePeriod = GamePeriod.FRAME18;
                        break;
                    case 19:
                        gamePeriod = GamePeriod.FRAME19;
                        break;
                    case 20:
                        gamePeriod = GamePeriod.FRAME20;
                        break;
                    case 21:
                        gamePeriod = GamePeriod.FRAME21;
                        break;
                    case 22:
                        gamePeriod = GamePeriod.FRAME22;
                        break;
                    case 23:
                        gamePeriod = GamePeriod.FRAME23;
                        break;
                    case 24:
                        gamePeriod = GamePeriod.FRAME24;
                        break;
                    case 25:
                        gamePeriod = GamePeriod.FRAME25;
                        break;
                    case 26:
                        gamePeriod = GamePeriod.FRAME26;
                        break;
                    case 27:
                        gamePeriod = GamePeriod.FRAME27;
                        break;
                    case 28:
                        gamePeriod = GamePeriod.FRAME28;
                        break;
                    case 29:
                        gamePeriod = GamePeriod.FRAME29;
                        break;
                    case 30:
                        gamePeriod = GamePeriod.FRAME30;
                        break;
                    case 31:
                        gamePeriod = GamePeriod.FRAME31;
                        break;
                    case 32:
                        gamePeriod = GamePeriod.FRAME32;
                        break;
                    case 33:
                        gamePeriod = GamePeriod.FRAME33;
                        break;
                    case 34:
                        gamePeriod = GamePeriod.FRAME34;
                        break;
                    case 35:
                        gamePeriod = GamePeriod.FRAME35;
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
     * Returns the sequence id to use for frame based markets
     * 
     * @param setOffset 0 = current set, 1 = next set etc
     * @return null if specified set can't occur, else the sequence id
     */
    @JsonIgnore
    public String getSequenceIdforFrame(int setOffset) {
        int setNo = this.getFramesA() + this.getFramesB() + setOffset + 1;
        if (setNo > matchFormat.getnFramesInMatch())
            return null;
        else
            return String.format("F%d", setNo);
    }

    /**
     * Returns the sequence id to use for point based markets (within the currently active game or tie break)
     * 
     * @param pointOffset 0 = current frame, 1 = next frame etc
     * @return null if specified point can't occur, else the sequence id
     */
    @JsonIgnore
    public String getSequenceIdForPoint(int pointOffset) {
        int pointNo = this.getPointsA() + this.getPointsB() + pointOffset;
        if (this.isPointMayBePlayed(pointNo))
            return String.format("F%d.%d", this.getSetNo(), pointNo);
        else
            return null;
    }

    /**
     * Returns true if within a frame and point no n (starting at 1 for first point in frame) might be played
     * 
     * @param n point No
     * @return true or false
     */
    @JsonIgnore
    boolean isPointMayBePlayed(int n) {
        return n >= getPointNo();
    }

    /**
     * Returns frame no in range 1-5
     * 
     * @return frameNo
     */
    @JsonIgnore
    public int getSetNo() {
        return framesA + framesB + 1;
    }

    /**
     * Returns point no starting at 1 for first point of frame
     * 
     * @return pointNo
     */
    @JsonIgnore
    public int getPointNo() {
        int pointNo = pointsA + pointsB + 1;
        return pointNo + 1;
    }

    /**
     * Returns true if in preMatch
     * 
     * @return true or false
     */
    @Override
    public boolean preMatch() {
        return this.serveInFirstSet == TeamId.UNKNOWN;
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
    public SimpleMatchState generateSimpleMatchState() {
        Map<String, PairOfIntegers> pair = new HashMap<String, PairOfIntegers>();
        for (int i = 1; i <= frameScoreInMatchN.length; i++)
            pair.put("scoreInFrame" + i, frameScoreInMatchN[i - 1]);
        SnookerSimpleMatchState simpleMatchState = new SnookerSimpleMatchState(this.preMatch(), this.isMatchCompleted(),
                        this.getFramesA(), this.getFramesB(), this.getPointsA(), this.getPointsB(), this.getServe(),
                        this.getServeInFirstSet(), pair);
        return simpleMatchState;
    }

    public int getMatchCenturies(TeamId teamId) {
        int countA = 0;
        int countB = 0;
        for (int i = 0; i < frameBreakScore.length; i++) {

            if (frameBreakScore[i].A >= 100)
                countA++;
            if (frameBreakScore[i].B >= 100)
                countB++;
        }
        if (teamId == TeamId.A) {
            return countA;
        } else if (teamId == TeamId.B) {
            return countB;
        } else
            return countA + countB;
    }

    @Override
    public AlgoMatchState generateMatchStateForMatchResult(MatchResultMap matchManualResult) {
        int framesA = 0;
        int framesB = 0;
        Map<String, MatchResultElement> map = matchManualResult.getMap();
        int nFrames = ((SnookerMatchFormat) this.getMatchFormat()).getnFramesInMatch();
        PairOfIntegers[] framesScore = new PairOfIntegers[nFrames];
        int iPlusOne = 0;
        for (int i = 0; i < nFrames; i++) {
            iPlusOne = i + 1;
            MatchResultElement setScoreString = map.get("frame" + iPlusOne + "Score");
            if (!setScoreString.getValue().equals("-1-0")) {
                framesScore[i] = setScoreString.valueAsPairOfIntegers();
                if (framesScore[i].A > framesScore[i].B) {
                    framesA++;
                } else if (framesScore[i].A < framesScore[i].B) {
                    framesB++;
                } else {
                    System.out.println("Should never have equal number : " + framesScore[i].A + "-" + framesScore[i].B);
                }
            } else
                System.out.println("Frame " + iPlusOne + "not played");
        }
        SnookerMatchState endMatchState = (SnookerMatchState) this.copy();
        endMatchState.setFrameScoreInMatchN(framesScore);
        endMatchState.setFramesA(framesA);
        endMatchState.setFramesB(framesB);
        return endMatchState;
    }
}
