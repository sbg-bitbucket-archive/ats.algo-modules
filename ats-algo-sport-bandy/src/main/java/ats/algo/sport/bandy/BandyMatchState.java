package ats.algo.sport.bandy;

import java.util.ArrayList;
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
import ats.algo.core.common.ElapsedTimeMatchIncident;
import ats.algo.core.common.ElapsedTimeMatchIncident.ElapsedTimeMatchIncidentType;
import ats.algo.core.common.TeamId;
import ats.algo.core.matchresult.MatchResultMap;
import ats.algo.sport.bandy.BandyMatchIncident.BandyMatchIncidentType;

/**
 * AlgoMatchState class for Bandy
 * 
 * @author Jin
 *
 */
public class BandyMatchState extends AlgoMatchState {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // /* no need timers if use feeds terminate sinBins*/
    @JsonIgnore
    private ArrayList<Integer> minorSinBinTimer_A = new ArrayList<Integer>();
    @JsonIgnore
    private ArrayList<Integer> minorSinBinTimer_B = new ArrayList<Integer>();
    @JsonIgnore
    private ArrayList<Integer> majorSinBinTimer_A = new ArrayList<Integer>();
    @JsonIgnore
    private ArrayList<Integer> majorSinBinTimer_B = new ArrayList<Integer>();
    @JsonIgnore
    private ArrayList<Integer> tenMinsSinBinTimer_A = new ArrayList<Integer>();
    @JsonIgnore
    private ArrayList<Integer> tenMinsSinBinTimer_B = new ArrayList<Integer>();

    @JsonIgnore
    private int elapsedTimeSecsFirstGoal = 0;

    private final int extraPeriodSecs;

    private int majorSinBinA;
    private int majorSinBinB;
    private int tenMinsSinBinA;
    private int tenMinsSinBinB;
    private int goalsA;
    private int goalsB;
    private int elapsedTimeSecs;
    private int elapsedTimeAtLastMatchIncidentSecs; // the time from the last matchIncident report
    private int elapsedTimeThisPeriodSecs; // recent MatchIncident;
    private final int normalPeriodSecs;

    private int elapsedTimeAtLastGoalSecs;
    private BandyMatchPeriod matchPeriodInWhichLastGoalScored;
    private BandyMatchPeriod matchPeriod; // the state following the most
    private BandyMatchFormat matchFormat;
    private TeamId teamScoringLastGoal;
    private int currentPeriodGoalsA;
    private int currentPeriodGoalsB;

    private int goalsFirstHalfA;
    private int goalsFirstHalfB;
    private int goalsSecondHalfA;
    private int goalsSecondHalfB;

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
    public BandyMatchState() {
        this(new BandyMatchFormat());
        // super();
        // normalPeriodSecs = 90 * 30;
        // elapsedTimeThisPeriodSecs = 2800 - normalPeriodSecs;
        //
        // minorSinBinTimer_A = new ArrayList<Integer>();
        // minorSinBinTimer_B = new ArrayList<Integer>();
        //
        // majorSinBinTimer_A = new ArrayList<Integer>();
        // majorSinBinTimer_B = new ArrayList<Integer>();
        //
        // majorSinBinA = 0;
        // majorSinBinB = 0;
        // tenMinsSinBinA = 0;
        // tenMinsSinBinB = 0;
        //
        // goalsA = 3;
        // goalsB = 2;
        // elapsedTimeSecs = 2800;
        // this.matchFormat = null;
        // teamScoringLastGoal = TeamId.A;
        // currentPeriodGoalsA = 1;
        // currentPeriodGoalsB = 0;
        // matchPeriod = BandyMatchPeriod.IN_SECOND_HALF;
        // extraPeriodSecs = 15 * 60;

    }

