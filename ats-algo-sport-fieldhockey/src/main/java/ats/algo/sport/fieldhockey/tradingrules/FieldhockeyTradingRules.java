package ats.algo.sport.fieldhockey.tradingrules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ats.algo.core.markets.DerivedMarketSpec;
import ats.algo.core.markets.DerivedMarketSpecApplicability;
import ats.algo.core.tradingrules.DerivedMarketTradingRule;
import ats.algo.core.tradingrules.TradingRules;

public class FieldhockeyTradingRules extends TradingRules {
    // private static final int HALFTIMEINSECONDS = 1800;
    public static final String ALL_MARKET = "All_MARKET";
    private List<String> prematchDisplayMarketList = new ArrayList<String>(Arrays.asList(ALL_MARKET));
    private List<String> inplayDisplayMarketList = new ArrayList<String>(Arrays.asList(ALL_MARKET));

    private List<String> suspendMarketList13Minutes =
                    new ArrayList<String>(Arrays.asList("P:AXB_P1", "P:OU_P1", "P:CS_P1", "P:BTTS_P1"));

    private List<String> suspendMarketList28Minutes =
                    new ArrayList<String>(Arrays.asList("P:AXB_P2", "P:OU_P2", "P:CS_P2", "P:BTTS_P2"));
    private List<String> suspendMarketList43Minutes =
                    new ArrayList<String>(Arrays.asList("P:AXB_P3", "P:OU_P3", "P:CS_P3", "P:BTTS_P3"));
    private List<String> suspendMarketList55Minutes = new ArrayList<String>(Arrays.asList("P:AXB_P4", "P:OU_P4",
                    "P:CS_P4", "P:BTTS_P4", "FT:DBLC", "FT:EH", "FT:SPRD", "OT:NG", "FT:A:OU", "FT:B:OU", "FT:CS"));
    private List<String> suspendMarketList58Minutes = new ArrayList<String>(Arrays.asList(ALL_MARKET));

    public FieldhockeyTradingRules() {
        super();
        for (int i = 0; i < 6; i++) {
            FieldhockeyTradingRule fieldhockeyTradingRule =
                            new FieldhockeyTradingRule("Fieldhockey trading rule", i, null);
            fieldhockeyTradingRule.setPrematchDisplayMarketList(prematchDisplayMarketList);
            fieldhockeyTradingRule.setInplayDisplayMarketList(inplayDisplayMarketList);
            fieldhockeyTradingRule.addInPlaySuspendAtTimeMarketList(suspendMarketList13Minutes,
                            13 * SECONDS_IN_A_MINUTE);
            fieldhockeyTradingRule.addInPlaySuspendAtTimeMarketList(suspendMarketList28Minutes,
                            28 * SECONDS_IN_A_MINUTE);
            fieldhockeyTradingRule.addInPlaySuspendAtTimeMarketList(suspendMarketList43Minutes,
                            43 * SECONDS_IN_A_MINUTE);
            fieldhockeyTradingRule.addInPlaySuspendAtTimeMarketList(suspendMarketList55Minutes,
                            55 * SECONDS_IN_A_MINUTE);
            fieldhockeyTradingRule.addInPlaySuspendAtTimeMarketList(suspendMarketList58Minutes,
                            58 * SECONDS_IN_A_MINUTE);
            addRule(fieldhockeyTradingRule);

            DerivedMarketSpec preMatchLinesToDisplaySpec = DerivedMarketSpec
                            .getDerivedMarketSpecForRegularLines(DerivedMarketSpecApplicability.PRE_MATCH_ONLY, 1, 1);
            addRule(new DerivedMarketTradingRule(i, "FT:SPRD", preMatchLinesToDisplaySpec));
            addRule(new DerivedMarketTradingRule(i, "FT:OU", preMatchLinesToDisplaySpec));

            /**
             * Derived markets creation rule
             */
            // DerivedMarketSpec inPlayLinesToDisplaySpec = DerivedMarketSpec
            // .getDerivedMarketSpecForRegularLines(DerivedMarketSpecApplicability.IN_PLAY_ONLY, 2, 1);
            //
            // DerivedMarketsStopingCriterias<MatchState> r =
            // (MatchState ms) -> ((FieldhockeyMatchState) ms).getElapsedTimeSecs() < (55 * SECONDS_IN_A_MINUTE);
            //
            // addRule(new DerivedMarketTradingRule(i, "FT:SPRD", inPlayLinesToDisplaySpec, r));
            // addRule(new DerivedMarketTradingRule(i, "FT:OU", inPlayLinesToDisplaySpec, r));

            addRule(new FieldhockeySuspendToAwaitParamFindTradingRule());// SUSPEND EVENT WHEN GOAL HAPPENS

        }

    }



}
