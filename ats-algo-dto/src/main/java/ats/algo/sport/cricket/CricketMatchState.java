package ats.algo.sport.cricket;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ats.algo.core.GamePeriod;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchIncidentPrompt;
import ats.algo.core.baseclasses.MatchIncidentResult;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.TeamId;
import ats.algo.sport.cricket.CricketMatchIncident.CricketMatchIncidentType;
import ats.algo.sport.cricket.CricketMatchIncidentResult.CricketMatchIncidentResultType;

public class CricketMatchState extends MatchState {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private CricketMatchFormat matchFormat;
    private int oversA;
    private int oversB;
    private int ballsA;
    private int ballsB;
    private int ball;
    private int wicket;
    private int inning;
    private TeamId bat;
    private int runsA;
    private int runsB;
    private int wicketsA;
    private int wicketsB;
    private int extrasA;
    private int extrasB;
    private int extra;
    private int[] runsInOverNA;
    private int[] runsInOverNB;
    @JsonIgnore
    private int[] runsPerWicketA;
    @JsonIgnore
    private int[] runsPerWicketB;
    @JsonIgnore
    private int adjustScore;
    private boolean freeHit;
    private boolean powerPlay;
    private int powerPlayNo;
    private boolean duckWorthLewis;
    private String duckWorthLewisScore;

    // private CricketPlayer cricketPlayer;
    private CricketMatchIncidentResult currentMatchState; // the state
                                                          // following the

    /**
     * @param wicketsA set Team A wicket No
     */
    public void setWicketsA(int wicketsA) {
        this.wicketsA = wicketsA;
    }

    /**
     * Returns how many adjust scores
     * 
     * @return adjustScore
     */
    public int getAdjustScore() {
        return adjustScore;
    }

    /**
     * @param adjustScore sets how many adjust scores
     */
    public void setAdjustScore(int adjustScore) {
        this.adjustScore = adjustScore;
    }

    /**
     * Returns if duck worth lewis method has been activated
     * 
     * @return duckWorthLewis
     */
    public boolean isDuckWorthLewis() {
        return duckWorthLewis;
    }

    /**
     * @param duckWorthLewis sets if duck worth lewis method has been activated
     */
    public void setDuckWorthLewis(boolean duckWorthLewis) {
        this.duckWorthLewis = duckWorthLewis;
    }

    /**
     * Returns the duck worth lewis score format (115:7)
     * 
     * @return duckWorthLewisScore
     */
    public String getDuckWorthLewisScore() {
        return duckWorthLewisScore;
    }

    /**
     * @param duckWorthLewisScore sets the duck worth lewis score format (115:7)
     */
    public void setDuckWorthLewisScore(String duckWorthLewisScore) {
        this.duckWorthLewisScore = duckWorthLewisScore;
    }

    /**
     * Returns how many power play overs No
     * 
     * @return powerPlayNo
     */
    public int getPowerPlayNo() {
        return powerPlayNo;
    }

    /**
     * @param powerPlayNo sets how many power play overs No
     */
    public void setPowerPlayNo(int powerPlayNo) {
        this.powerPlayNo = powerPlayNo;
    }

    /**
     * Returns if the over in power play
     * 
     * @return powerPlay
     */
    public boolean isPowerPlay() {
        return powerPlay;
    }

    /**
     * @param powerPlay sets if the over in power play
     */
    public void setPowerPlay(boolean powerPlay) {
        this.powerPlay = powerPlay;
    }

    /**
     * Returns the current inning No
     * 
     * @return inning
     */
    public int getInning() {
        return inning;
    }

    /**
     * @param inning sets the current inning No
     */
    public void setInning(int inning) {
        this.inning = inning;
    }

    /**
     * Returns if the next hit is free hit
     * 
     * @return freeHit
     */
    public boolean isFreeHit() {
        return freeHit;
    }

    /**
     * @param freeHit sets if the next hit is free hit
     */
    public void setFreeHit(boolean freeHit) {
        this.freeHit = freeHit;
    }

    /**
     * 
     * @param wicketsB sets team b wicket No
     */
    public void setWicketsB(int wicketsB) {
        this.wicketsB = wicketsB;
    }

    /**
     * Return the team A wicket No
     * 
     * @return wicketsA
     */
    public int getWicketsA() {
        return wicketsA;
    }

    /**
     * Return the team B wicket No
     * 
     * @return wicketsB
     */
    public int getWicketsB() {
        return wicketsB;
    }

    /**
     * Returns team A/B current wicket No
     * 
     * @return wicket
     * 
     */
    public int getWicket() {
        return wicket;
    }

    /**
     * @param wicket sets team A/B current wicket No
     */
    public void setWicket(int wicket) {
        this.wicket = wicket;
    }

    /**
     * @param ball sets team A/B current ball No
     */
    public void setBall(int ball) {
        this.ball = ball;
    }

    /**
     * Returns team A/B current ball No
     * 
     * @return ball
     */
    public int getBall() {
        return ball;
    }

    /**
     * @param oversA sets team A current over No
     */
    public void setOversA(int oversA) {
        this.oversA = oversA;
    }

    /**
     * @param oversB sets team A current over No
     */
    public void setOversB(int oversB) {
        this.oversB = oversB;
    }

    /**
     * Returns team A current Over No
     * 
     * @return oversA
     */
    public int getOversA() {
        return oversA;
    }

    /**
     * Returns team B current Over No
     * 
     * @return oversB
     */
    public int getOversB() {
        return oversB;
    }

    /**
     * Returns team A current ball No
     * 
     * @return ballsA
     */
    public int getBallsA() {
        return ballsA;
    }

    /**
     * Returns team B current ball No
     * 
     * @return ballsB
     */
    public int getBallsB() {
        return ballsB;
    }

    /**
     * @param ballsA sets team A current ball No
     */
    public void setBallsA(int ballsA) {
        this.ballsA = ballsA;
    }

    /**
     * 
     * @param ballsB sets team B current ball No
     */
    public void setBallsB(int ballsB) {
        this.ballsB = ballsB;
    }

    /**
     * 
     * @param runsA sets team A current run No
     */
    public void setRunsA(int runsA) {
        this.runsA = runsA;
    }

    /**
     * @param runsB sets team B current run No
     */
    public void setRunsB(int runsB) {
        this.runsB = runsB;
    }

    /**
     * Returns team A current run No
     * 
     * @return runsA
     */
    public int getRunsA() {
        return runsA;
    }

