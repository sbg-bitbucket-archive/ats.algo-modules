package ats.algo.sport.tennisG;

import ats.algo.sport.tennisG.TennisGMatchState;

/**
 * holds the set of "facts" collected during each iteration of the Monte Carlo run of the match class
 * 
 * @author Geoff
 *
 */
class TennisGMatchFacts {
    boolean neitherPlayerHasOverFourGamesInThisSet;
    boolean preMatch;
    boolean inTieBreak;
    boolean mayBeNextGame;
    boolean mayBeGamePlus2;
    boolean firstToFiveGamesInThisSetIsA;
    boolean thisGameWinnerIsA;
    boolean nextGameWinnerIsA;
    boolean gamePlusTwoWinnerIsA;
    boolean thisGametoDeuce;
    boolean nextGameToDeuce;
    boolean gamePlusTwoToDeuce;
    boolean thisGamePointPlusTwoWinnerIsA;
    boolean thisGamePointPlusThreeWinnerIsA;
    boolean nextGamePointOneWinnerIsA;
    boolean nextGamePointTwoWinnerIsA;
    boolean tieBreakWinnerIsA;
    int tieBreakScoreA;
    int tieBreakScoreB;
    int thisGameScoreA;
    int thisGameScoreB;

    void reset(TennisGMatchState matchState) {
        preMatch = matchState.isPreMatch();
        neitherPlayerHasOverFourGamesInThisSet = !matchState.isAlreadyOver4GamesInCurrentSet();
        inTieBreak = matchState.isInTieBreak();
        int startingGameNo = matchState.getGameNo();
        mayBeNextGame = matchState.isGameMayBePlayed(startingGameNo + 1);
        mayBeGamePlus2 = matchState.isGameMayBePlayed(startingGameNo + 2);

    }
}
