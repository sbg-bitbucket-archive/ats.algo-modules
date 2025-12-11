package ats.algo.matchrunner.marketscollector;

import java.util.HashSet;
import java.util.Set;

import ats.algo.core.markets.Market;

public class CollectedMarket {
    private String type;
    private String description;
    private String seqId;
    private String lineId;
    private Set<String> selections;

    public CollectedMarket(Market market) {
        this.type = market.getType();
        this.description = market.getMarketDescription();
        this.seqId = market.getSequenceId();
        this.lineId = market.getLineId();
        selections = new HashSet<String>();
        addSelections(market);
    }

    public void addSelections(Market market) {
        for (String selection : market.getSelectionsProbs().keySet())
            selections.add(selection);

    }

    public String toCsvString() {
        StringBuilder b = new StringBuilder();
        for (String selection : selections) {
            b.append(type).append(",").append("\"").append(description).append("\"").append(",").append("\"")
                            .append(seqId).append("\"").append(",").append("\"").append(lineId).append("\"").append(",")
                            .append("\"").append(selection).append("\"").append("\n");
        }
        return b.toString();
    }

    public static String csvHeader() {
        StringBuilder b = new StringBuilder();
        b.append("type").append(",").append("description").append(",").append("seqId").append(",").append("lineId")
                        .append(",").append("selection").append("\n");
        return b.toString();
    }


}
