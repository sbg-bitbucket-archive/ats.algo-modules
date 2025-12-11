package ats.algo.core.baseclasses;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ats.algo.core.common.AbandonMatchIncident;
import ats.algo.core.common.DatafeedMatchIncident;
import ats.algo.core.common.ElapsedTimeMatchIncident;
import ats.algo.core.common.MatchReferralIncident;
import ats.algo.core.common.PlayerMatchIncident;
import ats.algo.core.common.ResultedMarketsMatchIncident;
import ats.algo.core.common.TeamId;
import ats.algo.core.common.TeamSheetMatchIncident;
import ats.algo.genericsupportfunctions.CopySerializableObject;
import ats.algo.sport.afl.AflMatchIncident;
import ats.algo.sport.americanfootball.AmericanfootballMatchIncident;
import ats.algo.sport.badminton.BadmintonMatchIncident;
import ats.algo.sport.bandy.BandyMatchIncident;
import ats.algo.sport.baseball.BaseballMatchIncident;
import ats.algo.sport.basketball.BasketballMatchIncident;
import ats.algo.sport.beachvolleyball.BeachVolleyballMatchIncident;
import ats.algo.sport.bowls.BowlsMatchIncident;
import ats.algo.sport.cricket.CricketMatchIncident;
import ats.algo.sport.darts.DartMatchIncident;
import ats.algo.sport.fieldhockey.FieldhockeyMatchIncident;
import ats.algo.sport.floorball.FloorballMatchIncident;
import ats.algo.sport.football.FootballMatchIncident;
import ats.algo.sport.futsal.FutsalMatchIncident;
import ats.algo.sport.handball.HandballMatchIncident;
import ats.algo.sport.icehockey.IcehockeyMatchIncident;
import ats.algo.sport.outrights.OutrightsMatchIncident;
import ats.algo.sport.rollerhockey.RollerhockeyMatchIncident;
import ats.algo.sport.rugbyunion.RugbyUnionMatchIncident;
import ats.algo.sport.rugbyleague.RugbyLeagueMatchIncident;
import ats.algo.sport.snooker.SnookerMatchIncident;
import ats.algo.sport.squash.SquashMatchIncident;
import ats.algo.sport.tabletennis.TabletennisMatchIncident;
import ats.algo.sport.tennis.TennisMatchIncident;
import ats.algo.sport.testcricket.TestCricketMatchIncident;
import ats.algo.sport.testsport.TestSportMatchIncident;
import ats.algo.sport.fantasyexample.FantasyExampleSportMatchIncident;
import ats.algo.sport.volleyball.VolleyballMatchIncident;
import ats.core.util.json.JsonUtil;

