package ats.algo.sport.ufc;


import java.text.DecimalFormat;
import java.text.NumberFormat;

import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketCategory;

/**
 * creates instances of each of the supplied market types from the supplied params.
 *
 * @author Rob
 */
public class UfcMarketCalcs {

    private final double pFinishFirstHalf = 0.6;
    private final double pFinishSecondHalf = 0.4;
    private UfcMatchParams ufcMatchParams;
    private UfcMatchState ufcMatchState;
    private double p1a;
    private double p2a;
    private double p3a;
    private double p4a;
    private double p5a;
    private double pda;
    private double p1b;
    private double p2b;
    private double p3b;
    private double p4b;
    private double p5b;
    private double pdb;
    private double draw;
    private int roundsPerMatch;
    private NumberFormat formatter = new DecimalFormat("#0.000");

    public UfcMarketCalcs(UfcMatchState matchState, UfcMatchParams matchParams, int roundsPerMatchParam) {
        this.ufcMatchParams = matchParams;
        this.ufcMatchState = matchState;
        p1a = ufcMatchParams.getRound1AToWin().getGaussian().getBiasAdjustedMean();
        p2a = ufcMatchParams.getRound2AToWin().getGaussian().getBiasAdjustedMean();
        p3a = ufcMatchParams.getRound3AToWin().getGaussian().getBiasAdjustedMean();
        if (roundsPerMatchParam == 5) {
            p4a = ufcMatchParams.getRound4AToWin().getGaussian().getBiasAdjustedMean();
            p5a = ufcMatchParams.getRound5AToWin().getGaussian().getBiasAdjustedMean();
        } else {
            p4a = p5a = 0;
        }
        pda = ufcMatchParams.getDecisionAToWin().getGaussian().getBiasAdjustedMean();
        p1b = ufcMatchParams.getRound1BToWin().getGaussian().getBiasAdjustedMean();
        p2b = ufcMatchParams.getRound2BToWin().getGaussian().getBiasAdjustedMean();
        p3b = ufcMatchParams.getRound3BToWin().getGaussian().getBiasAdjustedMean();
        if (roundsPerMatchParam == 5) {
            p4b = ufcMatchParams.getRound4BToWin().getGaussian().getBiasAdjustedMean();
            p5b = ufcMatchParams.getRound5BToWin().getGaussian().getBiasAdjustedMean();
        } else {
            p4b = p5b = 0;
        }
        pdb = ufcMatchParams.getDecisionBToWin().getGaussian().getBiasAdjustedMean();
        draw = 1 - (p1b + p2b + p3b + p4b + p5b + pdb + p1a + p2a + p3a + p4a + p5a + pda);
        draw = Double.valueOf(formatter.format(draw));
        roundsPerMatch = roundsPerMatchParam;
    }

    public double getDraw() {
        return draw;
    }

    /**
     * Generates an example match winner market.
     */
    public Market generateMatchWinnerMarket() {

        Market market = new Market(MarketCategory.GENERAL, "FT:ML", ufcMatchState.getSequenceIdForMatch(),
                        "Match odds");
        market.setLineId(null);
        market.setIsValid(true);
        market.setSequenceId(ufcMatchState.getSequenceIdForMatch());

        market.put("A", p1a + p2a + p3a + p4a + p5a + pda);
        market.put("B", p1b + p2b + p3b + p4b + p5b + pdb);
        market.put("Draw", draw);
        return market;
    }

    public Market generateWinnerHalfRoundMarket() {

        Market market = new Market(MarketCategory.GENERAL, "FT:WR", ufcMatchState.getSequenceIdForMatch(),
                        " Winner and Total Rounds 0.5");
        market.setLineId("0.5");
        market.setIsValid(true);
        market.setSequenceId(ufcMatchState.getSequenceIdForRound(0));
        market.put("A and Under 0.5", p1a * pFinishFirstHalf);
        market.put("A and Over 0.5", p1a * pFinishSecondHalf + p2a + p3a + p4a + p5a + pda);
        market.put("B  and Under 0.5", p1b * pFinishFirstHalf);
        market.put("B  and over 0.5", p1b * pFinishSecondHalf + p2b + p3b + p4b + p5b + pdb);
        market.put("Draw or Tech Draw", draw);
        return market;
    }

