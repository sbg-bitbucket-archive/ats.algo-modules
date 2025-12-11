package ats.algo.sport.testcricket;

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
import ats.algo.core.matchresult.MatchResultMap;
import ats.algo.sport.testcricket.TestCricketMatchIncident.CricketMatchIncidentType;
import ats.algo.sport.testcricket.TestCricketMatchIncidentResult.CricketMatchIncidentResultType;

public class TestCricketMatchState extends AlgoMatchState {

    private static final long serialVersionUID = 1L;
    private TestCricketMatchFormat matchFormat;
    private int oversA;
    private int oversB;
    private int ballsA;
    private int ballsB;
    private int ball;
    private int wicket;
    private TeamId bat;
    private int runsA;
    private int runsB;
    private int runsA1;
    private int runsB1;
    private int runsA2;
    private int runsB2;
    private int inningsOneRunsA;
    private int inningsOneRunsB;
    private int wicketsA;
    private int wicketsB;
    private int extrasA;
    private int extrasB;
    private int extra;
    private int[][] runsInOverNA;
    private int[][] runsInOverNB;
    private int[][] runsPerWicketA;
    private int[][] runsPerWicketB;
    private int[][] runsPerWicketA1;
    private int[][] runsPerWicketB1;
    private int inningsA;
    private int inningsB;
    private int day;
    private int totalDays;
    private int oversPerDay;
    private double rainingPer;
    private String[] onFieldPlayerName;
    private boolean isPlayerABating;
    private int previousPlayer;
    private Boolean isPlayerOut = null;
    private TestCricketMatchIncidentResult currentMatchState; // the state
                                                              // following the

    /**
     * @param wicketsA set Team A wicket No
     */
    public void setWicketsA(int wicketsA) {
        this.wicketsA = wicketsA;
    }

    /**
     * Returns true if player out
     * 
     * @return isPlayerOut
     */
    public Boolean isPlayerOut() {
        return isPlayerOut;
    }

    /**
     * @param isPlayerOut sets if player out
     */
    public void setPlayerOut(Boolean isPlayerOut) {
        this.isPlayerOut = isPlayerOut;
    }

    /**
     * Returns true if player A bat
     * 
     * @return isPlayerABating
     */
    public boolean isPlayerABating() {
        return isPlayerABating;
    }

    /**
     * @param isPlayerABating sets if player A bat
     */
    public void setPlayerABating(boolean isPlayerABating) {
        this.isPlayerABating = isPlayerABating;
    }

    /**
     * Returns int[][] to contain player A wicket No in inning
     * 
     * @return runsPerWicketA1
     */
    public int[][] getRunsPerWicketA1() {
        return runsPerWicketA1;
    }

    /**
     * @param runsPerWicketA1 sets runsPerWicketA1 to contain player A wicket No in inning
     */
    public void setRunsPerWicketA1(int[][] runsPerWicketA1) {
        this.runsPerWicketA1 = runsPerWicketA1;
    }

    /**
     * Returns int[][] to contain player B wicket No in inning
     * 
     * @return runsPerWicketB1
     */
    public int[][] getRunsPerWicketB1() {
        return runsPerWicketB1;
    }

    /**
     * @param runsPerWicketB1 sets runsPerWicketA1 to contain player B wicket No in inning
     */
    public void setRunsPerWicketB1(int[][] runsPerWicketB1) {
        this.runsPerWicketB1 = runsPerWicketB1;
    }

    /**
     * Returns how many days in a match
     * 
     * @return totalDays
     */
    public int getTotalDays() {
        return totalDays;
    }

    /**
     * @param totalDays sets how many days in a match
     */
    public void setTotalDays(int totalDays) {
        this.totalDays = totalDays;
    }

    /**
     * Returns how many overs estimated in a day
     * 
     * @return oversPerDay
     */
    public int getOversPerDay() {
        return oversPerDay;
    }

    /**
     * @param oversPerDay sets how many overs estimated in a day
     */
    public void setOversPerDay(int oversPerDay) {
        this.oversPerDay = oversPerDay;
    }

    /**
     * Returns who is previous player no
     * 
     * @return previousPlayer
     */
    public int getPreviousPlayer() {
        return previousPlayer;
    }

    /**
     * @param previousPlayer sets who is previous player no
     */
    public void setPreviousPlayer(int previousPlayer) {
        this.previousPlayer = previousPlayer;
    }

    /**
     * Returns team A runs No
     * 
     * @return runsA
     */
    public int getRunsA() {
        return runsA;
    }

    /**
     * @param runsA sets team A runs No
     */
    public void setRunsA(int runsA) {
        this.runsA = runsA;
    }

    /**
     * Returns team B runs No
     * 
     * @return runsB
     */
    public int getRunsB() {
        return runsB;
    }

    /**
     * @param runsB sets team B runs No
     */
    public void setRunsB(int runsB) {
        this.runsB = runsB;
    }

    /**
     * Returns the name of on field player
     * 
     * @return onFieldPlayerName
     */
    public String[] getOnFieldPlayerName() {
        return onFieldPlayerName;
    }

    /**
     * @param onFieldPlayerName sets the name of on field player
     */
    public void setOnFieldPlayerName(String[] onFieldPlayerName) {
        this.onFieldPlayerName = onFieldPlayerName;
    }

    /**
     * Returns int[][] to contain team A runs per each wicket
     * 
     * @return runsPerWicketA
     */
    public int[][] getRunsPerWicketA() {
        return runsPerWicketA;
    }

    /**
     * @param runsPerWicketA sets int[][] to contain team A runs per each wicket
     */
    public void setRunsPerWicketA(int[][] runsPerWicketA) {
        this.runsPerWicketA = runsPerWicketA;
    }

    /**
     * Returns int[][] to contain team B runs per each wicket
     * 
     * @return runsPerWicketB
     */
    public int[][] getRunsPerWicketB() {
        return runsPerWicketB;
    }

    /**
     * @param runsPerWicketB sets int[][] to contain team B runs per each wicket
     */
    public void setRunsPerWicketB(int[][] runsPerWicketB) {
        this.runsPerWicketB = runsPerWicketB;
    }

    /**
     * Returns raining percentage
     * 
     * @return rainingPer
     */
    public double getRainingPer() {
        return rainingPer;
    }

    /**
     * @param rainingPer sets raining percentage
     */
    public void setRainingPer(double rainingPer) {
        this.rainingPer = rainingPer;
    }

    /**
     * Returns remaining overs Per Day
     * 
     * @return oversPerDay
     */
    public int getRemainingOvers() {
        return oversPerDay;
    }

    /**
     * @param remainingOvers sets remaining overs Per Day
     */
    public void setRemainingOvers(int remainingOvers) {
        this.oversPerDay = remainingOvers;
    }

    /**
     * Returns current day
     * 
     * @return day
     */
    public int getDay() {
        return day;
    }

    /**
     * @param day sets current day
     */
    public void setDay(int day) {
        this.day = day;
    }

    /**
     * Returns total runs A in inning One
     * 
     * @return inningsOneRunsA
     */
    public int getInningsOneRunsA() {
        return inningsOneRunsA;
    }

    /**
     * @param inningsOneRunsA sets total runs A in inning One
     */
    public void setInningsOneRunsA(int inningsOneRunsA) {
        this.inningsOneRunsA = inningsOneRunsA;
    }

    /**
     * Returns total runs B in inning One
     * 
     * @return inningsOneRunsB
     */
    public int getInningsOneRunsB() {
        return inningsOneRunsB;
    }

    /**
     * @param inningsOneRunsB sets total runs B in inning One
     */
    public void setInningsOneRunsB(int inningsOneRunsB) {
        this.inningsOneRunsB = inningsOneRunsB;
    }

    /**
     * Returns team A innings No
     * 
     * @return inningsA
     */
    public int getInningsA() {
        return inningsA;
    }

    /**
     * @param inningsA sets team A innings No
     */
    public void setInningsA(int inningsA) {
        this.inningsA = inningsA;
    }

    /**
     * Returns team B innings No
     * 
     * @return inningsB
     */
    public int getInningsB() {
        return inningsB;
    }

    /**
     * @param inningsB sets team B innings No
     */
    public void setInningsB(int inningsB) {
        this.inningsB = inningsB;
    }

    /**
     * @param wicketsB sets team B wicket No
     */
    public void setWicketsB(int wicketsB) {
        this.wicketsB = wicketsB;
    }

    /**
     * Returns team A wicket No
     * 
     * @return wicketsA
     */
    public int getWicketsA() {
        return wicketsA;
    }

    /**
     * Returns team B wicket No
     * 
     * @return wicketsB
     */
    public int getWicketsB() {
        return wicketsB;
    }

    /**
     * Returns team A/B wicket No
     * 
     * @return wicket
     */
    public int getWicket() {
        return wicket;
    }

    /**
     * @param wicket sets team A/B wicket No
     */
    public void setWicket(int wicket) {
        this.wicket = wicket;
    }

    /**
     * @param ball sets team A/B ball No
     */
    public void setBall(int ball) {
        this.ball = ball;
    }

    /**
     * Returns team A/B ball No
     * 
     * @return ball
     */
    public int getBall() {
        return ball;
    }

    /**
     * @param oversA sets team A over No
     */
    public void setOversA(int oversA) {
        this.oversA = oversA;
    }

    /**
     * @param oversB sets team B over No
     */
    public void setOversB(int oversB) {
        this.oversB = oversB;
    }

    /**
     * Returns team A over No
     * 
     * @return oversA
     */
    public int getOversA() {
        return oversA;
    }

    /**
     * Returns team B over No
     * 
     * @return oversB
     */
    public int getOversB() {
        return oversB;
    }

    /**
     * Returns team A balls No
     * 
     * @return ballsA
     */
    public int getBallsA() {
        return ballsA;
    }

    /**
     * Returns team B balls No
     * 
     * @return ballsB
     */
    public int getBallsB() {
        return ballsB;
    }

    /**
     * @param ballsA sets team A ball No
     */
    public void setBallsA(int ballsA) {
        this.ballsA = ballsA;
    }

    /**
     * @param ballsB sets team B ball No
     */
    public void setBallsB(int ballsB) {
        this.ballsB = ballsB;
    }

    /**
     * @param runsA1 sets player A1 run No
     */
    public void setRunsA1(int runsA1) {
        this.runsA1 = runsA1;
    }

