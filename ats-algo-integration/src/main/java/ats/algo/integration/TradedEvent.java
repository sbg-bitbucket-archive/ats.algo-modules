package ats.algo.integration;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import ats.algo.algomanager.EventStateBlob;
import ats.algo.core.SportMetaData;
import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.comparetomarket.ParamFindResults;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketTypeDescriptor;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarket;
import ats.core.AtsBean;
import ats.core.util.ExceptionUtil;
import generated.ats.betsync.dto.Attribute;
import generated.ats.betsync.dto.EventTradingState;
import generated.ats.betsync.dto.EventTradingStateUpdate;
import generated.ats.betsync.dto.MatchDetails;
import generated.ats.betsync.dto.NotificationMessage;
import generated.ats.betsync.dto.Participant;
import generated.ats.betsync.dto.Participants;

public final class TradedEvent extends AtsBean {
    private MatchDetails matchDetails;
    private Set<MarketTypeDescriptor> createdMarkets = Sets.newConcurrentHashSet();
    private Set<String> receivedIncidentIds = Sets.newConcurrentHashSet();
    private Map<MarketTypeDescriptor, String> resultedMarkets = Maps.newHashMap();
    private Long eventId;
    private Markets markets;
    private boolean paramsOk = true;
    private Instant completedAt;
    private static Map<String, Set<String>> MARKET_SELECTION_TYPES = Maps.newConcurrentMap();
    private MatchParams matchParams;
    private SupportedSportType sportType;
    private String paramFindingSource;
    private ParamFindResults lastParamFindResults;
    private MatchFormat matchFormat;
    private Map<String, Market> marketsToEventuallyDiscontinue = Maps.newConcurrentMap();
    private boolean forResultingOnly;
    private int traderSavedParamCount;
    private int numTimesPublishedMarketPrices;
    private GenericMatchParams updatedMatchParams;
    private String incidentProvider;
    private String rawBookieWeightingsSettingValue = "";
    private long blobRecoveredTime;
    private int numberOfIncidentsForEvent;
    private String pricingMethod;
    private Cache<String, Boolean> settingNamesChangedCache =
                    CacheBuilder.newBuilder().expireAfterWrite(7, TimeUnit.SECONDS).build();

    public TradedEvent(MatchDetails match) {
        matchDetails = match;
        eventId = match.getMatchId();
        sportType = SupportedSportType.valueOf(match.getSportcode());
    }

    public TradedEvent(Long matchId, String sportCode) {
        eventId = matchId;
        sportType = SupportedSportType.valueOf(sportCode);
    }

    public void setPricingMethod(String pricingMethod) {
        this.pricingMethod = pricingMethod;
    }

    public boolean isMirroredPricingInAts() {
        return pricingMethod != null && "MIRRORED".equals(pricingMethod);
    }

    public boolean isForResultingOnly() {
        return forResultingOnly;
    }

    public void setForResultingOnly(boolean forResultingOnly) {
        this.forResultingOnly = forResultingOnly;
    }

    public boolean isDescendantOf(String otherPath) {
        Objects.requireNonNull(matchDetails, "Match Details is null");
        String path = Objects.requireNonNull(matchDetails.getPath(), "Match Details Path is null");
        Objects.requireNonNull(otherPath, "Compare Path is null");
        return path.startsWith(otherPath);
    }

    public void settingPublished(String settingName) {
        settingNamesChangedCache.put(settingName, Boolean.TRUE);
    }

    public boolean justPublishedSetting(String settingName) {
        return settingNamesChangedCache.asMap().remove(settingName) != null;
    }

    public boolean isNewBookieWeightings(String rawBookieWeightingsSettingValue) {
        if (!rawBookieWeightingsSettingValue.equals(this.rawBookieWeightingsSettingValue)) {
            this.rawBookieWeightingsSettingValue = rawBookieWeightingsSettingValue;
            return true;
        }
        return false;
    }

    public MatchFormat getMatchFormat() {
        if (matchFormat == null) {
            SportMetaData meta = SportMetaData.forSportType(sportType);
            if (meta != null) {
                try {
                    matchFormat = meta.getMatchFormat().newInstance();
                    @SuppressWarnings("unchecked")
                    Map<String, Object> format = (Map<String, Object>) matchDetails.getFormat();
                    if (format != null && !format.isEmpty()) {
                        matchFormat.applyFormat(format);
                    }
                } catch (Exception e) {
                    throw ExceptionUtil.unexpectedException(e);
                }
            }
        }
        return matchFormat;
    }

    public boolean isSport(SupportedSportType type) {
        return sportType == type;
    }

    public SupportedSportType getSport() {
        return sportType;
    }

    public Participant getParticipantA() {
        return getParticipantByPosition(0);
    }

    public Participant getParticipantB() {
        return getParticipantByPosition(1);
    }

