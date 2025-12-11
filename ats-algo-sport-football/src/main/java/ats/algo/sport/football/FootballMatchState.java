package ats.algo.sport.football;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ats.algo.core.GamePeriod;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchIncidentPrompt;
import ats.algo.core.baseclasses.MatchIncidentResult;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.ElapsedTimeMatchIncident;
import ats.algo.core.common.MatchReferralIncident;
import ats.algo.core.common.PlayerInfo;
import ats.algo.core.common.PlayerMatchIncident;
import ats.algo.core.common.TeamId;
import ats.algo.core.common.TeamSheet;
import ats.algo.core.common.TeamSheetMatchIncident;
import ats.algo.core.matchresult.MatchResultElement;
import ats.algo.core.matchresult.MatchResultMap;
import ats.algo.genericsupportfunctions.ArrayToMap;
import ats.algo.genericsupportfunctions.PairOfIntegers;
import ats.algo.sport.football.FootballMatchIncident.FootballMatchIncidentType;
import ats.core.util.CollectionsUtil;
import ats.core.util.Pair;

/* test */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FootballMatchState extends AlgoMatchState {

    /**
     * MatchState class for Football
     * 
     * @author Jin
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final int defaultInjuryTimeFirstHalfSecs = 120;
    private static final int defaultInjuryTimeSecondHalfSecs = 180;
    private static final int defaultInjuryTimeExtraTimeHalfDeciSeconds = 60;
    private static final int defaultFiveMinutesSecs = 300;
    private static final int fiveMinutesArrayLength = 25;

    // Updated by Robert required by Betstar
    @JsonIgnore
    protected boolean varReferralInProgress;
    private List<GoalInfo> goalInfo;

    private List<GoalInfo> cornerInfo;

    private List<CardInfo> bookingInfo;
    private boolean isInjuryTime;



    private int firstCardTimeSlot = -1;
    private int firstCornerTimeSlot = -1;

    private Boolean secondLegMatch = false;
    private int lastLegGoalA = 0;
    private int lastLegGoalB = 0;
    /* last match incident type for trading rule */

    MatchIncident lastMatchIncidentType;
    /*
     * set by constructor - read-only
     */
    private int normalHalfSecs;
    private int extraHalfSecs;
    private FootballMatchFormat matchFormat;
    /*
     * set by getters/setters
     */

    boolean awayGoalDouble;

    private int goalsA;
    private int goalsB;
    private int normalTimeGoalsA;
    private int normalTimeGoalsB;
    private int extraTimeFHGoalsA;// first half
    private int extraTimeFHGoalsB;// first half
    private int extraTimeSHGoalsA;// second half
    private int extraTimeSHGoalsB;// second half
    private int currentPeriodGoalsA;
    private int currentPeriodGoalsB;
    private int previousPeriodGoalsA;
    private int previousPeriodGoalsB;

    private int cornersA;
    private int cornersB;
    private int normalTimeCornersA;
    private int normalTimeCornersB;
    private int extraTimeFHCornersA;// first half
    private int extraTimeFHCornersB;// first half
    private int extraTimeSHCornersA;// second half
    private int extraTimeSHCornersB;// second half
    private int currentPeriodCornersA;
    private int currentPeriodCornersB;
    private int previousPeriodCornersA;
    private int previousPeriodCornersB;


    private int yellowCardsA;
    private int yellowCardsB;
    private int normalTimeYellowCardsA;
    private int normalTimeYellowCardsB;
    private int extraTimeFHYellowCardsA;// first half
    private int extraTimeFHYellowCardsB;// first half
    private int extraTimeSHYellowCardsA;// second half
    private int extraTimeSHYellowCardsB;// second half
    private int currentPeriodYellowCardsA;
    private int currentPeriodYellowCardsB;
    private int previousPeriodYellowCardsA;
    private int previousPeriodYellowCardsB;

    private int redCardsA;
    private int redCardsB;
    private int normalTimeRedCardsA;
    private int normalTimeRedCardsB;
    private int extraTimeFHRedCardsA;// first half
    private int extraTimeFHRedCardsB;// first half
    private int extraTimeSHRedCardsA;// second half
    private int extraTimeSHRedCardsB;// second half
    private int currentPeriodRedCardsA;
    private int currentPeriodRedCardsB;
    private int previousPeriodRedCardsA;
    private int previousPeriodRedCardsB;

    private int cardsA;
    private int cardsB;
    private int normalTimeCardsA;
    private int normalTimeCardsB;
    private int extraTimeFHCardsA;// first half
    private int extraTimeFHCardsB;// first half
    private int extraTimeSHCardsA;// second half
    private int extraTimeSHCardsB;// second half
    private int currentPeriodCardsA;
    private int currentPeriodCardsB;
    private int previousPeriodCardsA;
    private int previousPeriodCardsB;


    private List<Pair<Integer, Integer>> scoreHistory = CollectionsUtil.newList();

    private List<TeamId> firstHalfGoals = CollectionsUtil.newList();
    private List<TeamId> secondHalfGoals = CollectionsUtil.newList();

    private List<TeamId> firstHalfCorners = CollectionsUtil.newList();
    private List<TeamId> secondHalfCorners = CollectionsUtil.newList();

    private List<TeamId> firstHalfYellowCards = CollectionsUtil.newList();
    private List<TeamId> secondHalfYellowCards = CollectionsUtil.newList();

    private List<TeamId> firstHalfRedCards = CollectionsUtil.newList();
    private List<TeamId> secondHalfRedCards = CollectionsUtil.newList();

    private int elapsedTimeSecs;
    private int elapsedTimeAtLastMatchIncidentSecs; // the time from the last
    private int elapsedTimeAtLastGoalSecs;
    private int elapsedTimeAtLastCornerSecs = -1;
    private FootballMatchPeriod matchPeriodInWhichLastGoalScored;
    private FootballMatchIncidentType lastFootballMatchIncidentType;
    private int elapsedTimeAtLastFootballMatchIncidentSecs;
    private int elapsedTimeThisPeriodSecs;
    private int injuryTimeSecs;
    private FootballMatchPeriod matchPeriod; // the state following the most
                                             // recent MatchIncident;
    private boolean extraTimeEnteredFlag = false;

    private int[] fiveMinsGoalA = new int[fiveMinutesArrayLength]; // the 25th
                                                                   // is the
    // penalty
    private int[] fiveMinsGoalB = new int[fiveMinutesArrayLength];
    private int lastFiveMinsNO = 0;
    private int fiveMinsNo = 0;

    private TeamId teamScoringLastGoal;
    private TeamId teamScoringLastCorner;

    private PairOfIntegers[] goalScoreInPeriodN;
    private PairOfIntegers[] cornersInPeriodN;
    private PairOfIntegers[] yellowCardsInPeriodN;
    private PairOfIntegers[] redCardsInPeriodN;

    private TeamId penaltyStatus;

    private TeamId shootOutFirst;
    private int shooutOutCounterA;
    private int shooutOutCounterB;
    private FootballShootoutInfo penaltyInfo;
    private int shootOutTimeCounter;
    private int shootOutGoalsA;
    private int shootOutGoalsB;

    private String[] playerScoringGoalN;
    private TeamSheet teamSheet;
    private TeamId firstCorner;

    private static final int MAX_PLAYER_ARRAY_SIZE = 30;

    /**
     * Class constructor
     * 
     * @param matchFormat
     */
    public FootballMatchState(MatchFormat matchFormat) {
        super();
        setClockTimeOfLastElapsedTimeFromIncident();
        this.matchFormat = (FootballMatchFormat) matchFormat;
        matchPeriod = FootballMatchPeriod.PREMATCH;
        normalHalfSecs = this.matchFormat.getNormalTimeMinutes() * 30;
        extraHalfSecs = this.matchFormat.getExtraTimeMinutes() * 30;
        penaltyStatus = TeamId.UNKNOWN;
        secondLegMatch = this.matchFormat.isMatchInSecondLeg();
        if (secondLegMatch) {
            String[] goalsLastLeg = this.matchFormat.getLastScore().split(":");
            lastLegGoalA = Integer.valueOf(goalsLastLeg[0]);
            lastLegGoalB = Integer.valueOf(goalsLastLeg[1]);
        }
        elapsedTimeAtLastMatchIncidentSecs = -1;
        elapsedTimeAtLastFootballMatchIncidentSecs = -1;
        elapsedTimeAtLastGoalSecs = -1;
        lastMatchIncidentType = null;
        lastFootballMatchIncidentType = null;
        goalInfo = new ArrayList<GoalInfo>();
        cornerInfo = new ArrayList<GoalInfo>();
        bookingInfo = new ArrayList<CardInfo>();
        isInjuryTime = false;
        shootOutFirst = TeamId.UNKNOWN;
        shooutOutCounterA = 0;
        shooutOutCounterB = 0;
        int numPeriods = 2;
        if (this.matchFormat.getExtraTimeMinutes() > 0)
            numPeriods = 4;
        goalScoreInPeriodN = new PairOfIntegers[numPeriods];
        cornersInPeriodN = new PairOfIntegers[numPeriods];
        yellowCardsInPeriodN = new PairOfIntegers[numPeriods];
        redCardsInPeriodN = new PairOfIntegers[numPeriods];
        for (int i = 0; i < numPeriods; i++) {
            goalScoreInPeriodN[i] = new PairOfIntegers();
            cornersInPeriodN[i] = new PairOfIntegers();
            yellowCardsInPeriodN[i] = new PairOfIntegers();
            redCardsInPeriodN[i] = new PairOfIntegers();
        }
        penaltyInfo = new FootballShootoutInfo(0, 0, TeamId.UNKNOWN, 0, this.matchFormat.isShootOutNewFormat());

        playerScoringGoalN = new String[MAX_PLAYER_ARRAY_SIZE];
        /*
         * array index is goal or red card no, starting at 1, so set entry 0 to a null value
         */
        playerScoringGoalN[0] = "Null_Entry";
        teamSheet = TeamSheet.generateDefaultTeamSheet();
        firstCorner = TeamId.UNKNOWN;
        awayGoalDouble = this.matchFormat.isAwayGoalDouble();
    }

    @Override
    public boolean isVarReferralInProgress() {
        return varReferralInProgress;
    }

    public FootballMatchState() {
        this(new FootballMatchFormat());
    }

    public FootballShootoutInfo getPenaltyInfo() {
        return penaltyInfo;
    }

    public void setPenaltyInfo(FootballShootoutInfo penaltyInfo) {
        this.penaltyInfo = penaltyInfo;
    }

    public int getFirstCardTimeSlot() {
        return firstCardTimeSlot;
    }

    public void setFirstCardTimeSlot(int firstCardTimeSecs) {
        this.firstCardTimeSlot = firstCardTimeSecs;
    }

    public int getFirstCornerTimeSlot() {
        return firstCornerTimeSlot;
    }

    public void setFirstCornerTimeSlot(int firstCornerTimeSecs) {
        this.firstCornerTimeSlot = firstCornerTimeSecs;
    }

    public Boolean getSecondLegMatch() {
        return secondLegMatch;
    }

    public void setSecondLegMatch(Boolean secondLegMatch) {
        this.secondLegMatch = secondLegMatch;
    }

    public boolean isAwayGoalDouble() {
        return awayGoalDouble;
    }

    public void setAwayGoalDouble(boolean awayGoalDouble) {
        this.awayGoalDouble = awayGoalDouble;
    }

    public FootballMatchPeriod getMatchPeriodInWhichLastGoalScored() {
        return matchPeriodInWhichLastGoalScored;
    }

    public void setMatchPeriodInWhichLastGoalScored(FootballMatchPeriod matchPeriodInWhichLastGoalScored) {
        this.matchPeriodInWhichLastGoalScored = matchPeriodInWhichLastGoalScored;
    }

    public static int getDefaultfiveminutessecs() {
        return defaultFiveMinutesSecs;
    }

    public static int getFiveminutesarraylength() {
        return fiveMinutesArrayLength;
    }

    public static int getMaxPlayerArraySize() {
        return MAX_PLAYER_ARRAY_SIZE;
    }

    public static String getTwolegsgoalskey() {
        return twoLegsGoalsKey;
    }

    public static String getYellowcardskey() {
        return yellowCardsKey;
    }

    public static String getRedcardskey() {
        return redCardsKey;
    }

    public static String getShootoutchanceskey() {
        return shootOutChancesKey;
    }

    public static String getShootoutgoalskey() {
        return shootOutGoalsKey;
    }

    public static String getPenaltystatuskey() {
        return penaltyStatusKey;
    }

    public static String getLastincidentkey() {
        return lastIncidentKey;
    }

    public static String getIsininjurytimekey() {
        return isInInjuryTimeKey;
    }

    public static String getGoalinfokey() {
        return goalInfoKey;
    }

    public static String getNextshootoutkey() {
        return nextShootoutKey;
    }

    public static String getShootoutattemptkey() {
        return shootoutAttemptKey;
    }

    public static int getFirstFortyMinutes() {
        return FIRST_FORTY_MINUTES;
    }

    public static int getSecondFortyMinutes() {
        return SECOND_FORTY_MINUTES;
    }

    public void setNormalHalfSecs(int normalHalfSecs) {
        this.normalHalfSecs = normalHalfSecs;
    }

    public void setExtraHalfSecs(int extraHalfSecs) {
        this.extraHalfSecs = extraHalfSecs;
    }

    public void setMatchFormat(FootballMatchFormat matchFormat) {
        this.matchFormat = matchFormat;
    }

    public void setScoreHistory(List<Pair<Integer, Integer>> scoreHistory) {
        this.scoreHistory = scoreHistory;
    }

    public void setFirstHalfCorners(List<TeamId> firstHalfCorners) {
        this.firstHalfCorners = firstHalfCorners;
    }

    public void setSecondHalfCorners(List<TeamId> secondHalfCorners) {
        this.secondHalfCorners = secondHalfCorners;
    }

    public List<TeamId> getFirstHalfYellowCards() {
        return firstHalfYellowCards;
    }

    public void setFirstHalfYellowCards(List<TeamId> firstHalfYellowCards) {
        this.firstHalfYellowCards = firstHalfYellowCards;
    }

    public List<TeamId> getSecondHalfYellowCards() {
        return secondHalfYellowCards;
    }

    public void setSecondHalfYellowCards(List<TeamId> secondHalfYellowCards) {
        this.secondHalfYellowCards = secondHalfYellowCards;
    }

    public List<TeamId> getFirstHalfRedCards() {
        return firstHalfRedCards;
    }

    public void setFirstHalfRedCards(List<TeamId> firstHalfRedCards) {
        this.firstHalfRedCards = firstHalfRedCards;
    }

    public List<TeamId> getSecondHalfRedCards() {
        return secondHalfRedCards;
    }

    public void setSecondHalfRedCards(List<TeamId> secondHalfRedCards) {
        this.secondHalfRedCards = secondHalfRedCards;
    }

    public void setTeamSheet(TeamSheet teamSheet) {
        this.teamSheet = teamSheet;
    }

    public int getExtraTimeFHGoalsA() {
        return extraTimeFHGoalsA;
    }

    public void setExtraTimeFHGoalsA(int extraTimeGoalsA) {
        this.extraTimeFHGoalsA = extraTimeGoalsA;
    }

    public int getExtraTimeFHGoalsB() {
        return extraTimeFHGoalsB;
    }

    public void setExtraTimeFHGoalsB(int extraTimeGoalsB) {
        this.extraTimeFHGoalsB = extraTimeGoalsB;
    }

    public int getExtraTimeSHGoalsA() {
        return extraTimeSHGoalsA;
    }

    public void setExtraTimeSHGoalsA(int extraTimeGoalsA) {
        this.extraTimeSHGoalsA = extraTimeGoalsA;
    }

    public int getExtraTimeSHGoalsB() {
        return extraTimeSHGoalsB;
    }

    public void setExtraTimeSHGoalsB(int extraTimeGoalsB) {
        this.extraTimeSHGoalsB = extraTimeGoalsB;
    }

    public int getExtraTimeFHCornersA() {
        return extraTimeFHCornersA;
    }

    public void setExtraTimeFHCornersA(int extraTimeFHCornersA) {
        this.extraTimeFHCornersA = extraTimeFHCornersA;
    }

    public int getExtraTimeFHCornersB() {
        return extraTimeFHCornersB;
    }

    public void setExtraTimeFHCornersB(int extraTimeFHCornersB) {
        this.extraTimeFHCornersB = extraTimeFHCornersB;
    }

    public int getExtraTimeSHCornersA() {
        return extraTimeSHCornersA;
    }

    public void setExtraTimeSHCornersA(int extraTimeSHCornersA) {
        this.extraTimeSHCornersA = extraTimeSHCornersA;
    }

    public int getExtraTimeSHCornersB() {
        return extraTimeSHCornersB;
    }

    public void setExtraTimeSHCornersB(int extraTimeSHCornersB) {
        this.extraTimeSHCornersB = extraTimeSHCornersB;
    }

    public int getNormalTimeYellowCardsA() {
        return normalTimeYellowCardsA;
    }

    public void setNormalTimeYellowCardsA(int normalTimeYellowCardsA) {
        this.normalTimeYellowCardsA = normalTimeYellowCardsA;
    }

    public int getNormalTimeYellowCardsB() {
        return normalTimeYellowCardsB;
    }

    public void setNormalTimeYellowCardsB(int normalTimeYellowCardsB) {
        this.normalTimeYellowCardsB = normalTimeYellowCardsB;
    }

    public int getExtraTimeFHYellowCardsA() {
        return extraTimeFHYellowCardsA;
    }

    public void setExtraTimeFHYellowCardsA(int extraTimeFHYellowCardsA) {
        this.extraTimeFHYellowCardsA = extraTimeFHYellowCardsA;
    }

    public int getExtraTimeFHYellowCardsB() {
        return extraTimeFHYellowCardsB;
    }

    public void setExtraTimeFHYellowCardsB(int extraTimeFHYellowCardsB) {
        this.extraTimeFHYellowCardsB = extraTimeFHYellowCardsB;
    }

    public int getExtraTimeSHYellowCardsA() {
        return extraTimeSHYellowCardsA;
    }

    public void setExtraTimeSHYellowCardsA(int extraTimeSHYellowCardsA) {
        this.extraTimeSHYellowCardsA = extraTimeSHYellowCardsA;
    }

    public int getExtraTimeSHYellowCardsB() {
        return extraTimeSHYellowCardsB;
    }

    public void setExtraTimeSHYellowCardsB(int extraTimeSHYellowCardsB) {
        this.extraTimeSHYellowCardsB = extraTimeSHYellowCardsB;
    }

    public int getNormalTimeRedCardsA() {
        return normalTimeRedCardsA;
    }

    public void setNormalTimeRedCardsA(int normalTimeRedCardsA) {
        this.normalTimeRedCardsA = normalTimeRedCardsA;
    }

    public int getNormalTimeRedCardsB() {
        return normalTimeRedCardsB;
    }

    public void setNormalTimeRedCardsB(int normalTimeRedCardsB) {
        this.normalTimeRedCardsB = normalTimeRedCardsB;
    }

    public int getExtraTimeFHRedCardsA() {
        return extraTimeFHRedCardsA;
    }

    public void setExtraTimeFHRedCardsA(int extraTimeFHRedCardsA) {
        this.extraTimeFHRedCardsA = extraTimeFHRedCardsA;
    }

    public int getExtraTimeFHRedCardsB() {
        return extraTimeFHRedCardsB;
    }

    public void setExtraTimeFHRedCardsB(int extraTimeFHRedCardsB) {
        this.extraTimeFHRedCardsB = extraTimeFHRedCardsB;
    }

    public int getExtraTimeSHRedCardsA() {
        return extraTimeSHRedCardsA;
    }

    public void setExtraTimeSHRedCardsA(int extraTimeSHRedCardsA) {
        this.extraTimeSHRedCardsA = extraTimeSHRedCardsA;
    }

    public int getExtraTimeSHRedCardsB() {
        return extraTimeSHRedCardsB;
    }

    public void setExtraTimeSHRedCardsB(int extraTimeSHRedCardsB) {
        this.extraTimeSHRedCardsB = extraTimeSHRedCardsB;
    }

    public PairOfIntegers[] getGoalScoreInPeriodN() {
        return goalScoreInPeriodN;
    }

    public void setGoalScoreInPeriodN(PairOfIntegers[] goalScoreInPeriodN) {
        this.goalScoreInPeriodN = goalScoreInPeriodN;
    }

    public PairOfIntegers[] getCornersInPeriodN() {
        return cornersInPeriodN;
    }

    public void setCornersInPeriodN(PairOfIntegers[] cornersInPeriodN) {
        this.cornersInPeriodN = cornersInPeriodN;
    }

    public PairOfIntegers[] getYellowCardsInPeriodN() {
        return yellowCardsInPeriodN;
    }

    public void setYellowCardsInPeriodN(PairOfIntegers[] yellowCardsInPeriodN) {
        this.yellowCardsInPeriodN = yellowCardsInPeriodN;
    }

    public PairOfIntegers[] getRedCardsInPeriodN() {
        return redCardsInPeriodN;
    }

    public void setRedCardsInPeriodN(PairOfIntegers[] redCardsInPeriodN) {
        this.redCardsInPeriodN = redCardsInPeriodN;
    }

    public int getElapsedTimeAtLastGoalSecs() {
        return elapsedTimeAtLastGoalSecs;
    }

    public void setElapsedTimeAtLastGoalSecs(int elapsedTimeAtLastGoalSecs) {
        this.elapsedTimeAtLastGoalSecs = elapsedTimeAtLastGoalSecs;
    }

    public int getElapsedTimeAtLastCornerSecs() {
        return elapsedTimeAtLastCornerSecs;
    }

    public void setElapsedTimeAtLastCornerSecs(int elapsedTimeAtLastCornerSecs) {
        this.elapsedTimeAtLastCornerSecs = elapsedTimeAtLastCornerSecs;
    }

    public int getCardsA() {
        return redCardsA + yellowCardsA;
    }

    public void setCardsA(int cardsA) {
        this.cardsA = cardsA;
    }

    public int getCardsB() {
        return redCardsB + yellowCardsB;
    }

    public void setCardsB(int cardsB) {
        this.cardsB = cardsB;
    }

    public int getNormalTimeCardsA() {
        return normalTimeRedCardsA + normalTimeYellowCardsA;
    }

    public void setNormalTimeCardsA(int normalTimeCardsA) {
        this.normalTimeCardsA = normalTimeCardsA;
    }

    public int getNormalTimeCardsB() {
        return normalTimeRedCardsB + normalTimeYellowCardsB;
    }

    public void setNormalTimeCardsB(int normalTimeCardsB) {
        this.normalTimeCardsB = normalTimeCardsB;
    }

    public int getExtraTimeFHCardsA() {
        return extraTimeFHRedCardsA + extraTimeFHYellowCardsA;
    }

    public void setExtraTimeFHCardsA(int extraTimeFHCardsA) {
        this.extraTimeFHCardsA = extraTimeFHCardsA;
    }

    public int getExtraTimeFHCardsB() {
        return extraTimeFHRedCardsB + extraTimeFHYellowCardsB;
    }

    public void setExtraTimeFHCardsB(int extraTimeFHCardsB) {
        this.extraTimeFHCardsB = extraTimeFHCardsB;
    }

    public int getExtraTimeSHCardsA() {
        return extraTimeSHRedCardsA + extraTimeSHYellowCardsA;
    }

    public void setExtraTimeSHCardsA(int extraTimeSHCardsA) {
        this.extraTimeSHCardsA = extraTimeSHCardsA;
    }

    public int getExtraTimeSHCardsB() {
        return extraTimeSHRedCardsB + extraTimeSHYellowCardsB;
    }

    public void setExtraTimeSHCardsB(int extraTimeSHCardsB) {
        this.extraTimeSHCardsB = extraTimeSHCardsB;
    }

    public int getCurrentPeriodCardsA() {
        return currentPeriodRedCardsA + currentPeriodYellowCardsA;
    }

    public void setCurrentPeriodCardsA(int currentPeriodCardsA) {
        this.currentPeriodCardsA = currentPeriodCardsA;
    }

    public int getCurrentPeriodCardsB() {
        return currentPeriodRedCardsB + currentPeriodYellowCardsB;
    }

    public void setCurrentPeriodCardsB(int currentPeriodCardsB) {
        this.currentPeriodCardsB = currentPeriodCardsB;
    }

    public int getPreviousPeriodCardsA() {
        return previousPeriodRedCardsA + previousPeriodYellowCardsA;
    }

    public void setPreviousPeriodCardsA(int previousPeriodCardsA) {
        this.previousPeriodCardsA = previousPeriodCardsA;
    }

    public int getPreviousPeriodCardsB() {
        return previousPeriodRedCardsB + previousPeriodYellowCardsB;
    }

    public void setPreviousPeriodCardsB(int previousPeriodCardsB) {
        this.previousPeriodCardsB = previousPeriodCardsB;
    }

    /**
     *
     * @param period =0 for first half
     * @param goalsA
     * @param goalsB
     */
    void setGoalScoreInPeriodN(int period, int goalsA, int goalsB) {
        goalScoreInPeriodN[period].A = goalsA;
        goalScoreInPeriodN[period].B = goalsB;
        if (period == 0) {
            for (int i = 0; i < goalsA; i++)
                this.firstHalfGoals.add(TeamId.A);
            for (int i = 0; i < goalsB; i++)
                this.firstHalfGoals.add(TeamId.B);
        }
        if (period == 1) {
            for (int i = 0; i < goalsA; i++)
                this.secondHalfGoals.add(TeamId.A);
            for (int i = 0; i < goalsB; i++)
                this.secondHalfGoals.add(TeamId.B);
        }
    }

    /**
     *
     * @param setNo =0 for first set
     * @param cornersA
     * @param cornersB
     */
    void setCornersInPeriodN(int period, int cornersA, int cornersB) {
        cornersInPeriodN[period].A = cornersA;
        cornersInPeriodN[period].B = cornersB;
        if (period == 0) {
            for (int i = 0; i < cornersA; i++)
                this.firstHalfCorners.add(TeamId.A);
            for (int i = 0; i < cornersB; i++)
                this.firstHalfCorners.add(TeamId.B);
        }
        if (period == 1) {
            for (int i = 0; i < cornersA; i++)
                this.secondHalfCorners.add(TeamId.A);
            for (int i = 0; i < cornersB; i++)
                this.secondHalfCorners.add(TeamId.B);
        }
    }

    /**
     *
     * @param setNo =0 for first set
     * @param yellowCardsA
     * @param yellowCardsB
     */
    void setYellowCardsInPeriodN(int period, int yellowCardsA, int yellowCardsB) {
        yellowCardsInPeriodN[period].A = yellowCardsA;
        yellowCardsInPeriodN[period].B = yellowCardsB;
        if (period == 0) {
            for (int i = 0; i < yellowCardsA; i++)
                this.firstHalfYellowCards.add(TeamId.A);
            for (int i = 0; i < yellowCardsB; i++)
                this.firstHalfYellowCards.add(TeamId.B);
        }
        if (period == 1) {
            for (int i = 0; i < yellowCardsA; i++)
                this.secondHalfYellowCards.add(TeamId.A);
            for (int i = 0; i < yellowCardsB; i++)
                this.secondHalfYellowCards.add(TeamId.B);
        }
    }

    /**
     *
     * @param setNo =0 for first set
     * @param gamesA
     * @param gamesB
     */
    void setRedCardsInPeriodN(int period, int redCardsA, int redCardsB) {
        redCardsInPeriodN[period].A = redCardsA;
        redCardsInPeriodN[period].B = redCardsB;
        if (period == 0) {
            for (int i = 0; i < redCardsA; i++)
                this.firstHalfRedCards.add(TeamId.A);
            for (int i = 0; i < redCardsB; i++)
                this.firstHalfRedCards.add(TeamId.B);
        }
        if (period == 1) {
            for (int i = 0; i < redCardsA; i++)
                this.secondHalfRedCards.add(TeamId.A);
            for (int i = 0; i < redCardsB; i++)
                this.secondHalfRedCards.add(TeamId.B);
        }
    }

    public boolean isExtraTimeEnteredFlag() {
        return extraTimeEnteredFlag;
    }

    public void setExtraTimeEnteredFlag(boolean extraTimeEnteredFlag) {
        this.extraTimeEnteredFlag = extraTimeEnteredFlag;
    }

    public TeamId getFirstCorner() {
        return firstCorner;
    }

    public void setFirstCorner(TeamId firstCorner) {
        this.firstCorner = firstCorner;
    }

    /**
     * Get shoot out attempts for team A
     * 
     * @return shooutOutCounterA
     */
    public int getShooutOutCounterA() {
        return shooutOutCounterA;
    }

    /**
     * Set shoot out attempts for team A
     * 
     * @param shooutOutCounterA
     */
    public void setShooutOutCounterA(int shooutOutCounterA) {
        this.shooutOutCounterA = shooutOutCounterA;
    }

    /**
     * Get shoot out attempts for team B
     * 
     * @return shooutOutCounterB
     */
    public int getShooutOutCounterB() {
        return shooutOutCounterB;
    }

    /**
     * Set shoot out attempts for team B
     * 
     * @param shooutOutCounterB
     */
    public void setShooutOutCounterB(int shooutOutCounterB) {
        this.shooutOutCounterB = shooutOutCounterB;
    }

    /**
     * Get first team start the shoot out
     * 
     * @return shootOutFirst
     */
    public TeamId getShootOutFirst() {
        return shootOutFirst;
    }

    /**
     * Set first team start the shoot out
     * 
     * @param shootOutFirst
     */
    public void setShootOutFirst(TeamId shootOutFirst) {
        this.shootOutFirst = shootOutFirst;
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

    public List<GoalInfo> getCornerInfo() {
        return cornerInfo;
    }

    public void setCornerInfo(List<GoalInfo> cornerInfo) {
        this.cornerInfo = cornerInfo;
    }

    public List<CardInfo> getBookingInfo() {
        return bookingInfo;
    }


    public void setBookingInfo(List<CardInfo> bookingInfo) {
        this.bookingInfo = bookingInfo;
    }

    /**
     * Check if in the injury time
     * 
     * @return isInjuryTime
     */

    public boolean isInjuryTime() {
        return isInjuryTime;
    }

    /**
     * Set if match in injury time
     * 
     * @param isInjuryTime
     */
    public void setInjuryTime(boolean isInjuryTime) {
        this.isInjuryTime = isInjuryTime;
    }

    /**
     * Get team is shooting penalty
     * 
     * @return penaltyStatus
     */
    public TeamId getPenaltyStatus() {
        return penaltyStatus;
    }

    /**
     * Set team is shooting penalty
     * 
     * @param penaltyStatus
     */
    public void setPenaltyStatus(TeamId penaltyStatus) {
        this.penaltyStatus = penaltyStatus;
    }

    /**
     * Get last match incident type
     * 
     * @return lastMatchIncidentType
     */
    public MatchIncident getLastMatchIncidentType() {
        return lastMatchIncidentType;
    }

    /**
     * Set last match incident type
     * 
     * @param lastMatchIncidentType
     */
    public void setLastMatchIncidentType(MatchIncident lastMatchIncidentType) {
        this.lastMatchIncidentType = lastMatchIncidentType;
    }


    public int getYellowCardsA() {
        return yellowCardsA;
    }

    public void setYellowCardsA(int yellowCardsA) {
        this.yellowCardsA = yellowCardsA;
    }

    public int getYellowCardsB() {
        return yellowCardsB;
    }

    public void setYellowCardsB(int yellowCardsB) {
        this.yellowCardsB = yellowCardsB;
    }

    public int getRedCardsA() {
        return redCardsA;
    }

    public void setRedCardsA(int redCardsA) {
        this.redCardsA = redCardsA;
    }

    public int getRedCardsB() {
        return redCardsB;
    }

    public void setRedCardsB(int redCardsB) {
        this.redCardsB = redCardsB;
    }

    /**
     * Get no of corners in normal game for team A
     * 
     * @return normalTimeCornersA
     */
    public int getNormalTimeCornersA() {
        return normalTimeCornersA;
    }

    /**
     * Set no of corners in normal game for team A
     * 
     * @param normalTimeCornersA
     */
    public void setNormalTimeCornersA(int normalTimeCornersA) {
        this.normalTimeCornersA = normalTimeCornersA;
    }

    /**
     * Get no of corners in normal game for team B
     * 
     * @return normalTimeCornersB
     */
    public int getNormalTimeCornersB() {
        return normalTimeCornersB;
    }

    /**
     * Set no of corners in normal game for team B
     * 
     * @param normalTimeCornersB
     */
    public void setNormalTimeCornersB(int normalTimeCornersB) {
        this.normalTimeCornersB = normalTimeCornersB;
    }

    /**
     * Get no of goals in normal game for team A
     * 
     * @return normalTimeGoalsA
     */
    public int getNormalTimeGoalsA() {
        return normalTimeGoalsA;
    }

    /**
     * Set no of goals in normal game for team A
     * 
     * @param normalTimeGoalsA
     */
    public void setNormalTimeGoalsA(int normalTimeGoalsA) {
        this.normalTimeGoalsA = normalTimeGoalsA;
    }

    /**
     * Get no of goals in normal game for team B
     * 
     * @return normalTimeGoalsB
     */
    public int getNormalTimeGoalsB() {
        return normalTimeGoalsB;
    }

    /**
     * Set no of goals in normal game for team B
     * 
     * @param normalTimeGoalsB
     */
    public void setNormalTimeGoalsB(int normalTimeGoalsB) {
        this.normalTimeGoalsB = normalTimeGoalsB;
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
     * Get no of shoot out goals for team A
     * 
     * @return shootOutGoalsA
     */
    public int getShootOutGoalsA() {
        return shootOutGoalsA;
    }

    /**
     * Set no of shoot out goals for team A
     * 
     * @param shootOutGoalsA
     */
    public void setShootOutGoalsA(int shootOutGoalsA) {
        this.shootOutGoalsA = shootOutGoalsA;
    }

    /**
     * Get no of shoot out goals for team B
     * 
     * @return shootOutGoalsB
     */
    public int getShootOutGoalsB() {
        return shootOutGoalsB;
    }

    /**
     * Set no of shoot out goals for team B
     * 
     * @param shootOutGoalsB
     */
    public void setShootOutGoalsB(int shootOutGoalsB) {
        this.shootOutGoalsB = shootOutGoalsB;
    }

    /**
     * Get sequence number of the last five minutes
     * 
     * @return lastFiveMinsNO
     */
    public int getLastFiveMinsNO() {
        return lastFiveMinsNO;
    }

    /**
     * Set sequence number of the last five minutes
     * 
     * @param lastFiveMinsNO
     */
    public void setLastFiveMinsNO(int lastFiveMinsNO) {
        this.lastFiveMinsNO = lastFiveMinsNO;
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
     * Set current five minutes no
     * 
     * @param fiveMinsNo
     */
    public void setFiveMinsNo(int fiveMinsNo) {
        this.fiveMinsNo = fiveMinsNo;
    }

    /**
     * Get goals for team A in an integer array, each element in the array is associate to a certain 5 minutes time slot
     * 
     * @return fiveMinsGoalA
     */
    public int[] getFiveMinsGoalA() {
        return fiveMinsGoalA;
    }

    /**
     * Set goals for team A in an integer array, each element in the array is associate to a certain 5 minutes time slot
     * 
     * @param fiveMinsGoalA
     */
    public void setFiveMinsGoalA(int[] fiveMinsGoalA) {
        this.fiveMinsGoalA = fiveMinsGoalA;
    }

    /**
     * Get goals for team B in an integer array, each element in the array is associate to a certain 5 minutes time slot
     * 
     * @return fiveMinsGoalB
     */
    public int[] getFiveMinsGoalB() {
        return fiveMinsGoalB;
    }

    /**
     * Set goals for team B in an integer array, each element in the array is associate to a certain 5 minutes time slot
     * 
     * @param fiveMinsGoalB
     */
    public void setFiveMinsGoalB(int[] fiveMinsGoalB) {
        this.fiveMinsGoalB = fiveMinsGoalB;
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
    void setGoalsA(int goalsA) {
        this.goalsA = goalsA;
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
    void setGoalsB(int goalsB) {
        this.goalsB = goalsB;
    }

    /**
     * Get # corners by team A in the previous period of the match
     * 
     * @return previousPeriodCornersA
     */
    public int getPreviousPeriodCornersA() {
        return previousPeriodCornersA;
    }

    /**
     * Set # corners by team A in the previous period of the match
     * 
     * @param previousPeriodCornersA
     */
    public void setPreviousPeriodCornersA(int previousPeriodCornersA) {
        this.previousPeriodCornersA = previousPeriodCornersA;
    }

    /**
     * Get # corners by team B in the previous period of the match
     * 
     * @return previousPeriodCornersB
     */
    public int getPreviousPeriodCornersB() {
        return previousPeriodCornersB;
    }

    /**
     * Set # corners by team B in the previous period of the match
     * 
     * @param previousPeriodCornersB
     */
    public void setPreviousPeriodCornersB(int previousPeriodCornersB) {
        this.previousPeriodCornersB = previousPeriodCornersB;
    }

    /**
     * Get # corners by team A
     * 
     * @return cornersA
     */
    public int getCornersA() {
        return cornersA;
    }

    /**
     * Set # corners by team A
     * 
     * @param cornersA
     */
    void setCornersA(int cornersA) {
        this.cornersA = cornersA;
    }

    /**
     * Get # corners by team B
     * 
     * @return cornersB
     */
    public int getCornersB() {
        return cornersB;
    }

    /**
     * Set # corners by team B
     * 
     * @param cornersB
     */
    void setCornersB(int cornersB) {
        this.cornersB = cornersB;
    }

    /**
     * Set the elapsed time in seconds since the start of the match
     * 
     * @param elapsedTimeSecs
     */
    void setElapsedTimeSecs(int elapsedTimeSecs) {
        this.elapsedTimeSecs = elapsedTimeSecs;
    }

    /**
     * @return the elapsed time in seconds since the start of the match
     */
    public int getElapsedTimeSecs() {
        return elapsedTimeSecs;
    }

    /**
     * Get the elapsed time in seconds for this match period
     * 
     * @return elapsedTimeThisPeriodSecs
     */
    public int getElapsedTimeThisPeriodSecs() {
        return elapsedTimeThisPeriodSecs;
    }

    /**
     * Set the elapsed time in seconds for this match period
     * 
     * @param elapsedTimeThisPeriodSecs
     */
    void setElapsedTimeThisPeriodSecs(int elapsedTimeThisPeriodSecs) {
        this.elapsedTimeThisPeriodSecs = elapsedTimeThisPeriodSecs;
    }

    /**
     * Get the last football match incident for a game
     * 
     * @return lastFootballMatchIncidentType
     */
    public FootballMatchIncidentType getLastFootballMatchIncidentType() {
        return lastFootballMatchIncidentType;
    }

    /**
     * Set the last football incident
     * 
     * @param lastFootballMatchIncidentType
     */
    public void setLastFootballMatchIncidentType(FootballMatchIncidentType lastFootballMatchIncidentType) {
        this.lastFootballMatchIncidentType = lastFootballMatchIncidentType;
    }

    /**
     * Get the team scored previous goal
     * 
     * @return teamScoringLastGoal
     */
    public TeamId getTeamScoringLastGoal() {
        return teamScoringLastGoal;
    }

    /**
     * Set the team scored previous goal
     * 
     * @param teamScoringLastGoal
     */
    void setTeamScoringLastGoal(TeamId teamScoringLastGoal) {
        this.teamScoringLastGoal = teamScoringLastGoal;
    }

    /**
     * Get the team kicks previous corner
     * 
     * @return teamScoringLastCorner
     */
    public TeamId getTeamScoringLastCorner() {
        return teamScoringLastCorner;
    }

    /**
     * Set the team kicks previous corner
     * 
     * @param teamScoringLastCorner
     */
    void setTeamScoringLastCorner(TeamId teamScoringLastCorner) {
        this.teamScoringLastCorner = teamScoringLastCorner;
    }

    /**
     * Get the corner no of team A in the current match period
     * 
     * @return currentPeriodCornersA
     */
    public int getCurrentPeriodCornersA() {
        return currentPeriodCornersA;
    }

    /**
     * Set the corner no of team A in the current match period
     * 
     * @param currentPeriodCornersA
     */
    void setCurrentPeriodCornersA(int currentPeriodCornersA) {
        this.currentPeriodCornersA = currentPeriodCornersA;
    }

    /**
     * Get the corner no of team B in the current match period
     * 
     * @return currentPeriodCornersB
     */
    public int getCurrentPeriodCornersB() {
        return currentPeriodCornersB;
    }

    /**
     * Set the corner no of team B in the current match period
     * 
     * @param currentPeriodCornersB
     */
    void setCurrentPeriodCornersB(int currentPeriodCornersB) {
        this.currentPeriodCornersB = currentPeriodCornersB;
    }

    public int getCurrentPeriodYellowCardsA() {
        return currentPeriodYellowCardsA;
    }

    public void setCurrentPeriodYellowCardsA(int currentPeriodYellowCardsA) {
        this.currentPeriodYellowCardsA = currentPeriodYellowCardsA;
    }

    public int getCurrentPeriodYellowCardsB() {
        return currentPeriodYellowCardsB;
    }

    public void setCurrentPeriodYellowCardsB(int currentPeriodYellowCardsB) {
        this.currentPeriodYellowCardsB = currentPeriodYellowCardsB;
    }

    public int getPreviousPeriodYellowCardsA() {
        return previousPeriodYellowCardsA;
    }

    public void setPreviousPeriodYellowCardsA(int previousPeriodYellowCardsA) {
        this.previousPeriodYellowCardsA = previousPeriodYellowCardsA;
    }

    public int getPreviousPeriodYellowCardsB() {
        return previousPeriodYellowCardsB;
    }

    public void setPreviousPeriodYellowCardsB(int previousPeriodYellowCardsB) {
        this.previousPeriodYellowCardsB = previousPeriodYellowCardsB;
    }

    public int getCurrentPeriodRedCardsA() {
        return currentPeriodRedCardsA;
    }

    public void setCurrentPeriodRedCardsA(int currentPeriodRedCardsA) {
        this.currentPeriodRedCardsA = currentPeriodRedCardsA;
    }

    public int getCurrentPeriodRedCardsB() {
        return currentPeriodRedCardsB;
    }

    public void setCurrentPeriodRedCardsB(int currentPeriodRedCardsB) {
        this.currentPeriodRedCardsB = currentPeriodRedCardsB;
    }

    public int getPreviousPeriodRedCardsA() {
        return previousPeriodRedCardsA;
    }

    public void setPreviousPeriodRedCardsA(int previousPeriodRedCardsA) {
        this.previousPeriodRedCardsA = previousPeriodRedCardsA;
    }

    public int getPreviousPeriodRedCardsB() {
        return previousPeriodRedCardsB;
    }

    public void setPreviousPeriodRedCardsB(int previousPeriodRedCardsB) {
        this.previousPeriodRedCardsB = previousPeriodRedCardsB;
    }

    /**
     * Get the length of each half in seconds
     * 
     * @return normalHalfSecs
     */
    public int getNormalHalfSecs() {
        return normalHalfSecs;
    }

    /**
     * Get the length of each extra time half in seconds
     * 
     * @return extraHalfSecs
     */
    public int getExtraHalfSecs() {
        return extraHalfSecs;
    }

    /**
     * Get the elapsed time at the previous match incident
     * 
     * @return elapsedTimeAtLastMatchIncidentSecs
     */
    public int getElapsedTimeAtLastMatchIncidentSecs() {
        return elapsedTimeAtLastMatchIncidentSecs;
    }

    /**
     * Set the elapsed time at the previous match incident
     * 
     * @param elapsedTimeAtLastMatchIncidentSecs
     */
    public void setElapsedTimeAtLastMatchIncidentSecs(int elapsedTimeAtLastMatchIncidentSecs) {
        this.elapsedTimeAtLastMatchIncidentSecs = elapsedTimeAtLastMatchIncidentSecs;
    }

    /**
     * Get elapsed time since last football match incident
     * 
     * @return
     */
    public int getElapsedTimeAtLastFootballMatchIncidentSecs() {
        return elapsedTimeAtLastFootballMatchIncidentSecs;
    }

    /**
     * sets the elapsed time at the previous football match Incident
     * 
     * @param elapsedTimeAtLastFootballMatchIncidentSecs
     */
    public void setElapsedTimeAtLastFootballMatchIncidentSecs(int elapsedTimeAtLastFootballMatchIncidentSecs) {
        this.elapsedTimeAtLastFootballMatchIncidentSecs = elapsedTimeAtLastFootballMatchIncidentSecs;
    }

    @Override
    public TeamSheet getTeamSheet() {
        return teamSheet;
    }

    /**
     * Get the default injury time in the first half
     * 
     * @return defaultInjuryTimeFirstHalfSecs
     */
    public static int getDefaultinjurytimefirsthalfsecs() {
        return defaultInjuryTimeFirstHalfSecs;
    }

    /**
     * Get the default injury time in the second half
     * 
     * @return defaultInjuryTimeSecondHalfSecs
     */
    public static int getDefaultinjurytimesecondhalfsecs() {
        return defaultInjuryTimeSecondHalfSecs;
    }

    /**
     * Get the default injury time in the first half of extra time match
     * 
     * @return defaultInjuryTimeExtraTimeHalfDeciSeconds
     */
    public static int getDefaultinjurytimeextratimehalfdeciseconds() {
        return defaultInjuryTimeExtraTimeHalfDeciSeconds;
    }

    static String getGoalskey() {
        return goalsKey;
    }

    static String getElapsedtimekey() {
        return elapsedTimeKey;
    }

    static String getInjurytimekey() {
        return injuryTimeKey;
    }

    static String getMatchperiodkey() {
        return matchPeriodKey;
    }

    static String getPeriodsequencekey() {
        return periodSequenceKey;
    }

    static String getGoalsequencekey() {
        return goalSequenceKey;
    }

    static String getCornerskey() {
        return cornersKey;
    }

    static String getCornersequencekey() {
        return cornerSequenceKey;
    }

    /**
     * Set goals of team A in the current match period
     * 
     * @param currentPeriodGoalsA
     */
    void setCurrentPeriodGoalsA(int currentPeriodGoalsA) {
        this.currentPeriodGoalsA = currentPeriodGoalsA;
    }

    /**
     * Set goals of team B in the current match period
     * 
     * @param currentPeriodGoalsB
     */
    void setCurrentPeriodGoalsB(int currentPeriodGoalsB) {
        this.currentPeriodGoalsB = currentPeriodGoalsB;
    }

    /**
     * Set goals of team A in the previous match period
     * 
     * @param previousPeriodGoalsA
     */
    void setPreviousPeriodGoalsA(int previousPeriodGoalsA) {
        this.previousPeriodGoalsA = previousPeriodGoalsA;
    }

    /**
     * Set goals of team B in the previous match period
     * 
     * @param previousPeriodGoalsB
     */
    void setPreviousPeriodGoalsB(int previousPeriodGoalsB) {
        this.previousPeriodGoalsB = previousPeriodGoalsB;
    }

    /**
     * Get the goal sequence no
     * 
     * @return
     */

    public int getGoalNo() {
        return goalsA + goalsB + 1;
    }

    /**
     * Get the corner sequence no
     * 
     * @return
     */

    public int getCornerNo() {
        return cornersA + cornersB + 1;
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
                n = 3;
                break;
            case IN_EXTRA_TIME_HALF_TIME:
            case IN_EXTRA_TIME_SECOND_HALF:
                n = 4;
                break;
            case IN_SHOOTOUT:
                n = 5;
                break;
            case MATCH_COMPLETED:
                n = 6;
                break;
        }
        return n;
    }

    /**
     * Check if this match is a second leg match
     * 
     * @return
     */

    public Boolean getIsSecondLegMatch() {
        return secondLegMatch;
    }

    /**
     * Set second leg match format to true or false
     * 
     * @param isSecondLegMatch
     */

    public void setIsSecondLegMatch(Boolean secondLegMatch) {
        this.secondLegMatch = secondLegMatch;
    }

    /**
     * Get goals of team A in the last leg match
     * 
     * @return lastLegGoalA
     */

    public int getLastLegGoalA() {
        return lastLegGoalA;
    }

    /**
     * Set goals of team A in the last leg match
     * 
     * @param lastLegGoalA
     */

    public void setLastLegGoalA(int lastLegGoalA) {
        this.lastLegGoalA = lastLegGoalA;
    }

    /**
     * Get goals of team B in the last leg match
     * 
     * @return lastLegGoalB
     */

    public int getLastLegGoalB() {
        return lastLegGoalB;
    }

    /**
     * Set goals of team B in the last leg match
     * 
     * @param lastLegGoalB
     */

    public void setLastLegGoalB(int lastLegGoalB) {
        this.lastLegGoalB = lastLegGoalB;
    }


    public String getGoalSequenceId() {
        return String.format("G%d", getGoalNo());
    }


    public String getCornerSequenceId() {
        return String.format("C%d", getCornerNo());
    }


    public String getPeriodSequenceId() {
        return String.format("H%d", getPeriodNo());
    }

    /**
     * Get injury time in seconds
     * 
     * @return injuryTimeSecs
     */

    public int getInjuryTimeSecs() {
        return injuryTimeSecs;
    }

    /**
     * Set injury time in seconds
     * 
     * @param injuryTimeSecs
     */

    public void setInjuryTimeSecs(int injuryTimeSecs) {
        this.injuryTimeSecs = injuryTimeSecs;
    }

    /**
     * Get the current matchPeriod
     * 
     * @return matchPeriod
     */

    public FootballMatchPeriod getMatchPeriod() {
        return matchPeriod;
    }

    /**
     * Set the current matchPeriod
     * 
     * @param matchPeriod
     */
    public void setMatchPeriod(FootballMatchPeriod matchPeriod) {
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


    public String[] getPlayerScoringGoalN() {
        return playerScoringGoalN;
    }


    public String getPlayerScoringGoalN(int goalNo) {
        return playerScoringGoalN[goalNo];
    }


    public void setPlayerScoringGoalN(String[] playerScoringGoalN) {
        this.playerScoringGoalN = playerScoringGoalN;
    }


    public List<Pair<Integer, Integer>> getScoreHistory() {
        return scoreHistory;
    }


    public List<TeamId> getFirstHalfGoals() {
        return firstHalfGoals;
    };

    public void setFirstHalfGoals(List<TeamId> firstHalfGoals) {
        this.firstHalfGoals = firstHalfGoals;
    }

    public void setSecondHalfGoals(List<TeamId> secondHalfGoals) {
        this.secondHalfGoals = secondHalfGoals;
    }


    public int getFirstHalfGoalsA() {
        int goals = 0;
        for (TeamId team : firstHalfGoals) {
            if (team == TeamId.A)
                goals++;
        }
        return goals;
    };


    public int getFirstHalfGoalsB() {
        int goals = 0;
        for (TeamId team : firstHalfGoals) {
            if (team == TeamId.B)
                goals++;
        }
        return goals;
    };


    public int getSecondHalfGoalsA() {
        int goals = 0;
        for (TeamId team : secondHalfGoals) {
            if (team == TeamId.A)
                goals++;
        }
        return goals;
    };


    public int getSecondHalfGoalsB() {
        int goals = 0;
        for (TeamId team : secondHalfGoals) {
            if (team == TeamId.B)
                goals++;
        }
        return goals;
    };


    public List<TeamId> getSecondHalfGoals() {
        return secondHalfGoals;
    };


    public List<TeamId> getFirstHalfCorners() {
        return firstHalfCorners;
    }

    public List<TeamId> getSecondHalfCorners() {
        return secondHalfCorners;
    }


    public boolean wasTeamLeading(TeamId id) {
        for (Pair<Integer, Integer> score : scoreHistory) {
            if (TeamId.A == id && score.getT1() > score.getT2() || TeamId.B == id && score.getT2() > score.getT1()) {
                return true;
            }
        }
        return false;
    }


    public boolean wasTeamBehind(TeamId id) {
        for (Pair<Integer, Integer> score : scoreHistory) {
            if (TeamId.A == id && score.getT1() < score.getT2() || TeamId.B == id && score.getT2() < score.getT1()) {
                return true;
            }
        }
        return false;
    }

    @Override
    /**
     * matchIncident may be one of two types: FootballMatchIncident or ElapsedTimeMatchIncident
     */
    public MatchIncidentResult updateStateForIncident(MatchIncident matchIncident,
                    boolean autosyncMatchStateToFeedOnMismatch) {
        // update var_ref variable
        Object subType = matchIncident.getIncidentSubType();

        checkUpdateReferralProgress(subType);

        if (!((matchIncident instanceof FootballMatchIncident) || (matchIncident instanceof ElapsedTimeMatchIncident)
                        || (matchIncident instanceof TeamSheetMatchIncident)
                        || ((matchIncident instanceof PlayerMatchIncident))))
            return super.updateStateForIncident(matchIncident, autosyncMatchStateToFeedOnMismatch);



        lastFiveMinsNO = fiveMinsNo;

        if (extraTimeEnteredFlag == false && this.extraHalfSecs != 0) {// if
                                                                       // never
                                                                       // entered
                                                                       // extra
                                                                       // time
            extraTimeEnteredFlag = (this.matchPeriod).equals(FootballMatchPeriod.IN_EXTRA_TIME_FIRST_HALF)
                            || (this.matchPeriod).equals(FootballMatchPeriod.IN_EXTRA_TIME_HALF_TIME)
                            || (this.matchPeriod).equals(FootballMatchPeriod.IN_EXTRA_TIME_SECOND_HALF);
        }

        int elapsedTimeFromIncident = matchIncident.getElapsedTimeSecs();
        if (elapsedTimeFromIncident > 0) {
            /*
             * -1 or 0 means incident does not contain a valid elapsed time
             */

            setElapsedTime(elapsedTimeFromIncident);
            elapsedTimeAtLastMatchIncidentSecs = elapsedTimeSecs;
            setClockTimeOfLastElapsedTimeFromIncident();
        }
        Class<? extends MatchIncident> clazz = matchIncident.getClass();
        TeamSheet updatedTeamSheet;
        if (clazz == TeamSheetMatchIncident.class) {
            lastMatchIncidentType = (TeamSheetMatchIncident) matchIncident;
            switch (((TeamSheetMatchIncident) matchIncident).getIncidentSubType()) {
                case INITIAL_TEAM_SHEET:
                    teamSheet = ((TeamSheetMatchIncident) matchIncident).getTeamSheet();
                    break;
                case UPDATED_TEAM_SHEET:
                    updatedTeamSheet = ((TeamSheetMatchIncident) matchIncident).getTeamSheet();
                    for (Entry<String, PlayerInfo> e : updatedTeamSheet.getTeamSheetMap().entrySet()) {
                        String key = e.getKey();
                        PlayerInfo updatedPlayerInfo = e.getValue();
                        PlayerInfo playerInfo = teamSheet.getTeamSheetMap().get(key);
                        if (playerInfo == null) {
                            /*
                             * player is not on the team sheet - so ignore
                             */
                        } else
                            playerInfo.setPlayerStatus(updatedPlayerInfo.getPlayerStatus());
                    }
                    break;
                /*
                 * Don't think we need this change
                 */
                // case NAME_CHANGE:
                // String oldPlayerName = ((TeamSheetMatchIncident)
                // matchIncident).getOldName();
                // String newPlayerName = ((TeamSheetMatchIncident)
                // matchIncident).getNewName();
                // Map<String, PlayerInfo> teamSheetMap =
                // teamSheet.getTeamSheetMap();
                // PlayerInfo playerInfo = teamSheetMap.get(oldPlayerName);
                // if (playerInfo == null) {
                // /*
                // * old player not in list, so do nothing
                // */
                // } else {
                // /*
                // * remove the entry for the old key and add again with the new key
                // */
                // teamSheetMap.remove(oldPlayerName);
                // playerInfo.setPlayerName(newPlayerName);
                // teamSheetMap.put(newPlayerName, playerInfo);
                // }
                // break;

            }
            return matchPeriod;
        } else if (clazz == PlayerMatchIncident.class) {
            PlayerMatchIncident playerMatchIncident = (PlayerMatchIncident) matchIncident;
            int seqNo = playerMatchIncident.getSequenceNo();
            if (seqNo > MAX_PLAYER_ARRAY_SIZE)
                throw new IllegalArgumentException(
                                "Max array size for playerMatchIncidents exceeded.  MatchIncident: " + matchIncident);
            switch (playerMatchIncident.getIncidentSubType()) {
                case GOAL_SCORER:
                    this.playerScoringGoalN[playerMatchIncident.getSequenceNo()] = playerMatchIncident.getPlayerKey();
                    break;
            }
            return matchPeriod;
        } else if (clazz == FootballMatchIncident.class) {

            TeamId teamId = ((FootballMatchIncident) matchIncident).getTeamId();
            lastMatchIncidentType = (FootballMatchIncident) matchIncident; // for
                                                                           // trading
                                                                           // rules
                                                                           // specifically
            lastFootballMatchIncidentType = (FootballMatchIncidentType) lastMatchIncidentType.getIncidentSubType();
            elapsedTimeAtLastFootballMatchIncidentSecs = elapsedTimeSecs;
            FootballMatchStateFromFeed stateFromFeed = null;
            if (((FootballMatchIncident) matchIncident).getFootballMatchStateFromFeed() != null) {
                stateFromFeed = ((FootballMatchIncident) matchIncident).getFootballMatchStateFromFeed().copy();
            }

            switch (((FootballMatchIncident) matchIncident).getIncidentSubType()) {
                case SHOOTOUT_MISS:
                    if (teamId == TeamId.A)
                        penaltyInfo.updateShootoutListStatusA(
                                        ((FootballMatchIncident) matchIncident).getIncidentSubType());
                    else if (teamId == TeamId.B)
                        penaltyInfo.updateShootoutListStatusB(
                                        ((FootballMatchIncident) matchIncident).getIncidentSubType());

                    shootOutTimeCounter++;
                    this.penaltyInfo.setShootingCounter(shootOutTimeCounter);
                    if (isShootoutSettled(this.penaltyInfo.startedPenalty)) {
                        if (teamId == TeamId.A)
                            goalsB++;
                        if (teamId == TeamId.B)
                            goalsA++;
                        matchPeriod = FootballMatchPeriod.MATCH_COMPLETED;
                    }
                    break;
                case SHOOTOUT_START:
                    shootOutFirst = matchIncident.getTeamId();
                    penaltyInfo.setStartedPenalty(shootOutFirst);
                    break;
                case GOAL:
                    elapsedTimeAtLastGoalSecs = elapsedTimeSecs;
                    matchPeriodInWhichLastGoalScored = matchPeriod;

                    penaltyStatus = TeamId.UNKNOWN; // always reset penaltystatus if
                                                    // a goal happens

                    GoalInfo goalInfoCurrent = new GoalInfo();
                    goalInfoCurrent.setMins(elapsedTimeSecs / 60);
                    goalInfoCurrent.setTeam(teamId);
                    goalInfoCurrent.setMatchPeriodIndex(this.getPeriodNo());
                    goalInfo.add(goalInfoCurrent);
                    switch (teamId) {
                        case A:
                            if (matchPeriod.equals(FootballMatchPeriod.IN_SHOOTOUT)) {
                                if (this.penaltyInfo.startedPenalty == TeamId.UNKNOWN && shootOutTimeCounter % 2 == 0)
                                    this.penaltyInfo.startedPenalty = TeamId.A;

                                penaltyInfo.updateShootoutListStatusA(
                                                ((FootballMatchIncident) matchIncident).getIncidentSubType());
                                shootOutGoalsA++;
                                shootOutTimeCounter++;
                                this.penaltyInfo.setShootingCounter(this.shootOutTimeCounter);
                                this.penaltyInfo.setPenaltyScoreA(this.shootOutGoalsA);
                                if (isShootoutSettled(this.penaltyInfo.startedPenalty)) {
                                    this.goalsA++;
                                    matchPeriod = FootballMatchPeriod.MATCH_COMPLETED;
                                }
                            } else {
                                if (matchPeriod.equals(FootballMatchPeriod.IN_FIRST_HALF)
                                                || matchPeriod.equals(FootballMatchPeriod.IN_SECOND_HALF))
                                    normalTimeGoalsA++;

                                if (matchPeriod.equals(FootballMatchPeriod.IN_EXTRA_TIME_FIRST_HALF))
                                    extraTimeFHGoalsA++;
                                if (matchPeriod.equals(FootballMatchPeriod.IN_EXTRA_TIME_SECOND_HALF))
                                    extraTimeSHGoalsA++;

                                goalsA++;
                                currentPeriodGoalsA++;
                                teamScoringLastGoal = TeamId.A;
                                fiveMinsGoalA[fiveMinsNo]++;
                            }
                            break;
                        case B:
                            if (matchPeriod.equals(FootballMatchPeriod.IN_SHOOTOUT)) {
                                if (this.penaltyInfo.startedPenalty == TeamId.UNKNOWN && shootOutTimeCounter % 2 == 0)
                                    this.penaltyInfo.startedPenalty = TeamId.B;
                                penaltyInfo.updateShootoutListStatusB(
                                                ((FootballMatchIncident) matchIncident).getIncidentSubType());
                                shootOutGoalsB++;
                                shootOutTimeCounter++;
                                this.penaltyInfo.setShootingCounter(this.shootOutTimeCounter);
                                this.penaltyInfo.setPenaltyScoreB(this.shootOutGoalsB);
                                if (isShootoutSettled(this.penaltyInfo.startedPenalty)) {
                                    this.goalsB++;
                                    matchPeriod = FootballMatchPeriod.MATCH_COMPLETED;
                                }
                            } else {// Non shootout goals
                                if (matchPeriod.equals(FootballMatchPeriod.IN_FIRST_HALF)
                                                || matchPeriod.equals(FootballMatchPeriod.IN_SECOND_HALF))
                                    normalTimeGoalsB++;

                                if (matchPeriod.equals(FootballMatchPeriod.IN_EXTRA_TIME_FIRST_HALF))
                                    extraTimeFHGoalsB++;
                                if (matchPeriod.equals(FootballMatchPeriod.IN_EXTRA_TIME_SECOND_HALF))
                                    extraTimeSHGoalsB++;

                                goalsB++;
                                currentPeriodGoalsB++;
                                teamScoringLastGoal = TeamId.B;
                                fiveMinsGoalB[fiveMinsNo]++;
                            }
                            break;
                        case UNKNOWN:
                            throw new IllegalArgumentException("TeamId must be specified for goal scored");
                    }
                    scoreHistory.add(new Pair<>(goalsA, goalsB));
                    if (matchPeriod == FootballMatchPeriod.IN_FIRST_HALF) {
                        firstHalfGoals.add(teamId);
                    } else if (matchPeriod == FootballMatchPeriod.IN_SECOND_HALF) {
                        secondHalfGoals.add(teamId);
                    }
                    break;
                case CORNER:
                    GoalInfo cornerInfoCurrent = new GoalInfo();
                    cornerInfoCurrent.setMins(elapsedTimeSecs / 60);
                    cornerInfoCurrent.setTeam(teamId);
                    cornerInfoCurrent.setMatchPeriodIndex(this.getPeriodNo());
                    cornerInfo.add(cornerInfoCurrent);
                    elapsedTimeAtLastCornerSecs = elapsedTimeSecs;
                    switch (teamId) {
                        case A:
                            if (matchPeriod.equals(FootballMatchPeriod.IN_FIRST_HALF)
                                            || matchPeriod.equals(FootballMatchPeriod.IN_SECOND_HALF))
                                normalTimeCornersA++;
                            cornersA++;
                            currentPeriodCornersA++;
                            teamScoringLastCorner = TeamId.A;
                            if (firstCorner == TeamId.UNKNOWN)
                                firstCorner = TeamId.A;
                            break;
                        case B:
                            if (matchPeriod.equals(FootballMatchPeriod.IN_FIRST_HALF)
                                            || matchPeriod.equals(FootballMatchPeriod.IN_SECOND_HALF))
                                normalTimeCornersB++;
                            cornersB++;
                            currentPeriodCornersB++;
                            teamScoringLastCorner = TeamId.B;
                            if (firstCorner == TeamId.UNKNOWN)
                                firstCorner = TeamId.B;
                            break;
                        case UNKNOWN:
                            throw new IllegalArgumentException("TeamId must be specified for corner scored");
                    }
                    if (firstCornerTimeSlot == -1 && this.getPeriodNo() < 3)
                        firstCornerTimeSlot = Math.min(this.elapsedTimeSecs, 5399) / 600;
                    if (FootballMatchPeriod.IN_FIRST_HALF == matchPeriod) {
                        firstHalfCorners.add(teamId);
                    } else if (FootballMatchPeriod.IN_SECOND_HALF == matchPeriod) {
                        secondHalfCorners.add(teamId);
                    }
                    break;
                case DANGEROUS_ATTACK:
                case SAFE:
                case ATTACK:
                case THROWIN:
                case FREE_KICK:
                case SHOT_ON_TARGET:
                case SHOT_OFF_TARGET:
                case GOAL_KICK:
                    // Do nothing for now - add later
                    break;
                case YELLOW_CARD:
                    CardInfo cardInfoCurrent = new CardInfo();
                    cardInfoCurrent.setMins(elapsedTimeSecs / 60);
                    cardInfoCurrent.setTeam(teamId);
                    cardInfoCurrent.setMatchPeriodIndex(this.getPeriodNo());
                    cardInfoCurrent.setColor("Y");
                    bookingInfo.add(cardInfoCurrent);

                    if (firstCardTimeSlot == -1 && this.getPeriodNo() < 3)
                        firstCardTimeSlot = Math.min(this.elapsedTimeSecs, 5399) / 600;
                    switch (teamId) {
                        case A:
                            yellowCardsA++;
                            currentPeriodYellowCardsA++;
                            break;
                        case B:
                            yellowCardsB++;
                            currentPeriodYellowCardsB++;
                            break;
                        default:
                            throw new IllegalArgumentException("Should not go here");
                    }
                    if (FootballMatchPeriod.IN_FIRST_HALF == matchPeriod) {
                        firstHalfYellowCards.add(teamId);
                    } else if (FootballMatchPeriod.IN_SECOND_HALF == matchPeriod) {
                        firstHalfYellowCards.add(teamId);
                    }
                    break;
                case RED_CARD:

                    cardInfoCurrent = new CardInfo();
                    cardInfoCurrent.setMins(elapsedTimeSecs / 60);
                    cardInfoCurrent.setTeam(teamId);
                    cardInfoCurrent.setMatchPeriodIndex(this.getPeriodNo());
                    cardInfoCurrent.setColor("R");
                    bookingInfo.add(cardInfoCurrent);
                    if (firstCardTimeSlot == -1 && this.getPeriodNo() < 3)
                        firstCardTimeSlot = Math.min(this.elapsedTimeSecs, 5399) / 600;
                    switch (teamId) {
                        case A:
                            redCardsA++;
                            break;
                        case B:
                            redCardsB++;
                            break;
                        default:
                            throw new IllegalArgumentException("Should not go here");
                    }
                    if (FootballMatchPeriod.IN_FIRST_HALF == matchPeriod) {
                        firstHalfRedCards.add(teamId);
                    } else if (FootballMatchPeriod.IN_SECOND_HALF == matchPeriod) {
                        firstHalfRedCards.add(teamId);
                    }
                    break;

                case SET_INJURY_TIME:
                    injuryTimeSecs = ((FootballMatchIncident) matchIncident).getInjuryTimeSecs();
                    break;
                case PENALTY_CONFIRMED:
                    penaltyStatus = teamId;
                    break;
                case PENALTY_MISSED:
                    penaltyStatus = TeamId.UNKNOWN;
                    break;

                case CORNER_NOT_CONFIRMED:
                case GOAL_NOT_CONFIRMED:
                case PENALTY:
                case PENALTY_NOT_CONFIRMED:
                case POSSIBLE_CORNER:
                case POSSIBLE_GOAL:
                case POSSIBLE_PENALTY:
                case POSSIBLE_RED_CARD:
                case POSSIBLE_YELLOW_CARD:
                case RED_CARD_NOT_CONFIRMED:
                case YELLOW_CARD_NOT_CONFIRMED:
                    break;

                default:
                    throw new IllegalArgumentException("Unknown incident");
            }
            if (this.matchPeriod == FootballMatchPeriod.IN_SHOOTOUT)
                this.penaltyInfo.updateShootingNext();
            copyMatchStateFromFeed(stateFromFeed, autosyncMatchStateToFeedOnMismatch);

        } else { // must be ElapsedTimeMatchIncident
            this.setClockStatus(((ElapsedTimeMatchIncident) matchIncident).getIncidentSubType());
            lastMatchIncidentType = (ElapsedTimeMatchIncident) matchIncident;
            /*
             * Added in score sync logic in elapased time match incident
             */
            switch (((ElapsedTimeMatchIncident) matchIncident).getIncidentSubType()) {
                case SET_MATCH_CLOCK: // used to explicitly set the elapsed time in
                                      // the match
                    // if (matchPeriod.equals(FootballMatchPeriod.IN_SHOOTOUT)) {
                    // shootOutTimeCounter++;
                    // if (isShootoutSettled()) {
                    // this.goalsA += shootOutGoalsA > shootOutGoalsB ? 1 : 0;
                    // this.goalsB += shootOutGoalsA > shootOutGoalsB ? 0 : 1;
                    // matchPeriod = FootballMatchPeriod.MATCH_COMPLETED;
                    // }
                    // }

                    switch (matchPeriod) {
                        case IN_FIRST_HALF:
                            if (elapsedTimeThisPeriodSecs >= normalHalfSecs)
                                isInjuryTime = true;
                            break; // invalid input
                        case IN_SECOND_HALF:
                            elapsedTimeThisPeriodSecs = elapsedTimeSecs - normalHalfSecs;
                            if (elapsedTimeThisPeriodSecs >= normalHalfSecs)
                                isInjuryTime = true;
                            break; // invalid input
                        case IN_EXTRA_TIME_FIRST_HALF:
                            if (elapsedTimeThisPeriodSecs >= extraHalfSecs)
                                isInjuryTime = true;
                            break; // invalid input
                        case IN_EXTRA_TIME_SECOND_HALF:
                            if (elapsedTimeThisPeriodSecs >= extraHalfSecs)
                                isInjuryTime = true;
                            break; // invalid input
                        default:
                            break;
                    }
                    break;
                case SET_PERIOD_START: // start the next period - automatically
                                       // starts the match timer
                    switch (matchPeriod) {
                        case PREMATCH:
                            elapsedTimeThisPeriodSecs = 0;
                            matchPeriod = FootballMatchPeriod.IN_FIRST_HALF;
                            injuryTimeSecs = defaultInjuryTimeFirstHalfSecs;
                            break;
                        case IN_FIRST_HALF:
                            break; // invalid input
                        case AT_HALF_TIME:
                            elapsedTimeThisPeriodSecs = 0;
                            matchPeriod = FootballMatchPeriod.IN_SECOND_HALF;
                            injuryTimeSecs = defaultInjuryTimeSecondHalfSecs;
                            break;
                        case IN_SECOND_HALF:
                            break; // invalid input
                        case AT_FULL_TIME:
                            elapsedTimeThisPeriodSecs = 0;
                            matchPeriod = FootballMatchPeriod.IN_EXTRA_TIME_FIRST_HALF;
                            injuryTimeSecs = defaultInjuryTimeExtraTimeHalfDeciSeconds;
                            break;
                        case IN_EXTRA_TIME_FIRST_HALF:
                            break; // invalid input
                        case IN_EXTRA_TIME_HALF_TIME:
                            elapsedTimeThisPeriodSecs = 0;
                            matchPeriod = FootballMatchPeriod.IN_EXTRA_TIME_SECOND_HALF;
                            injuryTimeSecs = defaultInjuryTimeExtraTimeHalfDeciSeconds;
                            break;
                        case IN_EXTRA_TIME_SECOND_HALF:
                            break; // invalid input
                        case MATCH_COMPLETED:
                            break; // invalid input
                        default:
                            new IllegalArgumentException("Should not go here");
                            break;
                    }
                    break;
                case SET_PERIOD_END: // end the current period - automatically stops
                                     // the match timer
                    isInjuryTime = false;
                    switch (matchPeriod) {
                        case PREMATCH:
                            break; // invalid input
                        case IN_FIRST_HALF:
                            matchPeriod = FootballMatchPeriod.AT_HALF_TIME;
                            previousPeriodGoalsA = currentPeriodGoalsA;
                            previousPeriodGoalsB = currentPeriodGoalsB;
                            currentPeriodGoalsA = 0;
                            currentPeriodGoalsB = 0;

                            previousPeriodCornersA = currentPeriodCornersA;
                            previousPeriodCornersB = currentPeriodCornersB;
                            currentPeriodCornersA = 0;
                            currentPeriodCornersB = 0;

                            previousPeriodYellowCardsA = currentPeriodYellowCardsA;
                            previousPeriodYellowCardsB = currentPeriodYellowCardsB;
                            currentPeriodYellowCardsA = 0;
                            currentPeriodYellowCardsB = 0;

                            previousPeriodRedCardsA = currentPeriodRedCardsA;
                            previousPeriodRedCardsB = currentPeriodRedCardsB;
                            currentPeriodRedCardsA = 0;
                            currentPeriodRedCardsB = 0;

                            setElapsedTime(normalHalfSecs); // Careful : Injure time if
                                                            // needs to be added in
                            elapsedTimeAtLastMatchIncidentSecs = normalHalfSecs;
                            setClockTimeOfLastElapsedTimeFromIncident();
                            break;
                        case AT_HALF_TIME:
                            break; // invalid input
                        case IN_SECOND_HALF:
                            if (extraHalfSecs == 0)
                                matchPeriod = FootballMatchPeriod.MATCH_COMPLETED;
                            else {
                                if (secondLegMatch) {
                                    if (secondLegMatchSettled())
                                        matchPeriod = FootballMatchPeriod.MATCH_COMPLETED;
                                    else
                                        matchPeriod = FootballMatchPeriod.AT_FULL_TIME;
                                } else {
                                    if (goalsA != goalsB)
                                        matchPeriod = FootballMatchPeriod.MATCH_COMPLETED;
                                    else
                                        matchPeriod = FootballMatchPeriod.AT_FULL_TIME;
                                }
                            }
                            previousPeriodGoalsA = currentPeriodGoalsA;
                            previousPeriodGoalsB = currentPeriodGoalsB;
                            currentPeriodGoalsA = 0;
                            currentPeriodGoalsB = 0;

                            previousPeriodCornersA = currentPeriodCornersA;
                            previousPeriodCornersB = currentPeriodCornersB;
                            currentPeriodCornersA = 0;
                            currentPeriodCornersB = 0;

                            previousPeriodYellowCardsA = currentPeriodYellowCardsA;
                            previousPeriodYellowCardsB = currentPeriodYellowCardsB;
                            currentPeriodYellowCardsA = 0;
                            currentPeriodYellowCardsB = 0;

                            previousPeriodRedCardsA = currentPeriodRedCardsA;
                            previousPeriodRedCardsB = currentPeriodRedCardsB;
                            currentPeriodRedCardsA = 0;
                            currentPeriodRedCardsB = 0;

                            setElapsedTime(normalHalfSecs * 2);
                            elapsedTimeAtLastMatchIncidentSecs = normalHalfSecs;
                            setClockTimeOfLastElapsedTimeFromIncident();
                            break;
                        case AT_FULL_TIME:
                            break; // invalid input
                        case IN_EXTRA_TIME_FIRST_HALF:
                            matchPeriod = FootballMatchPeriod.IN_EXTRA_TIME_HALF_TIME;
                            previousPeriodGoalsA = currentPeriodGoalsA;
                            previousPeriodGoalsB = currentPeriodGoalsB;
                            currentPeriodGoalsA = 0;
                            currentPeriodGoalsB = 0;

                            previousPeriodCornersA = currentPeriodCornersA;
                            previousPeriodCornersB = currentPeriodCornersB;
                            currentPeriodCornersA = 0;
                            currentPeriodCornersB = 0;

                            previousPeriodYellowCardsA = currentPeriodYellowCardsA;
                            previousPeriodYellowCardsB = currentPeriodYellowCardsB;
                            currentPeriodYellowCardsA = 0;
                            currentPeriodYellowCardsB = 0;

                            previousPeriodRedCardsA = currentPeriodRedCardsA;
                            previousPeriodRedCardsB = currentPeriodRedCardsB;
                            currentPeriodRedCardsA = 0;
                            currentPeriodRedCardsB = 0;

                            setElapsedTime(normalHalfSecs * 2 + extraHalfSecs);
                            elapsedTimeAtLastMatchIncidentSecs = extraHalfSecs;
                            setClockTimeOfLastElapsedTimeFromIncident();
                            break;
                        case IN_EXTRA_TIME_HALF_TIME:
                            break; // invalid input
                        case IN_EXTRA_TIME_SECOND_HALF:

                            if (secondLegMatch) {
                                if (secondLegMatchSettled())
                                    matchPeriod = FootballMatchPeriod.MATCH_COMPLETED;
                                else
                                    matchPeriod = FootballMatchPeriod.IN_SHOOTOUT;
                            } else {
                                if (goalsA != goalsB)
                                    matchPeriod = FootballMatchPeriod.MATCH_COMPLETED;
                                else
                                    matchPeriod = FootballMatchPeriod.IN_SHOOTOUT;
                            }

                            previousPeriodGoalsA = currentPeriodGoalsA;
                            previousPeriodGoalsB = currentPeriodGoalsB;
                            currentPeriodGoalsA = 0;
                            currentPeriodGoalsB = 0;

                            previousPeriodCornersA = currentPeriodCornersA;
                            previousPeriodCornersB = currentPeriodCornersB;
                            currentPeriodCornersA = 0;
                            currentPeriodCornersB = 0;

                            previousPeriodYellowCardsA = currentPeriodYellowCardsA;
                            previousPeriodYellowCardsB = currentPeriodYellowCardsB;
                            currentPeriodYellowCardsA = 0;
                            currentPeriodYellowCardsB = 0;

                            previousPeriodRedCardsA = currentPeriodRedCardsA;
                            previousPeriodRedCardsB = currentPeriodRedCardsB;
                            currentPeriodRedCardsA = 0;
                            currentPeriodRedCardsB = 0;

                            setElapsedTime(normalHalfSecs * 2 + extraHalfSecs * 2);
                            elapsedTimeAtLastMatchIncidentSecs = extraHalfSecs;
                            setClockTimeOfLastElapsedTimeFromIncident();

                            break;
                        case MATCH_COMPLETED:
                            break; // invalid input
                        default:
                            new IllegalArgumentException("Should not go here");
                            break;
                    }
                    break;
                case SET_STOP_MATCH_CLOCK: // stop the independent match clock timer
                                           // - for those sports where this is
                                           // possible
                case SET_START_MATCH_CLOCK: // start the match clock timer
                    /*
                     * do nothing - not valid incidents for football
                     */
                    break;
            }
        }
        fiveMinsNo = this.generateFiveMinsNo();

        FootballMatchStateFromFeed stateFromFeed = null;
        if (matchIncident.getMatchStateFromFeed() != null) {
            stateFromFeed = (FootballMatchStateFromFeed) matchIncident.getMatchStateFromFeed();
            copyMatchStateFromFeed(stateFromFeed, autosyncMatchStateToFeedOnMismatch);
        }

        return matchPeriod;
    }

    /**
     * Only set referral flat to false when referral is completed
     */
    private void checkUpdateReferralProgress(Object subType) {
        if (!this.varReferralInProgress) {
            if (subType == MatchReferralIncident.MatchReferralIncidentType.VAR_REFERRAL_CONFIRMED
                            || subType == MatchReferralIncident.MatchReferralIncidentType.VAR_POSSIBLE_REFERRAL) {
                this.varReferralInProgress = true;
            }
        } else { // already in referral status
            if (subType == MatchReferralIncident.MatchReferralIncidentType.VAR_REFERRAL_COMPLETED
                            || isMatchIncidentCompleteRfereal(subType)) {
                this.varReferralInProgress = false;
            }
        }

    }

    private boolean isMatchIncidentCompleteRfereal(Object subType) {
        for (FootballMatchIncident.FootballMatchIncidentType type : FootballMatchIncident.FootballMatchIncidentType
                        .values())
            if (type == subType)
                return true;
        return false;
    }

    private boolean secondLegMatchSettled() {
        boolean matchSettled = false;
        if ((goalsA == lastLegGoalB) && (goalsB == lastLegGoalA)) {
            matchSettled = false;
        } else {
            matchSettled = true;
        }
        return matchSettled;
    }


    TeamId getTwoLegsMatchWinner() {
        if (!secondLegMatch)
            return null;

        if (awayGoalDouble) {
            if ((goalsA + lastLegGoalA * 2) == (goalsB * 2 + lastLegGoalB))
                return TeamId.UNKNOWN;
            return (goalsA + lastLegGoalA * 2) > (goalsB * 2 + lastLegGoalB) ? TeamId.A : TeamId.B;
        } else {

            if ((goalsA + lastLegGoalA) == (goalsB + lastLegGoalB))
                return TeamId.UNKNOWN;
            return (goalsA + lastLegGoalA) > (goalsB + lastLegGoalB) ? TeamId.A : TeamId.B;
        }
    }

    private int generateFiveMinsNo() {
        int fiveMinsNo = elapsedTimeSecs / defaultFiveMinutesSecs;

        // last five minutes period in the first half
        if (elapsedTimeSecs >= (normalHalfSecs) && matchPeriod.equals(FootballMatchPeriod.IN_FIRST_HALF)) {
            fiveMinsNo = 8;
        } else if (elapsedTimeSecs >= (normalHalfSecs * 2) && matchPeriod.equals(FootballMatchPeriod.IN_SECOND_HALF)) {
            fiveMinsNo = 17;
        } else if (elapsedTimeSecs >= (normalHalfSecs * 2 + extraHalfSecs)
                        && matchPeriod.equals(FootballMatchPeriod.IN_EXTRA_TIME_FIRST_HALF)) {
            fiveMinsNo = 20;
        } else if (elapsedTimeSecs >= (normalHalfSecs * 2 + extraHalfSecs * 2)
                        && matchPeriod.equals(FootballMatchPeriod.IN_EXTRA_TIME_SECOND_HALF)) {
            fiveMinsNo = 23;
        }
        // if(fiveMinsNo>=24){
        // System.out.println(" ");
        // }

        return fiveMinsNo;
        // Careful: extra time fiveMinsNo is setted to be in the last slot of
        // the period
    }

    /**
     * sets the elapsed time from a reported matchIncident.
     * 
     * @param elapsedTimeSecs
     */
    /**
     * sets the elapsed time either from a reported matchIncident or as a result of a matchSimulation call to the
     * incrementSimulationElapsedTime method.
     * 
     * @param elapsedTimeSecs The updated elapsed time in tenths of a second
     */
    private void setElapsedTime(int elapsedTimeSecs) {
        if (elapsedTimeSecs == -1)
            return;
        this.elapsedTimeSecs = elapsedTimeSecs;
        switch (matchPeriod) {
            case PREMATCH:
            case AT_HALF_TIME:
            case AT_FULL_TIME:
            case IN_EXTRA_TIME_HALF_TIME:
            case MATCH_COMPLETED:
                /*
                 * do nothing
                 */
                break;
            case IN_FIRST_HALF:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs;
                checkWhetherToExtendInjuryTime(elapsedTimeThisPeriodSecs, normalHalfSecs);
                break;
            case IN_SECOND_HALF:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs - normalHalfSecs;
                checkWhetherToExtendInjuryTime(elapsedTimeThisPeriodSecs, normalHalfSecs);
                break;
            case IN_EXTRA_TIME_FIRST_HALF:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs - 2 * normalHalfSecs;
                checkWhetherToExtendInjuryTime(elapsedTimeThisPeriodSecs, extraHalfSecs);
                break;
            case IN_EXTRA_TIME_SECOND_HALF:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs - 2 * normalHalfSecs - extraHalfSecs;
                checkWhetherToExtendInjuryTime(elapsedTimeThisPeriodSecs, extraHalfSecs);
                break;
            // default:
            // throw new IllegalArgumentException("Should not go here");
            case IN_SHOOTOUT:
                break;
            default:
                break;
        }
    }

    /*
     * This method copy the match state and reset the data feed state mismatch alarm
     */
    private void copyMatchStateFromFeed(FootballMatchStateFromFeed stateFromFeed,
                    boolean autosyncMatchStateToFeedOnMismatch) {
        if (stateFromFeed != null && autosyncMatchStateToFeedOnMismatch) {
            setMatchStateEqualToFeedState(this, stateFromFeed);
            super.setDatafeedStateMismatch(false);
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
        FootballMatchState cc = new FootballMatchState(matchFormat);
        cc.setEqualTo(this);
        return cc;

    }

    @Override
    public void setEqualTo(AlgoMatchState matchState) {
        super.setEqualTo(matchState);
        this.setGoalsA(((FootballMatchState) matchState).getGoalsA());
        this.setGoalsB(((FootballMatchState) matchState).getGoalsB());
        this.setCornersA(((FootballMatchState) matchState).getCornersA());
        this.setCornersB(((FootballMatchState) matchState).getCornersB());
        this.setElapsedTimeSecs(((FootballMatchState) matchState).getElapsedTimeSecs());
        this.setElapsedTimeAtLastMatchIncidentSecs(
                        ((FootballMatchState) matchState).getElapsedTimeAtLastMatchIncidentSecs());
        this.setElapsedTimeAtLastFootballMatchIncidentSecs(
                        ((FootballMatchState) matchState).getElapsedTimeAtLastMatchIncidentSecs());
        this.setElapsedTimeThisPeriodSecs(((FootballMatchState) matchState).getElapsedTimeThisPeriodSecs());
        this.setInjuryTimeSecs(((FootballMatchState) matchState).getInjuryTimeSecs());
        this.setMatchPeriod(((FootballMatchState) matchState).getMatchPeriod());
        this.setTeamScoringLastGoal(((FootballMatchState) matchState).getTeamScoringLastGoal());
        this.setTeamScoringLastCorner(((FootballMatchState) matchState).getTeamScoringLastCorner());
        this.setCurrentPeriodGoalsA(((FootballMatchState) matchState).getCurrentPeriodGoalsA());
        this.setCurrentPeriodGoalsB(((FootballMatchState) matchState).getCurrentPeriodGoalsB());
        this.setPreviousPeriodGoalsA(((FootballMatchState) matchState).getPreviousPeriodGoalsA());
        this.setPreviousPeriodGoalsB(((FootballMatchState) matchState).getPreviousPeriodGoalsB());
        this.setCurrentPeriodCornersA(((FootballMatchState) matchState).getCurrentPeriodCornersA());
        this.setCurrentPeriodCornersB(((FootballMatchState) matchState).getCurrentPeriodCornersB());
        int[] temp_A = Arrays.copyOf(((FootballMatchState) matchState).getFiveMinsGoalA(), fiveMinutesArrayLength);
        int[] temp_B = Arrays.copyOf(((FootballMatchState) matchState).getFiveMinsGoalB(), fiveMinutesArrayLength);
        this.setFiveMinsGoalA(temp_A);
        this.setFiveMinsGoalB(temp_B);
        this.setMatchPeriodInWhichLastGoalScored(
                        ((FootballMatchState) matchState).getMatchPeriodInWhichLastGoalScored());
        this.setFiveMinsNo(((FootballMatchState) matchState).getFiveMinsNo());
        this.setLastFiveMinsNO(((FootballMatchState) matchState).getLastFiveMinsNO());
        this.setShootOutGoalsA(((FootballMatchState) matchState).getShootOutGoalsA());
        this.setShootOutGoalsB(((FootballMatchState) matchState).getShootOutGoalsB());
        this.setShootOutTimeCounter(((FootballMatchState) matchState).getShootOutTimeCounter());
        this.setIsSecondLegMatch(((FootballMatchState) matchState).secondLegMatch);
        this.setLastLegGoalA(((FootballMatchState) matchState).getLastLegGoalA());
        this.setLastLegGoalB(((FootballMatchState) matchState).getLastLegGoalB());
        this.setPreviousPeriodCornersA(((FootballMatchState) matchState).getPreviousPeriodCornersA());
        this.setPreviousPeriodCornersB(((FootballMatchState) matchState).getPreviousPeriodCornersB());
        this.setNormalTimeGoalsA(((FootballMatchState) matchState).getNormalTimeGoalsA());
        this.setNormalTimeGoalsB(((FootballMatchState) matchState).getNormalTimeGoalsB());
        this.setNormalTimeCornersA(((FootballMatchState) matchState).getNormalTimeCornersA());
        this.setNormalTimeCornersB(((FootballMatchState) matchState).getNormalTimeCornersB());

        this.setYellowCardsA(((FootballMatchState) matchState).getYellowCardsA());
        this.setYellowCardsB(((FootballMatchState) matchState).getYellowCardsB());
        this.setRedCardsA(((FootballMatchState) matchState).getRedCardsA());
        this.setRedCardsB(((FootballMatchState) matchState).getRedCardsB());
        this.setLastMatchIncidentType(((FootballMatchState) matchState).getLastMatchIncidentType());
        this.setPenaltyStatus(((FootballMatchState) matchState).getPenaltyStatus());
        this.setVarReferralInProgress(((FootballMatchState) matchState).isVarReferralInProgress());
        this.setShootOutFirst(((FootballMatchState) matchState).getShootOutFirst());
        this.setInjuryTime(((FootballMatchState) matchState).isInjuryTime);
        int size = ((FootballMatchState) matchState).getGoalInfo().size();
        List<GoalInfo> temp_C = new ArrayList<GoalInfo>(size);
        for (int index = 0; index < size; index++)
            temp_C.add(((FootballMatchState) matchState).getGoalInfo().get(index));
        this.setGoalInfo(temp_C);

        size = ((FootballMatchState) matchState).getCornerInfo().size();
        temp_C = new ArrayList<GoalInfo>(size);
        for (int index = 0; index < size; index++)
            temp_C.add(((FootballMatchState) matchState).getCornerInfo().get(index));
        this.setCornerInfo(temp_C);

        List<CardInfo> temp_D = new ArrayList<CardInfo>(size);
        size = ((FootballMatchState) matchState).getBookingInfo().size();
        temp_D = new ArrayList<CardInfo>(size);
        for (int index = 0; index < size; index++)
            temp_D.add(((FootballMatchState) matchState).getBookingInfo().get(index));
        this.setBookingInfo(temp_D);

        this.setShootOutGoalsA(((FootballMatchState) matchState).getShootOutGoalsA());
        this.setShootOutGoalsB(((FootballMatchState) matchState).getShootOutGoalsB());
        this.playerScoringGoalN = ((FootballMatchState) matchState).getPlayerScoringGoalN();
        this.teamSheet = ((FootballMatchState) matchState).getTeamSheet();
        this.firstCorner = ((FootballMatchState) matchState).getFirstCorner();
        this.extraTimeEnteredFlag = ((FootballMatchState) matchState).isExtraTimeEnteredFlag();
        this.setExtraTimeFHGoalsA(((FootballMatchState) matchState).getExtraTimeFHGoalsA());
        this.setExtraTimeFHGoalsB(((FootballMatchState) matchState).getExtraTimeFHGoalsB());
        this.setExtraTimeSHGoalsA(((FootballMatchState) matchState).getExtraTimeSHGoalsA());
        this.setExtraTimeSHGoalsB(((FootballMatchState) matchState).getExtraTimeSHGoalsB());
        this.firstHalfGoals = CollectionsUtil.newList();
        firstHalfGoals.addAll(((FootballMatchState) matchState).getFirstHalfGoals());
        secondHalfGoals = CollectionsUtil.newList();
        secondHalfGoals.addAll(((FootballMatchState) matchState).getSecondHalfGoals());
        scoreHistory = CollectionsUtil.newList();
        scoreHistory.addAll(((FootballMatchState) matchState).getScoreHistory());
        this.setFirstCardTimeSlot(((FootballMatchState) matchState).getFirstCardTimeSlot());
        this.setFirstCornerTimeSlot(((FootballMatchState) matchState).getFirstCornerTimeSlot());
        firstHalfCorners = CollectionsUtil.newList();
        firstHalfCorners.addAll(((FootballMatchState) matchState).getFirstHalfCorners());
        secondHalfCorners = CollectionsUtil.newList();
        secondHalfCorners.addAll(((FootballMatchState) matchState).getSecondHalfCorners());
        firstHalfYellowCards = CollectionsUtil.newList();
        firstHalfYellowCards.addAll(((FootballMatchState) matchState).getFirstHalfYellowCards());
        secondHalfYellowCards = CollectionsUtil.newList();
        secondHalfYellowCards.addAll(((FootballMatchState) matchState).getSecondHalfYellowCards());
        firstHalfRedCards = CollectionsUtil.newList();
        firstHalfRedCards.addAll(((FootballMatchState) matchState).getFirstHalfRedCards());
        secondHalfRedCards = CollectionsUtil.newList();
        secondHalfRedCards.addAll(((FootballMatchState) matchState).getSecondHalfRedCards());
        this.setLastFootballMatchIncidentType(((FootballMatchState) matchState).getLastFootballMatchIncidentType());
        this.setElapsedTimeAtLastCornerSecs(((FootballMatchState) matchState).getElapsedTimeAtLastCornerSecs());
        this.setElapsedTimeAtLastGoalSecs(((FootballMatchState) matchState).getElapsedTimeAtLastGoalSecs());
        if (((FootballMatchState) matchState).getPenaltyInfo() != null)
            this.setPenaltyInfo(((FootballMatchState) matchState).getPenaltyInfo().clone());
    }


    private void setVarReferralInProgress(boolean varReferralInProgress2) {
        this.varReferralInProgress = varReferralInProgress2;
    }

    @Override
    public MatchIncidentPrompt getNextPrompt() {
        /*
         * no longer used for Football. c.f. FootballMatchIncidentGenerator class
         */
        return null;
    }

    @Override
    /**
     * returns either FootballMatchIncident or ElapsedTimeMatchIncident
     */

    public MatchIncident getMatchIncident(String response) {
        return null;
        /*
         * comment out the old code rather than deleting in case need to refer to it while testing the new gui
         */
        // FootballMatchIncidentType footballMatchIncidentType = null;
        // ElapsedTimeMatchIncidentType elapsedTimeMatchIncidentType = null;
        // TeamId teamId = null;
        // int incidentElapsedTimeSecs = elapsedTimeSecs;
        // int injuryTimeSecs = 0;
        // MatchIncident matchIncident = null;
        // char c = response.toUpperCase().charAt(0);
        // switch (c) {
        // case 'T': // For trading rules incidents
        // String d = response.toUpperCase().substring(1, 3);
        // switch (d) {
        // case "GP":
        // footballMatchIncidentType = FootballMatchIncidentType.POSSIBLE_GOAL;
        // break;
        // case "GD":
        // footballMatchIncidentType =
        // FootballMatchIncidentType.GOAL_NOT_CONFIRMED;
        // break;
        // case "CP":
        // footballMatchIncidentType =
        // FootballMatchIncidentType.POSSIBLE_CORNER;
        // break;
        // case "CD":
        // footballMatchIncidentType =
        // FootballMatchIncidentType.CORNER_NOT_CONFIRMED;
        // break;
        // case "YP":
        // footballMatchIncidentType =
        // FootballMatchIncidentType.POSSIBLE_YELLOW_CARD;
        // break;
        // case "YD":
        // footballMatchIncidentType =
        // FootballMatchIncidentType.YELLOW_CARD_NOT_CONFIRMED;
        // break;
        // case "PR":
        // footballMatchIncidentType =
        // FootballMatchIncidentType.POSSIBLE_RED_CARD;
        // break;
        // case "DR":
        // footballMatchIncidentType =
        // FootballMatchIncidentType.RED_CARD_NOT_CONFIRMED;
        // break;
        // case "PP":
        // footballMatchIncidentType =
        // FootballMatchIncidentType.POSSIBLE_PENALTY;
        // break;
        // case "PD":
        // footballMatchIncidentType =
        // FootballMatchIncidentType.PENALTY_NOT_CONFIRMED;
        // break;
        // case "PH":
        // footballMatchIncidentType = FootballMatchIncidentType.PENALTY;
        // teamId = TeamId.A;
        // break;
        // case "PA":
        // footballMatchIncidentType = FootballMatchIncidentType.PENALTY;
        // teamId = TeamId.B;
        // break;
        // case "PC":
        // String t = response.toUpperCase().substring(3, 4);
        // footballMatchIncidentType =
        // FootballMatchIncidentType.PENALTY_CONFIRMED;
        // if (t.equals("H"))
        // teamId = TeamId.A;
        // else
        // teamId = TeamId.B;
        // break;
        // }
        // break;
        //
        // case 'Z':
        // footballMatchIncidentType = FootballMatchIncidentType.PENALTY_MISSED;
        //
        // case 'N':
        // elapsedTimeMatchIncidentType =
        // ElapsedTimeMatchIncidentType.SET_MATCH_CLOCK;
        // int repeatCount;
        // try {
        // repeatCount = Integer.parseInt(response.substring(1));
        // if (repeatCount < 0 || repeatCount > 500)
        // repeatCount = 1;
        // } catch (Exception e) {
        // repeatCount = 1;
        // }
        // /*
        // * 10 second increments
        // */
        // incidentElapsedTimeSecs = elapsedTimeSecs + repeatCount * 10;
        // break;
        // case 'Y':
        // if (response.substring(1).toUpperCase().equals("H"))
        // teamId = TeamId.A;
        // else if (response.substring(1).toUpperCase().equals("A"))
        // teamId = TeamId.B;
        // else {
        // throw new IllegalArgumentException("Team is needed");
        // }
        // footballMatchIncidentType = FootballMatchIncidentType.YELLOW_CARD;
        // break;
        //
        // case 'R':
        // if (response.substring(1).toUpperCase().equals("H"))
        // teamId = TeamId.A;
        // else if (response.substring(1).toUpperCase().equals("A"))
        // teamId = TeamId.B;
        // else {
        // throw new IllegalArgumentException("Team is needed");
        // }
        // footballMatchIncidentType = FootballMatchIncidentType.RED_CARD;
        // break;
        //
        // case 'M':
        // footballMatchIncidentType = FootballMatchIncidentType.SHOOTOUT_MISS;
        // break;
        // case 'O':
        // if (response.substring(1).toUpperCase().equals("H"))
        // teamId = TeamId.A;
        // else if (response.substring(1).toUpperCase().equals("A"))
        // teamId = TeamId.B;
        // footballMatchIncidentType = FootballMatchIncidentType.SHOOTOUT_START;
        // break;
        // case 'H':
        // footballMatchIncidentType = FootballMatchIncidentType.GOAL;
        // teamId = TeamId.A;
        // break;
        // case 'A':
        // footballMatchIncidentType = FootballMatchIncidentType.GOAL;
        // teamId = TeamId.B;
        // break;
        // case 'Q':
        // footballMatchIncidentType = FootballMatchIncidentType.CORNER;
        // teamId = TeamId.A;
        // break;
        // case 'P':
        // footballMatchIncidentType = FootballMatchIncidentType.CORNER;
        // teamId = TeamId.B;
        // break;
        // case 'I':
        // footballMatchIncidentType =
        // FootballMatchIncidentType.SET_INJURY_TIME;
        // try {
        // injuryTimeSecs = Integer.parseInt(response.substring(1));
        // if (injuryTimeSecs < 0 || injuryTimeSecs > 1000)
        // throw new Exception();
        // } catch (Exception e) {
        // return null;
        // }
        // break;
        // case 'S':
        // switch (matchPeriod) {
        // case PREMATCH:
        // case AT_HALF_TIME:
        // case AT_FULL_TIME:
        // case IN_EXTRA_TIME_HALF_TIME:
        // elapsedTimeMatchIncidentType =
        // ElapsedTimeMatchIncidentType.SET_PERIOD_START;
        // break;
        // default:
        // return null;
        // }
        // break;
        // case 'E':
        // switch (matchPeriod) {
        // case IN_FIRST_HALF:
        // case IN_SECOND_HALF:
        // case IN_EXTRA_TIME_FIRST_HALF:
        // case IN_EXTRA_TIME_SECOND_HALF:
        // elapsedTimeMatchIncidentType =
        // ElapsedTimeMatchIncidentType.SET_PERIOD_END;
        // break;
        // default:
        // return null; // invalid input so return null
        // }
        // break;
        // case 'B':
        // /*
        // * update player status. syntax B:playerName:status
        // */
        // try {
        // String[] responseBits = response.split(":");
        // String playerId = responseBits[1];
        // String status = responseBits[2].toUpperCase();
        // PlayerStatus playerStatus = PlayerStatus.valueOf(status);
        // TeamSheet updatedTeamSheet = new TeamSheet();
        // PlayerInfo info = teamSheet.getTeamSheetMap().get(playerId);
        //
        // updatedTeamSheet.addPlayer(info.getTeamId(), info.getPlayerName(),
        // playerStatus);
        // matchIncident =
        // TeamSheetMatchIncident.generateMatchIncidentForUpdateTeamSheet(updatedTeamSheet);
        // } catch (Exception e) {
        // return null;
        // }
        // break;
        // case 'G':
        // /*
        // * specifies player who scored goal n. syntax G:playerId:nn, where
        // playerID is of the form
        // * "A.George Best"
        // */
        // try {
        // String[] responseBits = response.split(":");
        // String playerId = responseBits[1];
        // String goalNoStr = responseBits[2].toUpperCase();
        // String[] playerBits = playerId.split("\\.");
        // String teamIdStr = playerBits[0];
        // String playerName = playerBits[1];
        // TeamId teamId2 = Enum.valueOf(TeamId.class, teamIdStr);
        // int goalNo = Integer.valueOf(goalNoStr);
        // matchIncident = new
        // PlayerMatchIncident(PlayerMatchIncidentType.GOAL_SCORER, goalNo,
        // teamId2,
        // playerName);
        // } catch (Exception e) {
        // return null;
        // }
        // break;
        // case 'U':
        // // TeamSheetMatchIncidentType.INITIAL_TEAM_SHEET;
        // matchIncident = TeamSheetMatchIncident
        // .generateMatchIncidentForInitialTeamSheet(ExampleTeamSheets.getExampleTeamSheet());
        // break;
        // }
        // /*
        // * newer code initialises the matchIncident. If not already
        // initialised then do so here
        // */
        // if (matchIncident == null) {
        // if (footballMatchIncidentType != null) {
        // if (footballMatchIncidentType ==
        // FootballMatchIncidentType.SET_INJURY_TIME) {
        // matchIncident = new FootballMatchIncident(footballMatchIncidentType,
        // incidentElapsedTimeSecs,
        // injuryTimeSecs);
        // } else {
        // matchIncident = new FootballMatchIncident(footballMatchIncidentType,
        // incidentElapsedTimeSecs,
        // teamId);
        // }
        // } else {
        // matchIncident = new
        // ElapsedTimeMatchIncident(elapsedTimeMatchIncidentType,
        // incidentElapsedTimeSecs);
        // }
        // }
        // return matchIncident;
    }

    private static final String goalsKey = "Goals";
    private static final String twoLegsGoalsKey = "Two legs Goals";
    private static final String elapsedTimeKey = "Elapsed time";
    private static final String injuryTimeKey = "Injury time to be played";
    private static final String matchPeriodKey = "Match period";
    private static final String periodSequenceKey = "Period sequence id";
    private static final String goalSequenceKey = "Goal sequence id";
    private static final String cornersKey = "Corners";
    private static final String yellowCardsKey = "Yellow Cards";
    private static final String redCardsKey = "Red Cards";
    private static final String cornerSequenceKey = "Corner sequence id";
    private static final String shootOutChancesKey = "Shoot-out used";
    private static final String shootOutGoalsKey = "Shoot-out Scores";
    private static final String penaltyStatusKey = "Penalty Status";
    private static final String lastIncidentKey = "Last Match Incident";
    private static final String isInInjuryTimeKey = "Is injury time";
    private static final String goalInfoKey = "Goal Info";
    private static final String nextShootoutKey = "Shoot out next";
    private static final String shootoutAttemptKey = "Shoot out attempt";

    @Override

    public LinkedHashMap<String, String> getAsMap() {
        String tempDisplay = "";

        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put(goalsKey, String.format("%d-%d", goalsA, goalsB));
        if (secondLegMatch)
            map.put(twoLegsGoalsKey, String.format("%d-%d", goalsA + lastLegGoalA, goalsB + lastLegGoalB));
        int mins = elapsedTimeSecs / 60;
        int secs = elapsedTimeSecs - mins * 60;
        map.put(elapsedTimeKey, String.format("%d:%02d", mins, secs));
        mins = injuryTimeSecs / 60;
        secs = injuryTimeSecs - mins * 60;
        map.put(injuryTimeKey, String.format("%d:%02d", mins, secs));
        map.put(matchPeriodKey, matchPeriod.toString());
        map.put(periodSequenceKey, getPeriodSequenceId());
        map.put(goalSequenceKey, getGoalSequenceId());
        map.put(cornersKey, String.format("%d-%d", cornersA, cornersB));
        map.put(cornerSequenceKey, getCornerSequenceId());
        map.put(yellowCardsKey, String.format("%d-%d", yellowCardsA, yellowCardsB));
        map.put(redCardsKey, String.format("%d-%d", redCardsA, redCardsB));
        map.put(shootOutChancesKey, String.format("%d", shootOutTimeCounter));
        map.put(shootOutGoalsKey, String.format("%d-%d", shootOutGoalsA, shootOutGoalsB));
        if (lastMatchIncidentType != (null))
            tempDisplay = lastMatchIncidentType.toString();
        map.put(lastIncidentKey, tempDisplay);
        map.put(penaltyStatusKey, penaltyStatus.toString());
        map.put(isInInjuryTimeKey, String.valueOf(isInjuryTime));
        String previousGoalInfo = "";
        int size = goalInfo.size();
        for (int index = 0; index < size; index++)
            previousGoalInfo += goalInfo.get(index).getMins() + "-" + goalInfo.get(index).getTeam() + ",";
        map.put(goalInfoKey, previousGoalInfo);
        map.put(nextShootoutKey, penaltyInfo.getShootingNext().toString());
        if (shootOutFirst != TeamId.UNKNOWN) {
            if (shootOutFirst == TeamId.A) {
                shooutOutCounterA = (int) Math.ceil((double) shootOutTimeCounter / 2);
                shooutOutCounterB = (int) Math.floor((double) shootOutTimeCounter / 2);
            } else {
                shooutOutCounterB = (int) Math.ceil((double) shootOutTimeCounter / 2);
                shooutOutCounterA = (int) Math.floor((double) shootOutTimeCounter / 2);
            }
        }
        map.put(shootoutAttemptKey, shooutOutCounterA + "-" + shooutOutCounterB);

        for (int i = 1; i < playerScoringGoalN.length; i++) {
            String playerName = playerScoringGoalN[i];
            if (playerName != null) {
                map.put("Scorer of goal " + Integer.toString(i), playerName);
            }
        }
        return map;
    }

    @Override
    public LinkedHashMap<String, String> getTeamSheetAsMap() {
        LinkedHashMap<String, String> opMap = new LinkedHashMap<String, String>();
        for (Entry<String, PlayerInfo> e : teamSheet.getTeamSheetMap().entrySet()) {
            opMap.put(e.getKey(), e.getValue().getPlayerStatus().toString());
        }
        return opMap;
    }

    @Override
    public String setFromMap(Map<String, String> map) {
        /*
         * do nothing - not allowing the user to manually update the score
         */
        return null;
    }


    @Override
    public boolean isMatchCompleted() {
        return (matchPeriod == FootballMatchPeriod.MATCH_COMPLETED);
    }


    public boolean isNormalTimeMatchCompleted() {
        int currentPeriodNo = getPeriodNo();
        if (currentPeriodNo <= 2)
            return false;

        return true;
    }


    public boolean isFirstHalfCompleted() {
        int currentPeriodNo = getPeriodNo();
        if (currentPeriodNo <= 1)
            return false;

        return true;
    }


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


    public TeamId getNormalTimeMatchWinner() {
        if (!isNormalTimeMatchCompleted())
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


    public boolean isExtraTimeMarketReadyToResult() {
        if (this.extraHalfSecs == 0)
            return false;
        else {
            switch (this.getPeriodNo()) {
                case 1:
                case 2:
                case 3:
                case 4:
                    return false;
                case 5: // penalty
                    return true;
                case 6: // match ended
                    return this.extraTimeEnteredFlag;
                default:
                    return false;
            }
        }
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


    public TeamId getPreviousPeriodMatchWinner() {
        TeamId teamId;
        if (previousPeriodGoalsA > previousPeriodGoalsB)
            teamId = TeamId.A;
        else if (previousPeriodGoalsA == previousPeriodGoalsB)
            teamId = TeamId.UNKNOWN;
        else
            teamId = TeamId.B;
        return teamId;
    }

    public int getPreviousPeriodGoalsA() {
        return previousPeriodGoalsA;
    }

    public int getPreviousPeriodGoalsB() {
        return previousPeriodGoalsB;
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
            case IN_EXTRA_TIME_HALF_TIME:
            case IN_SHOOTOUT:
            case MATCH_COMPLETED:
                break;
            case IN_FIRST_HALF:
            case IN_SECOND_HALF:
            case IN_EXTRA_TIME_FIRST_HALF:
            case IN_EXTRA_TIME_SECOND_HALF:
                int secs = getSecsSinceLastElapsedTimeFromIncident();
                setElapsedTime(elapsedTimeAtLastMatchIncidentSecs + secs);
                updatePrices = shouldUpdatePrices();
                break;
            default:
                throw new IllegalArgumentException("Should not go here");
        }
        return updatePrices;
    }

    private static final int FIRST_FORTY_MINUTES = 40 * 60;
    private static final int SECOND_FORTY_MINUTES = 80 * 60;

    private boolean shouldUpdatePrices() {
        int intervalBetweenPriceCalcs;
        if (elapsedTimeSecs < FIRST_FORTY_MINUTES)
            intervalBetweenPriceCalcs = 120;
        else if (elapsedTimeSecs < SECOND_FORTY_MINUTES)
            intervalBetweenPriceCalcs = 60;
        else
            intervalBetweenPriceCalcs = 10;
        boolean shouldUpdate = getSecsSinceLastPriceRecalc() >= intervalBetweenPriceCalcs;
        return shouldUpdate;
    }

    /**
     * increments the elapsed time by the stated number of seconds.
     * 
     * @param timeSliceSecs
     */
    void incrementSimulationElapsedTime(int timeSliceSecs) {
        // if (matchPeriod != FootballMatchPeriod.IN_SHOOTOUT)
        elapsedTimeSecs += timeSliceSecs;

        lastFiveMinsNO = fiveMinsNo;
        switch (matchPeriod) {
            case PREMATCH:
                matchPeriod = FootballMatchPeriod.IN_FIRST_HALF;
                elapsedTimeThisPeriodSecs = elapsedTimeSecs;
                injuryTimeSecs = defaultInjuryTimeFirstHalfSecs;
                break;

            case IN_FIRST_HALF:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs;
                if (elapsedTimeThisPeriodSecs >= normalHalfSecs + injuryTimeSecs) {
                    elapsedTimeSecs = normalHalfSecs; // reset counter
                                                      // to remove
                                                      // injury time
                    elapsedTimeThisPeriodSecs = 0;
                    injuryTimeSecs = defaultInjuryTimeSecondHalfSecs;
                    matchPeriod = FootballMatchPeriod.IN_SECOND_HALF;

                    previousPeriodGoalsA = currentPeriodGoalsA;
                    previousPeriodGoalsB = currentPeriodGoalsB;
                    currentPeriodGoalsA = 0;
                    currentPeriodGoalsB = 0;

                    previousPeriodCornersA = currentPeriodCornersA;
                    previousPeriodCornersB = currentPeriodCornersB;
                    currentPeriodCornersA = 0;
                    currentPeriodCornersB = 0;

                    previousPeriodYellowCardsA = currentPeriodYellowCardsA;
                    previousPeriodYellowCardsB = currentPeriodYellowCardsB;
                    currentPeriodYellowCardsA = 0;
                    currentPeriodYellowCardsB = 0;

                    previousPeriodRedCardsA = currentPeriodRedCardsA;
                    previousPeriodRedCardsB = currentPeriodRedCardsB;
                    currentPeriodRedCardsA = 0;
                    currentPeriodRedCardsB = 0;

                }
                break;
            case AT_HALF_TIME:
                matchPeriod = FootballMatchPeriod.IN_SECOND_HALF;
                elapsedTimeThisPeriodSecs = 0;
                injuryTimeSecs = defaultInjuryTimeSecondHalfSecs;
                break;
            case IN_SECOND_HALF:

                elapsedTimeThisPeriodSecs = elapsedTimeSecs - normalHalfSecs;
                if (elapsedTimeThisPeriodSecs >= normalHalfSecs + injuryTimeSecs) {
                    if (extraHalfSecs == 0) {
                        matchPeriod = FootballMatchPeriod.MATCH_COMPLETED;
                    } else {

                        if (secondLegMatch) {
                            if (secondLegMatchSettled())
                                matchPeriod = FootballMatchPeriod.MATCH_COMPLETED;
                            else
                                matchPeriod = FootballMatchPeriod.IN_EXTRA_TIME_FIRST_HALF;
                        } else {
                            if (goalsA != goalsB)
                                matchPeriod = FootballMatchPeriod.MATCH_COMPLETED;
                            else
                                matchPeriod = FootballMatchPeriod.IN_EXTRA_TIME_FIRST_HALF;
                        }
                        if (!matchPeriod.equals(FootballMatchPeriod.MATCH_COMPLETED)) {
                            elapsedTimeSecs = 2 * normalHalfSecs;
                            elapsedTimeThisPeriodSecs = 0;
                            injuryTimeSecs = defaultInjuryTimeExtraTimeHalfDeciSeconds;
                        }
                    }
                    previousPeriodGoalsA = currentPeriodGoalsA;
                    previousPeriodGoalsB = currentPeriodGoalsB;
                    currentPeriodGoalsA = 0;
                    currentPeriodGoalsB = 0;

                    previousPeriodCornersA = currentPeriodCornersA;
                    previousPeriodCornersB = currentPeriodCornersB;
                    currentPeriodCornersA = 0;
                    currentPeriodCornersB = 0;

                    previousPeriodYellowCardsA = currentPeriodYellowCardsA;
                    previousPeriodYellowCardsB = currentPeriodYellowCardsB;
                    currentPeriodYellowCardsA = 0;
                    currentPeriodYellowCardsB = 0;

                    previousPeriodRedCardsA = currentPeriodRedCardsA;
                    previousPeriodRedCardsB = currentPeriodRedCardsB;
                    currentPeriodRedCardsA = 0;
                    currentPeriodRedCardsB = 0;

                }
                break;
            case AT_FULL_TIME:
                matchPeriod = FootballMatchPeriod.IN_SECOND_HALF;
                if (extraHalfSecs == 0)
                    matchPeriod = FootballMatchPeriod.MATCH_COMPLETED;
                else {
                    if (secondLegMatch) {
                        if (secondLegMatchSettled())
                            matchPeriod = FootballMatchPeriod.MATCH_COMPLETED;
                        else
                            matchPeriod = FootballMatchPeriod.IN_EXTRA_TIME_FIRST_HALF;
                    } else {
                        if (goalsA != goalsB)
                            matchPeriod = FootballMatchPeriod.MATCH_COMPLETED;
                        else
                            matchPeriod = FootballMatchPeriod.IN_EXTRA_TIME_FIRST_HALF;
                    }
                }

                break;

            case IN_EXTRA_TIME_FIRST_HALF:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs - 2 * normalHalfSecs;
                if (elapsedTimeThisPeriodSecs >= extraHalfSecs + injuryTimeSecs) {
                    elapsedTimeSecs = 2 * normalHalfSecs + extraHalfSecs;
                    elapsedTimeThisPeriodSecs = 0;
                    injuryTimeSecs = defaultInjuryTimeExtraTimeHalfDeciSeconds;
                    matchPeriod = FootballMatchPeriod.IN_EXTRA_TIME_SECOND_HALF;
                    previousPeriodCornersA = currentPeriodCornersA;
                    previousPeriodCornersB = currentPeriodCornersB;
                    currentPeriodCornersA = 0;
                    currentPeriodCornersB = 0;

                    previousPeriodYellowCardsA = currentPeriodYellowCardsA;
                    previousPeriodYellowCardsB = currentPeriodYellowCardsB;
                    currentPeriodYellowCardsA = 0;
                    currentPeriodYellowCardsB = 0;

                    previousPeriodRedCardsA = currentPeriodRedCardsA;
                    previousPeriodRedCardsB = currentPeriodRedCardsB;
                    currentPeriodRedCardsA = 0;
                    currentPeriodRedCardsB = 0;
                }
                break;
            case IN_EXTRA_TIME_HALF_TIME:
            case IN_EXTRA_TIME_SECOND_HALF:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs - 2 * normalHalfSecs - extraHalfSecs;
                if (elapsedTimeThisPeriodSecs >= extraHalfSecs + injuryTimeSecs) {
                    previousPeriodCornersA = currentPeriodCornersA;
                    previousPeriodCornersB = currentPeriodCornersB;
                    currentPeriodCornersA = 0;
                    currentPeriodCornersB = 0;

                    previousPeriodYellowCardsA = currentPeriodYellowCardsA;
                    previousPeriodYellowCardsB = currentPeriodYellowCardsB;
                    currentPeriodYellowCardsA = 0;
                    currentPeriodYellowCardsB = 0;

                    previousPeriodRedCardsA = currentPeriodRedCardsA;
                    previousPeriodRedCardsB = currentPeriodRedCardsB;
                    currentPeriodRedCardsA = 0;
                    currentPeriodRedCardsB = 0;

                    if (secondLegMatch) {
                        if (secondLegMatchSettled())
                            matchPeriod = FootballMatchPeriod.MATCH_COMPLETED;
                        else
                            matchPeriod = FootballMatchPeriod.IN_SHOOTOUT;
                    } else {
                        if (goalsA != goalsB)
                            matchPeriod = FootballMatchPeriod.MATCH_COMPLETED;
                        else
                            matchPeriod = FootballMatchPeriod.IN_SHOOTOUT;
                    }

                }
                break;

            case IN_SHOOTOUT:
                elapsedTimeSecs -= timeSliceSecs; // elapsed time offset
                shootOutTimeCounter++;
                if (isShootoutSettled(this.penaltyInfo.startedPenalty)) {
                    goalsA += shootOutGoalsA > shootOutGoalsB ? 1 : 0;
                    goalsB += shootOutGoalsA > shootOutGoalsB ? 0 : 1;
                    matchPeriod = FootballMatchPeriod.MATCH_COMPLETED;
                }

                break;
            default:
                throw new IllegalArgumentException("Should not get here");
        }
        //
        // can cause error, needed to be verifyed
        if (elapsedTimeSecs % defaultFiveMinutesSecs == 0)
            fiveMinsNo = this.generateFiveMinsNo();
    }

    /**
     * If game is in shoot out period, check whether this shoot out is settled
     * 
     * @return
     */
    private boolean isShootoutSettled(TeamId startedShootout) {
        boolean settled = false;

        if (shootOutTimeCounter < 10 && (shootOutGoalsA != shootOutGoalsB)) {

            double shooutOutCounterA = Math.ceil((double) shootOutTimeCounter / 2);
            double shooutOutCounterB = Math.floor((double) shootOutTimeCounter / 2);

            if (startedShootout == TeamId.B) {
                double temp = shooutOutCounterA;
                shooutOutCounterA = shooutOutCounterB;
                shooutOutCounterB = temp;
            }

            if (shootOutGoalsA > (shootOutGoalsB + 5 - shooutOutCounterB)
                            || shootOutGoalsB > (shootOutGoalsA + 5 - shooutOutCounterA)) {
                settled = true;
            }

        } else if ((shootOutGoalsA != shootOutGoalsB) && shootOutTimeCounter >= 10 && shootOutTimeCounter % 2 == 0) {

            settled = true;
        }

        return settled;
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
            case IN_EXTRA_TIME_HALF_TIME:
                gamePeriod = GamePeriod.EXTRA_TIME_HALF_TIME;
                break;
            case IN_EXTRA_TIME_SECOND_HALF:
                gamePeriod = GamePeriod.EXTRA_TIME_SECOND_HALF;
                break;
            case IN_SHOOTOUT:
                gamePeriod = GamePeriod.PENALTIES;
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
     * gets the sequence id to use for five minutes based markets
     * 
     * @return
     */

    public String getSequenceIdForFiveMinsResult() {
        return String.format("F%d", (int) fiveMinsNo);
    }

    /**
     * gets the sequence id to use for ten minutes based markets
     * 
     * @return
     */

    public String getSequenceIdForTenMinsResult() {
        return String.format("F%d", (int) Math.floor(fiveMinsNo) * 5 / 10);
    }

    /**
     * gets the sequence id to use for fifteen minutes based markets
     * 
     * @return
     */

    public String getSequenceIdForFifteenMinsResult() {
        return String.format("F%d", (int) Math.floor(fiveMinsNo) * 5 / 15);
    }

    @Override
    public FootballSimpleMatchState generateSimpleMatchState() {
        return generateSimpleMatchState(1);

    }

    @Override
    public FootballSimpleMatchState generateSimpleMatchState(long eventTier) {
        TeamSheet teamSheet;
        if (FootballMatchParams.individualsInTier((int) eventTier))
            teamSheet = this.teamSheet;
        else
            teamSheet = null;
        TeamId goalFirstTeam = checkFirstTeamScored();
        FootballSimpleMatchState simpleMatchState = new FootballSimpleMatchState(preMatch(), isMatchCompleted(),
                        isClockRunning(), matchPeriod, elapsedTimeSecs, goalsA, goalsB, cornersA, cornersB,
                        yellowCardsA, yellowCardsB, redCardsA, redCardsB, getFirstHalfGoalsA(), getFirstHalfGoalsB(),
                        getSecondHalfGoalsA(), getSecondHalfGoalsB(), goalFirstTeam,
                        ArrayToMap.convert(playerScoringGoalN, "goal"), teamSheet, penaltyInfo, varReferralInProgress);
        return simpleMatchState;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (awayGoalDouble ? 1231 : 1237);
        result = prime * result + ((bookingInfo == null) ? 0 : bookingInfo.hashCode());
        result = prime * result + cardsA;
        result = prime * result + cardsB;
        result = prime * result + ((cornerInfo == null) ? 0 : cornerInfo.hashCode());
        result = prime * result + cornersA;
        result = prime * result + cornersB;
        result = prime * result + Arrays.hashCode(cornersInPeriodN);
        result = prime * result + currentPeriodCardsA;
        result = prime * result + currentPeriodCardsB;
        result = prime * result + currentPeriodCornersA;
        result = prime * result + currentPeriodCornersB;
        result = prime * result + currentPeriodGoalsA;
        result = prime * result + currentPeriodGoalsB;
        result = prime * result + currentPeriodRedCardsA;
        result = prime * result + currentPeriodRedCardsB;
        result = prime * result + currentPeriodYellowCardsA;
        result = prime * result + currentPeriodYellowCardsB;
        result = prime * result + elapsedTimeAtLastCornerSecs;
        result = prime * result + elapsedTimeAtLastFootballMatchIncidentSecs;
        result = prime * result + elapsedTimeAtLastGoalSecs;
        result = prime * result + elapsedTimeAtLastMatchIncidentSecs;
        result = prime * result + elapsedTimeSecs;
        result = prime * result + elapsedTimeThisPeriodSecs;
        result = prime * result + extraHalfSecs;
        result = prime * result + (extraTimeEnteredFlag ? 1231 : 1237);
        result = prime * result + extraTimeFHCardsA;
        result = prime * result + extraTimeFHCardsB;
        result = prime * result + extraTimeFHCornersA;
        result = prime * result + extraTimeFHCornersB;
        result = prime * result + extraTimeFHGoalsA;
        result = prime * result + extraTimeFHGoalsB;
        result = prime * result + extraTimeFHRedCardsA;
        result = prime * result + extraTimeFHRedCardsB;
        result = prime * result + extraTimeFHYellowCardsA;
        result = prime * result + extraTimeFHYellowCardsB;
        result = prime * result + extraTimeSHCardsA;
        result = prime * result + extraTimeSHCardsB;
        result = prime * result + extraTimeSHCornersA;
        result = prime * result + extraTimeSHCornersB;
        result = prime * result + extraTimeSHGoalsA;
        result = prime * result + extraTimeSHGoalsB;
        result = prime * result + extraTimeSHRedCardsA;
        result = prime * result + extraTimeSHRedCardsB;
        result = prime * result + extraTimeSHYellowCardsA;
        result = prime * result + extraTimeSHYellowCardsB;
        result = prime * result + firstCardTimeSlot;
        result = prime * result + ((firstCorner == null) ? 0 : firstCorner.hashCode());
        result = prime * result + firstCornerTimeSlot;
        result = prime * result + ((firstHalfCorners == null) ? 0 : firstHalfCorners.hashCode());
        result = prime * result + ((firstHalfGoals == null) ? 0 : firstHalfGoals.hashCode());
        result = prime * result + ((firstHalfRedCards == null) ? 0 : firstHalfRedCards.hashCode());
        result = prime * result + ((firstHalfYellowCards == null) ? 0 : firstHalfYellowCards.hashCode());
        result = prime * result + Arrays.hashCode(fiveMinsGoalA);
        result = prime * result + Arrays.hashCode(fiveMinsGoalB);
        result = prime * result + fiveMinsNo;
        result = prime * result + ((goalInfo == null) ? 0 : goalInfo.hashCode());
        result = prime * result + Arrays.hashCode(goalScoreInPeriodN);
        result = prime * result + goalsA;
        result = prime * result + goalsB;
        result = prime * result + injuryTimeSecs;
        result = prime * result + (isInjuryTime ? 1231 : 1237);
        result = prime * result + lastFiveMinsNO;
        result = prime * result
                        + ((lastFootballMatchIncidentType == null) ? 0 : lastFootballMatchIncidentType.hashCode());
        result = prime * result + lastLegGoalA;
        result = prime * result + lastLegGoalB;
        result = prime * result + ((lastMatchIncidentType == null) ? 0 : lastMatchIncidentType.hashCode());
        result = prime * result + ((matchFormat == null) ? 0 : matchFormat.hashCode());
        result = prime * result + ((matchPeriod == null) ? 0 : matchPeriod.hashCode());
        result = prime * result + ((matchPeriodInWhichLastGoalScored == null) ? 0
                        : matchPeriodInWhichLastGoalScored.hashCode());
        result = prime * result + normalHalfSecs;
        result = prime * result + normalTimeCardsA;
        result = prime * result + normalTimeCardsB;
        result = prime * result + normalTimeCornersA;
        result = prime * result + normalTimeCornersB;
        result = prime * result + normalTimeGoalsA;
        result = prime * result + normalTimeGoalsB;
        result = prime * result + normalTimeRedCardsA;
        result = prime * result + normalTimeRedCardsB;
        result = prime * result + normalTimeYellowCardsA;
        result = prime * result + normalTimeYellowCardsB;
        result = prime * result + ((penaltyInfo == null) ? 0 : penaltyInfo.hashCode());
        result = prime * result + ((penaltyStatus == null) ? 0 : penaltyStatus.hashCode());
        result = prime * result + Arrays.hashCode(playerScoringGoalN);
        result = prime * result + previousPeriodCardsA;
        result = prime * result + previousPeriodCardsB;
        result = prime * result + previousPeriodCornersA;
        result = prime * result + previousPeriodCornersB;
        result = prime * result + previousPeriodGoalsA;
        result = prime * result + previousPeriodGoalsB;
        result = prime * result + previousPeriodRedCardsA;
        result = prime * result + previousPeriodRedCardsB;
        result = prime * result + previousPeriodYellowCardsA;
        result = prime * result + previousPeriodYellowCardsB;
        result = prime * result + redCardsA;
        result = prime * result + redCardsB;
        result = prime * result + Arrays.hashCode(redCardsInPeriodN);
        result = prime * result + ((scoreHistory == null) ? 0 : scoreHistory.hashCode());
        result = prime * result + ((secondHalfCorners == null) ? 0 : secondHalfCorners.hashCode());
        result = prime * result + ((secondHalfGoals == null) ? 0 : secondHalfGoals.hashCode());
        result = prime * result + ((secondHalfRedCards == null) ? 0 : secondHalfRedCards.hashCode());
        result = prime * result + ((secondHalfYellowCards == null) ? 0 : secondHalfYellowCards.hashCode());
        result = prime * result + ((secondLegMatch == null) ? 0 : secondLegMatch.hashCode());
        result = prime * result + ((shootOutFirst == null) ? 0 : shootOutFirst.hashCode());
        result = prime * result + shootOutGoalsA;
        result = prime * result + shootOutGoalsB;
        result = prime * result + shootOutTimeCounter;
        result = prime * result + shooutOutCounterA;
        result = prime * result + shooutOutCounterB;
        result = prime * result + ((teamScoringLastCorner == null) ? 0 : teamScoringLastCorner.hashCode());
        result = prime * result + ((teamScoringLastGoal == null) ? 0 : teamScoringLastGoal.hashCode());
        result = prime * result + ((teamSheet == null) ? 0 : teamSheet.hashCode());
        result = prime * result + yellowCardsA;
        result = prime * result + yellowCardsB;
        result = prime * result + Arrays.hashCode(yellowCardsInPeriodN);
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
        FootballMatchState other = (FootballMatchState) obj;
        if (awayGoalDouble != other.awayGoalDouble)
            return false;
        if (bookingInfo == null) {
            if (other.bookingInfo != null)
                return false;
        } else if (!bookingInfo.equals(other.bookingInfo))
            return false;
        if (cardsA != other.cardsA)
            return false;
        if (cardsB != other.cardsB)
            return false;
        if (cornerInfo == null) {
            if (other.cornerInfo != null)
                return false;
        } else if (!cornerInfo.equals(other.cornerInfo))
            return false;
        if (cornersA != other.cornersA)
            return false;
        if (cornersB != other.cornersB)
            return false;
        if (!Arrays.equals(cornersInPeriodN, other.cornersInPeriodN))
            return false;
        if (currentPeriodCardsA != other.currentPeriodCardsA)
            return false;
        if (currentPeriodCardsB != other.currentPeriodCardsB)
            return false;
        if (currentPeriodCornersA != other.currentPeriodCornersA)
            return false;
        if (currentPeriodCornersB != other.currentPeriodCornersB)
            return false;
        if (currentPeriodGoalsA != other.currentPeriodGoalsA)
            return false;
        if (currentPeriodGoalsB != other.currentPeriodGoalsB)
            return false;
        if (currentPeriodRedCardsA != other.currentPeriodRedCardsA)
            return false;
        if (currentPeriodRedCardsB != other.currentPeriodRedCardsB)
            return false;
        if (currentPeriodYellowCardsA != other.currentPeriodYellowCardsA)
            return false;
        if (currentPeriodYellowCardsB != other.currentPeriodYellowCardsB)
            return false;
        if (elapsedTimeAtLastCornerSecs != other.elapsedTimeAtLastCornerSecs)
            return false;
        if (elapsedTimeAtLastFootballMatchIncidentSecs != other.elapsedTimeAtLastFootballMatchIncidentSecs)
            return false;
        if (elapsedTimeAtLastGoalSecs != other.elapsedTimeAtLastGoalSecs)
            return false;
        if (elapsedTimeAtLastMatchIncidentSecs != other.elapsedTimeAtLastMatchIncidentSecs)
            return false;
        if (elapsedTimeSecs != other.elapsedTimeSecs)
            return false;
        if (elapsedTimeThisPeriodSecs != other.elapsedTimeThisPeriodSecs)
            return false;
        if (extraHalfSecs != other.extraHalfSecs)
            return false;
        if (extraTimeEnteredFlag != other.extraTimeEnteredFlag)
            return false;
        if (extraTimeFHCardsA != other.extraTimeFHCardsA)
            return false;
        if (extraTimeFHCardsB != other.extraTimeFHCardsB)
            return false;
        if (extraTimeFHCornersA != other.extraTimeFHCornersA)
            return false;
        if (extraTimeFHCornersB != other.extraTimeFHCornersB)
            return false;
        if (extraTimeFHGoalsA != other.extraTimeFHGoalsA)
            return false;
        if (extraTimeFHGoalsB != other.extraTimeFHGoalsB)
            return false;
        if (extraTimeFHRedCardsA != other.extraTimeFHRedCardsA)
            return false;
        if (extraTimeFHRedCardsB != other.extraTimeFHRedCardsB)
            return false;
        if (extraTimeFHYellowCardsA != other.extraTimeFHYellowCardsA)
            return false;
        if (extraTimeFHYellowCardsB != other.extraTimeFHYellowCardsB)
            return false;
        if (extraTimeSHCardsA != other.extraTimeSHCardsA)
            return false;
        if (extraTimeSHCardsB != other.extraTimeSHCardsB)
            return false;
        if (extraTimeSHCornersA != other.extraTimeSHCornersA)
            return false;
        if (extraTimeSHCornersB != other.extraTimeSHCornersB)
            return false;
        if (extraTimeSHGoalsA != other.extraTimeSHGoalsA)
            return false;
        if (extraTimeSHGoalsB != other.extraTimeSHGoalsB)
            return false;
        if (extraTimeSHRedCardsA != other.extraTimeSHRedCardsA)
            return false;
        if (extraTimeSHRedCardsB != other.extraTimeSHRedCardsB)
            return false;
        if (extraTimeSHYellowCardsA != other.extraTimeSHYellowCardsA)
            return false;
        if (extraTimeSHYellowCardsB != other.extraTimeSHYellowCardsB)
            return false;
        if (firstCardTimeSlot != other.firstCardTimeSlot)
            return false;
        if (firstCorner != other.firstCorner)
            return false;
        if (firstCornerTimeSlot != other.firstCornerTimeSlot)
            return false;
        if (firstHalfCorners == null) {
            if (other.firstHalfCorners != null)
                return false;
        } else if (!firstHalfCorners.equals(other.firstHalfCorners))
            return false;
        if (firstHalfGoals == null) {
            if (other.firstHalfGoals != null)
                return false;
        } else if (!firstHalfGoals.equals(other.firstHalfGoals))
            return false;
        if (firstHalfRedCards == null) {
            if (other.firstHalfRedCards != null)
                return false;
        } else if (!firstHalfRedCards.equals(other.firstHalfRedCards))
            return false;
        if (firstHalfYellowCards == null) {
            if (other.firstHalfYellowCards != null)
                return false;
        } else if (!firstHalfYellowCards.equals(other.firstHalfYellowCards))
            return false;
        if (!Arrays.equals(fiveMinsGoalA, other.fiveMinsGoalA))
            return false;
        if (!Arrays.equals(fiveMinsGoalB, other.fiveMinsGoalB))
            return false;
        if (fiveMinsNo != other.fiveMinsNo)
            return false;
        if (goalInfo == null) {
            if (other.goalInfo != null)
                return false;
        } else if (!goalInfo.equals(other.goalInfo))
            return false;
        if (!Arrays.equals(goalScoreInPeriodN, other.goalScoreInPeriodN))
            return false;
        if (goalsA != other.goalsA)
            return false;
        if (goalsB != other.goalsB)
            return false;
        if (injuryTimeSecs != other.injuryTimeSecs)
            return false;
        if (isInjuryTime != other.isInjuryTime)
            return false;
        if (lastFiveMinsNO != other.lastFiveMinsNO)
            return false;
        if (lastFootballMatchIncidentType != other.lastFootballMatchIncidentType)
            return false;
        if (lastLegGoalA != other.lastLegGoalA)
            return false;
        if (lastLegGoalB != other.lastLegGoalB)
            return false;
        if (lastMatchIncidentType == null) {
            if (other.lastMatchIncidentType != null)
                return false;
        } else if (!lastMatchIncidentType.equals(other.lastMatchIncidentType))
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
        if (normalHalfSecs != other.normalHalfSecs)
            return false;
        if (normalTimeCardsA != other.normalTimeCardsA)
            return false;
        if (normalTimeCardsB != other.normalTimeCardsB)
            return false;
        if (normalTimeCornersA != other.normalTimeCornersA)
            return false;
        if (normalTimeCornersB != other.normalTimeCornersB)
            return false;
        if (normalTimeGoalsA != other.normalTimeGoalsA)
            return false;
        if (normalTimeGoalsB != other.normalTimeGoalsB)
            return false;
        if (normalTimeRedCardsA != other.normalTimeRedCardsA)
            return false;
        if (normalTimeRedCardsB != other.normalTimeRedCardsB)
            return false;
        if (normalTimeYellowCardsA != other.normalTimeYellowCardsA)
            return false;
        if (normalTimeYellowCardsB != other.normalTimeYellowCardsB)
            return false;
        if (penaltyInfo == null) {
            if (other.penaltyInfo != null)
                return false;
        } else if (!penaltyInfo.equals(other.penaltyInfo))
            return false;
        if (penaltyStatus != other.penaltyStatus)
            return false;
        if (!Arrays.equals(playerScoringGoalN, other.playerScoringGoalN))
            return false;
        if (previousPeriodCardsA != other.previousPeriodCardsA)
            return false;
        if (previousPeriodCardsB != other.previousPeriodCardsB)
            return false;
        if (previousPeriodCornersA != other.previousPeriodCornersA)
            return false;
        if (previousPeriodCornersB != other.previousPeriodCornersB)
            return false;
        if (previousPeriodGoalsA != other.previousPeriodGoalsA)
            return false;
        if (previousPeriodGoalsB != other.previousPeriodGoalsB)
            return false;
        if (previousPeriodRedCardsA != other.previousPeriodRedCardsA)
            return false;
        if (previousPeriodRedCardsB != other.previousPeriodRedCardsB)
            return false;
        if (previousPeriodYellowCardsA != other.previousPeriodYellowCardsA)
            return false;
        if (previousPeriodYellowCardsB != other.previousPeriodYellowCardsB)
            return false;
        if (redCardsA != other.redCardsA)
            return false;
        if (redCardsB != other.redCardsB)
            return false;
        if (!Arrays.equals(redCardsInPeriodN, other.redCardsInPeriodN))
            return false;
        if (scoreHistory == null) {
            if (other.scoreHistory != null)
                return false;
        } else if (!scoreHistory.equals(other.scoreHistory))
            return false;
        if (secondHalfCorners == null) {
            if (other.secondHalfCorners != null)
                return false;
        } else if (!secondHalfCorners.equals(other.secondHalfCorners))
            return false;
        if (secondHalfGoals == null) {
            if (other.secondHalfGoals != null)
                return false;
        } else if (!secondHalfGoals.equals(other.secondHalfGoals))
            return false;
        if (secondHalfRedCards == null) {
            if (other.secondHalfRedCards != null)
                return false;
        } else if (!secondHalfRedCards.equals(other.secondHalfRedCards))
            return false;
        if (secondHalfYellowCards == null) {
            if (other.secondHalfYellowCards != null)
                return false;
        } else if (!secondHalfYellowCards.equals(other.secondHalfYellowCards))
            return false;
        if (secondLegMatch == null) {
            if (other.secondLegMatch != null)
                return false;
        } else if (!secondLegMatch.equals(other.secondLegMatch))
            return false;
        if (shootOutFirst != other.shootOutFirst)
            return false;
        if (shootOutGoalsA != other.shootOutGoalsA)
            return false;
        if (shootOutGoalsB != other.shootOutGoalsB)
            return false;
        if (shootOutTimeCounter != other.shootOutTimeCounter)
            return false;
        if (shooutOutCounterA != other.shooutOutCounterA)
            return false;
        if (shooutOutCounterB != other.shooutOutCounterB)
            return false;
        if (teamScoringLastCorner != other.teamScoringLastCorner)
            return false;
        if (teamScoringLastGoal != other.teamScoringLastGoal)
            return false;
        if (teamSheet == null) {
            if (other.teamSheet != null)
                return false;
        } else if (!teamSheet.equals(other.teamSheet))
            return false;
        if (yellowCardsA != other.yellowCardsA)
            return false;
        if (yellowCardsB != other.yellowCardsB)
            return false;
        if (!Arrays.equals(yellowCardsInPeriodN, other.yellowCardsInPeriodN))
            return false;
        return true;
    }

    /**
     * Returns the id of the team winning the current five minutes
     * 
     * @return
     */

    public TeamId getFiveMinsMatchWinner() {
        TeamId winnerTeamId = TeamId.UNKNOWN;
        if (lastFiveMinsNO < fiveMinsNo) {
            int fiveMinsGoalsA = Integer.valueOf(fiveMinsGoalA[lastFiveMinsNO]);
            int fiveMinsGoalsB = Integer.valueOf(fiveMinsGoalB[lastFiveMinsNO]);
            // System.out.println(currentFiveMinsSequenceNo + " - "+
            // fiveMinsGoalsA+" - "+fiveMinsGoalsB);
            if (fiveMinsGoalsA > fiveMinsGoalsB)
                winnerTeamId = TeamId.A;
            if (fiveMinsGoalsA < fiveMinsGoalsB)
                winnerTeamId = TeamId.B;
            return winnerTeamId;
        } else {
            return null;
        }
    }

    /**
     * Returns the id of the team winning the current five minutes
     * 
     * @return
     */

    public TeamId getTenMinsMatchWinner() {
        TeamId winnerTeamId = TeamId.UNKNOWN;
        if (lastFiveMinsNO < fiveMinsNo && fiveMinsNo % 2 == 0) {
            int fiveMinsGoalsA = Integer.valueOf(fiveMinsGoalA[lastFiveMinsNO]);
            int fiveMinsGoalsB = Integer.valueOf(fiveMinsGoalB[lastFiveMinsNO]);
            // System.out.println(currentFiveMinsSequenceNo + " - "+
            // fiveMinsGoalsA+" - "+fiveMinsGoalsB);
            if (fiveMinsGoalsA > fiveMinsGoalsB)
                winnerTeamId = TeamId.A;
            if (fiveMinsGoalsA < fiveMinsGoalsB)
                winnerTeamId = TeamId.B;
            return winnerTeamId;
        } else {
            return null;
        }
    }

    /**
     * Returns the id of the team winning the current five minutes
     * 
     * @return
     */

    public TeamId getFifteenMinsMatchWinner() {
        TeamId winnerTeamId = TeamId.UNKNOWN;
        if (lastFiveMinsNO < fiveMinsNo && fiveMinsNo % 3 == 0) {
            int fiveMinsGoalsA = Integer.valueOf(fiveMinsGoalA[lastFiveMinsNO]);
            int fiveMinsGoalsB = Integer.valueOf(fiveMinsGoalB[lastFiveMinsNO]);
            // System.out.println(currentFiveMinsSequenceNo + " - "+
            // fiveMinsGoalsA+" - "+fiveMinsGoalsB);
            if (fiveMinsGoalsA > fiveMinsGoalsB)
                winnerTeamId = TeamId.A;
            if (fiveMinsGoalsA < fiveMinsGoalsB)
                winnerTeamId = TeamId.B;
            return winnerTeamId;
        } else {
            return null;
        }
    }

    /**
     * Check if any goal happened in the current five minutes of match
     * 
     * @return
     */

    public Boolean getFiveMinsIfGoal() {
        if (lastFiveMinsNO < fiveMinsNo) {
            int fiveMinsGoalsA = Integer.valueOf(fiveMinsGoalA[lastFiveMinsNO]);
            int fiveMinsGoalsB = Integer.valueOf(fiveMinsGoalB[lastFiveMinsNO]);
            // System.out.println(currentFiveMinsSequenceNo + " - "+
            // fiveMinsGoalsA+" - "+fiveMinsGoalsB);
            if ((fiveMinsGoalsA + fiveMinsGoalsB) > 0)
                return true;
            return false;

        } else {
            return null;
        }
    }

    /**
     * Get team winning the ten/fifteen minutes of the match findMatchWinnerForPeriod(int periodIndicator, int
     * typeIndicator) periodIndicator = 0 - 25, is index of five minutes match slot typeIndicator = 1 checks ten minutes
     * match result typeIndicator = 2 checks fifteen minutes match result
     * 
     * @return
     */
    public TeamId findMatchWinnerForPeriod(int periodIndicator, int typeIndicator) { // typeIndicator
                                                                                     // =
                                                                                     // 1,
                                                                                     // 2
        TeamId winnerTeamId = null;
        int periodGoalsA = 0;
        int periodGoalsB = 0;
        if (typeIndicator == 1) {
            periodGoalsA = fiveMinsGoalA[periodIndicator * 2] + fiveMinsGoalA[periodIndicator * 2 + 1];
            periodGoalsB = fiveMinsGoalB[periodIndicator * 2] + fiveMinsGoalB[periodIndicator * 2 + 1];
        } else if (typeIndicator == 2) {
            periodGoalsA = fiveMinsGoalA[periodIndicator * 3] + fiveMinsGoalA[periodIndicator * 3 + 1]
                            + fiveMinsGoalA[periodIndicator * 3 + 2];
            periodGoalsB = fiveMinsGoalB[periodIndicator * 3] + fiveMinsGoalB[periodIndicator * 3 + 1]
                            + fiveMinsGoalB[periodIndicator * 3 + 2];
        } else {
            // for all the rest typeindicators
            for (int ii = 0; ii < typeIndicator; ii++) {
                periodGoalsA += fiveMinsGoalA[periodIndicator * typeIndicator + ii];
                periodGoalsB += fiveMinsGoalB[periodIndicator * typeIndicator + ii];
            }
        }
        winnerTeamId = TeamId.UNKNOWN;
        // System.out.println(currentFiveMinsSequenceNo + " - "+
        // fiveMinsGoalsA+" - "+fiveMinsGoalsB);
        if (periodGoalsA > periodGoalsB)
            winnerTeamId = TeamId.A;
        if (periodGoalsA < periodGoalsB)
            winnerTeamId = TeamId.B;
        return winnerTeamId;
    }

    /**
     * returns true if in preMatch
     */
    @Override
    public boolean preMatch() {
        return (matchPeriod == FootballMatchPeriod.PREMATCH);
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
            case IN_EXTRA_TIME_HALF_TIME:
                secs = 0;
                break;
            case IN_FIRST_HALF:
            case IN_SECOND_HALF:
                secs = normalHalfSecs + injuryTimeSecs - elapsedTimeThisPeriodSecs;
                break;
            case IN_EXTRA_TIME_FIRST_HALF:
            case IN_EXTRA_TIME_SECOND_HALF:
                secs = extraHalfSecs + injuryTimeSecs - elapsedTimeThisPeriodSecs;
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


    public boolean wasTeamFirstHalfLeader(TeamId teamIdToCheck) {
        int homeTeamGoals = 0;
        int awayTeamGoals = 0;
        for (TeamId teamId : firstHalfGoals) {
            if (TeamId.A == teamId)
                homeTeamGoals++;
            if (TeamId.B == teamId)
                awayTeamGoals++;
        }
        if (TeamId.A == teamIdToCheck)
            return homeTeamGoals > awayTeamGoals;
        if (TeamId.B == teamIdToCheck)
            return awayTeamGoals > homeTeamGoals;
        // draw
        if (TeamId.UNKNOWN == teamIdToCheck)
            return awayTeamGoals == homeTeamGoals;
        return false;
    }


    public boolean wasTeamSecondHalfLeader(TeamId teamIdToCheck) {
        int homeTeamGoals = 0;
        int awayTeamGoals = 0;
        for (TeamId teamId : secondHalfGoals) {
            if (TeamId.A == teamId)
                homeTeamGoals++;
            if (TeamId.B == teamId)
                awayTeamGoals++;
        }
        if (TeamId.A == teamIdToCheck)
            return homeTeamGoals > awayTeamGoals;
        if (TeamId.B == teamIdToCheck)
            return awayTeamGoals > homeTeamGoals;
        // draw
        if (TeamId.UNKNOWN == teamIdToCheck)
            return awayTeamGoals == homeTeamGoals;
        return false;
    }


    public boolean didTeamScoreFirst(TeamId teamIdToCheck) {
        return scoreHistory.size() > 0 && (TeamId.A == teamIdToCheck ? scoreHistory.get(0).getT1() == 1
                        : scoreHistory.get(0).getT2() == 1);
    }

    /**
     * holds the state for cards
     *
     * @author Jin
     * 
     */
    public class CardInfo implements Serializable {
        private static final long serialVersionUID = 1L;
        int mins;
        int matchPeriodIndex;
        TeamId team;
        String color;

        public int getMins() {
            return mins;
        }

        public void setMins(int mins) {
            this.mins = mins;
        }

        public int getMatchPeriodIndex() {
            return matchPeriodIndex;
        }

        public void setMatchPeriodIndex(int matchPeriodIndex) {
            this.matchPeriodIndex = matchPeriodIndex;
        }

        public TeamId getTeam() {
            return team;
        }

        public void setTeam(TeamId team) {
            this.team = team;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ((color == null) ? 0 : color.hashCode());
            result = prime * result + matchPeriodIndex;
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
            CardInfo other = (CardInfo) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
            if (color == null) {
                if (other.color != null)
                    return false;
            } else if (!color.equals(other.color))
                return false;
            if (matchPeriodIndex != other.matchPeriodIndex)
                return false;
            if (mins != other.mins)
                return false;
            if (team != other.team)
                return false;
            return true;
        }

        private FootballMatchState getOuterType() {
            return FootballMatchState.this;
        }

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

        private FootballMatchState getOuterType() {
            return FootballMatchState.this;
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
            if (x.getMatchPeriodIndex() < 3) { // change 3 to an input so can
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

    /**
     * check team to x goal, period index = 0 means full time, 1 1st half, 2 second half...
     */
    public TeamId checkTeamToXCorners(int i, int periodIndex) {
        if (this.cornerInfo.size() < i)
            return null;

        int goalsA = 0;
        int goalsB = 0;
        TeamId team = null;
        for (GoalInfo x : this.cornerInfo) {
            if (x.getMatchPeriodIndex() < i) { // change 3 to an input so can
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

    public TeamId checkFirstTeamScored() {
        if (this.goalInfo.size() == 0)
            return TeamId.UNKNOWN;
        else
            return this.goalInfo.get(0).getTeam();
    }

    public TeamId checkFirstTeamScoredFirstHalf() {
        if (this.goalInfo.size() == 0)
            return TeamId.UNKNOWN;
        else if (this.goalInfo.get(0).getMatchPeriodIndex() < 2)
            return this.goalInfo.get(0).getTeam();
        else
            return TeamId.UNKNOWN;
    }

    public TeamId checkLastTeamScored() {
        if (this.goalInfo.size() == 0)
            return TeamId.UNKNOWN;
        else
            return this.goalInfo.get(goalInfo.size() - 1).getTeam();
    }

    /** Input need to be a multiply of 5 */

    public String getSequenceIdForXXMinsResult(int i) {
        return String.format("F%d", (int) Math.floor(this.elapsedTimeSecs / (i * 60)));
    }


    public Boolean getXXMinsIfGoal(int marketSeqNo, int in) {
        if (lastFiveMinsNO / (in / 5) < fiveMinsNo / (in / 5)) {
            int xxMinsGoalsA = 0;// Integer.valueOf(fiveMinsGoalA[lastFiveMinsNO]);
            int xxMinsGoalsB = 0;// Integer.valueOf(fiveMinsGoalB[lastFiveMinsNO]);
            for (int i = 0; i < (in / 5); i++) {
                xxMinsGoalsA += fiveMinsGoalA[marketSeqNo * (in / 5) + i];
                xxMinsGoalsB += fiveMinsGoalB[marketSeqNo * (in / 5) + i];
            }
            // System.out.println(currentFiveMinsSequenceNo + " - "+
            // fiveMinsGoalsA+" - "+fiveMinsGoalsB);
            if ((xxMinsGoalsA + xxMinsGoalsB) > 0)
                return true;
            return false;
        } else {
            return null;
        }
    }


    public void setMatchStateEqualToFeedState(MatchState matchState, FootballMatchStateFromFeed matchIncidentState) {
        // System.out.println("Match State From Feeds: " + matchIncidentState);
        // System.out.println("Match State For Present: " + this);

        this.setGoalsA(matchIncidentState.getGoalsA());
        this.setGoalsB(matchIncidentState.getGoalsB());
        this.setCornersA(matchIncidentState.getCornersA());
        this.setCornersB(matchIncidentState.getCornersB());
        this.setYellowCardsA(matchIncidentState.getYellowCardA());
        this.setYellowCardsB(matchIncidentState.getYellowCardB());
        this.setRedCardsA(matchIncidentState.getRedCardA());
        this.setRedCardsB(matchIncidentState.getRedCardB());
        if (!((FootballMatchState) matchState).isNormalTimeMatchCompleted()) {
            this.setNormalTimeGoalsA(matchIncidentState.getGoalsA());
            this.setNormalTimeGoalsB(matchIncidentState.getGoalsB());
        }
        this.setMatchPeriod(matchIncidentState.getMatchPeriod());
    }

    public int checkTimeFirstGoal(TeamId team, int i) {

        if (team.equals(TeamId.UNKNOWN)) {
            if (goalsA + goalsB == 0)
                return (90 / i);
            else
                return Math.min(firstGoalForTeam(this.fiveMinsGoalA), firstGoalForTeam(this.fiveMinsGoalB));
        } else if (team.equals(TeamId.A)) {
            if (goalsA == 0)
                return (90 / i);
            else
                return firstGoalForTeam(this.fiveMinsGoalA);

        } else if (team.equals(TeamId.B)) {
            if (goalsB == 0)
                return (90 / i);
            else
                return firstGoalForTeam(this.fiveMinsGoalB);

        } else {
            throw new IllegalArgumentException("Should not get here");
        }
    }

    private int firstGoalForTeam(int[] fiveMinsGoalA2) { // only check first
                                                         // goal time for
                                                         // full time

        for (int i = 0; i < 18; i++) {// 17 is the last 5 minutes section for
                                      // the full time
            if (fiveMinsGoalA2[i] != 0)
                return i / 2;
        }
        return 9;// no goal case
    }

    public int cornerInXXMins(int fiveMinutesSequenceNo, TeamId unknown, int m) { // XX
                                                                                  // is
                                                                                  // multiply
                                                                                  // of
                                                                                  // 5,
                                                                                  // when
                                                                                  // m=1,
                                                                                  // is
                                                                                  // in
                                                                                  // 5
                                                                                  // minutes
        int tempTotal = 0;
        int timeMinutes = fiveMinutesSequenceNo * 5 * m;
        if (unknown.equals(TeamId.UNKNOWN))
            for (GoalInfo corner : this.cornerInfo) {
                if (corner.getMins() > (timeMinutes + 5 * m))
                    break;
                if (corner.getMins() >= (timeMinutes))
                    tempTotal++;
            }
        else if (unknown.equals(TeamId.A))
            for (GoalInfo corner : this.cornerInfo) {
                if (corner.getMins() > (timeMinutes + 5 * m))
                    break;
                if (corner.getMins() >= (timeMinutes) && corner.team.equals(TeamId.A))
                    tempTotal++;
            }
        else if (unknown.equals(TeamId.B))
            for (GoalInfo corner : this.cornerInfo) {
                if (corner.getMins() > (timeMinutes + 5 * m))
                    break;
                if (corner.getMins() >= (timeMinutes) && corner.team.equals(TeamId.B))
                    tempTotal++;
            }
        return tempTotal;
    }

    public boolean bookingNotYetHappend(int index, int i, TeamId unknown) {
        // int index = Integer.parseInt(fiveMinutesIndicator.substring(1));
        for (CardInfo card : this.bookingInfo) {
            int mins = card.getMins();
            if (index * i <= mins && (index + 1) * i > mins)
                return true;
            else if ((index + 1) * i < mins)
                return false;
        }
        return false;
    }

    @Override
    public AlgoMatchState generateMatchStateForMatchResult(MatchResultMap matchManualResult) {

        boolean halfGoalsInput = false;
        boolean halfCornersInput = false;
        boolean halfYellowCardsInput = false;
        boolean halfRedCardsInput = false;

        boolean fullGoalsInput = false;
        boolean fullCornersInput = false;
        boolean fullYellowCardsInput = false;
        boolean fullRedCardsInput = false;

        Map<String, MatchResultElement> map = matchManualResult.getMap();
        int numOfScores = 2;

        if (((FootballMatchFormat) this.getMatchFormat()).getExtraTimeMinutes() > 0)
            numOfScores = 4;

        PairOfIntegers[] halfResultGoals = new PairOfIntegers[numOfScores];
        PairOfIntegers[] halfResultCorners = new PairOfIntegers[numOfScores];
        PairOfIntegers[] halfResultYellowCards = new PairOfIntegers[numOfScores];
        PairOfIntegers[] halfResultRedCards = new PairOfIntegers[numOfScores];

        PairOfIntegers[] ftResultGoals = new PairOfIntegers[1];
        PairOfIntegers[] ftResultCorners = new PairOfIntegers[1];
        PairOfIntegers[] ftResultYellowCards = new PairOfIntegers[1];
        PairOfIntegers[] ftResultRedCards = new PairOfIntegers[1];

        if (map.get("firstHalfGoals") != null && map.get("secondHalfGoals") != null)
            if (map.get("firstHalfGoals").valueAsPairOfIntegersCheckingNegatives() != null
                            && map.get("secondHalfGoals").valueAsPairOfIntegersCheckingNegatives() != null) {

                halfResultGoals[0] = map.get("firstHalfGoals").valueAsPairOfIntegersCheckingNegatives();
                halfResultGoals[1] = map.get("secondHalfGoals").valueAsPairOfIntegersCheckingNegatives();
                if ((halfResultGoals[0].A > -1 || halfResultGoals[1].A > -1 || halfResultGoals[0].B > -1
                                || halfResultGoals[1].B > -1)) {
                    halfGoalsInput = true;
                }
            } else if (map.get("firstHalfGoals").valueAsPairOfIntegersCheckingNegatives() != null
                            && map.get("secondHalfGoals").valueAsPairOfIntegersCheckingNegatives() != null) {

                halfResultGoals[0] = map.get("firstHalfGoals").valueAsPairOfIntegersCheckingNegatives();
                halfResultGoals[1] = map.get("secondHalfGoals").valueAsPairOfIntegersCheckingNegatives();
                halfGoalsInput = true;
            }
        if (map.get("totalGoals") != null)
            if (map.get("totalGoals").valueAsPairOfIntegersCheckingNegatives() != null) {

                ftResultGoals[0] = map.get("totalGoals").valueAsPairOfIntegersCheckingNegatives();

                if (ftResultGoals[0].A > -1 || ftResultGoals[1].B > -1) {
                    fullGoalsInput = true;
                }
            } else if (map.get("totalGoals").valueAsPairOfIntegersCheckingNegatives() != null) {

                ftResultGoals[0] = map.get("totalGoals").valueAsPairOfIntegersCheckingNegatives();
                fullGoalsInput = true;
            }

        if (map.get("firstHalfCorners") != null && map.get("secondHalfCorners") != null)
            if (map.get("firstHalfCorners").valueAsPairOfIntegersCheckingNegatives() != null
                            && map.get("secondHalfCorners").valueAsPairOfIntegersCheckingNegatives() != null) {

                halfResultCorners[0] = map.get("firstHalfCorners").valueAsPairOfIntegersCheckingNegatives();
                halfResultCorners[1] = map.get("secondHalfCorners").valueAsPairOfIntegersCheckingNegatives();
                if ((halfResultCorners[0].A > -1 || halfResultCorners[1].A > -1 || halfResultCorners[0].B > -1
                                || halfResultCorners[1].B > -1)) {
                    halfCornersInput = true;
                }
            } else if (map.get("firstHalfCorners").valueAsPairOfIntegersCheckingNegatives() != null
                            && map.get("secondHalfCorners").valueAsPairOfIntegersCheckingNegatives() != null) {

                halfResultCorners[0] = map.get("firstHalfCorners").valueAsPairOfIntegersCheckingNegatives();
                halfResultCorners[1] = map.get("secondHalfCorners").valueAsPairOfIntegersCheckingNegatives();
                halfCornersInput = true;
            }
        if (map.get("totalCorners") != null)
            if (map.get("totalCorners").valueAsPairOfIntegersCheckingNegatives() != null) {

                ftResultCorners[0] = map.get("totalCorners").valueAsPairOfIntegersCheckingNegatives();

                if (ftResultCorners[0].A > -1 || ftResultCorners[1].B > -1) {
                    fullCornersInput = true;
                }
            } else if (map.get("totalCorners").valueAsPairOfIntegersCheckingNegatives() != null) {

                ftResultCorners[0] = map.get("totalCorners").valueAsPairOfIntegersCheckingNegatives();
                fullCornersInput = true;
            }
        if (map.get("firstHalfCorners") != null && map.get("secondHalfCorners") != null)
            if (map.get("firstHalfCorners").valueAsPairOfIntegersCheckingNegatives() != null
                            && map.get("secondHalfCorners").valueAsPairOfIntegersCheckingNegatives() != null) {

                halfResultCorners[0] = map.get("firstHalfCorners").valueAsPairOfIntegersCheckingNegatives();
                halfResultCorners[1] = map.get("secondHalfCorners").valueAsPairOfIntegersCheckingNegatives();
                if ((halfResultCorners[0].A > -1 || halfResultCorners[1].A > -1 || halfResultCorners[0].B > -1
                                || halfResultCorners[1].B > -1)) {
                    halfCornersInput = true;
                }
            } else if (map.get("firstHalfCorners").valueAsPairOfIntegersCheckingNegatives() != null
                            && map.get("secondHalfCorners").valueAsPairOfIntegersCheckingNegatives() != null) {

                halfResultCorners[0] = map.get("firstHalfCorners").valueAsPairOfIntegersCheckingNegatives();
                halfResultCorners[1] = map.get("secondHalfCorners").valueAsPairOfIntegersCheckingNegatives();
                halfCornersInput = true;
            }
        if (map.get("totalCorners") != null)
            if (map.get("totalCorners").valueAsPairOfIntegersCheckingNegatives() != null) {

                ftResultCorners[0] = map.get("totalCorners").valueAsPairOfIntegersCheckingNegatives();

                if (ftResultCorners[0].A > -1 || ftResultCorners[1].B > -1) {
                    fullCornersInput = true;
                }
            } else if (map.get("totalCorners").valueAsPairOfIntegersCheckingNegatives() != null) {

                ftResultCorners[0] = map.get("totalCorners").valueAsPairOfIntegersCheckingNegatives();
                fullCornersInput = true;
            }

        if (map.get("firstHalfYellowCards") != null && map.get("secondHalfYellowCards") != null)
            if (map.get("firstHalfYellowCards").valueAsPairOfIntegersCheckingNegatives() != null
                            && map.get("secondHalfYellowCards").valueAsPairOfIntegersCheckingNegatives() != null) {

                halfResultYellowCards[0] = map.get("firstHalfYellowCards").valueAsPairOfIntegersCheckingNegatives();
                halfResultYellowCards[1] = map.get("secondHalfYellowCards").valueAsPairOfIntegersCheckingNegatives();
                if ((halfResultYellowCards[0].A > -1 || halfResultYellowCards[1].A > -1
                                || halfResultYellowCards[0].B > -1 || halfResultYellowCards[1].B > -1)) {
                    halfYellowCardsInput = true;
                }
            } else if (map.get("firstHalfYellowCards").valueAsPairOfIntegersCheckingNegatives() != null
                            && map.get("secondHalfYellowCards").valueAsPairOfIntegersCheckingNegatives() != null) {

                halfResultYellowCards[0] = map.get("firstHalfYellowCards").valueAsPairOfIntegersCheckingNegatives();
                halfResultYellowCards[1] = map.get("secondHalfYellowCards").valueAsPairOfIntegersCheckingNegatives();
                halfYellowCardsInput = true;
            }

        if (map.get("totalYellowCards") != null)
            if (map.get("totalYellowCards").valueAsPairOfIntegersCheckingNegatives() != null) {

                ftResultYellowCards[0] = map.get("totalYellowCards").valueAsPairOfIntegersCheckingNegatives();

                if (ftResultYellowCards[0].A > -1 || ftResultYellowCards[1].B > -1) {
                    fullYellowCardsInput = true;
                }
            } else if (map.get("totalYellowCards").valueAsPairOfIntegersCheckingNegatives() != null) {

                ftResultYellowCards[0] = map.get("totalYellowCards").valueAsPairOfIntegersCheckingNegatives();
                fullYellowCardsInput = true;
            }
        if (map.get("firstHalfRedCards") != null)
            if (map.get("firstHalfRedCards").valueAsPairOfIntegersCheckingNegatives() != null
                            && map.get("secondHalfRedCards").valueAsPairOfIntegersCheckingNegatives() != null) {

                halfResultRedCards[0] = map.get("firstHalfRedCards").valueAsPairOfIntegersCheckingNegatives();
                halfResultRedCards[1] = map.get("secondHalfRedCards").valueAsPairOfIntegersCheckingNegatives();
                if ((halfResultRedCards[0].A > -1 || halfResultRedCards[1].A > -1 || halfResultRedCards[0].B > -1
                                || halfResultRedCards[1].B > -1)) {
                    halfRedCardsInput = true;
                }
            } else if (map.get("firstHalfRedCards").valueAsPairOfIntegersCheckingNegatives() != null
                            && map.get("secondHalfRedCards").valueAsPairOfIntegersCheckingNegatives() != null) {

                halfResultRedCards[0] = map.get("firstHalfRedCards").valueAsPairOfIntegersCheckingNegatives();
                halfResultRedCards[1] = map.get("secondHalfRedCards").valueAsPairOfIntegersCheckingNegatives();
                halfRedCardsInput = true;
            }
        if (map.get("totalRedCards") != null)
            if (map.get("totalRedCards").valueAsPairOfIntegersCheckingNegatives() != null) {

                ftResultRedCards[0] = map.get("totalRedCards").valueAsPairOfIntegersCheckingNegatives();

                if (ftResultRedCards[0].A > -1 || ftResultRedCards[1].B > -1) {
                    fullRedCardsInput = true;
                }
            } else if (map.get("totalRedCards").valueAsPairOfIntegersCheckingNegatives() != null) {

                ftResultRedCards[0] = map.get("totalRedCards").valueAsPairOfIntegersCheckingNegatives();
                fullRedCardsInput = true;
            }

        if (numOfScores == 4) {
            if (map.get("extraTimeFirstHalfGoals") != null && map.get("extraTimeSecondHalfGoals") != null) {
                halfResultGoals[2] = map.get("extraTimeFirstHalfGoals").valueAsPairOfIntegersCheckingNegatives();
                halfResultGoals[3] = map.get("extraTimeSecondHalfGoals").valueAsPairOfIntegersCheckingNegatives();
            } else
                numOfScores = 2;
        }

        FootballMatchState endMatchState = (FootballMatchState) this.copy();

        if (fullGoalsInput) {
            if (halfGoalsInput) {
                updateMatchStateForPeriodGoals(endMatchState, halfResultGoals, numOfScores);
            } else {
                updateMatchStateForFullTimeGoals(endMatchState, ftResultGoals);
            }
        } else if (halfGoalsInput) {
            updateMatchStateForPeriodGoals(endMatchState, halfResultGoals, numOfScores);
        }
        if (fullCornersInput) {
            if (halfCornersInput) {
                updateMatchStateForPeriodCorners(endMatchState, halfResultCorners, numOfScores);
            } else {
                updateMatchStateForFullTimeCorners(endMatchState, ftResultCorners);
            }
        } else if (halfCornersInput) {
            updateMatchStateForPeriodCorners(endMatchState, halfResultCorners, numOfScores);
        }
        if (fullRedCardsInput || fullYellowCardsInput) {
            if (halfRedCardsInput || halfYellowCardsInput) {
                updateMatchStateForPeriodCards(endMatchState, halfResultRedCards, halfRedCardsInput,
                                halfResultYellowCards, halfYellowCardsInput, numOfScores);
            } else {
                updateMatchStateForFullTimeCards(endMatchState, ftResultRedCards, fullRedCardsInput,
                                ftResultYellowCards, fullYellowCardsInput);
            }
        } else if (halfRedCardsInput || halfYellowCardsInput) {
            updateMatchStateForPeriodCards(endMatchState, halfResultRedCards, halfRedCardsInput, halfResultYellowCards,
                            halfYellowCardsInput, numOfScores);
        }

        endMatchState.setMatchPeriod(FootballMatchPeriod.MATCH_COMPLETED);
        return endMatchState;
    }

    private void updateMatchStateForFullTimeCards(FootballMatchState endMatchState, PairOfIntegers[] ftResultRedCards,
                    boolean fullRedCardsInput, PairOfIntegers[] ftResultYellowCards, boolean fullYellowCardsInput) {

        int totalYellowCardsA = 0;
        int totalYellowCardsB = 0;
        int totalRedCardsA = 0;
        int totalRedCardsB = 0;

        if (fullYellowCardsInput) {
            totalYellowCardsA = ftResultYellowCards[0].A;
            totalYellowCardsB = ftResultYellowCards[0].B;

            endMatchState.setYellowCardsA(totalYellowCardsA);
            endMatchState.setYellowCardsB(totalYellowCardsB);
        }

        if (fullRedCardsInput) {
            totalRedCardsA = ftResultRedCards[0].A;
            totalRedCardsB = ftResultRedCards[0].B;

            endMatchState.setRedCardsA(totalRedCardsA);
            endMatchState.setRedCardsB(totalRedCardsB);
        }

        if (fullYellowCardsInput && fullRedCardsInput) {
            endMatchState.setCardsA(totalRedCardsA + totalYellowCardsA);
            endMatchState.setCardsB(totalRedCardsB + totalYellowCardsB);
        }

    }

    private void updateMatchStateForFullTimeCorners(FootballMatchState endMatchState,
                    PairOfIntegers[] ftResultCorners) {
        int totalCornersA = ftResultCorners[0].A;
        int totalCornersB = ftResultCorners[0].B;

        endMatchState.setCornersA(totalCornersA);
        endMatchState.setCornersB(totalCornersB);

    }

    private void updateMatchStateForFullTimeGoals(FootballMatchState endMatchState, PairOfIntegers[] ftResultGoals) {

        int totalGoalsA = ftResultGoals[0].A;
        int totalGoalsB = ftResultGoals[0].B;

        endMatchState.setGoalsA(totalGoalsA);
        endMatchState.setGoalsB(totalGoalsB);
    }

    private void updateMatchStateForPeriodCards(FootballMatchState endMatchState, PairOfIntegers[] halfResultRedCards,
                    boolean redCardsInput, PairOfIntegers[] halfResultYellowCards, boolean yellowCardsInput,
                    int numOfScores) {

        int totalRedCardsAForNormalTime = 0;
        int totalRedCardsBForNormalTime = 0;

        int totalRedCardsAForExtraTimeFirstHalf = 0;
        int totalRedCardsAForExtraTimeSecondHalf = 0;

        int totalRedCardsBForExtraTimeFirstHalf = 0;
        int totalRedCardsBForExtraTimeSecondHalf = 0;

        if (redCardsInput) {
            for (int i = 0; i < numOfScores; i++) {

                int halfRedCardsTeamA = halfResultRedCards[i].A;
                int halfRedCardsTeamB = halfResultRedCards[i].B;

                endMatchState.setRedCardsInPeriodN(i, halfRedCardsTeamA, halfRedCardsTeamB);

                if (i < 2) {
                    totalRedCardsAForNormalTime += halfRedCardsTeamA;
                    totalRedCardsBForNormalTime += halfRedCardsTeamB;
                }

                if (i > 1) {
                    if (i == 2) {
                        totalRedCardsAForExtraTimeFirstHalf = halfRedCardsTeamA;
                        totalRedCardsBForExtraTimeFirstHalf = halfRedCardsTeamB;
                    }

                    if (i == 3) {
                        totalRedCardsAForExtraTimeSecondHalf = halfRedCardsTeamA;
                        totalRedCardsBForExtraTimeSecondHalf = halfRedCardsTeamB;
                    }
                }
            }
        }

        int totalRedCardsA = totalRedCardsAForNormalTime + totalRedCardsAForExtraTimeFirstHalf
                        + totalRedCardsAForExtraTimeSecondHalf;
        int totalRedCardsB = totalRedCardsBForNormalTime + totalRedCardsBForExtraTimeFirstHalf
                        + totalRedCardsBForExtraTimeSecondHalf;

        endMatchState.setRedCardsA(totalRedCardsA);
        endMatchState.setRedCardsB(totalRedCardsB);

        endMatchState.setNormalTimeRedCardsA(totalRedCardsAForNormalTime);
        endMatchState.setNormalTimeRedCardsB(totalRedCardsBForNormalTime);

        endMatchState.setExtraTimeFHRedCardsA(totalRedCardsAForExtraTimeFirstHalf);
        endMatchState.setExtraTimeFHRedCardsB(totalRedCardsBForExtraTimeFirstHalf);

        endMatchState.setExtraTimeSHRedCardsA(totalRedCardsAForExtraTimeSecondHalf);
        endMatchState.setExtraTimeSHRedCardsB(totalRedCardsBForExtraTimeSecondHalf);

        if (redCardsInput) {
            endMatchState.setPreviousPeriodRedCardsA(halfResultRedCards[0].A);
            endMatchState.setPreviousPeriodRedCardsB(halfResultRedCards[0].B);
            endMatchState.setCurrentPeriodRedCardsA(halfResultRedCards[1].A);
            endMatchState.setCurrentPeriodRedCardsB(halfResultRedCards[1].B);
        }

        int totalYellowCardsAForNormalTime = 0;
        int totalYellowCardsBForNormalTime = 0;

        int totalYellowCardsAForExtraTimeFirstHalf = 0;
        int totalYellowCardsAForExtraTimeSecondHalf = 0;

        int totalYellowCardsBForExtraTimeFirstHalf = 0;
        int totalYellowCardsBForExtraTimeSecondHalf = 0;

        if (yellowCardsInput) {
            for (int i = 0; i < numOfScores; i++) {

                int halfYellowCardsTeamA = halfResultYellowCards[i].A;
                int halfYellowCardsTeamB = halfResultYellowCards[i].B;

                endMatchState.setYellowCardsInPeriodN(i, halfYellowCardsTeamA, halfYellowCardsTeamB);

                if (i < 2) {
                    totalYellowCardsAForNormalTime += halfYellowCardsTeamA;
                    totalYellowCardsBForNormalTime += halfYellowCardsTeamB;
                }

                if (i > 1) {
                    if (i == 2) {
                        totalYellowCardsAForExtraTimeFirstHalf = halfYellowCardsTeamA;
                        totalYellowCardsBForExtraTimeFirstHalf = halfYellowCardsTeamB;
                    }

                    if (i == 3) {
                        totalYellowCardsAForExtraTimeSecondHalf = halfYellowCardsTeamA;
                        totalYellowCardsBForExtraTimeSecondHalf = halfYellowCardsTeamB;
                    }
                }
            }
        }

        int totalYellowCardsA = totalYellowCardsAForNormalTime + totalYellowCardsAForExtraTimeFirstHalf
                        + totalYellowCardsAForExtraTimeSecondHalf;
        int totalYellowCardsB = totalYellowCardsBForNormalTime + totalYellowCardsBForExtraTimeFirstHalf
                        + totalYellowCardsBForExtraTimeSecondHalf;

        endMatchState.setYellowCardsA(totalYellowCardsA);
        endMatchState.setYellowCardsB(totalYellowCardsB);

        endMatchState.setNormalTimeYellowCardsA(totalYellowCardsAForNormalTime);
        endMatchState.setNormalTimeYellowCardsB(totalYellowCardsBForNormalTime);

        endMatchState.setExtraTimeFHYellowCardsA(totalYellowCardsAForExtraTimeFirstHalf);
        endMatchState.setExtraTimeFHYellowCardsB(totalYellowCardsBForExtraTimeFirstHalf);

        endMatchState.setExtraTimeSHYellowCardsA(totalYellowCardsAForExtraTimeSecondHalf);
        endMatchState.setExtraTimeSHYellowCardsB(totalYellowCardsBForExtraTimeSecondHalf);

        if (yellowCardsInput) {
            endMatchState.setPreviousPeriodYellowCardsA(halfResultYellowCards[0].A);
            endMatchState.setPreviousPeriodYellowCardsB(halfResultYellowCards[0].B);
            endMatchState.setCurrentPeriodYellowCardsA(halfResultYellowCards[1].A);
            endMatchState.setCurrentPeriodYellowCardsB(halfResultYellowCards[1].B);
        }

        endMatchState.setCardsA(endMatchState.getCardsA());
        endMatchState.setCardsB(endMatchState.getCardsB());

        endMatchState.setNormalTimeCardsA(endMatchState.getNormalTimeCardsA());
        endMatchState.setNormalTimeCardsB(endMatchState.getNormalTimeCardsA());

        endMatchState.setExtraTimeFHCardsA(endMatchState.getExtraTimeFHCardsA());
        endMatchState.setExtraTimeFHCardsB(endMatchState.getExtraTimeFHCardsB());

        endMatchState.setExtraTimeSHCardsA(endMatchState.getExtraTimeSHCardsA());
        endMatchState.setExtraTimeSHCardsB(endMatchState.getExtraTimeSHCardsB());

        endMatchState.setPreviousPeriodCardsA(endMatchState.getPreviousPeriodCardsA());
        endMatchState.setPreviousPeriodCardsB(endMatchState.getPreviousPeriodCardsB());
        endMatchState.setCurrentPeriodCardsA(endMatchState.getCurrentPeriodCardsA());
        endMatchState.setCurrentPeriodCardsB(endMatchState.getCurrentPeriodCardsB());

    }

    private void updateMatchStateForPeriodCorners(FootballMatchState endMatchState, PairOfIntegers[] halfResultCorners,
                    int numOfScores) {

        int totalCornersAForNormalTime = 0;
        int totalCornersBForNormalTime = 0;

        int totalCornersAForExtraTimeFirstHalf = 0;
        int totalCornersAForExtraTimeSecondHalf = 0;

        int totalCornersBForExtraTimeFirstHalf = 0;
        int totalCornersBForExtraTimeSecondHalf = 0;

        for (int i = 0; i < numOfScores; i++) {

            int halfCornersTeamA = halfResultCorners[i].A;
            int halfCornersTeamB = halfResultCorners[i].B;

            endMatchState.setCornersInPeriodN(i, halfCornersTeamA, halfCornersTeamB);

            if (i < 2) {
                totalCornersAForNormalTime += halfCornersTeamA;
                totalCornersBForNormalTime += halfCornersTeamB;
            }

            if (i > 1) {
                if (i == 2) {
                    totalCornersAForExtraTimeFirstHalf = halfCornersTeamA;
                    totalCornersBForExtraTimeFirstHalf = halfCornersTeamB;
                }

                if (i == 3) {
                    totalCornersAForExtraTimeSecondHalf = halfCornersTeamA;
                    totalCornersBForExtraTimeSecondHalf = halfCornersTeamB;
                }
            }
        }

        int totalCornersA = totalCornersAForNormalTime + totalCornersAForExtraTimeFirstHalf
                        + totalCornersAForExtraTimeSecondHalf;
        int totalCornersB = totalCornersBForNormalTime + totalCornersBForExtraTimeFirstHalf
                        + totalCornersBForExtraTimeSecondHalf;

        endMatchState.setCornersA(totalCornersA);
        endMatchState.setCornersB(totalCornersB);

        endMatchState.setNormalTimeCornersA(totalCornersAForNormalTime);
        endMatchState.setNormalTimeCornersB(totalCornersBForNormalTime);

        endMatchState.setExtraTimeFHCornersA(totalCornersAForExtraTimeFirstHalf);
        endMatchState.setExtraTimeFHCornersB(totalCornersBForExtraTimeFirstHalf);

        endMatchState.setExtraTimeSHCornersA(totalCornersAForExtraTimeSecondHalf);
        endMatchState.setExtraTimeSHCornersB(totalCornersBForExtraTimeSecondHalf);

        endMatchState.setPreviousPeriodCornersA(halfResultCorners[0].A);
        endMatchState.setPreviousPeriodCornersB(halfResultCorners[0].B);
        endMatchState.setCurrentPeriodCornersA(halfResultCorners[1].A);
        endMatchState.setCurrentPeriodCornersB(halfResultCorners[1].B);

    }

    private void updateMatchStateForPeriodGoals(FootballMatchState endMatchState, PairOfIntegers[] halfResultGoals,
                    int numOfScores) {

        int totalGoalsAForNormalTime = 0;
        int totalGoalsBForNormalTime = 0;

        int totalGoalsAForExtraTimeFirstHalf = 0;
        int totalGoalsAForExtraTimeSecondHalf = 0;

        int totalGoalsBForExtraTimeFirstHalf = 0;
        int totalGoalsBForExtraTimeSecondHalf = 0;

        for (int i = 0; i < numOfScores; i++) {

            int halfGoalsTeamA = halfResultGoals[i].A;
            int halfGoalsTeamB = halfResultGoals[i].B;

            endMatchState.setGoalScoreInPeriodN(i, halfGoalsTeamA, halfGoalsTeamB);

            if (i < 2) {
                totalGoalsAForNormalTime += halfGoalsTeamA;
                totalGoalsBForNormalTime += halfGoalsTeamB;
            }

            if (i > 1) {
                if (i == 2) {
                    totalGoalsAForExtraTimeFirstHalf = halfGoalsTeamA;
                    totalGoalsBForExtraTimeFirstHalf = halfGoalsTeamB;
                }

                if (i == 3) {
                    totalGoalsAForExtraTimeSecondHalf = halfGoalsTeamA;
                    totalGoalsBForExtraTimeSecondHalf = halfGoalsTeamB;
                }
            }
        }

        int totalGoalsA =
                        totalGoalsAForNormalTime + totalGoalsAForExtraTimeFirstHalf + totalGoalsAForExtraTimeSecondHalf;
        int totalGoalsB =
                        totalGoalsBForNormalTime + totalGoalsBForExtraTimeFirstHalf + totalGoalsBForExtraTimeSecondHalf;

        endMatchState.setGoalsA(totalGoalsA);
        endMatchState.setGoalsB(totalGoalsB);

        endMatchState.setNormalTimeGoalsA(totalGoalsAForNormalTime);
        endMatchState.setNormalTimeGoalsB(totalGoalsBForNormalTime);

        endMatchState.setExtraTimeFHGoalsA(totalGoalsAForExtraTimeFirstHalf);
        endMatchState.setExtraTimeFHGoalsB(totalGoalsBForExtraTimeFirstHalf);

        endMatchState.setExtraTimeSHGoalsA(totalGoalsAForExtraTimeSecondHalf);
        endMatchState.setExtraTimeSHGoalsB(totalGoalsBForExtraTimeSecondHalf);

        endMatchState.setPreviousPeriodGoalsA(halfResultGoals[0].A);
        endMatchState.setPreviousPeriodGoalsB(halfResultGoals[0].B);
        endMatchState.setCurrentPeriodGoalsA(halfResultGoals[1].A);
        endMatchState.setCurrentPeriodGoalsB(halfResultGoals[1].B);

    }

}
