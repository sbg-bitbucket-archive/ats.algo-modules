package ats.algo.integration;

import static ats.algo.core.markets.ATSMarketTypeConverter.startsWithALetter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import ats.algo.core.markets.ATSMarketTypeConverter;
import ats.algo.core.markets.MarketCategory;
import ats.algo.core.markets.MarketTypeDescriptor;
import ats.algo.core.markets.ResultedMarket;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.integration.AtsMarketTypeMapper.AtsMarketType;
import ats.betting.betsync.dto.FeedBetsyncDto;
import ats.core.AtsBean;
import ats.core.util.CollectionsUtil;
import generated.ats.betsync.dto.EventTradingState;
import generated.ats.betsync.dto.EventTradingStateUpdate;
import generated.ats.betsync.dto.MarketResult;
import generated.ats.betsync.dto.MarketResults;
import generated.ats.betsync.dto.ResultType;
import generated.ats.betsync.dto.SelectionResult;

public class ResultedMarketsHelper extends AtsBean {

    private AtsMarketTypeMapper atsMarketTypeMapper;

    public ResultedMarketsHelper(AtsMarketTypeMapper atsMarketTypeMapper) {
        this.atsMarketTypeMapper = atsMarketTypeMapper;
    }


    public boolean addMarketResults(FeedBetsyncDto feedBetsyncDto, TradedEvent tradedEvent,
                    ResultedMarkets resultedMarkets) {

        EventTradingStateUpdate eventTradingStateUpdate = tradedEvent.createEventTradingStateUpdate();
        EventTradingState eventTradingState = eventTradingStateUpdate.getEventTradingState();

        MarketResults marketResults = new MarketResults();
        List<MarketResult> marketResultsList = marketResults.getMarket();



        for (ResultedMarket rm : resultedMarkets) {

            MarketCategory category = rm.getDescriptor().getCategory();

            MarketResult atsMkt = createAtsMarketResult(tradedEvent, rm, resultedMarkets.getIncidentId());

            if (atsMkt != null) {
                marketResultsList.add(atsMkt);
            }

            if (category != MarketCategory.GENERAL) {
                List<MarketTypeDescriptor> subtypes =
                                tradedEvent.getSubtypesForSequence(rm.getType(), rm.getSequenceId());
                for (MarketTypeDescriptor mtd : subtypes) {
                    if (!Objects.equals(rm.getLineId(), mtd.getSubType())) {
                        ResultedMarket resultedMarketForSubType = rm.getResultedMarketForSubType(mtd.getSubType());
                        if (resultedMarketForSubType != null) {
                            atsMkt = createAtsMarketResult(tradedEvent, resultedMarketForSubType,
                                            resultedMarkets.getIncidentId());
                            if (atsMkt != null) {
                                info("Adding other %s %s line results for %s", rm.getType(), rm.getSequenceId(),
                                                mtd.getSubType());
                                marketResultsList.add(atsMkt);
                            } else {
                                if (!tradedEvent.hasResulted(resultedMarketForSubType)) {
                                    warn("No Ats market other %s %s line %s result returned for %s",
                                                    resultedMarketForSubType.getType(),
                                                    resultedMarketForSubType.getSequenceId(),
                                                    resultedMarketForSubType.getLineId(), mtd.getSubType());
                                } else {
                                    trace("Ats market other %s %s line %s already resulted for %s",
                                                    resultedMarketForSubType.getType(),
                                                    resultedMarketForSubType.getSequenceId(),
                                                    resultedMarketForSubType.getLineId(), mtd.getSubType());
                                }
                            }
                        }
                    }
                }
            }
        }

        boolean newResults = !marketResultsList.isEmpty();

        if (newResults) {
            eventTradingState.setResults(marketResults);
            eventTradingStateUpdate.setEventTradingState(eventTradingState);
            feedBetsyncDto.setEventTradingStateUpdate(eventTradingStateUpdate);
        }

        return newResults;
    }

    private void addAtsSelectionResult(MarketResult marketResults, String type, ResultType result,
                    List<String> scores) {
        SelectionResult selectionResult = new SelectionResult();
        selectionResult.setType(type);
        selectionResult.setResult(result);
        if (scores != null) {
            @SuppressWarnings("unchecked")
            ArrayList<String> temp = (ArrayList<String>) ((ArrayList<String>) scores).clone();
            if (type.contains("AH")) {
                selectionResult.setHandicapScore(temp);
            } else if (type.contains("BH")) {
                selectionResult.setHandicapScore(temp);
            }
        }
        marketResults.getSelection().add(selectionResult);
    }

