package ats.algo.sport.tennisG;

import java.util.Map.Entry;

import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.common.TeamId;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketCategory;
import ats.algo.core.markets.Markets;
import ats.algo.montecarloframework.MarketsFactory;
import ats.algo.sport.tennisG.TennisGMatchParams;
import ats.algo.sport.tennisG.TennisGMatchState;

public class TennisGMatchMarketsFactory extends MarketsFactory {
    private TwoWayStatistic matchWinner;
    private CorrectScoreStatistic correctSetScore;
    private TotalStatistic gamesTotal;
    private TotalStatistic gamesTotalA;
    private TotalStatistic gamesTotalB;
    private HandicapStatistic gamesHandicap;
    private TwoWayStatistic tieBreakPlayed;
    private TwoWayStatistic winASetA;
    private TwoWayStatistic winASetB;
    private TwoWayStatistic raceToFiveGames;
    private TwoWayStatistic thisGameWinner;
    private TwoWayStatistic nextGameWinner;
    private TwoWayStatistic gamePlusTwoWinner;
    private TwoWayStatistic thisGameToDeuce;
    private TwoWayStatistic nextGameToDeuce;
    private TwoWayStatistic gamePlusTwoToDeuce;
    private TwoWayStatistic nextGamePointOne;
    private TwoWayStatistic nextGamePointTwo;
    private CorrectScoreStatistic thisGameCorrectScore;
    private TwoWayStatistic tieBreakWinner;
    private CorrectScoreStatistic tieBreakScore;

    private int nSetsInMatch;

