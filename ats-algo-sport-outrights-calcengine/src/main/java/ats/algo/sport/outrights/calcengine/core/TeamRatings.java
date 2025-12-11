package ats.algo.sport.outrights.calcengine.core;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ats.algo.sport.outrights.calcengine.momentum.Momentum;
import ats.algo.sport.outrights.calcengine.momentum.Rating;
import ats.core.util.json.JsonUtil;

class TeamRatings {

    private Map<String, Rating> ratings;
    private Momentum momentum;

    public TeamRatings(Teams teams, RatingsFactors ratingsFactors, boolean ignoreBias) {
        ratings = new HashMap<>(teams.size());
        for (Team team : teams.getTeams().values()) {
            double biasAttack;
            double biasDefense;
            if (ignoreBias) {
                biasAttack = 0.0;
                biasDefense = 0.0;
            } else {
                biasAttack = team.getBiasAttack();
                biasDefense = team.getBiasDefense();
            }
            ratings.put(team.getTeamID(), new Rating(team.getRatingAttack() + biasAttack,
                            team.getRatingDefense() + biasDefense, ratingsFactors.getRatingsStdDevn()));
        }
        momentum = new Momentum(ratingsFactors);
    }

    public void updateRatings(Rating teamA, Rating teamB, FixtureResult result, boolean playedOnNeutralGround) {
        momentum.updateRatings(teamA, teamB, result.getScoreA(), result.getScoreB(), playedOnNeutralGround);
    }

    @JsonIgnore
    public Rating get(String teamId) {
        return ratings.get(teamId);
    }

    public Map<String, Rating> getRatings() {
        return ratings;
    }

    public String toString() {
        return JsonUtil.marshalJson(this);
    }

}
