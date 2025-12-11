package ats.algo.core.baseclasses;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ats.algo.core.GamePeriod;
import ats.algo.core.common.DatafeedMatchIncident;
import ats.algo.core.common.TeamId;
import ats.algo.core.common.DatafeedMatchIncident.DatafeedMatchIncidentType;
import ats.algo.core.common.ElapsedTimeMatchIncident.ElapsedTimeMatchIncidentType;
import ats.algo.core.common.TeamSheet;
import ats.algo.core.markets.DataToGenerateStaticMarkets;

import ats.algo.sport.afl.AflMatchState;
import ats.algo.sport.americanfootball.AmericanfootballMatchState;
import ats.algo.sport.badminton.BadmintonMatchState;
import ats.algo.sport.bandy.BandyMatchState;
import ats.algo.sport.baseball.BaseballMatchState;
import ats.algo.sport.basketball.BasketballMatchState;
import ats.algo.sport.beachvolleyball.BeachVolleyballMatchState;
import ats.algo.sport.bowls.BowlsMatchState;
import ats.algo.sport.cricket.CricketMatchState;
import ats.algo.sport.darts.DartMatchState;
import ats.algo.sport.fieldhockey.FieldhockeyMatchState;
import ats.algo.sport.floorball.FloorballMatchState;
import ats.algo.sport.football.FootballMatchState;
import ats.algo.sport.futsal.FutsalMatchState;
import ats.algo.sport.handball.HandballMatchState;
import ats.algo.sport.icehockey.IcehockeyMatchState;
import ats.algo.sport.rollerhockey.RollerhockeyMatchState;
import ats.algo.sport.rugbyunion.RugbyUnionMatchState;
import ats.algo.sport.snooker.SnookerMatchState;
import ats.algo.sport.squash.SquashMatchState;
import ats.algo.sport.tabletennis.TabletennisMatchState;
import ats.algo.sport.tennis.TennisMatchState;
import ats.algo.sport.testcricket.TestCricketMatchState;
import ats.algo.sport.volleyball.VolleyballMatchState;
import ats.algo.sport.testsport.TestSportMatchState;

import ats.algo.sport.afl.AflSimpleMatchState;
import ats.algo.sport.americanfootball.AmericanfootballSimpleMatchState;
import ats.algo.sport.badminton.BadmintonSimpleMatchState;
import ats.algo.sport.bandy.BandySimpleMatchState;
import ats.algo.sport.baseball.BaseballSimpleMatchState;
import ats.algo.sport.basketball.BasketballSimpleMatchState;
import ats.algo.sport.beachvolleyball.BeachVolleyballSimpleMatchState;
import ats.algo.sport.bowls.BowlsSimpleMatchState;
import ats.algo.sport.cricket.CricketSimpleMatchState;
import ats.algo.sport.darts.DartSimpleMatchState;
import ats.algo.sport.esports.EsportsSimpleMatchState;
import ats.algo.sport.fantasyexample.FantasyExampleSportMatchState;
import ats.algo.sport.fantasyexample.FantasyExampleSportSimpleMatchState;
import ats.algo.sport.fieldhockey.FieldhockeySimpleMatchState;
import ats.algo.sport.floorball.FloorballSimpleMatchState;
import ats.algo.sport.football.FootballSimpleMatchState;
import ats.algo.sport.futsal.FutsalSimpleMatchState;
import ats.algo.sport.handball.HandballSimpleMatchState;
import ats.algo.sport.icehockey.IcehockeySimpleMatchState;
import ats.algo.sport.outrights.OutrightsMatchState;
import ats.algo.sport.outrights.OutrightsSimpleMatchState;
import ats.algo.sport.rollerhockey.RollerhockeySimpleMatchState;
import ats.algo.sport.rugbyleague.RugbyLeagueMatchState;
import ats.algo.sport.rugbyleague.RugbyLeagueSimpleMatchState;
import ats.algo.sport.rugbyunion.RugbyUnionSimpleMatchState;
import ats.algo.sport.snooker.SnookerSimpleMatchState;
import ats.algo.sport.squash.SquashSimpleMatchState;
import ats.algo.sport.tabletennis.TabletennisSimpleMatchState;
import ats.algo.sport.tennis.TennisSimpleMatchState;
import ats.algo.sport.testcricket.TestCricketSimpleMatchState;
import ats.algo.sport.testsport.TestSportSimpleMatchState;
import ats.algo.sport.volleyball.VolleyballSimpleMatchState;
import ats.core.util.json.JsonUtil;

