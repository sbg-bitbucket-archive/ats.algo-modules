package ats.algo.sport.outrights.calcengine.core;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ats.algo.core.comparetomarket.ParamFindResults;
import ats.algo.core.comparetomarket.ParamFindResultsStatus;
import ats.algo.core.comparetomarket.TraderParamFindResultsDescription;
import ats.algo.core.request.ParamFindRequest;
import ats.algo.core.request.ParamFindResponse;
import ats.algo.core.request.PriceCalcRequest;
import ats.algo.core.request.PriceCalcResponse;
import ats.algo.genericsupportfunctions.FileReadWrite;
import ats.algo.sport.outrights.OutrightsMatchIncident;
import ats.algo.sport.outrights.OutrightsWatchList;
import ats.algo.sport.outrights.calcengine.core.Competition;
import ats.algo.sport.outrights.calcengine.core.Competitions;
import ats.algo.sport.outrights.calcengine.core.DataType;
import ats.algo.sport.outrights.calcengine.core.Fixture;
import ats.algo.sport.outrights.calcengine.core.FullMatchProbs;
import ats.algo.sport.outrights.calcengine.core.CompetitionWarnings;
import ats.algo.sport.outrights.calcengine.core.Team;
import ats.algo.sport.outrights.calcengine.core.Teams;
import ats.algo.sport.outrights.competitionsdata.EnglishChampionshipLeague1819;
import ats.algo.sport.outrights.competitionsdata.PremierLeague1819;
import ats.algo.sport.outrights.competitionsdata.TestCompetition;
import ats.algo.sport.outrights.server.api.TeamDataUpdate;

public class Outrights {

    private static final Logger logger = LoggerFactory.getLogger(Outrights.class);

    private Competitions competitions;

    public Outrights() {
        competitions = new Competitions();
        addCompetition(TestCompetition.generate());
        addCompetition(PremierLeague1819.generate());
        addCompetition(EnglishChampionshipLeague1819.generate());
    }

    private void addCompetition(Competition competition) {
        competitions.put(competition.getEventID(), competition);
        logger.info(String.format("Added competition: %s with eventID: %d", competition.getName(),
                        competition.getEventID()));
    }

    public Competitions getCompetitions() {
        return competitions;
    }

    public void calculate(Competition competition) {
        logger.info("Starting calc for competition: " + competition.getName());
        CalcEngine calcEngine = new CalcEngine(competition);
        calcEngine.calculate();
        logger.info("Calc completed for competition: " + competition.getName());
    }

    public void calculateAll() {
        for (Competition competition : competitions.values()) {
            calculate(competition);
        }
    }

    public CompetitionWarnings checkCompetitionStateForErrors(Competition competition) {
        CalcEngine calcEngine = new CalcEngine(competition);
        return calcEngine.checkCompetitionStateForErrors();
    }

    public static FullMatchProbs calcFixtureProbs(Competition competition, Fixture fixture) {
        Team homeTeam = competition.getTeams().get(fixture.getHomeTeamID());
        Team awayTeam = competition.getTeams().get(fixture.getAwayTeamID());
        FullMatchProbs matchProbs = new FullMatchProbs(homeTeam, awayTeam, competition.getRatingsFactors(),
                        fixture.isPlayedAtNeutralGround());
        return matchProbs;
    }

    public void updateTeam(Competition competition, Team updatedTeam) {
        synchronized (competition) {
            updateSingleTeam(competition, updatedTeam);
            persistData(competition, DataType.TEAMS);
        }
    }



    public void teamDataUpdate(Competition competition, Map<String, TeamDataUpdate> teamData) {
        Teams teams = competition.getTeams();
        for (Entry<String, TeamDataUpdate> e : teamData.entrySet()) {
            Team team = teams.getFromFiveThirtyEightName(e.getKey());
            if (team == null)
                logger.warn("No match for fivethirtyeight team name:" + e.getKey());
            else {
                logger.info("Updating ratings for team:" + team.getDisplayName());
                team.setRatingAttack(e.getValue().getRatingOffense());
                team.setRatingDefense(e.getValue().getRatingDefense());
            }
        }
        persistData(competition, DataType.TEAMS);

    }

