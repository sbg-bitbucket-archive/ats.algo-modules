package ats.algo.integration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Consumer;

import com.google.common.collect.Lists;

import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketCategory;
import ats.algo.core.markets.MarketTypeDescriptor;
import ats.algo.core.markets.Markets;
import ats.algo.core.timestamp.TimeStamp;
import ats.betting.betsync.dto.FeedBetsyncDto;
import ats.core.AtsBean;
import generated.ats.betsync.dto.Attribute;
import generated.ats.betsync.dto.Event;
import generated.ats.betsync.dto.EventTreeCreation;
import generated.ats.betsync.dto.MatchDetails;
import generated.ats.betsync.dto.Participant;
import generated.ats.betsync.dto.Selection;

public class MarketCreationHelper extends AtsBean {

    private AtsMarketTypeMapper atsMarketTypeMapper;

    public MarketCreationHelper(AtsMarketTypeMapper atsMarketTypeMapper) {
        this.atsMarketTypeMapper = atsMarketTypeMapper;
    }

    public void generateNewBetsyncMarkets(TradedEvent tradedEvent, Markets markets, TimeStamp timeStamp,
                    int newMarketsToBetsyncBatchSize, Consumer<FeedBetsyncDto> batchConsumer, boolean resetEvent) {
        MatchDetails matchDetails = tradedEvent.getMatchDetails();
        EventTreeCreation eventTreeCreation =
                        generateEventTreeCreation(tradedEvent.getEventId(), matchDetails, markets);
        Event event = eventTreeCreation.getEvent().get(0);
        int newMarkets = 0;
        for (Market market : markets) {
            if (tradedEvent.hasMarketAlreadyBeenCreated(market) && !resetEvent) {
                continue;
            }

            debug("Market Creation Algo Market = " + market);

            generated.ats.betsync.dto.Market atsMkt = new generated.ats.betsync.dto.Market();
            atsMkt.setName(generateMarketName(tradedEvent, market));
            atsMkt.setAlgoKey(market.getDescriptor().toToken());

            boolean canResolveToLegacyAtsMarketType = atsMarketTypeMapper.canResolveToLegacyAtsMarketType(tradedEvent);
            boolean useNewStructureForMarkets = atsMarketTypeMapper.useNewStructureForMarkets(tradedEvent);

            atsMkt.setType(atsMarketTypeMapper.resolveAtsMarketType(tradedEvent, market,
                            canResolveToLegacyAtsMarketType));
            atsMkt.setSubtype(atsMarketTypeMapper.resolveAtsMarketSubType(tradedEvent, market,
                            canResolveToLegacyAtsMarketType, useNewStructureForMarkets));

            atsMkt.getSelection().addAll(
                            generateSelections(tradedEvent, market, atsMkt.getType(), canResolveToLegacyAtsMarketType));

            // if (!market.getCategory().equals(MarketCategory.GENERAL)) {
            // atsMkt.setLine(market.getLineId());
            // String subType = atsMkt.getSubtype();
            // atsMkt.setSubtype(subType.split("#")[0]);
            // Attribute attribute = new Attribute();
            // attribute.setName("marketClass");
            // attribute.setValue("MOVING");
            // atsMkt.getAttributes().add(attribute);
            // }
            debug("Algo Market = " + market);
            debug("Ats Market = " + atsMkt.toString());

            event.getMarket().add(atsMkt);

            newMarkets++;

            if (newMarkets == newMarketsToBetsyncBatchSize) {
                forwardNewMarketsBatch(eventTreeCreation, timeStamp, batchConsumer);
                newMarkets = 0;
                eventTreeCreation = generateEventTreeCreation(tradedEvent.getEventId(), matchDetails, markets);
                event = eventTreeCreation.getEvent().get(0);
            }
        }

        tradedEvent.setMarkets(markets);

        if (newMarkets > 0) {
            forwardNewMarketsBatch(eventTreeCreation, timeStamp, batchConsumer);
        }
    }

