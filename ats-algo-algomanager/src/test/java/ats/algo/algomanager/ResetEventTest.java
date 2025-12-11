package ats.algo.algomanager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import ats.algo.core.eventsettings.EventSettings;
import ats.algo.core.eventsettings.GeneralEventSetting;
import org.junit.Test;

import ats.algo.core.common.SupportedSportType;
import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.Gaussian;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchIncident;
import ats.algo.sport.tennis.TennisMatchParams;
import ats.algo.sport.tennis.TennisSimpleMatchState;
import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;

import java.util.Map;

public class ResetEventTest extends AlgoManagerSharedMemoryTestBase {

    @Test
    public void testResetEvent() {
        TennisMatchFormat tennisMatchFormat = new TennisMatchFormat();
        /*
         * create match and get first set of published markets
         */
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, 13579L, tennisMatchFormat);
        waitOnPublishedMatchParams();
        TennisMatchParams defaultMatchParams = (TennisMatchParams) this.publishedMatchParams;
        defaultMatchParams.setEventId(11L);
        Gaussian g = defaultMatchParams.getOnServePctA1();
        g.setMean(g.getMean() + 0.000001); // make a small change the default params
        algoManager.handleSetMatchParams(defaultMatchParams.generateGenericMatchParams());
        waitOnPublishedMarkets();

        EventDetails eventDetails = algoManager.getEventDetails(13579L);
        EventSettings eventSettings = eventDetails.getEventSettings();
        eventSettings.getGeneralEventSettings().put("SOURCEWEIGHT_Pinnacle", new GeneralEventSetting(false,"200.0"));
        eventSettings.getGeneralEventSettings().put("SOURCEWEIGHT_Unibet", new GeneralEventSetting(false,"20.0"));
        eventSettings.getGeneralEventSettings().put("SOURCEWEIGHT_BetVictor", new GeneralEventSetting(false,"5.0"));

        /*
         * send in MatchIncident and then immediately a reset event. Should see in the logs a) msg saying abandon
         * priceCalcRequest then b) msg saying pricecalcResponse recevied for unexpected uniqueRequestId
         */
        TennisMatchIncident tennisMatchIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A);
        tennisMatchIncident.setEventId(13579L);
        tennisMatchIncident.setIncidentId("REQUEST_ID_235");
        algoManager.handleMatchIncident(tennisMatchIncident, true);
        super.publishedMarkets = null;
        algoManager.handleResetEvent(13579L, tennisMatchFormat, 3);

        // Test for SourceWeight persistence
        eventDetails = algoManager.getEventDetails(13579L);
        Map<String, GeneralEventSetting> eventGeneralSettings =
                        eventDetails.getEventSettings().getGeneralEventSettings();
        boolean sourceWeightsFound = false;
        for(Map.Entry<String, GeneralEventSetting> eventGeneralSettingEntry: eventGeneralSettings.entrySet()){
            if(eventGeneralSettingEntry.getKey().startsWith("SOURCEWEIGHT"))
            {
                sourceWeightsFound = true;
                break;
            }
        }
        assertTrue(sourceWeightsFound);
        //End of SourceWeight persistence test

        defaultMatchParams = (TennisMatchParams) this.publishedMatchParams;
        defaultMatchParams.setEventId(11L);
        g = defaultMatchParams.getOnServePctA1();
        g.setMean(g.getMean() + 0.000001); // make a small change the default params
        algoManager.handleSetMatchParams(defaultMatchParams.generateGenericMatchParams());
        TennisSimpleMatchState simpleMatchState = (TennisSimpleMatchState) publishedMatchState;
        waitOnPublishedMarkets();
        assertEquals(TeamId.UNKNOWN, simpleMatchState.getOnServeNow());
    }
}
