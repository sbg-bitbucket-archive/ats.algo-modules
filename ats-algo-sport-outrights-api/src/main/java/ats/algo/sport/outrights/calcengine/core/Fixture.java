package ats.algo.sport.outrights.calcengine.core;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ats.core.util.json.JsonUtil;

public class Fixture {
    private String date;
    private FixtureType fixtureType;
    private String fixtureID;
    private String homeTeamID;
    private String awayTeamID;
    private long eventID;
    private String tag;
    private boolean playedAtNeutralGround;
    private FixtureStatus status;
    private boolean probsSourcedfromATS;
    private int goalsHome;
    private int goalsAway;
    private boolean mustHaveAWinner;

    /*
     * these properties are only relevant if this is the second leg of a two leg fixture
     */
    private String firstLegFixtureID;
    private int firstLegScoreA;
    private int firstLegScoreB;
    /*
     * these properties are not part of the API
     */
    private LocalDateTime dateTime;
    private TagMetaData tagMetaData;

    public Fixture() {}

    /**
     * 
     * @param date
     * @param fixtureID
     * @param homeTeamID
     * @param awayTeamID
     * @param tag
     * @param playedAtNeutralGround
     */
    public Fixture(String date, FixtureType fixtureType, String fixtureID, String homeTeamID, String awayTeamID,
                    String tag, boolean playedAtNeutralGround) {
        super();
        this.date = date;
        this.dateTime = LocalDateTime.parse(date);
        this.fixtureType = fixtureType;
        this.fixtureID = fixtureID;
        this.homeTeamID = homeTeamID;
        this.awayTeamID = awayTeamID;
        this.eventID = 0;
        this.tag = tag;
        this.tagMetaData = new TagMetaData(fixtureType, tag);
        this.playedAtNeutralGround = playedAtNeutralGround;
        this.status = FixtureStatus.PRE_MATCH;
        this.probsSourcedfromATS = false;
        this.goalsHome = 0;
        this.goalsAway = 0;
        this.mustHaveAWinner = false;
    }

    public static Fixture generateSecondLegFixture(String date, FixtureType fixtureType, String fixtureID,
                    String homeTeamID, String awayTeamID, String tag, boolean playedAtNeutralGround,
                    String firstLegFixtureID) {
        Fixture fixture = new Fixture(date, fixtureType, fixtureID, homeTeamID, awayTeamID, tag, playedAtNeutralGround);
        fixture.firstLegFixtureID = firstLegFixtureID;
        return fixture;
    }

    public void setEqualTo(Fixture other) {
        this.date = other.date;
        this.dateTime = other.dateTime;
        this.fixtureType = other.fixtureType;
        this.fixtureID = other.fixtureID;
        this.homeTeamID = other.homeTeamID;
        this.awayTeamID = other.awayTeamID;
        this.eventID = other.eventID;
        this.tag = other.tag;
        this.tagMetaData = other.tagMetaData;
        this.playedAtNeutralGround = other.playedAtNeutralGround;
        this.status = other.status;
        this.probsSourcedfromATS = other.probsSourcedfromATS;
        this.goalsHome = other.goalsHome;
        this.goalsAway = other.goalsAway;
        this.mustHaveAWinner = other.mustHaveAWinner;
        this.firstLegFixtureID = other.firstLegFixtureID;
        this.firstLegScoreA = other.firstLegScoreA;
        this.firstLegScoreB = other.firstLegScoreB;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
        this.dateTime = LocalDateTime.parse(date);
    }

    public String getFixtureID() {
        return fixtureID;
    }

    public void setFixtureID(String fixtureID) {
        this.fixtureID = fixtureID;
    }

    public FixtureStatus getStatus() {
        return status;
    }

    public void setStatus(FixtureStatus status) {
        this.status = status;
    }

    public String getHomeTeamID() {
        return homeTeamID;
    }

    public void setHomeTeamID(String homeTeamID) {
        this.homeTeamID = homeTeamID;
    }

    public String getAwayTeamID() {
        return awayTeamID;
    }

    public void setAwayTeamID(String awayTeamID) {
        this.awayTeamID = awayTeamID;
    }

    public long getEventID() {
        return eventID;
    }

    public void setEventID(long eventID) {
        this.eventID = eventID;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
        /*
         * when deserialising json, the setFixtureType andsetTag methods may get called in either order. can only
         * initialise TagMetaData when both have been invoked
         */
        if (fixtureType != null) {
            this.tagMetaData = new TagMetaData(fixtureType, tag);
        }
    }

    public boolean isPlayedAtNeutralGround() {
        return playedAtNeutralGround;
    }

    public void setPlayedAtNeutralGround(boolean playedAtNeutralGround) {
        this.playedAtNeutralGround = playedAtNeutralGround;
    }

    public int getGoalsHome() {
        return goalsHome;
    }

    public void setGoalsHome(int goalsHome) {
        this.goalsHome = goalsHome;
    }

    public int getGoalsAway() {
        return goalsAway;
    }

    public void setGoalsAway(int goalsAway) {
        this.goalsAway = goalsAway;
    }

