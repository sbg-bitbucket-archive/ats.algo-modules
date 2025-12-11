package ats.algo.sport.snooker;


import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.common.TeamId;
import ats.algo.core.markets.Markets;
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
    private NWayStatistic matchCenturies;
    private NWayStatistic matchCenturiesA;
    private NWayStatistic matchCenturiesB;
    private TwoWayStatistic raceTo30;
    private TwoWayStatistic break50A;
    private TwoWayStatistic break50B;
    private TwoWayStatistic break100A;
    private TwoWayStatistic break100B;
    private HandicapStatistic framesHandicap;
    private TotalStatistic framesTotal;
    private TwoWayStatistic frameWinner;
    private TwoWayStatistic frameOddEven;
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
    private boolean preMatch;

    private String[] matchCenturiesSelections = {"0", "1", "2", "3", "4", "5+"};

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
        if (matchState.getMatchCenturies(TeamId.UNKNOWN) <= 5)

            matchCenturies = new NWayStatistic("FT:MC", "Match Centuries", generateMarket, "M",
                            matchCenturiesSelections);

        if (matchState.getMatchCenturies(TeamId.UNKNOWN) <= 5)

            matchCenturiesA = new NWayStatistic("FT:MC:A", "Player A Match Centuries", generateMarket, "M",
                            matchCenturiesSelections);

        if (matchState.getMatchCenturies(TeamId.UNKNOWN) <= 5)

            matchCenturiesB = new NWayStatistic("FT:MC:B", "Player B Match Centuries", generateMarket, "M",
                            matchCenturiesSelections);

        frameOddEven = new TwoWayStatistic("FT:OE", "Frame Odd/Even", generateMarket, fullTime, "Odd", "Even");
        framesHandicap = new HandicapStatistic("FT:SPRD", "Match Frame handicap", generateMarket, fullTime,
                        maxHandicapMatch);

        framesTotal = new TotalStatistic("FT:OU", "Total frames", generateMarket, fullTime, maxPointScoreA);
        String marketDescription = String.format("Frame %d Winner ", currentFrameNo);
        preMatch = matchState.preMatch();
        if (preMatch) {
            frameWinner = new TwoWayStatistic("P:ML", marketDescription, generateMarket, thisFrameSequenceId, "A", "B");
            marketDescription = String.format("Frame %d Race to 30 ", currentFrameNo);
            raceTo30 = new TwoWayStatistic("P:R30", marketDescription, generateMarket, thisFrameSequenceId, "A", "B");
            marketDescription = String.format("Frame %d Player A break 50 ", currentFrameNo);
            break50A = new TwoWayStatistic("P:B50:A", marketDescription, generateMarket, thisFrameSequenceId, "Yes",
                            "No");
            marketDescription = String.format("Frame %d Player B break 50 ", currentFrameNo);
            break50B = new TwoWayStatistic("P:B50:B", marketDescription, generateMarket, thisFrameSequenceId, "Yes",
                            "No");
            marketDescription = String.format("Frame %d Player A Centuries ", currentFrameNo);
            break100A = new TwoWayStatistic("P:B100:A", marketDescription, generateMarket, thisFrameSequenceId, "Yes",
                            "No");
            marketDescription = String.format("Frame %d Player B Centuries ", currentFrameNo);
            break100B = new TwoWayStatistic("P:B100:B", marketDescription, generateMarket, thisFrameSequenceId, "Yes",
                            "No");

        } else {
            thisFrameSequenceId = matchState.getSequenceIdforFrame(1);
            if (currentFrameNo != frameNo) {
                marketDescription = String.format("Frame %d Winner ", currentFrameNo + 1);
                frameWinner = new TwoWayStatistic("P:ML", marketDescription, generateMarket, thisFrameSequenceId, "A",
                                "B");
                marketDescription = String.format("Frame %d Race to 30 ", currentFrameNo + 1);
                raceTo30 = new TwoWayStatistic("P:R30", marketDescription, generateMarket, thisFrameSequenceId, "A",
                                "B");
                marketDescription = String.format("Frame %d Player A break 50 ", currentFrameNo + 1);
                break50A = new TwoWayStatistic("P:B50:A", marketDescription, generateMarket, thisFrameSequenceId, "Yes",
                                "No");
                marketDescription = String.format("Frame %d Player B break 50 ", currentFrameNo + 1);
                break50B = new TwoWayStatistic("P:B50:B", marketDescription, generateMarket, thisFrameSequenceId, "Yes",
                                "No");
                marketDescription = String.format("Frame %d Player A Centuries ", currentFrameNo + 1);
                break100A = new TwoWayStatistic("P:B100:A", marketDescription, generateMarket, thisFrameSequenceId,
                                "Yes", "No");
                marketDescription = String.format("Frame %d Player B Centuries ", currentFrameNo + 1);
                break100B = new TwoWayStatistic("P:B100:B", marketDescription, generateMarket, thisFrameSequenceId,
                                "Yes", "No");
            }
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
        int framesA = matchState.getFramesA();
        int framesB = matchState.getFramesB();
        int raceA = 0;
        int raceB = 0;
        for (int i = 1; i < matchState.getRaceScore().length; i++) {
            if (i == timesRace2) {
                raceA = matchState.getRaceScore(i).A - matchState.getRaceScore(i - 1).A;
                raceB = matchState.getRaceScore(i).B - matchState.getRaceScore(i - 1).B;
            }
        }
        TeamId teamId = TeamId.UNKNOWN;
        if (framesA > framesB)
            teamId = TeamId.A;
        if (framesA < framesB)
            teamId = TeamId.B;
        matchWinnersetsOdd.increment(teamId == TeamId.A);
        framesHandicap.increment(framesA - framesB);
        if (preMatch) {
            frameWinner.increment(matchFacts.frameWinnerCurrentIsA == TeamId.A);
            raceTo30.increment(teamId == TeamId.A);
        } else {
            if (currentFrameNo != frameNo && matchFacts.frameWinnerNextIsA != TeamId.UNKNOWN) {
                frameWinner.increment(matchFacts.frameWinnerNextIsA == TeamId.A);
                raceTo30.increment(teamId == TeamId.A);
            }
        }
        framesTotal.increment(framesA + framesB);
        if (timesRace2 * raceNumber < frameNo && raceA >= 0 && raceB >= 0)
            frameXtoYCorrectsetScore.increment(raceA, raceB);
        frameCorrectScore.increment(framesA, framesB);
        if (raceNumber * timesRace < frameNo)
            raceTo4Frames.increment(matchFacts.frame4WinnerIsA);
        frameOddEven.increment(isOdd(framesA + framesB));
        raceTo30.increment(teamId == TeamId.A);
        break50A.increment(matchFacts.break50PlusA);
        break50B.increment(matchFacts.break50PlusB);
        break100A.increment(matchFacts.break100PlusA);
        break100B.increment(matchFacts.break100PlusB);
        matchCenturies.increment(matchState.getMatchCenturies(TeamId.UNKNOWN) >= 5 ? 5
                        : matchState.getMatchCenturies(TeamId.UNKNOWN));
        matchCenturiesA.increment(
                        matchState.getMatchCenturies(TeamId.A) >= 5 ? 5 : matchState.getMatchCenturies(TeamId.A));
        matchCenturiesB.increment(
                        matchState.getMatchCenturies(TeamId.B) >= 5 ? 5 : matchState.getMatchCenturies(TeamId.B));
    }

    @Override
    public void addDerivedMarkets(Markets markets, AlgoMatchState matchState, MatchParams matchParams) {
        SnookerStaticMarketFactory snookerStaticMarketFactory =
                        new SnookerStaticMarketFactory((SnookerMatchState) matchState);
        Markets staticMarkets = snookerStaticMarketFactory.generatSnookerStaticMarkets();
        staticMarkets.forEach(x -> markets.addMarketWithFullKey(x));

    }

    private boolean isOdd(int i) {
        return i % 2 == 1;
    }

}
