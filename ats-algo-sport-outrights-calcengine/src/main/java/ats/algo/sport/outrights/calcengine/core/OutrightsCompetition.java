package ats.algo.sport.outrights.calcengine.core;

import java.util.Map;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.montecarloframework.MonteCarloMatch;
import ats.algo.sport.outrights.calcengine.leagues.LeagueMarketsFactory;
import ats.algo.sport.outrights.calcengine.momentum.Rating;

/**
 * simulates a single iteration of the competition
 * 
 * @author Geoff
 * 
 */
public class OutrightsCompetition extends MonteCarloMatch {
    private AbstractState simulationState;
    private Map<Long, FullMatchProbs> atsProbs;
    private MatchProbsCache matchProbsCache;
    private MatchProbs matchProbs;
    private TeamRatings teamRatings;
    private boolean ignoreBias;
    private boolean usingStdDevns;

    /**
     * 
     * @param competitionFormat
     * @param competitionState
     * @param competitionParams
     * @param atsProbs
     * @param marketsFactory
     * @param matchProbsCache
     * @param ignoreBias - set true if ignore bias when doing calc - e.g. while param finding
     */
    public OutrightsCompetition(AbstractFormat competitionFormat, AbstractState competitionState,
                    AbstractParams competitionParams, Map<Long, FullMatchProbs> atsProbs,
                    AbstractMarketsFactory marketsFactory, MatchProbsCache matchProbsCache, boolean ignoreBias) {
        super((MatchFormat) competitionFormat, (AlgoMatchState) competitionState, (MatchParams) competitionParams);
        monteCarloMarkets = marketsFactory;
        simulationState = (AbstractState) competitionState.copy();
        this.atsProbs = atsProbs;
        this.matchProbsCache = matchProbsCache;
        this.usingStdDevns = matchProbsCache == null;
        this.ignoreBias = ignoreBias;
        matchProbs = new MatchProbs();
        teamRatings = new TeamRatings(competitionState.competition.getTeams(),
                        competitionState.competition.getRatingsFactors(), ignoreBias);

    }

    @Override
    public MonteCarloMatch clone() {
        OutrightsCompetition cc = new OutrightsCompetition((AbstractFormat) this.matchFormat,
                        (AbstractState) this.matchState, (AbstractParams) this.matchParams, atsProbs,
                        ((AbstractMarketsFactory) monteCarloMarkets).copy(), this.matchProbsCache, this.ignoreBias);
        return cc;
    }

    /**
     * simulate a single run through the entire competition
     */
    @Override
    public void playMatch() {
        simulationState.setEqualTo(matchState);
        while (!simulationState.competitionCompleted()) {
            Fixture fixture = simulationState.nextFixture();

            FixtureResult result = simulateFixture(fixture);
            simulationState.updateStateForFixtureResult(fixture, result);
            if (this.usingStdDevns) {
                Rating ratingA = teamRatings.get(fixture.getHomeTeamID());
                Rating ratingB = teamRatings.get(fixture.getAwayTeamID());
                teamRatings.updateRatings(ratingA, ratingB, result, fixture.isPlayedAtNeutralGround());
            }
        }
        ((AbstractMarketsFactory) monteCarloMarkets).updateStats(simulationState);
    }

    private FixtureResult simulateFixture(Fixture fixture) {
        /*
         * see whether matchProbs are being driven by an ats event
         */
        MatchProbs probs = atsProbs.get(fixture.getEventID());
        /*
         * next see whether there is a cache entry for this fixture
         */
        if (probs == null && !usingStdDevns) {
            probs = this.matchProbsCache.get(fixture.getFixtureID());
        }
        /*
         * if both those fail generate the matchProbs (which is computationally expensive)
         */
        if (probs == null) {
            Rating home = teamRatings.get(fixture.getHomeTeamID());
            Rating away = teamRatings.get(fixture.getAwayTeamID());
            probs = matchProbs;
            probs.initialise(home.getRatingAttack().nextRandom(), home.getRatingDefense().nextRandom(),
                            away.getRatingAttack().nextRandom(), away.getRatingDefense().nextRandom(),
                            simulationState.competition.getRatingsFactors(), fixture.isPlayedAtNeutralGround());
        }
        return probs.simulateMatch(fixture.isMustHaveAWinner());
    }

    @Override
    public void consolidateStats(MonteCarloMatch cc) {
        LeagueMarketsFactory srcStats = (LeagueMarketsFactory) ((OutrightsCompetition) cc).monteCarloMarkets;
        this.monteCarloMarkets.consolidate(srcStats);
    }

    @Override
    public void resetStatistics() {
        monteCarloMarkets.reset();
    }

    public AbstractState getSimulationState() {
        return simulationState;
    }

}
