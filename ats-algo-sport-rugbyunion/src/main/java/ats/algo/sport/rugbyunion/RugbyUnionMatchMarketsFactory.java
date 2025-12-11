package ats.algo.sport.rugbyunion;

import java.util.Map;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.common.TeamId;
import ats.algo.core.markets.Markets;
import ats.algo.montecarloframework.MarketsFactory;

public class RugbyUnionMatchMarketsFactory extends MarketsFactory {

    /**
     * holds any facts about a simulation of a match that has just been played (via the Match class) that are need in
     * the calculation of the required statistics and that are not captured by the matchState object for RugbyUnion if
     * we need to know who won leg N+2, where N is the current leg, then we need to collect that "fact" in this class
     * 
     * @author Jin
     * 
     */

    private TwoWayStatistic moneyLine;
    private ThreeWayStatistic matchResult;
    @SuppressWarnings("unused")
    private ThreeWayStatistic fiveMinsWinner;
    private ThreeWayStatistic matchDoubleChance;
    @SuppressWarnings("unused")
    private CorrectScoreStatistic fulltimeCorrectScore;
    private CorrectScoreStatistic ftotCorrectPoints;
    private TotalStatistic pointsTotal;
    private TotalStatistic pointsTotalA;
    private TotalStatistic pointsTotalB;
    private TwoWayStatistic periodBothTeamToScore;
    @SuppressWarnings("unused")
    private TwoWayStatistic fiveMinsIfGoal;
    private EuroHandicapStatistic matchWinnerEuroHandicap;

    private HandicapStatistic pointsHandicap;
    private ThreeWayStatistic nextGoal;
    private ThreeWayStatistic nextTry;
    private ThreeWayStatistic overTimeNextGoal;
    private TotalStatistic trysTotalCurrentPeriod;
    private ThreeWayStatistic periodWinner;
    private CorrectScoreStatistic periodCorrectTrys;
    private TwoWayStatistic overTimePossible;
    private static final int maxNoGoalsPerTeam = 280;
    private boolean allowGenerateAllSelection = false;

