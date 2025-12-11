package ats.algo.sport.handball;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ats.algo.core.GamePeriod;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchIncidentPrompt;
import ats.algo.core.baseclasses.MatchIncidentResult;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.common.DatafeedMatchIncident;
import ats.algo.core.common.ElapsedTimeMatchIncident;
import ats.algo.core.common.ElapsedTimeMatchIncident.ElapsedTimeMatchIncidentType;
import ats.algo.core.matchresult.MatchResultMap;
import ats.algo.core.common.TeamId;
import ats.algo.sport.handball.HandballMatchIncident.HandballMatchIncidentType;

/**
 * AlgoMatchState class for Handball
 * 
 * @author Jin
 * 
 */
public class HandballMatchState extends AlgoMatchState {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private int currentPeriodGoalsA;
    private int currentPeriodGoalsB;
    @JsonIgnore
    private int previousPeriodGoalsA;
    @JsonIgnore
    private int previousPeriodGoalsB;
    @JsonIgnore
    private int thisSendoffLength = 2 * 60; // hard set to be 2 minutes send off
    private int goalsFirstHalfA;
    private int goalsFirstHalfB;
    private int goalsSecondHalfA;
    private int goalsSecondHalfB;
    private int overtimeGoalsA;
    private int overtimeGoalsB;

    private BallPosition ballPossession;
    @JsonIgnore
    private int shootOutGoalsHome;
    @JsonIgnore
    private int shootOutGoalsAway;
    @JsonIgnore
    private ArrayList<Integer> twoMinsSuspensionA = new ArrayList<Integer>();
    @JsonIgnore
    private ArrayList<Integer> twoMinsSuspensionB = new ArrayList<Integer>();
    private int twoMinsSuspendedNoA;
    private int twoMinsSuspendedNoB;
    private TeamId teamScoringLastGoal;
    private int goalsA;
    private int goalsB;

    // private double simuGoalsA;
    // private double simuGoalsB;
    @JsonIgnore
    private int elapsedTimeAtLastMatchIncidentSecs; // the time from the last
    private int elapsedTimeAtLastGoalSecs;
    public HandballMatchPeriod matchPeriodInWhichLastGoalScored;
    private int normalTimeGoalsA;
    private int normalTimeGoalsB;
    private int elapsedTimeSecs;
    private int elapsedTimeThisPeriodSecs;
    @JsonIgnore
    private int overtimeNo;
    private HandballMatchPeriod matchPeriod; // the state following the most
    private int normalHalfsSecs;
    private final int extraTimeSecs;
    private final HandballMatchFormat matchFormat;
    // private static final int timeIncrementSecs = 10;

    /**
     * Json class constructor. Not for general use
     */
    public HandballMatchState() {
        this(new HandballMatchFormat());
        // super();
        // currentPeriodGoalsA = 0;
        // currentPeriodGoalsB = 0;
        // previousPeriodGoalsA = 0;
        // previousPeriodGoalsB = 0;
        // thisSendoffLength = 2 * 60; // hard set to be 2 minutes send off
        // goalsFirstHalfA = 0;
        // goalsFirstHalfB = 0;
        // goalsSecondHalfA = 0;
        // goalsSecondHalfB = 0;
        // shootOutGoalsHome = 0;
        // shootOutGoalsAway = 0;
        // twoMinsSuspensionA = new ArrayList<Integer>();
        // twoMinsSuspensionB = new ArrayList<Integer>();
        // twoMinsSuspendedNoA = 0;
        // twoMinsSuspendedNoB = 0;
        // goalsA = 0;
        // goalsB = 0;
        // elapsedTimeSecs = 0;
        // elapsedTimeThisPeriodSecs = 0;
        // matchPeriod = HandballMatchPeriod.IN_SECOND_HALF;
        // normalHalfsSecs = 0;
        // extraTimeSecs = 0;
        // this.matchFormat = null;
        // ballPossession = new BallPosition();
    }

    /**
     * Class constructor
     * 
     * @param matchFormat
     */
    public HandballMatchState(MatchFormat matchFormat) {
        super();
        this.matchFormat = (HandballMatchFormat) matchFormat;
        matchPeriod = HandballMatchPeriod.PREMATCH;
        normalHalfsSecs =
                        this.matchFormat.getNormalTimeMinutes() * 30; /* 20 for 3 periods game, 30 for 2 periods game */
        extraTimeSecs = this.matchFormat.getExtraTimeMinutes() * 60;
        this.matchFormat.isPenaltiesPossible();
        elapsedTimeAtLastGoalSecs = -1;
        elapsedTimeAtLastMatchIncidentSecs = -1;
        ballPossession = new BallPosition();
        twoMinsSuspensionA = new ArrayList<Integer>();
        twoMinsSuspensionB = new ArrayList<Integer>();
    }

    /**
     * Get the ball holding information class
     * 
     * @return ballPossession
     */

    public BallPosition getBallPossession() {
        return ballPossession;
    }

    /**
     * Set the ball holding information class
     * 
     * @param ballPossession
     */
    public void setBallPossession(BallPosition ballPosession) {
        this.ballPossession = ballPosession;
    }

    /**
     * Get time for a normal half match
     * 
     * @return secondHalfPointsA
     */
    public int getNormalHalfsSecs() {
        return normalHalfsSecs;
    }

    /**
     * Set time for a normal half match
     * 
     * @return normalHalfsSecs
     */
    public void setNormalHalfsSecs(int normalHalfsSecs) {
        this.normalHalfsSecs = normalHalfsSecs;
    }

    /**
     * Get 2 minutes sin bin ending time for team A players in a ArrayList
     * 
     * @return twoMinsSuspensionA
     */
    public ArrayList<Integer> getTwoMinsSuspensionA() {
        return twoMinsSuspensionA;
    }

    /**
     * Get team A normal time goals
     * 
     * @return normalTimeGoalsA
     */
    public int getNormalTimeGoalsA() {
        return normalTimeGoalsA;
    }

    /**
     * Set team A normal time goals
     * 
     * @param normalTimeGoalsA
     */
    public void setNormalTimeGoalsA(int normalTimeGoalsA) {
        this.normalTimeGoalsA = normalTimeGoalsA;
    }

    /**
     * Get team B normal time goals
     * 
     * @return normalTimeGoalsB
     */
    public int getNormalTimeGoalsB() {
        return normalTimeGoalsB;
    }

    /**
     * Set team B normal time goals
     * 
     * @param normalTimeGoalsB
     */
    public void setNormalTimeGoalsB(int normalTimeGoalsB) {
        this.normalTimeGoalsB = normalTimeGoalsB;
    }

