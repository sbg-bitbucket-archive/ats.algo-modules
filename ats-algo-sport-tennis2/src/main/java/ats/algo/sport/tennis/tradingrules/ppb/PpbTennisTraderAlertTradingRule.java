package ats.algo.sport.tennis.tradingrules.ppb;

import java.util.HashMap;
import java.util.Map;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.AbandonMatchIncident.AbandonMatchIncidentType;
import ats.algo.core.traderalert.TraderAlert;
import ats.algo.core.traderalert.TraderAlertAttributes;
import ats.algo.core.traderalert.TraderAlert.TraderAlertType;
import ats.algo.core.traderalert.tradingrules.TraderAlertTradingRule;
import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;
import ats.algo.sport.tennis.TennisMatchState;

public class PpbTennisTraderAlertTradingRule extends TraderAlertTradingRule {

    private static final long serialVersionUID = 1L;

    protected PpbTennisTraderAlertTradingRule(String ruleName) {
        super("Ppb TraderAlertTradingRule");
    }

    @Override
    public TraderAlert shouldCreateTraderAlertFromIncident(MatchIncident matchIncident, MatchState matchState) {

        Object incidentSubType = matchIncident.getIncidentSubType();

        TraderAlert traderAlert = null;
        String description = "PPBF Incident Trading Rule Alert";

        Map<String, TraderAlertAttributes> alert = new HashMap<String, TraderAlertAttributes>();

        if (incidentSubType != null && incidentSubType instanceof TennisMatchIncidentType) {
            TennisMatchIncidentType tennisMatchIncidentType = (TennisMatchIncidentType) incidentSubType;

            alert.put("Event ID", new TraderAlertAttributes(Long.toString(matchIncident.getEventId())));
            alert.put("Feed Provider", new TraderAlertAttributes(matchIncident.getSourceSystem()));
            alert.put("Alert Title", new TraderAlertAttributes(description));
            TraderAlertType alertType = null;

            switch (tennisMatchIncidentType) {
                case CHALLENGE:
                    alert.put("Alert Text", new TraderAlertAttributes("There is currently a CHALLENGE"));
                    alertType = TraderAlertType.CHALLENGE;
                    break;
                case HEAT_DELAY:
                    alert.put("Alert Text", new TraderAlertAttributes("There is currently a HEAT DELAY"));
                    alertType = TraderAlertType.HEAT_DELAY;
                    break;
                case MEDICAL_TIMEOUT:
                    alert.put("Alert Text", new TraderAlertAttributes("There is currently a MEDICAL TIMEOUT"));
                    alertType = TraderAlertType.MEDICAL_TIMEOUT;
                    break;
                case ON_COURT_COACHING:
                    alert.put("Alert Text", new TraderAlertAttributes("There is currently ON COURT COACHING"));
                    alertType = TraderAlertType.ON_COURT_COACHING;
                    break;
                case RAIN:
                    alert.put("Alert Text", new TraderAlertAttributes("There is currently RAIN"));
                    alertType = TraderAlertType.RAIN;
                    break;
                case TOILET_BREAK:
                    alert.put("Alert Text", new TraderAlertAttributes("There is currently a TOILET BREAK"));
                    alertType = TraderAlertType.TOILET_BREAK;
                    break;
                /*
                 * ar-922 new alerts
                 */
                case POINT_WON:
                    TennisMatchState tennisMatchState = (TennisMatchState) matchState;

                    if (tennisMatchState.getSetNo() > 1 && tennisMatchState.getGameNo() == 1 &&
                                    (tennisMatchState.getGame().getPointsA() + tennisMatchState.getGame().getPointsB())
                                                    == 0) {
                        alert.put("Alert Text", new TraderAlertAttributes("Side swap after every set is over"));

                        alertType = TraderAlertType.SIDE_SWAP_SET;
                        break;
                    }

                    if (tennisMatchState.getGameNo() % 2 == 0) {
                        if ((tennisMatchState.getGame().getPointsA() + tennisMatchState.getGame().getPointsB()) == 0) {
                            alert.put("Alert Text",
                                            new TraderAlertAttributes("Side swap after every odd game is over"));
                            alertType = TraderAlertType.SIDE_SWAP_GAME;
                            break;
                        }
                    }

                    if (tennisMatchState.isInTieBreak()) {
                        int totalPointsInTiebreak;
                        if (tennisMatchState.isInSuperTieBreak()) {
                            totalPointsInTiebreak = tennisMatchState.getSuperTieBreak().getPointsA() + tennisMatchState
                                            .getSuperTieBreak().getPointsB();
                        } else {
                            totalPointsInTiebreak =
                                            tennisMatchState.getTieBreak().getPointsA() + tennisMatchState.getTieBreak()
                                                            .getPointsB();
                        }
                        if (totalPointsInTiebreak % 6 == 0 && totalPointsInTiebreak > 0) {
                            alert.put("Alert Text", new TraderAlertAttributes(
                                            "Side swap after every 6 points in a Normal/Championship Tiebreak"));

                            alertType = TraderAlertType.SIDE_SWAP_TIEBREAK;
                            break;
                        }
                    }
                default:
                    return null;
            }
            traderAlert = new TraderAlert(alertType, alert);
            return traderAlert;

        } else if (incidentSubType != null && incidentSubType instanceof AbandonMatchIncidentType) {
            AbandonMatchIncidentType abandonMatchIncidentType = (AbandonMatchIncidentType) incidentSubType;

            alert.put("Event ID", new TraderAlertAttributes(Long.toString(matchIncident.getEventId())));
            alert.put("Feed Provider", new TraderAlertAttributes(matchIncident.getSourceSystem()));
            alert.put("Alert Title", new TraderAlertAttributes(description));
            TraderAlertType alertType = null;

            switch (abandonMatchIncidentType) {
                case SCOUT_ABANDONED:
                    alert.put("Alert Text", new TraderAlertAttributes("Scout has been disconnected"));
                    alertType = TraderAlertType.SCOUT_ABANDONED;
                    break;
                default:
                    return null;
            }

            traderAlert = new TraderAlert(alertType, alert);
            return traderAlert;


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


    @Override
    public TraderAlert shouldCreateTraderAlertFromErrorMessage(long eventId, MatchState matchState, String errorCause) {
        // TODO Auto-generated method stub
        return null;
    }


}
