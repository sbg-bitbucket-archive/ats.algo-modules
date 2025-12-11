package ats.algo.algomanager;

import java.util.Map;
import java.util.Set;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.core.comparetomarket.ParamFindResults;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.recordplayback.RecordedItem;
import ats.algo.core.timestamp.TimeStamp;
import ats.algo.core.traderalert.TraderAlert;

public interface AlgoManagerPublishable {

    public void publishMatchParams(long eventId, GenericMatchParams matchParams);

    public void publishMatchState(long eventId, SimpleMatchState matchState);

    public void publishMarkets(long eventId, Markets markets, Set<String> keysForDiscontinuedMarkets,
                    TimeStamp timeStamp, String currentSequenceId);

    public void publishResultedMarkets(long eventId, ResultedMarkets resultedMarkets);

    public void publishParamFindResults(long eventId, ParamFindResults paramFindResults,
                    GenericMatchParams genericMatchParams, long elapsedTimeMs);

    public void notifyEventCompleted(long eventId, boolean isCompleted);

    public void notifyFatalError(long eventId, String uniqueRequestId, String errorCause);

    public void publishRecordedItem(long eventId, RecordedItem recordedItem);

    public void publishTraderAlert(long eventId, TraderAlert traderAlert);

    public void publishEventState(long eventId, EventStateBlob eventStateBlob);

    public void publishEventSuspensionStatus(long eventId, boolean suspend, Set<MarketGroup> marketGroups);

    public void publishEventProperties(long eventId, Map<String, String> properties);
}
