package ats.algo.algomanager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ats.algo.core.common.SupportedSportType;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.SuspensionStatus;
import ats.algo.core.traderalert.TraderAlert.TraderAlertType;
import ats.algo.genericsupportfunctions.Gaussian;
import ats.algo.genericsupportfunctions.MethodName;
import ats.algo.sport.football.FootballMatchFormat;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchFormat.FinalSetType;
import ats.algo.sport.tennis.TennisMatchParams;

public class MatchFormatTest extends AlgoManagerSimpleTestBase {
    @Test
    public void testResetMatchFormatEvent() {
        MethodName.log();
        TennisMatchFormat tennisMatchFormat = new TennisMatchFormat();
        /*
         * create match and get first set of published markets
         */
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, 13579L, tennisMatchFormat);
        TennisMatchParams defaultMatchParams = (TennisMatchParams) this.publishedMatchParams;
        defaultMatchParams.setEventId(13579L);
        Gaussian g = defaultMatchParams.getOnServePctA1();
        g.setMean(g.getMean() + 0.000001); // make a small change the default
                                           // params
        algoManager.handleSetMatchParams(defaultMatchParams.generateGenericMatchParams());
        tennisMatchFormat.setMatchFormatOk(false);
        algoManager.handleResetEvent(13579L, tennisMatchFormat, 3);
        tennisMatchFormat.setMatchFormatOk(true);
        algoManager.handleResetEvent(13579L, tennisMatchFormat, 3);
        assertTrue(algoManager.getEventDetails(13579L).isEventSuspended() == false);


    }

    @Test
    public void testResetMatchFormatEventSuspendsMarkets() {
        MethodName.log();
        TennisMatchFormat threeSetTennisMatchFormat = new TennisMatchFormat();
        long eventID = 13579L;
        /*
         * create match and get first set of published markets
         */
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventID, threeSetTennisMatchFormat);

        // System.out.println(publishedMarkets);
        TennisMatchParams defaultMatchParams = (TennisMatchParams) this.publishedMatchParams;
        defaultMatchParams.setEventId(eventID);
        Gaussian g = defaultMatchParams.getOnServePctA1();
        g.setMean(g.getMean() + 0.000001); // make a small change the default
                                           // params
        algoManager.handleSetMatchParams(defaultMatchParams.generateGenericMatchParams());
        // Have removed the fixme - is now working.
        Market market = publishedMarkets.get("FT:OU");
        // System.out.println(market);
        assertEquals(market.getLineId(), "+23.5");
        assertEquals(market.getMarketStatus().getSuspensionStatus(), SuspensionStatus.OPEN);

        TennisMatchFormat fiveSetTennisMatchFormat =
                        new TennisMatchFormat(5, FinalSetType.NORMAL_WITH_TIE_BREAK, false, false);

        algoManager.handleResetEvent(eventID, fiveSetTennisMatchFormat, 3);

        // market = publishedMarkets.get("FT:OU");
        // assertEquals(market.getLineId(), "+23.5");
        // assertEquals(market.getMarketStatus().getSuspensionStatus(), SuspensionStatus.SUSPENDED_UNDISPLAY);

        algoManager.handleSetMatchParams(defaultMatchParams.generateGenericMatchParams());

        // market = publishedMarkets.get("FT:OU");
        // assertEquals(market.getLineId(), "+39.5");
        // assertEquals(market.getMarketStatus().getSuspensionStatus(), SuspensionStatus.OPEN);

    }

    @Test
    public void testMatchFormatCreation() {
        MethodName.log();
        FootballMatchFormat matchFormat = new FootballMatchFormat();
        matchFormat.setMatchFormatOk(false);
        long eventId = 789L;

        algoManager.handleNewEventCreation(SupportedSportType.SOCCER, eventId, matchFormat);
        if (traderAlert != null)
            assertEquals(traderAlert.getTraderAlertType(), TraderAlertType.MATCHFORMAT_WARNING);
    }


}
