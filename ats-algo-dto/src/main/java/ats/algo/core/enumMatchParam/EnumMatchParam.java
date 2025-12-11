package ats.algo.core.enumMatchParam;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchParam;
import ats.algo.core.baseclasses.MatchParamType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EnumMatchParam extends MatchParam {

    private static final long serialVersionUID = 1L;

    private String value;
    private String[] allowedValues;


    @JsonCreator
    public EnumMatchParam(@JsonProperty("description") String description, @JsonProperty("value") String value,
                    @JsonProperty("allowedValues") String[] allowedValues) {
        super();
        super.setMatchParameterType(MatchParamType.ENUM);
        super.setMarketGroup(MarketGroup.NOT_SPECIFIED);
        super.setDescription(description);
        this.allowedValues = allowedValues;
        if (!this.validEnum(value))
            throw new IllegalArgumentException(
                            "value " + value + "not in supplied array of allowed values " + this.validSelectionsStr());
        this.value = value;

    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String[] getAllowedValues() {
        return allowedValues;
    }

    public void setAllowedValues(String[] allowedValues) {
        this.allowedValues = allowedValues;
    }

    @Override
    public void setEqualTo(MatchParam other) {
        super.setDescription(other.getDescription());
        this.value = ((EnumMatchParam) other).getValue();
        this.allowedValues = ((EnumMatchParam) other).getAllowedValues();
    }

    @Override
    public MatchParam copy() {
        MatchParam cc = new EnumMatchParam(super.getDescription(), this.value, this.allowedValues);
        return cc;
    }

    public boolean validEnum(String value) {
        boolean valueInAllowedValues = false;
        for (String allowedValue : allowedValues)
            if (value.equals(allowedValue)) {
                valueInAllowedValues = true;
                break;
            }
        return valueInAllowedValues;
    }

    public String validSelectionsStr() {
        String str = "{";
        for (int i = 0; i < allowedValues.length; i++) {
            str += allowedValues[i];
            if (i != allowedValues.length - 1)
                str += ", ";
        }

        str += "}";
        return str;
    }



}
