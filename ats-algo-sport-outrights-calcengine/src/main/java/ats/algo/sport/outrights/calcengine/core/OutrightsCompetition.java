package ats.algo.sport.outrights.calcengine.core;

import java.util.Map;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.montecarloframework.MonteCarloMatch;
import ats.algo.sport.outrights.calcengine.leagues.LeagueMarketsFactory;
import ats.algo.sport.outrights.calcengine.momentum.Rating;

/**
 * simulates the playing of a single match
 * 
 * @author Geoff
 * 
 */
public class OutrightsCompetition extends MonteCarloMatch {
    private AbstractState simulationState;
    private Map<Long, MatchProbs> atsProbs;
    private MatchProbs matchProbs;
    private TeamRatings teamRatings;

    /**
     * 
     * @param competitionFormat
     * @param competitionState
     * @param competitionParams
     * @param atsProbs
     * @param marketsFactory
     */
    public OutrightsCompetition(AbstractFormat competitionFormat, AbstractState competitionState,
                    AbstractParams competitionParams, Map<Long, MatchProbs> atsProbs,
                    AbstractMarketsFactory marketsFactory) {
        super((MatchFormat) competitionFormat, (MatchState) competitionState, (MatchParams) competitionParams);
        monteCarloMarkets = marketsFactory;
        simulationState = (AbstractState) competitionState.copy();
        this.atsProbs = atsProbs;
        matchProbs = new MatchProbs();
        teamRatings = new TeamRatings(competitionState.competition.getTeams(),
                        competitionState.competition.getRatingsFactors());

    }

    @Override
    public MonteCarloMatch clone() {
        OutrightsCompetition cc = new OutrightsCompetition((AbstractFormat) this.matchFormat,
                        (AbstractState) this.matchState, (AbstractParams) this.matchParams, atsProbs,
                        ((AbstractMarketsFactory) monteCarloMarkets).copy());
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
            Rating ratingA = teamRatings.get(fixture.getHomeTeamID());
            Rating ratingB = teamRatings.get(fixture.getAwayTeamID());
            FixtureResult result = simulateFixture(ratingA, ratingB, fixture);
            simulationState.updateStateForFixtureResult(fixture, result);
            teamRatings.updateRatings(ratingA, ratingB, result, fixture.isPlayedAtNeutralGround());
        }
        ((AbstractMarketsFactory) monteCarloMarkets).updateStats(simulationState);
    }

    private FixtureResult simulateFixture(Rating ratingA, Rating ratingB, Fixture fixture) {
        Rating home = teamRatings.get(fixture.getHomeTeamID());
        Rating away = teamRatings.get(fixture.getAwayTeamID());
        MatchProbs probs = atsProbs.get(fixture.getEventID());
        if (probs == null) {
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
