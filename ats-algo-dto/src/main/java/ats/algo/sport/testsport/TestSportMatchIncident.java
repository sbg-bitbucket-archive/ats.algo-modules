package ats.algo.sport.testsport;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.TeamId;

public class TestSportMatchIncident extends MatchIncident {


    private static final long serialVersionUID = 1L;

    private String incidentSubType = "GAME_WINNER";

    @Override
    public Object getIncidentSubType() {
        return incidentSubType;
    }

    public static TestSportMatchIncident generateIncident(TeamId teamId) {
        TestSportMatchIncident incident = new TestSportMatchIncident();
        incident.setTeamId(teamId);
        return incident;
    }

    public static MatchIncident generateIncident(long eventId, TeamId teamId) {
        TestSportMatchIncident incident = generateIncident(teamId);
        incident.setEventId(eventId);
        return incident;
    }

    public static MatchIncident generateInvalidIncident(long eventId) {
        TestSportMatchIncident incident = generateIncident(null);
        incident.setEventId(eventId);
        incident.incidentSubType = "INVALID";
        return incident;
    }


}
