package ats.algo.sport.outrights.calcengine.core;

import java.util.Map;

import ats.algo.core.markets.Market;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.Selection;
import ats.algo.core.markets.SuspensionStatus;

public class OutrightTradingRules {

    public static final double PROB_LOWER_SUSPENSION_LIMIT = 0.25;
    public static final double PROB_UPPER_SUSPENSION_LIMIT = 0.75;


    /**
     * 
     * @param fixtures
     * @param markets
     * @param suspendMarkets set to true if suspension requested via the gui
     * @param atsFixtureProbs
     * @return
     */
    public static Markets apply(Teams teams, Fixtures fixtures, Markets markets, boolean suspendMarkets) {
        if (suspendMarkets) {
            /*
             * suspend all markets if this flag is set
             */
            markets.getMarkets().forEach((key, market) -> {
                market.setSuspensionStatus("OutrightTradingRules", SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Suspend all markets set via the trader gui");
            });
        } else {
            for (Fixture fixture : fixtures) {
                if (fixture.isFixtureSuspended())
                    applyOutrightsSuspensionRule(teams, fixture.getHomeTeamID(), fixture.getAwayTeamID(), markets);
            }
        }
        return markets;
    }

    private static void applyOutrightsSuspensionRule(Teams teams, String homeTeamID, String awayTeamID,
                    Markets markets) {
        String homeSelectionName = teams.get(homeTeamID).getDisplayName();
        String awaySelectionName = teams.get(awayTeamID).getDisplayName();
        for (Market market : markets.getMarkets().values()) {
            Map<String, Selection> selections = market.getSelections();
            Selection homeSelection = selections.get(homeSelectionName);
            Selection awaySelection = selections.get(awaySelectionName);
            if (checkSuspendMarket(market, homeSelectionName, homeSelection))
                continue;
            if (checkSuspendMarket(market, awaySelectionName, awaySelection))
                continue;
            checkSuspendMarketViaMarketType(market, homeTeamID, awayTeamID, homeSelectionName, awaySelectionName);
        }
    }


    private static boolean checkSuspendMarket(Market market, String selectionName, Selection selection) {
        if (selection == null)
            return false;
        double prob = selection.getProb();
        boolean needsSuspending = prob >= PROB_LOWER_SUSPENSION_LIMIT && prob <= PROB_UPPER_SUSPENSION_LIMIT;
        if (needsSuspending) {
            market.setSuspensionStatus("OutrightTradingRules", SuspensionStatus.SUSPENDED_DISPLAY, String.format(
                            "Suspend market because fixture involving %s is suspended and selection prob %.3f is within limits ",
                            selectionName, prob));
        }
        return needsSuspending;
    }

    private static void checkSuspendMarketViaMarketType(Market market, String homeTeamID, String awayTeamID,
                    String homeTeamName, String awayTeamName) {
        String type = market.getType();
        if (type.contains(homeTeamID) || type.contains(awayTeamID)) {
            market.setSuspensionStatus("OutrightTradingRules", SuspensionStatus.SUSPENDED_DISPLAY,
                            String.format("Suspend market because fixture involving %s and %s is suspended",
                                            homeTeamName, awayTeamName));
        }
    }


}