    /**
     * @param runsB1 sets player B1 run No
     */
    public void setRunsB1(int runsB1) {
        this.runsB1 = runsB1;
    }

    /**
     * Returns team A2 run No
     * 
     * @return runsA2
     */
    public int getRunsA2() {
        return runsA2;
    }

    /**
     * Returns team B2 run No
     * 
     * @return runsB2
     */
    public int getRunsB2() {
        return runsB2;
    }

    /**
     * @param runsA2 sets player A2 run No
     */
    public void setRunsA2(int runsA2) {
        this.runsA2 = runsA2;
    }

    /**
     * @param runsB2 sets player B2 run No
     */
    public void setRunsB2(int runsB2) {
        this.runsB2 = runsB2;
    }

    /**
     * Returns team A1 run No
     * 
     * @return runsA1
     */
    public int getRunsA1() {
        return runsA1;
    }

    /**
     * Returns team B1 run No
     * 
     * @return runsB1
     */
    public int getRunsB1() {
        return runsB1;
    }

    /**
     * @param bat sets who is bat
     */
    public void setBat(TeamId bat) {
        this.bat = bat;
    }

    /**
     * Returns who is bat
     * 
     * @return bat
     */
    public TeamId getBat() {
        return bat;
    }

    /**
     * Returns team A/B extra No
     * 
     * @return extra
     */
    public int getExtra() {
        return extra;
    }

    /**
     * @param extra sets current extra No
     */
    public void setExtra(int extra) {
        this.extra = extra;
    }

    /**
     * Returns team A extra No
     * 
     * @return extrasA
     */
    public int getExtrasA() {
        return extrasA;
    }

    /**
     * Returns team B extra No
     * 
     * @return extrasB
     */
    public int getExtrasB() {
        return extrasB;
    }

    /**
     * @param extrasA sets team A current extra No
     */
    public void setExtrasA(int extrasA) {
        this.extrasA = extrasA;
    }

    /**
     * @param extrasB sets team B current extra No
     */
    public void setExtrasB(int extrasB) {
        this.extrasB = extrasB;
    }

    /**
     * Returns int[][] to contain team A runs in each over
     * 
     * @return runsInOverNA
     */
    public int[][] getRunsInOverNA() {
        return runsInOverNA;
    }

    /**
     * @param runsInOverNA Sets int[][] to contain team A runs in each over
     */
    public void setRunsInOverNA(int[][] runsInOverNA) {
        this.runsInOverNA = runsInOverNA;
    }

    /**
     * Returns int[][] to contain team B runs in each over
     * 
     * @return runsInOverNB
     */
    public int[][] getRunsInOverNB() {
        return runsInOverNB;
    }

    /**
     * @param runsInOverNB Sets int[][] to contain team B runs in each over
     */
    public void setRunsInOverNB(int[][] runsInOverNB) {
        this.runsInOverNB = runsInOverNB;
    }

    /**
     * Returns the current match state as test cricket match incident result
     * 
     * @return currentMatchState
     */
    protected TestCricketMatchIncidentResult getCurrentMatchState() {
        return currentMatchState;
    }

    /**
     * 
     * 
     * @param currentMatchState sets current test cricket match state
     * 
     */
    protected void setCurrentMatchState(TestCricketMatchIncidentResult currentMatchState) {
        this.currentMatchState = currentMatchState;
    }

    public TestCricketMatchState(MatchFormat matchFormat) {
        super();
        this.matchFormat = (TestCricketMatchFormat) matchFormat;
        runsA1 = 0;
        runsB1 = 0;
        runsA2 = 0;
        runsB2 = 0;
        runsA = 0;
        runsB = 0;
        ballsA = 0;
        ballsB = 0;
        oversA = 1;
        oversB = 1;
        inningsA = 1;
        inningsB = 1;
        bat = TeamId.UNKNOWN;
        ball = 0;
        wicket = 0;
        wicketsA = 0;
        wicketsB = 0;
        extrasA = 0;
        extrasB = 0;
        extra = 0;
        day = 1;
        oversPerDay = 90;
        inningsOneRunsB = 0;
        inningsOneRunsA = 0;
        totalDays = ((TestCricketMatchFormat) matchFormat).getnDayinMatch();
        rainingPer = 0.0;
        runsInOverNA = new int[3][((TestCricketMatchFormat) matchFormat).getMaxOverinInning()];
        runsInOverNB = new int[3][((TestCricketMatchFormat) matchFormat).getMaxOverinInning()];
        runsPerWicketA = new int[3][11];
        runsPerWicketB = new int[3][11];
        runsPerWicketA1 = new int[3][11];
        runsPerWicketB1 = new int[3][11];
        for (int m = 0; m < 2; m++) {
            for (int i = 0; i < runsInOverNA.length - 1; i++) {
                runsInOverNA[m][i] = 0;
                runsInOverNB[m][i] = 0;
            }
            for (int i = 0; i < runsPerWicketA.length - 1; i++) {
                runsPerWicketA[m][i] = 0;
                runsPerWicketB[m][i] = 0;
                runsPerWicketA1[m][i] = 0;
                runsPerWicketB1[m][i] = 0;
            }
        }
        onFieldPlayerName = new String[2];
        onFieldPlayerName[0] = "Player unknown";
        onFieldPlayerName[1] = "Player unknown";
        isPlayerABating = true;
        previousPlayer = 0;
        currentMatchState = new TestCricketMatchIncidentResult(CricketMatchIncidentResultType.PREMATCH);
    }

    public TestCricketMatchState() {
        this(new TestCricketMatchFormat());
    }

