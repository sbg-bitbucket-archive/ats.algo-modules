package ats.algo.sport.snooker;

import ats.algo.core.common.TeamId;
import ats.algo.montecarloframework.MarketsFactory;
import ats.algo.sport.snooker.SnookerMatch.SnookerMatchFacts;

public class SnookerMatchMarketsFactory extends MarketsFactory {

    /**
     * holds any facts about a simulation of a match that has just been played (via the Match class) that are need in
     * the calculation of the required statistics and that are not captured by the matchState object for example if we
     * need to know who won set N+2, where N is the current set, then we need to collect that "fact" in this class
     * 
     * @author Robert
     *
     */

    private TwoWayStatistic matchWinnersetsOdd;
    private HandicapStatistic framesHandicap;
    private TotalStatistic framesTotal;
    private TwoWayStatistic frameWinner;
    private TwoWayStatistic nextFrameWinner;
    private CorrectScoreStatistic frameCorrectScore;
    private CorrectScoreStatistic frameXtoYCorrectsetScore;

    private TwoWayStatistic raceTo4Frames;

    private int frameNo;
    private int currentFrameNo;
    private int currentFrameNoA;
    private int currentFrameNoB;
    private int raceNumber;
    private int timesRace;
    private int timesRace2;
    private int startX;
    private int endY;

    SnookerMatchMarketsFactory(SnookerMatchState matchState) {
        frameNo = matchState.getSetScoreForWin() * 2 - 1;
        currentFrameNo = matchState.getFramesA() + matchState.getFramesB() + 1;
        currentFrameNoA = matchState.getFramesA();
        currentFrameNoB = matchState.getFramesB();
        raceNumber = 4;
        timesRace = Math.max(currentFrameNoA, currentFrameNoB) / raceNumber + 1;
        timesRace2 = (currentFrameNoA + currentFrameNoB) / raceNumber + 1;
        int maxPointScoreA = 200;
        int maxHandicapMatch = 75;
        int maxHandicapSet = 25;
        String thisFrameSequenceId = matchState.getSequenceIdforFrame(0);
        Boolean generateMarket = true;
        String fullTime = "M";

        matchWinnersetsOdd = new TwoWayStatistic("FT:ML", "Match Winner", generateMarket, fullTime, "A", "B");
        framesHandicap = new HandicapStatistic("FT:SPRD", "Match Frame handicap", generateMarket, fullTime,
                        maxHandicapMatch);

        framesTotal = new TotalStatistic("FT:OU", "Total frames", generateMarket, fullTime, maxPointScoreA);
        String marketDescription = String.format("Frame %d Winner ", currentFrameNo);

        frameWinner = new TwoWayStatistic("P:ML", marketDescription, generateMarket, thisFrameSequenceId, "A", "B");
        thisFrameSequenceId = matchState.getSequenceIdforFrame(1);
        if (currentFrameNo != frameNo) {
            marketDescription = String.format("Frame %d Winner ", currentFrameNo + 1);
            nextFrameWinner = new TwoWayStatistic("P:ML", marketDescription, generateMarket, thisFrameSequenceId, "A",
                            "B");
        }

        frameCorrectScore = new CorrectScoreStatistic("FT:CS", "Frame Correct Score", generateMarket, fullTime,
                        maxHandicapSet);
        marketDescription = String.format("Race to %d Frames ", raceNumber * timesRace);
        String thisRaceFrameSequenceId = String.format("R%d", raceNumber * timesRace);
        if (raceNumber * timesRace < frameNo)
            raceTo4Frames = new TwoWayStatistic("P:RACE", marketDescription, generateMarket, thisRaceFrameSequenceId,
                            "A", "B");
        startX = timesRace2 * raceNumber - 3;
        endY = timesRace2 * raceNumber;
        String xToY = "P:X-Y";
        marketDescription = String.format("Frame %d-%d Correct Score ", startX, endY);
        thisFrameSequenceId = String.format("S%d", raceNumber * timesRace2);
        if (timesRace2 * raceNumber < frameNo)
            frameXtoYCorrectsetScore = new CorrectScoreStatistic(xToY, marketDescription, generateMarket,
                            thisFrameSequenceId, maxHandicapSet);

    }

    void updateStats(SnookerMatchState matchState, SnookerMatchFacts matchFacts) {
        int setsA = matchState.getFramesA();
        int setsB = matchState.getFramesB();
        int raceA = 0;
        int raceB = 0;
        for (int i = 1; i < matchState.getRaceScore().length; i++) {
            if (i == timesRace2) {
                raceA = matchState.getRaceScore(i).A - matchState.getRaceScore(i - 1).A;
                raceB = matchState.getRaceScore(i).B - matchState.getRaceScore(i - 1).B;
            }
        }
        TeamId teamId = TeamId.UNKNOWN;
        if (setsA > setsB)
            teamId = TeamId.A;
        if (setsA < setsB)
            teamId = TeamId.B;
        matchWinnersetsOdd.increment(teamId == TeamId.A);
        framesHandicap.increment(setsA - setsB);
        frameWinner.increment(matchFacts.frameWinnerCurrentIsA == TeamId.A);
        if (currentFrameNo != frameNo && matchFacts.frameWinnerNextIsA != TeamId.UNKNOWN)
            nextFrameWinner.increment(matchFacts.frameWinnerNextIsA == TeamId.A);
        framesTotal.increment(setsA + setsB);
        if (timesRace2 * raceNumber < frameNo && raceA >= 0 && raceB >= 0)
            frameXtoYCorrectsetScore.increment(raceA, raceB);
        frameCorrectScore.increment(setsA, setsB);
        if (raceNumber * timesRace < frameNo)
            raceTo4Frames.increment(matchFacts.frame4WinnerIsA);

    }

}
