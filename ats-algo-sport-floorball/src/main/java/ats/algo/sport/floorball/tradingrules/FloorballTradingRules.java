package ats.algo.sport.floorball.tradingrules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ats.algo.core.markets.DerivedMarketSpec;
import ats.algo.core.markets.DerivedMarketSpecApplicability;
import ats.algo.core.tradingrules.DerivedMarketTradingRule;
import ats.algo.core.tradingrules.TradingRules;

public class FloorballTradingRules extends TradingRules {
    // private static final int HALFTIMEINSECONDS = 1800;
    public static final String ALL_MARKET = "All_MARKET";
    private List<String> prematchDisplayMarketList = new ArrayList<String>(Arrays.asList(ALL_MARKET));
    private List<String> inplayDisplayMarketList = new ArrayList<String>(Arrays.asList(ALL_MARKET));

    private List<String> suspendMarketList18Minutes = new ArrayList<String>(Arrays.asList("P:AXB_P1", "P:OU_P1"));

    private List<String> suspendMarketList38Minutes = new ArrayList<String>(Arrays.asList("P:AXB_P2", "P:OU_P2"));
    private List<String> suspendMarketList56Minutes = new ArrayList<String>(
                    Arrays.asList("P:AXB_P3", "P:OU_P3", "FT:SPRD", "FT:A:OU", "FT:B:OU", "FT:EH"));
    private List<String> suspendMarketList58Minutes = new ArrayList<String>(Arrays.asList(ALL_MARKET));

    public FloorballTradingRules() {
        super();
        for (int i = 0; i < 6; i++) {
            FloorballTradingRule floorballTradingRule = new FloorballTradingRule("Floorball trading rule", i, null);
            floorballTradingRule.setPrematchDisplayMarketList(prematchDisplayMarketList);
            floorballTradingRule.setInplayDisplayMarketList(inplayDisplayMarketList);
            floorballTradingRule.addInPlaySuspendAtTimeMarketList(suspendMarketList18Minutes, 18 * SECONDS_IN_A_MINUTE);
            floorballTradingRule.addInPlaySuspendAtTimeMarketList(suspendMarketList38Minutes, 38 * SECONDS_IN_A_MINUTE);
            floorballTradingRule.addInPlaySuspendAtTimeMarketList(suspendMarketList56Minutes, 56 * SECONDS_IN_A_MINUTE);
            floorballTradingRule.addInPlaySuspendAtTimeMarketList(suspendMarketList58Minutes, 58 * SECONDS_IN_A_MINUTE);
            addRule(floorballTradingRule);

            DerivedMarketSpec preMatchLinesToDisplaySpec = DerivedMarketSpec
                            .getDerivedMarketSpecForRegularLines(DerivedMarketSpecApplicability.PRE_MATCH_ONLY, 1, 1);
            addRule(new DerivedMarketTradingRule(i, "FT:SPRD", preMatchLinesToDisplaySpec));
            addRule(new DerivedMarketTradingRule(i, "FT:OU", preMatchLinesToDisplaySpec));

            /**
             * Derived markets creation rule
             */
            // DerivedMarketSpec inPlayLinesToDisplaySpec = DerivedMarketSpec
            // .getDerivedMarketSpecForRegularLines(DerivedMarketSpecApplicability.IN_PLAY_ONLY,
            // 2, 1);
            //
            // DerivedMarketsStopingCriterias<MatchState> r =
            // (MatchState ms) -> ((FloorballMatchState)
            // ms).getElapsedTimeSecs() < (55 * SECONDS_IN_A_MINUTE);
            //
            // addRule(new DerivedMarketTradingRule(i, "FT:SPRD",
            // inPlayLinesToDisplaySpec, r));
            // addRule(new DerivedMarketTradingRule(i, "FT:OU",
            // inPlayLinesToDisplaySpec, r));

            addRule(new FloorballSuspendToAwaitParamFindTradingRule());// SUSPEND
                                                                       // EVENT
                                                                       // WHEN
                                                                       // GOAL
                                                                       // HAPPENS

        }

    }

}
