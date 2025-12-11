package ats.algo.sport.icehockey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ats.algo.core.GamePeriod;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchIncidentPrompt;
import ats.algo.core.baseclasses.MatchIncidentResult;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.common.ElapsedTimeMatchIncident;
import ats.algo.core.common.ElapsedTimeMatchIncident.ElapsedTimeMatchIncidentType;
import ats.algo.core.matchresult.MatchResultMap;
import ats.algo.core.common.TeamId;
import ats.algo.sport.icehockey.IcehockeyMatchIncident.IcehockeyMatchIncidentType;

/**
 * AlgoMatchState class for Icehockey
 * 
 * @author Jin
 *
 */
public class IcehockeyMatchState extends AlgoMatchState {
    /* CJ added for penalties condition */

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // /* no need timers if use feeds terminate sinBins*/
    @JsonIgnore
    private int[] minorSinBinTimer_A = new int[2];
    @JsonIgnore
    private int[] minorSinBinTimer_B = new int[2];

    private List<GoalInfo> goalInfo;

    @JsonIgnore
    private int[] majorSinBinTimer_A = new int[2];
    @JsonIgnore
    private int[] majorSinBinTimer_B = new int[2];

    @JsonIgnore
    private int elapsedTimeSecsFirstGoal = 0;

    private final int extraPeriodSecs;
    private boolean isOvertimeOrShootOutPlayed;
    private int minorSinBinA; // to be check in the match fact
    private int minorSinBinB;
    private int majorSinBinA;
    private int majorSinBinB;
    private int goalsA;
    private int goalsB;
    private int elapsedTimeSecs;
    private int elapsedTimeAtLastMatchIncidentSecs; // the time from the last matchIncident report
    private int elapsedTimeAtLastGoalSecs;
    private IcehockeyMatchPeriod matchPeriodInWhichLastGoalScored;
    private int elapsedTimeThisPeriodSecs; // recent MatchIncident;
    private final int normalPeriodSecs;
    private IcehockeyMatchPeriod matchPeriod; // the state following the most
    private final IcehockeyMatchFormat matchFormat;
    private TeamId teamScoringLastGoal;
    private int currentPeriodGoalsA;
    private int currentPeriodGoalsB;

    private int goalsFirstPeriodA;
    private int goalsFirstPeriodB;
    private int goalsSecondPeriodA;
    private int goalsSecondPeriodB;
    private int goalsThirdPeriodA;
    private int goalsThirdPeriodB;
    private int overtimeGoalsA;
    private int overtimeGoalsB;

    private int thisSendoffLength = 2 * 60;
    @JsonIgnore
    private int previousFiveMinsEnder = 300;
    @JsonIgnore
    private int currentFiveMinsEnder = 300;
    @JsonIgnore
    private int previousFiveMinsGoalsA;
    @JsonIgnore
    private int previousFiveMinsGoalsB;
    @JsonIgnore
    private int fiveMinsGoalsA;
    @JsonIgnore
    private int fiveMinsGoalsB;
    @JsonIgnore
    private int fiveMinsNo;
    @JsonIgnore
    private int overtimeNo;
    @JsonIgnore
    private boolean penaltiesPossible;
    @JsonIgnore
    private int shootOutGoalsA;
    @JsonIgnore
    private int shootOutGoalsB;
    @JsonIgnore
    int goalSequence;
    @JsonIgnore
    private int shootOutTimeCounter;
    @JsonIgnore
    private int normalTimeGoalsA;
    @JsonIgnore
    private int normalTimeGoalsB;

    @JsonIgnore
    private int previousPeriodGoalsA;
    @JsonIgnore
    private int previousPeriodGoalsB;
    @JsonIgnore
    private static final int timeIncrementSecs = 10;

    /**
     * Json class constructor. Not for general use
     */
    public IcehockeyMatchState() {
        this(new IcehockeyMatchFormat());
        // super();
        // normalPeriodSecs = 60 * 20;
        // elapsedTimeThisPeriodSecs = 1300 - normalPeriodSecs;
        //
        // minorSinBinTimer_A = new int[2];
        // minorSinBinTimer_B = new int[2];
        //
        // majorSinBinTimer_A = new int[2];
        // majorSinBinTimer_B = new int[2];
        //
        // majorSinBinA = 0;
        // majorSinBinB = 0;
        // minorSinBinA = 0;
        // minorSinBinB = 0;
        //
        // goalsA = 3;
        // goalsB = 2;
        // elapsedTimeSecs = 1300;
        // this.matchFormat = null;
        // teamScoringLastGoal = TeamId.A;
        // currentPeriodGoalsA = 1;
        // currentPeriodGoalsB = 0;
        // matchPeriod = IcehockeyMatchPeriod.IN_SECOND_PERIOD;
        // extraPeriodSecs = 5 * 60;

    }

    /**
     * Class constructor
     * 
     * @param matchFormat
     */
    public IcehockeyMatchState(MatchFormat matchFormat) {
        super();
        this.matchFormat = (IcehockeyMatchFormat) matchFormat;
        matchPeriod = IcehockeyMatchPeriod.PREMATCH;
        normalPeriodSecs = this.matchFormat.getNormalTimeMinutes() * 20;
        extraPeriodSecs = this.matchFormat.getExtraTimeMinutes() * 60;
        penaltiesPossible = this.matchFormat.isPenaltiesPossible();
        elapsedTimeAtLastMatchIncidentSecs = -1;
        elapsedTimeAtLastGoalSecs = -1;
        minorSinBinTimer_A = new int[2];
        minorSinBinTimer_B = new int[2];
        majorSinBinTimer_A = new int[2];
        majorSinBinTimer_B = new int[2];
        goalInfo = new ArrayList<GoalInfo>();
    }

    /**
     * Check if over time shoot out played in match
     * 
     * @return isOvertimeOrShootOutPlayed
     */
    @JsonIgnore
    public boolean isOvertimeOrShootOutPlayed() {
        return isOvertimeOrShootOutPlayed;
    }

    /**
     * Get the goals for Team A in the shootout period
     * 
     * @return shootOutGoalsA
     */
    @JsonIgnore
    public int getShootOutGoalsA() {
        return shootOutGoalsA;
    }

    /**
     * Get goal information as a List of GoalInfo class
     * 
     * @return shootOutFirst
     */
    public List<GoalInfo> getGoalInfo() {
        return goalInfo;
    }

    /**
     * Set goal information as a List of GoalInfo class
     * 
     * @param goalInfo
     */
    public void setGoalInfo(List<GoalInfo> goalInfo) {
        this.goalInfo = goalInfo;
    }

    /**
     * Set the goals for Team A in the shootout period
     * 
     * @param shootOutGoalsA
     */
    @JsonIgnore
    public void setShootOutGoalsA(int shootOutGoalsA) {
        this.shootOutGoalsA = shootOutGoalsA;
    }

    /**
     * Get the goals for Team B in the shootout period
     * 
     * @return shootOutGoalsB
     */
    @JsonIgnore
    public int getShootOutGoalsB() {
        return shootOutGoalsB;
    }

    /**
     * Set the goals for Team B in the shootout period
     * 
     * @param shootOutGoalsB
     */
    @JsonIgnore
    public void setShootOutGoalsB(int shootOutGoalsB) {
        this.shootOutGoalsB = shootOutGoalsB;
    }

    /**
     * Gets total attempts in the shootout period
     * 
     * @return
     */
    @JsonIgnore
    public int getShootOutTimeCounter() {
        return shootOutTimeCounter;
    }