    /**
     * Return test Cricket MatchIncident Result after the match incident updated
     * 
     * @return matchEventResult
     */
    @Override
    public MatchIncidentResult updateStateForIncident(MatchIncident matchIncident,
                    boolean autosyncMatchStateToFeedOnMismatch) {
        TestCricketMatchIncidentResult matchEventResult = null;
        String cricketPlayerName = "Player unknown";
        if (!(matchIncident instanceof TestCricketMatchIncident))
            return super.updateStateForIncident(matchIncident, autosyncMatchStateToFeedOnMismatch);

        else {
            TestCricketMatchIncident mI = (TestCricketMatchIncident) matchIncident;
            cricketPlayerName = mI.getCricketPlayerName();
            boolean updatePlayerBatting = false;
            boolean updatePlayerBattingByRun = false;
            if (isPlayerOut != null)
                if (isPlayerABating != isPlayerOut)
                    updatePlayerBatting = true;
            boolean serveA = bat == TeamId.A;
            boolean once = true;
            int wicketType = -1;
            int countExtra = 0;
            int countAdjust = 0;
            TeamId teamId = mI.getTeamId();
            isPlayerABating = (cricketPlayerName.equals(onFieldPlayerName[0]));
            switch (mI.getIncidentSubType()) {
                case BATFIRST:
                    matchEventResult = new TestCricketMatchIncidentResult(CricketMatchIncidentResultType.BAT, teamId);
                    bat = teamId;
                    if (bat == TeamId.A) {
                        onFieldPlayerName[0] = cpAKey[0];
                        onFieldPlayerName[1] = cpAKey[1];
                        isPlayerABating = true;
                    } else {
                        onFieldPlayerName[0] = cpBKey[0];
                        onFieldPlayerName[1] = cpBKey[1];
                        isPlayerABating = true;
                    }
                    break;
                case DECLARATION:
                    matchEventResult = new TestCricketMatchIncidentResult(CricketMatchIncidentResultType.DECLARATION,
                                    teamId);
                    bat = switchServer(bat);
                    ball = 0;
                    wicket = 0;
                    extra = 0;
                    if (bat.equals(TeamId.A)) {
                        inningsB++;
                        if (inningsB == 2)
                            inningsOneRunsB = runsB;
                        onFieldPlayerName[0] = cpAKey[0];
                        onFieldPlayerName[1] = cpAKey[1];
                        isPlayerABating = true;
                        runsA1 = 0;
                        runsA2 = 0;
                        previousPlayer = 0;
                    } else {
                        inningsA++;
                        if (inningsA == 2)
                            inningsOneRunsA = runsA;
                        onFieldPlayerName[0] = cpBKey[0];
                        onFieldPlayerName[1] = cpBKey[1];
                        isPlayerABating = true;
                        runsB1 = 0;
                        runsB2 = 0;
                        previousPlayer = 0;
                    }
                    break;
                case DAY:
                    if (day != 0)
                        matchEventResult = new TestCricketMatchIncidentResult(CricketMatchIncidentResultType.DAY);
                    else
                        matchEventResult = new TestCricketMatchIncidentResult(CricketMatchIncidentResultType.DRAW,
                                        TeamId.UNKNOWN);
                    break;
                case SWITCHBATMAN:
                    isPlayerABating = !isPlayerABating;
                    matchEventResult = new TestCricketMatchIncidentResult(CricketMatchIncidentResultType.SWITCHBATMAN);
                    break;
                case FOLLOW_ON:
                    matchEventResult = new TestCricketMatchIncidentResult(CricketMatchIncidentResultType.FOLLOW_ON);
                    bat = switchServer(bat);
                    ball = 0;
                    wicket = 0;
                    extra = 0;
                    if (bat.equals(TeamId.A)) {
                        onFieldPlayerName[0] = cpAKey[0];
                        onFieldPlayerName[1] = cpAKey[1];
                        isPlayerABating = true;
                        runsA1 = 0;
                        runsA2 = 0;
                        previousPlayer = 0;
                    } else {
                        onFieldPlayerName[0] = cpBKey[0];
                        onFieldPlayerName[1] = cpBKey[1];
                        isPlayerABating = true;
                        runsB1 = 0;
                        runsB2 = 0;
                        previousPlayer = 0;
                    }
                    break;
                case NORUNS:
                    matchEventResult = new TestCricketMatchIncidentResult(CricketMatchIncidentResultType.NORUNS);
                    ball++;

                    if (serveA) {
                        ballsA = ball;
                        runsInOverNA[inningsA - 1][oversA - 1] = runsA;
                        oversA = ball / 6 + 1;
                        wicketsA = wicket;
                    } else {
                        ballsB = ball;
                        runsInOverNB[inningsB - 1][oversB - 1] = runsB;
                        oversB = ball / 6 + 1;
                        wicketsB = wicket;
                    }
                    if (ball % 6 == 0)
                        isPlayerABating = switchBat(isPlayerABating);
                    break;
                case RUN1:
                    matchEventResult = new TestCricketMatchIncidentResult(CricketMatchIncidentResultType.RUN1);
                    ball++;

                    if (serveA) {
                        ballsA = ball;
                        wicketsA = wicket;
                        runsA += 1;
                        runsInOverNA[inningsA - 1][oversA - 1] = runsA;
                        oversA = ball / 6 + 1;
                        if (isPlayerABating) {
                            runsA1 += 1;
                            runsPerWicketA[inningsA - 1][previousPlayer] = runsA1;
                        } else {
                            runsA2 += 1;
                            runsPerWicketA[inningsA - 1][wicket + 1] = runsA2;
                        }
                    } else {
                        ballsB = ball;
                        runsB += 1;
                        wicketsB = wicket;
                        runsInOverNB[inningsB - 1][oversB - 1] = runsB;
                        oversB = ball / 6 + 1;
                        if (isPlayerABating) {
                            runsB1 += 1;
                            runsPerWicketB[inningsB - 1][previousPlayer] = runsB1;
                        } else {
                            runsB2 += 1;
                            runsPerWicketB[inningsB - 1][wicket + 1] = runsB2;
                        }
                    }
                    isPlayerABating = switchBat(isPlayerABating);
                    if (ball % 6 == 0)
                        isPlayerABating = switchBat(isPlayerABating);
                    break;
                case RUN2:
                    matchEventResult = new TestCricketMatchIncidentResult(CricketMatchIncidentResultType.RUN2);
                    ball++;

                    if (serveA) {
                        ballsA = ball;
                        runsA += 2;
                        wicketsA = wicket;
                        runsInOverNA[inningsA - 1][oversA - 1] = runsA;
                        oversA = ball / 6 + 1;
                        if (isPlayerABating) {
                            runsA1 += 2;
                            runsPerWicketA[inningsA - 1][previousPlayer] = runsA1;
                        } else {
                            runsA2 += 2;
                            runsPerWicketA[inningsA - 1][wicket + 1] = runsA2;
                        }
                    } else {
                        ballsB = ball;
                        runsB += 2;
                        wicketsB = wicket;
                        runsInOverNB[inningsB - 1][oversB - 1] = runsB;
                        oversB = ball / 6 + 1;
                        if (isPlayerABating) {
                            runsB1 += 2;
                            runsPerWicketB[inningsB - 1][previousPlayer] = runsB1;
                        } else {
                            runsB2 += 2;
                            runsPerWicketB[inningsB - 1][wicket + 1] = runsB2;
                        }
                    }
                    if (ball % 6 == 0)
                        isPlayerABating = switchBat(isPlayerABating);
                    break;
                case RUN3:
                    matchEventResult = new TestCricketMatchIncidentResult(CricketMatchIncidentResultType.RUN3);
                    ball++;
                    if (serveA) {
                        ballsA = ball;
                        runsA += 3;
                        wicketsA = wicket;
                        runsInOverNA[inningsA - 1][oversA - 1] = runsA;
                        oversA = ball / 6 + 1;
                        if (isPlayerABating) {
                            runsA1 += 3;
                            runsPerWicketA[inningsA - 1][previousPlayer] = runsA1;
                        } else {
                            runsA2 += 3;
                            runsPerWicketA[inningsA - 1][wicket + 1] = runsA2;
                        }
                    } else {
                        ballsB = ball;
                        runsB += 3;
                        wicketsB = wicket;
                        runsInOverNB[inningsB - 1][oversB - 1] = runsB;
                        oversB = ball / 6 + 1;
                        if (isPlayerABating) {
                            runsB1 += 3;
                            runsPerWicketB[inningsB - 1][previousPlayer] = runsB1;
                        } else {
                            runsB2 += 3;
                            runsPerWicketB[inningsB - 1][wicket + 1] = runsB2;
                        }
                    }
                    isPlayerABating = switchBat(isPlayerABating);
                    if (ball % 6 == 0)
                        isPlayerABating = switchBat(isPlayerABating);
                    break;
                case RUN4:
                    matchEventResult = new TestCricketMatchIncidentResult(CricketMatchIncidentResultType.RUN4);
                    ball++;

                    if (serveA) {
                        ballsA = ball;
                        runsA += 4;
                        wicketsA = wicket;
                        runsInOverNA[inningsA - 1][oversA - 1] = runsA;
                        oversA = ball / 6 + 1;
                        if (isPlayerABating) {
                            runsA1 += 4;
                            runsPerWicketA[inningsA - 1][previousPlayer] = runsA1;
                        } else {
                            runsA2 += 4;
                            runsPerWicketA[inningsA - 1][wicket + 1] = runsA2;
                        }
                    } else {
                        ballsB = ball;
                        runsB += 4;
                        wicketsB = wicket;
                        runsInOverNB[inningsB - 1][oversB - 1] = runsB;
                        oversB = ball / 6 + 1;
                        if (isPlayerABating) {
                            runsB1 += 4;
                            runsPerWicketB[inningsB - 1][previousPlayer] = runsB1;
                        } else {
                            runsB2 += 4;
                            runsPerWicketB[inningsB - 1][wicket + 1] = runsB2;
                        }
                    }
                    if (ball % 6 == 0)
                        isPlayerABating = switchBat(isPlayerABating);
                    break;
                case RUN5:
                    matchEventResult = new TestCricketMatchIncidentResult(CricketMatchIncidentResultType.RUN5);
                    ball++;

                    if (serveA) {
                        ballsA = ball;
                        runsA += 5;
                        wicketsA = wicket;
                        runsInOverNA[inningsA - 1][oversA - 1] = runsA;
                        oversA = ball / 6 + 1;
                        if (isPlayerABating) {
                            runsA1 += 5;
                            runsPerWicketA[inningsA - 1][previousPlayer] = runsA1;
                        } else {
                            runsA2 += 5;
                            runsPerWicketA[inningsA - 1][wicket + 1] = runsA2;
                        }
                    } else {
                        ballsB = ball;
                        runsB += 5;
                        wicketsB = wicket;
                        runsInOverNB[inningsB - 1][oversB - 1] = runsB;
                        oversB = ball / 6 + 1;
                        if (isPlayerABating) {
                            runsB1 += 5;
                            runsPerWicketB[inningsB - 1][previousPlayer] = runsB1;
                        } else {
                            runsB2 += 5;
                            runsPerWicketB[inningsB - 1][wicket + 1] = runsB2;
                        }
                    }
                    isPlayerABating = switchBat(isPlayerABating);
                    if (ball % 6 == 0)
                        isPlayerABating = switchBat(isPlayerABating);
                    break;
                case RUN6:
                    matchEventResult = new TestCricketMatchIncidentResult(CricketMatchIncidentResultType.RUN6);
                    ball++;
                    if (serveA) {
                        ballsA = ball;
                        runsA += 6;
                        wicketsA = wicket;
                        runsInOverNA[inningsA - 1][oversA - 1] = runsA;
                        oversA = ball / 6 + 1;
                        if (isPlayerABating) {
                            runsA1 += 6;
                            runsPerWicketA[inningsA - 1][previousPlayer] = runsA1;
                        } else {
                            runsA2 += 6;
                            runsPerWicketA[inningsA - 1][wicket + 1] = runsA2;
                        }
                    } else {
                        ballsB = ball;
                        runsB += 6;
                        wicketsB = wicket;
                        runsInOverNB[inningsB - 1][oversB - 1] = runsB;
                        oversB = ball / 6 + 1;
                        if (isPlayerABating) {
                            runsB1 += 6;
                            runsPerWicketB[inningsB - 1][previousPlayer] = runsB1;
                        } else {
                            runsB2 += 6;
                            runsPerWicketB[inningsB - 1][wicket + 1] = runsB2;
                        }
                    }
                    if (ball % 6 == 0)
                        isPlayerABating = switchBat(isPlayerABating);

                    if (isInningEnded()) {
                        bat = switchServer(bat);
                        ball = 0;
                        wicket = 0;
                        extra = 0;
                        if (bat.equals(TeamId.A)) {
                            inningsB++;
                            if (inningsB == 2)
                                inningsOneRunsB = runsB;
                            onFieldPlayerName[0] = cpAKey[0];
                            onFieldPlayerName[1] = cpAKey[1];
                            isPlayerABating = true;
                            runsA1 = 0;
                            runsA2 = 0;
                            previousPlayer = 0;
                        } else {
                            inningsA++;
                            if (inningsA == 2)
                                inningsOneRunsA = runsA;
                            onFieldPlayerName[1] = cpBKey[1];
                            onFieldPlayerName[0] = cpBKey[0];
                            isPlayerABating = true;
                            runsB1 = 0;
                            runsB2 = 0;
                            previousPlayer = 0;
                        }
                        if (inningsA > 2 && inningsB > 2) {
                            bat = TeamId.UNKNOWN;
                            if (runsA > runsB)
                                matchEventResult = new TestCricketMatchIncidentResult(true, wicketType,
                                                CricketMatchIncidentResultType.MATCHWON, TeamId.A);
                            else if (runsA < runsB)
                                matchEventResult = new TestCricketMatchIncidentResult(true, wicketType,
                                                CricketMatchIncidentResultType.MATCHWON, TeamId.B);
                            else
                                matchEventResult =
                                                new TestCricketMatchIncidentResult(CricketMatchIncidentResultType.DRAW);
                            if (wicketType != -1) {
                                matchEventResult.setLastPointWicket(true);
                                matchEventResult.setWicketType(wicketType);
                            }
                        }
                    }
                    break;

                case WICKET_CAUGHT:
                    if (once) {
                        matchEventResult = new TestCricketMatchIncidentResult(
                                        CricketMatchIncidentResultType.WICKET_CAUGHT);
                        once = false;
                        wicketType = 0;
                    }
                case WICKET_BOWLED:
                    if (once) {
                        matchEventResult = new TestCricketMatchIncidentResult(
                                        CricketMatchIncidentResultType.WICKET_BOWLED);
                        once = false;
                        wicketType = 1;
                    }
                case WICKET_LBW:
                    if (once) {
                        matchEventResult =
                                        new TestCricketMatchIncidentResult(CricketMatchIncidentResultType.WICKET_LBW);
                        once = false;
                        wicketType = 2;
                    }
                case WICKET_OTHER:
                    if (once) {
                        matchEventResult =
                                        new TestCricketMatchIncidentResult(CricketMatchIncidentResultType.WICKET_OTHER);
                        once = false;
                        wicketType = 5;
                    }
                case WICKET_RUN_OUT:
                    if (once) {
                        matchEventResult = new TestCricketMatchIncidentResult(
                                        CricketMatchIncidentResultType.WICKET_RUN_OUT);
                        once = false;
                        wicketType = 3;
                    }
                case WICKET_STUMPED:
                    if (once) {
                        matchEventResult = new TestCricketMatchIncidentResult(
                                        CricketMatchIncidentResultType.WICKET_STUMPED);
                        once = false;
                        wicketType = 4;
                    }

                case WICKET_RUN_OUT_AND_RUN6:
                    if (once) {
                        once = false;
                        matchEventResult = new TestCricketMatchIncidentResult(
                                        CricketMatchIncidentResultType.WICKET_RUN_OUT_AND_RUN);
                        wicketType = 3;
                        if (serveA) {
                            runsA += 6;
                            if (isPlayerABating) {
                                runsA1 += 6;
                                runsPerWicketA[inningsA - 1][previousPlayer] = runsA1;
                            } else {
                                runsA2 += 6;
                                runsPerWicketA[inningsA - 1][wicket + 1] = runsA2;
                            }
                        } else {
                            runsB += 6;
                            if (isPlayerABating) {
                                runsB1 += 6;
                                runsPerWicketB[inningsB - 1][previousPlayer] = runsB1;
                            } else {
                                runsB2 += 6;
                                runsPerWicketB[inningsB - 1][wicket + 1] = runsB2;
                            }
                        }
                    }
                case WICKET_RUN_OUT_AND_RUN5:
                    if (once) {
                        once = false;
                        matchEventResult = new TestCricketMatchIncidentResult(
                                        CricketMatchIncidentResultType.WICKET_RUN_OUT_AND_RUN);
                        wicketType = 3;
                        updatePlayerBattingByRun = true;
                        // isPlayerABating = switchBat(isPlayerABating);
                        if (serveA) {
                            runsA += 5;
                            if (isPlayerABating) {
                                runsA1 += 5;
                                runsPerWicketA[inningsA - 1][previousPlayer] = runsA1;
                            } else {
                                runsA2 += 5;
                                runsPerWicketA[inningsA - 1][wicket + 1] = runsA2;
                            }
                        } else {
                            runsB += 5;
                            if (isPlayerABating) {
                                runsB1 += 5;
                                runsPerWicketB[inningsB - 1][previousPlayer] = runsB1;
                            } else {
                                runsB2 += 5;
                                runsPerWicketB[inningsB - 1][wicket + 1] = runsB2;
                            }
                        }
                    }
                case WICKET_RUN_OUT_AND_RUN4:
                    if (once) {
                        once = false;
                        matchEventResult = new TestCricketMatchIncidentResult(
                                        CricketMatchIncidentResultType.WICKET_RUN_OUT_AND_RUN);
                        wicketType = 3;
                        if (serveA) {
                            runsA += 4;
                            if (isPlayerABating) {
                                runsA1 += 4;
                                runsPerWicketA[inningsA - 1][previousPlayer] = runsA1;
                            } else {
                                runsA2 += 4;
                                runsPerWicketA[inningsA - 1][wicket + 1] = runsA2;
                            }
                        } else {
                            runsB += 4;
                            if (isPlayerABating) {
                                runsB1 += 4;
                                runsPerWicketB[inningsB - 1][previousPlayer] = runsB1;
                            } else {
                                runsB2 += 4;
                                runsPerWicketB[inningsB - 1][wicket + 1] = runsB2;
                            }
                        }
                    }
                case WICKET_RUN_OUT_AND_RUN3:
                    if (once) {
                        once = false;
                        matchEventResult = new TestCricketMatchIncidentResult(
                                        CricketMatchIncidentResultType.WICKET_RUN_OUT_AND_RUN);
                        wicketType = 3;
                        // isPlayerABating = switchBat(isPlayerABating);
                        updatePlayerBattingByRun = true;
                        if (serveA) {
                            runsA += 3;
                            if (isPlayerABating) {
                                runsA1 += 3;
                                runsPerWicketA[inningsA - 1][previousPlayer] = runsA1;
                            } else {
                                runsA2 += 3;
                                runsPerWicketA[inningsA - 1][wicket + 1] = runsA2;
                            }
                        } else {
                            runsB += 3;
                            if (isPlayerABating) {
                                runsB1 += 3;
                                runsPerWicketB[inningsB - 1][previousPlayer] = runsB1;
                            } else {
                                runsB2 += 3;
                                runsPerWicketB[inningsB - 1][wicket + 1] = runsB2;
                            }
                        }
                    }
                case WICKET_RUN_OUT_AND_RUN2:
                    if (once) {
                        once = false;
                        matchEventResult = new TestCricketMatchIncidentResult(
                                        CricketMatchIncidentResultType.WICKET_RUN_OUT_AND_RUN);
                        wicketType = 3;
                        if (serveA) {
                            runsA += 1;
                            if (isPlayerABating) {
                                runsA1 += 2;
                                runsPerWicketA[inningsA - 1][previousPlayer] = runsA1;
                            } else {
                                runsA2 += 2;
                                runsPerWicketA[inningsA - 1][wicket + 1] = runsA2;
                            }
                        } else {
                            runsB += 2;
                            if (isPlayerABating) {
                                runsB1 += 2;
                                runsPerWicketB[inningsB - 1][previousPlayer] = runsB1;
                            } else {
                                runsB2 += 2;
                                runsPerWicketB[inningsB - 1][wicket + 1] = runsB2;
                            }
                        }
                    }
                case WICKET_RUN_OUT_AND_RUN1:
                    if (once) {
                        matchEventResult = new TestCricketMatchIncidentResult(
                                        CricketMatchIncidentResultType.WICKET_RUN_OUT_AND_RUN);
                        once = false;
                        // isPlayerABating = switchBat(isPlayerABating);
                        updatePlayerBattingByRun = true;
                        wicketType = 3;
                        if (serveA) {
                            runsA += 1;
                            if (isPlayerABating) {
                                runsA1 += 1;
                                runsPerWicketA[inningsA - 1][previousPlayer] = runsA1;
                            } else {
                                runsA2 += 1;
                                runsPerWicketA[inningsA - 1][wicket + 1] = runsA2;
                            }
                        } else {
                            runsB += 1;
                            if (isPlayerABating) {
                                runsB1 += 1;
                                runsPerWicketB[inningsB - 1][previousPlayer] = runsB1;
                            } else {
                                runsB2 += 1;
                                runsPerWicketB[inningsB - 1][wicket + 1] = runsB2;
                            }
                        }
                    }

                    wicket++;
                    ball++;
                    if (serveA) {
                        ballsA = ball;
                        runsInOverNA[inningsA - 1][oversA - 1] = runsA;
                        oversA = ball / 6 + 1;
                        wicketsA = wicket;
                        if (wicket != 10) {
                            if (isPlayerABating) {
                                runsPerWicketA[inningsA - 1][previousPlayer] = runsA1;
                                runsPerWicketA1[inningsA - 1][wicket - 1] = runsA;
                                previousPlayer = getCricketPlayerNo(cpAKey, onFieldPlayerName[1]);
                                runsA1 = runsA2;
                                onFieldPlayerName[0] = onFieldPlayerName[1];
                                onFieldPlayerName[1] = cpAKey[wicket + 1];
                                runsA2 = 0;
                                isPlayerABating = !isPlayerABating;
                            } else {
                                runsPerWicketA[inningsA - 1][previousPlayer] = runsA2;
                                runsPerWicketA1[inningsA - 1][wicket - 1] = runsA;
                                previousPlayer = getCricketPlayerNo(cpAKey, onFieldPlayerName[0]);
                                runsA2 = 0;
                                onFieldPlayerName[1] = cpAKey[wicket + 1];
                            }
                        } else {
                            if (isPlayerABating) {
                                runsPerWicketA[inningsA - 1][previousPlayer] = runsA1;
                                runsPerWicketA1[inningsA - 1][wicket - 1] = runsA;
                                runsA1 = runsA2;
                                runsA2 = 0;
                                isPlayerABating = !isPlayerABating;
                                previousPlayer = 0;
                            } else {
                                runsPerWicketA[inningsA - 1][previousPlayer] = runsA2;
                                runsPerWicketA1[inningsA - 1][wicket - 1] = runsA;
                                isPlayerABating = !isPlayerABating;
                                previousPlayer = 0;
                            }
                        }

                    } else {
                        ballsB = ball;
                        runsInOverNB[inningsB - 1][oversB - 1] = runsB;
                        oversB = ball / 6 + 1;
                        wicketsB = wicket;
                        if (wicket != 10) {
                            if (isPlayerABating) {
                                runsPerWicketB[inningsB - 1][previousPlayer] = runsB1;
                                runsPerWicketB1[inningsB - 1][wicket - 1] = runsB;
                                previousPlayer = getCricketPlayerNo(cpBKey, onFieldPlayerName[1]);
                                runsB1 = runsB2;
                                onFieldPlayerName[0] = onFieldPlayerName[1];
                                onFieldPlayerName[1] = cpBKey[wicket + 1];
                                runsB2 = 0;
                                isPlayerABating = !isPlayerABating;
                            } else {
                                runsPerWicketB[inningsB - 1][previousPlayer] = runsB2;
                                runsPerWicketB1[inningsB - 1][wicket - 1] = runsB;
                                previousPlayer = getCricketPlayerNo(cpBKey, onFieldPlayerName[0]);
                                runsB2 = 0;
                                onFieldPlayerName[1] = cpBKey[wicket + 1];
                            }
                        } else {
                            if (isPlayerABating) {
                                runsPerWicketB[inningsB - 1][previousPlayer] = runsB1;
                                runsPerWicketB1[inningsB - 1][wicket - 1] = runsB;
                                runsB1 = runsB2;
                                runsB2 = 0;
                                isPlayerABating = !isPlayerABating;
                                previousPlayer = 0;
                            } else {
                                runsPerWicketB[inningsB - 1][previousPlayer] = runsB2;
                                runsPerWicketB1[inningsB - 1][wicket - 1] = runsB;
                                previousPlayer = 0;
                            }
                        }
                    }
                    if (updatePlayerBatting)
                        isPlayerABating = switchBat(isPlayerABating);
                    if (updatePlayerBattingByRun)
                        isPlayerABating = switchBat(isPlayerABating);
                    if (ball % 6 == 0)
                        isPlayerABating = switchBat(isPlayerABating);
                    if (isInningEnded()) {
                        bat = switchServer(bat);
                        ball = 0;
                        wicket = 0;
                        extra = 0;
                        if (bat.equals(TeamId.A)) {
                            inningsB++;
                            if (inningsB == 2)
                                inningsOneRunsB = runsB;
                            onFieldPlayerName[0] = cpAKey[0];
                            onFieldPlayerName[1] = cpAKey[1];
                            isPlayerABating = true;
                            runsA1 = 0;
                            runsA2 = 0;
                        } else {
                            inningsA++;
                            if (inningsA == 2)
                                inningsOneRunsA = runsA;
                            onFieldPlayerName[0] = cpBKey[0];
                            onFieldPlayerName[1] = cpBKey[1];
                            isPlayerABating = true;
                            runsB1 = 0;
                            runsB2 = 0;
                        }
                        if (inningsA == 2 && inningsB == 3) {
                            if (runsA > runsB)
                                matchEventResult = new TestCricketMatchIncidentResult(true, wicketType,
                                                CricketMatchIncidentResultType.MATCHWON, TeamId.A);
                            if (wicketType != -1) {
                                matchEventResult.setLastPointWicket(true);
                                matchEventResult.setWicketType(wicketType);
                            }
                        }
                        if (inningsA == 3 && inningsB == 2) {
                            if (runsA < runsB)
                                matchEventResult = new TestCricketMatchIncidentResult(true, wicketType,
                                                CricketMatchIncidentResultType.MATCHWON, TeamId.B);
                            if (wicketType != -1) {
                                matchEventResult.setLastPointWicket(true);
                                matchEventResult.setWicketType(wicketType);
                            }
                        }
                    }
                    break;
                case EXTRAS6:
                    matchEventResult = new TestCricketMatchIncidentResult(CricketMatchIncidentResultType.EXTRAS);
                    countExtra++;
                case EXTRAS5:
                    matchEventResult = new TestCricketMatchIncidentResult(CricketMatchIncidentResultType.EXTRAS);
                    countExtra++;
                case EXTRAS4:
                    matchEventResult = new TestCricketMatchIncidentResult(CricketMatchIncidentResultType.EXTRAS);
                    countExtra++;
                case EXTRAS3:
                    matchEventResult = new TestCricketMatchIncidentResult(CricketMatchIncidentResultType.EXTRAS);
                    countExtra++;
                case EXTRAS2:
                    matchEventResult = new TestCricketMatchIncidentResult(CricketMatchIncidentResultType.EXTRAS);
                    countExtra++;
                case EXTRAS1:
                    matchEventResult = new TestCricketMatchIncidentResult(CricketMatchIncidentResultType.EXTRAS);
                case EXTRAS:
                    matchEventResult = new TestCricketMatchIncidentResult(CricketMatchIncidentResultType.EXTRAS);
                    countExtra++;
                    extra += countExtra;
                    if (serveA) {
                        extrasA = extra;
                        ballsA = ball;
                        runsA = runsA + countExtra;
                        runsInOverNA[inningsA - 1][oversA - 1] = runsA;
                        oversA = ball / 6 + 1;
                        wicketsA = wicket;
                    } else {
                        extrasB = extra;
                        ballsB = ball;
                        runsB = runsB + countExtra;
                        runsInOverNB[inningsB - 1][oversB - 1] = runsB;
                        oversB = ball / 6 + 1;
                        wicketsB = wicket;
                    }
                    break;
                case BYES6:
                    matchEventResult = new TestCricketMatchIncidentResult(CricketMatchIncidentResultType.BYES);
                    countExtra++;
                case BYES5:
                    matchEventResult = new TestCricketMatchIncidentResult(CricketMatchIncidentResultType.BYES);
                    countExtra++;
                case BYES4:
                    matchEventResult = new TestCricketMatchIncidentResult(CricketMatchIncidentResultType.BYES);
                    countExtra++;
                case BYES3:
                    matchEventResult = new TestCricketMatchIncidentResult(CricketMatchIncidentResultType.BYES);
                    countExtra++;
                case BYES2:
                    matchEventResult = new TestCricketMatchIncidentResult(CricketMatchIncidentResultType.BYES);
                    countExtra++;
                case BYES1:
                    matchEventResult = new TestCricketMatchIncidentResult(CricketMatchIncidentResultType.BYES);
                    countExtra++;
                    ball++;
                    extra += countExtra;
                    if (countExtra % 2 == 1)
                        isPlayerABating = switchBat(isPlayerABating);
                    if (serveA) {
                        extrasA = extra;
                        ballsA = ball;
                        runsA = runsA + countExtra;
                        runsInOverNA[inningsA - 1][oversA - 1] = runsA;
                        oversA = ball / 6 + 1;
                        wicketsA = wicket;
                    } else {
                        extrasB = extra;
                        ballsB = ball;
                        runsB = runsB + countExtra;
                        runsInOverNB[inningsB - 1][oversB - 1] = runsB;
                        oversB = ball / 6 + 1;
                        wicketsB = wicket;
                    }
                    break;
                case AJDUSTSCORE7:
                    countAdjust++;
                    countAdjust++;
                case AJDUSTSCORE5:
                    countAdjust++;
                case AJDUSTSCORE4:
                    countAdjust++;
                case AJDUSTSCORE3:
                    countAdjust++;
                case AJDUSTSCORE2:
                    countAdjust++;
                case AJDUSTSCORE1:
                    countAdjust++;
                    if (serveA) {
                        runsA = runsA + countAdjust;
                        runsInOverNA[inningsA - 1][oversA - 1] = runsA;
                        if (isPlayerABating) {
                            runsA1 = runsA1 + countAdjust;
                            runsPerWicketA[inningsA - 1][previousPlayer] = runsA1;
                        } else {
                            runsA2 = runsA2 + countAdjust;
                            runsPerWicketA[inningsA - 1][wicket + 1] = runsA2;
                        }
                    } else {
                        runsA = runsB + countAdjust;
                        if (isPlayerABating) {
                            runsB1 = runsB1 + countAdjust;
                            runsPerWicketB[inningsB - 1][previousPlayer] = runsB1;
                        } else {
                            runsB2 = runsB2 + countAdjust;
                            runsPerWicketB[inningsB - 1][wicket + 1] = runsB2;
                        }
                    }
                    matchEventResult = new TestCricketMatchIncidentResult(CricketMatchIncidentResultType.ADJUSTSCORE);
                    break;
                case MATCH_FINISHED:
                    bat = TeamId.UNKNOWN;
                    break;
                case MATCH_COMPLETED:
                    matchEventResult =
                                    new TestCricketMatchIncidentResult(CricketMatchIncidentResultType.MATCH_COMPLETED);
                    break;
                default:
                    break;
            }
        }
        if (inningsA == 2 && inningsB == 3) {
            if (runsA > runsB)
                matchEventResult =
                                new TestCricketMatchIncidentResult(CricketMatchIncidentResultType.MATCHWON, TeamId.A);
        }
        if (inningsA == 3 && inningsB == 2) {
            if (runsA < runsB)
                matchEventResult =
                                new TestCricketMatchIncidentResult(CricketMatchIncidentResultType.MATCHWON, TeamId.B);
        }
        if (inningsA > 2 && inningsB > 2)
            if ((bat == TeamId.A && runsA > runsB) || (bat == TeamId.B && runsB > runsA)) {
                if (runsA > runsB)
                    matchEventResult = new TestCricketMatchIncidentResult(CricketMatchIncidentResultType.MATCHWON,
                                    TeamId.A);

                else if (runsA < runsB)
                    matchEventResult = new TestCricketMatchIncidentResult(CricketMatchIncidentResultType.MATCHWON,
                                    TeamId.B);
                else
                    matchEventResult = new TestCricketMatchIncidentResult(CricketMatchIncidentResultType.DRAW,
                                    TeamId.UNKNOWN);
            }
        if (inningsA > 2 && oversB > 240) {
            matchEventResult = new TestCricketMatchIncidentResult(CricketMatchIncidentResultType.DRAW, TeamId.UNKNOWN);
        }
        if (inningsB > 2 && oversA > 240) {
            matchEventResult = new TestCricketMatchIncidentResult(CricketMatchIncidentResultType.DRAW, TeamId.UNKNOWN);
        }
        matchEventResult.setCricketPlayerName(cricketPlayerName);
        currentMatchState = matchEventResult;
        // if (oversA == 2000) {
        // System.out.println(runsA + "--" + ballsA + "--" + runsA1 + "--" +
        // ball + "---" + wicket + "--"
        // + runsB + "--" + ballsB + "--" + inningsA + "--" + inningsB + "--" +
        // bat + "--"
        // + wicketsA + "--" +
        // wicketsB+currentMatchState.getcricketMatchIncidentResultType()
        // +onFieldPlayerName[0]+"--"+onFieldPlayerName[1]);
        // }
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
        TestCricketMatchState cc = new TestCricketMatchState(matchFormat);
        cc.setEqualTo(this);
        return cc;
    }

