package ats.algo.sport.americanfootball;

import java.util.ArrayList;
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
import ats.algo.core.common.ElapsedTimeMatchIncident.ElapsedTimeMatchIncidentType;
import ats.algo.sport.americanfootball.AmericanfootballMatchIncident.FieldPositionType;
import ats.algo.sport.americanfootball.AmericanfootballMatchIncident;
import ats.algo.sport.americanfootball.AmericanfootballMatchIncident.AmericanfootballMatchIncidentType;
import ats.algo.core.common.TeamId;

public class AmericanfootballMatchState extends MatchState {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final int pointsForTouchDown = 6;
    private static final int pointsForFieldGoal = 3;
    private static final int pointsForConversion1 = 1;
    private static final int pointsForConversion2 = 2;

    // /* CJ added for penalties condition */
    // private static final int defaultInjuryTimeFirstHalfSecs = 120;
    // private static final int defaultInjuryTimeSecondHalfSecs = 180;
    // private static final int defaultInjuryTimeExtraTimeHalfDeciSeconds = 60;
    // /* no need timers if use feeds terminate sinBins*/
    private int redCardA = 0;
    private int redCardB = 0;

    /* last match incident type for trading rule */
    @JsonIgnore
    MatchIncident lastMatchIncidentType;

    private boolean firstScoreTDinOverTime = false;
    private int elapsedTimeAtLastGoalSecs;
    private AmericanfootballMatchPeriod matchPeriodInWhichLastGoalScored;

    @JsonIgnore
    private ArrayList<Integer> tenMinsSinBinTimer_A = new ArrayList<Integer>();
    @JsonIgnore
    private ArrayList<Integer> tenMinsSinBinTimer_B = new ArrayList<Integer>();

    @JsonIgnore
    private int elapsedTimeSecsFirstGoal = 0;
    private int injuryTimeSecs;
    private final int extraPeriodSecs;
    private AmericanfootballMatchStatus americanfootballMatchStatus;
    private BallPosition ballPosition;
    private int tenMinsSinBinA;
    private int tenMinsSinBinB;
    /* For score board use */
    private int totalPointsA;
    private int totalPointsB;

    private int firstQuarterTDA = 0;
    private int secondQuarterTDA = 0;
    private int thirdQuarterTDA = 0;
    private int fourthQuarterTDA = 0;
    private int extraTimeQuarterTDA = 0;

    private int firstQuarterTDB = 0;
    private int secondQuarterTDB = 0;
    private int thirdQuarterTDB = 0;
    private int fourthQuarterTDB = 0;
    private int extraTimeQuarterTDB = 0;

    private int firstQuarterPointsA = 0;
    private int secondQuarterPointsA = 0;
    private int thirdQuarterPointsA = 0;
    private int fourthQuarterPointsA = 0;
    private int extraTimeQuarterPointsA = 0;

    private int firstQuarterPointsB = 0;
    private int secondQuarterPointsB = 0;
    private int thirdQuarterPointsB = 0;
    private int fourthQuarterPointsB = 0;
    private int extraTimeQuarterPointsB = 0;

    private TeamId firstSidePossedTheBallExtraTime = TeamId.UNKNOWN;
    private TeamId secondSidePossedTheBallExtraTime = TeamId.UNKNOWN;

    private int tdA, tdB; // TOUCH DOWN
    private int conv1A, conv1B;
    private int conv2A, conv2B;
    private int fgA, fgB; // FIELD GOAL

    private int elapsedTimeSecs;
    private int elapsedTimeAtLastMatchIncidentSecs; // the time from the last matchIncident report
    private int elapsedTimeThisPeriodSecs; // recent MatchIncident;
    private final int normalPeriodSecs;
    private AmericanfootballMatchPeriod matchPeriod; // the state following the most
    private final AmericanfootballMatchFormat matchFormat;
    private TeamId teamScoringLastPoint;
    private int currentPeriodPointsA;
    private int currentPeriodPointsB;

    private int previousPeriodTdA = 0;
    private int previousPeriodTdB = 0;
    private int currentPeriodTdA = 0;
    private int currentPeriodTdB = 0;
    private int thisSendoffLength = 10 * 60;
    @JsonIgnore
    private int previousFiveMinsEnder = 300;
    @JsonIgnore
    private int currentFiveMinsEnder = 300;
    @JsonIgnore
    private int previousFiveMinsGoalsA;
    @JsonIgnore
    private int previousFiveMinsGoalsB;
    @JsonIgnore
    private int fiveMinsPointsA;
    @JsonIgnore
    private int fiveMinsPointsB;
    @JsonIgnore
    private int fiveMinsNo;
    @JsonIgnore
    private int overtimeNo;
    @JsonIgnore
    private boolean penaltiesPossible;
    @JsonIgnore
    int goalSequence;
    @JsonIgnore
    private int shootOutTimeCounter;
    @JsonIgnore
    private int normalTimePointsA;
    @JsonIgnore
    private int normalTimePointsB;
    @JsonIgnore
    private int previousPeriodPointsA;
    @JsonIgnore
    private int previousPeriodPointsB;
    @JsonIgnore
    private int previousHalfPointsA = 0;
    @JsonIgnore
    private int previousHalfPointsB = 0;

    @JsonIgnore
    private static final int timeIncrementSecs = 10;

    public AmericanfootballMatchState() {
        this(new AmericanfootballMatchFormat());
        // super();
        // normalPeriodSecs = 15 * 60;
        // elapsedTimeThisPeriodSecs = 0;
        //
        // tenMinsSinBinTimer_A = new ArrayList<Integer>();
        // tenMinsSinBinTimer_B = new ArrayList<Integer>();
        //
        // tenMinsSinBinA = 0;
        // tenMinsSinBinB = 0;
        // ballPosition = new BallPosition();
        // tdA = 0;
        // tdB = 0;
        // conv1A = 0;
        // conv1B = 0;
        // conv2A = 0;
        // conv2B = 0;
        // fgA = 0;
        // fgB = 0;
        // elapsedTimeSecs = 0;
        // this.matchFormat = new AmericanfootballMatchFormat();
        // teamScoringLastPoint = TeamId.UNKNOWN;
        // currentPeriodPointsA = 0;
        // currentPeriodPointsB = 0;
        // matchPeriod = AmericanfootballMatchPeriod.PREMATCH;
        // extraPeriodSecs = 5 * 60;
        // americanfootballMatchStatus = new AmericanfootballMatchStatus();
        // firstSidePossedTheBallExtraTime = TeamId.UNKNOWN;
        // secondSidePossedTheBallExtraTime = TeamId.UNKNOWN;

    }

    /*
     * for score board use
     * 
     */

    public int getPreviousPeriodTdA() {
        return previousPeriodTdA;
    }

    public int getFirstQuarterTDA() {
        return firstQuarterTDA;
    }

    public void setFirstQuarterTDA(int firstQuarterTDA) {
        this.firstQuarterTDA = firstQuarterTDA;
    }

    public int getSecondQuarterTDA() {
        return secondQuarterTDA;
    }

    public void setSecondQuarterTDA(int secondQuarterTDA) {
        this.secondQuarterTDA = secondQuarterTDA;
    }

    public int getThirdQuarterTDA() {
        return thirdQuarterTDA;
    }

    public void setThirdQuarterTDA(int thirdQuarterTDA) {
        this.thirdQuarterTDA = thirdQuarterTDA;
    }

    public int getFourthQuarterTDA() {
        return fourthQuarterTDA;
    }

    public void setFourthQuarterTDA(int fourthQuarterTDA) {
        this.fourthQuarterTDA = fourthQuarterTDA;
    }

    public int getExtraTimeQuarterTDA() {
        return extraTimeQuarterTDA;
    }

    public void setExtraTimeQuarterTDA(int extraTimeQuateTDA) {
        this.extraTimeQuarterTDA = extraTimeQuateTDA;
    }

    public int getFirstQuarterTDB() {
        return firstQuarterTDB;
    }

    public void setFirstQuarterTDB(int firstQuarterTDB) {
        this.firstQuarterTDB = firstQuarterTDB;
    }

    public int getSecondQuarterTDB() {
        return secondQuarterTDB;
    }

    public void setSecondQuarterTDB(int secondQuarterTDB) {
        this.secondQuarterTDB = secondQuarterTDB;
    }

    public int getThirdQuarterTDB() {
        return thirdQuarterTDB;
    }

    public void setThirdQuarterTDB(int thirdQuarterTDB) {
        this.thirdQuarterTDB = thirdQuarterTDB;
    }

    public int getFourthQuarterTDB() {
        return fourthQuarterTDB;
    }

    public void setFourthQuarterTDB(int fourthQuarterTDB) {
        this.fourthQuarterTDB = fourthQuarterTDB;
    }

    public int getExtraTimeQuarterTDB() {
        return extraTimeQuarterTDB;
    }

    public void setExtraTimeQuarterTDB(int extraTimeQuarterTDB) {
        this.extraTimeQuarterTDB = extraTimeQuarterTDB;
    }

    public int getPreviousHalfPointsA() {
        return previousHalfPointsA;
    }

    public void setPreviousHalfPointsA(int previousHalfPointsA) {
        this.previousHalfPointsA = previousHalfPointsA;
    }

    public int getPreviousHalfPointsB() {
        return previousHalfPointsB;
    }

    public void setPreviousHalfPointsB(int previousHalfPointsB) {
        this.previousHalfPointsB = previousHalfPointsB;
    }

    public void setPreviousPeriodTdA(int previousPeriodTdA) {
        this.previousPeriodTdA = previousPeriodTdA;
    }

    public int getPreviousPeriodTdB() {
        return previousPeriodTdB;
    }

    public void setPreviousPeriodTdB(int previousPeriodTdB) {
        this.previousPeriodTdB = previousPeriodTdB;
    }

    public int getPreviousPeriodPointsA() {
        return previousPeriodPointsA;
    }

    public int getPreviousPeriodPointsB() {
        return previousPeriodPointsB;
    }

    public int getTotalPointsA() {
        totalPointsA = tdA * pointsForTouchDown + conv1A * pointsForConversion1 + conv2A * pointsForConversion2
                        + fgA * pointsForFieldGoal;
        return totalPointsA;
    }

    public void setTotalPointsA(int totalPointsA) {
        this.totalPointsA = totalPointsA;
    }

    public int getTotalPointsB() {
        totalPointsB = tdB * pointsForTouchDown + conv1B * pointsForConversion1 + conv2A * pointsForConversion2
                        + fgB * pointsForFieldGoal;
        return totalPointsB;
    }

    public void setTotalPointsB(int totalPointsB) {
        this.totalPointsB = totalPointsB;
    }

    public int getFirstQuarterPointsA() {
        return firstQuarterPointsA;
    }

    public void setFirstQuarterPointsA(int firstQuarterPointsA) {
        this.firstQuarterPointsA = firstQuarterPointsA;
    }

    public int getSecondQuarterPointsA() {
        return secondQuarterPointsA;
    }

    public void setSecondQuarterPointsA(int secondQuarterPointsA) {
        this.secondQuarterPointsA = secondQuarterPointsA;
    }

    public int getThirdQuarterPointsA() {
        return thirdQuarterPointsA;
    }

    public void setThirdQuarterPointsA(int thirdQuarterPointsA) {
        this.thirdQuarterPointsA = thirdQuarterPointsA;
    }

    public int getFourthQuarterPointsA() {
        return fourthQuarterPointsA;
    }

    public void setFourthQuarterPointsA(int fourthQuarterPointsA) {
        this.fourthQuarterPointsA = fourthQuarterPointsA;
    }

    public int getExtraTimeQuarterPointsA() {
        return extraTimeQuarterPointsA;
    }

    public void setExtraTimeQuarterPointsA(int extraTimeQuarterPointsA) {
        this.extraTimeQuarterPointsA = extraTimeQuarterPointsA;
    }

    public int getFirstQuarterPointsB() {
        return firstQuarterPointsB;
    }

    public void setFirstQuarterPointsB(int firstQuarterPointsB) {
        this.firstQuarterPointsB = firstQuarterPointsB;
    }

    public int getSecondQuarterPointsB() {
        return secondQuarterPointsB;
    }

    public void setSecondQuarterPointsB(int secondQuarterPointsB) {
        this.secondQuarterPointsB = secondQuarterPointsB;
    }

    public int getThirdQuarterPointsB() {
        return thirdQuarterPointsB;
    }

    public void setThirdQuarterPointsB(int thirdQuarterPointsB) {
        this.thirdQuarterPointsB = thirdQuarterPointsB;
    }

    public int getFourthQuarterPointsB() {
        return fourthQuarterPointsB;
    }

