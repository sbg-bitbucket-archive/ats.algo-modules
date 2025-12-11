package ats.algo.core.traderalert.tradingrules;

import java.io.Serializable;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.traderalert.TraderAlert;
import ats.algo.core.tradingrules.AbstractTradingRule;
import ats.algo.core.tradingrules.TradingRuleType;

public abstract class TraderAlertTradingRule extends AbstractTradingRule implements Serializable {

    private static final long serialVersionUID = 1L;

    public TraderAlertTradingRule(String ruleName) {
        super(TradingRuleType.TRADER_ALERT, ruleName, null);

    }

    public abstract TraderAlert shouldCreateTraderAlertFromIncident(MatchIncident matchIncident, MatchState matchState);

    public abstract TraderAlert shouldCreateTraderAlertFromIncident(MatchIncident matchIncident,
                    MatchState currentMatchState, MatchState previousMatchState, MatchFormat matchFormat);
}
