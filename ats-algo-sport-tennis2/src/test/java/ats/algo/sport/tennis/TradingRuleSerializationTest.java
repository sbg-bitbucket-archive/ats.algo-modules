package ats.algo.sport.tennis;

import ats.algo.core.tradingrules.AbstractTradingRule;
import ats.algo.genericsupportfunctions.JsonSerializer;
import ats.algo.genericsupportfunctions.MethodName;
import ats.algo.sport.tennis.tradingrules.TennisTradingRules;
import org.junit.Test;
import static org.junit.Assert.fail;

public class TradingRuleSerializationTest {

    @Test
    public void testRuleSet() {
        MethodName.log();
        TennisTradingRules ttr1 = new TennisTradingRules();
        for (AbstractTradingRule tr : ttr1.getTradingRules()) {
            try {
                @SuppressWarnings("unused")
                String json = JsonSerializer.serialize(tr, false);
                // System.out.println(json);
            } catch (Exception e) {
                fail();
            }
        }
    }
}
