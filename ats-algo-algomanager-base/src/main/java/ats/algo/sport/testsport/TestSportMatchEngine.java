package ats.algo.sport.testsport;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchState;
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

public class TestSportMatchEngine extends MatchEngine {

    /*
     * static flags to support unit testing of error and other conditions
     */
    public static boolean disableMatchWinnerCalc;
    public static boolean simulateFatalError;

    public static void resetTestFlags() {
        disableMatchWinnerCalc = false;
        simulateFatalError = false;
    }

    /*
     * end of static flags
     */

    /**
     * class constructor - instantiate the various objects
     * 
     * @param matchFormat
     */
    public TestSportMatchEngine(MatchFormat matchFormat) {
        super(SupportedSportType.TEST_SPORT);
        matchParams = new TestSportMatchParams();
        matchParams.setDefaultParams(matchFormat);
        matchState = new TestSportMatchState((TestSportMatchFormat) matchFormat);
    }

    @Override
    /**
     * code below does not represent a real tennis model. It is just intended to show how markets of each type get
     * generated and resulted and how the various classes fit together
     */
    public void calculate() {
        if (simulateFatalError)
            throw new IllegalArgumentException("Test exception");
        TestSportMatchState matchState = (TestSportMatchState) super.getMatchState();
        TestSportMatchParams matchParams = (TestSportMatchParams) super.getMatchParams();

        Markets markets = generateMarkets(matchState, matchParams);
        super.calculatedMarkets = markets;
    }

    @Override
    public ResultedMarkets resultMarkets(Markets markets, MatchState previousMatchState, MatchState currentMatchState) {
        /*
         * Can just use the resulting logic from the existing ATS tennis model
         */
        return resultMarkets(markets, (TestSportMatchState) previousMatchState,
                        (TestSportMatchState) currentMatchState);
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

    static Markets generateMarkets(TestSportMatchState matchState, TestSportMatchParams matchParams) {
        double pA = matchParams.getPctAWinsGame().getGaussian().getMean();
        int winningScore = matchState.getWinningScore();
        int scoreA = matchState.getScoreA();
        int scoreB = matchState.getScoreB();
        /*
         * generate the markov prob matrix of probs of all possible scores
         */
        double[][] probs = new double[winningScore + 1][winningScore + 1];
        probs[scoreA][scoreB] = 1;
        for (int pointNo = scoreA + scoreB + 1; pointNo < 2 * winningScore; pointNo++) {
            for (int n = scoreA; n <= pointNo; n++) {
                int uScoreA = n;
                int uScoreB = pointNo - n;
                if ((uScoreA >= scoreA && uScoreB >= scoreB) && (uScoreA <= winningScore && uScoreB <= winningScore)) {
                    // System.out.printf("pointNo: %d, n: %d, uscoreA: %d,
                    // uscoreB: %d\n", pointNo, n, uScoreA, uScoreB);
                    if (uScoreA - 1 >= 0 && uScoreB != winningScore)
                        probs[uScoreA][uScoreB] += pA * probs[uScoreA - 1][uScoreB];
                    if (uScoreB - 1 >= 0 && uScoreA != winningScore)
                        probs[uScoreA][uScoreB] += (1 - pA) * probs[uScoreA][uScoreB - 1];
                }
            }
        }
        Markets markets = new Markets();
        markets.addMarketWithShortKey(generateMatchWinnerMarket(probs, winningScore));
        markets.addMarketWithShortKey(generateTotalGamesMarket(probs, winningScore));
        markets.addMarketWithShortKey(generateNextGameWinnerMarket(scoreA, scoreB, pA));
        Market hcapMkt = generateGamesHandicapMarket(probs, winningScore);
        markets.addMarketWithShortKey(hcapMkt);
        markets.addMarketWithShortKey(generateAsianHandicapMarket(scoreA, scoreB, hcapMkt));
        return markets;
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
        if (disableMatchWinnerCalc)
            p = 0.5;
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

    public static ResultedMarkets resultMarkets(Markets markets, TestSportMatchState previousMatchState,
                    TestSportMatchState currentMatchState) {
        /**
         * don't do any resulting for now
         */
        return new ResultedMarkets();
    }

}
