package ats.algo.sport.bowls;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ats.algo.core.GamePeriod;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchIncidentPrompt;
import ats.algo.core.baseclasses.MatchIncidentResult;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.PairOfIntegers;
import ats.algo.sport.bowls.BowlsMatchIncident.BowlsMatchIncidentType;
import ats.algo.sport.bowls.BowlsMatchIncidentResult.BowlsMatchIncidentResultType;

public class BowlsMatchState extends MatchState {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private BowlsMatchFormat matchFormat;
    private double setsA;
    private double setsB;
    private int currentEnd;
    private boolean setRace;
    private TeamId serve;
    private TeamId serveIncurrentEnd;
    private final int bowlsInEnd;
    private final int endScoreForWin;
    private final int endsInFinalSet;
    private final double setScoreForWin;
    private final int setScoreForDraw;
    private PairOfIntegers[] gameScoreInSetN;
    private int pointsA;
    private int pointsB;
    private boolean extraEnd;
    private boolean finalSet;
    @JsonIgnore
    private BowlsMatchIncidentResult currentMatchState; // the state
                                                        // following the
                                                        // most recent

    /**
     * Returns true if race has been recorded
     * 
     * @return setRace
     */
    public boolean isSetRace() {
        return setRace;
    }

    /**
     * 
     * 
     * @param setRace to check set race is true or false
     * 
     */
    public void setSetRace(boolean setRace) {
        this.setRace = setRace;
    }

    /**
     * Returns how many bowls in end
     * 
     * @return bowlsInEnd
     */
    public int getBowlsInEnd() {
        return bowlsInEnd;
    }

    /**
     * Returns the current end No
     * 
     * @return currentEnd
     */
    public int getCurrentEnd() {
        return currentEnd;
    }

    /**
     * 
     * 
     * @param currentEnd to set current end No
     * 
     */
    public void setCurrentEnd(int currentEnd) {
        this.currentEnd = currentEnd;
    }

    /**
     * 
     * 
     * @param extraEnd to set if it has extra end
     * 
     */
    public void setExtraEnd(boolean extraEnd) {
        this.extraEnd = extraEnd;
    }

    /**
     * Returns true if match has extra end
     * 
     * @return extraEnd
     */
    public boolean isExtraEnd() {
        return extraEnd;
    }

    /**
     * Returns true if it is in final end
     * 
     * @return finalSet
     */
    public boolean isFinalSet() {
        return finalSet;
    }

    /**
     * 
     * 
     * @param finalSet to set if it is in final end
     * 
     */
    public void setFinalSet(boolean finalSet) {
        this.finalSet = finalSet;
    }

    /**
     * Returns PairOfIntegers[i] to display set point score of both teams
     * 
     * @param i set number
     * @return gameScoreInSetN[i]
     */
    public PairOfIntegers getGameScoreInSetN(int i) {
        return gameScoreInSetN[i];
    }

    /**
     *
     * 
     * @param serve sets who is serve now
     * 
     */
    public void setServe(TeamId serve) {
        this.serve = serve;
    }

    /**
     * 
     * 
     * @param serveInFirstSet sets who is serve in first set
     * 
     */
    public void setServeInFirstSet(TeamId serveInFirstSet) {
        this.serveIncurrentEnd = serveInFirstSet;
    }

    /**
     * Returns who is serve now
     * 
     * @return serve
     */
    public TeamId getServe() {
        return serve;
    }

    /**
     * Returns who is serve in first set now
     * 
     * @return serveInFirstSet
     */
    public TeamId getServeInFirstSet() {
        return serveIncurrentEnd;
    }

    /**
     * Returns how many sets in draw match
     * 
     * @return setScoreForDraw
     */
    public int getSetScoreForDraw() {
        return setScoreForDraw;
    }

    /**
     * Returns how many sets in match
     * 
     * @return setScoreForWin
     */
    public double getSetScoreForWin() {
        return setScoreForWin;
    }

    /**
     * Returns how many ends in set
     * 
     * @return endScoreForWin
     */
    public int getEndScoreForWin() {
        return endScoreForWin;
    }

    /**
     *
     * 
     * @param pointsA Sets the Team A point score
     * 
     */
    public void setPointsA(int pointsA) {
        this.pointsA = pointsA;
    }

    /**
     *
     * 
     * @param pointsB Sets the Team B point score
     * 
     */
    public void setPointsB(int pointsB) {
        this.pointsB = pointsB;
    }

