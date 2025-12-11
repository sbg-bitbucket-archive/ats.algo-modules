package ats.algo.sport.fantasyexample;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.TeamId;

public class FantasyExampleSportMatchIncident extends MatchIncident {

    public enum FantasyExampleSportMatchIncidentType {
        GAME_WINNER,
        GOAL,
        YELLOW_CARD
    }

    private FantasyExampleSportMatchIncidentType incidentType;
    private int playerNo;


    private static final long serialVersionUID = 1L;

    @Override
    public Object getIncidentSubType() {
        return incidentType;
    }


    public int getPlayerNo() {
        return playerNo;
    }



    public static FantasyExampleSportMatchIncident generateIncident(TeamId teamId) {
        FantasyExampleSportMatchIncident incident = new FantasyExampleSportMatchIncident();
        incident.incidentType = FantasyExampleSportMatchIncidentType.GAME_WINNER;
        incident.setTeamId(teamId);
        return incident;
    }

    public static MatchIncident generateIncident(long eventId, TeamId teamId) {
        FantasyExampleSportMatchIncident incident = generateIncident(teamId);
        incident.setEventId(eventId);
        return incident;
    }

    public static MatchIncident generatePlayerIncident(long eventId, FantasyExampleSportMatchIncidentType type,
                    int playerNo) {
        FantasyExampleSportMatchIncident incident = new FantasyExampleSportMatchIncident();
        incident.setEventId(eventId);
        incident.incidentType = type;
        incident.playerNo = playerNo;
        return incident;
    }

}
