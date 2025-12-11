package ats.algo.sport.futsal;

import java.util.Map;

import ats.algo.core.GamePeriod;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.common.TeamId;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketCategory;
import ats.algo.core.markets.Markets;
import ats.algo.montecarloframework.MarketsFactory;

public class FutsalMatchMarketsFactory extends MarketsFactory {

    /**
     * holds any facts about a simulation of a match that has just been played (via the Match class) that are need in
     * the calculation of the required statistics and that are not captured by the matchState object for Futsal if we
     * need to know who won leg N+2, where N is the current leg, then we need to collect that "fact" in this class
     * 
     * @author Jin
     * 
     */
    String[] timeOfFirstGoalSelections = {"No Goal", "1-10", "11-20", "21-30", "31-40", "41-50", "51-60"};
    private HandicapStatistic simGoalsHandicap;
    private TwoWayStatistic matchResult;
    private ThreeWayStatistic fiveMinsWinner;
    private ThreeWayStatistic matchDoubleChance;
    private CorrectScoreStatistic fulltimeCorrectScore;
    private CorrectScoreStatistic ftotCorrectScore;
    // private TotalStatistic goalsTotal;
    private TotalStatistic goalsTotalA;
    private TotalStatistic goalsTotalB;
    private TotalStatistic goalsTotal;
    private TwoWayStatistic periodBothTeamToScore;
    private TwoWayStatistic fiveMinsIfGoal;
    private EuroHandicapStatistic matchWinnerEuroHandicap;

    // private HandicapStatistic goalsHandicap;
    private HandicapStatistic periodHandicap;
    private ThreeWayStatistic nextGoal;
    private ThreeWayStatistic overTimeNextGoal;
    private TotalStatistic goalsTotalCurrentPeriod;
    private ThreeWayStatistic periodWinner;
    private CorrectScoreStatistic periodCorrectGoalScore;
    private TwoWayStatistic overTimePossible;
    private NWayStatistic timeFirstGoal;

    private static final int maxNoGoalsPerTeam = 40;
    private boolean allowGenerateAllSelection = false;