    public Market generateWinnerTwoHalfRoundMarket() {

        Market market = new Market(MarketCategory.GENERAL, "FT:WR", ufcMatchState.getSequenceIdForMatch(),
                        " Winner and Total Rounds 2.5");
        market.setLineId("2.5");
        market.setIsValid(true);
        market.setSequenceId(ufcMatchState.getSequenceIdForRound(2));
        market.put("A and Under 2.5", p1a + p2a + p3a * pFinishFirstHalf);
        market.put("A and Over 2.5", p3a * pFinishSecondHalf + p4a + p5a + pda);
        market.put("B  and Under 2.5", p1b + p2b + p3b * pFinishFirstHalf);
        market.put("B  and over 2.5", p3b * pFinishSecondHalf + p4b + p5b + pdb);
        market.put("Draw or Tech Draw", draw);
        return market;
    }

    public Market generateWinnerThreeHalfRoundMarket() {

        Market market = new Market(MarketCategory.GENERAL, "FT:WR", ufcMatchState.getSequenceIdForMatch(),
                        " Winner and Total Rounds 3.5");
        market.setLineId("3.5");
        market.setIsValid(true);
        market.setSequenceId(ufcMatchState.getSequenceIdForRound(3));
        market.put("A and Under 3.5", p1a + p2a + p3a + p4a * pFinishFirstHalf);
        market.put("A and Over 3.5", p4a * pFinishSecondHalf + p5a + pda);
        market.put("B  and Under 3.5", p1b + p2b + p3b + p4b * pFinishFirstHalf);
        market.put("B  and over 3.5", p4b * pFinishSecondHalf + p5b + pdb);
        market.put("Draw or Tech Draw", draw);
        return market;
    }

    public Market generateWinnerOneHalfRoundMarket() {

        Market market = new Market(MarketCategory.GENERAL, "FT:WR", ufcMatchState.getSequenceIdForMatch(),
                        " Winner and Total Rounds 1.5");
        market.setLineId("1.5");
        market.setIsValid(true);
        market.setSequenceId(ufcMatchState.getSequenceIdForRound(1));
        market.put("A and Under 1.5", p1a + p2a * pFinishFirstHalf);
        market.put("A and Over 1.5", p2a * pFinishSecondHalf + p3a + p4a + p5a + pda);
        market.put("B  and Under 1.5", p1b + p2b * pFinishFirstHalf);
        market.put("B  and over 1.5", p2b * pFinishSecondHalf + p3b + p4b + p5b + pdb);
        market.put("Draw or Tech Draw", draw);
        return market;
    }

    public Market generateWinnerFourHalfRoundMarket() {

        Market market = new Market(MarketCategory.GENERAL, "FT:WR", ufcMatchState.getSequenceIdForMatch(),
                        " Winner and Total Rounds 4.5");
        market.setLineId("4.5");
        market.setIsValid(true);
        market.setSequenceId(ufcMatchState.getSequenceIdForRound(4));
        market.put("A and Under 4.5", p1a + p2a + p3a + p4a + p5a * pFinishFirstHalf);
        market.put("A and Over 4.5", p5a * pFinishSecondHalf + pda);
        market.put("B  and Under 4.5", p1b + p2b + p3b + p4b + p5b * pFinishFirstHalf);
        market.put("B  and over 4.5", p5b * pFinishSecondHalf + pdb);
        market.put("Draw or Tech Draw", draw);
        return market;
    }

    public Market generateMethodOfVictoryMarket() {
        Market market = new Market(MarketCategory.GENERAL, "FT:MV", ufcMatchState.getSequenceIdForMatch(),
                        "Winner and Method of Victory");
        market.setLineId(null);
        market.setIsValid(true);
        market.setSequenceId(ufcMatchState.getSequenceIdForMatch());
        market.put("A by KO, TKO, DQ or Submission", p1a + p2a + p3a + p4a + p5a);
        market.put("A by Decision/Tech Dec", pda);
        market.put("B by KO, TKO, DQ or Submission", p1b + p2b + p3b + p4b + p5b);
        market.put("B by Decision/Tech Dec", pdb);
        market.put("Draw or Tech Draw", draw);
        return market;
    }

