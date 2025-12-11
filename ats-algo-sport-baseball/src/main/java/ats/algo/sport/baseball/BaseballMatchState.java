package ats.algo.sport.baseball;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ats.algo.core.GamePeriod;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchIncidentPrompt;
import ats.algo.core.baseclasses.MatchIncidentResult;
import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.common.TeamId;
import ats.algo.core.matchresult.MatchResultMap;
import ats.algo.sport.baseball.BaseballMatchIncident.BaseballMatchIncidentType;
import ats.algo.sport.baseball.BaseballMatchIncidentResult.BaseballMatchPeriod;

public class BaseballMatchState extends AlgoMatchState {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private BaseballMatchFormat matchFormat;
    private int hitsA;
    private int hitsB;
    private int hit;
    private int out;
    private int inning;
    private TeamId bat;
    private TeamId batFirst;
    private int runsA;
    private int runsB;
    private int strike;
    private int ball;
    private int firstHalfRunsA;
    private int firstHalfRunsB;
    private int[][] runsInInningsN;
    private int[][] hitsInInningsN;
    private int extraInningsRunsA;
    private int extraInningsRunsB;
    @JsonIgnore
    private int adjustScore;
    private boolean base1;
    private boolean base2;
    private boolean base3;
    private boolean firstHalf;
    private boolean pitcherOneA;
    private boolean pitcherOneB;
    private int extraInnings;
    private TeamId teamScoreLast;
    private TeamId teamScoreFirst;

    // private BaseballPlayer BaseballPlayer;
    private BaseballMatchIncidentResult currentMatchState; // the state

    // following the



    /**
     * Returns player A hits No
     * 
     * @return hitsA
     */
    public int getHitsA() {
        return hitsA;
    }

    public int getFirstHalfRunsA() {
        return firstHalfRunsA;
    }

    public void setFirstHalfRunsA(int firstHalfRunsA) {
        this.firstHalfRunsA = firstHalfRunsA;
    }

    public int getFirstHalfRunsB() {
        return firstHalfRunsB;
    }

    public void setFirstHalfRunsB(int firstHalfRunsB) {
        this.firstHalfRunsB = firstHalfRunsB;
    }

    public TeamId getTeamScoreLast() {
        return teamScoreLast;
    }

    public void setTeamScoreLast(TeamId teamScoreLast) {
        this.teamScoreLast = teamScoreLast;
    }



    public TeamId getTeamScoreFirst() {
        return teamScoreFirst;
    }

    public void setTeamScoreFirst(TeamId teamScoreFirst) {
        this.teamScoreFirst = teamScoreFirst;
    }

    /**
     * @param hitsA sets player A hits No
     */
    public void setHitsA(int hitsA) {
        this.hitsA = hitsA;
    }

    /**
     * Returns player A hits No
     * 
     * @return hitsA
     */
    public int getHitsB() {
        return hitsB;
    }

    /**
     * Returns true or false if it is in first half
     * 
     * @return firstHalf
     */
    public boolean isFirstHalf() {
        return firstHalf;
    }

    /**
     * @param firstHalf sets if it is in first half
     */
    public void setFirstHalf(boolean firstHalf) {
        this.firstHalf = firstHalf;
    }

    /**
     * @param hitsB sets player B hits No
     */
    public void setHitsB(int hitsB) {
        this.hitsB = hitsB;
    }

    /**
     * Returns player A/B hits No
     * 
     * @return hit
     */
    public int getHit() {
        return hit;
    }

    /**
     * Returns player A/B strike No
     * 
     * @return strike
     */
    public int getStrike() {
        return strike;
    }

    /**
     * @param strike sets strike No
     */
    public void setStrike(int strike) {
        this.strike = strike;
    }

    /**
     * Returns player A/B ball No
     * 
     * @return ball
     */
    public int getBall() {
        return ball;
    }

    /**
     * @param ball sets ball No
     */
    public void setBall(int ball) {
        this.ball = ball;
    }

    /**
     * Returns true or false if it is in pitcherOneA
     * 
     * @return pitcherOneA
     */
    public boolean isPitcherOneA() {
        return pitcherOneA;
    }

    /**
     * @param pitcherOneA sets if it is in pitcherOneA
     */
    public void setPitcherOneA(boolean pitcherOneA) {
        this.pitcherOneA = pitcherOneA;
    }

    /**
     * Returns true or false if it is in pitcherOneB
     * 
     * @return pitcherOneB
     */
    public boolean isPitcherOneB() {
        return pitcherOneB;
    }

    /**
     * @param pitcherOneB sets if it is in pitcherOneB
     */
    public void setPitcherOneB(boolean pitcherOneB) {
        this.pitcherOneB = pitcherOneB;
    }

    /**
     * @param hit sets player A/B hit No
     */
    public void setHit(int hit) {
        this.hit = hit;
    }

    /**
     * Returns player A/B out No
     * 
     * @return out No
     */
    public int getOut() {
        return out;
    }

