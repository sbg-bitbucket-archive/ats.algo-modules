package ats.algo.loadtestsport;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.montecarloframework.MonteCarloMatch;
import ats.algo.sport.football.FootballMatchFormat;
import ats.algo.sport.football.FootballMatchParams;
import ats.algo.sport.football.FootballMatchState;

public class LoadTestSportMatch extends MonteCarloMatch {


    public LoadTestSportMatch(FootballMatchFormat matchFormat, FootballMatchState matchState,
                    FootballMatchParams matchParams) {

        super((MatchFormat) matchFormat, (AlgoMatchState) matchState, (MatchParams) matchParams);
        monteCarloMarkets = new LoadTestSportMarketsFactory(matchState);
        /*
         * create the container for holding match facts just once rather than every time playMatch is executed -
         * improves performance
         */

    }

    @Override
    public MonteCarloMatch clone() {
        LoadTestSportMatch cc = new LoadTestSportMatch((FootballMatchFormat) matchFormat,
                        (FootballMatchState) matchState, (FootballMatchParams) matchParams);
        return cc;
    }

    @Override
    public void resetStatistics() {
        monteCarloMarkets = new LoadTestSportMarketsFactory((FootballMatchState) matchState);
    }


    @Override
    public void playMatch() {

        /*
         * do nothing
         */

    }

    @Override
    public void consolidateStats(MonteCarloMatch match) {
        this.monteCarloMarkets.consolidate(((LoadTestSportMatch) match).monteCarloMarkets);
    }

}
