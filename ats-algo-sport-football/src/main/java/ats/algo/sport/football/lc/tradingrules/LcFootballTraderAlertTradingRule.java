package ats.algo.sport.football.lc.tradingrules;

import java.util.HashMap;
import java.util.Map;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.AbandonMatchIncident;
import ats.algo.core.common.AbandonMatchIncident.AbandonMatchIncidentType;
import ats.algo.core.common.AlertColour;
import ats.algo.core.common.DatafeedMatchIncident;
import ats.algo.core.common.DatafeedMatchIncident.DatafeedMatchIncidentType;
import ats.algo.core.common.ElapsedTimeMatchIncident;
import ats.algo.core.common.ElapsedTimeMatchIncident.ElapsedTimeMatchIncidentType;
import ats.algo.core.common.MatchReferralIncident;
import ats.algo.core.common.MatchReferralIncident.MatchReferralIncidentType;
import ats.algo.core.traderalert.TraderAlert;
import ats.algo.core.traderalert.TraderAlert.TraderAlertType;
import ats.algo.core.traderalert.TraderAlertAttributes;
import ats.algo.core.traderalert.tradingrules.TraderAlertTradingRule;
import ats.algo.sport.football.FootballMatchFormat;
import ats.algo.sport.football.FootballMatchIncident;
import ats.algo.sport.football.FootballMatchIncident.FootballMatchIncidentType;
import ats.algo.sport.football.FootballMatchPeriod;
import ats.algo.sport.football.FootballMatchState;

public class LcFootballTraderAlertTradingRule extends TraderAlertTradingRule {

    private static final long serialVersionUID = 1L;

    protected LcFootballTraderAlertTradingRule(String ruleName) {
        super("LC TraderAlertTradingRule");
    }

    @Override
    public TraderAlert shouldCreateTraderAlertFromIncident(MatchIncident matchIncident, MatchState currentMatchState) {
        return null;
    }

