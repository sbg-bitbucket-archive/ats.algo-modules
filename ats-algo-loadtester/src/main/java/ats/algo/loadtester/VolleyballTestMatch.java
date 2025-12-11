package ats.algo.loadtester;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.common.TeamId;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.genericsupportfunctions.RandomNoGenerator;
import ats.algo.sport.volleyball.VolleyballMatchFormat;
import ats.algo.sport.volleyball.VolleyballMatchIncident;
import ats.algo.sport.volleyball.VolleyballMatchIncident.VolleyballMatchIncidentType;


class VolleyballTestMatch extends TestMatch {

    boolean preMatch;

    public VolleyballTestMatch(long eventId) {
        super(eventId, "VolleyballMatch with randomly generated incidents");
        preMatch = true;
    }

    public MatchIncident getStartMatchIncident() {
        VolleyballMatchIncident incident = new VolleyballMatchIncident();
        incident.set(VolleyballMatchIncidentType.SERVEFIRST, TeamId.A);
        return incident;
    }

    @Override
    public MatchIncident getNextIncident() {

        double r = RandomNoGenerator.nextDouble();
        TeamId playerId;
        VolleyballMatchIncidentType incidentType;
        if (r > 0.2) {
            playerId = TeamId.A;
        } else {
            playerId = TeamId.B;
        }
        if (preMatch) {
            incidentType = VolleyballMatchIncidentType.SERVEFIRST;
            preMatch = false;
        } else
            incidentType = VolleyballMatchIncidentType.POINTWON;
        VolleyballMatchIncident incident = new VolleyballMatchIncident();
        incident.set(incidentType, playerId);
        incident.setEventId(eventId);
        noIncidentsGenerated++;
        incident.setIncidentId(String.format("Request_%d_for_EventId_%d", noIncidentsGenerated, eventId));
        return incident;
    }

    @Override
    public SupportedSportType getSupportedSport() {
        return SupportedSportType.VOLLEYBALL;
    }

    @Override
    public MatchFormat getMatchFormat() {
        return new VolleyballMatchFormat();

    }

    @Override
    MatchParams getNextMatchParams() {
        noIncidentsGenerated++;
        return null;
    }

    @Override
    MarketPricesList getNextMarketPricesList() {
        noIncidentsGenerated++;
        return null;
    }

    @Override
    TestMatch newInstance(long eventId) {
        return new VolleyballTestMatch(eventId);
    }



}
