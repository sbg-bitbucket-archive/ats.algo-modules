package ats.algo.sport.fantasyexample;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchParam;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.markets.DerivedMarketSpec;
import ats.algo.core.markets.DerivedMarketSpecApplicability;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketCategory;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.markets.DerivedMarketSpec.DerivedMarketGapBetweenLines;
import ats.algo.core.tradingrules.DerivedMarketTradingRule;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.genericsupportfunctions.GCMath;
import ats.algo.matchengineframework.MatchEngine;
import ats.algo.sport.fantasyexample.FantasyExampleSportMatchFormat;
import ats.algo.sport.fantasyexample.FantasyExampleSportMatchState;
import ats.algo.sport.fantasyexample.FantasyExampleSportMatchParams;

public class FantasyExampleSportMatchEngine extends MatchEngine {



    /**
     * class constructor - instantiate the various objects
     * 
     * @param matchFormat
     */
    public FantasyExampleSportMatchEngine(MatchFormat matchFormat) {
        super(SupportedSportType.TEST_SPORT);
        matchParams = new FantasyExampleSportMatchParams();
        matchParams.setDefaultParams(matchFormat);
        matchState = new FantasyExampleSportMatchState((FantasyExampleSportMatchFormat) matchFormat);
    }

    @Override
    /**
     * code below does not represent a real tennis model. It is just intended to show how markets of each type get
     * generated and resulted and how the various classes fit together
     */
    public void calculate() {
        FantasyExampleSportMatchState matchState = (FantasyExampleSportMatchState) super.getMatchState();
        FantasyExampleSportMatchParams matchParams = (FantasyExampleSportMatchParams) super.getMatchParams();

        Markets markets = generateMarkets(matchState, matchParams);
        super.calculatedMarkets = markets;
    }

    @Override
    public ResultedMarkets resultMarkets(Markets markets, AlgoMatchState previousMatchState,
                    AlgoMatchState currentMatchState) {
        /*
         * Can just use the resulting logic from the existing ATS tennis model
         */
        return resultMarkets(markets, (FantasyExampleSportMatchState) previousMatchState,
                        (FantasyExampleSportMatchState) currentMatchState);
    }

    @Override
    public TradingRules getTradingRuleSet() {
        TradingRules tradingRules = new TradingRules();
        DerivedMarketSpec generateAsianHandicap = DerivedMarketSpec.getDerivedMarketSpecForAsianLines("", "",
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, 3, 1,
                        DerivedMarketGapBetweenLines.QUARTER_LINE);
        tradingRules.addRule(new DerivedMarketTradingRule(3, "FT:SPRD", generateAsianHandicap));
        return tradingRules;
    }

    static Markets generateMarkets(FantasyExampleSportMatchState matchState,
                    FantasyExampleSportMatchParams matchParams) {

        Markets markets = new Markets();
        markets.addMarketWithShortKey(generateMatchupMarket(1, "Patrick Bamford", matchParams.getPlayer1(),
                        matchState.getPtsPlayer1(), 2, "Steven Defour", matchParams.getPlayer2(),
                        matchState.getPtsPlayer2()));
        markets.addMarketWithShortKey(generateMatchupMarket(1, "Patrick Bamford", matchParams.getPlayer1(),
                        matchState.getPtsPlayer1(), 3, "Ryan Bertrand", matchParams.getPlayer3(),
                        matchState.getPtsPlayer3()));
        markets.addMarketWithShortKey(
                        generateMatchupMarket(2, "Steven Defour", matchParams.getPlayer2(), matchState.getPtsPlayer2(),
                                        3, "Ryan Bertrand", matchParams.getPlayer3(), matchState.getPtsPlayer3()));
        return markets;
    }

    private static Market generateMatchupMarket(int n1, String name1, MatchParam player1, int ptsPlayer1, int n2,
                    String name2, MatchParam player2, int ptsPlayer2) {
        // TODO Auto-generated method stub
        Market market = new Market(MarketCategory.GENERAL, "P" + Integer.toString(n1) + ":P" + Integer.toString(n2),
                        "M", name1 + " vs " + name2);
        market.setLineId(null);
        market.setIsValid(true);
        market.setSequenceId("M");
        double nExp1 = ((double) ptsPlayer1) + player1.getGaussian().getMean();
        double nExp2 = ((double) ptsPlayer2) + player2.getGaussian().getMean();
        double pointsDiff = Math.abs(nExp1 - nExp2);
        if (pointsDiff > 10.0)
            pointsDiff = 10.0;
        double probDraw = 0.2 * (1 - pointsDiff / 10);
        double probA = (1 - probDraw) * nExp1 / (nExp1 + nExp2);
        double probB = (1 - probDraw) * nExp2 / (nExp1 + nExp2);
        market.put(name1, GCMath.round(probA, 4));
        market.put(name2, GCMath.round(probB, 4));
        market.put("Draw", GCMath.round(probDraw, 4));
        return market;
    }

