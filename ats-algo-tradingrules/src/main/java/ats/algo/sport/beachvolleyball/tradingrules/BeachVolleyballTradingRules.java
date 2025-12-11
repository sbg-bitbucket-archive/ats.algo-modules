package ats.algo.sport.beachvolleyball.tradingrules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ats.algo.core.tradingrules.TradingRules;

public class BeachVolleyballTradingRules extends TradingRules {
    private List<String> setPointSuspendMarketList = new ArrayList<String>(Arrays.asList("P:PW"));
    private List<String> finalSetSuspendMarketList =
                    new ArrayList<String>(Arrays.asList("P:ML", "FT:PSPRD", "FT:CS", "FT:OU"));
    private List<String> firstSetSuspendMarketList = new ArrayList<String>(Arrays.asList("FT:SPRD"));
    private List<String> prematchDisplayMarketList = new ArrayList<String>(Arrays.asList("FT:CS", "FT:ML", "FT:OU",
                    "FT:A:OU", "FT:B:OU", "FT:SPRD", "FT:PSPRD", "P:ML", "P:PW", "P:SPRD", "P:OU", "P:PW", "P:PSPRD"));
    private List<String> inplayDisplayMarketList = new ArrayList<String>(Arrays.asList("FT:CS", "FT:ML", "FT:OU",
                    "FT:A:OU", "FT:B:OU", "FT:PSPRD", "P:ML", "P:PW", "P:SPRD", "P:OU", "P:PSPRD", "FT:SPRD"));

    public BeachVolleyballTradingRules() {
        super();
        BeachVolleyballTradingRule beachVolleyballTradingRule =
                        new BeachVolleyballTradingRule("BeachVolleyball Coral/Lad trading rules", 3, null);
        beachVolleyballTradingRule.setSetPointSuspendMarketList(setPointSuspendMarketList);
        beachVolleyballTradingRule.setFinalSetSuspendMarketList(finalSetSuspendMarketList);
        beachVolleyballTradingRule.setFirstSetSuspendMarketList(firstSetSuspendMarketList);
        beachVolleyballTradingRule.setPrematchDisplayMarketList(prematchDisplayMarketList);
        beachVolleyballTradingRule.setInplayDisplayMarketList(inplayDisplayMarketList);
        addRule(beachVolleyballTradingRule);



    }
}
