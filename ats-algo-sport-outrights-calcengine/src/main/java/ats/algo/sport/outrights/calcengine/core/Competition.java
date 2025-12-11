package ats.algo.sport.outrights.calcengine.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ats.algo.core.markets.Market;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarket;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.markets.SuspensionStatus;
import ats.algo.sport.outrights.CompetitionType;
import ats.algo.sport.outrights.OutrightsFixtureData;
import ats.algo.sport.outrights.OutrightsFixtureStatus;
import ats.algo.sport.outrights.OutrightsWatchList;
import ats.algo.sport.outrights.calcengine.leagues.LeaguePlayoffFixturesUpdater;
import ats.algo.sport.outrights.server.api.Alert;
import ats.algo.sport.outrights.server.api.StandingsList;
import ats.algo.sport.outrights.server.api.StandingsListEntry;
import ats.algo.sport.outrights.calcengine.leagues.LeagueMarketsResulter;
import ats.core.util.json.JsonUtil;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Competition {
    private CompetitionType competitionType;
    private String outrightsCompetitionID;
    private String atsCompetitionID;
    private long eventID;
    private String name;
    private String fiveThirtyEightName;
    private Teams teams;
    private Fixtures fixtures;
    private String reqdInputMarketType;
    private RatingsFactors ratingsFactors;
    private FcastStandings fcastStandings;
    private boolean suspendMarkets;
    private CompetitionAlerts alerts;

    /*
     * the following properties are not part of the API and should be marked as @JsonIgnore
     */
    private Map<Long, FullMatchProbs> fixturesProbs;
    private Markets markets;
    private ResultedMarkets newResultedMarkets;
    private ResultedMarkets previouslyPublishedResultedMarkets;



    public Competition() {
        fixturesProbs = new HashMap<>();
        alerts = new CompetitionAlerts();
        previouslyPublishedResultedMarkets = new ResultedMarkets();
    }

    /**
     * 
     * @param competitionType
     * @param outrightsCompetitionID
     * @param eventID
     * @param name
     * @param fiveThirtyEightName
     * @param teams
     * @param fixtures
     */
    public Competition(CompetitionType competitionType, String outrightsCompetitionID, long eventID, String name,
                    String fiveThirtyEightName, Teams teams, Fixtures fixtures) {
        this();
        this.outrightsCompetitionID = outrightsCompetitionID;
        this.competitionType = competitionType;
        this.atsCompetitionID = "";
        this.eventID = eventID;
        this.name = name;
        this.fiveThirtyEightName = fiveThirtyEightName;
        this.teams = teams;
        this.fixtures = fixtures;
        this.ratingsFactors = RatingsFactors.defaultFiveThirtyEightRatingsFactors();
        this.fcastStandings = initFcastStandings(teams);
        fixtures.forEach(f -> {
            String s = LeaguePlayoffFixturesUpdater.validateFixture(f);
            if (s != null)
                throw new IllegalArgumentException(s);
        });
    }

    private FcastStandings initFcastStandings(Teams teams) {
        FcastStandings fcastStandings = new FcastStandings();
        teams.getTeams().forEach((k, v) -> {
            fcastStandings.put(k, new FcastStanding());
        });
        return fcastStandings;
    }

    public CompetitionType getCompetitionType() {
        return competitionType;
    }

    public void setCompetitionType(CompetitionType competitionType) {
        this.competitionType = competitionType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFiveThirtyEightName() {
        return fiveThirtyEightName;
    }

    public void setFiveThirtyEightName(String fiveThirtyEightName) {
        this.fiveThirtyEightName = fiveThirtyEightName;
    }

    public Teams getTeams() {
        return teams;
    }

    public void setTeams(Teams teams) {
        this.teams = teams;
    }

    public Fixtures getFixtures() {
        return fixtures;
    }

    public void setFixtures(Fixtures fixtures) {
        this.fixtures = fixtures;
    }

    public Standings generateStandings() {
        return Standings.generateStandingsFromFixtures(teams, fixtures);
    }

    /**
     * the eventID to be used when publishing markets.
     * 
     * @return
     */
    public long getEventID() {
        return eventID;
    }

    public void setEventID(long atsEventID) {
        this.eventID = atsEventID;
    }

    public String getReqdInputMarketType() {
        return reqdInputMarketType;
    }

    public void setReqdInputMarketType(String reqdInputMarketType) {
        this.reqdInputMarketType = reqdInputMarketType;
    }

    public RatingsFactors getRatingsFactors() {
        return ratingsFactors;
    }

    public void setRatingsFactors(RatingsFactors ratingsFactors) {
        this.ratingsFactors = ratingsFactors;
    }

    public FcastStandings getFcastStandings() {
        return fcastStandings;
    }

    public void setFcastStandings(FcastStandings fcastStandings) {
        this.fcastStandings = fcastStandings;
    }

    public void setSuspendMarkets(boolean suspendMarkets) {
        this.suspendMarkets = suspendMarkets;
    }

    public boolean isSuspendMarkets() {
        return suspendMarkets;
    }

    public String getAtsCompetitionID() {
        return atsCompetitionID;
    }

    public void setAtsCompetitionID(String atsCompetitionId) {
        this.atsCompetitionID = atsCompetitionId;
    }

    public String getOutrightsCompetitionID() {
        return outrightsCompetitionID;
    }

    public void setOutrightsCompetitionID(String outrightsCompetitionID) {
        this.outrightsCompetitionID = outrightsCompetitionID;
    }

    /*
     * Non API properties...
     * 
     */
    @JsonIgnore
    public Markets getMarkets() {
        return markets;
    }

    @JsonIgnore
    public void setMarkets(Markets markets) {
        this.markets = markets;
    }

    @JsonIgnore
    public ResultedMarkets getNewResultedMarkets() {
        return newResultedMarkets;
    }

    @JsonIgnore
    public void setNewResultedMarkets(ResultedMarkets resultedMarkets) {
        this.newResultedMarkets = resultedMarkets;
    }

    @JsonIgnore
    public FullMatchProbs getFixtureProbs(long eventId) {
        return fixturesProbs.get(eventId);
    }

    @JsonIgnore
    public ResultedMarkets getPreviouslyPublishedResultedMarkets() {
        return previouslyPublishedResultedMarkets;
    }

    @JsonIgnore
    public void setPreviouslyPublishedResultedMarkets(ResultedMarkets previouslyPublishedResultedMarkets) {
        this.previouslyPublishedResultedMarkets = previouslyPublishedResultedMarkets;
    }



    private LocalDateTime dateOfXmas;

    /**
     * for testing purposes only
     * 
     * @param dateStr
     */
    public void setXmas(String dateStr) {
        this.dateOfXmas = LocalDateTime.parse(dateStr);
    }

    @JsonIgnore
    public LocalDateTime getDateOfXmas() {
        if (dateOfXmas == null) {
            Fixture firstFixture = fixtures.get(0);
            int y = firstFixture.getDateTime().getYear();
            dateOfXmas = LocalDateTime.parse(y + "-12-25T00:00:00");
        }
        return dateOfXmas;
    }

    /*
     * ... end non API properties
     * 
     */

    public Map<Long, FullMatchProbs> atsFixtureProbs() {
        return fixturesProbs;
    }

    /**
     * 
     * @param outrightsFixturesData
     * @return true if any fixture has been resulted, else false
     */
    public boolean handleOutrightsFixturesData(Map<Long, OutrightsFixtureData> outrightsFixturesData) {
        /*
         * update the list of fixtures with the data sourced from ATS
         */
        boolean anyFixtureResulted = false;
        fixturesProbs.clear();
        for (Fixture fixture : fixtures) {
            boolean fixtureDataContainsMarket = false;
            long eventId = fixture.getEventID();
            if (eventId != 0) {
                OutrightsFixtureData outrightsFixtureData = outrightsFixturesData.get(eventId);
                if (outrightsFixtureData != null) {
                    boolean thisFixtureResulted = false;
                    ResultedMarket resultedMarket = outrightsFixtureData.getResultedMarket();
                    if (resultedMarket != null)
                        if (resultedMarket.isFullyResulted()) {
                            anyFixtureResulted = true;
                            thisFixtureResulted = true;
                            fixture.updateWithResult(resultedMarket.getWinningSelections().get(0));
                        }
                    if (!thisFixtureResulted) {
                        Market market = outrightsFixtureData.getMarket();
                        fixtureDataContainsMarket = market != null;
                        if (fixtureDataContainsMarket) {
                            /*
                             * only set fixture status from supplied data if we have a market
                             */
                            fixturesProbs.put(eventId, new FullMatchProbs(market));
                            fixture.setStatus(outrightsFixtureData.getFixtureStatus());
                            fixture.updateScore(market);
                            SuspensionStatus suspensionStatus = market.getMarketStatus().getSuspensionStatus();
                            boolean marketSuspended = suspensionStatus.equals(SuspensionStatus.SUSPENDED_DISPLAY)
                                            || suspensionStatus.equals(SuspensionStatus.SUSPENDED_UNDISPLAY);
                            fixture.setFixtureSuspended(marketSuspended || outrightsFixtureData.isSuspended());
                        }
                    }
                }
            }
            fixture.setProbsSourcedfromATS(fixtureDataContainsMarket);
        }
        return anyFixtureResulted;
    }

    @Override
    public String toString() {
        return JsonUtil.marshalJson(this);
    }

    public OutrightsWatchList generateWatchList() {
        OutrightsWatchList watchList = new OutrightsWatchList();
        for (Fixture fixture : fixtures) {
            long eventID = fixture.getEventID();
            if (eventID != 0 && fixture.getStatus() != OutrightsFixtureStatus.COMPLETED) {
                String marketType;
                if (fixture.isMustHaveAWinner())
                    marketType = "FT:TQ"; // to qualify
                else
                    marketType = "FT:CS"; // correct score
                watchList.addEntry(eventID, marketType);
            }
        }
        return watchList;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((competitionType == null) ? 0 : competitionType.hashCode());
        result = prime * result + (int) (eventID ^ (eventID >>> 32));
        result = prime * result + ((fcastStandings == null) ? 0 : fcastStandings.hashCode());
        result = prime * result + ((fiveThirtyEightName == null) ? 0 : fiveThirtyEightName.hashCode());
        result = prime * result + ((fixtures == null) ? 0 : fixtures.hashCode());
        result = prime * result + ((fixturesProbs == null) ? 0 : fixturesProbs.hashCode());
        result = prime * result + ((markets == null) ? 0 : markets.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((ratingsFactors == null) ? 0 : ratingsFactors.hashCode());
        result = prime * result + ((reqdInputMarketType == null) ? 0 : reqdInputMarketType.hashCode());
        result = prime * result + ((newResultedMarkets == null) ? 0 : newResultedMarkets.hashCode());
        result = prime * result + (suspendMarkets ? 1231 : 1237);
        result = prime * result + ((teams == null) ? 0 : teams.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Competition other = (Competition) obj;
        if (competitionType != other.competitionType)
            return false;
        if (eventID != other.eventID)
            return false;
        if (fcastStandings == null) {
            if (other.fcastStandings != null)
                return false;
        } else if (!fcastStandings.equals(other.fcastStandings))
            return false;
        if (fiveThirtyEightName == null) {
            if (other.fiveThirtyEightName != null)
                return false;
        } else if (!fiveThirtyEightName.equals(other.fiveThirtyEightName))
            return false;
        if (fixtures == null) {
            if (other.fixtures != null)
                return false;
        } else if (!fixtures.equals(other.fixtures))
            return false;
        if (fixturesProbs == null) {
            if (other.fixturesProbs != null)
                return false;
        } else if (!fixturesProbs.equals(other.fixturesProbs))
            return false;
        if (markets == null) {
            if (other.markets != null)
                return false;
        } else if (!markets.equals(other.markets))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (ratingsFactors == null) {
            if (other.ratingsFactors != null)
                return false;
        } else if (!ratingsFactors.equals(other.ratingsFactors))
            return false;
        if (reqdInputMarketType == null) {
            if (other.reqdInputMarketType != null)
                return false;
        } else if (!reqdInputMarketType.equals(other.reqdInputMarketType))
            return false;
        if (newResultedMarkets == null) {
            if (other.newResultedMarkets != null)
                return false;
        } else if (!newResultedMarkets.equals(other.newResultedMarkets))
            return false;
        if (suspendMarkets != other.suspendMarkets)
            return false;
        if (teams == null) {
            if (other.teams != null)
                return false;
        } else if (!teams.equals(other.teams))
            return false;
        return true;
    }

    public void updateDerivedFixtures() {
        switch (competitionType) {
            case CHAMPIONSHIP_LEAGUE:
            case EFL_DIVISION_1:
            case EFL_DIVISION_2:
                LeagueMarketsResulter resulter = new LeagueMarketsResulter(this);
                LeaguePlayoffFixturesUpdater.update(fixtures, resulter);
                break;
            case LA_LIGA:
            case PREMIER_LEAGUE:
                /*
                 * do nothing - there are no derived fixtures
                 */
                break;
            case UNKNOWN:
            default:
                throw new IllegalArgumentException(
                                "competition type - not yet supported for derived fixtures: " + competitionType);
        }
    }

    public StandingsList generateStandingsList() {
        List<Standing> list = this.generateStandings().finishOrder();
        LeagueMarketsResulter resulter = new LeagueMarketsResulter(this);
        StandingsList standingsList = new StandingsList();
        for (Standing standing : list) {
            StandingsListEntry e = new StandingsListEntry();
            e.setStanding(standing);
            String id = standing.getTeamId();
            e.setHighestPossibleFinishPosn(resulter.getHighestPossibleFinishPosn(id));
            e.setLowestPossibleFinishPosn(resulter.getLowestPossibleFinishPosn(id));
            e.setManualPointsAdj(teams.get(id).getManualPointsAdjustment());
            e.setManualtieBreakAdj(teams.get(id).getManualTieBreakAdjustment());
            standingsList.add(e);
        }
        return standingsList;
    }

    public void updateManualAdjustments(String teamId, int manualPointsAdj, int manualTieBreakAdj) {
        Team team = teams.get(teamId);
        if (team != null) {
            team.setManualPointsAdjustment(manualPointsAdj);
            team.setManualTieBreakAdjustment(manualTieBreakAdj);
        }
    }


    @JsonIgnore
    public List<Alert> getAlerts() {
        return alerts.getList();
    }

    public void addAlert(Alert alert) {
        alerts.add(alert);
    }

    public void updateAlert(Alert alert) {
        alerts.updateAlert(alert.getId(), alert.isAcknowledged());
    }
}
