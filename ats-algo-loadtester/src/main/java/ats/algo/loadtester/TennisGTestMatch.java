package ats.algo.loadtester;

import ats.algo.core.baseclasses.AlgoMatchParams;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.common.TeamId;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.genericsupportfunctions.RandomNoGenerator;
import ats.algo.sport.tennisG.*;
import ats.algo.sport.tennisG.TennisGMatchIncident.TennisMatchIncidentType;;


class TennisGTestMatch extends TestMatch {

    boolean preMatch;

    public TennisGTestMatch(long eventId) {
        super(eventId, "TennisGMatch with randomly generated incidents");
        preMatch = true;
    }

    public MatchIncident getStartMatchIncident() {
        return new TennisGMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A);
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
        TennisGMatchIncident incident = new TennisGMatchIncident(0, incidentType, playerId);
        incident.setEventId(eventId);
        noIncidentsGenerated++;
        incident.setIncidentId(String.format("Request_%d", noIncidentsGenerated));
        return incident;
    }

    @Override
    public SupportedSportType getSupportedSport() {
        return SupportedSportType.TEST_TENNISG;
    }

    @Override
    public MatchFormat getMatchFormat() {
        return new TennisGMatchFormat();

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
        return new TennisGTestMatch(eventId);
    }



}
