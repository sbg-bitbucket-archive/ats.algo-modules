package ats.algo.core.markets;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ats.core.util.CollectionsUtil;
import ats.core.util.json.JsonUtil;

/**
 * immutable container to hold the result of a market - e.g. which selection won.
 * 
 * @author Geoff
 *
 */
public class ResultedMarket implements Serializable {

    private static final long serialVersionUID = 1L;
    protected boolean fullyResulted = true;
    protected List<String> losingSelections = new ArrayList<>();
    protected List<String> voidedSelections = new ArrayList<>();

    protected String type;
    protected String lineId;
    protected MarketCategory category;
    protected String sequenceId;
    protected boolean marketVoided;
    protected String marketDescription;
    protected List<String> winningSelections;
    protected List<String> halfLost = null;
    protected List<String> halfWon = null;
    protected List<String> stakeBack = null;
    protected int actualOutcome; // for OVUN and HCAP markets holds the actual
                                 // outcome
    protected List<String> scores;

    /**
     * Constructor for json use ony
     */
    public ResultedMarket() {

    }

    /**
     * Constructor to use when there is only one winning selection
     * 
     * @param type
     * @param subType
     * @param category
     * @param sequenceId
     * @param marketVoided
     * @param marketDescription
     * @param winningSelection
     * @param actualLineId the actual outcome for an OVUN or HCAP market -e.g. total games was 22
     */
    public ResultedMarket(String type, String subType, MarketCategory category, String sequenceId, boolean marketVoided,
                    String marketDescription, String winningSelection, int actualLineId) {
        this.type = type;
        this.lineId = subType;
        this.category = category;
        this.sequenceId = sequenceId;
        this.marketVoided = marketVoided;
        this.winningSelections = new ArrayList<String>(1);
        this.winningSelections.add(winningSelection);
        this.marketDescription = marketDescription;
        this.actualOutcome = actualLineId;

    }

    /**
     * Constructor to use when there are multiple winning selections
     * 
     * @param type
     * @param subType
     * @param category
     * @param sequenceId
     * @param marketVoided
     * @param marketDescription
     * @param actualLineId the actual outcome for an OVUN or HCAP market -e.g. total games was 22
     */
    public ResultedMarket(String type, String subType, MarketCategory category, String sequenceId, boolean marketVoided,
                    String marketDescription, List<String> winningSelections, int actualLineId) {
        this.type = type;
        this.lineId = subType;
        this.category = category;
        this.sequenceId = sequenceId;
        this.marketVoided = marketVoided;
        this.winningSelections = winningSelections;
        this.marketDescription = marketDescription;
        this.actualOutcome = actualLineId;
    }

    /**
     * Constructor to use when there are multiple winning selections
     * 
     * @param type
     * @param subType
     * @param category
     * @param sequenceId
     * @param marketVoided
     * @param marketDescription
     * @param actualLineId the actual outcome for an OVUN or HCAP market -e.g. total games was 22
     */
    public ResultedMarket(String type, String subType, MarketCategory category, String sequenceId, boolean marketVoided,
                    String marketDescription, List<String> winningSelections, int actualLineId, List<String> scores) {
        this.type = type;
        this.lineId = subType;
        this.category = category;
        this.sequenceId = sequenceId;
        this.marketVoided = marketVoided;
        this.winningSelections = winningSelections;
        this.marketDescription = marketDescription;
        this.actualOutcome = actualLineId;
        this.scores = scores;
    }

    /**
     * Constructor to use when there are multiple winning selections and losing selections (Only will be used to
     * resulting selections)
     * 
     * @param type
     * @param subType
     * @param category
     * @param sequenceId
     * @param marketVoided
     * @param marketDescription
     * @param actualLineId the actual outcome for an OVUN or HCAP market -e.g. total games was 22
     */
    public ResultedMarket(String type, String subType, MarketCategory category, String sequenceId, boolean marketVoided,
                    String marketDescription, List<String> winningSelections, int actualLineId,
                    List<String> partiallosingSelections, List<String> partialvoidSelections, boolean fullyResulted) {
        this.type = type;
        this.lineId = subType;
        this.category = category;
        this.sequenceId = sequenceId;
        this.marketVoided = marketVoided;
        this.winningSelections = winningSelections;
        this.marketDescription = marketDescription;
        this.actualOutcome = actualLineId;
        this.losingSelections = partiallosingSelections;
        this.voidedSelections = partialvoidSelections;
        this.fullyResulted = fullyResulted;
    }



    public boolean isFullyResulted() {
        return fullyResulted;
    }

    public void setFullyResulted(boolean fullyResulted) {
        this.fullyResulted = fullyResulted;
    }

    public List<String> getVoidedSelections() {
        return voidedSelections;
    }

    public void setVoidedSelections(List<String> partialVoidedSelections) {
        this.voidedSelections = partialVoidedSelections;
    }

    public List<String> getLosingSelections() {
        return losingSelections;
    }