    /**
     * @param out sets player A/B out No
     */
    public void setOut(int out) {
        this.out = out;
    }

    /**
     * Returns player A/B inning No
     * 
     * @return inning No
     */
    public int getInning() {
        return inning;
    }

    /**
     * @param inning sets player A/B inning No
     */
    public void setInning(int inning) {
        this.inning = inning;
    }

    /**
     * Returns player A/B bat No
     * 
     * @return bat No
     */
    public TeamId getBat() {
        return bat;
    }

    /**
     * @param bat sets player A/B bat No
     */
    public void setBat(TeamId bat) {
        this.bat = bat;
    }

    /**
     * Returns player A runs No
     * 
     * @return runsA
     */
    public int getRunsA() {
        return runsA;
    }

    /**
     * @param runsA sets player A runs No
     */
    public void setRunsA(int runsA) {
        this.runsA = runsA;
    }

    /**
     * Returns player B runs No
     * 
     * @return runsB
     */
    public int getRunsB() {
        return runsB;
    }

    /**
     * @param runsB sets player B runs No
     */
    public void setRunsB(int runsB) {
        this.runsB = runsB;
    }

    /**
     * Returns int[][] to display all the runs scores in inningsN of both teams
     * 
     * @return runsInInningsN
     */
    public int[][] getRunsInInningsN() {
        return runsInInningsN;
    }

    /**
     * 
     * @param runsInInningsN sets int[][] to record all the runs scores in inningsN of both teams
     * 
     */
    public void setRunsInInningsN(int[][] runsInInningsN) {
        this.runsInInningsN = runsInInningsN;
    }

    /**
     * Returns the current match state as baseball match incident result
     * 
     * @return currentMatchState
     */
    protected BaseballMatchIncidentResult getCurrentMatchState() {
        return currentMatchState;
    }

    /**
     * @param currentMatchState sets current baseball match state
     * 
     */
    protected void setCurrentMatchState(BaseballMatchIncidentResult currentMatchState) {
        this.currentMatchState = currentMatchState;
    }

    /**
     * Returns true or false if a player on base 1
     * 
     * @return base1
     */
    public boolean isBase1() {
        return base1;
    }

    /**
     * @param base1 sets true or false if a player on base 1
     * 
     */
    public void setBase1(boolean base1) {
        this.base1 = base1;
    }

    /**
     * Returns true or false if a player on base 2
     * 
     * @return base2
     */
    public boolean isBase2() {
        return base2;
    }

    /**
     * @param base2 sets true or false if a player on base 2
     * 
     */
    public void setBase2(boolean base2) {
        this.base2 = base2;
    }

    /**
     * Returns true or false if a player on base 3
     * 
     * @return base3
     */
    public boolean isBase3() {
        return base3;
    }

    /**
     * @param base3 sets true or false if a player on base 3
     * 
     */
    public void setBase3(boolean base3) {
        this.base3 = base3;
    }

    /**
     * Returns extra inning No
     * 
     * @return extraInnings
     */
    public int getExtraInnings() {
        return extraInnings;
    }

    /**
     * @param extraInnings sets extra inning No
     * 
     */
    public void setExtraInnings(int extraInnings) {
        this.extraInnings = extraInnings;
    }

    /**
     * Returns team id of bat first
     * 
     * @return batFirst
     */
    public TeamId getBatFirst() {
        return batFirst;
    }

    /**
     * @param batFirst sets team id of bat first
     * 
     */
    public void setBatFirst(TeamId batFirst) {
        this.batFirst = batFirst;
    }

    /**
     * Returns int[][] to display all the hits scores in inningsN of both teams
     * 
     * @return hitsInInningsN
     */
    public int[][] getHitsInInningsN() {
        return hitsInInningsN;
    }

    /**
     * 
     * @param hitsInInningsN sets int[][] to record all the hits scores in inningsN of both teams
     * 
     */
    public void setHitsInInningsN(int[][] hitsInInningsN) {
        this.hitsInInningsN = hitsInInningsN;
    }

    public int getExtraInningsRunsA() {
        updateCurrentExtraInnings();
        return extraInningsRunsA;
    }

    public void setExtraInningsRunsA(int extraInningsRunsA) {
        this.extraInningsRunsA = extraInningsRunsA;
    }

    public int getExtraInningsRunsB() {
        updateCurrentExtraInnings();
        return extraInningsRunsB;
    }

    public void setExtraInningsRunsB(int extraInningsRunsB) {
        this.extraInningsRunsB = extraInningsRunsB;
    }

    public BaseballMatchState() {
        this(new BaseballMatchFormat());
    }

