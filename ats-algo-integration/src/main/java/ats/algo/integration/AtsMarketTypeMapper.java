package ats.algo.integration;

import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import ats.algo.core.common.SupportedSportType;
import ats.algo.core.markets.ATSMarketTypeConverter;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.ResultedMarket;
import generated.ats.betsync.dto.Selection;

public class AtsMarketTypeMapper {
    private MarketTypeAliasResolver marketTypeAliasResolver;
    // private static final Logger log = LoggerFactory.getLogger(AtsMarketTypeMapper.class);

    public static boolean sequenceIdsAreTheSame(String s1, String s2) {
        if (s1 == s2) {
            return true;
        }

        if (s1 == null && s2 != null) {
            return s2.isEmpty();
        }

        if (s2 == null && s1 != null) {
            return s1.isEmpty();
        }

        return s1 != null && s1.equals(s2);
    }

    public String resolveAtsMarketType(TradedEvent tradedEvent, Market market,
                    boolean canResolveToLegacyAtsMarketType) {
        if (canResolveToLegacyAtsMarketType) {
            AtsMarketType marketType = resolveAtsMarketType(tradedEvent, market);
            if (marketType != null) {
                return marketType.getMarketType();
            }
        }
        return tradedEvent.toATSMarketType(market);
    }

    public String resolveAtsMarketSubType(TradedEvent tradedEvent, Market market,
                    boolean canResolveToLegacyAtsMarketType, boolean useNewStructureForMarkets) {
        if (canResolveToLegacyAtsMarketType) {
            AtsMarketType marketType = resolveAtsMarketType(tradedEvent, market);
            if (marketType != null) {
                if (useNewStructureForMarkets) {
                    String subtype = ATSMarketTypeConverter.getATSSubType(market);
                    return subtype;
                }
                return marketType.getMarketSubtype();
            }
        }
        return ATSMarketTypeConverter.getATSSubType(market);
    }



    public AtsMarketType resolveAtsMarketType(TradedEvent tradedEvent, String type, String subType) {
        String marketType = marketTypeAliasResolver.getATSMarketCode(tradedEvent, type);
        if (marketType == null || marketType.isEmpty()) {
            return null;
        }

        if (subType != null && !subType.isEmpty()) {
            String subTypeFormat = marketTypeAliasResolver.getSubTypeFormat(type);

            int count = counterString(subTypeFormat, ".");
            if (subType.contains("M")) {
                subType = subType.replaceAll("M#", "");
            }
            if (subType.contains("S")) {
                subType = subType.replaceAll("S", "");
            }
            if (subType.contains(":")) {
                subType = subType.replaceAll(":", "-");
            }
            for (int i = 0; i < count; i++) {
                subType = StringUtils.replaceOnce(subType, ".", "-");
            }
        }
        AtsMarketType atsMarketType = new AtsMarketType(marketType, subType);
        return atsMarketType;
    }

    public AtsMarketType resolveAtsMarketType(TradedEvent event, Market market) {
        return resolveAtsMarketType(event, market.getType(), market.getATSSubType());
    }

    public AtsMarketType resolveAtsMarketType(TradedEvent event, ResultedMarket market) {
        return resolveAtsMarketType(event, market.getType(), market.getSequenceId());
    }

    public Selection resolveAtsSelectionType(Entry<String, Double> selnEntry) {
        String algoSelection = selnEntry.getKey();
        Selection atsSeln = new Selection();
        String ATSSelectionType = marketTypeAliasResolver.getATSSelectionType(algoSelection);
        String ATSSelectionName = marketTypeAliasResolver.getATSSelectionName(algoSelection);
        atsSeln.setName(ATSSelectionName);
        atsSeln.setType(ATSSelectionType);
        return atsSeln;
    }

    public String resolveAtsSelectionType(String algoSelection) {
        String oldSelectionType = marketTypeAliasResolver.getATSSelectionType(algoSelection);
        return oldSelectionType == null || oldSelectionType.isEmpty() ? algoSelection : oldSelectionType;
    }

    public class AtsMarketType {
        private String marketType;
        private String marketSubtype;

        public AtsMarketType(String marketType, String marketSubtype) {
            this.marketType = marketType;
            this.marketSubtype = marketSubtype;
        }

        public String getMarketType() {
            return marketType;
        }

        public String getMarketSubtype() {
            return marketSubtype;
        }

        @Override
        public String toString() {
            return "AtsMarketType [marketType=" + marketType + ", marketSubtype=" + marketSubtype + "]";
        }
    }

    public static void main(String[] args) {
        AtsMarketTypeMapper amtm = new AtsMarketTypeMapper();
        DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource marketMappingFile = resourceLoader.getResource("classpath:marketMappingFileLC.csv");
        MarketTypeAliasResolver marketTypeAliasResolver = new MarketTypeAliasResolver();
        marketTypeAliasResolver.setMarketTypeAliasFile(marketMappingFile);
        marketTypeAliasResolver.init();
        String newMarketType = "FT:AXB";
        String print = marketTypeAliasResolver
                        .getATSMarketCode(new TradedEvent(10L, SupportedSportType.SOCCER.toString()), newMarketType);
        System.out.println(print);
        amtm.setMarketTypeAliasResolver(marketTypeAliasResolver);
        String algoSelection = "home";
        String atsSelectionType = marketTypeAliasResolver.getATSSelectionType(algoSelection);
        System.out.println(atsSelectionType);
        String atsSelectionName = marketTypeAliasResolver.getATSSelectionName(algoSelection);
        System.out.println(atsSelectionName);
        newMarketType = "ET:AXB";
        algoSelection = "line";
        print = marketTypeAliasResolver.getATSMarketCode(new TradedEvent(10L, SupportedSportType.SOCCER.toString()),
                        newMarketType);
        System.out.println(print);
        atsSelectionType = marketTypeAliasResolver.getATSSelectionType(algoSelection);
        System.out.println(atsSelectionType);
        atsSelectionName = marketTypeAliasResolver.getATSSelectionName(algoSelection);
        System.out.println(atsSelectionName);
    }

    private int counterString(String s, String search) {
        int times = 0;
        int index = s.indexOf(search, 0);
        while (index > 0) {
            index = s.indexOf(search, index + 1);
            ++times;
        }
        return times;
    }

    public void setMarketTypeAliasResolver(MarketTypeAliasResolver marketTypeAliasResolver) {
        this.marketTypeAliasResolver = marketTypeAliasResolver;
    }

    public boolean canResolveToLegacyAtsMarketType(TradedEvent tradedEvent) {
        String sportStr = tradedEvent.getSport().toString().toLowerCase();
        String mappingFileProperty = sportStr + ".mappingFile";
        String mappingFileUsed = System.getProperty(mappingFileProperty);

        if (mappingFileUsed == null) {
            return false;
        } else {
            return true;
        }
    }

    public boolean useNewStructureForMarkets(TradedEvent tradedEvent) {
        String sportStr = tradedEvent.getSport().toString().toLowerCase();
        String mappingFileProperty = sportStr + ".useNewStructureForMarkets";
        String mappingFileUsed = System.getProperty(mappingFileProperty);

        if (mappingFileUsed != null) {
            if (mappingFileUsed.equals("true")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