    @Override
    public void setEqualTo(AlgoMatchState matchState) {
        TestCricketMatchState vs = (TestCricketMatchState) matchState;
        super.setEqualTo(matchState);
        this.matchFormat = (TestCricketMatchFormat) vs.getMatchFormat();
        this.setBallsA(vs.getBallsA());
        this.setBallsB(vs.getBallsB());
        this.setOversA(vs.getOversA());
        this.setOversB(vs.getOversB());
        this.setBall(vs.getBall());
        this.setRunsA(vs.getRunsA());
        this.setRunsB(vs.getRunsB());
        this.setRunsA1(vs.getRunsA1());
        this.setRunsB1(vs.getRunsB1());
        this.setRunsA2(vs.getRunsA2());
        this.setRunsB2(vs.getRunsB2());
        this.setWicket(vs.getWicket());
        this.setBat(vs.getBat());
        this.setWicketsA(vs.getWicketsA());
        this.setWicketsB(vs.getWicketsB());
        this.setExtra(vs.getExtra());
        this.setExtrasA(vs.getExtrasA());
        this.setExtrasB(vs.getExtrasB());
        int[][] temp_A = copy(vs.getRunsInOverNA());
        int[][] temp_B = copy(vs.getRunsInOverNB());
        int[][] temp_WA = copy(vs.getRunsPerWicketA());
        int[][] temp_WB = copy(vs.getRunsPerWicketB());
        int[][] temp_WA1 = copy(vs.getRunsPerWicketA1());
        int[][] temp_WB1 = copy(vs.getRunsPerWicketB1());
        String[] temp_FieldPlayerName = copyString(vs.getOnFieldPlayerName());
        this.setOnFieldPlayerName(temp_FieldPlayerName);
        this.setPreviousPlayer(vs.getPreviousPlayer());
        this.setRunsInOverNA(temp_A);
        this.setRunsInOverNB(temp_B);
        this.setRunsPerWicketA(temp_WA);
        this.setRunsPerWicketB(temp_WB);
        this.setRunsPerWicketA1(temp_WA1);
        this.setRunsPerWicketB1(temp_WB1);
        this.setInningsA(vs.getInningsA());
        this.setInningsB(vs.getInningsB());
        this.setInningsOneRunsA(vs.getInningsOneRunsA());
        this.setInningsOneRunsB(vs.getInningsOneRunsB());
        this.setDay(vs.getDay());
        this.setPlayerABating(vs.isPlayerABating);
        this.setRemainingOvers(vs.getRemainingOvers());
        this.setPlayerOut(vs.isPlayerOut != null ? null : vs.isPlayerOut);
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
        String lastLegWinner = "BA"; // set default prompt to be whoever on the
                                     // last leg
        String matchWinner = "A";
        String nextPromt =
                        "Enter RUNS of next BALL (R1/2/3/4/5/6/0) OR wicket method : O0 bowled, O1 caught, O2 LBW, O3 RUNOUT , O4 STUMPED,O5 OTHER, R1O3 RUNOUTANDRUN A/B OR Extra(W1/2/3/4/5/6) and Byes/Leg Byes(B1/2/3/4/5/6/7) Declaration (DA/B) DAY1/2/3/4/5 Follow_on(FO) END of Last day(Day0) Switch bat man(SW) Adjustscore(A1/2/3/4/5/6/7)";
        TeamId teamId = currentMatchState.getTeamId();
        switch (currentMatchState.getcricketMatchIncidentResultType()) {
            case PREMATCH:
                matchIncidentPrompt = new MatchIncidentPrompt("Enter BA/BB as who bat first", lastLegWinner);
                break;
            case NORUNS:
                lastLegWinner = "R0";
                matchIncidentPrompt = new MatchIncidentPrompt(nextPromt, lastLegWinner);
                break;
            case RUN1:
                lastLegWinner = "R0";
                matchIncidentPrompt = new MatchIncidentPrompt(nextPromt, lastLegWinner);
                break;
            case RUN2:
                lastLegWinner = "R0";
                matchIncidentPrompt = new MatchIncidentPrompt(nextPromt, lastLegWinner);
                break;
            case RUN3:
                lastLegWinner = "R0";
                matchIncidentPrompt = new MatchIncidentPrompt(nextPromt, lastLegWinner);
                break;
            case RUN4:
                lastLegWinner = "R0";
                matchIncidentPrompt = new MatchIncidentPrompt(nextPromt, lastLegWinner);
                break;
            case RUN5:
                lastLegWinner = "R0";
                matchIncidentPrompt = new MatchIncidentPrompt(nextPromt, lastLegWinner);
                break;
            case RUN6:
                lastLegWinner = "R0";
                matchIncidentPrompt = new MatchIncidentPrompt(nextPromt, lastLegWinner);
                break;
            case BAT:
                lastLegWinner = "R0";
                matchIncidentPrompt = new MatchIncidentPrompt(nextPromt, lastLegWinner);
                break;
            case EXTRAS:
                lastLegWinner = "W1";
                matchIncidentPrompt = new MatchIncidentPrompt(nextPromt, lastLegWinner);
                break;
            case BYES:
                lastLegWinner = "N1";
                matchIncidentPrompt = new MatchIncidentPrompt(nextPromt, lastLegWinner);
                break;
            case WICKET_BOWLED:
                lastLegWinner = "O0";
                matchIncidentPrompt = new MatchIncidentPrompt(nextPromt, lastLegWinner);
                break;
            case WICKET_CAUGHT:
                lastLegWinner = "O1";
                matchIncidentPrompt = new MatchIncidentPrompt(nextPromt, lastLegWinner);
                break;
            case WICKET_LBW:
                lastLegWinner = "O2";
                matchIncidentPrompt = new MatchIncidentPrompt(nextPromt, lastLegWinner);
                break;
            case WICKET_OTHER:
                lastLegWinner = "O5";
                matchIncidentPrompt = new MatchIncidentPrompt(nextPromt, lastLegWinner);
                break;

            case WICKET_RUN_OUT:
                lastLegWinner = "O3";
                matchIncidentPrompt = new MatchIncidentPrompt(nextPromt, lastLegWinner);
                break;
            case WICKET_RUN_OUT_AND_RUN:
                lastLegWinner = "R1O3";
                matchIncidentPrompt = new MatchIncidentPrompt(nextPromt, lastLegWinner);
                break;
            case WICKET_STUMPED:
                lastLegWinner = "O4";
                matchIncidentPrompt = new MatchIncidentPrompt(nextPromt, lastLegWinner);
                break;
            case DECLARATION:
                lastLegWinner = "R0";
                matchIncidentPrompt = new MatchIncidentPrompt(nextPromt, lastLegWinner);
                break;
            case FOLLOW_ON:
                lastLegWinner = "R0";
                matchIncidentPrompt = new MatchIncidentPrompt(nextPromt, lastLegWinner);
                break;
            case DAY:
                lastLegWinner = "R0";
                matchIncidentPrompt = new MatchIncidentPrompt(nextPromt, lastLegWinner);
                break;
            case SWITCHBATMAN:
                lastLegWinner = "R0";
                matchIncidentPrompt = new MatchIncidentPrompt(nextPromt, lastLegWinner);
                break;
            case ADJUSTSCORE:
                lastLegWinner = "R0";
                matchIncidentPrompt = new MatchIncidentPrompt(nextPromt, lastLegWinner);
                break;
            case DRAW:
                matchWinner = "Draw";
                matchIncidentPrompt = new MatchIncidentPrompt(
                                String.format("Match is draw , Enter MC to complete the match"));
                break;
            case MATCHWON:
                matchWinner = "MC";
                if (TeamId.A == teamId)
                    matchWinner = "A";
                else
                    matchWinner = "B";
                matchIncidentPrompt = new MatchIncidentPrompt(String
                                .format("Match is finished - won by %s ,Enter MC to complete the match", matchWinner));
                break;
            case MATCH_COMPLETED:
                matchWinner = "MC";
                matchIncidentPrompt = new MatchIncidentPrompt(String.format("Match completed"));
                break;
            default:
                break;

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
        TestCricketMatchIncident matchEvent = new TestCricketMatchIncident();
        TeamId teamId;
        String name = isPlayerABating ? onFieldPlayerName[0] : onFieldPlayerName[1];
        switch (response.toUpperCase()) {
            case "R0":
                matchEvent.set(CricketMatchIncidentType.NORUNS);
                matchEvent.setCricketPlayerName(name);
                break;
            case "R1":
                matchEvent.set(CricketMatchIncidentType.RUN1);
                matchEvent.setCricketPlayerName(name);
                break;
            case "R2":
                matchEvent.set(CricketMatchIncidentType.RUN2);
                matchEvent.setCricketPlayerName(name);
                break;
            case "R3":
                matchEvent.set(CricketMatchIncidentType.RUN3);
                matchEvent.setCricketPlayerName(name);
                break;
            case "R4":
                matchEvent.set(CricketMatchIncidentType.RUN4);
                matchEvent.setCricketPlayerName(name);
                break;
            case "R5":
                matchEvent.set(CricketMatchIncidentType.RUN5);
                matchEvent.setCricketPlayerName(name);
                break;
            case "R6":
                matchEvent.set(CricketMatchIncidentType.RUN6);
                matchEvent.setCricketPlayerName(name);
                break;
            case "O0":
            case "O0A":
                matchEvent.set(CricketMatchIncidentType.WICKET_BOWLED);
                matchEvent.setCricketPlayerName(onFieldPlayerName[0]);
                isPlayerOut = isPlayerABating;
                break;
            case "O1":
            case "O1A":
                matchEvent.set(CricketMatchIncidentType.WICKET_CAUGHT);
                matchEvent.setCricketPlayerName(onFieldPlayerName[0]);
                isPlayerOut = true;
                break;
            case "O2":
            case "O2A":
                matchEvent.set(CricketMatchIncidentType.WICKET_LBW);
                matchEvent.setCricketPlayerName(onFieldPlayerName[0]);
                isPlayerOut = true;
                break;
            case "O3":
            case "O3A":
                matchEvent.set(CricketMatchIncidentType.WICKET_RUN_OUT);
                matchEvent.setCricketPlayerName(onFieldPlayerName[0]);
                isPlayerOut = true;
                break;
            case "O4":
            case "O4A":
                matchEvent.set(CricketMatchIncidentType.WICKET_STUMPED);
                matchEvent.setCricketPlayerName(onFieldPlayerName[0]);
                isPlayerOut = true;
                break;
            case "O5":
            case "O5A":
                matchEvent.set(CricketMatchIncidentType.WICKET_OTHER);
                matchEvent.setCricketPlayerName(onFieldPlayerName[0]);
                isPlayerOut = true;
                break;
            case "R1O3":
            case "R1O3A":
                matchEvent.set(CricketMatchIncidentType.WICKET_RUN_OUT_AND_RUN1);
                matchEvent.setCricketPlayerName(onFieldPlayerName[0]);
                isPlayerOut = true;
                break;
            case "R2O3":
            case "R2O3A":
                matchEvent.set(CricketMatchIncidentType.WICKET_RUN_OUT_AND_RUN2);
                matchEvent.setCricketPlayerName(onFieldPlayerName[0]);
                isPlayerOut = true;
                break;
            case "R3O3":
            case "R3O3A":
                matchEvent.set(CricketMatchIncidentType.WICKET_RUN_OUT_AND_RUN3);
                matchEvent.setCricketPlayerName(onFieldPlayerName[0]);
                isPlayerOut = true;
                break;
            case "R4O3":
            case "R4O3A":
                matchEvent.set(CricketMatchIncidentType.WICKET_RUN_OUT_AND_RUN4);
                matchEvent.setCricketPlayerName(onFieldPlayerName[0]);
                isPlayerOut = true;
                break;
            case "R5O3":
            case "R5O3A":
                matchEvent.set(CricketMatchIncidentType.WICKET_RUN_OUT_AND_RUN5);
                matchEvent.setCricketPlayerName(onFieldPlayerName[0]);
                isPlayerOut = true;
                break;
            case "R6O3":
            case "R6O3A":
                matchEvent.set(CricketMatchIncidentType.WICKET_RUN_OUT_AND_RUN6);
                matchEvent.setCricketPlayerName(onFieldPlayerName[0]);
                isPlayerOut = true;
                break;
            case "O0B":
                matchEvent.set(CricketMatchIncidentType.WICKET_BOWLED);
                matchEvent.setCricketPlayerName(onFieldPlayerName[1]);
                isPlayerOut = false;
                break;
            case "O1B":
                matchEvent.set(CricketMatchIncidentType.WICKET_CAUGHT);
                matchEvent.setCricketPlayerName(onFieldPlayerName[1]);
                isPlayerOut = false;
                break;
            case "O2B":
                matchEvent.set(CricketMatchIncidentType.WICKET_LBW);
                matchEvent.setCricketPlayerName(onFieldPlayerName[1]);
                isPlayerOut = false;
                break;
            case "O3B":
                matchEvent.set(CricketMatchIncidentType.WICKET_RUN_OUT);
                matchEvent.setCricketPlayerName(onFieldPlayerName[1]);
                isPlayerOut = false;
                break;
            case "O4B":
                matchEvent.set(CricketMatchIncidentType.WICKET_STUMPED);
                matchEvent.setCricketPlayerName(onFieldPlayerName[1]);
                isPlayerOut = false;
                break;
            case "O5B":
                matchEvent.set(CricketMatchIncidentType.WICKET_OTHER);
                matchEvent.setCricketPlayerName(onFieldPlayerName[1]);
                isPlayerOut = false;
                break;
            case "R1O3B":
                matchEvent.set(CricketMatchIncidentType.WICKET_RUN_OUT_AND_RUN1);
                matchEvent.setCricketPlayerName(onFieldPlayerName[1]);
                isPlayerOut = false;
                break;
            case "R2O3B":
                matchEvent.set(CricketMatchIncidentType.WICKET_RUN_OUT_AND_RUN2);
                matchEvent.setCricketPlayerName(onFieldPlayerName[1]);
                isPlayerOut = false;
                break;
            case "R3O3B":
                matchEvent.set(CricketMatchIncidentType.WICKET_RUN_OUT_AND_RUN3);
                matchEvent.setCricketPlayerName(onFieldPlayerName[1]);
                isPlayerOut = false;
                break;
            case "R4O3B":
                matchEvent.set(CricketMatchIncidentType.WICKET_RUN_OUT_AND_RUN4);
                matchEvent.setCricketPlayerName(onFieldPlayerName[1]);
                isPlayerOut = false;
                break;
            case "R5O3B":
                matchEvent.set(CricketMatchIncidentType.WICKET_RUN_OUT_AND_RUN5);
                matchEvent.setCricketPlayerName(onFieldPlayerName[1]);
                isPlayerOut = false;
                break;
            case "R6O3B":
                matchEvent.set(CricketMatchIncidentType.WICKET_RUN_OUT_AND_RUN6);
                matchEvent.setCricketPlayerName(onFieldPlayerName[1]);
                isPlayerOut = false;
                break;
            case "W1":
                matchEvent.set(CricketMatchIncidentType.EXTRAS1);
                matchEvent.setCricketPlayerName(name);
                break;
            case "W2":
                matchEvent.set(CricketMatchIncidentType.EXTRAS2);
                matchEvent.setCricketPlayerName(name);
                break;
            case "W3":
                matchEvent.set(CricketMatchIncidentType.EXTRAS3);
                matchEvent.setCricketPlayerName(name);
                break;
            case "W4":
                matchEvent.set(CricketMatchIncidentType.EXTRAS4);
                matchEvent.setCricketPlayerName(name);
                break;
            case "W5":
                matchEvent.set(CricketMatchIncidentType.EXTRAS5);
                matchEvent.setCricketPlayerName(name);
                break;
            case "W6":
                matchEvent.set(CricketMatchIncidentType.EXTRAS6);
                matchEvent.setCricketPlayerName(name);
                break;
            case "B1":
                matchEvent.set(CricketMatchIncidentType.BYES1);
                matchEvent.setCricketPlayerName(name);
                break;
            case "B2":
                matchEvent.set(CricketMatchIncidentType.BYES2);
                matchEvent.setCricketPlayerName(name);
                break;
            case "B3":
                matchEvent.set(CricketMatchIncidentType.BYES3);
                matchEvent.setCricketPlayerName(name);
                break;
            case "B4":
                matchEvent.set(CricketMatchIncidentType.BYES4);
                matchEvent.setCricketPlayerName(name);
                break;
            case "B5":
                matchEvent.set(CricketMatchIncidentType.BYES5);
                matchEvent.setCricketPlayerName(name);
                break;
            case "B6":
                matchEvent.set(CricketMatchIncidentType.BYES6);
                matchEvent.setCricketPlayerName(name);
                break;
            case "BA":
                teamId = TeamId.A;
                matchEvent.set(CricketMatchIncidentType.BATFIRST, teamId);
                break;
            case "BB":
                teamId = TeamId.B;
                matchEvent.set(CricketMatchIncidentType.BATFIRST, teamId);
                break;
            case "DA":
                teamId = TeamId.A;
                matchEvent.set(CricketMatchIncidentType.DECLARATION, teamId);
                break;
            case "DB":
                teamId = TeamId.B;
                matchEvent.set(CricketMatchIncidentType.DECLARATION, teamId);
                break;
            case "FO":
                matchEvent.set(CricketMatchIncidentType.FOLLOW_ON);
                break;
            case "DAY1":
            case "DAY":
                day = 1;
                matchEvent.set(CricketMatchIncidentType.DAY);
                break;
            case "DAY2":
                day = 2;
                matchEvent.set(CricketMatchIncidentType.DAY);
                break;
            case "DAY3":
                day = 3;
                matchEvent.set(CricketMatchIncidentType.DAY);
                break;
            case "DAY4":
                day = 4;
                matchEvent.set(CricketMatchIncidentType.DAY);
                break;
            case "DAY5":
                day = 5;
                matchEvent.set(CricketMatchIncidentType.DAY);
                break;
            case "DAY0":
                day = 0;
                matchEvent.set(CricketMatchIncidentType.DAY);
                break;
            case "A1":
                matchEvent.set(CricketMatchIncidentType.AJDUSTSCORE1);
                break;
            case "A2":
                matchEvent.set(CricketMatchIncidentType.AJDUSTSCORE2);
                break;
            case "A3":
                matchEvent.set(CricketMatchIncidentType.AJDUSTSCORE3);
                break;
            case "A4":
                matchEvent.set(CricketMatchIncidentType.AJDUSTSCORE4);
                break;
            case "A5":
                matchEvent.set(CricketMatchIncidentType.AJDUSTSCORE5);
                break;
            case "A6":
                matchEvent.set(CricketMatchIncidentType.AJDUSTSCORE6);
                break;
            case "A7":
                matchEvent.set(CricketMatchIncidentType.AJDUSTSCORE7);
                break;
            case "MF":
                matchEvent.set(CricketMatchIncidentType.MATCH_FINISHED);
                break;
            case "MC":
                matchEvent.set(CricketMatchIncidentType.MATCH_COMPLETED);
                break;
            case "SW":
                matchEvent.set(CricketMatchIncidentType.SWITCHBATMAN);
                matchEvent.setCricketPlayerName(name);
                break;
            default:
                return null; // invalid input so return null
        }
        return matchEvent;
    }

    private static final String inScoreKey = "inning score";
    private static final String seScoreKey = "over score";
    private static final String baScoreKey = "ball score";
    private static final String poScoreKey = "run score";
    private static final String wiScoreKey = "wicket";
    private static final String wcScoreKey = "wicket score";
    private static final String exScoreKey = "extra score";
    private static final String onServeKey = "which team is bating";
    private static final String inABScoreKey = "inning one total runs A:B";
    private static final String intABScoreKey = "inning two total runs A:B";
    private static final String onFieldKey = "on field players";
    private static final String whoIsBatKey = "who is batting";
    private static final String dayKey = "Day";
    private static final String remainOverKey = "overs per day";
    private static final String rainPerKey = "Raining Per";
    private String[] cpAKey = {"PlayerA1", "PlayerA2", "PlayerA3", "PlayerA4", "PlayerA5", "PlayerA6", "PlayerA7",
            "PlayerA8", "PlayerA9", "PlayerA10", "PlayerA11"};
    private String[] cpBKey = {"PlayerB1", "PlayerB2", "PlayerB3", "PlayerB4", "PlayerB5", "PlayerB6", "PlayerB7",
            "PlayerB8", "PlayerB9", "PlayerB10", "PlayerB11"};
    private static final String runAPerKey = "Run A1:A2";
    private static final String runBPerKey = "Run B1:B2";

    /**
     * Returns map
     * 
     * @return map
     */
    @JsonIgnore
    @Override
    public LinkedHashMap<String, String> getAsMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put(inScoreKey, String.format("%d-%d", inningsA, inningsB));
        map.put(seScoreKey, String.format("%d-%d", oversA, oversB));
        map.put(baScoreKey, String.format("%d-%d", ballsA, ballsB));
        map.put(baScoreKey, String.format("%d", ball % 6));
        map.put(wiScoreKey, String.format("%d", wicket));
        map.put(poScoreKey, String.format("%d-%d", runsA, runsB));
        map.put(wcScoreKey, String.format("%d-%d", wicketsA, wicketsB));
        map.put(exScoreKey, String.format("%d-%d", extrasA, extrasB));
        map.put(onServeKey, String.format("%s", bat));
        if (inningsA == 1 || inningsB == 1) {
            map.put(inABScoreKey, String.format("%d-%d", runsA, runsB));
            map.put(intABScoreKey, String.format("%d-%d", 0, 0));
        } else {
            map.put(inABScoreKey, String.format("%d-%d", inningsOneRunsA, inningsOneRunsB));
            map.put(intABScoreKey, String.format("%d-%d", runsA - inningsOneRunsA, runsB - inningsOneRunsB));
        }
        map.put(onFieldKey, String.format("%s , %s", onFieldPlayerName[0], onFieldPlayerName[1]));
        map.put(whoIsBatKey, isPlayerABating ? onFieldPlayerName[0] : onFieldPlayerName[1]);
        map.put(runAPerKey, String.format("%d-%d", runsA1, runsA2));
        map.put(runBPerKey, String.format("%d-%d", runsB1, runsB2));
        map.put(dayKey, String.format("%s", day));
        map.put(remainOverKey, String.format("%s", oversPerDay));
        map.put(rainPerKey, String.format("%s", rainingPer));
        map.put("P:" + cpAKey[0], String.format("%s-%s", runsPerWicketA[0][0], runsPerWicketA[1][0]));
        map.put(cpAKey[1], String.format("%s-%s", runsPerWicketA[0][1], runsPerWicketA[1][1]));
        map.put("P:" + cpAKey[2], String.format("%s-%s", runsPerWicketA[0][2], runsPerWicketA[1][2]));
        map.put("P:" + cpAKey[3], String.format("%s-%s", runsPerWicketA[0][3], runsPerWicketA[1][3]));
        map.put("P:" + cpAKey[4], String.format("%s-%s", runsPerWicketA[0][4], runsPerWicketA[1][4]));
        map.put("P:" + cpAKey[5], String.format("%s-%s", runsPerWicketA[0][5], runsPerWicketA[1][5]));
        map.put("P:" + cpAKey[6], String.format("%s-%s", runsPerWicketA[0][6], runsPerWicketA[1][6]));
        map.put("P:" + cpAKey[7], String.format("%s-%s", runsPerWicketA[0][7], runsPerWicketA[1][7]));
        map.put("P:" + cpAKey[8], String.format("%s-%s", runsPerWicketA[0][8], runsPerWicketA[1][8]));
        map.put("P:" + cpAKey[9], String.format("%s-%s", runsPerWicketA[0][9], runsPerWicketA[1][9]));
        map.put("P:" + cpAKey[10], String.format("%s-%s", runsPerWicketA[0][10], runsPerWicketA[1][10]));
        map.put("P:" + cpBKey[0], String.format("%s-%s", runsPerWicketB[0][0], runsPerWicketB[1][0]));
        map.put("P:" + cpBKey[1], String.format("%s-%s", runsPerWicketB[0][1], runsPerWicketB[1][1]));
        map.put("P:" + cpBKey[2], String.format("%s-%s", runsPerWicketB[0][2], runsPerWicketB[1][2]));
        map.put("P:" + cpBKey[3], String.format("%s-%s", runsPerWicketB[0][3], runsPerWicketB[1][3]));
        map.put("P:" + cpBKey[4], String.format("%s-%s", runsPerWicketB[0][4], runsPerWicketB[1][4]));
        map.put("P:" + cpBKey[5], String.format("%s-%s", runsPerWicketB[0][5], runsPerWicketB[1][5]));
        map.put("P:" + cpBKey[6], String.format("%s-%s", runsPerWicketB[0][6], runsPerWicketB[1][6]));
        map.put("P:" + cpBKey[7], String.format("%s-%s", runsPerWicketB[0][7], runsPerWicketB[1][7]));
        map.put("P:" + cpBKey[8], String.format("%s-%s", runsPerWicketB[0][8], runsPerWicketB[1][8]));
        map.put("P:" + cpBKey[9], String.format("%s-%s", runsPerWicketB[0][9], runsPerWicketB[1][9]));
        map.put("P:" + cpBKey[10], String.format("%s-%s", runsPerWicketB[0][10], runsPerWicketB[1][10]));
        return map;
    }

