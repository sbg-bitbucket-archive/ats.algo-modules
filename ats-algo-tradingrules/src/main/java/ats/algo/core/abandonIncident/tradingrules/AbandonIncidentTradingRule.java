package ats.algo.core.abandonIncident.tradingrules;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ats.algo.core.markets.Market;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.SuspensionStatus;
import ats.algo.core.tradingrules.AbstractTradingRule;
import ats.algo.core.tradingrules.TradingRuleType;

public class AbandonIncidentTradingRule extends AbstractTradingRule implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<String> suspendList = new ArrayList<String>();

    public AbandonIncidentTradingRule() {
        super(TradingRuleType.ABANDON_MATCH_RESULTING_AND_SUSPENSION_MARKETS_RULE,
                        "Abandon match but suspend certain markets", null);
    }

    public AbandonIncidentTradingRule(List<String> list) {
        this();
        suspendList = list;
    }

    public List<String> getSuspendList() {
        return suspendList;
    }

    public void setSuspendList(List<String> suspendList) {
        this.suspendList = suspendList;
    }


    public void applyPublishSuspendMarketRule(Markets publishedMarkets) {

        publishedMarkets.getMarkets().entrySet().removeIf((entry -> !suspendList.contains(entry.getValue().getType())));

        publishedMarkets.getMarkets().entrySet().forEach(item -> {
            ((Market) item.getValue()).setSuspensionStatus("abandon publish markets rule",
                            SuspensionStatus.SUSPENDED_UNDISPLAY, "betfair requirement");
            debug("Removing All Markets Except: " + item.getKey());
        });

    }


}