    /**
     * Returns team B current run No
     * 
     * @return runsB
     */
    public int getRunsB() {
        return runsB;
    }

    /**
     * @param bat sets team A/B current bat No
     */
    public void setBat(TeamId bat) {
        this.bat = bat;
    }

    /**
     * Returns team A/B current bat No
     * 
     * @return bat
     */
    public TeamId getBat() {
        return bat;
    }

    /**
     * Returns team A/B current extra No
     * 
     * @return extra
     */
    public int getExtra() {
        return extra;
    }

    /**
     * @param extra sets team A/B current extra No
     */
    public void setExtra(int extra) {
        this.extra = extra;
    }

    /**
     * Returns team A current extra No
     * 
     * @return extrasA
     */
    public int getExtrasA() {
        return extrasA;
    }

    /**
     * Returns team B current extra No
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
     * Returns int[] to contain team A runs in each over
     * 
     * @return runsInOverNA
     */
    public int[] getRunsInOverNA() {
        return runsInOverNA;
    }

    /**
     * @param runsInOverNA Sets int[] to contain team A runs in each over
     */
    public void setRunsInOverNA(int[] runsInOverNA) {
        this.runsInOverNA = runsInOverNA;
    }

    /**
     * Returns int[] to contain team B runs in each over
     * 
     * @return runsInOverNB
     */
    public int[] getRunsInOverNB() {
        return runsInOverNB;
    }

    /**
     * @param runsInOverNB sets int[] to contain team B runs in each over
     */
    public void setRunsInOverNB(int[] runsInOverNB) {
        this.runsInOverNB = runsInOverNB;
    }

    /**
     * Returns int[] to contain team A wicket in each over
     * 
     * @return runsPerWicketA
     */
    public int[] getRunsPerWicketA() {
        return runsPerWicketA;
    }

    /**
     * @param runsPerWicketA sets int[] to contain team A wicket in each over
     */
    public void setRunsPerWicketA(int[] runsPerWicketA) {
        this.runsPerWicketA = runsPerWicketA;
    }

    /**
     * Returns int[] to contain team B wicket in each over
     * 
     * @return runsPerWicketB
     */
    public int[] getRunsPerWicketB() {
        return runsPerWicketB;
    }

    /**
     * @param runsPerWicketB sets int[] to contain team B wicket in each over
     */
    public void setRunsPerWicketB(int[] runsPerWicketB) {
        this.runsPerWicketB = runsPerWicketB;
    }

    /**
     * sets the starting score. Starting points assumed to be zero
     * 
     * @param oversA sets oversA
     * @param oversB sets oversB
     * @param runsA Sets runsA
     * @param runsB Sets runsB
     * @param ballsA Sets ballsA
     * @param ballsB Sets ballsB
     * @param wicketsA Sets wicketsA
     * @param wicketsB Sets wicketsB
     * @param extrasA Sets extrasA
     * @param extrasB Sets extrasB
     * @param onBatNow may be A,B or unknown
     */
    public void setScore(int oversA, int oversB, int runsA, int runsB, int ballsA, int ballsB, int wicketsA,
                    int wicketsB, int extrasA, int extrasB, TeamId onBatNow) {
        this.oversA = oversA;
        this.oversB = oversB;
        this.runsA = runsA;
        this.runsB = runsB;
        this.ballsA = ballsA;
        this.ballsB = ballsB;
        this.wicketsA = wicketsA;
        this.wicketsB = wicketsB;
        this.extrasA = extrasA;
        this.extrasB = extrasB;
        this.bat = onBatNow;
    }

    /**
     * Returns the current match state as cricket match incident result
     * 
     * @return currentMatchState
     */
    protected CricketMatchIncidentResult getCurrentMatchState() {
        return currentMatchState;
    }

    /**
     * 
     * 
     * @param currentMatchState sets current cricket match state
     * 
     */
    protected void setCurrentMatchState(CricketMatchIncidentResult currentMatchState) {
        this.currentMatchState = currentMatchState;
    }

    public CricketMatchState() {
        this(new CricketMatchFormat());
    }

    public CricketMatchState(MatchFormat matchFormat) {
        super();
        this.matchFormat = (CricketMatchFormat) matchFormat;
        runsA = 0;
        runsB = 0;
        ballsA = 0;
        ballsB = 0;
        oversA = 1;
        oversB = 1;
        bat = TeamId.UNKNOWN;
        ball = 0;
        wicket = 0;
        wicketsA = 0;
        wicketsB = 0;
        extrasA = 0;
        extrasB = 0;
        extra = 0;
        inning = 1;
        freeHit = false;
        powerPlay = false;
        duckWorthLewis = false;
        duckWorthLewisScore = "";
        powerPlayNo = 0;
        adjustScore = 0;
        setScore(oversA, oversB, runsA, runsB, ballsA, ballsB, wicketsA, wicketsB, extrasA, extrasB, TeamId.UNKNOWN);
        runsInOverNA = new int[((CricketMatchFormat) matchFormat).getnOversinMatch()];
        runsInOverNB = new int[((CricketMatchFormat) matchFormat).getnOversinMatch()];
        for (int i = 0; i < runsInOverNA.length - 1; i++) {
            runsInOverNA[i] = 0;
        }

        for (int i = 0; i < runsInOverNA.length - 1; i++) {
            runsInOverNB[i] = 0;
        }
        runsPerWicketA = new int[10];
        runsPerWicketB = new int[10];
        for (int i = 0; i < runsPerWicketA.length - 1; i++) {
            runsPerWicketA[i] = 0;
        }
        for (int i = 0; i < runsPerWicketB.length - 1; i++) {
            runsPerWicketB[i] = 0;
        }

        currentMatchState = new CricketMatchIncidentResult(CricketMatchIncidentResultType.PREMATCH);
    }

