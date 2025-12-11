package ats.algo.sport.tennis.ppb.tradingrules;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.traderalert.TraderAlert;
import ats.algo.core.traderalert.TraderAlert.TraderAlertType;
import ats.algo.core.traderalert.tradingrules.TraderAlertTradingRule;
import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;

public class PpbTennisTraderAlertTradingRule extends TraderAlertTradingRule {

    private static final long serialVersionUID = 1L;

    protected PpbTennisTraderAlertTradingRule(String ruleName) {
        super("Ppb TraderAlertTradingRule");
    }

    @Override
    public TraderAlert shouldCreateTraderAlertFromIncident(MatchIncident matchIncident, MatchState matchState) {

        TraderAlert traderAlert = null;

        Object incidentSubType = matchIncident.getIncidentSubType();

        if (incidentSubType != null && incidentSubType instanceof TennisMatchIncidentType) {
            TennisMatchIncidentType tennisMatchIncidentType = (TennisMatchIncidentType) incidentSubType;

            switch (tennisMatchIncidentType) {
                case CHALLENGE:
                    traderAlert = new TraderAlert(TraderAlertType.CHALLENGE, "There is currently a CHALLENGE", null);
                    return traderAlert;
                case HEAT_DELAY:
                    traderAlert = new TraderAlert(TraderAlertType.HEAT_DELAY, "There is currently a HEAT DELAY", null);
                    return traderAlert;
                case MEDICAL_TIMEOUT:
                    traderAlert = new TraderAlert(TraderAlertType.MEDICAL_TIMEOUT,
                                    "There is currently a MEDICAL TIMEOUT", null);
                    return traderAlert;
                case ON_COURT_COACHING:
                    traderAlert = new TraderAlert(TraderAlertType.ON_COURT_COACHING,
                                    "There is currently an ON COURT COACHING", null);
                    return traderAlert;
                case RAIN:
                    traderAlert = new TraderAlert(TraderAlertType.RAIN, "There is currently some RAIN", null);
                    return traderAlert;
                case TOILET_BREAK:
                    traderAlert = new TraderAlert(TraderAlertType.TOILET_BREAK, "There is currently a TOILET BREAK",
                                    null);
                    return traderAlert;
                default:
                    return null;
            }
        } else {
            return null;
        }

    }

    @Override
    public TraderAlert shouldCreateTraderAlertFromIncident(MatchIncident matchIncident, MatchState currentMatchState,
                    MatchState previousMatchState, MatchFormat matchFormat) {
        // TODO Auto-generated method stub
        return null;
    }

}
