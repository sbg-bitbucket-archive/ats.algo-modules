package ats.algo.sport.tennisG;
//

import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.RandomNoGenerator;
import ats.algo.montecarloframework.MonteCarloMatch;
import ats.algo.sport.tennisG.TennisGMatchFormat;
import ats.algo.sport.tennisG.TennisGMatchIncident;
import ats.algo.sport.tennisG.TennisGMatchIncidentResult;
import ats.algo.sport.tennisG.TennisGMatchParams;
import ats.algo.sport.tennisG.TennisGMatchState;
import ats.algo.sport.tennisG.TennisGMatchIncident.TennisMatchIncidentType;
import ats.algo.sport.tennisG.TennisGMatchIncidentResult.TennisMatchIncidentResultType;

/**
 * Implements a singles Tennis Match
 * 
 * @author Geoff
 * 
 */
class TennisGMatch extends MonteCarloMatch {
    private TennisGMatchFacts tennisMatchFacts;
    private TennisGMatchState simulationMatchState;
    private TennisGMatchIncident tennisMatchIncident;

    /**
     * sets this instance of the class to point to the same instance of matchState and matchParameters as the parent
     * 
     * @param nSetsInMatch
     * @param finalSetTieBreak
     * @param tennisMatchState
     * @param tennisMatchParams
     */
    TennisGMatch(TennisGMatchFormat matchFormat, TennisGMatchState matchState, TennisGMatchParams matchParams) {
        super(matchFormat, matchState, matchParams);
        monteCarloMarkets = new TennisGMatchMarketsFactory(matchFormat.getSetsPerMatch(), matchState);
        this.simulationMatchState = (TennisGMatchState) matchState.copy();
        tennisMatchFacts = new TennisGMatchFacts();
        tennisMatchIncident = new TennisGMatchIncident();
    }

    public TennisGMatch clone() {
        TennisGMatch cc = new TennisGMatch((TennisGMatchFormat) this.matchFormat, (TennisGMatchState) this.matchState,
                        (TennisGMatchParams) this.matchParams);
        return cc;
    }

    @Override
    public void resetStatistics() {
        int nSetsPerMatch = ((TennisGMatchFormat) matchFormat).getSetsPerMatch();
        monteCarloMarkets = new TennisGMatchMarketsFactory(nSetsPerMatch, (TennisGMatchState) matchState);
    }

