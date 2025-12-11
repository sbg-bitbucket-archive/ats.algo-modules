package ats.algo.core.tradingrules;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import ats.algo.core.MarketGroup;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.tradingrules.SetSuspensionStatusTradingRule;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.sport.generic.tradingrules.ElapsedTimeTradingRule;

public class TradingRulesRepositoryTest {

    ArrayList<SetSuspensionStatusTradingRule> opRules;

    @Test
    public void test() {
        TradingRulesRepository repository = new TradingRulesRepository();
        /*
         * add rules as array
         */
        SetSuspensionStatusTradingRule[] soccerRules = new SetSuspensionStatusTradingRule[3];
        soccerRules[0] = new ElapsedTimeTradingRule("Test rule 0", null, null);
        soccerRules[1] = new ElapsedTimeTradingRule("Test rule 1", null, null);
        soccerRules[2] = new ElapsedTimeTradingRule("Test rule 2", null, null);
        repository.setTradingRules(SupportedSportType.SOCCER, soccerRules);
        /*
         * add rules as rule set
         */
        TradingRules genericRuleSet = new TradingRules();
        genericRuleSet.addRule(new ElapsedTimeTradingRule("Test rule 2", null, null));
        repository.setTradingRules(null, genericRuleSet);

        TradingRules badmintonRuleSet = new TradingRules();
        badmintonRuleSet.addRule(new ElapsedTimeTradingRule("Test rule 2", null, null));
        repository.setTradingRules(SupportedSportType.BADMINTON, badmintonRuleSet);


        opRules = repository.getMarketSuspensionRules(SupportedSportType.BADMINTON, 2, MarketGroup.NOT_SPECIFIED);
        assertEquals(2, opRules.size());
        assertTrue(opRules.get(0).getRuleName().equals("Test rule 2"));

        opRules = repository.getMarketSuspensionRules(SupportedSportType.SOCCER, 2, MarketGroup.NOT_SPECIFIED);
        // printRules();
        assertEquals(4, opRules.size());
        /*
         * set the marketTier for rule1 to be different to market - should be excluded from the group returned
         */

        soccerRules[1].setMarketGroup(MarketGroup.CORNERS);
        opRules = repository.getMarketSuspensionRules(SupportedSportType.SOCCER, 2, MarketGroup.NOT_SPECIFIED);
        assertEquals(3, opRules.size());
        /*
         * same market group
         */

        soccerRules[1].setMarketGroup(MarketGroup.NOT_SPECIFIED);
        opRules = repository.getMarketSuspensionRules(SupportedSportType.SOCCER, 2, MarketGroup.NOT_SPECIFIED);
        assertEquals(4, opRules.size());

    }

    // private void printRules() {
    // for (int i = 0; i < opRules.size(); i++) {
    // TradingRule rule = opRules.get(i);
    // System.out.printf("index: %d, name: %s, priority: %d, eventTier: %d\n", i, rule.getRuleName(),
    // rule.getRulePriority(), rule.getEventTier());
    // }
    // }

}
