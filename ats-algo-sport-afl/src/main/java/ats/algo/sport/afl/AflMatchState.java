package ats.algo.sport.afl;

import java.util.ArrayList;
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
import ats.algo.genericsupportfunctions.PairOfIntegers;
import ats.algo.core.common.TeamId;
import ats.algo.core.matchresult.MatchResultElement;
import ats.algo.core.matchresult.MatchResultMap;
import ats.algo.sport.afl.AflMatchIncident.AflMatchIncidentType;
import ats.algo.sport.afl.AflMatchIncident.FieldPositionType;

/**
 * AlgoMatchState class for Aussie Rules
 * 
 * @author
 *
 */
public class AflMatchState extends AlgoMatchState {
    /* CJ added for penalties condition */

    /**
     * 
     */

    private static final long serialVersionUID = 1L;
    private boolean mannulResulting = false;
    private final int extraPeriodSecs;
    @JsonIgnore
    private List<GoalInfo> goalInfoList;
    private BallPosition ballPosition;
    private TeamId teamScoringLastGoal = TeamId.UNKNOWN;
    private TeamId teamScoringLastBehind = TeamId.UNKNOWN;
    private TeamId teamScoringLastPoint = TeamId.UNKNOWN;
    private int elapsedTimeAtLastGoalSecs;
    private int pointsA;
    private int pointsB;
    private int goalsA;
    private int goalsB;
    private int behindsA;
    private int behindsB;
    private PairOfIntegers[] pointScoreInQuarterN;
    private PairOfIntegers[] behindScoreInQuarterN;
    private PairOfIntegers[] goalScoreInQuarterN;
    private int elapsedTimeSecs;
    private AflMatchPeriod matchPeriodInWhichLastGoalScored;
    private int elapsedTimeAtLastMatchIncidentSecs; // the time from the last
                                                    // matchIncident report
    private int elapsedTimeThisPeriodSecs; // recent MatchIncident;
    private final int normalPeriodSecs;
    private AflMatchPeriod matchPeriod; // the state following the most
    private AflMatchFormat matchFormat;
    private int currentPeriodPointsA;
    private int currentPeriodPointsB;
    private int currentPeriodGoalsA;
    private int currentPeriodGoalsB;

    private int timeOfFirstGoalMatch;
    private int timeOfFirstGoalQuarter;

    private TeamId firstTo3B = TeamId.UNKNOWN;
    private TeamId firstTo3G = TeamId.UNKNOWN;
    private TeamId firstTo4B = TeamId.UNKNOWN;
    private TeamId firstTo4G = TeamId.UNKNOWN;
    private TeamId firstTo5B = TeamId.UNKNOWN;
    private TeamId firstTo5G = TeamId.UNKNOWN;
    private TeamId firstTo6B = TeamId.UNKNOWN;
    private TeamId firstTo6G = TeamId.UNKNOWN;

    @JsonIgnore
    MatchIncident lastMatchIncidentType;
    @JsonIgnore
    private int overtimeNo;
    @JsonIgnore
    private int normalTimePointsA;
    @JsonIgnore
    private int normalTimePointsB;

    @JsonIgnore
    private int previousPeriodPointsA;
    @JsonIgnore
    private int previousPeriodPointsB;

    @JsonIgnore
    private int previousPeriodGoalsA;
    @JsonIgnore
    private int previousPeriodGoalsB;
    @JsonIgnore
    private static final int timeIncrementSecs = 10;

    /* AFL ladbroke markets required param */
    @JsonIgnore
    int firstScoringPlay2Quarter = 0; // home goal 1, home behind 2, away goal 3, away behind 4,
                                      // default 0
    @JsonIgnore
    int firstScoringPlay3Quarter = 0;
    @JsonIgnore
    int firstScoringPlay4Quarter = 0;

    private boolean matchClockRunning;

    private int goalQ1A = 0;
    private int goalQ2A = 0;
    private int goalQ3A = 0;
    private int goalQ4A = 0;
    private int goalQEA = 0;
    private int goalQ1B = 0;
    private int goalQ2B = 0;
    private int goalQ3B = 0;
    private int goalQ4B = 0;
    private int goalQEB = 0;

    private int behindQ1A = 0;
    private int behindQ2A = 0;
    private int behindQ3A = 0;
    private int behindQ4A = 0;
    private int behindQEA = 0;
    private int behindQ1B = 0;
    private int behindQ2B = 0;
    private int behindQ3B = 0;
    private int behindQ4B = 0;
    private int behindQEB = 0;

    private int pointsQ1A = 0;
    private int pointsQ2A = 0;
    private int pointsQ3A = 0;
    private int pointsQ4A = 0;
    private int pointsQEA = 0;
    private int pointsQ1B = 0;
    private int pointsQ2B = 0;
    private int pointsQ3B = 0;
    private int pointsQ4B = 0;
    private int pointsQEB = 0;

    /**
     * json class constructor. Not for general use
     */
    public AflMatchState() {
        this(new AflMatchFormat());
        // super();
        // normalPeriodSecs = 60 * 20;
        // elapsedTimeThisPeriodSecs = 1300 - normalPeriodSecs;
        // pointsA = 3;
        // pointsB = 2;
        // elapsedTimeSecs = 1300;
        // this.matchFormat = null;
        // currentPeriodPointsA = 1;
        // currentPeriodPointsB = 0;
        // goalInfoList = new ArrayList<GoalInfo>();
        // ballPosition = new BallPosition();
        // matchPeriod = AflMatchPeriod.IN_SECOND_PERIOD;
        // extraPeriodSecs = 5 * 60;
        // matchPeriodInWhichLastGoalScored = AflMatchPeriod.PREMATCH;
        // elapsedTimeAtLastMatchIncidentSecs = 0;
        // timeOfFirstGoalMatch = -1;
        // timeOfFirstGoalQuarter = -1;
        // mannulResulting = false;
    }

    /**
     * class constructor
     * 
     * @param matchFormat
     */
    public AflMatchState(MatchFormat matchFormat) {
        super();
        this.matchFormat = (AflMatchFormat) matchFormat;
        matchPeriod = AflMatchPeriod.PREMATCH;
        ballPosition = new BallPosition();
        normalPeriodSecs = this.matchFormat.getNormalTimeMinutes() * 15;
        extraPeriodSecs = this.matchFormat.getExtraTimeMinutes() * 60;
        elapsedTimeAtLastMatchIncidentSecs = -1;
        elapsedTimeAtLastGoalSecs = -1;
        goalInfoList = new ArrayList<GoalInfo>();
        timeOfFirstGoalMatch = -1;
        timeOfFirstGoalQuarter = -1;
        mannulResulting = false;
        matchClockRunning = false;
        pointScoreInQuarterN = new PairOfIntegers[4];
        behindScoreInQuarterN = new PairOfIntegers[4];
        goalScoreInQuarterN = new PairOfIntegers[4];
        for (int i = 0; i < 4; i++) {
            pointScoreInQuarterN[i] = new PairOfIntegers();
            behindScoreInQuarterN[i] = new PairOfIntegers();
            goalScoreInQuarterN[i] = new PairOfIntegers();
        }
    }

    public boolean isMannulResulting() {
        return mannulResulting;
    }

    public void setMannulResulting(boolean mannulResulting) {
        this.mannulResulting = mannulResulting;
    }

    public int getTimeOfFirstGoalQuarter() {
        return timeOfFirstGoalQuarter;
    }

    public void setTimeOfFirstGoalQuarter(int timeOfFirstGoalQuarter) {
        this.timeOfFirstGoalQuarter = timeOfFirstGoalQuarter;
    }

    public int getTimeOfFirstGoalMatch() {
        return timeOfFirstGoalMatch;
    }

    public void setTimeOfFirstGoalMatch(int timeOfFirstGoalMatch) {
        this.timeOfFirstGoalMatch = timeOfFirstGoalMatch;
    }

    /**
     * Get goal information as a List of GoalInfo class
     * 
     * @return shootOutFirst
     */
    @JsonIgnore
    public List<GoalInfo> getGoalInfoList() {
        return goalInfoList;
    }

    public PairOfIntegers[] getPointScoreInQuarterN() {
        getCurrentPointScoreInQuarterN();
        return pointScoreInQuarterN;
    }

    public void setPointScoreInQuarterN(PairOfIntegers[] pointScoreInQuarterN) {
        this.pointScoreInQuarterN = pointScoreInQuarterN;
    }

    public PairOfIntegers[] getBehindScoreInQuarterN() {
        getCurrentBehindScoreInQuarterN();
        return behindScoreInQuarterN;
    }

    public void setBehindScoreInQuarterN(PairOfIntegers[] behindScoreInQuarterN) {
        this.behindScoreInQuarterN = behindScoreInQuarterN;
    }

    public PairOfIntegers[] getGoalScoreInQuarterN() {
        getCurrentGoalScoreInQuarterN();
        return goalScoreInQuarterN;
    }

    public void setGoalScoreInQuarterN(PairOfIntegers[] goalScoreInQuarterN) {
        this.goalScoreInQuarterN = goalScoreInQuarterN;
    }

    /**
     *
     * @param i i = 0 for the first set
     * @return
     */
    public PairOfIntegers getPointScoreInQuarterN(int i) {
        getCurrentPointScoreInQuarterN();
        if (i < 4)
            return pointScoreInQuarterN[i];
        else
            return null;
    }

    public PairOfIntegers getGoalScoreInQuarterN(int i) {
        getCurrentGoalScoreInQuarterN();
        if (i < 4)
            return goalScoreInQuarterN[i];
        else
            return null;
    }

    public PairOfIntegers getBehindScoreInQuarterN(int i) {
        getCurrentBehindScoreInQuarterN();
        if (i < 4)
            return behindScoreInQuarterN[i];
        else
            return null;
    }