    /**
     * Set 2 minutes sin bin ending time for team A players in a ArrayList
     * 
     * @param twoMinsSuspensionA
     */
    public void setTwoMinsSuspensionA(ArrayList<Integer> twoMinsSuspensionA) {
        this.twoMinsSuspensionA = twoMinsSuspensionA;
    }

    /**
     * Get two minutes sin bin ending time for team B players in a ArrayList
     * 
     * @return twoMinsSuspensionB
     */
    public ArrayList<Integer> getTwoMinsSuspensionB() {
        return twoMinsSuspensionB;
    }

    /**
     * Set two minutes sin bin ending time for team B players in a ArrayList
     * 
     * @param twoMinsSuspensionB
     */
    public void setTwoMinsSuspensionB(ArrayList<Integer> twoMinsSuspensionB) {
        this.twoMinsSuspensionB = twoMinsSuspensionB;
    }

    /**
     * Get number of players currently on two minutes sin bin for team A
     * 
     * @return twoMinsSuspendedNoA
     */
    public int getTwoMinsSuspensionNumberA() {
        return twoMinsSuspendedNoA;
    }

    /**
     * Set number of players currently on two minutes sin bin for team A
     * 
     * @param twoMinsSinBinA
     */
    public void setTwoMinsSuspensionNumberA(int sendOffA) {
        this.twoMinsSuspendedNoA = sendOffA;
    }

    /**
     * Get number of players currently on two minutes sin bin for team B
     * 
     * @return twoMinsSinBinB
     */
    public int getTwoMinsSuspensionNumberB() {
        return twoMinsSuspendedNoB;
    }

    /**
     * Set number of players currently on two minutes sin bin for team B
     * 
     * @param twoMinsSinBinB
     */
    public void setTwoMinsSuspensionNumberB(int sendOffB) {
        this.twoMinsSuspendedNoB = sendOffB;
    }

    /**
     * Get no of points scored by team A in the second half
     * 
     * @return goalsSecondHalfA
     */
    public int getGoalsSecondHalfA() {
        return goalsSecondHalfA;
    }

    /**
     * Set no of points scored by team A in the second half
     * 
     * @param goalsSecondHalfA
     */
    public void setGoalsSecondHalfA(int goalsHomeSecondHalf) {
        this.goalsSecondHalfA = goalsHomeSecondHalf;
    }

    /**
     * Get no of points scored by team B in the second half
     * 
     * @return goalsSecondHalfB
     */
    public int getGoalsSecondHalfB() {
        return goalsSecondHalfB;
    }

    /**
     * Set no of points scored by team B in the second half
     * 
     * @param goalsSecondHalfB
     */
    public void setGoalsSecondHalfB(int goalsAwaySecondHalf) {
        this.goalsSecondHalfB = goalsAwaySecondHalf;
    }

    /**
     * Get no of points scored by team A in the first half
     * 
     * @return goalsFirstHalfA
     */
    public int getGoalsFirstHalfA() {
        return goalsFirstHalfA;
    }

    /**
     * Set no of points scored by team A in the first half
     * 
     * @param goalsFirstHalfA
     */
    public void setGoalsFirstHalfA(int goalsHomeFirstHalf) {
        this.goalsFirstHalfA = goalsHomeFirstHalf;
    }

    /**
     * Get no of points scored by team B in the first half
     * 
     * @return goalsFirstHalfB
     */
    public int getGoalsFirstHalfB() {
        return goalsFirstHalfB;
    }

    /**
     * Set no of points scored by team B in the first half
     * 
     * @param goalsFirstHalfB
     */
    public void setGoalsFirstHalfB(int goalsAwayFirstHalf) {
        this.goalsFirstHalfB = goalsAwayFirstHalf;
    }

    /**
     * Get no of points scored by team A
     * 
     * @return pointsA
     */
    public int getGoalsA() {
        return goalsA;
    }

    /**
     * Set no of points scored by team A
     * 
     * @param pointsA
     */
    public void setGoalsA(int goalsHome) {
        this.goalsA = goalsHome;
    }

    /**
     * Get no of points scored by team B
     * 
     * @return pointsB
     */
    public int getGoalsB() {
        return goalsB;
    }

    /**
     * Set no of points scored by team B
     * 
     * @param pointsB
     */
    public void setGoalsB(int goalsAway) {
        this.goalsB = goalsAway;
    }

    /**
     * Get the elapsed time in the match
     * 
     * @return elapsedTimeSecs
     */
    public int getElapsedTimeSecs() {
        return elapsedTimeSecs;
    }

    /**
     * Set the elapsed time in the match
     * 
     * @param elapsedTimeSecs
     */
    public void setElapsedTimeSecs(int elapsedTimeSecs) {
        this.elapsedTimeSecs = elapsedTimeSecs;
    }

    /**
     * Set team scoring the most recent goal
     * 
     * @param teamScoringLastGoal
     */
    public void setTeamScoringLastGoal(TeamId teamId) {
        this.teamScoringLastGoal = teamId;
    }

    /**
     * Get team scoring the most recent goal
     * 
     * @return UNKNOWN if no goal scored, else A or B
     */
    public TeamId getTeamScoringLastGoal() {
        return teamScoringLastGoal;
    }

    /**
     * Get the elapsed time in the current period
     * 
     * @return elapsedTimeThisPeriodSecs
     */
    public int getElapsedTimeThisPeriodSecs() {
        return elapsedTimeThisPeriodSecs;
    }

    /**
     * Set the time elapsed during the current period
     * 
     * @param elapsedTimeThisPeriodSecs
     */
    public void setElapsedTimeThisPeriodSecs(int elapsedTimeThisPeriodSecs) {
        this.elapsedTimeThisPeriodSecs = elapsedTimeThisPeriodSecs;
    }

    /**
     * Get overtime period no
     * 
     * @return overtimeNo
     */
    public int getOvertimeNo() {
        return overtimeNo;
    }

    /**
     * Set overtime period no
     * 
     * @param overtimeNo
     */
    public void setOvertimeNo(int overtimeNo) {
        this.overtimeNo = overtimeNo;
    }

    public int getThisSendoffLength() {
        return thisSendoffLength;
    }

    public void setThisSendoffLength(int thisSendoffLength) {
        this.thisSendoffLength = thisSendoffLength;
    }

    public int getOvertimeGoalsA() {
        return overtimeGoalsA;
    }

    public void setOvertimeGoalsA(int overtimeGoalsA) {
        this.overtimeGoalsA = overtimeGoalsA;
    }

    public int getOvertimeGoalsB() {
        return overtimeGoalsB;
    }