    public void setLosingSelections(List<String> losingSelections) {
        this.losingSelections = losingSelections;
    }

    /**
     * gets the type of this market - e.g. "FT:ML"
     * 
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * gets the lineId for the resulted market
     * 
     * @return
     */
    public String getLineId() {
        return lineId;
    }


    /*
     * 
     */
    public List<String> getWinningSelections() {
        return winningSelections;
    }

    /**
     * gets the description of this market
     * 
     * @return
     */
    public String getMarketDescription() {
        return marketDescription;
    }

    /**
     * sets the actual outcome for markets of type OVUN or HCAP. e.g. if for a handicap market teamB won by 2 goals than
     * actualLineId would be set to -2
     * 
     * @param actualLineId
     */
    public void setActualOutcome(int actualLineId) {
        this.actualOutcome = actualLineId;
    }

    /**
     * if true then this market was voided - i.e did not happen or was not completed
     * 
     * @return
     */
    public boolean isMarketVoided() {
        return marketVoided;
    }


    /**
     * gets the list of any "half lost" selections. currently only applicable to Asian handicap and total markets.
     * 
     * @return may be null if there are no halfLost selections
     */
    public List<String> getHalfLost() {
        return halfLost;
    }

    /**
     * sets the list of any "half lost" selections. currently only applicable to Asian handicap and total markets.
     * 
     * @param halfLost
     */
    public void setHalfLost(List<String> halfLost) {
        this.halfLost = halfLost;
    }

    /**
     * gets the list of any "half won" selections. currently only applicable to Asian handicap and total markets.
     * 
     * @return may be null if there are no halfWon selections
     */
    public List<String> getHalfWon() {
        return halfWon;
    }

    /**
     * sets the list of any "half won" selections. Currently only applicable to Asian handicap and total markets.
     * 
     * @param halfWon
     */
    public void setHalfWon(List<String> halfWon) {
        this.halfWon = halfWon;
    }

    /**
     * gets the list of any selections where stake is to be returned to the client. Currently only applicable to Asian
     * handicap and total markets.
     * 
     * @return may be null if there are no stakeBack selections
     */
    public List<String> getStakeBack() {
        return stakeBack;
    }


    /**
     * sets the list of any "stake back" selections, where stake is to be returned to the client. Currently only
     * applicable to Asian handicap and total markets.
     * 
     * @param halfWon
     */
    public void setStakeBack(List<String> stakeBack) {
        this.stakeBack = stakeBack;
    }

