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
                    int newMarketsToBetsyncBatchSize, Consumer<FeedBetsyncDto> batchConsumer) {
        MatchDetails matchDetails = tradedEvent.getMatchDetails();
        EventTreeCreation eventTreeCreation =
                        generateEventTreeCreation(tradedEvent.getEventId(), matchDetails, markets);
        Event event = eventTreeCreation.getEvent().get(0);
        int newMarkets = 0;
        for (Market market : markets) {

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

            if (!market.getCategory().equals(MarketCategory.GENERAL)) {
                atsMkt.setLine(market.getLineId());
                String subType = atsMkt.getSubtype();
                atsMkt.setSubtype(subType.split("#")[0]);
            }

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

    public Markets alreadyCreatedMarkets(TradedEvent tradedEvent, Markets markets) {
        Markets alreadyCreatedmarkets = new Markets();
        for (Market market : markets) {
            if (tradedEvent.hasMarketAlreadyBeenCreated(market)) {
                alreadyCreatedmarkets.addMarketWithFullKey(market);
            }
        }

        if (alreadyCreatedmarkets.size() < 1) {
            return null;
        }

        return alreadyCreatedmarkets;
    }

    public Markets newMarkets(TradedEvent tradedEvent, Markets markets) {
        Markets newMarkets = new Markets();
        for (Market market : markets) {
            if (!tradedEvent.hasMarketAlreadyBeenCreated(market)) {
                newMarkets.addMarketWithFullKey(market);
            }
        }

        if (newMarkets.size() < 1) {
            return null;
        }

        return newMarkets;
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
            assignParticipantName(tradedEvent, selection);
            selections.add(selection);
        }
        return selections;
    }


    private void assignParticipantName(TradedEvent tradedEvent, Selection atsSeln) {

        try {
            String newSelTeamA = "";
            String newSelTeamB = "";
            Participant participantA = tradedEvent.getParticipantA();
            Participant participantB = tradedEvent.getParticipantB();
            boolean updatedSelection = false;
            debug("Selection = %s. Participant A = %s. Participant B = %s", atsSeln.getName(), participantA.getName(),
                            participantB.getName());
            if (atsSeln.getName().contains("Team A") && !updatedSelection) {
                newSelTeamA = atsSeln.getName().replace("Team A", participantA.getName());
                atsSeln.setName(newSelTeamA);
                assignCompetitorAttribute(atsSeln, participantA);
                updatedSelection = true;
            }
            if (atsSeln.getName().contains("Team B") && !updatedSelection) {
                newSelTeamB = atsSeln.getName().replace("Team B", participantB.getName());
                atsSeln.setName(newSelTeamB);
                assignCompetitorAttribute(atsSeln, participantB);
                updatedSelection = true;

            }
            if (atsSeln.getName().contains("Player A") && !updatedSelection) {
                newSelTeamA = atsSeln.getName().replace("Player A", participantA.getName());
                atsSeln.setName(newSelTeamA);
                assignCompetitorAttribute(atsSeln, participantA);
                updatedSelection = true;
            }
            if (atsSeln.getName().contains("Player B") && !updatedSelection) {
                newSelTeamB = atsSeln.getName().replace("Player B", participantB.getName());
                atsSeln.setName(newSelTeamB);
                assignCompetitorAttribute(atsSeln, participantB);
                updatedSelection = true;
            }
            if ((atsSeln.getName().matches("A .*-.*") || atsSeln.getName().matches("A .*+")) && !updatedSelection) {
                newSelTeamA = atsSeln.getName().replace("A", participantA.getName());
                atsSeln.setName(newSelTeamA);
                assignCompetitorAttribute(atsSeln, participantA);
                updatedSelection = true;
            }
            if ((atsSeln.getName().matches("B .*-.*") || atsSeln.getName().matches("B .*+")) && !updatedSelection) {
                newSelTeamB = atsSeln.getName().replace("B", participantB.getName());
                atsSeln.setName(newSelTeamB);
                assignCompetitorAttribute(atsSeln, participantB);
                updatedSelection = true;
            }
            if (atsSeln.getName().contains("after extra points A") && !updatedSelection) {
                newSelTeamA = atsSeln.getName().replace("A", participantA.getName());
                atsSeln.setName(newSelTeamA);
                assignCompetitorAttribute(atsSeln, participantA);
                updatedSelection = true;
            }
            if (atsSeln.getName().contains("after extra points B") && !updatedSelection) {
                newSelTeamB = atsSeln.getName().replace("B", participantB.getName());
                atsSeln.setName(newSelTeamB);
                assignCompetitorAttribute(atsSeln, participantB);
                updatedSelection = true;
            }
            if (atsSeln.getName().contains("{HomeTeam}") && !updatedSelection) {
                newSelTeamA = atsSeln.getName().replace("{HomeTeam}", participantA.getName());
                atsSeln.setName(newSelTeamA);
                assignCompetitorAttribute(atsSeln, participantA);
                updatedSelection = true;
            }
            if (atsSeln.getName().contains("{AwayTeam}") && !updatedSelection) {
                newSelTeamB = atsSeln.getName().replace("{AwayTeam}", participantB.getName());
                atsSeln.setName(newSelTeamB);
                assignCompetitorAttribute(atsSeln, participantB);
                updatedSelection = true;
            }
            if (atsSeln.getName().contains("HomeTeam") && !updatedSelection) {
                newSelTeamA = atsSeln.getName().replace("HomeTeam", participantA.getName());
                atsSeln.setName(newSelTeamA);
                assignCompetitorAttribute(atsSeln, participantA);
                updatedSelection = true;
            }
            if (atsSeln.getName().contains("AwayTeam") && !updatedSelection) {
                newSelTeamB = atsSeln.getName().replace("AwayTeam", participantB.getName());
                atsSeln.setName(newSelTeamB);
                assignCompetitorAttribute(atsSeln, participantB);
                updatedSelection = true;
            }
            if (atsSeln.getName().equals("AH") && !updatedSelection) {
                newSelTeamA = atsSeln.getName().replace("AH", participantA.getName());
                atsSeln.setName(newSelTeamA);
                assignCompetitorAttribute(atsSeln, participantA);
                updatedSelection = true;
            }
            if (atsSeln.getName().equals("BH") && !updatedSelection) {
                newSelTeamB = atsSeln.getName().replace("BH", participantB.getName());
                atsSeln.setName(newSelTeamB);
                assignCompetitorAttribute(atsSeln, participantB);
                updatedSelection = true;
            }
            if (atsSeln.getName().contains("playerA") && !updatedSelection) {
                newSelTeamA = atsSeln.getName().replace("playerA", participantA.getName());
                atsSeln.setName(newSelTeamA);
                assignCompetitorAttribute(atsSeln, participantA);
                updatedSelection = true;
            }
            if (atsSeln.getName().contains("playerB") && !updatedSelection) {
                newSelTeamB = atsSeln.getName().replace("playerB", participantB.getName());
                atsSeln.setName(newSelTeamB);
                assignCompetitorAttribute(atsSeln, participantB);
                updatedSelection = true;
            }
            if (atsSeln.getName().equals("A") && !updatedSelection) {
                newSelTeamA = atsSeln.getName().replace("A", participantA.getName());
                atsSeln.setName(newSelTeamA);
                assignCompetitorAttribute(atsSeln, participantA);
                updatedSelection = true;
            }
            if (atsSeln.getName().equals("B") && !updatedSelection) {
                newSelTeamB = atsSeln.getName().replace("B", participantB.getName());
                atsSeln.setName(newSelTeamB);
                assignCompetitorAttribute(atsSeln, participantB);
                updatedSelection = true;
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

    public static void main(String[] args) {
        Participant participant1 = new Participant();
        participant1.setName("A Astakhova");
        Participant participant2 = new Participant();
        participant2.setName("Bethanie Mattek-Sands");
        Selection selection = new Selection();
        selection.setName("Player A wins the match and both players win a set");
        String newSel = assignParticipantNameTest(participant1, participant2, selection);
        System.out.println(newSel);

        selection.setName("Player A 7-0");
        newSel = assignParticipantNameTest(participant1, participant2, selection);
        System.out.println(newSel);

        selection.setName("A 2-1");
        newSel = assignParticipantNameTest(participant1, participant2, selection);
        System.out.println(newSel);
    }

    private static String assignParticipantNameTest(Participant A, Participant B, Selection atsSeln) {

        String newSelTeamA = "";
        String newSelTeamB = "";
        Participant participantA = A;
        Participant participantB = B;
        boolean updatedSelection = false;
        System.out.println("Selection = " + atsSeln.getName() + ". Participant A = " + participantA.getName()
                        + ". Participant B = " + participantB.getName());
        if (atsSeln.getName().contains("Team A") && !updatedSelection) {
            newSelTeamA = atsSeln.getName().replace("Team A", participantA.getName());
            atsSeln.setName(newSelTeamA);
            updatedSelection = true;
        }
        if (atsSeln.getName().contains("Team B") && !updatedSelection) {
            newSelTeamB = atsSeln.getName().replace("Team B", participantB.getName());
            atsSeln.setName(newSelTeamB);
            updatedSelection = true;
        }
        if (atsSeln.getName().contains("Player A") && !updatedSelection) {
            newSelTeamA = atsSeln.getName().replace("Player A", participantA.getName());
            atsSeln.setName(newSelTeamA);
            updatedSelection = true;
        }
        if (atsSeln.getName().contains("Player B") && !updatedSelection) {
            newSelTeamB = atsSeln.getName().replace("Player B", participantB.getName());
            atsSeln.setName(newSelTeamB);
            updatedSelection = true;
        }
        if ((atsSeln.getName().matches("A .*-.*") || atsSeln.getName().matches("A .*+")) && !updatedSelection) {
            newSelTeamA = atsSeln.getName().replace("A", participantA.getName());
            atsSeln.setName(newSelTeamA);
            updatedSelection = true;
        }
        if ((atsSeln.getName().matches("B .*-.*") || atsSeln.getName().matches("B .*+")) && !updatedSelection) {
            newSelTeamB = atsSeln.getName().replace("B", participantB.getName());
            atsSeln.setName(newSelTeamB);
            updatedSelection = true;
        }
        if (atsSeln.getName().contains("after extra points A") && !updatedSelection) {
            newSelTeamA = atsSeln.getName().replace("A", participantA.getName());
            atsSeln.setName(newSelTeamA);
            updatedSelection = true;
        }
        if (atsSeln.getName().contains("after extra points B") && !updatedSelection) {
            newSelTeamB = atsSeln.getName().replace("B", participantB.getName());
            atsSeln.setName(newSelTeamB);
            updatedSelection = true;
        }
        if (atsSeln.getName().contains("{HomeTeam}") && !updatedSelection) {
            newSelTeamA = atsSeln.getName().replace("{HomeTeam}", participantA.getName());
            atsSeln.setName(newSelTeamA);
            updatedSelection = true;
        }
        if (atsSeln.getName().contains("{AwayTeam}") && !updatedSelection) {
            newSelTeamB = atsSeln.getName().replace("{AwayTeam}", participantB.getName());
            atsSeln.setName(newSelTeamB);
            updatedSelection = true;
        }
        if (atsSeln.getName().contains("HomeTeam") && !updatedSelection) {
            newSelTeamA = atsSeln.getName().replace("HomeTeam", participantA.getName());
            atsSeln.setName(newSelTeamA);
            updatedSelection = true;
        }
        if (atsSeln.getName().contains("AwayTeam") && !updatedSelection) {
            newSelTeamB = atsSeln.getName().replace("AwayTeam", participantB.getName());
            atsSeln.setName(newSelTeamB);
            updatedSelection = true;
        }
        if (atsSeln.getName().equals("AH") && !updatedSelection) {
            newSelTeamA = atsSeln.getName().replace("AH", participantA.getName());
            atsSeln.setName(newSelTeamA);
            updatedSelection = true;
        }
        if (atsSeln.getName().equals("BH") && !updatedSelection) {
            newSelTeamB = atsSeln.getName().replace("BH", participantB.getName());
            atsSeln.setName(newSelTeamB);
            updatedSelection = true;
        }
        if (atsSeln.getName().contains("playerA") && !updatedSelection) {
            newSelTeamA = atsSeln.getName().replace("playerA", participantA.getName());
            atsSeln.setName(newSelTeamA);
            updatedSelection = true;
        }
        if (atsSeln.getName().contains("playerB") && !updatedSelection) {
            newSelTeamB = atsSeln.getName().replace("playerB", participantB.getName());
            atsSeln.setName(newSelTeamB);
            updatedSelection = true;
        }
        if (atsSeln.getName().equals("A") && !updatedSelection) {
            newSelTeamA = atsSeln.getName().replace("A", participantA.getName());
            atsSeln.setName(newSelTeamA);
            updatedSelection = true;
        }
        if (atsSeln.getName().equals("B") && !updatedSelection) {
            newSelTeamB = atsSeln.getName().replace("B", participantB.getName());
            atsSeln.setName(newSelTeamB);
            updatedSelection = true;
        }

        System.out.println("NEW AtsSeln Name = " + atsSeln.getName());
        return atsSeln.getName();
    }
}
