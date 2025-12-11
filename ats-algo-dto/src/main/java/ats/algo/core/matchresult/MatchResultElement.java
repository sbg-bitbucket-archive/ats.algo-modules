package ats.algo.core.matchresult;

import java.io.Serializable;

import ats.algo.genericsupportfunctions.PairOfIntegers;

public class MatchResultElement implements Serializable {

    private static final long serialVersionUID = 1L;

    private MatchResultEntryType matchResultEntryType;
    private int minAllowedValue;
    private int maxAllowedValue;
    private String value;

    /**
     * json constructor
     */
    public MatchResultElement() {

    }

    public MatchResultElement(MatchResultEntryType matchResultEntryType, int minAllowedValue, int maxAllowedValue,
                    String value) {
        super();
        this.matchResultEntryType = matchResultEntryType;
        this.minAllowedValue = minAllowedValue;
        this.maxAllowedValue = maxAllowedValue;
        this.value = value;
    }

    public MatchResultEntryType getMatchResultEntryType() {
        return matchResultEntryType;
    }

    public void setMatchResultEntryType(MatchResultEntryType matchResultEntryType) {
        this.matchResultEntryType = matchResultEntryType;
    }

    public int getMinAllowedValue() {
        return minAllowedValue;
    }

    public void setMinAllowedValue(int minAllowedValue) {
        this.minAllowedValue = minAllowedValue;
    }

    public int getMaxAllowedValue() {
        return maxAllowedValue;
    }

    public void setMaxAllowedValue(int maxAllowedValue) {
        this.maxAllowedValue = maxAllowedValue;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     * returns the value as Integer
     * 
     * @return null if MatchResultEntry not of type INT or not of int format, otherwise the value
     */
    public Integer valueAsInteger() {
        Integer n = null;
        if (this.matchResultEntryType == MatchResultEntryType.INT)
            try {
                n = Integer.valueOf(value);
            } catch (NumberFormatException ex) {
                n = null;
            }
        return n;
    }

    /**
     * returns the value as PairofIntegers.
     * 
     * @return null if MatchResultEntry not of type INT_HYPHEN_INT or not of int-int format, otherwise the value
     */
    public PairOfIntegers valueAsPairOfIntegersCheckingNegatives() {
        PairOfIntegers p = null;
        if (this.matchResultEntryType == MatchResultEntryType.INT_HYPHEN_INT) {
            p = PairOfIntegers.generateFromStringCheckingNegatives(value);
        }
        return p;
    }

    /**
     * returns the value as PairofIntegers.
     * 
     * @return null if MatchResultEntry not of type INT_HYPHEN_INT or not of int-int format, otherwise the value
     */
    public PairOfIntegers valueAsPairOfIntegers() {
        PairOfIntegers p = null;
        if (this.matchResultEntryType == MatchResultEntryType.INT_HYPHEN_INT) {
            p = PairOfIntegers.generateFromString(value);
        }
        return p;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((matchResultEntryType == null) ? 0 : matchResultEntryType.hashCode());
        result = prime * result + maxAllowedValue;
        result = prime * result + minAllowedValue;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
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
        MatchResultElement other = (MatchResultElement) obj;
        if (matchResultEntryType != other.matchResultEntryType)
            return false;
        if (maxAllowedValue != other.maxAllowedValue)
            return false;
        if (minAllowedValue != other.minAllowedValue)
            return false;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "[matchResultEntryType=" + matchResultEntryType + ", minAllowedValue=" + minAllowedValue
                        + ", maxAllowedValue=" + maxAllowedValue + ", value=" + value + "]";
    }



}
