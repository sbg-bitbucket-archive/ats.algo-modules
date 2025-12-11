package ats.algo.sport.outrights.calcengine.core;

import ats.algo.core.markets.Market;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarket;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.markets.Selection;
import ats.algo.montecarloframework.ConcurrentCalculationManager;
import ats.algo.montecarloframework.MatchEngineThreadPool;
import ats.algo.montecarloframework.MonteCarloMatch;
import ats.algo.montecarloframework.matchengine.MonteCarloMatchEngine;
import ats.algo.sport.outrights.calcengine.core.AbstractFormat;
import ats.algo.sport.outrights.calcengine.core.AbstractMarketsFactory;
import ats.algo.sport.outrights.calcengine.core.AbstractParams;
import ats.algo.sport.outrights.calcengine.core.AbstractState;
import ats.algo.sport.outrights.calcengine.core.Competition;
import ats.algo.sport.outrights.calcengine.core.OutrightsCompetition;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorCompletionService;

/**
 * perform the calcs for a single competition
 * 
 * @author gicha
 *
 */
class CalcEngine extends MonteCarloMatchEngine {

    private static int nThreads;
    private static final int MAX_THREADS = 8;

    public static int getnThreads() {
        return nThreads;
    }

    public static void setnThreads(int nThreads) {
        CalcEngine.nThreads = nThreads;
    }

    private static final int N_ITERATIONS = 64000;

    private Competition competition;
    private boolean ignoreBias;
    AbstractMarketsResulter resulter;


    public CalcEngine(Competition competition) {
        super(null);
        nThreads = Runtime.getRuntime().availableProcessors();
        if (nThreads > MAX_THREADS)
            nThreads = MAX_THREADS;
        this.competition = competition;
        this.ignoreBias = false;

    }

    public void setIgnoreBias(boolean ignoreBias) {
        this.ignoreBias = ignoreBias;
    }

    /**
     * for testing purposes only
     * 
     * @return
     */
    AbstractMarketsResulter getResulter() {
        return resulter;
    }


    @Override
    public void calculate() {
        AbstractFormat competitionFormat = CompetitionProperties.competitionMatchFormatInstance(competition);
        AbstractParams matchParams = new AbstractParams();
        AbstractState competitionState = CompetitionProperties.competitionStateInstance(competition, competitionFormat);

        AbstractMarketsFactory marketsFactory = CompetitionProperties.competitionMarketsFactoryInstance(competition);
        MatchProbsCache matchProbsCache = null;
        if (competition.getRatingsFactors().getRatingsStdDevn() == 0.0)
            matchProbsCache = new MatchProbsCache(competition, ignoreBias);

        OutrightsCompetition match = new OutrightsCompetition(competitionFormat, competitionState, matchParams,
                        competition.atsFixtureProbs(), marketsFactory, matchProbsCache, ignoreBias);
        MatchEngineThreadPool matchEngineThreadPool = null;
        ExecutorCompletionService<Boolean> completionService = null;
        if (nThreads > 1) {
            matchEngineThreadPool = new MatchEngineThreadPool(nThreads - 1);
            completionService = matchEngineThreadPool.getExecutorCompletionService();
        }
        ConcurrentCalculationManager ccm = new ConcurrentCalculationManager(nThreads, N_ITERATIONS,
                        (MonteCarloMatch) match, completionService);
        ccm.calculate();
        if (matchEngineThreadPool != null)
            matchEngineThreadPool.close();
        Markets markets = match.generateMarkets();
        Markets markets2 = OutrightTradingRules.apply(competition.getTeams(), competition.getFixtures(), markets,
                        competition.isSuspendMarkets());
        resulter = CompetitionProperties.competitionResulterInstance(competition);
        ResultedMarkets newResultedMarkets = resulter.execute(markets2);
        filterMarketsForResultedMarkets(markets, newResultedMarkets);
        filterSelections(markets2);
        competition.setMarkets(markets2);
        AbstractMarketsResulter.filterAlreadyPublished(newResultedMarkets,
                        competition.getPreviouslyPublishedResultedMarkets());
        competition.setNewResultedMarkets(newResultedMarkets);
        competition.setFcastStandings(marketsFactory.generateFcastStandings(competition.getFcastStandings()));
    }

    /**
     * remove fullyResultedMarkets and resultedSelections from partiallyResultedMarkets from markets
     * 
     * @param markets
     * @param resultedMarkets
     */
    private void filterMarketsForResultedMarkets(Markets markets, ResultedMarkets resultedMarkets) {

        Set<Market> marketsToRemove = new HashSet<>();
        Set<String> selectionsToRemove = new HashSet<>();
        for (Market market : markets.getMarkets().values()) {
            ResultedMarket resultedMarket =
                            resultedMarkets.get(market.getType(), market.getLineId(), market.getSequenceId());
            if (resultedMarket != null) {
                if (resultedMarket.isFullyResulted())
                    marketsToRemove.add(market);
                else {
                    selectionsToRemove.clear();
                    Map<String, Selection> selections = market.getSelections();
                    List<String> winningSelections = resultedMarket.getWinningSelections();
                    List<String> losingSelections = resultedMarket.getLosingSelections();
                    for (Entry<String, Selection> e : selections.entrySet()) {
                        String selName = e.getKey();
                        if (winningSelections.contains(selName))
                            selectionsToRemove.add(selName);
                        else if (losingSelections.contains(selName))
                            selectionsToRemove.add(selName);
                    }
                    selectionsToRemove.forEach(selName -> selections.remove(selName));
                }
            }
        }
        marketsToRemove.forEach(market -> markets.remove(market));
    }

    static final double ONE_THRESHOLD = 0.995;
    static final double NOT_ZERO = 0.001;

    /**
     * applies business rules to market selections - removes any with prob = 1.0
     * 
     * @param markets
     */
    private void filterSelections(Markets markets) {
        Set<String> selectionsToRemove = new HashSet<>();
        for (Market market : markets.getMarkets().values()) {
            Map<String, Selection> selections = market.getSelections();
            selectionsToRemove.clear();
            for (Entry<String, Selection> e : selections.entrySet()) {
                double selProb = e.getValue().getProb();
                if (selProb > ONE_THRESHOLD) {
                    /*
                     * implement business rule to remove any selections where prob = 1.0
                     */
                    selectionsToRemove.add(e.getKey());
                } else if (selProb < NOT_ZERO) {
                    /*
                     * avoid possible downstream divide by zero errors
                     */
                    e.getValue().setProb(NOT_ZERO);
                }
            }
            selectionsToRemove.forEach(selName -> selections.remove(selName));
        }
    }

}