    FutsalMatchMarketsFactory(FutsalMatchState matchState) {
        this.setAllowGenerateAllSelection(allowGenerateAllSelection);
        String periodSequenceId = matchState.getSequenceIdForPeriod(0);
        String matchSequenceId = matchState.getSequenceIdForMatch();
        String goalSequenceId = matchState.getSequenceIdForGoal(0);

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

        matchResult = new TwoWayStatistic("FT:ML", "Match Winner", false, matchSequenceId, "A", "B");

        overTimePossible = new TwoWayStatistic("FT:OT", "Overtime(Yes/No)", false, matchSequenceId, "Yes", "No");

        String fiveMinsSequenceId = matchState.getFiveMinsSequenceId();
        // if (!matchState.isNormalTimeMatchCompleted())
        // generateMarketFT = true;
        fiveMinsWinner = new ThreeWayStatistic("FT:5MR", "5 Min Result", false, fiveMinsSequenceId, "A", "B", "Draw");
        fiveMinsIfGoal = new TwoWayStatistic("FT:5MG", "5 Min Goal - Yes/No", false, periodSequenceId, "Yes", "No");

        matchDoubleChance = new ThreeWayStatistic("FT:DBLC", "Double Chance", false, matchSequenceId, "AX", "BX", "AB");

        ftotCorrectScore = new CorrectScoreStatistic("FTOT:CS", "Full time over time correct Score", false,
                        matchSequenceId, maxNoGoalsPerTeam);

        boolean tfgCreation = matchState.getElapsedTimeSecs() > 35 || matchState.getGoalNo() > 1;

        timeFirstGoal = new NWayStatistic("FT:TOFG", "Time of 1st Goal", tfgCreation, matchSequenceId,
                        timeOfFirstGoalSelections);

        goalsTotal = new TotalStatistic("FT:OU", "Total Goals", generateMarketFT && inMatchNormalTime, matchSequenceId,
                        2 * maxNoGoalsPerTeam);
        goalsTotalA = new TotalStatistic("FT:A:OU", "Home Team Total Goals", generateMarketFT, matchSequenceId,
                        maxNoGoalsPerTeam);
        goalsTotalB = new TotalStatistic("FT:B:OU", "Away Team Total Goals", generateMarketFT, matchSequenceId,
                        maxNoGoalsPerTeam);
        // goalsHandicap = new HandicapStatistic("FT:SPRD", "Handicap",
        // generateMarketFT, matchSequenceId,
        // maxNoGoalsPerTeam);

        matchWinnerEuroHandicap =
                        new EuroHandicapStatistic("FT:EH", "Euro Handicap", false, matchSequenceId, maxNoGoalsPerTeam);

        String marketDescription = String.format("Next Goal ");
        nextGoal = new ThreeWayStatistic("FT:NG", marketDescription, false, goalSequenceId, "A", "B", "No goal");
        periodSequenceId = matchState.getSequenceIdForPeriod(0);
        periodNo = matchState.getPeriodNo();
        String handicapMarketDescription = "";
        switch (periodNo) {
            case 0: // pre-match
            case 1:
                marketDescription = "1st Half total goals";
                handicapMarketDescription = "1st Half Handicap";
                break;
            // case 2: //half time
            case 2: // second half
                marketDescription = "2nd period total goals";
                handicapMarketDescription = "2nd Half Handicap";
                break;

            case 3: // second half
                throw new IllegalArgumentException("period 3 does not exist");
            default:
                marketDescription = "";
        }
        goalsTotalCurrentPeriod =
                        new TotalStatistic("P:OU", marketDescription, false, periodSequenceId, maxNoGoalsPerTeam);

        periodHandicap = new HandicapStatistic("P:SPRD", handicapMarketDescription, generateMarketFT, periodSequenceId,
                        maxNoGoalsPerTeam);

        switch (periodNo) {
            case 0: // pre-match
            case 1:
                marketDescription = "1st Half Correct Score";
                break;
            // case 2: //half time
            case 2: // second half
                marketDescription = "2nd Half Correct Score";
                break;

            case 3: // second half
                throw new IllegalArgumentException("period 3 does not exist");
            default:
                marketDescription = "";
        }
        periodCorrectGoalScore = new CorrectScoreStatistic("P:CS", marketDescription, false, periodSequenceId,
                        maxNoGoalsPerTeam);

        switch (periodNo) {
            case 0: // pre-match
            case 1:
                marketDescription = "1st Period Both Team To Score";
                break;
            // case 2: //half time
            case 2: // second half
                marketDescription = "2nd Period Both Team To Score";
                break;

            case 3: // second half
                throw new IllegalArgumentException("period 3 does not exist");
            default:
                marketDescription = "";
        }
        generateMarketFT = (matchState.getMatchPeriod() != FutsalMatchPeriod.AT_HALF_TIME
                        && matchState.getMatchPeriod() != FutsalMatchPeriod.AT_FULL_TIME
                        && matchState.getMatchPeriod() != FutsalMatchPeriod.IN_EXTRA_TIME_FIRST_HALF
                        && matchState.getMatchPeriod() != FutsalMatchPeriod.IN_EXTRA_TIME_SECOND_HALF
                        && matchState.getMatchPeriod() != FutsalMatchPeriod.AT_EXTRA_TIME_HALF_TIME
                        && matchState.getMatchPeriod() != FutsalMatchPeriod.AT_EXTRA_TIME_END
                        && matchState.getMatchPeriod() != FutsalMatchPeriod.IN_SHOOTOUT
                        && (matchState.getCurrentPeriodGoalsA() == 0 || matchState.getCurrentPeriodGoalsB() == 0));
        periodBothTeamToScore = new TwoWayStatistic("P:BTTS", marketDescription, false, periodSequenceId, "Yes", "No");

        switch (periodNo) {
            case 0: // pre-match
            case 1: // first half
                marketDescription = "1st Half result";
                break;
            // case 2: //half time
            case 2: // second half
                marketDescription = "2nd Half result";
                break;

            case 3: // second half
                throw new IllegalArgumentException("period 3 does not exist");

            default:
                marketDescription = "";
        }

        periodWinner = new ThreeWayStatistic("P:AXB", marketDescription, generateMarketFT, periodSequenceId, "A", "B",
                        "Draw");

        marketDescription = String.format("OT - Next Goal");
        overTimeNextGoal =
                        new ThreeWayStatistic("OT:NG", marketDescription, false, goalSequenceId, "A", "B", "No goal");

        fulltimeCorrectScore = new CorrectScoreStatistic("FT:CS", "Correct Score (full time only)", true,
                        matchSequenceId, maxNoGoalsPerTeam);
        /*
         * Extra time markets
         */
        simGoalsHandicap = new HandicapStatistic("FT:SPRD", "Goal Spread (full time only)",
                        generateMarketFT && inMatchNormalTime, matchSequenceId, maxNoGoalsPerTeam);
    }