    public PairOfIntegers[] getCurrentPointScoreInQuarterN() {
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 0:
                    pointScoreInQuarterN[i].A = pointsQ1A;
                    pointScoreInQuarterN[i].B = pointsQ1B;
                    break;
                case 1:
                    pointScoreInQuarterN[i].A = pointsQ2A;
                    pointScoreInQuarterN[i].B = pointsQ2B;
                    break;
                case 2:
                    pointScoreInQuarterN[i].A = pointsQ3A;
                    pointScoreInQuarterN[i].B = pointsQ3B;
                    break;
                case 3:
                    pointScoreInQuarterN[i].A = pointsQ4A;
                    pointScoreInQuarterN[i].B = pointsQ4B;
                    break;

            }

        }
        return pointScoreInQuarterN;
    }

    public PairOfIntegers[] getCurrentBehindScoreInQuarterN() {
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 0:
                    behindScoreInQuarterN[i].A = behindQ1A;
                    behindScoreInQuarterN[i].B = behindQ1B;
                    break;
                case 1:
                    behindScoreInQuarterN[i].A = behindQ2A;
                    behindScoreInQuarterN[i].B = behindQ2B;
                    break;
                case 2:
                    behindScoreInQuarterN[i].A = behindQ3A;
                    behindScoreInQuarterN[i].B = behindQ3B;
                    break;
                case 3:
                    behindScoreInQuarterN[i].A = behindQ4A;
                    behindScoreInQuarterN[i].B = behindQ4B;
                    break;

            }

        }
        return behindScoreInQuarterN;
    }

    public PairOfIntegers[] getCurrentGoalScoreInQuarterN() {
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 0:
                    goalScoreInQuarterN[i].A = goalQ1A;
                    goalScoreInQuarterN[i].B = goalQ1B;
                    break;
                case 1:
                    goalScoreInQuarterN[i].A = goalQ2A;
                    goalScoreInQuarterN[i].B = goalQ2B;
                    break;
                case 2:
                    goalScoreInQuarterN[i].A = goalQ3A;
                    goalScoreInQuarterN[i].B = goalQ3B;
                    break;
                case 3:
                    goalScoreInQuarterN[i].A = goalQ4A;
                    goalScoreInQuarterN[i].B = goalQ4B;
                    break;

            }

        }
        return goalScoreInQuarterN;
    }

    /**
     *
     * @param setNo =0 for first set
     * @param gamesA
     * @param gamesB
     */
    void setPointScoreInQuarterN(int quarterNo, int pointsA, int pointsB) {
        pointScoreInQuarterN[quarterNo].A = pointsA;
        pointScoreInQuarterN[quarterNo].B = pointsB;
    }

    void setGoalScoreInQuarterN(int quarterNo, int goalsA, int goalsB) {
        goalScoreInQuarterN[quarterNo].A = goalsA;
        goalScoreInQuarterN[quarterNo].B = goalsB;
    }

    void setBehindScoreInQuarterN(int quarterNo, int behindsA, int behindsB) {
        behindScoreInQuarterN[quarterNo].A = behindsA;
        behindScoreInQuarterN[quarterNo].B = behindsB;
    }

    /**
     * First to x Goal/ Behinds getters setters
     * 
     **/
    public TeamId getFirstTo3B() {
        return firstTo3B;
    }

    public void setFirstTo3B(TeamId firstTo3B) {
        this.firstTo3B = firstTo3B;
    }

    public TeamId getFirstTo3G() {
        return firstTo3G;
    }

    public void setFirstTo3G(TeamId firstTo3G) {
        this.firstTo3G = firstTo3G;
    }

    public TeamId getFirstTo4B() {
        return firstTo4B;
    }

    public void setFirstTo4B(TeamId firstTo4B) {
        this.firstTo4B = firstTo4B;
    }

    public TeamId getFirstTo4G() {
        return firstTo4G;
    }

    public void setFirstTo4G(TeamId firstTo4G) {
        this.firstTo4G = firstTo4G;
    }

    public TeamId getFirstTo5B() {
        return firstTo5B;
    }

    public void setFirstTo5B(TeamId firstTo5B) {
        this.firstTo5B = firstTo5B;
    }

    public TeamId getFirstTo5G() {
        return firstTo5G;
    }

    public void setFirstTo5G(TeamId firstTo5G) {
        this.firstTo5G = firstTo5G;
    }

    public TeamId getFirstTo6B() {
        return firstTo6B;
    }

    public void setFirstTo6B(TeamId firstTo6B) {
        this.firstTo6B = firstTo6B;
    }

    public TeamId getFirstTo6G() {
        return firstTo6G;
    }

    public void setFirstTo6G(TeamId firstTo6G) {
        this.firstTo6G = firstTo6G;
    }

    public int getPointsQ1A() {
        return pointsQ1A;
    }

    public void setPointsQ1A(int pointsQ1A) {
        this.pointsQ1A = pointsQ1A;
    }

    public int getPointsQ2A() {
        return pointsQ2A;
    }

    public void setPointsQ2A(int pointsQ2A) {
        this.pointsQ2A = pointsQ2A;
    }

    public int getPointsQ3A() {
        return pointsQ3A;
    }

    public void setPointsQ3A(int pointsQ3A) {
        this.pointsQ3A = pointsQ3A;
    }

    public int getPointsQ4A() {
        return pointsQ4A;
    }

    public void setPointsQ4A(int pointsQ4A) {
        this.pointsQ4A = pointsQ4A;
    }

    public int getPointsQEA() {
        return pointsQEA;
    }

    public void setPointsQEA(int pointsQEA) {
        this.pointsQEA = pointsQEA;
    }

    public int getPointsQ1B() {
        return pointsQ1B;
    }

    public void setPointsQ1B(int pointsQ1B) {
        this.pointsQ1B = pointsQ1B;
    }

    public int getPointsQ2B() {
        return pointsQ2B;
    }

    public void setPointsQ2B(int pointsQ2B) {
        this.pointsQ2B = pointsQ2B;
    }

    public int getPointsQ3B() {
        return pointsQ3B;
    }

    public void setPointsQ3B(int pointsQ3B) {
        this.pointsQ3B = pointsQ3B;
    }

    public int getPointsQ4B() {
        return pointsQ4B;
    }

    public void setPointsQ4B(int pointsQ4B) {
        this.pointsQ4B = pointsQ4B;
    }

    public int getPointsQEB() {
        return pointsQEB;
    }

    public void setPointsQEB(int pointsQEB) {
        this.pointsQEB = pointsQEB;
    }

    /**
     * Set goal information as a List of GoalInfo class
     * 
     * @param goalInfo
     */
    @JsonIgnore
    public void setGoalInfoList(List<GoalInfo> goalInfoList) {
        this.goalInfoList = goalInfoList;
    }

    @JsonIgnore
    public int getQ1PointsA() {
        return goalQ1A * 6 + behindQ1A;
    }

    @JsonIgnore
    public int getQ1PointsB() {
        return goalQ1B * 6 + behindQ1B;
    }

    @JsonIgnore
    public int getQ2PointsA() {
        return goalQ2A * 6 + behindQ2A;
    }

    @JsonIgnore
    public int getQ2PointsB() {
        return goalQ2B * 6 + behindQ2B;
    }

    @JsonIgnore
    public int getQ3PointsA() {
        return goalQ3A * 6 + behindQ3A;
    }

    @JsonIgnore
    public int getQ3PointsB() {
        return goalQ3B * 6 + behindQ3B;
    }

    @JsonIgnore
    public int getQ4PointsA() {
        return goalQ4A * 6 + behindQ4A;
    }

    @JsonIgnore
    public int getQ4PointsB() {
        return goalQ4B * 6 + behindQ4B;
    }

    public int getGoalQ1A() {
        return goalQ1A;
    }

    public void setGoalQ1A(int goalQ1A) {
        this.goalQ1A = goalQ1A;
    }

    public int getGoalQ2A() {
        return goalQ2A;
    }

    public void setGoalQ2A(int goalQ2A) {
        this.goalQ2A = goalQ2A;
    }

    public int getGoalQ3A() {
        return goalQ3A;
    }

    public void setGoalQ3A(int goalQ3A) {
        this.goalQ3A = goalQ3A;
    }

    public int getGoalQ4A() {
        return goalQ4A;
    }

    public void setGoalQ4A(int goalQ4A) {
        this.goalQ4A = goalQ4A;
    }

    public int getGoalQEA() {
        return goalQEA;
    }

    public void setGoalQEA(int goalQEA) {
        this.goalQEA = goalQEA;
    }

    public int getGoalQ1B() {
        return goalQ1B;
    }

    public void setGoalQ1B(int goalQ1B) {
        this.goalQ1B = goalQ1B;
    }

    public int getGoalQ2B() {
        return goalQ2B;
    }

    public void setGoalQ2B(int goalQ2B) {
        this.goalQ2B = goalQ2B;
    }

    public int getGoalQ3B() {
        return goalQ3B;
    }

    public void setGoalQ3B(int goalQ3B) {
        this.goalQ3B = goalQ3B;
    }

    public int getGoalQ4B() {
        return goalQ4B;
    }

    public void setGoalQ4B(int goalQ4B) {
        this.goalQ4B = goalQ4B;
    }

    public int getGoalQEB() {
        return goalQEB;
    }

    public void setGoalQEB(int goalQEB) {
        this.goalQEB = goalQEB;
    }

    public int getBehindQ1A() {
        return behindQ1A;
    }

    public void setBehindQ1A(int behindQ1A) {
        this.behindQ1A = behindQ1A;
    }

    public int getBehindQ2A() {
        return behindQ2A;
    }

    public void setBehindQ2A(int behindQ2A) {
        this.behindQ2A = behindQ2A;
    }

    public int getBehindQ3A() {
        return behindQ3A;
    }

    public void setBehindQ3A(int behindQ3A) {
        this.behindQ3A = behindQ3A;
    }

    public int getBehindQ4A() {
        return behindQ4A;
    }

    public void setBehindQ4A(int behindQ4A) {
        this.behindQ4A = behindQ4A;
    }

    public int getBehindQEA() {
        return behindQEA;
    }

    public void setBehindQEA(int behindQEA) {
        this.behindQEA = behindQEA;
    }

    public int getBehindQ1B() {
        return behindQ1B;
    }

    public void setBehindQ1B(int behindQ1B) {
        this.behindQ1B = behindQ1B;
    }

    public int getBehindQ2B() {
        return behindQ2B;
    }

    public void setBehindQ2B(int behindQ2B) {
        this.behindQ2B = behindQ2B;
    }

    public int getBehindQ3B() {
        return behindQ3B;
    }

    public void setBehindQ3B(int behindQ3B) {
        this.behindQ3B = behindQ3B;
    }

    public int getBehindQ4B() {
        return behindQ4B;
    }

    public void setBehindQ4B(int behindQ4B) {
        this.behindQ4B = behindQ4B;
    }

    public int getBehindQEB() {
        return behindQEB;
    }

    public void setBehindQEB(int behindQEB) {
        this.behindQEB = behindQEB;
    }

    /**
     * Get the first scoring team/method indicator for 2nd quarter 0 no team scored, 1 home team goal, 2 home team
     * behind, 3 away team goal, 4 away team behind
     * 
     * @return firstScoringPlay2Quarter
     */
    public int getFirstScoringPlay2Quarter() {
        return firstScoringPlay2Quarter;
    }

    /**
     * Set the first scoring team/method indicator for 2nd quarter 0 no team scored, 1 home team goal, 2 home team
     * behind, 3 away team goal, 4 away team behind
     * 
     * @param firstScoringPlay2Quarter
     */
    public void setFirstScoringPlay2Quarter(int firstScoringPlay2Quarter) {
        this.firstScoringPlay2Quarter = firstScoringPlay2Quarter;
    }

    /**
     * Get the first scoring team/method indicator for 3rd quarter 0 no team scored, 1 home team goal, 2 home team
     * behind, 3 away team goal, 4 away team behind
     * 
     * @return firstScoringPlay3Quarter
     */
    public int getFirstScoringPlay3Quarter() {
        return firstScoringPlay3Quarter;
    }

    /**
     * Set the first scoring team/method indicator for 3rd quarter 0 no team scored, 1 home team goal, 2 home team
     * behind, 3 away team goal, 4 away team behind
     * 
     * @param firstScoringPlay3Quarter
     */
    public void setFirstScoringPlay3Quarter(int firstScoringPlay3Quarter) {
        this.firstScoringPlay3Quarter = firstScoringPlay3Quarter;
    }

    /**
     * Get the first scoring team/method indicator for 4th quarter 0 no team scored, 1 home team goal, 2 home team
     * behind, 3 away team goal, 4 away team behind
     * 
     * @return firstScoringPlay4Quarter
     */
    public int getFirstScoringPlay4Quarter() {
        return firstScoringPlay4Quarter;
    }

    /**
     * Set the first scoring team/method indicator for 4th quarter 0 no team scored, 1 home team goal, 2 home team
     * behind, 3 away team goal, 4 away team behind
     * 
     * @param firstScoringPlay4Quarter
     */
    public void setFirstScoringPlay4Quarter(int firstScoringPlay4Quarter) {
        this.firstScoringPlay4Quarter = firstScoringPlay4Quarter;
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

    /**
     * gets the current ball posn and who is in possession
     * 
     * @return
     */
    public BallPosition getBallPosition() {
        return ballPosition;
    }

    /**
     * get teamA normal time score
     * 
     * @return
     */
    @JsonIgnore
    public int getNormalTimePointsA() {
        return normalTimePointsA;
    }

    /**
     * set teamA normal time score
     * 
     * @param normalTimeGoalsA
     */
    @JsonIgnore
    public void setNormalTimePointssA(int normalTimeGoalsA) {
        this.normalTimePointsA = normalTimeGoalsA;
    }

    /**
     * get teamB normal time score
     * 
     * @return
     */
    @JsonIgnore
    public int getNormalTimePointsB() {
        return normalTimePointsB;
    }

    /**
     * set teamB normal time score
     * 
     * @param normalTimeGoalsB
     */
    @JsonIgnore
    public void setNormalTimePointsB(int normalTimeGoalsB) {
        this.normalTimePointsB = normalTimeGoalsB;
    }

    /**
     * get the overtime period no
     * 
     * @return
     */
    @JsonIgnore
    public int getOvertimeNo() {
        return overtimeNo;
    }

    /**
     * set the overtime period no
     * 
     * @param overtimeNo
     */
    @JsonIgnore
    public void setOvertimeNo(int overtimeNo) {
        this.overtimeNo = overtimeNo;
    }

    /**
     * get length of each extra time period
     * 
     * @return
     */
    public int getExtraPeriodSecs() {
        return extraPeriodSecs;
    }

    /**
     * get length of each normal time period
     * 
     * @return
     */
    public int getNormalPeriodSecs() {
        return normalPeriodSecs;
    }

    /**
     * set the time elapsed during the current period
     * 
     * @param elapsedTimeThisPeriodSecs
     */
    public void setElapsedTimeThisPeriodSecs(int elapsedTimeThisPeriodSecs) {
        this.elapsedTimeThisPeriodSecs = elapsedTimeThisPeriodSecs;
    }

    /**
     * set no of points scored by team A in the previous match period
     * 
     * @param previousPeriodGoalsA
     */
    public void setPreviousPeriodPointsA(int previousPeriodGoalsA) {
        this.previousPeriodPointsA = previousPeriodGoalsA;
    }

    /**
     * set no of points scored by team B in the previous match period
     * 
     * @param previousPeriodGoalsB
     */
    public void setPreviousPeriodPointsB(int previousPeriodGoalsB) {
        this.previousPeriodPointsB = previousPeriodGoalsB;
    }

    /**
     * set no of goals scored by team A in the previous match period
     * 
     * @param previousPeriodGoalsA
     */
    public void setPreviousPeriodGoalsA(int previousPeriodGoalsA) {
        this.previousPeriodGoalsA = previousPeriodGoalsA;
    }

    /**
     * set no of goals scored by team B in the previous match period
     * 
     * @param previousPeriodGoalsB
     */
    public void setPreviousPeriodGoalsB(int previousPeriodGoalsB) {
        this.previousPeriodGoalsB = previousPeriodGoalsB;
    }

    /**
     * set points scored by team A in the current match period
     * 
     * @param currentPeriodGoalsA
     */
    public void setCurrentPeriodPointsA(int currentPeriodGoalsA) {
        this.currentPeriodPointsA = currentPeriodGoalsA;
    }

    /**
     * set points scored by team B in the current match period
     * 
     * @param currentPeriodGoalsB
     */
    public void setCurrentPeriodPointsB(int currentPeriodGoalsB) {
        this.currentPeriodPointsB = currentPeriodGoalsB;
    }

    /**
     * get # goals scored by team A in the current match period
     * 
     * @param currentPeriodGoalsA
     */
    public void setCurrentPeriodGoalsA(int currentPeriodGoalsA) {
        this.currentPeriodGoalsA = currentPeriodGoalsA;
    }

    /**
     * get # goals scored by team B in the current match period
     * 
     * @param currentPeriodGoalsB
     */
    public void setCurrentPeriodGoalsB(int currentPeriodGoalsB) {
        this.currentPeriodGoalsB = currentPeriodGoalsB;
    }

    /**
     * get # goals scored by Team A in the current match period
     * 
     * @return
     */
    public int getCurrentPeriodGoalsA() {
        return currentPeriodGoalsA;
    }

    /**
     * get # goals scored by Team B in the current match period
     * 
     * @return
     */
    public int getCurrentPeriodGoalsB() {
        return currentPeriodGoalsB;
    }

    /**
     * get total points scored by team A in the match so far
     * 
     * @return
     */
    public int getPointsA() {
        return pointsA;
    }

    /**
     * set total points scored by team A in the match so far
     * 
     * @param goalsHome
     */
    public void setPointsA(int goalsHome) {
        this.pointsA = goalsHome;
    }

    /**
     * get total points scored by team B in the match so far
     * 
     * @return
     */
    public int getPointsB() {
        return pointsB;
    }

    /**
     * set total points scored by team B in the match so far
     * 
     * @param goalsAway
     */
    public void setPointsB(int goalsAway) {
        this.pointsB = goalsAway;
    }

    /**
     * get the elapsed time in the match
     * 
     * @return
     */
    public int getElapsedTimeSecs() {
        return elapsedTimeSecs;
    }

    /**
     * get the elapsed time in the current period
     * 
     * @return
     */
    public int getElapsedTimeThisPeriodSecs() {
        return elapsedTimeThisPeriodSecs;
    }

    public boolean isMatchClockRunning() {
        return matchClockRunning;
    }

    public void setMatchClockRunning(boolean isMatchClockRunning) {
        this.matchClockRunning = isMatchClockRunning;
    }

    /**
     * get id of team scoring the most recent goal
     * 
     * @return UNKNOWN if no goal scored, else A or B
     */
    public TeamId getTeamScoringLastGoal() {
        return teamScoringLastGoal;
    }

    /**
     * set id of team scoring the most recent goal
     * 
     * @param teamScoringLastGoal
     */
    public void setTeamScoringLastGoal(TeamId teamScoringLastGoal) {
        this.teamScoringLastGoal = teamScoringLastGoal;
    }

    /**
     * get the id of the team which scored the most recent behind
     * 
     * @return
     */
    public TeamId getTeamScoringLastBehind() {
        return teamScoringLastBehind;
    }

    /**
     * set the id of the team which scored the most recent behind
     * 
     * @param teamScoringLastBehind
     */
    public void setTeamScoringLastBehind(TeamId teamScoringLastBehind) {
        this.teamScoringLastBehind = teamScoringLastBehind;
    }

    /**
     * get the id of the team scoring the most recent point
     * 
     * @return
     */
    public TeamId getTeamScoringLastPoint() {
        return teamScoringLastPoint;
    }

    /**
     * set the id of the team scoring the most recent point
     * 
     * @param teamScoringLastPoint
     */
    public void setTeamScoringLastPoint(TeamId teamScoringLastPoint) {
        this.teamScoringLastPoint = teamScoringLastPoint;
    }

    /**
     * get # goals scored by team A
     * 
     * @return
     */
    public int getGoalsA() {
        return goalsA;
    }

    /**
     * set # goals scored by team A
     * 
     * @param goalsA
     */
    public void setGoalsA(int goalsA) {
        this.goalsA = goalsA;
    }

    /**
     * get # goals scored by team B
     * 
     * @return
     */
    public int getGoalsB() {
        return goalsB;
    }

    /**
     * set # goals scored by team B
     * 
     * @param goalsB
     */
    public void setGoalsB(int goalsB) {
        this.goalsB = goalsB;
    }

    /**
     * get # behinds scored by team A
     * 
     * @return
     */
    public int getBehindsA() {
        return behindsA;
    }

    /**
     * set # behinds scored by team A
     * 
     * @param behindsA
     */
    public void setBehindsA(int behindsA) {
        this.behindsA = behindsA;
    }

    /**
     * get # behinds scored by team B
     * 
     * @return
     */
    public int getBehindsB() {
        return behindsB;
    }

    /**
     * set # behinds scored by team B
     * 
     * @param behindsB
     */
    public void setBehindsB(int behindsB) {
        this.behindsB = behindsB;
    }

    /**
     * get the current period no
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
            case AT_THIRD_PERIOD_END:
            case IN_FOURTH_PERIOD:
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
        return n;
    }

    // FIX ME AND CODE FROM ABOVE
    /**
     * set the elapsed time in match and in current period
     * 
     * @param elapsedTimeSecs
     * @param overtimeNoIn which overtime period we are in. 0 if not in overtime
     */

    void setElapsedTimeSecs(int elapsedTimeSecs) {
        this.elapsedTimeSecs = elapsedTimeSecs;
    }

    // public void setElapsedTimeSecs(int elapsedTimeSecs, int overtimeNoIn) {
    // this.elapsedTimeSecs = elapsedTimeSecs;
    // this.elapsedTimeThisPeriodSecs = elapsedTimeSecs;
    //
    // matchPeriod = AflMatchPeriod.IN_FIRST_PERIOD;
    //
    // if (elapsedTimeSecs == normalPeriodSecs) {
    // this.elapsedTimeThisPeriodSecs = 0;
    // matchPeriod = AflMatchPeriod.AT_FIRST_PERIOD_END;
    // }
    // if (elapsedTimeSecs > normalPeriodSecs) {
    // this.elapsedTimeThisPeriodSecs = elapsedTimeSecs - normalPeriodSecs;
    // matchPeriod = AflMatchPeriod.IN_SECOND_PERIOD;
    // }
    // if (elapsedTimeSecs == 2 * normalPeriodSecs) {
    // this.elapsedTimeThisPeriodSecs = 0;
    // matchPeriod = AflMatchPeriod.AT_SECOND_PERIOD_END;
    // }
    // if (elapsedTimeSecs > 2 * normalPeriodSecs) {
    // this.elapsedTimeThisPeriodSecs = elapsedTimeSecs - 2 * normalPeriodSecs;
    // matchPeriod = AflMatchPeriod.IN_THIRD_PERIOD;
    // }
    //
    // if (elapsedTimeSecs == 3 * normalPeriodSecs) {
    // this.elapsedTimeThisPeriodSecs = 0;
    // matchPeriod = AflMatchPeriod.AT_THIRD_PERIOD_END;
    // }
    //
    // if (elapsedTimeSecs > 3 * normalPeriodSecs) {
    // this.elapsedTimeThisPeriodSecs = elapsedTimeSecs - 3 * normalPeriodSecs;
    // matchPeriod = AflMatchPeriod.IN_FOURTH_PERIOD;
    // }
    // if (elapsedTimeSecs == 4 * normalPeriodSecs) {
    // this.elapsedTimeThisPeriodSecs = 0;
    // matchPeriod = AflMatchPeriod.AT_FULL_TIME;
    // }
    // if (elapsedTimeSecs > 4 * normalPeriodSecs) {
    // this.elapsedTimeThisPeriodSecs = elapsedTimeSecs - overtimeNoIn *
    // extraPeriodSecs;
    // matchPeriod = AflMatchPeriod.IN_EXTRA_TIME;
    // }
    // }

    /**
     * get the current matchPeriod
     * 
     * @return
     */
    public AflMatchPeriod getMatchPeriod() {
        return matchPeriod;
    }

    /**
     * set the current match period
     * 
     * @param matchPeriod
     */
    public void setMatchPeriod(AflMatchPeriod matchPeriod) {
        this.matchPeriod = matchPeriod;
    }

    /**
     * get # points scored by team A in current period
     * 
     * @return
     */
    public int getCurrentPeriodPointsA() {

        return currentPeriodPointsA;
    }

    /**
     * get # points scored by team B in current period
     * 
     * @return
     */
    public int getCurrentPeriodPointsB() {

        return currentPeriodPointsB;
    }

    private List<GoalInfo> cloneGoalInfoList(List<GoalInfo> list) throws CloneNotSupportedException {
        List<GoalInfo> cloned = new ArrayList<GoalInfo>(list.size());
        for (GoalInfo item : list)
            cloned.add((GoalInfo) item.clone());
        return cloned;
    }

    // public class GoalInfo implements Serializable, Cloneable,
    // Comparable<GoalInfo> {
    // private static final long serialVersionUID = 1L;
    // int secs;
    // TeamId team;
    // int method; // 0 is a goal, 1 is a behind
    //
    // protected Object clone() throws CloneNotSupportedException {
    // return super.clone();
    // }
    //
    // public int getSecs() {
    // return secs;
    // }
    //
    // public void setSecs(int mins) {
    // this.secs = mins;
    // }
    //
    // public TeamId getTeam() {
    // return team;
    // }
    //
    // public void setTeam(TeamId team) {
    // this.team = team;
    // }
    //
    // public int getMethod() {
    // return method;
    // }
    //
    // public void setMethod(int method) {
    // this.method = method;
    // }
    //
    // @Override
    // public int compareTo(GoalInfo arg0) {
    // return this.secs - arg0.getSecs();
    // }
    //
    // @Override
    // public int hashCode() {
    // final int prime = 31;
    // int result = 1;
    // result = prime * result + method;
    // result = prime * result + secs;
    // result = prime * result + ((team == null) ? 0 : team.hashCode());
    // return result;
    // }
    //
    // @Override
    // public boolean equals(Object obj) {
    // if (this == obj)
    // return true;
    // if (obj == null)
    // return false;
    // if (getClass() != obj.getClass())
    // return false;
    // GoalInfo other = (GoalInfo) obj;
    // if (method != other.method)
    // return false;
    // if (secs != other.secs)
    // return false;
    // if (team != other.team)
    // return false;
    // return true;
    // }
    //
    // }

    @Override
    public MatchIncidentResult updateStateForIncident(MatchIncident matchIncident,
                    boolean autosyncMatchStateToFeedOnMismatch) {

        if (matchIncident.getIncidentSubType() == ElapsedTimeMatchIncidentType.SET_START_MATCH_CLOCK
                        || matchIncident.getIncidentSubType() == ElapsedTimeMatchIncidentType.SET_STOP_MATCH_CLOCK
                        || matchIncident.getIncidentSubType() == ElapsedTimeMatchIncidentType.SET_MATCH_CLOCK) {
            switch (matchPeriod) {
                case AT_FIRST_PERIOD_END:
                    throw new IllegalArgumentException("Match Clock updates shouldn't happen " + matchPeriod);
                case AT_SECOND_PERIOD_END:
                    throw new IllegalArgumentException("Match Clock updates shouldn't happen " + matchPeriod);
                case AT_THIRD_PERIOD_END:
                    throw new IllegalArgumentException("Match Clock updates shouldn't happen " + matchPeriod);
                case AT_FULL_TIME:
                    throw new IllegalArgumentException("Match Clock updates shouldn't happen " + matchPeriod);
                case AT_EXTRA_PERIOD_END:
                    throw new IllegalArgumentException("Match Clock updates shouldn't happen " + matchPeriod);
                case AT_HALF_TIME:
                    throw new IllegalArgumentException("Match Clock updates shouldn't happen " + matchPeriod);
                case PREMATCH:
                    throw new IllegalArgumentException("Match Clock updates shouldn't happen " + matchPeriod);
                default:
                    break;
            }
        }

        if (matchIncident.getClass() == AflMatchIncident.class) {
            AflMatchIncidentType aflMatchIncidentType = (AflMatchIncidentType) matchIncident.getIncidentSubType();

            if (aflMatchIncidentType == AflMatchIncidentType.ONE_POINTS_SCORED
                            || aflMatchIncidentType == AflMatchIncidentType.SIX_POINTS_SCORED
                            || aflMatchIncidentType == AflMatchIncidentType.POINT_SCORED) {

                switch (matchPeriod) {
                    case AT_FIRST_PERIOD_END:
                        throw new IllegalArgumentException("Score updates shouldn't happen here " + matchPeriod);
                    case AT_SECOND_PERIOD_END:
                        throw new IllegalArgumentException("Score updates shouldn't happen here " + matchPeriod);
                    case AT_THIRD_PERIOD_END:
                        throw new IllegalArgumentException("Score updates shouldn't happen here " + matchPeriod);
                    case AT_FULL_TIME:
                        throw new IllegalArgumentException("Score updates shouldn't happen here " + matchPeriod);
                    case AT_EXTRA_PERIOD_END:
                        throw new IllegalArgumentException("Score updates shouldn't happen here " + matchPeriod);
                    case AT_HALF_TIME:
                        throw new IllegalArgumentException("Score updates shouldn't happen here " + matchPeriod);
                    case PREMATCH:
                        throw new IllegalArgumentException("Score updates shouldn't happen here " + matchPeriod);
                    default:
                        break;
                }

            }
        }

        setElapsedTime((matchIncident.getElapsedTimeSecs()));
        elapsedTimeAtLastMatchIncidentSecs = elapsedTimeSecs; // ?? what's this
        setClockTimeOfLastElapsedTimeFromIncident();

        if (matchIncident.getClass() == AflMatchIncident.class) {
            TeamId teamId = ((AflMatchIncident) matchIncident).getTeamId();
            lastMatchIncidentType = (AflMatchIncident) matchIncident; // for
                                                                      // specifically

            switch (((AflMatchIncident) matchIncident).getIncidentSubType()) {

                case BALL_POSITION_SETTING: // ball possession and field position settings
                    FieldPositionType fieldPosition = ((AflMatchIncident) matchIncident).getFieldPosition();
                    switch (teamId) {
                        case A:
                            ballPosition.setBallHoldingNow(TeamId.A);
                            this.ballPosition.fieldPosition = fieldPosition;
                            break;
                        case B:
                            ballPosition.setBallHoldingNow(TeamId.B);
                            this.ballPosition.fieldPosition = fieldPosition;
                            break;
                        default:
                            ballPosition.setBallHoldingNow(TeamId.UNKNOWN);
                            this.ballPosition.fieldPosition = fieldPosition;
                            break;
                        // throw new IllegalArgumentException("Need a team");
                    }
                    break;

                case FIELD_POSITION_SETTING: // set field position and ball posession at the same time

                    fieldPosition = ((AflMatchIncident) matchIncident).getFieldPosition();
                    TeamId teamHolding = ((AflMatchIncident) matchIncident).getTeamId();
                    this.ballPosition.fieldPosition = fieldPosition;
                    this.ballPosition.ballHoldingNow = teamHolding;
                    break;

                case SIX_POINTS_SCORED:
                    if (matchPeriod.equals(AflMatchPeriod.IN_FIRST_PERIOD)) {

                    } else if (matchPeriod.equals(AflMatchPeriod.IN_SECOND_PERIOD)) {

                    } else if (matchPeriod.equals(AflMatchPeriod.IN_THIRD_PERIOD)) {

                    } else if (matchPeriod.equals(AflMatchPeriod.IN_FOURTH_PERIOD)) {

                    } else if (matchPeriod.equals(AflMatchPeriod.IN_EXTRA_TIME)
                                    || matchPeriod.equals(AflMatchPeriod.AT_EXTRA_PERIOD_END)) {

                    } else {
                        throw new IllegalArgumentException("Goals should not happening at match period " + matchPeriod);
                    }

                    GoalInfo goal = new GoalInfo();
                    elapsedTimeAtLastGoalSecs = elapsedTimeSecs;
                    matchPeriodInWhichLastGoalScored = matchPeriod;
                    ballPosition.resetBallPosition();
                    if (timeOfFirstGoalMatch == -1)
                        timeOfFirstGoalMatch = elapsedTimeAtLastGoalSecs;
                    if (timeOfFirstGoalQuarter == -1)
                        timeOfFirstGoalQuarter = elapsedTimeAtLastGoalSecs;
                    switch (teamId) {
                        case A:
                            goal.setMethod(0);
                            goal.setSecs(elapsedTimeSecs);
                            goal.setTeam(TeamId.A);
                            goal.setPeriodNoG(this.getPeriodNo());
                            goalInfoList.add(goal);

                            processFirstScoringIndicator(matchPeriodInWhichLastGoalScored, 1);
                            if (!matchPeriod.equals(AflMatchPeriod.IN_EXTRA_TIME))
                                normalTimePointsA += 6;
                            goalsA++;
                            pointsA += 6;
                            currentPeriodPointsA += 6;
                            currentPeriodGoalsA++;
                            teamScoringLastGoal = TeamId.A;
                            teamScoringLastPoint = TeamId.A;

                            if (matchPeriod.equals(AflMatchPeriod.IN_FIRST_PERIOD)) {
                                goalQ1A++;
                                pointsQ1A += 6;
                            } else if (matchPeriod.equals(AflMatchPeriod.IN_SECOND_PERIOD)) {
                                goalQ2A++;
                                pointsQ2A += 6;
                            } else if (matchPeriod.equals(AflMatchPeriod.IN_THIRD_PERIOD)) {
                                goalQ3A++;
                                pointsQ3A += 6;
                            } else if (matchPeriod.equals(AflMatchPeriod.IN_FOURTH_PERIOD)) {
                                goalQ4A++;
                                pointsQ4A += 6;
                            } else if (matchPeriod.equals(AflMatchPeriod.IN_EXTRA_TIME)
                                            || matchPeriod.equals(AflMatchPeriod.AT_EXTRA_PERIOD_END)) {
                                goalQEA++;
                                pointsQEA += 6;
                            } else {
                                throw new IllegalArgumentException(
                                                "Goals should not happening at match period " + matchPeriod);
                            }
                            if (firstTo3G.equals(TeamId.UNKNOWN))
                                updateFirstToXG(3, TeamId.A);
                            else if (firstTo4G.equals(TeamId.UNKNOWN))
                                updateFirstToXG(4, TeamId.A);
                            else if (firstTo5G.equals(TeamId.UNKNOWN))
                                updateFirstToXG(5, TeamId.A);
                            else if (firstTo6G.equals(TeamId.UNKNOWN))
                                updateFirstToXG(6, TeamId.A);
                            break;
                        case B:
                            goal.setMethod(0);
                            goal.setSecs(elapsedTimeSecs);
                            goal.setTeam(TeamId.B);
                            goal.setPeriodNoG(this.getPeriodNo());
                            goalInfoList.add(goal);

                            processFirstScoringIndicator(matchPeriodInWhichLastGoalScored, 3);
                            if (!matchPeriod.equals(AflMatchPeriod.IN_EXTRA_TIME))
                                normalTimePointsB += 6;
                            goalsB++;
                            pointsB += 6;
                            currentPeriodPointsB += 6;
                            currentPeriodGoalsB++;
                            teamScoringLastGoal = TeamId.B;
                            teamScoringLastPoint = TeamId.B;

                            if (matchPeriod.equals(AflMatchPeriod.IN_FIRST_PERIOD)) {
                                goalQ1B++;
                                pointsQ1B += 6;
                            } else if (matchPeriod.equals(AflMatchPeriod.IN_SECOND_PERIOD)) {
                                goalQ2B++;
                                pointsQ2B += 6;
                            } else if (matchPeriod.equals(AflMatchPeriod.IN_THIRD_PERIOD)) {
                                goalQ3B++;
                                pointsQ3B += 6;
                            } else if (matchPeriod.equals(AflMatchPeriod.IN_FOURTH_PERIOD)) {
                                goalQ4B++;
                                pointsQ4B += 6;
                            } else if (matchPeriod.equals(AflMatchPeriod.IN_EXTRA_TIME)
                                            || matchPeriod.equals(AflMatchPeriod.AT_EXTRA_PERIOD_END)) {
                                goalQEB++;
                                pointsQEB += 6;
                            } else {
                                throw new IllegalArgumentException(
                                                "Goals should not happening at match period " + matchPeriod);
                            }

                            if (firstTo3G.equals(TeamId.UNKNOWN))
                                updateFirstToXG(3, TeamId.B);
                            else if (firstTo4G.equals(TeamId.UNKNOWN))
                                updateFirstToXG(4, TeamId.B);
                            else if (firstTo5G.equals(TeamId.UNKNOWN))
                                updateFirstToXG(5, TeamId.B);
                            else if (firstTo6G.equals(TeamId.UNKNOWN))
                                updateFirstToXG(6, TeamId.B);
                            break;

                        case UNKNOWN:
                            throw new IllegalArgumentException("TeamId must be specified for goal scored");
                    }
                    break;

                case RESET_FIELD_BALL_INFO:
                    ballPosition.resetBallPosition();
                    break;

                case ONE_POINTS_SCORED:
                    GoalInfo behind = new GoalInfo();
                    elapsedTimeAtLastGoalSecs = elapsedTimeSecs;
                    matchPeriodInWhichLastGoalScored = matchPeriod;
                    ballPosition.resetBallPosition();
                    switch (teamId) {
                        case A:
                            behind.setMethod(1);
                            behind.setSecs(elapsedTimeSecs);
                            behind.setTeam(TeamId.A);
                            behind.setPeriodNoG(this.getPeriodNo());
                            goalInfoList.add(behind);

                            processFirstScoringIndicator(matchPeriodInWhichLastGoalScored, 2);
                            if (!matchPeriod.equals(AflMatchPeriod.IN_EXTRA_TIME))
                                normalTimePointsA += 1;
                            behindsA++;
                            pointsA += 1;
                            currentPeriodPointsA += 1;
                            teamScoringLastBehind = TeamId.A;
                            teamScoringLastPoint = TeamId.A;

                            if (matchPeriod.equals(AflMatchPeriod.IN_FIRST_PERIOD)) {
                                behindQ1A++;
                                pointsQ1A++;
                            } else if (matchPeriod.equals(AflMatchPeriod.IN_SECOND_PERIOD)) {
                                behindQ2A++;
                                pointsQ2A++;
                            } else if (matchPeriod.equals(AflMatchPeriod.IN_THIRD_PERIOD)) {
                                behindQ3A++;
                                pointsQ3A++;
                            } else if (matchPeriod.equals(AflMatchPeriod.IN_FOURTH_PERIOD)) {
                                behindQ4A++;
                                pointsQ4A++;
                            } else if (matchPeriod.equals(AflMatchPeriod.IN_EXTRA_TIME)
                                            || matchPeriod.equals(AflMatchPeriod.AT_EXTRA_PERIOD_END)) {
                                behindQEA++;
                                pointsQEA++;
                            } else {
                                throw new IllegalArgumentException(
                                                "Goals should not happening at match period " + matchPeriod);
                            }

                            if (firstTo3B.equals(TeamId.UNKNOWN))
                                updateFirstToXB(3, TeamId.A);
                            else if (firstTo4B.equals(TeamId.UNKNOWN))
                                updateFirstToXB(4, TeamId.A);
                            else if (firstTo5B.equals(TeamId.UNKNOWN))
                                updateFirstToXB(5, TeamId.A);
                            else if (firstTo6B.equals(TeamId.UNKNOWN))
                                updateFirstToXB(6, TeamId.A);

                            break;
                        case B:
                            behind.setMethod(1);
                            behind.setSecs(elapsedTimeSecs);
                            behind.setTeam(TeamId.B);
                            behind.setPeriodNoG(this.getPeriodNo());
                            goalInfoList.add(behind);

                            processFirstScoringIndicator(matchPeriodInWhichLastGoalScored, 4);
                            if (!matchPeriod.equals(AflMatchPeriod.IN_EXTRA_TIME))
                                normalTimePointsB += 1;
                            behindsB++;
                            pointsB += 1;
                            currentPeriodPointsB += 1;
                            teamScoringLastBehind = TeamId.B;
                            teamScoringLastPoint = TeamId.B;
                            if (matchPeriod.equals(AflMatchPeriod.IN_FIRST_PERIOD)) {
                                behindQ1B++;
                                pointsQ1B++;
                            } else if (matchPeriod.equals(AflMatchPeriod.IN_SECOND_PERIOD)) {
                                behindQ2B++;
                                pointsQ2B++;
                            } else if (matchPeriod.equals(AflMatchPeriod.IN_THIRD_PERIOD)) {
                                behindQ3B++;
                                pointsQ3B++;
                            } else if (matchPeriod.equals(AflMatchPeriod.IN_FOURTH_PERIOD)) {
                                behindQ4B++;
                                pointsQ4B++;
                            } else if (matchPeriod.equals(AflMatchPeriod.IN_EXTRA_TIME)
                                            || matchPeriod.equals(AflMatchPeriod.AT_EXTRA_PERIOD_END)) {
                                behindQEB++;
                                pointsQEB++;
                            } else {
                                throw new IllegalArgumentException(
                                                "Goals should not happening at match period " + matchPeriod);
                            }

                            if (firstTo3B.equals(TeamId.UNKNOWN))
                                updateFirstToXB(3, TeamId.B);
                            else if (firstTo4B.equals(TeamId.UNKNOWN))
                                updateFirstToXB(4, TeamId.B);
                            else if (firstTo5B.equals(TeamId.UNKNOWN))
                                updateFirstToXB(5, TeamId.B);
                            else if (firstTo6B.equals(TeamId.UNKNOWN))
                                updateFirstToXB(6, TeamId.B);
                            break;

                        case UNKNOWN:
                            throw new IllegalArgumentException("TeamId must be specified for goal scored");
                    }

                    break;

                default:
                    throw new IllegalArgumentException("Unknown match incidences type");
            }
        } else {// must be ElapsedTimeMatchIncident
            lastMatchIncidentType = (ElapsedTimeMatchIncident) matchIncident;
            this.setClockStatus(((ElapsedTimeMatchIncident) matchIncident).getIncidentSubType());

            switch (((ElapsedTimeMatchIncident) matchIncident).getIncidentSubType()) {

                case SET_MATCH_CLOCK:

                    /*
                     * Do Nothing
                     */
                    break;

                case SET_PERIOD_START:
                    setMatchClockRunning(true);
                    switch (matchPeriod) {
                        case PREMATCH:
                            elapsedTimeThisPeriodSecs = 0;
                            matchPeriod = AflMatchPeriod.IN_FIRST_PERIOD;
                            break;
                        /*
                         * 
                         * four periods type
                         * 
                         */

                        case IN_FIRST_PERIOD:
                            break;

                        case AT_FIRST_PERIOD_END:
                            timeOfFirstGoalQuarter = -1;
                            elapsedTimeThisPeriodSecs = 0;
                            matchPeriod = AflMatchPeriod.IN_SECOND_PERIOD;
                            break;

                        case IN_SECOND_PERIOD:
                            break;

                        case AT_SECOND_PERIOD_END:
                            timeOfFirstGoalQuarter = -1;
                            elapsedTimeThisPeriodSecs = 0;
                            matchPeriod = AflMatchPeriod.IN_THIRD_PERIOD;
                            break;

                        case IN_THIRD_PERIOD:
                            break;

                        case AT_THIRD_PERIOD_END:
                            timeOfFirstGoalQuarter = -1;
                            elapsedTimeThisPeriodSecs = 0;
                            matchPeriod = AflMatchPeriod.IN_FOURTH_PERIOD;
                            break;

                        case IN_FOURTH_PERIOD:
                            break;

                        case AT_FULL_TIME:
                            overtimeNo++;
                            timeOfFirstGoalQuarter = -1;
                            elapsedTimeThisPeriodSecs = 0;
                            matchPeriod = AflMatchPeriod.IN_EXTRA_TIME;
                            break;

                        case IN_EXTRA_TIME:
                            break;

                        case AT_EXTRA_PERIOD_END:
                            overtimeNo++;
                            timeOfFirstGoalQuarter = -1;
                            elapsedTimeThisPeriodSecs = 0;
                            if (pointsA == pointsB) {
                                matchPeriod = AflMatchPeriod.IN_EXTRA_TIME;
                                break;
                            } else {
                                setMatchClockRunning(false);
                                matchPeriod = AflMatchPeriod.MATCH_COMPLETED;
                            }
                            break;

                        case MATCH_COMPLETED:
                            setMatchClockRunning(false);
                            break;
                        default:
                            break;

                    }
                    break;

                case SET_PERIOD_END:
                    setMatchClockRunning(false);
                    switch (matchPeriod) {
                        case PREMATCH:
                            break;
                        case IN_FIRST_PERIOD:
                            matchPeriod = AflMatchPeriod.AT_FIRST_PERIOD_END;
                            previousPeriodPointsA = currentPeriodPointsA;
                            previousPeriodPointsB = currentPeriodPointsB;
                            currentPeriodPointsA = 0;
                            currentPeriodPointsB = 0;

                            previousPeriodGoalsA = currentPeriodGoalsA;
                            previousPeriodGoalsB = currentPeriodGoalsB;
                            currentPeriodGoalsA = 0;
                            currentPeriodGoalsB = 0;
                            setElapsedTime(normalPeriodSecs);
                            elapsedTimeAtLastMatchIncidentSecs = normalPeriodSecs;
                            setClockTimeOfLastElapsedTimeFromIncident();
                            timeOfFirstGoalQuarter = -1;
                            elapsedTimeThisPeriodSecs = 0;
                            break;
                        case AT_FIRST_PERIOD_END:
                            timeOfFirstGoalQuarter = -1;
                            elapsedTimeThisPeriodSecs = 0;
                            break;
                        case IN_SECOND_PERIOD:
                            matchPeriod = AflMatchPeriod.AT_SECOND_PERIOD_END;
                            previousPeriodPointsA = currentPeriodPointsA;
                            previousPeriodPointsB = currentPeriodPointsB;
                            currentPeriodPointsA = 0;
                            currentPeriodPointsB = 0;
                            previousPeriodGoalsA = currentPeriodGoalsA;
                            previousPeriodGoalsB = currentPeriodGoalsB;
                            currentPeriodGoalsA = 0;
                            currentPeriodGoalsB = 0;
                            setElapsedTime(normalPeriodSecs * 2);
                            elapsedTimeAtLastMatchIncidentSecs = normalPeriodSecs * 2;
                            setClockTimeOfLastElapsedTimeFromIncident();
                            timeOfFirstGoalQuarter = -1;
                            elapsedTimeThisPeriodSecs = 0;
                            break;
                        case AT_SECOND_PERIOD_END:
                            timeOfFirstGoalQuarter = -1;
                            elapsedTimeThisPeriodSecs = 0;
                            break;
                        case IN_THIRD_PERIOD:
                            matchPeriod = AflMatchPeriod.AT_THIRD_PERIOD_END;
                            previousPeriodPointsA = currentPeriodPointsA;
                            previousPeriodPointsB = currentPeriodPointsB;
                            currentPeriodPointsA = 0;
                            currentPeriodPointsB = 0;
                            previousPeriodGoalsA = currentPeriodGoalsA;
                            previousPeriodGoalsB = currentPeriodGoalsB;
                            currentPeriodGoalsA = 0;
                            currentPeriodGoalsB = 0;
                            setElapsedTime(normalPeriodSecs * 3);
                            elapsedTimeAtLastMatchIncidentSecs = normalPeriodSecs * 3;
                            setClockTimeOfLastElapsedTimeFromIncident();
                            timeOfFirstGoalQuarter = -1;
                            elapsedTimeThisPeriodSecs = 0;
                            break;
                        case AT_THIRD_PERIOD_END:
                            timeOfFirstGoalQuarter = -1;
                            elapsedTimeThisPeriodSecs = 0;
                            break;
                        case IN_FOURTH_PERIOD:
                            if ((pointsA != pointsB) || this.extraPeriodSecs == 0)
                                matchPeriod = AflMatchPeriod.MATCH_COMPLETED;
                            else
                                matchPeriod = AflMatchPeriod.AT_FULL_TIME;
                            previousPeriodPointsA = currentPeriodPointsA;
                            previousPeriodPointsB = currentPeriodPointsB;
                            currentPeriodPointsA = 0;
                            currentPeriodPointsB = 0;
                            previousPeriodGoalsA = currentPeriodGoalsA;
                            previousPeriodGoalsB = currentPeriodGoalsB;
                            currentPeriodGoalsA = 0;
                            currentPeriodGoalsB = 0;
                            setElapsedTime(normalPeriodSecs * 4);
                            elapsedTimeAtLastMatchIncidentSecs = normalPeriodSecs;
                            setClockTimeOfLastElapsedTimeFromIncident();
                            timeOfFirstGoalQuarter = -1;
                            elapsedTimeThisPeriodSecs = 0;
                            break;
                        case AT_FULL_TIME:
                            timeOfFirstGoalQuarter = -1;
                            elapsedTimeThisPeriodSecs = 0;
                            break;

                        case IN_EXTRA_TIME:
                            if (pointsA == pointsB) {
                                matchPeriod = AflMatchPeriod.AT_EXTRA_PERIOD_END;
                            } else {
                                matchPeriod = AflMatchPeriod.MATCH_COMPLETED;
                            }
                            previousPeriodPointsA = currentPeriodPointsA;
                            previousPeriodPointsB = currentPeriodPointsB;
                            currentPeriodPointsA = 0;
                            currentPeriodPointsB = 0;
                            previousPeriodGoalsA = currentPeriodGoalsA;
                            previousPeriodGoalsB = currentPeriodGoalsB;
                            currentPeriodGoalsA = 0;
                            currentPeriodGoalsB = 0;
                            setElapsedTime(normalPeriodSecs * 4 + (overtimeNo) * extraPeriodSecs);

                            if (overtimeNo == 0) {
                                elapsedTimeAtLastMatchIncidentSecs = normalPeriodSecs;
                            } else {
                                elapsedTimeAtLastMatchIncidentSecs = extraPeriodSecs;
                            }

                            setClockTimeOfLastElapsedTimeFromIncident();
                            break;

                        case AT_EXTRA_PERIOD_END:
                            timeOfFirstGoalQuarter = -1;
                            elapsedTimeThisPeriodSecs = 0;
                            break;

                        case MATCH_COMPLETED:
                            break;
                        default:
                            throw new IllegalArgumentException("SET_PERIOD_END Unknown");
                    }

                case SET_STOP_MATCH_CLOCK: // stop the independent match clock timer
                                           // - for those sports where this is
                                           // possible
                    setMatchClockRunning(false);
                    this.setClockStatus(ElapsedTimeMatchIncidentType.SET_STOP_MATCH_CLOCK);
                    break;

                case SET_START_MATCH_CLOCK: // start the match clock timer
                    if (this.matchPeriod.equals(AflMatchPeriod.AT_FIRST_PERIOD_END)
                                    || this.matchPeriod.equals(AflMatchPeriod.AT_HALF_TIME)
                                    || this.matchPeriod.equals(AflMatchPeriod.AT_THIRD_PERIOD_END)
                                    || this.matchPeriod.equals(AflMatchPeriod.AT_FULL_TIME)) {
                        break;
                    }
                    setMatchClockRunning(true);
                    this.setClockStatus(ElapsedTimeMatchIncidentType.SET_START_MATCH_CLOCK);

                    break;

            }
        }
        return matchPeriod;
    }

    private void updateFirstToXB(int i, TeamId teamId) {
        // already checked from outside if need to update
        if (behindsA == i || behindsB == i)
            switch (i) {
                case 3:
                    this.firstTo3B = teamId;
                    break;
                case 4:
                    this.firstTo4B = teamId;
                    break;
                case 5:
                    this.firstTo5B = teamId;
                    break;
                case 6:
                    this.firstTo6B = teamId;
                    break;
                default:
                    throw new IllegalArgumentException("Only first to 3,4,5,6 Goal/Behind param exists");

            }
    }

    private void updateFirstToXG(int i, TeamId teamId) {
        // already checked from outside if need to update
        if (goalsA == i || goalsB == i)
            switch (i) {
                case 3:
                    this.firstTo3G = teamId;
                    break;
                case 4:
                    this.firstTo4G = teamId;
                    break;
                case 5:
                    this.firstTo5G = teamId;
                    break;
                case 6:
                    this.firstTo6G = teamId;
                    break;
                default:
                    throw new IllegalArgumentException("Only first to 3,4,5,6 Goal/Behind param exists");

            }
    }

    private void processFirstScoringIndicator(AflMatchPeriod matchPeriodInWhichLastGoalScored2, int i) {
        if (matchPeriodInWhichLastGoalScored2.equals(AflMatchPeriod.IN_SECOND_PERIOD)
                        && firstScoringPlay2Quarter == 0) {
            firstScoringPlay2Quarter = i;
        } else if (matchPeriodInWhichLastGoalScored2.equals(AflMatchPeriod.IN_THIRD_PERIOD)
                        && firstScoringPlay3Quarter == 0) {
            firstScoringPlay3Quarter = i;
        } else if (matchPeriodInWhichLastGoalScored2.equals(AflMatchPeriod.IN_FOURTH_PERIOD)
                        && firstScoringPlay4Quarter == 0) {
            firstScoringPlay4Quarter = i;
        }
    }

    private void setElapsedTime(int elapsedTimeSecs) {
        this.elapsedTimeSecs = elapsedTimeSecs;
        switch (matchPeriod) {
            case PREMATCH:
            case AT_FIRST_PERIOD_END:
            case AT_SECOND_PERIOD_END:
            case AT_THIRD_PERIOD_END:

            case AT_FULL_TIME:
            case AT_EXTRA_PERIOD_END:
            case MATCH_COMPLETED:
            case AT_HALF_TIME:
                /*
                 * do nothing
                 */
                break;
            case IN_FIRST_HALF:
            case IN_FIRST_PERIOD:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs;
                break;
            case IN_SECOND_HALF:
            case IN_SECOND_PERIOD:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs - normalPeriodSecs;
                break;
            case IN_THIRD_PERIOD:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs - 2 * normalPeriodSecs;
                break;
            case IN_FOURTH_PERIOD:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs - 3 * normalPeriodSecs;
                break;
            case IN_EXTRA_TIME:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs - 4 * normalPeriodSecs - extraPeriodSecs * (overtimeNo - 1);

                break;
            default:
                throw new IllegalArgumentException("setElapsedTime");
        }
    }

    @Override
    public AlgoMatchState copy() {
        AflMatchState cc = new AflMatchState(matchFormat);
        cc.setEqualTo(this);
        return cc;

    }

    @Override
    public void setEqualTo(AlgoMatchState matchState) {
        super.setEqualTo(matchState);
        try {
            this.ballPosition = (BallPosition) (((AflMatchState) matchState).ballPosition).clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        this.setMannulResulting(((AflMatchState) matchState).isMannulResulting());
        this.setMatchClockRunning(((AflMatchState) matchState).isMatchClockRunning());
        this.setPointsA(((AflMatchState) matchState).getPointsA());
        this.setPointsB(((AflMatchState) matchState).getPointsB());
        this.setElapsedTimeSecs(((AflMatchState) matchState).getElapsedTimeSecs());
        // this.setElapsedTimeSecs(((AflMatchState) matchState).getElapsedTimeSecs(),
        // ((AflMatchState) matchState).getOvertimeNo());
        this.setMatchPeriod(((AflMatchState) matchState).getMatchPeriod());
        this.setOvertimeNo(((AflMatchState) matchState).getOvertimeNo());

        this.setCurrentPeriodPointsA(((AflMatchState) matchState).getCurrentPeriodPointsA());
        this.setCurrentPeriodPointsB(((AflMatchState) matchState).getCurrentPeriodPointsB());

        this.setCurrentPeriodGoalsA(((AflMatchState) matchState).getCurrentPeriodGoalsA());
        this.setCurrentPeriodGoalsB(((AflMatchState) matchState).getCurrentPeriodGoalsB());

        this.setOvertimeNo(((AflMatchState) matchState).getOvertimeNo());
        this.setNormalTimePointssA(((AflMatchState) matchState).getNormalTimePointsA());
        this.setNormalTimePointsB(((AflMatchState) matchState).getNormalTimePointsB());
        this.setBehindsA(((AflMatchState) matchState).getBehindsA());
        this.setBehindsB(((AflMatchState) matchState).getBehindsB());
        this.setGoalsA(((AflMatchState) matchState).getGoalsA());
        this.setGoalsB(((AflMatchState) matchState).getGoalsB());
        this.setLastMatchIncidentType(((AflMatchState) matchState).getLastMatchIncidentType());
        this.setElapsedTimeAtLastMatchIncidentSecs(
                        ((AflMatchState) matchState).getElapsedTimeAtLastMatchIncidentSecs());
        this.setFirstScoringPlay2Quarter(((AflMatchState) matchState).getFirstScoringPlay2Quarter());
        this.setFirstScoringPlay3Quarter(((AflMatchState) matchState).getFirstScoringPlay3Quarter());
        this.setFirstScoringPlay4Quarter(((AflMatchState) matchState).getFirstScoringPlay4Quarter());
        this.setTeamScoringLastBehind(((AflMatchState) matchState).getTeamScoringLastBehind());
        this.setTeamScoringLastGoal(((AflMatchState) matchState).getTeamScoringLastGoal());
        this.setTeamScoringLastPoint(((AflMatchState) matchState).getTeamScoringLastPoint());

        this.setBehindQ1A(((AflMatchState) matchState).getBehindQ1A());
        this.setBehindQ1B(((AflMatchState) matchState).getBehindQ1B());
        this.setBehindQ2A(((AflMatchState) matchState).getBehindQ2A());
        this.setBehindQ2B(((AflMatchState) matchState).getBehindQ2B());
        this.setBehindQ3A(((AflMatchState) matchState).getBehindQ3A());
        this.setBehindQ3B(((AflMatchState) matchState).getBehindQ3B());
        this.setBehindQ4A(((AflMatchState) matchState).getBehindQ4A());
        this.setBehindQ4B(((AflMatchState) matchState).getBehindQ4B());
        this.setBehindQEA(((AflMatchState) matchState).getBehindQEA());
        this.setBehindQEB(((AflMatchState) matchState).getBehindQEB());

        this.setGoalQ1A(((AflMatchState) matchState).getGoalQ1A());
        this.setGoalQ1B(((AflMatchState) matchState).getGoalQ1B());
        this.setGoalQ2A(((AflMatchState) matchState).getGoalQ2A());
        this.setGoalQ2B(((AflMatchState) matchState).getGoalQ2B());
        this.setGoalQ3A(((AflMatchState) matchState).getGoalQ3A());
        this.setGoalQ3B(((AflMatchState) matchState).getGoalQ3B());
        this.setGoalQ4A(((AflMatchState) matchState).getGoalQ4A());
        this.setGoalQ4B(((AflMatchState) matchState).getGoalQ4B());
        this.setGoalQEA(((AflMatchState) matchState).getGoalQEA());
        this.setGoalQEB(((AflMatchState) matchState).getGoalQEB());

        this.setPointsQ1A(((AflMatchState) matchState).getPointsQ1A());
        this.setPointsQ2A(((AflMatchState) matchState).getPointsQ2A());
        this.setPointsQ3A(((AflMatchState) matchState).getPointsQ3A());
        this.setPointsQ4A(((AflMatchState) matchState).getPointsQ4A());
        this.setPointsQEA(((AflMatchState) matchState).getPointsQEA());
        this.setPointsQ1B(((AflMatchState) matchState).getPointsQ1B());
        this.setPointsQ2B(((AflMatchState) matchState).getPointsQ2B());
        this.setPointsQ3B(((AflMatchState) matchState).getPointsQ3B());
        this.setPointsQ4B(((AflMatchState) matchState).getPointsQ4B());
        this.setPointsQEB(((AflMatchState) matchState).getPointsQEB());

        // TeamId tempId = ((AflMatchState) matchState).getFirstTo3B();
        this.setFirstTo3B(((AflMatchState) matchState).getFirstTo3B());
        this.setFirstTo3G(((AflMatchState) matchState).getFirstTo3G());
        // TeamId tempId2 = ((AflMatchState) matchState).getFirstTo3B();
        this.setFirstTo4B(((AflMatchState) matchState).getFirstTo4B());
        this.setFirstTo4G(((AflMatchState) matchState).getFirstTo4G());
        this.setFirstTo5B(((AflMatchState) matchState).getFirstTo5B());
        this.setFirstTo5G(((AflMatchState) matchState).getFirstTo5G());
        this.setFirstTo6B(((AflMatchState) matchState).getFirstTo6B());
        this.setFirstTo6G(((AflMatchState) matchState).getFirstTo6G());
        this.setTimeOfFirstGoalMatch(((AflMatchState) matchState).getTimeOfFirstGoalMatch());
        this.setTimeOfFirstGoalQuarter(((AflMatchState) matchState).getTimeOfFirstGoalQuarter());
        for (int i = 0; i < 4; i++) {
            if (i < ((AflMatchState) matchState).getPointsScoredMap().size()) {
                PairOfIntegers p = ((AflMatchState) matchState).getPointScoreInQuarterN(i);
                this.pointScoreInQuarterN[i].A = p.A;
                this.pointScoreInQuarterN[i].B = p.B;
                PairOfIntegers g = ((AflMatchState) matchState).getGoalScoreInQuarterN(i);
                this.goalScoreInQuarterN[i].A = g.A;
                this.goalScoreInQuarterN[i].B = g.B;
                PairOfIntegers b = ((AflMatchState) matchState).getBehindScoreInQuarterN(i);
                this.behindScoreInQuarterN[i].A = b.A;
                this.behindScoreInQuarterN[i].B = b.B;
            } else {
                PairOfIntegers s = PairOfIntegers.generateFromString("0-0");
                this.pointScoreInQuarterN[i].A = s.A;
                this.pointScoreInQuarterN[i].B = s.B;
                this.goalScoreInQuarterN[i].A = s.A;
                this.goalScoreInQuarterN[i].B = s.B;
                this.behindScoreInQuarterN[i].A = s.A;
                this.behindScoreInQuarterN[i].B = s.B;
            }
        }
        try {
            this.setGoalInfoList(cloneGoalInfoList(((AflMatchState) matchState).getGoalInfoList()));
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    @JsonIgnore
    @Override
    public MatchIncidentPrompt getNextPrompt() {
        MatchIncidentPrompt matchIncidentPrompt = null;
        if (matchPeriod == AflMatchPeriod.MATCH_COMPLETED)
            matchIncidentPrompt = new MatchIncidentPrompt("Match finished");
        else if (matchPeriod == AflMatchPeriod.PREMATCH || matchPeriod == AflMatchPeriod.AT_FIRST_PERIOD_END
                        || matchPeriod == AflMatchPeriod.AT_SECOND_PERIOD_END
                        || matchPeriod == AflMatchPeriod.AT_THIRD_PERIOD_END
                        || matchPeriod == AflMatchPeriod.AT_FULL_TIME
                        || matchPeriod == AflMatchPeriod.AT_EXTRA_PERIOD_END
                        || matchPeriod == AflMatchPeriod.AT_HALF_TIME)
            matchIncidentPrompt = new MatchIncidentPrompt("S - start match", "S");

        else {
            matchIncidentPrompt = new MatchIncidentPrompt(
                            "Next event (Nnn-Nothing happened for nn periods, H1/6-Home behind/goal, A1/6-Away behind/goal, F(H/A)(1/2) - Ball position (35m or 50m), "
                                            + "PA/H-Home/Away Ball holding by H/A," + ")",
                            "N");
        }
        return matchIncidentPrompt;
    }

    @Override
    public MatchIncident getMatchIncident(String response) {

        AflMatchIncidentType aflMatchIncidentType = null;
        ElapsedTimeMatchIncidentType elapsedTimeMatchIncidentType = null;
        TeamId teamId = null;
        FieldPositionType fieldPosition = null;
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

            case 'K':
                throw new IllegalArgumentException("UNKNOWN FREE THROW TEAM");

            case 'M':
                break;

            case 'P':
                try {
                    String teamCount = (response.substring(1, 2));
                    String yard1 = (response.substring(2, 3));
                    String yard2 = (response.substring(3));
                    fieldPosition = FieldPositionType.MIDFIELD;
                    teamId = TeamId.UNKNOWN;
                    if (teamCount.equalsIgnoreCase("H")) { // home team
                        aflMatchIncidentType = AflMatchIncidentType.BALL_POSITION_SETTING;
                        teamId = TeamId.A;
                        if (yard1.equalsIgnoreCase("M")) {
                            fieldPosition = FieldPositionType.MIDFIELD;
                            break;
                        }
                        if (yard1.equalsIgnoreCase("A")) {
                            if (yard2.equals("1")) {
                                fieldPosition = FieldPositionType.B35;
                            } else if (yard2.equals("2")) {
                                fieldPosition = FieldPositionType.B50;
                            }
                            break;
                        }
                        if (yard1.equalsIgnoreCase("H")) {
                            if (yard2.equals("1")) {
                                fieldPosition = FieldPositionType.A35;
                            } else if (yard2.equals("2")) {
                                fieldPosition = FieldPositionType.A50;
                            }
                            break;
                        }
                        break;
                    } else if (teamCount.equalsIgnoreCase("A")) {
                        aflMatchIncidentType = AflMatchIncidentType.BALL_POSITION_SETTING;
                        teamId = TeamId.B;
                        if (yard1.equalsIgnoreCase("M")) {
                            fieldPosition = FieldPositionType.MIDFIELD;
                            break;
                        }
                        if (yard1.equalsIgnoreCase("A")) {
                            if (yard2.equals("1")) {
                                fieldPosition = FieldPositionType.B35;
                            } else if (yard2.equals("2")) {
                                fieldPosition = FieldPositionType.B50;
                            }
                            break;
                        }
                        if (yard1.equalsIgnoreCase("H")) {
                            if (yard2.equals("1")) {
                                fieldPosition = FieldPositionType.A35;
                            } else if (yard2.equals("2")) {
                                fieldPosition = FieldPositionType.A50;
                            }
                            break;
                        }
                        break;
                    } else { // unknown team posession
                        aflMatchIncidentType = AflMatchIncidentType.BALL_POSITION_SETTING;
                        teamId = TeamId.UNKNOWN;
                        if (yard1.equalsIgnoreCase("M")) {
                            fieldPosition = FieldPositionType.MIDFIELD;
                            break;
                        }
                        if (yard1.equalsIgnoreCase("A")) {
                            if (yard2.equals("1")) {
                                fieldPosition = FieldPositionType.B35;
                            } else if (yard2.equals("2")) {
                                fieldPosition = FieldPositionType.B50;
                            }
                            break;
                        }
                        if (yard1.equalsIgnoreCase("H")) {
                            if (yard2.equals("1")) {
                                fieldPosition = FieldPositionType.A35;
                            } else if (yard2.equals("2")) {
                                fieldPosition = FieldPositionType.A50;
                            }
                            break;
                        }
                        break;
                    }

                } catch (Exception e) {
                    // int teamCount = 3;
                }
                break;

            case 'F': // all controled by P
                try {
                    String yard1 = (response.substring(1, 2));
                    String yard2 = (response.substring(2));
                    if (yard1.equalsIgnoreCase("M")) {
                        aflMatchIncidentType = AflMatchIncidentType.FIELD_POSITION_SETTING;
                        fieldPosition = FieldPositionType.MIDFIELD;
                        break;
                    }
                    if (yard1.equalsIgnoreCase("A")) {
                        aflMatchIncidentType = AflMatchIncidentType.FIELD_POSITION_SETTING;
                        if (yard2.equals("1")) {
                            fieldPosition = FieldPositionType.B35;
                        } else if (yard2.equals("2")) {
                            fieldPosition = FieldPositionType.B50;
                        }
                        break;
                    }
                    if (yard1.equalsIgnoreCase("H")) {
                        aflMatchIncidentType = AflMatchIncidentType.FIELD_POSITION_SETTING;
                        if (yard2.equals("1")) {
                            fieldPosition = FieldPositionType.A35;
                        } else if (yard2.equals("2")) {
                            fieldPosition = FieldPositionType.A50;
                        }
                        break;
                    }

                } catch (Exception e) {
                    // int teamCount = 3;
                }
                break;

            case 'R':
                aflMatchIncidentType = AflMatchIncidentType.RESET_FIELD_BALL_INFO;
                break;

            case 'H':
                int points = Integer.parseInt(response.substring(1, 2));
                if (points == 1) {
                    aflMatchIncidentType = AflMatchIncidentType.ONE_POINTS_SCORED;
                } else if (points == 6) {
                    aflMatchIncidentType = AflMatchIncidentType.SIX_POINTS_SCORED;
                } else {
                    throw new IllegalArgumentException("UNKNOWN POINTS SCORED");
                }
                teamId = TeamId.A;
                break;
            case 'A':
                points = Integer.parseInt(response.substring(1, 2));
                if (points == 1) {
                    aflMatchIncidentType = AflMatchIncidentType.ONE_POINTS_SCORED;
                } else if (points == 6) {
                    aflMatchIncidentType = AflMatchIncidentType.SIX_POINTS_SCORED;
                } else {
                    throw new IllegalArgumentException("UNKNOWN POINTS SCORED");
                }
                teamId = TeamId.B;
                break;

            case 'S':
                switch (matchPeriod) {
                    case PREMATCH:
                    case AT_FIRST_PERIOD_END:
                    case AT_SECOND_PERIOD_END:
                    case AT_THIRD_PERIOD_END:
                    case AT_FULL_TIME:
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
                    case IN_FIRST_PERIOD:
                    case IN_SECOND_PERIOD:
                    case IN_THIRD_PERIOD:
                    case IN_FOURTH_PERIOD:
                    case IN_EXTRA_TIME:
                        elapsedTimeMatchIncidentType = ElapsedTimeMatchIncidentType.SET_PERIOD_END;
                        break;
                    default:
                        return null; // invalid input so return null
                }
        }
        MatchIncident incident = null;
        if (aflMatchIncidentType != null) {
            if (aflMatchIncidentType != AflMatchIncidentType.FIELD_POSITION_SETTING
                            && aflMatchIncidentType != AflMatchIncidentType.BALL_POSITION_SETTING) {
                incident = new AflMatchIncident(aflMatchIncidentType, incidentElapsedTimeSecs, teamId);
            } else if (aflMatchIncidentType == AflMatchIncidentType.BALL_POSITION_SETTING) {
                incident = new AflMatchIncident(aflMatchIncidentType, incidentElapsedTimeSecs, fieldPosition, teamId);
            }
        } else {
            incident = new ElapsedTimeMatchIncident(elapsedTimeMatchIncidentType, incidentElapsedTimeSecs);
        }
        return incident;
    }

    private static final String pointsKey = "Points";
    private static final String goalsKey = "Goals";

    private static final String elapsedTimeKey = "Elapsed time";
    // private static final String injuryTimeKey = "Injury time to be played";
    private static final String matchPeriodKey = "Match period";
    @JsonIgnore
    private static final String periodSequenceKey = "Period sequence id";
    @JsonIgnore
    private static final String goalSequenceKey = "Goal sequence id";
    @JsonIgnore
    private static final String overTimeSequenceKey = "Over time sequence id";
    private static final String ballPositionKey = "Ball possession";
    private static final String fieldPositionKey = "Field position";

    @JsonIgnore
    private static final String quater1PointsKey = "Quarter 1 Points/Goals/Behinds";
    @JsonIgnore
    private static final String quater2PointsKey = "Quarter 2 Points/Goals/Behinds";
    @JsonIgnore
    private static final String quater3PointsKey = "Quarter 3 Points/Goals/Behinds";
    @JsonIgnore
    private static final String quater4PointsKey = "Quarter 4 Points/Goals/Behinds";
    @JsonIgnore
    private static final String ClockRunning = "Match Clock Running";

    @Override
    public LinkedHashMap<String, String> getAsMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put(pointsKey, String.format("%d-%d", pointsA, pointsB));
        map.put(goalsKey, String.format("%d-%d", goalsA, goalsB));
        int mins = elapsedTimeSecs / 60;
        int secs = elapsedTimeSecs - mins * 60;
        map.put(elapsedTimeKey, String.format("%d:%02d", mins, secs));
        map.put(matchPeriodKey, matchPeriod.toString());
        map.put(ballPositionKey, (this.ballPosition.getBallHoldingNow()).toString());
        map.put(fieldPositionKey, (this.ballPosition.getFieldPosition()).toString());
        map.put(overTimeSequenceKey, Integer.toString(overtimeNo));
        map.put(quater1PointsKey, String.format("%d-%d, %d-%d, %d-%d", this.getQ1PointsA(), this.getQ1PointsB(),
                        this.getGoalQ1A(), this.getGoalQ1B(), this.getBehindQ1A(), this.getBehindQ1B()));
        map.put(quater2PointsKey, String.format("%d-%d, %d-%d, %d-%d", this.getQ2PointsA(), this.getQ2PointsB(),
                        this.getGoalQ2A(), this.getGoalQ2B(), this.getBehindQ2A(), this.getBehindQ2B()));
        map.put(quater3PointsKey, String.format("%d-%d, %d-%d, %d-%d", this.getQ3PointsA(), this.getQ3PointsB(),
                        this.getGoalQ3A(), this.getGoalQ3B(), this.getBehindQ3A(), this.getBehindQ3B()));
        map.put(quater4PointsKey, String.format("%d-%d, %d-%d, %d-%d", this.getQ4PointsA(), this.getQ4PointsB(),
                        this.getGoalQ4A(), this.getGoalQ4B(), this.getBehindQ4A(), this.getBehindQ4B()));
        map.put(ClockRunning, String.valueOf(this.isMatchClockRunning()));
        for (int i = 0; i < 4; i++) {
            PairOfIntegers p = pointScoreInQuarterN[i];
            map.put(String.format("Point Score in Quarter %d", i + 1), String.format("%d-%d", p.A, p.B));
            PairOfIntegers g = goalScoreInQuarterN[i];
            map.put(String.format("Goal Score in Quarter %d", i + 1), String.format("%d-%d", g.A, g.B));
            PairOfIntegers b = behindScoreInQuarterN[i];
            map.put(String.format("Behind Score in Quarter %d", i + 1), String.format("%d-%d", b.A, b.B));
        }
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
        return (matchPeriod == AflMatchPeriod.MATCH_COMPLETED);
    }

    /**
     * returns true if at the end of normal time
     * 
     * @return
     */
    @JsonIgnore
    public boolean isNormalTimeMatchCompleted() {
        int currentPeriodNo = getPeriodNo();
        if (currentPeriodNo <= 4)
            return false;
        return true;
    }

    /**
     * returns true if at the end of one period and before the next one (if any) starts
     * 
     * @return
     */
    @JsonIgnore
    public boolean isPeriodCompleted() {
        return (matchPeriod == AflMatchPeriod.AT_FIRST_PERIOD_END || matchPeriod == AflMatchPeriod.AT_SECOND_PERIOD_END
                        || matchPeriod == AflMatchPeriod.MATCH_COMPLETED);
    }

    /**
     * gets the id of the team winning at the end of normalTime
     * 
     * @return UNKNOWN if a draw, else A or B
     */
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

    /**
     * Creating odd even market for quarter no 2 or 3
     * 
     * @return 0, 2, 3, when returning 0 stop creaking odd even quarter market
     */
    public int generatingOddEvenMarketForQuarterNo() {
        int quarterNo = 0;
        if (elapsedTimeSecs <= 1800)
            quarterNo = 2;
        else if (elapsedTimeSecs <= 3000)
            quarterNo = 3;
        return quarterNo;
    }

    /**
     * Get first team to xx points, in the xx quarter
     **/
    public TeamId checkListForScoreXXPoints(int periodNo, int points) {// when periodNo==0 is full time
        TeamId firstTeam = TeamId.UNKNOWN;
        // ArrayList is sequential list
        // Collections.sort(this.goalInfoList);
        int scoreA = 0;
        int scoreB = 0;
        int tempPoints = 0;

        if (periodNo == 0) {
            for (GoalInfo item : this.goalInfoList) {
                if (true) {
                    if (item.getMethod() == 0)
                        tempPoints = 6;
                    else
                        tempPoints = 1;

                    switch (item.getTeam()) {
                        case A:
                            scoreA += tempPoints;
                            if (scoreA >= points) {
                                return TeamId.A;
                            }
                            break;
                        case B:
                            scoreB += tempPoints;
                            if (scoreB >= points) {
                                return TeamId.B;
                            }
                            break;
                        default:
                            throw new IllegalArgumentException(
                                            "Check list for score xx points error, all points should associate to a scoring team");
                    }
                }
            }

        }
        {

            for (GoalInfo item : this.goalInfoList) {
                if (item.getPeriodNoG() == periodNo) {
                    if (item.getMethod() == 0)
                        tempPoints = 6;
                    else
                        tempPoints = 1;

                    switch (item.getTeam()) {
                        case A:
                            scoreA += tempPoints;
                            if (scoreA >= points) {
                                return TeamId.A;
                            }
                            break;
                        case B:
                            scoreB += tempPoints;
                            if (scoreB >= points) {
                                return TeamId.B;
                            }
                            break;
                        default:
                            throw new IllegalArgumentException(
                                            "Check list for score xx points error, all points should associate to a scoring team");
                    }
                }
            }

        }
        return firstTeam;
    }

    /**
     * Whether to create match points odd even market
     * 
     * @return True or False
     */
    @JsonIgnore
    public boolean isCreatingTotalPointsOddEvenMarket() {
        return elapsedTimeSecs <= 4200;
    }

    /**
     * Check the score list to find the first team scores in the quarter
     * 
     * @return TeamId
     **/

    public TeamId checkListForFirstScoreTeamForQuarter(int periodNo) {
        int firstScoreInPeriodSeconds = 1200 * (periodNo); // an integer larger than 1200 will do
        TeamId teamFirstScore = TeamId.UNKNOWN;

        if (periodNo == -1) {
            firstScoreInPeriodSeconds = 9999;
            for (GoalInfo item : this.goalInfoList) {
                if (item.getSecs() < firstScoreInPeriodSeconds) {
                    firstScoreInPeriodSeconds = item.getSecs();
                    teamFirstScore = item.getTeam();
                }
            }
        } else {
            for (GoalInfo item : this.goalInfoList) {
                if (item.getPeriodNoG() == periodNo) {
                    firstScoreInPeriodSeconds = item.getSecs();
                    teamFirstScore = item.getTeam();
                }
            }
        }
        return teamFirstScore;
    }

    /**
     * Check the score list to find the first team and method scores in the match
     * 
     * @return int
     **/

    public int checkListForFirstScoreTeamMethod() {
        TeamId teamFirstScore = TeamId.UNKNOWN;
        int method; // 0 is a goal, 1 is a behind
        int firstScorePlay = 0;

        for (GoalInfo item : this.goalInfoList) {
            method = item.getMethod();
            teamFirstScore = item.getTeam();
            if (teamFirstScore == TeamId.A) {
                if (method == 0)
                    firstScorePlay = 1;
                else
                    firstScorePlay = 2;
            } else if (teamFirstScore == TeamId.B) {
                if (method == 0)
                    firstScorePlay = 1;
                else
                    firstScorePlay = 2;
            }
        }

        return firstScorePlay;
    }

    public TeamId checkListForFirstScoreTeam() {
        TeamId teamFirstScore = TeamId.UNKNOWN;
        int method; // 0 is a goal, 1 is a behind

        for (GoalInfo item : this.goalInfoList) {
            method = item.getMethod();
            teamFirstScore = item.getTeam();
            if (teamFirstScore == TeamId.A) {
                if (method == 0)
                    teamFirstScore = TeamId.A;
                else
                    teamFirstScore = TeamId.A;
            } else if (teamFirstScore == TeamId.B) {
                if (method == 0)
                    teamFirstScore = TeamId.B;
                else
                    teamFirstScore = TeamId.B;
            }
        }

        return teamFirstScore;
    }

    /**
     * Check the score list to find the last team scores in the quarter
     * 
     * @return TeamId
     **/
    public TeamId checkListForTeamLastScoreInQuarter(List<GoalInfo> goalInfoList, int periodNo) {
        int lastScoreInPeriodSeconds = -1;
        TeamId team = TeamId.UNKNOWN;
        if (periodNo == -1) {
            for (GoalInfo item : goalInfoList) {
                int scoreSecs = item.getSecs();
                if (scoreSecs > lastScoreInPeriodSeconds) {
                    lastScoreInPeriodSeconds = item.getSecs();
                    team = item.getTeam();
                }
            }
        } else {
            for (GoalInfo item : goalInfoList) {
                int scoreSecs = item.getSecs();
                if (scoreSecs < 1200 * periodNo && scoreSecs >= 1200 * (periodNo - 1)
                                && scoreSecs > lastScoreInPeriodSeconds) {
                    lastScoreInPeriodSeconds = item.getSecs();
                    team = item.getTeam();
                }
            }
        }
        return team;
    }

    /**
     * checkListForLastScorePlay
     **/
    public int checkListForLastScore(int periodNo) {
        int lastScoreInPeriodSeconds = -1;
        TeamId team = TeamId.UNKNOWN;
        int scoreMethod = -1;
        if (periodNo != -1) {
            for (GoalInfo item : goalInfoList) {
                int scoreSecs = item.getSecs();
                if (scoreSecs < 1200 * periodNo && scoreSecs >= 1200 * (periodNo - 1)
                                && scoreSecs > lastScoreInPeriodSeconds) {
                    lastScoreInPeriodSeconds = item.getSecs();
                    team = item.getTeam();
                    scoreMethod = item.getMethod();
                }
            }
        } else {
            for (GoalInfo item : goalInfoList) {
                int scoreSecs = item.getSecs();
                if (scoreSecs > lastScoreInPeriodSeconds) {
                    lastScoreInPeriodSeconds = item.getSecs();
                    team = item.getTeam();
                    scoreMethod = item.getMethod();
                }
            }

        }

        if (scoreMethod == 0 && team.equals(TeamId.A)) {
            lastScoreInPeriodSeconds = 0;
        } else if (scoreMethod == 1 && team.equals(TeamId.A)) {
            lastScoreInPeriodSeconds = 1;
        } else if (scoreMethod == 0 && team.equals(TeamId.B)) {
            lastScoreInPeriodSeconds = 2;
        } else if (scoreMethod == 1 && team.equals(TeamId.B)) {
            lastScoreInPeriodSeconds = 3;
        } else {
            lastScoreInPeriodSeconds = 4; // No score in period
        }

        return lastScoreInPeriodSeconds;
    }

    /**
     * gets the id of the wininng team
     * 
     * @return UNKNOWN if a draw, else A or B
     */
    @JsonIgnore
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
     * get # points scored by team A in the previous match period
     * 
     * @return
     */
    public int getPreviousPeriodPointsA() {
        return previousPeriodPointsA;
    }

    /**
     * get # points scored by team B in the previous match period
     * 
     * @return
     */
    public int getPreviousPeriodPointsB() {
        return previousPeriodPointsB;
    }

    /**
     * get # goals scored by team A in the previous match period
     * 
     * @return
     */
    public int getPreviousPeriodGoalsA() {
        return previousPeriodGoalsA;
    }

    /**
     * get # goals scored by team B in the previous match period
     * 
     * @return
     */
    public int getPreviousPeriodGoalsB() {
        return previousPeriodGoalsB;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + currentPeriodPointsA;
        result = prime * result + currentPeriodPointsB;
        result = prime * result + elapsedTimeSecs;
        result = prime * result + elapsedTimeThisPeriodSecs;
        result = prime * result + extraPeriodSecs;
        result = prime * result + pointsA;
        result = prime * result + pointsB;
        result = prime * result + ((matchFormat == null) ? 0 : matchFormat.hashCode());
        result = prime * result + ((matchPeriod == null) ? 0 : matchPeriod.hashCode());
        result = prime * result + normalPeriodSecs;
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
        AflMatchState other = (AflMatchState) obj;
        if (currentPeriodPointsA != other.currentPeriodPointsA)
            return false;
        if (currentPeriodPointsB != other.currentPeriodPointsB)
            return false;
        if (elapsedTimeSecs != other.elapsedTimeSecs)
            return false;
        if (elapsedTimeThisPeriodSecs != other.elapsedTimeThisPeriodSecs)
            return false;
        if (extraPeriodSecs != other.extraPeriodSecs)
            return false;
        if (pointsA != other.pointsA)
            return false;
        if (pointsB != other.pointsB)
            return false;
        // if (matchFormat == null) {
        // if (other.matchFormat != null)
        // return false;
        // } else if (!matchFormat.equals(other.matchFormat))
        // return false;
        if (matchPeriod != other.matchPeriod)
            return false;
        if (normalPeriodSecs != other.normalPeriodSecs)
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
    // this.matchFormat = (AflMatchFormat) matchFormat;
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
            case AT_FIRST_PERIOD_END:
            case AT_SECOND_PERIOD_END:
            case AT_THIRD_PERIOD_END:
            case AT_FULL_TIME:
            case AT_HALF_TIME:
            case AT_EXTRA_PERIOD_END:
            case MATCH_COMPLETED:
                break;
            case IN_FIRST_HALF:
            case IN_SECOND_HALF:
            case IN_FIRST_PERIOD:
            case IN_SECOND_PERIOD:
            case IN_THIRD_PERIOD:
            case IN_FOURTH_PERIOD:
            case IN_EXTRA_TIME:
                int secs = getSecsSinceLastElapsedTimeFromIncident();
                setElapsedTime(elapsedTimeAtLastMatchIncidentSecs + secs);
                updatePrices = getSecsSinceLastPriceRecalc() >= 30;
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
                matchPeriod = AflMatchPeriod.IN_FIRST_PERIOD;
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
                    matchPeriod = AflMatchPeriod.IN_SECOND_PERIOD;
                    timeOfFirstGoalQuarter = -1;
                }
                break;
            case AT_SECOND_PERIOD_END:
            case IN_SECOND_PERIOD:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs - normalPeriodSecs;
                if (elapsedTimeThisPeriodSecs >= normalPeriodSecs) {
                    elapsedTimeSecs = 2 * normalPeriodSecs;
                    elapsedTimeThisPeriodSecs = 0;
                    matchPeriod = AflMatchPeriod.IN_THIRD_PERIOD;
                    timeOfFirstGoalQuarter = -1;
                }
                break;

            case AT_THIRD_PERIOD_END:
            case IN_THIRD_PERIOD:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs - 2 * normalPeriodSecs;
                if (elapsedTimeThisPeriodSecs >= normalPeriodSecs) {
                    elapsedTimeSecs = 3 * normalPeriodSecs;
                    elapsedTimeThisPeriodSecs = 0;
                    matchPeriod = AflMatchPeriod.IN_FOURTH_PERIOD;
                    timeOfFirstGoalQuarter = -1;
                }
                break;
            case AT_FULL_TIME:
            case IN_FOURTH_PERIOD:
                elapsedTimeThisPeriodSecs = elapsedTimeSecs - 3 * normalPeriodSecs;
                if (elapsedTimeThisPeriodSecs >= normalPeriodSecs) {
                    if (pointsA != pointsB || this.extraPeriodSecs == 0) {
                        matchPeriod = AflMatchPeriod.MATCH_COMPLETED;
                    } else {
                        elapsedTimeSecs = 4 * normalPeriodSecs;
                        elapsedTimeThisPeriodSecs = 0;
                        matchPeriod = AflMatchPeriod.IN_EXTRA_TIME;
                        timeOfFirstGoalQuarter = -1;
                        overtimeNo++;
                    }
                }
                break;
            case AT_EXTRA_PERIOD_END:
                if (pointsA != pointsB) {
                    matchPeriod = AflMatchPeriod.MATCH_COMPLETED;
                } else {
                    overtimeNo++;
                    matchPeriod = AflMatchPeriod.IN_EXTRA_TIME;
                    timeOfFirstGoalQuarter = -1;
                }

                break;
            case IN_EXTRA_TIME:
                if (overtimeNo > 2 && pointsA != pointsB) // golden goal in third
                                                          // extra time period
                {
                    matchPeriod = AflMatchPeriod.MATCH_COMPLETED;
                    break;
                }
                elapsedTimeThisPeriodSecs = elapsedTimeSecs - 4 * normalPeriodSecs - (overtimeNo - 1) * extraPeriodSecs;
                if (elapsedTimeThisPeriodSecs >= extraPeriodSecs) {
                    elapsedTimeThisPeriodSecs = 0;
                    elapsedTimeSecs = 4 * normalPeriodSecs + (overtimeNo) * extraPeriodSecs;
                    if (pointsA != pointsB) {
                        matchPeriod = AflMatchPeriod.MATCH_COMPLETED;
                        break;
                    } else {
                        matchPeriod = AflMatchPeriod.AT_EXTRA_PERIOD_END;
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

            case IN_FIRST_PERIOD:
                gamePeriod = GamePeriod.FIRST_QUARTER;
                break;
            case AT_FIRST_PERIOD_END:
                gamePeriod = GamePeriod.END_OF_FIRST_QUARTER;
                break;
            case IN_SECOND_PERIOD:
                gamePeriod = GamePeriod.SECOND_QUARTER;
                break;
            case AT_SECOND_PERIOD_END:
                gamePeriod = GamePeriod.END_OF_SECOND_QUARTER;
                break;
            case IN_THIRD_PERIOD:
                gamePeriod = GamePeriod.THIRD_QUARTER;
                break;
            case AT_THIRD_PERIOD_END:
                gamePeriod = GamePeriod.END_OF_THIRD_QUARTER;
                break;
            case IN_FOURTH_PERIOD:
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

        if (matchFormat.getExtraTimeMinutes() == 0) {
            if (periodNo > 5)
                return null;
            return String.format("P%d", periodNo);
        } else {
            return String.format("P%d", periodNo);
        }
    }

    /**
     * gets the sequence id to use for match half based markets
     * 
     * @return null if specified set can't occur, else the sequence id
     */
    @JsonIgnore
    public String getSequenceIdForHalf(int halfOffSet) {
        int periodNo = getPeriodNo() + halfOffSet;
        int halfNo = 1;

        if (periodNo > 5)
            return null;
        if (periodNo <= 2)
            halfNo = 1 + halfOffSet;
        else if (periodNo <= 4)
            halfNo = 2 + halfOffSet;
        else
            halfNo = 3 + halfOffSet;
        return String.format("P%d", halfNo);

    }

    /**
     * gets the sequence id to use for Goal based markets
     * 
     * @param goalOffset 0 = current , 1 = next etc
     */
    public String getSequenceIdForGoal(int goalOffset) {
        return String.format("G%d", goalsA + goalsB + 1 + goalOffset);
    }

    /**
     * gets the sequence id to use for Behind based markets
     * 
     * @param behindOffset 0 = current , 1 = next etc
     */
    public String getSequenceIdForBehind(int behindOffset) {
        return String.format("B%d", behindsA + behindsB + 1 + behindOffset);
    }

    /**
     * gets the sequence id to use for point based markets
     * 
     * @param pointOffset 0 = current , 1 = next etc
     */
    @JsonIgnore
    public String getSequenceIdForPoint(int pointOffset) {
        return String.format("P%d", pointsA + pointsB + 1 + pointOffset);
    }

    /**
     * returns true if in preMatch
     */

    @Override
    public boolean preMatch() {
        return (matchPeriod == AflMatchPeriod.PREMATCH);
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

            case AT_FIRST_PERIOD_END:
            case AT_SECOND_PERIOD_END:
            case AT_THIRD_PERIOD_END:

            case AT_EXTRA_PERIOD_END:

            case MATCH_COMPLETED:
            case PREMATCH:
                secs = 0;
                break;
            case IN_FIRST_HALF:
            case IN_SECOND_HALF:

            case IN_FIRST_PERIOD:
            case IN_SECOND_PERIOD:
            case IN_THIRD_PERIOD:
            case IN_FOURTH_PERIOD:
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

    public int getAPointsForPeriod(int periodSeqNo) {
        switch (periodSeqNo) {
            case 1:
                return goalQ1A * 6 + behindQ1A;
            case 2:
                return goalQ2A * 6 + behindQ2A;
            case 3:
                return goalQ3A * 6 + behindQ3A;
            case 4:
                return goalQ4A * 6 + behindQ4A;
            case 5:
                return goalQEA * 6 + behindQEA;

            default:
                return 0;

        }
    }

    public int getBPointsForPeriod(int periodSeqNo) {
        switch (periodSeqNo) {
            case 1:
                return goalQ1B * 6 + behindQ1B;
            case 2:
                return goalQ2B * 6 + behindQ2B;
            case 3:
                return goalQ3B * 6 + behindQ3B;
            case 4:
                return goalQ4B * 6 + behindQ4B;
            case 5:
                return goalQEB * 6 + behindQEB;

            default:
                return 0;

        }
    }

    public int getTotalAPointsForPeriod(int periodSeqNo) {
        switch (periodSeqNo) {
            case 1:
                return goalQ1A * 6 + behindQ1A;
            case 2:
                return goalQ2A * 6 + behindQ2A + goalQ1A * 6 + behindQ1A;
            case 3:
                return goalQ3A * 6 + behindQ3A + goalQ2A * 6 + behindQ2A + goalQ1A * 6 + behindQ1A;
            case 4:
                return goalQ4A * 6 + behindQ4A;
            case 5:
                return goalQEA * 6 + behindQEA;

            default:
                return 0;

        }
    }

    private LinkedHashMap<String, PairOfIntegers> getPointsScoredMap() {
        LinkedHashMap<String, PairOfIntegers> scoreMap = new LinkedHashMap<String, PairOfIntegers>();
        getCurrentPointScoreInQuarterN();
        for (int i = 1; i < 5; i++) {
            scoreMap.put("pointsInQuarter" + Integer.toString(i), pointScoreInQuarterN[i - 1]);
        }
        return scoreMap;
    }

    private LinkedHashMap<String, PairOfIntegers> getGoalsScoredMap() {
        LinkedHashMap<String, PairOfIntegers> scoreMap = new LinkedHashMap<String, PairOfIntegers>();
        getCurrentGoalScoreInQuarterN();
        for (int i = 1; i < 5; i++) {
            scoreMap.put("goalsInQuarter" + Integer.toString(i), goalScoreInQuarterN[i - 1]);
        }
        return scoreMap;
    }

    private LinkedHashMap<String, PairOfIntegers> getBehindsScoredMap() {
        LinkedHashMap<String, PairOfIntegers> scoreMap = new LinkedHashMap<String, PairOfIntegers>();
        getCurrentBehindScoreInQuarterN();
        for (int i = 1; i < 5; i++) {
            scoreMap.put("behindsInQuarter" + Integer.toString(i), behindScoreInQuarterN[i - 1]);
        }
        return scoreMap;
    }

    public int getTotalBPointsForPeriod(int periodSeqNo) {
        switch (periodSeqNo) {
            case 1:
                return goalQ1B * 6 + behindQ1B;
            case 2:
                return goalQ2B * 6 + behindQ2B + goalQ1B * 6 + behindQ1B;
            case 3:
                return goalQ3B * 6 + behindQ3B + goalQ2B * 6 + behindQ2B + goalQ1B * 6 + behindQ1B;
            case 4:
                return goalQ4B * 6 + behindQ4B;
            case 5:
                return goalQEB * 6 + behindQEB;

            default:
                return 0;

        }
    }

    // public boolean isPreMatch() {
    // return matchPeriod == AflMatchPeriod.PREMATCH;
    // }

    @Override
    public AflSimpleMatchState generateSimpleMatchState() {
        return new AflSimpleMatchState(preMatch(), isMatchCompleted(), getPointsA(), getPointsB(), getGoalsA(),
                        getGoalsB(), getBehindsA(), getBehindsB(), getPointsScoredMap(), getGoalsScoredMap(),
                        getBehindsScoredMap(), getBallPosition(), getMatchPeriod(), getElapsedTimeSecs());
    }

    @Override
    public AlgoMatchState generateMatchStateForMatchResult(MatchResultMap matchManualResult) {

        Map<String, MatchResultElement> map = matchManualResult.getMap();
        int extraTimeMinutes = ((AflMatchFormat) this.getMatchFormat()).getExtraTimeMinutes();// EXTRA
                                                                                              // TIME
                                                                                              // INDICATOR
        int nPairs = 8;
        if (extraTimeMinutes > 0)
            nPairs = 10;

        PairOfIntegers[] teamScore = new PairOfIntegers[nPairs];
        teamScore[0] = map.get("q1Point").valueAsPairOfIntegers();
        teamScore[1] = map.get("q1Goal").valueAsPairOfIntegers();
        teamScore[2] = map.get("q2Point").valueAsPairOfIntegers();
        teamScore[3] = map.get("q2Goal").valueAsPairOfIntegers();
        teamScore[4] = map.get("q3Point").valueAsPairOfIntegers();
        teamScore[5] = map.get("q3Goal").valueAsPairOfIntegers();
        teamScore[6] = map.get("q4Point").valueAsPairOfIntegers();
        teamScore[7] = map.get("q4Goal").valueAsPairOfIntegers();
        if (extraTimeMinutes > 0) {
            teamScore[8] = map.get("qExtraTimePoint").valueAsPairOfIntegers();
            teamScore[9] = map.get("qExtratimeGoal").valueAsPairOfIntegers();
        }

        int pointsTotalA = 0;
        int pointsTotalB = 0;
        int goalsTotalA = 0;
        int goalsTotalB = 0;
        // int q1GoalsA = 0;
        // int q1GoalsB = 0;
        // int q2pointsA = 0;
        // int q2PointsB = 0;
        // int q2GoalsA = 0;
        // int q2GoalsB = 0;
        // int q3pointsA = 0;
        // int q3PointsB = 0;
        // int q3GoalsA = 0;
        // int q3GoalsB = 0;
        // int q4pointsA = 0;
        // int q4PointsB = 0;
        // int q4GoalsA = 0;
        // int q4GoalsB = 0;
        // int qExtraPointsA = 0;
        // int qExtraPointsB = 0;
        // int qExtraGoalsA = 0;
        // int qExtraGoalsB = 0;
        AflMatchState endMatchState = (AflMatchState) this.copy();
        for (int i = 0; i < nPairs; i++) {
            int qPointsA = teamScore[i].A;
            int qPointsB = teamScore[i].B;
            pointsTotalA += qPointsA;
            pointsTotalB += qPointsB;
            i++;
            int qGoalsA = teamScore[i].A;
            int qGoalsB = teamScore[i].B;
            goalsTotalA += qGoalsA;
            goalsTotalB += qGoalsB;
            // System.out.println(" PointsA " + qPointsA + " qGoalsA " + qGoalsA + " PointsB
            // " + qPointsB + " qGoalsB "
            // + qGoalsB);
            switch (i / 2) {
                case 0:
                    endMatchState.setGoalQ1A(qGoalsA);
                    endMatchState.setBehindQ1A(qPointsA - qGoalsA * 6);
                    endMatchState.setGoalQ1B(qGoalsB);
                    endMatchState.setBehindQ1B(qPointsB - qGoalsB * 6);
                    break;
                case 1:
                    endMatchState.setGoalQ2A(qGoalsA);
                    endMatchState.setBehindQ2A(qPointsA - qGoalsA * 6);
                    endMatchState.setGoalQ2B(qGoalsB);
                    endMatchState.setBehindQ2B(qPointsB - qGoalsB * 6);
                    break;
                case 2:
                    endMatchState.setGoalQ3A(qGoalsA);
                    endMatchState.setBehindQ3A(qPointsA - qGoalsA * 6);
                    endMatchState.setGoalQ3B(qGoalsB);
                    endMatchState.setBehindQ3B(qPointsB - qGoalsB * 6);
                    break;
                case 3:
                    endMatchState.setGoalQ4A(qGoalsA);
                    endMatchState.setBehindQ4A(qPointsA - qGoalsA * 6);
                    endMatchState.setGoalQ4B(qGoalsB);
                    endMatchState.setBehindQ4B(qPointsB - qGoalsB * 6);
                    break;
                case 4:
                    endMatchState.setGoalQEA(qGoalsA);
                    endMatchState.setBehindQEA(qPointsA - qGoalsA * 6);
                    endMatchState.setGoalQEB(qGoalsB);
                    endMatchState.setBehindQEB(qPointsB - qGoalsB * 6);
                    break;
            }
        }
        endMatchState.setPointsA(pointsTotalA);
        endMatchState.setPointsB(pointsTotalB);
        endMatchState.setGoalsA(goalsTotalA);
        endMatchState.setGoalsB(goalsTotalB);
        if (teamScore.length == 8) {
            endMatchState.setElapsedTimeSecs(4800);// was 4800,0
        } else {
            endMatchState.setElapsedTimeSecs(5100);// was 5100,1

        }
        endMatchState.setMannulResulting(true);
        endMatchState.setMatchPeriod(AflMatchPeriod.MATCH_COMPLETED);
        return endMatchState;
    }

}