    public void setOvertimeGoalsB(int overtimeGoalsB) {
        this.overtimeGoalsB = overtimeGoalsB;
    }

    public int getShootOutGoalsHome() {
        return shootOutGoalsHome;
    }

    public void setShootOutGoalsHome(int shootOutGoalsHome) {
        this.shootOutGoalsHome = shootOutGoalsHome;
    }

    public int getShootOutGoalsAway() {
        return shootOutGoalsAway;
    }

    public void setShootOutGoalsAway(int shootOutGoalsAway) {
        this.shootOutGoalsAway = shootOutGoalsAway;
    }

    public int getTwoMinsSuspendedNoA() {
        return twoMinsSuspendedNoA;
    }

    public void setTwoMinsSuspendedNoA(int twoMinsSuspendedNoA) {
        this.twoMinsSuspendedNoA = twoMinsSuspendedNoA;
    }

    public int getTwoMinsSuspendedNoB() {
        return twoMinsSuspendedNoB;
    }

    public void setTwoMinsSuspendedNoB(int twoMinsSuspendedNoB) {
        this.twoMinsSuspendedNoB = twoMinsSuspendedNoB;
    }

    public int getElapsedTimeAtLastMatchIncidentSecs() {
        return elapsedTimeAtLastMatchIncidentSecs;
    }

    public void setElapsedTimeAtLastMatchIncidentSecs(int elapsedTimeAtLastMatchIncidentSecs) {
        this.elapsedTimeAtLastMatchIncidentSecs = elapsedTimeAtLastMatchIncidentSecs;
    }

    public int getElapsedTimeAtLastGoalSecs() {
        return elapsedTimeAtLastGoalSecs;
    }

    public void setElapsedTimeAtLastGoalSecs(int elapsedTimeAtLastGoalSecs) {
        this.elapsedTimeAtLastGoalSecs = elapsedTimeAtLastGoalSecs;
    }

    public HandballMatchPeriod getMatchPeriodInWhichLastGoalScored() {
        return matchPeriodInWhichLastGoalScored;
    }