    public Market generateRoundBettingMarket() {
        Market market = new Market(MarketCategory.GENERAL, "FT:RB", ufcMatchState.getSequenceIdForMatch(),
                        "Round Betting");
        market.setLineId(null);
        market.setIsValid(true);
        market.setSequenceId(ufcMatchState.getSequenceIdForMatch());

        market.put("A Round 1", p1a);
        market.put("A Round 2", p2a);
        market.put("A Round 3", p3a);
        if (roundsPerMatch > 3) {
            market.put("A Round 4", p4a);
            market.put("A Round 5", p5a);
        }
        market.put("A by Decision/Tech Dec", pda);

        market.put("B Round 1", p1b);
        market.put("B Round 2", p2b);
        market.put("B Round 3", p3b);
        if (roundsPerMatch > 3) {
            market.put("B Round 4", p4b);
            market.put("B Round 5", p5b);
        }
        market.put("B by Decision/Tech Dec", pdb);
        market.put("Draw or Tech Draw", draw);
        return market;
    }

    public Market generateFinishInOddEvenRoundMarket() {
        Market market = new Market(MarketCategory.GENERAL, "FT:FOER", ufcMatchState.getSequenceIdForMatch(),
                        "To Finish in Odd or Even Round");
        market.setLineId(null);
        market.setIsValid(true);
        market.setSequenceId(ufcMatchState.getSequenceIdForMatch());

        market.put("Odd", p1a + p1b + p3a + p3b + p5a + p5b);
        market.put("Even", p2a + p2b + p4a + p4b);
        return market;
    }

    public Market generateHowFightWillNotBeWonMarket() {
        Market market = new Market(MarketCategory.GENERAL, "FT:HFNW", ufcMatchState.getSequenceIdForMatch(),
                        "How Will the Fight not be Won");
        market.setLineId(null);
        market.setIsValid(true);
        market.setSequenceId(ufcMatchState.getSequenceIdForMatch());

        market.put("A Not to Win by KO, TKO, DQ or Submission", 1 - (p1a + p2a + p3a + p4a + p5a));
        market.put("A Not to Win by Decision/Tech Dec", 1 - pda);
        market.put("B Not to Win by KO, TKO, DQ or Submission", 1 - (p1b + p2b + p3b + p4b + p5b));
        market.put("B Not to Win by Decision/Tech Dec", 1 - pdb);

        return market;
    }

    public Market generateTotalRoundsMarket(double line) {
        Market market = new Market(MarketCategory.GENERAL, "FT:OU", ufcMatchState.getSequenceIdForMatch(),
                        "Total Rounds " + line);
        market.setLineId(Double.toString(line));
        market.setIsValid(true);
        market.setSequenceId(ufcMatchState.getSequenceIdForRound((int) (line - 0.5)));

        Double[][] a = {{p1a, p1b}, {p2a, p2b}, {p3a, p3b}, {p4a, p4b}, {p5a, p5b}};
        Double pOver = pda + pdb + draw;
        Double pUnder = 0.0;

        for (int i = 1; i <= line + 0.5; i++) {
            int round = i - 1;
            if (line + 0.5 == i) {
                pUnder += pFinishFirstHalf * (a[round][0] + a[round][1]);
            } else if (line + 0.5 > i) {
                pUnder += a[round][0] + a[round][1];
            }
        }

        for (int i = (int) (line + 0.5); i < 6; i++) {
            int round = i - 1;
            if (line + 0.5 == i) {
                pOver += pFinishSecondHalf * (a[round][0] + a[round][1]);
            } else if (line + 0.5 < i) {
                pOver += a[round][0] + a[round][1];
            }
        }

        market.put("Over" + line, pOver);
        market.put("Under" + line, pUnder);
        return market;
    }