    public BaseballMatchState(MatchFormat matchFormat) {
        super();
        this.matchFormat = (BaseballMatchFormat) matchFormat;
        teamScoreLast = TeamId.UNKNOWN;
        runsA = 0;
        runsB = 0;
        hitsA = 0;
        hitsB = 0;
        bat = TeamId.UNKNOWN;
        batFirst = TeamId.UNKNOWN;
        hit = 0;
        out = 0;
        inning = 0;
        adjustScore = 0;
        strike = 0;
        ball = 0;
        base1 = false;
        base2 = false;
        base3 = false;
        firstHalf = true;
        pitcherOneA = true;
        pitcherOneB = true;
        extraInnings = this.matchFormat.getnExtraInnings();
        extraInningsRunsA = 0;
        extraInningsRunsB = 0;
        runsInInningsN = new int[2][extraInnings + ((BaseballMatchFormat) matchFormat).getnInningsinMatch()];
        hitsInInningsN = new int[2][extraInnings + ((BaseballMatchFormat) matchFormat).getnInningsinMatch()];
        for (int x = 0; x < 2; x++)
            for (int i = 0; i < runsInInningsN[0].length - 1; i++) {
                runsInInningsN[x][i] = 0;
                hitsInInningsN[x][i] = 0;
            }
        currentMatchState = new BaseballMatchIncidentResult(BaseballMatchPeriod.PREMATCH, TeamId.UNKNOWN);
    }

