package ats.algo.sport.tennis;
//

import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.RandomNoGenerator;
import ats.algo.montecarloframework.MonteCarloMatch;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchIncident;
import ats.algo.sport.tennis.TennisMatchIncidentResult;
import ats.algo.sport.tennis.TennisMatchParams;
import ats.algo.sport.tennis.TennisMatchState;
import ats.core.util.ExceptionUtil;
import ats.core.util.log.Logger;
import ats.core.util.log.LoggerFactory;
import ats.algo.sport.tennis.TennisMatchFormat.TournamentLevel;
import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;
import ats.algo.sport.tennis.TennisMatchIncidentResult.TennisMatchIncidentResultType;

/**
 * Implements a singles Tennis Match
 * 
 * @author Geoff
 * 
 */
class TennisMatch extends MonteCarloMatch {
    private static final Logger log = LoggerFactory.getLogger(TennisMatch.class);
    private TennisMatchFacts doubletennisMatchFacts;
    private TennisMatchState simulationMatchState;
    private TennisMatchIncident doubletennisMatchIncident;
    private boolean isDoubleFormat;
    @SuppressWarnings("unused")
    private boolean powerPointFormat;

    @SuppressWarnings("unused")
    private boolean isNoAdvantageFormat;

    /**
     * sets this instance of the class to point to the same instance of matchState and matchParameters as the parent
     * 
     * @param nSetsInMatch
     * @param finalSetTieBreak
     * @param doubletennisMatchState
     * @param doubletennisMatchParams
     */
    TennisMatch(TennisMatchFormat matchFormat, TennisMatchState matchState, TennisMatchParams matchParams) {
        super(matchFormat, matchState, matchParams);
        monteCarloMarkets = new TennisMatchMarketsFactory(matchFormat.getSetsPerMatch(), matchState);
        this.simulationMatchState = (TennisMatchState) matchState.copy();

        this.simulationMatchState.getGame().setRealMatch(false);
        this.simulationMatchState.getTieBreak().setRealMatch(false);
        this.simulationMatchState.getSuperTieBreak().setRealMatch(false);

        this.isDoubleFormat = ((TennisMatchState) matchState).isDoubleMatch();
        doubletennisMatchFacts = new TennisMatchFacts();
        doubletennisMatchIncident = new TennisMatchIncident();
        powerPointFormat = matchFormat.getTournamentLevel().equals(TournamentLevel.IPTL);
        isNoAdvantageFormat = matchFormat.isNoAdvantageGameFormat();
    }

    public TennisMatch clone() {
        TennisMatch cc = new TennisMatch((TennisMatchFormat) this.matchFormat, (TennisMatchState) this.matchState,
                        (TennisMatchParams) this.matchParams);
        return cc;
    }

    @Override
    public void resetStatistics() {
        int nSetsPerMatch = ((TennisMatchFormat) matchFormat).getSetsPerMatch();
        monteCarloMarkets = new TennisMatchMarketsFactory(nSetsPerMatch, (TennisMatchState) matchState);
    }