    public Market generateFightFinishMarket() {

        Market market = new Market(MarketCategory.GENERAL, "FT:FFM", ufcMatchState.getSequenceIdForMatch(),
                        "What Round will the Fight Finish Market");
        market.setLineId(null);
        market.setIsValid(true);
        market.setSequenceId(ufcMatchState.getSequenceIdForMatch());
        /*
         * Either Fighter in Round 1 11.0% Either Fighter in Round 2 10.0% Either Fighter in Round 3 9.0% Either Fighter
         * inRound 4 8.0% Either Fighter in Round 5 6.0% Either Fighter on by Decision/Tech Dec 55.7% Draw or Tech Draw
         * 0.3%
         */
        market.put("Either Fighter in Round 1", p1a + p1b);
        market.put("Either Fighter in Round 2", p2a + p2b);
        market.put("Either Fighter in Round 3", p3a + p3b);
        if (roundsPerMatch == 5) {
            market.put("Either Fighter in Round 4", p4a + p4b);
            market.put("Either Fighter in Round 5", p5a + p5b);
        }
        market.put("Either Fighter on by Decision/Tech Dec", pda + pdb);
        market.put("Draw or Tech Draw", draw);
        return market;
    }

    public Market generateRoundToFinishOne() {
        Market market = new Market(MarketCategory.GENERAL, "FT:RTF", ufcMatchState.getSequenceIdForMatch(),
                        "When will the Fight Finish 1");
        market.setLineId(null);
        market.setIsValid(true);
        market.setSequenceId("1");
        switch (roundsPerMatch) {
            case 3:
                market.put("Either Fighter in Round 1 or 2", p1a + p1b + p2a + p2b);
                market.put("Either Fighter in Round 3", p3a + p3b);
                break;
            case 5:
                market.put("Either Fighter in Round 1 or 2", p1a + p1b + p2a + p2b);
                market.put("Either Fighter in Round 3 or 4", p3a + p3b + p4a + p4b);
                market.put("Either Fighter in Round 5", p5a + p5b);
                break;
        }
        market.put("Either Fighter on by Decision/Tech Dec", pda + pdb);
        market.put("Draw or Tech Draw", draw);
        return market;
    }

    public Market generateRoundToFinishTwo() {
        Market market = new Market(MarketCategory.GENERAL, "FT:RTF", ufcMatchState.getSequenceIdForMatch(),
                        "When will the Fight Finish 2");
        market.setLineId(null);
        market.setIsValid(true);
        market.setSequenceId("2");
        market.put("Either Fighter in Round 1", p1a + p1b);
        market.put("Either Fighter in Round 2 or 3", p2a + p2b + p3a + p3b);
        if (roundsPerMatch == 5) {
            market.put("Either Fighter in Round 4 or 5", p4a + p4b + p5a + p5b);
        }
        market.put("Either Fighter on by Decision/Tech Dec", pda + pdb);
        market.put("Draw or Tech Draw", draw);
        return market;
    }

    public Market generateRoundToFinishThree() {
        Market market = new Market(MarketCategory.GENERAL, "FT:RTF", ufcMatchState.getSequenceIdForMatch(),
                        "When will the Fight Finish 3");
        market.setLineId(null);
        market.setIsValid(true);
        market.setSequenceId("3");
        market.put("Either Fighter in Round 1, 2 or 3", p1a + p1b + p2a + p2b + p3a + p3b);
        market.put("Either Fighter in Round 4 or 5", p4a + p4b + p5a + p5b);
        market.put("Either Fighter on by Decision/Tech Dec", pda + pdb);
        market.put("Draw or Tech Draw", draw);
        return market;
    }

    public Market generateRoundToFinishFour() {
        Market market = new Market(MarketCategory.GENERAL, "FT:RTF", ufcMatchState.getSequenceIdForMatch(),
                        "When will the Fight Finish 4");
        market.setLineId(null);
        market.setIsValid(true);
        market.setSequenceId("4");
        market.put("Either Fighter in Round 1 or 2", p1a + p1b + p2a + p2b);
        market.put("Either Fighter in Round 3, 4 or 5", p3a + p3b + p4a + p4b + p5a + p5b);
        market.put("Either Fighter on by Decision/Tech Dec", pda + pdb);
        market.put("Draw or Tech Draw", draw);
        return market;
    }

    public Market generateDoubleChanceOne() {
        Market market = new Market(MarketCategory.GENERAL, "FT:DC", ufcMatchState.getSequenceIdForMatch(),
                        "Double Chance 1");
        market.setLineId(null);
        market.setIsValid(true);
        market.setSequenceId("1");
        market.put("A in Round 1 or by Decision", p1a + pda);
        market.put("B in Round 1 or by Decision", p1b + pdb);
        return market;
    }

