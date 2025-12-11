package ats.algo.sport.handball.tradingrules.ilt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.markets.DerivedMarketSpec;
import ats.algo.core.markets.DerivedMarketSpecApplicability;
import ats.algo.core.tradingrules.DerivedMarketTradingRule;
import ats.algo.core.tradingrules.DerivedMarketsStopingCriterias;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.sport.handball.HandballMatchState;
import ats.algo.sport.handball.tradingrules.HandballTradingRule;

public class IntralotHandballTradingRules extends TradingRules {
    private List<String> allTiersAcceptedMarketList = Arrays.asList("FT:A:OU", "FT:AHCP", "FT:ATG", "FT:AXB",
                    "FT:AXB$OU", "FT:B:OU", "FT:DBLC", "FT:DNB", "FT:HSH", "FT:HTFT", "FT:ML", "FT:OE", "FT:OU",
                    "FT:RX", "FT:WM", "FT:WSPX", "P:AHCP", "P:ATG", "P:AXB", "P:DBLC", "P:DNB", "P:3HCP", "P:OE");

    public static final String ALL_MARKET = "All_MARKET";
    private List<String> prematchDisplayMarketList = new ArrayList<String>(Arrays.asList(ALL_MARKET));
    private List<String> inplayDisplayMarketList = new ArrayList<String>(Arrays.asList(ALL_MARKET));
    private List<String> halfTimeSuspendMarketList28Minutes = new ArrayList<String>(
                    Arrays.asList("P:OU", "FT:A:OU", "P:DNB", "P:SPRD", "OT:NG", "P:AXB_P1", "P:OU", "P:DBLC"));
    private List<String> halfTimeSuspendMarketList50Minutes = new ArrayList<String>(Arrays.asList("FT:DBLC"));
    private List<String> halfTimeSuspendMarketList55Minutes = new ArrayList<String>(Arrays.asList("FT:DNB", "FT:AXB"));
    private List<String> halfTimeSuspendMarketList57Minutes = new ArrayList<String>(Arrays.asList("P:OU", "FT:A:OU",
                    "FT:OU", "P:DNB", "P:SPRD", "OT:NG", "P:AXB_P1", "P:OU", "FT:SPRD", "P:DBLC", "FT:B:OU", "P:AXB"));
    private List<String> halfTimeSuspendMarketList58Minutes = new ArrayList<String>(Arrays.asList(ALL_MARKET));

    public IntralotHandballTradingRules() {
        super();

        HandballTradingRule handballTradingRule =
                        new HandballTradingRule("Intralot Handball All Tiers Trading Rule", null, null);

        handballTradingRule.setPrematchDisplayMarketList(allTiersAcceptedMarketList);
        handballTradingRule.setInplayDisplayMarketList(allTiersAcceptedMarketList);
        handballTradingRule.setAcceptedMarketsList(allTiersAcceptedMarketList);

        handballTradingRule.setPrematchDisplayMarketList(prematchDisplayMarketList);
        handballTradingRule.setInplayDisplayMarketList(inplayDisplayMarketList);
        handballTradingRule.addInPlaySuspendAtTimeMarketList(halfTimeSuspendMarketList28Minutes,
                        28 * SECONDS_IN_A_MINUTE);
        handballTradingRule.addInPlaySuspendAtTimeMarketList(halfTimeSuspendMarketList50Minutes,
                        50 * SECONDS_IN_A_MINUTE);
        handballTradingRule.addInPlaySuspendAtTimeMarketList(halfTimeSuspendMarketList55Minutes,
                        55 * SECONDS_IN_A_MINUTE);
        handballTradingRule.addInPlaySuspendAtTimeMarketList(halfTimeSuspendMarketList57Minutes,
                        57 * SECONDS_IN_A_MINUTE);
        handballTradingRule.addInPlaySuspendAtTimeMarketList(halfTimeSuspendMarketList58Minutes,
                        58 * SECONDS_IN_A_MINUTE);

        DerivedMarketSpec preMatchLinesToDisplaySpec = DerivedMarketSpec
                        .getDerivedMarketSpecForRegularLines(DerivedMarketSpecApplicability.PRE_MATCH_ONLY, 1, 1);
        addRule(new DerivedMarketTradingRule(null, "FT:SPRD", preMatchLinesToDisplaySpec));
        addRule(new DerivedMarketTradingRule(null, "FT:OU", preMatchLinesToDisplaySpec));

        DerivedMarketSpec inPlayLinesToDisplaySpec = DerivedMarketSpec
                        .getDerivedMarketSpecForRegularLines(DerivedMarketSpecApplicability.IN_PLAY_ONLY, 2, 1);

        DerivedMarketsStopingCriterias<AlgoMatchState> r = (AlgoMatchState ms) -> ((HandballMatchState) ms)
                        .getElapsedTimeSecs() < (55 * SECONDS_IN_A_MINUTE);

        addRule(new DerivedMarketTradingRule(null, "FT:SPRD", inPlayLinesToDisplaySpec, r));



        addRule(handballTradingRule);



    }
}