    RugbyUnionMatchMarketsFactory(RugbyUnionMatchState matchState) {
        this.setAllowGenerateAllSelection(allowGenerateAllSelection);
        String periodSequenceId = matchState.getSequenceIdForPeriod(0);
        String matchSequenceId = matchState.getSequenceIdForMatch();
        String fiveMinsSequenceId = matchState.getSequenceIdForFiveMinsResult();
        String goalSequenceId = matchState.getSequenceIdForGoal(0);
        String trySequenceId = matchState.getSequenceIdForTry(0);

        int periodNo = matchState.getPeriodNo();
        Boolean generateMarketFT = false;
        @SuppressWarnings("unused")
        Boolean generateMarketFTOT = false;
        @SuppressWarnings("unused")
        Boolean generateMarketOT = false;
        Boolean inMatchNormalTime = true;
        @SuppressWarnings("unused")
        boolean overTimePenaltyPossible = false;
        // Boolean generateMarket = false;
        switch (periodNo) {
            case 0: // pre-match
            case 1: // first period
                generateMarketFT = true;
                generateMarketFTOT = true;
                inMatchNormalTime = true;
                break;
            case 2: // second period
                generateMarketFT = true;
                generateMarketFTOT = true;
                inMatchNormalTime = true;
                break;
            case 3: // third period
                throw new IllegalArgumentException("period 3 does not exist");
            case 4: // extra time
                generateMarketFT = false;
                generateMarketFTOT = true;
                generateMarketOT = true;
                break;
            default:
                generateMarketFT = false;
                generateMarketFTOT = false;
                inMatchNormalTime = false;
                generateMarketOT = false;
        }

        if (matchState.isPenaltiesPossible() || (matchState.getExtraPeriodSecs() > 0))
            overTimePenaltyPossible = true;
        matchResult = new ThreeWayStatistic("FT:AXB", "Match Result", MarketGroup.GOALS, generateMarketFT,
                        matchSequenceId, "A", "B", "Draw");

        moneyLine = new TwoWayStatistic("FT:ML", "Match Winner", MarketGroup.GOALS, false, matchSequenceId, "A", "B");

        overTimePossible = new TwoWayStatistic("FT:OT", "Overtime(Yes/No)", generateMarketFT, matchSequenceId, "Yes",
                        "No");

        fiveMinsWinner = new ThreeWayStatistic("FT:5MR", "5 Min Result", false, fiveMinsSequenceId, "A", "B", "Draw");
        fiveMinsIfGoal = new TwoWayStatistic("FT:5MG", "5 Min Goal - Yes/No", false, periodSequenceId, "Yes", "No");

        matchDoubleChance = new ThreeWayStatistic("FT:DBLC", "Double Chance", generateMarketFT, matchSequenceId, "AX",
                        "BX", "AB");
        // fulltimeCorrectScore = new CorrectScoreStatistic("FT:CS", "Correct Score", generateMarketFT, matchSequenceId,
        // maxNoGoalsPerTeam);

        ftotCorrectPoints = new CorrectScoreStatistic("FTOT:CS", "Correct Score", MarketGroup.GOALS, generateMarketFT,
                        matchSequenceId, maxNoGoalsPerTeam);
        String[] timeOfFirstGoalSelections = {"No Goal", "1-10", "11-20", "21-30", "31-40", "41-50", "51-60"};

        new NWayStatistic("FT:TOFG", "Time of 1st Goal", false, matchSequenceId, timeOfFirstGoalSelections);

        pointsTotal = new TotalStatistic("FT:OU", "Total Points", MarketGroup.GOALS,
                        generateMarketFT && inMatchNormalTime, "M", 2 * maxNoGoalsPerTeam);
        pointsTotalA = new TotalStatistic("FT:A:OU", "Home Team Total Points", MarketGroup.GOALS, generateMarketFT,
                        matchSequenceId, maxNoGoalsPerTeam);
        pointsTotalB = new TotalStatistic("FT:B:OU", "Away Team Total Points", MarketGroup.GOALS, generateMarketFT,
                        matchSequenceId, maxNoGoalsPerTeam);

        pointsHandicap = new HandicapStatistic("FT:SPRD", "Points Handicap", MarketGroup.GOALS, generateMarketFT, "M",
                        maxNoGoalsPerTeam);
        matchWinnerEuroHandicap =
                        new EuroHandicapStatistic("FT:EH", "Euro Handicap", true, matchSequenceId, maxNoGoalsPerTeam);

        String marketDescription = String.format("Next Goal ");
        nextGoal = new ThreeWayStatistic("FT:NG", marketDescription, generateMarketFT, goalSequenceId, "A", "B",
                        "No goal");
        marketDescription = String.format("Next Try");
        nextTry = new ThreeWayStatistic("FT:NT", marketDescription, generateMarketFT, trySequenceId, "A", "B",
                        "No Try");
        periodSequenceId = matchState.getSequenceIdForPeriod(0);
        periodNo = matchState.getPeriodNo();
        switch (periodNo) {
            case 0: // pre-match
            case 1:
                marketDescription = "1st Half Total Tries";
                break;
            // case 2: //half time
            case 2: // second half
                marketDescription = "2nd Half Total Tries";
                break;

            case 3: // second half
                throw new IllegalArgumentException("period 3 does not exist");
            default:
                marketDescription = "";
        }
        trysTotalCurrentPeriod = new TotalStatistic("P:TRYOU", marketDescription, MarketGroup.GOALS, generateMarketFT,
                        periodSequenceId, maxNoGoalsPerTeam);

        switch (periodNo) {
            case 0: // pre-match
            case 1:
                marketDescription = "1st Half Correct Tries";
                break;
            // case 2: //half time
            case 2: // second half
                marketDescription = "2nd Half Correct Tries";
                break;

            case 3: // second half
                throw new IllegalArgumentException("period 3 does not exist");
            default:
                marketDescription = "";
        }
        periodCorrectTrys = new CorrectScoreStatistic("P:TRYCS", marketDescription, MarketGroup.GOALS, generateMarketFT,
                        periodSequenceId, maxNoGoalsPerTeam);

        switch (periodNo) {
            case 0: // pre-match
            case 1:
                marketDescription = "1st Half Both Team To Score";
                break;
            // case 2: //half time
            case 2: // second half
                marketDescription = "2nd Half Both Team To Score";
                break;

            case 3: // second half
                throw new IllegalArgumentException("period 3 does not exist");
            default:
                marketDescription = "";
        }
        generateMarketFT = (matchState.getMatchPeriod() != RugbyUnionMatchPeriod.AT_HALF_TIME
                        && matchState.getMatchPeriod() != RugbyUnionMatchPeriod.AT_FULL_TIME
                        && matchState.getMatchPeriod() != RugbyUnionMatchPeriod.IN_EXTRA_TIME_FIRST_HALF
                        && matchState.getMatchPeriod() != RugbyUnionMatchPeriod.IN_EXTRA_TIME_SECOND_HALF
                        && matchState.getMatchPeriod() != RugbyUnionMatchPeriod.AT_EXTRA_TIME_HALF_TIME
                        && matchState.getMatchPeriod() != RugbyUnionMatchPeriod.AT_EXTRA_TIME_END
                        && matchState.getMatchPeriod() != RugbyUnionMatchPeriod.IN_SHOOTOUT
                        && (matchState.getCurrentPeriodPointsA() == 0 || matchState.getCurrentPeriodPointsB() == 0));
        periodBothTeamToScore = new TwoWayStatistic("P:BTTS", marketDescription, generateMarketFT, periodSequenceId,
                        "Yes", "No");

        switch (periodNo) {
            case 0: // pre-match
            case 1: // first half
                marketDescription = "1st Half Winner";
                break;
            // case 2: //half time
            case 2: // second half
                marketDescription = "2nd Half Winner";
                break;

            case 3: // second half
                throw new IllegalArgumentException("period 3 does not exist");

            default:
                marketDescription = "";
        }

        periodWinner = new ThreeWayStatistic("P:AXB", marketDescription, generateMarketFT, periodSequenceId, "A", "B",
                        "Draw");

        marketDescription = String.format("Next Goal");
        overTimeNextGoal = new ThreeWayStatistic("OT:NG", marketDescription, true, goalSequenceId, "A", "B", "No goal");
        /*
         * Extra time markets
         */

    }

