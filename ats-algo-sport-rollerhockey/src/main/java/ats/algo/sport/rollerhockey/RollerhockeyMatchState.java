package ats.algo.sport.rollerhockey;

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
import ats.algo.core.common.TeamId;
import ats.algo.core.common.ElapsedTimeMatchIncident.ElapsedTimeMatchIncidentType;
import ats.algo.core.matchresult.MatchResultMap;
import ats.algo.sport.rollerhockey.RollerhockeyMatchIncident.RollerhockeyMatchIncidentType;

public class RollerhockeyMatchState extends AlgoMatchState {
    /**
     * AlgoMatchState class for Rollerhockey
     * 
     * @author Jin
     *
     */

    private static final long serialVersionUID = 1L;
    // /* no need timers if use feeds terminate sinBins*/
    @JsonIgnore
    private ArrayList<Integer> twoMinsSinBinTimer_A = new ArrayList<Integer>();
    @JsonIgnore
    private ArrayList<Integer> twoMinsSinBinTimer_B = new ArrayList<Integer>();
    @JsonIgnore
    private ArrayList<Integer> fiveMinsSinBinTimer_A = new ArrayList<Integer>();
    @JsonIgnore
    private ArrayList<Integer> fiveMinsSinBinTimer_B = new ArrayList<Integer>();

    @JsonIgnore
    private int elapsedTimeSecsFirstGoal = 0;

    private final int extraPeriodSecs;
    private int elapsedTimeAtLastGoalSecs;
    private RollerhockeyMatchPeriod matchPeriodInWhichLastGoalScored;

    private int twoMinsSinBinA;
    private int twoMinsSinBinB;
    private int fiveMinsSinBinA;
    private int fiveMinsSinBinB;
    private int goalsA;
    private int goalsB;
    private int elapsedTimeSecs;
    private int elapsedTimeAtLastMatchIncidentSecs; // the time from the last matchIncident report
    private int elapsedTimeThisPeriodSecs; // recent MatchIncident;
    private final int normalPeriodSecs;
    private RollerhockeyMatchPeriod matchPeriod; // the state following the most
    private final RollerhockeyMatchFormat matchFormat;
    private TeamId teamScoringLastGoal;
    private int currentPeriodGoalsA;
    private int currentPeriodGoalsB;
    private int secondHalfGoalsA;
    private int secondHalfGoalsB;
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
    public RollerhockeyMatchState() {
        this(new RollerhockeyMatchFormat());
        // super();
        // normalPeriodSecs = 40 * 30;
        // elapsedTimeThisPeriodSecs = 2100 - normalPeriodSecs;
        //
        // twoMinsSinBinTimer_A = new ArrayList<Integer>();
        // twoMinsSinBinTimer_B = new ArrayList<Integer>();
        //
        // twoMinsSinBinA = 0;
        // twoMinsSinBinB = 0;
        // fiveMinsSinBinA = 0;
        // fiveMinsSinBinB = 0;
        //
        // goalsA = 3;
        // goalsB = 2;
        // elapsedTimeSecs = 2100;
        // this.matchFormat = null;
        // teamScoringLastGoal = TeamId.A;
        // currentPeriodGoalsA = 1;
        // currentPeriodGoalsB = 0;
        // matchPeriod = RollerhockeyMatchPeriod.IN_SECOND_HALF;
        // extraPeriodSecs = 5 * 30;
    }

    /**
     * Class constructor
     * 
     * @param matchFormat
     */
    public RollerhockeyMatchState(MatchFormat matchFormat) {
        super();
        this.matchFormat = (RollerhockeyMatchFormat) matchFormat;
        matchPeriod = RollerhockeyMatchPeriod.PREMATCH;
        normalPeriodSecs = this.matchFormat.getNormalTimeMinutes() * 30;
        extraPeriodSecs = this.matchFormat.getExtraTimeMinutes() * 30;
        penaltiesPossible = this.matchFormat.isPenaltiesPossible();
        elapsedTimeAtLastMatchIncidentSecs = -1;
        elapsedTimeAtLastGoalSecs = -1;
        twoMinsSinBinTimer_A = new ArrayList<Integer>();
        twoMinsSinBinTimer_B = new ArrayList<Integer>();

    }



    public int getSecondHalfGoalsA() {
        return secondHalfGoalsA;
    }