    public void setFourthQuarterPointsB(int fourthQuarterPointsB) {
        this.fourthQuarterPointsB = fourthQuarterPointsB;
    }

    public int getExtraTimeQuarterPointsB() {
        return extraTimeQuarterPointsB;
    }

    public void setExtraTimeQuarterPointsB(int extraTimeQuarterPointsB) {
        this.extraTimeQuarterPointsB = extraTimeQuarterPointsB;
    }

    /////////// Score Board Params Ends Here /////////////

    public TeamId getFirstSidePossedTheBallExtraTime() {
        return firstSidePossedTheBallExtraTime;
    }

    public void setFirstSidePossedTheBallExtraTime(TeamId firstSidePossedTheBallExtraTime) {
        this.firstSidePossedTheBallExtraTime = firstSidePossedTheBallExtraTime;
    }

    public TeamId getSecondSidePossedTheBallExtraTime() {
        return secondSidePossedTheBallExtraTime;
    }

    public void setSecondSidePossedTheBallExtraTime(TeamId secondSidePossedTheBallExtraTime) {
        this.secondSidePossedTheBallExtraTime = secondSidePossedTheBallExtraTime;
    }

    @JsonIgnore
    public int getPointsA() {
        return tdA * pointsForTouchDown + conv1A * pointsForConversion1 + conv2A * pointsForConversion2
                        + fgA * pointsForFieldGoal;
    }

    @JsonIgnore
    public int getPointsB() {
        return tdB * pointsForTouchDown + conv1B * pointsForConversion1 + conv2B * pointsForConversion2
                        + fgB * pointsForFieldGoal;
    }

    public MatchIncident getLastMatchIncidentType() {
        return lastMatchIncidentType;
    }

    public void setLastMatchIncidentType(MatchIncident lastMatchIncidentType) {
        this.lastMatchIncidentType = lastMatchIncidentType;
    }

    public AmericanfootballMatchState(MatchFormat matchFormat) {
        super();
        this.matchFormat = (AmericanfootballMatchFormat) matchFormat;
        ballPosition = new BallPosition();
        tenMinsSinBinTimer_A = new ArrayList<Integer>();
        tenMinsSinBinTimer_B = new ArrayList<Integer>();
        teamScoringLastPoint = TeamId.UNKNOWN;
        firstSidePossedTheBallExtraTime = TeamId.UNKNOWN;
        secondSidePossedTheBallExtraTime = TeamId.UNKNOWN;
        matchPeriod = AmericanfootballMatchPeriod.PREMATCH;
        normalPeriodSecs = this.matchFormat.getNormalTimeMinutes() * 15;
        extraPeriodSecs = this.matchFormat.getExtraTimeMinutes() * 60;
        penaltiesPossible = this.matchFormat.isPenaltiesPossible();
        americanfootballMatchStatus = new AmericanfootballMatchStatus();
        elapsedTimeAtLastMatchIncidentSecs = -1;
        elapsedTimeAtLastGoalSecs = -1;
    }

    public boolean isFirstScoreTDinOverTime() {
        return firstScoreTDinOverTime;
    }

    public void setFirstScoreTDinOverTime(boolean firstScoreTDinOverTime) {
        this.firstScoreTDinOverTime = firstScoreTDinOverTime;
    }

    public int getRedCardA() {
        return redCardA;
    }

    public void setRedCardA(int redCardA) {
        this.redCardA = redCardA;
    }

    public int getRedCardB() {
        return redCardB;
    }

    public void setRedCardB(int redCardB) {
        this.redCardB = redCardB;
    }

    @JsonIgnore
    public AmericanfootballMatchStatus getAmericanfootballMatchStatus() {
        return americanfootballMatchStatus;
    }

    public BallPosition getBallPosition() {
        return ballPosition;
    }

    public void setBallPosition(BallPosition ballPosition) {
        this.ballPosition = ballPosition;
    }

    @JsonIgnore
    public int getInjuryTimeSecs() {
        return injuryTimeSecs;
    }

    @JsonIgnore
    public void setInjuryTimeSecs(int injuryTimeSecs) {
        this.injuryTimeSecs = injuryTimeSecs;
    }

    @JsonIgnore
    public int getShootOutTimeCounter() {
        return shootOutTimeCounter;
    }

    @JsonIgnore
    public void setShootOutTimeCounter(int shootOutTimeCounter) {
        this.shootOutTimeCounter = shootOutTimeCounter;
    }

    @JsonIgnore
    public boolean isPenaltiesPossible() {
        return penaltiesPossible;
    }

    @JsonIgnore
    public void setPenaltiesPossible(boolean penaltiesPossible) {
        this.penaltiesPossible = penaltiesPossible;
    }

    public int getTenMinsSinBinA() {
        return tenMinsSinBinA;
    }

    public void setTenMinsSinBinA(int majorSinBinA) {
        this.tenMinsSinBinA = majorSinBinA;
    }

    public int getTenMinsSinBinB() {
        return tenMinsSinBinB;
    }

    public void setTenMinsSinBinB(int majorSinBinB) {
        this.tenMinsSinBinB = majorSinBinB;
    }

    @JsonIgnore
    public int getFiveMinsNo() {
        return fiveMinsNo;
    }

    @JsonIgnore
    public int getGoalSequence() {
        return goalSequence;
    }

    @JsonIgnore
    public void setGoalSequence(int goalSequence) {
        this.goalSequence = goalSequence;
    }

    @JsonIgnore
    public void setFiveMinsNo(int fiveMinsNo) {
        this.fiveMinsNo = fiveMinsNo;
    }

    @JsonIgnore
    public int getPreviousFiveMinsGoalsA() {
        return previousFiveMinsGoalsA;
    }

    @JsonIgnore
    public void setPreviousFiveMinsGoalsA(int previousFiveMinsGoalsA) {
        this.previousFiveMinsGoalsA = previousFiveMinsGoalsA;
    }

    @JsonIgnore
    public int getPreviousFiveMinsGoalsB() {
        return previousFiveMinsGoalsB;
    }

    @JsonIgnore
    public void setPreviousFiveMinsGoalsB(int previousFiveMinsGoalsB) {
        this.previousFiveMinsGoalsB = previousFiveMinsGoalsB;
    }

    @JsonIgnore
    public int getPreviousFiveMinsEnder() {
        return previousFiveMinsEnder;
    }

    @JsonIgnore
    public void setPreviousFiveMinsEnder(int previousFiveMinsEnder) {
        this.previousFiveMinsEnder = previousFiveMinsEnder;
    }

    @JsonIgnore
    public int getCurrentFiveMinsEnder() {
        return currentFiveMinsEnder;
    }

    @JsonIgnore
    public void setCurrentFiveMinsEnder(int thisFiveMinsEnder) {
        this.currentFiveMinsEnder = thisFiveMinsEnder;
    }

    @JsonIgnore
    public int getFiveMinsPointsA() {
        return fiveMinsPointsA;
    }

    @JsonIgnore
    public void setFiveMinsPointsA(int fiveMinsGoalsA) {
        this.fiveMinsPointsA = fiveMinsGoalsA;
    }

    @JsonIgnore
    public int getFiveMinsPointsB() {
        return fiveMinsPointsB;
    }

    @JsonIgnore
    public void setFiveMinsPointsB(int fiveMinsGoalsB) {
        this.fiveMinsPointsB = fiveMinsGoalsB;
    }

    @JsonIgnore
    public int getNormalTimePointsA() {
        return normalTimePointsA;
    }

    @JsonIgnore
    public void setNormalTimePointsA(int normalTimeGoalsA) {
        this.normalTimePointsA = normalTimeGoalsA;
    }

    @JsonIgnore
    public int getNormalTimePointsB() {
        return normalTimePointsB;
    }

    @JsonIgnore
    public void setNormalTimePointsB(int normalTimeGoalsB) {
        this.normalTimePointsB = normalTimeGoalsB;
    }

    @JsonIgnore
    public int getThisSendoffLength() {
        return thisSendoffLength;
    }

    @JsonIgnore
    public void setThisSendoffLength(int thisSendoffLength) {
        this.thisSendoffLength = thisSendoffLength;
    }

    @JsonIgnore
    public int getOvertimeNo() {
        return overtimeNo;
    }

    @JsonIgnore
    public void setOvertimeNo(int overtimeNo) {
        this.overtimeNo = overtimeNo;
    }

    public int getExtraPeriodSecs() {
        return extraPeriodSecs;
    }

    public int getNormalPeriodSecs() {
        return normalPeriodSecs;
    }

    public void setElapsedTimeThisPeriodSecs(int elapsedTimeThisPeriodSecs) {
        this.elapsedTimeThisPeriodSecs = elapsedTimeThisPeriodSecs;
    }

    public void setPreviousPeriodPointsA(int previousPeriodGoalsA) {
        this.previousPeriodPointsA = previousPeriodGoalsA;
    }

    public void setPreviousPeriodPointsB(int previousPeriodGoalsB) {
        this.previousPeriodPointsB = previousPeriodGoalsB;
    }

    public void setCurrentPeriodPointsA(int currentPeriodGoalsA) {
        this.currentPeriodPointsA = currentPeriodGoalsA;
    }

    public void setCurrentPeriodPointsB(int currentPeriodGoalsB) {
        this.currentPeriodPointsB = currentPeriodGoalsB;
    }

    public void setCurrentPeriodTdA(int currentPeriodTrysA) {
        this.currentPeriodTdA = currentPeriodTrysA;
    }

    public void setCurrentPeriodTdB(int currentPeriodTrysB) {
        this.currentPeriodTdB = currentPeriodTrysB;
    }

    @JsonIgnore
    public ArrayList<Integer> getTenMinsSinBinTimer_A() {
        return tenMinsSinBinTimer_A;
    }

    @JsonIgnore
    public void setTenMinsSinBinTimer_A(ArrayList<Integer> majorSinBinTimer_A) {
        this.tenMinsSinBinTimer_A = majorSinBinTimer_A;
    }

    @JsonIgnore
    public ArrayList<Integer> getTenMinsSinBinTimer_B() {
        return tenMinsSinBinTimer_B;
    }

    @JsonIgnore
    public void setTenMinsSinBinTimer_B(ArrayList<Integer> majorSinBinTimer_B) {
        this.tenMinsSinBinTimer_B = majorSinBinTimer_B;
    }

    public int getTdA() {
        return tdA;
    }

    public void setTdA(int tryA) {
        this.tdA = tryA;
    }

    public int getTdB() {
        return tdB;
    }

    public void setTdB(int tryB) {
        this.tdB = tryB;
    }

    public int getConv1A() {
        return conv1A;
    }

    public void setConv1A(int convA) {
        this.conv1A = convA;
    }

    public int getConv1B() {
        return conv1B;
    }

    public void setConv1B(int convB) {
        this.conv1B = convB;
    }

    public int getConv2A() {
        return conv2A;
    }

    public void setConv2A(int conv2a) {
        conv2A = conv2a;
    }

    public int getConv2B() {
        return conv2B;
    }

    public void setConv2B(int conv2b) {
        conv2B = conv2b;
    }

    public int getFgA() {
        return fgA;
    }

    public void setFgA(int dropGoalA) {
        this.fgA = dropGoalA;
    }

    public int getFgB() {
        return fgB;
    }

    public void setFgB(int dropGoalB) {
        this.fgB = dropGoalB;
    }

    public int getElapsedTimeSecs() {
        return elapsedTimeSecs;
    }

    public void setTeamScoringLastPoint(TeamId teamId) {
        this.teamScoringLastPoint = teamId;
    }

    public TeamId getTeamScoringLastPoint() {
        return teamScoringLastPoint;
    }

    public int getElapsedTimeThisPeriodSecs() {
        return elapsedTimeThisPeriodSecs;
    }

    @JsonIgnore
    public int getGoalNo() {
        return goalSequence + 1;
    }

