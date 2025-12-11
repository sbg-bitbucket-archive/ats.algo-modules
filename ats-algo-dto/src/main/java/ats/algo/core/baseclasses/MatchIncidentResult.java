package ats.algo.core.baseclasses;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import ats.algo.sport.afl.AflMatchPeriod;
import ats.algo.sport.americanfootball.AmericanfootballMatchPeriod;
import ats.algo.sport.badminton.BadmintonMatchIncidentResult;
import ats.algo.sport.bandy.BandyMatchPeriod;
import ats.algo.sport.baseball.BaseballMatchIncidentResult;
import ats.algo.sport.basketball.BasketballMatchPeriod;
import ats.algo.sport.beachvolleyball.BeachVolleyballMatchIncidentResult;
import ats.algo.sport.bowls.BowlsMatchIncidentResult;
import ats.algo.sport.cricket.CricketMatchIncidentResult;
import ats.algo.sport.darts.DartMatchIncidentResult;
import ats.algo.sport.fieldhockey.FieldhockeyMatchPeriod;
import ats.algo.sport.floorball.FloorballMatchPeriod;
import ats.algo.sport.football.FootballMatchPeriod;
import ats.algo.sport.futsal.FutsalMatchPeriod;
import ats.algo.sport.handball.HandballMatchPeriod;
import ats.algo.sport.icehockey.IcehockeyMatchPeriod;
import ats.algo.sport.rollerhockey.RollerhockeyMatchPeriod;
import ats.algo.sport.rugbyleague.RugbyLeagueMatchPeriod;
import ats.algo.sport.rugbyunion.RugbyUnionMatchPeriod;
import ats.algo.sport.snooker.SnookerMatchIncidentResult;
import ats.algo.sport.squash.SquashMatchIncidentResult;
import ats.algo.sport.tabletennis.TabletennisMatchIncidentResult;
import ats.algo.sport.tennis.TennisMatchIncidentResult;
import ats.algo.sport.testcricket.TestCricketMatchIncidentResult;
import ats.algo.sport.testsport.TestSportMatchIncidentResult;
import ats.algo.sport.volleyball.VolleyballMatchIncidentResult;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

/**
 * defines the states that can arise when MatchState is updated for a MatchIncident. e.g. MatchIncident playerA wins
 * point might lead to a MatchIncidentResult of playerA winning the set or the match
 * 
 * @author Geoff
 * 
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "subClass")
@JsonSubTypes({@Type(name = "AflMatchPeriod", value = AflMatchPeriod.class),
        @Type(name = "AmericanfootballMatchPeriod", value = AmericanfootballMatchPeriod.class),
        @Type(name = "BadmintonMatchIncidentResult", value = BadmintonMatchIncidentResult.class),
        @Type(name = "BandyMatchPeriod", value = BandyMatchPeriod.class),
        @Type(name = "BaseballMatchIncidentResult", value = BaseballMatchIncidentResult.class),
        @Type(name = "BasketballMatchPeriod", value = BasketballMatchPeriod.class),
        @Type(name = "BeachVolleyballMatchIncidentResult", value = BeachVolleyballMatchIncidentResult.class),
        @Type(name = "BowlsMatchIncidentResult", value = BowlsMatchIncidentResult.class),
        @Type(name = "CricketMatchIncidentResult", value = CricketMatchIncidentResult.class),
        @Type(name = "DartMatchIncidentResult", value = DartMatchIncidentResult.class),
        @Type(name = "FieldhockeyMatchPeriod", value = FieldhockeyMatchPeriod.class),
        @Type(name = "FloorballMatchPeriod", value = FloorballMatchPeriod.class),
        @Type(name = "FootballMatchPeriod", value = FootballMatchPeriod.class),
        @Type(name = "FutsalMatchPeriod", value = FutsalMatchPeriod.class),
        @Type(name = "HandballMatchPeriod", value = HandballMatchPeriod.class),
        @Type(name = "IcehockeyMatchPeriod", value = IcehockeyMatchPeriod.class),
        @Type(name = "RollerhockeyMatchPeriod", value = RollerhockeyMatchPeriod.class),
        @Type(name = "RugbyUnionMatchPeriod", value = RugbyUnionMatchPeriod.class),
        @Type(name = "RugbyLeagueMatchPeriod", value = RugbyLeagueMatchPeriod.class),
        @Type(name = "SnookerMatchIncidentResult", value = SnookerMatchIncidentResult.class),
        @Type(name = "SquashMatchIncidentResult", value = SquashMatchIncidentResult.class),
        @Type(name = "TabletennisMatchIncidentResult", value = TabletennisMatchIncidentResult.class),
        @Type(name = "TennisMatchIncidentResult", value = TennisMatchIncidentResult.class),
        @Type(name = "TestCricketMatchIncidentResult", value = TestCricketMatchIncidentResult.class),
        @Type(name = "VolleyballMatchIncidentResult", value = VolleyballMatchIncidentResult.class),
        @Type(name = "TestSportMatchIncidentResult", value = TestSportMatchIncidentResult.class)})
public interface MatchIncidentResult extends Serializable {
}