    public void setMatchPeriodInWhichLastGoalScored(HandballMatchPeriod matchPeriodInWhichLastGoalScored) {
        this.matchPeriodInWhichLastGoalScored = matchPeriodInWhichLastGoalScored;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public int getExtraTimeSecs() {
        return extraTimeSecs;
    }

    public static String getGoalskey() {
        return goalsKey;
    }

    public static String getFirsthalfgoalskey() {
        return firstHalfGoalsKey;
    }

    public static String getSecondhalfgoalskey() {
        return secondHalfGoalsKey;
    }

    public static String getElapsedtimekey() {
        return elapsedTimeKey;
    }

    public static String getMatchperiodkey() {
        return matchPeriodKey;
    }

    public static String getYellowcardkey() {
        return yellowCardKey;
    }

    public static String getShootoutkey() {
        return shootOutKey;
    }

    public static String getBallpossessionkey() {
        return ballPossessionKey;
    }

    public void setCurrentPeriodGoalsA(int currentPeriodGoalsA) {
        this.currentPeriodGoalsA = currentPeriodGoalsA;
    }

    public void setCurrentPeriodGoalsB(int currentPeriodGoalsB) {
        this.currentPeriodGoalsB = currentPeriodGoalsB;
    }

    public void setPreviousPeriodGoalsA(int previousPeriodGoalsA) {
        this.previousPeriodGoalsA = previousPeriodGoalsA;
    }

    public void setPreviousPeriodGoalsB(int previousPeriodGoalsB) {
        this.previousPeriodGoalsB = previousPeriodGoalsB;
    }

    /**
     * Get the current period no
     * 
     * @return
     */
    @JsonIgnore
    public int getPeriodNo() {
        int n = 0;
        switch (matchPeriod) {
            case PREMATCH:
            case IN_FIRST_HALF:
                n = 1;
                break;
            case AT_HALF_TIME:
            case IN_SECOND_HALF:
                n = 2;
                break;
            case AT_FULL_TIME:
            case IN_EXTRATIME_PERIOD:
                n = 3;
                break;
            case AT_EXTRATIME_PERIOD_END:
                n = 6;
                break;

            case IN_SHOOTOUT_PERIOD:
                n = 4;
                break;
            case AT_SHOOTOUT_PERIOD_END:
                n = 7;
                break;
            case MATCH_COMPLETED:
                n = 5;
                break;
            default:
                throw new IllegalArgumentException("In match state can not return valid getPeriodNo()");
        }
        return n;
    }

    /**
     * Get the goal sequence no
     * 
     * @return
     */
    @JsonIgnore
    public int getGoalNo() {
        return goalsA + goalsB + 1;
    }

    /**
     * Get the current matchPeriod
     * 
     * @return matchPeriod
     */
    public HandballMatchPeriod getMatchPeriod() {
        return matchPeriod;
    }

    /**
     * Set the current match period
     * 
     * @param matchPeriod
     */
    public void setMatchPeriod(HandballMatchPeriod matchPeriod) {
        this.matchPeriod = matchPeriod;
    }

    /**
     * Get # goals scored by team A in current period
     * 
     * @return currentPeriodGoalsA
     */
    public int getCurrentPeriodGoalsA() {

        return currentPeriodGoalsA;
    }

    /**
     * Get # goals scored by team B in current period
     * 
     * @return currentPeriodGoalsB
     */
    public int getCurrentPeriodGoalsB() {

        return currentPeriodGoalsB;
    }

    @Override
    public MatchIncidentResult updateStateForIncident(MatchIncident matchIncident,
                    boolean autosyncMatchStateToFeedOnMismatch) {
        setElapsedTime((matchIncident.getElapsedTimeSecs()));
        elapsedTimeAtLastMatchIncidentSecs = elapsedTimeSecs;
        setClockTimeOfLastElapsedTimeFromIncident();

        if (matchIncident instanceof DatafeedMatchIncident)
            return super.updateStateForIncident(matchIncident, autosyncMatchStateToFeedOnMismatch);


        if (matchIncident.getClass() == HandballMatchIncident.class) {

            TeamId teamId = ((HandballMatchIncident) matchIncident).getTeamId();

            switch (((HandballMatchIncident) matchIncident).getIncidentSubType()) {
                case TWO_MINS_SUSPENSION_START:
                    switch (teamId) {
                        case A:
                            twoMinsSuspensionTrigger(twoMinsSuspensionA, elapsedTimeSecs);
                            twoMinsSuspendedNoA++;
                            break;

                        case B:
                            twoMinsSuspensionTrigger(twoMinsSuspensionB, elapsedTimeSecs);
                            twoMinsSuspendedNoB++;
                            break;
                        default:
                            throw new IllegalArgumentException("team Id Unknown");

                    }
                    break;

                case TWO_MINS_SUSPENSION_END: // power play end
                    switch (teamId) {
                        case A:
                            if (twoMinsSuspendedNoA == 0)
                                throw new IllegalArgumentException("Send-Off number is wrong");
                            twoMinsSuspendedNoA--;
                            cleanSuspensionTimer(twoMinsSuspensionA, twoMinsSuspendedNoA);

                            break;

                        case B:
                            if (twoMinsSuspendedNoB == 0)
                                throw new IllegalArgumentException("Send-Off number is wrong");
                            twoMinsSuspendedNoB--;
                            cleanSuspensionTimer(twoMinsSuspensionB, twoMinsSuspendedNoB);

                            break;
                        default:
                            throw new IllegalArgumentException("team Id Unknown");

                    }
                    break;

                case BALL_POSESSION_SETTING:
                    ballPossession.setBallHoldingTeam(teamId);
                    break;

                case GOAL:
                    elapsedTimeAtLastGoalSecs = elapsedTimeSecs;
                    matchPeriodInWhichLastGoalScored = matchPeriod;
                    ballPossession.resetBallPosition();
                    switch (teamId) {
                        case A:
                            if (!matchPeriod.equals(HandballMatchPeriod.IN_SHOOTOUT_PERIOD)) {

                                if (!matchPeriod.equals(HandballMatchPeriod.IN_SHOOTOUT_PERIOD)
                                                && !matchPeriod.equals(HandballMatchPeriod.IN_EXTRATIME_PERIOD))
                                    goalsA++;
                                if (matchPeriod.equals(HandballMatchPeriod.IN_FIRST_HALF))
                                    goalsFirstHalfA++;
                                if (matchPeriod.equals(HandballMatchPeriod.IN_SECOND_HALF))
                                    goalsSecondHalfA++;
                                if (matchPeriod.equals(HandballMatchPeriod.IN_EXTRATIME_PERIOD))
                                    overtimeGoalsA++;

                                currentPeriodGoalsA++;
                                teamScoringLastGoal = TeamId.A;

                            } else {

                            }
                            break;
                        case B:
                            if (!matchPeriod.equals(HandballMatchPeriod.IN_SHOOTOUT_PERIOD)) {

                                if (!matchPeriod.equals(HandballMatchPeriod.IN_SHOOTOUT_PERIOD)
                                                && !matchPeriod.equals(HandballMatchPeriod.IN_EXTRATIME_PERIOD))
                                    goalsB++;
                                currentPeriodGoalsB++;

                                if (matchPeriod.equals(HandballMatchPeriod.IN_FIRST_HALF))
                                    goalsFirstHalfB++;
                                if (matchPeriod.equals(HandballMatchPeriod.IN_SECOND_HALF))
                                    goalsSecondHalfB++;
                                if (matchPeriod.equals(HandballMatchPeriod.IN_EXTRATIME_PERIOD))
                                    overtimeGoalsB++;
                                teamScoringLastGoal = TeamId.B;

                            } else {

                            }
                            break;
                        case UNKNOWN:
                            throw new IllegalArgumentException("TeamId must be specified for goal scored");
                    }

                    break;
                default:
                    throw new IllegalArgumentException("Unknown match incidences type");
            }
        } else {// must be ElapsedTimeMatchIncident
            this.setClockStatus(((ElapsedTimeMatchIncident) matchIncident).getIncidentSubType());

            switch (((ElapsedTimeMatchIncident) matchIncident).getIncidentSubType()) {

                case SET_MATCH_CLOCK:
                    // if (matchPeriod.equals(HandballMatchPeriod.IN_SHOOTOUT_PERIOD))
                    // shootOutTimeCounter++;
                    break;

                case SET_PERIOD_START:
                    switch (matchPeriod) {
                        case PREMATCH:
                            matchPeriod = HandballMatchPeriod.IN_FIRST_HALF;
                            break;

                        case IN_FIRST_HALF:
                            break;

                        case AT_HALF_TIME:
                            matchPeriod = HandballMatchPeriod.IN_SECOND_HALF;
                            break;

                        case IN_SECOND_HALF:
                            break;

                        case AT_FULL_TIME:
                            if (extraTimeSecs == 0 || goalsA != goalsB) {
                                matchPeriod = HandballMatchPeriod.MATCH_COMPLETED;
                            } else {
                                matchPeriod = HandballMatchPeriod.IN_EXTRATIME_PERIOD;
                            }
                            break;

                        case IN_EXTRATIME_PERIOD:
                            break;

                        case AT_EXTRATIME_PERIOD_END:
                            break;

                        case MATCH_COMPLETED:
                            break;

                        default:
                            break;

                    }
                    break;
                case SET_PERIOD_END:
                    switch (matchPeriod) {
                        case PREMATCH:
                            break;
                        case IN_FIRST_HALF:
                            matchPeriod = HandballMatchPeriod.AT_HALF_TIME;
                            previousPeriodGoalsA = currentPeriodGoalsA;
                            previousPeriodGoalsB = currentPeriodGoalsB;
                            currentPeriodGoalsA = 0;
                            currentPeriodGoalsB = 0;
                            setElapsedTime(normalHalfsSecs);
                            elapsedTimeAtLastMatchIncidentSecs = normalHalfsSecs;
                            setClockTimeOfLastElapsedTimeFromIncident();
                            break;

                        case AT_HALF_TIME:
                            break;

                        case IN_SECOND_HALF:
                            if (goalsA != goalsB || extraTimeSecs == 0) {
                                matchPeriod = HandballMatchPeriod.MATCH_COMPLETED;
                            } else {
                                matchPeriod = HandballMatchPeriod.IN_EXTRATIME_PERIOD;
                            }
                            normalTimeGoalsA = goalsA;
                            normalTimeGoalsB = goalsB;
                            previousPeriodGoalsA = currentPeriodGoalsA;
                            previousPeriodGoalsB = currentPeriodGoalsB;
                            currentPeriodGoalsA = 0;
                            currentPeriodGoalsB = 0;
                            setElapsedTime(normalHalfsSecs * 2);
                            elapsedTimeAtLastMatchIncidentSecs = normalHalfsSecs;
                            setClockTimeOfLastElapsedTimeFromIncident();
                            break;

                        case AT_FULL_TIME:
                            break;

                        case IN_EXTRATIME_PERIOD:
                            if (goalsA == goalsB) {
                                matchPeriod = HandballMatchPeriod.AT_EXTRATIME_PERIOD_END;
                            } else {
                                matchPeriod = HandballMatchPeriod.MATCH_COMPLETED;
                            }
                            previousPeriodGoalsA = currentPeriodGoalsA;
                            previousPeriodGoalsB = currentPeriodGoalsB;
                            currentPeriodGoalsA = 0;
                            currentPeriodGoalsB = 0;
                            setElapsedTime(normalHalfsSecs * 3 + (overtimeNo + 1) * extraTimeSecs);
                            if (overtimeNo == 0) {
                                elapsedTimeAtLastMatchIncidentSecs = normalHalfsSecs;
                            } else {
                                elapsedTimeAtLastMatchIncidentSecs = extraTimeSecs;
                            }

                            setClockTimeOfLastElapsedTimeFromIncident();
                            break;

                        case AT_EXTRATIME_PERIOD_END:
                            break;

                        case MATCH_COMPLETED:
                            break;
                        default:
                            throw new IllegalArgumentException("SET_PERIOD_END Unknown");
                    }

                case SET_STOP_MATCH_CLOCK: // stop the independent match clock timer - for those sports where this is
                                           // possible
                case SET_START_MATCH_CLOCK: // start the match clock timer
                    break;

            }
        }

        return matchPeriod;
    }

    private void setElapsedTime(int elapsedTimeSecs) {
        this.elapsedTimeSecs = elapsedTimeSecs;
        switch (matchPeriod) {
            case PREMATCH:
            case AT_HALF_TIME:
            case AT_FULL_TIME:
            case AT_EXTRATIME_PERIOD_END:
            case MATCH_COMPLETED:
                /*
                 * do nothing
                 */
                break;
            case IN_FIRST_HALF:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs;
                break;
            case IN_SECOND_HALF:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs - normalHalfsSecs;
                break;
            case IN_EXTRATIME_PERIOD:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs - 2 * normalHalfsSecs - overtimeNo * (extraTimeSecs - 1);
                break;
            case AT_SHOOTOUT_PERIOD_END:
                break;
            case IN_SHOOTOUT_PERIOD:
                break;
            default:
                break;
        }
    }

    private void cleanSuspensionTimer(ArrayList<Integer> twoMinsSuspension, int twoMinsSuspendedNo) {
        // different from reset in icehockey

        boolean toClean = true;
        for (int i = 0; i < twoMinsSuspension.size(); i++) {
            if (toClean) // only clean one at a time
                twoMinsSuspension.remove(i);
            toClean = false;
        }
        if (twoMinsSuspension.size() != twoMinsSuspendedNo)
            throw new IllegalArgumentException(
                            "Timer size equals zero!" + twoMinsSuspension.size() + " " + twoMinsSuspendedNo);

    }

    private void twoMinsSuspensionTrigger(ArrayList<Integer> twoMinsSuspensionLocal, int elapsedTimeSecsLocal) {
        // this function records suspension ones per call, also clean settled suspension
        if (twoMinsSuspensionLocal.size() == 0) {
            twoMinsSuspensionLocal.add(elapsedTimeSecsLocal + thisSendoffLength);
        } else {

            twoMinsSuspensionLocal.add(elapsedTimeSecsLocal + thisSendoffLength);

            for (int i = 0; i < twoMinsSuspensionLocal.size(); i++) {

                if (twoMinsSuspensionLocal.get(i) < elapsedTimeSecsLocal)
                    twoMinsSuspensionLocal.remove(i); // clean settled suspension
            }

        }

    }

    @Override
    public AlgoMatchState copy() {
        HandballMatchState cc = new HandballMatchState(matchFormat);
        cc.setEqualTo(this);
        return cc;

    }

    @Override
    public void setEqualTo(AlgoMatchState matchState) {

        this.setGoalsA(((HandballMatchState) matchState).getGoalsA());
        this.setGoalsB(((HandballMatchState) matchState).getGoalsB());

        this.setGoalsFirstHalfA(((HandballMatchState) matchState).getGoalsFirstHalfA());
        this.setGoalsFirstHalfB(((HandballMatchState) matchState).getGoalsFirstHalfB());

        this.setGoalsSecondHalfA(((HandballMatchState) matchState).getGoalsSecondHalfA());
        this.setGoalsSecondHalfB(((HandballMatchState) matchState).getGoalsSecondHalfB());

        this.setElapsedTimeSecs(((HandballMatchState) matchState).getElapsedTimeSecs());
        this.setElapsedTimeThisPeriodSecs(((HandballMatchState) matchState).getElapsedTimeThisPeriodSecs());
        this.setMatchPeriod(((HandballMatchState) matchState).getMatchPeriod());
        this.setCurrentPeriodGoalsA(((HandballMatchState) matchState).getCurrentPeriodGoalsA());
        this.setCurrentPeriodGoalsB(((HandballMatchState) matchState).getCurrentPeriodGoalsB());
        this.setOvertimeNo(((HandballMatchState) matchState).getOvertimeNo());
        this.setOvertimeGoalsA(((HandballMatchState) matchState).getOvertimeGoalsA());
        this.setOvertimeGoalsB(((HandballMatchState) matchState).getOvertimeGoalsB());
        this.setTeamScoringLastGoal(((HandballMatchState) matchState).getTeamScoringLastGoal());
        /* CJ amended */
        this.setTwoMinsSuspensionNumberA(((HandballMatchState) matchState).getTwoMinsSuspensionNumberA());
        this.setTwoMinsSuspensionNumberB(((HandballMatchState) matchState).getTwoMinsSuspensionNumberB());
        // this.twoMinsSuspensionA.addAll((HandballMatchState) matchState).getTwoMinsSuspensionA());
        this.twoMinsSuspensionA.clear();
        this.twoMinsSuspensionA = cloneIntegerList(((HandballMatchState) matchState).getTwoMinsSuspensionA());

        this.twoMinsSuspensionB.clear();
        this.twoMinsSuspensionB = cloneIntegerList(((HandballMatchState) matchState).getTwoMinsSuspensionB());

        this.setNormalHalfsSecs(((HandballMatchState) matchState).getNormalHalfsSecs());
        try {
            this.ballPossession = (BallPosition) (((HandballMatchState) matchState).ballPossession).clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

    }

    public static ArrayList<Integer> cloneIntegerList(ArrayList<Integer> list) {
        ArrayList<Integer> clone = new ArrayList<Integer>(list.size());
        for (Integer item : list)
            clone.add(item);
        return clone;
    }

    @JsonIgnore
    @Override
    public MatchIncidentPrompt getNextPrompt() {
        MatchIncidentPrompt matchIncidentPrompt = null;
        if (matchPeriod == HandballMatchPeriod.MATCH_COMPLETED)
            matchIncidentPrompt = new MatchIncidentPrompt("Match finished");
        else if (matchPeriod == HandballMatchPeriod.PREMATCH || matchPeriod == HandballMatchPeriod.AT_HALF_TIME
                        || matchPeriod == HandballMatchPeriod.AT_FULL_TIME
                        || matchPeriod == HandballMatchPeriod.AT_EXTRATIME_PERIOD_END)
            matchIncidentPrompt = new MatchIncidentPrompt("S - start match", "S");

        else
            matchIncidentPrompt = new MatchIncidentPrompt(
                            "Next event (Nnn-Nothing happened for nn periods, H-Home goal, A-Away goal, A-Away goal, GH/A-Home/Away 2 minutes send off, "
                                            + "T - end send off, E- end period)",
                            "N");
        return matchIncidentPrompt;
    }

    @Override
    public MatchIncident getMatchIncident(String response) {
        HandballMatchIncidentType handballMatchIncidentType = null;
        ElapsedTimeMatchIncidentType elapsedTimeMatchIncidentType = null;
        TeamId teamId = null;
        int incidentElapsedTimeSecs = elapsedTimeSecs;
        char c = response.toUpperCase().charAt(0);
        switch (c) {
            case 'N':
                elapsedTimeMatchIncidentType = ElapsedTimeMatchIncidentType.SET_MATCH_CLOCK;
                int repeatCount;
                try {
                    repeatCount = Integer.parseInt(response.substring(1));
                    if (repeatCount < 0 || repeatCount > 500)
                        repeatCount = 1;
                } catch (Exception e) {
                    repeatCount = 1;
                }
                /*
                 * 10 second increments
                 */
                incidentElapsedTimeSecs = elapsedTimeSecs + repeatCount * 10;
                break;
            case 'H':
                handballMatchIncidentType = HandballMatchIncidentType.GOAL;
                teamId = TeamId.A;
                break;
            case 'A':
                handballMatchIncidentType = HandballMatchIncidentType.GOAL;
                teamId = TeamId.B;
                break;

            case 'F':
                String ballWithTeam = (response.toUpperCase().substring(1, 2));
                handballMatchIncidentType = HandballMatchIncidentType.BALL_POSESSION_SETTING;

                if (ballWithTeam.equals("H")) {
                    teamId = TeamId.A;
                } else if (ballWithTeam.equals("A")) {
                    teamId = TeamId.B;
                } else if (ballWithTeam.equals("R")) {
                    teamId = TeamId.UNKNOWN;
                } else {
                    throw new IllegalArgumentException("UNKNOWN FREE THROW TEAM");
                }
                break;

            case 'G':// yellow card
                try {
                    String teamCount = (response.substring(1));
                    if (teamCount.equalsIgnoreCase("H")) { // home team yellow card
                        handballMatchIncidentType = HandballMatchIncidentType.TWO_MINS_SUSPENSION_START;
                        teamId = TeamId.A;

                        break;
                    }
                    if (teamCount.equalsIgnoreCase("A")) { // away team yellow card
                        handballMatchIncidentType = HandballMatchIncidentType.TWO_MINS_SUSPENSION_START;
                        teamId = TeamId.B;
                        break;
                    }

                } catch (Exception e) {
                    // int teamCount = 3;
                }

                break;
            case 'T':// terminate minor sin bin
                if (twoMinsSuspendedNoA == 0 && twoMinsSuspendedNoB == 0) {
                    throw new IllegalArgumentException("no existing minor sin bins");
                }
                try {
                    String teamCount = (response.substring(1));
                    if (teamCount.equalsIgnoreCase("H")) { // home team yellow card
                        handballMatchIncidentType = HandballMatchIncidentType.TWO_MINS_SUSPENSION_END;
                        teamId = TeamId.A;
                        break;
                    }
                    if (teamCount.equalsIgnoreCase("A")) { // away team yellow card
                        handballMatchIncidentType = HandballMatchIncidentType.TWO_MINS_SUSPENSION_END;
                        teamId = TeamId.B;
                        break;
                    }

                } catch (Exception e) {
                    // int teamCount = 3;
                }
                break;

            case 'S':
                switch (matchPeriod) {
                    case PREMATCH:
                    case AT_HALF_TIME:
                    case AT_FULL_TIME:
                    case AT_EXTRATIME_PERIOD_END:
                        elapsedTimeMatchIncidentType = ElapsedTimeMatchIncidentType.SET_PERIOD_START;
                        break;
                    default:
                        return null;
                }
                break;
            case 'E':
                switch (matchPeriod) {
                    case IN_FIRST_HALF:
                    case IN_SECOND_HALF:
                    case IN_EXTRATIME_PERIOD:
                        elapsedTimeMatchIncidentType = ElapsedTimeMatchIncidentType.SET_PERIOD_END;
                        break;
                    default:
                        return null; // invalid input so return null
                }
        }
        MatchIncident incident;
        if (handballMatchIncidentType != null) {
            incident = new HandballMatchIncident(handballMatchIncidentType, incidentElapsedTimeSecs, teamId);
        } else {
            incident = new ElapsedTimeMatchIncident(elapsedTimeMatchIncidentType, incidentElapsedTimeSecs);
        }
        return incident;
    }

    /* Display section for current match state */
    private static final String goalsKey = "Goals";
    private static final String firstHalfGoalsKey = "Goals First Half";
    private static final String secondHalfGoalsKey = "Goals Second Half";
    private static final String overtimeGoalsKey = "Goals Overtime";
    private static final String elapsedTimeKey = "Elapsed time";
    private static final String matchPeriodKey = "Match period";
    private static final String yellowCardKey = "Two minutes suspension info";
    private static final String shootOutKey = "Shootout goals";
    @JsonIgnore
    private static final String ballPossessionKey = "Ball possession";

    @Override
    public LinkedHashMap<String, String> getAsMap() {
        /* without update commands the displayed info is not updated */
        // if (matchPeriod != HandballMatchPeriod.PREMATCH) {
        // updateSuspensionNoA();
        // updateSuspensionNoB();
        // }
        //
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put(ballPossessionKey, ballPossession.getBallHoldingTeam().toString());

        map.put(firstHalfGoalsKey, String.format("%d-%d", goalsFirstHalfA, goalsFirstHalfB));
        map.put(secondHalfGoalsKey, String.format("%d-%d", goalsSecondHalfA, goalsSecondHalfB));
        map.put(overtimeGoalsKey, String.format("%d-%d", overtimeGoalsA, overtimeGoalsB));
        map.put(goalsKey, String.format("%d-%d", goalsA, goalsB));
        int mins = elapsedTimeSecs / 60;
        int secs = elapsedTimeSecs - mins * 60;
        map.put(elapsedTimeKey, String.format("%d:%02d", mins, secs));
        map.put(yellowCardKey, String.format("%d-%d", twoMinsSuspendedNoA, twoMinsSuspendedNoB));
        map.put(matchPeriodKey, matchPeriod.toString());
        map.put(shootOutKey, String.format("%d-%d", shootOutGoalsHome, shootOutGoalsAway));
        // map.put(powerPlayKey,);

        return map;
    }

    @Override
    public String setFromMap(Map<String, String> map) {
        /*
         * do nothing - not allowing the user to manually update the score
         */
        return null;
    }

    /**
     * Returns true if at the end of match
     * 
     * @return
     */
    @Override
    @JsonIgnore
    public boolean isMatchCompleted() {
        return (matchPeriod == HandballMatchPeriod.MATCH_COMPLETED);
    }

    /**
     * Returns true if at the end of one period and before the next one (if any) starts
     * 
     * @return
     */
    @JsonIgnore
    public boolean isCurrentPeriodCompleted() {

        return (elapsedTimeSecs == normalHalfsSecs || elapsedTimeSecs == normalHalfsSecs);
    }

    /**
     * Gets the id of the winning team
     * 
     * @return UNKNOWN if a draw, else A or B
     */

    public TeamId getMatchWinner() {
        if (!isMatchCompleted())
            return null;
        else {
            TeamId teamId;
            if (goalsA > goalsB)
                teamId = TeamId.A;
            else if (goalsA == goalsB)
                teamId = TeamId.UNKNOWN;
            else
                teamId = TeamId.B;
            return teamId;
        }
    }

    /**
     * Gets the id of the winning team for a match period
     * 
     * @return UNKNOWN if a draw, else A or B
     */
    public TeamId getPeriodWinner(int periodSeqNo) {
        TeamId teamId = null;

        if (periodSeqNo == 1) {
            if (goalsFirstHalfA > goalsFirstHalfB)
                teamId = TeamId.A;
            else if (goalsFirstHalfA == goalsFirstHalfB)
                teamId = TeamId.UNKNOWN;
            else
                teamId = TeamId.B;
        } else if (periodSeqNo == 2) {
            if (goalsSecondHalfA > goalsSecondHalfB)
                teamId = TeamId.A;
            else if (goalsSecondHalfA == goalsSecondHalfB)
                teamId = TeamId.UNKNOWN;
            else
                teamId = TeamId.B;
        } else if (periodSeqNo > 2) {
            if (overtimeGoalsA > overtimeGoalsB)
                teamId = TeamId.A;
            else if (overtimeGoalsA == overtimeGoalsB)
                teamId = TeamId.UNKNOWN;
            else
                teamId = TeamId.B;
        }

        return teamId;

    }

    @SuppressWarnings("unused")
    private String getPeriodSequenceId() {
        return "P" + getPeriodNo();
    }

    @SuppressWarnings("unused")
    private TeamId wasTeamFirstHalfLeader() {
        return getPeriodWinner(1);
    }

    @SuppressWarnings("unused")
    private TeamId wasTeamSecondHalfLeader() {
        return getPeriodWinner(2);
    }

    public boolean isNormalTimeMatchCompleted() {
        return getPeriodNo() > 2;
    }


    /**
     * Get # goals scored by team A in the previous match period
     * 
     * @return previousPeriodGoalsA
     */
    @JsonIgnore
    public int getPreviousPeriodGoalsA() {
        return previousPeriodGoalsA;
    }

    /**
     * Get # goals scored by team B in the previous match period
     * 
     * @return previousPeriodGoalsB
     */
    @JsonIgnore
    public int getPreviousPeriodGoalsB() {
        return previousPeriodGoalsB;
    }

    @JsonIgnore
    @Override
    public MatchFormat getMatchFormat() {
        return matchFormat;
    }

    @Override
    /**
     * updates the elapsed time in between receipt of matchIncidents. Called roughly once a second by a timer
     * 
     * @return if more than 10 seconds have elapsed since last price calc then returns true
     */
    public boolean updateElapsedTime() {
        boolean updatePrices = false;
        switch (matchPeriod) {
            case PREMATCH:
            case AT_HALF_TIME:
            case AT_FULL_TIME:
            case AT_EXTRATIME_PERIOD_END:
            case AT_SHOOTOUT_PERIOD_END:
            case MATCH_COMPLETED:
                break;
            case IN_FIRST_HALF:
            case IN_SECOND_HALF:
            case IN_EXTRATIME_PERIOD:
            case IN_SHOOTOUT_PERIOD:
                int secs = getSecsSinceLastElapsedTimeFromIncident();
                setElapsedTime(elapsedTimeAtLastMatchIncidentSecs + secs);
                updatePrices = getSecsSinceLastPriceRecalc() >= 10;
                break;
        }
        return updatePrices;
    }

    /**
     * increments the elapsed time by the stated number of seconds.
     * 
     * @param timeSliceSecs
     */
    void incrementSimulationElapsedTime(int timeSliceSecs) {
        elapsedTimeSecs += timeSliceSecs;
        switch (matchPeriod) {
            case PREMATCH:
                matchPeriod = HandballMatchPeriod.IN_FIRST_HALF;
                elapsedTimeThisPeriodSecs = elapsedTimeSecs;
                break;
            case AT_HALF_TIME:
            case IN_FIRST_HALF:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs;
                if (elapsedTimeThisPeriodSecs >= normalHalfsSecs) {
                    elapsedTimeSecs = normalHalfsSecs; // reset counter
                                                       // to remove
                                                       // injury time
                    elapsedTimeThisPeriodSecs = 0;
                    matchPeriod = HandballMatchPeriod.IN_SECOND_HALF;
                    previousPeriodGoalsA = currentPeriodGoalsA;
                    previousPeriodGoalsB = currentPeriodGoalsB;
                    currentPeriodGoalsA = 0;
                    currentPeriodGoalsB = 0;
                }
                break;
            case AT_FULL_TIME:
            case IN_SECOND_HALF:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs - normalHalfsSecs;
                if (elapsedTimeThisPeriodSecs >= normalHalfsSecs) {
                    normalTimeGoalsA = goalsA;
                    normalTimeGoalsB = goalsB;

                    if (goalsA == goalsB || extraTimeSecs == 0) {
                        matchPeriod = HandballMatchPeriod.MATCH_COMPLETED;
                    } else {
                        elapsedTimeSecs = 2 * normalHalfsSecs;
                        elapsedTimeThisPeriodSecs = 0;
                        matchPeriod = HandballMatchPeriod.IN_EXTRATIME_PERIOD;
                        overtimeNo++;
                    }
                    previousPeriodGoalsA = currentPeriodGoalsA;
                    previousPeriodGoalsB = currentPeriodGoalsB;
                    currentPeriodGoalsA = 0;
                    currentPeriodGoalsB = 0;
                }
                break;
            case AT_EXTRATIME_PERIOD_END:
            case IN_EXTRATIME_PERIOD:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs - 2 * normalHalfsSecs - overtimeNo * extraTimeSecs;
                if (elapsedTimeThisPeriodSecs >= extraTimeSecs) {
                    elapsedTimeSecs = 2 * normalHalfsSecs + overtimeNo * extraTimeSecs;
                    elapsedTimeThisPeriodSecs = 0;
                    if (overtimeNo >= 2) {
                        if (goalsA == goalsB && this.matchFormat.isPenaltiesPossible())
                            matchPeriod = HandballMatchPeriod.IN_SHOOTOUT_PERIOD;

                        matchPeriod = HandballMatchPeriod.MATCH_COMPLETED;
                    } else {
                        previousPeriodGoalsA = currentPeriodGoalsA;
                        previousPeriodGoalsB = currentPeriodGoalsB;
                        currentPeriodGoalsA = 0;
                        currentPeriodGoalsB = 0;
                        matchPeriod = HandballMatchPeriod.IN_EXTRATIME_PERIOD;
                        overtimeNo++;
                    }
                }
                break;

            case IN_SHOOTOUT_PERIOD:
                // to be fixed
                if (shootOutGoalsHome == shootOutGoalsAway)
                    ;
                matchPeriod = HandballMatchPeriod.MATCH_COMPLETED;

                break;
            default:
                throw new IllegalArgumentException("Should not get here");
        }

    }

    @Override
    public GamePeriod getGamePeriod() {
        GamePeriod gamePeriod = null;
        switch (matchPeriod) {
            case AT_FULL_TIME:
                gamePeriod = GamePeriod.NORMAL_TIME_END;
                break;
            case AT_HALF_TIME:
                gamePeriod = GamePeriod.HALF_TIME;
                break;
            case IN_EXTRATIME_PERIOD:
                if (overtimeNo == 0) {// can use elapsed time to make the decision
                    gamePeriod = GamePeriod.EXTRA_TIME_FIRST_HALF;
                    break;
                }
                if (overtimeNo == 1) {
                    gamePeriod = GamePeriod.EXTRA_TIME_SECOND_HALF;
                    break;
                }
            case AT_EXTRATIME_PERIOD_END:
                gamePeriod = GamePeriod.EXTRA_TIME_HALF_TIME;
                break;
            case IN_FIRST_HALF:
                gamePeriod = GamePeriod.FIRST_HALF;
                break;
            case IN_SECOND_HALF:
                gamePeriod = GamePeriod.SECOND_HALF;
                break;
            case MATCH_COMPLETED:
                gamePeriod = GamePeriod.POSTMATCH;
                break;
            case PREMATCH:
                gamePeriod = GamePeriod.PREMATCH;
                break;
            default:
                break;

        }
        return gamePeriod;
    }

    /**
     * gets the sequence id to use for match based markets
     * 
     * @return
     */
    @JsonIgnore
    public String getSequenceIdForMatch() {
        return "M";
    }

    /**
     * gets the sequence id to use for match period based markets
     * 
     * @return null if specified set can't occur, else the sequence id
     */
    @JsonIgnore
    public String getSequenceIdForPeriod(int periodOffSet) {
        int periodNo = getPeriodNo() + periodOffSet;

        if (matchFormat.getExtraTimeMinutes() == 0) {
            if (periodNo > 5)
                return null;
            return String.format("P%d", periodNo);
        } else {
            return String.format("P%d", periodNo);
        }
    }

    /**
     * gets the sequence id to use for Goal based markets
     * 
     * @param goalOffset 0 = current , 1 = next etc
     */
    @JsonIgnore
    public String getSequenceIdForGoal(int goalOffset) {
        return String.format("G%d", goalsA + goalsB + 1 + goalOffset);
    }


    /**
     * returns true if in preMatch
     */
    @Override
    public boolean preMatch() {
        return (matchPeriod == HandballMatchPeriod.PREMATCH);
    }

    /**
     * calculates no of secs to go within current period. Returns zero if pre Match or at half time etc.
     * 
     * @return no of secs remaining in current period (including actual or estimated injury time).
     */
    @Override
    public int secsLeftInCurrentPeriod() {
        int secs = 0;
        switch (matchPeriod) {
            case AT_FULL_TIME:
            case AT_HALF_TIME:
            case IN_SHOOTOUT_PERIOD:
            case AT_SHOOTOUT_PERIOD_END:
            case MATCH_COMPLETED:
            case PREMATCH:
            case AT_EXTRATIME_PERIOD_END:
                secs = 0;
                break;
            case IN_FIRST_HALF:
            case IN_SECOND_HALF:
                secs = normalHalfsSecs - elapsedTimeThisPeriodSecs;
                break;
            case IN_EXTRATIME_PERIOD:
                secs = extraTimeSecs - elapsedTimeThisPeriodSecs;
                break;
        }
        return secs;

    }

    public TeamId getCurrentPeriodMatchWinner() {
        TeamId teamId;
        if (currentPeriodGoalsA > currentPeriodGoalsB)
            teamId = TeamId.A;
        else if (currentPeriodGoalsA == currentPeriodGoalsB)
            teamId = TeamId.UNKNOWN;
        else
            teamId = TeamId.B;
        return teamId;
    }

    /**
     * the no of seconds since the last goal was scored
     * 
     * @return -1 if no goal scored or scored during a previous period, otherwise the no of secs
     */
    public int secsSinceLastGoal() {
        int secs = -1;
        if (elapsedTimeAtLastGoalSecs >= 0 && matchPeriodInWhichLastGoalScored == matchPeriod)
            secs = elapsedTimeSecs - elapsedTimeAtLastGoalSecs;
        return secs;
    }

    @Override
    public HandballSimpleMatchState generateSimpleMatchState() {
        return new HandballSimpleMatchState(this.preMatch(), this.isMatchCompleted(), matchPeriod, elapsedTimeSecs,
                        isClockRunning(), goalsA, goalsB, goalsFirstHalfA, goalsFirstHalfB, goalsSecondHalfA,
                        goalsSecondHalfB, overtimeGoalsA, overtimeGoalsB);
    }

    @Override
    public AlgoMatchState generateMatchStateForMatchResult(MatchResultMap matchResultMap) {
        /*
         * not yet supported for this sport
         */
        return null;
    }

}
