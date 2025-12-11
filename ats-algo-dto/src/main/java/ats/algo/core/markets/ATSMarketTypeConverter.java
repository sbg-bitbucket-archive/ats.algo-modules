package ats.algo.core.markets;

import com.google.common.base.Strings;

public class ATSMarketTypeConverter {


    /**
     * ATS does not have the concept of a sequence sadly, so this method must combine the {@link Market#getLineId()
     * subType} with {@link Market#getSequenceId() sequenceId} to create a unique sub type in ATS.
     * 
     * @param market the Algo Manager market
     */
    public static String getATSSubType(Market market) {
        String subType = Strings.emptyToNull(market.getLineId());
        String sequenceId = Strings.emptyToNull(market.getSequenceId());

        return generateATSSubType(market.getCategory(), subType, sequenceId);
    }

    public static String getATSSubType(ResultedMarket market) {
        String subType = Strings.emptyToNull(market.getLineId());
        String sequenceId = Strings.emptyToNull(market.getSequenceId());
        return generateATSSubType(market.getCategory(), subType, sequenceId);
    }

    private static String generateATSSubType(MarketCategory cat, String subType, String sequenceId) {
        if (subType == null) {
            return sequenceId == null ? null : sequenceId;
        } else {
            if (cat != MarketCategory.GENERAL && !subType.isEmpty() && subType.endsWith("0")) {
                subType = String.valueOf(Double.parseDouble(subType));
            }
            return sequenceId == null ? subType : sequenceId + "#" + subType;
        }
    }


    public static boolean startsWithALetter(String atsSubTypeOrAlgoLine) {
        return atsSubTypeOrAlgoLine != null && !atsSubTypeOrAlgoLine.isEmpty()
                        && Character.isLetter(atsSubTypeOrAlgoLine.charAt(0));
    }

    public static MarketCategory extractMarketCategory(String atsMarketType) {
        int typeIdx = atsMarketType.indexOf(':');
        if (typeIdx == -1) {
            throw new IllegalArgumentException("invalid ATS market type format : " + atsMarketType);
        }

        String[] parts = atsMarketType.split(":");
        String marketFamily = parts[1];

        if (marketFamily.endsWith("OU")) {
            return MarketCategory.OVUN;
        }

        if (marketFamily.endsWith("SPRD")) {
            return MarketCategory.HCAP;
        }

        if (marketFamily.endsWith("SPRDEU")) {
            return MarketCategory.HCAPEU;
        }

        return MarketCategory.GENERAL;
    }

}
