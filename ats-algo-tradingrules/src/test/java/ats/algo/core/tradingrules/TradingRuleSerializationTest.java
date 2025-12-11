package ats.algo.core.tradingrules;

import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.core.markets.DerivedMarketSpec;
import ats.algo.core.markets.DerivedMarketSpecApplicability;
import ats.algo.core.markets.SuspensionStatus;
import ats.algo.genericsupportfunctions.JsonSerializer;
import ats.algo.sport.afl.tradingrules.AflTradingRules;
import ats.algo.sport.generic.tradingrules.ElapsedTimeTradingRule;
import ats.algo.sport.tennis.tradingrules.TennisTradingRules;

public class TradingRuleSerializationTest {

    @Test
    public void testRuleSet() {
        TennisTradingRules ttr1 = new TennisTradingRules();
        for (AbstractTradingRule tr : ttr1.getTradingRules()) {
            try {
                String json = JsonSerializer.serialize(tr, false);
                System.out.println(json);
            } catch (Exception e) {
                fail();
            }
        }
    }

    @Test
    public void test1() {
        ElapsedTimeTradingRule rule1 = new ElapsedTimeTradingRule("Test rule 1", 3, null);
        rule1.setDefaultStatusPreMatch(SuspensionStatus.OPEN);
        rule1.setDefaultStatusInPlay(SuspensionStatus.OPEN);
        rule1.setStatusInAmberZone(SuspensionStatus.SUSPENDED_DISPLAY);
        rule1.setStatusInRedZone(SuspensionStatus.SUSPENDED_DISPLAY);
        rule1.setAmberZoneSecsBeforePeriodEnd(300);
        rule1.setRedZoneSecsBeforePeriodEnd(60);

        String json = JsonSerializer.serialize(rule1, true);
        System.out.print(json);
        ElapsedTimeTradingRule rule2 = JsonSerializer.deserialize(json, ElapsedTimeTradingRule.class);
        assertEquals(rule1, rule2);
    }

    @Test
    public void testDerivedMarketTradingRuleSerialization() {
        DerivedMarketSpec derivedMarketSpec = DerivedMarketSpec.getDerivedMarketSpecForDynamicRangeHcap("ABCD",
                        "TEST_HCAP_MKT", DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, 6, 10, true);
        DerivedMarketTradingRule rule1 = new DerivedMarketTradingRule(null, "FT:SPRD", derivedMarketSpec);
        String json = JsonSerializer.serialize(rule1, true);
        System.out.print(json);
        DerivedMarketTradingRule rule2 = JsonSerializer.deserialize(json, DerivedMarketTradingRule.class);
        assertEquals(rule1, rule2);
    }

    @Test
    public void testAflTradingRulesSerialization() {
        TradingRules ttr1 = new AflTradingRules();
        for (AbstractTradingRule tr : ttr1.getTradingRules()) {
            try {
                String json = JsonSerializer.serialize(tr, true);
                System.out.println(json);
            } catch (Exception e) {
                fail();
            }
        }
        try {
            String json = JsonSerializer.serialize(ttr1, true);
            System.out.println(json);
        } catch (Exception e) {

        }
    }
}