    public TennisGMatchMarketsFactory(int nSetsInMatch, TennisGMatchState matchState) {
        super();
        int setNo = matchState.getSetNo();
        int gameNo = matchState.getGameNo();

        int maxSetScore = nSetsInMatch / 2 + 1;
        this.nSetsInMatch = nSetsInMatch;
        /*
         * Set the characteristics of each market
         */
        matchWinner = new TwoWayStatistic("FT:ML", "Match winner", true, "M", "A", "B");

        correctSetScore = new CorrectScoreStatistic("FT:CS", "Match set correct score", true, "M", maxSetScore + 1);

        // allow for long final set
        gamesTotal = new TotalStatistic("FT:OU", "Match total games", true, "M", 200);
        gamesTotalA = new TotalStatistic("FT:OU:A", "Match total games A", true, "M", 100);
        gamesTotalB = new TotalStatistic("FT:OU:B", "Match total games B", true, "M", 100);
        gamesHandicap = new HandicapStatistic("FT:SPRD", "Match total games handicap", true, "M",
                        nSetsInMatch * 12 + 1);

        tieBreakPlayed = new TwoWayStatistic("FT:TBIM", "Tie break played in match",
                        !matchState.isTieBreakAlreadyPlayedInMatch(), "M", "Yes", "No");

        winASetA = new TwoWayStatistic("FT:W1S:A", "A to win at least one set", !matchState.isAlreadyWonASetA(), "M",
                        "Yes", "No");

        winASetB = new TwoWayStatistic("FT:W1S:B", "B to win at least one set", !matchState.isAlreadyWonASetB(), "M",
                        "Yes", "No");

        String thisSetSequenceId = matchState.getSequenceIdforSet(0);
        String marketDescription = String.format("First player to win 5 games in set %d", setNo);
        raceToFiveGames = new TwoWayStatistic("FT:PW5G", marketDescription,
                        !matchState.isAlreadyOver4GamesInCurrentSet(), thisSetSequenceId, "A", "B");

        String thisGameSequenceId = matchState.getSequenceIdForGame(0);
        Boolean generateMarket = !matchState.isPreMatch() && matchState.isGameMayBePlayed(gameNo);
        marketDescription = String.format("Set %d Game %d Winner", setNo, gameNo);
        thisGameWinner = new TwoWayStatistic("G:ML", marketDescription, generateMarket, thisGameSequenceId, "A", "B");

        marketDescription = String.format("Set %d Game %d reaches deuce", setNo, gameNo);
        thisGameToDeuce = new TwoWayStatistic("G:DEUCE", marketDescription, generateMarket, thisGameSequenceId, "Yes",
                        "No");

        String nextGameSequenceId = matchState.getSequenceIdForGame(1);
        generateMarket = !matchState.isPreMatch() && matchState.isGameMayBePlayed(gameNo + 1);
        marketDescription = String.format("Set %d Game %d Winner", setNo, gameNo + 1);
        nextGameWinner = new TwoWayStatistic("G:ML", marketDescription, generateMarket, nextGameSequenceId, "A", "B");

        marketDescription = String.format("Set %d Game %d reaches deuce", setNo, gameNo + 1);
        nextGameToDeuce = new TwoWayStatistic("G:DEUCE", marketDescription, generateMarket, nextGameSequenceId, "Yes",
                        "No");

        String sequenceId = matchState.getSequenceIdForGame(2);
        marketDescription = String.format("Set %d Game %d Winner", setNo, gameNo + 2);
        gamePlusTwoWinner = new TwoWayStatistic("G:ML", marketDescription, generateMarket, sequenceId, "A", "B");

        marketDescription = String.format("Set %d Game %d reaches deuce", setNo, gameNo + 2);
        gamePlusTwoToDeuce = new TwoWayStatistic("G:DEUCE", marketDescription, generateMarket, sequenceId, "Yes", "No");

        sequenceId = matchState.getSequenceIdForNextGamePoint(1);
        generateMarket = !matchState.isPreMatch() && sequenceId != null;
        marketDescription = String.format("Set %d Game %d Point 1 Winner", setNo, gameNo + 1);
        nextGamePointOne = new TwoWayStatistic("G:PW", marketDescription, generateMarket, sequenceId, "A", "B");

        sequenceId = matchState.getSequenceIdForNextGamePoint(2);
        generateMarket = !matchState.isPreMatch() && sequenceId != null;
        marketDescription = String.format("Set %d Game %d Point 2 Winner", setNo, gameNo + 1);
        nextGamePointTwo = new TwoWayStatistic("G:PW", marketDescription, generateMarket, sequenceId, "A", "B");

        sequenceId = matchState.getSequenceIdForGame(0);
        generateMarket = !matchState.isPreMatch() && sequenceId != null;
        marketDescription = String.format("Set %d Game %d Correct Score", setNo, gameNo);
        thisGameCorrectScore = new CorrectScoreStatistic("G:CS", marketDescription, generateMarket, sequenceId, 6);

        sequenceId = matchState.getSequenceIdforSet(0);
        generateMarket = matchState.isInTieBreak();
        marketDescription = String.format("Set %d Tie break Winner", setNo);
        tieBreakWinner = new TwoWayStatistic("P:TBML", marketDescription, generateMarket, sequenceId, "A", "B");
        marketDescription = String.format("Set %d Tie break correct score", setNo);
        tieBreakScore = new CorrectScoreStatistic("P:TBCS", marketDescription, generateMarket, sequenceId, 100);
    }

    public void updateStats(TennisGMatchState matchState, TennisGMatchFacts matchFacts) {
        matchWinner.increment(matchState.getSetsA() > matchState.getSetsB());
        correctSetScore.increment(matchState.getSetsA(), matchState.getSetsB());
        int totalGamesA = 0;
        int totalGamesB = 0;
        boolean tbPlayed = false;
        for (int i = 0; i < nSetsInMatch; i++) {
            totalGamesA += matchState.getGameScoreInSetN(i).A;
            totalGamesB += matchState.getGameScoreInSetN(i).B;
            tbPlayed = tbPlayed || (matchState.getGameScoreInSetN(i).A + matchState.getGameScoreInSetN(i).B == 13);
        }
        gamesTotal.increment(totalGamesA + totalGamesB);
        gamesTotalA.increment(totalGamesA);
        gamesTotalB.increment(totalGamesB);
        gamesHandicap.increment(totalGamesA - totalGamesB);
        tieBreakPlayed.increment(tbPlayed);

        winASetA.increment(matchState.getSetsA() > 0);
        winASetB.increment(matchState.getSetsB() > 0);
        if (matchFacts.neitherPlayerHasOverFourGamesInThisSet)
            raceToFiveGames.increment(matchFacts.firstToFiveGamesInThisSetIsA);
        if (!matchFacts.preMatch) {
            if (matchFacts.inTieBreak) {
                tieBreakWinner.increment(matchFacts.tieBreakWinnerIsA);
                tieBreakScore.increment(matchFacts.tieBreakScoreA, matchFacts.tieBreakScoreB);
            } else {
                thisGameWinner.increment(matchFacts.thisGameWinnerIsA);
                thisGameToDeuce.increment(matchFacts.thisGametoDeuce);
                thisGameCorrectScore.increment(matchFacts.thisGameScoreA, matchFacts.thisGameScoreB);
                if (matchFacts.mayBeNextGame) {
                    nextGameWinner.increment(matchFacts.nextGameWinnerIsA);
                    nextGameToDeuce.increment(matchFacts.nextGameToDeuce);
                    nextGamePointOne.increment(matchFacts.nextGamePointOneWinnerIsA);
                    nextGamePointTwo.increment(matchFacts.nextGamePointTwoWinnerIsA);
                }
                if (matchFacts.mayBeGamePlus2) {
                    gamePlusTwoWinner.increment(matchFacts.gamePlusTwoWinnerIsA);
                    gamePlusTwoToDeuce.increment(matchFacts.gamePlusTwoToDeuce);
                }
            }
        }
    }

