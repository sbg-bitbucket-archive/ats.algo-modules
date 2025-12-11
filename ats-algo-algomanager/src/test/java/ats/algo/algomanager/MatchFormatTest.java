package ats.algo.algomanager;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ats.algo.core.common.SupportedSportType;
import ats.algo.core.traderalert.TraderAlert.TraderAlertType;
import ats.algo.genericsupportfunctions.Gaussian;
import ats.algo.sport.football.FootballMatchFormat;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchParams;

public class MatchFormatTest extends AlgoManagerSharedMemoryTestBase {
    @Test
    public void testResetMatchFormatEvent() {
        TennisMatchFormat tennisMatchFormat = new TennisMatchFormat();
        /*
         * create match and get first set of published markets
         */
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, 13579L, tennisMatchFormat);
        waitOnPublishedMatchParams();
        TennisMatchParams defaultMatchParams = (TennisMatchParams) this.publishedMatchParams;
        defaultMatchParams.setEventId(11L);
        Gaussian g = defaultMatchParams.getOnServePctA1();
        g.setMean(g.getMean() + 0.000001); // make a small change the default
                                           // params
        algoManager.handleSetMatchParams(defaultMatchParams.generateGenericMatchParams());
        waitOnPublishedMarkets();
        tennisMatchFormat.setMatchFormatOk(false);
        algoManager.handleResetEvent(13579L, tennisMatchFormat, 3);
        tennisMatchFormat.setMatchFormatOk(true);
        algoManager.handleResetEvent(13579L, tennisMatchFormat, 3);
        assertTrue(algoManager.getEventDetails(13579L).isEventSuspended() == false);


    }

    @Test
    public void testMatchFormatCreation() {
        FootballMatchFormat matchFormat = new FootballMatchFormat();
        matchFormat.setMatchFormatOk(false);
        long eventId = 789L;

        algoManager.handleNewEventCreation(SupportedSportType.SOCCER, eventId, matchFormat);
        assertTrue(traderAlert.getTraderAlertType() == TraderAlertType.MATCHFORMAT_WARNING);
    }


}