    /**
     * Generates an example match winner market.
     */
    static Market generateMatchWinnerMarket(double[][] probs, int winningScore) {

        Market market = new Market(MarketCategory.GENERAL, "FT:ML", "M", "Match Winner");
        market.setLineId(null);
        market.setIsValid(true);
        market.setSequenceId("M");
        double p = 0;
        for (int i = 0; i < winningScore; i++)
            p += probs[winningScore][i];

        market.put("A", GCMath.round(p, 4));
        market.put("B", GCMath.round(1 - p, 4));
        return market;
    }

    /**
     * Generates an example total games market.
     * 
     * @param matchParams
     */
    static Market generateTotalGamesMarket(double[][] probs, int winningScore) {

        Market market = new Market(MarketCategory.OVUN, "FT:OU", "M", "Match total games");
        market.setIsValid(true);
        market.setLineBase(winningScore);
        double[] q = new double[winningScore];
        for (int i = 0; i < winningScore; i++)
            q[i] = probs[winningScore][i] + probs[i][winningScore];
        double[] p = convertToCumulative(q);
        market.setLineProbs(p);
        String s = market.generateBalancedLineId();
        return market.getMarketForLineId(s);
    }

    /**
     * Generates an example handicap market.
     * 
     * @param matchParams
     */
    static Market generateGamesHandicapMarket(double[][] probs, int winningScore) {
        Market market = new Market(MarketCategory.HCAP, "FT:SPRD", "M", "Match total games handicap");
        market.setIsValid(true);
        market.setLineBase(-winningScore);
        double[] q = new double[2 * winningScore + 1];
        for (int score = 0; score <= winningScore; score++) {
            q[score] = probs[score][winningScore];
            q[2 * winningScore - score] = probs[winningScore][score];
        }
        double[] p = convertToCumulative(q);
        market.setLineProbs(p);
        String s = market.generateBalancedLineId();
        return market.getMarketForLineId(s);

    }

    private static double[] convertToCumulative(double q[]) {
        double[] p = new double[q.length];
        p[0] = GCMath.round(q[0], 4);
        for (int i = 1; i < q.length; i++)
            p[i] = GCMath.round(p[i - 1] + q[i], 4);
        return p;
    }

    /**
     * generates example game winner market. Uses fixed probs instead of calculating from a model
     * 
     * @param matchParams
     */
    static Market generateNextGameWinnerMarket(int scoreA, int scoreB, double prob) {
        String sequenceId = "G" + Integer.toString(scoreA + scoreB + 1);
        Market market = new Market(MarketCategory.GENERAL, "G:ML", sequenceId, "Next game winner");
        market.setIsValid(true);
        market.put("A", GCMath.round(prob, 4));
        market.put("B", GCMath.round(1 - prob, 4));
        return market;
    }

    static Market generateAsianHandicapMarket(int scoreA, int scoreB, Market hcapMkt) {
        String sequenceId = "S" + Integer.toString(scoreA) + "-" + Integer.toString(scoreB);
        Market market = new Market(MarketCategory.HCAP, "FT:AHCP", sequenceId, "AsianHandicap");
        market.setIsValid(true);
        market.setLineProbs(hcapMkt.getLineProbs());
        int scoreDiff = scoreA - scoreB;
        market.setLineBase(hcapMkt.getLineBase() - scoreDiff);
        market.put("AH", GCMath.round(hcapMkt.get("AH"), 4));
        market.put("BH", GCMath.round(hcapMkt.get("BH"), 4));
        return market;
    }

    public static ResultedMarkets resultMarkets(Markets markets, FantasyExampleSportMatchState previousMatchState,
                    FantasyExampleSportMatchState currentMatchState) {
        /**
         * don't do any resulting for now
         */
        return new ResultedMarkets();
    }

}
