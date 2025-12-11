package ats.algo.sport.basketball;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ats.algo.core.GamePeriod;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchIncidentPrompt;
import ats.algo.core.baseclasses.MatchIncidentResult;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.ElapsedTimeMatchIncident;
import ats.algo.core.common.TeamId;
import ats.algo.core.common.ElapsedTimeMatchIncident.ElapsedTimeMatchIncidentType;
import ats.algo.sport.basketball.BasketballMatchIncident.BasketballMatchIncidentType;
import ats.algo.sport.basketball.BasketballMatchIncident.FieldPositionType;

/**
 * MatchState class for Basketball
 * 
 * @author Jin
 * 
 */
public class BasketballMatchState extends MatchState {
    /* CJ added for penalties condition */

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final int extraPeriodSecs;
    private String[] freeThrowStatus = {"UNKNOWN", "0", "0"}; // teamid, free throw granted, free throw used
    private int pointsA;
    private int pointsB;
    /*
     * For points board use
     * 
     */
    private int firstQuarterPointsA = 0;
    private int firstHalfPointsA = 0;
    private int secondQuarterPointsA = 0;
    private int secondHalfPointsA = 0;
    private int thirdQuarterPointsA = 0;
    private int fourthQuarterPointsA = 0;

    private int overTimeQuarterPointsA = 0; // shared by two format of match

    private int firstQuarterPointsB = 0;
    private int firstHalfPointsB = 0;
    private int secondQuarterPointsB = 0;
    private int secondHalfPointsB = 0;
    private int thirdQuarterPointsB = 0;
    private int fourthQuarterPointsB = 0;

    private int overTimeQuarterPointsB = 0; // shared by two format of match

    private BallPosition ballPosition;
    private boolean isFourtyMinutiesMatch;
    private int elapsedTimeAtLastGoalSecs;
    private BasketballMatchPeriod matchPeriodInWhichLastGoalScored;
    private int elapsedTimeSecs;
    private int elapsedTimeAtLastMatchIncidentSecs; // the time from the last matchIncident report
    private int elapsedTimeThisPeriodSecs; // recent MatchIncident;
    private final int normalPeriodSecs;
    private BasketballMatchPeriod matchPeriod; // the state following the most
    private final BasketballMatchFormat matchFormat;
    private int currentPeriodGoalsA;
    private int currentPeriodGoalsB;
    @JsonIgnore
    private int overtimeNo;
    @JsonIgnore
    private boolean twoHalvesFormat;
    @JsonIgnore
    private int normalTimeGoalsA;
    @JsonIgnore
    private int normalTimeGoalsB;

    @JsonIgnore
    private int previousPeriodGoalsA;
    @JsonIgnore
    private int previousPeriodGoalsB;
    @JsonIgnore
    private static final int timeIncrementSecs = 2;

    /**
     * Json class constructor. Not for general use
     */
    public BasketballMatchState() {
        this(new BasketballMatchFormat());
        // super();
        // normalPeriodSecs = 60 * 20;
        // elapsedTimeThisPeriodSecs = 0;
        // twoHalvesFormat = false;
        // pointsA = 0;
        // pointsB = 0;
        // elapsedTimeSecs = 1300;
        // this.matchFormat = new BasketballMatchFormat();
        // currentPeriodGoalsA = 0;
        // currentPeriodGoalsB = 0;
        // matchPeriod = BasketballMatchPeriod.PREMATCH;
        // extraPeriodSecs = 5 * 60;
        // initialiseFreeThrow(this.freeThrowStatus);
        // ballPosition = new BallPosition();

    }

    private void initialiseFreeThrow(String[] freeThrowStatusIn) {
        freeThrowStatusIn[0] = "UNKNOWN";
        freeThrowStatusIn[1] = "0";
        freeThrowStatusIn[2] = "0";
    }

    /**
     * Class constructor
     * 
     * @param matchFormat
     */
    public BasketballMatchState(MatchFormat matchFormat) {
        super();
        this.matchFormat = (BasketballMatchFormat) matchFormat;
        matchPeriod = BasketballMatchPeriod.PREMATCH;
        twoHalvesFormat = this.matchFormat.isTwoHalvesFormat();
        isFourtyMinutiesMatch = this.matchFormat.isFourtyMinutesMatch();
        if (!twoHalvesFormat) {
            normalPeriodSecs = this.matchFormat.getNormalTimeMinutes() * 15;
        } else {
            normalPeriodSecs = this.matchFormat.getNormalTimeMinutes() * 30;
        }
        extraPeriodSecs = this.matchFormat.getExtraTimeMinutes() * 60;
        elapsedTimeAtLastGoalSecs = -1;
        elapsedTimeAtLastMatchIncidentSecs = -1;
        initialiseFreeThrow(this.freeThrowStatus);
        ballPosition = new BallPosition();
    }

    /**
     * Get the ball holding information class
     * 
     * @return ballPosition
     */
    public BallPosition getBallPosition() {
        return ballPosition;
    }

    /**
     * Get the points for Team A in the first quarter
     * 
     * @return firstQuarterPointsA
     */
    public int getFirstQuarterPointsA() {
        return firstQuarterPointsA;
    }

    /**
     * Set the points for Team A in the first quarter
     * 
     * @param firstQuarterPointsA
     */
    public void setFirstQuarterPointsA(int firstQuarterPointsA) {
        this.firstQuarterPointsA = firstQuarterPointsA;
    }

    /**
     * Get the points for Team A in the first half
     * 
     * @return firstHalfPointsA
     */
    public int getFirstHalfPointsA() {
        return firstHalfPointsA;
    }

    /**
     * Set the points for Team A in the first half
     * 
     * @param firstHalfPointsA
     */
    public void setFirstHalfPointsA(int firstHalfPointsA) {
        this.firstHalfPointsA = firstHalfPointsA;
    }

    /**
     * Get the points for Team A in the second quarter
     * 
     * @return secondQuarterPointsA
     */
    public int getSecondQuarterPointsA() {
        return secondQuarterPointsA;
    }

    /**
     * Set the points for Team A in the second quarter
     * 
     * @param secondQuarterPointsA
     */
    public void setSecondQuarterPointsA(int secondQuarterPointsA) {
        this.secondQuarterPointsA = secondQuarterPointsA;
    }

    /**
     * Get the points for Team A in the second half
     * 
     * @return secondHalfPointsA
     */
    public int getSecondHalfPointsA() {
        return secondHalfPointsA;
    }

    /**
     * Set the points for Team A in the second half
     * 
     * @param secondHalfPointsA
     */
    public void setSecondHalfPointsA(int secondHalfPointsA) {
        this.secondHalfPointsA = secondHalfPointsA;
    }

    /**
     * Get the points for Team A in the third quarter
     * 
     * @return thirdQuarterPointsA
     */
    public int getThirdQuarterPointsA() {
        return thirdQuarterPointsA;
    }

    /**
     * Set the points for Team A in the third quarter
     * 
     * @param thirdQuarterPointsA
     */
    public void setThirdQuarterPointsA(int thirdQuarterPointsA) {
        this.thirdQuarterPointsA = thirdQuarterPointsA;
    }

    /**
     * Get the points for Team A in the fourth quarter
     * 
     * @return fourthQuarterPointsA
     */
    public int getFourthQuarterPointsA() {
        return fourthQuarterPointsA;
    }

    /**
     * Set the points for Team A in the fourth quarter
     * 
     * @param fourthQuarterPointsA
     */
    public void setFourthQuarterPointsA(int fourthQuarterPointsA) {
        this.fourthQuarterPointsA = fourthQuarterPointsA;
    }

    /**
     * Get the points for Team A in the over time quarters
     * 
     * @return overTimeQuarterPointsA
     */
    public int getOverTimeQuarterPointsA() {
        return overTimeQuarterPointsA;
    }

    /**
     * Set the points for Team A in the over time quarters
     * 
     * @param overTimeQuarterPointsA
     */
    public void setOverTimeQuarterPointsA(int overTimeQuarterPointsA) {
        this.overTimeQuarterPointsA = overTimeQuarterPointsA;
    }

    /**
     * Get the points for Team B in the first quarter
     * 
     * @return firstQuarterPointsB
     */
    public int getFirstQuarterPointsB() {
        return firstQuarterPointsB;
    }

    /**
     * Set the points for Team B in the first quarter
     * 
     * @param firstQuarterPointsB
     */
    public void setFirstQuarterPointsB(int firstQuarterPointsB) {
        this.firstQuarterPointsB = firstQuarterPointsB;
    }

    /**
     * Get the points for Team B in the first half
     * 
     * @return firstHalfPointsB
     */
    public int getFirstHalfPointsB() {
        return firstHalfPointsB;
    }

    /**
     * Set the points for Team B in the first half
     * 
     * @param firstHalfPointsB
     */
    public void setFirstHalfPointsB(int firstHalfPointsB) {
        this.firstHalfPointsB = firstHalfPointsB;
    }

    /**
     * Get the points for Team B in the second quarter
     * 
     * @return secondQuarterPointsB
     */
    public int getSecondQuarterPointsB() {
        return secondQuarterPointsB;
    }

    /**
     * Set the points for Team B in the second quarter
     * 
     * @param secondQuarterPointsB
     */
    public void setSecondQuarterPointsB(int secondQuarterPointsB) {
        this.secondQuarterPointsB = secondQuarterPointsB;
    }

    /**
     * Get the points for Team B in the second half
     * 
     * @return secondHalfPointsB
     */
    public int getSecondHalfPointsB() {
        return secondHalfPointsB;
    }

