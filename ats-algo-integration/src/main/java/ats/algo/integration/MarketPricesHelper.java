package ats.algo.integration;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketCategory;
import ats.algo.core.markets.MarketStatus;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.MarketsMetaData;
import ats.algo.core.markets.Selection;
import ats.algo.core.markets.SuspensionStatus;
import ats.algo.core.timestamp.TimeStamp;
import ats.betting.betsync.dto.FeedBetsyncDto;
import ats.core.AtsBean;
import generated.ats.betsync.dto.ActivityTime;
import generated.ats.betsync.dto.ChannelPrices;
import generated.ats.betsync.dto.EventTradingStateUpdate;
import generated.ats.betsync.dto.MarketPrice;
import generated.ats.betsync.dto.MarketPrices;
import generated.ats.betsync.dto.MarketState;
import generated.ats.betsync.dto.SelectionPrice;
import generated.ats.betsync.dto.SelectionState;
import generated.ats.betsync.dto.Timing;

public class MarketPricesHelper extends AtsBean {

    private AtsMarketTypeMapper atsMarketTypeMapper;

    public MarketPricesHelper(AtsMarketTypeMapper atsMarketTypeMapper) {
        this.atsMarketTypeMapper = atsMarketTypeMapper;
    }

    public FeedBetsyncDto addPrices(FeedBetsyncDto feedBetsyncDto, TradedEvent tradedEvent, Markets markets,
                    Set<String> keysForDiscontinuedMarkets, TimeStamp timeStamp, boolean resetEvent) {
        EventTradingStateUpdate update = tradedEvent.createEventTradingStateUpdate();
        feedBetsyncDto.setEventTradingStateUpdate(update);
        MarketPrices marketPrices = new MarketPrices();
        update.getEventTradingState().setPrices(marketPrices);
        MarketsMetaData marketsMetaData = markets.getMarketsMetaData();
        List<MarketPrice> marketPricesList = marketPrices.getMarket();
        if (tradedEvent.incrementAndGetNumTimesPublishedMarketPrices() == 1) {
            update.getEventTradingState().setSnapshot(true);
        }
        if (tradedEvent.getNumberOfIncidentsForEvent() == 1) {
            update.getEventTradingState().setSnapshot(true);
        }

        markets.forEach(market -> processMarket(market, marketPricesList, tradedEvent, marketsMetaData, resetEvent));// (marketPricesList.add(toMarketPrice(tradedEvent,
        // marketsMetaData,
        // market)));

        if (keysForDiscontinuedMarkets != null && !keysForDiscontinuedMarkets.isEmpty()) {
            for (String fullKey : keysForDiscontinuedMarkets) {
                Market discontinuedMarket = tradedEvent.removeDiscontinuedMarket(fullKey);
                if (discontinuedMarket != null) {
                    debug("Making discontinued %s market %s SUSPENDED and UNDISPLAYED", tradedEvent, fullKey);
                    MarketStatus marketStatus = discontinuedMarket.getMarketStatus();
                    marketStatus.setSuspensionStatus(SuspensionStatus.SUSPENDED_UNDISPLAY);
                    marketStatus.setSuspensionStatusReason("Discontinued");
                    marketStatus.setSuspensionStatusRuleName("Discontinued Line");
                    marketPricesList.add(toMarketPrice(tradedEvent, marketsMetaData, discontinuedMarket));
                } else {
                    warn("Could find not prior-created, as yet discontinued %s market in %s to take off site", fullKey,
                                    tradedEvent);
                }
            }
        }

        if (timeStamp != null) {
            Timing timing = new Timing();
            timing.setEventId(timeStamp.getEventId());
            timing.setOriginatingReqId(timeStamp.getRequestId());
            timing.setOriginatingProvider(tradedEvent.getIncidentProvider());

            addActivityTime("incidentCreated", timeStamp.getTimeMatchIncidentCreated(), timing);
            addActivityTime("incidentReceivedByAlgoManager", timeStamp.getTimeMatchIncidentReceivedByAlgoManager(),
                            timing);
            addActivityTime("priceCalcRequestIssued", timeStamp.getTimePriceCalcRequestIssued(), timing);
            addActivityTime("receivedByCalculationServer",
                            timeStamp.getTimePriceCalcRequestReceivedByCalculationServer(), timing);
            addActivityTime("issuedByCalculationServer", timeStamp.getTimePriceCalcResponseIssedByCalculationServer(),
                            timing);
            addActivityTime("priceCalcResponseInAlgoManager", timeStamp.getTimePriceCalcResponseReceived(), timing);
            if (timeStamp.getTimeNewMarketsSentToBetsyc() > 0) {
                addActivityTime("newLazyMarketsSentToBetsync", timeStamp.getTimeNewMarketsSentToBetsyc(), timing);
            }
            addActivityTime("marketsPublishedByAlgoManager", timeStamp.getTimeUpdatedMarketsPublishedByAlgoManager(),
                            timing);
            marketPrices.setTiming(timing);

            double tripSecs = (System.currentTimeMillis() - timeStamp.getTimeMatchIncidentCreated()) / 1000d;
            info("incident %s %s tripSeconds so far %s", timing.getOriginatingReqId(), tripSecs, timeStamp);
        }

        return feedBetsyncDto;
    }

