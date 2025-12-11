package ats.algo.sport.tennis;

import ats.algo.core.common.TeamId;
import ats.algo.sport.tennis.TennisMatchState;

/**
 * holds the set of "facts" collected during each iteration of the Monte Carlo run of the match class
 * 
 * @author Geoff
 *
 */
class TennisMatchFacts {
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
    boolean nextGamePlayed;
    boolean gamePlusTwoToDeuce;
    boolean gamePlusTwoPlayed;
    boolean thisGamePointPlusOneWinnerIsA;
    boolean thisGamePointPlusTwoWinnerIsA;
    boolean thisGamePointEqualOneWinnerIsA;
    boolean isBreakPointPossible;

    boolean nextGamePointOneWinnerIsA;
    boolean nextGamePointTwoWinnerIsA;
    boolean tieBreakWinnerIsA;
    boolean mayBeNextSet;
    int tieBreakScoreA;
    int tieBreakScoreB;
    int thisGameScoreA;
    int thisGameScoreB;

    int gameScoreAAfter2;
    int gameScoreBAfter2;
    int gameScoreAAfter4;
    int gameScoreBAfter4;
    int gameScoreAAfter6;
    int gameScoreBAfter6;
    int acturalSetNo;
    TeamId setXGameYAndXGameYWinner = TeamId.UNKNOWN;
    TeamId setXGameYAndXGameYPlusOneWinner = TeamId.UNKNOWN;
    int setX = 0;
    int gameY = 0;

    void reset(TennisMatchState matchState) {
        preMatch = matchState.isPreMatch();
        neitherPlayerHasOverFourGamesInThisSet = !matchState.isAlreadyOver4GamesInCurrentSet();
        inTieBreak = matchState.isInTieBreak();
        int startingGameNo = matchState.getGameNo();
        int startingSetNo = matchState.getSetNo();
        mayBeNextGame = matchState.isGameMayBePlayed(startingGameNo + 1);
        mayBeGamePlus2 = matchState.isGameMayBePlayed(startingGameNo + 2);
        acturalSetNo = matchState.getSetNo();
        mayBeNextSet = matchState.isSetMayBePlayed(startingSetNo + 1);
        thisGameScoreA = matchState.getPointsA();
        thisGameScoreB = matchState.getPointsB();
        tieBreakScoreA = matchState.getPointsA();
        tieBreakScoreB = matchState.getPointsB();
        isBreakPointPossible = matchState.isBreakPointStillPossible();
        thisGametoDeuce = false;
        nextGameToDeuce = false;
        gamePlusTwoToDeuce = false;
        nextGamePlayed = false;
        gamePlusTwoPlayed = false;

        setXGameYAndXGameYPlusOneWinner = TeamId.UNKNOWN;
        setXGameYAndXGameYWinner = TeamId.UNKNOWN;
        setX = matchState.getSetNo();
        gameY = matchState.getGameNo();

        gameScoreAAfter2 = 0;
        gameScoreBAfter2 = 0;
        gameScoreAAfter4 = 0;
        gameScoreBAfter4 = 0;
        gameScoreAAfter6 = 0;
        gameScoreBAfter6 = 0;
    }


}