    void updateStats(RugbyUnionMatchState matchState, RugbyUnionMatchFacts matchFacts) {

        int pointsA = matchState.getPointsA();
        int pointsB = matchState.getPointsB();

        if (matchState.isPenaltiesPossible() || (matchState.getExtraPeriodSecs() > 0)) {
            // System.out.println("increment at "+goalsA+" "+goalsB);
            moneyLine.increment(pointsA > pointsB);
        }

        int normalTimeGoalsA = matchState.getNormalTimePointsA();
        int normalTimeGoalsB = matchState.getNormalTimePointsB();
        TeamId teamId = TeamId.UNKNOWN;
        if (normalTimeGoalsA > normalTimeGoalsB)
            teamId = TeamId.A;
        if (normalTimeGoalsA < normalTimeGoalsB)
            teamId = TeamId.B;

        if (matchState.isNormalTimeMatchCompleted())
            matchResult.increment(teamId);

        if (normalTimeGoalsA == normalTimeGoalsB) {
            overTimePossible.increment(true);
        } else {
            overTimePossible.increment(false);
        }

        if (normalTimeGoalsA >= normalTimeGoalsB) {
            teamId = TeamId.A;
            matchDoubleChance.increment(teamId);
        }
        if (normalTimeGoalsA <= normalTimeGoalsB) {
            teamId = TeamId.B;
            matchDoubleChance.increment(teamId);
        }
        if (normalTimeGoalsA < normalTimeGoalsB || normalTimeGoalsA > normalTimeGoalsB) {
            teamId = TeamId.UNKNOWN;
            matchDoubleChance.increment(teamId);
        }
        // fulltimeCorrectScore.increment(normalTimeGoalsA, normalTimeGoalsB);
        ftotCorrectPoints.increment(pointsA, pointsB);
        pointsTotal.increment(pointsA + pointsB);
        pointsTotalA.increment(pointsA);
        pointsTotalB.increment(pointsB);
        pointsHandicap.increment(pointsA - pointsB);
        matchWinnerEuroHandicap.increment(pointsA - pointsB);
        nextGoal.increment(matchFacts.getNextPoint());
        nextTry.increment(matchFacts.getNextToScoreTry());
        overTimeNextGoal.increment(matchFacts.getNextPoint());
        trysTotalCurrentPeriod.increment(matchFacts.getTrysTotalCurrentPeriod());
        int periodTrysA = matchFacts.getCurrentPeriodTrysA();
        int periodTrysB = matchFacts.getCurrentPeriodTrysB();
        periodCorrectTrys.increment(periodTrysA, periodTrysB);
        teamId = TeamId.UNKNOWN;
        if (periodTrysA > periodTrysB)
            teamId = TeamId.A;
        if (periodTrysA < periodTrysB)
            teamId = TeamId.B;
        periodWinner.increment(teamId);
        periodBothTeamToScore.increment(matchFacts.getBothTeamToScoreCurrentPeriod());
    }