    /**
     * Sets total attempts in the shootout period
     * 
     * @param shootOutTimeCounter
     */
    @JsonIgnore
    public void setShootOutTimeCounter(int shootOutTimeCounter) {
        this.shootOutTimeCounter = shootOutTimeCounter;
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

    /**
     * Check if penalty is possible for this format of match
     * 
     * @return
     */
    @JsonIgnore
    public boolean isPenaltiesPossible() {
        return penaltiesPossible;
    }

    /**
     * Switch penalty on/off for this format of match
     * 
     * @param penaltiesPossible
     */
    @JsonIgnore
    public void setPenaltiesPossible(boolean penaltiesPossible) {
        this.penaltiesPossible = penaltiesPossible;
    }

    /**
     * Get number of players currently on major sin bin for team A
     * 
     * @return majorSinBinA
     */
    public int getMajorSinBinA() {
        return majorSinBinA;
    }

    /**
     * Set number of players currently on major sin bin for team A
     * 
     * @param majorSinBinA
     */
    public void setMajorSinBinA(int majorSinBinA) {
        this.majorSinBinA = majorSinBinA;
    }

    /**
     * Get number of players currently on major sin bin for team B
     * 
     * @return majorSinBinB
     */
    public int getMajorSinBinB() {
        return majorSinBinB;
    }

    /**
     * Set number of players currently on major sin bin for team B
     * 
     * @param majorSinBinB
     */
    public void setMajorSinBinB(int majorSinBinB) {
        this.majorSinBinB = majorSinBinB;
    }

    /**
     * Get sequence number of the current five minutes since the begging of match
     * 
     * @return
     */
    @JsonIgnore
    public int getFiveMinsNo() {
        return fiveMinsNo;
    }

    /**
     * Get the sequence number of the goal
     * 
     * @return
     */
    @JsonIgnore
    public int getGoalSequence() {
        return goalSequence;
    }

    /**
     * Set the sequence number of the goal since the begging of match
     * 
     * @param goalSequence
     */
    @JsonIgnore
    public void setGoalSequence(int goalSequence) {
        this.goalSequence = goalSequence;
    }

    /**
     * Set current five minutes no
     * 
     * @param fiveMinsNo
     */
    @JsonIgnore
    public void setFiveMinsNo(int fiveMinsNo) {
        this.fiveMinsNo = fiveMinsNo;
    }

    /**
     * Get the number of goal scored by team A in the previous five minutes of the game
     * 
     * @return
     */
    @JsonIgnore
    public int getPreviousFiveMinsGoalsA() {
        return previousFiveMinsGoalsA;
    }

    /**
     * Set the number of goal scored by team A in the previous five minutes of the game
     * 
     * @param previousFiveMinsGoalsA
     */
    @JsonIgnore
    public void setPreviousFiveMinsGoalsA(int previousFiveMinsGoalsA) {
        this.previousFiveMinsGoalsA = previousFiveMinsGoalsA;
    }

    /**
     * Get the number of goal scored by team B in the previous five minutes of the game
     * 
     * @return
     */
    @JsonIgnore
    public int getPreviousFiveMinsGoalsB() {
        return previousFiveMinsGoalsB;
    }

    /**
     * Set the number of goal scored by team B in the previous five minutes of the game
     * 
     * @param previousFiveMinsGoalsB
     */
    @JsonIgnore
    public void setPreviousFiveMinsGoalsB(int previousFiveMinsGoalsB) {
        this.previousFiveMinsGoalsB = previousFiveMinsGoalsB;
    }

    /**
     * Get the ending time in the match of last five minutes slot
     * 
     * @return previousFiveMinsEnder
     */
    @JsonIgnore
    public int getPreviousFiveMinsEnder() {
        return previousFiveMinsEnder;
    }

    /**
     * Set the ending time in the match of last five minutes slot
     * 
     * @param previousFiveMinsEnder
     */
    @JsonIgnore
    public void setPreviousFiveMinsEnder(int previousFiveMinsEnder) {
        this.previousFiveMinsEnder = previousFiveMinsEnder;
    }

    /**
     * Get the ending time in the match of current five minutes slot
     * 
     * @return currentFiveMinsEnder
     */
    @JsonIgnore
    public int getCurrentFiveMinsEnder() {
        return currentFiveMinsEnder;
    }

    /**
     * Set the ending time in the match of current five minutes slot
     * 
     * @param currentFiveMinsEnder
     */
    @JsonIgnore
    public void setCurrentFiveMinsEnder(int thisFiveMinsEnder) {
        this.currentFiveMinsEnder = thisFiveMinsEnder;
    }

    /**
     * Get the number of goal scored by team A in the current five minutes of the game
     * 
     * @return fiveMinsGoalsA
     */
    @JsonIgnore
    public int getFiveMinsGoalsA() {
        return fiveMinsGoalsA;
    }

    /**
     * Set the number of goal scored by team A in the current five minutes of the game
     * 
     * @param fiveMinsGoalsA
     */
    @JsonIgnore
    public void setFiveMinsGoalsA(int fiveMinsGoalsA) {
        this.fiveMinsGoalsA = fiveMinsGoalsA;
    }

    /**
     * Get the number of goal scored by team B in the current five minutes of the game
     * 
     * @return
     */
    @JsonIgnore
    public int getFiveMinsGoalsB() {
        return fiveMinsGoalsB;
    }

    /**
     * Set the number of goal scored by team B in the current five minutes of the game
     * 
     * @param fiveMinsGoalsB
     */
    @JsonIgnore
    public void setFiveMinsGoalsB(int fiveMinsGoalsB) {
        this.fiveMinsGoalsB = fiveMinsGoalsB;
    }

    /**
     * Get team A normal time goals
     * 
     * @return normalTimeGoalsA
     */
    @JsonIgnore
    public int getNormalTimeGoalsA() {
        return normalTimeGoalsA;
    }

    /**
     * Set team A normal time goals
     * 
     * @param normalTimeGoalsA
     */
    @JsonIgnore
    public void setNormalTimeGoalsA(int normalTimeGoalsA) {
        this.normalTimeGoalsA = normalTimeGoalsA;
    }

    /**
     * Get team B normal time goals
     * 
     * @return normalTimeGoalsB
     */
    @JsonIgnore
    public int getNormalTimeGoalsB() {
        return normalTimeGoalsB;
    }

    /**
     * Set team B normal time goals
     * 
     * @param normalTimeGoalsB
     */
    @JsonIgnore
    public void setNormalTimeGoalsB(int normalTimeGoalsB) {
        this.normalTimeGoalsB = normalTimeGoalsB;
    }

    /**
     * Get the length of duration of this sin bin
     * 
     * @return thisSendoffLength
     */
    @JsonIgnore
    public int getThisSendoffLength() {
        return thisSendoffLength;
    }

    /**
     * Set the length of duration of this sin bin
     * 
     * @param thisSendoffLength
     */
    @JsonIgnore
    public void setThisSendoffLength(int thisSendoffLength) {
        this.thisSendoffLength = thisSendoffLength;
    }

    /**
     * Get overtime period no
     * 
     * @return overtimeNo
     */
    @JsonIgnore
    public int getOvertimeNo() {
        return overtimeNo;
    }

    /**
     * Set overtime period no
     * 
     * @param overtimeNo
     */
    @JsonIgnore
    public void setOvertimeNo(int overtimeNo) {
        this.overtimeNo = overtimeNo;
    }

    /**
     * Get length of each extra time period
     * 
     * @return extraPeriodSecs
     */
    public int getExtraPeriodSecs() {
        return extraPeriodSecs;
    }

    /**
     * Get length of each normal time period
     * 
     * @return normalPeriodSecs
     */
    public int getNormalPeriodSecs() {
        return normalPeriodSecs;
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
     * Set no of goals scored by team A in the previous match period
     * 
     * @param previousPeriodGoalsA
     */
    public void setPreviousPeriodGoalsA(int previousPeriodGoalsA) {
        this.previousPeriodGoalsA = previousPeriodGoalsA;
    }

    /**
     * Set no of goals scored by team B in the previous match period
     * 
     * @param previousPeriodGoalsB
     */
    public void setPreviousPeriodGoalsB(int previousPeriodGoalsB) {
        this.previousPeriodGoalsB = previousPeriodGoalsB;
    }

    /**
     * Get no of players from team A current on minor sin bin
     * 
     * @return minorSinBinA
     */
    public int getMinorSinBinA() {

        return minorSinBinA;
    }

    /**
     * Get no of players from team B current on minor sin bin
     * 
     * @return minorSinBinB
     */
    public int getMinorSinBinB() {

        return minorSinBinB;
    }

    /**
     * Set goals scored by team A in the current match period
     * 
     * @param currentPeriodGoalsA
     */
    public void setCurrentPeriodGoalsA(int currentPeriodGoalsA) {
        this.currentPeriodGoalsA = currentPeriodGoalsA;
    }

    /**
     * Set goals scored by team B in the current match period
     * 
     * @param currentPeriodGoalsB
     */
    public void setCurrentPeriodGoalsB(int currentPeriodGoalsB) {
        this.currentPeriodGoalsB = currentPeriodGoalsB;
    }

    /**
     * Set no of players from team A current on minor sin bin
     * 
     * @param minorSinBinA
     */
    public void setSendOffA(int sendoffh) {
        this.minorSinBinA = sendoffh;
    }

    /**
     * Set no of players from team B current on minor sin bin
     * 
     * @param minorSinBinB
     */
    public void setSendOffB(int sendoffa) {
        this.minorSinBinB = sendoffa;
    }

    /**
     * Get major sin bin ending time for team A players in a ArrayList
     * 
     * @return minorSinBinTimer_A
     */
    public int[] getMajorSinBinTimer_A() {
        return majorSinBinTimer_A;
    }

    /**
     * Set major sin bin ending time for team A players in a ArrayList
     * 
     * @param minorSinBinTimer_A
     */
    public void setMajorSinBinTimer_A(int[] majorSinBinTimer_A) {
        this.majorSinBinTimer_A = majorSinBinTimer_A;
    }

    /**
     * Get major sin bin ending time for team B players in a ArrayList
     * 
     * @return majorSinBinTimer_B
     */
    public int[] getMajorSinBinTimer_B() {
        return majorSinBinTimer_B;
    }

    /**
     * Set major sin bin ending time for team B players in a ArrayList
     * 
     * @param majorSinBinTimer_B
     */
    public void setMajorSinBinTimer_B(int[] majorSinBinTimer_B) {
        this.majorSinBinTimer_B = majorSinBinTimer_B;
    }

    /**
     * Get minor sin bin ending time for team A players in a ArrayList
     * 
     * @return minorSinBinTimer_A
     */
    @JsonIgnore
    public int[] getSendOffTimer_A() {
        return minorSinBinTimer_A;
    }

    /**
     * Set minor sin bin ending time for team A players in a ArrayList
     * 
     * @param minorSinBinTimer_A
     */
    public void setSendOffTimer_A(int[] sendOffTimer_Ain) {
        this.minorSinBinTimer_A = sendOffTimer_Ain;
    }

    /**
     * Get minor sin bin ending time for team B players in a ArrayList
     * 
     * @return minorSinBinTimer_B
     */
    @JsonIgnore
    public int[] getSendOffTimer_B() {
        return minorSinBinTimer_B;
    }

    /**
     * Set minor sin bin ending time for team B players in a ArrayList
     * 
     * @param minorSinBinTimer_B
     */
    public void setSendOffTimer_B(int[] sendOffTimer_Bin) { // pass by address
        this.minorSinBinTimer_B = sendOffTimer_Bin;
    }

    /**
     * Get # goals scored by team A
     * 
     * @return goalsA
     */
    public int getGoalsA() {
        return goalsA;
    }

    /**
     * Set # goals scored by team A
     * 
     * @param goalsA
     */
    public void setGoalsA(int goalsHome) {
        this.goalsA = goalsHome;
    }

    /**
     * Get # goals scored by team B
     * 
     * @return goalsB
     */
    public int getGoalsB() {
        return goalsB;
    }

    /**
     * Set # goals scored by team B
     * 
     * @param goalsB
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
     * Get the goal sequence no
     * 
     * @return
     */
    @JsonIgnore
    public int getGoalNo() {
        return goalSequence + 1;
    }


    public int getGoalsFirstPeriodA() {
        return goalsFirstPeriodA;
    }

    public void setGoalsFirstPeriodA(int goalsFirstPeriodA) {
        this.goalsFirstPeriodA = goalsFirstPeriodA;
    }

    public int getGoalsFirstPeriodB() {
        return goalsFirstPeriodB;
    }

    public void setGoalsFirstPeriodB(int goalsFirstPeriodB) {
        this.goalsFirstPeriodB = goalsFirstPeriodB;
    }

    public int getGoalsSecondPeriodA() {
        return goalsSecondPeriodA;
    }

    public void setGoalsSecondPeriodA(int goalsSecondPeriodA) {
        this.goalsSecondPeriodA = goalsSecondPeriodA;
    }

    public int getGoalsSecondPeriodB() {
        return goalsSecondPeriodB;
    }

    public void setGoalsSecondPeriodB(int goalsSecondPeriodB) {
        this.goalsSecondPeriodB = goalsSecondPeriodB;
    }

    public int getGoalsThirdPeriodA() {
        return goalsThirdPeriodA;
    }

    public void setGoalsThirdPeriodA(int goalsThirdPeriodA) {
        this.goalsThirdPeriodA = goalsThirdPeriodA;
    }

    public int getGoalsThirdPeriodB() {
        return goalsThirdPeriodB;
    }

    public void setGoalsThirdPeriodB(int goalsThirdPeriodB) {
        this.goalsThirdPeriodB = goalsThirdPeriodB;
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
            case IN_FIRST_PERIOD:
                n = 1;
                break;
            case AT_FIRST_PERIOD_END:
            case IN_SECOND_PERIOD:
                n = 2;
                break;
            case AT_SECOND_PERIOD_END:
            case IN_THIRD_PERIOD:
                n = 3;
                break;
            case AT_FULL_TIME:
            case IN_EXTRA_TIME:
                n = 4;
                break;
            case AT_EXTRA_PERIOD_END:
            case IN_SHOOTOUT:
                n = 6;
                break;
            case MATCH_COMPLETED:
                n = 5;
                break;

            case AT_SHOOTOUT_END:
                n = 7;
                break;
            default:
                throw new IllegalArgumentException("Unknown match period");
        }
        return n;
    }

    /**
     * Get the time for the first goal happens
     * 
     * @return elapsedTimeSecsFirstGoal
     */
    @JsonIgnore
    public int getElapsedTimeSecsFirstGoal() {
        return elapsedTimeSecsFirstGoal;
    }

    /**
     * Set the time for the first goal happens
     * 
     * @param elapsedTimeSecsFirstGoal
     */
    @JsonIgnore
    public void setElapsedTimeSecsFirstGoal(int elapsedTimeSecsFirstGoal) {
        this.elapsedTimeSecsFirstGoal = elapsedTimeSecsFirstGoal;
    }


    /**
     * Set the time for the first goal happens
     * 
     * @param elapsedTimeSecsFirstGoal
     */
    public void setElapsedTimeSecs(int elapsedTimeSecs, int overtimeNoIn) {
        this.elapsedTimeSecs = elapsedTimeSecs;
        this.elapsedTimeThisPeriodSecs = elapsedTimeSecs;
        matchPeriod = IcehockeyMatchPeriod.IN_FIRST_PERIOD;
        if (elapsedTimeSecs == normalPeriodSecs) {
            this.elapsedTimeThisPeriodSecs = 0;
            matchPeriod = IcehockeyMatchPeriod.AT_FIRST_PERIOD_END;
        }
        if (elapsedTimeSecs > normalPeriodSecs) {
            this.elapsedTimeThisPeriodSecs = elapsedTimeSecs - normalPeriodSecs;
            matchPeriod = IcehockeyMatchPeriod.IN_SECOND_PERIOD;
        }
        if (elapsedTimeSecs == 2 * normalPeriodSecs) {
            this.elapsedTimeThisPeriodSecs = 0;
            matchPeriod = IcehockeyMatchPeriod.AT_SECOND_PERIOD_END;
        }
        if (elapsedTimeSecs > 2 * normalPeriodSecs) {
            this.elapsedTimeThisPeriodSecs = elapsedTimeSecs - 2 * normalPeriodSecs;
            matchPeriod = IcehockeyMatchPeriod.IN_THIRD_PERIOD;
        }
        if (elapsedTimeSecs == 3 * normalPeriodSecs) {
            this.elapsedTimeThisPeriodSecs = 0;
            matchPeriod = IcehockeyMatchPeriod.AT_FULL_TIME;
        }
        if (elapsedTimeSecs > 3 * normalPeriodSecs) {
            this.elapsedTimeThisPeriodSecs = elapsedTimeSecs - overtimeNoIn * extraPeriodSecs;
            matchPeriod = IcehockeyMatchPeriod.IN_EXTRA_TIME;
        }
        // updateTwoMinsSinBinA();
        // updateTwoMinsSinBinB();
    }

    /**
     * Get the current matchPeriod
     * 
     * @return matchPeriod
     */
    public IcehockeyMatchPeriod getMatchPeriod() {
        return matchPeriod;
    }

    /**
     * Set the current match period
     * 
     * @param matchPeriod
     */
    public void setMatchPeriod(IcehockeyMatchPeriod matchPeriod) {
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
        elapsedTimeAtLastMatchIncidentSecs = elapsedTimeSecs; // ?? what's this
        setClockTimeOfLastElapsedTimeFromIncident(); // ??

        if (!((matchIncident instanceof IcehockeyMatchIncident) || (matchIncident instanceof ElapsedTimeMatchIncident)))
            return super.updateStateForIncident(matchIncident, autosyncMatchStateToFeedOnMismatch);


        if (matchIncident.getClass() == IcehockeyMatchIncident.class) {

            TeamId teamId = ((IcehockeyMatchIncident) matchIncident).getTeamId();

            switch (((IcehockeyMatchIncident) matchIncident).getIncidentSubType()) {
                case TWO_MINUTE_PENALTY_END:
                    switch (teamId) {
                        case A:
                            if (minorSinBinA == 0)
                                throw new IllegalArgumentException("Send-Off number is wrong");
                            minorSinBinA--;
                            cleanSinBinTimer(minorSinBinTimer_A, minorSinBinA);

                            break;

                        case B:
                            if (minorSinBinB == 0)
                                throw new IllegalArgumentException("Send-Off number is wrong");
                            minorSinBinB--;
                            cleanSinBinTimer(minorSinBinTimer_B, minorSinBinB);

                            break;
                        default:
                            throw new IllegalArgumentException("team Id Unknown");

                    }
                    break;

                case FIVE_MINUTE_PENALTY_END: // power play end
                    switch (teamId) {
                        case A:
                            if (majorSinBinA == 0)
                                throw new IllegalArgumentException("Send-Off number is wrong");
                            majorSinBinA--;
                            cleanSinBinTimer(majorSinBinTimer_A, majorSinBinA);

                            break;

                        case B:
                            if (majorSinBinB == 0)
                                throw new IllegalArgumentException("Send-Off number is wrong");
                            majorSinBinB--;
                            cleanSinBinTimer(majorSinBinTimer_B, majorSinBinB);

                            break;
                        default:
                            throw new IllegalArgumentException("team Id Unknown");

                    }
                    break;

                case TWO_MINUTE_PENALTY_START:
                    switch (teamId) {
                        case A:
                            // thisSendoffLength = 60 * 2;
                            sinBinTimerTrigger(minorSinBinTimer_A, elapsedTimeSecs, 2 * 60);
                            minorSinBinA++;
                            break;

                        case B:
                            // thisSendoffLength = 60 * 2;
                            sinBinTimerTrigger(minorSinBinTimer_B, elapsedTimeSecs, 2 * 60);
                            minorSinBinB++;
                            break;

                        case UNKNOWN:
                            throw new IllegalArgumentException("TeamId must be specified for power play");

                    }
                    break;

                case FIVE_MINUTE_PENALTY_START: // power play started
                    switch (teamId) {
                        case A:
                            sinBinTimerTrigger(majorSinBinTimer_A, elapsedTimeSecs, 5 * 60);
                            majorSinBinA++;
                            break;

                        case B:
                            sinBinTimerTrigger(majorSinBinTimer_B, elapsedTimeSecs, 5 * 60);
                            majorSinBinB++;
                            break;
                        case UNKNOWN:
                            throw new IllegalArgumentException("TeamId must be specified for five minutes power play");

                    }
                    break;

                case GOAL:
                    elapsedTimeAtLastGoalSecs = elapsedTimeSecs;
                    matchPeriodInWhichLastGoalScored = matchPeriod;
                    GoalInfo goalInfoCurrent = new GoalInfo();
                    goalInfoCurrent.setMins(elapsedTimeSecs / 60);
                    goalInfoCurrent.setTeam(teamId);
                    goalInfoCurrent.setMatchPeriodIndex(this.getPeriodNo());
                    goalInfo.add(goalInfoCurrent);
                    switch (teamId) {
                        case A:
                            if (!matchPeriod.equals(IcehockeyMatchPeriod.IN_SHOOTOUT)) {

                                if (!matchPeriod.equals(IcehockeyMatchPeriod.IN_EXTRA_TIME)) {
                                    normalTimeGoalsA++;
                                } else {
                                    overtimeGoalsA++;
                                }

                                updateFiveMinsStatus();
                                fiveMinsGoalsA++;

                                if (goalsA == 0 && goalsB == 0) {
                                    elapsedTimeSecsFirstGoal = elapsedTimeSecs;
                                }
                                if (matchPeriod.equals(IcehockeyMatchPeriod.IN_FIRST_PERIOD))
                                    goalsFirstPeriodA++;
                                if (matchPeriod.equals(IcehockeyMatchPeriod.IN_SECOND_PERIOD))
                                    goalsSecondPeriodA++;
                                if (matchPeriod.equals(IcehockeyMatchPeriod.IN_THIRD_PERIOD))
                                    goalsThirdPeriodA++;

                                goalsA++;
                                currentPeriodGoalsA++;
                                teamScoringLastGoal = TeamId.A;
                                goalSequence++;

                            } else {
                                shootOutGoalsA++;
                                shootOutTimeCounter++;
                                if (isShootoutSettled()) {
                                    this.goalsA++;// = shootOutGoalsA>shootOutGoalsB? 1:0;
                                    matchPeriod = IcehockeyMatchPeriod.MATCH_COMPLETED;
                                }

                            }
                            break;
                        case B:
                            if (!matchPeriod.equals(IcehockeyMatchPeriod.IN_SHOOTOUT)) {

                                if (!matchPeriod.equals(IcehockeyMatchPeriod.IN_EXTRA_TIME)) {
                                    normalTimeGoalsB++;
                                } else {
                                    overtimeGoalsB++;
                                }

                                updateFiveMinsStatus();
                                fiveMinsGoalsB++;

                                if (goalsA == 0 && goalsB == 0) {
                                    elapsedTimeSecsFirstGoal = elapsedTimeSecs;
                                }
                                if (matchPeriod.equals(IcehockeyMatchPeriod.IN_FIRST_PERIOD))
                                    goalsFirstPeriodB++;
                                if (matchPeriod.equals(IcehockeyMatchPeriod.IN_SECOND_PERIOD))
                                    goalsSecondPeriodB++;
                                if (matchPeriod.equals(IcehockeyMatchPeriod.IN_THIRD_PERIOD))
                                    goalsThirdPeriodB++;
                                goalsB++;
                                currentPeriodGoalsB++;
                                teamScoringLastGoal = TeamId.B;
                                goalSequence++;

                            } else {
                                shootOutGoalsB++;
                                shootOutTimeCounter++;
                                if (isShootoutSettled()) {
                                    this.goalsB++;
                                    matchPeriod = IcehockeyMatchPeriod.MATCH_COMPLETED;
                                }

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
                    updateFiveMinsStatus();
                    if (matchPeriod.equals(IcehockeyMatchPeriod.IN_SHOOTOUT)) {
                        shootOutTimeCounter++;
                        if (isShootoutSettled()) {
                            this.goalsA += shootOutGoalsA > shootOutGoalsB ? 1 : 0;
                            this.goalsB += shootOutGoalsA > shootOutGoalsB ? 0 : 1;
                            matchPeriod = IcehockeyMatchPeriod.MATCH_COMPLETED;
                        }
                    }
                    break;

                case SET_PERIOD_START:
                    switch (matchPeriod) {
                        case PREMATCH:
                            elapsedTimeThisPeriodSecs = 0;
                            matchPeriod = IcehockeyMatchPeriod.IN_FIRST_PERIOD;
                            break;

                        case IN_FIRST_PERIOD:
                            break;

                        case AT_FIRST_PERIOD_END:
                            elapsedTimeThisPeriodSecs = 0;
                            matchPeriod = IcehockeyMatchPeriod.IN_SECOND_PERIOD;
                            break;

                        case IN_SECOND_PERIOD:
                            break;

                        case AT_SECOND_PERIOD_END:
                            elapsedTimeThisPeriodSecs = 0;
                            matchPeriod = IcehockeyMatchPeriod.IN_THIRD_PERIOD;
                            break;

                        case IN_THIRD_PERIOD:
                            break;

                        case AT_FULL_TIME:
                            overtimeNo++;
                            elapsedTimeThisPeriodSecs = 0;
                            matchPeriod = IcehockeyMatchPeriod.IN_EXTRA_TIME;
                            break;

                        case IN_EXTRA_TIME:
                            break;

                        case AT_EXTRA_PERIOD_END:
                            overtimeNo++;
                            elapsedTimeThisPeriodSecs = 0;
                            if (penaltiesPossible && goalsA == goalsB) {
                                matchPeriod = IcehockeyMatchPeriod.IN_SHOOTOUT;
                                break;
                            } else {
                                if (goalsA != goalsB) {
                                    matchPeriod = IcehockeyMatchPeriod.MATCH_COMPLETED;
                                } else {
                                    matchPeriod = IcehockeyMatchPeriod.IN_EXTRA_TIME;
                                }
                            }
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
                        case IN_FIRST_PERIOD:
                            matchPeriod = IcehockeyMatchPeriod.AT_FIRST_PERIOD_END;
                            previousPeriodGoalsA = currentPeriodGoalsA;
                            previousPeriodGoalsB = currentPeriodGoalsB;
                            currentPeriodGoalsA = 0;
                            currentPeriodGoalsB = 0;
                            setElapsedTime(normalPeriodSecs);
                            elapsedTimeAtLastMatchIncidentSecs = normalPeriodSecs;
                            setClockTimeOfLastElapsedTimeFromIncident();
                            break;

                        case AT_FIRST_PERIOD_END:
                            break;

                        case IN_SECOND_PERIOD:
                            matchPeriod = IcehockeyMatchPeriod.AT_SECOND_PERIOD_END;
                            previousPeriodGoalsA = currentPeriodGoalsA;
                            previousPeriodGoalsB = currentPeriodGoalsB;
                            currentPeriodGoalsA = 0;
                            currentPeriodGoalsB = 0;
                            setElapsedTime(normalPeriodSecs * 2);
                            elapsedTimeAtLastMatchIncidentSecs = normalPeriodSecs * 2;
                            setClockTimeOfLastElapsedTimeFromIncident();
                            break;

                        case AT_SECOND_PERIOD_END:
                            break;

                        case IN_THIRD_PERIOD:
                            if (extraPeriodSecs == 0 || goalsA != goalsB)
                                matchPeriod = IcehockeyMatchPeriod.MATCH_COMPLETED;
                            else
                                matchPeriod = IcehockeyMatchPeriod.AT_FULL_TIME;
                            previousPeriodGoalsA = currentPeriodGoalsA;
                            previousPeriodGoalsB = currentPeriodGoalsB;
                            currentPeriodGoalsA = 0;
                            currentPeriodGoalsB = 0;
                            setElapsedTime(normalPeriodSecs * 3);
                            elapsedTimeAtLastMatchIncidentSecs = normalPeriodSecs;
                            setClockTimeOfLastElapsedTimeFromIncident();
                            break;

                        case AT_FULL_TIME:
                            break;

                        case IN_EXTRA_TIME:
                            if (goalsA == goalsB) {
                                matchPeriod = IcehockeyMatchPeriod.AT_EXTRA_PERIOD_END;
                            } else {
                                matchPeriod = IcehockeyMatchPeriod.MATCH_COMPLETED;
                            }
                            previousPeriodGoalsA = currentPeriodGoalsA;
                            previousPeriodGoalsB = currentPeriodGoalsB;
                            currentPeriodGoalsA = 0;
                            currentPeriodGoalsB = 0;
                            setElapsedTime(normalPeriodSecs * 3 + (overtimeNo) * extraPeriodSecs);
                            if (overtimeNo == 0) {
                                elapsedTimeAtLastMatchIncidentSecs = normalPeriodSecs;
                            } else {
                                elapsedTimeAtLastMatchIncidentSecs = extraPeriodSecs;
                            }

                            setClockTimeOfLastElapsedTimeFromIncident();
                            break;

                        case AT_EXTRA_PERIOD_END:
                            break;

                        case IN_SHOOTOUT:
                            matchPeriod = IcehockeyMatchPeriod.MATCH_COMPLETED;
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

    /**
     * If game is in shoot out period, check whether this shoot out is settled
     * 
     * @return
     */
    private boolean isShootoutSettled() {
        boolean settled = false;

        if (shootOutTimeCounter < 6 && (shootOutGoalsA != shootOutGoalsB)) {
            double shooutOutCounterA = Math.ceil((double) shootOutTimeCounter / 2);
            double shooutOutCounterB = Math.floor((double) shootOutTimeCounter / 2);

            if (shootOutGoalsA > (shootOutGoalsB + 3 - shooutOutCounterB)
                            || shootOutGoalsB > (shootOutGoalsA + 3 - shooutOutCounterA)) {
                settled = true;
            }

        } else if ((shootOutGoalsA != shootOutGoalsB) && shootOutTimeCounter >= 6 && shootOutTimeCounter % 2 == 0) {

            settled = true;
        }

        return settled;
    }

    private void cleanSinBinTimer(int[] twoMinsSinBinTimer_Local, int minorSinBinNumber) {
        if (minorSinBinNumber == 0) {
            twoMinsSinBinTimer_Local[0] = 0;
            twoMinsSinBinTimer_Local[1] = 0;
        }
        if (minorSinBinNumber == 1) {
            if (twoMinsSinBinTimer_Local[0] < twoMinsSinBinTimer_Local[1]) {
                twoMinsSinBinTimer_Local[0] = 0;
            }

            if (twoMinsSinBinTimer_Local[0] > twoMinsSinBinTimer_Local[1]) {
                twoMinsSinBinTimer_Local[1] = 0;
            }

            if (twoMinsSinBinTimer_Local[0] == twoMinsSinBinTimer_Local[1]) {

                twoMinsSinBinTimer_Local[0] = 0;

            }

        }

        int localCounter = 0;
        if (twoMinsSinBinTimer_Local[0] != 0)
            localCounter++;
        if (twoMinsSinBinTimer_Local[1] != 0)
            localCounter++;

        if (minorSinBinNumber > 2 || localCounter != minorSinBinNumber) {
            throw new IllegalArgumentException(
                            "reseted sin bin number can not be equal or greater than 2/ Or sin Bin already ends");
        }

    }

    private void updateFiveMinsStatus() {
        int newFiveMinsEnder = (int) (300 * (Math.floor(elapsedTimeSecs / 300)) + 300);
        fiveMinsNo = (int) ((Math.floor(elapsedTimeSecs / 300)));
        previousFiveMinsEnder = currentFiveMinsEnder;
        if (currentFiveMinsEnder < newFiveMinsEnder) {
            currentFiveMinsEnder = newFiveMinsEnder;
            previousFiveMinsGoalsA = fiveMinsGoalsA;
            previousFiveMinsGoalsB = fiveMinsGoalsB;
            fiveMinsGoalsA = 0;
            fiveMinsGoalsB = 0;
        }

        if (currentFiveMinsEnder < newFiveMinsEnder - 300) { // time jump multiple 5 minutes
            currentFiveMinsEnder = newFiveMinsEnder;
            previousFiveMinsGoalsA = 0;
            previousFiveMinsGoalsB = 0;
            fiveMinsGoalsA = 0;
            fiveMinsGoalsB = 0;
        }
    }

    private void sinBinTimerTrigger(int[] sendOffTimer_local, int elapsedTimeSecsIn, int sendOffLength) {
        // clean up sin bin timer first

        if (sendOffTimer_local[0] <= sendOffTimer_local[1]) {
            sendOffTimer_local[0] = elapsedTimeSecsIn + sendOffLength;
        } else {
            sendOffTimer_local[1] = elapsedTimeSecsIn + sendOffLength;
        }
    }

    private void setElapsedTime(int elapsedTimeSecs) {
        this.elapsedTimeSecs = elapsedTimeSecs;
        switch (matchPeriod) {
            case PREMATCH:
            case AT_FIRST_PERIOD_END:
            case AT_SECOND_PERIOD_END:
            case AT_FULL_TIME:
            case AT_EXTRA_PERIOD_END:
            case MATCH_COMPLETED:
                /*
                 * do nothing
                 */
                break;
            case IN_FIRST_PERIOD:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs;
                break;
            case IN_SECOND_PERIOD:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs - normalPeriodSecs;
                break;
            case IN_THIRD_PERIOD:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs - 2 * normalPeriodSecs;
                break;
            case IN_EXTRA_TIME:
                isOvertimeOrShootOutPlayed = true;
                elapsedTimeThisPeriodSecs = elapsedTimeSecs - 3 * normalPeriodSecs - extraPeriodSecs * (overtimeNo - 1);
                break;
            case IN_SHOOTOUT:
                isOvertimeOrShootOutPlayed = true;
                elapsedTimeThisPeriodSecs = elapsedTimeSecs - 3 * normalPeriodSecs - extraPeriodSecs * (overtimeNo - 1);
                break;
            default:
                throw new IllegalArgumentException("setElapsedTime");
        }
    }

    @Override
    public AlgoMatchState copy() {
        IcehockeyMatchState cc = new IcehockeyMatchState(matchFormat);
        cc.setEqualTo(this);
        return cc;

    }

    @Override
    public void setEqualTo(AlgoMatchState matchState) {
        super.setEqualTo(matchState);
        this.setGoalsFirstPeriodA(((IcehockeyMatchState) matchState).getGoalsFirstPeriodA());
        this.setGoalsSecondPeriodA(((IcehockeyMatchState) matchState).getGoalsSecondPeriodA());
        this.setGoalsThirdPeriodA(((IcehockeyMatchState) matchState).getGoalsThirdPeriodA());
        this.setGoalsFirstPeriodB(((IcehockeyMatchState) matchState).getGoalsFirstPeriodB());
        this.setGoalsSecondPeriodB(((IcehockeyMatchState) matchState).getGoalsSecondPeriodB());
        this.setGoalsThirdPeriodB(((IcehockeyMatchState) matchState).getGoalsThirdPeriodB());


        this.setGoalsA(((IcehockeyMatchState) matchState).getGoalsA());
        this.setGoalsB(((IcehockeyMatchState) matchState).getGoalsB());
        this.setElapsedTimeSecs(((IcehockeyMatchState) matchState).getElapsedTimeSecs(),
                        ((IcehockeyMatchState) matchState).getOvertimeNo());
        this.setMatchPeriod(((IcehockeyMatchState) matchState).getMatchPeriod());
        this.setTeamScoringLastGoal(((IcehockeyMatchState) matchState).getTeamScoringLastGoal());
        /* update cards infor and sendoff info */
        int[] temp_A = Arrays.copyOf(((IcehockeyMatchState) matchState).getSendOffTimer_A(), 2);
        int[] temp_B = Arrays.copyOf(((IcehockeyMatchState) matchState).getSendOffTimer_B(), 2);
        this.setSendOffTimer_A(temp_A);
        this.setSendOffTimer_B(temp_B);
        int[] temp_C = Arrays.copyOf(((IcehockeyMatchState) matchState).getMajorSinBinTimer_A(), 2);
        int[] temp_D = Arrays.copyOf(((IcehockeyMatchState) matchState).getMajorSinBinTimer_B(), 2);
        this.setMajorSinBinTimer_A(temp_C);
        this.setMajorSinBinTimer_B(temp_D);
        // minor sin bin
        this.setShootOutGoalsA(((IcehockeyMatchState) matchState).getShootOutGoalsA());
        this.setShootOutGoalsB(((IcehockeyMatchState) matchState).getShootOutGoalsB());
        this.setShootOutTimeCounter(((IcehockeyMatchState) matchState).getShootOutTimeCounter());
        this.setOvertimeNo(((IcehockeyMatchState) matchState).getOvertimeNo());
        this.setGoalSequence(((IcehockeyMatchState) matchState).getGoalSequence());
        this.setMajorSinBinA(((IcehockeyMatchState) matchState).getMajorSinBinA());
        this.setMajorSinBinA(((IcehockeyMatchState) matchState).getMajorSinBinB());
        this.setSendOffA(((IcehockeyMatchState) matchState).getMinorSinBinA());
        this.setSendOffB(((IcehockeyMatchState) matchState).getMinorSinBinB());
        this.setCurrentPeriodGoalsA(((IcehockeyMatchState) matchState).getCurrentPeriodGoalsA());
        this.setCurrentPeriodGoalsB(((IcehockeyMatchState) matchState).getCurrentPeriodGoalsB());
        this.setOvertimeNo(((IcehockeyMatchState) matchState).getOvertimeNo());
        this.setNormalTimeGoalsA(((IcehockeyMatchState) matchState).getNormalTimeGoalsA());
        this.setNormalTimeGoalsB(((IcehockeyMatchState) matchState).getNormalTimeGoalsB());
        this.setElapsedTimeSecsFirstGoal(((IcehockeyMatchState) matchState).getElapsedTimeSecsFirstGoal());
        this.setFiveMinsGoalsA(((IcehockeyMatchState) matchState).getFiveMinsGoalsA());
        this.setFiveMinsGoalsB(((IcehockeyMatchState) matchState).getFiveMinsGoalsB());
        this.setPreviousFiveMinsGoalsA(((IcehockeyMatchState) matchState).getPreviousFiveMinsGoalsA());
        this.setPreviousFiveMinsGoalsB(((IcehockeyMatchState) matchState).getPreviousFiveMinsGoalsB());
        this.setCurrentFiveMinsEnder(((IcehockeyMatchState) matchState).getCurrentFiveMinsEnder());
        this.setPreviousFiveMinsEnder(((IcehockeyMatchState) matchState).getPreviousFiveMinsEnder());
        this.setFiveMinsNo(((IcehockeyMatchState) matchState).getFiveMinsNo());
        this.isOvertimeOrShootOutPlayed = ((IcehockeyMatchState) matchState).isOvertimeOrShootOutPlayed;
        int size = ((IcehockeyMatchState) matchState).getGoalInfo().size();
        List<GoalInfo> temp_G = new ArrayList<GoalInfo>(size);
        for (int index = 0; index < size; index++)
            temp_G.add(((IcehockeyMatchState) matchState).getGoalInfo().get(index));
        this.setGoalInfo(temp_G);

    }

    @JsonIgnore
    @Override
    public MatchIncidentPrompt getNextPrompt() {
        MatchIncidentPrompt matchIncidentPrompt = null;
        if (matchPeriod == IcehockeyMatchPeriod.MATCH_COMPLETED)
            matchIncidentPrompt = new MatchIncidentPrompt("Match finished");
        else if (matchPeriod == IcehockeyMatchPeriod.PREMATCH || matchPeriod == IcehockeyMatchPeriod.AT_FIRST_PERIOD_END
                        || matchPeriod == IcehockeyMatchPeriod.AT_SECOND_PERIOD_END
                        || matchPeriod == IcehockeyMatchPeriod.AT_FULL_TIME
                        || matchPeriod == IcehockeyMatchPeriod.AT_EXTRA_PERIOD_END)
            matchIncidentPrompt = new MatchIncidentPrompt("S - start match", "S");

        else
            matchIncidentPrompt = new MatchIncidentPrompt(
                            "Next event (Nnn-Nothing happened for nn periods, H-Home goal, A-Away goal, A-Away goal, GH/A-Home/Away 2 minutes send off, "
                                            + "YH/A-Home/Away 5 minutes send off,"
                                            + "TH/A,QH/A- end send off home or away, E- end period)",
                            "N");
        return matchIncidentPrompt;
    }

    @Override
    public MatchIncident getMatchIncident(String response) {

        IcehockeyMatchIncidentType icehockeyMatchIncidentType = null;
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
                icehockeyMatchIncidentType = IcehockeyMatchIncidentType.GOAL;
                teamId = TeamId.A;
                break;
            case 'A':
                icehockeyMatchIncidentType = IcehockeyMatchIncidentType.GOAL;
                teamId = TeamId.B;
                break;

            case 'G':// green card
                if (elapsedTimeSecs == 0) {
                    throw new IllegalArgumentException("Card can not happen now");
                }

                try {
                    String teamCount = (response.substring(1));
                    if (teamCount.equalsIgnoreCase("H")) { // home team yellow card
                        icehockeyMatchIncidentType = IcehockeyMatchIncidentType.TWO_MINUTE_PENALTY_START;
                        teamId = TeamId.A;

                        break;
                    }
                    if (teamCount.equalsIgnoreCase("A")) { // away team yellow card
                        icehockeyMatchIncidentType = IcehockeyMatchIncidentType.TWO_MINUTE_PENALTY_START;
                        teamId = TeamId.B;
                        break;
                    }

                } catch (Exception e) {
                    // int teamCount = 3;
                }

                break;
            case 'Y':// yellow card
                if (elapsedTimeSecs == 0) {
                    throw new IllegalArgumentException("Card can not happen now");
                }
                try {
                    String teamCount = (response.substring(1));
                    if (teamCount.equalsIgnoreCase("H")) { // home team yellow card
                        icehockeyMatchIncidentType = IcehockeyMatchIncidentType.FIVE_MINUTE_PENALTY_START;
                        teamId = TeamId.A;
                        break;
                    }
                    if (teamCount.equalsIgnoreCase("A")) { // away team yellow card
                        icehockeyMatchIncidentType = IcehockeyMatchIncidentType.FIVE_MINUTE_PENALTY_START;
                        teamId = TeamId.B;
                        break;
                    }

                } catch (Exception e) {
                    // int teamCount = 3;
                }
                break;

            case 'T':// terminate minor sin bin
                if (minorSinBinA == 0 && minorSinBinB == 0) {
                    throw new IllegalArgumentException("no existing minor sin bins");
                }
                try {
                    String teamCount = (response.substring(1));
                    if (teamCount.equalsIgnoreCase("H")) { // home team yellow card
                        icehockeyMatchIncidentType = IcehockeyMatchIncidentType.TWO_MINUTE_PENALTY_END;
                        teamId = TeamId.A;
                        break;
                    }
                    if (teamCount.equalsIgnoreCase("A")) { // away team yellow card
                        icehockeyMatchIncidentType = IcehockeyMatchIncidentType.TWO_MINUTE_PENALTY_END;
                        teamId = TeamId.B;
                        break;
                    }

                } catch (Exception e) {
                    // int teamCount = 3;
                }
                break;

            case 'Q':// terminate major sin bin
                if (majorSinBinA == 0 && majorSinBinB == 0) {
                    throw new IllegalArgumentException("no existing minor sin bins");
                }
                try {
                    String teamCount = (response.substring(1));
                    if (teamCount.equalsIgnoreCase("H")) { // home team yellow card
                        icehockeyMatchIncidentType = IcehockeyMatchIncidentType.FIVE_MINUTE_PENALTY_END;
                        teamId = TeamId.A;
                        break;
                    }
                    if (teamCount.equalsIgnoreCase("A")) { // away team yellow card
                        icehockeyMatchIncidentType = IcehockeyMatchIncidentType.FIVE_MINUTE_PENALTY_END;
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
                    case AT_FIRST_PERIOD_END:
                    case AT_SECOND_PERIOD_END:
                    case AT_FULL_TIME:
                    case IN_EXTRA_TIME:
                    case AT_EXTRA_PERIOD_END:
                    case IN_SHOOTOUT:
                        elapsedTimeMatchIncidentType = ElapsedTimeMatchIncidentType.SET_PERIOD_START;
                        break;
                    default:
                        return null;
                }
                break;
            case 'E':
                switch (matchPeriod) {
                    case IN_FIRST_PERIOD:
                    case IN_SECOND_PERIOD:
                    case IN_THIRD_PERIOD:
                    case IN_EXTRA_TIME:
                    case IN_SHOOTOUT:
                        elapsedTimeMatchIncidentType = ElapsedTimeMatchIncidentType.SET_PERIOD_END;
                        break;
                    default:
                        return null; // invalid input so return null
                }
        }
        MatchIncident incident;
        if (icehockeyMatchIncidentType != null) {
            incident = new IcehockeyMatchIncident(icehockeyMatchIncidentType, incidentElapsedTimeSecs, teamId);
        } else {
            incident = new ElapsedTimeMatchIncident(elapsedTimeMatchIncidentType, incidentElapsedTimeSecs);
        }
        return incident;
    }

    private static final String goalsKey = "Goals";
    private static final String shootOutKey = "Shoot out goals";
    private static final String elapsedTimeKey = "Elapsed time";
    // private static final String injuryTimeKey = "Injury time to be played";
    private static final String matchPeriodKey = "Match period";
    @JsonIgnore
    private static final String periodSequenceKey = "Period sequence id";
    @JsonIgnore
    private static final String goalSequenceKey = "Goal sequence id";
    @JsonIgnore
    private static final String overTimeSequenceKey = "Over time sequence id";
    @JsonIgnore
    private static final String shootOutCounterKey = "Shoot out chances used";

    private static final String sendOffKey = "Minor Send off status";
    private static final String majorSendOffKey = "Major Send off status";

    @Override
    public LinkedHashMap<String, String> getAsMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put(goalsKey, String.format("%d-%d", goalsA, goalsB));
        int mins = elapsedTimeSecs / 60;
        int secs = elapsedTimeSecs - mins * 60;
        map.put(elapsedTimeKey, String.format("%d:%02d", mins, secs));
        map.put(matchPeriodKey, matchPeriod.toString());
        map.put(goalSequenceKey, getSequenceIdForGoal(0));
        map.put(sendOffKey, String.format("%d-%d", minorSinBinA, minorSinBinB));
        map.put(majorSendOffKey, String.format("%d-%d", majorSinBinA, majorSinBinB));
        map.put(shootOutKey, String.format("%d-%d", shootOutGoalsA, shootOutGoalsB));
        map.put(overTimeSequenceKey, Integer.toString(overtimeNo));
        map.put(shootOutCounterKey, Integer.toString(shootOutTimeCounter));
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
    @JsonIgnore
    @Override
    public boolean isMatchCompleted() {
        return (matchPeriod == IcehockeyMatchPeriod.MATCH_COMPLETED);
    }

    /**
     * Returns true if at the end of normal time
     * 
     * @return
     */
    @JsonIgnore
    public boolean isNormalTimeMatchCompleted() {
        return (elapsedTimeSecs >= normalPeriodSecs * 3);
    }

    /**
     * Returns true if at the end of one period and before the next one (if any) starts
     * 
     * @return
     */
    @JsonIgnore
    public boolean isPeriodCompleted() {
        return (matchPeriod == IcehockeyMatchPeriod.AT_FIRST_PERIOD_END
                        || matchPeriod == IcehockeyMatchPeriod.AT_SECOND_PERIOD_END
                        || matchPeriod == IcehockeyMatchPeriod.MATCH_COMPLETED);
    }

    /**
     * Returns the id of the team winning the current five minutes
     * 
     * @return
     */
    @JsonIgnore
    public TeamId getFiveMinsMatchWinner() {
        if (currentFiveMinsEnder == previousFiveMinsEnder || elapsedTimeSecs > 3 * normalPeriodSecs)
            return null;
        else if (currentFiveMinsEnder - previousFiveMinsEnder > 300) {
            // waiting for logic resulting skipped markets

            return TeamId.UNKNOWN;
        } else {
            TeamId teamId;
            if (previousFiveMinsGoalsA > previousFiveMinsGoalsB)
                teamId = TeamId.A;
            else if (previousFiveMinsGoalsA == previousFiveMinsGoalsB)
                teamId = TeamId.UNKNOWN;
            else
                teamId = TeamId.B;
            return teamId;
        }
    }

    /**
     * Gets the id of the team winning at the end of normalTime
     * 
     * @return UNKNOWN if a draw, else A or B
     */
    @JsonIgnore
    public TeamId getNormalTimeMatchWinner() {
        if (!isNormalTimeMatchCompleted())
            return null;
        else {
            TeamId teamId;
            if (normalTimeGoalsA > normalTimeGoalsB)
                teamId = TeamId.A;
            else if (normalTimeGoalsA == normalTimeGoalsB)
                teamId = TeamId.UNKNOWN;
            else
                teamId = TeamId.B;
            return teamId;
        }
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
     * Get # goals scored by team A in the previous match period
     * 
     * @return previousPeriodGoalsA
     */
    public int getPreviousPeriodGoalsA() {
        return previousPeriodGoalsA;
    }

    /**
     * Get # goals scored by team B in the previous match period
     * 
     * @return previousPeriodGoalsB
     */
    public int getPreviousPeriodGoalsB() {
        return previousPeriodGoalsB;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + currentPeriodGoalsA;
        result = prime * result + currentPeriodGoalsB;
        result = prime * result + elapsedTimeSecs;
        result = prime * result + elapsedTimeThisPeriodSecs;
        result = prime * result + extraPeriodSecs;
        result = prime * result + goalsA;
        result = prime * result + goalsB;
        result = prime * result + ((matchFormat == null) ? 0 : matchFormat.hashCode());
        result = prime * result + ((matchPeriod == null) ? 0 : matchPeriod.hashCode());
        result = prime * result + normalPeriodSecs;
        result = prime * result + minorSinBinA;
        result = prime * result + minorSinBinB;
        result = prime * result + ((teamScoringLastGoal == null) ? 0 : teamScoringLastGoal.hashCode());
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
        IcehockeyMatchState other = (IcehockeyMatchState) obj;
        if (currentPeriodGoalsA != other.currentPeriodGoalsA)
            return false;
        if (currentPeriodGoalsB != other.currentPeriodGoalsB)
            return false;
        if (elapsedTimeSecs != other.elapsedTimeSecs)
            return false;
        if (elapsedTimeThisPeriodSecs != other.elapsedTimeThisPeriodSecs)
            return false;
        if (extraPeriodSecs != other.extraPeriodSecs)
            return false;
        if (goalsA != other.goalsA)
            return false;
        if (goalsB != other.goalsB)
            return false;
        if (matchFormat == null) {
            if (other.matchFormat != null)
                return false;
        } else if (!matchFormat.equals(other.matchFormat))
            return false;
        if (matchPeriod != other.matchPeriod)
            return false;
        if (normalPeriodSecs != other.normalPeriodSecs)
            return false;
        if (minorSinBinA != other.minorSinBinA)
            return false;
        if (minorSinBinB != other.minorSinBinB)
            return false;
        if (teamScoringLastGoal != other.teamScoringLastGoal)
            return false;
        return true;
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
            case AT_FIRST_PERIOD_END:
            case AT_SECOND_PERIOD_END:
            case AT_FULL_TIME:
            case AT_EXTRA_PERIOD_END:
            case AT_SHOOTOUT_END:
            case MATCH_COMPLETED:
                break;
            case IN_SHOOTOUT:
            case IN_FIRST_PERIOD:
            case IN_SECOND_PERIOD:
            case IN_THIRD_PERIOD:
            case IN_EXTRA_TIME:
                int secs = getSecsSinceLastElapsedTimeFromIncident();
                setElapsedTime(elapsedTimeAtLastMatchIncidentSecs + secs);
                updatePrices = getSecsSinceLastPriceRecalc() >= 10;
                break;
            default:
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
                matchPeriod = IcehockeyMatchPeriod.IN_FIRST_PERIOD;
                elapsedTimeThisPeriodSecs = elapsedTimeSecs;
                break;

            case AT_FIRST_PERIOD_END:
            case IN_FIRST_PERIOD:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs;
                if (elapsedTimeThisPeriodSecs >= normalPeriodSecs) {
                    elapsedTimeSecs = normalPeriodSecs; // reset counter
                                                        // to remove
                                                        // injury time
                    elapsedTimeThisPeriodSecs = 0;
                    matchPeriod = IcehockeyMatchPeriod.IN_SECOND_PERIOD;
                }
                break;
            case AT_SECOND_PERIOD_END:
            case IN_SECOND_PERIOD:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs - normalPeriodSecs;
                if (elapsedTimeThisPeriodSecs >= normalPeriodSecs) {
                    elapsedTimeSecs = 2 * normalPeriodSecs;
                    elapsedTimeThisPeriodSecs = 0;
                    matchPeriod = IcehockeyMatchPeriod.IN_THIRD_PERIOD;
                }
                break;
            case AT_FULL_TIME:
            case IN_THIRD_PERIOD:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs - 2 * normalPeriodSecs;
                if (elapsedTimeThisPeriodSecs >= normalPeriodSecs) {
                    if (extraPeriodSecs == 0 || goalsA != goalsB) {
                        matchPeriod = IcehockeyMatchPeriod.MATCH_COMPLETED;
                    } else {
                        elapsedTimeSecs = 3 * normalPeriodSecs;
                        elapsedTimeThisPeriodSecs = 0;
                        matchPeriod = IcehockeyMatchPeriod.IN_EXTRA_TIME;
                        overtimeNo++;
                    }
                }
                break;
            case AT_EXTRA_PERIOD_END:
            case IN_EXTRA_TIME:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs - 3 * normalPeriodSecs - (overtimeNo - 1) * extraPeriodSecs;
                isOvertimeOrShootOutPlayed = true;
                // golden goals
                if (goalsA != goalsB) {
                    matchPeriod = IcehockeyMatchPeriod.MATCH_COMPLETED;
                    break;
                }

                if (elapsedTimeThisPeriodSecs >= extraPeriodSecs) {
                    elapsedTimeThisPeriodSecs = 0;
                    elapsedTimeSecs = 3 * normalPeriodSecs + (overtimeNo) * extraPeriodSecs;
                    if (goalsA != goalsB) {
                        matchPeriod = IcehockeyMatchPeriod.MATCH_COMPLETED;
                        break;
                    } else {
                        if (penaltiesPossible) {
                            matchPeriod = IcehockeyMatchPeriod.IN_SHOOTOUT;
                            break;
                        } else {
                            matchPeriod = IcehockeyMatchPeriod.AT_EXTRA_PERIOD_END;
                            overtimeNo++;
                            break;
                        }
                    }

                }
                break;
            case IN_SHOOTOUT:
                isOvertimeOrShootOutPlayed = true;
                shootOutTimeCounter++;
                if (isShootoutSettled()) {
                    goalsA += shootOutGoalsA > shootOutGoalsB ? 1 : 0;
                    goalsB += shootOutGoalsA > shootOutGoalsB ? 0 : 1;
                    matchPeriod = IcehockeyMatchPeriod.MATCH_COMPLETED;
                }

                break;

            case MATCH_COMPLETED:
                break;
            default:
                throw new IllegalArgumentException("Should not get here");
        }

    }

    /**
     * check team to x goal, period index = 0 means full time, 1 1st half, 2 second half...
     */
    public TeamId checkTeamToXGoals(int i, int periodIndex) {
        if (this.goalInfo.size() < i)
            return null;

        int goalsA = 0;
        int goalsB = 0;
        TeamId team = null;
        for (GoalInfo x : this.goalInfo) {
            if (x.getMatchPeriodIndex() < 4) { // change 3 to an input so can
                                               // control from outside
                if (x.team.equals(TeamId.A))
                    goalsA++;
                else
                    goalsB++;
            }

            if (goalsA >= i) {
                team = TeamId.A;
                break;
            }

            if (goalsB >= i) {
                team = TeamId.B;
                break;
            }
        }
        return team;
    }

    @Override
    public GamePeriod getGamePeriod() {
        GamePeriod gamePeriod = null;
        switch (matchPeriod) {
            case AT_FULL_TIME:
                gamePeriod = GamePeriod.NORMAL_TIME_END;
                break;
            case AT_FIRST_PERIOD_END:
                gamePeriod = GamePeriod.HALF_TIME;
                break;
            case AT_SECOND_PERIOD_END:
                gamePeriod = GamePeriod.HALF_TIME;
                break;
            case IN_SHOOTOUT:
                gamePeriod = GamePeriod.PENALTY_SHOOTING;
                break;
            case AT_SHOOTOUT_END:
                gamePeriod = GamePeriod.POSTMATCH; // can be wrong
                break;
            case IN_EXTRA_TIME:
                if (overtimeNo == 1) {
                    gamePeriod = GamePeriod.EXTRA_TIME_FIRST_HALF;
                    break;
                }
                if (overtimeNo == 2) {
                    gamePeriod = GamePeriod.EXTRA_TIME_SECOND_HALF;
                    break;
                }
            case AT_EXTRA_PERIOD_END:
                if (overtimeNo == 1)
                    gamePeriod = GamePeriod.EXTRA_TIME_HALF_TIME;
                if (overtimeNo == 2)
                    gamePeriod = GamePeriod.EXTRA_TIME_ENDED;
                break;

            case IN_FIRST_PERIOD:
                gamePeriod = GamePeriod.PERIOD1;
                break;
            case IN_SECOND_PERIOD:
                gamePeriod = GamePeriod.PERIOD2;
                break;
            case IN_THIRD_PERIOD:
                gamePeriod = GamePeriod.PERIOD3;
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

    @JsonIgnore
    public String getSequenceIdForFiveMinsResult() {
        return String.format("F%d", (int) fiveMinsNo + 1);
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
        return (matchPeriod == IcehockeyMatchPeriod.PREMATCH);
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
            case AT_FIRST_PERIOD_END:
            case AT_SHOOTOUT_END:
            case AT_SECOND_PERIOD_END:
            case AT_EXTRA_PERIOD_END:
            case IN_SHOOTOUT:
            case MATCH_COMPLETED:
            case PREMATCH:
                secs = 0;
                break;
            case IN_FIRST_PERIOD:
            case IN_SECOND_PERIOD:
            case IN_THIRD_PERIOD:
                secs = normalPeriodSecs - elapsedTimeThisPeriodSecs;
                break;
            case IN_EXTRA_TIME:
                secs = extraPeriodSecs - elapsedTimeThisPeriodSecs;
                break;
        }
        return secs;

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

    public TeamId checkFirstTeamScored() {
        if (this.goalInfo.size() == 0)
            return TeamId.UNKNOWN;
        else
            return this.goalInfo.get(0).getTeam();
    }

    /**
     * holds the state for the currently active game
     *
     * @author Robert
     * 
     */
    public class GoalInfo implements Serializable {
        private static final long serialVersionUID = 1L;
        int mins;
        int matchPeriodIndex;
        TeamId team;

        public int getMins() {
            return mins;
        }

        public void setMins(int mins) {
            this.mins = mins;
        }

        public TeamId getTeam() {
            return team;
        }

        public void setTeam(TeamId team) {
            this.team = team;
        }

        public int getMatchPeriodIndex() {
            return matchPeriodIndex;
        }

        public void setMatchPeriodIndex(int matchPeriodIndex) {
            this.matchPeriodIndex = matchPeriodIndex;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + mins;
            result = prime * result + ((team == null) ? 0 : team.hashCode());
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
            GoalInfo other = (GoalInfo) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
            if (mins != other.mins)
                return false;
            if (team != other.team)
                return false;
            return true;
        }

        private IcehockeyMatchState getOuterType() {
            return IcehockeyMatchState.this;
        }

    }

    @Override
    public IcehockeySimpleMatchState generateSimpleMatchState() {
        return new IcehockeySimpleMatchState(this.preMatch(), this.isMatchCompleted(), matchPeriod, elapsedTimeSecs,
                        isClockRunning(), goalsA, goalsB, goalsFirstPeriodA, goalsFirstPeriodB, goalsSecondPeriodA,
                        goalsSecondPeriodB, goalsThirdPeriodA, goalsThirdPeriodB, overtimeGoalsA, overtimeGoalsB,
                        minorSinBinA, majorSinBinA, minorSinBinB, majorSinBinB);
    }

    @Override
    public AlgoMatchState generateMatchStateForMatchResult(MatchResultMap matchResultMap) {
        /*
         * not yet supported for this sport
         */
        return null;
    }
}