    protected void forwardNewMarketsBatch(EventTreeCreation eventTreeCreation, TimeStamp timeStamp,
                    Consumer<FeedBetsyncDto> batchConsumer) {
        if (timeStamp != null && timeStamp.getTimeNewMarketsSentToBetsyc() == 0) {
            timeStamp.setTimeNewMarketsSentToBetsyc(System.currentTimeMillis());
        }
        FeedBetsyncDto feedBetsyncDto = new FeedBetsyncDto();
        feedBetsyncDto.setEventTreeCreation(eventTreeCreation);
        Event event = eventTreeCreation.getEvent().get(0);
        debug("%s new markets in event %s", event.getMarket().size(), event.getId());
        batchConsumer.accept(feedBetsyncDto);
    }

    private EventTreeCreation generateEventTreeCreation(long eventId, MatchDetails matchDetails, Markets markets) {
        EventTreeCreation eventTreeCreation = new EventTreeCreation();
        eventTreeCreation.setSystemcode(IntegrationConstants.ALGO_MGR_SYSTEM_CODE);
        Event event = new Event();
        event.setId(eventId);
        event.setSportcode(matchDetails.getSportcode());
        eventTreeCreation.setEvent(Lists.newArrayList(event));
        return eventTreeCreation;
    }

    private String generateMarketName(TradedEvent tradedEvent, Market market) {
        String marketDescription = market.getMarketDescription();
        MarketTypeDescriptor descriptor = market.getDescriptor();
        if (shouldAddLine(market)) {
            String lineId = market.getLineId();
            if (lineId == null || lineId.isEmpty()) {
                warn("Missing lineId for %s %s", marketDescription, descriptor);
            } else if (!marketDescription.endsWith(lineId)) {
                marketDescription += " " + lineId;
            }
        }
        Participant participant = null;
        if (marketDescription.contains("Player A")) {
            participant = tradedEvent.getParticipantA();
            marketDescription.replace("Player A", participant.getName());

        }
        if (marketDescription.contains("Player B")) {
            participant = tradedEvent.getParticipantB();
            marketDescription.replace("Player B", participant.getName());
        }
        if (marketDescription.contains("{HomeTeam}")) {
            participant = tradedEvent.getParticipantA();
            marketDescription.replace("{HomeTeam}", participant.getName());
        }
        if (marketDescription.contains("{AwayTeam}")) {
            participant = tradedEvent.getParticipantA();
            marketDescription.replace("{AwayTeam}", participant.getName());
        }

        return marketDescription;
    }

    private boolean shouldAddLine(Market market) {
        MarketCategory category = market.getCategory();
        return (category == MarketCategory.OVUN || category == MarketCategory.HCAP || category == MarketCategory.HCAPEU
                        || category == MarketCategory.OVUN_EU);
    }

    private List<Selection> generateSelections(TradedEvent tradedEvent, Market market, String type,
                    boolean canResolveToLegacyAtsMarketType) {
        List<Selection> selections = new ArrayList<>();
        for (Entry<String, Double> selnEntry : market.getSelectionsProbs().entrySet()) {
            Selection selection = null;
            if (canResolveToLegacyAtsMarketType) {
                selection = atsMarketTypeMapper.resolveAtsSelectionType(selnEntry);
            } else {
                selection = new Selection();
                selection.setName(selnEntry.getKey());
            }
            String selectionType = selection.getType();
            if (null == selectionType || selectionType.isEmpty()) {
                selection.setType(selnEntry.getKey());
            }
            assignParticipantName(tradedEvent, market, selection);
            selections.add(selection);
        }
        return selections;
    }


