package ats.algo.sport.outrights.calcengine.core;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

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
import ats.algo.sport.outrights.competitionsdata.ChampionshipLeague1819;
import ats.algo.sport.outrights.competitionsdata.LaLiga1819;
import ats.algo.sport.outrights.competitionsdata.PremierLeague1819;
import ats.algo.sport.outrights.server.api.Alert;
import ats.algo.sport.outrights.server.api.Alerts;
import ats.algo.sport.outrights.server.api.StandingsListEntry;
import ats.algo.sport.outrights.server.api.TargetPointsEntry;
import ats.algo.sport.outrights.server.api.TeamDataUpdate;

public class Outrights {

    private static final Logger logger = LoggerFactory.getLogger(Outrights.class);

    private Competitions competitions;

    private String persistedDataFolder;

    public Outrights() {
        competitions = new Competitions();
        addCompetition(PremierLeague1819.generate());
        addCompetition(LaLiga1819.generate());
        addCompetition(ChampionshipLeague1819.generate());
    }

    public Outrights(Environment env) {
        this();
        for (Competition competition : competitions.values()) {
            String eventIdPropertyName =
                            "outrights." + competition.getOutrightsCompetitionID().toLowerCase() + ".eventid";
            long eventId = 0L;
            String eventIdStr = env.getProperty(eventIdPropertyName);
            try {
                if (eventIdStr != null)
                    eventId = Long.parseLong(eventIdStr);
            } catch (NumberFormatException | NullPointerException e) {
                /*
                 * do nothing
                 */
            }
            if (eventId != 0L) {
                logger.info(String.format("Changing eventId for %s to %d", competition.getOutrightsCompetitionID(),
                                eventId));
                competitions.changeEventId(competition, eventId);
            }
        }
    }

    private void addCompetition(Competition competition) {
        competitions.add(competition);
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
        logger.info(competition.getMarkets().toString());
        logger.info(competition.getNewResultedMarkets().toString());
        logResourcesSnapshot();
    }

    private void logResourcesSnapshot() {
        long heapSize = Runtime.getRuntime().totalMemory();
        long heapFreeSize = Runtime.getRuntime().freeMemory();
        int threadCount = Thread.activeCount();
        logger.info(String.format("heapSize: %d, freeSize: %d, threadCount: %d\n", heapSize, heapFreeSize,
                        threadCount));
    }

    public static FullMatchProbs calcFixtureProbs(Competition competition, Fixture fixture) {
        Team homeTeam = competition.getTeams().get(fixture.getHomeTeamID());
        Team awayTeam = competition.getTeams().get(fixture.getAwayTeamID());
        if (homeTeam == null || awayTeam == null)
            return null;
        return new FullMatchProbs(homeTeam, awayTeam, competition.getRatingsFactors(),
                        fixture.isPlayedAtNeutralGround());
    }

    public void updateTeam(Competition competition, Team updatedTeam) {
        synchronized (competition) {
            updateSingleTeam(competition, updatedTeam);
            persistData(competition, DataType.TEAMS);
        }
    }

    public void updateFixture(Competition competition, Fixture updatedFixture) {
        synchronized (competition) {
            updateSingleFixture(competition, updatedFixture);
            competition.updateDerivedFixtures();
            persistData(competition, DataType.FIXTURES);
        }
    }

    public void updateStanding(Competition competition, StandingsListEntry e) {
        synchronized (competition) {
            competition.updateManualAdjustments(e.getStanding().getTeamId(), e.getManualPointsAdj(),
                            e.getManualtieBreakAdj());
            persistData(competition, DataType.TEAMS);
        }
    }

    private void updateSingleFixture(Competition competition, Fixture updatedFixture) {
        Fixture fixture = competition.getFixtures().getByFixtureID(updatedFixture.getFixtureID());
        /*
         * update only those properties that are editable in the gui
         */
        fixture.setEventID(updatedFixture.getEventID());
        fixture.setStatus(updatedFixture.getStatus());
        fixture.setGoalsHome(updatedFixture.getGoalsHome());
        fixture.setGoalsAway(updatedFixture.getGoalsAway());
    }