    /**
     * Class constructor
     * 
     * @param matchFormat
     */
    public BandyMatchState(MatchFormat matchFormat) {
        super();
        this.matchFormat = (BandyMatchFormat) matchFormat;
        matchPeriod = BandyMatchPeriod.PREMATCH;
        normalPeriodSecs = this.matchFormat.getNormalTimeMinutes() * 30;
        extraPeriodSecs = this.matchFormat.getExtraTimeMinutes() * 60;
        penaltiesPossible = this.matchFormat.isPenaltiesPossible();
        elapsedTimeAtLastGoalSecs = -1;
        elapsedTimeAtLastMatchIncidentSecs = -1;
        minorSinBinTimer_A = new ArrayList<Integer>();
        minorSinBinTimer_B = new ArrayList<Integer>();
        majorSinBinTimer_A = new ArrayList<Integer>();
        majorSinBinTimer_B = new ArrayList<Integer>();
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
     * @return
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
     * @return
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
     * Get the sequence number of the goal since the begging of match
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
     * Check sequence number of the current five minutes since the begging of match
     * 
     * @param fiveMinsNo
     */
    @JsonIgnore
    public void setFiveMinsNo(int fiveMinsNo) {
        this.fiveMinsNo = fiveMinsNo;
    }

    /**
     * Get the number of goal scored by team A in the last five minutes of the game
     * 
     * @return
     */
    @JsonIgnore
    public int getPreviousFiveMinsGoalsA() {
        return previousFiveMinsGoalsA;
    }

    /**
     * Set the number of goal scored by team A in the last five minutes of the game
     * 
     * @param previousFiveMinsGoalsA
     */
    @JsonIgnore
    public void setPreviousFiveMinsGoalsA(int previousFiveMinsGoalsA) {
        this.previousFiveMinsGoalsA = previousFiveMinsGoalsA;
    }

    /**
     * Get the number of goal scored by team B in the last five minutes of the game
     * 
     * @return
     */
    @JsonIgnore
    public int getPreviousFiveMinsGoalsB() {
        return previousFiveMinsGoalsB;
    }

    /**
     * Set the number of goal scored by team B in the last five minutes of the game
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
     * @return
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
     * Get no of players from team A current on 10 minutes sin bin
     * 
     * @return tenMinsSinBinA
     */
    public int getTenMinsSinBinA() {
        return tenMinsSinBinA;
    }

    /**
     * Set no of players from team A current on 10 minutes sin bin
     * 
     * @param tenMinsSinBinA
     */
    public void setTenMinsSinBinA(int tenMinsSinBinA) {
        this.tenMinsSinBinA = tenMinsSinBinA;
    }

    /**
     * Get no of players from team B current on 10 minutes sin bin
     * 
     * @return
     */
    public int getTenMinsSinBinB() {
        return tenMinsSinBinB;
    }

    /**
     * Set no of players from team B current on 10 minutes sin bin
     * 
     * @param tenMinsSinBinA
     */
    public void setTenMinsSinBinB(int tenMinsSinBinB) {
        this.tenMinsSinBinB = tenMinsSinBinB;
    }

    /**
     * Get 10 minutes sin bin ending time for team A players in a ArrayList
     * 
     * @return tenMinsSinBinTimer_A
     */
    @JsonIgnore
    public ArrayList<Integer> getTenMinsSinBinTimer_A() {
        return tenMinsSinBinTimer_A;
    }

    /**
     * Set 10 minutes sin bin ending time for team A players in a ArrayList
     * 
     * @param tenMinsSinBinTimer_A
     */
    @JsonIgnore
    public void setTenMinsSinBinTimer_A(ArrayList<Integer> tenMinsSinBinTimer_A) {
        this.tenMinsSinBinTimer_A = tenMinsSinBinTimer_A;
    }

    /**
     * Get 10 minutes sin bin ending time for team B players in a ArrayList
     * 
     * @return
     */
    @JsonIgnore
    public ArrayList<Integer> getTenMinsSinBinTimer_B() {
        return tenMinsSinBinTimer_B;
    }

    /**
     * Set 10 minutes sin bin ending time for team B players in a ArrayList
     * 
     * @param tenMinsSinBinTimer_B
     */
    @JsonIgnore
    public void setTenMinsSinBinTimer_B(ArrayList<Integer> tenMinsSinBinTimer_B) {
        this.tenMinsSinBinTimer_B = tenMinsSinBinTimer_B;
    }

    /**
     * Get minor sin bin ending time for team A players
     * 
     * @return
     */
    @JsonIgnore
    public ArrayList<Integer> getMinorSinBinTimer_A() {
        return minorSinBinTimer_A;
    }

    /**
     * Set minor sin bin ending time for team A players
     * 
     * @param minorSinBinTimer_A
     */
    @JsonIgnore
    public void setMinorSinBinTimer_A(ArrayList<Integer> minorSinBinTimer_A) {
        this.minorSinBinTimer_A = minorSinBinTimer_A;
    }

    /**
     * Get minor sin bin ending time for team B players
     * 
     * @return
     */
    @JsonIgnore
    public ArrayList<Integer> getMinorSinBinTimer_B() {
        return minorSinBinTimer_B;
    }

    /**
     * Set minor sin bin ending time for team B players
     * 
     * @param minorSinBinTimer_B
     */
    @JsonIgnore
    public void setMinorSinBinTimer_B(ArrayList<Integer> minorSinBinTimer_B) {
        this.minorSinBinTimer_B = minorSinBinTimer_B;
    }

    /**
     * get major sin bin ending time for team A players
     * 
     * @return
     */
    @JsonIgnore
    public ArrayList<Integer> getMajorSinBinTimer_A() {
        return majorSinBinTimer_A;
    }

    /**
     * set major sin bin ending time for team A players
     * 
     * @param majorSinBinTimer_A
     */
    @JsonIgnore
    public void setMajorSinBinTimer_A(ArrayList<Integer> majorSinBinTimer_A) {
        this.majorSinBinTimer_A = majorSinBinTimer_A;
    }

    /**
     * get major sin bin ending time for team B players
     * 
     * @return
     */
    @JsonIgnore
    public ArrayList<Integer> getMajorSinBinTimer_B() {
        return majorSinBinTimer_B;
    }

    /**
     * set major sin bin ending time for team B players
     * 
     * @param majorSinBinTimer_B
     */
    @JsonIgnore
    public void setMajorSinBinTimer_B(ArrayList<Integer> majorSinBinTimer_B) {
        this.majorSinBinTimer_B = majorSinBinTimer_B;
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


    public int getGoalsFirstHalfA() {
        return goalsFirstHalfA;
    }

    public void setGoalsFirstHalfA(int goalsFirstHalfA) {
        this.goalsFirstHalfA = goalsFirstHalfA;
    }

    public int getGoalsFirstHalfB() {
        return goalsFirstHalfB;
    }

    public void setGoalsFirstHalfB(int goalsFirstHalfB) {
        this.goalsFirstHalfB = goalsFirstHalfB;
    }

    public int getGoalsSecondHalfA() {
        return goalsSecondHalfA;
    }

    public void setGoalsSecondHalfA(int goalsSecondHalfA) {
        this.goalsSecondHalfA = goalsSecondHalfA;
    }

    public int getGoalsSecondHalfB() {
        return goalsSecondHalfB;
    }

    public void setGoalsSecondHalfB(int goalsSecondHalfB) {
        this.goalsSecondHalfB = goalsSecondHalfB;
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
            case IN_EXTRA_TIME_FIRST_HALF:
            case AT_EXTRA_TIME_HALF_TIME:
            case IN_EXTRA_TIME_SECOND_HALF:
                n = 4;
                break;
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

    void setElapsedTimeSecs(int elapsedTimeSecs) {
        this.elapsedTimeSecs = elapsedTimeSecs;
    }

    /**
     * Set the elapsed time in match and in current period
     * 
     * @param elapsedTimeSecs
     * @param overtimeNoIn which overtime period we are in. 0 if not in overtime
     */
    /* replaced by the above setter */
    public void setElapsedTimeSecs(int elapsedTimeSecs, int overtimeNoIn) {
        this.elapsedTimeSecs = elapsedTimeSecs;
        this.elapsedTimeThisPeriodSecs = elapsedTimeSecs;
        matchPeriod = BandyMatchPeriod.IN_FIRST_HALF;
        if (elapsedTimeSecs == normalPeriodSecs) {
            this.elapsedTimeThisPeriodSecs = 0;
            matchPeriod = BandyMatchPeriod.AT_HALF_TIME;
        }
        if (elapsedTimeSecs > normalPeriodSecs) {
            this.elapsedTimeThisPeriodSecs = elapsedTimeSecs - normalPeriodSecs;
            matchPeriod = BandyMatchPeriod.IN_SECOND_HALF;
        }
        if (elapsedTimeSecs == 2 * normalPeriodSecs) {
            this.elapsedTimeThisPeriodSecs = 0;
            matchPeriod = BandyMatchPeriod.AT_FULL_TIME;
        }
        if (elapsedTimeSecs > 2 * normalPeriodSecs) {
            this.elapsedTimeThisPeriodSecs = elapsedTimeSecs - 2 * normalPeriodSecs;
            matchPeriod = BandyMatchPeriod.IN_EXTRA_TIME_FIRST_HALF;
        }
        if (elapsedTimeSecs == 2 * normalPeriodSecs + extraPeriodSecs) {
            this.elapsedTimeThisPeriodSecs = 0;
            matchPeriod = BandyMatchPeriod.AT_EXTRA_TIME_HALF_TIME;
        }
        if (elapsedTimeSecs > 2 * normalPeriodSecs + extraPeriodSecs) {
            this.elapsedTimeThisPeriodSecs = elapsedTimeSecs - overtimeNoIn * extraPeriodSecs;
            matchPeriod = BandyMatchPeriod.IN_EXTRA_TIME_SECOND_HALF;
        }
    }

    /**
     * Get the current matchPeriod
     * 
     * @return matchPeriod
     */
    public BandyMatchPeriod getMatchPeriod() {
        return matchPeriod;
    }

    /**
     * Set the current match period
     * 
     * @param matchPeriod
     */
    public void setMatchPeriod(BandyMatchPeriod matchPeriod) {
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

        if (matchIncident.getClass() == BandyMatchIncident.class) {

            TeamId teamId = ((BandyMatchIncident) matchIncident).getTeamId();

            switch (((BandyMatchIncident) matchIncident).getIncidentSubType()) {

                case MAJOR_PENALTY_END: // power play end
                    switch (teamId) {
                        case A:
                            majorSinBinA--;
                            cleanSuspensionTimer(majorSinBinTimer_A, majorSinBinA);
                            if (majorSinBinA < 0)
                                throw new IllegalArgumentException("Send-Off number is wrong");
                            break;

                        case B:
                            majorSinBinB--;
                            cleanSuspensionTimer(majorSinBinTimer_B, majorSinBinB);
                            if (majorSinBinB < 0)
                                throw new IllegalArgumentException("Send-Off number is wrong");
                            break;
                        default:
                            throw new IllegalArgumentException("team Id Unknown");

                    }
                    break;

                case TEN_MINS_PENALTY_END: // power play end
                    switch (teamId) {
                        case A:
                            tenMinsSinBinA--;
                            cleanSuspensionTimer(tenMinsSinBinTimer_A, tenMinsSinBinA);
                            if (tenMinsSinBinA < 0)
                                throw new IllegalArgumentException("Send-Off number is wrong");
                            break;

                        case B:
                            tenMinsSinBinB--;
                            cleanSuspensionTimer(tenMinsSinBinTimer_B, tenMinsSinBinB);
                            if (tenMinsSinBinB < 0)
                                throw new IllegalArgumentException("Send-Off number is wrong");
                            break;
                        default:
                            throw new IllegalArgumentException("team Id Unknown");

                    }
                    break;

                case MAJOR_PENALTY_START: // power play started
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

                case TEN_MINS_PENALTY_START: // power play started
                    switch (teamId) {
                        case A:
                            sinBinTimerTrigger(tenMinsSinBinTimer_A, elapsedTimeSecs, 10 * 60);
                            tenMinsSinBinA++;
                            break;

                        case B:
                            sinBinTimerTrigger(tenMinsSinBinTimer_B, elapsedTimeSecs, 10 * 60);
                            tenMinsSinBinB++;
                            break;
                        case UNKNOWN:
                            throw new IllegalArgumentException("TeamId must be specified for ten minutes power play");

                    }
                    break;

                case GOAL:
                    elapsedTimeAtLastGoalSecs = elapsedTimeSecs;
                    matchPeriodInWhichLastGoalScored = matchPeriod;
                    switch (teamId) {
                        case A:
                            if (!matchPeriod.equals(BandyMatchPeriod.IN_SHOOTOUT)) {

                                if (!matchPeriod.equals(BandyMatchPeriod.IN_EXTRA_TIME_FIRST_HALF)
                                                && !matchPeriod.equals(BandyMatchPeriod.IN_EXTRA_TIME_SECOND_HALF))
                                    normalTimeGoalsA++;

                                updateFiveMinsStatus();
                                fiveMinsGoalsA++;

                                if (goalsA == 0 && goalsB == 0) {
                                    elapsedTimeSecsFirstGoal = elapsedTimeSecs;
                                }

                                if (matchPeriod.equals(BandyMatchPeriod.IN_FIRST_HALF))
                                    goalsFirstHalfA++;
                                if (matchPeriod.equals(BandyMatchPeriod.IN_SECOND_HALF))
                                    goalsSecondHalfA++;

                                goalsA++;
                                currentPeriodGoalsA++;
                                teamScoringLastGoal = TeamId.A;
                                goalSequence++;

                            } else {
                                shootOutGoalsA++;
                                shootOutTimeCounter++;
                                if (isShootoutSettled()) {
                                    this.goalsA++;// = shootOutGoalsA>shootOutGoalsB? 1:0;
                                    matchPeriod = BandyMatchPeriod.MATCH_COMPLETED;
                                }

                            }
                            break;
                        case B:
                            if (!matchPeriod.equals(BandyMatchPeriod.IN_SHOOTOUT)) {

                                if (!matchPeriod.equals(BandyMatchPeriod.IN_EXTRA_TIME_FIRST_HALF)
                                                && !matchPeriod.equals(BandyMatchPeriod.IN_EXTRA_TIME_SECOND_HALF))
                                    normalTimeGoalsB++;

                                updateFiveMinsStatus();
                                fiveMinsGoalsB++;

                                if (goalsA == 0 && goalsB == 0) {
                                    elapsedTimeSecsFirstGoal = elapsedTimeSecs;
                                }

                                if (matchPeriod.equals(BandyMatchPeriod.IN_FIRST_HALF))
                                    goalsFirstHalfB++;
                                if (matchPeriod.equals(BandyMatchPeriod.IN_SECOND_HALF))
                                    goalsSecondHalfB++;

                                goalsB++;
                                currentPeriodGoalsB++;
                                teamScoringLastGoal = TeamId.B;
                                goalSequence++;

                            } else {
                                shootOutGoalsB++;
                                shootOutTimeCounter++;
                                if (isShootoutSettled()) {
                                    this.goalsB++;
                                    matchPeriod = BandyMatchPeriod.MATCH_COMPLETED;
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
        } else// must be ElapsedTimeMatchIncident
            switch (((ElapsedTimeMatchIncident) matchIncident).getIncidentSubType()) {

                case SET_MATCH_CLOCK:
                    updateFiveMinsStatus();
                    if (matchPeriod.equals(BandyMatchPeriod.IN_SHOOTOUT)) {
                        shootOutTimeCounter++;
                        if (isShootoutSettled()) {
                            this.goalsA += shootOutGoalsA > shootOutGoalsB ? 1 : 0;
                            this.goalsB += shootOutGoalsA > shootOutGoalsB ? 0 : 1;
                            matchPeriod = BandyMatchPeriod.MATCH_COMPLETED;
                        }
                    }
                    break;

                case SET_PERIOD_START:
                    switch (matchPeriod) {
                        case PREMATCH:
                            elapsedTimeThisPeriodSecs = 0;
                            matchPeriod = BandyMatchPeriod.IN_FIRST_HALF;
                            break;
                        case IN_FIRST_HALF:
                            break;
                        case AT_HALF_TIME:
                            elapsedTimeThisPeriodSecs = 0;
                            matchPeriod = BandyMatchPeriod.IN_SECOND_HALF;
                            break;
                        case IN_SECOND_HALF:
                            break;
                        case AT_FULL_TIME:
                            if (extraPeriodSecs > 0)
                                overtimeNo++;

                            elapsedTimeThisPeriodSecs = 0;
                            matchPeriod = BandyMatchPeriod.IN_EXTRA_TIME_FIRST_HALF;
                            break;

                        case IN_EXTRA_TIME_FIRST_HALF:
                            break;

                        case AT_EXTRA_TIME_HALF_TIME:
                            overtimeNo++;
                            elapsedTimeThisPeriodSecs = 0;
                            matchPeriod = BandyMatchPeriod.IN_EXTRA_TIME_SECOND_HALF;
                            break;

                        case AT_EXTRA_TIME_END:
                            overtimeNo++;
                            elapsedTimeThisPeriodSecs = 0;
                            if (penaltiesPossible && goalsA == goalsB) {
                                matchPeriod = BandyMatchPeriod.IN_SHOOTOUT;
                                break;
                            } else {
                                if (goalsA != goalsB) {
                                    matchPeriod = BandyMatchPeriod.MATCH_COMPLETED;
                                } else {
                                    matchPeriod = BandyMatchPeriod.IN_EXTRA_TIME_FIRST_HALF;
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
                        case IN_FIRST_HALF:
                            matchPeriod = BandyMatchPeriod.AT_HALF_TIME;
                            previousPeriodGoalsA = currentPeriodGoalsA;
                            previousPeriodGoalsB = currentPeriodGoalsB;
                            currentPeriodGoalsA = 0;
                            currentPeriodGoalsB = 0;
                            setElapsedTime(normalPeriodSecs);
                            elapsedTimeAtLastMatchIncidentSecs = normalPeriodSecs;
                            setClockTimeOfLastElapsedTimeFromIncident();
                            break;

                        case AT_HALF_TIME:
                            break;

                        case IN_SECOND_HALF:

                            if (extraPeriodSecs == 0 || goalsA != goalsB)
                                matchPeriod = BandyMatchPeriod.MATCH_COMPLETED;
                            else
                                matchPeriod = BandyMatchPeriod.AT_FULL_TIME;
                            previousPeriodGoalsA = currentPeriodGoalsA;
                            previousPeriodGoalsB = currentPeriodGoalsB;
                            currentPeriodGoalsA = 0;
                            currentPeriodGoalsB = 0;
                            setElapsedTime(normalPeriodSecs * 2);
                            elapsedTimeAtLastMatchIncidentSecs = normalPeriodSecs;
                            setClockTimeOfLastElapsedTimeFromIncident();
                            break;

                        case AT_FULL_TIME:
                            break;

                        case IN_EXTRA_TIME_FIRST_HALF:
                            if (goalsA == goalsB) {
                                matchPeriod = BandyMatchPeriod.AT_EXTRA_TIME_HALF_TIME;
                            } else {
                                matchPeriod = BandyMatchPeriod.MATCH_COMPLETED;
                            }
                            previousPeriodGoalsA = currentPeriodGoalsA;
                            previousPeriodGoalsB = currentPeriodGoalsB;
                            currentPeriodGoalsA = 0;
                            currentPeriodGoalsB = 0;
                            setElapsedTime(normalPeriodSecs * 2 + (overtimeNo) * extraPeriodSecs);

                            elapsedTimeAtLastMatchIncidentSecs = normalPeriodSecs + extraPeriodSecs;

                            setClockTimeOfLastElapsedTimeFromIncident();
                            break;
                        case AT_EXTRA_TIME_HALF_TIME:
                        case IN_EXTRA_TIME_SECOND_HALF:
                            if (goalsA == goalsB) {
                                matchPeriod = BandyMatchPeriod.AT_EXTRA_TIME_END;
                            } else {
                                matchPeriod = BandyMatchPeriod.MATCH_COMPLETED;
                            }
                            previousPeriodGoalsA = currentPeriodGoalsA;
                            previousPeriodGoalsB = currentPeriodGoalsB;
                            currentPeriodGoalsA = 0;
                            currentPeriodGoalsB = 0;
                            setElapsedTime(normalPeriodSecs * 2 + (overtimeNo) * extraPeriodSecs);

                            elapsedTimeAtLastMatchIncidentSecs = normalPeriodSecs + 2 * extraPeriodSecs;

                            setClockTimeOfLastElapsedTimeFromIncident();
                            break;

                        case IN_SHOOTOUT:
                            matchPeriod = BandyMatchPeriod.MATCH_COMPLETED;
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

    private void cleanSuspensionTimer(ArrayList<Integer> twoMinsSuspension, int twoMinsSuspendedNo) {

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

    private void sinBinTimerTrigger(ArrayList<Integer> sendOffTimer_local, int elapsedTimeSecsIn, int sendOffLength) {
        // clean up sin bin timer first

        if (sendOffTimer_local.size() == 0) {
            sendOffTimer_local.add(elapsedTimeSecsIn + sendOffLength);
        } else {

            sendOffTimer_local.add(elapsedTimeSecsIn + sendOffLength);
            // disable auto-clean
            // for (int i = 0; i < sendOffTimer_local.size(); i++) {
            //
            // if (sendOffTimer_local.get(i) < elapsedTimeSecsIn )
            // sendOffTimer_local.remove(i); // clean settled suspension
            // }

        }
    }

    private void setElapsedTime(int elapsedTimeSecs) {
        this.elapsedTimeSecs = elapsedTimeSecs;
        switch (matchPeriod) {
            case PREMATCH:
            case AT_HALF_TIME:
            case AT_FULL_TIME:
            case AT_EXTRA_TIME_HALF_TIME:
            case AT_EXTRA_TIME_END:
            case MATCH_COMPLETED:
                /*
                 * do nothing
                 */
                break;
            case IN_FIRST_HALF:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs;
                break;
            case IN_SECOND_HALF:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs - normalPeriodSecs;
                break;
            case IN_EXTRA_TIME_FIRST_HALF:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs - 2 * normalPeriodSecs;
                break;
            case IN_EXTRA_TIME_SECOND_HALF:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs - 2 * normalPeriodSecs - extraPeriodSecs;
                break;
            case IN_SHOOTOUT:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs - 2 * normalPeriodSecs - extraPeriodSecs * 2;
                break;
            default:
                throw new IllegalArgumentException("setElapsedTime");
        }
    }

    @Override
    public AlgoMatchState copy() {
        BandyMatchState cc = new BandyMatchState(matchFormat);
        cc.setEqualTo(this);
        return cc;

    }

    @Override
    public void setEqualTo(AlgoMatchState matchState) {
        super.setEqualTo(matchState);
        this.setGoalsA(((BandyMatchState) matchState).getGoalsA());
        this.setGoalsB(((BandyMatchState) matchState).getGoalsB());

        this.setElapsedTimeSecs(((BandyMatchState) matchState).getElapsedTimeSecs());

        // this.setElapsedTimeSecs(((BandyMatchState) matchState).getElapsedTimeSecs(),
        // ((BandyMatchState) matchState).getOvertimeNo());

        this.setMatchPeriod(((BandyMatchState) matchState).getMatchPeriod());
        this.setTeamScoringLastGoal(((BandyMatchState) matchState).getTeamScoringLastGoal());
        /* update cards infor and sendoff info */
        this.minorSinBinTimer_A.clear();
        this.minorSinBinTimer_A = cloneIntegerList(((BandyMatchState) matchState).getMinorSinBinTimer_A());

        this.minorSinBinTimer_B.clear();
        this.minorSinBinTimer_B = cloneIntegerList(((BandyMatchState) matchState).getMinorSinBinTimer_B());

        this.majorSinBinTimer_A.clear();
        this.majorSinBinTimer_A = cloneIntegerList(((BandyMatchState) matchState).getMajorSinBinTimer_A());

        this.majorSinBinTimer_B.clear();
        this.majorSinBinTimer_B = cloneIntegerList(((BandyMatchState) matchState).getMajorSinBinTimer_B());

        this.tenMinsSinBinTimer_A.clear();
        this.tenMinsSinBinTimer_A = cloneIntegerList(((BandyMatchState) matchState).getTenMinsSinBinTimer_A());

        this.tenMinsSinBinTimer_B.clear();
        this.tenMinsSinBinTimer_B = cloneIntegerList(((BandyMatchState) matchState).getTenMinsSinBinTimer_B());

        this.setTenMinsSinBinA(((BandyMatchState) matchState).getTenMinsSinBinA());
        this.setTenMinsSinBinB(((BandyMatchState) matchState).getTenMinsSinBinB());
        this.setMajorSinBinA(((BandyMatchState) matchState).getMajorSinBinA());
        this.setMajorSinBinB(((BandyMatchState) matchState).getMajorSinBinB());
        this.setShootOutGoalsA(((BandyMatchState) matchState).getShootOutGoalsA());
        this.setShootOutGoalsB(((BandyMatchState) matchState).getShootOutGoalsB());
        this.setShootOutTimeCounter(((BandyMatchState) matchState).getShootOutTimeCounter());
        this.setOvertimeNo(((BandyMatchState) matchState).getOvertimeNo());
        this.setGoalSequence(((BandyMatchState) matchState).getGoalSequence());

        this.setCurrentPeriodGoalsA(((BandyMatchState) matchState).getCurrentPeriodGoalsA());
        this.setCurrentPeriodGoalsB(((BandyMatchState) matchState).getCurrentPeriodGoalsB());
        this.setOvertimeNo(((BandyMatchState) matchState).getOvertimeNo());
        this.setNormalTimeGoalsA(((BandyMatchState) matchState).getNormalTimeGoalsA());
        this.setNormalTimeGoalsB(((BandyMatchState) matchState).getNormalTimeGoalsB());
        this.setElapsedTimeSecsFirstGoal(((BandyMatchState) matchState).getElapsedTimeSecsFirstGoal());
        this.setFiveMinsGoalsA(((BandyMatchState) matchState).getFiveMinsGoalsA());
        this.setFiveMinsGoalsB(((BandyMatchState) matchState).getFiveMinsGoalsB());
        this.setPreviousFiveMinsGoalsA(((BandyMatchState) matchState).getPreviousFiveMinsGoalsA());
        this.setPreviousFiveMinsGoalsB(((BandyMatchState) matchState).getPreviousFiveMinsGoalsB());
        this.setCurrentFiveMinsEnder(((BandyMatchState) matchState).getCurrentFiveMinsEnder());
        this.setPreviousFiveMinsEnder(((BandyMatchState) matchState).getPreviousFiveMinsEnder());
        this.setFiveMinsNo(((BandyMatchState) matchState).getFiveMinsNo());

    }

    @JsonIgnore
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
        if (matchPeriod == BandyMatchPeriod.MATCH_COMPLETED)
            matchIncidentPrompt = new MatchIncidentPrompt("Match finished");
        else if (matchPeriod == BandyMatchPeriod.PREMATCH || matchPeriod == BandyMatchPeriod.AT_HALF_TIME
                        || matchPeriod == BandyMatchPeriod.AT_FULL_TIME
                        || matchPeriod == BandyMatchPeriod.AT_EXTRA_TIME_HALF_TIME
                        || matchPeriod == BandyMatchPeriod.AT_EXTRA_TIME_END)
            matchIncidentPrompt = new MatchIncidentPrompt("S - start match", "S");

        else
            matchIncidentPrompt = new MatchIncidentPrompt(
                            "Next event (Nnn-Nothing happened for nn periods, H-Home goal, A-Away goal, A-Away goal, "
                                            + "YH/A-Home/Away 5 minutes send off,"
                                            + "BH/A-Home/Away 10 minutes send off (Blue Card),"
                                            + "QH/A,UH/A- end send off home or away, E- end period)",
                            "N");
        return matchIncidentPrompt;
    }

    @Override
    public MatchIncident getMatchIncident(String response) {

        BandyMatchIncidentType bandyMatchIncidentType = null;
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
                bandyMatchIncidentType = BandyMatchIncidentType.GOAL;
                teamId = TeamId.A;
                break;
            case 'A':
                bandyMatchIncidentType = BandyMatchIncidentType.GOAL;
                teamId = TeamId.B;
                break;

            case 'Y':// yellow card
                if (elapsedTimeSecs == 0) {
                    throw new IllegalArgumentException("Card can not happen now");
                }
                try {
                    String teamCount = (response.substring(1));
                    if (teamCount.equalsIgnoreCase("H")) { // home team yellow card
                        bandyMatchIncidentType = BandyMatchIncidentType.MAJOR_PENALTY_START;
                        teamId = TeamId.A;
                        break;
                    }
                    if (teamCount.equalsIgnoreCase("A")) { // away team yellow card
                        bandyMatchIncidentType = BandyMatchIncidentType.MAJOR_PENALTY_START;
                        teamId = TeamId.B;
                        break;
                    }

                } catch (Exception e) {
                    // int teamCount = 3;
                }
                break;

            case 'B':// red card 10 mins
                if (elapsedTimeSecs == 0) {
                    throw new IllegalArgumentException("Card can not happen now");
                }
                try {
                    String teamCount = (response.substring(1));
                    if (teamCount.equalsIgnoreCase("H")) { // home team yellow card
                        bandyMatchIncidentType = BandyMatchIncidentType.TEN_MINS_PENALTY_START;
                        teamId = TeamId.A;
                        break;
                    }
                    if (teamCount.equalsIgnoreCase("A")) { // away team yellow card
                        bandyMatchIncidentType = BandyMatchIncidentType.TEN_MINS_PENALTY_START;
                        teamId = TeamId.B;
                        break;
                    }

                } catch (Exception e) {
                    // int teamCount = 3;
                }
                break;

            case 'U':// terminate major sin bin
                if (tenMinsSinBinA == 0 && tenMinsSinBinB == 0) {
                    throw new IllegalArgumentException("no existing 10 MINS sin bins");
                }
                try {
                    String teamCount = (response.substring(1));
                    if (teamCount.equalsIgnoreCase("H")) { // home team yellow card
                        bandyMatchIncidentType = BandyMatchIncidentType.TEN_MINS_PENALTY_END;
                        teamId = TeamId.A;
                        break;
                    }
                    if (teamCount.equalsIgnoreCase("A")) { // away team yellow card
                        bandyMatchIncidentType = BandyMatchIncidentType.TEN_MINS_PENALTY_END;
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
                        bandyMatchIncidentType = BandyMatchIncidentType.MAJOR_PENALTY_END;
                        teamId = TeamId.A;
                        break;
                    }
                    if (teamCount.equalsIgnoreCase("A")) { // away team yellow card
                        bandyMatchIncidentType = BandyMatchIncidentType.MAJOR_PENALTY_END;
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
                    case AT_EXTRA_TIME_HALF_TIME:
                    case AT_EXTRA_TIME_END:
                    case IN_SHOOTOUT:
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
                    case IN_EXTRA_TIME_FIRST_HALF:
                    case IN_EXTRA_TIME_SECOND_HALF:
                    case IN_SHOOTOUT:
                        elapsedTimeMatchIncidentType = ElapsedTimeMatchIncidentType.SET_PERIOD_END;
                        break;
                    default:
                        return null; // invalid input so return null
                }
        }
        MatchIncident incident;
        if (bandyMatchIncidentType != null) {
            incident = new BandyMatchIncident(bandyMatchIncidentType, incidentElapsedTimeSecs, teamId);
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

    private static final String majorSendOffKey = "Five Minutes Send off status";
    private static final String tenMinsSendOffKey = "Ten Minutes Send off status";

    @Override
    public LinkedHashMap<String, String> getAsMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put(goalsKey, String.format("%d-%d", goalsA, goalsB));
        int mins = elapsedTimeSecs / 60;
        int secs = elapsedTimeSecs - mins * 60;
        map.put(elapsedTimeKey, String.format("%d:%02d", mins, secs));
        map.put(matchPeriodKey, matchPeriod.toString());
        map.put(goalSequenceKey, getSequenceIdForGoal(0));
        map.put(majorSendOffKey, String.format("%d-%d", majorSinBinA, majorSinBinB));
        map.put(tenMinsSendOffKey, String.format("%d-%d", tenMinsSinBinA, tenMinsSinBinB));
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
        return (matchPeriod == BandyMatchPeriod.MATCH_COMPLETED);
    }

    /**
     * Returns true if at the end of normal time
     * 
     * @return
     */
    @JsonIgnore
    public boolean isNormalTimeMatchCompleted() {
        return (elapsedTimeSecs >= normalPeriodSecs * 2);
    }

    /**
     * Returns true if at the end of one period and before the next one (if any) starts
     * 
     * @return
     */
    @JsonIgnore
    public boolean isPeriodCompleted() {
        return (matchPeriod == BandyMatchPeriod.AT_HALF_TIME || matchPeriod == BandyMatchPeriod.AT_FULL_TIME
                        || matchPeriod == BandyMatchPeriod.MATCH_COMPLETED);
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
     * Gets the id of the wininng team
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
        BandyMatchState other = (BandyMatchState) obj;
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
        if (teamScoringLastGoal != other.teamScoringLastGoal)
            return false;
        return true;
    }

    @JsonIgnore
    @Override
    public MatchFormat getMatchFormat() {
        return matchFormat;
    }
    // @Override
    // public void setMatchFormat( MatchFormat matchFormat) {
    // if(this.matchFormat==null){
    // this.matchFormat = (BandyMatchFormat) matchFormat;}
    // }

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
            case AT_EXTRA_TIME_HALF_TIME:
            case AT_EXTRA_TIME_END:
            case AT_SHOOTOUT_END:
            case MATCH_COMPLETED:
                break;
            case IN_SHOOTOUT:
            case IN_FIRST_HALF:
            case IN_SECOND_HALF:
            case IN_EXTRA_TIME_FIRST_HALF:
            case IN_EXTRA_TIME_SECOND_HALF:
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
                matchPeriod = BandyMatchPeriod.IN_FIRST_HALF;
                elapsedTimeThisPeriodSecs = elapsedTimeSecs;
                break;

            case AT_HALF_TIME:
            case IN_FIRST_HALF:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs;
                if (elapsedTimeThisPeriodSecs >= normalPeriodSecs) {
                    elapsedTimeSecs = normalPeriodSecs; // reset counter
                                                        // to remove
                                                        // injury time
                    elapsedTimeThisPeriodSecs = 0;
                    matchPeriod = BandyMatchPeriod.IN_SECOND_HALF;
                }
                break;
            case AT_FULL_TIME:
            case IN_SECOND_HALF:

                elapsedTimeThisPeriodSecs = elapsedTimeSecs - normalPeriodSecs;
                if (elapsedTimeThisPeriodSecs >= normalPeriodSecs) {
                    if (extraPeriodSecs == 0 || goalsA != goalsB) {
                        matchPeriod = BandyMatchPeriod.MATCH_COMPLETED;
                    } else {
                        elapsedTimeSecs = 2 * normalPeriodSecs;
                        elapsedTimeThisPeriodSecs = 0;
                        matchPeriod = BandyMatchPeriod.IN_EXTRA_TIME_FIRST_HALF;
                        overtimeNo++;
                    }
                }

                break;
            case AT_EXTRA_TIME_HALF_TIME:
            case IN_EXTRA_TIME_FIRST_HALF:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs - 2 * normalPeriodSecs;
                // golden goals

                if (goalsA != goalsB) {
                    matchPeriod = BandyMatchPeriod.MATCH_COMPLETED;
                    break;
                }
                if (elapsedTimeThisPeriodSecs >= extraPeriodSecs) {
                    elapsedTimeThisPeriodSecs = 0;
                    elapsedTimeSecs = 2 * normalPeriodSecs + (overtimeNo) * extraPeriodSecs;
                    if (goalsA != goalsB) {
                        matchPeriod = BandyMatchPeriod.MATCH_COMPLETED;
                        break;
                    } else {
                        matchPeriod = BandyMatchPeriod.AT_EXTRA_TIME_HALF_TIME;
                        overtimeNo++;
                        break;
                    }

                }
                break;
            case AT_EXTRA_TIME_END:
            case IN_EXTRA_TIME_SECOND_HALF:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs - 2 * normalPeriodSecs - extraPeriodSecs;
                // golden goals
                if (goalsA != goalsB) {
                    matchPeriod = BandyMatchPeriod.MATCH_COMPLETED;
                    break;
                }
                if (elapsedTimeThisPeriodSecs >= extraPeriodSecs) {
                    elapsedTimeThisPeriodSecs = 0;
                    elapsedTimeSecs = 2 * normalPeriodSecs + (overtimeNo) * extraPeriodSecs;
                    if (goalsA != goalsB) {
                        matchPeriod = BandyMatchPeriod.MATCH_COMPLETED;
                        break;
                    } else {
                        if (penaltiesPossible) {
                            matchPeriod = BandyMatchPeriod.IN_SHOOTOUT;
                            break;
                        } else {
                            matchPeriod = BandyMatchPeriod.AT_EXTRA_TIME_END;
                            overtimeNo++;
                            break;
                        }
                    }

                }
                break;
            case IN_SHOOTOUT:
                shootOutTimeCounter++;
                if (isShootoutSettled()) {
                    goalsA += shootOutGoalsA > shootOutGoalsB ? 1 : 0;
                    goalsB += shootOutGoalsA > shootOutGoalsB ? 0 : 1;
                    matchPeriod = BandyMatchPeriod.MATCH_COMPLETED;
                }

                break;

            case MATCH_COMPLETED:
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
            case IN_EXTRA_TIME_FIRST_HALF:
                gamePeriod = GamePeriod.EXTRA_TIME_FIRST_HALF;
                break;
            case IN_EXTRA_TIME_SECOND_HALF:
                gamePeriod = GamePeriod.EXTRA_TIME_SECOND_HALF;
                break;
            case AT_EXTRA_TIME_HALF_TIME:
                gamePeriod = GamePeriod.EXTRA_TIME_HALF_TIME;
                break;
            case AT_EXTRA_TIME_END:
                gamePeriod = GamePeriod.EXTRA_TIME_ENDED;
                break;
            case IN_SHOOTOUT:
                gamePeriod = GamePeriod.PENALTY_SHOOTING;
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

    @JsonIgnore
    public String getSequenceIdForFiveMinsResult() {
        return String.format("F%d", (int) fiveMinsNo + 1);
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
     * returns true if in preMatch
     */
    @Override
    public boolean preMatch() {
        return (matchPeriod == BandyMatchPeriod.PREMATCH);
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
            case AT_EXTRA_TIME_HALF_TIME:
            case AT_EXTRA_TIME_END:

            case IN_SHOOTOUT:
            case MATCH_COMPLETED:
            case PREMATCH:
                secs = 0;
                break;
            case IN_FIRST_HALF:
            case IN_SECOND_HALF:
                secs = normalPeriodSecs - elapsedTimeThisPeriodSecs;
                break;
            case IN_EXTRA_TIME_FIRST_HALF:
            case IN_EXTRA_TIME_SECOND_HALF:
                secs = extraPeriodSecs - elapsedTimeThisPeriodSecs;
                break;
            default:
                throw new IllegalArgumentException("Should not go here, secsLeftInCurrentPeriod() in Bandy");
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

    @Override
    public SimpleMatchState generateSimpleMatchState() {
        return new BandySimpleMatchState(this.preMatch(), this.isMatchCompleted(), matchPeriod, elapsedTimeSecs, goalsA,
                        goalsB, goalsFirstHalfA, goalsFirstHalfB, goalsSecondHalfA, goalsSecondHalfB);
    }

    @Override
    public AlgoMatchState generateMatchStateForMatchResult(MatchResultMap matchResultMap) {
        /*
         * not yet supported for this sport
         */
        return null;
    }
}
