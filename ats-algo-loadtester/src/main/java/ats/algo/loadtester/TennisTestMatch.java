package ats.algo.loadtester;

import ats.algo.core.baseclasses.AlgoMatchParams;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.common.TeamId;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.genericsupportfunctions.RandomNoGenerator;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchIncident;
import ats.algo.sport.tennis.TennisMatchFormat.FinalSetType;
import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;


class TennisTestMatch extends TestMatch {

    boolean preMatch;

    public TennisTestMatch(long eventId) {
        super(eventId, "TennisMatch with randomly generated incidents");
        preMatch = true;
    }

    public MatchIncident getStartMatchIncident() {
        return new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A);
    }

    @Override
    public MatchIncident getNextIncident() {

        double r = RandomNoGenerator.nextDouble();
        TeamId playerId;
        TennisMatchIncidentType incidentType;
        if (r > 0.2) {
            playerId = TeamId.A;
        } else {
            playerId = TeamId.B;
        }
        if (preMatch) {
            incidentType = TennisMatchIncidentType.MATCH_STARTING;
            preMatch = false;
        } else
            incidentType = TennisMatchIncidentType.POINT_WON;
        TennisMatchIncident incident = new TennisMatchIncident(0, incidentType, playerId);
        incident.setEventId(eventId);
        noIncidentsGenerated++;
        incident.setIncidentId(String.format("Request_%d_for_EventId_%d", noIncidentsGenerated, eventId));
        return incident;
    }

    @Override
    public SupportedSportType getSupportedSport() {
        return SupportedSportType.TENNIS;
    }

    @Override
    public MatchFormat getMatchFormat() {
        return new TennisMatchFormat(3, FinalSetType.NORMAL_WITH_TIE_BREAK, false, false);

    }

    @Override
    AlgoMatchParams getNextMatchParams() {
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
        return new TennisTestMatch(eventId);
    }



}