    private void processMarket(Market market, List<MarketPrice> marketPrices, TradedEvent tradedEvent,
                    MarketsMetaData marketsMetaData, boolean resetEvent) {
        if (!resetEvent && market.getCategory() != MarketCategory.GENERAL && !isMostBalanced(market, marketsMetaData)) {
            return;
        }
        MarketPrice marketPrice = toMarketPrice(tradedEvent, marketsMetaData, market);
        marketPrices.add(marketPrice);
    }

    private void addActivityTime(String activityName, long epoch, Timing timing) {
        ActivityTime activity = new ActivityTime();
        activity.setActivity(activityName);
        activity.setEpoch(epoch);
        timing.getActivityTimes().add(activity);
    }

    private MarketPrice toMarketPrice(TradedEvent tradedEvent, MarketsMetaData marketsMetaData, Market market) {
        MarketPrice marketPrice = new MarketPrice();

        marketPrice.setChannelCode(IntegrationConstants.ALL_CHANNELS);

        debug("Market Prices: Algo Market = " + market);

        boolean canResolveToLegacyAtsMarketType = atsMarketTypeMapper.canResolveToLegacyAtsMarketType(tradedEvent)
                        && atsMarketTypeMapper.resolveAtsMarketType(tradedEvent, market) != null;
        boolean useNewStructureForMarkets = atsMarketTypeMapper.useNewStructureForMarkets(tradedEvent)
                        && atsMarketTypeMapper.resolveAtsMarketType(tradedEvent, market) != null;

        marketPrice.setType(
                        atsMarketTypeMapper.resolveAtsMarketType(tradedEvent, market, canResolveToLegacyAtsMarketType));


        String subType = atsMarketTypeMapper.resolveAtsMarketSubType(tradedEvent, market,
                        canResolveToLegacyAtsMarketType, useNewStructureForMarkets);

        marketPrice.setSubtype(subType);
        marketPrice.setAlgoKey(market.getDescriptor().toToken());

        if (market.getCategory() != MarketCategory.GENERAL) {
            boolean mostBalanced = isMostBalanced(market, marketsMetaData);
            marketPrice.setMostBalanced(mostBalanced);
        }

        MarketStatus marketStatus = market.getMarketStatus();
        if (marketStatus != null) {
            bindMarketTradingState(marketPrice, marketStatus);
        }

        ChannelPrices channelPrices = new ChannelPrices();
        market.getSelections().forEach((k, v) -> addSelection(channelPrices, k, v, canResolveToLegacyAtsMarketType));
        marketPrice.getChannel().add(channelPrices);

        return marketPrice;
    }

    private boolean isMostBalanced(Market market, MarketsMetaData marketsMetaData) {
        String balancedLineForType = marketsMetaData.getBalancedLineForType(market.getType());
        if (balancedLineForType != null && market.getLineId() != null) {
            String lineId = market.getLineId();
            if (balancedLineForType.endsWith("0") && !lineId.endsWith("0")) {
                lineId += "0";
            }
            return balancedLineForType.equals(lineId);
        }
        return false;
    }

    protected void bindMarketTradingState(MarketPrice marketPrice, MarketStatus marketStatus) {
        SuspensionStatus statusCode = marketStatus.getSuspensionStatus();
        if (statusCode != null) {
            MarketState state = null;
            Boolean displayed = null;
            if (statusCode == SuspensionStatus.OPEN) {
                state = MarketState.OPEN;
                displayed = Boolean.TRUE;
            } else if (statusCode == SuspensionStatus.SUSPENDED_UNDISPLAY) {
                state = MarketState.SUSPENDED;
                displayed = Boolean.FALSE;
            } else if (statusCode == SuspensionStatus.SUSPENDED_DISPLAY) {
                state = MarketState.SUSPENDED;
                displayed = Boolean.TRUE;
            } else if (statusCode == SuspensionStatus.ACTIVE_UNDISPLAY) {
                state = MarketState.OPEN;
                displayed = Boolean.FALSE;
            }
            marketPrice.setDisplayed(displayed);
            marketPrice.setState(state);
            marketPrice.setStateTradingRuleName(marketStatus.getSuspensionStatusRuleName());
        }
    }

    protected void addSelection(ChannelPrices channelPrices, String algoSelection, Selection selectiopn,
                    boolean resolveToLegacyAtsTypes) {
        SelectionPrice selectionPrice = new SelectionPrice();
        selectionPrice.setProbability(BigDecimal.valueOf(selectiopn.getProb()));
        if (resolveToLegacyAtsTypes) {
            algoSelection = atsMarketTypeMapper.resolveAtsSelectionType(algoSelection);
        }
        selectionPrice.setType(algoSelection);
        SuspensionStatus suspensionStatus = selectiopn.getSuspensionStatus();
        if (suspensionStatus != null) {
            if (suspensionStatus == SuspensionStatus.OPEN) {
                selectionPrice.setState(SelectionState.ACTIVE);
            } else if (suspensionStatus == SuspensionStatus.ACTIVE_UNDISPLAY) {
                selectionPrice.setState(SelectionState.ACTIVE);
                selectionPrice.setDisplayed(Boolean.FALSE);
            } else if (suspensionStatus == SuspensionStatus.SUSPENDED_DISPLAY) {
                selectionPrice.setState(SelectionState.SUSPENDED);
                selectionPrice.setDisplayed(Boolean.TRUE);
            } else if (suspensionStatus == SuspensionStatus.SUSPENDED_UNDISPLAY) {
                selectionPrice.setState(SelectionState.SUSPENDED);
                selectionPrice.setDisplayed(Boolean.FALSE);
            }
        }
        channelPrices.getSelection().add(selectionPrice);
    }
}