/**
 * holds everything related to the current state of the match that is necessary to calculate prices e.g. for tennis
 * might be the score + what the serve order is
 *
 * @author Geoff
 *
 */

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "subClass")
@JsonSubTypes({@Type(name = "AflMatchState", value = AflMatchState.class),
        @Type(name = "AmericanfootballMatchState", value = AmericanfootballMatchState.class),
        @Type(name = "BadmintonMatchState", value = BadmintonMatchState.class),
        @Type(name = "BandyMatchState", value = BandyMatchState.class),
        @Type(name = "BaseballMatchState", value = BaseballMatchState.class),
        @Type(name = "BasketballMatchState", value = BasketballMatchState.class),
        @Type(name = "BeachVolleyballMatchState", value = BeachVolleyballMatchState.class),
        @Type(name = "BowlsMatchState", value = BowlsMatchState.class),
        @Type(name = "CricketMatchState", value = CricketMatchState.class),
        @Type(name = "DartMatchState", value = DartMatchState.class),
        @Type(name = "FieldhockeyMatchState", value = FieldhockeyMatchState.class),
        @Type(name = "FloorballMatchState", value = FloorballMatchState.class),
        @Type(name = "FootballMatchState", value = FootballMatchState.class),
        @Type(name = "FutsalMatchState", value = FutsalMatchState.class),
        @Type(name = "HandballMatchState", value = HandballMatchState.class),
        @Type(name = "IcehockeyMatchState", value = IcehockeyMatchState.class),
        @Type(name = "RollerhockeyMatchState", value = RollerhockeyMatchState.class),
        @Type(name = "RugbyUnionMatchState", value = RugbyUnionMatchState.class),
        @Type(name = "RugbyLeagueMatchState", value = RugbyLeagueMatchState.class),
        @Type(name = "SnookerMatchState", value = SnookerMatchState.class),
        @Type(name = "SquashMatchState", value = SquashMatchState.class),
        @Type(name = "TabletennisMatchState", value = TabletennisMatchState.class),
        @Type(name = "TennisMatchState", value = TennisMatchState.class),
        @Type(name = "TestCricketMatchState", value = TestCricketMatchState.class),
        @Type(name = "VolleyballMatchState", value = VolleyballMatchState.class),
        @Type(name = "TestSportMatchState", value = TestSportMatchState.class),
        @Type(name = "FantasyExampleSportMatchState", value = FantasyExampleSportMatchState.class),
        @Type(name = "OutrightsMatchState", value = OutrightsMatchState.class),
        @Type(name = "AflSimpleMatchState", value = AflSimpleMatchState.class),
        @Type(name = "AmericanfootballSimpleMatchState", value = AmericanfootballSimpleMatchState.class),
        @Type(name = "BadmintonSimpleMatchState", value = BadmintonSimpleMatchState.class),
        @Type(name = "BandySimpleMatchState", value = BandySimpleMatchState.class),
        @Type(name = "BaseballSimpleMatchState", value = BaseballSimpleMatchState.class),
        @Type(name = "BasketballSimpleMatchState", value = BasketballSimpleMatchState.class),
        @Type(name = "BeachVolleyballSimpleMatchState", value = BeachVolleyballSimpleMatchState.class),
        @Type(name = "BowlsSimpleMatchState", value = BowlsSimpleMatchState.class),
        @Type(name = "CricketSimpleMatchState", value = CricketSimpleMatchState.class),
        @Type(name = "DartSimpleMatchState", value = DartSimpleMatchState.class),
        @Type(name = "FieldhockeySimpleMatchState", value = FieldhockeySimpleMatchState.class),
        @Type(name = "FloorballSimpleMatchState", value = FloorballSimpleMatchState.class),
        @Type(name = "FootballSimpleMatchState", value = FootballSimpleMatchState.class),
        @Type(name = "FutsalSimpleMatchState", value = FutsalSimpleMatchState.class),
        @Type(name = "HandballSimpleMatchState", value = HandballSimpleMatchState.class),
        @Type(name = "IcehockeySimpleMatchState", value = IcehockeySimpleMatchState.class),
        @Type(name = "RollerhockeySimpleMatchState", value = RollerhockeySimpleMatchState.class),
        @Type(name = "RugbyLeagueSimpleMatchState", value = RugbyLeagueSimpleMatchState.class),
        @Type(name = "RugbyUnionSimpleMatchState", value = RugbyUnionSimpleMatchState.class),
        @Type(name = "SnookerSimpleMatchState", value = SnookerSimpleMatchState.class),
        @Type(name = "SquashSimpleMatchState", value = SquashSimpleMatchState.class),
        @Type(name = "TabletennisSimpleMatchState", value = TabletennisSimpleMatchState.class),
        @Type(name = "TennisSimpleMatchState", value = TennisSimpleMatchState.class),
        @Type(name = "TestCricketSimpleMatchState", value = TestCricketSimpleMatchState.class),
        @Type(name = "VolleyballSimpleMatchState", value = VolleyballSimpleMatchState.class),
        @Type(name = "TestSportSimpleMatchState", value = TestSportSimpleMatchState.class),
        @Type(name = "FantasyExampleSportSimpleMatchState", value = FantasyExampleSportSimpleMatchState.class),
        @Type(name = "OutrightsSimpleMatchState", value = OutrightsSimpleMatchState.class),
        @Type(name = "EsportsSimpleMatchState", value = EsportsSimpleMatchState.class)})
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class MatchState implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private long eventId;
    private String incidentId;
    protected long clockTimeOfLastPriceCalc;
    protected long clockTimeOfLastElapsedTimeFromIncident;

    private DatafeedMatchIncidentType dataFeedStatus = DatafeedMatchIncidentType.OK;
    private long clocktimeOfLastDatafeedMatchIncident;
    private ElapsedTimeMatchIncidentType clockStatus;
    private int noSuccessfulInPlayParamFindsExecuted;
    private boolean datafeedStateMismatch;
    private boolean clearingAlertHasBeenActivated;
    private boolean dataFeedStateMismatchCleared;

    @JsonIgnore
    protected TeamId matchAbandonedWonTeam;
    private DataToGenerateStaticMarkets dataToGenerateStaticMarkets;

    public MatchState() {
        dataToGenerateStaticMarkets = new DataToGenerateStaticMarkets();
        dataFeedStateMismatchCleared = true;
        datafeedStateMismatch = false;
        clearingAlertHasBeenActivated = false;
        matchAbandonedWonTeam = TeamId.UNKNOWN;
    }

    public ElapsedTimeMatchIncidentType getClockStatus() {
        return clockStatus;
    }

    /**
     * for sports where there is match clock returns fals if the clock has been stopped for any reason e.g. at period
     * end, injury, between plays
     * 
     * @return
     */
    public boolean isClockRunning() {
        return clockStatus != null && clockStatus != ElapsedTimeMatchIncidentType.SET_PERIOD_END
                        && clockStatus != ElapsedTimeMatchIncidentType.SET_STOP_MATCH_CLOCK && !isMatchCompleted();
    }

    /**
     * sets the match clock status
     * 
     * @param clockStatus
     */
    public void setClockStatus(ElapsedTimeMatchIncidentType clockStatus) {
        this.clockStatus = clockStatus;
    }

    /**
     * For mismatches, sets a flag that a clearing alert has been acyivated and we are looking to pass a cleared alert
     * 
     * @return
     */

    @JsonIgnore
    public boolean isClearingAlertHasBeenActivated() {
        return clearingAlertHasBeenActivated;
    }

    /**
     * Is set to true when we start the data feed mismatch Is only set to false when we clear the mismatch Used for
     * completed match updates.
     * 
     * @return dataFeedStateMismatchCleared
     */
    @JsonIgnore
    public boolean isDataFeedStateMismatchCleared() {
        return dataFeedStateMismatchCleared;
    }

    @JsonIgnore
    public TeamId getMatchAbandonedWonTeam() {
        return matchAbandonedWonTeam;
    }

    @JsonIgnore
    public void setMatchAbandonedWonTeam(TeamId matchAbandonedByTeam) {
        this.matchAbandonedWonTeam = matchAbandonedByTeam;
    }

    @JsonIgnore
    public boolean isVarReferralInProgress() {
        return false;
    }

    /**
     * sets the clearing alert
     * 
     * @param clearingAlertHasBeenActivated
     */

    public void setClearingAlertHasBeenActivated(boolean clearingAlertHasBeenActivated) {
        this.clearingAlertHasBeenActivated = clearingAlertHasBeenActivated;
    }

    /**
     * flag that informs the mismatch is cleared
     * 
     * @param dataFeedStateMismatchCleared
     */

    public void setDataFeedStateMismatchCleared(boolean dataFeedStateMismatchCleared) {
        this.dataFeedStateMismatchCleared = dataFeedStateMismatchCleared;
    }


    /**
     * Gets a token which may be used to marry up requests and responses
     * 
     * @return
     */
    public String getIncidentId() {
        return incidentId;
    }

    /**
     * Sets a token which may be used to marry up requests and responses
     *
     * @param requestId
     */
    public void setIncidentId(String incidentId) {
        this.incidentId = incidentId;
    }

    /**
     * Gets the matchClock value at time of last calc
     * 
     * @return
     */
    public long getClockTimeOfLastPriceCalc() {
        return clockTimeOfLastPriceCalc;
    }

    /**
     * Sets the matchClock value at time of last calc
     * 
     * @param clockTimeOfLastPriceCalc
     */
    public void setClockTimeOfLastPriceCalc(long clockTimeOfLastPriceCalc) {
        this.clockTimeOfLastPriceCalc = clockTimeOfLastPriceCalc;
    }

    /*
     * these methods don't seem to be used anywhere
     */
    // /**
    // *
    // * @return
    // */
    public long getClockTimeOfLastElapsedTimeFromIncident() {
        return clockTimeOfLastElapsedTimeFromIncident;
    }

    //
    public void setClockTimeOfLastElapsedTimeFromIncident(long clockTimeOfLastElapsedTimeFromIncident) {
        this.clockTimeOfLastElapsedTimeFromIncident = clockTimeOfLastElapsedTimeFromIncident;
    }


    /**
     * get the current status of the matchIncident feed
     * 
     * @return
     */
    public DatafeedMatchIncidentType getDataFeedStatus() {
        return dataFeedStatus;
    }

    /**
     * set the status of the matchIncident feed
     * 
     * @param dataFeedStatus
     */
    public void setDataFeedStatus(DatafeedMatchIncidentType dataFeedStatus) {
        this.dataFeedStatus = dataFeedStatus;
    }

    /**
     * get the match clock time the last datafeed incident occurred
     * 
     * @return
     */
    public long getClocktimeOfLastDatafeedMatchIncident() {
        return clocktimeOfLastDatafeedMatchIncident;
    }

    /**
     * set the match clock time the last datafeed incident occurred
     * 
     * @param clocktimeOfLastDatafeedMatchIncident
     */
    public void setClocktimeOfLastDatafeedMatchIncident(long clocktimeOfLastDatafeedMatchIncident) {
        this.clocktimeOfLastDatafeedMatchIncident = clocktimeOfLastDatafeedMatchIncident;
    }

    /**
     * get the no of param finds that have been executed for this event since it went in play
     * 
     * @return
     */
    public int getNoSuccessfulInPlayParamFindsExecuted() {
        return noSuccessfulInPlayParamFindsExecuted;
    }

    /**
     * set the no of param finds that have been executed for this event since it went in play
     * 
     * @param noSuccessfulInPlayParamFindsExecuted
     */
    public void setNoSuccessfulInPlayParamFindsExecuted(int noSuccessfulInPlayParamFindsExecuted) {
        this.noSuccessfulInPlayParamFindsExecuted = noSuccessfulInPlayParamFindsExecuted;
    }

    public DataToGenerateStaticMarkets getDataToGenerateStaticMarkets() {
        return dataToGenerateStaticMarkets;
    }

    public void setDataToGenerateStaticMarkets(DataToGenerateStaticMarkets dataToGenerateStaticMarkets) {
        this.dataToGenerateStaticMarkets = dataToGenerateStaticMarkets;
    }

    /**
     * For sports where matchIncidents may contain the current score as specified by the datafeed, then this flag will
     * be set to true if the datafeed score is not the same as the score maintained by MatchState. Implies a
     * matchIncident may have been missed or misprocessed or some other kind of error that requires trader intervention
     * to resolve.
     * 
     * @return
     */
    public boolean isDatafeedStateMismatch() {
        return datafeedStateMismatch;
    }

    /**
     * For sports where matchIncidents may contain the current score as specified by the datafeed, then this flag should
     * be set to true if the datafeed score is not the same as the score maintained by MatchState. Implies a
     * matchIncident may have been missed or misprocessed or some other kind of error that requires trader intervention
     * to resolve.
     * 
     * @param datafeedStateMismatch
     */
    public void setDatafeedStateMismatch(boolean datafeedStateMismatch) {
        this.datafeedStateMismatch = datafeedStateMismatch;
    }

    /**
     * increment the no of param finds that have been executed for this event since it went in play
     */
    public void incrementNoSuccessfulInPlayParamFindsExecuted() {
        noSuccessfulInPlayParamFindsExecuted++;
    }

    /**
     * updates the matchState following the occurrence of the specified matchIncident. The base method handles those
     * matchIncidents which are not sport specific. Override method should handle all sport-specific incidents
     *
     * @param matchIncident - the sport-specific event that has occurred
     * @return - the effect of that event on the matchState - e.g. match Won, Set won etc.
     */
    public MatchIncidentResult updateStateForIncident(MatchIncident matchIncident,
                    boolean autosyncMatchStateToFeedOnMismatch) {
        if (matchIncident.getClass() == DatafeedMatchIncident.class) {
            DatafeedMatchIncidentType status = ((DatafeedMatchIncident) matchIncident).getIncidentSubType();
            if (status != null) {
                dataFeedStatus = ((DatafeedMatchIncident) matchIncident).getIncidentSubType();
                clocktimeOfLastDatafeedMatchIncident = ((DatafeedMatchIncident) matchIncident).getElapsedTimeSecs();
            }
        }
        return null;
    }

    /**
     * makes a deep copy of this object, replicating all the state to the new instance of the class and new instances of
     * any referenced classes
     *
     * @return
     */
    public abstract MatchState copy();

    /**
     * Sets all the state for this instance to be the same as that for the specified object. this method implementation
     * should ensure that "setEqualTo" is also executed for any subObjects so that any changes subsequently made to this
     * object do not affect the supplied matchState object. This is therefore a "deep" setEqualTo rather than a
     * "shallow" set equalTo
     *
     * @param matchState the object who's state is to be copied to this
     */

    public void setEqualTo(MatchState matchState) {
        this.setIncidentId(matchState.getIncidentId());
        this.setClockTimeOfLastElapsedTimeFromIncident(matchState.getClockTimeOfLastElapsedTimeFromIncident());
        this.setClockTimeOfLastPriceCalc(matchState.getClockTimeOfLastPriceCalc());
        this.setClockStatus(matchState.getClockStatus());
        this.setDataFeedStatus(matchState.getDataFeedStatus());
        this.setDataToGenerateStaticMarkets(matchState.getDataToGenerateStaticMarkets());
    }

    /**
     * Gets the next prompt to put before a user when building test user interfaces
     *
     * @return
     */
    public abstract MatchIncidentPrompt getNextPrompt();

    /**
     * gets the matchIncident which corresponds to the user response to a previous call to the getNextPrompt() method.
     * For use in building test user interfaces
     *
     * @param response what the user typed in
     * @return if input is invalid then returns null, otherwise returns the matchIncident
     */
    public abstract MatchIncident getMatchIncident(String response);

    /**
     * Gets the set of variables which define the current state as a Map <String,String> object which can be used to
     * display the data in table format. LinkedHashMap is used so that the variables are displayed in the same order as
     * they were put into the Map
     *
     * @return
     */
    @JsonIgnore
    public abstract LinkedHashMap<String, String> getAsMap();

    @JsonIgnore
    /**
     * Gets the team sheet map
     * 
     * @return null if not relevant to this sport
     */
    public LinkedHashMap<String, String> getTeamSheetAsMap() {
        return null;
    }

    /**
     * Sets the values of the match format from the contents of the map, assuming there are no string parsing errors.
     * The key(s) must be the same as those generated by getAsMap
     *
     * @param map
     * @return null if success, or error message if one or more fields can't be interpreted
     */
    public abstract String setFromMap(Map<String, String> map);

    /**
     * converts the matchParamsMap to a string
     * 
     * @param inputMatchParamsMap
     * @return
     */
    public static String matchStateMapToString(LinkedHashMap<String, String> matchStateMap) {
        String s = "";
        for (Entry<String, String> e : matchStateMap.entrySet()) {
            s += e.getKey() + "= " + e.getValue().toString() + "\n";
        }
        return s;
    }

    public String toString() {
        return JsonUtil.marshalJson(this);
    }

    /**
     * returns true if the match has finished.
     *
     * @return
     */
    public abstract boolean isMatchCompleted();

    /**
     * returns the match format associated with the state
     *
     * @return
     */
    public abstract MatchFormat getMatchFormat();

    /**
     * gets the current game period - e.g. first half
     * 
     * @return
     */
    @JsonIgnore
    public abstract GamePeriod getGamePeriod();

    /*
     * methods to deal with updating elapsed time and deciding whether to recalc prices
     */


    /**
     * updates the elapsed time (for those sports that need it). This method should be overridden by any sport that
     * depends on elapsed time to increment (or, for some sports, decrement) the elapsed time.
     *
     * @return true if enough time has elapsed to recommend scheduling a price calc
     */
    public boolean updateElapsedTime() {
        return false;
    }

    /**
     * gets the no of seconds since the last price recalc
     */
    protected int getSecsSinceLastPriceRecalc() {
        return (int) (System.currentTimeMillis() - clockTimeOfLastPriceCalc + 500) / 1000;
    }

    /**
     * sets the time of the last price cal - invoked whenever matchEngine.calculate method is executed.
     */
    public void setPriceCalcTime() {
        clockTimeOfLastPriceCalc = System.currentTimeMillis();
    }

    /**
     * sets the time we last got a notification of the actual ElapsedTime via a matchIncident
     */
    protected void setClockTimeOfLastElapsedTimeFromIncident() {
        clockTimeOfLastElapsedTimeFromIncident = System.currentTimeMillis();
    }

    /**
     * returns true if preMatch, false if in play.
     *
     * @return
     */
    public abstract boolean preMatch();

    /**
     * no of secs remaining in current period.
     *
     * @return
     */
    public abstract int secsLeftInCurrentPeriod();

    /**
     * get no of seconds since we last got a notification of the actual ElapsedTime via a matchIncident
     * 
     * @return
     */
    @JsonIgnore
    public int getSecsSinceLastElapsedTimeFromIncident() {
        /*
         * round the time to the nearest second
         */
        return (int) (System.currentTimeMillis() - clockTimeOfLastElapsedTimeFromIncident + 500) / 1000;
    }

    /**
     * May be overriden to convert sequence id's from some external format to one that algoManager understands. e.g. for
     * tennis might convert "G1" to "S1.1"
     * 
     * @param sequenceId
     * @return
     */
    public String convertSequenceIdToAlgoMgrStd(String sequenceId) {
        return null;
    }

    /**
     * Should be overridden if needed for a particular sport. Returns the sequence Id for the current matchState that
     * matches the format of templateSequenceId. For example in tennis suppose current point sequence id is "S1.5.3" and
     * game sequenceId is "S1.5". Then if supplied templateSequenceId is "S2.4.3" "S1.5.3" will be returned. If supplied
     * templateSequenceId is "S2.4" then "S1.5" will be returned.
     * 
     * @param templateSequenceId
     * @param offset the offset to be applied to the current sequenceID. 0 is current sequenceId
     * 
     * @return
     */
    public String getSequenceId(String templateSequenceId, int offset) {
        throw new IllegalArgumentException("Should be overridden by sport-specific method if needed");
    }

    /**
     * may be overridden for a particular sport to provide the subset of state info required by trader commentary,
     * contingent on eventTier
     * 
     * @param eventTier
     * @return
     */
    public MatchState generateSimpleMatchState(long eventTier) {
        return generateSimpleMatchState();
    }

    /**
     * may be overridden for a particular sport to provide the subset of state info required by trder commentary. If not
     * overridden the full object is returned.
     * 
     * @return
     */
    public abstract MatchState generateSimpleMatchState();

    /**
     * may be overriden for sports that support teamsheets, otherwise returns null
     * 
     * @return
     */
    public TeamSheet getTeamSheet() {
        return null;
    }



    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (clockTimeOfLastElapsedTimeFromIncident
                        ^ (clockTimeOfLastElapsedTimeFromIncident >>> 32));
        result = prime * result + (int) (clockTimeOfLastPriceCalc ^ (clockTimeOfLastPriceCalc >>> 32));
        result = prime * result + ((incidentId == null) ? 0 : incidentId.hashCode());
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
        MatchState other = (MatchState) obj;
        if (clockTimeOfLastElapsedTimeFromIncident != other.clockTimeOfLastElapsedTimeFromIncident)
            return false;
        if (clockTimeOfLastPriceCalc != other.clockTimeOfLastPriceCalc)
            return false;
        if (incidentId == null) {
            if (other.incidentId != null)
                return false;
        } else if (!incidentId.equals(other.incidentId))
            return false;
        return true;
    }

}
