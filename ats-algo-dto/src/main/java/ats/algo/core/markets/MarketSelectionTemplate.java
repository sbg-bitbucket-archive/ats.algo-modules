package ats.algo.core.markets;

import java.io.Serializable;

/**
 * 
 * container for specifying a particular market as an offset from the current sequenceId
 * 
 * @author
 *
 */
public class MarketSelectionTemplate implements Serializable {

    private static final long serialVersionUID = 1L;
    private String marketType;
    private int sequenceIdOffset;

    /**
     * json constructor. not for normal use.
     */
    public MarketSelectionTemplate() {
        // for json
    }

    /**
     * container for specifying a particular market as an offset from the current sequenceId
     * 
     * @param marketType
     * @param sequenceIdOffset
     */
    public MarketSelectionTemplate(String marketType, int sequenceIdOffset) {
        super();
        this.marketType = marketType;
        this.sequenceIdOffset = sequenceIdOffset;
    }

    public String getMarketType() {
        return marketType;
    }

    public void setMarketType(String marketType) {
        this.marketType = marketType;
    }

    public int getSequenceIdOffset() {
        return sequenceIdOffset;
    }

    public void setSequenceIdOffset(int sequenceIdOffset) {
        this.sequenceIdOffset = sequenceIdOffset;
    }

}