    /**
     * Returns team A point score
     * 
     * @return pointsA
     */
    public int getPointsA() {
        return pointsA;
    }

    /**
     * Returns team B point score
     * 
     * @return pointsB
     */
    public int getPointsB() {
        return pointsB;
    }

    /**
     * Returns team A set score
     * 
     * @return setsA
     */
    public double getSetsA() {
        return setsA;
    }

    /**
     * Returns team B set score
     * 
     * @return setsB
     */
    public double getSetsB() {
        return setsB;
    }

    /**
     *
     * 
     * @param setsA Sets the player A set score
     * 
     */
    public void setSetsA(double setsA) {
        this.setsA = setsA;
    }

    /**
     *
     * 
     * @param setsB Sets the player B set score
     * 
     */
    public void setSetsB(double setsB) {
        this.setsB = setsB;
    }

    /**
     * Returns PairOfIntegers to display all the set point scores of both teams
     * 
     * @return gameScoreInSetN
     */
    public PairOfIntegers[] getGameScoreInSetN() {
        return gameScoreInSetN;
    }

    /**
     * set PairOfIntegers
     * 
     * @param gameScoreInSetN the pair of integers to record point score in each set
     * 
     */
    public void setGameScoreInSetN(PairOfIntegers[] gameScoreInSetN) {
        this.gameScoreInSetN = gameScoreInSetN;
    }

    /**
     * Returns how many ends in final set
     * 
     * @return endsInFinalSet
     */
    public int getEndsInFinalSet() {
        return endsInFinalSet;
    }

    /**
     * sets the starting score. Starting points assumed to be zero
     * 
     * @param setsA Sets setsA
     * @param setsB Sets setsB
     * @param pointA Sets pointA
     * @param pointB Sets pointB
     * @param onServeNow may be A,B or unknown
     */
    public void setScore(double setsA, double setsB, int pointA, int pointB, TeamId onServeNow) {
        this.setsA = setsA;
        this.setsB = setsB;
        this.pointsA = pointA;
        this.pointsB = pointB;
        this.serve = onServeNow;

    }

    /**
     * Returns the current match state as bowls match incident result
     * 
     * @return currentMatchState
     */
    protected BowlsMatchIncidentResult getCurrentMatchState() {
        return currentMatchState;
    }

    /**
     * 
     * 
     * @param currentMatchState sets current bowls match state
     * 
     */
    protected void setCurrentMatchState(BowlsMatchIncidentResult currentMatchState) {
        this.currentMatchState = currentMatchState;
    }

    public BowlsMatchState() {
        this(new BowlsMatchFormat());
    }

