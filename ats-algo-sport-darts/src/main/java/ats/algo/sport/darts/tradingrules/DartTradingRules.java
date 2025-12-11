package ats.algo.sport.darts.tradingrules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ats.algo.core.tradingrules.TradingRules;

public class DartTradingRules extends TradingRules {
    public static final String ALL_MARKET = "All_MARKET";
    private List<String> prematchDisplayMarketList = new ArrayList<String>(Arrays.asList("FT:CS", "FT:ML", "FT:OE",
                    "FT:OUS", "FT:SPRD", "G:ML", "G:OU", "G:SPRD", "P:CS", "P:ML", "P:OE", "P:OU", "P:SPRD"));

    private List<String> inplayDisplayMarketList = new ArrayList<String>(Arrays.asList("FT:CS", "FT:ML", "FT:OE",
                    "FT:OUS", "FT:SPRD", "G:ML", "G:OU", "G:SPRD", "P:CS", "P:ML", "P:OE", "P:OU", "P:SPRD"));


    public DartTradingRules() {
        super();
        DartTradingRule rule = new DartTradingRule("Tier Two Dart trading rule ", null, null);
        rule.setPrematchDisplayMarketList(prematchDisplayMarketList);
        rule.setInplayDisplayMarketList(inplayDisplayMarketList);
        addRule(rule);
    }
}
