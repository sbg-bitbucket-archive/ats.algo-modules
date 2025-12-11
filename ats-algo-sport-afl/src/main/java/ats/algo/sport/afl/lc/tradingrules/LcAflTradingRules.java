package ats.algo.sport.afl.lc.tradingrules;

import static ats.algo.sport.afl.tradingrules.AflTradingList.EXTRA_TIME_OPEN_MARKET;
import static ats.algo.sport.afl.tradingrules.AflTradingList.FIRST_HALF_SUSPEND_MARKET;
import static ats.algo.sport.afl.tradingrules.AflTradingList.FULL_TIME_SUSPEND_MARKET;
import static ats.algo.sport.afl.tradingrules.AflTradingList.INPLAY_OPEN_MARKET;
import static ats.algo.sport.afl.tradingrules.AflTradingList.PREMATCH_OPEN_MARKET;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ats.algo.core.markets.DerivedMarketSpec;
import ats.algo.core.markets.DerivedMarketSpecApplicability;
import ats.algo.core.tradingrules.DerivedMarketTradingRule;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.sport.afl.tradingrules.AflTradingRule;

public class LcAflTradingRules extends TradingRules {
    private List<String> prematchDisplayMarketList = new ArrayList<String>(Arrays.asList(PREMATCH_OPEN_MARKET));
    private List<String> inplayDisplayMarketList = new ArrayList<String>(Arrays.asList(INPLAY_OPEN_MARKET));
    private List<String> halfTimeSuspendMarketList = new ArrayList<String>(Arrays.asList(FIRST_HALF_SUSPEND_MARKET));
    private List<String> fullTimeSuspendMarketList = new ArrayList<String>(Arrays.asList(FULL_TIME_SUSPEND_MARKET));
    private List<String> extraTimeOpenMarketList = new ArrayList<String>(Arrays.asList(EXTRA_TIME_OPEN_MARKET));
    private int halfTimeSuspend = 2;
    private int fullTimeSuspend = 4;



    public LcAflTradingRules() {
        /*
         * add rule to control market display and suspension logic for tier 1 markets
         */



        AflTradingRule aflTradingRule = new AflTradingRule("Afl trading rule", 1, null);
        aflTradingRule.setPrematchDisplayMarketList(prematchDisplayMarketList);
        aflTradingRule.setInplayDisplayMarketList(inplayDisplayMarketList);
        aflTradingRule.setHalfTimeSuspend(halfTimeSuspend);
        aflTradingRule.setFullTimeSuspend(fullTimeSuspend);
        aflTradingRule.setHalfTimeSuspendMarketList(halfTimeSuspendMarketList);
        aflTradingRule.setFullTimeSuspendMarketList(fullTimeSuspendMarketList);
        aflTradingRule.setExtraTimeOpenMarketList(extraTimeOpenMarketList);
        addRule(aflTradingRule);
        /*
         * add derived market dynamicRange hcap
         */
        DerivedMarketSpec derivedMarketSpec = DerivedMarketSpec.getDerivedMarketSpecForDynamicRangeHcap("FT:SPRD_DR1",
                        "Winning margin", DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, 6, 10, true);
        DerivedMarketTradingRule derivedMarketTradingRule =
                        new DerivedMarketTradingRule(2, "FT:SPRD", derivedMarketSpec);
        addRule(derivedMarketTradingRule);
        /*
         * add another derived market dynamicRange hcap
         */
        DerivedMarketSpec derivedMarketSpec2 = DerivedMarketSpec.getDerivedMarketSpecForDynamicRangeHcap("FT:SPRD_DR2",
                        "Winning margin", DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, 4, 10, true);
        DerivedMarketTradingRule derivedMarketTradingRule2 =
                        new DerivedMarketTradingRule(2, "FT:SPRD", derivedMarketSpec2);
        addRule(derivedMarketTradingRule2);
        /*
         * add derived market dynamicRange total
         */
        DerivedMarketSpec derivedMarketSpec3 = DerivedMarketSpec.getDerivedMarketSpecForDynamicRangeTotal("FT:OU_DR3",
                        "Total points", DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, 4, 20, 150);
        DerivedMarketTradingRule derivedMarketTradingRule3 =
                        new DerivedMarketTradingRule(2, "FT:OU", derivedMarketSpec3);
        addRule(derivedMarketTradingRule3);
        /*
         * add extra lines each side of balanced line for FT:SPRD
         */
        DerivedMarketSpec derivedMarketSpec4 = DerivedMarketSpec.getDerivedMarketSpecForRegularLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, 3, 2);
        DerivedMarketTradingRule derivedMarketTradingRule4 =
                        new DerivedMarketTradingRule(2, "FT:SPRD", derivedMarketSpec4);
        addRule(derivedMarketTradingRule4);

        DerivedMarketSpec derivedMarketSpec5 = DerivedMarketSpec.getDerivedMarketSpecForDynamicRangeHcap("FT:SPRD_DR5",
                        "Winning margin", DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, 5, 20, true);
        DerivedMarketTradingRule derivedMarketTradingRule5 =
                        new DerivedMarketTradingRule(null, "FT:SPRD", derivedMarketSpec5);
        addRule(derivedMarketTradingRule5);
        /*
         * add an example of lines near 50-50
         */
        DerivedMarketSpec derivedMarketSpec6 = DerivedMarketSpec.getDerivedMarketSpecForLinesNearestEvens("FT:SPRD_DR6",
                        "Nearest evens example market", DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, 4);
        DerivedMarketTradingRule derivedMarketTradingRule6 =
                        new DerivedMarketTradingRule(2, "FT:SPRD", derivedMarketSpec6);
        addRule(derivedMarketTradingRule6);

    }
}