    public Market generateDoubleChanceTwo() {
        Market market = new Market(MarketCategory.GENERAL, "FT:DC", ufcMatchState.getSequenceIdForMatch(),
                        "Double Chance 2");
        market.setLineId(null);
        market.setIsValid(true);
        market.setSequenceId("2");
        market.put("A in Round 2 or by Decision", p2a + pda);
        market.put("B in Round 2 or by Decision", p2b + pdb);
        return market;
    }

    public Market generateDoubleChanceThree() {
        Market market = new Market(MarketCategory.GENERAL, "FT:DC", ufcMatchState.getSequenceIdForMatch(),
                        "Double Chance 3");
        market.setLineId(null);
        market.setIsValid(true);
        market.setSequenceId("3");
        market.put("A in Round 3 or by Decision", p3a + pda);
        market.put("B in Round 3 or by Decision", p3b + pdb);
        return market;
    }

    public Market generateDoubleChanceFour() {
        Market market = new Market(MarketCategory.GENERAL, "FT:DC", ufcMatchState.getSequenceIdForMatch(),
                        "Double Chance 4");
        market.setLineId(null);
        market.setIsValid(true);
        market.setSequenceId("4");
        market.put("A in Round 4 or by Decision", p4a + pda);
        market.put("B in Round 4 or by Decision", p4b + pdb);
        return market;
    }

    public Market generateDoubleChanceFive() {
        Market market = new Market(MarketCategory.GENERAL, "FT:DC", ufcMatchState.getSequenceIdForMatch(),
                        "Double Chance 5");
        market.setLineId(null);
        market.setIsValid(true);
        market.setSequenceId("5");
        market.put("A in Round 5 or by Decision", p5a + pda);
        market.put("B in Round 5 or by Decision", p5b + pdb);
        return market;
    }

    public Market generateDoubleChanceSix() {
        Market market = new Market(MarketCategory.GENERAL, "FT:DC", ufcMatchState.getSequenceIdForMatch(),
                        "Double Chance 6");
        market.setLineId(null);
        market.setIsValid(true);
        market.setSequenceId("6");
        market.put("A in Round 1-2 or by Decision", p1a + p2a + pda);
        market.put("B in Round 1-2 or by Decision", p1b + p2b + pdb);
        return market;
    }

    public Market generateDoubleChanceSeven() {
        Market market = new Market(MarketCategory.GENERAL, "FT:DC", ufcMatchState.getSequenceIdForMatch(),
                        "Double Chance 7");
        market.setLineId(null);
        market.setIsValid(true);
        market.setSequenceId("7");
        market.put("A in Round 1-3 or by Decision", p1a + p3a + pda);
        market.put("B in Round 1-3 or by Decision", p1b + p3b + pdb);
        return market;
    }

    public Market generateDoubleChanceEight() {
        Market market = new Market(MarketCategory.GENERAL, "FT:DC", ufcMatchState.getSequenceIdForMatch(),
                        "Double Chance 8");
        market.setLineId(null);
        market.setIsValid(true);
        market.setSequenceId("8");
        market.put("A in Round 2-3 or by Decision", p3a + p2a + pda);
        market.put("B in Round 2-3 or by Decision", p3b + p2b + pdb);
        return market;
    }

    public Market generateDoubleChanceNine() {
        Market market = new Market(MarketCategory.GENERAL, "FT:DC", ufcMatchState.getSequenceIdForMatch(),
                        "Double Chance 9");
        market.setLineId(null);
        market.setIsValid(true);
        market.setSequenceId("9");
        market.put("A in Round 2-4 or by Decision", p2a + p4a + pda);
        market.put("B in Round 2-4 or by Decision", p2b + p4b + pdb);
        return market;
    }

    public Market generateDoubleChanceTen() {
        Market market = new Market(MarketCategory.GENERAL, "FT:DC", ufcMatchState.getSequenceIdForMatch(),
                        "Double Chance 10");
        market.setLineId(null);
        market.setIsValid(true);
        market.setSequenceId("10");
        market.put("A in Round 3-4 or by Decision", p3a + p4a + pda);
        market.put("B in Round 3-4 or by Decision", p3b + p4b + pdb);
        return market;
    }

