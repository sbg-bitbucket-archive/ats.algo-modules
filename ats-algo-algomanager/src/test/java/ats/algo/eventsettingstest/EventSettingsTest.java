package ats.algo.eventsettingstest;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import ats.algo.algomanager.AlgoManager;
import ats.algo.algomanager.AlgoManagerPublishable;
import ats.algo.algomanager.EventStateBlob;
import ats.algo.algomanager.SupportedSportsInitialisation;
import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.comparetomarket.ParamFindResults;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.recordplayback.RecordedItem;
import ats.algo.core.timestamp.TimeStamp;
import ats.algo.core.traderalert.TraderAlert;
import ats.algo.sport.football.FootballMatchFormat;

public class EventSettingsTest implements AlgoManagerPublishable {

    AlgoManager algoManager;
    Map<String, String> publishedProperties;
    GenericMatchParams publishedMatchParams;

    public EventSettingsTest() {

    }

    @Test
    public void test() {
        MethodName.log();
        AlgoManagerConfigurationForEventSettingsTest algoManagerConfiguration =
                        new AlgoManagerConfigurationForEventSettingsTest();
        SupportedSportsInitialisation.init();
        algoManager = new AlgoManager(algoManagerConfiguration, this);
        long eventId = 1579L;
        algoManager.handleNewEventCreation(SupportedSportType.SOCCER, eventId, new FootballMatchFormat());
        assertEquals(3, AlgoManagerConfigurationForEventSettingsTest.eventTierOnEntry);
        assertNull(publishedProperties);
        Map<String, String> properties = new HashMap<>(1);
        properties.put("eventTier", "2");
        algoManager.handleSetEventProperties(eventId, properties);
        /*
         * should not publish properties following call to handleSetEventProperties
         */
        assertNull(publishedProperties);
        assertEquals(2, AlgoManagerConfigurationForEventSettingsTest.eventTierOnEntry);
        /*
         * calcreqeustCause = PARAMS_CHANGED_BY_TRADER causes AlgoManagerconfigurationForEventSettingsTest to set
         * eventTier to 1
         */
        algoManager.handleSetMatchParams(publishedMatchParams);
        String eventTierStr = publishedProperties.get("eventTier");
        assertEquals("1", eventTierStr);
        assertEquals(2, AlgoManagerConfigurationForEventSettingsTest.eventTierOnEntry);
        publishedProperties = null;
        algoManager.handleSetMatchParams(publishedMatchParams);
        assertEquals(1, AlgoManagerConfigurationForEventSettingsTest.eventTierOnEntry);
        algoManager.close();

    }

    @Override
    public void publishMatchParams(long eventId, GenericMatchParams matchParams) {
        this.publishedMatchParams = matchParams;

    }

    @Override
    public void publishMatchState(long eventId, SimpleMatchState matchState) {

    }

    @Override
    public void publishMarkets(long eventId, Markets markets, Set<String> keysForDiscontinuedMarkets,
                    TimeStamp timeStam, String sequenceIdp) {

    }

    @Override
    public void publishResultedMarkets(long eventId, ResultedMarkets ResultedMarkets) {

    }

    @Override
    public void publishParamFindResults(long eventId, ParamFindResults paramFindResults,
                    GenericMatchParams genericMatchParams, long elapsedTimeMs) {

    }

    @Override
    public void notifyEventCompleted(long eventId, boolean isCompleted) {

    }

    @Override
    public void notifyFatalError(long eventId, String requestId, String errorCause) {

    }

    @Override
    public void publishRecordedItem(long eventId, RecordedItem recordedItem) {

    }

    @Override
    public void publishTraderAlert(long eventId, TraderAlert traderAlert) {

    }

    @Override
    public void publishEventState(long eventId, EventStateBlob eventStateBlob) {

    }

    @Override
    public void publishEventSuspensionStatus(long eventId, boolean suspend, Set<MarketGroup> marketGroups) {

    }

    @Override
    public void publishEventProperties(long eventId, Map<String, String> properties) {
        this.publishedProperties = properties;
    }

}