    @Override
    public void addDerivedMarkets(Markets markets, AlgoMatchState matchState, MatchParams matchParams) {
        /*
         * Derive the no of sets played in match from the set score market
         */
        TennisGMatchState tennisMatchState = (TennisGMatchState) matchState;
        Market sourceMarket = markets.get("FT:CS");
        double[] probs = {0, 0, 0, 0, 0, 0};
        for (Entry<String, Double> e : sourceMarket.getSelectionsProbs().entrySet()) {
            String selectionName = e.getKey();
            double prob = e.getValue();
            probs[parseNameForTotal(selectionName)] += prob;
        }
        Market market = new Market(MarketCategory.GENERAL, "FT:NUMSET", tennisMatchState.getSequenceIdForMatch(),
                        "No of sets played in match");
        market.setIsValid(true);
        for (int i = 0; i < probs.length; i++) {
            if (probs[i] > 0) {
                String selectionName = String.format("%d sets", i);
                market.put(selectionName, probs[i]);
            }
        }
        markets.addMarketWithShortKey(market);
        /*
         * Derive the point winner markets from the matchParams
         */

        if (!tennisMatchState.isPreMatch()) {
            TeamId teamId = tennisMatchState.getOnServeNow();
            double probAWinsPoint;
            if (teamId == TeamId.A)
                probAWinsPoint = ((TennisGMatchParams) matchParams).getOnServePctA().getMean();
            else
                probAWinsPoint = 1 - ((TennisGMatchParams) matchParams).getOnServePctB().getMean();
            int pointNo = tennisMatchState.getPointNo();
            int gameNo = tennisMatchState.getGameNo();
            int setNo = tennisMatchState.getSetNo();
            if (tennisMatchState.isPointMayBePlayed(pointNo + 2)) {
                String marketName = String.format("Set %d Game %d Point %d Winner", setNo, gameNo, pointNo + 2);
                market = new Market(MarketCategory.GENERAL, "G:PW", tennisMatchState.getSequenceIdForPoint(2),
                                marketName);
                market.setIsValid(true);
                market.put("A", probAWinsPoint);
                market.put("B", 1 - probAWinsPoint);
                markets.addMarketWithShortKey(market);
            }
            if (tennisMatchState.isPointMayBePlayed(pointNo + 3)) {
                String marketName = String.format("Set %d Game %d Point %d Winner", setNo, gameNo, pointNo + 3);
                market = new Market(MarketCategory.GENERAL, "G:PW", tennisMatchState.getSequenceIdForPoint(3),
                                marketName);
                market.setIsValid(true);
                market.put("A", probAWinsPoint);
                market.put("B", 1 - probAWinsPoint);
                markets.addMarketWithShortKey(market);
            }
        }
    }

    /**
     * parses a string like "2-1" and generates the sum of the two integers
     * 
     * @param selectionName as generated for the "P:CS" market - 3 chars long
     * @return
     */
    private int parseNameForTotal(String s) {
        String scoreA = s.substring(0, 1);
        String scoreB = s.substring(2, 3);
        return Integer.parseInt(scoreA) + Integer.parseInt(scoreB);
    }

}