    private void assignParticipantName(TradedEvent tradedEvent, Market market, Selection atsSeln) {

        try {
            String newSelTeamA = "";
            String newSelTeamB = "";
            Participant participantA = tradedEvent.getParticipantA();
            Participant participantB = tradedEvent.getParticipantB();
            debug("Selection = %s. Participant A = %s. Participant B = %s", atsSeln.getName(), participantA.getName(),
                            participantB.getName());
            if (atsSeln.getName().contains("Team A")) {
                newSelTeamA = atsSeln.getName().replace("Team A", participantA.getName());
                atsSeln.setName(newSelTeamA);
                assignCompetitorAttribute(atsSeln, participantA);
            }
            if (atsSeln.getName().contains("Team B")) {
                newSelTeamB = atsSeln.getName().replace("Team B", participantB.getName());
                atsSeln.setName(newSelTeamB);
                assignCompetitorAttribute(atsSeln, participantB);
            }
            if (atsSeln.getName().contains("Player A")) {
                newSelTeamA = atsSeln.getName().replace("Player A", participantA.getName());
                atsSeln.setName(newSelTeamA);
                assignCompetitorAttribute(atsSeln, participantA);
            }
            if (atsSeln.getName().contains("Player B")) {
                newSelTeamB = atsSeln.getName().replace("Player B", participantB.getName());
                atsSeln.setName(newSelTeamB);
                assignCompetitorAttribute(atsSeln, participantB);
            }
            if (atsSeln.getName().matches("A .*-.*") || atsSeln.getName().matches("A .*+")) {
                newSelTeamA = atsSeln.getName().replace("A", participantA.getName());
                atsSeln.setName(newSelTeamA);
                assignCompetitorAttribute(atsSeln, participantA);
            }
            if (atsSeln.getName().matches("B .*-.*") || atsSeln.getName().matches("B .*+")) {
                newSelTeamB = atsSeln.getName().replace("B", participantB.getName());
                atsSeln.setName(newSelTeamB);
                assignCompetitorAttribute(atsSeln, participantB);
            }
            if (atsSeln.getName().contains("after extra points A")) {
                newSelTeamA = atsSeln.getName().replace("A", participantA.getName());
                atsSeln.setName(newSelTeamA);
                assignCompetitorAttribute(atsSeln, participantA);
            }
            if (atsSeln.getName().contains("after extra points B")) {
                newSelTeamB = atsSeln.getName().replace("B", participantB.getName());
                atsSeln.setName(newSelTeamB);
                assignCompetitorAttribute(atsSeln, participantB);
            }
            if (atsSeln.getName().contains("{HomeTeam}")) {
                newSelTeamA = atsSeln.getName().replace("{HomeTeam}", participantA.getName());
                atsSeln.setName(newSelTeamA);
                assignCompetitorAttribute(atsSeln, participantA);
            }
            if (atsSeln.getName().contains("{AwayTeam}")) {
                newSelTeamB = atsSeln.getName().replace("{AwayTeam}", participantB.getName());
                atsSeln.setName(newSelTeamB);
                assignCompetitorAttribute(atsSeln, participantB);
            }
            if (atsSeln.getName().contains("HomeTeam")) {
                newSelTeamA = atsSeln.getName().replace("HomeTeam", participantA.getName());
                atsSeln.setName(newSelTeamA);
                assignCompetitorAttribute(atsSeln, participantA);
            }
            if (atsSeln.getName().contains("AwayTeam")) {
                newSelTeamB = atsSeln.getName().replace("AwayTeam", participantB.getName());
                atsSeln.setName(newSelTeamB);
                assignCompetitorAttribute(atsSeln, participantB);
            }
            if (atsSeln.getName().equals("AH")) {
                newSelTeamA = atsSeln.getName().replace("AH", participantA.getName());
                atsSeln.setName(newSelTeamA);
                assignCompetitorAttribute(atsSeln, participantA);
            }
            if (atsSeln.getName().equals("BH")) {
                newSelTeamB = atsSeln.getName().replace("BH", participantB.getName());
                atsSeln.setName(newSelTeamB);
                assignCompetitorAttribute(atsSeln, participantB);
            }
            if (atsSeln.getName().contains("playerA")) {
                newSelTeamA = atsSeln.getName().replace("playerA", participantA.getName());
                atsSeln.setName(newSelTeamA);
                assignCompetitorAttribute(atsSeln, participantA);
            }
            if (atsSeln.getName().contains("playerB")) {
                newSelTeamB = atsSeln.getName().replace("playerB", participantB.getName());
                atsSeln.setName(newSelTeamB);
                assignCompetitorAttribute(atsSeln, participantB);
            }

            debug("NEW AtsSeln Name = %s", atsSeln.getName());
        } catch (Exception ex) {
            error("unexcepted error found %s" + ex);
        }
    }

    private void assignCompetitorAttribute(Selection atsSeln, Participant participant) {
        Attribute participantIdAttr = new Attribute();
        participantIdAttr.setName("competitorId");
        participantIdAttr.setValue(String.valueOf(participant.getId()));
        atsSeln.getAttributes().add(participantIdAttr);
    }
}
