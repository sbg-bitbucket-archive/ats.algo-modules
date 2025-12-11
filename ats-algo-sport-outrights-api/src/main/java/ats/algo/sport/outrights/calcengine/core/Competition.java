package ats.algo.sport.outrights.calcengine.core;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ats.algo.core.markets.Market;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.sport.outrights.CompetitionType;
import ats.algo.sport.outrights.OutrightsWatchList;
import ats.core.util.json.JsonUtil;

public class Competition {
    private CompetitionType competitionType;
    private long eventID;
    private String name;
    private String fiveThirtyEightName;
    private Teams teams;
    private Fixtures fixtures;
    private Standings standings;
    private String reqdInputMarketType;
    private RatingsFactors ratingsFactors;

    /*
     * the following properties are not part of the API and should be marked as @JsonIgnore
     */
    private Map<Long, Market> inputMarkets;
    private Markets markets;
    private ResultedMarkets resultedMarkets;

    public Competition() {}

    /**
     * 
     * @param competitionType
     * @param eventID
     * @param name
     * @param fiveThirtyEightName
     * @param teams
     * @param fixtures
     * @param standings
     */
    public Competition(CompetitionType competitionType, long eventID, String name, String fiveThirtyEightName,
                    Teams teams, Fixtures fixtures, Standings standings) {
        super();
        this.competitionType = competitionType;
        this.eventID = eventID;
        this.name = name;
        this.fiveThirtyEightName = fiveThirtyEightName;
        this.teams = teams;
        this.fixtures = fixtures;
        this.standings = standings;
        this.ratingsFactors = RatingsFactors.defaultFiveThirtyEightRatingsFactors();
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

    public Standings getStandings() {
        return standings;
    }

    public void setStandings(Standings standings) {
        this.standings = standings;
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
    public ResultedMarkets getResultedMarkets() {
        return resultedMarkets;
    }

    @JsonIgnore
    public void setResultedMarkets(ResultedMarkets resultedMarkets) {
        this.resultedMarkets = resultedMarkets;
    }

    @JsonIgnore
    public Map<Long, Market> getInputMarkets() {
        return inputMarkets;
    }

    @JsonIgnore
    public void setInputMarkets(Map<Long, Market> inputMarkets) {
        this.inputMarkets = inputMarkets;
    }

    /*
     * ... end non API properties
     * 
     */

    @Override
    public String toString() {
        return JsonUtil.marshalJson(this);
    }

    public OutrightsWatchList generateWatchList() {
        OutrightsWatchList watchList = new OutrightsWatchList();
        for (Fixture fixture : fixtures) {
            long eventID = fixture.getEventID();
            if (eventID != 0) {
                String marketType;
                if (fixture.isMustHaveAWinner())
                    marketType = "FT:TQ"; // Correct score
                else
                    marketType = "FT:CS"; // to qualify
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
        result = prime * result + ((fiveThirtyEightName == null) ? 0 : fiveThirtyEightName.hashCode());
        result = prime * result + ((fixtures == null) ? 0 : fixtures.hashCode());
        result = prime * result + ((inputMarkets == null) ? 0 : inputMarkets.hashCode());
        result = prime * result + ((markets == null) ? 0 : markets.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((ratingsFactors == null) ? 0 : ratingsFactors.hashCode());
        result = prime * result + ((reqdInputMarketType == null) ? 0 : reqdInputMarketType.hashCode());
        result = prime * result + ((resultedMarkets == null) ? 0 : resultedMarkets.hashCode());
        result = prime * result + ((standings == null) ? 0 : standings.hashCode());
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
        if (inputMarkets == null) {
            if (other.inputMarkets != null)
                return false;
        } else if (!inputMarkets.equals(other.inputMarkets))
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
        if (resultedMarkets == null) {
            if (other.resultedMarkets != null)
                return false;
        } else if (!resultedMarkets.equals(other.resultedMarkets))
            return false;
        if (standings == null) {
            if (other.standings != null)
                return false;
        } else if (!standings.equals(other.standings))
            return false;
        if (teams == null) {
            if (other.teams != null)
                return false;
        } else if (!teams.equals(other.teams))
            return false;
        return true;
    }
}
