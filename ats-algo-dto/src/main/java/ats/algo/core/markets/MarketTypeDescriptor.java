package ats.algo.core.markets;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.base.CharMatcher;

/**
 * used for ATS integration. Not for general use
 * 
 * @author Geoff
 *
 */
public class MarketTypeDescriptor {

    private static Map<MarketTypeDescriptor, MarketTypeDescriptor> internedTypes =
                    new ConcurrentHashMap<MarketTypeDescriptor, MarketTypeDescriptor>(2096, 0.9f, 1);

    private MarketCategory category; // "OVUN", "HCAP" or "GENERAL"
    private String type; // the unique key for this market
    private String subType; // if category = "OVUN" or "HCAP" then the line
    private String sequenceId;

    public MarketTypeDescriptor(MarketCategory category, String type, String subType, String sequenceId) {
        super();
        this.category = category;
        this.type = type;
        if (category != MarketCategory.GENERAL && subType != null && subType.endsWith("0")) {
            this.subType = String.valueOf(tryParseLine(subType));
        } else {
            this.subType = subType;
        }
        this.sequenceId = sequenceId;
    }

    public MarketTypeDescriptor intern() {
        MarketTypeDescriptor interned = internedTypes.get(this);
        if (interned == null) {
            internedTypes.put(this, interned = this);
        }
        return interned;
    }

    public MarketCategory getCategory() {
        return category;
    }

    public String getType() {
        return type;
    }

    public String getSubType() {
        return subType;
    }

    public Double getLine() {
        if (category != MarketCategory.GENERAL) {
            return tryParseLine(subType);
        }
        return null;
    }

    public String getSequenceId() {
        return sequenceId;
    }

    protected Double tryParseLine(String potentialLine) {
        try {
            return Double.parseDouble(potentialLine);
        } catch (NumberFormatException nfe) {
            return Double.parseDouble(CharMatcher.anyOf("-.0123456789").retainFrom(potentialLine));
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((category == null) ? 0 : category.hashCode());
        result = prime * result + ((subType == null) ? 0 : subType.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((sequenceId == null) ? 0 : sequenceId.hashCode());
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
        MarketTypeDescriptor other = (MarketTypeDescriptor) obj;
        if (category != other.category)
            return false;
        if (subType == null) {
            if (other.subType != null)
                return false;
        } else if (!subType.equals(other.subType))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        if (sequenceId == null) {
            if (other.sequenceId != null)
                return false;
        } else if (!sequenceId.equals(other.sequenceId))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(category).append(' ').append(type);
        if (subType != null && !subType.isEmpty()) {
            sb.append(' ').append(subType);
        }
        if (sequenceId != null && !sequenceId.isEmpty()) {
            sb.append(' ').append(sequenceId);
        }
        return sb.toString();
    }

    public String toToken() {
        StringBuilder sb = new StringBuilder();
        sb.append(category.name());
        sb.append(" ");
        sb.append(type);
        sb.append(" ");
        sb.append(subType);
        sb.append(" ");
        sb.append(sequenceId);
        return sb.toString();
    }

    public static MarketTypeDescriptor parseToken(String token) {
        MarketTypeDescriptor fromToken = null;
        String[] split = token.split(" ");
        if (split.length == 4) {
            fromToken = new MarketTypeDescriptor(MarketCategory.valueOf(split[0]), split[1], nullStr2Null(split[2]),
                            nullStr2Null(split[3]));
        }
        return fromToken;
    }

    private static String nullStr2Null(String string) {
        return "null".equals(string) ? null : string;
    }
}