    public void setSecondHalfGoalsA(int secondHalfGoalsA) {
        this.secondHalfGoalsA = secondHalfGoalsA;
    }

    public int getSecondHalfGoalsB() {
        return secondHalfGoalsB;
    }

    public void setSecondHalfGoalsB(int secondHalfGoalsB) {
        this.secondHalfGoalsB = secondHalfGoalsB;
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
     * @return shootOutTimeCounter
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
     * Get number of players currently on 2 minutes sin bin for team A
     * 
     * @return twoMinsSinBinA
     */
    public int getTwoMinsSinBinA() {
        return twoMinsSinBinA;
    }

    /**
     * Set number of players currently on 2 minutes sin bin for team A
     * 
     * @param twoMinsSinBinA
     */
    public void setTwoMinsSinBinA(int majorSinBinA) {
        this.twoMinsSinBinA = majorSinBinA;
    }

    /**
     * Get number of players currently on 2 minutes sin bin for team B
     * 
     * @return twoMinsSinBinB
     */
    public int getTwoMinsSinBinB() {
        return twoMinsSinBinB;
    }

    /**
     * Set number of players currently on 2 minutes sin bin for team B
     * 
     * @param twoMinsSinBinB
     */
    public void setTwoMinsSinBinB(int majorSinBinB) {
        this.twoMinsSinBinB = majorSinBinB;
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
     * @return fiveMinsGoalsB
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
     * Get no of players from team A current on 5 minutes sin bin
     * 
     * @return fiveMinsSinBinA
     */
    public int getFiveMinsSinBinA() {
        return fiveMinsSinBinA;
    }

    /**
     * Set no of players from team A current on 5 minutes sin bin
     * 
     * @param fiveMinsSinBinA
     */
    public void setFiveMinsSinBinA(int tenMinsSinBinA) {
        this.fiveMinsSinBinA = tenMinsSinBinA;
    }

    /**
     * Get no of players from team B current on 5 minutes sin bin
     * 
     * @return fiveMinsSinBinB
     */
    public int getFiveMinsSinBinB() {
        return fiveMinsSinBinB;
    }

    /**
     * Set no of players from team B current on 5 minutes sin bin
     * 
     * @param fiveMinsSinBinB
     */
    public void setFiveMinsSinBinB(int tenMinsSinBinB) {
        this.fiveMinsSinBinB = tenMinsSinBinB;
    }

    /**
     * Get 5 minutes sin bin ending time for team A players in a ArrayList
     * 
     * @return fiveMinsSinBinTimer_A
     */
    @JsonIgnore
    public ArrayList<Integer> getFiveMinsSinBinTimer_A() {
        return fiveMinsSinBinTimer_A;
    }

    /**
     * Set 5 minutes sin bin ending time for team A players in a ArrayList
     * 
     * @param fiveMinsSinBinTimer_A
     */
    @JsonIgnore
    public void setFiveMinsSinBinTimer_A(ArrayList<Integer> tenMinsSinBinTimer_A) {
        this.fiveMinsSinBinTimer_A = tenMinsSinBinTimer_A;
    }

    /**
     * Get 5 minutes sin bin ending time for team B players in a ArrayList
     * 
     * @return fiveMinsSinBinTimer_B
     */
    @JsonIgnore
    public ArrayList<Integer> getFiveMinsSinBinTimer_B() {
        return fiveMinsSinBinTimer_B;
    }

    /**
     * Set 5 minutes sin bin ending time for team B players in a ArrayList
     * 
     * @param fiveMinsSinBinTimer_B
     */
    @JsonIgnore
    public void setFiveMinsSinBinTimer_B(ArrayList<Integer> tenMinsSinBinTimer_B) {
        this.fiveMinsSinBinTimer_B = tenMinsSinBinTimer_B;
    }

    /**
     * Get 2 minutes sin bin ending time for team A players in a ArrayList
     * 
     * @return twoMinsSinBinTimer_A
     */
    @JsonIgnore
    public ArrayList<Integer> getTwoMinsSinBinTimer_A() {
        return twoMinsSinBinTimer_A;
    }

    /**
     * Set 2 minutes sin bin ending time for team A players in a ArrayList
     * 
     * @param twoMinsSinBinTimer_A
     */
    @JsonIgnore
    public void setTwoMinsSinBinTimer_A(ArrayList<Integer> majorSinBinTimer_A) {
        this.twoMinsSinBinTimer_A = majorSinBinTimer_A;
    }

    /**
     * Get 2 minutes sin bin ending time for team B players in a ArrayList
     * 
     * @return twoMinsSinBinTimer_B
     */
    @JsonIgnore
    public ArrayList<Integer> getTwoMinsSinBinTimer_B() {
        return twoMinsSinBinTimer_B;
    }

    /**
     * Set 2 minutes sin bin ending time for team B players in a ArrayList
     * 
     * @param twoMinsSinBinTimer_B
     */
    @JsonIgnore
    public void setTwoMinsSinBinTimer_B(ArrayList<Integer> majorSinBinTimer_B) {
        this.twoMinsSinBinTimer_B = majorSinBinTimer_B;
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
            case AT_EXTRA_TIME_END:
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


    /**
     * Set the time for the first goal happens
     * 
     * @param elapsedTimeSecsFirstGoal
     */
    public void setElapsedTimeSecs(int elapsedTimeSecs, int overtimeNoIn) {
        this.elapsedTimeSecs = elapsedTimeSecs;
        this.elapsedTimeThisPeriodSecs = elapsedTimeSecs;
        matchPeriod = RollerhockeyMatchPeriod.IN_FIRST_HALF;
        if (elapsedTimeSecs == normalPeriodSecs) {
            this.elapsedTimeThisPeriodSecs = 0;
            matchPeriod = RollerhockeyMatchPeriod.AT_HALF_TIME;
        }
        if (elapsedTimeSecs > normalPeriodSecs) {
            this.elapsedTimeThisPeriodSecs = elapsedTimeSecs - normalPeriodSecs;
            matchPeriod = RollerhockeyMatchPeriod.IN_SECOND_HALF;
        }
        if (elapsedTimeSecs == 2 * normalPeriodSecs) {
            this.elapsedTimeThisPeriodSecs = 0;
            matchPeriod = RollerhockeyMatchPeriod.AT_FULL_TIME;
        }
        if (elapsedTimeSecs > 2 * normalPeriodSecs) {
            this.elapsedTimeThisPeriodSecs = elapsedTimeSecs - 2 * normalPeriodSecs;
            matchPeriod = RollerhockeyMatchPeriod.IN_EXTRA_TIME_FIRST_HALF;
        }
        if (elapsedTimeSecs == 2 * normalPeriodSecs + extraPeriodSecs) {
            this.elapsedTimeThisPeriodSecs = 0;
            matchPeriod = RollerhockeyMatchPeriod.AT_EXTRA_TIME_HALF_TIME;
        }
        if (elapsedTimeSecs > 2 * normalPeriodSecs + extraPeriodSecs) {
            this.elapsedTimeThisPeriodSecs = elapsedTimeSecs - overtimeNoIn * extraPeriodSecs;
            matchPeriod = RollerhockeyMatchPeriod.IN_EXTRA_TIME_SECOND_HALF;
        }
    }

    /**
     * Get the current matchPeriod
     * 
     * @return matchPeriod
     */
    public RollerhockeyMatchPeriod getMatchPeriod() {
        return matchPeriod;
    }

    /**
     * Set the current match period
     * 
     * @param matchPeriod
     */
    public void setMatchPeriod(RollerhockeyMatchPeriod matchPeriod) {
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

        if (matchIncident.getClass() == RollerhockeyMatchIncident.class) {

            TeamId teamId = ((RollerhockeyMatchIncident) matchIncident).getTeamId();

            switch (((RollerhockeyMatchIncident) matchIncident).getIncidentSubType()) {

                case TWO_MINS_PENALTY_END: // power play end
                    switch (teamId) {
                        case A:
                            if (twoMinsSinBinA == 0)
                                throw new IllegalArgumentException("Send-Off number is wrong");
                            twoMinsSinBinA--;
                            cleanSuspensionTimer(twoMinsSinBinTimer_A, twoMinsSinBinA);

                            break;

                        case B:
                            if (twoMinsSinBinB == 0)
                                throw new IllegalArgumentException("Send-Off number is wrong");
                            twoMinsSinBinB--;
                            cleanSuspensionTimer(twoMinsSinBinTimer_B, twoMinsSinBinB);

                            break;
                        default:
                            throw new IllegalArgumentException("team Id Unknown");

                    }
                    break;

                case FIVE_MINS_PENALTY_END: // power play end
                    switch (teamId) {
                        case A:
                            if (fiveMinsSinBinA == 0)
                                throw new IllegalArgumentException("Send-Off number is wrong");
                            fiveMinsSinBinA--;
                            cleanSuspensionTimer(fiveMinsSinBinTimer_A, fiveMinsSinBinA);

                            break;

                        case B:
                            if (fiveMinsSinBinB == 0)
                                throw new IllegalArgumentException("Send-Off number is wrong");
                            fiveMinsSinBinB--;
                            cleanSuspensionTimer(fiveMinsSinBinTimer_B, fiveMinsSinBinB);

                            break;
                        default:
                            throw new IllegalArgumentException("team Id Unknown");

                    }
                    break;

                case TWO_MINS_PENALTY_START: // power play started
                    switch (teamId) {
                        case A:
                            sinBinTimerTrigger(twoMinsSinBinTimer_A, elapsedTimeSecs, 2 * 60);
                            twoMinsSinBinA++;
                            break;

                        case B:
                            sinBinTimerTrigger(twoMinsSinBinTimer_B, elapsedTimeSecs, 2 * 60);
                            twoMinsSinBinB++;
                            break;
                        case UNKNOWN:
                            throw new IllegalArgumentException("TeamId must be specified for five minutes power play");

                    }
                    break;

                case FIVE_MINS_PENALTY_START: // power play started
                    switch (teamId) {
                        case A:
                            sinBinTimerTrigger(fiveMinsSinBinTimer_A, elapsedTimeSecs, 5 * 60);
                            fiveMinsSinBinA++;
                            break;

                        case B:
                            sinBinTimerTrigger(fiveMinsSinBinTimer_B, elapsedTimeSecs, 5 * 60);
                            fiveMinsSinBinB++;
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
                            if (!matchPeriod.equals(RollerhockeyMatchPeriod.IN_SHOOTOUT)) {

                                if (!matchPeriod.equals(RollerhockeyMatchPeriod.IN_EXTRA_TIME_FIRST_HALF)
                                                && !matchPeriod.equals(
                                                                RollerhockeyMatchPeriod.IN_EXTRA_TIME_SECOND_HALF))
                                    normalTimeGoalsA++;

                                updateFiveMinsStatus();
                                fiveMinsGoalsA++;
                                if (matchPeriod.equals(RollerhockeyMatchPeriod.IN_SECOND_HALF))
                                    this.secondHalfGoalsA++;
                                if (goalsA == 0 && goalsB == 0) {
                                    elapsedTimeSecsFirstGoal = elapsedTimeSecs;
                                }

                                goalsA++;
                                currentPeriodGoalsA++;
                                teamScoringLastGoal = TeamId.A;
                                goalSequence++;

                            } else {
                                shootOutGoalsA++;
                                shootOutTimeCounter++;
                                if (isShootoutSettled()) {
                                    this.goalsA++;// = shootOutGoalsA>shootOutGoalsB? 1:0;
                                    matchPeriod = RollerhockeyMatchPeriod.MATCH_COMPLETED;
                                }

                            }
                            break;
                        case B:
                            if (!matchPeriod.equals(RollerhockeyMatchPeriod.IN_SHOOTOUT)) {

                                if (!matchPeriod.equals(RollerhockeyMatchPeriod.IN_EXTRA_TIME_FIRST_HALF)
                                                && !matchPeriod.equals(
                                                                RollerhockeyMatchPeriod.IN_EXTRA_TIME_SECOND_HALF))
                                    normalTimeGoalsB++;

                                updateFiveMinsStatus();
                                fiveMinsGoalsB++;
                                if (matchPeriod.equals(RollerhockeyMatchPeriod.IN_SECOND_HALF))
                                    this.secondHalfGoalsB++;
                                if (goalsA == 0 && goalsB == 0) {
                                    elapsedTimeSecsFirstGoal = elapsedTimeSecs;
                                }

                                goalsB++;
                                currentPeriodGoalsB++;
                                teamScoringLastGoal = TeamId.B;
                                goalSequence++;

                            } else {
                                shootOutGoalsB++;
                                shootOutTimeCounter++;
                                if (isShootoutSettled()) {
                                    this.goalsB++;
                                    matchPeriod = RollerhockeyMatchPeriod.MATCH_COMPLETED;
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
                    if (matchPeriod.equals(RollerhockeyMatchPeriod.IN_SHOOTOUT)) {
                        shootOutTimeCounter++;
                        if (isShootoutSettled()) {
                            this.goalsA += shootOutGoalsA > shootOutGoalsB ? 1 : 0;
                            this.goalsB += shootOutGoalsA > shootOutGoalsB ? 0 : 1;
                            matchPeriod = RollerhockeyMatchPeriod.MATCH_COMPLETED;
                        }
                    }
                    break;

                case SET_PERIOD_START:
                    switch (matchPeriod) {
                        case PREMATCH:
                            elapsedTimeThisPeriodSecs = 0;
                            matchPeriod = RollerhockeyMatchPeriod.IN_FIRST_HALF;
                            break;
                        case IN_FIRST_HALF:
                            break;
                        case AT_HALF_TIME:
                            elapsedTimeThisPeriodSecs = 0;
                            matchPeriod = RollerhockeyMatchPeriod.IN_SECOND_HALF;
                            break;
                        case IN_SECOND_HALF:
                            break;
                        case AT_FULL_TIME:
                            if (extraPeriodSecs > 0)
                                overtimeNo++;

                            elapsedTimeThisPeriodSecs = 0;
                            matchPeriod = RollerhockeyMatchPeriod.IN_EXTRA_TIME_FIRST_HALF;
                            break;

                        case IN_EXTRA_TIME_FIRST_HALF:
                            break;

                        case AT_EXTRA_TIME_HALF_TIME:
                            overtimeNo++;
                            elapsedTimeThisPeriodSecs = 0;
                            matchPeriod = RollerhockeyMatchPeriod.IN_EXTRA_TIME_SECOND_HALF;
                            break;

                        case AT_EXTRA_TIME_END:
                            overtimeNo++;
                            elapsedTimeThisPeriodSecs = 0;
                            if (penaltiesPossible && goalsA == goalsB) {
                                matchPeriod = RollerhockeyMatchPeriod.IN_SHOOTOUT;
                                break;
                            } else {
                                if (goalsA != goalsB) {
                                    matchPeriod = RollerhockeyMatchPeriod.MATCH_COMPLETED;
                                } else {
                                    matchPeriod = RollerhockeyMatchPeriod.IN_EXTRA_TIME_FIRST_HALF;
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
                            matchPeriod = RollerhockeyMatchPeriod.AT_HALF_TIME;
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
                                matchPeriod = RollerhockeyMatchPeriod.MATCH_COMPLETED;
                            else
                                matchPeriod = RollerhockeyMatchPeriod.AT_FULL_TIME;
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
                                matchPeriod = RollerhockeyMatchPeriod.AT_EXTRA_TIME_HALF_TIME;
                            } else {
                                matchPeriod = RollerhockeyMatchPeriod.MATCH_COMPLETED;
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
                                matchPeriod = RollerhockeyMatchPeriod.AT_EXTRA_TIME_END;
                            } else {
                                matchPeriod = RollerhockeyMatchPeriod.MATCH_COMPLETED;
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
                            matchPeriod = RollerhockeyMatchPeriod.MATCH_COMPLETED;
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
        RollerhockeyMatchState cc = new RollerhockeyMatchState(matchFormat);
        cc.setEqualTo(this);
        return cc;

    }

    @Override
    public void setEqualTo(AlgoMatchState matchState) {
        super.setEqualTo(matchState);
        this.setGoalsA(((RollerhockeyMatchState) matchState).getGoalsA());
        this.setGoalsB(((RollerhockeyMatchState) matchState).getGoalsB());
        this.setElapsedTimeSecs(((RollerhockeyMatchState) matchState).getElapsedTimeSecs(),
                        ((RollerhockeyMatchState) matchState).getOvertimeNo());
        this.setMatchPeriod(((RollerhockeyMatchState) matchState).getMatchPeriod());
        this.setTeamScoringLastGoal(((RollerhockeyMatchState) matchState).getTeamScoringLastGoal());
        /* update cards infor and sendoff info */
        this.twoMinsSinBinTimer_A.clear();
        this.twoMinsSinBinTimer_A = cloneIntegerList(((RollerhockeyMatchState) matchState).getTwoMinsSinBinTimer_A());

        this.twoMinsSinBinTimer_B.clear();
        this.twoMinsSinBinTimer_B = cloneIntegerList(((RollerhockeyMatchState) matchState).getTwoMinsSinBinTimer_B());

        this.fiveMinsSinBinTimer_A.clear();
        this.fiveMinsSinBinTimer_A = cloneIntegerList(((RollerhockeyMatchState) matchState).getFiveMinsSinBinTimer_A());

        this.fiveMinsSinBinTimer_B.clear();
        this.fiveMinsSinBinTimer_B = cloneIntegerList(((RollerhockeyMatchState) matchState).getFiveMinsSinBinTimer_B());

        this.setSecondHalfGoalsA(((RollerhockeyMatchState) matchState).getSecondHalfGoalsA());
        this.setSecondHalfGoalsB(((RollerhockeyMatchState) matchState).getSecondHalfGoalsB());

        this.setFiveMinsSinBinA(((RollerhockeyMatchState) matchState).getFiveMinsSinBinA());
        this.setFiveMinsSinBinB(((RollerhockeyMatchState) matchState).getFiveMinsSinBinB());
        this.setTwoMinsSinBinA(((RollerhockeyMatchState) matchState).getTwoMinsSinBinA());
        this.setTwoMinsSinBinB(((RollerhockeyMatchState) matchState).getTwoMinsSinBinB());
        this.setShootOutGoalsA(((RollerhockeyMatchState) matchState).getShootOutGoalsA());
        this.setShootOutGoalsB(((RollerhockeyMatchState) matchState).getShootOutGoalsB());
        this.setShootOutTimeCounter(((RollerhockeyMatchState) matchState).getShootOutTimeCounter());
        this.setOvertimeNo(((RollerhockeyMatchState) matchState).getOvertimeNo());
        this.setGoalSequence(((RollerhockeyMatchState) matchState).getGoalSequence());

        this.setCurrentPeriodGoalsA(((RollerhockeyMatchState) matchState).getCurrentPeriodGoalsA());
        this.setCurrentPeriodGoalsB(((RollerhockeyMatchState) matchState).getCurrentPeriodGoalsB());
        this.setOvertimeNo(((RollerhockeyMatchState) matchState).getOvertimeNo());
        this.setNormalTimeGoalsA(((RollerhockeyMatchState) matchState).getNormalTimeGoalsA());
        this.setNormalTimeGoalsB(((RollerhockeyMatchState) matchState).getNormalTimeGoalsB());
        this.setElapsedTimeSecsFirstGoal(((RollerhockeyMatchState) matchState).getElapsedTimeSecsFirstGoal());
        this.setFiveMinsGoalsA(((RollerhockeyMatchState) matchState).getFiveMinsGoalsA());
        this.setFiveMinsGoalsB(((RollerhockeyMatchState) matchState).getFiveMinsGoalsB());
        this.setPreviousFiveMinsGoalsA(((RollerhockeyMatchState) matchState).getPreviousFiveMinsGoalsA());
        this.setPreviousFiveMinsGoalsB(((RollerhockeyMatchState) matchState).getPreviousFiveMinsGoalsB());
        this.setCurrentFiveMinsEnder(((RollerhockeyMatchState) matchState).getCurrentFiveMinsEnder());
        this.setPreviousFiveMinsEnder(((RollerhockeyMatchState) matchState).getPreviousFiveMinsEnder());
        this.setFiveMinsNo(((RollerhockeyMatchState) matchState).getFiveMinsNo());

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
        if (matchPeriod == RollerhockeyMatchPeriod.MATCH_COMPLETED)
            matchIncidentPrompt = new MatchIncidentPrompt("Match finished");
        else if (matchPeriod == RollerhockeyMatchPeriod.PREMATCH || matchPeriod == RollerhockeyMatchPeriod.AT_HALF_TIME
                        || matchPeriod == RollerhockeyMatchPeriod.AT_FULL_TIME
                        || matchPeriod == RollerhockeyMatchPeriod.AT_EXTRA_TIME_HALF_TIME
                        || matchPeriod == RollerhockeyMatchPeriod.AT_EXTRA_TIME_END)
            matchIncidentPrompt = new MatchIncidentPrompt("S - start match", "S");

        else
            matchIncidentPrompt = new MatchIncidentPrompt(
                            "Next event (Nnn-Nothing happened for nn periods, H-Home goal, A-Away goal, A-Away goal, "
                                            + "GH/A-Home/Away 2 minutes send off,"
                                            + "YH/A-Home/Away 5 minutes send off,"
                                            + "TH/A,QH/A- end send off home or away, E- end period)",
                            "N");
        return matchIncidentPrompt;
    }

    @Override
    public MatchIncident getMatchIncident(String response) {

        RollerhockeyMatchIncidentType rollerhockeyMatchIncidentType = null;
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
                rollerhockeyMatchIncidentType = RollerhockeyMatchIncidentType.GOAL;
                teamId = TeamId.A;
                break;
            case 'A':
                rollerhockeyMatchIncidentType = RollerhockeyMatchIncidentType.GOAL;
                teamId = TeamId.B;
                break;

            case 'G':// GREEN card
                if (elapsedTimeSecs == 0) {
                    throw new IllegalArgumentException("Card can not happen now");
                }
                try {
                    String teamCount = (response.substring(1));
                    if (teamCount.equalsIgnoreCase("H")) { // home team yellow card
                        rollerhockeyMatchIncidentType = RollerhockeyMatchIncidentType.TWO_MINS_PENALTY_START;
                        teamId = TeamId.A;
                        break;
                    }
                    if (teamCount.equalsIgnoreCase("A")) { // away team yellow card
                        rollerhockeyMatchIncidentType = RollerhockeyMatchIncidentType.TWO_MINS_PENALTY_START;
                        teamId = TeamId.B;
                        break;
                    }

                } catch (Exception e) {
                    // int teamCount = 3;
                }
                break;

            case 'Y':// red card 10 mins
                if (elapsedTimeSecs == 0) {
                    throw new IllegalArgumentException("Card can not happen now");
                }
                try {
                    String teamCount = (response.substring(1));
                    if (teamCount.equalsIgnoreCase("H")) { // home team yellow card
                        rollerhockeyMatchIncidentType = RollerhockeyMatchIncidentType.FIVE_MINS_PENALTY_START;
                        teamId = TeamId.A;
                        break;
                    }
                    if (teamCount.equalsIgnoreCase("A")) { // away team yellow card
                        rollerhockeyMatchIncidentType = RollerhockeyMatchIncidentType.FIVE_MINS_PENALTY_START;
                        teamId = TeamId.B;
                        break;
                    }

                } catch (Exception e) {
                    // int teamCount = 3;
                }
                break;

            case 'Q':// terminate major sin bin
                if (fiveMinsSinBinA == 0 && fiveMinsSinBinB == 0) {
                    throw new IllegalArgumentException("no existing 10 MINS sin bins");
                }
                try {
                    String teamCount = (response.substring(1));
                    if (teamCount.equalsIgnoreCase("H")) { // home team yellow card
                        rollerhockeyMatchIncidentType = RollerhockeyMatchIncidentType.FIVE_MINS_PENALTY_END;
                        teamId = TeamId.A;
                        break;
                    }
                    if (teamCount.equalsIgnoreCase("A")) { // away team yellow card
                        rollerhockeyMatchIncidentType = RollerhockeyMatchIncidentType.FIVE_MINS_PENALTY_END;
                        teamId = TeamId.B;
                        break;
                    }

                } catch (Exception e) {
                    // int teamCount = 3;
                }
                break;

            case 'T':// terminate major sin bin
                if (twoMinsSinBinA == 0 && twoMinsSinBinB == 0) {
                    throw new IllegalArgumentException("no existing minor sin bins");
                }
                try {
                    String teamCount = (response.substring(1));
                    if (teamCount.equalsIgnoreCase("H")) { // home team yellow card
                        rollerhockeyMatchIncidentType = RollerhockeyMatchIncidentType.TWO_MINS_PENALTY_END;
                        teamId = TeamId.A;
                        break;
                    }
                    if (teamCount.equalsIgnoreCase("A")) { // away team yellow card
                        rollerhockeyMatchIncidentType = RollerhockeyMatchIncidentType.TWO_MINS_PENALTY_END;
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
        if (rollerhockeyMatchIncidentType != null) {
            incident = new RollerhockeyMatchIncident(rollerhockeyMatchIncidentType, incidentElapsedTimeSecs, teamId);
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
        map.put(majorSendOffKey, String.format("%d-%d", twoMinsSinBinA, twoMinsSinBinB));
        map.put(tenMinsSendOffKey, String.format("%d-%d", fiveMinsSinBinA, fiveMinsSinBinB));
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
        return (matchPeriod == RollerhockeyMatchPeriod.MATCH_COMPLETED);
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
        return (matchPeriod == RollerhockeyMatchPeriod.AT_HALF_TIME
                        || matchPeriod == RollerhockeyMatchPeriod.AT_FULL_TIME
                        || matchPeriod == RollerhockeyMatchPeriod.MATCH_COMPLETED);
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
        RollerhockeyMatchState other = (RollerhockeyMatchState) obj;
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
                matchPeriod = RollerhockeyMatchPeriod.IN_FIRST_HALF;
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
                    matchPeriod = RollerhockeyMatchPeriod.IN_SECOND_HALF;
                }
                break;
            case AT_FULL_TIME:
            case IN_SECOND_HALF:

                elapsedTimeThisPeriodSecs = elapsedTimeSecs - normalPeriodSecs;
                if (elapsedTimeThisPeriodSecs >= normalPeriodSecs) {
                    if (extraPeriodSecs == 0 || goalsA != goalsB) {
                        matchPeriod = RollerhockeyMatchPeriod.MATCH_COMPLETED;
                    } else {
                        elapsedTimeSecs = 2 * normalPeriodSecs;
                        elapsedTimeThisPeriodSecs = 0;
                        matchPeriod = RollerhockeyMatchPeriod.IN_EXTRA_TIME_FIRST_HALF;
                        overtimeNo++;
                    }
                }

                break;
            case AT_EXTRA_TIME_HALF_TIME:
            case IN_EXTRA_TIME_FIRST_HALF:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs - 2 * normalPeriodSecs;
                // golden goals

                if (goalsA != goalsB) {
                    matchPeriod = RollerhockeyMatchPeriod.MATCH_COMPLETED;
                    break;
                }
                if (elapsedTimeThisPeriodSecs >= extraPeriodSecs) {
                    elapsedTimeThisPeriodSecs = 0;
                    elapsedTimeSecs = 2 * normalPeriodSecs + (overtimeNo) * extraPeriodSecs;
                    if (goalsA != goalsB) {
                        matchPeriod = RollerhockeyMatchPeriod.MATCH_COMPLETED;
                        break;
                    } else {
                        matchPeriod = RollerhockeyMatchPeriod.AT_EXTRA_TIME_HALF_TIME;
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
                    matchPeriod = RollerhockeyMatchPeriod.MATCH_COMPLETED;
                    break;
                }
                if (elapsedTimeThisPeriodSecs >= extraPeriodSecs) {
                    elapsedTimeThisPeriodSecs = 0;
                    elapsedTimeSecs = 2 * normalPeriodSecs + (overtimeNo) * extraPeriodSecs;
                    if (goalsA != goalsB) {
                        matchPeriod = RollerhockeyMatchPeriod.MATCH_COMPLETED;
                        break;
                    } else {
                        if (penaltiesPossible) {
                            matchPeriod = RollerhockeyMatchPeriod.IN_SHOOTOUT;
                            break;
                        } else {
                            matchPeriod = RollerhockeyMatchPeriod.AT_EXTRA_TIME_END;
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
                    matchPeriod = RollerhockeyMatchPeriod.MATCH_COMPLETED;
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
            case IN_SHOOTOUT:
                gamePeriod = GamePeriod.PENALTY_SHOOTING;
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
    public String getSequenceIdForMatch() {
        return "M";
    }

    @JsonIgnore
    public String getSequenceIdForFiveMins() {
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
        return (matchPeriod == RollerhockeyMatchPeriod.PREMATCH);
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
            case IN_SHOOTOUT:
            case MATCH_COMPLETED:
            case PREMATCH:
            case AT_EXTRA_TIME_HALF_TIME:
            case AT_SHOOTOUT_END:
            case AT_EXTRA_TIME_END:
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
        return new RollerhockeySimpleMatchState(this.preMatch(), this.isMatchCompleted(), this.getMatchPeriod(),
                        this.getElapsedTimeSecs(), this.getGoalsA(), this.getGoalsB(), this.getCurrentPeriodGoalsA(),
                        this.getCurrentPeriodGoalsB(), this.getShootOutGoalsA(), this.getShootOutGoalsB());
    }

    @Override
    public AlgoMatchState generateMatchStateForMatchResult(MatchResultMap matchResultMap) {
        /*
         * not yet supported for this sport
         */
        return null;
    }
}
