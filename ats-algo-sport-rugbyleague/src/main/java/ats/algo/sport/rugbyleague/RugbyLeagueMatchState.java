package ats.algo.sport.rugbyleague;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ats.algo.core.GamePeriod;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchIncidentPrompt;
import ats.algo.core.baseclasses.MatchIncidentResult;
import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.common.ElapsedTimeMatchIncident;
import ats.algo.core.common.ElapsedTimeMatchIncident.ElapsedTimeMatchIncidentType;
import ats.algo.core.matchresult.MatchResultMap;
import ats.algo.core.common.TeamId;
import ats.algo.sport.rugbyleague.RugbyLeagueMatchIncident.FieldPositionType;
import ats.algo.sport.rugbyleague.RugbyLeagueMatchIncident.RugbyLeagueMatchIncidentType;

/**
 * AlgoMatchState class for Rugby League
 * 
 * @author
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RugbyLeagueMatchState extends AlgoMatchState {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /* CJ added for penalties condition */
    private static final int defaultInjuryTimeFirstHalfSecs = 120;
    private static final int defaultInjuryTimeSecondHalfSecs = 180;
    private static final int defaultInjuryTimeExtraTimeHalfDeciSeconds = 60;
    // /* no need timers if use feeds terminate sinBins*/
    private int redCardA = 0;
    private int redCardB = 0;

    private ArrayList<Integer> tenMinsSinBinTimer_A = new ArrayList<Integer>();

    private ArrayList<Integer> tenMinsSinBinTimer_B = new ArrayList<Integer>();


    private int elapsedTimeSecsFirstGoal = 0;
    private int injuryTimeSecs;
    private final int extraPeriodSecs;
    private RugbyLeagueMatchStatus rugbyLeagueMatchStatus;
    private BallPosition ballPosition;
    private int tenMinsSinBinA;
    private int tenMinsSinBinB;
    private int elapsedTimeAtLastGoalSecs;
    private RugbyLeagueMatchPeriod matchPeriodInWhichLastGoalScored;

    private int tryA, tryB;
    private int convA, convB;
    private int dropGoalA, dropGoalB;

    private int elapsedTimeSecs;
    private int elapsedTimeAtLastMatchIncidentSecs; // the time from the last matchIncident report
    private int elapsedTimeThisPeriodSecs; // recent MatchIncident;
    private final int normalPeriodSecs;
    private RugbyLeagueMatchPeriod matchPeriod; // the state following the most
    private final RugbyLeagueMatchFormat matchFormat;
    private TeamId teamScoringLastPoint;
    private TeamId teamTryLastPoint;
    private int currentPeriodPointsA;
    private int currentPeriodPointsB;

    private int pointsFirstHalfA;
    private int pointsFirstHalfB;
    private int pointsSecondHalfA;
    private int pointsSecondHalfB;

    private int currentPeriodTrysA;
    private int currentPeriodTrysB;
    private int thisSendoffLength = 10 * 60;

    private int previousFiveMinsEnder = 300;

    private int currentFiveMinsEnder = 300;

    private int previousFiveMinsGoalsA;

    private int previousFiveMinsGoalsB;

    private int fiveMinsPointsA;

    private int fiveMinsPointsB;

    private int fiveMinsNo;

    private int overtimeNo;

    private boolean penaltiesPossible;

    private int shootOutGoalsA;

    private int shootOutGoalsB;

    int goalSequence;

    private int shootOutTimeCounter;

    private int normalTimePointsA;

    private int normalTimePointsB;


    private int previousPeriodGoalsA;

    private int previousPeriodGoalsB;

    /**
     * json class constructor. Not for general use
     */
    public RugbyLeagueMatchState() {
        super();
        normalPeriodSecs = 80 * 30;
        elapsedTimeThisPeriodSecs = 1200 - normalPeriodSecs;

        tenMinsSinBinTimer_A = new ArrayList<Integer>();
        tenMinsSinBinTimer_B = new ArrayList<Integer>();

        tenMinsSinBinA = 0;
        tenMinsSinBinB = 0;
        ballPosition = new BallPosition();
        tryA = 0;
        tryB = 0;
        convA = 0;
        convB = 0;
        dropGoalA = 0;
        dropGoalB = 0;
        elapsedTimeSecs = 0;
        this.matchFormat = null;
        teamScoringLastPoint = TeamId.UNKNOWN;
        teamTryLastPoint = TeamId.UNKNOWN;
        currentPeriodPointsA = 0;
        currentPeriodPointsB = 0;
        matchPeriod = RugbyLeagueMatchPeriod.PREMATCH;
        extraPeriodSecs = 5 * 60;
        rugbyLeagueMatchStatus = new RugbyLeagueMatchStatus();

    }

    /**
     * gets team A points
     * 
     * @return
     */

    public int getPointsA() {
        return tryA * 4 + convA * 2 + dropGoalA * 1;
    }

    /**
     * gets team B points
     * 
     * @return
     */

    public int getPointsB() {
        return tryB * 4 + convB * 2 + dropGoalB * 1;
    }

    /**
     * class constructor
     * 
     * @param matchFormat
     */
    public RugbyLeagueMatchState(MatchFormat matchFormat) {
        super();
        this.matchFormat = (RugbyLeagueMatchFormat) matchFormat;
        ballPosition = new BallPosition();
        matchPeriod = RugbyLeagueMatchPeriod.PREMATCH;
        normalPeriodSecs = this.matchFormat.getNormalTimeMinutes() * 30;
        extraPeriodSecs = this.matchFormat.getExtraTimeMinutes() * 60;
        penaltiesPossible = this.matchFormat.isPenaltiesPossible();
        rugbyLeagueMatchStatus = new RugbyLeagueMatchStatus();
        elapsedTimeAtLastMatchIncidentSecs = -1;
        elapsedTimeAtLastGoalSecs = -1;
        tenMinsSinBinTimer_A = new ArrayList<Integer>();
        tenMinsSinBinTimer_B = new ArrayList<Integer>();
        teamScoringLastPoint = TeamId.UNKNOWN;
        teamTryLastPoint = TeamId.UNKNOWN;
    }

    /**
     * gets team A red cards no
     * 
     * @return redCardA
     */
    public int getRedCardA() {
        return redCardA;
    }

    /**
     * sets team A red cards no
     * 
     * @return redCardA
     */
    public void setRedCardA(int redCardA) {
        this.redCardA = redCardA;
    }

    /**
     * gets team B red cards no
     * 
     * @return redCardB
     */
    public int getRedCardB() {
        return redCardB;
    }

    /**
     * sets team B red cards no
     * 
     * @return redCardB
     */
    public void setRedCardB(int redCardB) {
        this.redCardB = redCardB;
    }

    /**
     * gets match status
     * 
     * @return rugbyLeagueMatchStatus
     */

    public RugbyLeagueMatchStatus getRugbyLeagueMatchStatus() {
        return rugbyLeagueMatchStatus;
    }

    /**
     * gets ball position info
     * 
     * @return ballPosition
     */
    //
    public BallPosition getBallPosition() {
        return ballPosition;
    }

    /**
     * sets ball position info
     * 
     * @return ballPosition
     */
    public void setBallPosition(BallPosition ballPosition) {
        this.ballPosition = ballPosition;
    }

    public int getElapsedTimeAtLastGoalSecs() {
        return elapsedTimeAtLastGoalSecs;
    }

    public void setElapsedTimeAtLastGoalSecs(int elapsedTimeAtLastGoalSecs) {
        this.elapsedTimeAtLastGoalSecs = elapsedTimeAtLastGoalSecs;
    }

    public RugbyLeagueMatchPeriod getMatchPeriodInWhichLastGoalScored() {
        return matchPeriodInWhichLastGoalScored;
    }

    public void setMatchPeriodInWhichLastGoalScored(RugbyLeagueMatchPeriod matchPeriodInWhichLastGoalScored) {
        this.matchPeriodInWhichLastGoalScored = matchPeriodInWhichLastGoalScored;
    }

    public int getElapsedTimeAtLastMatchIncidentSecs() {
        return elapsedTimeAtLastMatchIncidentSecs;
    }

    public void setElapsedTimeAtLastMatchIncidentSecs(int elapsedTimeAtLastMatchIncidentSecs) {
        this.elapsedTimeAtLastMatchIncidentSecs = elapsedTimeAtLastMatchIncidentSecs;
    }

    public TeamId getTeamScoringLastPoint() {
        return teamScoringLastPoint;
    }

    public void setTeamScoringLastPoint(TeamId teamScoringLastPoint) {
        this.teamScoringLastPoint = teamScoringLastPoint;
    }

    public int getFiveMinsPointsA() {
        return fiveMinsPointsA;
    }

    public void setFiveMinsPointsA(int fiveMinsPointsA) {
        this.fiveMinsPointsA = fiveMinsPointsA;
    }

    public int getFiveMinsPointsB() {
        return fiveMinsPointsB;
    }

    public void setFiveMinsPointsB(int fiveMinsPointsB) {
        this.fiveMinsPointsB = fiveMinsPointsB;
    }

    public static int getDefaultinjurytimefirsthalfsecs() {
        return defaultInjuryTimeFirstHalfSecs;
    }

    public static int getDefaultinjurytimesecondhalfsecs() {
        return defaultInjuryTimeSecondHalfSecs;
    }

    public static int getDefaultinjurytimeextratimehalfdeciseconds() {
        return defaultInjuryTimeExtraTimeHalfDeciSeconds;
    }

    public void setRugbyLeagueMatchStatus(RugbyLeagueMatchStatus rugbyLeagueMatchStatus) {
        this.rugbyLeagueMatchStatus = rugbyLeagueMatchStatus;
    }

    public void setElapsedTimeSecs(int elapsedTimeSecs) {
        this.elapsedTimeSecs = elapsedTimeSecs;
    }

    public void setCurrentPeriodPointsA(int currentPeriodPointsA) {
        this.currentPeriodPointsA = currentPeriodPointsA;
    }

    public void setCurrentPeriodPointsB(int currentPeriodPointsB) {
        this.currentPeriodPointsB = currentPeriodPointsB;
    }

    /**
     * gets injury time in seconds
     * 
     * @return injuryTimeSecs
     */

    public int getInjuryTimeSecs() {
        return injuryTimeSecs;
    }

    /**
     * sets injury time in seconds
     * 
     * @return injuryTimeSecs
     */

    public void setInjuryTimeSecs(int injuryTimeSecs) {
        this.injuryTimeSecs = injuryTimeSecs;
    }

    /**
     * gets shoot out goals team A
     * 
     * @return shootOutGoalsA
     */

    public int getShootOutGoalsA() {
        return shootOutGoalsA;
    }

    /**
     * sets shoot out goals team A
     * 
     * @return shootOutGoalsA
     */

    public void setShootOutGoalsA(int shootOutGoalsA) {
        this.shootOutGoalsA = shootOutGoalsA;
    }

    /**
     * gets shoot out goals team B
     * 
     * @return shootOutGoalsB
     */

    public int getShootOutGoalsB() {
        return shootOutGoalsB;
    }

    /**
     * sets shoot out goals team B
     * 
     * @return shootOutGoalsB
     */

    public void setShootOutGoalsB(int shootOutGoalsB) {
        this.shootOutGoalsB = shootOutGoalsB;
    }

    /**
     * Get no of shoot out attempts
     * 
     * @return shootOutTimeCounter
     */

    public int getShootOutTimeCounter() {
        return shootOutTimeCounter;
    }

    /**
     * Set no of shoot out attempts
     * 
     * @param shootOutTimeCounter
     */

    public void setShootOutTimeCounter(int shootOutTimeCounter) {
        this.shootOutTimeCounter = shootOutTimeCounter;
    }

    /**
     * Check if penalty is possible for this match format
     * 
     * @return
     */

    public boolean isPenaltiesPossible() {
        return penaltiesPossible;
    }

    /**
     * Set penalty if is possible for this match format
     * 
     * @param penaltiesPossible
     */

    public void setPenaltiesPossible(boolean penaltiesPossible) {
        this.penaltiesPossible = penaltiesPossible;
    }


    public int getPointsFirstHalfA() {
        return pointsFirstHalfA;
    }


    public void setPointsFirstHalfA(int pointsFirstHalfA) {
        this.pointsFirstHalfA = pointsFirstHalfA;
    }


    public int getPointsFirstHalfB() {
        return pointsFirstHalfB;
    }


    public void setPointsFirstHalfB(int pointsFirstHalfB) {
        this.pointsFirstHalfB = pointsFirstHalfB;
    }


    public int getPointsSecondHalfA() {
        return pointsSecondHalfA;
    }


    public void setPointsSecondHalfA(int pointsSecondHalfA) {
        this.pointsSecondHalfA = pointsSecondHalfA;
    }


    public int getPointsSecondHalfB() {
        return pointsSecondHalfB;
    }


    public void setPointsSecondHalfB(int pointsSecondHalfB) {
        this.pointsSecondHalfB = pointsSecondHalfB;
    }

    /**
     * Get number of players currently on 10 minutes sin bin for team A
     * 
     * @return tenMinsSinBinA
     */
    public int getTenMinsSinBinA() {
        return tenMinsSinBinA;
    }

    /**
     * Set number of players currently on 10 minutes sin bin for team A
     * 
     * @param tenMinsSinBinA
     */
    public void setTenMinsSinBinA(int majorSinBinA) {
        this.tenMinsSinBinA = majorSinBinA;
    }

    /**
     * Get number of players currently on 10 minutes sin bin for team B
     * 
     * @return tenMinsSinBinB
     */
    public int getTenMinsSinBinB() {
        return tenMinsSinBinB;
    }

    /**
     * Set number of players currently on 10 minutes sin bin for team B
     * 
     * @param tenMinsSinBinB
     */
    public void setTenMinsSinBinB(int majorSinBinB) {
        this.tenMinsSinBinB = majorSinBinB;
    }

    /**
     * Get sequence number of the current five minutes since the begging of match
     * 
     * @return
     */

    public int getFiveMinsNo() {
        return fiveMinsNo;
    }

    /**
     * Get the sequence number of the goal
     * 
     * @return
     */

    public int getGoalSequence() {
        return goalSequence;
    }

    /**
     * Set the sequence number of the goal since the begging of match
     * 
     * @param goalSequence
     */

    public void setGoalSequence(int goalSequence) {
        this.goalSequence = goalSequence;
    }

    /**
     * Set current five minutes no
     * 
     * @param fiveMinsNo
     */

    public void setFiveMinsNo(int fiveMinsNo) {
        this.fiveMinsNo = fiveMinsNo;
    }

    /**
     * Get the number of goal scored by team A in the previous five minutes of the game
     * 
     * @return
     */

    public int getPreviousFiveMinsGoalsA() {
        return previousFiveMinsGoalsA;
    }

    /**
     * Set the number of goal scored by team A in the previous five minutes of the game
     * 
     * @param previousFiveMinsGoalsA
     */

    public void setPreviousFiveMinsGoalsA(int previousFiveMinsGoalsA) {
        this.previousFiveMinsGoalsA = previousFiveMinsGoalsA;
    }

    /**
     * Get the number of goal scored by team B in the previous five minutes of the game
     * 
     * @return
     */

    public int getPreviousFiveMinsGoalsB() {
        return previousFiveMinsGoalsB;
    }

    /**
     * Set the number of goal scored by team B in the previous five minutes of the game
     * 
     * @param previousFiveMinsGoalsB
     */

    public void setPreviousFiveMinsGoalsB(int previousFiveMinsGoalsB) {
        this.previousFiveMinsGoalsB = previousFiveMinsGoalsB;
    }

    /**
     * Get the ending time in the match of last five minutes slot
     * 
     * @return previousFiveMinsEnder
     */

    public int getPreviousFiveMinsEnder() {
        return previousFiveMinsEnder;
    }

    /**
     * Set the ending time in the match of last five minutes slot
     * 
     * @param previousFiveMinsEnder
     */

    public void setPreviousFiveMinsEnder(int previousFiveMinsEnder) {
        this.previousFiveMinsEnder = previousFiveMinsEnder;
    }

    /**
     * Get the ending time in the match of current five minutes slot
     * 
     * @return currentFiveMinsEnder
     */

    public int getCurrentFiveMinsEnder() {
        return currentFiveMinsEnder;
    }

    /**
     * Set the ending time in the match of current five minutes slot
     * 
     * @param currentFiveMinsEnder
     */

    public void setCurrentFiveMinsEnder(int thisFiveMinsEnder) {
        this.currentFiveMinsEnder = thisFiveMinsEnder;
    }

    /**
     * Get the number of points scored by team A in the current five minutes of the game
     * 
     * @return fiveMinsPointsA
     */

    public int getFiveMinsGoalsA() {
        return fiveMinsPointsA;
    }

    /**
     * Set the number of points scored by team A in the current five minutes of the game
     * 
     * @param fiveMinsPointsA
     */

    public void setFiveMinsGoalsA(int fiveMinsGoalsA) {
        this.fiveMinsPointsA = fiveMinsGoalsA;
    }

    /**
     * Get the number of points scored by team B in the current five minutes of the game
     * 
     * @return fiveMinsPointsB
     */

    public int getFiveMinsGoalsB() {
        return fiveMinsPointsB;
    }

    /**
     * Set the number of points scored by team B in the current five minutes of the game
     * 
     * @param fiveMinsPointsB
     */

    public void setFiveMinsGoalsB(int fiveMinsGoalsB) {
        this.fiveMinsPointsB = fiveMinsGoalsB;
    }

    /**
     * Get team A normal time points
     * 
     * @return normalTimePointsA
     */

    public int getNormalTimePointsA() {
        return normalTimePointsA;
    }

    /**
     * Set team A normal time points
     * 
     * @param normalTimePointsA
     */

    public void setNormalTimePointsA(int normalTimeGoalsA) {
        this.normalTimePointsA = normalTimeGoalsA;
    }

    /**
     * Get team B normal time points
     * 
     * @return normalTimePointsB
     */

    public int getNormalTimePointsB() {
        return normalTimePointsB;
    }

    /**
     * Set team B normal time points
     * 
     * @param normalTimePointsB
     */

    public void setNormalTimePointsB(int normalTimeGoalsB) {
        this.normalTimePointsB = normalTimeGoalsB;
    }

    /**
     * Get the length of duration of this sin bin
     * 
     * @return thisSendoffLength
     */

    public int getThisSendoffLength() {
        return thisSendoffLength;
    }

    /**
     * Set the length of duration of this sin bin
     * 
     * @param thisSendoffLength
     */

    public void setThisSendoffLength(int thisSendoffLength) {
        this.thisSendoffLength = thisSendoffLength;
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
     * Set no of points scored by team A in the previous match period
     * 
     * @param previousPeriodGoalsA
     */
    public void setPreviousPeriodGoalsA(int previousPeriodGoalsA) {
        this.previousPeriodGoalsA = previousPeriodGoalsA;
    }

    /**
     * Set no of points scored by team B in the previous match period
     * 
     * @param previousPeriodGoalsB
     */
    public void setPreviousPeriodGoalsB(int previousPeriodGoalsB) {
        this.previousPeriodGoalsB = previousPeriodGoalsB;
    }

    public void setPreviousPeriodTrysA(int previousPeriodTrysA) {}

    public void setPreviousPeriodTrysB(int previousPeriodTrysB) {}

    /**
     * Set points scored by team A in the current match period
     * 
     * @param currentPeriodPointsA
     */
    public void setCurrentPeriodGoalsA(int currentPeriodGoalsA) {
        this.currentPeriodPointsA = currentPeriodGoalsA;
    }

    /**
     * Set points scored by team B in the current match period
     * 
     * @param currentPeriodPointsB
     */
    public void setCurrentPeriodGoalsB(int currentPeriodGoalsB) {
        this.currentPeriodPointsB = currentPeriodGoalsB;
    }

    /**
     * Set trys scored by team A in the current match period
     * 
     * @param currentPeriodTrysA
     */
    public void setCurrentPeriodTrysA(int currentPeriodTrysA) {
        this.currentPeriodTrysA = currentPeriodTrysA;
    }

    /**
     * Set trys scored by team B in the current match period
     * 
     * @param currentPeriodTrysB
     */
    public void setCurrentPeriodTrysB(int currentPeriodTrysB) {
        this.currentPeriodTrysB = currentPeriodTrysB;
    }

    /**
     * Get 10 minutes sin bin ending time for team A players in a ArrayList
     * 
     * @return tenMinsSinBinTimer_A
     */

    public ArrayList<Integer> getTenMinsSinBinTimer_A() {
        return tenMinsSinBinTimer_A;
    }

    /**
     * Set 10 minutes sin bin ending time for team A players in a ArrayList
     * 
     * @param tenMinsSinBinTimer_A
     */

    public void setTenMinsSinBinTimer_A(ArrayList<Integer> majorSinBinTimer_A) {
        this.tenMinsSinBinTimer_A = majorSinBinTimer_A;
    }

    /**
     * Get 10 minutes sin bin ending time for team B players in a ArrayList
     * 
     * @return tenMinsSinBinTimer_B
     */

    public ArrayList<Integer> getTenMinsSinBinTimer_B() {
        return tenMinsSinBinTimer_B;
    }

    /**
     * Set 10 minutes sin bin ending time for team B players in a ArrayList
     * 
     * @param tenMinsSinBinTimer_B
     */

    public void setTenMinsSinBinTimer_B(ArrayList<Integer> majorSinBinTimer_B) {
        this.tenMinsSinBinTimer_B = majorSinBinTimer_B;
    }

    /**
     * Get trys scored by team A
     * 
     * @return tryA
     */
    public int getTryA() {
        return tryA;
    }

    /**
     * Set trys scored by team A
     * 
     * @param tryA
     */
    public void setTryA(int tryA) {
        this.tryA = tryA;
    }

    /**
     * Get trys scored by team B
     * 
     * @return tryB
     */
    public int getTryB() {
        return tryB;
    }

    /**
     * Set trys scored by team B
     * 
     * @param tryB
     */
    public void setTryB(int tryB) {
        this.tryB = tryB;
    }

    /**
     * Get conversion scored by team A
     * 
     * @return convA
     */
    public int getConvA() {
        return convA;
    }

    /**
     * Set conversion scored by team A
     * 
     * @param convA
     */
    public void setConvA(int convA) {
        this.convA = convA;
    }

    /**
     * Get conversion scored by team B
     * 
     * @return convB
     */
    public int getConvB() {
        return convB;
    }

    /**
     * Set conversion scored by team B
     * 
     * @param convB
     */
    public void setConvB(int convB) {
        this.convB = convB;
    }

    /**
     * Get drop goals scored by team A
     * 
     * @return dropGoalA
     */
    public int getDropGoalA() {
        return dropGoalA;
    }

    /**
     * Set drop goals scored by team A
     * 
     * @param dropGoalA
     */
    public void setDropGoalA(int dropGoalA) {
        this.dropGoalA = dropGoalA;
    }

    /**
     * Get drop goals scored by team B
     * 
     * @return dropGoalB
     */
    public int getDropGoalB() {
        return dropGoalB;
    }

    /**
     * Set drop goals scored by team B
     * 
     * @param dropGoalB
     */
    public void setDropGoalB(int dropGoalB) {
        this.dropGoalB = dropGoalB;
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
        this.teamScoringLastPoint = teamId;
    }

    /**
     * Get team scoring the most recent goal
     * 
     * @return UNKNOWN if no goal scored, else A or B
     */
    public TeamId getTeamScoringLastGoal() {
        return teamScoringLastPoint;
    }

    /**
     * Get the elapsed time in the current period
     * 
     * @return elapsedTimeThisPeriodSecs
     */
    public int getElapsedTimeThisPeriodSecs() {
        return elapsedTimeThisPeriodSecs;
    }



    public TeamId getTeamTryLastPoint() {
        return teamTryLastPoint;
    }

    public void setTeamTryLastPoint(TeamId teamTryLastPoint) {
        this.teamTryLastPoint = teamTryLastPoint;
    }

    /**
     * Get the goal sequence no
     * 
     * @return
     */

    public int getGoalNo() {
        return goalSequence + 1;
    }

    /**
     * Get the current period no
     * 
     * @return
     */

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

    public int getElapsedTimeSecsFirstGoal() {
        return elapsedTimeSecsFirstGoal;
    }

    /**
     * Set the time for the first goal happens
     * 
     * @param elapsedTimeSecsFirstGoal
     */

    public void setElapsedTimeSecsFirstGoal(int elapsedTimeSecsFirstGoal) {
        this.elapsedTimeSecsFirstGoal = elapsedTimeSecsFirstGoal;
    }

    /**
     * Set the elapsed time in match and in current period
     * 
     * @param elapsedTimeSecs
     * @param overtimeNoIn which overtime period we are in. 0 if not in overtime
     */
    public void setElapsedTimeSecs(int elapsedTimeSecs, int overtimeNoIn) {
        this.elapsedTimeSecs = elapsedTimeSecs;
        this.elapsedTimeThisPeriodSecs = elapsedTimeSecs;
        matchPeriod = RugbyLeagueMatchPeriod.IN_FIRST_HALF;
        if (elapsedTimeSecs == normalPeriodSecs) {
            this.elapsedTimeThisPeriodSecs = 0;
            matchPeriod = RugbyLeagueMatchPeriod.AT_HALF_TIME;
        }
        if (elapsedTimeSecs > normalPeriodSecs) {
            this.elapsedTimeThisPeriodSecs = elapsedTimeSecs - normalPeriodSecs;
            matchPeriod = RugbyLeagueMatchPeriod.IN_SECOND_HALF;
        }
        if (elapsedTimeSecs == 2 * normalPeriodSecs) {
            this.elapsedTimeThisPeriodSecs = 0;
            matchPeriod = RugbyLeagueMatchPeriod.AT_FULL_TIME;
        }
        if (elapsedTimeSecs > 2 * normalPeriodSecs) {
            this.elapsedTimeThisPeriodSecs = elapsedTimeSecs - 2 * normalPeriodSecs;
            matchPeriod = RugbyLeagueMatchPeriod.IN_EXTRA_TIME_FIRST_HALF;
        }
        if (elapsedTimeSecs == 2 * normalPeriodSecs + extraPeriodSecs) {
            this.elapsedTimeThisPeriodSecs = 0;
            matchPeriod = RugbyLeagueMatchPeriod.AT_EXTRA_TIME_HALF_TIME;
        }
        if (elapsedTimeSecs > 2 * normalPeriodSecs + extraPeriodSecs) {
            this.elapsedTimeThisPeriodSecs = elapsedTimeSecs - overtimeNoIn * extraPeriodSecs;
            matchPeriod = RugbyLeagueMatchPeriod.IN_EXTRA_TIME_SECOND_HALF;
        }
    }

    /**
     * Get the current matchPeriod
     * 
     * @return matchPeriod
     */
    public RugbyLeagueMatchPeriod getMatchPeriod() {
        return matchPeriod;
    }

    /**
     * Set the current match period
     * 
     * @param matchPeriod
     */
    public void setMatchPeriod(RugbyLeagueMatchPeriod matchPeriod) {
        this.matchPeriod = matchPeriod;
    }

    /**
     * Get # goals scored by team A in current period
     * 
     * @return currentPeriodGoalsA
     */
    public int getCurrentPeriodPointsA() {

        return currentPeriodPointsA;
    }

    /**
     * Get # goals scored by team B in current period
     * 
     * @return currentPeriodGoalsB
     */
    public int getCurrentPeriodPointsB() {

        return currentPeriodPointsB;
    }

    /**
     * Get # trys scored by team A in current period
     * 
     * @return currentPeriodTrysA
     */
    public int getCurrentPeriodTrysA() {
        return currentPeriodTrysA;
    }

    /**
     * Get # trys scored by team B in current period
     * 
     * @return currentPeriodTrysB
     */
    public int getCurrentPeriodTrysB() {
        return currentPeriodTrysB;
    }

    @Override
    public MatchIncidentResult updateStateForIncident(MatchIncident matchIncident,
                    boolean autosyncMatchStateToFeedOnMismatch) {
        setElapsedTime((matchIncident.getElapsedTimeSecs()));
        elapsedTimeAtLastMatchIncidentSecs = elapsedTimeSecs; // ?? what's this
        setClockTimeOfLastElapsedTimeFromIncident(); // ??

        if (!((matchIncident instanceof RugbyLeagueMatchIncident)
                        || (matchIncident instanceof ElapsedTimeMatchIncident)))
            return super.updateStateForIncident(matchIncident, autosyncMatchStateToFeedOnMismatch);

        if (matchIncident.getClass() == RugbyLeagueMatchIncident.class) {

            TeamId teamId = ((RugbyLeagueMatchIncident) matchIncident).getTeamId();

            switch (((RugbyLeagueMatchIncident) matchIncident).getIncidentSubType()) {

                case BALL_POSITION_SETTING:
                    switch (teamId) {
                        case A:
                            ballPosition.setBallHoldingNow(TeamId.A);
                            break;
                        case B:
                            ballPosition.setBallHoldingNow(TeamId.B);
                            break;
                        default:
                            throw new IllegalArgumentException(" Illegal input ");
                    }
                    break;

                case RESET_FIELD_BALL_INFO:
                    ballPosition.resetBallPosition();
                    break;

                case PENALTY_START:
                    rugbyLeagueMatchStatus.setRugbyLeagueMatchStatus(RugbyLeagueMatchStatusType.PENALTY, teamId);
                    break;

                case CONVERSION_START:
                    rugbyLeagueMatchStatus.setRugbyLeagueMatchStatus(RugbyLeagueMatchStatusType.CONVERSION, teamId);
                    break;

                // case PENALTY_END:
                // rugbyLeagueMatchStatus.resetRugbyLeagueMatchStatus();
                // break;
                //
                // case CONVERSION_END:
                // rugbyLeagueMatchStatus.resetRugbyLeagueMatchStatus();
                // break;

                case FIELD_POSITION_SETTING:
                    FieldPositionType fieldPosition = ((RugbyLeagueMatchIncident) matchIncident).getFieldPosition();
                    this.ballPosition.fieldPosition = fieldPosition;
                    break;
                case TEN_MINS_PENALTY_END: // power play end
                    switch (teamId) {
                        case A:
                            if (tenMinsSinBinA == 0)
                                throw new IllegalArgumentException("Send-Off number is wrong");
                            tenMinsSinBinA--;
                            cleanSuspensionTimer(tenMinsSinBinTimer_A, tenMinsSinBinA);

                            break;

                        case B:
                            if (tenMinsSinBinB == 0)
                                throw new IllegalArgumentException("Send-Off number is wrong");
                            tenMinsSinBinB--;
                            cleanSuspensionTimer(tenMinsSinBinTimer_B, tenMinsSinBinB);

                            break;
                        default:
                            throw new IllegalArgumentException("team Id Unknown");

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
                            throw new IllegalArgumentException("TeamId must be specified for five minutes power play");

                    }
                    break;

                case RED_CARD:
                    switch (teamId) {
                        case A:
                            redCardA++;
                            break;

                        case B:
                            redCardB++;
                            break;
                        case UNKNOWN:
                            throw new IllegalArgumentException("TeamId must be specified for five minutes power play");

                    }
                    break;

                case SET_INJURY_TIME:
                    injuryTimeSecs = ((RugbyLeagueMatchIncident) matchIncident).getInjuryTimeSecs();
                    break;

                case TRY:
                    elapsedTimeAtLastGoalSecs = elapsedTimeSecs;
                    matchPeriodInWhichLastGoalScored = matchPeriod;

                    rugbyLeagueMatchStatus.setRugbyLeagueMatchStatus(RugbyLeagueMatchStatusType.CONVERSION, teamId);
                    switch (teamId) {
                        case A:
                            if (!matchPeriod.equals(RugbyLeagueMatchPeriod.IN_EXTRA_TIME_FIRST_HALF)
                                            && !matchPeriod.equals(RugbyLeagueMatchPeriod.IN_EXTRA_TIME_SECOND_HALF))
                                normalTimePointsA += 4;
                            updateFiveMinsStatus();
                            fiveMinsPointsA += 4;
                            if (tryA + convA + dropGoalA == 0 && tryB + convB + dropGoalB == 0) {
                                elapsedTimeSecsFirstGoal = elapsedTimeSecs;
                            }
                            tryA++;

                            if (matchPeriod.equals(RugbyLeagueMatchPeriod.IN_FIRST_HALF))
                                pointsFirstHalfA += 4;
                            if (matchPeriod.equals(RugbyLeagueMatchPeriod.IN_SECOND_HALF))
                                pointsSecondHalfA += 4;

                            currentPeriodPointsA += 4;
                            currentPeriodTrysA++;
                            teamScoringLastPoint = TeamId.A;
                            teamTryLastPoint = TeamId.A;
                            goalSequence++;
                            break;
                        case B:
                            if (!matchPeriod.equals(RugbyLeagueMatchPeriod.IN_EXTRA_TIME_FIRST_HALF)
                                            && !matchPeriod.equals(RugbyLeagueMatchPeriod.IN_EXTRA_TIME_SECOND_HALF))
                                normalTimePointsB += 4;
                            updateFiveMinsStatus();
                            fiveMinsPointsB += 4;
                            if (tryA + convA + dropGoalA == 0 && tryB + convB + dropGoalB == 0) {
                                elapsedTimeSecsFirstGoal = elapsedTimeSecs;
                            }

                            if (matchPeriod.equals(RugbyLeagueMatchPeriod.IN_FIRST_HALF))
                                pointsFirstHalfB += 4;
                            if (matchPeriod.equals(RugbyLeagueMatchPeriod.IN_SECOND_HALF))
                                pointsSecondHalfB += 4;

                            tryB++;
                            currentPeriodPointsB += 4;
                            currentPeriodTrysB++;
                            teamScoringLastPoint = TeamId.B;
                            teamTryLastPoint = TeamId.B;
                            goalSequence++;
                            break;
                        case UNKNOWN:
                            throw new IllegalArgumentException("TeamId must be specified for a try!");
                    }
                    break;
                case CONVERSION_GOAL:
                    elapsedTimeAtLastGoalSecs = elapsedTimeSecs;
                    matchPeriodInWhichLastGoalScored = matchPeriod;

                    checkIfPenatlyOrConversionGoalApproporate(RugbyLeagueMatchIncidentType.CONVERSION_GOAL, teamId);
                    switch (teamId) {
                        case A:
                            if (!matchPeriod.equals(RugbyLeagueMatchPeriod.IN_EXTRA_TIME_FIRST_HALF)
                                            && !matchPeriod.equals(RugbyLeagueMatchPeriod.IN_EXTRA_TIME_SECOND_HALF))
                                normalTimePointsA += 2;

                            updateFiveMinsStatus();
                            fiveMinsPointsA += 2;

                            if (tryA + convA + dropGoalA == 0 && tryB + convB + dropGoalB == 0) {
                                elapsedTimeSecsFirstGoal = elapsedTimeSecs;
                            }

                            if (matchPeriod.equals(RugbyLeagueMatchPeriod.IN_FIRST_HALF))
                                pointsFirstHalfA += 2;
                            if (matchPeriod.equals(RugbyLeagueMatchPeriod.IN_SECOND_HALF))
                                pointsSecondHalfA += 2;

                            convA++;
                            currentPeriodPointsA += 2;
                            teamScoringLastPoint = TeamId.A;
                            teamTryLastPoint = TeamId.A;
                            goalSequence++;
                            break;
                        case B:
                            if (!matchPeriod.equals(RugbyLeagueMatchPeriod.IN_EXTRA_TIME_FIRST_HALF)
                                            && !matchPeriod.equals(RugbyLeagueMatchPeriod.IN_EXTRA_TIME_SECOND_HALF))
                                normalTimePointsB += 2;
                            updateFiveMinsStatus();
                            fiveMinsPointsB += 2;
                            if (tryA + convA + dropGoalA == 0 && tryB + convB + dropGoalB == 0) {
                                elapsedTimeSecsFirstGoal = elapsedTimeSecs;
                            }

                            if (matchPeriod.equals(RugbyLeagueMatchPeriod.IN_FIRST_HALF))
                                pointsFirstHalfB += 2;
                            if (matchPeriod.equals(RugbyLeagueMatchPeriod.IN_SECOND_HALF))
                                pointsSecondHalfB += 2;

                            convB++;
                            currentPeriodPointsB += 2;
                            teamScoringLastPoint = TeamId.B;
                            teamTryLastPoint = TeamId.B;
                            goalSequence++;
                            break;
                        case UNKNOWN:
                            throw new IllegalArgumentException("TeamId must be specified for goal scored");
                    }

                    break;

                case CONVERSION_MISS:
                    checkIfPenatlyOrConversionGoalApproporate(RugbyLeagueMatchIncidentType.CONVERSION_MISS, teamId);
                    break;
                case PENALTY_MISS:
                    checkIfPenatlyOrConversionGoalApproporate(RugbyLeagueMatchIncidentType.PENALTY_MISS, teamId);
                    break;
                case PENALTY_GOAL:
                    elapsedTimeAtLastGoalSecs = elapsedTimeSecs;
                    matchPeriodInWhichLastGoalScored = matchPeriod;

                    checkIfPenatlyOrConversionGoalApproporate(RugbyLeagueMatchIncidentType.PENALTY_GOAL, teamId);
                    switch (teamId) {
                        case A:
                            if (!matchPeriod.equals(RugbyLeagueMatchPeriod.IN_EXTRA_TIME_FIRST_HALF)
                                            && !matchPeriod.equals(RugbyLeagueMatchPeriod.IN_EXTRA_TIME_SECOND_HALF))
                                normalTimePointsA += 2;
                            updateFiveMinsStatus();
                            fiveMinsPointsA += 2;
                            if (tryA + convA + dropGoalA == 0 && tryB + convB + dropGoalB == 0) {
                                elapsedTimeSecsFirstGoal = elapsedTimeSecs;
                            }

                            if (matchPeriod.equals(RugbyLeagueMatchPeriod.IN_FIRST_HALF))
                                pointsFirstHalfA += 2;
                            if (matchPeriod.equals(RugbyLeagueMatchPeriod.IN_SECOND_HALF))
                                pointsSecondHalfA += 2;

                            dropGoalA++;
                            currentPeriodPointsA += 2;
                            teamScoringLastPoint = TeamId.A;
                            goalSequence++;
                            break;
                        case B:
                            if (!matchPeriod.equals(RugbyLeagueMatchPeriod.IN_EXTRA_TIME_FIRST_HALF)
                                            && !matchPeriod.equals(RugbyLeagueMatchPeriod.IN_EXTRA_TIME_SECOND_HALF))
                                normalTimePointsB += 2;
                            updateFiveMinsStatus();
                            fiveMinsPointsB += 2;
                            if (tryA + convA + dropGoalA == 0 && tryB + convB + dropGoalB == 0) {
                                elapsedTimeSecsFirstGoal = elapsedTimeSecs;
                            }

                            if (matchPeriod.equals(RugbyLeagueMatchPeriod.IN_FIRST_HALF))
                                pointsFirstHalfB += 2;
                            if (matchPeriod.equals(RugbyLeagueMatchPeriod.IN_SECOND_HALF))
                                pointsSecondHalfB += 2;

                            dropGoalB++;
                            currentPeriodPointsB += 2;
                            teamScoringLastPoint = TeamId.B;
                            goalSequence++;
                            break;
                        case UNKNOWN:
                            throw new IllegalArgumentException("TeamId must be specified for goal scored");
                    }

                    break;
                case DROP_GOAL:
                    elapsedTimeAtLastGoalSecs = elapsedTimeSecs;
                    matchPeriodInWhichLastGoalScored = matchPeriod;

                    switch (teamId) {
                        case A:
                            if (!matchPeriod.equals(RugbyLeagueMatchPeriod.IN_SHOOTOUT)) {

                                if (!matchPeriod.equals(RugbyLeagueMatchPeriod.IN_EXTRA_TIME_FIRST_HALF) && !matchPeriod
                                                .equals(RugbyLeagueMatchPeriod.IN_EXTRA_TIME_SECOND_HALF))
                                    normalTimePointsA += 1;

                                updateFiveMinsStatus();
                                fiveMinsPointsA += 1;

                                if (tryA + convA + dropGoalA == 0 && tryB + convB + dropGoalB == 0) {
                                    elapsedTimeSecsFirstGoal = elapsedTimeSecs;
                                }

                                if (matchPeriod.equals(RugbyLeagueMatchPeriod.IN_FIRST_HALF))
                                    pointsFirstHalfA += 1;
                                if (matchPeriod.equals(RugbyLeagueMatchPeriod.IN_SECOND_HALF))
                                    pointsSecondHalfA += 1;

                                dropGoalA++;
                                currentPeriodPointsA += 1;
                                teamScoringLastPoint = TeamId.A;
                                goalSequence++;

                            } else {
                                shootOutGoalsA++;
                                shootOutTimeCounter++;
                                if (isShootoutSettled()) {
                                    this.dropGoalA++;// = shootOutGoalsA>shootOutGoalsB? 1:0;
                                    matchPeriod = RugbyLeagueMatchPeriod.MATCH_COMPLETED;
                                }

                            }
                            break;
                        case B:
                            if (!matchPeriod.equals(RugbyLeagueMatchPeriod.IN_SHOOTOUT)) {

                                if (!matchPeriod.equals(RugbyLeagueMatchPeriod.IN_EXTRA_TIME_FIRST_HALF) && !matchPeriod
                                                .equals(RugbyLeagueMatchPeriod.IN_EXTRA_TIME_SECOND_HALF))
                                    normalTimePointsB += 1;

                                updateFiveMinsStatus();
                                fiveMinsPointsB += 1;

                                if (tryA + convA + dropGoalA == 0 && tryB + convB + dropGoalB == 0) {
                                    elapsedTimeSecsFirstGoal = elapsedTimeSecs;
                                }
                                if (matchPeriod.equals(RugbyLeagueMatchPeriod.IN_FIRST_HALF))
                                    pointsFirstHalfB += 1;
                                if (matchPeriod.equals(RugbyLeagueMatchPeriod.IN_SECOND_HALF))
                                    pointsSecondHalfB += 1;

                                dropGoalB++;
                                currentPeriodPointsB += 1;
                                teamScoringLastPoint = TeamId.B;
                                goalSequence++;

                            } else {
                                shootOutGoalsB++;
                                shootOutTimeCounter++;
                                if (isShootoutSettled()) {
                                    this.dropGoalB++;
                                    matchPeriod = RugbyLeagueMatchPeriod.MATCH_COMPLETED;
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
                    if (matchPeriod.equals(RugbyLeagueMatchPeriod.IN_SHOOTOUT)) {
                        shootOutTimeCounter++;
                        if (isShootoutSettled()) {
                            this.dropGoalA += shootOutGoalsA > shootOutGoalsB ? 1 : 0;
                            this.dropGoalB += shootOutGoalsA > shootOutGoalsB ? 0 : 1;
                            matchPeriod = RugbyLeagueMatchPeriod.MATCH_COMPLETED;
                        }
                    }
                    break;

                case SET_PERIOD_START:
                    switch (matchPeriod) {
                        case PREMATCH:
                            elapsedTimeThisPeriodSecs = 0;
                            matchPeriod = RugbyLeagueMatchPeriod.IN_FIRST_HALF;
                            injuryTimeSecs = defaultInjuryTimeFirstHalfSecs;
                            break;
                        case IN_FIRST_HALF:
                            break;
                        case AT_HALF_TIME:
                            elapsedTimeThisPeriodSecs = 0;
                            matchPeriod = RugbyLeagueMatchPeriod.IN_SECOND_HALF;
                            injuryTimeSecs = defaultInjuryTimeSecondHalfSecs;
                            break;
                        case IN_SECOND_HALF:
                            break;
                        case AT_FULL_TIME:
                            if (extraPeriodSecs > 0)
                                overtimeNo++;

                            elapsedTimeThisPeriodSecs = 0;
                            matchPeriod = RugbyLeagueMatchPeriod.IN_EXTRA_TIME_FIRST_HALF;
                            injuryTimeSecs = defaultInjuryTimeExtraTimeHalfDeciSeconds;

                            break;

                        case IN_EXTRA_TIME_FIRST_HALF:
                            break;

                        case AT_EXTRA_TIME_HALF_TIME:
                            overtimeNo++;
                            elapsedTimeThisPeriodSecs = 0;
                            matchPeriod = RugbyLeagueMatchPeriod.IN_EXTRA_TIME_SECOND_HALF;
                            injuryTimeSecs = defaultInjuryTimeExtraTimeHalfDeciSeconds;
                            break;

                        case AT_EXTRA_TIME_END:
                            overtimeNo++;
                            elapsedTimeThisPeriodSecs = 0;
                            if (penaltiesPossible && (tryA + convA + dropGoalA == tryB + convB + dropGoalB)) {
                                matchPeriod = RugbyLeagueMatchPeriod.IN_SHOOTOUT;
                                break;
                            } else {
                                if (tryA + convA + dropGoalA != tryB + convB + dropGoalB) {
                                    matchPeriod = RugbyLeagueMatchPeriod.MATCH_COMPLETED;
                                } else {
                                    matchPeriod = RugbyLeagueMatchPeriod.IN_EXTRA_TIME_FIRST_HALF;
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
                            matchPeriod = RugbyLeagueMatchPeriod.AT_HALF_TIME;
                            previousPeriodGoalsA = currentPeriodPointsA;
                            previousPeriodGoalsB = currentPeriodPointsB;
                            currentPeriodPointsA = 0;
                            currentPeriodPointsB = 0;

                            currentPeriodTrysA = 0;
                            currentPeriodTrysB = 0;

                            setElapsedTime(normalPeriodSecs);
                            elapsedTimeAtLastMatchIncidentSecs = normalPeriodSecs;
                            setClockTimeOfLastElapsedTimeFromIncident();
                            break;

                        case AT_HALF_TIME:
                            break;

                        case IN_SECOND_HALF:

                            if (extraPeriodSecs == 0 || tryA + convA + dropGoalA != tryB + convB + dropGoalB)
                                matchPeriod = RugbyLeagueMatchPeriod.MATCH_COMPLETED;
                            else
                                matchPeriod = RugbyLeagueMatchPeriod.AT_FULL_TIME;
                            previousPeriodGoalsA = currentPeriodPointsA;
                            previousPeriodGoalsB = currentPeriodPointsB;
                            currentPeriodPointsA = 0;
                            currentPeriodPointsB = 0;
                            currentPeriodTrysA = 0;
                            currentPeriodTrysB = 0;
                            setElapsedTime(normalPeriodSecs * 2);
                            elapsedTimeAtLastMatchIncidentSecs = normalPeriodSecs;
                            setClockTimeOfLastElapsedTimeFromIncident();
                            break;

                        case AT_FULL_TIME:
                            break;

                        case IN_EXTRA_TIME_FIRST_HALF:
                            if (tryA + convA + dropGoalA == tryB + convB + dropGoalB) {
                                matchPeriod = RugbyLeagueMatchPeriod.AT_EXTRA_TIME_HALF_TIME;
                            } else {
                                matchPeriod = RugbyLeagueMatchPeriod.MATCH_COMPLETED;
                            }
                            previousPeriodGoalsA = currentPeriodPointsA;
                            previousPeriodGoalsB = currentPeriodPointsB;
                            currentPeriodPointsA = 0;
                            currentPeriodPointsB = 0;
                            currentPeriodTrysA = 0;
                            currentPeriodTrysB = 0;
                            setElapsedTime(normalPeriodSecs * 2 + (overtimeNo) * extraPeriodSecs);

                            elapsedTimeAtLastMatchIncidentSecs = normalPeriodSecs + extraPeriodSecs;

                            setClockTimeOfLastElapsedTimeFromIncident();
                            break;
                        case AT_EXTRA_TIME_HALF_TIME:
                        case IN_EXTRA_TIME_SECOND_HALF:
                            if (tryA + convA + dropGoalA == tryB + convB + dropGoalB) {
                                matchPeriod = RugbyLeagueMatchPeriod.AT_EXTRA_TIME_END;
                            } else {
                                matchPeriod = RugbyLeagueMatchPeriod.MATCH_COMPLETED;
                            }
                            previousPeriodGoalsA = currentPeriodPointsA;
                            previousPeriodGoalsB = currentPeriodPointsB;
                            currentPeriodPointsA = 0;
                            currentPeriodPointsB = 0;
                            currentPeriodTrysA = 0;
                            currentPeriodTrysB = 0;
                            setElapsedTime(normalPeriodSecs * 2 + (overtimeNo) * extraPeriodSecs);

                            elapsedTimeAtLastMatchIncidentSecs = normalPeriodSecs + 2 * extraPeriodSecs;

                            setClockTimeOfLastElapsedTimeFromIncident();
                            break;

                        case IN_SHOOTOUT:
                            matchPeriod = RugbyLeagueMatchPeriod.MATCH_COMPLETED;
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

    private void checkIfPenatlyOrConversionGoalApproporate(RugbyLeagueMatchIncidentType rugbyLeagueMatchIncidentType,
                    TeamId teamId) {
        // Penalty or conversion should only happens after penalty fault or a try.
        if (rugbyLeagueMatchIncidentType == RugbyLeagueMatchIncidentType.CONVERSION_GOAL
                        || rugbyLeagueMatchIncidentType == RugbyLeagueMatchIncidentType.CONVERSION_MISS) {
            if (rugbyLeagueMatchStatus.getRugbyLeagueMatchStatus() != RugbyLeagueMatchStatusType.CONVERSION)
                throw new IllegalArgumentException("Conversion goal can not be happening now");
            rugbyLeagueMatchStatus.resetRugbyLeagueMatchStatus();
        } else if (rugbyLeagueMatchIncidentType == RugbyLeagueMatchIncidentType.PENALTY_GOAL
                        || rugbyLeagueMatchIncidentType == RugbyLeagueMatchIncidentType.PENALTY_MISS) {
            if (rugbyLeagueMatchStatus.getRugbyLeagueMatchStatus() != RugbyLeagueMatchStatusType.PENALTY)
                throw new IllegalArgumentException("Penalty goal can not be happening now");
            rugbyLeagueMatchStatus.resetRugbyLeagueMatchStatus();
        }

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
            previousFiveMinsGoalsA = fiveMinsPointsA;
            previousFiveMinsGoalsB = fiveMinsPointsB;
            fiveMinsPointsA = 0;
            fiveMinsPointsB = 0;
        }

        if (currentFiveMinsEnder < newFiveMinsEnder - 300) { // time jump multiple 5 minutes
            currentFiveMinsEnder = newFiveMinsEnder;
            previousFiveMinsGoalsA = 0;
            previousFiveMinsGoalsB = 0;
            fiveMinsPointsA = 0;
            fiveMinsPointsB = 0;
        }
    }

    private void sinBinTimerTrigger(ArrayList<Integer> sendOffTimer_local, int elapsedTimeSecsIn, int sendOffLength) {
        // clean up sin bin timer first
        if (sendOffTimer_local.size() == 0) {
            sendOffTimer_local.add(elapsedTimeSecsIn + sendOffLength);
        } else {

            sendOffTimer_local.add(elapsedTimeSecsIn + sendOffLength);
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
                checkWhetherToExtendInjuryTime(elapsedTimeThisPeriodSecs, normalPeriodSecs);
                break;
            case IN_SECOND_HALF:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs - normalPeriodSecs;
                checkWhetherToExtendInjuryTime(elapsedTimeThisPeriodSecs, normalPeriodSecs);
                break;
            case IN_EXTRA_TIME_FIRST_HALF:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs - 2 * normalPeriodSecs;
                checkWhetherToExtendInjuryTime(elapsedTimeThisPeriodSecs, normalPeriodSecs);
                break;
            case IN_EXTRA_TIME_SECOND_HALF:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs - 2 * normalPeriodSecs - extraPeriodSecs;
                checkWhetherToExtendInjuryTime(elapsedTimeThisPeriodSecs, normalPeriodSecs);
                break;
            case IN_SHOOTOUT:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs - 2 * normalPeriodSecs - extraPeriodSecs * 2;
                break;
            default:
                throw new IllegalArgumentException("setElapsedTime");
        }
    }

    /**
     * if elapsed time in a matchIncident is within one minute of exceeding normal+injury time then need to extend
     * injury time - our estimate was probably too low
     * 
     * @param elapsedTimeThisPeriodSecs
     * @param halfPeriodSecs
     */
    private void checkWhetherToExtendInjuryTime(int elapsedTimeThisPeriodSecs, int halfPeriodSecs) {
        if (elapsedTimeThisPeriodSecs >= halfPeriodSecs + injuryTimeSecs - 60)
            /*
             * add a minute to current elapsed time to extend injury time
             */
            injuryTimeSecs = elapsedTimeThisPeriodSecs - halfPeriodSecs + 60;
    }

    @Override
    public AlgoMatchState copy() {
        RugbyLeagueMatchState cc = new RugbyLeagueMatchState(matchFormat);
        cc.setEqualTo(this);
        return cc;

    }

    @Override
    public void setEqualTo(AlgoMatchState matchState) {
        super.setEqualTo(matchState);
        try {
            this.ballPosition = (BallPosition) (((RugbyLeagueMatchState) matchState).ballPosition).clone();
            this.rugbyLeagueMatchStatus =
                            (RugbyLeagueMatchStatus) (((RugbyLeagueMatchState) matchState).rugbyLeagueMatchStatus)
                                            .clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        this.setTryA(((RugbyLeagueMatchState) matchState).getTryA());
        this.setTryB(((RugbyLeagueMatchState) matchState).getTryB());

        this.setConvA(((RugbyLeagueMatchState) matchState).getConvA());
        this.setConvB(((RugbyLeagueMatchState) matchState).getConvB());

        this.setDropGoalA(((RugbyLeagueMatchState) matchState).getDropGoalA());
        this.setDropGoalB(((RugbyLeagueMatchState) matchState).getDropGoalB());

        this.setElapsedTimeSecs(((RugbyLeagueMatchState) matchState).getElapsedTimeSecs(),
                        ((RugbyLeagueMatchState) matchState).getOvertimeNo());
        this.setMatchPeriod(((RugbyLeagueMatchState) matchState).getMatchPeriod());
        this.setTeamScoringLastGoal(((RugbyLeagueMatchState) matchState).getTeamScoringLastGoal());
        /* update cards infor and sendoff info */
        this.setRedCardA(((RugbyLeagueMatchState) matchState).getRedCardA());
        this.setRedCardB(((RugbyLeagueMatchState) matchState).getRedCardB());

        this.tenMinsSinBinTimer_A.clear();
        this.tenMinsSinBinTimer_A = cloneIntegerList(((RugbyLeagueMatchState) matchState).getTenMinsSinBinTimer_A());

        this.tenMinsSinBinTimer_B.clear();
        this.tenMinsSinBinTimer_B = cloneIntegerList(((RugbyLeagueMatchState) matchState).getTenMinsSinBinTimer_B());
        this.setInjuryTimeSecs(((RugbyLeagueMatchState) matchState).getInjuryTimeSecs());
        this.setTenMinsSinBinA(((RugbyLeagueMatchState) matchState).getTenMinsSinBinA());
        this.setTenMinsSinBinB(((RugbyLeagueMatchState) matchState).getTenMinsSinBinB());
        this.setShootOutGoalsA(((RugbyLeagueMatchState) matchState).getShootOutGoalsA());
        this.setShootOutGoalsB(((RugbyLeagueMatchState) matchState).getShootOutGoalsB());
        this.setShootOutTimeCounter(((RugbyLeagueMatchState) matchState).getShootOutTimeCounter());
        this.setOvertimeNo(((RugbyLeagueMatchState) matchState).getOvertimeNo());
        this.setGoalSequence(((RugbyLeagueMatchState) matchState).getGoalSequence());
        this.setCurrentPeriodGoalsA(((RugbyLeagueMatchState) matchState).getCurrentPeriodPointsA());
        this.setCurrentPeriodGoalsB(((RugbyLeagueMatchState) matchState).getCurrentPeriodPointsB());
        this.setOvertimeNo(((RugbyLeagueMatchState) matchState).getOvertimeNo());
        this.setNormalTimePointsA(((RugbyLeagueMatchState) matchState).getNormalTimePointsA());
        this.setNormalTimePointsB(((RugbyLeagueMatchState) matchState).getNormalTimePointsB());
        this.setElapsedTimeSecsFirstGoal(((RugbyLeagueMatchState) matchState).getElapsedTimeSecsFirstGoal());
        this.setFiveMinsGoalsA(((RugbyLeagueMatchState) matchState).getFiveMinsGoalsA());
        this.setFiveMinsGoalsB(((RugbyLeagueMatchState) matchState).getFiveMinsGoalsB());
        this.setPreviousFiveMinsGoalsA(((RugbyLeagueMatchState) matchState).getPreviousFiveMinsGoalsA());
        this.setPreviousFiveMinsGoalsB(((RugbyLeagueMatchState) matchState).getPreviousFiveMinsGoalsB());
        this.setCurrentFiveMinsEnder(((RugbyLeagueMatchState) matchState).getCurrentFiveMinsEnder());
        this.setPreviousFiveMinsEnder(((RugbyLeagueMatchState) matchState).getPreviousFiveMinsEnder());
        this.setFiveMinsNo(((RugbyLeagueMatchState) matchState).getFiveMinsNo());
        this.setCurrentPeriodTrysA(((RugbyLeagueMatchState) matchState).getCurrentPeriodTrysA());
        this.setCurrentPeriodTrysB(((RugbyLeagueMatchState) matchState).getCurrentPeriodTrysB());

    }


    public static ArrayList<Integer> cloneIntegerList(ArrayList<Integer> list) {
        ArrayList<Integer> clone = new ArrayList<Integer>(list.size());
        for (Integer item : list)
            clone.add(item);
        return clone;
    }


    @Override
    public MatchIncidentPrompt getNextPrompt() {
        MatchIncidentPrompt matchIncidentPrompt = null;
        if (matchPeriod == RugbyLeagueMatchPeriod.MATCH_COMPLETED)
            matchIncidentPrompt = new MatchIncidentPrompt("Match finished");
        else if (matchPeriod == RugbyLeagueMatchPeriod.PREMATCH || matchPeriod == RugbyLeagueMatchPeriod.AT_HALF_TIME
                        || matchPeriod == RugbyLeagueMatchPeriod.AT_FULL_TIME
                        || matchPeriod == RugbyLeagueMatchPeriod.AT_EXTRA_TIME_HALF_TIME
                        || matchPeriod == RugbyLeagueMatchPeriod.AT_EXTRA_TIME_END)
            matchIncidentPrompt = new MatchIncidentPrompt("S - start match", "S");

        else
            matchIncidentPrompt = new MatchIncidentPrompt(
                            "Next event (Nnn-Nothing happened for nn periods, H-Home goal, A-Away goal, A-Away goal, "
                                            + "RH/A-Home/Away 10 minutes sin bin, TH/A- Terminate sin bin, CH/A Conversion home away scored"
                                            + "PH/A- Penalty home or away, F(H/A)/(1/2)- Field position, BH/A- Ball possession, E- end period)",
                            "N");
        return matchIncidentPrompt;
    }

    @Override
    public MatchIncident getMatchIncident(String response) {
        /*
         * 
         * M: reset field info
         * 
         * F + (M/H/A) + (1/2) (Mid field , 22m, 10m) : FM, FH1, FA2 etc.
         * 
         * CSH/A (conversion started, home away) CH, CA (conversion scored home away)
         * 
         * PH/A/M (penalty given to home/ away, penalty missed)
         * 
         * H drop goal, HP, home penalty goal, HC home conversion goal A drop goal, AP, away penalty goal, AC away
         * conversion goal
         * 
         * BH, BA ball position settings TH/A try home, away
         * 
         * 
         * 
         */
        RugbyLeagueMatchIncidentType rugbyMatchIncidentType = null;
        ElapsedTimeMatchIncidentType elapsedTimeMatchIncidentType = null;
        TeamId teamId = null;
        FieldPositionType fieldPosition = null;
        int incidentElapsedTimeSecs = elapsedTimeSecs;
        int injuryTimeSecs = 0;
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
            case 'I':
                rugbyMatchIncidentType = RugbyLeagueMatchIncidentType.SET_INJURY_TIME;
                try {
                    injuryTimeSecs = Integer.parseInt(response.substring(1));
                    if (injuryTimeSecs < 0 || injuryTimeSecs > 1000)
                        throw new Exception();
                } catch (Exception e) {
                    return null;
                }
                break;

            case 'M':
                rugbyMatchIncidentType = RugbyLeagueMatchIncidentType.RESET_FIELD_BALL_INFO;
                break;

            case 'F':
                try {
                    String yard1 = (response.substring(1, 2));
                    String yard2 = (response.substring(2));
                    if (yard1.equalsIgnoreCase("M")) {
                        rugbyMatchIncidentType = RugbyLeagueMatchIncidentType.FIELD_POSITION_SETTING;
                        fieldPosition = FieldPositionType.HALFWAYLINE;
                        break;
                    }
                    if (yard1.equalsIgnoreCase("A")) {
                        rugbyMatchIncidentType = RugbyLeagueMatchIncidentType.FIELD_POSITION_SETTING;
                        if (yard2.equals("1")) {
                            fieldPosition = FieldPositionType.B22MLINE_CENTRE;
                        } else if (yard2.equals("2")) {
                            fieldPosition = FieldPositionType.B10MLINE_CENTRE;
                        }
                        break;
                    }
                    if (yard1.equalsIgnoreCase("H")) {
                        rugbyMatchIncidentType = RugbyLeagueMatchIncidentType.FIELD_POSITION_SETTING;
                        if (yard2.equals("1")) {
                            fieldPosition = FieldPositionType.A22MLINE_CENTRE;
                        } else if (yard2.equals("2")) {
                            fieldPosition = FieldPositionType.A10MLINE_CENTRE;
                        }
                        break;
                    }

                } catch (Exception e) {
                    // int teamCount = 3;
                }
                break;

            case 'C': {
                String s = response.substring(1, 2).toUpperCase();
                switch (s) {
                    case "S":
                        String i = response.substring(2, 3).toUpperCase();
                        if (i.equals("H")) {
                            rugbyMatchIncidentType = RugbyLeagueMatchIncidentType.CONVERSION_START;
                            teamId = TeamId.A;
                        } else if (i.equals("A")) {
                            rugbyMatchIncidentType = RugbyLeagueMatchIncidentType.CONVERSION_START;
                            teamId = TeamId.B;
                        }

                        break;

                    case "H":
                        rugbyMatchIncidentType = RugbyLeagueMatchIncidentType.CONVERSION_GOAL;
                        teamId = TeamId.A;
                        break;

                    case "A":
                        rugbyMatchIncidentType = RugbyLeagueMatchIncidentType.CONVERSION_GOAL;
                        teamId = TeamId.B;
                        break;

                    case "M":
                        rugbyMatchIncidentType = RugbyLeagueMatchIncidentType.CONVERSION_MISS;
                        teamId = TeamId.UNKNOWN;
                        break;
                }

                break;
            }

            case 'P': {
                String s = response.substring(1).toUpperCase();
                switch (s) {
                    case "H":
                        rugbyMatchIncidentType = RugbyLeagueMatchIncidentType.PENALTY_START;
                        teamId = TeamId.A;
                        break;

                    case "A":
                        rugbyMatchIncidentType = RugbyLeagueMatchIncidentType.PENALTY_START;
                        teamId = TeamId.B;
                        break;

                    case "M":
                        rugbyMatchIncidentType = RugbyLeagueMatchIncidentType.PENALTY_MISS;
                        teamId = TeamId.UNKNOWN;
                        break;
                }
                break;
            }

            case 'H':
                if (response.length() == 1) {

                    // if( rugbyLeagueMatchStatus.getRugbyLeagueMatchStatus().equals(RugbyLeagueMatchStatusType.NORMAL))
                    rugbyMatchIncidentType = RugbyLeagueMatchIncidentType.DROP_GOAL;

                } else {
                    String s = response.substring(1);
                    switch (s) {
                        case "P":
                            rugbyMatchIncidentType = RugbyLeagueMatchIncidentType.PENALTY_GOAL;
                            teamId = TeamId.A;
                            break;
                        case "C":
                            rugbyMatchIncidentType = RugbyLeagueMatchIncidentType.CONVERSION_GOAL;
                            teamId = TeamId.A;
                            break;
                    }
                }
                teamId = TeamId.A;
                break;
            case 'A':
                if (response.length() == 1) {
                    rugbyMatchIncidentType = RugbyLeagueMatchIncidentType.DROP_GOAL;
                } else {
                    String s = response.substring(1);
                    switch (s) {
                        case "P":
                            rugbyMatchIncidentType = RugbyLeagueMatchIncidentType.PENALTY_GOAL;
                            teamId = TeamId.B;
                            break;
                        case "C":
                            rugbyMatchIncidentType = RugbyLeagueMatchIncidentType.CONVERSION_GOAL;
                            teamId = TeamId.B;
                            break;
                    }
                }
                teamId = TeamId.B;
                break;

            case 'B':
                try {
                    String teamCount = (response.substring(1));
                    if (teamCount.equalsIgnoreCase("H")) { // home team yellow card
                        rugbyMatchIncidentType = RugbyLeagueMatchIncidentType.BALL_POSITION_SETTING;
                        teamId = TeamId.A;
                        break;
                    }
                    if (teamCount.equalsIgnoreCase("A")) { // away team yellow card
                        rugbyMatchIncidentType = RugbyLeagueMatchIncidentType.BALL_POSITION_SETTING;
                        teamId = TeamId.B;
                        break;
                    }
                } catch (Exception e) {
                    // int teamCount = 3;
                }
                break;

            case 'T':// hit a try
                try {
                    String teamCount = (response.substring(1));
                    if (teamCount.equalsIgnoreCase("H")) { // home team yellow card
                        rugbyMatchIncidentType = RugbyLeagueMatchIncidentType.TRY;
                        teamId = TeamId.A;
                        break;
                    }
                    if (teamCount.equalsIgnoreCase("A")) { // away team yellow card
                        rugbyMatchIncidentType = RugbyLeagueMatchIncidentType.TRY;
                        teamId = TeamId.B;
                        break;
                    }
                } catch (Exception e) {
                    // int teamCount = 3;
                }
                break;

            case 'R':// red card and sin bins
                if (elapsedTimeSecs == 0) {
                    throw new IllegalArgumentException("Card can not happen now");
                }
                try {
                    String teamCount = (response.substring(1, 2));
                    if (teamCount.equalsIgnoreCase("H")) { // home team yellow card
                        rugbyMatchIncidentType = RugbyLeagueMatchIncidentType.TEN_MINS_PENALTY_START;
                        teamId = TeamId.A;
                        break;
                    }
                    if (teamCount.equalsIgnoreCase("A")) { // away team yellow card
                        rugbyMatchIncidentType = RugbyLeagueMatchIncidentType.TEN_MINS_PENALTY_START;
                        teamId = TeamId.B;
                        break;
                    }
                    if (teamCount.equalsIgnoreCase("R")) { // away team yellow card
                        rugbyMatchIncidentType = RugbyLeagueMatchIncidentType.RED_CARD;
                        if (response.substring(2, 3).equalsIgnoreCase("H"))
                            teamId = TeamId.A;
                        if (response.substring(2, 3).equalsIgnoreCase("A"))
                            teamId = TeamId.B;
                        break;
                    }

                } catch (Exception e) {
                    // int teamCount = 3;
                }
                break;

            case 'Q':// terminate major sin bin
                if (tenMinsSinBinA == 0 && tenMinsSinBinB == 0) {
                    throw new IllegalArgumentException("no existing minor sin bins");
                }
                try {
                    String teamCount = (response.substring(1));
                    if (teamCount.equalsIgnoreCase("H")) { // home team yellow card
                        rugbyMatchIncidentType = RugbyLeagueMatchIncidentType.TEN_MINS_PENALTY_END;
                        teamId = TeamId.A;
                        break;
                    }
                    if (teamCount.equalsIgnoreCase("A")) { // away team yellow card
                        rugbyMatchIncidentType = RugbyLeagueMatchIncidentType.TEN_MINS_PENALTY_END;
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
        if (rugbyMatchIncidentType != null) {
            if (rugbyMatchIncidentType != RugbyLeagueMatchIncidentType.FIELD_POSITION_SETTING) {// &&rugbyMatchIncidentType
                                                                                                // !=
                                                                                                // RugbyLeagueMatchIncidentType.CONVERSION_START&&rugbyMatchIncidentType
                                                                                                // !=
                                                                                                // RugbyLeagueMatchIncidentType.PENALTY_START
                incident = new RugbyLeagueMatchIncident(rugbyMatchIncidentType, incidentElapsedTimeSecs, teamId);
            } else {
                incident = new RugbyLeagueMatchIncident(rugbyMatchIncidentType, incidentElapsedTimeSecs, fieldPosition);
            }
        } else {
            incident = new ElapsedTimeMatchIncident(elapsedTimeMatchIncidentType, incidentElapsedTimeSecs);
        }
        return incident;
    }

    private static final String goalsKey = "Points";
    private static final String shootOutKey = "Shoot out goals";
    private static final String elapsedTimeKey = "Elapsed time";
    // private static final String injuryTimeKey = "Injury time to be played";
    private static final String matchPeriodKey = "Match period";
    private static final String matchStatusKey = "Match Status";

    private static final String shootOutCounterKey = "Shoot out chances used";
    private static final String ballPositionKey = "Ball possession";
    private static final String tenMinsSendOffKey = "Ten Minutes sin bin and Red Card status";
    private static final String injuryTimeKey = "Injury time to be played";
    private static final String fieldPositionKey = "Field position";

    @Override
    public LinkedHashMap<String, String> getAsMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put(goalsKey, String.format("%d-%d", tryA * 4 + convA * 2 + dropGoalA * 1,
                        tryB * 4 + convB * 2 + dropGoalB * 1));
        int mins = elapsedTimeSecs / 60;
        int secs = elapsedTimeSecs - mins * 60;
        map.put(elapsedTimeKey, String.format("%d:%02d", mins, secs));
        map.put(matchPeriodKey, matchPeriod.toString());
        map.put(matchStatusKey, rugbyLeagueMatchStatus.getRugbyLeagueMatchStatus().toString());
        map.put(ballPositionKey, (this.ballPosition.getBallHoldingNow()).toString());
        map.put(fieldPositionKey, (this.ballPosition.getFieldPosition()).toString());

        // map.put(goalSequenceKey, getGoalSequenceId());
        map.put(tenMinsSendOffKey, String.format("%d-%d", tenMinsSinBinA + redCardA, tenMinsSinBinB + redCardB));
        map.put(shootOutKey, String.format("%d-%d", shootOutGoalsA, shootOutGoalsB));
        // map.put(overTimeSequenceKey, Integer.toString(overtimeNo));
        map.put(shootOutCounterKey, Integer.toString(shootOutTimeCounter));
        mins = injuryTimeSecs / 60;
        secs = injuryTimeSecs - mins * 60;
        map.put(injuryTimeKey, String.format("%d:%02d", mins, secs));

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
    public boolean isMatchCompleted() {
        return (matchPeriod == RugbyLeagueMatchPeriod.MATCH_COMPLETED);
    }

    /**
     * Returns true if at the end of normal time
     * 
     * @return
     */

    public boolean isNormalTimeMatchCompleted() {
        return (!(matchPeriod == RugbyLeagueMatchPeriod.PREMATCH || matchPeriod == RugbyLeagueMatchPeriod.IN_FIRST_HALF
                        || matchPeriod == RugbyLeagueMatchPeriod.IN_SECOND_HALF
                        || matchPeriod == RugbyLeagueMatchPeriod.AT_HALF_TIME));
    }

    /**
     * Returns true if at the end of one period and before the next one (if any) starts
     * 
     * @return
     */

    public boolean isPeriodCompleted() {
        return (matchPeriod == RugbyLeagueMatchPeriod.AT_HALF_TIME || matchPeriod == RugbyLeagueMatchPeriod.AT_FULL_TIME
                        || matchPeriod == RugbyLeagueMatchPeriod.MATCH_COMPLETED);
    }

    /**
     * Returns the id of the team winning the current five minutes
     * 
     * @return
     */

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

    public TeamId getNormalTimeMatchWinner() {
        if (!isNormalTimeMatchCompleted())
            return null;
        else {
            TeamId teamId;
            if (normalTimePointsA > normalTimePointsB)
                teamId = TeamId.A;
            else if (normalTimePointsA == normalTimePointsB)
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
            if (tryA * 4 + convA * 2 + dropGoalA * 1 > tryB * 4 + convB * 2 + dropGoalB * 1)
                teamId = TeamId.A;
            else if (tryA * 4 + convA * 2 + dropGoalA * 1 == tryB * 4 + convB * 2 + dropGoalB * 1)
                teamId = TeamId.UNKNOWN;
            else
                teamId = TeamId.B;
            return teamId;
        }
    }

    /**
     * Get # points scored by team A in the previous match period
     * 
     * @return previousPeriodGoalsA
     */
    public int getPreviousPeriodGoalsA() {
        return previousPeriodGoalsA;
    }

    /**
     * Get # points scored by team B in the previous match period
     * 
     * @return previousPeriodGoalsB
     */
    public int getPreviousPeriodGoalsB() {
        return previousPeriodGoalsB;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((ballPosition == null) ? 0 : ballPosition.hashCode());
        result = prime * result + convA;
        result = prime * result + convB;
        result = prime * result + currentFiveMinsEnder;
        result = prime * result + currentPeriodPointsA;
        result = prime * result + currentPeriodPointsB;
        result = prime * result + currentPeriodTrysA;
        result = prime * result + currentPeriodTrysB;
        result = prime * result + dropGoalA;
        result = prime * result + dropGoalB;
        result = prime * result + elapsedTimeAtLastGoalSecs;
        result = prime * result + elapsedTimeAtLastMatchIncidentSecs;
        result = prime * result + elapsedTimeSecs;
        result = prime * result + elapsedTimeSecsFirstGoal;
        result = prime * result + elapsedTimeThisPeriodSecs;
        result = prime * result + extraPeriodSecs;
        result = prime * result + fiveMinsNo;
        result = prime * result + fiveMinsPointsA;
        result = prime * result + fiveMinsPointsB;
        result = prime * result + goalSequence;
        result = prime * result + injuryTimeSecs;
        result = prime * result + ((matchFormat == null) ? 0 : matchFormat.hashCode());
        result = prime * result + ((matchPeriod == null) ? 0 : matchPeriod.hashCode());
        result = prime * result + ((matchPeriodInWhichLastGoalScored == null) ? 0
                        : matchPeriodInWhichLastGoalScored.hashCode());
        result = prime * result + normalPeriodSecs;
        result = prime * result + normalTimePointsA;
        result = prime * result + normalTimePointsB;
        result = prime * result + overtimeNo;
        result = prime * result + (penaltiesPossible ? 1231 : 1237);
        result = prime * result + pointsFirstHalfA;
        result = prime * result + pointsFirstHalfB;
        result = prime * result + pointsSecondHalfA;
        result = prime * result + pointsSecondHalfB;
        result = prime * result + previousFiveMinsEnder;
        result = prime * result + previousFiveMinsGoalsA;
        result = prime * result + previousFiveMinsGoalsB;
        result = prime * result + previousPeriodGoalsA;
        result = prime * result + previousPeriodGoalsB;
        result = prime * result + redCardA;
        result = prime * result + redCardB;
        result = prime * result + ((rugbyLeagueMatchStatus == null) ? 0 : rugbyLeagueMatchStatus.hashCode());
        result = prime * result + shootOutGoalsA;
        result = prime * result + shootOutGoalsB;
        result = prime * result + shootOutTimeCounter;
        result = prime * result + ((teamScoringLastPoint == null) ? 0 : teamScoringLastPoint.hashCode());
        result = prime * result + ((teamTryLastPoint == null) ? 0 : teamTryLastPoint.hashCode());
        result = prime * result + tenMinsSinBinA;
        result = prime * result + tenMinsSinBinB;
        result = prime * result + ((tenMinsSinBinTimer_A == null) ? 0 : tenMinsSinBinTimer_A.hashCode());
        result = prime * result + ((tenMinsSinBinTimer_B == null) ? 0 : tenMinsSinBinTimer_B.hashCode());
        result = prime * result + thisSendoffLength;
        result = prime * result + tryA;
        result = prime * result + tryB;
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
        RugbyLeagueMatchState other = (RugbyLeagueMatchState) obj;
        if (ballPosition == null) {
            if (other.ballPosition != null)
                return false;
        } else if (!ballPosition.equals(other.ballPosition))
            return false;
        if (convA != other.convA)
            return false;
        if (convB != other.convB)
            return false;
        if (currentFiveMinsEnder != other.currentFiveMinsEnder)
            return false;
        if (currentPeriodPointsA != other.currentPeriodPointsA)
            return false;
        if (currentPeriodPointsB != other.currentPeriodPointsB)
            return false;
        if (currentPeriodTrysA != other.currentPeriodTrysA)
            return false;
        if (currentPeriodTrysB != other.currentPeriodTrysB)
            return false;
        if (dropGoalA != other.dropGoalA)
            return false;
        if (dropGoalB != other.dropGoalB)
            return false;
        if (elapsedTimeAtLastGoalSecs != other.elapsedTimeAtLastGoalSecs)
            return false;
        if (elapsedTimeAtLastMatchIncidentSecs != other.elapsedTimeAtLastMatchIncidentSecs)
            return false;
        if (elapsedTimeSecs != other.elapsedTimeSecs)
            return false;
        if (elapsedTimeSecsFirstGoal != other.elapsedTimeSecsFirstGoal)
            return false;
        if (elapsedTimeThisPeriodSecs != other.elapsedTimeThisPeriodSecs)
            return false;
        if (extraPeriodSecs != other.extraPeriodSecs)
            return false;
        if (fiveMinsNo != other.fiveMinsNo)
            return false;
        if (fiveMinsPointsA != other.fiveMinsPointsA)
            return false;
        if (fiveMinsPointsB != other.fiveMinsPointsB)
            return false;
        if (goalSequence != other.goalSequence)
            return false;
        if (injuryTimeSecs != other.injuryTimeSecs)
            return false;
        if (matchFormat == null) {
            if (other.matchFormat != null)
                return false;
        } else if (!matchFormat.equals(other.matchFormat))
            return false;
        if (matchPeriod != other.matchPeriod)
            return false;
        if (matchPeriodInWhichLastGoalScored != other.matchPeriodInWhichLastGoalScored)
            return false;
        if (normalPeriodSecs != other.normalPeriodSecs)
            return false;
        if (normalTimePointsA != other.normalTimePointsA)
            return false;
        if (normalTimePointsB != other.normalTimePointsB)
            return false;
        if (overtimeNo != other.overtimeNo)
            return false;
        if (penaltiesPossible != other.penaltiesPossible)
            return false;
        if (pointsFirstHalfA != other.pointsFirstHalfA)
            return false;
        if (pointsFirstHalfB != other.pointsFirstHalfB)
            return false;
        if (pointsSecondHalfA != other.pointsSecondHalfA)
            return false;
        if (pointsSecondHalfB != other.pointsSecondHalfB)
            return false;
        if (previousFiveMinsEnder != other.previousFiveMinsEnder)
            return false;
        if (previousFiveMinsGoalsA != other.previousFiveMinsGoalsA)
            return false;
        if (previousFiveMinsGoalsB != other.previousFiveMinsGoalsB)
            return false;
        if (previousPeriodGoalsA != other.previousPeriodGoalsA)
            return false;
        if (previousPeriodGoalsB != other.previousPeriodGoalsB)
            return false;
        if (redCardA != other.redCardA)
            return false;
        if (redCardB != other.redCardB)
            return false;
        if (rugbyLeagueMatchStatus == null) {
            if (other.rugbyLeagueMatchStatus != null)
                return false;
        } else if (!rugbyLeagueMatchStatus.equals(other.rugbyLeagueMatchStatus))
            return false;
        if (shootOutGoalsA != other.shootOutGoalsA)
            return false;
        if (shootOutGoalsB != other.shootOutGoalsB)
            return false;
        if (shootOutTimeCounter != other.shootOutTimeCounter)
            return false;
        if (teamScoringLastPoint != other.teamScoringLastPoint)
            return false;
        if (teamTryLastPoint != other.teamTryLastPoint)
            return false;
        if (tenMinsSinBinA != other.tenMinsSinBinA)
            return false;
        if (tenMinsSinBinB != other.tenMinsSinBinB)
            return false;
        if (tenMinsSinBinTimer_A == null) {
            if (other.tenMinsSinBinTimer_A != null)
                return false;
        } else if (!tenMinsSinBinTimer_A.equals(other.tenMinsSinBinTimer_A))
            return false;
        if (tenMinsSinBinTimer_B == null) {
            if (other.tenMinsSinBinTimer_B != null)
                return false;
        } else if (!tenMinsSinBinTimer_B.equals(other.tenMinsSinBinTimer_B))
            return false;
        if (thisSendoffLength != other.thisSendoffLength)
            return false;
        if (tryA != other.tryA)
            return false;
        if (tryB != other.tryB)
            return false;
        return true;
    }

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
                matchPeriod = RugbyLeagueMatchPeriod.IN_FIRST_HALF;
                elapsedTimeThisPeriodSecs = elapsedTimeSecs;
                injuryTimeSecs = defaultInjuryTimeFirstHalfSecs;
                break;

            case AT_HALF_TIME:
            case IN_FIRST_HALF:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs;
                if (elapsedTimeThisPeriodSecs >= normalPeriodSecs + injuryTimeSecs) {
                    elapsedTimeSecs = normalPeriodSecs; // reset counter
                                                        // to remove
                                                        // injury time
                    injuryTimeSecs = defaultInjuryTimeSecondHalfSecs;
                    elapsedTimeThisPeriodSecs = 0;
                    matchPeriod = RugbyLeagueMatchPeriod.IN_SECOND_HALF;
                }
                break;
            case AT_FULL_TIME:
            case IN_SECOND_HALF:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs - normalPeriodSecs;
                if (elapsedTimeThisPeriodSecs >= normalPeriodSecs + injuryTimeSecs) {
                    if (extraPeriodSecs == 0
                                    || tryA * 4 + convA * 2 + dropGoalA * 1 != tryB * 4 + convB * 2 + dropGoalB * 1) {
                        matchPeriod = RugbyLeagueMatchPeriod.MATCH_COMPLETED;
                    } else {
                        elapsedTimeSecs = 2 * normalPeriodSecs;
                        elapsedTimeThisPeriodSecs = 0;
                        injuryTimeSecs = defaultInjuryTimeExtraTimeHalfDeciSeconds;
                        matchPeriod = RugbyLeagueMatchPeriod.IN_EXTRA_TIME_FIRST_HALF;
                        overtimeNo++;
                    }
                }
                break;

            case IN_EXTRA_TIME_FIRST_HALF:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs - 2 * normalPeriodSecs;
                // golden goals
                if (tryA * 4 + convA * 2 + dropGoalA * 1 != tryB * 4 + convB * 2 + dropGoalB * 1) {
                    matchPeriod = RugbyLeagueMatchPeriod.MATCH_COMPLETED;
                    break;
                }
                if (elapsedTimeThisPeriodSecs >= extraPeriodSecs + injuryTimeSecs) {
                    elapsedTimeThisPeriodSecs = 0;
                    elapsedTimeSecs = 2 * normalPeriodSecs + (overtimeNo) * extraPeriodSecs;
                    injuryTimeSecs = defaultInjuryTimeExtraTimeHalfDeciSeconds;
                    if (tryA * 4 + convA * 2 + dropGoalA * 1 != tryB * 4 + convB * 2 + dropGoalB * 1) {
                        matchPeriod = RugbyLeagueMatchPeriod.MATCH_COMPLETED;
                        break;
                    } else {
                        matchPeriod = RugbyLeagueMatchPeriod.AT_EXTRA_TIME_HALF_TIME;
                        overtimeNo++;
                        break;
                    }

                }
                break;
            case AT_EXTRA_TIME_HALF_TIME:
            case AT_EXTRA_TIME_END:
            case IN_EXTRA_TIME_SECOND_HALF:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs - 2 * normalPeriodSecs - extraPeriodSecs;
                // golden goals
                if (tryA * 4 + convA * 2 + dropGoalA * 1 != tryB * 4 + convB * 2 + dropGoalB * 1) {
                    matchPeriod = RugbyLeagueMatchPeriod.MATCH_COMPLETED;
                    break;
                }
                if (elapsedTimeThisPeriodSecs >= extraPeriodSecs + injuryTimeSecs) {
                    elapsedTimeThisPeriodSecs = 0;
                    elapsedTimeSecs = 2 * normalPeriodSecs + (overtimeNo) * extraPeriodSecs;
                    if (tryA * 4 + convA * 2 + dropGoalA * 1 != tryB * 4 + convB * 2 + dropGoalB * 1) {
                        matchPeriod = RugbyLeagueMatchPeriod.MATCH_COMPLETED;
                        break;
                    } else {
                        if (penaltiesPossible) {
                            matchPeriod = RugbyLeagueMatchPeriod.IN_SHOOTOUT;
                            break;
                        } else {
                            matchPeriod = RugbyLeagueMatchPeriod.AT_EXTRA_TIME_END;
                            overtimeNo++;
                            break;
                        }
                    }

                }
                break;
            case IN_SHOOTOUT:
                shootOutTimeCounter++;
                if (isShootoutSettled()) {
                    dropGoalA += shootOutGoalsA > shootOutGoalsB ? 1 : 0;
                    dropGoalB += shootOutGoalsA > shootOutGoalsB ? 0 : 1;
                    matchPeriod = RugbyLeagueMatchPeriod.MATCH_COMPLETED;
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
            case AT_EXTRA_TIME_HALF_TIME:
                gamePeriod = GamePeriod.EXTRA_TIME_HALF_TIME;
                break;
            case IN_EXTRA_TIME_SECOND_HALF:
                gamePeriod = GamePeriod.EXTRA_TIME_SECOND_HALF;
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

    // public class RugbyLeagueMatchStatus implements Cloneable {
    // @Override
    // protected Object clone() throws CloneNotSupportedException {
    // RugbyLeagueMatchStatus clone = (RugbyLeagueMatchStatus) super.clone();
    // return clone;
    // }
    //
    // RugbyLeagueMatchStatusType rugbyLeagueMatchStatus;
    // TeamId teamId;
    //
    // public RugbyLeagueMatchStatus() {
    // rugbyLeagueMatchStatus = RugbyLeagueMatchStatusType.NORMAL;
    // }
    //
    // public RugbyLeagueMatchStatusType getRugbyLeagueMatchStatus() {
    // if (rugbyLeagueMatchStatus == null) {
    // rugbyLeagueMatchStatus = RugbyLeagueMatchStatusType.NORMAL;
    // }
    // return rugbyLeagueMatchStatus;
    // }
    //
    // public TeamId getTeamId() {
    // return teamId;
    // }
    //
    // public void setRugbyLeagueMatchStatus(RugbyLeagueMatchStatusType rugbyLeagueMatchStatus, TeamId teamId) {
    // this.rugbyLeagueMatchStatus = rugbyLeagueMatchStatus;
    // this.teamId = teamId;
    // }
    //
    // public void resetRugbyLeagueMatchStatus() {
    // rugbyLeagueMatchStatus = RugbyLeagueMatchStatusType.NORMAL;
    // teamId = TeamId.UNKNOWN;
    // }
    // }
    //
    // enum RugbyLeagueMatchStatusType {
    // NORMAL, PENALTY, CONVERSION
    // }

    // public class BallPosition implements Cloneable {
    // TeamId ballHoldingNow;
    // FieldPositionType fieldPosition;
    //
    // @Override
    // protected Object clone() throws CloneNotSupportedException {
    // BallPosition clone = (BallPosition) super.clone();
    // return clone;
    // }
    //
    // // Possibly adding yard info
    // public BallPosition() {
    // }
    //
    // public void resetBallPosition() {
    // ballHoldingNow = TeamId.UNKNOWN;
    // fieldPosition = FieldPositionType.UNKNOWN;
    // }
    //
    // public FieldPositionType getFieldPosition() {
    // if (fieldPosition == null) {
    // fieldPosition = FieldPositionType.UNKNOWN;
    // }
    // return fieldPosition;
    // }
    //
    // public void setFieldPosition(FieldPositionType fieldPosition) {
    // this.fieldPosition = fieldPosition;
    // }
    //
    // public void setBallHoldingNow(TeamId team) {
    // this.ballHoldingNow = team;
    // }
    //
    // public TeamId getBallHoldingNow() {
    // if (ballHoldingNow == null) {
    // ballHoldingNow = TeamId.UNKNOWN;
    // }
    //
    // return ballHoldingNow;
    // }
    //
    // public void alterBallHoldingNow() {
    // switch (ballHoldingNow) {
    // case A:
    // this.ballHoldingNow = TeamId.B;
    // break;
    // case B:
    // this.ballHoldingNow = TeamId.A;
    // break;
    // default:
    // throw new IllegalArgumentException("No current ball position info");
    // }
    //
    // }
    //
    // }

    /**
     * gets the sequence id to use for match based markets
     * 
     * @return
     */

    public String getSequenceIdForMatch() {
        return "M";
    }


    public String getSequenceIdForFiveMinsResult() {
        return String.format("F%d", (int) fiveMinsNo + 1);
    }

    /**
     * gets the sequence id to use for match period based markets
     * 
     * @return null if specified set can't occur, else the sequence id
     */

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
    public String getSequenceIdForGoal(int goalOffset) {
        return String.format("G%d", dropGoalA + dropGoalB + 1 + goalOffset);
    }

    /**
     * gets the sequence id to use for Conversion based markets
     * 
     * @param convOffset 0 = current , 1 = next etc
     */
    public String getSequenceIdForConversion(int convOffset) {
        return String.format("C%d", convA + convB + 1 + convOffset);
    }

    /**
     * gets the sequence id to use for Try based markets
     * 
     * @param convOffset 0 = current , 1 = next etc
     */

    public String getSequenceIdForTry(int tryOffset) {
        return String.format("T%d", tryA + tryB + 1 + tryOffset);
    }

    /**
     * returns true if in preMatch
     */
    @Override
    public boolean preMatch() {
        return (matchPeriod == RugbyLeagueMatchPeriod.PREMATCH);
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
            case AT_SHOOTOUT_END:
            case MATCH_COMPLETED:
            case PREMATCH:
            case AT_EXTRA_TIME_HALF_TIME:
            case AT_EXTRA_TIME_END:
                secs = 0;
                break;
            case IN_FIRST_HALF:
            case IN_SECOND_HALF:
                secs = normalPeriodSecs + injuryTimeSecs - elapsedTimeThisPeriodSecs;
                break;
            case IN_EXTRA_TIME_FIRST_HALF:
            case IN_EXTRA_TIME_SECOND_HALF:
                secs = extraPeriodSecs + injuryTimeSecs - elapsedTimeThisPeriodSecs;
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
        RugbyLeagueSimpleMatchState simpleMatchState =
                        new RugbyLeagueSimpleMatchState(this.preMatch(), this.isMatchCompleted(), matchPeriod,
                                        elapsedTimeSecs, isClockRunning(), getPointsA(), getPointsB(), pointsFirstHalfA,
                                        pointsFirstHalfB, pointsSecondHalfA, pointsSecondHalfB, getBallPosition());
        return simpleMatchState;
    }

    @Override
    public AlgoMatchState generateMatchStateForMatchResult(MatchResultMap matchResultMap) {
        /*
         * not yet supported for this sport
         */
        return null;
    }
}
