package ats.algo.core.baseclasses;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ats.algo.core.common.SupportedSportType;
import ats.algo.core.matchresult.MatchResultMap;
import ats.algo.core.matchresult.MatchResulter;
import ats.algo.sport.afl.AflMatchFormat;
import ats.algo.sport.americanfootball.AmericanfootballMatchFormat;
import ats.algo.sport.badminton.BadmintonMatchFormat;
import ats.algo.sport.bandy.BandyMatchFormat;
import ats.algo.sport.baseball.BaseballMatchFormat;
import ats.algo.sport.basketball.BasketballMatchFormat;
import ats.algo.sport.beachvolleyball.BeachVolleyballMatchFormat;
import ats.algo.sport.bowls.BowlsMatchFormat;
import ats.algo.sport.cricket.CricketMatchFormat;
import ats.algo.sport.darts.DartMatchFormat;
import ats.algo.sport.fantasyexample.FantasyExampleSportMatchFormat;
import ats.algo.sport.fieldhockey.FieldhockeyMatchFormat;
import ats.algo.sport.floorball.FloorballMatchFormat;
import ats.algo.sport.football.FootballMatchFormat;
import ats.algo.sport.futsal.FutsalMatchFormat;
import ats.algo.sport.handball.HandballMatchFormat;
import ats.algo.sport.icehockey.IcehockeyMatchFormat;
import ats.algo.sport.outrights.OutrightsMatchFormat;
import ats.algo.sport.rollerhockey.RollerhockeyMatchFormat;
import ats.algo.sport.rugbyleague.RugbyLeagueMatchFormat;
import ats.algo.sport.rugbyunion.RugbyUnionMatchFormat;
import ats.algo.sport.snooker.SnookerMatchFormat;
import ats.algo.sport.squash.SquashMatchFormat;
import ats.algo.sport.tabletennis.TabletennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.testcricket.TestCricketMatchFormat;
import ats.algo.sport.testsport.TestSportMatchFormat;
import ats.algo.sport.volleyball.VolleyballMatchFormat;
import ats.core.util.json.JsonUtil;

