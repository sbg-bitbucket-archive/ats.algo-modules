package ats.algo.core.baseclasses;

import static ats.algo.core.dto.util.DtoJsonUtil.toDouble;
import static ats.algo.core.dto.util.DtoJsonUtil.toBoolean;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

import ats.algo.core.MarketGroup;
import ats.algo.core.enumMatchParam.EnumMatchParam;
import ats.algo.core.tradercontrol.TraderControlMatchParam;
import ats.algo.genericsupportfunctions.Gaussian;
import ats.core.util.json.JsonUtil;

/**
 * holds the specification for a parameter which drives the pricing model.
 * 
 * @author Geoff
 * 
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "subClass")
@JsonSubTypes({@Type(name = "TraderControl", value = TraderControlMatchParam.class),
        @Type(name = "Standard", value = MatchParam.class), @Type(name = "Enum", value = EnumMatchParam.class)})
public class MatchParam implements Serializable {

    private static final long serialVersionUID = 2L;

    private MatchParamType matchParameterType;
    private Gaussian gaussian;
    private MarketGroup marketGroup;
    private double minAllowedParamValue;
    private double maxAllowedParamValue;
    private boolean displayAsPercentage;
    protected String description;

    /**
     * Gets the type of this MatchParameter
     * 
     * @return
     */
    public MatchParamType getMatchParameterType() {
        return matchParameterType;
    }

    /**
     * sets the matchParam type
     * 
     * @param matchParameterType
     */
    public void setMatchParameterType(MatchParamType matchParameterType) {
        this.matchParameterType = matchParameterType;
    }

    /**
     * gets the {mean, stdDevn, bias} triplet which defines the numeric values of this param
     * 
     * @return
     */
    public Gaussian getGaussian() {
        return gaussian;
    }

    /**
     * sets the {mean, stdDevn, bias} triplet which defines the numeric values of this param
     * 
     * @param src
     */
    public void setGaussian(Gaussian src) {
        this.gaussian = src;
    }


    /**
     * gets the min allowed for the mean value of this param
     * 
     * @return
     */
    public double getMinAllowedParamValue() {
        return minAllowedParamValue;
    }

    /**
     * sets the min allowed for the mean value of this param
     * 
     * @return
     */
    public void setMinAllowedParamValue(double minAllowedParamValue) {
        this.minAllowedParamValue = minAllowedParamValue;
    }

    /**
     * gets the max allowed for the mean value of this param
     * 
     * @return
     */
    public double getMaxAllowedParamValue() {
        return maxAllowedParamValue;
    }

    /**
     * sets the max allowed for the mean value of this param
     * 
     * @param maxAllowedParamValue
     */
    public void setMaxAllowedParamValue(double maxAllowedParamValue) {
        this.maxAllowedParamValue = maxAllowedParamValue;
    }

    /**
     * updates mean and stdDevn, setting bias to zero
     * 
     * @param mean
     * @param stdDevn
     */
    public void updateGaussian(double mean, double stdDevn) {
        this.gaussian.setMean(mean);
        this.gaussian.setStdDevn(stdDevn);
        this.gaussian.setBias(0);
        this.gaussian.setApplyBias(false);
    }

    /**
     * Gets the marketGroup to which this param belongs. May be used by marketRules and during param finding
     * 
     * @return
     */
    public MarketGroup getMarketGroup() {
        return marketGroup;
    }

    /**
     * Sets the marketGroup to which this param belongs. May be used by marketRules and during param finding
     * 
     * @param marketGroup
     */
    public void setMarketGroup(MarketGroup marketGroup) {
        this.marketGroup = marketGroup;
    }

    /**
     * if set to true then directs any UI apps to display the value as a percentage
     * 
     * @return
     */
    public boolean isDisplayAsPercentage() {
        return displayAsPercentage;
    }

    /**
     * if set to true then directs any UI apps to display the value as a percentage
     * 
     * @return
     */
    public void setDisplayAsPercentage(boolean displayAsPercentage) {
        this.displayAsPercentage = displayAsPercentage;
    }


    /**
     * Optional string to be used in place of the key when displaying the param to the user in a GUI. If null the key
     * should be used
     * 
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * Optional string to be used in place of the key when displaying the param to the user. If null the key should be
     * used
     * 
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * constructor - for use by Json
     */
    public MatchParam() {
        gaussian = new Gaussian();
        description = null;
    }


    /**
     * Constructor for normal use
     * 
     * @param type the type of this parameter (A, B, BOTHCOMBINED, BOTHDIFFERENCE)
     * @param marketGroup the MarketGroup this param affects
     * @param value the starting value for his parameter
     * @param stdDevn the starting stdDevn for this parameter. If set to 0 then param will be ignored when param finding
     * @param minAllowedValue the min allowed value of this param
     * @param maxAllowedValue the max allowed value of this param
     * @param displayAsPercentage set to true to display as a percentage
     */
    public MatchParam(MatchParamType type, MarketGroup marketGroup, double value, double stdDevn,
                    double minAllowedValue, double maxAllowedValue, boolean displayAsPercentage) {
        initialiseMatchParam(type, marketGroup, value, stdDevn, minAllowedValue, maxAllowedValue, displayAsPercentage);
    }


    /**
     * Convenience constructor for json unmarshalling - with specification of marketGroup
     */
    @SuppressWarnings("unchecked")
    public MatchParam(MatchParamType type, MarketGroup marketGroup, Object jsonMap) {
        Map<String, Object> map = (Map<String, Object>) jsonMap;
        Double minAllowedValue = toDouble(map.get("minAllowedParamValue"));
        Double maxAllowedValue = toDouble(map.get("maxAllowedParamValue"));
        boolean displayAsPercentage = toBoolean(map.get("displayAsPercentage"), false);
        Map<String, Object> gaussianMap = (Map<String, Object>) map.get("gaussian");
        Double mean = toDouble(gaussianMap.get("mean"));
        Double stdDevn = toDouble(gaussianMap.get("stdDevn"));
        Double bias = toDouble(gaussianMap.get("bias"));

        initialiseMatchParam(type, marketGroup, mean, stdDevn, minAllowedValue, maxAllowedValue, displayAsPercentage);
        this.getGaussian().setBias(bias);
    }

    private void initialiseMatchParam(MatchParamType type, MarketGroup marketGroup, double value, double stdDevn,
                    double minAllowedValue, double maxAllowedValue, boolean displayAsPercentage) {
        this.matchParameterType = type;
        this.marketGroup = marketGroup;
        gaussian = new Gaussian(value, stdDevn);
        this.minAllowedParamValue = minAllowedValue;
        this.maxAllowedParamValue = maxAllowedValue;
        this.displayAsPercentage = displayAsPercentage;
    }

    /**
     * sets the state of this instance to be the same as that of the supplied copy
     * 
     * @param other
     */
    public void setEqualTo(MatchParam other) {
        this.marketGroup = other.getMarketGroup();
        this.matchParameterType = other.getMatchParameterType();
        this.gaussian.setEqualTo(other.getGaussian());
        this.minAllowedParamValue = other.getMinAllowedParamValue();
        this.maxAllowedParamValue = other.getMaxAllowedParamValue();
        this.displayAsPercentage = other.isDisplayAsPercentage();
        this.description = other.getDescription();
    }

    /**
     * makes a copy of itself, replicating all state
     * 
     * @return
     */
    public MatchParam copy() {
        MatchParam cc = new MatchParam();
        cc.setEqualTo(this);
        return cc;
    }

    /**
     * tests a value to see whether it falls between allowed lower and upper bounds.
     * 
     * @param newValue
     * @return true if bounds have not been set (i.e. both zero) or if value is ok
     */
    public boolean valueIsValid(double value, double bias) {
        if (minAllowedParamValue == 0 && maxAllowedParamValue == 0)
            return true;
        if ((value < minAllowedParamValue) || (value > maxAllowedParamValue))
            return false;
        if ((value + bias < minAllowedParamValue) || (value + bias > maxAllowedParamValue))
            return false;
        return true;
    }



    @Override
    public String toString() {
        return JsonUtil.marshalJson(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (displayAsPercentage ? 1231 : 1237);
        result = prime * result + ((gaussian == null) ? 0 : gaussian.hashCode());
        result = prime * result + ((marketGroup == null) ? 0 : marketGroup.hashCode());
        result = prime * result + ((matchParameterType == null) ? 0 : matchParameterType.hashCode());
        long temp;
        temp = Double.doubleToLongBits(maxAllowedParamValue);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(minAllowedParamValue);
        result = prime * result + (int) (temp ^ (temp >>> 32));
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
        MatchParam other = (MatchParam) obj;
        if (displayAsPercentage != other.displayAsPercentage)
            return false;
        if (gaussian == null) {
            if (other.gaussian != null)
                return false;
        } else if (!gaussian.equals(other.gaussian))
            return false;
        if (marketGroup != other.marketGroup)
            return false;
        if (matchParameterType != other.matchParameterType)
            return false;
        if (Double.doubleToLongBits(maxAllowedParamValue) != Double.doubleToLongBits(other.maxAllowedParamValue))
            return false;
        if (Double.doubleToLongBits(minAllowedParamValue) != Double.doubleToLongBits(other.minAllowedParamValue))
            return false;
        return true;
    }



}