    /**
     * Returns next player No
     * 
     * @param cp sets players name
     * @param player sets current player name
     * @return playerNo
     */
    @JsonIgnore
    private int getCricketPlayerNo(String[] cp, String player) {
        int playerNo = 0;
        for (int i = 0; i < cp.length; i++) {
            if (player.equals(cp[i])) {
                playerNo = i;
                return playerNo;
            }
        }
        return playerNo;
    }

    /**
     * Returns next player No
     * 
     * @param teamId sets players team
     * @return playerNo
     */
    @JsonIgnore
    public int[] getCricketPlayerNo(TeamId teamId) {
        int[] playerNo = {-1, -1};
        if (teamId.equals(TeamId.A)) {
            playerNo[0] = getCricketPlayerNo(this.cpAKey, onFieldPlayerName[0]);
            playerNo[1] = getCricketPlayerNo(this.cpAKey, onFieldPlayerName[1]);
        } else {
            playerNo[0] = getCricketPlayerNo(this.cpBKey, onFieldPlayerName[0]);
            playerNo[1] = getCricketPlayerNo(this.cpBKey, onFieldPlayerName[1]);
        }
        return playerNo;
    }

    /**
     * 
     * 
     * @param map sets null
     * 
     */
    @JsonIgnore
    @Override
    public String setFromMap(Map<String, String> map) {
        /*
         * do nothing - not allowing the user to manually update the score
         */
        return null;
    }