    /**
     * Return Cricket MatchIncident Result after the match incident updated
     * 
     * @return matchEventResult
     */
    @Override
    public MatchIncidentResult updateStateForIncident(MatchIncident matchIncident,
                    boolean autosyncMatchStateToFeedOnMismatch) {
        CricketMatchIncidentResult matchEventResult = null;
        if (!(matchIncident instanceof CricketMatchIncident))
            return super.updateStateForIncident(matchIncident, autosyncMatchStateToFeedOnMismatch);
        else {
            CricketMatchIncident mI = (CricketMatchIncident) matchIncident;

            boolean serveA = bat == TeamId.A;
            boolean once = true;
            int wicketType = -1;
            int countExtra = 0;
            int countAdjust = 0;
            freeHit = false;
            TeamId teamId = mI.getTeamId();
            if (serveA) {
                if (oversA > powerPlayNo) {
                    powerPlay = false;
                }
            } else {
                if (oversB > powerPlayNo) {
                    powerPlay = false;
                }
            }
            switch (mI.getIncidentSubType()) {
                case BATFIRST:
                    matchEventResult = new CricketMatchIncidentResult(CricketMatchIncidentResultType.BAT, teamId);
                    bat = teamId;
                    break;
                case POWERPLAY:
                    powerPlay = true;
                    if (serveA)
                        powerPlayNo = oversA + 5;
                    else
                        powerPlayNo = oversB + 5;
                    matchEventResult = new CricketMatchIncidentResult(CricketMatchIncidentResultType.POWERPLAY, teamId);
                    break;
                case NORUNS:
                    matchEventResult = new CricketMatchIncidentResult(CricketMatchIncidentResultType.NORUNS);
                    ball++;
                    if (serveA) {
                        ballsA = ball;
                        runsInOverNA[oversA - 1] = runsA;
                        oversA = ball / 6 + 1;
                        oversA = oversA > runsInOverNA.length ? runsInOverNA.length : oversA;
                    } else {
                        ballsB = ball;
                        runsInOverNB[oversB - 1] = runsB;
                        oversB = ball / 6 + 1;
                        oversB = oversB > runsInOverNA.length ? runsInOverNA.length : oversB;
                    }
                    if (isOverEnded()) {
                        bat = switchServer(bat);
                        ball = 0;
                        wicket = 0;
                        extra = 0;
                        if (ballsA > 0 && ballsB > 0) {
                            bat = TeamId.UNKNOWN;

                            if (runsA > runsB)
                                matchEventResult = new CricketMatchIncidentResult(
                                                CricketMatchIncidentResultType.MATCHWON, TeamId.A);
                            else if (runsA < runsB)
                                matchEventResult = new CricketMatchIncidentResult(
                                                CricketMatchIncidentResultType.MATCHWON, TeamId.B);
                            else
                                matchEventResult = new CricketMatchIncidentResult(CricketMatchIncidentResultType.DRAW);
                        }
                    }
                    break;
                case RUN1:
                    matchEventResult = new CricketMatchIncidentResult(CricketMatchIncidentResultType.RUN1);
                    ball++;
                    if (serveA) {
                        ballsA = ball;
                        runsA += 1;
                        runsInOverNA[oversA - 1] = runsA;
                        oversA = ball / 6 + 1;
                        oversA = oversA > runsInOverNA.length ? runsInOverNA.length : oversA;
                    } else {
                        ballsB = ball;
                        runsB += 1;
                        runsInOverNB[oversB - 1] = runsB;
                        oversB = ball / 6 + 1;
                        oversB = oversB > runsInOverNA.length ? runsInOverNA.length : oversB;
                    }
                    if (isOverEnded()) {
                        bat = switchServer(bat);
                        ball = 0;
                        wicket = 0;
                        extra = 0;
                        if (ballsA > 0 && ballsB > 0) {
                            bat = TeamId.UNKNOWN;
                            if (runsA > runsB)
                                matchEventResult = new CricketMatchIncidentResult(
                                                CricketMatchIncidentResultType.MATCHWON, TeamId.A);
                            else if (runsA < runsB)
                                matchEventResult = new CricketMatchIncidentResult(
                                                CricketMatchIncidentResultType.MATCHWON, TeamId.B);
                            else
                                matchEventResult = new CricketMatchIncidentResult(CricketMatchIncidentResultType.DRAW);
                        }
                    }
                    break;
                case RUN2:
                    matchEventResult = new CricketMatchIncidentResult(CricketMatchIncidentResultType.RUN2);
                    ball++;
                    if (serveA) {
                        ballsA = ball;
                        runsA += 2;
                        runsInOverNA[oversA - 1] = runsA;
                        oversA = ball / 6 + 1;
                        oversA = oversA > runsInOverNA.length ? runsInOverNA.length : oversA;
                    } else {
                        ballsB = ball;
                        runsB += 2;
                        runsInOverNB[oversB - 1] = runsB;
                        oversB = ball / 6 + 1;
                        oversB = oversB > runsInOverNA.length ? runsInOverNA.length : oversB;
                    }
                    if (isOverEnded()) {
                        bat = switchServer(bat);
                        ball = 0;
                        wicket = 0;
                        extra = 0;
                        if (ballsA > 0 && ballsB > 0) {
                            bat = TeamId.UNKNOWN;
                            if (runsA > runsB)
                                matchEventResult = new CricketMatchIncidentResult(
                                                CricketMatchIncidentResultType.MATCHWON, TeamId.A);
                            else if (runsA < runsB)
                                matchEventResult = new CricketMatchIncidentResult(
                                                CricketMatchIncidentResultType.MATCHWON, TeamId.B);
                            else
                                matchEventResult = new CricketMatchIncidentResult(CricketMatchIncidentResultType.DRAW);
                        }
                    }
                    break;
                case RUN3:
                    matchEventResult = new CricketMatchIncidentResult(CricketMatchIncidentResultType.RUN3);
                    ball++;
                    if (serveA) {
                        ballsA = ball;
                        runsA += 3;
                        runsInOverNA[oversA - 1] = runsA;
                        oversA = ball / 6 + 1;
                        oversA = oversA > runsInOverNA.length ? runsInOverNA.length : oversA;
                    } else {
                        ballsB = ball;
                        runsB += 3;
                        runsInOverNB[oversB - 1] = runsB;
                        oversB = ball / 6 + 1;
                        oversB = oversB > runsInOverNA.length ? runsInOverNA.length : oversB;
                    }
                    if (isOverEnded()) {
                        bat = switchServer(bat);
                        ball = 0;
                        wicket = 0;
                        extra = 0;
                        if (ballsA > 0 && ballsB > 0) {
                            bat = TeamId.UNKNOWN;
                            if (runsA > runsB)
                                matchEventResult = new CricketMatchIncidentResult(
                                                CricketMatchIncidentResultType.MATCHWON, TeamId.A);
                            else if (runsA < runsB)
                                matchEventResult = new CricketMatchIncidentResult(
                                                CricketMatchIncidentResultType.MATCHWON, TeamId.B);
                            else
                                matchEventResult = new CricketMatchIncidentResult(CricketMatchIncidentResultType.DRAW);
                        }
                    }
                    break;
                case RUN4:
                    matchEventResult = new CricketMatchIncidentResult(CricketMatchIncidentResultType.RUN4);
                    ball++;
                    if (serveA) {
                        ballsA = ball;
                        runsA += 4;
                        runsInOverNA[oversA - 1] = runsA;
                        oversA = ball / 6 + 1;
                        oversA = oversA > runsInOverNA.length ? runsInOverNA.length : oversA;
                    } else {
                        ballsB = ball;
                        runsB += 4;
                        runsInOverNB[oversB - 1] = runsB;
                        oversB = ball / 6 + 1;
                        oversB = oversB > runsInOverNA.length ? runsInOverNA.length : oversB;
                    }
                    if (isOverEnded()) {
                        bat = switchServer(bat);
                        ball = 0;
                        wicket = 0;
                        extra = 0;
                        if (ballsA > 0 && ballsB > 0) {
                            bat = TeamId.UNKNOWN;
                            if (runsA > runsB)
                                matchEventResult = new CricketMatchIncidentResult(
                                                CricketMatchIncidentResultType.MATCHWON, TeamId.A);
                            else if (runsA < runsB)
                                matchEventResult = new CricketMatchIncidentResult(
                                                CricketMatchIncidentResultType.MATCHWON, TeamId.B);
                            else
                                matchEventResult = new CricketMatchIncidentResult(CricketMatchIncidentResultType.DRAW);
                        }
                    }
                    break;
                case RUN5:
                    matchEventResult = new CricketMatchIncidentResult(CricketMatchIncidentResultType.RUN5);
                    ball++;
                    if (serveA) {
                        ballsA = ball;
                        runsA += 5;
                        runsInOverNA[oversA - 1] = runsA;
                        oversA = ball / 6 + 1;
                        oversA = oversA > runsInOverNA.length ? runsInOverNA.length : oversA;
                    } else {
                        ballsB = ball;
                        runsB += 5;
                        runsInOverNB[oversB - 1] = runsB;
                        oversB = ball / 6 + 1;
                        oversB = oversB > runsInOverNA.length ? runsInOverNA.length : oversB;
                    }
                    if (isOverEnded()) {
                        bat = switchServer(bat);
                        ball = 0;
                        wicket = 0;
                        extra = 0;
                        if (ballsA > 0 && ballsB > 0) {
                            bat = TeamId.UNKNOWN;
                            if (runsA > runsB)
                                matchEventResult = new CricketMatchIncidentResult(
                                                CricketMatchIncidentResultType.MATCHWON, TeamId.A);
                            else if (runsA < runsB)
                                matchEventResult = new CricketMatchIncidentResult(
                                                CricketMatchIncidentResultType.MATCHWON, TeamId.B);
                            else
                                matchEventResult = new CricketMatchIncidentResult(CricketMatchIncidentResultType.DRAW);
                        }
                    }
                    break;
                case RUN6:
                    matchEventResult = new CricketMatchIncidentResult(CricketMatchIncidentResultType.RUN6);
                    ball++;
                    if (serveA) {
                        ballsA = ball;
                        runsA += 6;
                        runsInOverNA[oversA - 1] = runsA;
                        oversA = ball / 6 + 1;
                        oversA = oversA > runsInOverNA.length ? runsInOverNA.length : oversA;
                    } else {
                        ballsB = ball;
                        runsB += 6;
                        runsInOverNB[oversB - 1] = runsB;
                        oversB = ball / 6 + 1;
                        oversB = oversB > runsInOverNA.length ? runsInOverNA.length : oversB;
                    }
                    if (isOverEnded()) {
                        bat = switchServer(bat);
                        ball = 0;
                        wicket = 0;
                        extra = 0;
                        if (ballsA > 0 && ballsB > 0) {
                            bat = TeamId.UNKNOWN;
                            if (runsA > runsB)
                                matchEventResult = new CricketMatchIncidentResult(
                                                CricketMatchIncidentResultType.MATCHWON, TeamId.A);
                            else if (runsA < runsB)
                                matchEventResult = new CricketMatchIncidentResult(
                                                CricketMatchIncidentResultType.MATCHWON, TeamId.B);
                            else
                                matchEventResult = new CricketMatchIncidentResult(CricketMatchIncidentResultType.DRAW);
                        }
                    }
                    break;

                case WICKET_CAUGHT:
                    if (once) {
                        matchEventResult = new CricketMatchIncidentResult(CricketMatchIncidentResultType.WICKET_CAUGHT);
                        once = false;
                        wicketType = 0;
                    }
                case WICKET_BOWLED:
                    if (once) {
                        matchEventResult = new CricketMatchIncidentResult(CricketMatchIncidentResultType.WICKET_BOWLED);
                        once = false;
                        wicketType = 1;
                    }
                case WICKET_LBW:
                    if (once) {
                        matchEventResult = new CricketMatchIncidentResult(CricketMatchIncidentResultType.WICKET_LBW);
                        once = false;
                        wicketType = 2;
                    }
                case WICKET_OTHER:
                    if (once) {
                        matchEventResult = new CricketMatchIncidentResult(CricketMatchIncidentResultType.WICKET_OTHER);
                        once = false;
                        wicketType = 5;
                    }
                case WICKET_RUN_OUT:
                    if (once) {
                        matchEventResult =
                                        new CricketMatchIncidentResult(CricketMatchIncidentResultType.WICKET_RUN_OUT);
                        once = false;
                        wicketType = 3;
                    }
                case WICKET_STUMPED:
                    if (once) {
                        matchEventResult =
                                        new CricketMatchIncidentResult(CricketMatchIncidentResultType.WICKET_STUMPED);
                        once = false;
                        wicketType = 4;
                    }
                case WICKET_RUN_OUT_AND_RUN6:
                    if (once) {
                        once = false;
                        matchEventResult = new CricketMatchIncidentResult(
                                        CricketMatchIncidentResultType.WICKET_RUN_OUT_AND_RUN);
                        wicketType = 3;
                        if (serveA)
                            runsA += 6;
                        else
                            runsB += 6;
                    }
                case WICKET_RUN_OUT_AND_RUN5:
                    if (once) {
                        once = false;
                        matchEventResult = new CricketMatchIncidentResult(
                                        CricketMatchIncidentResultType.WICKET_RUN_OUT_AND_RUN);
                        wicketType = 3;
                        if (serveA)
                            runsA += 5;
                        else
                            runsB += 5;
                    }
                case WICKET_RUN_OUT_AND_RUN4:
                    if (once) {
                        once = false;
                        matchEventResult = new CricketMatchIncidentResult(
                                        CricketMatchIncidentResultType.WICKET_RUN_OUT_AND_RUN);
                        wicketType = 3;
                        if (serveA)
                            runsA += 4;
                        else
                            runsB += 4;
                    }
                case WICKET_RUN_OUT_AND_RUN3:
                    if (once) {
                        once = false;
                        matchEventResult = new CricketMatchIncidentResult(
                                        CricketMatchIncidentResultType.WICKET_RUN_OUT_AND_RUN);
                        wicketType = 3;
                        if (serveA)
                            runsA += 3;
                        else
                            runsB += 3;
                    }
                case WICKET_RUN_OUT_AND_RUN2:
                    if (once) {
                        once = false;
                        matchEventResult = new CricketMatchIncidentResult(
                                        CricketMatchIncidentResultType.WICKET_RUN_OUT_AND_RUN);
                        wicketType = 3;
                        if (serveA)
                            runsA += 2;
                        else
                            runsB += 2;
                    }
                case WICKET_RUN_OUT_AND_RUN1:
                    if (once) {
                        matchEventResult = new CricketMatchIncidentResult(
                                        CricketMatchIncidentResultType.WICKET_RUN_OUT_AND_RUN);
                        once = false;
                        wicketType = 3;
                        if (serveA)
                            runsA += 1;
                        else
                            runsB += 1;
                    }
                    wicket++;
                    ball++;
                    if (serveA) {
                        ballsA = ball;
                        runsInOverNA[oversA - 1] = runsA;
                        if (wicket == 1)
                            runsPerWicketA[0] = runsA;
                        runsPerWicketA[wicket - 1] = runsA;
                        for (int i = wicket - 1; i > 0; i--) {
                            runsPerWicketA[wicket - 1] = runsPerWicketA[wicket - 1] - runsPerWicketA[i - 1];
                        }

                        oversA = ball / 6 + 1;
                        wicketsA = wicket;
                        oversA = oversA > runsInOverNA.length ? runsInOverNA.length : oversA;
                        matchEventResult.setRuns(runsPerWicketA[wicket - 1]);
                        matchEventResult.setWicket(wicket - 1);
                    } else {
                        ballsB = ball;
                        runsInOverNB[oversB - 1] = runsB;
                        if (wicket == 1)
                            runsPerWicketB[0] = runsB;
                        runsPerWicketB[wicket - 1] = runsB;
                        for (int i = wicket - 1; i > 0; i--)
                            runsPerWicketB[wicket - 1] = runsPerWicketB[wicket - 1] - runsPerWicketB[i - 1];
                        oversB = ball / 6 + 1;
                        wicketsB = wicket;
                        oversB = oversB > runsInOverNA.length ? runsInOverNA.length : oversB;
                        matchEventResult.setRuns(runsPerWicketB[wicket - 1]);
                        matchEventResult.setWicket(wicket - 1);
                    }
                    matchEventResult.setTeamId(bat);
                    if (isOverEnded()) {
                        bat = switchServer(bat);
                        ball = 0;
                        wicket = 0;
                        extra = 0;
                        if (ballsA > 0 && ballsB > 0) {
                            bat = TeamId.UNKNOWN;
                            if (runsA > runsB)
                                matchEventResult = new CricketMatchIncidentResult(true, wicketType,
                                                CricketMatchIncidentResultType.MATCHWON, TeamId.A);
                            else if (runsA < runsB)
                                matchEventResult = new CricketMatchIncidentResult(true, wicketType,
                                                CricketMatchIncidentResultType.MATCHWON, TeamId.B);
                            else
                                matchEventResult = new CricketMatchIncidentResult(CricketMatchIncidentResultType.DRAW);
                            if (wicketType != -1) {
                                matchEventResult.setLastPointWicket(true);
                                matchEventResult.setWicketType(wicketType);
                            }
                        }
                    }
                    break;
                case EXTRAS6:
                    matchEventResult = new CricketMatchIncidentResult(CricketMatchIncidentResultType.EXTRAS);
                    countExtra++;
                case EXTRAS5:
                    matchEventResult = new CricketMatchIncidentResult(CricketMatchIncidentResultType.EXTRAS);
                    countExtra++;
                case EXTRAS4:
                    matchEventResult = new CricketMatchIncidentResult(CricketMatchIncidentResultType.EXTRAS);
                    countExtra++;
                case EXTRAS3:
                    matchEventResult = new CricketMatchIncidentResult(CricketMatchIncidentResultType.EXTRAS);
                    countExtra++;
                case EXTRAS2:
                    matchEventResult = new CricketMatchIncidentResult(CricketMatchIncidentResultType.EXTRAS);
                    countExtra++;
                case EXTRAS1:
                    matchEventResult = new CricketMatchIncidentResult(CricketMatchIncidentResultType.EXTRAS);
                    countExtra++;
                case EXTRAS:
                    matchEventResult = new CricketMatchIncidentResult(CricketMatchIncidentResultType.EXTRAS);
                    extra += countExtra;
                    if (serveA) {
                        extrasA = extra;
                        ballsA = ball;
                        runsA = runsA + countExtra;
                        runsInOverNA[oversA - 1] = runsA;
                        oversA = ball / 6 + 1;
                        oversA = oversA > runsInOverNA.length ? runsInOverNA.length : oversA;
                    } else {
                        extrasB = extra;
                        ballsB = ball;
                        runsB = runsB + countExtra;
                        runsInOverNB[oversB - 1] = runsB;
                        oversB = ball / 6 + 1;
                        oversB = oversB > runsInOverNA.length ? runsInOverNA.length : oversB;
                    }
                    if (isOverEnded()) {
                        bat = switchServer(bat);
                        ball = 0;
                        wicket = 0;
                        extra = 0;
                        if (ballsA > 0 && ballsB > 0) {
                            bat = TeamId.UNKNOWN;
                            if (runsA > runsB)
                                matchEventResult = new CricketMatchIncidentResult(
                                                CricketMatchIncidentResultType.MATCHWON, TeamId.A);

                            else if (runsA < runsB)
                                matchEventResult = new CricketMatchIncidentResult(
                                                CricketMatchIncidentResultType.MATCHWON, TeamId.B);
                            else
                                matchEventResult = new CricketMatchIncidentResult(CricketMatchIncidentResultType.DRAW);
                        }
                    }
                    break;
                case FREEHIT7:
                    matchEventResult = new CricketMatchIncidentResult(CricketMatchIncidentResultType.FREEHIT);
                    countExtra++;
                case FREEHIT6:
                    matchEventResult = new CricketMatchIncidentResult(CricketMatchIncidentResultType.FREEHIT);
                    countExtra++;
                case FREEHIT5:
                    matchEventResult = new CricketMatchIncidentResult(CricketMatchIncidentResultType.FREEHIT);
                    countExtra++;
                case FREEHIT4:
                    matchEventResult = new CricketMatchIncidentResult(CricketMatchIncidentResultType.FREEHIT);
                    countExtra++;
                case FREEHIT3:
                    matchEventResult = new CricketMatchIncidentResult(CricketMatchIncidentResultType.FREEHIT);
                    countExtra++;
                case FREEHIT2:
                    matchEventResult = new CricketMatchIncidentResult(CricketMatchIncidentResultType.FREEHIT);
                    countExtra++;
                case FREEHIT1:
                    matchEventResult = new CricketMatchIncidentResult(CricketMatchIncidentResultType.FREEHIT);
                    countExtra++;
                    extra += countExtra;
                    freeHit = true;
                    if (serveA) {
                        extrasA = extra;
                        ballsA = ball;
                        runsA = runsA + countExtra;
                        runsInOverNA[oversA - 1] = runsA;
                        oversA = ball / 6 + 1;
                        oversA = oversA > runsInOverNA.length ? runsInOverNA.length : oversA;
                    } else {
                        extrasB = extra;
                        ballsB = ball;
                        runsB = runsB + countExtra;
                        runsInOverNB[oversB - 1] = runsB;
                        oversB = ball / 6 + 1;
                        oversB = oversB > runsInOverNA.length ? runsInOverNA.length : oversB;
                    }
                    if (isOverEnded()) {
                        bat = switchServer(bat);
                        ball = 0;
                        wicket = 0;
                        extra = 0;
                        if (ballsA > 0 && ballsB > 0) {
                            bat = TeamId.UNKNOWN;
                            if (runsA > runsB)
                                matchEventResult = new CricketMatchIncidentResult(
                                                CricketMatchIncidentResultType.MATCHWON, TeamId.A);

                            else if (runsA < runsB)
                                matchEventResult = new CricketMatchIncidentResult(
                                                CricketMatchIncidentResultType.MATCHWON, TeamId.B);
                            else
                                matchEventResult = new CricketMatchIncidentResult(CricketMatchIncidentResultType.DRAW);
                        }
                    }
                    break;
                case DUCKWORTHLEWIS:
                    if (!duckWorthLewis) {
                        if (serveA) {
                            runsA = Integer.valueOf(duckWorthLewisScore.split(":")[0]);
                            bat = switchServer(bat);
                            ball = 0;
                            wicket = 0;
                            extra = 0;
                        } else {
                            runsB = Integer.valueOf(duckWorthLewisScore.split(":")[0]);
                            bat = switchServer(bat);
                            ball = 0;
                            wicket = 0;
                            extra = 0;
                        }
                    } else {
                        if (serveA) {
                            runsA = Integer.valueOf(duckWorthLewisScore.split(":")[0]);
                        } else {
                            runsB = Integer.valueOf(duckWorthLewisScore.split(":")[0]);
                        }
                    }
                    matchEventResult = new CricketMatchIncidentResult(CricketMatchIncidentResultType.DUCKWORTHLEWIS);
                    break;
                case AJDUSTSCORE7:
                    countAdjust++;
                case AJDUSTSCORE6:
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
                    adjustScore = countAdjust;
                    if (serveA) {
                        runsA = runsA + adjustScore;
                    } else {
                        runsA = runsB + adjustScore;
                    }
                    matchEventResult = new CricketMatchIncidentResult(CricketMatchIncidentResultType.ADJUSTSCORE);
                    break;
                case AJDUSTBALL:
                    if (ball > 0)
                        ball--;
                    matchEventResult = new CricketMatchIncidentResult(CricketMatchIncidentResultType.ADJUSTBALL);
                    break;
                case MATCH_COMPLETED:
                    matchEventResult = new CricketMatchIncidentResult(CricketMatchIncidentResultType.MATCH_COMPLETED);
                    break;
                default:
                    break;
            }
        }

        currentMatchState = matchEventResult;
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
    public MatchState copy() {
        CricketMatchState cc = new CricketMatchState(this.matchFormat);
        cc.setEqualTo(this);
        return cc;
    }

    @Override
    public void setEqualTo(MatchState matchState) {
        CricketMatchState vs = (CricketMatchState) matchState;
        this.matchFormat = (CricketMatchFormat) vs.getMatchFormat();
        this.setBallsA(vs.getBallsA());
        this.setBallsB(vs.getBallsB());
        this.setOversA(vs.getOversA());
        this.setOversB(vs.getOversB());
        this.setBall(vs.getBall());
        this.setRunsA(vs.getRunsA());
        this.setRunsB(vs.getRunsB());
        this.setWicket(vs.getWicket());
        this.setBat(vs.getBat());
        this.setWicketsA(vs.getWicketsA());
        this.setWicketsB(vs.getWicketsB());
        this.setInning(vs.getInning());
        this.setExtra(vs.getExtra());
        this.setExtrasA(vs.getExtrasA());
        this.setExtrasB(vs.getExtrasB());
        this.setRunsInOverNA(vs.getRunsInOverNA().clone());
        this.setRunsInOverNB(vs.getRunsInOverNB().clone());
        this.setRunsPerWicketA(vs.getRunsPerWicketA().clone());
        this.setRunsPerWicketB(vs.getRunsPerWicketB().clone());
        this.setFreeHit(vs.isFreeHit());
        this.setDuckWorthLewis(vs.isDuckWorthLewis());
        this.setDuckWorthLewisScore(vs.getDuckWorthLewisScore());
        this.setAdjustScore(vs.getAdjustScore());
        this.setCurrentMatchState(vs.getCurrentMatchState());
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
                        "Enter RUNS of next BALL (R1/2/3/4/5/6/0) OR wicket method : O0 bowled, O1 caught, O2 LBW, O3 RUNOUT , O4 STUMPED,O5 OTHER, R1O3 RUNOUTANDRUN OR Extra(W1/2/3/4/5/6) and FreeHit(N1/2/3/4/5/6/7) PowerPlay (PP) Adjustscore(A1/2/3/4/5/6/7) AdjustBall(AB) DUCKWORTHLEWIS and score(DWL-100:10)";
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
                lastLegWinner = "R1";
                matchIncidentPrompt = new MatchIncidentPrompt(nextPromt, lastLegWinner);
                break;
            case RUN2:
                lastLegWinner = "R2";
                matchIncidentPrompt = new MatchIncidentPrompt(nextPromt, lastLegWinner);
                break;
            case RUN3:
                lastLegWinner = "R3";
                matchIncidentPrompt = new MatchIncidentPrompt(nextPromt, lastLegWinner);
                break;
            case RUN4:
                lastLegWinner = "R4";
                matchIncidentPrompt = new MatchIncidentPrompt(nextPromt, lastLegWinner);
                break;
            case RUN5:
                lastLegWinner = "R5";
                matchIncidentPrompt = new MatchIncidentPrompt(nextPromt, lastLegWinner);
                break;
            case RUN6:
                lastLegWinner = "R6";
                matchIncidentPrompt = new MatchIncidentPrompt(nextPromt, lastLegWinner);
                break;
            case BAT:
                lastLegWinner = "R1";
                matchIncidentPrompt = new MatchIncidentPrompt(nextPromt, lastLegWinner);
                break;
            case EXTRAS:
                lastLegWinner = "W1";
                matchIncidentPrompt = new MatchIncidentPrompt(nextPromt, lastLegWinner);
                break;
            case FREEHIT:
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
            case POWERPLAY:
                lastLegWinner = "PP";
                powerPlay = true;
                matchIncidentPrompt = new MatchIncidentPrompt(nextPromt, lastLegWinner);
                break;
            case DUCKWORTHLEWIS:
                lastLegWinner = "R0";
                matchIncidentPrompt = new MatchIncidentPrompt(nextPromt, lastLegWinner);
                break;
            case ADJUSTSCORE:
                lastLegWinner = "R0";
                matchIncidentPrompt = new MatchIncidentPrompt(nextPromt, lastLegWinner);
                break;
            case ADJUSTBALL:
                lastLegWinner = "R0";
                matchIncidentPrompt = new MatchIncidentPrompt(nextPromt, lastLegWinner);
                break;
            case DRAW:
                matchWinner = "Draw";
            case MATCHWON:
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
        CricketMatchIncident matchEvent = new CricketMatchIncident();
        TeamId teamId;
        if (response.contains("-")) {
            duckWorthLewisScore = response.split("-")[1];
            response = response.split("-")[0];
            duckWorthLewis = true;
        }
        switch (response.toUpperCase()) {
            case "R0":
                matchEvent.set(CricketMatchIncidentType.NORUNS);
                break;
            case "R1":
                matchEvent.set(CricketMatchIncidentType.RUN1);
                break;
            case "R2":
                matchEvent.set(CricketMatchIncidentType.RUN2);
                break;
            case "R3":
                matchEvent.set(CricketMatchIncidentType.RUN3);
                break;
            case "R4":
                matchEvent.set(CricketMatchIncidentType.RUN4);
                break;
            case "R5":
                matchEvent.set(CricketMatchIncidentType.RUN5);
                break;
            case "R6":
                matchEvent.set(CricketMatchIncidentType.RUN6);
                break;
            case "O0":
                matchEvent.set(CricketMatchIncidentType.WICKET_BOWLED);
                break;
            case "O1":
                matchEvent.set(CricketMatchIncidentType.WICKET_CAUGHT);
                break;
            case "O2":
                matchEvent.set(CricketMatchIncidentType.WICKET_LBW);
                break;
            case "O3":
                matchEvent.set(CricketMatchIncidentType.WICKET_RUN_OUT);
                break;
            case "O4":
                matchEvent.set(CricketMatchIncidentType.WICKET_STUMPED);
                break;
            case "O5":
                matchEvent.set(CricketMatchIncidentType.WICKET_OTHER);
                break;
            case "R1O3":
                matchEvent.set(CricketMatchIncidentType.WICKET_RUN_OUT_AND_RUN1);
                break;
            case "R2O3":
                matchEvent.set(CricketMatchIncidentType.WICKET_RUN_OUT_AND_RUN2);
                break;
            case "R3O3":
                matchEvent.set(CricketMatchIncidentType.WICKET_RUN_OUT_AND_RUN3);
                break;
            case "R4O3":
                matchEvent.set(CricketMatchIncidentType.WICKET_RUN_OUT_AND_RUN4);
                break;
            case "R5O3":
                matchEvent.set(CricketMatchIncidentType.WICKET_RUN_OUT_AND_RUN5);
                break;
            case "R6O3":
                matchEvent.set(CricketMatchIncidentType.WICKET_RUN_OUT_AND_RUN6);
                break;
            case "W1":
                matchEvent.set(CricketMatchIncidentType.EXTRAS1);
                break;
            case "W2":
                matchEvent.set(CricketMatchIncidentType.EXTRAS2);
                break;
            case "W3":
                matchEvent.set(CricketMatchIncidentType.EXTRAS3);
                break;
            case "W4":
                matchEvent.set(CricketMatchIncidentType.EXTRAS4);
                break;
            case "W5":
                matchEvent.set(CricketMatchIncidentType.EXTRAS5);
                break;
            case "W6":
                matchEvent.set(CricketMatchIncidentType.EXTRAS6);
                break;
            case "N1":
                matchEvent.set(CricketMatchIncidentType.FREEHIT1);
                break;
            case "N2":
                matchEvent.set(CricketMatchIncidentType.FREEHIT2);
                break;
            case "N3":
                matchEvent.set(CricketMatchIncidentType.FREEHIT3);
                break;
            case "N4":
                matchEvent.set(CricketMatchIncidentType.FREEHIT4);
                break;
            case "N5":
                matchEvent.set(CricketMatchIncidentType.FREEHIT5);
                break;
            case "N6":
                matchEvent.set(CricketMatchIncidentType.FREEHIT6);
                break;
            case "N7":
                matchEvent.set(CricketMatchIncidentType.FREEHIT7);
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
            case "AB":
                matchEvent.set(CricketMatchIncidentType.AJDUSTBALL);
                break;
            case "PP":
                matchEvent.set(CricketMatchIncidentType.POWERPLAY);
                break;
            case "BA":
                teamId = TeamId.A;
                matchEvent.set(CricketMatchIncidentType.BATFIRST, teamId);
                break;
            case "BB":
                teamId = TeamId.B;
                matchEvent.set(CricketMatchIncidentType.BATFIRST, teamId);
                break;
            case "MC":
                matchEvent.set(CricketMatchIncidentType.MATCH_COMPLETED);
                break;
            case "DWL":
                matchEvent.set(CricketMatchIncidentType.DUCKWORTHLEWIS);
                break;
            default:
                return null; // invalid input so return null
        }
        return matchEvent;
    }

    private static final String seScoreKey = "over score";
    private static final String baScoreKey = "ball score";
    private static final String poScoreKey = "run score";
    private static final String wiScoreKey = "wicket";
    private static final String wcScoreKey = "wicket score";
    private static final String exScoreKey = "extra score";
    private static final String onServeKey = "who is bating";
    private static final String freeHitKey = "freeHit Next ball";
    private static final String powerPlayKey = "Power Play";
    private static final String powerPlayEndKey = "Power Play End Over";
    private static final String duckWorthLewisKey = "DuckWorthLewis";
    private static final String duckWorthLewisScoreKey = "DuckWorthLewisKey Score";

    /**
     * Returns map
     * 
     * @return map
     */
    @Override
    public LinkedHashMap<String, String> getAsMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put(seScoreKey, String.format("%d-%d", oversA - 1, oversB - 1));
        map.put(baScoreKey, String.format("%d-%d", ballsA, ballsB));
        map.put(baScoreKey, String.format("%d", ball % 6));
        map.put(wiScoreKey, String.format("%d", wicket));
        map.put(poScoreKey, String.format("%d-%d", runsA, runsB));
        map.put(wcScoreKey, String.format("%d-%d", wicketsA, wicketsB));
        map.put(exScoreKey, String.format("%d-%d", extrasA, extrasB));
        map.put(onServeKey, String.format("%s", bat));
        map.put(freeHitKey, String.format("%s", freeHit));
        map.put(powerPlayKey, String.format("%s", powerPlay));
        map.put(powerPlayEndKey, String.format("%s", powerPlayNo));
        map.put(duckWorthLewisKey, String.format("%s", duckWorthLewis));
        map.put(duckWorthLewisScoreKey, duckWorthLewisScore);
        return map;
    }