    @Override
    public void playMatch() {
        simulationMatchState.setEqualTo(matchState);
        doubletennisMatchFacts.reset((TennisMatchState) matchState);

        int startingSetNo = ((TennisMatchState) matchState).getSetNo();
        int startingGameNo = ((TennisMatchState) matchState).getGameNo();
        int startingPointNo = ((TennisMatchState) matchState).getPointNo();
        boolean firstToFiveGameReached = ((TennisMatchState) matchState).isAlreadyOver4GamesInCurrentSet();
        boolean inStartingTieBreak = ((TennisMatchState) matchState).isInTieBreak();
        /*
         * draw the skills as random variables from the respective Gaussian distributions
         */
        double skillA1 = 0.5;
        double skillA2 = 0.5;
        double skillB1 = 0.5;
        double skillB2 = 0.5;
        // FIXME: better solution is in progress
        do {
            skillA1 = ((TennisMatchParams) matchParams).getOnServePctA1().nextRandom();
            skillB1 = ((TennisMatchParams) matchParams).getOnServePctB1().nextRandom();
            skillA2 = ((TennisMatchParams) matchParams).getOnServePctA2().nextRandom();
            skillB2 = ((TennisMatchParams) matchParams).getOnServePctB2().nextRandom();

        } while ((skillA1 < 0.00001 && skillB1 < 0.00001) || (skillA1 > 0.9999 && skillB1 > 0.9999));
        /*
         * add a means of forcing generation of an exception to support testing
         */
        if (skillA1 < -50)
            throw new IllegalArgumentException("Exception forced via onServePctA1 < -50");

        /*
         * if pre-match set either player to serve first with equal probability
         */

        // change under codes to incoporate stronger player serve in new games

        if (simulationMatchState.getOnServeTeamNow() == TeamId.UNKNOWN) {
            if (RandomNoGenerator.nextBool()) {
                simulationMatchState.setOnServeSideNow(TeamId.A);

                if (isDoubleFormat) { // simulate double games
                    if (RandomNoGenerator.nextBool())
                        simulationMatchState.setOnServePlayerTeamANow(1); // initialise a serving order
                    else
                        simulationMatchState.setOnServePlayerTeamANow(2);

                    if (RandomNoGenerator.nextBool())
                        simulationMatchState.setOnServePlayerTeamBNow(1); // initialise b serving order
                    else
                        simulationMatchState.setOnServePlayerTeamBNow(2);
                } else { // simulate single games
                    simulationMatchState.setOnServePlayerTeamANow(1);
                    simulationMatchState.setOnServePlayerTeamBNow(1);
                }
            } else {
                simulationMatchState.setOnServeSideNow(TeamId.B);
                if (isDoubleFormat) {
                    if (RandomNoGenerator.nextBool())
                        simulationMatchState.setOnServePlayerTeamBNow(1);
                    else
                        simulationMatchState.setOnServePlayerTeamBNow(2);

                    if (RandomNoGenerator.nextBool())
                        simulationMatchState.setOnServePlayerTeamANow(1); // initialise a serving order
                    else
                        simulationMatchState.setOnServePlayerTeamANow(2);
                } else {
                    simulationMatchState.setOnServePlayerTeamANow(1);
                    simulationMatchState.setOnServePlayerTeamBNow(1);
                }
            }
        } else if (isDoubleFormat) {
            if (simulationMatchState.getOnServePlayerTeamANow() == 0) {
                if (RandomNoGenerator.nextBool())
                    simulationMatchState.setOnServePlayerTeamANow(1); // initialise a serving order
                else
                    simulationMatchState.setOnServePlayerTeamANow(2);
            }

            if (simulationMatchState.getOnServePlayerTeamBNow() == 0) {
                if (RandomNoGenerator.nextBool())
                    simulationMatchState.setOnServePlayerTeamBNow(1); // initialise a serving order
                else
                    simulationMatchState.setOnServePlayerTeamBNow(2);
            }

        } else if (!isDoubleFormat) { // single format
            simulationMatchState.setOnServePlayerTeamANow(1);
            simulationMatchState.setOnServePlayerTeamBNow(1);
        }

        TennisMatchIncidentResult doubletennisMatchIncidentResult;
        TeamId teamId;
        int calculationCount = 0;
        int maxCalculations = 100000;

        do {
            double r = RandomNoGenerator.nextDouble();
            boolean pointWonByTeamA;
            int playerId = 0;

            if (simulationMatchState.isNewSetSwitchServePlayer()) { // need to become strong player serve first
                // initialise player serving order for both team.
                if (RandomNoGenerator.nextBool()) {
                    simulationMatchState.setOnServePlayerTeamANow(1);
                } else {
                    simulationMatchState.setOnServePlayerTeamANow(2);
                }

                if (RandomNoGenerator.nextBool()) {
                    simulationMatchState.setOnServePlayerTeamBNow(1);
                } else {
                    simulationMatchState.setOnServePlayerTeamBNow(2);
                }

                simulationMatchState.setNewSetSwitchServePlayer(false);
            }

            if (simulationMatchState.getOnServeTeamNow() == TeamId.A)
                if (simulationMatchState.getOnServePlayerTeamANow() == 1) {
                    playerId = 1;
                    pointWonByTeamA = r < skillA1;
                } else if (simulationMatchState.getOnServePlayerTeamANow() == 2) {
                    playerId = 2;
                    pointWonByTeamA = r < skillA2;
                } else {
                    throw new IllegalArgumentException();
                }
            else if (simulationMatchState.getOnServeTeamNow() == TeamId.B) {
                if (simulationMatchState.getOnServePlayerTeamBNow() == 1) {
                    playerId = 1;
                    pointWonByTeamA = r < 1 - skillB1;
                } else if (simulationMatchState.getOnServePlayerTeamBNow() == 2) {
                    playerId = 2;
                    pointWonByTeamA = r < 1 - skillB2;
                } else {
                    throw new IllegalArgumentException();
                }
            } else {
                pointWonByTeamA = false;
                throw new IllegalArgumentException("Tennis MatchState on server now (getOnServeNow) : "
                                + simulationMatchState.getOnServeNow());
            }
            if (pointWonByTeamA)
                teamId = TeamId.A;
            else
                teamId = TeamId.B;
            /*
             * Record last Game No in case of entering next game or set
             */
            int lastGameNo = ((TennisMatchState) simulationMatchState).getGameNo(); // match state is already moved
            int lastPointNo = ((TennisMatchState) simulationMatchState).getPointNo();
            int lastSetNo = ((TennisMatchState) simulationMatchState).getSetNo();
            doubletennisMatchIncident.setTennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, teamId, playerId,
                            null);
            doubletennisMatchIncidentResult = (TennisMatchIncidentResult) simulationMatchState
                            .updateStateForIncident(doubletennisMatchIncident, false);

            int currentSetNo = ((TennisMatchState) simulationMatchState).getSetNo();
            int currentGameNo = ((TennisMatchState) simulationMatchState).getGameNo(); // match state is already moved
                                                                                       // forward
            int currentPointNo = ((TennisMatchState) simulationMatchState).getPointNo() - 1;

            // FIXME: debug

            if (doubletennisMatchIncidentResult
                            .getTennisMatchIncidentResultType() == TennisMatchIncidentResultType.GAMEWONA
                            || doubletennisMatchIncidentResult
                                            .getTennisMatchIncidentResultType() == TennisMatchIncidentResultType.GAMEWONB
                            || doubletennisMatchIncidentResult
                                            .getTennisMatchIncidentResultType() == TennisMatchIncidentResultType.SETWON
                            || doubletennisMatchIncidentResult
                                            .getTennisMatchIncidentResultType() == TennisMatchIncidentResultType.MATCHWONA
                            || doubletennisMatchIncidentResult
                                            .getTennisMatchIncidentResultType() == TennisMatchIncidentResultType.MATCHWONB) {
                currentGameNo = lastGameNo;
                currentPointNo = lastPointNo;
                if (doubletennisMatchIncidentResult
                                .getTennisMatchIncidentResultType() == TennisMatchIncidentResultType.SETWON
                                || doubletennisMatchIncidentResult
                                                .getTennisMatchIncidentResultType() == TennisMatchIncidentResultType.MATCHWONA
                                || doubletennisMatchIncidentResult
                                                .getTennisMatchIncidentResultType() == TennisMatchIncidentResultType.MATCHWONB) {
                    currentSetNo = lastSetNo;
                }



                /*
                 * update facts related to setXGameYAndXGameYPlusOneWinner markets
                 */


                if (currentSetNo == doubletennisMatchFacts.setX) {

                    if (currentGameNo == doubletennisMatchFacts.gameY + 1) {
                        if (pointWonByTeamA) {
                            doubletennisMatchFacts.setXGameYAndXGameYPlusOneWinner = TeamId.A;
                        } else {
                            doubletennisMatchFacts.setXGameYAndXGameYPlusOneWinner = TeamId.B;
                        }

                    } else if (currentGameNo == doubletennisMatchFacts.gameY) {

                        if (pointWonByTeamA) {
                            doubletennisMatchFacts.setXGameYAndXGameYWinner = TeamId.A;
                        } else {
                            doubletennisMatchFacts.setXGameYAndXGameYWinner = TeamId.B;
                        }
                    }

                }


            }


            /*
             * update facts related to point markets - only if still within current set
             */

            if (currentSetNo == startingSetNo) {
                if (currentGameNo == startingGameNo) {
                    switch (currentPointNo - startingPointNo) {
                        case 0:
                            doubletennisMatchFacts.thisGamePointEqualOneWinnerIsA = pointWonByTeamA;
                            break;
                        case 1:
                            doubletennisMatchFacts.thisGamePointPlusOneWinnerIsA = pointWonByTeamA;
                            break;
                        case 2:
                            doubletennisMatchFacts.thisGamePointPlusTwoWinnerIsA = pointWonByTeamA;
                            break;
                    }
                }
                if (currentGameNo == startingGameNo + 1) {
                    switch (currentPointNo) {
                        case 1:
                            doubletennisMatchFacts.nextGamePointOneWinnerIsA = pointWonByTeamA;
                            break;
                        case 2:
                            doubletennisMatchFacts.nextGamePointTwoWinnerIsA = pointWonByTeamA;
                            break;
                    }
                }
            }

            /*
             * update facts related to end of game or tie break
             */
            // boolean thisGameDeuceDetermined = false;
            // boolean nextGameDeuceDetermined = false;
            // boolean gamePlusTwoDeuceDetermined = false;
            switch (doubletennisMatchIncidentResult.getTennisMatchIncidentResultType()) {
                case POINTWONONLY:
                    /*
                     * do nothing
                     */
                    // if(isNoAdvantageFormat) // if no advantage format, game to deuce to be determined when points won
                    if (currentSetNo == startingSetNo) {
                        int pointsA1 = simulationMatchState.getLastGameOrTieBreakPointsA();
                        int pointsB1 = simulationMatchState.getLastGameOrTieBreakPointsB();
                        if (pointsA1 == 3 && pointsB1 == 3)
                            switch (currentGameNo - startingGameNo) {
                                case 0:
                                    doubletennisMatchFacts.thisGametoDeuce = true;
                                    break;
                                case 1:// was 1
                                    doubletennisMatchFacts.nextGameToDeuce = true;
                                    break;
                                case 2:// was 2
                                    doubletennisMatchFacts.gamePlusTwoToDeuce = true;
                                    break;
                                default:
                                    break;
                            }
                    }
                    break;
                case GAMEWONA:
                case GAMEWONB:
                case SETWON:
                case MATCHWONA:
                case MATCHWONB: {
                    int pointsA = simulationMatchState.getLastGameOrTieBreakPointsA();
                    int pointsB = simulationMatchState.getLastGameOrTieBreakPointsB();
                    if (inStartingTieBreak) {
                        doubletennisMatchFacts.tieBreakScoreA = pointsA;
                        doubletennisMatchFacts.tieBreakScoreB = pointsB;
                        doubletennisMatchFacts.tieBreakWinnerIsA = pointsA > pointsB;
                        inStartingTieBreak = false;
                    } else {
                        if (currentSetNo == startingSetNo)
                            switch (currentGameNo - startingGameNo) {
                                case 0:
                                    doubletennisMatchFacts.thisGameWinnerIsA = pointsA > pointsB;
                                    // if(!isNoAdvantageFormat) // if advantage format
                                    // {doubletennisMatchFacts.thisGametoDeuce = pointsA + pointsB == 8;}

                                    doubletennisMatchFacts.thisGameScoreA = pointsA;
                                    doubletennisMatchFacts.thisGameScoreB = pointsB;
                                    break;
                                case 1:// was 1
                                    doubletennisMatchFacts.nextGameWinnerIsA = pointsA > pointsB;
                                    // if(!isNoAdvantageFormat)
                                    // doubletennisMatchFacts.nextGameToDeuce = pointsA + pointsB == 8;
                                    doubletennisMatchFacts.nextGamePlayed = true;
                                    break;
                                case 2:// was 2
                                    doubletennisMatchFacts.gamePlusTwoWinnerIsA = pointsA > pointsB;
                                    // if(!isNoAdvantageFormat)
                                    // doubletennisMatchFacts.gamePlusTwoToDeuce = pointsA + pointsB == 8;
                                    doubletennisMatchFacts.gamePlusTwoPlayed = true;
                                    break;
                                default:
                                    break;
                            }
                    }
                    if ((currentSetNo == startingSetNo) && (!firstToFiveGameReached)) {
                        if (simulationMatchState.isAlreadyOver4GamesInCurrentSet()) {
                            doubletennisMatchFacts.firstToFiveGamesInThisSetIsA = simulationMatchState.getGamesA() == 5;
                            firstToFiveGameReached = true;
                        }
                    }
                    if (currentSetNo == startingSetNo) {
                        if (currentGameNo == 2) {
                            doubletennisMatchFacts.gameScoreAAfter2 =
                                            ((TennisMatchState) simulationMatchState).getGamesA();
                            doubletennisMatchFacts.gameScoreBAfter2 =
                                            ((TennisMatchState) simulationMatchState).getGamesB();
                        }
                        if (currentGameNo == 4) {
                            doubletennisMatchFacts.gameScoreAAfter4 =
                                            ((TennisMatchState) simulationMatchState).getGamesA();
                            doubletennisMatchFacts.gameScoreBAfter4 =
                                            ((TennisMatchState) simulationMatchState).getGamesB();
                        }
                        if (currentGameNo == 6) {
                            doubletennisMatchFacts.gameScoreAAfter6 =
                                            ((TennisMatchState) simulationMatchState).getGamesA();
                            doubletennisMatchFacts.gameScoreBAfter6 =
                                            ((TennisMatchState) simulationMatchState).getGamesB();
                        }
                    } else if (currentSetNo == startingSetNo + 1) {
                        if (((TennisMatchState) simulationMatchState).getGameScoreInSetN(currentSetNo - 2).A == 0
                                        && ((TennisMatchState) simulationMatchState)
                                                        .getGameScoreInSetN(currentSetNo - 2).B == 6) {
                            doubletennisMatchFacts.gameScoreAAfter6 = 0;
                            doubletennisMatchFacts.gameScoreBAfter6 = 6;
                        }
                        if (((TennisMatchState) simulationMatchState).getGameScoreInSetN(currentSetNo - 2).A == 6
                                        && ((TennisMatchState) simulationMatchState)
                                                        .getGameScoreInSetN(currentSetNo - 2).B == 0) {
                            doubletennisMatchFacts.gameScoreAAfter6 = 6;
                            doubletennisMatchFacts.gameScoreBAfter6 = 0;
                        }

                    } else if (simulationMatchState.isMatchCompleted() && doubletennisMatchFacts.gameScoreAAfter6 == 0
                                    && doubletennisMatchFacts.gameScoreBAfter6 == 0) {
                        doubletennisMatchFacts.gameScoreAAfter6 = ((TennisMatchState) simulationMatchState)
                                        .getGameScoreInSetN(currentSetNo - 2).A;
                        doubletennisMatchFacts.gameScoreAAfter6 = ((TennisMatchState) simulationMatchState)
                                        .getGameScoreInSetN(currentSetNo - 2).B;
                    }
                }
                default:
                    break;
            }

        } while (++calculationCount < maxCalculations
                        && doubletennisMatchIncidentResult
                                        .getTennisMatchIncidentResultType() != TennisMatchIncidentResultType.MATCHWONA
                        && doubletennisMatchIncidentResult
                                        .getTennisMatchIncidentResultType() != TennisMatchIncidentResultType.MATCHWONB);

        if (calculationCount >= maxCalculations) {
            log.error("max calculations breach : incident = %s, simulationMatchState = %s",
                            doubletennisMatchIncidentResult.getTennisMatchIncidentResultType(), simulationMatchState);
            throw ExceptionUtil.unexpectedException("max calculations breach");
        }
        ((TennisMatchMarketsFactory) monteCarloMarkets).updateStats(simulationMatchState, doubletennisMatchFacts);
    }
}
