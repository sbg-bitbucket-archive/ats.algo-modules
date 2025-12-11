package ats.algo.sport.rugbyleague;

import ats.algo.genericsupportfunctions.MethodName;
import org.junit.Test;

import ats.algo.sport.rugbyleague.RugbyLeagueMatch;
import ats.algo.sport.rugbyleague.RugbyLeagueMatchFormat;
import ats.algo.sport.rugbyleague.RugbyLeagueMatchParams;
import ats.algo.sport.rugbyleague.RugbyLeagueMatchState;

public class RugbyLeagueMatchTest {

    @Test
    public void test() {
        MethodName.log();
        RugbyLeagueMatchFormat matchFormat = new RugbyLeagueMatchFormat();
        RugbyLeagueMatchState matchState = new RugbyLeagueMatchState(matchFormat);
        RugbyLeagueMatchParams matchParams = new RugbyLeagueMatchParams();
        matchParams.setScoreTotal(58.0, 5);
        matchParams.setScoreSupremacy(5.5, 0);
        matchParams.setHomeLoseBoost(0.0, 0);
        matchParams.setAwayLoseBoost(0.0, 0);
        RugbyLeagueMatch match = new RugbyLeagueMatch(matchFormat, matchState, matchParams);
        match.playMatch();
    }
}