    /**
     * 
     * 
     * @param map sets null
     * 
     */
    @Override
    public String setFromMap(Map<String, String> map) {
        /*
         * do nothing - not allowing the user to manually update the score
         */
        return null;
    }

    /**
     * Returns if the over is ended
     * 
     * @return true or false
     */
    @JsonIgnore
    public boolean isOverEnded() {
        if (wicket == 10) {
            return true;
        }
        if (ball == (matchFormat.getnBallsinOver() * matchFormat.getnOversinMatch()))
            return true;
        if (duckWorthLewis) {
            int duckWorthLewisOver = Integer.valueOf(duckWorthLewisScore.split(":")[1]);
            if (duckWorthLewis && (ball == (matchFormat.getnBallsinOver() * duckWorthLewisOver))) {
                return true;
            }
        }

        if (ballsA > 0 && ballsB > 0) {
            if (bat == TeamId.A) {
                return runsA > runsB;
            }
            if (bat == TeamId.B) {
                return runsA < runsB;
            }
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
        return (ballsA > 0 && ballsB > 0 && bat == TeamId.UNKNOWN);
    }

    /**
     * Returns if the match is finished
     * 
     * @return true or false
     */
    @JsonIgnore
    public boolean isMatchFinished() {
        return (ballsA > 0 && ballsB > 0 && bat == TeamId.UNKNOWN);
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
            case DUCKWORTHLEWIS:
            case ADJUSTSCORE:
            case ADJUSTBALL:
            case POWERPLAY:
                gamePeriod = GamePeriod.PERIOD1;
                break;
            default:
                break;
        }
        return gamePeriod;
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

    /**
     * Returns the sequence id to use for over based markets
     * 
     * @param setOffset 0 = current over, 1 = next over etc
     * @return null if specified over can't occur, else the sequence id
     */
    @JsonIgnore
    public String getSequenceIdforOver(int setOffset) {
        int currentOverNo;

        if (TeamId.A == this.bat) {
            currentOverNo = this.getOversA() + setOffset;
        } else {
            currentOverNo = this.getOversB() + setOffset;
        }
        if (currentOverNo > matchFormat.getnOversinMatch())
            return null;
        else
            return String.format("O%d", currentOverNo);
    }

    /**
     * Returns the sequence id to use for wicket based markets
     * 
     * @param setOffset 0 = current wicket, 1 = next wicket etc
     * @return null if specified wicket can't occur, else the sequence id
     */
    @JsonIgnore
    public String getSequenceIdforWicket(int setOffset) {
        int currentWicketNo;

        if (TeamId.A == bat) {
            currentWicketNo = this.getWicketsA() + setOffset;
        } else {
            currentWicketNo = this.getWicketsB() + setOffset;
        }
        if (currentWicketNo > 10)
            return null;
        else
            return String.format("W%d", currentWicketNo);
    }

    /**
     * Returns the sequence id to use for caught wicket based markets
     * 
     * @param setOffset 0 = current caught wicket, 1 = next caught wicket etc
     * @return null if specified caught wicket can't occur, else the sequence id
     */
    @JsonIgnore
    public String getSequenceIdforCaughtWicket(int setOffset) {
        int currentWicketNo;

        if (TeamId.A == bat) {
            currentWicketNo = this.getWicketsA() + setOffset;
        } else {
            currentWicketNo = this.getWicketsB() + setOffset;
        }
        if (currentWicketNo > 10)
            return null;
        else
            return String.format("C%d", currentWicketNo);
    }

    /**
     * Returns true if in preMatch
     * 
     * @return true or false
     */
    @Override
    public boolean preMatch() {
        return getBat() == TeamId.UNKNOWN;
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

    @Override
    public MatchState generateSimpleMatchState() {
        CricketSimpleMatchState simpleMatchState =
                        new CricketSimpleMatchState(preMatch(), isMatchCompleted(), getBall(), getBallsA(), getBallsB(),
                                        getExtra(), getExtrasA(), getExtrasB(), getOversA(), getOversB(), getRunsA(),
                                        getRunsB(), getWicket(), getWicketsA(), getWicketsB(), getInning(), getBat());
        return simpleMatchState;
    }

}