    void updateStats(FutsalMatchState matchState, FutsalMatchFacts matchFacts) {

        int goalsA = matchState.getGoalsA();
        int goalsB = matchState.getGoalsB();
        periodHandicap.increment(matchFacts.getCurrentPeriodGoalsA() - matchFacts.getCurrentPeriodGoalsB());
        if (matchState.isPenaltiesPossible() || (matchState.getExtraPeriodSecs() > 0)) {
            // System.out.println("increment at "+goalsA+" "+goalsB);
            matchResult.increment(goalsA > goalsB);
        }
        int normalTimeGoalsA = matchState.getNormalTimeGoalsA();
        int normalTimeGoalsB = matchState.getNormalTimeGoalsB();
        TeamId teamId = TeamId.UNKNOWN;
        if (normalTimeGoalsA > normalTimeGoalsB)
            teamId = TeamId.A;
        if (normalTimeGoalsA < normalTimeGoalsB)
            teamId = TeamId.B;

        int fiveMinsGoalsA = matchFacts.getFiveMinsGoalsA();
        int fiveMinsGoalsB = matchFacts.getFiveMinsGoalsB();
        teamId = TeamId.UNKNOWN;
        if (fiveMinsGoalsA > fiveMinsGoalsB)
            teamId = TeamId.A;
        if (fiveMinsGoalsA < fiveMinsGoalsB)
            teamId = TeamId.B;

        if (matchState.isNormalTimeMatchCompleted())
            fiveMinsWinner.increment(teamId);
        if (matchState.isNormalTimeMatchCompleted())
            fiveMinsIfGoal.increment((fiveMinsGoalsA + fiveMinsGoalsB) > 0);

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

        int timeFirstGoalIndex = matchState.getElapsedTimeSecsFirstGoal() / 600;
        timeFirstGoal.increment(timeFirstGoalIndex + 1);

        fulltimeCorrectScore.increment(normalTimeGoalsA, normalTimeGoalsB);
        ftotCorrectScore.increment(goalsA, goalsB);
        goalsTotal.increment(normalTimeGoalsA + normalTimeGoalsB);
        goalsTotalA.increment(goalsA);
        goalsTotalB.increment(goalsB);
        simGoalsHandicap.increment(normalTimeGoalsA - normalTimeGoalsB);
        matchWinnerEuroHandicap.increment(goalsA - goalsB);
        nextGoal.increment(matchFacts.getNextToScore());
        overTimeNextGoal.increment(matchFacts.getNextToScore());
        goalsTotalCurrentPeriod.increment(matchFacts.getGoalsTotalCurrentPeriod());
        int periodGoalsA = matchFacts.getCurrentPeriodGoalsA();
        int periodGoalsB = matchFacts.getCurrentPeriodGoalsB();
        periodCorrectGoalScore.increment(periodGoalsA, periodGoalsB);
        teamId = TeamId.UNKNOWN;
        if (periodGoalsA > periodGoalsB)
            teamId = TeamId.A;
        if (periodGoalsA < periodGoalsB)
            teamId = TeamId.B;
        periodWinner.increment(teamId);
        periodBothTeamToScore.increment(matchFacts.getBothTeamToScoreCurrentPeriod());
    }