/**
 * Holds static information about the match - e.g. for tennis, no sets, whether tie break in final set, surface played
 * on, tournament level
 * 
 * @author Geoff
 *
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "subClass")
@JsonSubTypes({

        @Type(name = "AflMatchFormat", value = AflMatchFormat.class),
        @Type(name = "AmericanfootballMatchFormat", value = AmericanfootballMatchFormat.class),
        @Type(name = "BadmintonMatchFormat", value = BadmintonMatchFormat.class),
        @Type(name = "BandyMatchFormat", value = BandyMatchFormat.class),
        @Type(name = "BaseballMatchFormat", value = BaseballMatchFormat.class),
        @Type(name = "BasketballMatchFormat", value = BasketballMatchFormat.class),
        @Type(name = "BeachVolleyballMatchFormat", value = BeachVolleyballMatchFormat.class),
        @Type(name = "BowlsMatchFormat", value = BowlsMatchFormat.class),
        @Type(name = "CricketMatchFormat", value = CricketMatchFormat.class),
        @Type(name = "DartMatchFormat", value = DartMatchFormat.class),
        @Type(name = "FieldhockeyMatchFormat", value = FieldhockeyMatchFormat.class),
        @Type(name = "FloorballMatchFormat", value = FloorballMatchFormat.class),
        @Type(name = "FootballMatchFormat", value = FootballMatchFormat.class),
        @Type(name = "FutsalMatchFormat", value = FutsalMatchFormat.class),
        @Type(name = "HandballMatchFormat", value = HandballMatchFormat.class),
        @Type(name = "IcehockeyMatchFormat", value = IcehockeyMatchFormat.class),
        @Type(name = "RollerhockeyMatchFormat", value = RollerhockeyMatchFormat.class),
        @Type(name = "RugbyUnionMatchFormat", value = RugbyUnionMatchFormat.class),
        @Type(name = "RugbyLeagueMatchFormat", value = RugbyLeagueMatchFormat.class),
        @Type(name = "SnookerMatchFormat", value = SnookerMatchFormat.class),
        @Type(name = "SquashMatchFormat", value = SquashMatchFormat.class),
        @Type(name = "TabletennisMatchFormat", value = TabletennisMatchFormat.class),
        @Type(name = "TennisMatchFormat", value = TennisMatchFormat.class),
        @Type(name = "TestCricketMatchFormat", value = TestCricketMatchFormat.class),
        @Type(name = "VolleyballMatchFormat", value = VolleyballMatchFormat.class),
        @Type(name = "TestSportMatchFormat", value = TestSportMatchFormat.class),
        @Type(name = "OutrightsMatchFormat", value = OutrightsMatchFormat.class),
        @Type(name = "FantasyExampleSportMatchFormat", value = FantasyExampleSportMatchFormat.class)})
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class MatchFormat implements Serializable {

    private static final long serialVersionUID = 1L;

    private SupportedSportType sportType;
    protected boolean matchFormatOk = true;

    private final static String marginChartNameKey = "marginChart";
    private boolean useMarginChart = true;

    private int marginChart;

    public int getMarginChart() {
        return marginChart;
    }

    public void setMarginChart(int marginChart) {
        this.marginChart = marginChart;
    }

    public boolean isUseMarginChart() {
        return useMarginChart;
    }

    public void setUseMarginChart(boolean useMarginChart) {
        this.useMarginChart = useMarginChart;
    }



    public static String getMarginchartnamekey() {
        return marginChartNameKey;
    }

    /**
     * This method check the sport choossen match format is a valid one or not, need to be implemented in individual
     * sports
     * 
     */
    @JsonIgnore
    public boolean matchFormatCheckingOk(MatchFormat matchFormat) {
        return true;
    }

    @JsonIgnore
    public MatchFormat changeMatchFormat() {
        return null;
    }

    public boolean isMatchFormatOk() {
        return matchFormatOk;
    }


    public void setMatchFormatOk(boolean matchFormatOk) {
        this.matchFormatOk = matchFormatOk;
    }



    /**
     * gets the set of parameters represented as a Map <String,String> object which can then be used to display the data
     * in table format
     * 
     * @return the map containing the parameters
     */

    @JsonIgnore
    public abstract LinkedHashMap<String, String> getAsMap();

    /**
     * sets the sport type associated with this sport
     * 
     * @param sportType
     */
    public MatchFormat(SupportedSportType sportType) {
        this.sportType = sportType;
        if (useMarginChart)
            this.marginChart = 1;
    }

    /**
     * gets the sport type associated with this sport
     * 
     * @return
     */
    public SupportedSportType getSportType() {
        return sportType;
    }

    /**
     * sets the sport type associated with this sport
     * 
     * @param sportType
     */
    public void setSportType(SupportedSportType sportType) {
        this.sportType = sportType;
    }

    /**
     * sets the values of the match format from the contents of the map. The keys must be the same as those generated by
     * getAsMap
     * 
     * @param map
     * @return null if success, or error message if one or more fields can't be interpreted
     */
    public abstract String setFromMap(Map<String, String> map);

    /**
     * should be overridden by each sport to return an instance of the xxxMatchFormatOptions class associated with this
     * MatchFormat class. c.f e.g TennisMatchFormat for an example
     */
    public abstract MatchFormatOptions matchFormatOptions();

    /**
     * Should be overridden by each sport to return an instance of the MatchResulter class for this sport
     * 
     * if not overridden an instance is returned with just one property - notSupportedForThisSport
     * 
     * @return
     */
    public MatchResulter generateMatchResulter() {
        MatchResulter matchResulter = new MatchResulter();
        return matchResulter;
    }

    /**
     * Should be overriden by each sport - generates the default match format per sport
     */
    public abstract MatchFormat generateDefaultMatchFormat();


    /**
     * Should be overridden for each sport to return the proforma that needs to be completed to result a match. the
     * contents of the proforma may vary depending on what is in this matchformat object - e.g. 3 or 5 sets tennis will
     * require 3 or 5 properties to be listed in the proforma
     * 
     * If not overridden then the default proforma generated by the base MatchResulter class
     * ("notSupportedforThisSport") will be returned
     * 
     * @return
     */
    public MatchResultMap generateMatchResultProForma() {
        MatchResulter matchResulter = generateMatchResulter();
        return matchResulter.generateProforma(this);
    }

    /**
     * Used to set properties from the contents of the json object
     * 
     * @param jsonObject
     */
    public abstract void applyFormat(Map<String, Object> jsonObject);

    /**
     * output the state of the object in readable format to the standard output device
     */
    public String toString() {
        return JsonUtil.marshalJson(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((sportType == null) ? 0 : sportType.hashCode());
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
        MatchFormat other = (MatchFormat) obj;
        if (sportType != other.sportType)
            return false;
        return true;
    }


}