    public Market generateGoDistanceMarket() {
        Market market = new Market(MarketCategory.GENERAL, "FT:GD", ufcMatchState.getSequenceIdForMatch(),
                        "Go the distance");
        market.setLineId(null);
        market.setIsValid(true);
        market.put("Yes", pda + pdb + draw);
        market.put("No", p3a + p4a + p5a + p1a + p2a + p3b + p4b + p5b + p1b + p2b);
        return market;
    }

    public Market generateGroupRoundBettingMarket() {
        Market market = new Market(MarketCategory.GENERAL, "FT:GRB1", ufcMatchState.getSequenceIdForMatch(),
                        "Grouped Round Betting 1");
        /*
         * McGregor Round 1-2 17.0% McGregor Round 3-4 13.0% McGregor Round 5 5.0% McGregor by Decision/Tech Dec 40.0%
         * Khabib Round 1-2 4.0% Khabib Round 3-4 4.0% Khabib Round 5 1.0% Khabib by Decision/Tech Dec 15.7% Draw or
         * Tech Draw 0.3%
         */
        market.setLineId(null);
        market.setIsValid(true);
        market.put("A Round 1-2", p1a + p2a);
        market.put("A Round 3-4", p3a + p4a);
        market.put("A Round 5", p5a);
        market.put("A by Decision/Tech Dec", pda);
        market.put("B Round 1-2", p1b + p2b);
        market.put("B Round 3-4", p3b + p4b);
        market.put("B Round 5", p5b);
        market.put("B by Decision/Tech Dec", pdb);
        market.put("Draw or Tech Draw", draw);
        return market;
    }

    public Market generateGroupRoundBettingOneMarket() {
        Market market = new Market(MarketCategory.GENERAL, "FT:GRB1", ufcMatchState.getSequenceIdForMatch(),
                        "Grouped Round Betting 1");
        /*
         * McGregor Round 1-2 17.0% McGregor Round 3-4 13.0% McGregor Round 5 5.0% McGregor by Decision/Tech Dec 40.0%
         * Khabib Round 1-2 4.0% Khabib Round 3-4 4.0% Khabib Round 5 1.0% Khabib by Decision/Tech Dec 15.7% Draw or
         * Tech Draw 0.3%
         */
        market.setLineId(null);
        market.setIsValid(true);
        market.put("A Round 1-2", p1a + p2a);
        if (roundsPerMatch == 5) {
            market.put("A Round 3-4", p3a + p4a);
            market.put("A Round 5", p5a);
        } else {
            market.put("A Round 3", p3a);
        }
        market.put("A by Decision/Tech Dec", pda);
        market.put("B Round 1-2", p1b + p2b);
        if (roundsPerMatch == 5) {
            market.put("B Round 3-4", p3b + p4b);
            market.put("B Round 5", p5b);
        } else {
            market.put("B Round 3", p3b);
        }
        market.put("B by Decision/Tech Dec", pdb);
        market.put("Draw or Tech Draw", draw);
        return market;
    }

    public Market generateGroupRoundBettingTwoMarket() {
        Market market = new Market(MarketCategory.GENERAL, "FT:GRB2", ufcMatchState.getSequenceIdForMatch(),
                        "Grouped Round Betting 2");
        /*
         * McGregor Round 1-2 17.0% McGregor Round 3-4 13.0% McGregor Round 5 5.0% McGregor by Decision/Tech Dec 40.0%
         * Khabib Round 1-2 4.0% Khabib Round 3-4 4.0% Khabib Round 5 1.0% Khabib by Decision/Tech Dec 15.7% Draw or
         * Tech Draw 0.3%
         */
        market.setLineId(null);
        market.setIsValid(true);
        market.put("A Round 1", p1a);
        market.put("A Round 2-3", p2a + p3a);
        if (roundsPerMatch == 5)
            market.put("A Round 4-5", p4a + p5a);
        market.put("A by Decision/Tech Dec", pda);
        market.put("B Round 1", p1b);
        market.put("B Round 2-3", p2b + p3b);
        if (roundsPerMatch == 5)
            market.put("B Round 4-5", p4b + p5b);
        market.put("B by Decision/Tech Dec", pdb);
        market.put("Draw or Tech Draw", draw);
        return market;
    }