    public boolean isProbsSourcedfromATS() {
        return probsSourcedfromATS;
    }

    public void setProbsSourcedfromATS(boolean probsSourcedfromATS) {
        this.probsSourcedfromATS = probsSourcedfromATS;
    }

    public FixtureType getFixtureType() {
        return fixtureType;
    }

    public void setFixtureType(FixtureType fixtureType) {
        this.fixtureType = fixtureType;
        /*
         * when deserialising json, the setFixtureType andsetTag methods may get called in either order. can only
         * initialise TagMetaData when both have been invoked
         */
        if (tag != null) {
            this.tagMetaData = new TagMetaData(fixtureType, tag);
        }
    }

    public boolean isMustHaveAWinner() {
        return mustHaveAWinner;
    }

    public void setMustHaveAWinner(boolean mustHaveAWinner) {
        this.mustHaveAWinner = mustHaveAWinner;
    }

    public int getFirstLegScoreA() {
        return firstLegScoreA;
    }

    public void setFirstLegScoreA(int firstLegScoreA) {
        this.firstLegScoreA = firstLegScoreA;
    }

    public int getFirstLegScoreB() {
        return firstLegScoreB;
    }

    public void setFirstLegScoreB(int firstLegScoreB) {
        this.firstLegScoreB = firstLegScoreB;
    }

    public void setFirstLegFixtureID(String firstLegFixtureID) {
        this.firstLegFixtureID = firstLegFixtureID;
    }

    public String getFirstLegFixtureID() {
        return firstLegFixtureID;
    }

    /**
     * properties below are not part of the API - derived from other properties
     * 
     * @return
     */
    @JsonIgnore
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @JsonIgnore
    public TagMetaData getTagMetaData() {
        return tagMetaData;
    }

    @Override
    public String toString() {
        return JsonUtil.marshalJson(this);
    }

    public Fixture copy() {
        Fixture cc = new Fixture();
        cc.setEqualTo(this);
        return cc;
    }

    /**
     * returns true if all the data required to calculate fixture probs is available
     * 
     * @return
     */
    public boolean canCalcProbs() {
        return homeTeamID != null && awayTeamID != null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((awayTeamID == null) ? 0 : awayTeamID.hashCode());
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ((dateTime == null) ? 0 : dateTime.hashCode());
        result = prime * result + (int) (eventID ^ (eventID >>> 32));
        result = prime * result + ((firstLegFixtureID == null) ? 0 : firstLegFixtureID.hashCode());
        result = prime * result + firstLegScoreA;
        result = prime * result + firstLegScoreB;
        result = prime * result + ((fixtureID == null) ? 0 : fixtureID.hashCode());
        result = prime * result + ((fixtureType == null) ? 0 : fixtureType.hashCode());
        result = prime * result + goalsAway;
        result = prime * result + goalsHome;
        result = prime * result + ((homeTeamID == null) ? 0 : homeTeamID.hashCode());
        result = prime * result + (mustHaveAWinner ? 1231 : 1237);
        result = prime * result + (playedAtNeutralGround ? 1231 : 1237);
        result = prime * result + (probsSourcedfromATS ? 1231 : 1237);
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + ((tag == null) ? 0 : tag.hashCode());
        result = prime * result + ((tagMetaData == null) ? 0 : tagMetaData.hashCode());
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
        Fixture other = (Fixture) obj;
        if (awayTeamID == null) {
            if (other.awayTeamID != null)
                return false;
        } else if (!awayTeamID.equals(other.awayTeamID))
            return false;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        if (dateTime == null) {
            if (other.dateTime != null)
                return false;
        } else if (!dateTime.equals(other.dateTime))
            return false;
        if (eventID != other.eventID)
            return false;
        if (firstLegFixtureID == null) {
            if (other.firstLegFixtureID != null)
                return false;
        } else if (!firstLegFixtureID.equals(other.firstLegFixtureID))
            return false;
        if (firstLegScoreA != other.firstLegScoreA)
            return false;
        if (firstLegScoreB != other.firstLegScoreB)
            return false;
        if (fixtureID == null) {
            if (other.fixtureID != null)
                return false;
        } else if (!fixtureID.equals(other.fixtureID))
            return false;
        if (fixtureType != other.fixtureType)
            return false;
        if (goalsAway != other.goalsAway)
            return false;
        if (goalsHome != other.goalsHome)
            return false;
        if (homeTeamID == null) {
            if (other.homeTeamID != null)
                return false;
        } else if (!homeTeamID.equals(other.homeTeamID))
            return false;
        if (mustHaveAWinner != other.mustHaveAWinner)
            return false;
        if (playedAtNeutralGround != other.playedAtNeutralGround)
            return false;
        if (probsSourcedfromATS != other.probsSourcedfromATS)
            return false;
        if (status != other.status)
            return false;
        if (tag == null) {
            if (other.tag != null)
                return false;
        } else if (!tag.equals(other.tag))
            return false;
        if (tagMetaData == null) {
            if (other.tagMetaData != null)
                return false;
        } else if (!tagMetaData.equals(other.tagMetaData))
            return false;
        return true;
    }

}