    /**
     * Set the points for Team B in the second half
     * 
     * @param secondHalfPointsB
     */
    public void setSecondHalfPointsB(int secondHalfPointsB) {
        this.secondHalfPointsB = secondHalfPointsB;
    }

    /**
     * Get the points for Team B in the third quarter
     * 
     * @return thirdQuarterPointsB
     */
    public int getThirdQuarterPointsB() {
        return thirdQuarterPointsB;
    }

    /**
     * Set the points for Team B in the third quarter
     * 
     * @param thirdQuarterPointsB
     */
    public void setThirdQuarterPointsB(int thirdQuarterPointsB) {
        this.thirdQuarterPointsB = thirdQuarterPointsB;
    }

    /**
     * Get the points for Team B in the fourth quarter
     * 
     * @return fourthQuarterPointsB
     */
    public int getFourthQuarterPointsB() {
        return fourthQuarterPointsB;
    }

    /**
     * Set the points for Team B in the fourth quarter
     * 
     * @param fourthQuarterPointsB
     */
    public void setFourthQuarterPointsB(int fourthQuarterPointsB) {
        this.fourthQuarterPointsB = fourthQuarterPointsB;
    }

    /**
     * Get the points for Team B in the over time quarters
     * 
     * @return overTimeQuarterPointsB
     */
    public int getOverTimeQuarterPointsB() {
        return overTimeQuarterPointsB;
    }

    /**
     * Set the points for Team B in the over time quarters
     * 
     * @param overTimeQuarterPointsB
     */
    public void setOverTimeQuarterPointsB(int overTimeQuarterPointsB) {
        this.overTimeQuarterPointsB = overTimeQuarterPointsB;
    }

    /**
     * Set the ball holding information class
     * 
     * @param ballPosition
     */
    public void setBallPosition(BallPosition ballPosition) {
        this.ballPosition = ballPosition;
    }

    /**
     * Check if this is a forty minutes match
     * 
     * @return true/false
     */
    public boolean isFourtyMinutiesMatch() {
        return isFourtyMinutiesMatch;
    }

    /**
     * Set if this is a forty minutes match
     * 
     * @param isFourtyMinutiesMatch
     */
    public void setFourtyMinutiesMatch(boolean isFourtyMinutiesMatch) {
        this.isFourtyMinutiesMatch = isFourtyMinutiesMatch;
    }

    /**
     * Check if this is two halves match, otherwise it should be a four quarters match
     * 
     * @return true/false
     */
    @JsonIgnore
    public boolean isTwoHalvesFormat() {
        return twoHalvesFormat;
    }

    /**
     * Set if this is a two halves match
     * 
     * @param isFourtyMinutiesMatch
     */
    @JsonIgnore
    public void setTwoHalvesFormat(boolean penaltiesPossible) {
        this.twoHalvesFormat = penaltiesPossible;
    }

    /**
     * Get team A normal time points
     * 
     * @return normalTimeGoalsA
     */
    @JsonIgnore
    public int getNormalTimeGoalsA() {
        return normalTimeGoalsA;
    }

    /**
     * Set team A normal time points
     * 
     * @param normalTimeGoalsA
     */
    @JsonIgnore
    public void setNormalTimeGoalsA(int normalTimeGoalsA) {
        this.normalTimeGoalsA = normalTimeGoalsA;
    }

    /**
     * Get team B normal time points
     * 
     * @return normalTimeGoalsB
     */
    @JsonIgnore
    public int getNormalTimeGoalsB() {
        return normalTimeGoalsB;
    }

