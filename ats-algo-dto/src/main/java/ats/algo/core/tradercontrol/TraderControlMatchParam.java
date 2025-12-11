package ats.algo.core.tradercontrol;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchParam;
import ats.algo.core.baseclasses.MatchParamType;

/**
 * uses MatchParam as a carrier for integer valued control variables
 * 
 * @author Geoff
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TraderControlMatchParam extends MatchParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonCreator
    /**
     * constructor - for normal use
     * 
     * @param defaultValue
     * @param minValue
     * @param maxValue
     */
    public TraderControlMatchParam(@JsonProperty("defaultValue") int defaultValue,
                    @JsonProperty("minValue") int minValue, @JsonProperty("maxValue") int maxValue) {
        super(MatchParamType.TRADER_CONTROL, MarketGroup.NOT_SPECIFIED, (double) defaultValue, 0.0, (double) minValue,
                        (double) maxValue, false);
    }

    /**
     * constructor - for json use
     * 
     * @param jsonMap
     */
    public TraderControlMatchParam(Object jsonMap) {
        super(MatchParamType.TRADER_CONTROL, MarketGroup.NOT_SPECIFIED, jsonMap);
    }

    /**
     * constructor - for json use
     */
    public TraderControlMatchParam() {
        super();
    }

    @JsonIgnore
    public int getValue() {
        double d = super.getGaussian().getMean();
        int v = (int) d;
        return v;
    }

    @JsonIgnore
    public void setValue(int v) {
        double d = (double) v;
        super.getGaussian().setMean(d);
    }

    @Override
    public MatchParam copy() {
        MatchParam cc = new TraderControlMatchParam();
        cc.setEqualTo(this);
        return cc;
    }
}