    @Override
    public void playMatch() {
        simulationMatchState.setEqualTo(matchState);
        tennisMatchFacts.reset((TennisGMatchState) matchState);
        int startingSetNo = ((TennisGMatchState) matchState).getSetNo();
        int startingGameNo = ((TennisGMatchState) matchState).getGameNo();
        int startingPointNo = ((TennisGMatchState) matchState).getPointNo();
        boolean firstToFiveGameReached = ((TennisGMatchState) matchState).isAlreadyOver4GamesInCurrentSet();
        boolean inStartingTieBreak = ((TennisGMatchState) matchState).isInTieBreak();
        /*
         * draw the skills as random variables from the respective Gaussian distributions
         */
        double skillA = ((TennisGMatchParams) matchParams).getOnServePctA().nextRandom();
        double skillB = ((TennisGMatchParams) matchParams).getOnServePctB().nextRandom();
        /*
         * if pre-match set either player to serve first with equal probability
         */
        if (simulationMatchState.getOnServeNow() == TeamId.UNKNOWN)
            if (RandomNoGenerator.nextBool())
                simulationMatchState.setOnServeNow(TeamId.A);
            else
                simulationMatchState.setOnServeNow(TeamId.B);
        TennisGMatchIncidentResult tennisMatchIncidentResult;
        TeamId playerId;
        do {
            double r = RandomNoGenerator.nextDouble();
            boolean pointWonByA;
            if (simulationMatchState.getOnServeNow() == TeamId.A)
                pointWonByA = r < skillA;
            else
                pointWonByA = r < 1 - skillB;
            if (pointWonByA)
                playerId = TeamId.A;
            else
                playerId = TeamId.B;
            tennisMatchIncident.setTennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, playerId);
            tennisMatchIncidentResult = (TennisGMatchIncidentResult) simulationMatchState
                            .updateStateForIncident(tennisMatchIncident, false);
            int currentSetNo = ((TennisGMatchState) simulationMatchState).getSetNo();
            int currentGameNo = ((TennisGMatchState) simulationMatchState).getGameNo();
            int currentPointNo = ((TennisGMatchState) simulationMatchState).getPointNo();
            /*
             * update facts related to point markets - only if still within current set
             */
            if (currentSetNo == startingSetNo) {
                if (currentGameNo == startingGameNo) {
                    switch (currentPointNo - startingPointNo - 1) {
                        case 2:
                            tennisMatchFacts.thisGamePointPlusTwoWinnerIsA = pointWonByA;
                            break;
                        case 3:
                            tennisMatchFacts.thisGamePointPlusThreeWinnerIsA = pointWonByA;
                            break;
                    }
                }
                if (currentGameNo == startingGameNo + 1) {
                    switch (currentPointNo) {
                        case 1:
                            tennisMatchFacts.nextGamePointOneWinnerIsA = pointWonByA;
                            break;
                        case 2:
                            tennisMatchFacts.nextGamePointTwoWinnerIsA = pointWonByA;
                            break;
                    }
                }
            }

            /*
             * update facts related to end of game or tie break
             */
            switch (tennisMatchIncidentResult.getTennisMatchIncidentResultType()) {
                case POINTWONONLY:
                    /*
                     * do nothing
                     */
                    break;
                case GAMEWONA:
                case GAMEWONB:
                case SETWONA:
                case SETWONB:
                case MATCHWONA:
                case MATCHWONB: {
                    int pointsA = simulationMatchState.getLastGameOrTieBreakPointsA();
                    int pointsB = simulationMatchState.getLastGameOrTieBreakPointsB();
                    if (inStartingTieBreak) {
                        tennisMatchFacts.tieBreakScoreA = pointsA;
                        tennisMatchFacts.tieBreakScoreB = pointsB;
                        tennisMatchFacts.tieBreakWinnerIsA = pointsA > pointsB;
                        inStartingTieBreak = false;
                    } else {
                        if (currentSetNo == startingSetNo)
                            switch (currentGameNo - startingGameNo - 1) {
                                case 0:
                                    tennisMatchFacts.thisGameWinnerIsA = pointsA > pointsB;
                                    tennisMatchFacts.thisGametoDeuce = pointsA + pointsB == 8;
                                    tennisMatchFacts.thisGameScoreA = pointsA;
                                    tennisMatchFacts.thisGameScoreB = pointsB;
                                    break;
                                case 1:
                                    tennisMatchFacts.nextGameWinnerIsA = pointsA > pointsB;
                                    tennisMatchFacts.nextGameToDeuce = pointsA + pointsB == 8;
                                    break;
                                case 2:
                                    tennisMatchFacts.gamePlusTwoWinnerIsA = pointsA > pointsB;
                                    tennisMatchFacts.gamePlusTwoToDeuce = pointsA + pointsB == 8;
                                    break;
                                default:
                                    break;
                            }
                    }
                    if ((currentSetNo == startingSetNo) && (!firstToFiveGameReached)) {
                        if (simulationMatchState.isAlreadyOver4GamesInCurrentSet()) {
                            tennisMatchFacts.firstToFiveGamesInThisSetIsA = simulationMatchState.getGamesA() == 5;
                            firstToFiveGameReached = true;
                        }
                    }
                }
                default:
                    break;
            }

        } while (tennisMatchIncidentResult.getTennisMatchIncidentResultType() != TennisMatchIncidentResultType.MATCHWONA
                        && tennisMatchIncidentResult
                                        .getTennisMatchIncidentResultType() != TennisMatchIncidentResultType.MATCHWONB);
        ((TennisGMatchMarketsFactory) monteCarloMarkets).updateStats(simulationMatchState, tennisMatchFacts);
    }
}
