package ats.algo.montecarloframework;


import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.genericsupportfunctions.RandomNoGenerator;
import ats.algo.montecarloframework.MonteCarloMatch;

/**
 * simple test match implementing a best of n match format, and collecting the statistics defined in TestStatistics
 * 
 * @author Geoff
 * 
 */
class TestMatch extends MonteCarloMatch {

    private TestMatchState simulationMatchState;



    public TestMatch(TestMatchFormat testMatchFormat, TestMatchState testMatchState, TestMatchParams testMatchParams) {
        super((MatchFormat) testMatchFormat, (MatchState) testMatchState, (MatchParams) testMatchParams);

        monteCarloMarkets = new TestStatistics(testMatchFormat.nSets);
        simulationMatchState = new TestMatchState(testMatchFormat.nSets, testMatchParams.probAWinsSet);
    }


    @Override
    public MonteCarloMatch clone() {
        TestMatch cc = new TestMatch((TestMatchFormat) this.matchFormat, (TestMatchState) this.matchState,
                        (TestMatchParams) this.matchParams);
        return cc;
    }

    @Override
    public void playMatch() {
        simulationMatchState.setEqualTo(matchState);
        TestMatchState.MatchEventOutcome matchEventOutcome;
        do {
            double r = RandomNoGenerator.nextDouble();
            boolean pAWins = r < simulationMatchState.probAWinsSet;
            matchEventOutcome = simulationMatchState.updateStateForEvent(pAWins);
        } while (!matchEventOutcome.isMatchWinner);
        ((TestStatistics) monteCarloMarkets).updateStats(matchEventOutcome.scoreA, matchEventOutcome.scoreB);
    }

    @Override
    public void consolidateStats(MonteCarloMatch cc) {
        TestStatistics srcStats = (TestStatistics) ((TestMatch) cc).monteCarloMarkets;
        this.monteCarloMarkets.consolidate(srcStats);
    }

    @Override
    public void resetStatistics() {
        monteCarloMarkets.reset();
    }
}