    @Override
    public void addDerivedMarkets(Markets markets, AlgoMatchState matchState, MatchParams matchParams) {
        String matchSequenceId = ((FutsalMatchState) matchState).getSequenceIdForMatch();
        if (matchState.getGamePeriod().equals(GamePeriod.PREMATCH)
                        || matchState.getGamePeriod().equals(GamePeriod.FIRST_HALF)
                        || matchState.getGamePeriod().equals(GamePeriod.HALF_TIME)
                        || matchState.getGamePeriod().equals(GamePeriod.SECOND_HALF)) {

            Market sourceMarket = markets.get("FT:CS");

            Map<String, Double> csGrid = sourceMarket.getSelectionsProbs();
            double[][] scoreGrid = new double[30][30];
            scoreGrid = creatScoreGrid(csGrid);

            double[] probs = {0, 0, 0};
            for (int i = 0; i < scoreGrid.length; i++) {
                for (int j = 0; j < scoreGrid.length; j++) {
                    if (i == j)
                        probs[2] += scoreGrid[i][j];
                    if (i < j)
                        probs[0] += scoreGrid[i][j];
                    if (i > j)
                        probs[1] += scoreGrid[i][j];
                }
            }
            Market market = new Market(MarketCategory.GENERAL, "FT:AXB", matchSequenceId, "Match Result");
            // market.setCategory(MarketCategory.GENERAL);
            market.setIsValid(true);
            // market.setSequenceId(matchSequenceId);
            market.put("A", probs[0]);
            market.put("B", probs[1]);
            market.put("Draw", probs[2]);
            markets.addMarketWithShortKey(market);

            // ///// HANDICAP MARKETS
            // // goalsHandicap = new HandicapStatistic("FT:SPRD", "Handicap",
            // generateMarketFT, matchSequenceId,
            // maxNoGoalsPerTeam);
            // Market ftHandicapMarket = new Market(MarketCategory.HCAP,
            // "FT:SPRD", matchSequenceId, "Handicap");
            // ftHandicapMarket.setCategory(MarketCategory.HCAP);
            //
            // Map<Double, double[]> handicapMap = new TreeMap<Double,
            // double[]>();
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
            // if ((i + tempLine) < j) {
            // probHC[0] += scoreGrid[i][j]; // b win
            // } else {
            // probHC[1] += scoreGrid[i][j]; // a win
            // }
            // }
            // }
            //
            //
            //
            // if (Math.abs((probHC[0] - probHC[1])) < lastDiff) {
            // finalHandicapLine = tempLine;
            // lastDiff = Math.abs(probHC[0] - probHC[1]);
            // finalHCprob = probHC.clone();
            // }
            // double[] tempProbHc = new double[2];
            // tempProbHc = probHC.clone();
            // handicapMap.put(tempLine, tempProbHc);
            // }
            //// for (Map.Entry<Double, double[]> entry :
            // handicapMap.entrySet()) {
            //// System.out.println(entry+" "+entry.getValue()[0]);
            //// }
            //
            // ftHandicapMarket.setLineId(String.valueOf(finalHandicapLine));
            // ftHandicapMarket.setIsValid(true);
            // ftHandicapMarket.setSequenceId(matchSequenceId);
            // ftHandicapMarket.setIsValid(true);
            // ftHandicapMarket.setSelectionNameOverOrA("A" + " (" +
            // String.valueOf(finalHandicapLine) + ")");
            // ftHandicapMarket.setSelectionNameUnderOrB("B" + " (" +
            // String.valueOf(-finalHandicapLine) + ")");
            // ftHandicapMarket.put("A", finalHCprob[1]);
            // ftHandicapMarket.put("B", finalHCprob[0]);
            //
            // double[] lineProbs = new double[handicapMap.size()];
            // int ii = handicapMap.size()-1;
            // for (Map.Entry<Double, double[]> entry : handicapMap.entrySet())
            // {
            // //System.out.println(entry);
            // lineProbs[ii] = entry.getValue()[0];
            // ii--;
            // }
            //
            // ftHandicapMarket.setLineProbs(lineProbs);
            //
            // markets.addMarket(ftHandicapMarket);
            //
            // /// Total Goals ///////////
            // Market ftOVUNMarket = new Market(MarketCategory.OVUN, "FT:OU",
            // matchSequenceId, "Total Goals");
            // ftOVUNMarket.setCategory(MarketCategory.OVUN);
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
            // ftOVUNMarket.setSelectionNameOverOrA("Over" + " (" +
            // String.valueOf(line + lineMark) + ")");
            // ftOVUNMarket.setSelectionNameUnderOrB("Under" + " (" +
            // String.valueOf(line + lineMark) + ")");
            // ftOVUNMarket.setLineId(String.valueOf(line + 0.5));
            // ftOVUNMarket.setLineProbs(probOVUN);
            // ftOVUNMarket.put("Over", 1 - ouLineProbs[1]);
            // ftOVUNMarket.put("Under", ouLineProbs[1]);
            // markets.addMarket(ftOVUNMarket);
        }
    }

    /**
     * 
     * @param csGrid as generated for the "FT:CS" market
     * @return SG return normalised correct score grid
     */
    private double[][] creatScoreGrid(Map<String, Double> csGrid) {
        double[][] SG = new double[30][30];
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