    /**
     * Return Baseball MatchIncident Result after the match incident updated
     * 
     * @return matchEventResult
     */
    @Override
    public MatchIncidentResult updateStateForIncident(MatchIncident matchIncident,
                    boolean autosyncMatchStateToFeedOnMismatch) {
        BaseballMatchIncidentResult matchEventResult = null;
        boolean tempBase;
        if (!(matchIncident instanceof BaseballMatchIncident))
            return super.updateStateForIncident(matchIncident, autosyncMatchStateToFeedOnMismatch);
        else {
            BaseballMatchIncident mI = (BaseballMatchIncident) matchIncident;

            boolean serveA = bat == TeamId.A;
            TeamId teamId = mI.getTeamId();
            matchEventResult = new BaseballMatchIncidentResult(currentMatchState.getBaseballMatchPeriod(), teamId);
            switch (mI.getIncidentSubType()) {
                case BATFIRST:
                    matchEventResult = new BaseballMatchIncidentResult(BaseballMatchPeriod.FIRST_INNING_FIRST_HALF,
                                    teamId);
                    base1 = false;
                    base2 = false;
                    base3 = false;
                    bat = teamId;
                    batFirst = teamId;
                    firstHalf = true;
                    pitcherOneA = true;
                    pitcherOneB = true;
                    break;
                case NORUNS:
                    hit++;
                    if (serveA) {
                        hitsA = hit;
                    } else
                        hitsB = hit;
                    if (serveA) {
                        runsInInningsN[0][inning] = runsA;
                        hitsInInningsN[0][inning] = hitsA;
                    } else {
                        runsInInningsN[1][inning] = runsB;
                        hitsInInningsN[1][inning] = hitsB;
                    }
                    break;
                case SWITCH_PITCHER:
                    if (serveA) {
                        pitcherOneA = !pitcherOneA;
                    } else {
                        pitcherOneB = !pitcherOneB;
                    }
                    break;
                case RUN1:
                    hit++;
                    if (serveA) {
                        hitsA = hit;
                        teamScoreLast = TeamId.A;
                    } else {
                        hitsB = hit;
                        teamScoreLast = TeamId.B;
                    }
                    if (serveA) {
                        if (runsA + runsB == 0) {
                            teamScoreFirst = TeamId.A;
                        }
                        runsA += 1;
                        runsInInningsN[0][inning] = runsA;
                        hitsInInningsN[0][inning] = hitsA;
                    } else {
                        if (runsA + runsB == 0) {
                            teamScoreFirst = TeamId.B;
                        }
                        runsB += 1;
                        runsInInningsN[1][inning] = runsB;
                        hitsInInningsN[1][inning] = hitsB;
                    }
                    if (base3)
                        base3 = false;
                    else if (base2)
                        base2 = false;
                    else if (base1)
                        base1 = false;
                    strike = 0;
                    ball = 0;
                    break;
                case RUN2:
                    hit++;
                    if (serveA) {
                        hitsA = hit;
                        teamScoreLast = TeamId.A;
                    } else {
                        hitsB = hit;
                        teamScoreLast = TeamId.B;
                    }
                    if (serveA) {
                        if (runsA + runsB == 0) {
                            teamScoreFirst = TeamId.A;
                        }
                        runsA += 2;
                        runsInInningsN[0][inning] = runsA;
                        hitsInInningsN[0][inning] = hitsA;
                    } else {
                        if (runsA + runsB == 0) {
                            teamScoreFirst = TeamId.B;
                        }
                        runsB += 2;
                        runsInInningsN[1][inning] = runsB;
                        hitsInInningsN[1][inning] = hitsB;
                    }
                    if (base3 && base2) {
                        base3 = false;
                        base2 = false;
                    } else {
                        base3 = false;
                        base1 = false;
                        base2 = false;
                    }

                    strike = 0;
                    ball = 0;
                    break;
                case RUN3:
                    hit++;
                    if (serveA) {
                        hitsA = hit;
                        teamScoreLast = TeamId.A;
                    } else {
                        hitsB = hit;
                        teamScoreLast = TeamId.B;
                    }
                    if (serveA) {
                        if (runsA + runsB == 0) {
                            teamScoreFirst = TeamId.A;
                        }
                        runsA += 3;
                        runsInInningsN[0][inning] = runsA;
                        hitsInInningsN[0][inning] = hitsA;
                    } else {
                        if (runsA + runsB == 0) {
                            teamScoreFirst = TeamId.B;
                        }
                        runsB += 3;
                        runsInInningsN[1][inning] = runsB;
                        hitsInInningsN[1][inning] = hitsB;
                    }
                    base1 = false;
                    base2 = false;
                    base3 = false;
                    strike = 0;
                    ball = 0;
                    break;
                case RUN4:
                    hit++;
                    if (serveA) {
                        hitsA = hit;
                        teamScoreLast = TeamId.A;
                    } else {
                        hitsB = hit;
                        teamScoreLast = TeamId.B;
                    }
                    if (serveA) {
                        if (runsA + runsB == 0) {
                            teamScoreFirst = TeamId.A;
                        }
                        runsA += 4;
                        runsInInningsN[0][inning] = runsA;
                        hitsInInningsN[0][inning] = hitsA;
                    } else {
                        if (runsA + runsB == 0) {
                            teamScoreFirst = TeamId.B;
                        }
                        runsB += 4;
                        runsInInningsN[1][inning] = runsB;
                        hitsInInningsN[1][inning] = hitsB;
                    }
                    base1 = false;
                    base2 = false;
                    base3 = false;
                    strike = 0;
                    ball = 0;
                    break;
                case BASE0:
                    base1 = false;
                    base2 = false;
                    base3 = false;
                    break;
                case BASE1:
                    base1 = true;
                    break;
                case BASE2:
                    base2 = true;
                    break;
                case BASE3:
                    base3 = true;
                    break;
                case OUT:
                    out++;
                    if (serveA) {
                        hitsA = hit;
                        hitsInInningsN[0][inning] = hitsA;
                    } else {
                        hitsB = hit;
                        hitsInInningsN[1][inning] = hitsB;
                    }
                    ball = 0;
                    strike = 0;
                    break;
                case OUT0:
                    hit++;
                    if (serveA) {
                        hitsA = hit;
                        hitsInInningsN[0][inning] = hitsA;
                    } else {
                        hitsB = hit;
                        hitsInInningsN[1][inning] = hitsB;
                    }
                    ball = 0;
                    strike = 0;
                    break;
                case OUT2:
                    hit++;
                    if (serveA) {
                        hitsA = hit;
                        hitsInInningsN[0][inning] = hitsA;
                    } else {
                        hitsB = hit;
                        hitsInInningsN[1][inning] = hitsB;
                    }
                    out = out + 2;
                    ball = 0;
                    strike = 0;
                    break;
                case OUT3:
                    hit++;
                    if (serveA) {
                        hitsA = hit;
                        hitsInInningsN[0][inning] = hitsA;
                    } else {
                        hitsB = hit;
                        hitsInInningsN[1][inning] = hitsB;
                    }
                    out = out + 3;
                    ball = 0;
                    strike = 0;
                    break;
                case STRIKE:
                    strike++;
                    if (strike == 3) {
                        strike = 0;
                        ball = 0;
                    }
                    break;
                case S12:
                    hit++;
                    if (serveA) {
                        hitsA = hit;
                        hitsInInningsN[0][inning] = hitsA;
                    } else {
                        hitsB = hit;
                        hitsInInningsN[1][inning] = hitsB;
                    }
                    base1 = false;
                    base2 = true;
                    break;
                case S13:
                    hit++;
                    if (serveA) {
                        hitsA = hit;
                        hitsInInningsN[0][inning] = hitsA;
                    } else {
                        hitsB = hit;
                        hitsInInningsN[1][inning] = hitsB;
                    }
                    base1 = false;
                    base3 = true;
                    break;
                case S23:
                    hit++;
                    if (serveA) {
                        hitsA = hit;
                        hitsInInningsN[0][inning] = hitsA;
                    } else {
                        hitsB = hit;
                        hitsInInningsN[1][inning] = hitsB;
                    }
                    base2 = false;
                    base3 = true;
                    break;
                case S34:
                    hit++;
                    if (serveA) {
                        hitsA = hit;
                        hitsInInningsN[0][inning] = hitsA;
                    } else {
                        hitsB = hit;
                        hitsInInningsN[1][inning] = hitsB;
                    }
                    if (serveA)
                        runsA++;
                    else
                        runsB++;
                    base3 = false;
                    break;
                case HITBYPITCH:
                    hit++;
                    if (serveA) {
                        hitsA = hit;
                        hitsInInningsN[0][inning] = hitsA;
                    } else {
                        hitsB = hit;
                        hitsInInningsN[1][inning] = hitsB;
                    }
                    if (serveA) {
                        tempBase = isBase1();
                        base1 = true;
                        ball = 0;
                        strike = 0;
                        if (tempBase) {
                            tempBase = isBase2();
                            base2 = true;
                        }
                        if (tempBase) {
                            tempBase = isBase3();
                            base3 = true;
                        }
                        if (tempBase) {
                            runsA++;
                            runsInInningsN[0][inning] = runsA;
                        }

                    } else {
                        tempBase = isBase1();
                        base1 = true;
                        ball = 0;
                        strike = 0;
                        if (tempBase) {
                            tempBase = isBase2();
                            base2 = true;
                        }
                        if (tempBase) {
                            tempBase = isBase3();;
                            base3 = true;
                        }
                        if (tempBase) {
                            runsB++;
                            runsInInningsN[1][inning] = runsB;
                        }
                    }
                    break;
                case BALL:
                    ball++;
                    if (ball == 4) {
                        if (serveA) {
                            tempBase = isBase1();
                            base1 = true;
                            ball = 0;
                            strike = 0;
                            if (tempBase) {
                                tempBase = isBase2();
                                base2 = true;
                            }
                            if (tempBase) {
                                tempBase = isBase3();
                                base3 = true;
                            }
                            if (tempBase) {
                                runsA++;
                                runsInInningsN[0][inning] = runsA;
                            }

                        } else {
                            tempBase = isBase1();
                            base1 = true;
                            ball = 0;
                            strike = 0;
                            if (tempBase) {
                                tempBase = isBase2();
                                base2 = true;
                            }
                            if (tempBase) {
                                tempBase = isBase3();
                                base3 = true;
                            }
                            if (tempBase) {
                                runsB++;
                                runsInInningsN[1][inning] = runsB;
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
            if (isStillInFirstHalf()) {
                firstHalfRunsA = runsA;
                firstHalfRunsB = runsB;
            }

            if (isHalfInningEnded()) {
                bat = switchServer(bat);

                // hit = 0;
                out = 0;
                strike = 0;
                ball = 0;
                firstHalf = !firstHalf;
                runsInInningsN[0][inning] = runsA;
                runsInInningsN[1][inning] = runsB;
                if (firstHalf) {
                    inning++;
                }
                base1 = false;
                base2 = false;
                base3 = false;
                boolean extraSeconfHalf = false;
                if (currentMatchState.getBaseballMatchPeriod().getPeriod() < 22) {
                    matchEventResult = new BaseballMatchIncidentResult(currentMatchState.getNextBaseballMatchPeriod(),
                                    teamId);
                    currentMatchState = matchEventResult;
                } else {
                    extraSeconfHalf = true;
                }

                // System.out.println(inning + "--" +
                // currentMatchState.getBaseballMatchPeriod().ordinal());
                if (currentMatchState.getBaseballMatchPeriod().getPeriod() == 19) {
                    if (runsA == runsB && matchFormat.drawPossible) {
                        matchEventResult = new BaseballMatchIncidentResult(BaseballMatchPeriod.DRAW, teamId);
                        currentMatchState = matchEventResult;
                    } else if (runsA == runsB) {
                        matchEventResult = new BaseballMatchIncidentResult(BaseballMatchPeriod.EXTRA_INNING_FIRST_HALF,
                                        teamId);
                        currentMatchState = matchEventResult;
                    }
                }
                if (extraInnings > 1 && currentMatchState.getBaseballMatchPeriod().getPeriod() == 22) {
                    if (extraSeconfHalf) {
                        matchEventResult = new BaseballMatchIncidentResult(BaseballMatchPeriod.EXTRA_INNING_FIRST_HALF,
                                        teamId);
                        currentMatchState = matchEventResult;
                        extraInnings--;
                    }
                } else if (currentMatchState.getBaseballMatchPeriod().getPeriod() == 22) {
                    if (extraSeconfHalf) {
                        matchEventResult = new BaseballMatchIncidentResult(BaseballMatchPeriod.MATCH_COMPLETED, teamId);
                        currentMatchState = matchEventResult;
                    }
                }
            }
            if (runsB > runsA && batFirst == TeamId.A && currentMatchState.getBaseballMatchPeriod()
                            .equals(BaseballMatchPeriod.NINTH_INNING_SECOND_HALF)) {
                matchEventResult = new BaseballMatchIncidentResult(BaseballMatchPeriod.MATCH_COMPLETED, teamId);
                currentMatchState = matchEventResult;
            }

            if (runsA > runsB && batFirst == TeamId.B && currentMatchState.getBaseballMatchPeriod()
                            .equals(BaseballMatchPeriod.NINTH_INNING_SECOND_HALF)) {
                matchEventResult = new BaseballMatchIncidentResult(BaseballMatchPeriod.MATCH_COMPLETED, teamId);
                currentMatchState = matchEventResult;
            }

            if (runsB > runsA && batFirst == TeamId.A && (inning == runsInInningsN[0].length - 1) && bat == TeamId.B) {
                matchEventResult = new BaseballMatchIncidentResult(BaseballMatchPeriod.MATCH_COMPLETED, teamId);
                currentMatchState = matchEventResult;
            }

            if (runsA > runsB && batFirst == TeamId.B && (inning == runsInInningsN[0].length - 1) && bat == TeamId.A) {
                matchEventResult = new BaseballMatchIncidentResult(BaseballMatchPeriod.MATCH_COMPLETED, teamId);
                currentMatchState = matchEventResult;
            }

        }

        currentMatchState = matchEventResult;
        return matchEventResult;

    }

    private boolean isStillInFirstHalf() {
        return inning < 6;
    }

    @Override
    public AlgoMatchState copy() {
        BaseballMatchState cc = new BaseballMatchState(this.matchFormat);
        cc.setEqualTo(this);
        return cc;
    }

    @Override
    public void setEqualTo(AlgoMatchState matchState) {
        BaseballMatchState vs = (BaseballMatchState) matchState;
        this.matchFormat = (BaseballMatchFormat) vs.getMatchFormat();
        this.setTeamScoreFirst(vs.getTeamScoreFirst());
        this.setRunsA(vs.getRunsA());
        this.setRunsB(vs.getRunsB());
        this.setHit(vs.getHit());
        this.setHitsA(vs.getHitsA());
        this.setHitsB(vs.getHitsB());
        this.setOut(vs.getOut());
        this.setBat(vs.getBat());
        this.setBase1(vs.isBase1());
        this.setBase2(vs.isBase2());
        this.setBase3(vs.isBase3());
        this.setFirstHalf(vs.isFirstHalf());
        this.setInning(vs.getInning());
        this.setCurrentMatchState(vs.getCurrentMatchState());
        this.setPitcherOneA(vs.isPitcherOneA());
        this.setPitcherOneB(vs.isPitcherOneB());
        this.setBall(vs.getBall());
        this.setStrike(vs.getStrike());
        this.setExtraInnings(vs.getExtraInnings());
        int[][] temp_A = copy(vs.getRunsInInningsN());
        this.setRunsInInningsN(temp_A);
        int[][] temp_B = copy(vs.getHitsInInningsN());
        this.setHitsInInningsN(temp_B);
        // this.setRunsInInningsN(vs.getRunsInInningsN().clone());
        this.setBatFirst(vs.getBatFirst());
        this.setExtraInningsRunsA(vs.getExtraInningsRunsA());
        this.setExtraInningsRunsB(vs.getExtraInningsRunsB());
        this.setTeamScoreLast(vs.getTeamScoreLast());
        this.setFirstHalfRunsA(vs.getFirstHalfRunsA());
        this.setFirstHalfRunsB(vs.getFirstHalfRunsB());
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
        String nextPromt =
                        "Enter RUNS R0/1/2/3/4 OR Out method : O0/1/2/3 Out 1,2,3, Player Strike:ST , Ball method: Ball, Base: B0/1/2/3 ,Hit by pitch: H, switch pitcher: SW, Steal base:S12/13/23/34 ";
        switch (currentMatchState.getBaseballMatchPeriod()) {
            case PREMATCH:
                matchIncidentPrompt = new MatchIncidentPrompt("Enter BA/BB as who bat first", lastLegWinner);
                break;
            case FIRST_INNING_FIRST_HALF:
            case FIRST_INNING_SECOND_HALF:
            case SECOND_INNING_FIRST_HALF:

            case SECOND_INNING_SECOND_HALF:
            case THIRD_INNING_FIRST_HALF:

            case THIRD_INNING_SECOND_HALF:

            case FOURTH_INNING_FIRST_HALF:
            case FOURTH_INNING_SECOND_HALF:

            case FIFTH_INNING_FIRST_HALF:
            case FIFTH_INNING_SECOND_HALF:

            case SIXTH_INNING_FIRST_HALF:

            case SIXTH_INNING_SECOND_HALF:
            case SEVENTH_INNING_FIRST_HALF:

            case SEVENTH_INNING_SECOND_HALF:

            case EIGHTH_INNING_FIRST_HALF:

            case EIGHTH_INNING_SECOND_HALF:

            case NINTH_INNING_FIRST_HALF:
            case NINTH_INNING_SECOND_HALF:
            case EXTRA_INNING_FIRST_HALF:
            case EXTRA_INNING_SECOND_HALF:
                lastLegWinner = "R0";
                matchIncidentPrompt = new MatchIncidentPrompt(nextPromt, lastLegWinner);
                break;

            case MATCH_COMPLETED:
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
        BaseballMatchIncident matchEvent = new BaseballMatchIncident();
        TeamId teamId;
        switch (response.toUpperCase()) {
            case "R0":
                matchEvent.setIncidentSubType(BaseballMatchIncidentType.NORUNS);
                break;
            case "R1":
                matchEvent.setIncidentSubType(BaseballMatchIncidentType.RUN1);
                break;
            case "B0":
                matchEvent.setIncidentSubType(BaseballMatchIncidentType.BASE0);
                break;
            case "B1":
                matchEvent.setIncidentSubType(BaseballMatchIncidentType.BASE1);
                break;
            case "B2":
                matchEvent.setIncidentSubType(BaseballMatchIncidentType.BASE2);
                break;
            case "B3":
                matchEvent.setIncidentSubType(BaseballMatchIncidentType.BASE3);
                break;
            case "R2":
                matchEvent.setIncidentSubType(BaseballMatchIncidentType.RUN2);
                break;
            case "R3":
                matchEvent.setIncidentSubType(BaseballMatchIncidentType.RUN3);
                break;
            case "R4":
                matchEvent.setIncidentSubType(BaseballMatchIncidentType.RUN4);
                break;
            case "H":
                matchEvent.setIncidentSubType(BaseballMatchIncidentType.HITBYPITCH);
                break;
            case "S12":
                matchEvent.setIncidentSubType(BaseballMatchIncidentType.S12);
                break;
            case "S13":
                matchEvent.setIncidentSubType(BaseballMatchIncidentType.S13);
                break;
            case "S23":
                matchEvent.setIncidentSubType(BaseballMatchIncidentType.S23);
                break;
            case "S34":
                matchEvent.setIncidentSubType(BaseballMatchIncidentType.S34);
                break;
            case "O0":
                matchEvent.setIncidentSubType(BaseballMatchIncidentType.OUT0);
                break;
            case "O1":
                matchEvent.setIncidentSubType(BaseballMatchIncidentType.OUT);
                break;
            case "O2":
                matchEvent.setIncidentSubType(BaseballMatchIncidentType.OUT2);
                break;
            case "O3":
                matchEvent.setIncidentSubType(BaseballMatchIncidentType.OUT3);
                break;
            case "ST":
                matchEvent.setIncidentSubType(BaseballMatchIncidentType.STRIKE);
                break;
            case "SW":
                matchEvent.setIncidentSubType(BaseballMatchIncidentType.SWITCH_PITCHER);
                break;
            case "BALL":
                matchEvent.setIncidentSubType(BaseballMatchIncidentType.BALL);
                break;
            case "BA":
                teamId = TeamId.A;
                matchEvent.set(BaseballMatchIncidentType.BATFIRST, teamId);
                break;
            case "BB":
                teamId = TeamId.B;
                matchEvent.set(BaseballMatchIncidentType.BATFIRST, teamId);
                break;
            case "MC":
                matchEvent.setIncidentSubType(BaseballMatchIncidentType.MATCH_COMPLETED);
                break;
            default:
                return null; // invalid input so return null
        }
        return matchEvent;
    }

    private static final String seScoreKey = "inning score";
    private static final String baScoreKey = "ball score";
    private static final String poScoreKey = "run score";
    private static final String hitScoreKey = "hit score";
    private static final String stScoreKey = "strike";
    private static final String outsScoreKey = "outs";
    private static final String baseOneKey = "Base 1";
    private static final String baseTwoKey = "Base 2";
    private static final String baseThreeKey = "Base 3";
    private static final String onServeKey = "who is bating";
    private static final String pitcherAKey = "PitcherA one";
    private static final String pitcherBKey = "PitcherB one";
    private static final String gamePeriodKey = "Period";
    private static final String extraScoreKey = "extra innings runs scored";


    /**
     * Returns map
     * 
     * @return map
     */
    @JsonIgnore
    @Override
    public LinkedHashMap<String, String> getAsMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put(gamePeriodKey, String.format("%s", currentMatchState.getBaseballMatchPeriod()));
        map.put(seScoreKey, String.format("%d", inning + 1));
        map.put(baScoreKey, String.format("%d", ball));
        map.put(stScoreKey, String.format("%d", strike));
        map.put(outsScoreKey, String.format("%d", out));
        map.put(hitScoreKey, String.format("%d-%d", hitsA, hitsB));
        map.put(poScoreKey, String.format("%d-%d", runsA, runsB));
        map.put(onServeKey, String.format("%s", bat));
        map.put(baseOneKey, String.format("%s", base1));
        map.put(baseTwoKey, String.format("%s", base2));
        map.put(baseThreeKey, String.format("%s", base3));
        map.put(pitcherAKey, String.format("%s", isPitcherOneA()));
        map.put(pitcherBKey, String.format("%s", isPitcherOneB()));
        map.put(extraScoreKey, String.format("%d-%d", extraInningsRunsA, extraInningsRunsB));

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
     * Returns true or false if the half inning is ended
     * 
     * @return true or false
     */
    @JsonIgnore
    public boolean isHalfInningEnded() {
        if (out >= 3) {
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
        return (currentMatchState.getBaseballMatchPeriod() == BaseballMatchPeriod.MATCH_COMPLETED)
                        || (currentMatchState.getBaseballMatchPeriod() == BaseballMatchPeriod.DRAW);
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
        switch (currentMatchState.getBaseballMatchPeriod()) {
            case PREMATCH:
                gamePeriod = GamePeriod.PREMATCH;
                break;
            case DRAW:
            case MATCH_COMPLETED:
                gamePeriod = GamePeriod.POSTMATCH;
                break;
            case FIRST_INNING_FIRST_HALF:
            case FIRST_INNING_SECOND_HALF:
            case SECOND_INNING_FIRST_HALF:

            case SECOND_INNING_SECOND_HALF:
            case THIRD_INNING_FIRST_HALF:

            case THIRD_INNING_SECOND_HALF:

            case FOURTH_INNING_FIRST_HALF:
            case FOURTH_INNING_SECOND_HALF:

            case FIFTH_INNING_FIRST_HALF:
            case FIFTH_INNING_SECOND_HALF:

            case SIXTH_INNING_FIRST_HALF:

            case SIXTH_INNING_SECOND_HALF:
            case SEVENTH_INNING_FIRST_HALF:

            case SEVENTH_INNING_SECOND_HALF:

            case EIGHTH_INNING_FIRST_HALF:

            case EIGHTH_INNING_SECOND_HALF:

            case NINTH_INNING_FIRST_HALF:
            case NINTH_INNING_SECOND_HALF:
                gamePeriod = GamePeriod.PERIOD1;
                break;
            case EXTRA_INNING_FIRST_HALF:
            case EXTRA_INNING_SECOND_HALF:
                gamePeriod = GamePeriod.EXTRA_TIME_INNINGS;
                break;
            default:
                break;
        }
        return gamePeriod;
    }

    /**
     * Returns true or false if the normal match is completed
     * 
     * @return true or false
     */
    @JsonIgnore
    public boolean isNormalTimeMatchCompleted() {
        return getGamePeriod().equals(GamePeriod.EXTRA_TIME_INNINGS);
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
     * Returns the sequence id to use for inning based markets
     * 
     * @param setOffset 0 = current inning, 1 = next inning etc
     * @return null if specified inning can't occur, else the sequence id
     */
    @JsonIgnore
    public String getSequenceIdforInning(int setOffset) {
        int currentInningNo;

        currentInningNo = this.getInning() + setOffset;
        if (currentInningNo > matchFormat.getnInningsinMatch())
            return null;
        else
            return String.format("I%d", currentInningNo);
    }

    /**
     * Returns true if in preMatch
     * 
     * @return true or false
     */
    @Override
    public boolean preMatch() {
        return (currentMatchState.getBaseballMatchPeriod() == BaseballMatchPeriod.PREMATCH);
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
     * Returns 0 no base has player 1 base1 has player 2 base2 has player 3 base3 has player 4 base 1 and 2 have player
     * 5 base 1 and 3 have player 6 base 2 and 3 have player 7 base 1 and 2 and 3 have player 8 else
     * 
     * @return base No range 1-8
     */
    @JsonIgnore
    public int getBaseInfo() {
        if (!base1 && !base2 && !base3)
            return 0;
        if (base1 && !base2 && !base3)
            return 1;
        if (!base1 && base2 && !base3)
            return 2;
        if (!base1 && !base2 && base3)
            return 3;
        if (base1 && base2 && !base3)
            return 4;
        if (base1 && !base2 && base3)
            return 5;
        if (!base1 && base2 && base3)
            return 6;
        if (base1 && base2 && base3)
            return 7;
        else
            return 8;
    }

    public void updateCurrentExtraInnings() {
        extraInningsRunsA = 0;
        extraInningsRunsB = 0;
        for (int i = this.matchFormat.getnInningsinMatch(); i < this.runsInInningsN[0].length; i++) {
            extraInningsRunsB += runsInInningsN[0][i];
            extraInningsRunsB += runsInInningsN[1][i];

        }
    }

    @Override
    public SimpleMatchState generateSimpleMatchState() {
        BaseballSimpleMatchState simpleMatchState = new BaseballSimpleMatchState(this.preMatch(),
                        this.isMatchCompleted(), this.getHitsA(), this.getHitsB(), this.getHit(), this.getOut(),
                        this.getInning(), this.getBat(), this.getBatFirst(), this.getRunsA(), this.getRunsB(),
                        this.getStrike(), this.getBall(), this.isBase1(), this.isBase2(), this.isBase3(),
                        this.isFirstHalf(), this.isPitcherOneA(), this.isPitcherOneB(), this.getExtraInnings(),
                        this.getExtraInningsRunsA(), this.getExtraInningsRunsB());
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