    @Override
    public String toString() {
        return JsonUtil.marshalJson(this);
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    public void setSequenceId(String sequenceId) {
        this.sequenceId = sequenceId;
    }

    public void setMarketVoided(boolean marketVoided) {
        this.marketVoided = marketVoided;
    }

    public void setMarketDescription(String marketDescription) {
        this.marketDescription = marketDescription;
    }

    public void setWinningSelections(List<String> winningSelections) {
        this.winningSelections = winningSelections;
    }

    public List<String> getScores() {
        return scores;
    }

    public void setScores(List<String> scores) {
        this.scores = scores;
    }

    @JsonIgnore
    public String getShortKey() {
        String sequenceId = this.sequenceId;
        if (sequenceId == null)
            sequenceId = "M";
        return this.type + "_" + sequenceId;
    }

    @JsonIgnore
    public String getFullKey() {
        String key;
        String sequenceId = this.getSequenceId();
        if (sequenceId == null) {
            sequenceId = "M";
        }
        String lineId = this.getLineId();
        double vLineId;
        try {
            vLineId = (Double.parseDouble(lineId));
            String lineKeyToken = String.valueOf(vLineId);
            key = this.getType() + "#" + lineKeyToken + "_" + sequenceId;
        } catch (NumberFormatException | NullPointerException e) {
            key = this.getType() + "_" + sequenceId;
        }
        return key;
    }

    /**
     * makes a copy of itself
     * 
     * @return
     */
    public ResultedMarket copy() {
        ResultedMarket resultedMarket = new ResultedMarket(this.type, this.lineId, this.category, this.sequenceId,
                        this.marketVoided, this.marketDescription, this.winningSelections, this.actualOutcome,
                        this.losingSelections, this.voidedSelections, this.fullyResulted);
        resultedMarket.setHalfLost(this.halfLost);
        resultedMarket.setHalfWon(this.halfWon);
        resultedMarket.setStakeBack(this.stakeBack);
        return resultedMarket;
    }

    /**
     * returns the outcome for the specified line market
     * 
     * @param subType
     * @return
     */
    public ResultedMarket getResultedMarketForSubType(String subType) {
        if (category == MarketCategory.GENERAL)
            return null;
        String winningSelection;
        if (category == MarketCategory.OVUN) {
            if (this.type.contains("AOU")) {
                winningSelection = CollectionsUtil.hasContent(winningSelections) ? winningSelections.get(0) : null;
            } else
                winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(actualOutcome, subType);
        } else if (category == MarketCategory.HCAPEU) {
            winningSelection = MarketUtilityFunctions.getWinningSelectionEuroHcapMarket(actualOutcome, subType);
        } else {
            winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(actualOutcome, subType);
        }
        ResultedMarket resultedMarket = new ResultedMarket(this.type, this.lineId, this.category, this.sequenceId,
                        this.marketVoided, this.marketDescription, winningSelection, this.actualOutcome);
        resultedMarket.setHalfLost(this.halfLost);
        resultedMarket.setHalfWon(this.halfWon);
        resultedMarket.setStakeBack(this.stakeBack);
        return resultedMarket;

    }

    public String getSequenceId() {
        return sequenceId;
    }

    public int getActualOutcome() {
        return actualOutcome;
    }

    @JsonIgnore
    public MarketTypeDescriptor getDescriptor() {
        return new MarketTypeDescriptor(category, type, lineId, sequenceId);

    }

    public MarketCategory getCategory() {
        return category;
    }

    public void setCategory(MarketCategory category) {
        this.category = category;
    }

    public Boolean getMarketVoided() {
        return marketVoided;
    }

    public String resultedSelectionsToString() {
        StringBuilder b = new StringBuilder();
        String prefix = "";
        prefix = addToBuilder(b, prefix, "won", this.getWinningSelections());
        prefix = addToBuilder(b, prefix, "lose", this.getLosingSelections());
        prefix = addToBuilder(b, prefix, "voided", this.getVoidedSelections());
        prefix = addToBuilder(b, prefix, "halfWon", this.getHalfWon());
        prefix = addToBuilder(b, prefix, "halfLost", this.getHalfLost());
        prefix = addToBuilder(b, prefix, "stakeBack", this.getStakeBack());
        b.append(".fullyResulted.");
        b.append(this.isFullyResulted());
        b.append(".");
        return b.toString();
    }


    private static String addToBuilder(StringBuilder b, String prefix, String selectionsDescription,
                    List<String> selections) {
        if (selections != null) {
            if (selections.size() > 0) {
                b.append(prefix);
                prefix = ", ";
                b.append(selectionsDescription);
                b.append(": [");
                int n = selections.size();
                for (int i = 0; i < n; i++) {
                    b.append(selections.get(i));
                    if (i < n - 1)
                        b.append(", ");
                    else
                        b.append("]");
                }
            }
        }
        return prefix;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + actualOutcome;
        result = prime * result + ((category == null) ? 0 : category.hashCode());
        result = prime * result + (fullyResulted ? 1231 : 1237);
        result = prime * result + ((halfLost == null) ? 0 : halfLost.hashCode());
        result = prime * result + ((halfWon == null) ? 0 : halfWon.hashCode());
        result = prime * result + ((lineId == null) ? 0 : lineId.hashCode());
        result = prime * result + ((losingSelections == null) ? 0 : losingSelections.hashCode());
        result = prime * result + ((marketDescription == null) ? 0 : marketDescription.hashCode());
        result = prime * result + (marketVoided ? 1231 : 1237);
        result = prime * result + ((scores == null) ? 0 : scores.hashCode());
        result = prime * result + ((sequenceId == null) ? 0 : sequenceId.hashCode());
        result = prime * result + ((stakeBack == null) ? 0 : stakeBack.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((winningSelections == null) ? 0 : winningSelections.hashCode());
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
        ResultedMarket other = (ResultedMarket) obj;
        if (actualOutcome != other.actualOutcome)
            return false;
        if (category != other.category)
            return false;
        if (fullyResulted != other.fullyResulted)
            return false;
        if (halfLost == null) {
            if (other.halfLost != null)
                return false;
        } else if (!halfLost.equals(other.halfLost))
            return false;
        if (halfWon == null) {
            if (other.halfWon != null)
                return false;
        } else if (!halfWon.equals(other.halfWon))
            return false;
        if (lineId == null) {
            if (other.lineId != null)
                return false;
        } else if (!lineId.equals(other.lineId))
            return false;
        if (losingSelections == null) {
            if (other.losingSelections != null)
                return false;
        } else if (!losingSelections.equals(other.losingSelections))
            return false;
        if (marketDescription == null) {
            if (other.marketDescription != null)
                return false;
        } else if (!marketDescription.equals(other.marketDescription))
            return false;
        if (marketVoided != other.marketVoided)
            return false;
        if (scores == null) {
            if (other.scores != null)
                return false;
        } else if (!scores.equals(other.scores))
            return false;
        if (sequenceId == null) {
            if (other.sequenceId != null)
                return false;
        } else if (!sequenceId.equals(other.sequenceId))
            return false;
        if (stakeBack == null) {
            if (other.stakeBack != null)
                return false;
        } else if (!stakeBack.equals(other.stakeBack))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        if (winningSelections == null) {
            if (other.winningSelections != null)
                return false;
        } else if (!winningSelections.equals(other.winningSelections))
            return false;
        return true;
    }

}
