package ats.algo.core.markets;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ats.algo.core.MarketGroup;
import ats.algo.genericsupportfunctions.GCMath;
import ats.core.util.json.JsonUtil;

/**
 * Holds a set of calculated probabilities and selections for a market
 * 
 * @author Geoff
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Market implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean valid; // set to false if this market is not valid given
                           // current market score - e.g. tie break
                           // winner when not in tie break
    private MarketCategory marketCategory;
    private String type; // the type code for this market e.g "FT:ML" - c.f.
                         // spreadsheet for full list of codes
    private String lineId; // if category = "OVUN" or "HCAP" then the line
                           // number associated with this market in string
                           // format, e.g. "4.5". Otherwise set to "" or null
    private String marketDescription; // text string useful for toString method,
                                      // but not used by ATS
    private String sequenceId;
    private Map<String, Selection> selections;
    private String selectionNameOverOrA;
    private String selectionNameUnderOrB;
    private String selectionNameDrawOrD;
    private MarketGroup marketGroup;
    private MarketStatus marketStatus;
    private DerivedMarketsInfo derivedMarketsInfo;
    private DerivedMarketDetails derivedMarketDetails; // will be null if not a
                                                       // derived market
    private String balancedLineId;
    // FIXME: this field can be removed after BetFair project is done, tempory added to stop certain markets got
    // resulted
    private boolean doNotResultThisMarket = false;
    private boolean filterMarketByClient = true;


    public boolean isDoNotResultThisMarket() {
        return doNotResultThisMarket;
    }

    public void setDoNotResultThisMarket(boolean doNotResultThisMarket) {
        this.doNotResultThisMarket = doNotResultThisMarket;
    }

    public boolean isFilterMarketByClient() {
        return filterMarketByClient;
    }

    public void setFilterMarketByClient(boolean filterMarketByClient) {
        this.filterMarketByClient = filterMarketByClient;
    }

    /**
     * 
     * @param category the category this market belongs to e.g. OVUN, HCAP
     * @param type the type code for this market e.g "FT:ML" - c.f. spreadsheet for full list of codes
     * @param sequenceId for markets related to particular events during the match
     * @param marketDescription text string useful for toString method but not used by ATS
     */
    public Market(MarketCategory category, String type, String sequenceId, String marketDescription) {
        initialiseMarket(category, MarketGroup.NOT_SPECIFIED, type, sequenceId, marketDescription);
    }

    /**
     * Constructor including specification of MarketGroup
     * 
     * @param category
     * @param marketGroup
     * @param type
     * @param sequenceId
     * @param marketDescription
     */
    public Market(MarketCategory category, MarketGroup marketGroup, String type, String sequenceId,
                    String marketDescription) {
        initialiseMarket(category, marketGroup, type, sequenceId, marketDescription);
    }

    private void initialiseMarket(MarketCategory category, MarketGroup marketGroup, String type, String sequenceId,
                    String marketDescription) {
        this.marketCategory = category;
        this.type = type;
        this.sequenceId = sequenceId;
        this.marketDescription = marketDescription;
        this.valid = false;
        this.marketGroup = marketGroup;
        selections = new TreeMap<String, Selection>();
        derivedMarketsInfo = new DerivedMarketsInfo();
        marketStatus = new MarketStatus();
    }

    /*
     * lineBase and lineProbs are used by the getMarketForSubType() method and only apply to markets of type OVUN or
     * HCAP. lineProbs will be null for other categories of market.
     */
    private int lineBase;
    private double[] lineProbs;

    public Map<String, Selection> getSelections() {
        return selections;
    }

    @JsonIgnore
    public SuspensionStatus getSelectionsSuspensionStatus(String selection) {
        if (this.selections.get(selection) != null)
            return this.selections.get(selection).getSuspensionStatus();
        return null;
    }

    /**
     * get the set of selection names and associated probabilities
     * 
     * @return
     */
    @JsonIgnore
    public Map<String, Double> getSelectionsProbs() {
        Map<String, Double> sel = new TreeMap<>();
        selections.entrySet().stream().forEach(x -> sel.put(x.getKey(), x.getValue().getProb()));
        return sel;
    }

    /**
     * set the set of selection names and associated probabilities and suspension status
     * 
     * 
     * @param selections
     */
    public void setSelections(Map<String, Selection> selections) {
        this.selections = selections;
    }

    public void setSelectionStatus(String selectionName, SuspensionStatus suspensionStatus) {
        Selection selection = selections.get(selectionName);
        if (selection != null)
            selection.setSuspensionStatus(suspensionStatus);
    }

    /**
     * Only applies to markets of type OVUN or HCAP. lineBase defines the actual line that lineProbs[0] refers to
     * 
     * @return
     */
    public int getLineBase() {
        return lineBase;
    }

    /**
     * Only applies to markets of type OVUN or HCAP. lineBase defines the actual line that lineProbs[0] refers to
     * 
     * @param lineBase
     */
    public void setLineBase(int lineBase) {
        this.lineBase = lineBase;
    }

    /**
     * Only applies to markets of type OVUN or HCAP. lineProbs[] is an array of probabilities where lineProbs[i] holds
     * the probability of the outcome being <= lineBase+i. For example in for a games handicap market if lineBase = -12
     * then lineProbabilities[17] = P(GAMES A – GAMES B <= 5).
     * 
     * @return
     */
    public double[] getLineProbs() {
        return lineProbs;
    }

    /**
     * Only applies to markets of type OVUN or HCAP. lineProbs[] is an array of probabilities where lineProbs[i] holds
     * the probability of the outcome being <= lineBase+i. For example in for a games handicap market if lineBase = -12
     * then lineProbabilities[17] = P(GAMES A – GAMES B <= 5).
     * 
     * @param lineProbs
     */
    public void setLineProbs(double[] lineProbs) {
        this.lineProbs = lineProbs;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    /**
     * returns the unique key for this market type and sequenceId
     * 
     * @return
     */
    @JsonIgnore
    public String getShortKey() {
        String sequenceId = this.sequenceId;
        if (sequenceId == null)
            sequenceId = "M";
        return this.type + "_" + sequenceId;
    }

    public MarketStatus getMarketStatus() {
        return marketStatus;
    }

    public void setMarketStatus(MarketStatus marketStatus) {
        this.marketStatus = marketStatus;
    }

    /**
     * returns the unique key for this market type, sequenceId and lineId
     * 
     * @return
     */
    @JsonIgnore
    public String getFullKey() {
        String lineId = this.getLineId();
        if (lineId != null && !lineId.isEmpty()) {
            try {
                double vLineId = Double.parseDouble(lineId);
                String lineKeyToken = String.valueOf(vLineId);
                String sequenceId = this.getSequenceId();
                if (sequenceId == null) {
                    sequenceId = "M";
                }
                return this.getType() + "#" + lineKeyToken + "_" + sequenceId;
            } catch (NumberFormatException | NullPointerException e) {
            }
        }
        return getShortKey();
    }

    /**
     * json constructor. Not for normal use
     */
    public Market() {
        /*
         * derivedMarketsInfo object is not carried over Json so create empty one
         */
        derivedMarketsInfo = new DerivedMarketsInfo();

    }

    /**
     * sets the valid flag - if false market may be generated but is not displayed
     * 
     * @param valid
     */
    public void setIsValid(boolean valid) {
        this.valid = valid;
    }

    /**
     * gets the valid flag - if false market may be generated but is not displayed
     * 
     * @return
     */
    public boolean isValid() {
        return valid;
    }

    @JsonIgnore
    public MarketTypeDescriptor getDescriptor() {
        return new MarketTypeDescriptor(marketCategory, type, lineId, sequenceId);
    }

    /**
     * set the string that identifies this market type - e.g. "FT:ML"
     * 
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * get the string that identifies this market type - e.g. "FT:ML"
     */
    public String getType() {
        return type;
    }

    /**
     * sets the free text meaningful description of what this market is e.g. "Next game winner (set 1 game3)"
     */
    public void setMarketDescription(String description) {
        this.marketDescription = description;
    }

    /**
     * gets the free text meaningful description of what this market is e.g. "Next game winner (set 1 game3)"
     */
    public String getMarketDescription() {
        return marketDescription;
    }

    /**
     * sets the market category - e.g. 'OVUN'
     * 
     * @param category
     */
    public void setCategory(MarketCategory category) {
        this.marketCategory = category;
    }

    /**
     * gets the market category - e.g. 'OVUN'
     * 
     * @param marketCategory
     */
    public MarketCategory getCategory() {
        return marketCategory;
    }

    /**
     * sets the line Id
     * 
     * @param lineId
     */
    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    /**
     * gets the line Id
     * 
     * @param lineId
     */
    public String getLineId() {
        return lineId;
    }

    /**
     * Sets the group this market belongs to. May be used by trading rules and as a hint by param finding
     * 
     * @param marketGroup
     */
    public void setMarketGroup(MarketGroup marketGroup) {
        this.marketGroup = marketGroup;
    }

    /**
     * Gets the group this market belongs to. May be used by trading rules and as a hint by param finding
     * 
     * @return
     */
    public MarketGroup getMarketGroup() {
        return marketGroup;
    }

    /**
     * Gets the status of this market. The properties of the status object are generally set by the application of
     * trading rules
     * 
     * @return
     */
    @JsonIgnore
    public DerivedMarketsInfo getDerivedMarketsInfo() {
        return derivedMarketsInfo;
    }

    /**
     * Sets the status of this market. The properties of the status object are generally set by the application of
     * trading rules
     * 
     * @param marketStatus
     */
    @JsonIgnore
    public void setDerivedMarketsInfo(DerivedMarketsInfo marketStatus) {
        this.derivedMarketsInfo = marketStatus;
    }

    @JsonIgnore
    public DerivedMarketDetails getDerivedMarketDetails() {
        return derivedMarketDetails;
    }

    @JsonIgnore
    public void setDerivedMarketDetails(DerivedMarketDetails derivedMarketDetails) {
        this.derivedMarketDetails = derivedMarketDetails;
    }

    /**
     * For ATS integration only. ATS does not have the concept of a sequence id, so this method must combine the
     * {@link #getLineId() subType} with {@link #getSequenceId() sequenceId} to create a unique sub type in ATS.
     */
    @JsonIgnore
    public String getATSSubType() {
        if (lineId == null || lineId.length() == 0) {
            return sequenceId == null ? null : sequenceId;
        } else {
            return sequenceId == null ? lineId : sequenceId + "#" + lineId;
        }
    }

    /**
     * gets the sequenceId associated with the market. the combination of sequenceId and type uniquely identifies a
     * particular market instance. e.g. type = "G:ML" and sequenceId = "S2.3" identifies the set 2 game 3 game winner
     * market
     * 
     * @return
     */
    public String getSequenceId() {
        return sequenceId;
    }

    /**
     * sets the sequenceId associated with the market. the combination of sequenceId and type uniquely identifies a
     * particular market instance. e.g. type = "G:ML" and sequenceId = "S2.3" identifies the set 2 game 3 game winner
     * market
     * 
     * @param sequenceId
     */
    public void setSequenceId(String sequenceId) {
        this.sequenceId = sequenceId;
    }

    /**
     * updates the suspension status for this market if supplied status has higher priority than current status
     * 
     * 
     * @param ruleName
     * @param status
     * @param reason
     */
    public void setSuspensionStatus(String ruleName, SuspensionStatus newStatus, String reason) {
        SuspensionStatus currentStatus = marketStatus.getSuspensionStatus();
        if (newStatus.isHigherPriorityThan(currentStatus)) {
            marketStatus.setSuspensionStatusRuleName(ruleName);
            marketStatus.setSuspensionStatus(newStatus);
            marketStatus.setSuspensionStatusReason(reason);
        }
    }

    /**
     * generates a new market instance with probabilities associated with the specified line
     * 
     * @param lineIdStr - the required line - e.g. "4.5"
     * @return
     */
    Market generateRegularMarketForLineId(String lineIdStr) {
        boolean validMarketType = this.valid
                        && (this.marketCategory == MarketCategory.HCAP || this.marketCategory == MarketCategory.OVUN);
        if (!validMarketType)
            return null;
        Market market = new Market(this.getCategory(), this.getMarketGroup(), this.getType(), this.getSequenceId(),
                        this.getMarketDescription());
        market.setIsValid(true);
        market.setLineBase(this.lineBase);
        market.setLineProbs(this.lineProbs);
        market.setLineId(lineIdStr);
        market.setDerivedMarketsInfo(this.derivedMarketsInfo); // new market
                                                               // points to
                                                               // same
        // status object as this one
        /*
         * use the previously stored trimmedBase and trimmedProbs[] to calculate the prob
         */
        int lineIdentifier = MarketUtilityFunctions.convertLineIdStringToLineIndex(lineIdStr);
        int index;
        switch (this.marketCategory) {
            case OVUN:
                index = lineIdentifier - this.lineBase;
                break;
            case HCAP:
                lineIdentifier = (int) GCMath.round(Double.valueOf(lineIdStr), 0);
                index = -lineIdentifier - this.lineBase;
                break;
            default:
                throw new IllegalArgumentException("Unexpected market category");
        }
        double p;

        if (index < 0)
            p = 0;
        else if (index > lineProbs.length - 1)
            p = 1;
        else
            p = lineProbs[index];
        if (this.marketCategory == MarketCategory.OVUN) {
            market.setSelectionNameOverOrA("Over " + lineIdStr);
            market.setSelectionNameUnderOrB("Under " + lineIdStr);
            market.put("Over", 1 - p);
            market.put("Under", p);
        } else if (this.marketCategory == MarketCategory.HCAP) {
            double line = Double.valueOf(lineIdStr);
            if (line == -0.0)
                line = 0.0;
            String selectionName1;
            String selectionName2;
            if (line < 0) {
                selectionName2 = "A" + " (" + line + ")";
                selectionName1 = "B" + " (+" + (-line) + ")";
            } else {
                selectionName2 = "A" + " (+" + line + ")";
                selectionName1 = "B" + " (" + (-line) + ")";
            }
            market.setSelectionNameOverOrA(selectionName2);
            market.setSelectionNameUnderOrB(selectionName1);

            market.put("A", 1 - p);
            market.put("B", p);
        }
        return market;
    }

    /**
     * generates a new market instance with probabilities associated with the specified line
     * 
     * @param lineIdStr - the required line - e.g. "4.5"
     * @return
     */
    @JsonIgnore
    public Market generateEuropeanMarketForLineId(String marketType, String marketDescription, String lineIdStr) {
        boolean validMarketType = this.valid
                        && (this.marketCategory == MarketCategory.HCAP || this.marketCategory == MarketCategory.OVUN);
        if (!validMarketType)
            return null;
        MarketCategory newMarketCategory;
        if (this.marketCategory == MarketCategory.HCAP)
            newMarketCategory = MarketCategory.HCAPEU;
        else {
            newMarketCategory = MarketCategory.OVUN_EU;
            throw new IllegalArgumentException("support for OVUN_EU type market not yet implemented");
        }
        Market market = new Market(newMarketCategory, this.getMarketGroup(), marketType, this.getSequenceId(),
                        marketDescription);
        market.setIsValid(true);
        market.setLineBase(this.lineBase);
        market.setLineProbs(this.lineProbs);
        market.setLineId(lineIdStr);
        market.setDerivedMarketsInfo(this.derivedMarketsInfo); // new market
                                                               // points to
                                                               // same
        // status object as this one
        /*
         * use the previously stored trimmedBase and trimmedProbs[] to calculate the prob
         */
        int lineIdentifier = MarketUtilityFunctions.convertLineIdStringToLineIndex(lineIdStr);
        int index; // the index into the probArray which represents the balanced
                   // line
        switch (newMarketCategory) {
            case HCAPEU:
                index = -lineIdentifier - this.lineBase - 1;
                break;
            case OVUN_EU:
                index = lineIdentifier - this.lineBase;
                break;
            default:
                throw new IllegalArgumentException("Unexpected market category");
        }
        double p;
        double p_draw = 0.0;
        if (index < 0)
            p = 0;
        else if (index > lineProbs.length - 1)
            p = 1;
        else
            p = lineProbs[index];
        if (this.marketCategory == MarketCategory.HCAPEU) {
            if (index < 0) {
                p = 0;
                p_draw = lineProbs[0];
                if (index < -1)
                    p_draw = 0;
            } else if (index >= lineProbs.length - 1)
                p = 1;
            else {
                p = lineProbs[index];
                p_draw = lineProbs[index + 1] - lineProbs[index];
            }

            double lineId = GCMath.round(lineIdentifier, 1);
            market.setLineId(String.format("%.2f", lineId));
            String selectionName1;
            String selectionName2;
            String selectionName3;
            if (lineId < 0) {
                selectionName2 = "A" + " (" + lineId + ")";
                selectionName1 = "B" + " (+" + (-lineId) + ")";
                selectionName3 = "D";
            } else {
                selectionName2 = "A" + " (+" + lineId + ")";
                selectionName1 = "B" + " (" + (-lineId) + ")";
                selectionName3 = "D";
            }
            market.setSelectionNameOverOrA(selectionName2);
            market.setSelectionNameUnderOrB(selectionName1);
            market.setSelectionNameDrawOrD(selectionName3);
            market.put("A", 1 - p - p_draw);
            market.put("D", p_draw);
            market.put("B", p);

        } else {
            double lineId = GCMath.round(lineIdentifier + .5, 1);
            String selectionName1;
            String selectionName2;
            if (lineId < 0) {
                selectionName2 = "A" + " (" + lineId + ")";
                selectionName1 = "B" + " (+" + (-lineId) + ")";
            } else {
                selectionName2 = "A" + " (+" + lineId + ")";
                selectionName1 = "B" + " (" + (-lineId) + ")";
            }
            market.setSelectionNameOverOrA(selectionName2);
            market.setSelectionNameUnderOrB(selectionName1);
            market.put("A", 1 - p);
            market.put("B", p);
        }
        return market;
    }

    /**
     * generates a new market instance with probabilities associated with the specified line
     * 
     * @param lineIdStr - the required line - e.g. "4.5"
     * @return
     */
    @JsonIgnore
    public Market getMarketForLineId(String lineIdStr) {
        boolean validMarketType = this.valid && (this.marketCategory == MarketCategory.HCAP
                        || this.marketCategory == MarketCategory.OVUN || this.marketCategory == MarketCategory.HCAPEU);
        if (!validMarketType)
            return null;
        Market market = new Market(this.getCategory(), this.getMarketGroup(), this.getType(), this.getSequenceId(),
                        this.getMarketDescription());
        market.setIsValid(true);
        market.setLineBase(this.lineBase);
        market.setLineId(lineIdStr);
        market.setLineProbs(this.lineProbs);
        market.setDerivedMarketsInfo(this.derivedMarketsInfo); // new market
                                                               // points to
                                                               // same
        // status object as this one
        /*
         * use the previously stored trimmedBase and trimmedProbs[] to calculate the prob
         */
        int lineIdentifier = MarketUtilityFunctions.convertLineIdStringToLineIndex(lineIdStr);
        int index; // offset into probArray
        switch (this.marketCategory) {
            case OVUN:
                index = lineIdentifier - this.lineBase;
                break;
            case HCAP:
                lineIdentifier = (int) GCMath.round(Double.valueOf(lineIdStr), 0);
                index = -lineIdentifier - this.lineBase;
                break;
            case HCAPEU:
                index = -lineIdentifier - this.lineBase - 1;
                break;
            default:
                throw new IllegalArgumentException("Unexpected market category");
        }
        double p;
        double p_draw = 0.0;
        // if (index > lineProbs.length - 1)
        // //DEBUG
        // System.out.println(index);
        if (index < 0)
            p = 0;
        else if (index > lineProbs.length - 1)
            p = 1;
        else
            p = lineProbs[index];

        if (this.marketCategory == MarketCategory.OVUN) {
            market.setSelectionNameOverOrA("Over");
            market.setSelectionNameUnderOrB("Under");

            if ((index > 1) && (index < lineProbs.length - 1)) {
                if ((Double.parseDouble(lineIdStr)) % 1 == 0.0)
                    p_draw = (lineProbs[index] - lineProbs[index - 1]) / 2;
                if (((Double.parseDouble(lineIdStr)) * 4 % 4) == 1)
                    p_draw = (lineProbs[index] - lineProbs[index - 1]) / 4;
                if (((Double.parseDouble(lineIdStr)) * 4 % 4) == 3)
                    p_draw = -(lineProbs[index] - lineProbs[index - 1]) / 4;
            }

            market.put("Over", 1 - p + p_draw);
            market.put("Under", p - p_draw);
        } else if (this.marketCategory == MarketCategory.HCAP) {
            double line = Double.valueOf(lineIdStr);
            if (line == -0.0)
                line = 0.0;
            String selectionName1;
            String selectionName2;
            if (line < 0) {
                selectionName2 = "AH" + " (" + line + ")";
                selectionName1 = "BH" + " (+" + (-line) + ")";
            } else {
                selectionName2 = "AH" + " (+" + line + ")";
                selectionName1 = "BH" + " (" + (-line) + ")";
            }
            market.setSelectionNameOverOrA(selectionName2);
            market.setSelectionNameUnderOrB(selectionName1);

            /*
             * calc prob of draw if index within the array - else will remain at default value of 0.0
             */
            if ((index >= 1) && (index < lineProbs.length - 1)) {

                if ((Math.abs(line) * 4 % 4) == 0) {
                    p_draw = -(lineProbs[index] - lineProbs[index - 1]) / 2;
                }
                if ((Math.abs(line) * 4 % 4) == 1) {
                    if (line > 0)
                        p_draw = -(lineProbs[index] - lineProbs[index - 1]) * 3 / 4;
                    else
                        p_draw = -(lineProbs[index] - lineProbs[index - 1]) / 4;
                }

                if ((Math.abs(line) * 4 % 4) == 3) {
                    if (line > 0)
                        p_draw = -(lineProbs[index] - lineProbs[index - 1]) / 4;
                    else
                        p_draw = -(lineProbs[index] - lineProbs[index - 1]) * 3 / 4;
                }
            }

            market.put("AH", 1 - p - p_draw);
            market.put("BH", p + p_draw);
        } else if (this.marketCategory == MarketCategory.HCAPEU) {

            if (index < 0) {
                p = 0;
                p_draw = lineProbs[0];
                if (index < -1)
                    p_draw = 0;
            } else if (index >= lineProbs.length - 2)
                p = 1;
            else {
                p = lineProbs[index];
                p_draw = lineProbs[index + 1] - lineProbs[index];
            }

            double lineId = GCMath.round(lineIdentifier, 1);
            market.setLineId(String.format("%.2f", lineId));
            String selectionName1;
            String selectionName2;
            String selectionName3;
            if (lineId < 0) {
                selectionName2 = "A" + " (" + lineId + ")";
                selectionName1 = "B" + " (+" + (-lineId) + ")";
                selectionName3 = "Draw";
            } else {
                selectionName2 = "A" + " (+" + lineId + ")";
                selectionName1 = "B" + " (" + (-lineId) + ")";
                selectionName3 = "Draw";
            }
            market.setSelectionNameOverOrA(selectionName2);
            market.setSelectionNameUnderOrB(selectionName1);
            market.setSelectionNameDrawOrD(selectionName3);
            market.put("AH", 1 - p - p_draw);
            market.put("DH", p_draw);
            market.put("BH", p);

        } else {
            double lineId = GCMath.round(lineIdentifier + .5, 1);
            String selectionName1;
            String selectionName2;
            if (lineId < 0) {
                selectionName2 = "A" + " (" + lineId + ")";
                selectionName1 = "B" + " (+" + (-lineId) + ")";
            } else {
                selectionName2 = "A" + " (+" + lineId + ")";
                selectionName1 = "B" + " (" + (-lineId) + ")";
            }
            market.setSelectionNameOverOrA(selectionName2);
            market.setSelectionNameUnderOrB(selectionName1);
            market.put("A", 1 - p);
            market.put("B", p);
        }
        return market;
    }

    /**
     * gets the index of the balanced line
     * 
     * @return the index. e.g. if balanced line is 17.5 then index is 17
     */
    @JsonIgnore
    public int getBalancedLineIndex() {
        if (lineProbs != null) {
            int arrayIndex = 0;
            double maxDiff = 1.0;
            for (int i = 0; i < lineProbs.length; i++) {
                double diff = Math.abs(lineProbs[i] - 0.5);
                if (diff < maxDiff) {
                    arrayIndex = i;
                    maxDiff = diff;
                }
            }
            return arrayIndex + lineBase;
        } else {
            /*
             * * TODO temp fix until can modify market properties
             */
            return 0;
        }
    }

    @JsonIgnore
    String getLineId(MarketCategory marketCategory, double lineIndex) {
        String lineId;
        if (marketCategory == MarketCategory.OVUN)
            lineId = String.format("%.2f", lineIndex + 0.5);
        else
            lineId = String.format("%.2f", -(lineIndex + 0.5));
        return lineId;
    }

    public String generateBalancedLineId() {
        if (this.marketCategory == MarketCategory.GENERAL)
            return null;
        else if (this.balancedLineId == null)
            balancedLineId = getLineId(this.marketCategory, getBalancedLineIndex());
        return balancedLineId;
    }

    public String getBalancedLineId() {
        return balancedLineId;
    }

    public void setBalancedLineId(String balancedLineId) {
        this.balancedLineId = balancedLineId;
    }

    @JsonIgnore
    public void setBalancedLine(double balancedLine) {
        this.balancedLineId = "" + balancedLine;
    }

    @JsonIgnore
    double getOvunLineId(MarketCategory category, int index, int reverseGap) {
        double lineId = 0.5;
        double maxDiff = 1.0;
        int arrayIndex = 0;
        int count = index - this.lineBase;
        if (count == 0)
            count = 1;
        if (reverseGap == 4) {
            double p = lineProbs[count];
            double p_draw1 = p + (lineProbs[count] - lineProbs[count - 1]) / 2;
            double p_draw2 = p + (lineProbs[count] - lineProbs[count - 1]) / 4;
            double p_draw3 = p - (lineProbs[count] - lineProbs[count - 1]) / 4;
            double p_draw4 = p - (lineProbs[count] - lineProbs[count - 1]) / 2;
            double p_draw5 = p + (lineProbs[count] - lineProbs[count - 1]) * 3 / 4;
            double p_draw6 = p - (lineProbs[count] - lineProbs[count - 1]) * 3 / 4;
            double[] pList = {p, p_draw1, p_draw2, p_draw3, p_draw4, p_draw5, p_draw6};
            for (int i = 0; i < pList.length; i++) {
                double diff = Math.abs(pList[i] - 0.5);
                if (diff < maxDiff) {
                    arrayIndex = i;
                    maxDiff = diff;
                }
            }
            if (arrayIndex == 0)
                lineId += index;
            if (arrayIndex == 1)
                lineId += index + 0.5;
            if (arrayIndex == 2)
                lineId += index + 0.25;
            if (arrayIndex == 3)
                lineId += index - 0.25;
            if (arrayIndex == 4)
                lineId += index - 0.5;
            if (arrayIndex == 5)
                lineId += index + 0.75;
            if (arrayIndex == 6)
                lineId += index - 0.75;
            return lineId;
        } else if (reverseGap == 2) {
            double p = lineProbs[count];
            double p_draw1 = p - (lineProbs[count] - lineProbs[count - 1]) / 2;
            double p_draw4 = p + (lineProbs[count] - lineProbs[count - 1]) / 2;
            double[] pList = {p, p_draw1, p_draw4};
            for (int i = 0; i < pList.length; i++) {
                double diff = Math.abs(pList[i] - 0.5);
                if (diff < maxDiff) {
                    arrayIndex = i;
                    maxDiff = diff;
                }
            }
            if (arrayIndex == 0)
                lineId += index;
            if (arrayIndex == 1)
                lineId += index - 0.5;
            if (arrayIndex == 2)
                lineId += index + 0.5;
            return lineId;
        } else if (reverseGap == 1) {
            lineId += index;
            return lineId;
        }
        return lineId;

    }

    @JsonIgnore
    double getAhcpLineId(MarketCategory category, int index, int reverseGap) {
        double lineId = 0.5;
        double maxDiff = 1.0;
        int arrayIndex = 0;
        int count = index - this.lineBase;
        if (count == 0)
            return 0;
        if (reverseGap == 4) {
            double p = lineProbs[count];
            double p_draw1 = p + (lineProbs[count] - lineProbs[count - 1]) / 2;
            double p_draw2 = p + (lineProbs[count] - lineProbs[count - 1]) / 4;
            double p_draw3 = p - (lineProbs[count] - lineProbs[count - 1]) / 4;
            double p_draw4 = p - (lineProbs[count] - lineProbs[count - 1]) / 2;
            double p_draw5 = p + (lineProbs[count] - lineProbs[count - 1]) * 3 / 4;
            double p_draw6 = p - (lineProbs[count] - lineProbs[count - 1]) * 3 / 4;
            double[] pList = {p, p_draw1, p_draw2, p_draw3, p_draw4, p_draw5, p_draw6};
            for (int i = 0; i < pList.length; i++) {
                double diff = Math.abs(pList[i] - 0.5);
                if (diff < maxDiff) {
                    arrayIndex = i;
                    maxDiff = diff;
                }
            }
            if (arrayIndex == 0)
                lineId += index;
            if (arrayIndex == 1)
                lineId += index + 0.5;
            if (arrayIndex == 2)
                lineId += index + 0.25;
            if (arrayIndex == 3)
                lineId += index - 0.25;
            if (arrayIndex == 4)
                lineId += index - 0.5;
            if (arrayIndex == 5)
                lineId += index + 0.75;
            if (arrayIndex == 6)
                lineId += index - 0.75;
            return lineId;
        } else if (reverseGap == 2) {
            double p = lineProbs[count];
            double p_draw1 = p - (lineProbs[count] - lineProbs[count - 1]) / 2;
            double p_draw4 = p + (lineProbs[count] - lineProbs[count - 1]) / 2;
            double[] pList = {p, p_draw1, p_draw4};
            for (int i = 0; i < pList.length; i++) {
                double diff = Math.abs(pList[i] - 0.5);
                if (diff < maxDiff) {
                    arrayIndex = i;
                    maxDiff = diff;
                }
            }
            if (arrayIndex == 0)
                lineId += index;
            if (arrayIndex == 1)
                lineId += index - 0.5;
            if (arrayIndex == 2)
                lineId += index + 0.5;
            return lineId;
        } else if (reverseGap == 1) {
            lineId += index;
            return lineId;
        }
        return lineId;

    }

    /**
     * gets the set of line markets for all the lineId's as specified in the MarketStatus object associated with this
     * market
     * 
     * @return
     */
    @JsonIgnore
    @Deprecated
    public Market[] getAllLineMarketsSpecifiedByTradingRules() {

        ArrayList<Market> mktList = new ArrayList<Market>();
        int nLines = derivedMarketsInfo.getNoLinesToDisplayEachSideOfBalancedLine();
        int spacing = derivedMarketsInfo.getDisplayLineSpacing();
        int balancedLineIndex = getBalancedLineIndex();
        int reverseGap = derivedMarketsInfo.getNormalLineInverseGap();
        double balancedLine;
        String line;
        switch (this.marketCategory) {
            case GENERAL:
                break;
            case HCAP:
                balancedLine = getAhcpLineId(marketCategory, balancedLineIndex, reverseGap);
                for (int i = -nLines; i <= nLines; i++) {
                    if (reverseGap == 4)
                        line = String.format("%.2f", -(balancedLine + (double) i * spacing / reverseGap));
                    else
                        line = String.format("%.2f", -(balancedLine + (double) i * spacing / reverseGap));
                    mktList.add(getMarketForLineId(line));
                }

                // static lines creations
                String baseLine = (derivedMarketsInfo.getDataToGenerateStaticMarkets().getMap().get("FT:SPRD"));
                boolean staticLineMarketsCreating = (baseLine != null);
                staticLineMarketsCreating =
                                (staticLineMarketsCreating && derivedMarketsInfo.getStaticLineList() != null);
                if (staticLineMarketsCreating) {
                    String[] staticLines = derivedMarketsInfo.getStaticLineList().split(",");
                    for (int ii = 0; ii < staticLines.length; ii++) {
                        line = String.format("%.2f", (Double.valueOf(baseLine)) + Double.valueOf(staticLines[ii]));
                        mktList.add(getMarketForLineId(line));
                    }
                }
                break;
            case HCAPEU:
                for (int i = -nLines; i <= nLines; i++) {
                    int lineIndex = balancedLineIndex + i * spacing;
                    line = getLineId(marketCategory, lineIndex);
                    mktList.add(getMarketForLineId(line));
                }
                break;
            case OVUN:
                balancedLine = getOvunLineId(marketCategory, balancedLineIndex, reverseGap);
                for (int i = -nLines; i <= nLines; i++) {
                    if (reverseGap == 4)
                        line = String.format("%.2f", balancedLine + (double) i * spacing / reverseGap);
                    else
                        line = String.format("%.2f", balancedLine + (double) i * spacing / reverseGap);
                    mktList.add(getMarketForLineId(line));
                }
                break;
            default:
                break;
        }

        Market[] markets = new Market[mktList.size()];
        for (int i = 0; i < mktList.size(); i++)
            markets[i] = mktList.get(i);
        return markets;
    }

    public List<Market> generateAllDerivedMarketsSpecifiedByTradingRules(
                    DataToGenerateStaticMarkets dataToGenerateStaticMarkets) {
        return DerivedMarket.generateAllDerivedMarkets(this, dataToGenerateStaticMarkets);
    }

    /**
     * Makes a copy of itself
     * 
     * @return
     */
    public Market copy() {
        Market marketCopy = new Market(this.getCategory(), this.getType(), this.getSequenceId(),
                        this.getMarketDescription());
        marketCopy.setSelectionNameOverOrA(this.getSelectionNameOverOrA());
        marketCopy.setSelectionNameUnderOrB(this.getSelectionNameUnderOrB());
        marketCopy.setLineId(this.getLineId());
        marketCopy.setIsValid(this.isValid());
        marketCopy.setLineBase(this.lineBase);
        marketCopy.setLineProbs(this.lineProbs);
        marketCopy.setMarketGroup(this.marketGroup);
        marketCopy.setMarketStatus(this.getMarketStatus().copy());
        DerivedMarketsInfo derivedMarketsInfo = this.getDerivedMarketsInfo();
        if (derivedMarketsInfo != null)
            marketCopy.setDerivedMarketsInfo(derivedMarketsInfo.copy());
        for (Entry<String, Selection> selEntry : selections.entrySet()) {
            marketCopy.getSelections().put(selEntry.getKey(), selEntry.getValue().copy());
        }
        return marketCopy;
    }

    @Override
    public String toString() {
        return JsonUtil.marshalJson(this);
        // String s = "[category=" + marketCategory + ", type=" + type + ",
        // sequenceId=" + sequenceId + ", lineId="
        // + lineId + ", valid=" + valid + ", marketDescription=" +
        // marketDescription + ", marketGroup="
        // + marketGroup + ",\nmarketStatus=" + marketStatus
        // + "derivedMarketsInfo" + derivedMarketsInfo + "\nselections: [";
        // int nSelections = selections.size();
        // if (!valid || nSelections == 0)
        // s += "No valid selections\n";
        // else {
        // String[] selection = new String[nSelections];
        // double[] prob = new double[nSelections];
        // int i = 0;
        // for (Map.Entry<String, Double> sEntry : selections.entrySet()) {
        // String sKey = sEntry.getKey();
        // selection[i] = sKey;
        // prob[i] = sEntry.getValue();
        // i++;
        // }
        // /*
        // * overwrite the generic with more detail - e.g. "Over 4.5" instead of
        // "Over
        // */
        // if (marketCategory != MarketCategory.GENERAL && marketCategory !=
        // MarketCategory.HCAPEU) {
        // for (int k = 0; k < 2; k++) {
        // if (selection[k] == "Over" || selection[k] == "A")
        // selection[k] = selectionNameOverOrA;
        // if (selection[k] == "Under" || selection[k] == "B")
        // selection[k] = selectionNameUnderOrB;
        // }
        // }
        // if (marketCategory == MarketCategory.HCAPEU) {
        // for (int k = 0; k < 3; k++) {
        // if (selection[k] == "Over" || selection[k] == "A")
        // selection[k] = selectionNameOverOrA;
        // if (selection[k] == "Under" || selection[k] == "B")
        // selection[k] = selectionNameUnderOrB;
        // if (selection[k] == "Draw" || selection[k] == "D")
        // selection[k] = selectionNameDrawOrD;
        // }
        // }
        // for (int j = 0; j < nSelections; j++) {
        // s += String.format("%s: %.3f", selection[j], prob[j]);
        // if (j < nSelections - 1)
        // s += ",";
        // }
        // }
        // s += "]]";
        // return s;
    }

    /**
     * Generate a string split over multiple lines containing all the properties of the market
     * 
     * @return
     */
    public String toMultiLineString() {
        String s = this.toString();
        s = s.replace(", ", "\n");
        String extraStuff = "";
        if (derivedMarketDetails == null) {
            extraStuff += "derivedMarket: false\n";
        } else {
            extraStuff += "derivedMarket: true\n";
            extraStuff += derivedMarketDetails.toString() + "\n";
        }
        if (marketCategory != MarketCategory.GENERAL) {
            extraStuff += "lineBase: " + lineBase + "\n";
            if (lineProbs != null) {
                extraStuff += "lineProbs: {";
                for (int i = 0; i < lineProbs.length; i++) {
                    extraStuff += String.format("%.4f", lineProbs[i]);
                    if (i < lineProbs.length - 1)
                        extraStuff += ", ";
                }
                extraStuff += "}\n";
            }
        }
        s = s.replace("]]", "]\n" + extraStuff + "]");

        return s;
    }

    /**
     * gets the autogenerated selection name associated with Over (for OVUN MarketCategory) or A (for HCAP
     * MarketCategory)
     * 
     * @return
     */
    public String getSelectionNameOverOrA() {
        return selectionNameOverOrA;
    }

    /**
     * sets the autogenerated selection name associated with Over (for OVUN MarketCategory) or A (for HCAP
     * MarketCategory)
     * 
     * @param selectionNameOverOrA
     */
    public void setSelectionNameOverOrA(String selectionNameOverOrA) {
        this.selectionNameOverOrA = selectionNameOverOrA;
    }

    /**
     * gets the autogenerated selection name associated with Under (for OVUN MarketCategory) or B (for HCAP
     * MarketCategory)
     * 
     * @return
     */
    public String getSelectionNameUnderOrB() {
        return selectionNameUnderOrB;
    }

    /**
     * sets the autogenerated selection name associated with Under (for OVUN MarketCategory) or B (for HCAP
     * MarketCategory)
     * 
     * @return
     */
    public void setSelectionNameUnderOrB(String selectionNameUnderOrB) {
        this.selectionNameUnderOrB = selectionNameUnderOrB;
    }

    /**
     * gets the autogenerated selection name associated with Draw
     * 
     * @return
     */
    public String getSelectionNameDrawOrD() {
        return selectionNameDrawOrD;
    }

    /**
     * sets the autogenerated selection name associated with Draw
     * 
     * @param selectionNameDrawOrD
     */
    public void setSelectionNameDrawOrD(String selectionNameDrawOrD) {
        this.selectionNameDrawOrD = selectionNameDrawOrD;
    }

    /**
     * gets the probability associated with the specified selection
     * 
     * @param key
     * @return
     */
    @JsonIgnore
    public Double get(Object key) {
        if (selections.get(key) != null)
            return selections.get(key).getProb();
        else
            return null;
    }

    /**
     * puts the probability associated with the specified selection.
     * 
     * @param key
     * @param value
     * @return
     */

    public void put(String key, Double value) {
        this.getSelections().put(key, new Selection(value));
    }


    public void put(String key, Double value, SuspensionStatus suspensionStatus) {
        this.getSelections().put(key, new Selection(suspensionStatus, value));
    }

    /**
     * returns the number of selections
     * 
     * @return
     */
    public int size() {
        return selections.size();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((derivedMarketDetails == null) ? 0 : derivedMarketDetails.hashCode());
        result = prime * result + ((derivedMarketsInfo == null) ? 0 : derivedMarketsInfo.hashCode());
        result = prime * result + lineBase;
        result = prime * result + ((lineId == null) ? 0 : lineId.hashCode());
        result = prime * result + Arrays.hashCode(lineProbs);
        result = prime * result + ((marketCategory == null) ? 0 : marketCategory.hashCode());
        result = prime * result + ((marketDescription == null) ? 0 : marketDescription.hashCode());
        result = prime * result + ((marketGroup == null) ? 0 : marketGroup.hashCode());
        result = prime * result + ((marketStatus == null) ? 0 : marketStatus.hashCode());
        result = prime * result + ((selectionNameDrawOrD == null) ? 0 : selectionNameDrawOrD.hashCode());
        result = prime * result + ((selectionNameOverOrA == null) ? 0 : selectionNameOverOrA.hashCode());
        result = prime * result + ((selectionNameUnderOrB == null) ? 0 : selectionNameUnderOrB.hashCode());
        result = prime * result + ((selections == null) ? 0 : selections.hashCode());
        result = prime * result + ((sequenceId == null) ? 0 : sequenceId.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + (valid ? 1231 : 1237);
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
        Market other = (Market) obj;
        if (derivedMarketDetails == null) {
            if (other.derivedMarketDetails != null)
                return false;
        } else if (!derivedMarketDetails.equals(other.derivedMarketDetails))
            return false;
        if (derivedMarketsInfo == null) {
            if (other.derivedMarketsInfo != null)
                return false;
        } else if (!derivedMarketsInfo.equals(other.derivedMarketsInfo))
            return false;
        if (lineBase != other.lineBase)
            return false;
        if (lineId == null) {
            if (other.lineId != null)
                return false;
        } else if (!lineId.equals(other.lineId))
            return false;
        if (!Arrays.equals(lineProbs, other.lineProbs))
            return false;
        if (marketCategory != other.marketCategory)
            return false;
        if (marketDescription == null) {
            if (other.marketDescription != null)
                return false;
        } else if (!marketDescription.equals(other.marketDescription))
            return false;
        if (marketGroup != other.marketGroup)
            return false;
        if (marketStatus == null) {
            if (other.marketStatus != null)
                return false;
        } else if (!marketStatus.equals(other.marketStatus))
            return false;
        if (selectionNameDrawOrD == null) {
            if (other.selectionNameDrawOrD != null)
                return false;
        } else if (!selectionNameDrawOrD.equals(other.selectionNameDrawOrD))
            return false;
        if (selectionNameOverOrA == null) {
            if (other.selectionNameOverOrA != null)
                return false;
        } else if (!selectionNameOverOrA.equals(other.selectionNameOverOrA))
            return false;
        if (selectionNameUnderOrB == null) {
            if (other.selectionNameUnderOrB != null)
                return false;
        } else if (!selectionNameUnderOrB.equals(other.selectionNameUnderOrB))
            return false;
        if (selections == null) {
            if (other.selections != null)
                return false;
        } else if (!selections.equals(other.selections))
            return false;
        if (sequenceId == null) {
            if (other.sequenceId != null)
                return false;
        } else if (!sequenceId.equals(other.sequenceId))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        if (valid != other.valid)
            return false;
        return true;
    }

}
