package ats.algo.sport.rollerhockey.tradingrules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ats.algo.core.markets.DerivedMarketSpec;
import ats.algo.core.markets.DerivedMarketSpecApplicability;
import ats.algo.core.tradingrules.DerivedMarketTradingRule;
import ats.algo.core.tradingrules.TradingRules;

public class RollerhockeyTradingRules extends TradingRules {
    // private static final int HALFTIMEINSECONDS = 1800;
    public static final String ALL_MARKET = "All_MARKET";
    private List<String> prematchDisplayMarketList = new ArrayList<String>(Arrays.asList(ALL_MARKET));
    private List<String> inplayDisplayMarketList = new ArrayList<String>(Arrays.asList(ALL_MARKET));

    private List<String> suspendMarketList17Minutes =
                    new ArrayList<String>(Arrays.asList("P:AXB_P1", "P:OU_P1", "P:BTTS_P1", "P:OU_P1", "P:CS_P1"));

    private List<String> suspendMarketList37Minutes =
                    new ArrayList<String>(Arrays.asList("P:AXB_P2", "P:OU_P2", "P:BTTS_P2", "P:OU_P2", "P:CS_P2"));
    private List<String> suspendMarketList38Minutes = new ArrayList<String>(Arrays.asList(ALL_MARKET));

    public RollerhockeyTradingRules() {
        super();
        for (int i = 0; i < 6; i++) {
            RollerhockeyTradingRule rollerhockeyTradingRule =
                            new RollerhockeyTradingRule("Rollerhockey trading rule", i, null);
            rollerhockeyTradingRule.setPrematchDisplayMarketList(prematchDisplayMarketList);
            rollerhockeyTradingRule.setInplayDisplayMarketList(inplayDisplayMarketList);
            rollerhockeyTradingRule.addInPlaySuspendAtTimeMarketList(suspendMarketList17Minutes,
                            17 * SECONDS_IN_A_MINUTE);
            rollerhockeyTradingRule.addInPlaySuspendAtTimeMarketList(suspendMarketList37Minutes,
                            37 * SECONDS_IN_A_MINUTE);
            rollerhockeyTradingRule.addInPlaySuspendAtTimeMarketList(suspendMarketList38Minutes,
                            38 * SECONDS_IN_A_MINUTE);
            addRule(rollerhockeyTradingRule);

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
            // (MatchState ms) -> ((RollerhockeyMatchState) ms).getElapsedTimeSecs() < (55 * SECONDS_IN_A_MINUTE);
            //
            // addRule(new DerivedMarketTradingRule(i, "FT:SPRD", inPlayLinesToDisplaySpec, r));
            // addRule(new DerivedMarketTradingRule(i, "FT:OU", inPlayLinesToDisplaySpec, r));

            addRule(new RollerhockeySuspendToAwaitParamFindTradingRule());// SUSPEND EVENT WHEN GOAL HAPPENS

        }

    }



}
