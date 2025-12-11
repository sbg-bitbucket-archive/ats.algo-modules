package ats.algo.loadtestsport;

import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.core.markets.Markets;
import ats.algo.loadtestsport.LoadTestSportMatchEngine;
import ats.algo.sport.football.FootballMatchFormat;
import ats.algo.sport.football.FootballMatchState;

public class testTestSport {
    @Test
    public void test() {
        FootballMatchFormat matchFormat = new FootballMatchFormat();
        LoadTestSportMatchEngine matchEngine = new LoadTestSportMatchEngine(matchFormat);
        FootballMatchState matchState = (FootballMatchState) matchEngine.getMatchState();
        String requestId = "TestRequestId";
        matchState.setIncidentId(requestId);
        matchEngine.setMatchState(matchState);
        matchEngine.calculate();
        Markets markets = matchEngine.getCalculatedMarkets();
        double pA = markets.get("AB").get("A");
        assertEquals(getTestProb(requestId), pA, 0.0000001);
    }

    private double getTestProb(String requestId) {
        double h = Math.abs(requestId.hashCode());
        double prob = h / ((double) Integer.MAX_VALUE);
        return prob;
    }
}
