package ats.algo.sport.darts;

import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.RandomNoGenerator;
import ats.algo.montecarloframework.MonteCarloMatch;
import ats.algo.sport.darts.DartMatchFormat;
import ats.algo.sport.darts.DartMatchParams;
import ats.algo.sport.darts.DartMatchState;
import ats.algo.sport.darts.DartProbs.BaseProbSet;
import ats.algo.sport.darts.LegWinResult.LegWinType;

/**
 * Contains all the calcs specific to Darts
 * 
 * @author Geoff
 * 
 */
class DartMatch extends MonteCarloMatch {

    // following properties are dynamic so each thread has it's own copy
    private PlayLeg dl;
    private DartMatchState simulationMatchState;
    private DartMatchFacts dartMatchFacts;
    private DartProbs probsA;
    private DartProbs probsB;

    /**
     * 
     * 
     * @param useLegWinnerProbTables
     * @param nLegsPerSet
     * @param nLegsOrSetsPerMatch
     */
    public DartMatch(DartMatchFormat matchFormat, DartMatchState matchState, DartMatchParams matchParams) {
        super(matchFormat, matchState, matchParams);

        dl = new PlayLeg();
        simulationMatchState = matchState.copy();
        monteCarloMarkets = new DartMatchMarketsFactory(matchState);
        dartMatchFacts = new DartMatchFacts();
        setProbSet(BaseProbSet.LIVE);
    }

    void setProbSet(BaseProbSet probSet) {
        probsA = new DartProbs(probSet, 1, 0);
        probsB = new DartProbs(probSet, 1, 0);
    }

    @Override
    public void playMatch() {
        simulationMatchState.setEqualTo(matchState);
        /*
         * set the initial counts for the part of the match that has already been played
         */

        dartMatchFacts.resetCountsForNextMatchIteration((DartMatchState) matchState);
        /*
         * set the player skills for this iteration of the match
         */
        double skillA = ((DartMatchParams) matchParams).getSkillA().nextRandom();
        double skillB = ((DartMatchParams) matchParams).getSkillB().nextRandom();
        double triplesVsDoublesA = ((DartMatchParams) matchParams).getTriplesVsDoublesA().nextRandom();
        double triplesVsDoublesB = ((DartMatchParams) matchParams).getTriplesVsDoublesB().nextRandom();
        probsA.updateProbs(skillA, triplesVsDoublesA);
        probsB.updateProbs(skillB, triplesVsDoublesB);
        dl.setPlayers(probsA, probsB);
        int currentLegNoPlusN = 0;
        int currentsetNoPlusN = 0;
        //
        // if pre-match then choose either player to start with equal probability
        //
        if (simulationMatchState.getPlayerAtOcheAtStartOfCurrentLeg() == TeamId.UNKNOWN) {
            if (RandomNoGenerator.nextBool()) {
                simulationMatchState.setPlayerAtOcheAtStartOfCurrentLeg(TeamId.A);
            } else {
                simulationMatchState.setPlayerAtOcheAtStartOfCurrentLeg(TeamId.B);
            }
        }

        LegResult legResult;
        LegWinResult legWinResult;

        boolean matchFinished = false;
        do {
            legResult = dl.play(simulationMatchState.getCurrentLeg());
            legWinResult = simulationMatchState.legWon(legResult.legWinner);
            dartMatchFacts.updateFactsWithLegResults(legResult, legWinResult, currentLegNoPlusN, currentsetNoPlusN);

            currentLegNoPlusN++;
            if (legWinResult.legWinType == LegWinType.IsSetWinner)
                currentsetNoPlusN++;
            matchFinished = (legWinResult.legWinType == LegWinType.IsMatchWinner)
                            || (legWinResult.legWinType == LegWinType.IsMatchDraw);
        } while (!matchFinished);
        ((DartMatchMarketsFactory) monteCarloMarkets).updateStats(simulationMatchState, dartMatchFacts);
    }



    @Override
    public void consolidateStats(MonteCarloMatch cc) {
        monteCarloMarkets.consolidate(((DartMatch) cc).monteCarloMarkets);
    }

    @Override
    public MonteCarloMatch clone() {
        DartMatch cc = new DartMatch((DartMatchFormat) this.matchFormat, (DartMatchState) this.matchState,
                        (DartMatchParams) this.matchParams);
        return cc;
    }

    // public void resetStats() {
    // monteCarloMarkets.reset();
    // }

    @Override
    public void resetStatistics() {
        monteCarloMarkets = new DartMatchMarketsFactory((DartMatchState) matchState);
    }

}
