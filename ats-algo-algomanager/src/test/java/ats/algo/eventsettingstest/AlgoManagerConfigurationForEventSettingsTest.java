package ats.algo.eventsettingstest;

import ats.algo.algomanager.AbstractParamFinder;
import ats.algo.algomanager.AbstractPriceCalculator;
import ats.algo.algomanager.AlgoManagerConfiguration;
import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.eventsettings.EventSettings;
import ats.algo.core.markets.Markets;
import ats.algo.core.request.ParamFindRequest;
import ats.algo.core.request.ParamFindResponse;
import ats.algo.core.request.PriceCalcRequest;
import ats.algo.core.request.PriceCalcResponse;

/**
 * A simple single threaded configuration which directly wires the inputs and outputs between AlgoManager AlgoCalculator
 * and AlgoPAramFinder
 * 
 * @author Geoff
 *
 */

public class AlgoManagerConfigurationForEventSettingsTest extends AlgoManagerConfiguration {

    public static long eventTierOnEntry;

    public AlgoManagerConfigurationForEventSettingsTest() {
        super();
    }

    @Override
    public void schedulePriceCalc(PriceCalcRequest request) {
        /*
         * serialise all the data going to Algo calculator, to simulate what happens in the live config
         */
        EventSettings eventSettings = request.getEventSettings();
        eventTierOnEntry = eventSettings.getEventTier();
        PriceCalcResponse response = new PriceCalcResponse(request.getUniqueRequestId(), new Markets(),
                        request.getMatchParams(), null, null);
        if (request.getCalcRequestCause().equals(CalcRequestCause.PARAMS_CHANGED_BY_TRADER))
            response.setEventSettings(EventSettings.generateEventSettingsForTesting(1));
        algoHandlePriceCalcResponse.handle(request.getEventId(), response);
    }

    @Override
    public boolean establishExternalModelConnection(String url) {
        return false;
    }

    @Override
    public void scheduleExternalPriceCalc(PriceCalcRequest request) {

    }

    @Override
    public void scheduleParamFind(ParamFindRequest request) {
        ParamFindResponse response = null;
        algoHandleParamFindResponse.handle(request.getEventId(), response);
    }

    @Override
    public void scheduleExternalParamFind(ParamFindRequest request) {

    }

    @Override
    public void schedulePriceCalc(PriceCalcRequest request, Class<? extends AbstractPriceCalculator> clazz) {}

    @Override
    public void scheduleParamFind(ParamFindRequest request, Class<? extends AbstractParamFinder> clazz) {}

    @Override
    public int getPriceCalcQueueSize() {
        return 0;
    }

    @Override
    public int getParamFindQueueSize() {
        return 0;
    }

    @Override
    public int getNoAlgoCalculators() {
        return 1;
    }

    @Override
    public int getNoAlgoParamFinders() {
        return 1;
    }

    @Override
    public void abandonPriceCalc(long eventId, String uniqueRequestId) {
        /*
         * do nothing
         */
    }

    @Override
    public void abandonParamFind(long eventId, String uniqueRequestId) {
        /*
         * do nothing
         */
    }
}
