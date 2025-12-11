package ats.algo.sport.ufc;

import ats.algo.core.baseclasses.MatchEngineSavedState;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.matchengineframework.MatchEngine;
import ats.algo.matchrunner.view.AlertBox;
import ats.core.util.log.LoggerFactory;
import ats.core.util.log.Logger;

public class UfcMatchEngine extends MatchEngine {
    /*
     * declare the logger instance to use with this class
     */
    private static final Logger log = LoggerFactory.getLogger(UfcMatchEngine.class);

    private int roundsPerMatch;

    /**
     * class constructor - instantiate the various objects
     * 
     * @param matchFormat
     */
    public UfcMatchEngine(MatchFormat matchFormat) {
        super(SupportedSportType.UFC);
        matchState = new UfcMatchState((UfcMatchFormat) matchFormat);
        roundsPerMatch = ((UfcMatchFormat) matchFormat).getRoundsPerMatch();
        matchParams = new UfcMatchParams(roundsPerMatch);
        matchParams.setDefaultParams(matchFormat);
    }

    @Override
    /**
     * code below does not represent a real ufc model. It is just intended to show how markets of each type get
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
        if (super.getCalcRequestCause() == CalcRequestCause.NEW_MATCH) {
            /*
             * if this is the start of a new match then create a new empty ExampleSavedState object, seralise to json
             * and add to MatchEngineSavedState
             */
            MatchEngineSavedState matchEngineSavedState = new MatchEngineSavedState();
            super.setMatchEngineSavedState(matchEngineSavedState);
        } else {
            /*
             * update the saved state object. Only do this if: a) matchIncident is not null and b) the incident is of
             * type TennisMatchIncident. Other incident types that might be received include DatafeedMatchIncident
             */
        }
        UfcMatchState matchState = (UfcMatchState) super.getMatchState();
        UfcMatchParams matchParams = (UfcMatchParams) super.getMatchParams();

        Markets markets = new Markets();
        /*
         * calculate some markets
         */
        UfcMarketCalcs ufcMarketCalcs = new UfcMarketCalcs(matchState, matchParams, roundsPerMatch);
        if (ufcMarketCalcs.getDraw() < 0) {
            AlertBox.displayError("draw prob" + ufcMarketCalcs.getDraw()
                            + " is less than Zero. Need reinput the match parameters ");
            return;
        }
        matchParams.getDraw().getGaussian().setMean(ufcMarketCalcs.getDraw());
        addMarketIfNotNull(markets, ufcMarketCalcs.generateMatchWinnerMarket());
        addMarketIfNotNull(markets, ufcMarketCalcs.generateWinnerHalfRoundMarket());
        addMarketIfNotNull(markets, ufcMarketCalcs.generateWinnerOneHalfRoundMarket());
        addMarketIfNotNull(markets, ufcMarketCalcs.generateWinnerTwoHalfRoundMarket());
        addMarketIfNotNull(markets, ufcMarketCalcs.generateMethodOfVictoryMarket());
        addMarketIfNotNull(markets, ufcMarketCalcs.generateRoundBettingMarket());
        addMarketIfNotNull(markets, ufcMarketCalcs.generateFinishInOddEvenRoundMarket());
        addMarketIfNotNull(markets, ufcMarketCalcs.generateHowFightWillNotBeWonMarket());
        addMarketIfNotNull(markets, ufcMarketCalcs.generateTotalRoundsMarket(0.5));
        addMarketIfNotNull(markets, ufcMarketCalcs.generateTotalRoundsMarket(1.5));
        addMarketIfNotNull(markets, ufcMarketCalcs.generateTotalRoundsMarket(2.5));
        addMarketIfNotNull(markets, ufcMarketCalcs.generateFightFinishMarket());
        addMarketIfNotNull(markets, ufcMarketCalcs.generateRoundToFinishOne());
        addMarketIfNotNull(markets, ufcMarketCalcs.generateRoundToFinishTwo());
        addMarketIfNotNull(markets, ufcMarketCalcs.generateDoubleChanceOne());
        addMarketIfNotNull(markets, ufcMarketCalcs.generateDoubleChanceTwo());
        addMarketIfNotNull(markets, ufcMarketCalcs.generateDoubleChanceThree());
        addMarketIfNotNull(markets, ufcMarketCalcs.generateDoubleChanceSix());
        addMarketIfNotNull(markets, ufcMarketCalcs.generateDoubleChanceEight());
        addMarketIfNotNull(markets, ufcMarketCalcs.generateGoDistanceMarket());
        addMarketIfNotNull(markets, ufcMarketCalcs.generateGroupRoundBettingOneMarket());
        addMarketIfNotNull(markets, ufcMarketCalcs.generateGroupRoundBettingTwoMarket());
        addMarketIfNotNull(markets, ufcMarketCalcs.generateNotWinInRoundMarket());
        if (roundsPerMatch > 3) {
            addMarketIfNotNull(markets, ufcMarketCalcs.generateRoundToFinishThree());
            addMarketIfNotNull(markets, ufcMarketCalcs.generateRoundToFinishFour());
            addMarketIfNotNull(markets, ufcMarketCalcs.generateGroupRoundBettingFourMarket());
            addMarketIfNotNull(markets, ufcMarketCalcs.generateWinnerThreeHalfRoundMarket());
            addMarketIfNotNull(markets, ufcMarketCalcs.generateWinnerFourHalfRoundMarket());
            addMarketIfNotNull(markets, ufcMarketCalcs.generateDoubleChanceFour());
            addMarketIfNotNull(markets, ufcMarketCalcs.generateDoubleChanceFive());
            addMarketIfNotNull(markets, ufcMarketCalcs.generateDoubleChanceSeven());
            addMarketIfNotNull(markets, ufcMarketCalcs.generateDoubleChanceNine());
            addMarketIfNotNull(markets, ufcMarketCalcs.generateDoubleChanceTen());
            addMarketIfNotNull(markets, ufcMarketCalcs.generateTotalRoundsMarket(3.5));
            addMarketIfNotNull(markets, ufcMarketCalcs.generateTotalRoundsMarket(4.5));
            addMarketIfNotNull(markets, ufcMarketCalcs.generateGroupRoundBettingThreeMarket());
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
         * Can just use the resulting logic from the existing ATS ufc model
         */
        UfcMatchResultMarkets resultMarkets = new UfcMatchResultMarkets();
        return resultMarkets.resultMarkets(markets, (UfcMatchState) previousMatchState,
                        (UfcMatchState) currentMatchState);
    }

    @Override
    public TradingRules getTradingRuleSet() {
        return new TradingRules();
    }
}