    /**
     * Returns if the inning is ended
     * 
     * @return true or false
     */
    @JsonIgnore
    public boolean isInningEnded() {
        if (wicket == 10) {
            return true;
        }

        return false;
    }

    /**
     * Returns if the match is completed
     * 
     * @return true or false
     */
    @JsonIgnore
    @Override
    public boolean isMatchCompleted() {
        if (inningsA == 2 && inningsB == 3) {
            if (runsA > runsB)
                return true;
        }
        if (inningsA == 3 && inningsB == 2) {
            if (runsA < runsB)
                return true;
        }
        if (inningsA > 2 && inningsB > 2)
            return true;
        else
            return false;
    }

    /**
     * Returns if the match is finished
     * 
     * @return true or false
     */
    @JsonIgnore
    public boolean isMatchFinished() {
        return (currentMatchState.getcricketMatchIncidentResultType() == CricketMatchIncidentResultType.MATCHWON)
                        || (currentMatchState
                                        .getcricketMatchIncidentResultType() == CricketMatchIncidentResultType.DRAW);
    }

    /**
     * Returns the server based on the current server
     * 
     * @param currentServer current serve
     * @return newServer
     * 
     */
    @JsonIgnore
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
     * Returns the bat based on the current bat
     * 
     * @param batPlayer current bat
     * @return batPlayer
     * 
     */
    @JsonIgnore
    private boolean switchBat(boolean batPlayer) {
        return !batPlayer;
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
     * Returns current game period
     * 
     * @return gamePeriod
     */
    @JsonIgnore
    @Override
    public GamePeriod getGamePeriod() {
        GamePeriod gamePeriod = null;
        switch (currentMatchState.getcricketMatchIncidentResultType()) {
            case PREMATCH:
                gamePeriod = GamePeriod.PREMATCH;
                break;
            case MATCHWON:
            case DRAW:
            case MATCH_FINISHED:
            case MATCH_COMPLETED:
                gamePeriod = GamePeriod.POSTMATCH;
                break;
            case BAT:
            case NORUNS:
            case RUN1:
            case RUN2:
            case RUN3:
            case RUN4:
            case RUN5:
            case RUN6:
            case WICKET_CAUGHT:
            case WICKET_RUN_OUT_AND_RUN:
            case EXTRAS:
            case WICKET_BOWLED:
            case WICKET_LBW:
            case WICKET_RUN_OUT:
            case WICKET_STUMPED:
            case WICKET_OTHER:
            case DECLARATION:
            case DAY:
            case SWITCHBATMAN:
            case FOLLOW_ON:
                gamePeriod = GamePeriod.PERIOD1;
                break;
            default:
                break;
        }
        return gamePeriod;
    }

    /**
     * Returns a new int[][] array with same No
     * 
     * @param array input array
     * @return copyString
     */
    @JsonIgnore
    public int[][] copy(final int[][] array) {
        if (array != null) {
            final int[][] copy = new int[array.length][];

            for (int i = 0; i < array.length; i++) {
                final int[] row = array[i];

                copy[i] = new int[row.length];
                System.arraycopy(row, 0, copy[i], 0, row.length);
            }

            return copy;
        }

        return null;
    }

    /**
     * Returns a new array with same string
     * 
     * @param array input array
     * @return copyString
     */
    @JsonIgnore
    public String[] copyString(final String[] array) {
        if (array != null) {
            final String[] copy = new String[array.length];

            for (int i = 0; i < array.length; i++) {
                if (array[i] == "") {
                } else {
                    copy[i] = array[i];
                }
            }
            return copy;
        }
        return null;
    }

    /**
     * Returns true if in preMatch
     * 
     * @return true or false
     */
    @JsonIgnore
    public boolean isPreMatch() {
        return getBat() == TeamId.UNKNOWN;
    }

    /**
     * Returns true if in preMatch
     * 
     * @return true or false
     */
    @Override
    public boolean preMatch() {
        return (isPreMatch());
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

    /**
     * Returns the sequence id to use for match based markets
     * 
     * @return "M"
     */
    @JsonIgnore
    public String getSequenceIdForMatch() {
        return "M";
    }

    @Override
    public SimpleMatchState generateSimpleMatchState() {
        TestCricketSimpleMatchState simpleMatchState = new TestCricketSimpleMatchState(preMatch(), isMatchCompleted(),
                        getBall(), getBallsA(), getBallsB(), getExtra(), getExtrasA(), getExtrasB(), getOversA(),
                        getOversB(), getRunsA(), getRunsB(), getWicket(), getWicketsA(), getWicketsB(), getInningsA(),
                        getInningsB(), getBat());
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