    public void updateTeams(Competition competition, List<Team> teams) {
        synchronized (competition) {
            for (Team updatedTeam : teams) {
                updateSingleTeam(competition, updatedTeam);
            }
            persistData(competition, DataType.TEAMS);
        }
    }

    private void updateSingleTeam(Competition competition, Team updatedTeam) {
        Team team = competition.getTeams().get(updatedTeam.getTeamID());
        team.setRatingAttack(updatedTeam.getRatingAttack());
        team.setRatingDefense(updatedTeam.getRatingDefense());
        String displayName = updatedTeam.getDisplayName();
        if (displayName != null)
            team.setDisplayName(displayName);
        String fiveThirtyEightName = updatedTeam.getFiveThirtyEightName();
        if (fiveThirtyEightName != null)
            team.setFiveThirtyEightName(fiveThirtyEightName);
    }

    private static final String COMPETITIONS_FILE = "competitions.json";

    /**
     * writes the contents of all competitions to file
     * 
     * @param competition
     * @param datatype
     */
    private void persistData(Competition competition, DataType datatype) {
        try {
            FileReadWrite.writeJson(COMPETITIONS_FILE, competitions);
            logger.info("Competitions data persisted to file");
        } catch (IOException e) {
            logger.error("Can't write to competitions file");
            logger.error(e.toString());
        }
    }

    /**
     * reads the file containing saved competitions and then adds to the supplied set. If a competition saved to file
     * has the same eventID as a competition in the set supplied then the former overwrites the latter
     * 
     * @param competitions
     */
    public void loadPersistedData() {
        logger.info("reading competitions persisted to file");
        try {
            Competitions savedCompetitions = FileReadWrite.readJson(COMPETITIONS_FILE, Competitions.class);
            for (Entry<Long, Competition> e : savedCompetitions.getCompetitions().entrySet()) {
                logger.info("updating persisted competition: " + e.getValue().getName());
                competitions.put(e.getKey(), e.getValue());
            }
        } catch (IOException e) {
            logger.error("Can't read competitions file");
            logger.error(e.toString());
        }
    }

    public PriceCalcResponse handlePriceCalcRequest(PriceCalcRequest request) {
        long eventID = request.getEventId();
        Competition competition = competitions.get(eventID);
        if (competition == null) {
            return PriceCalcResponse.generateFatalErrorResponse(request.getUniqueRequestId(), "eventID not known");
        }
        CompetitionWarnings stateConsistencySummary = this.checkCompetitionStateForErrors(competition);
        if (!stateConsistencySummary.isStateOk()) {
            for (String s : stateConsistencySummary.getWarningMessages())
                logger.error(s);
            return PriceCalcResponse.generateFatalErrorResponse(request.getUniqueRequestId(),
                            "Competition state has errors");
        }

        OutrightsMatchIncident incident = (OutrightsMatchIncident) request.getMatchIncident();
        PriceCalcResponse response = null;
        synchronized (competition) {
            if (incident != null)
                competition.setInputMarkets(incident.getInputMarkets());
            calculate(competition);
            OutrightsWatchList watchList = competition.generateWatchList();
            response = new PriceCalcResponse(request.getUniqueRequestId(), competition.getMarkets(),
                            request.getMatchParams(), watchList, competition.getResultedMarkets());
        }
        return response;
    }

    /*
     * don't do anything - just return success
     */
    public ParamFindResponse handleParamFindRequest(ParamFindRequest request) {
        ParamFindResults results = new ParamFindResults();
        results.setParamFindResultsStatus(ParamFindResultsStatus.GREEN);
        results.setTraderParamFindResultsDescription(new TraderParamFindResultsDescription());
        ParamFindResponse response =
                        new ParamFindResponse(request.getUniqueRequestId(), results, request.getMatchParams());
        return response;
    }

}