    @JsonIgnore
    public int getPeriodNo() {
        int n = 0;
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
                n = 6;
                break;
            case AT_EXTRA_TIME_END:
            case IN_EXTRA_TIME_15MINUTES:
                n = 5;
                break;
            default:
                throw new IllegalArgumentException("Unknown match period");
        }
        return n;
    }

    @JsonIgnore
    public int getElapsedTimeSecsFirstGoal() {
        return elapsedTimeSecsFirstGoal;
    }

    @JsonIgnore
    public void setElapsedTimeSecsFirstGoal(int elapsedTimeSecsFirstGoal) {
        this.elapsedTimeSecsFirstGoal = elapsedTimeSecsFirstGoal;
    }

    // FIXME: MIGHT NEED TO BE DELETED
    public void setElapsedTimeSecs(int elapsedTimeSecs, int overtimeNoIn) {
        this.elapsedTimeSecs = elapsedTimeSecs;
        this.elapsedTimeThisPeriodSecs = elapsedTimeSecs;


        matchPeriod = AmericanfootballMatchPeriod.PREMATCH;

        if (elapsedTimeSecs > 0) {
            matchPeriod = AmericanfootballMatchPeriod.IN_FIRST_QUARTER;
        }
        if (elapsedTimeSecs == normalPeriodSecs) {
            this.elapsedTimeThisPeriodSecs = 0;
            matchPeriod = AmericanfootballMatchPeriod.AT_FIRST_QUARTER_END;
        }
        if (elapsedTimeSecs > normalPeriodSecs) {
            this.elapsedTimeThisPeriodSecs = elapsedTimeSecs - normalPeriodSecs;
            matchPeriod = AmericanfootballMatchPeriod.IN_SECOND_QUARTER;
        }
        if (elapsedTimeSecs == 2 * normalPeriodSecs) {
            this.elapsedTimeThisPeriodSecs = 0;
            matchPeriod = AmericanfootballMatchPeriod.AT_SECOND_QUARTER_END;
        }
        if (elapsedTimeSecs > 2 * normalPeriodSecs) {
            this.elapsedTimeThisPeriodSecs = elapsedTimeSecs - 2 * normalPeriodSecs;
            matchPeriod = AmericanfootballMatchPeriod.IN_THIRD_QUARTER;
        }
        if (elapsedTimeSecs == 3 * normalPeriodSecs) {
            this.elapsedTimeThisPeriodSecs = 0;
            matchPeriod = AmericanfootballMatchPeriod.AT_FULL_TIME;
        }
        if (elapsedTimeSecs > 3 * normalPeriodSecs) {
            this.elapsedTimeThisPeriodSecs = elapsedTimeSecs - overtimeNoIn * extraPeriodSecs;
            matchPeriod = AmericanfootballMatchPeriod.IN_EXTRA_TIME_15MINUTES;
        }

    }

    public AmericanfootballMatchPeriod getMatchPeriod() {
        return matchPeriod;
    }

    public void setMatchPeriod(AmericanfootballMatchPeriod matchPeriod) {
        this.matchPeriod = matchPeriod;
    }

    public int getCurrentPeriodPointsA() {

        return currentPeriodPointsA;
    }

    public int getCurrentPeriodPointsB() {
        return currentPeriodPointsB;
    }

    public int getCurrentPeriodTdA() {
        return currentPeriodTdA;
    }

    public int getCurrentPeriodTdB() {
        return currentPeriodTdB;
    }

    @Override
    public MatchIncidentResult updateStateForIncident(MatchIncident matchIncident,
                    boolean autosyncMatchStateToFeedOnMismatch) {
        if (!((matchIncident instanceof AmericanfootballMatchIncident)
                        || (matchIncident instanceof ElapsedTimeMatchIncident)))
            return super.updateStateForIncident(matchIncident, autosyncMatchStateToFeedOnMismatch);
        setElapsedTime((matchIncident.getElapsedTimeSecs()));
        elapsedTimeAtLastMatchIncidentSecs = elapsedTimeSecs; // ?? what's this
        setClockTimeOfLastElapsedTimeFromIncident(); // ??

        if (matchIncident.getClass() == AmericanfootballMatchIncident.class) {
            TeamId teamId = ((AmericanfootballMatchIncident) matchIncident).getTeamId();
            lastMatchIncidentType = (AmericanfootballMatchIncident) matchIncident; // for trading rules specifically
            switch (((AmericanfootballMatchIncident) matchIncident).getIncidentSubType()) {
                case COMBO_SETTING:
                    this.ballPosition.setFieldPosition(
                                    ((AmericanfootballMatchIncident) matchIncident).getFieldPosition());
                    this.ballPosition.setBallHoldingNow(((AmericanfootballMatchIncident) matchIncident).getTeamId());
                    this.ballPosition.setBallHoldingNow(((AmericanfootballMatchIncident) matchIncident).getTeamId());

                    setBallHoldingInExtraTime(((AmericanfootballMatchIncident) matchIncident).getTeamId());

                    this.ballPosition.setDownsRemain(((AmericanfootballMatchIncident) matchIncident).getDownRemain());
                    break;

                // case BALL_POSITION_SETTING:
                // // switch (teamId) {
                // // case A:
                // // ballPosition.setBallHoldingNow(TeamId.A);
                // // break;
                // // case B:
                // // ballPosition.setBallHoldingNow(TeamId.B);
                // // break;
                // // default:
                // // throw new IllegalArgumentException(" Illegal input ");
                // // }
                // break;

                case RESET_FIELD_BALL_INFO:
                    ballPosition.resetBallPosition();
                    break;

                case DOWNS_CONSUME:
                    ballPosition.minusDownsNumber();
                    break;

                // case PENALTY_START:
                // americanfootballMatchStatus.setAmericanfootballMatchStatus(AmericanfootballMatchStatusType.PENALTY,
                // teamId);
                // break;

                case CONVERSION_START:
                    americanfootballMatchStatus
                                    .setAmericanfootballMatchStatus(AmericanfootballMatchStatusType.CONVERSION, teamId);
                    break;

                case FIELD_POSITION_SETTING:
                    FieldPositionType fieldPosition =
                                    ((AmericanfootballMatchIncident) matchIncident).getFieldPosition();
                    if (this.ballPosition.getBallHoldingNow() != TeamId.UNKNOWN) {
                        if (fieldAdvancingSuccssed(this.ballPosition.getBallHoldingNow(),
                                        this.ballPosition.getAnchorFieldPosition(), fieldPosition))
                            this.ballPosition.setDownsRemain(4);
                    }

                    this.ballPosition.fieldPosition = fieldPosition;
                    break;

                case TOUCH_DOWN:
                    elapsedTimeAtLastGoalSecs = elapsedTimeSecs;
                    matchPeriodInWhichLastGoalScored = matchPeriod;
                    americanfootballMatchStatus
                                    .setAmericanfootballMatchStatus(AmericanfootballMatchStatusType.CONVERSION, teamId);
                    switch (teamId) {
                        case A:
                            if (!matchPeriod.equals(AmericanfootballMatchPeriod.IN_EXTRA_TIME_15MINUTES))
                                normalTimePointsA += pointsForTouchDown;
                            updateFiveMinsStatus();

                            firstScoreTDinOverTime = checkIfFirstScoreTDinOverTime();
                            if (firstScoreTDinOverTime)
                                matchPeriod = AmericanfootballMatchPeriod.MATCH_COMPLETED;

                            fiveMinsPointsA += pointsForTouchDown;
                            if (tdA + conv1A + conv2A + fgA == 0 && tdB + conv1B + conv2B + fgB == 0) {
                                elapsedTimeSecsFirstGoal = elapsedTimeSecs;
                            }
                            tdA++;
                            currentPeriodPointsA += pointsForTouchDown;
                            currentPeriodTdA++;
                            teamScoringLastPoint = TeamId.A;
                            goalSequence++;

                            if (matchPeriod.equals(AmericanfootballMatchPeriod.IN_FIRST_QUARTER)) {
                                firstQuarterPointsA += pointsForTouchDown;
                                firstQuarterTDA += 1;
                            } else if (matchPeriod.equals(AmericanfootballMatchPeriod.IN_SECOND_QUARTER)) {
                                secondQuarterPointsA += pointsForTouchDown;
                                secondQuarterTDA += 1;
                            } else if (matchPeriod.equals(AmericanfootballMatchPeriod.IN_THIRD_QUARTER)) {
                                thirdQuarterPointsA += pointsForTouchDown;
                                thirdQuarterTDA += 1;
                            } else if (matchPeriod.equals(AmericanfootballMatchPeriod.IN_FOURTH_QUARTER)) {
                                fourthQuarterPointsA += pointsForTouchDown;
                                fourthQuarterTDA += 1;
                            } else if (matchPeriod.equals(AmericanfootballMatchPeriod.IN_EXTRA_TIME_15MINUTES)) {
                                extraTimeQuarterTDA += 1;
                                extraTimeQuarterPointsA += pointsForTouchDown;
                            }
                            break;
                        case B:
                            if (!matchPeriod.equals(AmericanfootballMatchPeriod.IN_EXTRA_TIME_15MINUTES))
                                normalTimePointsB += pointsForTouchDown;
                            updateFiveMinsStatus();

                            firstScoreTDinOverTime = checkIfFirstScoreTDinOverTime();
                            if (firstScoreTDinOverTime)
                                matchPeriod = AmericanfootballMatchPeriod.MATCH_COMPLETED;

                            fiveMinsPointsB += pointsForTouchDown;
                            if (tdA + conv1A + conv2A + fgA == 0 && tdB + conv1B + conv2B + fgB == 0) {
                                elapsedTimeSecsFirstGoal = elapsedTimeSecs;
                            }
                            tdB++;
                            currentPeriodPointsB += pointsForTouchDown;
                            currentPeriodTdB++;
                            teamScoringLastPoint = TeamId.B;
                            goalSequence++;
                            if (matchPeriod.equals(AmericanfootballMatchPeriod.IN_FIRST_QUARTER)) {
                                firstQuarterPointsB += pointsForTouchDown;
                                firstQuarterTDB += 1;
                            } else if (matchPeriod.equals(AmericanfootballMatchPeriod.IN_SECOND_QUARTER)) {
                                secondQuarterPointsB += pointsForTouchDown;
                                secondQuarterTDB += 1;
                            } else if (matchPeriod.equals(AmericanfootballMatchPeriod.IN_THIRD_QUARTER)) {
                                thirdQuarterPointsB += pointsForTouchDown;
                                thirdQuarterTDB += 1;
                            } else if (matchPeriod.equals(AmericanfootballMatchPeriod.IN_FOURTH_QUARTER)) {
                                fourthQuarterPointsB += pointsForTouchDown;
                                fourthQuarterTDB += 1;
                            } else if (matchPeriod.equals(AmericanfootballMatchPeriod.IN_EXTRA_TIME_15MINUTES)) {
                                extraTimeQuarterPointsB += pointsForTouchDown;
                                extraTimeQuarterTDB += 1;
                            }
                            break;
                        case UNKNOWN:
                            throw new IllegalArgumentException("TeamId must be specified for a try!");
                    }
                    break;
                case CONVERSION_SCORE1:
                    elapsedTimeAtLastGoalSecs = elapsedTimeSecs;
                    matchPeriodInWhichLastGoalScored = matchPeriod;
                    checkIfPenatlyOrConversionGoalApproporate(AmericanfootballMatchIncidentType.CONVERSION_SCORE1,
                                    teamId);
                    switch (teamId) {
                        case A:
                            if (!matchPeriod.equals(AmericanfootballMatchPeriod.IN_EXTRA_TIME_15MINUTES))
                                normalTimePointsA += pointsForConversion1;

                            updateFiveMinsStatus();
                            fiveMinsPointsA += pointsForConversion1;

                            if (tdA + conv1A + conv2A + fgA == 0 && tdB + conv1B + conv2B + fgB == 0) {
                                elapsedTimeSecsFirstGoal = elapsedTimeSecs;
                            }

                            conv1A++;
                            currentPeriodPointsA += pointsForConversion1;
                            teamScoringLastPoint = TeamId.A;
                            goalSequence++;

                            if (matchPeriod.equals(AmericanfootballMatchPeriod.IN_FIRST_QUARTER)) {
                                firstQuarterPointsA += pointsForConversion1;
                            } else if (matchPeriod.equals(AmericanfootballMatchPeriod.IN_SECOND_QUARTER)) {
                                secondQuarterPointsA += pointsForConversion1;
                            } else if (matchPeriod.equals(AmericanfootballMatchPeriod.IN_THIRD_QUARTER)) {
                                thirdQuarterPointsA += pointsForConversion1;
                            } else if (matchPeriod.equals(AmericanfootballMatchPeriod.IN_FOURTH_QUARTER)) {
                                fourthQuarterPointsA += pointsForConversion1;
                            } else if (matchPeriod.equals(AmericanfootballMatchPeriod.IN_EXTRA_TIME_15MINUTES)) {
                                extraTimeQuarterPointsA += pointsForConversion1;
                            }
                            break;
                        case B:
                            if (!matchPeriod.equals(AmericanfootballMatchPeriod.IN_EXTRA_TIME_15MINUTES))
                                normalTimePointsB += pointsForConversion1;
                            updateFiveMinsStatus();
                            fiveMinsPointsB += pointsForConversion1;
                            if (tdA + conv1A + conv2A + fgA == 0 && tdB + conv1B + conv2B + fgB == 0) {
                                elapsedTimeSecsFirstGoal = elapsedTimeSecs;
                            }
                            conv1B++;
                            currentPeriodPointsB += pointsForConversion1;
                            teamScoringLastPoint = TeamId.B;
                            goalSequence++;
                            if (matchPeriod.equals(AmericanfootballMatchPeriod.IN_FIRST_QUARTER)) {
                                firstQuarterPointsB += pointsForConversion1;
                            } else if (matchPeriod.equals(AmericanfootballMatchPeriod.IN_SECOND_QUARTER)) {
                                secondQuarterPointsB += pointsForConversion1;
                            } else if (matchPeriod.equals(AmericanfootballMatchPeriod.IN_THIRD_QUARTER)) {
                                thirdQuarterPointsB += pointsForConversion1;
                            } else if (matchPeriod.equals(AmericanfootballMatchPeriod.IN_FOURTH_QUARTER)) {
                                fourthQuarterPointsB += pointsForConversion1;
                            } else if (matchPeriod.equals(AmericanfootballMatchPeriod.IN_EXTRA_TIME_15MINUTES)) {
                                extraTimeQuarterPointsB += pointsForConversion1;
                            }
                            break;
                        case UNKNOWN:
                            throw new IllegalArgumentException("TeamId must be specified for goal scored");
                    }

                    break;

                case CONVERSION_SCORE2:
                    elapsedTimeAtLastGoalSecs = elapsedTimeSecs;
                    matchPeriodInWhichLastGoalScored = matchPeriod;
                    checkIfPenatlyOrConversionGoalApproporate(AmericanfootballMatchIncidentType.CONVERSION_SCORE2,
                                    teamId);
                    switch (teamId) {
                        case A:
                            if (!matchPeriod.equals(AmericanfootballMatchPeriod.IN_EXTRA_TIME_15MINUTES))
                                normalTimePointsA += pointsForConversion2;

                            updateFiveMinsStatus();
                            fiveMinsPointsA += pointsForConversion2;

                            if (tdA + conv1A + conv2A + fgA == 0 && tdB + conv1B + conv2B + fgB == 0) {
                                elapsedTimeSecsFirstGoal = elapsedTimeSecs;
                            }

                            conv2A++;
                            currentPeriodPointsA += pointsForConversion2;
                            teamScoringLastPoint = TeamId.A;
                            goalSequence++;
                            if (matchPeriod.equals(AmericanfootballMatchPeriod.IN_FIRST_QUARTER)) {
                                firstQuarterPointsA += pointsForConversion2;
                            } else if (matchPeriod.equals(AmericanfootballMatchPeriod.IN_SECOND_QUARTER)) {
                                secondQuarterPointsA += pointsForConversion2;
                            } else if (matchPeriod.equals(AmericanfootballMatchPeriod.IN_THIRD_QUARTER)) {
                                thirdQuarterPointsA += pointsForConversion2;
                            } else if (matchPeriod.equals(AmericanfootballMatchPeriod.IN_FOURTH_QUARTER)) {
                                fourthQuarterPointsA += pointsForConversion2;
                            } else if (matchPeriod.equals(AmericanfootballMatchPeriod.IN_EXTRA_TIME_15MINUTES)) {
                                extraTimeQuarterPointsA += pointsForConversion2;
                            }
                            break;
                        case B:
                            if (!matchPeriod.equals(AmericanfootballMatchPeriod.IN_EXTRA_TIME_15MINUTES))
                                normalTimePointsB += pointsForConversion2;
                            updateFiveMinsStatus();
                            fiveMinsPointsB += pointsForConversion2;
                            if (tdA + conv2A + fgA == 0 && tdB + conv2B + fgB == 0) {
                                elapsedTimeSecsFirstGoal = elapsedTimeSecs;
                            }
                            conv2B++;
                            currentPeriodPointsB += pointsForConversion2;
                            teamScoringLastPoint = TeamId.B;
                            goalSequence++;
                            if (matchPeriod.equals(AmericanfootballMatchPeriod.IN_FIRST_QUARTER)) {
                                firstQuarterPointsB += pointsForConversion2;
                            } else if (matchPeriod.equals(AmericanfootballMatchPeriod.IN_SECOND_QUARTER)) {
                                secondQuarterPointsB += pointsForConversion2;
                            } else if (matchPeriod.equals(AmericanfootballMatchPeriod.IN_THIRD_QUARTER)) {
                                thirdQuarterPointsB += pointsForConversion2;
                            } else if (matchPeriod.equals(AmericanfootballMatchPeriod.IN_FOURTH_QUARTER)) {
                                fourthQuarterPointsB += pointsForConversion2;
                            } else if (matchPeriod.equals(AmericanfootballMatchPeriod.IN_EXTRA_TIME_15MINUTES)) {
                                extraTimeQuarterPointsB += pointsForConversion2;
                            }
                            break;
                        case UNKNOWN:
                            throw new IllegalArgumentException("TeamId must be specified for goal scored");
                    }

                    break;

                case CONVERSION_MISS:
                    checkIfPenatlyOrConversionGoalApproporate(AmericanfootballMatchIncidentType.CONVERSION_MISS,
                                    teamId);
                    break;
                case FIELD_GOAL:
                    elapsedTimeAtLastGoalSecs = elapsedTimeSecs;
                    matchPeriodInWhichLastGoalScored = matchPeriod;
                    switch (teamId) {
                        case A:
                            if (!matchPeriod.equals(AmericanfootballMatchPeriod.IN_EXTRA_TIME_15MINUTES))
                                normalTimePointsA += pointsForFieldGoal;

                            updateFiveMinsStatus();
                            fiveMinsPointsA += pointsForFieldGoal;

                            if (tdA + conv1A + conv2A + fgA == 0 && tdB + conv1B + conv2B + fgB == 0) {
                                elapsedTimeSecsFirstGoal = elapsedTimeSecs;
                            }

                            fgA++;
                            currentPeriodPointsA += pointsForFieldGoal;
                            teamScoringLastPoint = TeamId.A;
                            goalSequence++;

                            if (matchPeriod.equals(AmericanfootballMatchPeriod.IN_FIRST_QUARTER)) {
                                firstQuarterPointsA += pointsForFieldGoal;
                            } else if (matchPeriod.equals(AmericanfootballMatchPeriod.IN_SECOND_QUARTER)) {
                                secondQuarterPointsA += pointsForFieldGoal;
                            } else if (matchPeriod.equals(AmericanfootballMatchPeriod.IN_THIRD_QUARTER)) {
                                thirdQuarterPointsA += pointsForFieldGoal;
                            } else if (matchPeriod.equals(AmericanfootballMatchPeriod.IN_FOURTH_QUARTER)) {
                                fourthQuarterPointsA += pointsForFieldGoal;
                            } else if (matchPeriod.equals(AmericanfootballMatchPeriod.IN_EXTRA_TIME_15MINUTES)) {
                                extraTimeQuarterPointsA += pointsForFieldGoal;
                            }

                            // if (bothPossedBallInOverTime()) {
                            // if (this.getPointsA() != this.getPointsB())
                            // matchPeriod = AmericanfootballMatchPeriod.MATCH_COMPLETED;
                            // }
                            break;
                        case B:
                            if (!matchPeriod.equals(AmericanfootballMatchPeriod.IN_EXTRA_TIME_15MINUTES))
                                normalTimePointsB += pointsForFieldGoal;

                            updateFiveMinsStatus();
                            fiveMinsPointsB += pointsForFieldGoal;

                            if (tdA + conv1A + conv2A + fgA == 0 && tdB + conv1B + conv2B + fgB == 0) {
                                elapsedTimeSecsFirstGoal = elapsedTimeSecs;
                            }

                            fgB++;
                            currentPeriodPointsB += pointsForFieldGoal;
                            teamScoringLastPoint = TeamId.B;
                            goalSequence++;
                            if (matchPeriod.equals(AmericanfootballMatchPeriod.IN_FIRST_QUARTER)) {
                                firstQuarterPointsB += pointsForFieldGoal;
                            } else if (matchPeriod.equals(AmericanfootballMatchPeriod.IN_SECOND_QUARTER)) {
                                secondQuarterPointsB += pointsForFieldGoal;
                            } else if (matchPeriod.equals(AmericanfootballMatchPeriod.IN_THIRD_QUARTER)) {
                                thirdQuarterPointsB += pointsForFieldGoal;
                            } else if (matchPeriod.equals(AmericanfootballMatchPeriod.IN_FOURTH_QUARTER)) {
                                fourthQuarterPointsB += pointsForFieldGoal;
                            } else if (matchPeriod.equals(AmericanfootballMatchPeriod.IN_EXTRA_TIME_15MINUTES)) {
                                extraTimeQuarterPointsB += pointsForFieldGoal;
                            }
                            // if (bothPossedBallInOverTime()) {
                            // if (this.getPointsA() != this.getPointsB())
                            // matchPeriod = AmericanfootballMatchPeriod.MATCH_COMPLETED;
                            // }

                            break;

                        case UNKNOWN:
                            throw new IllegalArgumentException("TeamId must be specified for goal scored");
                    }
                    ballPosition.resetFieldPosition();
                    break;
                default:
                    throw new IllegalArgumentException("Unknown match incidences type");
            }
        } else {// must be ElapsedTimeMatchIncident
            this.setClockStatus(((ElapsedTimeMatchIncident) matchIncident).getIncidentSubType());
            lastMatchIncidentType = (ElapsedTimeMatchIncident) matchIncident;
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
                            matchPeriod = AmericanfootballMatchPeriod.IN_FIRST_QUARTER;
                            break;

                        case AT_FIRST_QUARTER_END:
                            elapsedTimeThisPeriodSecs = 0;
                            matchPeriod = AmericanfootballMatchPeriod.IN_SECOND_QUARTER;
                            break;
                        case IN_SECOND_QUARTER:
                            break;

                        /*
                         * 
                         * four periods type
                         * 
                         */

                        case IN_FIRST_QUARTER:
                            break;

                        case AT_SECOND_QUARTER_END:
                            elapsedTimeThisPeriodSecs = 0;
                            matchPeriod = AmericanfootballMatchPeriod.IN_THIRD_QUARTER;
                            break;

                        case IN_THIRD_QUARTER:
                            break;

                        case AT_THIRD_QUARTER_END:
                            elapsedTimeThisPeriodSecs = 0;
                            matchPeriod = AmericanfootballMatchPeriod.IN_FOURTH_QUARTER;
                            break;

                        case IN_FOURTH_QUARTER:
                            break;

                        case AT_FULL_TIME:
                            overtimeNo++;
                            elapsedTimeThisPeriodSecs = 0;
                            matchPeriod = AmericanfootballMatchPeriod.IN_EXTRA_TIME_15MINUTES;
                            break;

                        case IN_EXTRA_TIME_15MINUTES:
                            if (firstScoreTDinOverTime) {
                                matchPeriod = AmericanfootballMatchPeriod.MATCH_COMPLETED;
                            }
                            if (bothPossedBallInOverTime()) {
                                if (this.getPointsA() != this.getPointsB())
                                    matchPeriod = AmericanfootballMatchPeriod.MATCH_COMPLETED;
                            }

                            break;

                        case AT_EXTRA_TIME_END:
                            overtimeNo++;
                            elapsedTimeThisPeriodSecs = 0;
                            if (penaltiesPossible
                                            && (tdA + conv1A + conv2A + fgA == 0 && tdB + conv1B + conv2B + fgB == 0)) {
                                matchPeriod = AmericanfootballMatchPeriod.IN_EXTRA_TIME_15MINUTES;
                                break;
                            } else {
                                if (tdA + conv1A + conv2A + fgA == 0 && tdB + conv1B + conv2B + fgB == 0) {
                                    matchPeriod = AmericanfootballMatchPeriod.MATCH_COMPLETED;
                                } else {
                                    matchPeriod = AmericanfootballMatchPeriod.IN_EXTRA_TIME_15MINUTES;
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
                        case IN_FIRST_QUARTER:
                            matchPeriod = AmericanfootballMatchPeriod.AT_FIRST_QUARTER_END;
                            previousPeriodPointsA = currentPeriodPointsA;
                            previousPeriodPointsB = currentPeriodPointsB;
                            currentPeriodPointsA = 0;
                            currentPeriodPointsB = 0;

                            previousPeriodTdA = currentPeriodTdA;
                            previousPeriodTdB = currentPeriodTdB;
                            currentPeriodTdA = 0;
                            currentPeriodTdB = 0;

                            setElapsedTime(normalPeriodSecs);
                            elapsedTimeAtLastMatchIncidentSecs = normalPeriodSecs;
                            setClockTimeOfLastElapsedTimeFromIncident();
                            break;

                        case AT_FIRST_QUARTER_END:
                            break;

                        case IN_SECOND_QUARTER:
                            matchPeriod = AmericanfootballMatchPeriod.AT_SECOND_QUARTER_END;
                            previousPeriodPointsA = currentPeriodPointsA;
                            previousPeriodPointsB = currentPeriodPointsB;
                            currentPeriodPointsA = 0;
                            currentPeriodPointsB = 0;

                            previousPeriodTdA = currentPeriodTdA;
                            previousPeriodTdB = currentPeriodTdB;
                            currentPeriodTdA = 0;
                            currentPeriodTdB = 0;
                            // FIXME: only given values at the end of halves
                            previousHalfPointsA = firstQuarterPointsA + secondQuarterPointsA;
                            previousHalfPointsB = firstQuarterPointsB + secondQuarterPointsB;

                            setElapsedTime(normalPeriodSecs * 2);
                            elapsedTimeAtLastMatchIncidentSecs = normalPeriodSecs * 2;
                            setClockTimeOfLastElapsedTimeFromIncident();
                            break;

                        case AT_SECOND_QUARTER_END:
                            break;

                        case IN_THIRD_QUARTER:
                            matchPeriod = AmericanfootballMatchPeriod.AT_THIRD_QUARTER_END;
                            previousPeriodPointsA = currentPeriodPointsA;
                            previousPeriodPointsB = currentPeriodPointsB;
                            currentPeriodPointsA = 0;
                            currentPeriodPointsB = 0;

                            previousPeriodTdA = currentPeriodTdA;
                            previousPeriodTdB = currentPeriodTdB;
                            currentPeriodTdA = 0;
                            currentPeriodTdB = 0;
                            setElapsedTime(normalPeriodSecs * 3);
                            elapsedTimeAtLastMatchIncidentSecs = normalPeriodSecs * 3;
                            setClockTimeOfLastElapsedTimeFromIncident();
                            break;

                        case AT_THIRD_QUARTER_END:
                            break;

                        case IN_FOURTH_QUARTER:
                            if (extraPeriodSecs == 0 || tdA + conv1A + conv2A + fgA != tdB + conv1B + conv2B + fgB)
                                matchPeriod = AmericanfootballMatchPeriod.MATCH_COMPLETED;
                            else
                                matchPeriod = AmericanfootballMatchPeriod.AT_FULL_TIME;
                            previousPeriodPointsA = currentPeriodPointsA;
                            previousPeriodPointsB = currentPeriodPointsB;
                            currentPeriodPointsA = 0;
                            currentPeriodPointsB = 0;
                            previousPeriodTdA = currentPeriodTdA;
                            previousPeriodTdB = currentPeriodTdB;
                            currentPeriodTdA = 0;
                            currentPeriodTdB = 0;
                            previousHalfPointsA = thirdQuarterPointsA + thirdQuarterPointsA;
                            previousHalfPointsB = thirdQuarterPointsB + thirdQuarterPointsB;
                            setElapsedTime(normalPeriodSecs * 4);
                            elapsedTimeAtLastMatchIncidentSecs = normalPeriodSecs;
                            setClockTimeOfLastElapsedTimeFromIncident();
                            break;

                        case AT_FULL_TIME:
                            break;

                        case IN_EXTRA_TIME_15MINUTES:
                            if (tdA + conv1A + conv2A + fgA == tdB + conv1B + conv2B + fgB) {
                                matchPeriod = AmericanfootballMatchPeriod.AT_EXTRA_TIME_END;
                            } else {
                                matchPeriod = AmericanfootballMatchPeriod.MATCH_COMPLETED;
                            }
                            previousPeriodPointsA = currentPeriodPointsA;
                            previousPeriodPointsB = currentPeriodPointsB;
                            currentPeriodPointsA = 0;
                            currentPeriodPointsB = 0;
                            previousPeriodTdA = currentPeriodTdA;
                            previousPeriodTdB = currentPeriodTdB;
                            currentPeriodTdA = 0;
                            currentPeriodTdB = 0;

                            setElapsedTime(normalPeriodSecs * 4 + (overtimeNo) * extraPeriodSecs);

                            if (overtimeNo == 0) {
                                elapsedTimeAtLastMatchIncidentSecs = normalPeriodSecs;
                            } else {
                                elapsedTimeAtLastMatchIncidentSecs = extraPeriodSecs;
                            }

                            setClockTimeOfLastElapsedTimeFromIncident();
                            break;

                        case AT_EXTRA_TIME_END:
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

    private void setBallHoldingInExtraTime(TeamId teamId) {
        if (matchPeriod == AmericanfootballMatchPeriod.IN_EXTRA_TIME_15MINUTES) {// if in extra time
            if (this.firstSidePossedTheBallExtraTime == TeamId.UNKNOWN) {// first
                this.firstSidePossedTheBallExtraTime = teamId;
            } else if (this.firstSidePossedTheBallExtraTime != TeamId.UNKNOWN
                            && this.secondSidePossedTheBallExtraTime == TeamId.UNKNOWN) { // second
                this.secondSidePossedTheBallExtraTime = teamId;
            }
        }

    }

    private boolean bothPossedBallInOverTime() {
        return (firstSidePossedTheBallExtraTime != TeamId.UNKNOWN && firstSidePossedTheBallExtraTime != TeamId.UNKNOWN);
    }

    private boolean checkIfFirstScoreTDinOverTime() {
        boolean ifFirstScore = false;
        if (matchPeriod.equals(AmericanfootballMatchPeriod.IN_EXTRA_TIME_15MINUTES)) {
            ifFirstScore = ((normalTimePointsA == tdA * pointsForTouchDown + conv1A * pointsForConversion1
                            + conv2A * pointsForConversion2 + fgA * pointsForFieldGoal)
                            && (tdB * pointsForTouchDown + conv1B * pointsForConversion1 + conv2B * pointsForConversion2
                                            + fgB * pointsForFieldGoal == normalTimePointsB));
        }
        return ifFirstScore;
    }

    private boolean fieldAdvancingSuccssed(TeamId ballHoldingNow, FieldPositionType presentFieldPositionType,
                    FieldPositionType newPosition) {
        // System.out.println("Anchor Position "+ presentFieldPositionType);
        boolean movingForward = false;
        if (!presentFieldPositionType.equals(FieldPositionType.UNKNOWN)) // unknown present field position case
            if (!presentFieldPositionType.equals(FieldPositionType.M50YARD)) {

                if (ballHoldingNow == TeamId.A) {
                    // Team A hold ball
                    if (presentFieldPositionType.toString().substring(0, 1).equals("A")) { // from A's ground
                        if (newPosition.toString().substring(0, 1).equals("B")) {// to B's ground
                            movingForward = true;
                        }

                        if (newPosition.toString().substring(0, 1).equals("A")) {// Still A's ground
                            if (Integer.parseInt(newPosition.toString().substring(1, 2)) > Integer
                                            .parseInt(presentFieldPositionType.toString().substring(1, 2)))
                                movingForward = true;
                        }

                    } else if (presentFieldPositionType.toString().substring(0, 1).equals("B")) { // from B's ground
                        if (newPosition.toString().substring(0, 1).equals("B")) {// to B's ground
                            if (Integer.parseInt(newPosition.toString().substring(1, 2)) < Integer
                                            .parseInt(presentFieldPositionType.toString().substring(1, 2)))
                                movingForward = true;
                        }

                        if (newPosition.toString().substring(0, 1).equals("A")) {// back to A's ground
                            // do nothing
                        }

                    } else if (presentFieldPositionType.toString().substring(0, 1).equals("M")) {
                        // from B's ground
                        if (newPosition.toString().substring(0, 1).equals("B")) {// to B's ground
                            movingForward = true;
                        }

                        if (newPosition.toString().substring(0, 1) == "A") {// back to A's ground
                            // do nothing
                        }
                    }

                } else if (ballHoldingNow == TeamId.B) {
                    // Team B hold ball
                    if (presentFieldPositionType.toString().substring(0, 1).equals("A")) { // from A's ground
                        if (newPosition.toString().substring(0, 1).equals("B")) {// to B's ground
                            // DO NOTHING
                        }

                        if (newPosition.toString().substring(0, 1).equals("A")) {// Still A's ground
                            if (Integer.parseInt(newPosition.toString().substring(1, 2)) < Integer
                                            .parseInt(presentFieldPositionType.toString().substring(1, 2)))
                                movingForward = true;
                        }

                    } else if (presentFieldPositionType.toString().substring(0, 1).equals("B")) { // from B's ground
                        if (newPosition.toString().substring(0, 1).equals("B")) {// to B's ground
                            if (Integer.parseInt(newPosition.toString().substring(1, 2)) > Integer
                                            .parseInt(presentFieldPositionType.toString().substring(1, 2)))
                                movingForward = true;
                        }

                        if (newPosition.toString().substring(0, 1).equals("A")) {// back to A's ground
                            movingForward = true;
                        }

                    } else if (presentFieldPositionType.toString().substring(0, 1).equals("M")) {
                        // from B's ground
                        if (newPosition.toString().substring(0, 1).equals("B")) {// to B's ground
                            // DO NOTHING
                        }

                        if (newPosition.toString().substring(0, 1) == "A") {// back to A's ground
                            movingForward = true;
                        }
                    }

                }
            } else { // present field from middle
                if (ballHoldingNow == TeamId.A) {
                    if (newPosition.toString().substring(0, 1) == "B") {// back to A's ground
                        movingForward = true;
                    }
                    if (newPosition.toString().substring(0, 1) == "A") {// back to A's ground
                        movingForward = true;
                    }

                }

                if (ballHoldingNow == TeamId.B) {
                    if (newPosition.toString().substring(0, 1) == "A") {// back to A's ground
                        movingForward = true;
                    }
                    if (newPosition.toString().substring(0, 1) == "B") {// back to A's ground
                        movingForward = true;
                    }
                }

            }

        return movingForward;
    }

    private void checkIfPenatlyOrConversionGoalApproporate(
                    AmericanfootballMatchIncidentType rugbyUnionMatchIncidentType, TeamId teamId) {
        // Penalty or conversion should only happens after penalty fault or a try.
        if (rugbyUnionMatchIncidentType == AmericanfootballMatchIncidentType.CONVERSION_SCORE1
                        || rugbyUnionMatchIncidentType == AmericanfootballMatchIncidentType.CONVERSION_SCORE2
                        || rugbyUnionMatchIncidentType == AmericanfootballMatchIncidentType.CONVERSION_MISS) {
            if (americanfootballMatchStatus
                            .getAmericanfootballMatchStatusType() != AmericanfootballMatchStatusType.CONVERSION)
                throw new IllegalArgumentException("Conversion goal can not be happening now");
            americanfootballMatchStatus.resetAmericanfootballMatchStatus();
            ballPosition.resetFieldPosition();
        }
        // else if (rugbyUnionMatchIncidentType == AmericanfootballMatchIncidentType.PENALTY_GOAL
        // || rugbyUnionMatchIncidentType == AmericanfootballMatchIncidentType.PENALTY_MISS) {
        // if (americanfootballMatchStatus.getAmericanfootballMatchStatus() != AmericanfootballMatchStatusType.PENALTY)
        // throw new IllegalArgumentException("Penalty goal can not be happening now");
        // americanfootballMatchStatus.resetAmericanfootballMatchStatus();
        // }
        //
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

    private void setElapsedTime(int elapsedTimeSecs) {
        this.elapsedTimeSecs = elapsedTimeSecs;
        switch (matchPeriod) {
            case PREMATCH:
            case AT_FIRST_QUARTER_END:
            case AT_SECOND_QUARTER_END:
            case AT_THIRD_QUARTER_END:
            case AT_FULL_TIME:
            case AT_EXTRA_TIME_END:
            case MATCH_COMPLETED:
                /*
                 * do nothing
                 */
                break;
            case IN_FIRST_QUARTER:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs;
                checkWhetherToExtendInjuryTime(elapsedTimeThisPeriodSecs, normalPeriodSecs);
                break;
            case IN_SECOND_QUARTER:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs - normalPeriodSecs;
                checkWhetherToExtendInjuryTime(elapsedTimeThisPeriodSecs, normalPeriodSecs);
                break;
            case IN_THIRD_QUARTER:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs - 2 * normalPeriodSecs;
                checkWhetherToExtendInjuryTime(elapsedTimeThisPeriodSecs, normalPeriodSecs);
                break;
            case IN_FOURTH_QUARTER:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs - 3 * normalPeriodSecs;
                checkWhetherToExtendInjuryTime(elapsedTimeThisPeriodSecs, normalPeriodSecs);
                break;
            case IN_EXTRA_TIME_15MINUTES:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs - 4 * normalPeriodSecs;
                checkWhetherToExtendInjuryTime(elapsedTimeThisPeriodSecs, normalPeriodSecs);
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
    public MatchState copy() {
        AmericanfootballMatchState cc = new AmericanfootballMatchState(matchFormat);
        cc.setEqualTo(this);
        return cc;

    }

    @Override
    public void setEqualTo(MatchState matchState) {
        super.setEqualTo(matchState);
        try {
            this.ballPosition = (BallPosition) (((AmericanfootballMatchState) matchState).ballPosition).clone();
            this.americanfootballMatchStatus =
                            (AmericanfootballMatchStatus) (((AmericanfootballMatchState) matchState).americanfootballMatchStatus)
                                            .clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        this.setTdA(((AmericanfootballMatchState) matchState).getTdA());
        this.setTdB(((AmericanfootballMatchState) matchState).getTdB());

        this.setConv1A(((AmericanfootballMatchState) matchState).getConv1A());
        this.setConv1B(((AmericanfootballMatchState) matchState).getConv1B());

        this.setConv2A(((AmericanfootballMatchState) matchState).getConv2A());
        this.setConv2B(((AmericanfootballMatchState) matchState).getConv2B());

        this.setFgA(((AmericanfootballMatchState) matchState).getFgA());
        this.setFgB(((AmericanfootballMatchState) matchState).getFgB());

        this.setElapsedTime(((AmericanfootballMatchState) matchState).getElapsedTimeSecs());
        this.setElapsedTimeThisPeriodSecs(((AmericanfootballMatchState) matchState).getElapsedTimeThisPeriodSecs());
        // this.setElapsedTimeSecs(((AmericanfootballMatchState) matchState).getElapsedTimeSecs(),
        // ((AmericanfootballMatchState) matchState).getOvertimeNo());
        this.setMatchPeriod(((AmericanfootballMatchState) matchState).getMatchPeriod());
        this.setTeamScoringLastPoint(((AmericanfootballMatchState) matchState).getTeamScoringLastPoint());
        /* update cards infor and sendoff info */
        this.setRedCardA(((AmericanfootballMatchState) matchState).getRedCardA());
        this.setRedCardB(((AmericanfootballMatchState) matchState).getRedCardB());

        this.setPreviousHalfPointsA(((AmericanfootballMatchState) matchState).getPreviousHalfPointsA());
        this.setPreviousHalfPointsB(((AmericanfootballMatchState) matchState).getPreviousHalfPointsB());

        this.tenMinsSinBinTimer_A.clear();
        this.tenMinsSinBinTimer_A =
                        cloneIntegerList(((AmericanfootballMatchState) matchState).getTenMinsSinBinTimer_A());

        this.tenMinsSinBinTimer_B.clear();
        this.tenMinsSinBinTimer_B =
                        cloneIntegerList(((AmericanfootballMatchState) matchState).getTenMinsSinBinTimer_B());
        this.setInjuryTimeSecs(((AmericanfootballMatchState) matchState).getInjuryTimeSecs());
        this.setTenMinsSinBinA(((AmericanfootballMatchState) matchState).getTenMinsSinBinA());
        this.setTenMinsSinBinB(((AmericanfootballMatchState) matchState).getTenMinsSinBinB());
        // this.setShootOutGoalsA(((AmericanfootballMatchState) matchState).getShootOutGoalsA());
        // this.setShootOutGoalsB(((AmericanfootballMatchState) matchState).getShootOutGoalsB());
        this.setShootOutTimeCounter(((AmericanfootballMatchState) matchState).getShootOutTimeCounter());
        this.setOvertimeNo(((AmericanfootballMatchState) matchState).getOvertimeNo());
        this.setGoalSequence(((AmericanfootballMatchState) matchState).getGoalSequence());

        this.setCurrentPeriodPointsA(((AmericanfootballMatchState) matchState).getCurrentPeriodPointsA());
        this.setCurrentPeriodPointsB(((AmericanfootballMatchState) matchState).getCurrentPeriodPointsB());
        this.setPreviousPeriodPointsA(((AmericanfootballMatchState) matchState).getPreviousPeriodPointsA());
        this.setPreviousPeriodPointsB(((AmericanfootballMatchState) matchState).getPreviousPeriodPointsB());

        this.setCurrentPeriodTdA(((AmericanfootballMatchState) matchState).getCurrentPeriodTdA());
        this.setCurrentPeriodTdB(((AmericanfootballMatchState) matchState).getCurrentPeriodTdB());
        this.setPreviousPeriodTdA(((AmericanfootballMatchState) matchState).getPreviousPeriodTdA());
        this.setPreviousPeriodTdB(((AmericanfootballMatchState) matchState).getPreviousPeriodTdB());

        this.setOvertimeNo(((AmericanfootballMatchState) matchState).getOvertimeNo());
        this.setNormalTimePointsA(((AmericanfootballMatchState) matchState).getNormalTimePointsA());
        this.setNormalTimePointsB(((AmericanfootballMatchState) matchState).getNormalTimePointsB());
        this.setElapsedTimeSecsFirstGoal(((AmericanfootballMatchState) matchState).getElapsedTimeSecsFirstGoal());
        this.setFiveMinsPointsA(((AmericanfootballMatchState) matchState).getFiveMinsPointsA());
        this.setFiveMinsPointsB(((AmericanfootballMatchState) matchState).getFiveMinsPointsB());
        this.setPreviousFiveMinsGoalsA(((AmericanfootballMatchState) matchState).getPreviousFiveMinsGoalsA());
        this.setPreviousFiveMinsGoalsB(((AmericanfootballMatchState) matchState).getPreviousFiveMinsGoalsB());
        this.setCurrentFiveMinsEnder(((AmericanfootballMatchState) matchState).getCurrentFiveMinsEnder());
        this.setPreviousFiveMinsEnder(((AmericanfootballMatchState) matchState).getPreviousFiveMinsEnder());
        this.setFiveMinsNo(((AmericanfootballMatchState) matchState).getFiveMinsNo());
        this.setCurrentPeriodTdA(((AmericanfootballMatchState) matchState).getCurrentPeriodTdA());
        this.setCurrentPeriodTdB(((AmericanfootballMatchState) matchState).getCurrentPeriodTdB());
        this.setFirstScoreTDinOverTime(((AmericanfootballMatchState) matchState).isFirstScoreTDinOverTime());
        this.setTotalPointsA(((AmericanfootballMatchState) matchState).getTotalPointsA());
        this.setTotalPointsB(((AmericanfootballMatchState) matchState).getTotalPointsB());

        this.setFirstSidePossedTheBallExtraTime(
                        ((AmericanfootballMatchState) matchState).getFirstSidePossedTheBallExtraTime());
        this.setSecondSidePossedTheBallExtraTime(
                        ((AmericanfootballMatchState) matchState).getSecondSidePossedTheBallExtraTime());

        this.setFirstQuarterPointsA(((AmericanfootballMatchState) matchState).getFirstQuarterPointsA());
        this.setSecondQuarterPointsA(((AmericanfootballMatchState) matchState).getSecondQuarterPointsA());
        this.setThirdQuarterPointsA(((AmericanfootballMatchState) matchState).getThirdQuarterPointsA());
        this.setFourthQuarterPointsA(((AmericanfootballMatchState) matchState).getFourthQuarterPointsA());
        this.setExtraTimeQuarterPointsA(((AmericanfootballMatchState) matchState).getExtraTimeQuarterPointsA());

        this.setFirstQuarterPointsB(((AmericanfootballMatchState) matchState).getFirstQuarterPointsB());
        this.setSecondQuarterPointsB(((AmericanfootballMatchState) matchState).getSecondQuarterPointsB());
        this.setThirdQuarterPointsB(((AmericanfootballMatchState) matchState).getThirdQuarterPointsB());
        this.setFourthQuarterPointsB(((AmericanfootballMatchState) matchState).getFourthQuarterPointsB());
        this.setExtraTimeQuarterPointsB(((AmericanfootballMatchState) matchState).getExtraTimeQuarterPointsB());

        this.setFirstQuarterTDA(((AmericanfootballMatchState) matchState).getFirstQuarterTDA());
        this.setSecondQuarterTDA(((AmericanfootballMatchState) matchState).getSecondQuarterTDA());
        this.setThirdQuarterTDA(((AmericanfootballMatchState) matchState).getThirdQuarterTDA());
        this.setFourthQuarterTDA(((AmericanfootballMatchState) matchState).getFourthQuarterTDA());
        this.setExtraTimeQuarterTDA(((AmericanfootballMatchState) matchState).getExtraTimeQuarterTDA());

        this.setFirstQuarterTDB(((AmericanfootballMatchState) matchState).getFirstQuarterTDB());
        this.setSecondQuarterTDB(((AmericanfootballMatchState) matchState).getSecondQuarterTDB());
        this.setThirdQuarterTDB(((AmericanfootballMatchState) matchState).getThirdQuarterTDB());
        this.setFourthQuarterTDB(((AmericanfootballMatchState) matchState).getFourthQuarterTDB());
        this.setExtraTimeQuarterTDB(((AmericanfootballMatchState) matchState).getExtraTimeQuarterTDB());

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
        if (matchPeriod == AmericanfootballMatchPeriod.MATCH_COMPLETED)
            matchIncidentPrompt = new MatchIncidentPrompt("Match finished");
        else if (matchPeriod == AmericanfootballMatchPeriod.PREMATCH
                        || matchPeriod == AmericanfootballMatchPeriod.AT_FIRST_QUARTER_END
                        || matchPeriod == AmericanfootballMatchPeriod.AT_FULL_TIME
                        || matchPeriod == AmericanfootballMatchPeriod.AT_SECOND_QUARTER_END
                        || matchPeriod == AmericanfootballMatchPeriod.AT_THIRD_QUARTER_END
                        || matchPeriod == AmericanfootballMatchPeriod.AT_EXTRA_TIME_END)
            matchIncidentPrompt = new MatchIncidentPrompt("S - start match", "S");

        else
            matchIncidentPrompt = new MatchIncidentPrompt(
                            "Next event (Nnn-Nothing happened for nn periods, H-Home points, A-Away points "
                                            + "TH/A Touch down home/Away"
                                            + "(A/B)(A/B)(Number)(Number) Team A/B in posession, Field A/B, Yard 1-4, Remaining 1-4"
                                            + "DC down chance consume, C(H/A)(1/2) Conversion home away scored"
                                            + "F(H/A)/(1/2)- Field position, BH/A- Ball possession, E- end period)",
                            "N");
        return matchIncidentPrompt;
    }

    @Override
    public MatchIncident getMatchIncident(String response) {

        AmericanfootballMatchIncidentType rugbyMatchIncidentType = null;
        ElapsedTimeMatchIncidentType elapsedTimeMatchIncidentType = null;
        TeamId teamId = null;
        FieldPositionType fieldPosition = null;
        int incidentElapsedTimeSecs = elapsedTimeSecs;
        @SuppressWarnings("unused")
        int injuryTimeSecs = 0;
        int conversionType = 0;
        MatchIncident incident;
        if (response.length() == 4) {
            // team posession + field + yard + down left
            try {
                String teamInPosession = response.substring(0, 1); // a b
                String fieldSide = response.substring(1, 2); // a b
                int yardIndex = Integer.parseInt(response.substring(2, 3));
                int downsRemain = Integer.parseInt(response.substring(3, 4));

                rugbyMatchIncidentType = AmericanfootballMatchIncidentType.COMBO_SETTING;
                incident = new AmericanfootballMatchIncident(teamInPosession, fieldSide, yardIndex, downsRemain,
                                rugbyMatchIncidentType, incidentElapsedTimeSecs);

            } catch (Exception e) {
                return null;
            }

        } else {
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

                case 'R':
                    try {
                        String item = (response.substring(1, 2));
                        if (item.equalsIgnoreCase("M")) {
                            rugbyMatchIncidentType = AmericanfootballMatchIncidentType.RESET_FIELD_BALL_INFO;
                            break;
                        }
                        if (item.equalsIgnoreCase("D")) {
                            rugbyMatchIncidentType = AmericanfootballMatchIncidentType.DOWNS_NUMBER_RESET;
                            break;
                        }

                    } catch (Exception e) {
                        // int teamCount = 3;
                    }

                    break;
                case 'D':
                    try {
                        String item = (response.substring(1, 2));
                        rugbyMatchIncidentType = AmericanfootballMatchIncidentType.DOWNS_CONSUME;
                        if (item.equalsIgnoreCase("H")) {
                            teamId = TeamId.A;
                            break;
                        }
                        if (item.equalsIgnoreCase("A")) {
                            teamId = TeamId.B;
                            break;
                        }

                    } catch (Exception e) {
                        // int teamCount = 3;
                    }
                    break;

                case 'F':
                    try {
                        String yard1 = (response.substring(1, 2));
                        String yard2 = (response.substring(2));
                        if (yard1.equalsIgnoreCase("M")) {
                            rugbyMatchIncidentType = AmericanfootballMatchIncidentType.FIELD_POSITION_SETTING;
                            fieldPosition = FieldPositionType.M50YARD;
                            break;
                        }
                        if (yard1.equalsIgnoreCase("A")) {
                            rugbyMatchIncidentType = AmericanfootballMatchIncidentType.FIELD_POSITION_SETTING;
                            if (yard2.equals("1")) {
                                fieldPosition = FieldPositionType.B10YARD;
                            } else if (yard2.equals("2")) {
                                fieldPosition = FieldPositionType.B20YARD;
                            } else if (yard2.equals("3")) {
                                fieldPosition = FieldPositionType.B30YARD;
                            } else if (yard2.equals("4")) {
                                fieldPosition = FieldPositionType.B40YARD;
                            }
                            break;
                        }
                        if (yard1.equalsIgnoreCase("H")) {
                            rugbyMatchIncidentType = AmericanfootballMatchIncidentType.FIELD_POSITION_SETTING;
                            if (yard2.equals("1")) {
                                fieldPosition = FieldPositionType.A10YARD;
                            } else if (yard2.equals("2")) {
                                fieldPosition = FieldPositionType.A20YARD;
                            } else if (yard2.equals("3")) {
                                fieldPosition = FieldPositionType.A30YARD;
                            } else if (yard2.equals("4")) {
                                fieldPosition = FieldPositionType.A40YARD;
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
                                rugbyMatchIncidentType = AmericanfootballMatchIncidentType.CONVERSION_START;
                                teamId = TeamId.A;
                            } else if (i.equals("A")) {
                                rugbyMatchIncidentType = AmericanfootballMatchIncidentType.CONVERSION_START;
                                teamId = TeamId.B;
                            }

                            break;

                        case "H":
                            conversionType = Integer.valueOf(response.substring(2, 3));
                            if (conversionType == 1)
                                rugbyMatchIncidentType = AmericanfootballMatchIncidentType.CONVERSION_SCORE1;
                            if (conversionType == 2)
                                rugbyMatchIncidentType = AmericanfootballMatchIncidentType.CONVERSION_SCORE2;
                            teamId = TeamId.A;
                            break;

                        case "A":
                            conversionType = Integer.valueOf(response.substring(2, 3));
                            if (conversionType == 1)
                                rugbyMatchIncidentType = AmericanfootballMatchIncidentType.CONVERSION_SCORE1;
                            if (conversionType == 2)
                                rugbyMatchIncidentType = AmericanfootballMatchIncidentType.CONVERSION_SCORE2;
                            teamId = TeamId.B;
                            break;

                        case "M":
                            rugbyMatchIncidentType = AmericanfootballMatchIncidentType.CONVERSION_MISS;
                            teamId = TeamId.UNKNOWN;
                            break;
                    }

                    break;
                }

                case 'T':// hit a try
                    try {
                        String teamCount = (response.substring(1));
                        if (teamCount.equalsIgnoreCase("H")) { // home team yellow card
                            rugbyMatchIncidentType = AmericanfootballMatchIncidentType.TOUCH_DOWN;
                            teamId = TeamId.A;
                            break;
                        }
                        if (teamCount.equalsIgnoreCase("A")) { // away team yellow card
                            rugbyMatchIncidentType = AmericanfootballMatchIncidentType.TOUCH_DOWN;
                            teamId = TeamId.B;
                            break;
                        }
                    } catch (Exception e) {
                        // int teamCount = 3;
                    }
                    break;

                case 'H':// field goal home
                    try {
                        rugbyMatchIncidentType = AmericanfootballMatchIncidentType.FIELD_GOAL;
                        teamId = TeamId.A;
                        break;
                    } catch (Exception e) {
                        // int teamCount = 3;
                    }
                    break;

                case 'A':// field goal away
                    try {
                        rugbyMatchIncidentType = AmericanfootballMatchIncidentType.FIELD_GOAL;
                        teamId = TeamId.B;
                        break;
                    } catch (Exception e) {
                        // int teamCount = 3;
                    }
                    break;

                case 'S':
                    switch (matchPeriod) {
                        case PREMATCH:
                        case AT_FIRST_QUARTER_END:
                        case AT_SECOND_QUARTER_END:
                        case AT_THIRD_QUARTER_END:
                        case AT_FULL_TIME:
                        case AT_EXTRA_TIME_END:
                            elapsedTimeMatchIncidentType = ElapsedTimeMatchIncidentType.SET_PERIOD_START;
                            break;
                        default:
                            return null;
                    }
                    break;
                case 'E':
                    switch (matchPeriod) {
                        case IN_FIRST_QUARTER:
                        case IN_SECOND_QUARTER:
                        case IN_THIRD_QUARTER:
                        case IN_FOURTH_QUARTER:
                        case IN_EXTRA_TIME_15MINUTES:
                            elapsedTimeMatchIncidentType = ElapsedTimeMatchIncidentType.SET_PERIOD_END;
                            break;
                        default:
                            return null; // invalid input so return null
                    }
            }
            // MatchIncident incident;
            if (rugbyMatchIncidentType != null) {
                if (rugbyMatchIncidentType != AmericanfootballMatchIncidentType.FIELD_POSITION_SETTING) {// &&rugbyMatchIncidentType
                    // !=
                    // AmericanfootballMatchIncidentType.CONVERSION_START&&rugbyMatchIncidentType
                    // !=
                    // AmericanfootballMatchIncidentType.PENALTY_START
                    incident = new AmericanfootballMatchIncident(rugbyMatchIncidentType, incidentElapsedTimeSecs,
                                    teamId);
                } else if (rugbyMatchIncidentType == AmericanfootballMatchIncidentType.CONVERSION_SCORE1) {
                    incident = new AmericanfootballMatchIncident(teamId, rugbyMatchIncidentType,
                                    incidentElapsedTimeSecs);
                } else {
                    incident = new AmericanfootballMatchIncident(rugbyMatchIncidentType, incidentElapsedTimeSecs,
                                    fieldPosition);
                }
            } else {
                incident = new ElapsedTimeMatchIncident(elapsedTimeMatchIncidentType, incidentElapsedTimeSecs);
            }

        }
        return incident;
    }

    private static final String goalsKey = "Points";
    // private static final String shootOutKey = "Shoot out goals";
    private static final String elapsedTimeKey = "Elapsed time";
    // private static final String injuryTimeKey = "Injury time to be played";
    private static final String matchPeriodKey = "Match period";
    private static final String matchStatusKey = "Match Status";
    @JsonIgnore
    private static final String periodSequenceKey = "Period sequence id";
    @JsonIgnore
    private static final String goalSequenceKey = "Goal sequence id";
    @JsonIgnore
    private static final String overTimeSequenceKey = "Over time sequence id";
    @JsonIgnore
    private static final String shootOutCounterKey = "Shoot out chances used";
    private static final String ballPositionKey = "Ball possession";
    // private static final String tenMinsSendOffKey = "Ten Minutes sin bin and Red Card status";
    private static final String injuryTimeKey = "Injury time to be played";
    private static final String fieldPositionKey = "Field position";
    private static final String downNumberKey = "Remaining downs for team";
    private static final String referencePositionKey = "Reference position for downs";
    private static final String extraTimePeriodKey = "Extra period NO.";

    @Override
    public LinkedHashMap<String, String> getAsMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put(goalsKey,
                        String.format("%d-%d",
                                        tdA * pointsForTouchDown + conv1A * pointsForConversion1
                                                        + conv2A * pointsForConversion2 + fgA * pointsForFieldGoal,
                                        tdB * pointsForTouchDown + conv1B * pointsForConversion1
                                                        + conv2B * pointsForConversion2 + fgB * pointsForFieldGoal));
        int mins = elapsedTimeSecs / 60;
        int secs = elapsedTimeSecs - mins * 60;
        map.put(elapsedTimeKey, String.format("%d:%02d", mins, secs));
        map.put(matchPeriodKey, matchPeriod.toString());
        map.put(extraTimePeriodKey, Integer.toString(overtimeNo));
        map.put(matchStatusKey, americanfootballMatchStatus.getAmericanfootballMatchStatusType().toString());
        map.put(ballPositionKey, (this.ballPosition.getBallHoldingNow()).toString());
        map.put(fieldPositionKey, (this.ballPosition.getFieldPosition()).toString());
        map.put(referencePositionKey, (this.ballPosition.getAnchorFieldPosition()).toString());

        map.put(downNumberKey, (Integer.toString(this.ballPosition.getDownsRemain())));
        // map.put(tenMinsSendOffKey, String.format("%d-%d", tenMinsSinBinA + redCardA, tenMinsSinBinB + redCardB));
        // map.put(shootOutKey, String.format("%d-%d", shootOutGoalsA, shootOutGoalsB));
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

    @JsonIgnore
    @Override
    public boolean isMatchCompleted() {
        return (matchPeriod == AmericanfootballMatchPeriod.MATCH_COMPLETED);
    }

    @JsonIgnore
    public boolean isNormalTimeMatchCompleted() {
        return (!(matchPeriod.equals(AmericanfootballMatchPeriod.PREMATCH)
                        || matchPeriod.equals(AmericanfootballMatchPeriod.IN_FIRST_QUARTER)
                        || matchPeriod.equals(AmericanfootballMatchPeriod.IN_SECOND_QUARTER)
                        || matchPeriod.equals(AmericanfootballMatchPeriod.IN_THIRD_QUARTER)
                        || matchPeriod.equals(AmericanfootballMatchPeriod.IN_FOURTH_QUARTER)
                        || matchPeriod.equals(AmericanfootballMatchPeriod.AT_FIRST_QUARTER_END)
                        || matchPeriod.equals(AmericanfootballMatchPeriod.AT_SECOND_QUARTER_END)
                        || matchPeriod.equals(AmericanfootballMatchPeriod.AT_THIRD_QUARTER_END)));
    }

    @JsonIgnore
    public boolean isPeriodCompleted() {
        return (matchPeriod.equals(AmericanfootballMatchPeriod.AT_FIRST_QUARTER_END)
                        || matchPeriod.equals(AmericanfootballMatchPeriod.AT_SECOND_QUARTER_END)
                        || matchPeriod.equals(AmericanfootballMatchPeriod.AT_THIRD_QUARTER_END)
                        || matchPeriod.equals(AmericanfootballMatchPeriod.AT_FULL_TIME)
                        || matchPeriod.equals(AmericanfootballMatchPeriod.MATCH_COMPLETED));
    }

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

    @JsonIgnore
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

    public TeamId getMatchWinner() {
        if (!isMatchCompleted())
            return null;
        else {
            TeamId teamId;
            if (tdA * pointsForTouchDown + conv1A * pointsForConversion1 + conv2A * pointsForConversion2
                            + fgA * pointsForFieldGoal > tdB * pointsForTouchDown + conv1B * pointsForConversion1
                                            + conv2B * pointsForConversion2 + fgB * pointsForFieldGoal)
                teamId = TeamId.A;
            else if (tdA * pointsForTouchDown + conv1A * pointsForConversion1 + conv2A * pointsForConversion2
                            + fgA * pointsForFieldGoal == tdB * pointsForTouchDown + conv1B * pointsForConversion1
                                            + conv2B * pointsForConversion2 + fgB * pointsForFieldGoal)
                teamId = TeamId.UNKNOWN;
            else
                teamId = TeamId.B;
            return teamId;
        }
    }

    // public int getPreviousPeriodGoalsA() {
    // return previousPeriodPointsA;
    // }
    //
    // public int getPreviousPeriodGoalsB() {
    // return previousPeriodPointsB;
    // }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((americanfootballMatchStatus == null) ? 0 : americanfootballMatchStatus.hashCode());
        result = prime * result + ((ballPosition == null) ? 0 : ballPosition.hashCode());
        result = prime * result + conv1A;
        result = prime * result + conv1B;
        result = prime * result + conv2A;
        result = prime * result + conv2B;
        result = prime * result + currentPeriodPointsA;
        result = prime * result + currentPeriodPointsB;
        result = prime * result + currentPeriodTdA;
        result = prime * result + currentPeriodTdB;
        result = prime * result + elapsedTimeSecs;
        result = prime * result + elapsedTimeSecsFirstGoal;
        result = prime * result + elapsedTimeThisPeriodSecs;
        result = prime * result + extraPeriodSecs;
        result = prime * result + fgA;
        result = prime * result + fgB;
        result = prime * result + (firstScoreTDinOverTime ? 1231 : 1237);
        result = prime * result + injuryTimeSecs;
        result = prime * result + ((matchFormat == null) ? 0 : matchFormat.hashCode());
        result = prime * result + ((matchPeriod == null) ? 0 : matchPeriod.hashCode());
        result = prime * result + normalPeriodSecs;
        result = prime * result + normalTimePointsA;
        result = prime * result + normalTimePointsB;
        result = prime * result + overtimeNo;
        result = prime * result + (penaltiesPossible ? 1231 : 1237);
        result = prime * result + previousPeriodPointsA;
        result = prime * result + previousPeriodPointsB;
        result = prime * result + shootOutTimeCounter;
        result = prime * result + tdA;
        result = prime * result + tdB;
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
        AmericanfootballMatchState other = (AmericanfootballMatchState) obj;
        if (americanfootballMatchStatus == null) {
            if (other.americanfootballMatchStatus != null)
                return false;
        } else if (!americanfootballMatchStatus.getAmericanfootballMatchStatusType()
                        .equals(other.americanfootballMatchStatus.getAmericanfootballMatchStatusType()))
            return false;
        if (ballPosition == null) {
            if (other.ballPosition != null)
                return false;
        } else if (!ballPosition.getBallHoldingNow().equals(other.ballPosition.getBallHoldingNow()))
            return false;
        if (conv1A != other.conv1A)
            return false;
        if (conv1B != other.conv1B)
            return false;
        if (conv2A != other.conv2A)
            return false;
        if (conv2B != other.conv2B)
            return false;
        if (currentPeriodPointsA != other.currentPeriodPointsA)
            return false;
        if (currentPeriodPointsB != other.currentPeriodPointsB)
            return false;
        if (currentPeriodTdA != other.currentPeriodTdA)
            return false;
        if (currentPeriodTdB != other.currentPeriodTdB)
            return false;
        if (elapsedTimeSecs != other.elapsedTimeSecs)
            return false;
        if (elapsedTimeSecsFirstGoal != other.elapsedTimeSecsFirstGoal)
            return false;
        if (elapsedTimeThisPeriodSecs != other.elapsedTimeThisPeriodSecs)
            return false;
        if (extraPeriodSecs != other.extraPeriodSecs)
            return false;
        if (fgA != other.fgA)
            return false;
        if (fgB != other.fgB)
            return false;
        if (firstScoreTDinOverTime != other.firstScoreTDinOverTime)
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
        if (previousPeriodPointsA != other.previousPeriodPointsA)
            return false;
        if (previousPeriodPointsB != other.previousPeriodPointsB)
            return false;
        if (shootOutTimeCounter != other.shootOutTimeCounter)
            return false;
        if (tdA != other.tdA)
            return false;
        if (tdB != other.tdB)
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
            case AT_EXTRA_TIME_END:
            case MATCH_COMPLETED:
                break;
            case IN_FIRST_QUARTER:
            case IN_SECOND_QUARTER:
            case IN_THIRD_QUARTER:
            case IN_FOURTH_QUARTER:
            case IN_EXTRA_TIME_15MINUTES:
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
                matchPeriod = AmericanfootballMatchPeriod.IN_FIRST_QUARTER;
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
                    matchPeriod = AmericanfootballMatchPeriod.IN_SECOND_QUARTER;
                    previousPeriodPointsA = currentPeriodPointsA;
                    previousPeriodPointsB = currentPeriodPointsB;
                    currentPeriodPointsA = 0;
                    currentPeriodPointsB = 0;
                    previousPeriodTdA = currentPeriodTdA;
                    previousPeriodTdB = currentPeriodTdB;
                    currentPeriodTdA = 0;
                    currentPeriodTdB = 0;
                }
                break;
            case AT_SECOND_QUARTER_END:
            case IN_SECOND_QUARTER:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs - normalPeriodSecs;
                if (elapsedTimeThisPeriodSecs >= normalPeriodSecs) {
                    elapsedTimeSecs = 2 * normalPeriodSecs;
                    elapsedTimeThisPeriodSecs = 0;
                    matchPeriod = AmericanfootballMatchPeriod.IN_THIRD_QUARTER;

                    previousPeriodPointsA = currentPeriodPointsA;
                    previousPeriodPointsB = currentPeriodPointsB;
                    currentPeriodPointsA = 0;
                    currentPeriodPointsB = 0;
                    previousPeriodTdA = currentPeriodTdA;
                    previousPeriodTdB = currentPeriodTdB;
                    currentPeriodTdA = 0;
                    currentPeriodTdB = 0;

                }
                break;

            case AT_THIRD_QUARTER_END:
            case IN_THIRD_QUARTER:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs - 2 * normalPeriodSecs;
                if (elapsedTimeThisPeriodSecs >= normalPeriodSecs) {
                    elapsedTimeSecs = 3 * normalPeriodSecs;
                    elapsedTimeThisPeriodSecs = 0;
                    matchPeriod = AmericanfootballMatchPeriod.IN_FOURTH_QUARTER;

                    previousPeriodPointsA = currentPeriodPointsA;
                    previousPeriodPointsB = currentPeriodPointsB;
                    currentPeriodPointsA = 0;
                    currentPeriodPointsB = 0;
                    previousPeriodTdA = currentPeriodTdA;
                    previousPeriodTdB = currentPeriodTdB;
                    currentPeriodTdA = 0;
                    currentPeriodTdB = 0;
                }
                break;
            case AT_FULL_TIME:
            case IN_FOURTH_QUARTER:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs - 3 * normalPeriodSecs;
                if (elapsedTimeThisPeriodSecs >= normalPeriodSecs) {
                    if (tdA * pointsForTouchDown + conv1A * pointsForConversion1 + conv2A * pointsForConversion2
                                    + fgA * pointsForFieldGoal != tdB * pointsForTouchDown
                                                    + conv1B * pointsForConversion1 + conv2B * pointsForConversion2
                                                    + fgB * pointsForFieldGoal) {
                        matchPeriod = AmericanfootballMatchPeriod.MATCH_COMPLETED;
                    } else {
                        elapsedTimeSecs = 4 * normalPeriodSecs;
                        elapsedTimeThisPeriodSecs = 0;
                        matchPeriod = AmericanfootballMatchPeriod.IN_EXTRA_TIME_15MINUTES;
                        overtimeNo = 1;
                    }

                    previousPeriodPointsA = currentPeriodPointsA;
                    previousPeriodPointsB = currentPeriodPointsB;
                    currentPeriodPointsA = 0;
                    currentPeriodPointsB = 0;
                    previousPeriodTdA = currentPeriodTdA;
                    previousPeriodTdB = currentPeriodTdB;
                    currentPeriodTdA = 0;
                    currentPeriodTdB = 0;
                }
                break;
            case AT_EXTRA_TIME_END:

            case IN_EXTRA_TIME_15MINUTES:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs - 4 * normalPeriodSecs - (overtimeNo - 1) * extraPeriodSecs;

                // if first score is a touch down, extra time finish
                if (firstScoreTDinOverTime) {
                    matchPeriod = AmericanfootballMatchPeriod.MATCH_COMPLETED;
                    break;
                }

                if (elapsedTimeThisPeriodSecs >= extraPeriodSecs) {
                    elapsedTimeThisPeriodSecs = 0;
                    elapsedTimeSecs = 4 * normalPeriodSecs + (overtimeNo) * extraPeriodSecs;

                    if (tdA * pointsForTouchDown + conv1A * pointsForConversion1 + conv2A * pointsForConversion2
                                    + fgA * pointsForFieldGoal != tdB * pointsForTouchDown
                                                    + conv1B * pointsForConversion1 + conv2B * pointsForConversion2
                                                    + fgB * pointsForFieldGoal) {
                        matchPeriod = AmericanfootballMatchPeriod.MATCH_COMPLETED;
                    } else {
                        matchPeriod = AmericanfootballMatchPeriod.AT_EXTRA_TIME_END;
                        overtimeNo++;

                    }

                    previousPeriodPointsA = currentPeriodPointsA;
                    previousPeriodPointsB = currentPeriodPointsB;
                    currentPeriodPointsA = 0;
                    currentPeriodPointsB = 0;
                    previousPeriodTdA = currentPeriodTdA;
                    previousPeriodTdB = currentPeriodTdB;
                    currentPeriodTdA = 0;
                    currentPeriodTdB = 0;
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

            case IN_EXTRA_TIME_15MINUTES:
                if (overtimeNo == 0) {
                    gamePeriod = GamePeriod.EXTRA_TIME_FIRST_HALF;
                    break;
                }
                if (overtimeNo == 1) {
                    gamePeriod = GamePeriod.EXTRA_TIME_SECOND_HALF;
                    break;
                }
            case AT_EXTRA_TIME_END:
                gamePeriod = GamePeriod.EXTRA_TIME_ENDED;
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

    enum AmericanfootballMatchStatusType {
        NORMAL,
        CONVERSION // PENALTY,
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
     * gets the sequence id to use for match period based markets
     * 
     * @return null if specified set can't occur, else the sequence id
     */
    @JsonIgnore
    public String getSequenceIdForHalf(int periodOffSet) {
        int periodNo = getPeriodNo() + periodOffSet;
        int halfNo = 0;
        halfNo = (periodNo > 2) ? 2 : 1;
        if (periodNo > 4)
            halfNo = 3;
        if (matchFormat.getExtraTimeMinutes() == 0) {
            if (halfNo > 3)
                return null;
            return "H" + Integer.toString(halfNo);
        } else {
            return "H" + Integer.toString(halfNo);
        }
    }

    /**
     * gets the sequence id to use for Goal based markets
     * 
     * @param goalOffset 0 = current , 1 = next etc
     */
    public String getSequenceIdForGoal(int goalOffset) {
        return String.format("G%d", fgA + fgB + 1 + goalOffset);
    }

    /**
     * gets the sequence id to use for Conversion based markets
     * 
     * @param convOffset 0 = current , 1 = next etc
     */
    public String getSequenceIdForConversion(int convOffset) {
        return String.format("C%d", conv1A + conv1B + conv2A + conv2B + 1 + convOffset);
    }

    /**
     * gets the sequence id to use for Try based markets
     * 
     * @param convOffset 0 = current , 1 = next etc
     */
    @JsonIgnore
    public String getSequenceIdForTry(int tryOffset) {
        return String.format("T%d", tdA + tdB + 1 + tryOffset);
    }

    /**
     * returns true if in preMatch
     */
    @Override
    public boolean preMatch() {
        return (matchPeriod == AmericanfootballMatchPeriod.PREMATCH);
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
            case AT_FIRST_QUARTER_END:
            case AT_SECOND_QUARTER_END:
            case AT_THIRD_QUARTER_END:
            case AT_EXTRA_TIME_END:
            case MATCH_COMPLETED:
            case PREMATCH:
                secs = 0;
                break;
            case IN_FIRST_QUARTER:
            case IN_SECOND_QUARTER:
            case IN_THIRD_QUARTER:
            case IN_FOURTH_QUARTER:
                secs = normalPeriodSecs + injuryTimeSecs - elapsedTimeThisPeriodSecs;
                break;
            case IN_EXTRA_TIME_15MINUTES:
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
    public MatchState generateSimpleMatchState() {
        AmericanfootballSimpleMatchState simpleMatchState = new AmericanfootballSimpleMatchState(this.preMatch(),
                        this.isMatchCompleted(), matchPeriod, elapsedTimeSecs, totalPointsA, totalPointsB,
                        firstQuarterTDA, secondQuarterTDA, thirdQuarterTDA, fourthQuarterTDA, extraTimeQuarterTDA,
                        firstQuarterTDB, secondQuarterTDB, thirdQuarterTDB, fourthQuarterTDB, extraTimeQuarterTDB,
                        firstQuarterPointsA, secondQuarterPointsA, thirdQuarterPointsA, fourthQuarterPointsA,
                        extraTimeQuarterPointsA, firstQuarterPointsB, secondQuarterPointsB, thirdQuarterPointsB,
                        fourthQuarterPointsB, extraTimeQuarterPointsB);
        return simpleMatchState;
    }

}
