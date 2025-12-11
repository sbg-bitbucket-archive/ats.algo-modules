package ats.algo.sport.tennis;

import ats.algo.algomanager.AlgoManagerSimpleTestBase;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.common.TeamId;
import ats.algo.core.comparetomarket.MarketPrice;
import ats.algo.core.comparetomarket.MarketPrices;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.core.markets.MarketCategory;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchParams;
import ats.algo.sport.tennis.TennisMatchFormat.FinalSetType;
import ats.algo.sport.tennis.TennisMatchFormat.Sex;
import ats.algo.sport.tennis.TennisMatchFormat.Surface;
import ats.algo.sport.tennis.TennisMatchFormat.TournamentLevel;
import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;

public class TennisParamFindProblemTest extends AlgoManagerSimpleTestBase {



    long eventId;


    /*
     * repeat the param find 100 times to try to get it to fail
     */
    public void test1() {
        for (int i = 0; i < 1; i++) {
            MatchFormat matchFormat = new TennisMatchFormat(false, Sex.WOMEN, Surface.HARD, TournamentLevel.ATP, 3,
                            FinalSetType.NORMAL_WITH_TIE_BREAK, false);
            algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, matchFormat, 2);
            publishedMatchParams.setEventId(eventId);
            algoManager.handleSetMatchParams(publishedMatchParams.generateGenericMatchParams());


            TennisMatchIncident matchIncident =
                            new TennisMatchIncident(1, TennisMatchIncidentType.POINT_WON, TeamId.B, 1);
            matchIncident.setEventId(eventId);
            matchIncident.setServerSideAtStartOfMatch(TeamId.A);

            algoManager.handleMatchIncident(matchIncident, true);
            matchIncident = new TennisMatchIncident(1, TennisMatchIncidentType.POINT_WON, TeamId.B, 1);
            algoManager.handleMatchIncident(matchIncident, true);
            matchIncident = new TennisMatchIncident(1, TennisMatchIncidentType.POINT_WON, TeamId.B, 1);
            algoManager.handleMatchIncident(matchIncident, true);
            matchIncident = new TennisMatchIncident(1, TennisMatchIncidentType.POINT_WON, TeamId.A, 1);
            algoManager.handleMatchIncident(matchIncident, true);
            matchIncident = new TennisMatchIncident(1, TennisMatchIncidentType.POINT_WON, TeamId.A, 1);
            algoManager.handleMatchIncident(matchIncident, true);

            algoManager.handleSupplyMarketPrices(eventId, getTestMarketPrices());
            System.out.println(publishedParamFinderResults);
            System.out.println(publishedMatchParams);
            System.out.println("--------------Params Found (" + i + ") ------------------");
        }


    }

    /*
     * run the price calc for the specific that params that fail
     */
    public void test2() {
        for (int i = 0; i < 100; i++) {
            MatchFormat matchFormat = new TennisMatchFormat(false, Sex.WOMEN, Surface.HARD, TournamentLevel.ATP, 3,
                            FinalSetType.NORMAL_WITH_TIE_BREAK, false);
            algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, matchFormat, 2);
            TennisMatchParams matchParams = (TennisMatchParams) publishedMatchParams;
            matchParams.setEventId(eventId);
            matchParams.getOnServePctA1().setProperties(0.63201111, 0.11551323570920102, 0);
            matchParams.getOnServePctB1().setProperties(0.77124561, 0.09784607903070702, 0);
            algoManager.handleSetMatchParams(publishedMatchParams.generateGenericMatchParams());
            System.out.println("--------------------------Successful Run (" + i
                            + " out of 100) ----------------------------");
        }
    }


    private MarketPricesList getTestMarketPrices() {
        MarketPricesList marketPricesList = new MarketPricesList();

        MarketPrices bet365 = new MarketPrices();
        bet365.setSourceWeight(1);
        marketPricesList.put("Bet365", bet365);
        MarketPrice b1 = new MarketPrice("FT:ML", "Match Betting", MarketCategory.GENERAL, null);
        b1.put("A", 1.10);
        b1.put("B", 7.00);
        bet365.addMarketPrice(b1);
        MarketPrice b2 = new MarketPrice("FT:OU", "Total Games", MarketCategory.OVUN, "18.5");
        b2.put("Over", 1.91);
        b2.put("Under", 1.80);
        bet365.addMarketPrice(b2);
        MarketPrice b3 = new MarketPrice("FT:SPRD", "Game Handicap", MarketCategory.HCAP, "-6.5");
        b3.put("AH", 2.00);
        b3.put("BH", 1.73);
        bet365.addMarketPrice(b3);
        MarketPrice b4 = new MarketPrice("G:ML", "Game Betting", MarketCategory.GENERAL, null);
        b4.setSequenceId("S1.1");
        b4.put("A", 2.25);
        b4.put("B", 1.57);
        bet365.addMarketPrice(b4);

        return marketPricesList;
    }

    public static void main(String[] args) {
        TennisParamFindProblemTest tester = new TennisParamFindProblemTest();
        tester.test1();
        System.out.println("finished");
    }



}

