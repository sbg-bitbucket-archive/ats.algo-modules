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
import ats.algo.sport.outrights.calcengine.core.MatchProbs;
import ats.algo.sport.outrights.calcengine.core.OutrightsCompetition;
import ats.algo.sport.outrights.calcengine.core.CompetitionWarnings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorCompletionService;

/**
 * perfosm the calcs for a single competition
 * 
 * @author gicha
 *
 */
class CalcEngine extends MonteCarloMatchEngine {

    int N_THREADS = 6;

    private static final int N_ITERATIONS = 64000;

    private Competition competition;
    private Map<Long, MatchProbs> atsProbs;

    public CalcEngine(Competition competition) {
        super(null);
        this.competition = competition;
        /*
         * create the MatchProbs objects for those fixtures that are being priced by ATS
         */
        Map<Long, Market> inputMarkets = competition.getInputMarkets();
        if (inputMarkets == null) {
            atsProbs = new HashMap<>(0);
        } else {
            atsProbs = new HashMap<>(inputMarkets.size());
            for (Entry<Long, Market> e : inputMarkets.entrySet()) {
                atsProbs.put(e.getKey(), new MatchProbs(e.getValue()));
            }
        }
    }

    public CompetitionWarnings checkCompetitionStateForErrors() {
        AbstractFormat competitionFormat = CompetitionProperties.competitionMatchFormatInstance(competition);
        AbstractState competitionState = CompetitionProperties.competitionStateInstance(competition, competitionFormat);
        return competitionState.checkStateForErrors();
    }

    @Override
    public void calculate() {

        AbstractFormat competitionFormat = CompetitionProperties.competitionMatchFormatInstance(competition);
        AbstractParams matchParams = new AbstractParams();
        AbstractState competitionState = CompetitionProperties.competitionStateInstance(competition, competitionFormat);

        AbstractMarketsFactory marketsFactory = CompetitionProperties.competitionMarketsFactoryInstance(competition);
        OutrightsCompetition match = new OutrightsCompetition(competitionFormat, competitionState, matchParams,
                        atsProbs, marketsFactory);
        ExecutorCompletionService<Boolean> completionService = null;
        if (N_THREADS > 1) {
            MatchEngineThreadPool matchEngineThreadPool = new MatchEngineThreadPool(N_THREADS - 1);
            completionService = matchEngineThreadPool.getExecutorCompletionService();
        }
        ConcurrentCalculationManager ccm = new ConcurrentCalculationManager(N_THREADS, N_ITERATIONS,
                        (MonteCarloMatch) match, completionService);
        ccm.calculate();
        Markets markets = match.generateMarkets();
        ResultedMarkets resultedMarkets = new ResultedMarkets();
        generatePartialResultedMarkets(markets, resultedMarkets);
        competition.setMarkets(markets);
        competition.setResultedMarkets(resultedMarkets);
    }

    private static final double threshold = 1.0e-5; // less than 1 in 64,000. to be outside this threshold zero or all
                                                    // monte carlo trials must have passed
    /*
     * generates resulted market entries for any selections where the prob is 0 or 1
     */

    private void generatePartialResultedMarkets(Markets markets, ResultedMarkets resultedMarkets) {
        for (Entry<String, Market> e : markets.getMarkets().entrySet()) {
            Market market = e.getValue();
            List<String> winningSelections = new ArrayList<>();
            List<String> losingSelections = new ArrayList<>();
            Map<String, Selection> selections = market.getSelections();
            for (Entry<String, Selection> e2 : selections.entrySet()) {
                String selName = e2.getKey();
                Selection selection = e2.getValue();
                if (selection.getProb() < threshold) {
                    losingSelections.add(selName);
                } else if (selection.getProb() > 1.0 - threshold) {
                    winningSelections.add(selName);
                }
            }
            if (winningSelections.size() + losingSelections.size() > 0) {
                ResultedMarket resultedMarket = new ResultedMarket(market.getType(), market.getLineId(),
                                market.getCategory(), market.getSequenceId(), false, market.getMarketDescription(),
                                winningSelections, 0);
                resultedMarket.setLosingSelections(losingSelections);
                resultedMarket.setFullyResulted(false);
                resultedMarkets.addMarket(resultedMarket);

                /*
                 * remove the resulted selections from the market
                 */
                for (String selName : winningSelections)
                    selections.remove(selName);
                for (String selName : losingSelections)
                    selections.remove(selName);
            }
        }
    }
}
