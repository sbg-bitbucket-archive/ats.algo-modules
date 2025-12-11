package ats.algo.sport.snooker;

import ats.algo.core.baseclasses.MatchResultMarkets;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.TeamId;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketUtilityFunctions;
import ats.algo.genericsupportfunctions.PairOfIntegers;

public class SnookerMatchResultMarkets extends MatchResultMarkets {
    SnookerMatchState previousMatchState;
    SnookerMatchState currentMatchState;
    int marketFrameNo;
    int marketPointNo;

    /**
     * 
     * @param market
     * @param matchState
     * @return
     */
    @Override
    protected CheckMarketResultedOutcome checkMarketResulted(Market market, MatchState previousMatchState,
                    MatchState currentMatchState) {
        this.previousMatchState = (SnookerMatchState) previousMatchState;
        this.currentMatchState = (SnookerMatchState) currentMatchState;

        parseSequenceId(market.getSequenceId());
        int pointsA1;
        int pointsB1;
        int sumA = 0;
        int sumB = 0;
        int currentFrameNo = this.currentMatchState.getFramesA() + this.currentMatchState.getFramesB();
        String winningSelection;

        switch (market.getType()) {
            case "FT:ML":
                if (this.currentMatchState.isMatchCompleted()) {
                    if (this.currentMatchState.getFramesA() > this.currentMatchState.getFramesB())
                        winningSelection = "A";
                    else
                        winningSelection = "B";

                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:HAS147":
                if (this.currentMatchState.isMatchCompleted()) {
                    for (int i = 0; i < (this.currentMatchState.getFramesA()
                                    + this.currentMatchState.getFramesB()); i++) {

                        pointsA1 = this.currentMatchState.getFrameScoreInMatchN(i).A;
                        pointsB1 = this.currentMatchState.getFrameScoreInMatchN(i).B;
                        if (pointsA1 == 147 || pointsB1 == 147)
                            return new CheckMarketResultedOutcome("Yes");
                    }

                    return new CheckMarketResultedOutcome("No");
                } else
                    return null;

            case "FT:MC":
                if (this.currentMatchState.isMatchCompleted()) {
                    int sum = 0;
                    for (int i = 0; i < (this.currentMatchState.getFramesA()
                                    + this.currentMatchState.getFramesB()); i++) {

                        pointsA1 = this.currentMatchState.getFrameScoreInMatchN(i).A;
                        pointsB1 = this.currentMatchState.getFrameScoreInMatchN(i).B;
                        if (pointsA1 > 100 || pointsB1 > 100)
                            sum++;
                    }
                    if (sum > 5)
                        return new CheckMarketResultedOutcome("5+");
                    else
                        return new CheckMarketResultedOutcome(sum + "");
                } else
                    return null;

            case "FT:MC:A":
                if (this.currentMatchState.isMatchCompleted()) {
                    int sum = 0;
                    for (int i = 0; i < (this.currentMatchState.getFramesA()
                                    + this.currentMatchState.getFramesB()); i++) {
                        pointsA1 = this.currentMatchState.getFrameScoreInMatchN(i).A;
                        if (pointsA1 > 100)
                            sum++;
                    }
                    if (sum > 5)
                        return new CheckMarketResultedOutcome("5+");
                    else
                        return new CheckMarketResultedOutcome(sum + "");
                } else
                    return null;

            case "FT:MC:B":
                if (this.currentMatchState.isMatchCompleted()) {
                    int sum = 0;
                    for (int i = 0; i < (this.currentMatchState.getFramesA()
                                    + this.currentMatchState.getFramesB()); i++) {
                        pointsB1 = this.currentMatchState.getFrameScoreInMatchN(i).B;
                        if (pointsB1 > 100)
                            sum++;
                    }
                    if (sum > 5)
                        return new CheckMarketResultedOutcome("5+");
                    else
                        return new CheckMarketResultedOutcome(sum + "");
                } else
                    return null;
            case "FT:SPRD":
                if (this.currentMatchState.isMatchCompleted()) {
                    int setDiff = this.currentMatchState.getFramesA() - this.currentMatchState.getFramesB();
                    winningSelection =
                                    MarketUtilityFunctions.getWinningSelectionHcapMarket(setDiff, market.getLineId());

                    return new CheckMarketResultedOutcome(winningSelection, setDiff);
                } else
                    return null;
            case "FT:OE":
                if (this.currentMatchState.isMatchCompleted()) {
                    int totalFrames = this.currentMatchState.getFramesA() + this.currentMatchState.getFramesB();
                    if (isOdd(totalFrames))
                        return new CheckMarketResultedOutcome("Odd");
                    else
                        return new CheckMarketResultedOutcome("Even");
                } else
                    return null;
            case "FT:PSPRD":
                if (this.currentMatchState.isMatchCompleted()) {
                    for (int i = 0; i < (this.currentMatchState.getFramesA()
                                    + this.currentMatchState.getFramesB()); i++) {

                        pointsA1 = this.currentMatchState.getFrameScoreInMatchN(i).A;
                        pointsB1 = this.currentMatchState.getFrameScoreInMatchN(i).B;
                        sumA += pointsA1;
                        sumB += pointsB1;

                    }

                    int pointtDiff = sumA - sumB;
                    winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(pointtDiff,
                                    market.getLineId());

                    return new CheckMarketResultedOutcome(winningSelection, pointtDiff);
                } else
                    return null;
            case "FT:OU":
                if (this.currentMatchState.isMatchCompleted()) {
                    int pointtTotal = this.currentMatchState.getFramesA() + this.currentMatchState.getFramesB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(pointtTotal,
                                    market.getLineId());

                    return new CheckMarketResultedOutcome(winningSelection, pointtTotal);
                } else
                    return null;

            case "FT:CS":
                if (this.currentMatchState.isMatchCompleted()) {
                    winningSelection = String.format("%d-%d", ((SnookerMatchState) currentMatchState).getFramesA(),
                                    ((SnookerMatchState) currentMatchState).getFramesB());
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "P:ML":
                return checkSetMktResulted();
            case "P:OU":
                return checkFrameOvunMktResulted(market.getLineId());
            case "P:OE":
                return checkFrameOEMktResulted();
            case "P:B50:B":
                return checkFrameBreakMktResulted(TeamId.B, 50);
            case "P:B50:A":
                return checkFrameBreakMktResulted(TeamId.A, 50);
            case "P:B100:B":
                return checkFrameBreakMktResulted(TeamId.B, 100);
            case "P:B100:A":
                return checkFrameBreakMktResulted(TeamId.A, 100);
            case "P:WM":
                return checkFrameWMMktResulted();
            case "P:HAS147":
                return checkFrame147MktResulted();
            case "P:R30":
                if (currentFrameNo == marketFrameNo - 1) {
                    if (this.currentMatchState.getPointsA() >= 30 && this.currentMatchState.getPointsB() < 30)

                        return new CheckMarketResultedOutcome("A");

                    if (this.currentMatchState.getPointsB() >= 30 && this.currentMatchState.getPointsA() < 30)

                        return new CheckMarketResultedOutcome("B");
                    return null;
                }
                if (currentFrameNo == marketFrameNo || this.currentMatchState.isMatchCompleted()) {
                    return new CheckMarketResultedOutcome();
                }
            case "P:CF":

                if (currentFrameNo == marketFrameNo - 1) {
                    if (this.currentMatchState.getPotFirstColor() != null)
                        if (this.currentMatchState.getPotFirstColor().equals("YELLOW")
                                        || this.currentMatchState.getPotFirstColor().equals("GREEN")
                                        || this.currentMatchState.getPotFirstColor().equals("BROWN"))
                            return new CheckMarketResultedOutcome("YELLOW/GREEN/BROWN");
                        else if (this.currentMatchState.getPotFirstColor().equals("BLUE")
                                        || this.currentMatchState.getPotFirstColor().equals("PINK"))
                            return new CheckMarketResultedOutcome("BLUE/PINK");
                        else if (this.currentMatchState.getPotFirstColor().equals("BLACK")) {
                            return new CheckMarketResultedOutcome("BLACK");
                        } else
                            return null;
                }
                if (currentFrameNo == marketFrameNo || this.currentMatchState.isMatchCompleted()) {
                    return new CheckMarketResultedOutcome();
                }
                return null;

            case "P:PF":
                if (currentFrameNo == marketFrameNo - 1) {
                    if (this.currentMatchState.getPotFirstBall() != TeamId.UNKNOWN)
                        if (this.currentMatchState.getPotFirstBall() == TeamId.A)
                            return new CheckMarketResultedOutcome("Player A");
                        else if (this.currentMatchState.getPotFirstBall() == TeamId.B)
                            return new CheckMarketResultedOutcome("Player B");
                        else
                            return null;
                }
                if (currentFrameNo == marketFrameNo || this.currentMatchState.isMatchCompleted()) {
                    return new CheckMarketResultedOutcome();
                }
                return null;
            case "P:X-Y":
                if (currentFrameNo == marketFrameNo + 1) {
                    int framesA = ((SnookerMatchState) currentMatchState).getFramesA()
                                    - ((SnookerMatchState) currentMatchState).getRaceScore()[currentFrameNo / 4 - 1].A;
                    int framesB = ((SnookerMatchState) currentMatchState).getFramesB()
                                    - ((SnookerMatchState) currentMatchState).getRaceScore()[currentFrameNo / 4 - 1].B;
                    winningSelection = String.format("%d-%d", framesA, framesB);
                    return new CheckMarketResultedOutcome(winningSelection);

                }
                if (this.currentMatchState.isMatchCompleted()) {
                    return new CheckMarketResultedOutcome();
                }
                return null;
            case "P:RACE":
                return checkRaceToFrameX();
            default:
                throw new IllegalArgumentException(
                                "Market type missing from SnookerMatchResultMarkets: " + market.getType());
        }

    }

    private CheckMarketResultedOutcome checkRaceToFrameX() {

        int framesA;
        framesA = this.currentMatchState.getFramesA();
        int framesB;
        framesB = this.currentMatchState.getFramesB();

        String winningSelection;
        if (framesA == marketFrameNo && framesB < marketFrameNo) {
            winningSelection = "A";
            return new CheckMarketResultedOutcome(winningSelection);
        }
        if (framesB == marketFrameNo && framesA < marketFrameNo) {
            winningSelection = "B";
            return new CheckMarketResultedOutcome(winningSelection);
        }
        if (this.currentMatchState.isMatchCompleted()) {

            framesA = this.currentMatchState.getFramesA();
            framesB = this.currentMatchState.getFramesB();
            if (framesA == marketFrameNo && framesB < marketFrameNo) {
                winningSelection = "A";
                return new CheckMarketResultedOutcome(winningSelection);
            }
            if (framesB == marketFrameNo && framesA < marketFrameNo) {
                winningSelection = "B";
                return new CheckMarketResultedOutcome(winningSelection);
            }
            return new CheckMarketResultedOutcome();
        }

        return null;
    }

    private CheckMarketResultedOutcome checkSetMktResulted() {
        int currentFrameNo = this.currentMatchState.getFramesA() + this.currentMatchState.getFramesB();
        if (currentFrameNo > marketFrameNo - 1) {
            int pointsA = this.currentMatchState.getFramesA();
            int pointsB = this.currentMatchState.getFramesB();
            int pointsA1 = this.previousMatchState.getFramesA();
            int pointsB1 = this.previousMatchState.getFramesB();

            String winningSelection;
            if (pointsA > pointsA1) {
                winningSelection = "A";
                return new CheckMarketResultedOutcome(winningSelection);
            }
            if (pointsB > pointsB1) {
                winningSelection = "B";
                return new CheckMarketResultedOutcome(winningSelection);
            }
        }
        if (this.currentMatchState.isMatchCompleted()) {
            return new CheckMarketResultedOutcome();
        }
        return null;
    }

    private CheckMarketResultedOutcome checkFrameOvunMktResulted(String lineId) {
        int currentFrameNo = this.currentMatchState.getFramesA() + this.currentMatchState.getFramesB();
        if (currentFrameNo > marketFrameNo - 1) {
            int frameScoreA = currentMatchState.getFrameScoreInMatchN(currentFrameNo - 1).A;
            int frameScoreB = currentMatchState.getFrameScoreInMatchN(currentFrameNo - 1).B;
            int totalScore = frameScoreA + frameScoreB;
            String winningSelection;
            if (totalScore > Double.valueOf(lineId)) {
                winningSelection = "Over";
                return new CheckMarketResultedOutcome(winningSelection);
            }
            if (totalScore < Double.valueOf(lineId)) {
                winningSelection = "Under";
                return new CheckMarketResultedOutcome(winningSelection);
            }
        }
        if (this.currentMatchState.isMatchCompleted()) {
            return new CheckMarketResultedOutcome();
        }
        return null;
    }

    private CheckMarketResultedOutcome checkFrameOEMktResulted() {
        int currentFrameNo = this.currentMatchState.getFramesA() + this.currentMatchState.getFramesB();
        if (currentFrameNo > marketFrameNo - 1) {
            int frameScoreA = currentMatchState.getFrameScoreInMatchN(currentFrameNo - 1).A;
            int frameScoreB = currentMatchState.getFrameScoreInMatchN(currentFrameNo - 1).B;
            int totalScore = frameScoreA + frameScoreB;
            String winningSelection;
            if (isOdd(totalScore)) {
                winningSelection = "Odd";
                return new CheckMarketResultedOutcome(winningSelection);
            } else
                return new CheckMarketResultedOutcome("Even");
        }
        if (this.currentMatchState.isMatchCompleted()) {
            return new CheckMarketResultedOutcome();
        }
        return null;
    }

    private CheckMarketResultedOutcome checkFrameBreakMktResulted(TeamId teamId, int point) {
        int currentFrameNo = this.currentMatchState.getFramesA() + this.currentMatchState.getFramesB();
        if (currentFrameNo > marketFrameNo - 1) {
            PairOfIntegers[] frameBreakScore = currentMatchState.getFrameBreakScore();
            int breakScore;
            if (teamId == TeamId.A)
                breakScore = frameBreakScore[currentFrameNo - 1].A;
            else
                breakScore = frameBreakScore[currentFrameNo - 1].B;

            if (breakScore > point)
                return new CheckMarketResultedOutcome("Yes");
            else
                return new CheckMarketResultedOutcome("No");
        }
        if (this.currentMatchState.isMatchCompleted())
            return new CheckMarketResultedOutcome();
        return null;

    }

    private CheckMarketResultedOutcome checkFrameWMMktResulted() {
        int currentFrameNo = this.currentMatchState.getFramesA() + this.currentMatchState.getFramesB();
        if (currentFrameNo > marketFrameNo - 1) {
            int frameScoreA = currentMatchState.getFrameScoreInMatchN(currentFrameNo - 1).A;
            int frameScoreB = currentMatchState.getFrameScoreInMatchN(currentFrameNo - 1).B;
            int totalScore = Math.abs(frameScoreA - frameScoreB);
            String winningSelection;
            if (totalScore < 50) {
                winningSelection = "0-49";
                return new CheckMarketResultedOutcome(winningSelection);
            } else if (totalScore < 100) {
                winningSelection = "50-99";
                return new CheckMarketResultedOutcome(winningSelection);
            } else if (totalScore < 120) {
                winningSelection = "100-199";
                return new CheckMarketResultedOutcome(winningSelection);
            } else if (totalScore < 147) {
                winningSelection = "120-146";
                return new CheckMarketResultedOutcome(winningSelection);
            } else {
                return new CheckMarketResultedOutcome("147+");
            }
        }
        if (this.currentMatchState.isMatchCompleted()) {
            return new CheckMarketResultedOutcome();
        }
        return null;
    }

    private CheckMarketResultedOutcome checkFrame147MktResulted() {
        int currentFrameNo = this.currentMatchState.getFramesA() + this.currentMatchState.getFramesB();
        if (currentFrameNo > marketFrameNo - 1) {
            int frameScoreA = currentMatchState.getFrameScoreInMatchN(currentFrameNo - 1).A;
            int frameScoreB = currentMatchState.getFrameScoreInMatchN(currentFrameNo - 1).B;
            String winningSelection;
            if (frameScoreA == 147 || frameScoreB == 147) {
                winningSelection = "Yes";
                return new CheckMarketResultedOutcome(winningSelection);
            } else {
                return new CheckMarketResultedOutcome("No");
            }
        }
        if (this.currentMatchState.isMatchCompleted()) {
            return new CheckMarketResultedOutcome();
        }
        return null;
    }

    void parseSequenceId(String sequenceId) {
        marketFrameNo = 0;
        marketPointNo = 0;
        String[] bits = sequenceId.split("\\.");
        if (bits[0].length() > 1 && bits[0] != "M") {
            String setNo = bits[0].substring(1, bits[0].length());
            marketFrameNo = Integer.parseInt(setNo);
        }
        if (bits.length >= 2)
            marketPointNo = Integer.parseInt(bits[1]);
    }

    private boolean isOdd(int i) {
        return i % 2 == 1;
    }

}