    public int getNumberOfIncidentsForEvent() {
        return numberOfIncidentsForEvent;
    }

    private Participant getParticipantByPosition(int position) {
        Participants participants = matchDetails.getParticipants();
        if (participants != null) {
            for (Participant p : participants.getParticipant()) {
                if (p.getPosition().intValue() == position) {
                    return p;
                }
            }
        }
        return null;
    }

    public boolean isChangedParams(MatchParams foundParams) {
        return matchParams == null || !matchParams.equals(foundParams);
    }

    public void setCurrentParams(MatchParams matchParams) {
        this.matchParams = matchParams.copy();
    }

    public MatchParams getMatchParams() {
        return matchParams;
    }

    public String getIncidentProvider() {
        return incidentProvider;
    }

    public boolean isNewIncident(MatchIncident incident) {
        String sourceSystem = incident.getSourceSystem();
        if (sourceSystem != null && (incidentProvider == null || !incidentProvider.equals(sourceSystem))) {
            incidentProvider = sourceSystem;
        }
        return receivedIncidentIds.add(incident.getIncidentId());
    }

    public boolean isPrematch() {
        if (receivedIncidentIds.isEmpty()) {
            return true;
        } else {
            numberOfIncidentsForEvent++;
            return false;
        }

    }

    public boolean addResultedMarket(ResultedMarket resultedMarket, String incidentIdThatTriggeredResults) {
        MarketTypeDescriptor descriptor = resultedMarket.getDescriptor().intern();
        debug("Resulted Market: %s. Descriptor: %s", resultedMarket, descriptor);
        synchronized (resultedMarkets) {
            if (!resultedMarkets.containsKey(descriptor)) {
                debug("Resulted Market has been added");
                resultedMarkets.put(descriptor, incidentIdThatTriggeredResults);
                return true;
            }
        }
        return false;
    }

    public boolean hasResulted(ResultedMarket resultedMarket) {
        synchronized (resultedMarkets) {
            return resultedMarkets.containsKey(resultedMarket.getDescriptor());
        }
    }

    public int clearResultsTriggeredByIncidentId(String requestId) {
        synchronized (resultedMarkets) {
            int initialSize = resultedMarkets.size();
            resultedMarkets.entrySet().removeIf(m -> m.getValue().equals(requestId));
            return initialSize - resultedMarkets.size();
        }
    }

    public NotificationMessage createDisplayVariable(String dvName, String value) {
        NotificationMessage dv = createNotificationMessage("DisplayVariable");
        dv.setName(dvName);
        dv.setDescription(value);
        return dv;
    }

    public NotificationMessage createEventBlobCheckpointNotification(EventStateBlob eventStateBlob) {
        NotificationMessage nm = createNotificationMessage("EventBlobCheckpoint");
        Attribute attribute = new Attribute();
        attribute.setName("requestId");
        attribute.setValue(eventStateBlob.getIncidentId());
        nm.getAttributes().add(attribute);
        return nm;
    }

    public NotificationMessage createSupplyAlgoMgrParamFindingMarketsNotification() {
        return createNotificationMessage("SupplyAlgoMgrParamFindingMarkets");
    }

    public NotificationMessage createNotificationMessage(String category) {
        NotificationMessage notification = new NotificationMessage();
        notification.setAtsEventId(getEventId());
        notification.setAlertTime(new Date());
        notification.setCategory(category);
        return notification;
    }

    public void setParamsOk(boolean ok) {
        paramsOk = ok;
    }

    public boolean isParamsOk() {
        return paramsOk;
    }

    public void setParamFindingSource(String source) {
        this.paramFindingSource = source;
    }

    public String getParamFindingSource() {
        return paramFindingSource;
    }

    public boolean hasMarketAlreadyBeenCreated(Market market) {
        Map<String, Set<String>> marketSelectionTypes = MARKET_SELECTION_TYPES;
        String fullUniqueMarket = getEventId() + ":" + getSport().toString() + ":" + market.getFullKey();
        if (!marketSelectionTypes.containsKey(fullUniqueMarket)) {
            Set<String> selectionKeys = market.getSelections().keySet();
            if (!selectionKeys.isEmpty()) {
                Set<String> selectionTypes = Sets.newHashSet(selectionKeys);
                marketSelectionTypes.put(fullUniqueMarket, selectionTypes);
            }
        }

        MarketTypeDescriptor descriptor = market.getDescriptor();
        descriptor.intern();
        boolean existingMarket = !createdMarkets.add(descriptor);

        recordEventualDiscontinuedMarket(market);

        return existingMarket;
    }

    public Market removeDiscontinuedMarket(String fullKey) {
        return marketsToEventuallyDiscontinue.remove(fullKey);
    }