    public BowlsMatchState(MatchFormat matchFormat) {
        super();
        this.matchFormat = (BowlsMatchFormat) matchFormat;
        int n = this.matchFormat.getnSetsInMatch();
        endScoreForWin = ((BowlsMatchFormat) matchFormat).getnEndInSet();
        setScoreForWin = n / 2 + 1;
        endsInFinalSet = ((BowlsMatchFormat) matchFormat).getnEndInFinalSet();
        if (2 * (n / 2) == n) // i.e. if n is even
            setScoreForDraw = n / 2;
        else
            setScoreForDraw = n; // set high enough so it doesn't ever get hit
        bowlsInEnd = ((BowlsMatchFormat) matchFormat).getBowlsInEnd();
        pointsA = 0;
        pointsB = 0;
        currentEnd = 1;
        finalSet = false;
        setRace = true; // to deceide if race to x points has been done
        currentMatchState = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.PREMATCH);
        gameScoreInSetN = new PairOfIntegers[n];
        for (int i = 0; i < n; i++)
            gameScoreInSetN[i] = new PairOfIntegers();
        serve = TeamId.UNKNOWN;
        serveIncurrentEnd = TeamId.UNKNOWN;
        setScore(0, 0, 0, 0, TeamId.UNKNOWN);
    }

    /**
     * Return bowls MatchIncident Result after the match incident updated
     * 
     * @return matchEventResult
     */
    @Override
    public MatchIncidentResult updateStateForIncident(MatchIncident matchIncident,
                    boolean autosyncMatchStateToFeedOnMismatch) {
        BowlsMatchIncidentResult matchEventResult = null;
        if (!(matchIncident instanceof BowlsMatchIncident))
            return super.updateStateForIncident(matchIncident, autosyncMatchStateToFeedOnMismatch);
        else {
            BowlsMatchIncident mI = (BowlsMatchIncident) matchIncident;
            TeamId teamId = mI.getTeamId();
            switch (mI.getIncidentSubType()) {
                case SERVEFIRST:
                    matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.SERVEFIRST, teamId);
                    serveIncurrentEnd = teamId;
                    serve = serveIncurrentEnd;
                    break;
                case SERVENOW:
                    matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.SERVENOW, teamId);
                    serve = teamId;
                    break;
                case ENDWON1:
                    if (TeamId.A == teamId) {
                        matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.ENDWON1, teamId);
                        currentEnd++;
                        pointsA++;

                        serveIncurrentEnd = switchServer(serveIncurrentEnd);
                        serve = serveIncurrentEnd;

                        if (isSetWinningGameScore(currentEnd, pointsA, pointsB)) {
                            if (pointsA < pointsB) {
                                matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.SETWON,
                                                TeamId.B);
                                setsB++;
                            } else if (pointsA == pointsB) {
                                matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.SETDRAW,
                                                TeamId.UNKNOWN);
                                setsB = setsB + 0.5;
                                setsA = setsA + 0.5;
                            } else {
                                matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.SETWON,
                                                TeamId.B);
                                setsA++;
                            }
                            if (!setRace)
                                matchEventResult.setRaceToXpoints(true);
                            if ((setsA == setScoreForWin - 1) && (setsB == setScoreForWin - 1))
                                finalSet = true;
                            currentEnd = 1;
                            gameScoreInSetN[(int) (setsA + setsB - 1)].A = pointsA;
                            gameScoreInSetN[(int) (setsA + setsB - 1)].B = pointsB;
                            pointsA = 0;
                            pointsB = 0;
                            setRace = true;
                            if (setsB > (setScoreForWin - 1)) {
                                matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.MATCHWON,
                                                TeamId.B);
                            }
                            if (setsA > (setScoreForWin - 1)) {
                                matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.MATCHWON);
                            }
                        }
                    } else {
                        matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.ENDWON1, teamId);
                        currentEnd++;
                        pointsB++;
                        serveIncurrentEnd = switchServer(serveIncurrentEnd);
                        serve = serveIncurrentEnd;

                        if (isSetWinningGameScore(currentEnd, pointsA, pointsB)) {
                            if (pointsA < pointsB) {
                                matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.SETWON,
                                                TeamId.B);
                                setsB++;
                            } else if (pointsA == pointsB) {
                                matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.SETDRAW);
                                setsB = setsB + 0.5;
                                setsA = setsA + 0.5;
                            } else {
                                matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.SETWON,
                                                TeamId.A);
                                setsA++;
                            }
                            if ((setsA == setScoreForWin - 1) && (setsB == setScoreForWin - 1))
                                finalSet = true;
                            currentEnd = 1;
                            gameScoreInSetN[(int) (setsA + setsB - 1)].A = pointsA;
                            gameScoreInSetN[(int) (setsA + setsB - 1)].B = pointsB;
                            pointsA = 0;
                            pointsB = 0;
                            setRace = true;
                            if (setsB > (setScoreForWin - 1)) {
                                matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.MATCHWON,
                                                TeamId.B);
                            }
                            if (setsA > (setScoreForWin - 1)) {
                                matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.MATCHWON,
                                                TeamId.A);
                            }
                        }
                    }

                    break;
                case ENDWON2:
                    if (TeamId.A == teamId) {

                        matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.ENDWON2, teamId);
                        currentEnd++;
                        if (finalSet)
                            pointsA++;
                        else
                            pointsA = pointsA + 2;

                        serveIncurrentEnd = switchServer(serveIncurrentEnd);
                        serve = serveIncurrentEnd;

                        if (isSetWinningGameScore(currentEnd, pointsA, pointsB)) {
                            if (pointsA < pointsB) {
                                matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.SETWON,
                                                TeamId.B);
                                setsB++;
                            } else if (pointsA == pointsB) {
                                matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.SETDRAW,
                                                TeamId.UNKNOWN);
                                setsB = setsB + 0.5;
                                setsA = setsA + 0.5;
                            } else {
                                matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.SETWON,
                                                TeamId.B);
                                setsA++;
                            }
                            if (!setRace)
                                matchEventResult.setRaceToXpoints(true);
                            if ((setsA == setScoreForWin - 1) && (setsB == setScoreForWin - 1))
                                finalSet = true;
                            currentEnd = 1;
                            gameScoreInSetN[(int) (setsA + setsB - 1)].A = pointsA;
                            gameScoreInSetN[(int) (setsA + setsB - 1)].B = pointsB;
                            pointsA = 0;
                            pointsB = 0;
                            setRace = true;
                            if (setsB > (setScoreForWin - 1)) {
                                matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.MATCHWON,
                                                TeamId.B);
                            }
                            if (setsA > (setScoreForWin - 1)) {
                                matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.MATCHWON);
                            }
                        }
                    } else {
                        matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.ENDWON2, teamId);
                        currentEnd++;
                        if (finalSet)
                            pointsB++;
                        else
                            pointsB = pointsB + 2;
                        serveIncurrentEnd = switchServer(serveIncurrentEnd);
                        serve = serveIncurrentEnd;

                        if (isSetWinningGameScore(currentEnd, pointsA, pointsB)) {
                            if (pointsA < pointsB) {
                                matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.SETWON,
                                                TeamId.B);
                                setsB++;
                            } else if (pointsA == pointsB) {
                                matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.SETDRAW);
                                setsB = setsB + 0.5;
                                setsA = setsA + 0.5;
                            } else {
                                matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.SETWON,
                                                TeamId.A);
                                setsA++;
                            }
                            if ((setsA == setScoreForWin - 1) && (setsB == setScoreForWin - 1))
                                finalSet = true;
                            currentEnd = 1;
                            gameScoreInSetN[(int) (setsA + setsB - 1)].A = pointsA;
                            gameScoreInSetN[(int) (setsA + setsB - 1)].B = pointsB;
                            pointsA = 0;
                            pointsB = 0;
                            setRace = true;
                            if (setsB > (setScoreForWin - 1)) {
                                matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.MATCHWON,
                                                TeamId.B);
                            }
                            if (setsA > (setScoreForWin - 1)) {
                                matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.MATCHWON,
                                                TeamId.A);
                            }

                        }
                    }
                    break;
                case ENDWON3:
                    if (TeamId.A == teamId) {

                        matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.ENDWON3, teamId);
                        currentEnd++;
                        if (finalSet)
                            pointsA++;
                        else
                            pointsA = pointsA + 3;

                        serveIncurrentEnd = switchServer(serveIncurrentEnd);
                        serve = serveIncurrentEnd;

                        if (isSetWinningGameScore(currentEnd, pointsA, pointsB)) {
                            if (pointsA < pointsB) {
                                matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.SETWON,
                                                TeamId.B);
                                setsB++;
                            } else if (pointsA == pointsB) {
                                matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.SETDRAW,
                                                TeamId.UNKNOWN);
                                setsB = setsB + 0.5;
                                setsA = setsA + 0.5;
                            } else {
                                matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.SETWON,
                                                TeamId.B);
                                setsA++;
                            }
                            if (!setRace)
                                matchEventResult.setRaceToXpoints(true);
                            if ((setsA == setScoreForWin - 1) && (setsB == setScoreForWin - 1))
                                finalSet = true;
                            currentEnd = 1;
                            gameScoreInSetN[(int) (setsA + setsB - 1)].A = pointsA;
                            gameScoreInSetN[(int) (setsA + setsB - 1)].B = pointsB;
                            pointsA = 0;
                            pointsB = 0;
                            setRace = true;
                            if (setsB > (setScoreForWin - 1)) {
                                matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.MATCHWON,
                                                TeamId.B);
                            }
                            if (setsA > (setScoreForWin - 1)) {
                                matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.MATCHWON);
                            }
                        }
                    } else {
                        matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.ENDWON3, teamId);
                        currentEnd++;
                        if (finalSet)
                            pointsB++;
                        else
                            pointsB = pointsB + 3;
                        serveIncurrentEnd = switchServer(serveIncurrentEnd);
                        serve = serveIncurrentEnd;

                        if (isSetWinningGameScore(currentEnd, pointsA, pointsB)) {
                            if (pointsA < pointsB) {
                                matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.SETWON,
                                                TeamId.B);
                                setsB++;
                            } else if (pointsA == pointsB) {
                                matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.SETDRAW);
                                setsB = setsB + 0.5;
                                setsA = setsA + 0.5;
                            } else {
                                matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.SETWON,
                                                TeamId.A);
                                setsA++;
                            }
                            if ((setsA == setScoreForWin - 1) && (setsB == setScoreForWin - 1))
                                finalSet = true;
                            currentEnd = 1;
                            gameScoreInSetN[(int) (setsA + setsB - 1)].A = pointsA;
                            gameScoreInSetN[(int) (setsA + setsB - 1)].B = pointsB;
                            pointsA = 0;
                            pointsB = 0;
                            setRace = true;
                            if (setsB > (setScoreForWin - 1)) {
                                matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.MATCHWON,
                                                TeamId.B);
                            }
                            if (setsA > (setScoreForWin - 1)) {
                                matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.MATCHWON,
                                                TeamId.A);
                            }

                        }
                    }
                    break;

                case ENDWON4:
                    if (TeamId.A == teamId) {

                        matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.ENDWON4, teamId);
                        currentEnd++;
                        if (finalSet)
                            pointsA++;
                        else
                            pointsA = pointsA + 4;

                        serveIncurrentEnd = switchServer(serveIncurrentEnd);
                        serve = serveIncurrentEnd;

                        if (isSetWinningGameScore(currentEnd, pointsA, pointsB)) {
                            if (pointsA < pointsB) {
                                matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.SETWON,
                                                TeamId.B);
                                setsB++;
                            } else if (pointsA == pointsB) {
                                matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.SETDRAW,
                                                TeamId.UNKNOWN);
                                setsB = setsB + 0.5;
                                setsA = setsA + 0.5;
                            } else {
                                matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.SETWON,
                                                TeamId.B);
                                setsA++;
                            }
                            if (!setRace)
                                matchEventResult.setRaceToXpoints(true);
                            if ((setsA == setScoreForWin - 1) && (setsB == setScoreForWin - 1))
                                finalSet = true;
                            currentEnd = 1;
                            gameScoreInSetN[(int) (setsA + setsB - 1)].A = pointsA;
                            gameScoreInSetN[(int) (setsA + setsB - 1)].B = pointsB;
                            pointsA = 0;
                            pointsB = 0;
                            setRace = true;
                            if (setsB > (setScoreForWin - 1)) {
                                matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.MATCHWON,
                                                TeamId.B);
                            }
                            if (setsA > (setScoreForWin - 1)) {
                                matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.MATCHWON);
                            }
                        }
                    } else {
                        matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.ENDWON4, teamId);
                        currentEnd++;
                        if (finalSet)
                            pointsB++;
                        else
                            pointsB = pointsB + 4;
                        serveIncurrentEnd = switchServer(serveIncurrentEnd);
                        serve = serveIncurrentEnd;

                        if (isSetWinningGameScore(currentEnd, pointsA, pointsB)) {
                            if (pointsA < pointsB) {
                                matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.SETWON,
                                                TeamId.B);
                                setsB++;
                            } else if (pointsA == pointsB) {
                                matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.SETDRAW);
                                setsB = setsB + 0.5;
                                setsA = setsA + 0.5;
                            } else {
                                matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.SETWON,
                                                TeamId.A);
                                setsA++;
                            }
                            if ((setsA == setScoreForWin - 1) && (setsB == setScoreForWin - 1))
                                finalSet = true;
                            currentEnd = 1;
                            gameScoreInSetN[(int) (setsA + setsB - 1)].A = pointsA;
                            gameScoreInSetN[(int) (setsA + setsB - 1)].B = pointsB;
                            pointsA = 0;
                            pointsB = 0;
                            setRace = true;
                            if (setsB > (setScoreForWin - 1)) {
                                matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.MATCHWON,
                                                TeamId.B);
                            }
                            if (setsA > (setScoreForWin - 1)) {
                                matchEventResult = new BowlsMatchIncidentResult(BowlsMatchIncidentResultType.MATCHWON,
                                                TeamId.A);
                            }

                        }
                    }
                    break;
                default:
                    break;
            }
        }
        matchEventResult.setPlayerAServedEnd(serveIncurrentEnd == TeamId.A);
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
        if ((x % 2) == 1)
            return false;
        else
            return true;

    }

    /**
     * Returns true if the games score is a set winning score
     * 
     * @param gamesWinner input numbers as gamesWinner
     * @param gamesLoser input numbers as gamesLoser
     * @return true or false
     */
    private boolean isSetWinningGameScore(int currentEnd, int pointsA, int pointsB) {
        if (finalSet) {
            return (currentEnd == endsInFinalSet + 1);
        } else {
            return (currentEnd == endScoreForWin + 1);
        }

    }

    @Override
    public MatchState copy() {
        BowlsMatchState cc = new BowlsMatchState(this.matchFormat);
        cc.setEqualTo(this);
        return cc;
    }

    @Override
    public void setEqualTo(MatchState matchState) {
        BowlsMatchState vs = (BowlsMatchState) matchState;
        this.setSetsA(vs.getSetsA());
        this.setSetsB(vs.getSetsB());
        this.setPointsA(vs.getPointsA());
        this.setPointsB(vs.getPointsB());
        this.setCurrentEnd(vs.getCurrentEnd());
        this.setSetRace(vs.isSetRace());
        this.setServe(vs.getServe());
        this.setServeInFirstSet(vs.getServeInFirstSet());
        this.setFinalSet(vs.isFinalSet());
        this.setScore(vs.getSetsA(), vs.getSetsB(), vs.getPointsA(), vs.getPointsB(), vs.getServe());
        for (int i = 0; i < matchFormat.getnSetsInMatch(); i++) {
            PairOfIntegers s = vs.getGameScoreInSetN(i);
            this.gameScoreInSetN[i].A = s.A;
            this.gameScoreInSetN[i].B = s.B;
        }
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
        String lastLegWinner = "PA"; // set default prompt to be whoever on the
                                     // last leg
        String matchWinner = "A";
        TeamId teamId = currentMatchState.getTeamId();
        switch (currentMatchState.getBowlsMatchIncidentResultType()) {
            case PREMATCH:
                matchIncidentPrompt = new MatchIncidentPrompt("Enter SA/SB as who serve first", "SA");
                break;
            case SERVEFIRST:
            case SERVENOW:
            case ENDWON1:
            case ENDWON2:
            case ENDWON3:
            case ENDWON4:
            case SETWON:
                if (TeamId.A == teamId)
                    lastLegWinner = "PA";
                else
                    lastLegWinner = "PB";
                matchIncidentPrompt =
                                new MatchIncidentPrompt("Enter winner of next end (PA/PB/1/2/3/4)", lastLegWinner);
                break;
            case SETDRAW:
                lastLegWinner = "PB";
                matchIncidentPrompt =
                                new MatchIncidentPrompt("Enter winner of next end (PA/PB/1/2/3/4)", lastLegWinner);
                break;
            case MATCHWON:
                if (TeamId.A == teamId)
                    matchWinner = "A";
                else
                    matchWinner = "B";
                matchIncidentPrompt =
                                new MatchIncidentPrompt(String.format("Match is finished - won by %s", matchWinner));
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
        BowlsMatchIncident matchEvent = new BowlsMatchIncident();
        TeamId teamId;
        switch (response.toUpperCase()) {
            case "PA":
            case "PA1":
                teamId = TeamId.A;
                matchEvent.set(BowlsMatchIncidentType.ENDWON1, teamId);
                break;
            case "PB":
            case "PB1":
                teamId = TeamId.B;
                matchEvent.set(BowlsMatchIncidentType.ENDWON1, teamId);
                break;
            case "PA2":
                teamId = TeamId.A;
                matchEvent.set(BowlsMatchIncidentType.ENDWON2, teamId);
                break;
            case "PB2":
                teamId = TeamId.B;
                matchEvent.set(BowlsMatchIncidentType.ENDWON2, teamId);
                break;
            case "PA3":
                teamId = TeamId.A;
                matchEvent.set(BowlsMatchIncidentType.ENDWON3, teamId);
                break;
            case "PB3":
                teamId = TeamId.B;
                matchEvent.set(BowlsMatchIncidentType.ENDWON3, teamId);
                break;
            case "PA4":
                teamId = TeamId.A;
                matchEvent.set(BowlsMatchIncidentType.ENDWON4, teamId);
                break;
            case "PB4":
                teamId = TeamId.B;
                matchEvent.set(BowlsMatchIncidentType.ENDWON4, teamId);
                break;
            case "SA":
                teamId = TeamId.A;
                matchEvent.set(BowlsMatchIncidentType.SERVEFIRST, teamId);
                break;
            case "SB":
                teamId = TeamId.B;
                matchEvent.set(BowlsMatchIncidentType.SERVEFIRST, teamId);
                break;
            case "A":
                teamId = TeamId.A;
                matchEvent.set(BowlsMatchIncidentType.SERVENOW, teamId);
                break;
            case "B":
                teamId = TeamId.B;
                matchEvent.set(BowlsMatchIncidentType.SERVENOW, teamId);
                break;
            default:
                return null; // invalid input so return null
        }
        return matchEvent;
    }

    private static final String poScoreKey = "point score";
    private static final String seScoreKey = "set score";
    private static final String cuEndKey = "current end";
    private static final String onServeKey = "serve now";
    private static final String serverCurrentEndKey = "current end first serve";

    /**
     * Returns map
     * 
     * @return map
     */
    @Override
    public LinkedHashMap<String, String> getAsMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

        map.put(poScoreKey, String.format("%d-%d", pointsA, pointsB));
        map.put(seScoreKey, String.format("%1$,.1f-%2$,.1f", setsA, setsB));
        map.put(cuEndKey, String.format("%s", currentEnd));
        map.put(onServeKey, String.format("%s", serve));
        map.put(serverCurrentEndKey, String.format("%s", serveIncurrentEnd));
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
     * Returns if the match is completed
     * 
     * @return true or false
     */
    @JsonIgnore
    @Override
    public boolean isMatchCompleted() {

        return (setsB > (setScoreForWin - 1)) || (setsA > (setScoreForWin - 1));
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
     * Returns serve in current End
     * 
     * @return serveIncurrentEnd
     */
    public TeamId getServeIncurrentEnd() {
        return serveIncurrentEnd;
    }

    /**
     * 
     * @param serveIncurrentEnd set serve in current end
     */
    public void setServeIncurrentEnd(TeamId serveIncurrentEnd) {
        this.serveIncurrentEnd = serveIncurrentEnd;
    }

    /**
     * 
     * 
     * @param matchFormat sets the volleyball match format
     * 
     */
    public void setMatchFormat(BowlsMatchFormat matchFormat) {
        this.matchFormat = matchFormat;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + bowlsInEnd;
        result = prime * result + currentEnd;
        result = prime * result + ((currentMatchState == null) ? 0 : currentMatchState.hashCode());
        result = prime * result + endScoreForWin;
        result = prime * result + endsInFinalSet;
        result = prime * result + (extraEnd ? 1231 : 1237);
        result = prime * result + (finalSet ? 1231 : 1237);
        result = prime * result + ((matchFormat == null) ? 0 : matchFormat.hashCode());
        result = prime * result + pointsA;
        result = prime * result + pointsB;
        result = prime * result + ((serve == null) ? 0 : serve.hashCode());
        result = prime * result + ((serveIncurrentEnd == null) ? 0 : serveIncurrentEnd.hashCode());
        result = prime * result + (setRace ? 1231 : 1237);
        result = prime * result + setScoreForDraw;
        long temp;
        temp = Double.doubleToLongBits(setScoreForWin);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(setsA);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(setsB);
        result = prime * result + (int) (temp ^ (temp >>> 32));
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
        BowlsMatchState other = (BowlsMatchState) obj;
        if (bowlsInEnd != other.bowlsInEnd)
            return false;
        if (currentEnd != other.currentEnd)
            return false;
        if (currentMatchState == null) {
            if (other.currentMatchState != null)
                return false;
        } else if (!currentMatchState.equals(other.currentMatchState))
            return false;
        if (endScoreForWin != other.endScoreForWin)
            return false;
        if (endsInFinalSet != other.endsInFinalSet)
            return false;
        if (extraEnd != other.extraEnd)
            return false;
        if (finalSet != other.finalSet)
            return false;
        for (int i = 0; i < gameScoreInSetN.length; i++) {
            if (gameScoreInSetN[i].A != other.gameScoreInSetN[i].A)
                return false;
            if (gameScoreInSetN[i].B != other.gameScoreInSetN[i].B)
                return false;
        }
        if (matchFormat == null) {
            if (other.matchFormat != null)
                return false;
        } else if (!matchFormat.equals(other.matchFormat))
            return false;
        if (pointsA != other.pointsA)
            return false;
        if (pointsB != other.pointsB)
            return false;
        if (serve != other.serve)
            return false;
        if (serveIncurrentEnd != other.serveIncurrentEnd)
            return false;
        if (setRace != other.setRace)
            return false;
        if (setScoreForDraw != other.setScoreForDraw)
            return false;
        if (Double.doubleToLongBits(setScoreForWin) != Double.doubleToLongBits(other.setScoreForWin))
            return false;
        if (Double.doubleToLongBits(setsA) != Double.doubleToLongBits(other.setsA))
            return false;
        if (Double.doubleToLongBits(setsB) != Double.doubleToLongBits(other.setsB))
            return false;
        return true;
    }

    /**
     * Returns current game period
     * 
     * @return gamePeriod
     */
    @Override
    public GamePeriod getGamePeriod() {
        GamePeriod gamePeriod = null;
        switch (currentMatchState.getBowlsMatchIncidentResultType()) {
            case PREMATCH:
                gamePeriod = GamePeriod.PREMATCH;
                break;
            case MATCHWON:
                gamePeriod = GamePeriod.POSTMATCH;
                break;
            case SETWON:
            case SERVEFIRST:
            case SERVENOW:
            case ENDWON1:
            case ENDWON2:
            case ENDWON3:
            case ENDWON4:
            case SETDRAW:
            case EXTRAEND:
                switch ((int) (this.getSetsA() + this.getSetsB() + 1)) {
                    case 1:
                        gamePeriod = GamePeriod.FIRST_SET;
                        break;
                    case 2:
                        gamePeriod = GamePeriod.SECOND_SET;
                        break;
                    case 3:
                        gamePeriod = GamePeriod.THIRD_SET;
                        break;
                }
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
     * Returns the sequence id to use for set based markets
     * 
     * @param setOffset 0 = current set, 1 = next set etc
     * @return null if specified set can't occur, else the sequence id
     */
    @JsonIgnore
    public String getSequenceIdforSet(int setOffset) {
        int setNo = (int) (this.getSetsA() + this.getSetsB() + setOffset + 1);
        if (setNo > matchFormat.getnSetsInMatch())
            return null;
        else
            return String.format("S%d", setNo);
    }

    /**
     * Returns the sequence id to use for end based markets (within the currently active end or tie break)
     * 
     * @param setOffset 0 = current end, 1 = next end etc
     * @return null if specified point can't occur, else the sequence id
     */
    @JsonIgnore
    public String getSequenceIdforEnd(int setOffset) {
        int endNo = this.getCurrentEnd();
        if (this.isFinalSet()) {
            if (endNo > matchFormat.getnEndInFinalSet())
                return null;
            else
                return String.format("E%s.%d", getSetNo(), endNo);
        } else {
            if (endNo > matchFormat.getnEndInSet())
                return null;
            else
                return String.format("E%s.%d", getSetNo(), endNo);
        }

    }

    /**
     * Returns the sequence id to use for point based markets (within the currently active game or tie break)
     * 
     * @param pointOffset 0 = current point, 1 = next point etc
     * @return null if specified point can't occur, else the sequence id
     */
    @JsonIgnore
    public String getSequenceIdForPoint(int pointOffset) {
        int pointNo = this.getPointsA() + this.getPointsB() + pointOffset;
        if (this.isPointMayBePlayed(pointNo))
            return String.format("S%d.%d", this.getSetNo(), pointNo);
        else
            return null;
    }

    /**
     * Returns true if within a game and point no n (starting at 1 for first point in game) might be played
     * 
     * @param n point No
     * @return true or false
     */
    @JsonIgnore
    boolean isPointMayBePlayed(int n) {
        return n >= getPointNo();
    }

    /**
     * Returns set no in range 1-7
     * 
     * @return setNo
     */
    @JsonIgnore
    public int getSetNo() {
        return (int) (setsA + setsB + 1);
    }

    /**
     * Returns point no starting at 1 for first point of end
     * 
     * @return pointNo
     */
    @JsonIgnore
    public int getPointNo() {
        int pointNo = pointsA + pointsB + 1;
        return pointNo + 1;
    }

    /**
     * Returns true if in preMatch
     * 
     * @return true or false
     */
    @Override
    public boolean preMatch() {
        return getServeInFirstSet() == TeamId.UNKNOWN;
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

    @JsonIgnore
    private LinkedHashMap<String, PairOfIntegers> getGamesScoreMap() {
        LinkedHashMap<String, PairOfIntegers> scoreMap = new LinkedHashMap<String, PairOfIntegers>();
        for (int i = 1; i < getSetNo(); i++) {
            scoreMap.put("scoreInSet" + Integer.toString(i), gameScoreInSetN[i - 1]);
        }
        return scoreMap;
    }

    @Override
    public MatchState generateSimpleMatchState() {
        return new BowlsSimpleMatchState(this.preMatch(), this.isMatchCompleted(), getSetsA(), getSetsB(),
                        getCurrentEnd(), isSetRace(), getServe(), getServeIncurrentEnd(), getBowlsInEnd(),
                        getEndScoreForWin(), getEndsInFinalSet(), getSetScoreForWin(), getSetScoreForDraw(),
                        getPointsA(), getPointsB(), isExtraEnd(), isFinalSet(), getGamesScoreMap());
    }

}
