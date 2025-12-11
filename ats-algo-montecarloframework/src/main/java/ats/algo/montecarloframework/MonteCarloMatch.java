package ats.algo.montecarloframework;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.markets.Markets;

/**
 * Contains all the logic associated with a specific sport, e.g. darts or tennis. The minimal set of methods required by
 * the concurrent calculation framework are given by this interface specification. Classes derived from this should not
 * hold any persistent state information other than what is in MatchState and MatchParams.
 * 
 * @author Geoff
 * 
 */
public abstract class MonteCarloMatch {
    protected MatchFormat matchFormat;
    protected MatchState matchState;
    protected MatchParams matchParams;
    protected MarketsFactory monteCarloMarkets;
    protected boolean isForParamFinder;

    protected MonteCarloMatch(MatchFormat matchFormat, MatchState matchState, MatchParams matchParams) {
        this.matchFormat = matchFormat;
        this.matchState = matchState;
        this.matchParams = matchParams;
        isForParamFinder = false;

    }

    public MatchState getMatchState() {
        return matchState;
    }

    public MatchParams getMatchParams() {
        return matchParams;
    }

    /**
     * Makes a shallow copy of the Match object. the new copy MUST point to the same instances of matchState and
     * matchParams
     * 
     * @return the cloned copy
     */
    public abstract MonteCarloMatch clone();

    /**
     * runs a single simulation of the match starting from the current MatchState through to match won/lost/drawn. This
     * the playMatch method is called many times by the MC framework to build up the probabilities. It therefore needs
     * to be efficiently implemented
     */
    public abstract void playMatch();

    /**
     * add the stats from the cc Match object into this object's stats
     * 
     * @param cc
     */
    public void consolidateStats(MonteCarloMatch cc) {
        this.monteCarloMarkets.consolidate(cc.monteCarloMarkets);

    };

    /**
     * clear any currently collected stats
     */
    public void resetStatistics() {
        monteCarloMarkets.reset();
    }

    /**
     * generates the set of markets associated with this sport. Only markets that should be generated given the current
     * matchState are returned.
     * 
     * @return The set of markets
     */
    public Markets generateMarkets() {
        Markets markets = monteCarloMarkets.generateMonteCarloMarkets();
        if (!isForParamFinder) {
            /*
             * only generate derived markets if not param finding
             */
            monteCarloMarkets.addDerivedMarkets(markets, matchState, matchParams);
        }
        return markets;
    }


}