    public void teamDataUpdate(Competition competition, Map<String, TeamDataUpdate> teamData) {
        Teams teams = competition.getTeams();
        for (Entry<String, TeamDataUpdate> e : teamData.entrySet()) {
            String fiveThirtyEightName = e.getKey();
            if (fiveThirtyEightName.equals("Wolverhampton"))
                System.out.println(fiveThirtyEightName);
            Team team = teams.getFromFiveThirtyEightName(fiveThirtyEightName);
            if (team == null)
                logger.warn("No match for fivethirtyeight team name:" + fiveThirtyEightName);
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
        team.setBiasAttack(updatedTeam.getBiasAttack());
        team.setBiasDefense(updatedTeam.getBiasDefense());
        updateName(() -> updatedTeam.getDisplayName(), name -> team.setDisplayName(name));
        updateName(() -> updatedTeam.getFiveThirtyEightName(), name -> team.setFiveThirtyEightName(name));
        updateName(() -> updatedTeam.getLsportsName(), name -> team.setLsportsName(name));

    }

    private void updateName(Supplier<String> getter, Consumer<String> setter) {
        String name = getter.get();
        if (name != null)
            setter.accept(name);
    }

    /**
     * writes the contents of all competitions to file. At present the supplied params are ignored
     * 
     * @param competition
     * @param datatype
     */
    private void persistData(Competition competition, DataType datatype) {
        synchronized (competition) {
            String fName = competitionFileName(competition);
            try {

                FileReadWrite.writeJson(fName, competition);
                logger.info("Competition data persisted to file: " + fName);
            } catch (IOException e) {
                logger.error("Can't write to competition file:" + fName);
                logger.error(e.toString());
            }
        }
    }

    private String competitionFileName(Competition competition) {
        return persistedDataFolder + competition.getOutrightsCompetitionID() + ".json";
    }

    public void setPersistedDataFolder(String folder) {
        this.persistedDataFolder = folder;
    }

    /**
     * reads the file containing saved competitions and then adds to the supplied set. If a competition saved to file
     * has the same eventID as a competition in the set supplied then the former overwrites the latter
     * 
     * @param competitions
     */
    public void loadPersistedData() {
        logger.info("reading competitions persisted to file");

        for (Competition competition : competitions) {
            String fName = competitionFileName(competition);
            Competition competition2 = null;
            try {
                competition2 = FileReadWrite.readJson(fName, Competition.class);
                logger.info("Successfully read competition file: " + fName);
                competitions.updateCompetition(competition, competition2);
                competition = competition2;
            } catch (IOException error) {
                logger.warn("Can't read competition file: " + fName);
            }
            logger.info("Updated data for: " + competition.getOutrightsCompetitionID());
        }
    }

    public PriceCalcResponse handlePriceCalcRequest(PriceCalcRequest request) {
        try {
            long eventID = request.getEventId();
            Competition competition = competitions.get(eventID);
            if (competition == null) {
                return PriceCalcResponse.generateFatalErrorResponse(request.getUniqueRequestId(), "eventID not known");
            }

            OutrightsMatchIncident incident = (OutrightsMatchIncident) request.getMatchIncident();
            PriceCalcResponse response = null;
            synchronized (competition) {
                if (incident != null)
                    synchronized (competition) {
                        /*
                         * must be in synchronized block because may update persistent data if a fixture is completed
                         */
                        if (competition.handleOutrightsFixturesData(incident.getOutrightsFixturesData())) {
                            persistData(competition, DataType.FIXTURES);
                            competition.updateDerivedFixtures();
                        }
                    }
                calculate(competition);
                OutrightsWatchList watchList = competition.generateWatchList();
                response = new PriceCalcResponse(request.getUniqueRequestId(), competition.getMarkets(),
                                request.getMatchParams(), watchList, competition.getNewResultedMarkets());
            }
            return response;
        } catch (Exception e) {
            logger.error("Unexpected exception when processing price calc request");
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            logger.error(sw.toString());
            logger.error("request causing the exception: " + request);
            return PriceCalcResponse.generateFatalErrorResponse(request.getUniqueRequestId(),
                            "Unexpected exception handling this request.  c.f outrights server log for details");
        }
    }

    /**
     * handler for param find requests received from AlgoManager - don't do anything just return success
     * 
     * @param request
     * @return
     */
    public ParamFindResponse handleParamFindRequest(ParamFindRequest request) {
        ParamFindResults results = new ParamFindResults();
        results.setParamFindResultsStatus(ParamFindResultsStatus.GREEN);
        results.setTraderParamFindResultsDescription(new TraderParamFindResultsDescription());
        ParamFindResponse response =
                        new ParamFindResponse(request.getUniqueRequestId(), results, request.getMatchParams());
        return response;
    }

    public void updateTargetPoints(Competition competition, TargetPointsEntry t) {
        synchronized (competition) {
            competition.getFcastStandings().updateTargetPoints(t.getTeamID(), t.getTargetPoints());
            persistData(competition, DataType.FIXTURES);
        }
    }

    public void updateEventIds(Competition competition, List<ATSFixture> atsFixtures) {
        synchronized (competition) {
            competition.getFixtures().updateEventIds(atsFixtures);
            persistData(competition, DataType.FIXTURES);
        }
    }

    /**
     * entry point for param finds via the api against target points
     * 
     * @param competition
     * @param list
     * @return
     */
    public Alert paramFind(Competition competition) {
        logger.info("Starting param find for competition: " + competition.getName());
        ParamFindEngine pfEngine = new ParamFindEngine(competition);
        pfEngine.calculate();
        Alert alert = pfEngine.getPfResult();
        logger.info(String.format("Pf completed for competition: %s, result: %s.", competition.getName(),
                        alert.getDescription()));
        return alert;
    }

    public void updateDerivedData() {
        competitions.forEach(c -> c.updateDerivedFixtures());

    }

    public Alerts getAlerts() {
        Alerts alerts = new Alerts();
        for (Entry<Long, Competition> e : competitions.getCompetitions().entrySet()) {
            Competition competition = e.getValue();
            alerts.addAlerts(competition.getAlerts());
        }
        return alerts;
    }



}
