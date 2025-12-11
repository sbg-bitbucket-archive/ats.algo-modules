package ats.algo.sport.football.tradingrules;

import static org.junit.Assert.fail;

import org.junit.Test;

import ats.algo.core.tradingrules.AbstractTradingRule;
import ats.algo.genericsupportfunctions.JsonSerializer;

public class TradingRulesSerializationTest {
    @Test
    public void testRuleSet() {
        FootballTradingRules ttr1 = new FootballTradingRules();
        for (AbstractTradingRule tr : ttr1.getTradingRules()) {
            try {
                String json = JsonSerializer.serialize(tr, false);
                System.out.println(json);
            } catch (Exception e) {
                fail();
            }
        }
    }
}