    /**
     * Set team B normal time points
     * 
     * @param normalTimeGoalsB
     */
    @JsonIgnore
    public void setNormalTimeGoalsB(int normalTimeGoalsB) {
        this.normalTimeGoalsB = normalTimeGoalsB;
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
     * Get current free throw status as a array of string
     * 
     * @return freeThrowStatus
     */
    public String[] getFreeThrowStatus() {
        return freeThrowStatus;
    }

    /**
     * Set current free throw status as a array of string, freeThrowStatus[1] = "3" free throw chances given
     * freeThrowStatus[2] = "1" free throw chances consumed freeThrowStatus[0] = "H" or "A" indicating team that on free
     * throw
     * 
     * @param freeThrowStatus
     */
    public void setFreeThrowStatus(String[] freeThrowStatus) {
        this.freeThrowStatus = freeThrowStatus;
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
     * Set the elapsed time in match
     * 
     * @param elapsedTimeThisPeriodSecs
     */
    public void setElapsedTimeSecs(int elapsedTimeSecs) {
        this.elapsedTimeSecs = elapsedTimeSecs;
    }

    /**
     * Set the elapsed time in current period
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

    /**
     * Set points scored by team A in the current match period
     * 
     * @param currentPeriodGoalsA
     */
    public void setCurrentPeriodGoalsA(int currentPeriodGoalsA) {
        this.currentPeriodGoalsA = currentPeriodGoalsA;
    }

    /**
     * Set points scored by team B in the current match period
     * 
     * @param currentPeriodGoalsB
     */
    public void setCurrentPeriodGoalsB(int currentPeriodGoalsB) {
        this.currentPeriodGoalsB = currentPeriodGoalsB;
    }

    /**
     * Get no of points scored by team A
     * 
     * @return pointsA
     */
    public int getPointsA() {
        return pointsA;
    }

    /**
     * Set no of points scored by team A
     * 
     * @param pointsA
     */
    public void setPointsA(int goalsHome) {
        this.pointsA = goalsHome;
    }

    /**
     * Get no of points scored by team B
     * 
     * @return pointsB
     */
    public int getPointsB() {
        return pointsB;
    }

    /**
     * Set no of points scored by team B
     * 
     * @param pointsB
     */
    public void setPointsB(int goalsAway) {
        this.pointsB = goalsAway;
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
     * Get the elapsed time in the current period
     * 
     * @return elapsedTimeThisPeriodSecs
     */
    public int getElapsedTimeThisPeriodSecs() {
        return elapsedTimeThisPeriodSecs;
    }

    /**
     * Get the current period no
     * 
     * @return
     */
    @JsonIgnore
    public int getPeriodNo() {
        int n = 0;
        if (!twoHalvesFormat) {
            switch (matchPeriod) {
                case PREMATCH:
                case IN_FIRST_QUARTER:
                    n = 1;
                    break;
                case AT_FIRST_QUARTER_END:
                case IN_SECOND_QUARTER:
                    n = 2;
                    break;
                case AT_SECOND_QUARTER_END:
                case IN_THIRD_QUARTER:
                    n = 3;
                    break;
                case AT_THIRD_QUARTER_END:
                case IN_FOURTH_QUARTER:
                    n = 4;
                    break;
                case AT_FULL_TIME:
                case MATCH_COMPLETED:
                    n = 5;
                    break;
                case AT_EXTRA_PERIOD_END:
                case IN_EXTRA_TIME:
                    n = 6;
                    break;
                default:
                    throw new IllegalArgumentException("Unknown match period");
            }
        } else {
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
                case IN_FOURTH_QUARTER:
                    n = 4;
                    break;
                case MATCH_COMPLETED:
                    n = 5;
                    break;
                case AT_EXTRA_PERIOD_END:
                case IN_EXTRA_TIME:
                    n = 6;
                    break;
                default:
                    throw new IllegalArgumentException("Shoudl not go here");
            }
        }
        return n;
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

        if (!twoHalvesFormat) {
            if (elapsedTimeSecs == normalPeriodSecs) {
                this.elapsedTimeThisPeriodSecs = 0;
                matchPeriod = BasketballMatchPeriod.AT_FIRST_QUARTER_END;
            }
            if (elapsedTimeSecs > normalPeriodSecs) {
                this.elapsedTimeThisPeriodSecs = elapsedTimeSecs - normalPeriodSecs;
                matchPeriod = BasketballMatchPeriod.IN_SECOND_QUARTER;
            }
            if (elapsedTimeSecs == 2 * normalPeriodSecs) {
                this.elapsedTimeThisPeriodSecs = 0;
                matchPeriod = BasketballMatchPeriod.AT_SECOND_QUARTER_END;
            }
            if (elapsedTimeSecs > 2 * normalPeriodSecs) {
                this.elapsedTimeThisPeriodSecs = elapsedTimeSecs - 2 * normalPeriodSecs;
                matchPeriod = BasketballMatchPeriod.IN_THIRD_QUARTER;
            }
            if (elapsedTimeSecs == 3 * normalPeriodSecs) {
                this.elapsedTimeThisPeriodSecs = 0;
                matchPeriod = BasketballMatchPeriod.AT_FULL_TIME;
            }
            if (elapsedTimeSecs > 3 * normalPeriodSecs) {
                this.elapsedTimeThisPeriodSecs = elapsedTimeSecs - overtimeNoIn * extraPeriodSecs;
                matchPeriod = BasketballMatchPeriod.IN_EXTRA_TIME;
            }
        } else {
            if (elapsedTimeSecs == normalPeriodSecs) {
                this.elapsedTimeThisPeriodSecs = 0;
                matchPeriod = BasketballMatchPeriod.AT_HALF_TIME;
            }
            if (elapsedTimeSecs > normalPeriodSecs) {
                this.elapsedTimeThisPeriodSecs = elapsedTimeSecs - normalPeriodSecs;
                matchPeriod = BasketballMatchPeriod.IN_SECOND_HALF;
            }
            if (elapsedTimeSecs == 2 * normalPeriodSecs) {
                this.elapsedTimeThisPeriodSecs = 0;
                matchPeriod = BasketballMatchPeriod.AT_FULL_TIME;
            }

            if (elapsedTimeSecs > 2 * normalPeriodSecs) {
                this.elapsedTimeThisPeriodSecs = elapsedTimeSecs - overtimeNoIn * extraPeriodSecs;
                matchPeriod = BasketballMatchPeriod.IN_EXTRA_TIME;
            }

        }
    }

    /**
     * Get the current matchPeriod
     * 
     * @return matchPeriod
     */
    public BasketballMatchPeriod getMatchPeriod() {
        return matchPeriod;
    }

    /**
     * Set the current match period
     * 
     * @param matchPeriod
     */
    public void setMatchPeriod(BasketballMatchPeriod matchPeriod) {
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

        if (matchIncident.getClass() == BasketballMatchIncident.class) {

            TeamId teamId = ((BasketballMatchIncident) matchIncident).getTeamId();

            switch (((BasketballMatchIncident) matchIncident).getIncidentSubType()) {

                case FIRST_FREETHROW_SCORED:
                case SECOND_FREETHROW_SCORED:
                case THIRD_FREETHROW_SCORED:
                    elapsedTimeAtLastGoalSecs = elapsedTimeSecs;
                    matchPeriodInWhichLastGoalScored = matchPeriod;
                    // if (!freeThrowStatus[1].equals(Integer.toString((Integer.parseInt(freeThrowStatus[2])) + 1)))
                    // throw new IllegalArgumentException("This IS NOT the right free throw order");
                    freeThrowStatus[2] = Integer.toString((Integer.parseInt(freeThrowStatus[2])) + 1);

                    if (matchPeriod.equals(BasketballMatchPeriod.IN_FIRST_QUARTER)) {
                        if (teamId.equals(TeamId.A)) {
                            firstQuarterPointsA += 1;
                        } else if (teamId.equals(TeamId.B)) {
                            firstQuarterPointsB += 1;
                        }
                    } else if (matchPeriod.equals(BasketballMatchPeriod.IN_SECOND_QUARTER)) {
                        if (teamId.equals(TeamId.A)) {
                            secondQuarterPointsA += 1;
                        } else if (teamId.equals(TeamId.B)) {
                            secondQuarterPointsB += 1;
                        }
                    } else if (matchPeriod.equals(BasketballMatchPeriod.IN_THIRD_QUARTER)) {
                        if (teamId.equals(TeamId.A)) {
                            thirdQuarterPointsA += 1;
                        } else if (teamId.equals(TeamId.B)) {
                            thirdQuarterPointsB += 1;
                        }
                    } else if (matchPeriod.equals(BasketballMatchPeriod.IN_FOURTH_QUARTER)) {
                        if (teamId.equals(TeamId.A)) {
                            fourthQuarterPointsA += 1;
                        } else if (teamId.equals(TeamId.B)) {
                            fourthQuarterPointsB += 1;
                        }
                    } else if (!matchPeriod.equals(BasketballMatchPeriod.IN_FIRST_HALF)) {
                        if (teamId.equals(TeamId.A)) {
                            firstHalfPointsA += 1;
                        } else if (teamId.equals(TeamId.B)) {
                            firstHalfPointsB += 1;
                        }
                    } else if (!matchPeriod.equals(BasketballMatchPeriod.IN_SECOND_HALF)) {
                        if (teamId.equals(TeamId.A)) {
                            secondHalfPointsA += 1;
                        } else if (teamId.equals(TeamId.B)) {
                            secondHalfPointsB += 1;
                        }
                    } else if (!matchPeriod.equals(BasketballMatchPeriod.IN_EXTRA_TIME)) {
                        if (teamId.equals(TeamId.A)) {
                            overTimeQuarterPointsA += 1;
                        } else if (teamId.equals(TeamId.B)) {
                            overTimeQuarterPointsB += 1;
                        }
                    }
                    switch (teamId) {
                        case A:
                            if (!matchPeriod.equals(BasketballMatchPeriod.IN_EXTRA_TIME))
                                normalTimeGoalsA += 1;
                            pointsA += 1;
                            currentPeriodGoalsA += 1;
                            break;
                        case B:
                            if (!matchPeriod.equals(BasketballMatchPeriod.IN_EXTRA_TIME))
                                normalTimeGoalsB += 1;
                            pointsB += 1;
                            currentPeriodGoalsB += 1;
                            break;
                        case UNKNOWN:
                            throw new IllegalArgumentException("TeamId must be specified for goal scored");
                    }
                    if (freeThrowStatus[2].equals(freeThrowStatus[1])) {
                        initialiseFreeThrow(freeThrowStatus);
                    }
                    // // FIXME : Ball possession deleted for speed concerns
                    // if (Integer.parseInt(freeThrowStatus[1]) - Integer.parseInt(freeThrowStatus[2]) == 0)
                    // ballPosition.resetBallPosition();
                    break;

                case FIRST_FREETHROW_MISSED:
                case SECOND_FREETHROW_MISSED:
                case THIRD_FREETHROW_MISSED:
                    // if (!freeThrowStatus[2].equals("2"))
                    // throw new IllegalArgumentException("This IS NOT the THIRD free throw");
                    freeThrowStatus[2] = Integer.toString((Integer.parseInt(freeThrowStatus[2])) + 1);
                    if (freeThrowStatus[2].equals(freeThrowStatus[1])) {
                        initialiseFreeThrow(freeThrowStatus);
                    }

                    // // FIXME : Ball possession deleted for speed concerns
                    // if (Integer.parseInt(freeThrowStatus[1]) - Integer.parseInt(freeThrowStatus[2]) == 0)
                    // ballPosition.resetBallPosition();

                    break;
                case ONE_FREETHROW_STARTED:
                    freeThrowStatus[1] = "1"; // free throw chances given
                    freeThrowStatus[2] = "0"; // free throw scored so far
                    switch (teamId) {
                        case A:
                            freeThrowStatus[0] = "H";
                            break;
                        case B:
                            freeThrowStatus[0] = "A";
                            break;
                        case UNKNOWN:
                            throw new IllegalArgumentException("TeamId must be specified for FREE THROW");
                    }
                    break;

                case TWO_FREETHROW_STARTED:
                    freeThrowStatus[1] = "2";
                    freeThrowStatus[2] = "0"; // free throw scored so far
                    switch (teamId) {
                        case A:
                            freeThrowStatus[0] = "H";
                            break;
                        case B:
                            freeThrowStatus[0] = "A";
                            break;
                        case UNKNOWN:
                            throw new IllegalArgumentException("TeamId must be specified for FREE THROW");
                    }
                    break;

                case THREE_FREETHROW_STARTED:
                    freeThrowStatus[1] = "3";
                    freeThrowStatus[2] = "0"; // free throw scored so far
                    switch (teamId) {
                        case A:
                            freeThrowStatus[0] = "H";
                            break;
                        case B:
                            freeThrowStatus[0] = "A";
                            break;
                        case UNKNOWN:
                            throw new IllegalArgumentException("TeamId must be specified for FREE THROW");
                    }
                    break;

                case THREE_POINTS_SCORED:
                    elapsedTimeAtLastGoalSecs = elapsedTimeSecs;
                    matchPeriodInWhichLastGoalScored = matchPeriod;
                    // FIXME : Ball possession deleted for speed concerns
                    // ballPosition.resetBallPosition();
                    if (matchPeriod.equals(BasketballMatchPeriod.IN_FIRST_QUARTER)) {
                        if (teamId.equals(TeamId.A)) {
                            firstQuarterPointsA += 3;
                        } else if (teamId.equals(TeamId.B)) {
                            firstQuarterPointsB += 3;
                        }
                    } else if (matchPeriod.equals(BasketballMatchPeriod.IN_SECOND_QUARTER)) {
                        if (teamId.equals(TeamId.A)) {
                            secondQuarterPointsA += 3;
                        } else if (teamId.equals(TeamId.B)) {
                            secondQuarterPointsB += 3;
                        }
                    } else if (matchPeriod.equals(BasketballMatchPeriod.IN_THIRD_QUARTER)) {
                        if (teamId.equals(TeamId.A)) {
                            thirdQuarterPointsA += 3;
                        } else if (teamId.equals(TeamId.B)) {
                            thirdQuarterPointsB += 3;
                        }
                    } else if (matchPeriod.equals(BasketballMatchPeriod.IN_FOURTH_QUARTER)) {
                        if (teamId.equals(TeamId.A)) {
                            fourthQuarterPointsA += 3;
                        } else if (teamId.equals(TeamId.B)) {
                            fourthQuarterPointsB += 3;
                        }
                    } else if (!matchPeriod.equals(BasketballMatchPeriod.IN_FIRST_HALF)) {
                        if (teamId.equals(TeamId.A)) {
                            firstHalfPointsA += 3;
                        } else if (teamId.equals(TeamId.B)) {
                            firstHalfPointsB += 3;
                        }
                    } else if (!matchPeriod.equals(BasketballMatchPeriod.IN_SECOND_HALF)) {
                        if (teamId.equals(TeamId.A)) {
                            secondHalfPointsA += 3;
                        } else if (teamId.equals(TeamId.B)) {
                            secondHalfPointsB += 3;
                        }
                    } else if (!matchPeriod.equals(BasketballMatchPeriod.IN_EXTRA_TIME)) {
                        if (teamId.equals(TeamId.A)) {
                            overTimeQuarterPointsA += 3;
                        } else if (teamId.equals(TeamId.B)) {
                            overTimeQuarterPointsB += 3;
                        }
                    }
                    switch (teamId) {
                        case A:
                            if (!matchPeriod.equals(BasketballMatchPeriod.IN_EXTRA_TIME))
                                normalTimeGoalsA += 3;

                            pointsA += 3;
                            currentPeriodGoalsA += 3;

                            break;
                        case B:

                            if (!matchPeriod.equals(BasketballMatchPeriod.IN_EXTRA_TIME))
                                normalTimeGoalsB += 3;

                            pointsB += 3;
                            currentPeriodGoalsB += 3;

                            break;

                        case UNKNOWN:
                            throw new IllegalArgumentException("TeamId must be specified for goal scored");
                    }
                    break;
                case TWO_POINTS_SCORED:
                    elapsedTimeAtLastGoalSecs = elapsedTimeSecs;
                    matchPeriodInWhichLastGoalScored = matchPeriod;
                    // FIXME : Ball possession deleted for speed concerns
                    // ballPosition.resetBallPosition();
                    if (matchPeriod.equals(BasketballMatchPeriod.IN_FIRST_QUARTER)) {
                        if (teamId.equals(TeamId.A)) {
                            firstQuarterPointsA += 2;
                        } else if (teamId.equals(TeamId.B)) {
                            firstQuarterPointsB += 2;
                        }
                    } else if (matchPeriod.equals(BasketballMatchPeriod.IN_SECOND_QUARTER)) {
                        if (teamId.equals(TeamId.A)) {
                            secondQuarterPointsA += 2;
                        } else if (teamId.equals(TeamId.B)) {
                            secondQuarterPointsB += 2;
                        }
                    } else if (matchPeriod.equals(BasketballMatchPeriod.IN_THIRD_QUARTER)) {
                        if (teamId.equals(TeamId.A)) {
                            thirdQuarterPointsA += 2;
                        } else if (teamId.equals(TeamId.B)) {
                            thirdQuarterPointsB += 2;
                        }
                    } else if (matchPeriod.equals(BasketballMatchPeriod.IN_FOURTH_QUARTER)) {
                        if (teamId.equals(TeamId.A)) {
                            fourthQuarterPointsA += 2;
                        } else if (teamId.equals(TeamId.B)) {
                            fourthQuarterPointsB += 2;
                        }
                    } else if (!matchPeriod.equals(BasketballMatchPeriod.IN_FIRST_HALF)) {
                        if (teamId.equals(TeamId.A)) {
                            firstHalfPointsA += 2;
                        } else if (teamId.equals(TeamId.B)) {
                            firstHalfPointsB += 2;
                        }
                    } else if (!matchPeriod.equals(BasketballMatchPeriod.IN_SECOND_HALF)) {
                        if (teamId.equals(TeamId.A)) {
                            secondHalfPointsA += 2;
                        } else if (teamId.equals(TeamId.B)) {
                            secondHalfPointsB += 2;
                        }
                    } else if (!matchPeriod.equals(BasketballMatchPeriod.IN_EXTRA_TIME)) {
                        if (teamId.equals(TeamId.A)) {
                            overTimeQuarterPointsA += 2;
                        } else if (teamId.equals(TeamId.B)) {
                            overTimeQuarterPointsB += 2;
                        }
                    }
                    switch (teamId) {
                        case A:
                            if (!matchPeriod.equals(BasketballMatchPeriod.IN_EXTRA_TIME))
                                normalTimeGoalsA += 2;

                            pointsA += 2;
                            currentPeriodGoalsA += 2;

                            break;
                        case B:

                            if (!matchPeriod.equals(BasketballMatchPeriod.IN_EXTRA_TIME))
                                normalTimeGoalsB += 2;

                            pointsB += 2;
                            currentPeriodGoalsB += 2;

                            break;

                        case UNKNOWN:
                            throw new IllegalArgumentException("TeamId must be specified for goal scored");
                    }

                    break;

                case FIELD_INFO_SETTING:
                    this.ballPosition.setFieldPositionType(
                                    ((BasketballMatchIncident) matchIncident).getFieldPositionType());
                    this.ballPosition.setBallHoldingTeam(((BasketballMatchIncident) matchIncident).getTeamId());
                    break;

                case POINT_SCORED:
                    // FIXME : Ball possession deleted for speed concerns
                    // ballPosition.resetBallPosition();

                    if (matchPeriod.equals(BasketballMatchPeriod.IN_FIRST_QUARTER)) {
                        if (teamId.equals(TeamId.A)) {
                            firstQuarterPointsA += 1;
                        } else if (teamId.equals(TeamId.B)) {
                            firstQuarterPointsB += 1;
                        }
                    } else if (matchPeriod.equals(BasketballMatchPeriod.IN_SECOND_QUARTER)) {
                        if (teamId.equals(TeamId.A)) {
                            secondQuarterPointsA += 1;
                        } else if (teamId.equals(TeamId.B)) {
                            secondQuarterPointsB += 1;
                        }
                    } else if (matchPeriod.equals(BasketballMatchPeriod.IN_THIRD_QUARTER)) {
                        if (teamId.equals(TeamId.A)) {
                            thirdQuarterPointsA += 1;
                        } else if (teamId.equals(TeamId.B)) {
                            thirdQuarterPointsB += 1;
                        }
                    } else if (matchPeriod.equals(BasketballMatchPeriod.IN_FOURTH_QUARTER)) {
                        if (teamId.equals(TeamId.A)) {
                            fourthQuarterPointsA += 1;
                        } else if (teamId.equals(TeamId.B)) {
                            fourthQuarterPointsB += 1;
                        }
                    } else if (!matchPeriod.equals(BasketballMatchPeriod.IN_FIRST_HALF)) {
                        if (teamId.equals(TeamId.A)) {
                            firstHalfPointsA += 1;
                        } else if (teamId.equals(TeamId.B)) {
                            firstHalfPointsB += 1;
                        }
                    } else if (!matchPeriod.equals(BasketballMatchPeriod.IN_SECOND_HALF)) {
                        if (teamId.equals(TeamId.A)) {
                            secondHalfPointsA += 1;
                        } else if (teamId.equals(TeamId.B)) {
                            secondHalfPointsB += 1;
                        }
                    } else if (!matchPeriod.equals(BasketballMatchPeriod.IN_EXTRA_TIME)) {
                        if (teamId.equals(TeamId.A)) {
                            overTimeQuarterPointsA += 1;
                        } else if (teamId.equals(TeamId.B)) {
                            overTimeQuarterPointsB += 1;
                        }
                    }

                    switch (teamId) {
                        case A:
                            if (!matchPeriod.equals(BasketballMatchPeriod.IN_EXTRA_TIME))
                                normalTimeGoalsA += 1;

                            pointsA += 1;
                            currentPeriodGoalsA += 1;

                            break;
                        case B:

                            if (!matchPeriod.equals(BasketballMatchPeriod.IN_EXTRA_TIME))
                                normalTimeGoalsB += 1;

                            pointsB += 1;
                            currentPeriodGoalsB += 1;

                            break;

                        case UNKNOWN:
                            throw new IllegalArgumentException("TeamId must be specified for goal scored");
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Unknown match incidences type");
            }
        } else// must be ElapsedTimeMatchIncident
        {
            this.setClockStatus(((ElapsedTimeMatchIncident) matchIncident).getIncidentSubType());

            switch (((ElapsedTimeMatchIncident) matchIncident).getIncidentSubType()) {

                case SET_MATCH_CLOCK:
                    /*
                     * Do Nothing
                     */
                    break;

                case SET_PERIOD_START:
                    switch (matchPeriod) {
                        case PREMATCH:
                            elapsedTimeThisPeriodSecs = 0;
                            if (!twoHalvesFormat) {
                                matchPeriod = BasketballMatchPeriod.IN_FIRST_QUARTER;
                            } else {
                                matchPeriod = BasketballMatchPeriod.IN_FIRST_HALF;
                            }
                            break;
                        /*
                         * 
                         * two halves type
                         * 
                         */

                        case IN_FIRST_HALF:
                            break;
                        case AT_HALF_TIME:
                            elapsedTimeThisPeriodSecs = 0;
                            matchPeriod = BasketballMatchPeriod.IN_SECOND_HALF;
                            break;
                        case IN_SECOND_HALF:
                            break;

                        /*
                         * 
                         * four periods type
                         * 
                         */

                        case IN_FIRST_QUARTER:
                            break;

                        case AT_FIRST_QUARTER_END:
                            elapsedTimeThisPeriodSecs = 0;
                            matchPeriod = BasketballMatchPeriod.IN_SECOND_QUARTER;
                            break;

                        case IN_SECOND_QUARTER:
                            break;

                        case AT_SECOND_QUARTER_END:
                            elapsedTimeThisPeriodSecs = 0;
                            matchPeriod = BasketballMatchPeriod.IN_THIRD_QUARTER;
                            break;

                        case IN_THIRD_QUARTER:
                            break;

                        case AT_THIRD_QUARTER_END:
                            elapsedTimeThisPeriodSecs = 0;
                            matchPeriod = BasketballMatchPeriod.IN_FOURTH_QUARTER;
                            break;

                        case IN_FOURTH_QUARTER:
                            break;

                        case AT_FULL_TIME:
                            overtimeNo++;
                            elapsedTimeThisPeriodSecs = 0;
                            matchPeriod = BasketballMatchPeriod.IN_EXTRA_TIME;
                            break;

                        case IN_EXTRA_TIME:
                            break;

                        case AT_EXTRA_PERIOD_END:
                            overtimeNo++;
                            elapsedTimeThisPeriodSecs = 0;
                            if (pointsA == pointsB) {
                                matchPeriod = BasketballMatchPeriod.IN_EXTRA_TIME;
                                break;
                            } else {
                                matchPeriod = BasketballMatchPeriod.MATCH_COMPLETED;
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
                        case IN_FIRST_QUARTER:
                            matchPeriod = BasketballMatchPeriod.AT_FIRST_QUARTER_END;
                            previousPeriodGoalsA = currentPeriodGoalsA;
                            previousPeriodGoalsB = currentPeriodGoalsB;
                            currentPeriodGoalsA = 0;
                            currentPeriodGoalsB = 0;
                            setElapsedTime(normalPeriodSecs);
                            elapsedTimeAtLastMatchIncidentSecs = normalPeriodSecs;
                            setClockTimeOfLastElapsedTimeFromIncident();
                            break;
                        case IN_FIRST_HALF:
                            matchPeriod = BasketballMatchPeriod.AT_HALF_TIME;
                            previousPeriodGoalsA = currentPeriodGoalsA;
                            previousPeriodGoalsB = currentPeriodGoalsB;
                            currentPeriodGoalsA = 0;
                            currentPeriodGoalsB = 0;
                            setElapsedTime(normalPeriodSecs);
                            elapsedTimeAtLastMatchIncidentSecs = normalPeriodSecs;
                            setClockTimeOfLastElapsedTimeFromIncident();
                            break;

                        case AT_HALF_TIME:
                        case AT_FIRST_QUARTER_END:
                            break;

                        case IN_SECOND_QUARTER:
                            matchPeriod = BasketballMatchPeriod.AT_SECOND_QUARTER_END;
                            previousPeriodGoalsA = currentPeriodGoalsA;
                            previousPeriodGoalsB = currentPeriodGoalsB;
                            currentPeriodGoalsA = 0;
                            currentPeriodGoalsB = 0;
                            setElapsedTime(normalPeriodSecs * 2);
                            elapsedTimeAtLastMatchIncidentSecs = normalPeriodSecs * 2;
                            setClockTimeOfLastElapsedTimeFromIncident();
                            break;

                        case AT_SECOND_QUARTER_END:
                            break;

                        case IN_THIRD_QUARTER:
                            matchPeriod = BasketballMatchPeriod.AT_THIRD_QUARTER_END;
                            previousPeriodGoalsA = currentPeriodGoalsA;
                            previousPeriodGoalsB = currentPeriodGoalsB;
                            currentPeriodGoalsA = 0;
                            currentPeriodGoalsB = 0;
                            setElapsedTime(normalPeriodSecs * 3);
                            elapsedTimeAtLastMatchIncidentSecs = normalPeriodSecs * 3;
                            setClockTimeOfLastElapsedTimeFromIncident();
                            break;

                        case AT_THIRD_QUARTER_END:
                            break;

                        case IN_FOURTH_QUARTER:
                            if (pointsA != pointsB)
                                matchPeriod = BasketballMatchPeriod.MATCH_COMPLETED;
                            else
                                matchPeriod = BasketballMatchPeriod.AT_FULL_TIME;
                            previousPeriodGoalsA = currentPeriodGoalsA;
                            previousPeriodGoalsB = currentPeriodGoalsB;
                            currentPeriodGoalsA = 0;
                            currentPeriodGoalsB = 0;
                            setElapsedTime(normalPeriodSecs * 4);
                            elapsedTimeAtLastMatchIncidentSecs = normalPeriodSecs;
                            setClockTimeOfLastElapsedTimeFromIncident();
                            break;

                        case IN_SECOND_HALF:
                            if (pointsA != pointsB)
                                matchPeriod = BasketballMatchPeriod.MATCH_COMPLETED;
                            else
                                matchPeriod = BasketballMatchPeriod.AT_FULL_TIME;
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

                        case IN_EXTRA_TIME:
                            if (pointsA == pointsB) {
                                matchPeriod = BasketballMatchPeriod.AT_EXTRA_PERIOD_END;
                            } else {
                                matchPeriod = BasketballMatchPeriod.MATCH_COMPLETED;
                            }
                            previousPeriodGoalsA = currentPeriodGoalsA;
                            previousPeriodGoalsB = currentPeriodGoalsB;
                            currentPeriodGoalsA = 0;
                            currentPeriodGoalsB = 0;

                            if (!twoHalvesFormat) {
                                setElapsedTime(normalPeriodSecs * 4 + (overtimeNo) * extraPeriodSecs);
                            } else {
                                setElapsedTime(normalPeriodSecs * 2 + (overtimeNo) * extraPeriodSecs);
                            }

                            if (overtimeNo == 0) {
                                elapsedTimeAtLastMatchIncidentSecs = normalPeriodSecs;
                            } else {
                                elapsedTimeAtLastMatchIncidentSecs = extraPeriodSecs;
                            }

                            setClockTimeOfLastElapsedTimeFromIncident();
                            break;

                        case AT_EXTRA_PERIOD_END:
                            break;

                        case MATCH_COMPLETED:
                            break;
                        default:
                            throw new IllegalArgumentException("SET_PERIOD_END Unknown");
                    }

                case SET_STOP_MATCH_CLOCK: // stop the independent match clock timer - for those sports where this is
                                           // possible

                    this.setClockStatus(ElapsedTimeMatchIncidentType.SET_STOP_MATCH_CLOCK);
                    break;
                case SET_START_MATCH_CLOCK: // start the match clock timer
                    this.setClockStatus(ElapsedTimeMatchIncidentType.SET_START_MATCH_CLOCK);
                    break;

            }
        }
        return matchPeriod;
    }

    private void setElapsedTime(int elapsedTimeSecs) {
        this.elapsedTimeSecs = elapsedTimeSecs;
        switch (matchPeriod) {
            case PREMATCH:
            case AT_FIRST_QUARTER_END:
            case AT_SECOND_QUARTER_END:
            case AT_THIRD_QUARTER_END:

            case AT_FULL_TIME:
            case AT_EXTRA_PERIOD_END:
            case MATCH_COMPLETED:
            case AT_HALF_TIME:
                /*
                 * do nothing
                 */
                break;
            case IN_FIRST_HALF:
            case IN_FIRST_QUARTER:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs;
                break;
            case IN_SECOND_HALF:
            case IN_SECOND_QUARTER:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs - normalPeriodSecs;
                break;
            case IN_THIRD_QUARTER:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs - 2 * normalPeriodSecs;
                break;
            case IN_FOURTH_QUARTER:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs - 3 * normalPeriodSecs;
                break;
            case IN_EXTRA_TIME:
                if (!twoHalvesFormat) {
                    elapsedTimeThisPeriodSecs =
                                    elapsedTimeSecs - 4 * normalPeriodSecs - extraPeriodSecs * (overtimeNo - 1);
                } else {
                    elapsedTimeThisPeriodSecs =
                                    elapsedTimeSecs - 2 * normalPeriodSecs - extraPeriodSecs * (overtimeNo - 1);
                }

                break;
            default:
                throw new IllegalArgumentException("setElapsedTime");
        }
    }

    @Override
    public MatchState copy() {
        BasketballMatchState cc = new BasketballMatchState(matchFormat);
        cc.setEqualTo(this);
        return cc;

    }

    @Override
    public void setEqualTo(MatchState matchState) {
        super.setEqualTo(matchState);
        /* point board use */
        this.setFirstHalfPointsA(((BasketballMatchState) matchState).getFirstHalfPointsA());
        this.setFirstQuarterPointsA(((BasketballMatchState) matchState).getFirstQuarterPointsA());
        this.setSecondHalfPointsA(((BasketballMatchState) matchState).getSecondHalfPointsA());
        this.setSecondQuarterPointsA(((BasketballMatchState) matchState).getSecondHalfPointsA());
        this.setThirdQuarterPointsA(((BasketballMatchState) matchState).getThirdQuarterPointsA());
        this.setFourthQuarterPointsA(((BasketballMatchState) matchState).getFourthQuarterPointsA());
        this.setOverTimeQuarterPointsA(((BasketballMatchState) matchState).getOverTimeQuarterPointsA());

        this.setFirstHalfPointsB(((BasketballMatchState) matchState).getFirstHalfPointsB());
        this.setFirstQuarterPointsB(((BasketballMatchState) matchState).getFirstQuarterPointsB());
        this.setSecondHalfPointsB(((BasketballMatchState) matchState).getSecondHalfPointsB());
        this.setSecondQuarterPointsB(((BasketballMatchState) matchState).getSecondHalfPointsB());
        this.setThirdQuarterPointsB(((BasketballMatchState) matchState).getThirdQuarterPointsB());
        this.setFourthQuarterPointsB(((BasketballMatchState) matchState).getFourthQuarterPointsB());
        this.setOverTimeQuarterPointsB(((BasketballMatchState) matchState).getOverTimeQuarterPointsB());

        this.setPointsA(((BasketballMatchState) matchState).getPointsA());
        this.setPointsB(((BasketballMatchState) matchState).getPointsB());

        // this.setElapsedTimeSecs(((BasketballMatchState) matchState).getElapsedTimeSecs(),
        // ((BasketballMatchState) matchState).getOvertimeNo());
        this.setElapsedTimeSecs(((BasketballMatchState) matchState).getElapsedTimeSecs());
        this.setMatchPeriod(((BasketballMatchState) matchState).getMatchPeriod());
        this.setOvertimeNo(((BasketballMatchState) matchState).getOvertimeNo());
        this.setCurrentPeriodGoalsA(((BasketballMatchState) matchState).getCurrentPeriodGoalsA());
        this.setCurrentPeriodGoalsB(((BasketballMatchState) matchState).getCurrentPeriodGoalsB());
        this.setOvertimeNo(((BasketballMatchState) matchState).getOvertimeNo());
        this.setNormalTimeGoalsA(((BasketballMatchState) matchState).getNormalTimeGoalsA());
        this.setNormalTimeGoalsB(((BasketballMatchState) matchState).getNormalTimeGoalsB());
        this.setTwoHalvesFormat(((BasketballMatchState) matchState).isTwoHalvesFormat());
        if (((BasketballMatchState) matchState).getFreeThrowStatus() == null) {
            this.setFreeThrowStatus(null); // can delete this in future
        } else {
            String[] temp_freethrowstatus = Arrays.copyOf(((BasketballMatchState) matchState).getFreeThrowStatus(), 3);
            this.setFreeThrowStatus(temp_freethrowstatus);
        }
        this.setFourtyMinutiesMatch(((BasketballMatchState) matchState).isFourtyMinutiesMatch());
        this.setBallPosition(ballPosition);

        try {
            this.ballPosition = (BallPosition) (((BasketballMatchState) matchState).ballPosition).clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    @JsonIgnore
    @Override
    public MatchIncidentPrompt getNextPrompt() {
        MatchIncidentPrompt matchIncidentPrompt = null;
        if (matchPeriod == BasketballMatchPeriod.MATCH_COMPLETED)
            matchIncidentPrompt = new MatchIncidentPrompt("Match finished");
        else if (matchPeriod == BasketballMatchPeriod.PREMATCH
                        || matchPeriod == BasketballMatchPeriod.AT_FIRST_QUARTER_END
                        || matchPeriod == BasketballMatchPeriod.AT_SECOND_QUARTER_END
                        || matchPeriod == BasketballMatchPeriod.AT_THIRD_QUARTER_END
                        || matchPeriod == BasketballMatchPeriod.AT_FULL_TIME
                        || matchPeriod == BasketballMatchPeriod.AT_EXTRA_PERIOD_END
                        || matchPeriod == BasketballMatchPeriod.AT_HALF_TIME)
            matchIncidentPrompt = new MatchIncidentPrompt("S - start match", "S");

        else {
            if (freeThrowStatus[0].equals("UNKNOWN")) {
                matchIncidentPrompt = new MatchIncidentPrompt(
                                "Next event (Nnn-Nothing happened for nn periods, H2/3-Home goal, A2/3-Away goal, A-Away goal, FH123/A123-Home/Away Free throw, "
                                                + "M/A-Home/Away Free throw missed,"
                                                + "PA/H-Home/Away Free throw scored," + ")",
                                "N");
            } else {
                matchIncidentPrompt =
                                new MatchIncidentPrompt("M Free throw missed," + "I Free throw scored," + ")", "I");

            }

        }
        return matchIncidentPrompt;
    }

    @Override
    public MatchIncident getMatchIncident(String response) {

        BasketballMatchIncidentType basketballMatchIncidentType = null;
        ElapsedTimeMatchIncidentType elapsedTimeMatchIncidentType = null;
        FieldPositionType fieldPositionType = FieldPositionType.UNKNOWN;
        TeamId teamId = null;
        int incidentElapsedTimeSecs = elapsedTimeSecs;
        char c = response.toUpperCase().charAt(0);
        String ballHoldingTeam = "";
        String fieldSide = "";
        String fieldPosition = "";
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

            case 'F':
                String freeThrowTeam = (response.toUpperCase().substring(1, 2));
                int freeThrowNumber = Integer.parseInt(response.substring(2, 3));
                if (freeThrowNumber == 1) {
                    basketballMatchIncidentType = BasketballMatchIncidentType.ONE_FREETHROW_STARTED;
                } else if (freeThrowNumber == 2) {
                    basketballMatchIncidentType = BasketballMatchIncidentType.TWO_FREETHROW_STARTED;
                } else if (freeThrowNumber == 3) {
                    basketballMatchIncidentType = BasketballMatchIncidentType.THREE_FREETHROW_STARTED;
                } else {
                    throw new IllegalArgumentException("UNKNOWN FREE THROW NUMBER");
                }

                if (freeThrowTeam.equals("H")) {
                    teamId = TeamId.A;
                } else if (freeThrowTeam.equals("A")) {
                    teamId = TeamId.B;
                } else {
                    throw new IllegalArgumentException("UNKNOWN FREE THROW TEAM");
                }
                break;

            case 'M':
                freeThrowTeam = freeThrowStatus[0];
                int noOfFreeThrow = Integer.parseInt(freeThrowStatus[2]);

                switch (noOfFreeThrow) {
                    case 0:
                        basketballMatchIncidentType = BasketballMatchIncidentType.FIRST_FREETHROW_MISSED;
                        break;
                    case 1:
                        basketballMatchIncidentType = BasketballMatchIncidentType.SECOND_FREETHROW_MISSED;
                        break;
                    case 2:
                        basketballMatchIncidentType = BasketballMatchIncidentType.THIRD_FREETHROW_MISSED;
                        break;
                }
                break;

            case 'B':
                ballHoldingTeam = (response.toUpperCase().substring(1, 2));
                fieldSide = (response.toUpperCase().substring(2, 3));
                fieldPosition = (response.toUpperCase().substring(3, 4));
                basketballMatchIncidentType = BasketballMatchIncidentType.FIELD_INFO_SETTING;

                if (ballHoldingTeam.equals("H"))
                    teamId = TeamId.A;
                else if (ballHoldingTeam.equals("A"))
                    teamId = TeamId.B;

                if (fieldSide.equals("H")) {
                    switch (fieldPosition) {
                        case "1":
                            fieldPositionType = FieldPositionType.HOME_THSLINE; // 3secs line
                            break;
                        case "2":
                            fieldPositionType = FieldPositionType.HOME_TWPLINE; // 3secs line
                            break;
                        case "3":
                            fieldPositionType = FieldPositionType.HOME_THPLINE; // 3secs line
                            break;
                    }
                }
                if (fieldSide.equals("A")) {
                    switch (fieldPosition) {
                        case "1":
                            fieldPositionType = FieldPositionType.AWAY_THSLINE; // 3secs line
                            break;
                        case "2":
                            fieldPositionType = FieldPositionType.AWAY_TWPLINE; // 3secs line
                            break;
                        case "3":
                            fieldPositionType = FieldPositionType.AWAY_THPLINE; // 3secs line
                            break;
                    }

                }

                break;

            case 'I':
                freeThrowTeam = freeThrowStatus[0];
                noOfFreeThrow = Integer.parseInt(freeThrowStatus[2]);
                if (freeThrowTeam.equals("H")) {
                    teamId = TeamId.A;
                } else if (freeThrowTeam.equals("A")) {
                    teamId = TeamId.B;
                } else {
                    throw new IllegalArgumentException("Team ID not avilable, or free throw should not happening");
                }
                switch (noOfFreeThrow) {
                    case 0:
                        basketballMatchIncidentType = BasketballMatchIncidentType.FIRST_FREETHROW_SCORED;
                        break;
                    case 1:
                        basketballMatchIncidentType = BasketballMatchIncidentType.SECOND_FREETHROW_SCORED;
                        break;
                    case 2:
                        basketballMatchIncidentType = BasketballMatchIncidentType.THIRD_FREETHROW_SCORED;
                        break;
                }
                break;

            case 'H':
                int points = Integer.parseInt(response.substring(1, 2));
                if (points == 2) {
                    basketballMatchIncidentType = BasketballMatchIncidentType.TWO_POINTS_SCORED;
                } else if (points == 3) {
                    basketballMatchIncidentType = BasketballMatchIncidentType.THREE_POINTS_SCORED;
                } else {
                    throw new IllegalArgumentException("UNKNOWN POINTS SCORED");
                }
                teamId = TeamId.A;
                break;
            case 'A':
                points = Integer.parseInt(response.substring(1, 2));
                if (points == 2) {
                    basketballMatchIncidentType = BasketballMatchIncidentType.TWO_POINTS_SCORED;
                } else if (points == 3) {
                    basketballMatchIncidentType = BasketballMatchIncidentType.THREE_POINTS_SCORED;
                } else {
                    throw new IllegalArgumentException("UNKNOWN POINTS SCORED");
                }
                teamId = TeamId.B;
                break;

            case 'S':
                switch (matchPeriod) {
                    case PREMATCH:
                    case AT_FIRST_QUARTER_END:
                    case AT_SECOND_QUARTER_END:
                    case AT_THIRD_QUARTER_END:
                    case AT_FULL_TIME:
                    case AT_HALF_TIME:
                    case IN_EXTRA_TIME:
                    case AT_EXTRA_PERIOD_END:
                        elapsedTimeMatchIncidentType = ElapsedTimeMatchIncidentType.SET_PERIOD_START;
                        break;
                    default:
                        return null;
                }
                break;
            case 'E':
                switch (matchPeriod) {
                    case IN_FIRST_QUARTER:
                    case IN_FIRST_HALF:
                    case IN_SECOND_QUARTER:
                    case IN_SECOND_HALF:
                    case IN_THIRD_QUARTER:
                    case IN_FOURTH_QUARTER:
                    case IN_EXTRA_TIME:
                        elapsedTimeMatchIncidentType = ElapsedTimeMatchIncidentType.SET_PERIOD_END;
                        break;
                    default:
                        return null; // invalid input so return null
                }
        }
        MatchIncident incident;
        if (basketballMatchIncidentType == BasketballMatchIncidentType.FIELD_INFO_SETTING) {
            incident = new BasketballMatchIncident(basketballMatchIncidentType, incidentElapsedTimeSecs, teamId,
                            fieldPositionType);
        } else if (basketballMatchIncidentType != null) {
            incident = new BasketballMatchIncident(basketballMatchIncidentType, incidentElapsedTimeSecs, teamId);
        } else {
            incident = new ElapsedTimeMatchIncident(elapsedTimeMatchIncidentType, incidentElapsedTimeSecs);
        }
        return incident;
    }

    private static final String goalsKey = "Points";
    private static final String elapsedTimeKey = "Elapsed time";
    // private static final String injuryTimeKey = "Injury time to be played";
    private static final String matchPeriodKey = "Match period";
    @JsonIgnore
    private static final String periodSequenceKey = "Period sequence id";
    @JsonIgnore
    private static final String goalSequenceKey = "Point sequence id";
    @JsonIgnore
    private static final String overTimeSequenceKey = "Over time sequence id";
    @JsonIgnore
    private static final String freeThrowStatusKey = "Free throw status";
    @JsonIgnore
    private static final String ballPossessionKey = "Ball possession";
    @JsonIgnore
    private static final String fieldPositionKey = "Field Position";
    @JsonIgnore
    private static final String quarter1PointsKey = "1st Quarter Points";
    @JsonIgnore
    private static final String quarter2PointsKey = "2nd Quarter Points";
    @JsonIgnore
    private static final String quarter3PointsKey = "3rd Quarter Points";
    @JsonIgnore
    private static final String quarter4PointsKey = "4th Quarter Points";
    @JsonIgnore
    private static final String quarterEPointsKey = "Overtime Quarter Points";

    @Override
    public LinkedHashMap<String, String> getAsMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put(goalsKey, String.format("%d-%d", pointsA, pointsB));
        int mins = elapsedTimeSecs / 60;
        int secs = elapsedTimeSecs - mins * 60;
        map.put(elapsedTimeKey, String.format("%d:%02d", mins, secs));
        map.put(matchPeriodKey, matchPeriod.toString());
        map.put(ballPossessionKey, ballPosition.getBallHoldingTeam().toString());
        map.put(fieldPositionKey, ballPosition.getFieldPositionType().toString());
        map.put(freeThrowStatusKey,
                        String.format("%s-%s-%s", freeThrowStatus[0], freeThrowStatus[1], freeThrowStatus[2]));

        map.put(overTimeSequenceKey, Integer.toString(overtimeNo));
        map.put(quarter1PointsKey, String.format("%d-%d", firstQuarterPointsA, firstQuarterPointsB));
        map.put(quarter2PointsKey, String.format("%d-%d", secondQuarterPointsA, secondQuarterPointsB));
        map.put(quarter3PointsKey, String.format("%d-%d", thirdQuarterPointsA, thirdQuarterPointsB));
        map.put(quarter4PointsKey, String.format("%d-%d", fourthQuarterPointsA, fourthQuarterPointsB));
        map.put(quarterEPointsKey, String.format("%d-%d", overTimeQuarterPointsA, overTimeQuarterPointsB));
        return map;
    }

    @Override
    public String setFromMap(Map<String, String> map) {
        /*
         * do nothing - not allowing the user to manually update the score
         */
        return null;
    }

    @JsonIgnore
    @Override
    public boolean isMatchCompleted() {
        return (matchPeriod == BasketballMatchPeriod.MATCH_COMPLETED);
    }

    /**
     * Returns true if at the end of normal time
     * 
     * @return
     */
    @JsonIgnore
    public boolean isNormalTimeMatchCompleted() {
        if (twoHalvesFormat) {
            return (elapsedTimeSecs >= normalPeriodSecs * 2);
        } else {
            return (elapsedTimeSecs >= normalPeriodSecs * 4);
        }
    }

    /**
     * Returns true if at the end of one period and before the next one (if any) starts
     * 
     * @return
     */
    @JsonIgnore
    public boolean isPeriodCompleted() {
        return (matchPeriod == BasketballMatchPeriod.AT_FIRST_QUARTER_END
                        || matchPeriod == BasketballMatchPeriod.AT_SECOND_QUARTER_END
                        || matchPeriod == BasketballMatchPeriod.AT_THIRD_QUARTER_END
                        || matchPeriod == BasketballMatchPeriod.AT_FULL_TIME
                        || matchPeriod == BasketballMatchPeriod.AT_EXTRA_PERIOD_END
                        || matchPeriod == BasketballMatchPeriod.AT_HALF_TIME
                        || matchPeriod == BasketballMatchPeriod.MATCH_COMPLETED);
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
            if (pointsA > pointsB)
                teamId = TeamId.A;
            else if (pointsA == pointsB)
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
        int result = super.hashCode();
        result = prime * result + ((ballPosition == null) ? 0 : ballPosition.hashCode());
        result = prime * result + elapsedTimeSecs;
        result = prime * result + elapsedTimeThisPeriodSecs;
        result = prime * result + Arrays.hashCode(freeThrowStatus);
        result = prime * result + (isFourtyMinutiesMatch ? 1231 : 1237);
        result = prime * result + ((matchFormat == null) ? 0 : matchFormat.hashCode());
        result = prime * result + ((matchPeriod == null) ? 0 : matchPeriod.hashCode());
        result = prime * result + normalPeriodSecs;
        result = prime * result + normalTimeGoalsA;
        result = prime * result + normalTimeGoalsB;
        result = prime * result + overtimeNo;
        result = prime * result + pointsA;
        result = prime * result + pointsB;
        result = prime * result + previousPeriodGoalsA;
        result = prime * result + previousPeriodGoalsB;
        result = prime * result + (twoHalvesFormat ? 1231 : 1237);
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
        BasketballMatchState other = (BasketballMatchState) obj;
        if (ballPosition == null) {
            if (other.ballPosition != null)
                return false;
        } else if (!ballPosition.equals(other.ballPosition))
            return false;
        if (elapsedTimeSecs != other.elapsedTimeSecs)
            return false;
        if (elapsedTimeThisPeriodSecs != other.elapsedTimeThisPeriodSecs)
            return false;
        if (!Arrays.equals(freeThrowStatus, other.freeThrowStatus))
            return false;
        if (isFourtyMinutiesMatch != other.isFourtyMinutiesMatch)
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
        if (normalTimeGoalsA != other.normalTimeGoalsA)
            return false;
        if (normalTimeGoalsB != other.normalTimeGoalsB)
            return false;
        if (overtimeNo != other.overtimeNo)
            return false;
        if (pointsA != other.pointsA)
            return false;
        if (pointsB != other.pointsB)
            return false;
        if (previousPeriodGoalsA != other.previousPeriodGoalsA)
            return false;
        if (previousPeriodGoalsB != other.previousPeriodGoalsB)
            return false;
        if (twoHalvesFormat != other.twoHalvesFormat)
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
            case AT_FIRST_QUARTER_END:
            case AT_SECOND_QUARTER_END:
            case AT_THIRD_QUARTER_END:
            case AT_FULL_TIME:
            case AT_HALF_TIME:
            case AT_EXTRA_PERIOD_END:
            case MATCH_COMPLETED:
                break;
            case IN_FIRST_HALF:
            case IN_SECOND_HALF:
            case IN_FIRST_QUARTER:
            case IN_SECOND_QUARTER:
            case IN_THIRD_QUARTER:
            case IN_FOURTH_QUARTER:
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

        if (!twoHalvesFormat) {
            switch (matchPeriod) {
                case PREMATCH:
                    matchPeriod = BasketballMatchPeriod.IN_FIRST_QUARTER;
                    elapsedTimeThisPeriodSecs = elapsedTimeSecs;
                    break;

                case AT_FIRST_QUARTER_END:
                case IN_FIRST_QUARTER:
                    elapsedTimeThisPeriodSecs = elapsedTimeSecs;
                    if (elapsedTimeThisPeriodSecs >= normalPeriodSecs) {
                        elapsedTimeSecs = normalPeriodSecs; // reset counter
                                                            // to remove
                                                            // injury time
                        elapsedTimeThisPeriodSecs = 0;
                        matchPeriod = BasketballMatchPeriod.IN_SECOND_QUARTER;
                        previousPeriodGoalsA = currentPeriodGoalsA;
                        previousPeriodGoalsB = currentPeriodGoalsB;
                        currentPeriodGoalsA = 0;
                        currentPeriodGoalsB = 0;
                    }
                    break;
                case AT_SECOND_QUARTER_END:
                case IN_SECOND_QUARTER:
                    elapsedTimeThisPeriodSecs = elapsedTimeSecs - normalPeriodSecs;
                    if (elapsedTimeThisPeriodSecs >= normalPeriodSecs) {
                        elapsedTimeSecs = 2 * normalPeriodSecs;
                        elapsedTimeThisPeriodSecs = 0;
                        matchPeriod = BasketballMatchPeriod.IN_THIRD_QUARTER;
                        previousPeriodGoalsA = currentPeriodGoalsA;
                        previousPeriodGoalsB = currentPeriodGoalsB;
                        currentPeriodGoalsA = 0;
                        currentPeriodGoalsB = 0;

                    }
                    break;

                case AT_THIRD_QUARTER_END:
                case IN_THIRD_QUARTER:
                    elapsedTimeThisPeriodSecs = elapsedTimeSecs - 2 * normalPeriodSecs;
                    if (elapsedTimeThisPeriodSecs >= normalPeriodSecs) {
                        elapsedTimeSecs = 3 * normalPeriodSecs;
                        elapsedTimeThisPeriodSecs = 0;
                        matchPeriod = BasketballMatchPeriod.IN_FOURTH_QUARTER;
                        previousPeriodGoalsA = currentPeriodGoalsA;
                        previousPeriodGoalsB = currentPeriodGoalsB;
                        currentPeriodGoalsA = 0;
                        currentPeriodGoalsB = 0;
                    }
                    break;
                case AT_FULL_TIME:
                case IN_FOURTH_QUARTER:
                    elapsedTimeThisPeriodSecs = elapsedTimeSecs - 3 * normalPeriodSecs;
                    if (elapsedTimeThisPeriodSecs >= normalPeriodSecs) {
                        if (pointsA != pointsB) {
                            matchPeriod = BasketballMatchPeriod.MATCH_COMPLETED;
                        } else {
                            elapsedTimeSecs = 4 * normalPeriodSecs;
                            elapsedTimeThisPeriodSecs = 0;
                            matchPeriod = BasketballMatchPeriod.IN_EXTRA_TIME;
                            overtimeNo++;
                        }
                        previousPeriodGoalsA = currentPeriodGoalsA;
                        previousPeriodGoalsB = currentPeriodGoalsB;
                        currentPeriodGoalsA = 0;
                        currentPeriodGoalsB = 0;
                    }
                    break;
                case AT_EXTRA_PERIOD_END:
                case IN_EXTRA_TIME:
                    elapsedTimeThisPeriodSecs =
                                    elapsedTimeSecs - 4 * normalPeriodSecs - (overtimeNo - 1) * extraPeriodSecs;
                    if (elapsedTimeThisPeriodSecs >= extraPeriodSecs) {
                        elapsedTimeThisPeriodSecs = 0;
                        elapsedTimeSecs = 4 * normalPeriodSecs + (overtimeNo) * extraPeriodSecs;
                        if (pointsA != pointsB) {
                            matchPeriod = BasketballMatchPeriod.MATCH_COMPLETED;
                            break;
                        } else {
                            previousPeriodGoalsA = currentPeriodGoalsA;
                            previousPeriodGoalsB = currentPeriodGoalsB;
                            currentPeriodGoalsA = 0;
                            currentPeriodGoalsB = 0;
                            matchPeriod = BasketballMatchPeriod.AT_EXTRA_PERIOD_END;
                            overtimeNo++;
                            break;
                        }
                    }

                case MATCH_COMPLETED:
                    break;

                default:
                    throw new IllegalArgumentException("Should not get here");
            }

        } else {

            switch (matchPeriod) {
                case PREMATCH:
                    matchPeriod = BasketballMatchPeriod.IN_FIRST_QUARTER;
                    elapsedTimeThisPeriodSecs = elapsedTimeSecs;
                    break;

                case AT_HALF_TIME:
                case IN_FIRST_HALF:
                    elapsedTimeThisPeriodSecs = elapsedTimeSecs;
                    if (elapsedTimeThisPeriodSecs >= normalPeriodSecs) {
                        elapsedTimeSecs = normalPeriodSecs;
                        elapsedTimeThisPeriodSecs = 0;
                        matchPeriod = BasketballMatchPeriod.IN_SECOND_HALF;
                        previousPeriodGoalsA = currentPeriodGoalsA;
                        previousPeriodGoalsB = currentPeriodGoalsB;
                        currentPeriodGoalsA = 0;
                        currentPeriodGoalsB = 0;
                    }
                    break;
                case AT_FULL_TIME:
                case IN_SECOND_HALF:
                    elapsedTimeThisPeriodSecs = elapsedTimeSecs - normalPeriodSecs;
                    if (elapsedTimeThisPeriodSecs >= normalPeriodSecs) {
                        if (pointsA != pointsB) {
                            matchPeriod = BasketballMatchPeriod.MATCH_COMPLETED;
                        } else {
                            elapsedTimeSecs = 2 * normalPeriodSecs;
                            elapsedTimeThisPeriodSecs = 0;
                            matchPeriod = BasketballMatchPeriod.IN_EXTRA_TIME;
                            overtimeNo++;
                        }
                        previousPeriodGoalsA = currentPeriodGoalsA;
                        previousPeriodGoalsB = currentPeriodGoalsB;
                        currentPeriodGoalsA = 0;
                        currentPeriodGoalsB = 0;
                    }
                    break;
                case AT_EXTRA_PERIOD_END:
                case IN_EXTRA_TIME:
                    elapsedTimeThisPeriodSecs =
                                    elapsedTimeSecs - 2 * normalPeriodSecs - (overtimeNo - 1) * extraPeriodSecs;
                    if (elapsedTimeThisPeriodSecs >= extraPeriodSecs) {
                        elapsedTimeThisPeriodSecs = 0;
                        elapsedTimeSecs = 2 * normalPeriodSecs + (overtimeNo) * extraPeriodSecs;
                        if (pointsA != pointsB) {
                            matchPeriod = BasketballMatchPeriod.MATCH_COMPLETED;
                            break;
                        } else {
                            previousPeriodGoalsA = currentPeriodGoalsA;
                            previousPeriodGoalsB = currentPeriodGoalsB;
                            currentPeriodGoalsA = 0;
                            currentPeriodGoalsB = 0;
                            matchPeriod = BasketballMatchPeriod.AT_EXTRA_PERIOD_END;
                            overtimeNo++;
                            break;
                        }
                    }

                case MATCH_COMPLETED:
                    break;

                default:
                    throw new IllegalArgumentException("Should not get here");
            }

        }

    }

    /*
     * Basketball Ball and Field info
     */

    @Override
    public GamePeriod getGamePeriod() {
        GamePeriod gamePeriod = null;
        switch (matchPeriod) {

            case IN_EXTRA_TIME:
                if (overtimeNo == 0) {
                    gamePeriod = GamePeriod.EXTRA_TIME_FIRST_HALF;
                    break;
                }
                if (overtimeNo == 1) {
                    gamePeriod = GamePeriod.EXTRA_TIME_SECOND_HALF;
                    break;
                }
            case AT_EXTRA_PERIOD_END:
                gamePeriod = GamePeriod.EXTRA_TIME_ENDED;
                break;

            case IN_FIRST_HALF:
                gamePeriod = GamePeriod.FIRST_HALF;
                break;
            case AT_HALF_TIME:
                gamePeriod = GamePeriod.HALF_TIME;
                break;
            case IN_SECOND_HALF:
                gamePeriod = GamePeriod.SECOND_HALF;
                break;

            case IN_FIRST_QUARTER:
                gamePeriod = GamePeriod.FIRST_QUARTER;
                break;
            case AT_FIRST_QUARTER_END:
                gamePeriod = GamePeriod.END_OF_FIRST_QUARTER;
                break;
            case IN_SECOND_QUARTER:
                gamePeriod = GamePeriod.SECOND_QUARTER;
                break;
            case AT_SECOND_QUARTER_END:
                gamePeriod = GamePeriod.END_OF_SECOND_QUARTER;
                break;
            case IN_THIRD_QUARTER:
                gamePeriod = GamePeriod.THIRD_QUARTER;
                break;
            case AT_THIRD_QUARTER_END:
                gamePeriod = GamePeriod.END_OF_THIRD_QUARTER;
                break;
            case IN_FOURTH_QUARTER:
                gamePeriod = GamePeriod.FOURTH_QUARTER;
                break;

            case AT_FULL_TIME:
                gamePeriod = GamePeriod.NORMAL_TIME_END;
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
        if (matchFormat.isTwoHalvesFormat()) {
            if (matchFormat.getExtraTimeMinutes() == 0) {
                if (periodNo > 5)
                    return null;
                return String.format("P%d", periodNo);
            } else {
                return String.format("P%d", periodNo);
            }
        } else {
            if (matchFormat.getExtraTimeMinutes() == 0) {
                if (periodNo > 5)
                    return null;
                return String.format("P%d", periodNo);
            } else {
                return String.format("P%d", periodNo);
            }
        }
    }

    /**
     * returns true if in preMatch
     */
    @Override
    public boolean preMatch() {
        return (matchPeriod == BasketballMatchPeriod.PREMATCH);
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
            case AT_FIRST_QUARTER_END:
            case AT_SECOND_QUARTER_END:
            case AT_THIRD_QUARTER_END:
            case AT_EXTRA_PERIOD_END:

            case MATCH_COMPLETED:
            case PREMATCH:
                secs = 0;
                break;
            case IN_FIRST_HALF:
            case IN_SECOND_HALF:
            case IN_FIRST_QUARTER:
            case IN_SECOND_QUARTER:
            case IN_THIRD_QUARTER:
            case IN_FOURTH_QUARTER:
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

    public String getSequenceIdForHalf(int periodOffSet) {
        int periodNo = getPeriodNo() + periodOffSet;
        if (matchFormat.isTwoHalvesFormat()) {
            // if (periodNo > 3)
            // return null;
            return String.format("H%d", periodNo);
        } else {
            if (periodNo < 3) {
                return String.format("H%d", 1);
            } else if (periodNo < 5) {
                return String.format("H%d", 2);
            } else if (periodNo >= 5) {
                return String.format("H%d", 3);
            } else {
                return null;
            }
        }
    }

    @Override
    public BasketballSimpleMatchState generateSimpleMatchState() {
        BasketballSimpleMatchState basketballSimpleMatchState = new BasketballSimpleMatchState(preMatch(),
                        isMatchCompleted(), matchPeriod, getElapsedTimeSecs(), pointsA, pointsB, firstQuarterPointsA,
                        secondQuarterPointsA, thirdQuarterPointsA, fourthQuarterPointsA, firstHalfPointsA,
                        secondHalfPointsA, firstQuarterPointsB, secondQuarterPointsB, thirdQuarterPointsB,
                        fourthQuarterPointsB, firstHalfPointsB, secondHalfPointsB);
        return basketballSimpleMatchState;
    }
}
