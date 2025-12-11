package example.algo.tennis;

import ats.algo.core.baseclasses.MatchEngineSavedState;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.matchengineframework.MatchEngine;
import ats.algo.sport.tennis.TennisMatchIncident;
import ats.algo.sport.tennis.TennisMatchResultMarkets;
import ats.algo.sport.tennis.TennisMatchState;
import ats.algo.sport.tennis.tradingrules.TennisTradingRules;
import ats.core.util.log.LoggerFactory;
import ats.core.util.log.Logger;

public class ExampleTennisMatchEngine extends MatchEngine {
    /*
     * declare the logger instance to use with this class
     */
    private static final Logger log = LoggerFactory.getLogger(ExampleTennisMatchEngine.class);

    /**
     * class constructor - instantiate the various objects
     * 
     * @param matchFormat
     */
    public ExampleTennisMatchEngine(MatchFormat matchFormat) {
        super(SupportedSportType.TENNIS);
        matchParams = new ExampleTennisMatchParams();
        matchParams.setDefaultParams(matchFormat);
        matchState = new TennisMatchState(matchFormat);
    }

    @Override
    /**
     * code below does not represent a real tennis model. It is just intended to show how markets of each type get
     * generated and resulted and how the various classes fit together
     */
    public void calculate() {
        /*
         * this is an example of a call to the logger that may be made from anywhere in the class. Besides 'info' other
         * useful methods are 'debug', 'warn', 'error', 'fatal'
         * 
         * In MatchRunner anything logged will appear in the console window. In production anything logged will be saved
         * in the system logs
         */
        log.info("Calculate method called");
        ExampleSavedState savedState;
        if (super.getCalcRequestCause() == CalcRequestCause.NEW_MATCH) {
            /*
             * if this is the start of a new match then create a new empty ExampleSavedState object, seralise to json
             * and add to MatchEngineSavedState
             */
            savedState = new ExampleSavedState();
            MatchEngineSavedState matchEngineSavedState = new MatchEngineSavedState();
            matchEngineSavedState.setSavedState(savedState.toJson());
            super.setMatchEngineSavedState(matchEngineSavedState);
        } else {
            /*
             * Recover our savedState object from the incoming request by obtaining the json representation and
             * deserializing
             */
            savedState = ExampleSavedState.fromJson(super.getMatchEngineSavedState().getSavedState());
            /*
             * update the saved state object. Only do this if: a) matchIncident is not null and b) the incident is of
             * type TennisMatchIncident. Other incident types that might be received include DatafeedMatchIncident
             */

            MatchIncident matchIncident = super.getMatchIncident();

            if (matchIncident != null)
                if (matchIncident.getClass().equals(TennisMatchIncident.class))
                    savedState.updateForIncident((TennisMatchIncident) matchIncident);
        }
        TennisMatchState matchState = (TennisMatchState) super.getMatchState();
        ExampleTennisMatchParams matchParams = (ExampleTennisMatchParams) super.getMatchParams();
        Markets markets = new Markets();
        /*
         * calculate some markets
         */

        addMarketIfNotNull(markets, ExampleTennisMarketCalcs.generateMatchWinnerMarket(matchState, matchParams));
        addMarketIfNotNull(markets, ExampleTennisMarketCalcs.generateTotalGamesMarket(matchState, matchParams));
        addMarketIfNotNull(markets, ExampleTennisMarketCalcs.generateGamesHandicapMarket(matchState, matchParams));
        if (!matchState.isPreMatch()) {
            addMarketIfNotNull(markets,
                            ExampleTennisMarketCalcs.generatePointPlus2WinnerMarket(matchState, matchParams));
            if (!matchState.isInTieBreak() && !matchState.isInSuperTieBreak()) {
                addMarketIfNotNull(markets,
                                ExampleTennisMarketCalcs.generateThisGameWinnerMarket(matchState, matchParams));
                addMarketIfNotNull(markets, ExampleTennisMarketCalcs.generateNextGameWinnerMarket(matchState,
                                matchParams, savedState));
            }
        }
        super.calculatedMarkets = markets;
    }

    private void addMarketIfNotNull(Markets markets, Market market) {
        if (market != null)
            markets.addMarketWithShortKey(market);


    }

    @Override
    public ResultedMarkets resultMarkets(Markets markets, AlgoMatchState previousMatchState,
                    AlgoMatchState currentMatchState) {
        /*
         * Can just use the resulting logic from the existing ATS tennis model
         */
        TennisMatchResultMarkets resultMarkets = new TennisMatchResultMarkets();
        return resultMarkets.resultMarkets(markets, (TennisMatchState) previousMatchState,
                        (TennisMatchState) currentMatchState);
    }

    @Override
    public TradingRules getTradingRuleSet() {
        return new TennisTradingRules();
    }
}
