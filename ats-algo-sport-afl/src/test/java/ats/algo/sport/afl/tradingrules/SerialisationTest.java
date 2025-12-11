package ats.algo.sport.afl.tradingrules;

import static org.junit.Assert.fail;

import org.junit.Test;

import ats.algo.core.tradingrules.AbstractTradingRule;

import ats.algo.core.tradingrules.TradingRules;
import ats.algo.genericsupportfunctions.JsonSerializer;
import ats.algo.genericsupportfunctions.MethodName;

public class SerialisationTest {
    @Test
    public void testAflTradingRulesSerialization() {
        MethodName.log();
        TradingRules ttr1 = new AflTradingRules();
        for (AbstractTradingRule tr : ttr1.getTradingRules()) {
            try {
                @SuppressWarnings("unused")
                String json = JsonSerializer.serialize(tr, true);
                // System.out.println(json);
            } catch (Exception e) {
                fail();
            }
        }
        try {
            @SuppressWarnings("unused")
            String json = JsonSerializer.serialize(ttr1, true);
            // System.out.println(json);
        } catch (Exception e) {

        }
    }
}