    private MarketResult createAtsMarketResult(TradedEvent tradedEvent, ResultedMarket rm,
                    String incidentThatTriggeredResultsId) {
        MarketResult atsMkt = null;
        try {
            boolean partial = !rm.isFullyResulted();
            if (!partial) {
                boolean newResultedMarket = tradedEvent.addResultedMarket(rm, incidentThatTriggeredResultsId);
                if (!newResultedMarket) {
                    return null;
                }
            }

            String atsMarketType = tradedEvent.toATSMarketType(rm);
            String atsSubType = ATSMarketTypeConverter.getATSSubType(rm);

            boolean resolveToLegacyAtsTypes = atsMarketTypeMapper.canResolveToLegacyAtsMarketType(tradedEvent);
            boolean useNewStructureForMarkets = atsMarketTypeMapper.useNewStructureForMarkets(tradedEvent);

            MarketCategory category = rm.getDescriptor().getCategory();

            if (resolveToLegacyAtsTypes && !useNewStructureForMarkets) {

                AtsMarketType marketType = atsMarketTypeMapper.resolveAtsMarketType(tradedEvent, rm);
                if (marketType != null) {
                    atsMarketType = marketType.getMarketType();
                    atsSubType = marketType.getMarketSubtype();
                } else {
                    resolveToLegacyAtsTypes = false;
                }

                if (category != MarketCategory.GENERAL && startsWithALetter(atsSubType)) {
                    atsSubType = rm.getLineId();
                }
            }

            List<String> winningSelections = rm.getWinningSelections();
            String winningSelection = CollectionsUtil.hasContent(winningSelections) ? winningSelections.get(0) : null;

            atsMkt = new MarketResult();
            atsMkt.setType(atsMarketType);
            atsMkt.setSubtype(atsSubType);
            atsMkt.setAlgoKey(rm.getDescriptor().toToken());


            boolean voided = rm.isMarketVoided()
                            || (winningSelection != null && winningSelection.equalsIgnoreCase("void"));
            int resulted = 0;

            resulted += addAtsResults(resolveToLegacyAtsTypes, atsMkt, ResultType.LOSE, rm.getLosingSelections(),
                            rm.getScores());
            resulted += addAtsResults(resolveToLegacyAtsTypes, atsMkt, ResultType.HALF_LOSE, rm.getHalfLost(),
                            rm.getScores());
            resulted += addAtsResults(resolveToLegacyAtsTypes, atsMkt, ResultType.HALF_WIN, rm.getHalfWon(),
                            rm.getScores());
            resulted += addAtsResults(resolveToLegacyAtsTypes, atsMkt, ResultType.VOID, rm.getStakeBack(),
                            rm.getScores());

            if (partial) {
                atsMkt.setPartial(Boolean.TRUE);
            }

            if (resulted == 0 && category != MarketCategory.GENERAL) {
                atsMkt.setResultLine(String.valueOf(rm.getActualOutcome()));

            } else if (resulted > 0 || category != MarketCategory.GENERAL) {
                debug("Not using %s line to result market in Ats - using half line selection results",
                                rm.getActualOutcome());
            }

            if (winningSelection != null && !voided) {
                addAtsResults(resolveToLegacyAtsTypes, atsMkt, ResultType.WIN, winningSelections, rm.getScores());
            } else if (voided) {
                info("Check if tradedEvent %s has correct market selection types %s ", tradedEvent.getEventId(),
                                rm.getFullKey() + "-" + rm.getType());
                Set<String> marketSelectionTypes = tradedEvent.getMarketSelectionTypes(rm.getFullKey());
                debug("Voiding %s selections in %s,", marketSelectionTypes, rm);
                if (marketSelectionTypes != null && !marketSelectionTypes.isEmpty()) {
                    for (String selectionType : marketSelectionTypes) {
                        if (resolveToLegacyAtsTypes) {
                            selectionType = atsMarketTypeMapper
                                            .resolveAtsSelectionType(atsMkt.getType() + selectionType);
                        }
                        boolean loseSelection = false;
                        if (rm.getLosingSelections() != null) {
                            for (String losingSelection : rm.getLosingSelections()) {
                                if (selectionType.equals(losingSelection)) {
                                    debug("Lose selections in %s, with SelectionType %s", rm.getMarketDescription(),
                                                    selectionType);
                                    addAtsSelectionResult(atsMkt, selectionType, ResultType.LOSE, rm.getScores());
                                    loseSelection = true;
                                }
                            }
                        }

                        if (!loseSelection) {
                            debug("Voiding selections in %s, with SelectionType %s", rm.getMarketDescription(),
                                            selectionType);
                            addAtsSelectionResult(atsMkt, selectionType, ResultType.VOID, rm.getScores());
                        }
                    }
                } else {
                    warn("%s could not determine selection types for market type in order to void selection results !",
                                    rm);
                    // Temp fix for adding back the
                    addAtsSelectionResult(atsMkt, "", ResultType.VOID, rm.getScores());
                    warn("current out resulted market is %s", atsMkt);
                    return atsMkt;
                }
            } else if (resulted == 0) {
                warn("%s not void nor has a winning selection in  results !", rm);
                return null;
            }
        } catch (Exception ex) {
            error("Problem resulting market %s", ex, rm);
            return null;
        }
        return atsMkt;
    }


    private int addAtsResults(boolean resolveToLegacyAtsTypes, MarketResult atsMkt, ResultType result,
                    List<String> selectionTypesForThatResult, List<String> scores) {
        int resulted = 0;
        if (CollectionsUtil.hasContent(selectionTypesForThatResult)) {
            for (String algoSelection : selectionTypesForThatResult) {
                if (resolveToLegacyAtsTypes) {
                    algoSelection = atsMarketTypeMapper.resolveAtsSelectionType(algoSelection);
                }
                addAtsSelectionResult(atsMkt, algoSelection, result, scores);
                ++resulted;
            }
        }
        return resulted;
    }
}
