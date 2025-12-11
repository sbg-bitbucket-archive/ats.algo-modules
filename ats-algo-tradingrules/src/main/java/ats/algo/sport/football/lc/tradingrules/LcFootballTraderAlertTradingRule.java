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
import ats.algo.core.traderalert.tradingrules.TraderAlertTradingRule;
import ats.algo.sport.football.FootballMatchFormat;
import ats.algo.sport.football.FootballMatchIncident;
import ats.algo.sport.football.FootballMatchIncident.FootballMatchIncidentType;
import ats.algo.sport.football.FootballMatchPeriod;
import ats.algo.sport.football.FootballMatchState;

public class LcFootballTraderAlertTradingRule extends TraderAlertTradingRule {

    private static final long serialVersionUID = 1L;

    protected LcFootballTraderAlertTradingRule(String ruleName) {
        super("Ppb TraderAlertTradingRule");
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
        String description = "LC Trading Rule Alert";

        Map<String, String> attributes = new HashMap<String, String>();

        if (currentMatchState != null && currentMatchState instanceof FootballMatchState) {
            currentFMS = (FootballMatchState) currentMatchState;
            previousFMS = (FootballMatchState) previousMatchState;
            fmf = (FootballMatchFormat) matchFormat;
        } else {
            return null;
        }


        if (incidentSubType != null) {
            attributes.put("Event ID", Long.toString(matchIncident.getEventId()));
            attributes.put("Feed Provider", matchIncident.getSourceSystem());

            if (incidentSubType instanceof FootballMatchIncidentType) {
                FootballMatchIncident footballMatchIncident = (FootballMatchIncident) matchIncident;

                switch (footballMatchIncident.getIncidentSubType()) {
                    case GOAL:
                        attributes.put("Alert Colour", AlertColour.AMBER.toString());

                        switch (matchIncident.getTeamId()) {
                            case A:
                                attributes.put("Alert Text", "Home Team Goal");
                            case B:
                                attributes.put("Alert Text", "Away Team Goal");
                            case UNKNOWN:
                                break;

                        }
                        break;
                    case CORNER:
                        attributes.put("Alert Colour", AlertColour.GREEN.toString());

                        switch (matchIncident.getTeamId()) {
                            case A:
                                attributes.put("Alert Text", "Home Team Corner");
                            case B:
                                attributes.put("Alert Text", "Away Team Corner");
                            case UNKNOWN:
                                break;

                        }
                        break;
                    case YELLOW_CARD:
                        attributes.put("Alert Colour", AlertColour.GREEN.toString());

                        switch (matchIncident.getTeamId()) {
                            case A:
                                if (currentFMS.getYellowCardsA() == 1) {
                                    attributes.put("Alert Text", "Home Team Yellow Card");
                                } else if (currentFMS.getYellowCardsA() == 2) {
                                    attributes.put("Alert Text", "2nd Home Team Yellow Card");
                                }
                            case B:
                                if (currentFMS.getYellowCardsA() == 1) {
                                    attributes.put("Alert Text", "Away team Yellow Card");
                                } else if (currentFMS.getYellowCardsA() == 2) {
                                    attributes.put("Alert Text", "2nd Home Team Yellow Card");
                                }
                            case UNKNOWN:
                                break;

                        }
                        break;
                    case RED_CARD:
                        attributes.put("Alert Colour", AlertColour.AMBER.toString());

                        switch (matchIncident.getTeamId()) {
                            case A:
                                attributes.put("Alert Text", "Home Team Red Card");
                            case B:
                                attributes.put("Alert Text", "Away Team Red Card");
                            case UNKNOWN:
                                break;

                        }
                        break;
                    case PENALTY_MISSED:
                        attributes.put("Alert Colour", AlertColour.GREEN.toString());

                        switch (matchIncident.getTeamId()) {
                            case A:
                                attributes.put("Alert Text", "Home Team Missed Penalty");
                            case B:
                                attributes.put("Alert Text", "Away Team Missed Penalty");
                            case UNKNOWN:
                                break;

                        }
                        break;
                    case PENALTY_CONFIRMED:
                    case PENALTY:
                        attributes.put("Alert Colour", AlertColour.AMBER.toString());

                        switch (matchIncident.getTeamId()) {
                            case A:
                                attributes.put("Alert Text", "Home Team Entering Penalty");
                            case B:
                                attributes.put("Alert Text", "Away Team Entering Penalty");
                            case UNKNOWN:
                                break;

                        }
                        break;
                    case POSSIBLE_SUB:
                        attributes.put("Alert Colour", AlertColour.GREEN.toString());

                        switch (matchIncident.getTeamId()) {
                            case A:
                                attributes.put("Alert Text", "Home Team Substitute");
                            case B:
                                attributes.put("Alert Text", "Away Team Substitute");
                            case UNKNOWN:
                                break;

                        }
                        break;
                    case SHOOTOUT_START:
                        attributes.put("Alert Colour", AlertColour.GREEN.toString());
                        attributes.put("Alert Text", "Shootout about to start");
                        break;
                    default:
                        return null;
                }
            } else if (incidentSubType instanceof ElapsedTimeMatchIncidentType) {
                ElapsedTimeMatchIncident elapsedTimeMatchIncident = (ElapsedTimeMatchIncident) matchIncident;

                switch (elapsedTimeMatchIncident.getIncidentSubType()) {
                    case SET_PERIOD_START:
                        attributes.put("Alert Colour", AlertColour.GREEN.toString());
                        if (previousFMS.preMatch() && !(currentFMS.preMatch())) {
                            attributes.put("Alert Text", "1st Half has started");
                        }
                        if (previousFMS.getMatchPeriod().equals(FootballMatchPeriod.AT_HALF_TIME)
                                        && currentFMS.getMatchPeriod().equals(FootballMatchPeriod.IN_SECOND_HALF)) {
                            attributes.put("Alert Text", "2nd Half has started");
                        }
                        if (previousFMS.getMatchPeriod().equals(FootballMatchPeriod.AT_FULL_TIME) && currentFMS
                                        .getMatchPeriod().equals(FootballMatchPeriod.IN_EXTRA_TIME_FIRST_HALF)) {
                            attributes.put("Alert Text", "1st Half of Extra Time has started");
                        }
                        if (previousFMS.getMatchPeriod().equals(FootballMatchPeriod.IN_EXTRA_TIME_HALF_TIME)
                                        && currentFMS.getMatchPeriod()
                                                        .equals(FootballMatchPeriod.IN_EXTRA_TIME_SECOND_HALF)) {
                            attributes.put("Alert Text", "2nd Half of Extra Time has started");
                        }
                        if (!(previousFMS.getMatchPeriod().equals(FootballMatchPeriod.IN_SHOOTOUT))
                                        && currentFMS.getMatchPeriod().equals(FootballMatchPeriod.IN_SHOOTOUT)) {
                            attributes.put("Alert Text", "Game is now in a penalty shootout");
                        }
                        break;
                    case SET_PERIOD_END:
                        if (previousFMS.getMatchPeriod().equals(FootballMatchPeriod.IN_SECOND_HALF)
                                        && currentFMS.getMatchPeriod().equals(FootballMatchPeriod.AT_FULL_TIME)
                                        && fmf.getExtraTimeMinutes() > 0) {
                            attributes.put("Alert Colour", AlertColour.AMBER.toString());
                            attributes.put("Alert Text", "Full time. Waiting for Extra Time");
                        }
                        if (previousFMS.getMatchPeriod().equals(FootballMatchPeriod.IN_FIRST_HALF)
                                        && currentFMS.getMatchPeriod().equals(FootballMatchPeriod.AT_HALF_TIME)) {
                            attributes.put("Alert Colour", AlertColour.GREEN.toString());
                            attributes.put("Alert Text", "Normal Time Half Time");
                        }
                        if (previousFMS.getMatchPeriod().equals(FootballMatchPeriod.IN_EXTRA_TIME_FIRST_HALF)
                                        && currentFMS.getMatchPeriod()
                                                        .equals(FootballMatchPeriod.IN_EXTRA_TIME_HALF_TIME)) {
                            attributes.put("Alert Colour", AlertColour.GREEN.toString());
                            attributes.put("Alert Text", "Extra Time Half Time");
                        }
                        if (previousFMS.getMatchPeriod().equals(FootballMatchPeriod.IN_EXTRA_TIME_SECOND_HALF)
                                        && currentFMS.getMatchPeriod().equals(FootballMatchPeriod.IN_SHOOTOUT)) {
                            attributes.put("Alert Colour", AlertColour.AMBER.toString());
                            attributes.put("Alert Text", "Waiting for Penalty Shootout");
                        }
                        break;
                    default:
                        break;
                }

                // FIXME need to put time incident alerts here
            } else if (incidentSubType instanceof DatafeedMatchIncidentType) {
                DatafeedMatchIncident datafeedMatchIncident = (DatafeedMatchIncident) matchIncident;

                switch (datafeedMatchIncident.getIncidentSubType()) {

                    case CANCELLED_COVERAGE:
                        attributes.put("Alert Colour", AlertColour.RED.toString());
                        attributes.put("Alert Text", "Cancelled Coverage");
                        break;
                    case BET_STOP:
                        attributes.put("Alert Colour", AlertColour.AMBER.toString());
                        attributes.put("Alert Text", "Bet Stop");
                        break;
                    case BET_START:
                        attributes.put("Alert Colour", AlertColour.GREEN.toString());
                        attributes.put("Alert Text", "Bet Start");
                        break;
                    case OK:
                        attributes.put("Alert Colour", AlertColour.GREEN.toString());
                        attributes.put("Alert Text", "Feed Data is Stable");
                        break;
                    case SCOUT_DISCONNECT:
                        attributes.put("Alert Colour", AlertColour.RED.toString());
                        attributes.put("Alert Text", "Scout Disconnect");
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
                        attributes.put("Alert Colour", AlertColour.RED.toString());
                        attributes.put("Alert Text", "Abandoned Match");
                        break;

                    default:
                        break;
                }
            } else if (incidentSubType instanceof MatchReferralIncidentType) {
                MatchReferralIncident matchReferralIncident = (MatchReferralIncident) matchIncident;

                switch (matchReferralIncident.getIncidentSubType()) {
                    case VAR_POSSIBLE_REFERRAL:
                        attributes.put("Alert Colour", AlertColour.AMBER.toString());
                        attributes.put("Alert Text", "VAR Possible");
                    case VAR_REFERRAL_CONFIRMED:
                        attributes.put("Alert Colour", AlertColour.AMBER.toString());
                        attributes.put("Alert Text", "VAR Confirmed");
                        break;
                    case VAR_REFERRAL_COMPLETED:
                        attributes.put("Alert Colour", AlertColour.GREEN.toString());
                        attributes.put("Alert Text", "VAR Completed");
                    default:
                        break;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }

        traderAlert = new TraderAlert(TraderAlertType.LADCOR_TRADER_ALERT, description, attributes);
        return traderAlert;
    }
}