    @Override
    public void addDerivedMarkets(Markets markets, AlgoMatchState matchState, MatchParams matchParams) {
        // if (matchState.getGamePeriod().equals(GamePeriod.PREMATCH)
        // || matchState.getGamePeriod().equals(GamePeriod.FIRST_HALF)
        // || matchState.getGamePeriod().equals(GamePeriod.HALF_TIME)
        // || matchState.getGamePeriod().equals(GamePeriod.SECOND_HALF)) {

        // Market sourceMarket = markets.get("FT:CS");
        // Map<String, Double> csGrid = sourceMarket.copy();
        // double[][] scoreGrid = new double[maxNoGoalsPerTeam][maxNoGoalsPerTeam];
        // scoreGrid = creatScoreGrid(csGrid);
        //
        // double[] probs = { 0, 0, 0 };
        // for (int i = 0; i < scoreGrid.l`ength; i++) {
        // for (int j = 0; j < scoreGrid.length; j++) {
        // if (i == j)
        // probs[2] += scoreGrid[i][j];
        // if (i < j)
        // probs[0] += scoreGrid[i][j];
        // if (i > j)
        // probs[1] += scoreGrid[i][j];
        // }
        // }
        // Market market = new Market(MarketCategory.GENERAL,"FT:AXB",matchSequenceId, "Match Result");
        // market.setCategory(MarketCategory.GENERAL);
        // market.setIsValid(true);
        // market.setSequenceId(matchSequenceId);
        // market.setIsValid(true);
        // market.put("A", probs[0]);
        // market.put("B", probs[1]);
        // market.put("D", probs[2]);
        // markets.addMarket( market);
        //
        // ///// HANDICAP MARKETS
        // // goalsHandicap = new HandicapStatistic("FT:SPRD", "Handicap", generateMarketFT, matchSequenceId,
        // maxNoGoalsPerTeam);
        // Market ftHandicapMarket = new Market(MarketCategory.CS_DERIVED_HCAP, "FT:SPRD",matchSequenceId, "Handicap");
        // ftHandicapMarket.setCategory(MarketCategory.CS_DERIVED_HCAP);
        //
        // Map<Double, double[]> handicapMap = new TreeMap<Double, double[]>();
        //
        // double[] probHC = { 0, 0 };
        // double[] finalHCprob = { 0, 0 };
        // double lastDiff = 1;
        // double finalHandicapLine = 0;
        // for (int l = -scoreGrid.length; l < scoreGrid.length; l++) {
        // double tempLine = l + 0.5;
        // probHC[0] = 0;
        // probHC[1] = 0;
        // for (int i = 0; i < scoreGrid.length; i++) {
        // for (int j = 0; j < scoreGrid.length; j++) {
        // if (i < (j + tempLine)) {
        // probHC[0] += scoreGrid[i][j];
        // } else {
        // probHC[1] += scoreGrid[i][j];
        // }
        // }
        // }
        // if (Math.abs((probHC[0] - probHC[1])) < lastDiff) {
        // finalHandicapLine = tempLine;
        // lastDiff = Math.abs(probHC[0] - probHC[1]);
        // finalHCprob = probHC.clone();
        // }
        // double[] tempProbHc = new double[2];
        // tempProbHc = probHC.clone();
        // handicapMap.put(tempLine, tempProbHc);
        // }
        //
        // ftHandicapMarket.setLineId(String.valueOf(finalHandicapLine));
        // ftHandicapMarket.setIsValid(true);
        // ftHandicapMarket.setSequenceId(matchSequenceId);
        // ftHandicapMarket.setIsValid(true);
        // ftHandicapMarket.setSelectionNameOverOrA("A" + " (" + String.valueOf(finalHandicapLine) + ")");
        // ftHandicapMarket.setSelectionNameUnderOrB("B" + " (" + String.valueOf(-finalHandicapLine) + ")");
        // ftHandicapMarket.put("A", finalHCprob[0]);
        // ftHandicapMarket.put("B", finalHCprob[1]);
        //
        //
        //
        // double[] lineProbs = new double[handicapMap.size()];
        // int ii = 0;
        // for (Map.Entry<Double, double[]> entry : handicapMap.entrySet()) {
        // lineProbs[ii] = entry.getValue()[0];
        // ii++;
        // }
        //
        // ftHandicapMarket.setLineProbs(lineProbs);
        //
        // markets.addMarket( ftHandicapMarket);
        //
        // /// Total Goals ///////////
        // Market ftOVUNMarket = new Market(MarketCategory.CS_DERIVED_OVUN, "FT:OU", matchSequenceId,"Total Goals");
        // ftOVUNMarket.setCategory(MarketCategory.CS_DERIVED_OVUN);
        // ftOVUNMarket.setIsValid(true);
        // ftOVUNMarket.setSequenceId(matchSequenceId);
        // market.setIsValid(true);
        //
        // double[] probOVUN = new double[scoreGrid.length * 2];
        //
        // for (int i = 0; i < scoreGrid.length; i++) {
        // for (int j = 0; j < scoreGrid.length; j++) {
        // probOVUN[i + j] += scoreGrid[i][j];
        // }
        // }
        //
        // for (int i = 0; i < probOVUN.length; i++) {
        // if (i > 0)
        // probOVUN[i] += probOVUN[i - 1];
        // }
        //
        // double tempDiff = 1;
        //
        // int line = 0;
        // for (int i = 0; i < probOVUN.length; i++) {
        // double diff = Math.abs(probOVUN[i] - 0.5);
        // if (diff < tempDiff) {
        // tempDiff = diff;
        // line = i;
        // }
        // }
        //
        // double[] ouLineProbs = { 0, 0, 0 };
        // ouLineProbs[0] = probOVUN[line - 1];
        // ouLineProbs[1] = probOVUN[line];
        // ouLineProbs[2] = probOVUN[line + 1];
        // double lineMark = 0.5;
        // ftOVUNMarket.setSelectionNameOverOrA("Over" + " (" + String.valueOf(line + lineMark) + ")");
        // ftOVUNMarket.setSelectionNameUnderOrB("Under" + " (" + String.valueOf(line + lineMark) + ")");
        // ftOVUNMarket.setLineId(String.valueOf(line + 0.5));
        // ftOVUNMarket.setLineProbs(probOVUN);
        // ftOVUNMarket.put("Over", 1 - ouLineProbs[1]);
        // ftOVUNMarket.put("Under", ouLineProbs[1]);
        // markets.addMarket( ftOVUNMarket);
        // }
    }

    /**
     * 
     * @param csGrid as generated for the "FT:CS" market
     * @return SG return normalised correct score grid
     */
    @SuppressWarnings("unused")
    private double[][] creatScoreGrid(Map<String, Double> csGrid) {
        double[][] SG = new double[maxNoGoalsPerTeam][maxNoGoalsPerTeam];
        double normTemp = 0;
        // normalising
        for (Map.Entry<String, Double> entry : csGrid.entrySet())
            normTemp += entry.getValue();

        for (Map.Entry<String, Double> entry : csGrid.entrySet()) {
            String scores = entry.getKey();
            String[] temp_scores = scores.split("-");
            int scoreA = Integer.parseInt(temp_scores[0]);
            int scoreB = Integer.parseInt(temp_scores[1]);
            SG[scoreB][scoreA] = entry.getValue() / normTemp;
        }
        return SG;
    }

}