    public Market generateGroupRoundBettingThreeMarket() {
        Market market = new Market(MarketCategory.GENERAL, "FT:GRB3", ufcMatchState.getSequenceIdForMatch(),
                        "Grouped Round Betting 3");
        /*
         * McGregor Round 1-2 17.0% McGregor Round 3-4 13.0% McGregor Round 5 5.0% McGregor by Decision/Tech Dec 40.0%
         * Khabib Round 1-2 4.0% Khabib Round 3-4 4.0% Khabib Round 5 1.0% Khabib by Decision/Tech Dec 15.7% Draw or
         * Tech Draw 0.3%
         */
        market.setLineId(null);
        market.setIsValid(true);
        market.put("A Round 1-2", p1a + p2a);
        market.put("A Round 3-5", p3a + p4a + p5a);
        market.put("A by Decision/Tech Dec", pda);
        market.put("B Round 1-2", p1b + p2b);
        market.put("B Round 3-5", p3b + p4b + p5b);
        market.put("B by Decision/Tech Dec", pdb);
        market.put("Draw or Tech Draw", draw);
        return market;
    }

    public Market generateGroupRoundBettingFourMarket() {
        Market market = new Market(MarketCategory.GENERAL, "FT:GRB4", ufcMatchState.getSequenceIdForMatch(),
                        "Grouped Round Betting 4");
        /*
         * McGregor Round 1-2 17.0% McGregor Round 3-4 13.0% McGregor Round 5 5.0% McGregor by Decision/Tech Dec 40.0%
         * Khabib Round 1-2 4.0% Khabib Round 3-4 4.0% Khabib Round 5 1.0% Khabib by Decision/Tech Dec 15.7% Draw or
         * Tech Draw 0.3%
         */
        market.setLineId(null);
        market.setIsValid(true);
        market.put("A Round 1-3", p1a + p2a + p3a);
        market.put("A Round 4-5", p4a + p5a);
        market.put("A by Decision/Tech Dec", pda);
        market.put("B Round 1-3", p1b + p2b + p3b);
        market.put("B Round 4-5", p4b + p5b);
        market.put("B by Decision/Tech Dec", pdb);
        market.put("Draw or Tech Draw", draw);
        return market;
    }

    public Market generateNotWinInRoundMarket() {
        Market market = new Market(MarketCategory.GENERAL, "FT:NTWR", ufcMatchState.getSequenceIdForMatch(),
                        "Not to Win in a Round");
        /*
         * McGregor Not to Win in Round 1 91.0% McGregor Not to Win in Round 2 92.0% McGregor Not to Win in Round 3
         * 93.0% McGregor Not to Win in Round 4 94.0% McGregor Not to Win in Round 5 95.0% McGregor Not to win on by
         * Decision/Tech Dec 60.0% Khabib Not to Win in Round 1 98.0% Khabib Not to Win in Round 2 98.0% Khabib Not to
         * Win in Round 3 98.0% Khabib Not to Win in Round 4 98.0% Khabib Not to Win in Round 5 99.0% Khabib Not to win
         * on by Decision/Tech Dec 84.3% Fight not to be a Draw or Tech Draw 99.7%
         * 
         */
        market.setLineId(null);
        market.setIsValid(true);
        market.put("A Not to Win in Round 1", 1 - p1a);
        market.put("A Not to Win in Round 2", 1 - p2a);
        market.put("A Not to Win in Round 3", 1 - p3a);
        if (roundsPerMatch > 3) {
            market.put("A Not to Win in Round 4", 1 - p4a);
            market.put("A Not to Win in Round 5", 1 - p5a);
        }
        market.put("A Not to Win Not to win on by Decision/Tech Dec", 1 - pda);
        market.put("B Not to Win in Round 1", 1 - p1b);
        market.put("B Not to Win in Round 2", 1 - p2b);
        market.put("B Not to Win in Round 3", 1 - p3b);
        if (roundsPerMatch > 3) {
            market.put("B Not to Win in Round 4", 1 - p4b);
            market.put("B Not to Win in Round 5", 1 - p5b);
        }
        market.put("B Not to Win Not to win on by Decision/Tech Dec", 1 - pdb);
        market.put("Fight not to be a Draw or Tech Draw", 1 - draw);
        return market;
    }


}