    @Override
    public TraderAlert shouldCreateTraderAlertFromIncident(MatchIncident matchIncident, MatchState currentMatchState,
                    MatchState previousMatchState, MatchFormat matchFormat) {

        TraderAlert traderAlert = null;
        FootballMatchState currentFMS = null;
        FootballMatchState previousFMS = null;
        FootballMatchFormat fmf = null;
        Object incidentSubType = matchIncident.getIncidentSubType();
        String alertTitle = "LC Trading Rule Alert";

        if (currentMatchState != null && currentMatchState instanceof FootballMatchState) {
            currentFMS = (FootballMatchState) currentMatchState;
            previousFMS = (FootballMatchState) previousMatchState;
            fmf = (FootballMatchFormat) matchFormat;
        } else {
            return null;
        }
        Map<String, TraderAlertAttributes> alert = new HashMap<String, TraderAlertAttributes>();

        if (incidentSubType != null) {
            alert.put("Event ID", new TraderAlertAttributes(Long.toString(matchIncident.getEventId())));
            alert.put("Alert Title", new TraderAlertAttributes(alertTitle));
            alert.put("Feed Provider", new TraderAlertAttributes(matchIncident.getSourceSystem()));

            if (incidentSubType instanceof FootballMatchIncidentType) {
                FootballMatchIncident footballMatchIncident = (FootballMatchIncident) matchIncident;

                switch (footballMatchIncident.getIncidentSubType()) {
                    case GOAL:
                        alert.put("Alert Colour", new TraderAlertAttributes(AlertColour.GREEN.toString()));

                        switch (matchIncident.getTeamId()) {
                            case A:
                                alert.put("Alert Text", new TraderAlertAttributes("Home Team Goal"));
                            case B:
                                alert.put("Alert Text", new TraderAlertAttributes("Away Team Goal"));
                            case UNKNOWN:
                                break;

                        }
                        break;
                    case CORNER:
                        alert.put("Alert Colour", new TraderAlertAttributes(AlertColour.GREEN.toString()));

                        switch (matchIncident.getTeamId()) {
                            case A:
                                alert.put("Alert Text", new TraderAlertAttributes("Home Team Corner"));
                            case B:
                                alert.put("Alert Text", new TraderAlertAttributes("Away Team Corner"));
                            case UNKNOWN:
                                break;

                        }
                        break;
                    case YELLOW_CARD:
                        alert.put("Alert Colour", new TraderAlertAttributes(AlertColour.GREEN.toString()));

                        switch (matchIncident.getTeamId()) {
                            case A:
                                if (currentFMS.getYellowCardsA() == 1) {
                                    alert.put("Alert Text", new TraderAlertAttributes("Home Team Yellow Card"));
                                } else if (currentFMS.getYellowCardsA() == 2) {
                                    alert.put("Alert Text", new TraderAlertAttributes("2nd Home Team Yellow Card"));
                                }
                            case B:
                                if (currentFMS.getYellowCardsB() == 1) {
                                    alert.put("Alert Text", new TraderAlertAttributes("Away team Yellow Card"));
                                } else if (currentFMS.getYellowCardsB() == 2) {
                                    alert.put("Alert Text", new TraderAlertAttributes("2nd Away Team Yellow Card"));
                                }
                            case UNKNOWN:
                                break;

                        }
                        break;
                    case RED_CARD:
                        alert.put("Alert Colour", new TraderAlertAttributes(AlertColour.RED.toString()));

                        switch (matchIncident.getTeamId()) {
                            case A:
                                alert.put("Alert Text", new TraderAlertAttributes("Home Team Red Card"));
                            case B:
                                alert.put("Alert Text", new TraderAlertAttributes("Away Team Red Card"));
                            case UNKNOWN:
                                break;

                        }
                        break;
                    case PENALTY_MISSED:
                        alert.put("Alert Colour", new TraderAlertAttributes(AlertColour.GREEN.toString()));

                        switch (matchIncident.getTeamId()) {
                            case A:
                                alert.put("Alert Text", new TraderAlertAttributes("Home Team Missed Penalty"));
                            case B:
                                alert.put("Alert Text", new TraderAlertAttributes("Away Team Missed Penalty"));
                            case UNKNOWN:
                                break;

                        }
                        break;
                    case PENALTY_CONFIRMED:
                    case PENALTY:
                        alert.put("Alert Colour", new TraderAlertAttributes(AlertColour.RED.toString()));

                        switch (matchIncident.getTeamId()) {
                            case A:
                                alert.put("Alert Text", new TraderAlertAttributes("Home Team Entering Penalty"));
                            case B:
                                alert.put("Alert Text", new TraderAlertAttributes("Away Team Entering Penalty"));
                            case UNKNOWN:
                                break;

                        }
                        break;
                    case POSSIBLE_SUB:
                        alert.put("Alert Colour", new TraderAlertAttributes(AlertColour.GREEN.toString()));

                        switch (matchIncident.getTeamId()) {
                            case A:
                                alert.put("Alert Text", new TraderAlertAttributes("Home Team Substitute"));
                            case B:
                                alert.put("Alert Text", new TraderAlertAttributes("Away Team Substitute"));
                            case UNKNOWN:
                                break;

                        }
                        break;
                    case SHOOTOUT_START:
                        alert.put("Alert Colour", new TraderAlertAttributes(AlertColour.GREEN.toString()));
                        alert.put("Alert Text", new TraderAlertAttributes("Shootout about to start"));
                        break;
                    default:
                        return null;
                }
            } else if (incidentSubType instanceof ElapsedTimeMatchIncidentType) {
                ElapsedTimeMatchIncident elapsedTimeMatchIncident = (ElapsedTimeMatchIncident) matchIncident;

                switch (elapsedTimeMatchIncident.getIncidentSubType()) {
                    case SET_PERIOD_START:
                        alert.put("Alert Colour", new TraderAlertAttributes(AlertColour.GREEN.toString()));
                        if (previousFMS.preMatch() && !(currentFMS.preMatch())) {
                            alert.put("Alert Text", new TraderAlertAttributes("1st Half has started"));
                        }
                        if (previousFMS.getMatchPeriod().equals(FootballMatchPeriod.AT_HALF_TIME)
                                        && currentFMS.getMatchPeriod().equals(FootballMatchPeriod.IN_SECOND_HALF)) {
                            alert.put("Alert Text", new TraderAlertAttributes("2nd Half has started"));
                        }
                        if (previousFMS.getMatchPeriod().equals(FootballMatchPeriod.AT_FULL_TIME) && currentFMS
                                        .getMatchPeriod().equals(FootballMatchPeriod.IN_EXTRA_TIME_FIRST_HALF)) {
                            alert.put("Alert Text", new TraderAlertAttributes("1st Half of Extra Time has started"));
                        }
                        if (previousFMS.getMatchPeriod().equals(FootballMatchPeriod.IN_EXTRA_TIME_HALF_TIME)
                                        && currentFMS.getMatchPeriod()
                                                        .equals(FootballMatchPeriod.IN_EXTRA_TIME_SECOND_HALF)) {
                            alert.put("Alert Text", new TraderAlertAttributes("2nd Half of Extra Time has started"));
                        }
                        if (!(previousFMS.getMatchPeriod().equals(FootballMatchPeriod.IN_SHOOTOUT))
                                        && currentFMS.getMatchPeriod().equals(FootballMatchPeriod.IN_SHOOTOUT)) {
                            alert.put("Alert Text", new TraderAlertAttributes("Game is now in a penalty shootout"));
                        }
                        break;
                    case SET_PERIOD_END:
                        if (previousFMS.getMatchPeriod().equals(FootballMatchPeriod.IN_SECOND_HALF)
                                        && currentFMS.getMatchPeriod().equals(FootballMatchPeriod.AT_FULL_TIME)
                                        && fmf.getExtraTimeMinutes() > 0) {
                            alert.put("Alert Colour", new TraderAlertAttributes(AlertColour.AMBER.toString()));
                            alert.put("Alert Text", new TraderAlertAttributes("Full time. Waiting for Extra Time"));
                        }
                        if (previousFMS.getMatchPeriod().equals(FootballMatchPeriod.IN_FIRST_HALF)
                                        && currentFMS.getMatchPeriod().equals(FootballMatchPeriod.AT_HALF_TIME)) {
                            alert.put("Alert Colour", new TraderAlertAttributes(AlertColour.GREEN.toString()));
                            alert.put("Alert Text", new TraderAlertAttributes("Normal Time Half Time"));
                        }
                        if (previousFMS.getMatchPeriod().equals(FootballMatchPeriod.IN_EXTRA_TIME_FIRST_HALF)
                                        && currentFMS.getMatchPeriod()
                                                        .equals(FootballMatchPeriod.IN_EXTRA_TIME_HALF_TIME)) {
                            alert.put("Alert Colour", new TraderAlertAttributes(AlertColour.GREEN.toString()));
                            alert.put("Alert Text", new TraderAlertAttributes("Extra Time Half Time"));
                        }
                        if (previousFMS.getMatchPeriod().equals(FootballMatchPeriod.IN_EXTRA_TIME_SECOND_HALF)
                                        && currentFMS.getMatchPeriod().equals(FootballMatchPeriod.IN_SHOOTOUT)) {
                            alert.put("Alert Colour", new TraderAlertAttributes(AlertColour.AMBER.toString()));
                            alert.put("Alert Text", new TraderAlertAttributes("Waiting for Penalty Shootout"));
                        }
                        break;
                    default:
                        break;
                }

            } else if (incidentSubType instanceof DatafeedMatchIncidentType) {
                DatafeedMatchIncident datafeedMatchIncident = (DatafeedMatchIncident) matchIncident;

                switch (datafeedMatchIncident.getIncidentSubType()) {

                    case CANCELLED_COVERAGE:
                        alert.put("Alert Colour", new TraderAlertAttributes(AlertColour.RED.toString()));
                        alert.put("Alert Text", new TraderAlertAttributes("Cancelled Coverage"));
                        break;
                    case BET_STOP:
                        alert.put("Alert Colour", new TraderAlertAttributes(AlertColour.AMBER.toString()));
                        alert.put("Alert Text", new TraderAlertAttributes("Bet Stop"));
                        break;
                    case BET_START:
                        alert.put("Alert Colour", new TraderAlertAttributes(AlertColour.GREEN.toString()));
                        alert.put("Alert Text", new TraderAlertAttributes("Bet Start"));
                        break;
                    case OK:
                        alert.put("Alert Colour", new TraderAlertAttributes(AlertColour.GREEN.toString()));
                        alert.put("Alert Text", new TraderAlertAttributes("Feed Data is Stable"));
                        break;
                    case SCOUT_DISCONNECT:
                        alert.put("Alert Colour", new TraderAlertAttributes(AlertColour.RED.toString()));
                        alert.put("Alert Text", new TraderAlertAttributes("Scout Disconnect"));
                        break;
                    default:
                        break;
                }

            } else if (incidentSubType instanceof AbandonMatchIncidentType) {
                AbandonMatchIncident abandonMatchIncident = (AbandonMatchIncident) matchIncident;

                switch (abandonMatchIncident.getIncidentSubType()) {

                    case CANCELLATION:
                    case DISQUALIFICATION:
                    case INJURY:
                    case RETIREMENT:
                    case WALKOVER:
                        alert.put("Alert Colour", new TraderAlertAttributes(AlertColour.RED.toString()));
                        alert.put("Alert Text", new TraderAlertAttributes("Abandoned Match"));
                        break;

                    default:
                        break;
                }
            } else if (incidentSubType instanceof MatchReferralIncidentType) {
                MatchReferralIncident matchReferralIncident = (MatchReferralIncident) matchIncident;

                switch (matchReferralIncident.getIncidentSubType()) {
                    case VAR_POSSIBLE_REFERRAL:
                        alert.put("Alert Colour", new TraderAlertAttributes(AlertColour.RED.toString()));
                        alert.put("Alert Text", new TraderAlertAttributes("VAR Possible"));
                    case VAR_REFERRAL_CONFIRMED:
                        alert.put("Alert Colour", new TraderAlertAttributes(AlertColour.RED.toString()));
                        alert.put("Alert Text", new TraderAlertAttributes("VAR Confirmed"));
                        break;
                    case VAR_REFERRAL_COMPLETED:
                        alert.put("Alert Colour", new TraderAlertAttributes(AlertColour.GREEN.toString()));
                        alert.put("Alert Text", new TraderAlertAttributes("VAR Completed"));
                    default:
                        break;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }

        traderAlert = new TraderAlert(TraderAlertType.LADCOR_INCIDENT_TRADER_ALERT, alert);
        return traderAlert;
    }

    @Override
    public TraderAlert shouldCreateTraderAlertFromErrorMessage(long eventId, MatchState matchState, String errorCause) {

        TraderAlert traderAlert = null;
        FootballMatchState currentFMS = null;
        String alertTitle = "LC FATAL ERROR Trading Rule Alert";

        if (matchState != null && matchState instanceof FootballMatchState) {
            currentFMS = (FootballMatchState) matchState;
        } else {
            return null;
        }

        Map<String, TraderAlertAttributes> alert = new HashMap<String, TraderAlertAttributes>();

        alert.put("Event ID", new TraderAlertAttributes(Long.toString(eventId)));
        alert.put("Alert Text", new TraderAlertAttributes(errorCause));
        alert.put("Match State", new TraderAlertAttributes(currentFMS.generateSimpleMatchState().toString()));
        alert.put("Alert Colour", new TraderAlertAttributes(AlertColour.RED.toString()));
        alert.put("Alert Title", new TraderAlertAttributes(alertTitle));

        traderAlert = new TraderAlert(TraderAlertType.LADCOR_FATAL_ERROR_ALERT, alert);
        return traderAlert;


    }
}