/**
 * The template for reporting incidents that occur during a match that affect price calculations
 * 
 * @author
 *
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "subClass")
@JsonSubTypes({@Type(name = "TennisMatchIncident", value = TennisMatchIncident.class),
        @Type(name = "AbandonMatchIncident", value = AbandonMatchIncident.class),
        @Type(name = "MatchReferralIncident", value = MatchReferralIncident.class),
        @Type(name = "AflMatchIncident", value = AflMatchIncident.class),
        @Type(name = "AmericanfootballMatchIncident", value = AmericanfootballMatchIncident.class),
        @Type(name = "BadmintonMatchIncident", value = BadmintonMatchIncident.class),
        @Type(name = "BandyMatchIncident", value = BandyMatchIncident.class),
        @Type(name = "BaseballMatchIncident", value = BaseballMatchIncident.class),
        @Type(name = "BasketballMatchIncident", value = BasketballMatchIncident.class),
        @Type(name = "BeachVolleyballMatchIncident", value = BeachVolleyballMatchIncident.class),
        @Type(name = "BowlsMatchIncident", value = BowlsMatchIncident.class),
        @Type(name = "CricketMatchIncident", value = CricketMatchIncident.class),
        @Type(name = "DartMatchIncident", value = DartMatchIncident.class),
        @Type(name = "DatafeedMatchIncident", value = DatafeedMatchIncident.class),
        @Type(name = "ElapsedTimeMatchIncident", value = ElapsedTimeMatchIncident.class),
        @Type(name = "FieldhockeyMatchIncident", value = FieldhockeyMatchIncident.class),
        @Type(name = "FloorballMatchIncident", value = FloorballMatchIncident.class),
        @Type(name = "FootballMatchIncident", value = FootballMatchIncident.class),
        @Type(name = "FutsalMatchIncident", value = FutsalMatchIncident.class),
        @Type(name = "HandballMatchIncident", value = HandballMatchIncident.class),
        @Type(name = "IcehockeyMatchIncident", value = IcehockeyMatchIncident.class),
        @Type(name = "RollerhockeyMatchIncident", value = RollerhockeyMatchIncident.class),
        @Type(name = "RugbyUnionMatchIncident", value = RugbyLeagueMatchIncident.class),
        @Type(name = "RugbyLeagueMatchIncident", value = RugbyUnionMatchIncident.class),
        @Type(name = "SnookerMatchIncident", value = SnookerMatchIncident.class),
        @Type(name = "SquashMatchIncident", value = SquashMatchIncident.class),
        @Type(name = "TabletennisMatchIncident", value = TabletennisMatchIncident.class),
        @Type(name = "TeamSheetMatchIncident", value = TeamSheetMatchIncident.class),
        @Type(name = "TestCricketMatchIncident", value = TestCricketMatchIncident.class),
        @Type(name = "VolleyballMatchIncident", value = VolleyballMatchIncident.class),
        @Type(name = "PlayerMatchIncident", value = PlayerMatchIncident.class),
        @Type(name = "TestSportMatchIncident", value = TestSportMatchIncident.class),
        @Type(name = "FantasyExampleSportMatchIncident", value = FantasyExampleSportMatchIncident.class),
        @Type(name = "ResultedMarketsMatchIncident", value = ResultedMarketsMatchIncident.class),
        @Type(name = "OutrightsMatchIncident", value = OutrightsMatchIncident.class)})
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class MatchIncident implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean undo;
    protected int elapsedTimeSecs;
    protected long eventId;
    private String incidentId;
    protected TeamId teamId; // TEAM THAT WON THE MATCH IF MATCH ABANDONED
    private String sourceSystem;
    private String externalEventId;
    private long timeStamp;
    private MatchStateFromFeed matchStateFromFeed;

    /**
     * Determine whether this incident has been cancelled/deleted/undone
     * 
     * @return
     */
    public boolean isUndo() {
        return undo;
    }

    /**
     * Specify whether this incident is to be cancelled/undone
     * 
     * @param undo
     */
    public void setUndo(boolean undo) {
        this.undo = undo;
    }

    /**
     * 
     * @return the unique identifier of this incident within the context of the event
     */
    public String getIncidentId() {
        return incidentId;
    }

    /**
     * Sets the unique incident id within the context of an event
     * 
     * @param requestId
     */
    public void setIncidentId(String incidentId) {
        this.incidentId = incidentId;
    }

    /**
     * Gets the source of this matchIncident
     * 
     * @return
     */
    public String getSourceSystem() {
        return sourceSystem;
    }

    /**
     * sets the source of this matchIncident
     * 
     * @param sourceSystem
     */
    public void setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
    }

    /**
     * gets the identifier associated with this event
     * 
     * @return
     */
    public String getExternalEventId() {
        return externalEventId;
    }

    /**
     * sets the identifier associated with this event
     * 
     * @param externalEventId
     */
    public void setExternalEventId(String externalEventId) {
        this.externalEventId = externalEventId;
    }

    /**
     * 
     * @return the ats event id of the event for the match from which the incident originated
     */
    public long getEventId() {
        return eventId;
    }

    /**
     * Specify the ats event id for the match from which the incident originated
     * 
     * @param eventId
     */
    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    /**
     * returns the type of this incident.
     * 
     * @return object of the form "xxxMatchIncidentType"
     */
    public abstract Object getIncidentSubType();



    /**
     * 
     * @return the elapsed time in seconds since the start of the match. elapsedTime = -1 means that the matchIncident
     *         does not contain a valid value for elapsedTime
     */
    public int getElapsedTimeSecs() {
        return elapsedTimeSecs;
    }

    /**
     * set the elapsed time in seconds since the start of the match. elapsedTime = -1 means that the matchIncident does
     * not contain a valid value for elapsedTime
     * 
     * @param elapsedTime
     */
    public void setElapsedTime(int elapsedTime) {
        this.elapsedTimeSecs = elapsedTime;
    }

    /**
     * Specify the team involved in the incident
     * 
     * @return
     */
    public void setTeamId(TeamId teamId) {
        this.teamId = teamId;
    }

    /**
     * get the team involved in the incident
     * 
     * @return
     */
    public TeamId getTeamId() {
        return teamId;
    }

    /**
     * The time when this MatchIncident was created, in milliseconds since 1/1/1970 (java std time)
     * 
     * @return
     */

    public long getTimeStamp() {
        return timeStamp;
    }

    /**
     * The time when this MatchIncident was created, in milliseconds since 1/1/1970 (java std time)
     * 
     * @param timeStamp
     */
    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setElapsedTimeSecs(int elapsedTimeSecs) {
        this.elapsedTimeSecs = elapsedTimeSecs;
    }



    public MatchStateFromFeed getMatchStateFromFeed() {
        return matchStateFromFeed;
    }

    public void setMatchStateFromFeed(MatchStateFromFeed matchStateFromFeed) {
        this.matchStateFromFeed = matchStateFromFeed;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + elapsedTimeSecs;
        result = prime * result + (int) (eventId ^ (eventId >>> 32));
        result = prime * result + ((externalEventId == null) ? 0 : externalEventId.hashCode());
        result = prime * result + ((matchStateFromFeed == null) ? 0 : matchStateFromFeed.hashCode());
        result = prime * result + ((incidentId == null) ? 0 : incidentId.hashCode());
        result = prime * result + ((sourceSystem == null) ? 0 : sourceSystem.hashCode());
        result = prime * result + ((teamId == null) ? 0 : teamId.hashCode());
        result = prime * result + (int) (timeStamp ^ (timeStamp >>> 32));
        result = prime * result + (undo ? 1231 : 1237);
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
        MatchIncident other = (MatchIncident) obj;
        if (elapsedTimeSecs != other.elapsedTimeSecs)
            return false;
        if (eventId != other.eventId)
            return false;
        if (externalEventId == null) {
            if (other.externalEventId != null)
                return false;
        } else if (!externalEventId.equals(other.externalEventId))
            return false;
        if (matchStateFromFeed == null) {
            if (other.matchStateFromFeed != null)
                return false;
        } else if (!matchStateFromFeed.equals(other.matchStateFromFeed))
            return false;
        if (incidentId == null) {
            if (other.incidentId != null)
                return false;
        } else if (!incidentId.equals(other.incidentId))
            return false;
        if (sourceSystem == null) {
            if (other.sourceSystem != null)
                return false;
        } else if (!sourceSystem.equals(other.sourceSystem))
            return false;
        if (teamId != other.teamId)
            return false;
        if (timeStamp != other.timeStamp)
            return false;
        if (undo != other.undo)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return JsonUtil.marshalJson(this);
    }

    /**
     * provides a short description of the score as supplied by the source feed
     * 
     * @return
     */
    public String toStringScorePerFeed() {
        if (matchStateFromFeed != null)
            return matchStateFromFeed.toShortString();
        else
            return "";
    }

    protected <E extends Enum<E>> E resolveEnumMember(String targetTypeName, Class<E> clazz) {
        for (E type : clazz.getEnumConstants()) {
            if (type.name().equalsIgnoreCase(targetTypeName)) {
                return type;
            }
        }
        return null;
    }

    public MatchIncident copy() {
        return CopySerializableObject.copy(this);
    }

    public String shortDescription() {

        return this.getClass().getSimpleName() + ": " + this.getIncidentSubType().toString();
    }
}
