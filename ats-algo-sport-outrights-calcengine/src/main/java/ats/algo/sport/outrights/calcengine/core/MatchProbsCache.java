package ats.algo.sport.outrights.calcengine.core;

import java.util.HashMap;
import java.util.Map;

public class MatchProbsCache {

    private Map<String, MatchProbs> cache;

    public MatchProbsCache(Competition competition, boolean ignoreBias) {
        Teams teams = competition.getTeams();
        Fixtures fixtures = competition.getFixtures();
        cache = new HashMap<>(fixtures.size());
        fixtures.forEach(fixture -> {
            String homeTeamID = fixture.getHomeTeamID();
            String awayTeamID = fixture.getAwayTeamID();
            if ((homeTeamID != null) && (awayTeamID != null)) {
                Team homeTeam = teams.get(homeTeamID);
                Team awayTeam = teams.get(awayTeamID);
                MatchProbs matchProbs = new MatchProbs();
                double homeAttack = calcRating(homeTeam.getRatingAttack(), homeTeam.getBiasAttack(), ignoreBias);
                double homeDefense = calcRating(homeTeam.getRatingDefense(), homeTeam.getBiasDefense(), ignoreBias);
                double awayAttack = calcRating(awayTeam.getRatingAttack(), awayTeam.getBiasAttack(), ignoreBias);
                double awayDefense = calcRating(awayTeam.getRatingDefense(), awayTeam.getBiasDefense(), ignoreBias);
                matchProbs.initialise(homeAttack, homeDefense, awayAttack, awayDefense, competition.getRatingsFactors(),
                                fixture.isPlayedAtNeutralGround());
                cache.put(fixture.getFixtureID(), matchProbs);
            }
        });
    }

    private double calcRating(double rating, double bias, boolean ignoreBias) {
        if (ignoreBias)
            return rating;
        else
            return rating + bias;
    }

    public MatchProbs get(String fixtureID) {
        return cache.get(fixtureID);
    }

}