    public Set<String> getMarketSelectionTypes(String fullKeyOfMarket) {
        String fullUniqueMarket = getEventId() + ":" + getSport().toString() + ":" + fullKeyOfMarket;
        Set<String> selectionTypes = MARKET_SELECTION_TYPES.get(fullUniqueMarket);
        return selectionTypes;
    }

    public Long getEventId() {
        return eventId;
    }

    public boolean isBiased() {
        return matchParams != null && matchParams.isBiased();
    }

    public MatchDetails getMatchDetails() {
        return matchDetails;
    }

    public String toATSMarketType(Market market) {
        return matchDetails.getSportcode() + ":" + market.getType();
    }

    public String toATSMarketType(ResultedMarket resultedMarket) {
        return matchDetails.getSportcode() + ":" + resultedMarket.getType();
    }

    public List<MarketTypeDescriptor> getSubtypes(String type) {
        return createdMarkets.stream().filter(mtd -> mtd.getType().equals(type)).collect(Collectors.toList());
    }

    public List<MarketTypeDescriptor> getSubtypesForSequence(String type, String sequence) {
        return createdMarkets.stream()
                        .filter(mtd -> mtd.getType().equals(type)
                                        && AtsMarketTypeMapper.sequenceIdsAreTheSame(sequence, mtd.getSequenceId()))
                        .collect(Collectors.toList());
    }

    public Markets getMarkets() {
        return markets;
    }

    public void setMarkets(Markets value) {
        if (markets != null) {
            Set<String> allMarketKeys = value.getMarketKeys();
            allMarketKeys.removeAll(markets.getMarketKeys());
            int size = allMarketKeys.size();
            if (size > 0) {
                debug("%s new markets added to %s : %s", size, toString(), allMarketKeys);
            } else {
                debug("No new markets for %s", matchDetails.getName());
                return;
            }
            markets.putAll(value);
        } else {
            markets = value;
        }
        for (Market m : value) {
            recordEventualDiscontinuedMarket(m);
        }
    }

    public void setLastParamFindResults(ParamFindResults paramFindResults) {
        this.lastParamFindResults = paramFindResults;
    }

    public ParamFindResults getLastParamFindResults() {
        return lastParamFindResults;
    }

    public EventTradingStateUpdate createEventTradingStateUpdate() {
        EventTradingStateUpdate update = new EventTradingStateUpdate();
        update.setSystemcode(IntegrationConstants.ALGO_MGR_SYSTEM_CODE);
        EventTradingState eventTradingState = new EventTradingState();
        eventTradingState.setId(getEventId());
        eventTradingState.setChannelCode(IntegrationConstants.ALL_CHANNELS);
        update.setEventTradingState(eventTradingState);
        return update;
    }

    public void completed(boolean isCompleted) {
        if (isCompleted) {
            if (completedAt == null) {
                completedAt = Instant.now();
                marketsToEventuallyDiscontinue.clear();
                resultedMarkets.clear();
                createdMarkets.clear();
                receivedIncidentIds.clear();
                numberOfIncidentsForEvent = 0;
            }
        } else {
            completedAt = null;
        }
    }

    public boolean isCompleted() {
        return completedAt != null;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public int incrementAndGetTraderSavedParamCount() {
        return ++traderSavedParamCount;
    }

    public int incrementAndGetNumTimesPublishedMarketPrices() {
        return ++numTimesPublishedMarketPrices;
    }

    private void recordEventualDiscontinuedMarket(Market market) {
        String fullKey = market.getFullKey();
        if (!marketsToEventuallyDiscontinue.containsKey(fullKey)) {
            Market copy = market.copy();
            copy.getSelectionsProbs().clear();
            marketsToEventuallyDiscontinue.put(fullKey, copy);
        }
    }

    public void blobRecovered() {
        blobRecoveredTime = System.currentTimeMillis();
    }

    public long getBlobRecoveredTime() {
        return blobRecoveredTime;
    }

    public GenericMatchParams getUpdatedMatchParams() {
        return updatedMatchParams;
    }

    public void setUpdatedMatchParams(GenericMatchParams updatedMatchParams) {
        this.updatedMatchParams = updatedMatchParams;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((eventId == null) ? 0 : eventId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TradedEvent other = (TradedEvent) obj;
        if (eventId == null) {
            if (other.eventId != null)
                return false;
        } else if (!eventId.equals(other.eventId))
            return false;
        return true;
    }

    @Override
    public String toString() {
        ToStringHelper sh = MoreObjects.toStringHelper(this);
        sh.addValue(sportType);
        if (matchDetails != null) {
            sh.addValue(matchDetails.getName());
        }
        sh.add("id", eventId);
        if (forResultingOnly) {
            sh.addValue("forResultingOnly");
        }
        sh.add("numTimesPublishedMarketPrices", numTimesPublishedMarketPrices);
        return sh.toString();
    }
}
