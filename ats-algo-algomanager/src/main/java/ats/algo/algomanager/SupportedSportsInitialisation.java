package ats.algo.algomanager;

import ats.algo.core.common.SupportedSportType;
import ats.algo.sport.afl.AflMatchEngine;
import ats.algo.sport.americanfootball.AmericanfootballMatchEngine;
import ats.algo.sport.americanfootball.tradingrules.AmericanFootballTradingRules;
import ats.algo.sport.badminton.BadmintonMatchEngine;
import ats.algo.sport.badminton.tradingrules.BadmintonTradingRules;
import ats.algo.sport.baseball.BaseballMatchEngine;
import ats.algo.sport.baseball.tradingrules.BaseballTradingRules;
import ats.algo.sport.basketball.BasketballMatchEngine;
import ats.algo.sport.basketball.tradingrules.BasketballTradingRules;
import ats.algo.sport.beachvolleyball.BeachVolleyballMatchEngine;
import ats.algo.sport.beachvolleyball.tradingrules.BeachVolleyballTradingRules;
import ats.algo.sport.bowls.BowlsMatchEngine;
import ats.algo.sport.bowls.tradingrules.BowlsTradingRules;
import ats.algo.sport.cricket.CricketMatchEngine;
import ats.algo.sport.cricket.tradingrules.CricketTradingRules;
import ats.algo.sport.darts.DartMatchEngine;
import ats.algo.sport.darts.tradingrules.DartTradingRules;
import ats.algo.sport.fieldhockey.FieldhockeyMatchEngine;
import ats.algo.sport.floorball.FloorballMatchEngine;
import ats.algo.sport.football.FootballMatchEngine;
import ats.algo.sport.football.tradingrules.FootballTradingRules;
import ats.algo.sport.futsal.FutsalMatchEngine;
import ats.algo.sport.handball.HandballMatchEngine;
import ats.algo.sport.icehockey.IcehockeyMatchEngine;
import ats.algo.sport.icehockey.tradingrules.IceHockeyTradingRules;
import ats.algo.sport.outrights.OutrightsMatchEngine;
import ats.algo.sport.rugbyleague.RugbyLeagueMatchEngine;
import ats.algo.sport.rugbyunion.RugbyUnionMatchEngine;
import ats.algo.sport.snooker.SnookerMatchEngine;
import ats.algo.sport.snooker.tradingrules.SnookerTradingRules;
import ats.algo.sport.squash.SquashMatchEngine;
import ats.algo.sport.squash.tradingrules.SquashTradingRules;
import ats.algo.sport.tabletennis.TabletennisMatchEngine;
import ats.algo.sport.tennis.TennisMatchEngine;
import ats.algo.sport.tennis.tradingrules.TennisTradingRules;
import ats.algo.sport.testcricket.TestCricketMatchEngine;
import ats.algo.sport.testsport.TestSportMatchEngine;
import ats.algo.sport.ufc.UfcMatchEngine;
import ats.algo.sport.volleyball.VolleyballMatchEngine;
import ats.algo.sport.volleyball.tradingrules.VolleyballTradingRules;

/**
 * the sole purpose of this class is to initialise the list of SupportedSports. It should only be referred to by
 * projects which have access to the projects associated with every sport or build error will result
 * 
 * @author Geoff
 *
 */
public class SupportedSportsInitialisation {

    /**
     * prevent this class being instantiated
     */
    private SupportedSportsInitialisation() {

    }


    /**
     * initialises the list of SupportedSports. It should only be referred to by projects which have access to the
     * projects associated with every sport or build error will result
     */
    public static void init() {
        SupportedSports.addSport(SupportedSportType.TENNIS, "Tennis", TennisMatchEngine.class,
                        TennisTradingRules.class);
        SupportedSports.addSport(SupportedSportType.DARTS, "Darts", DartMatchEngine.class, DartTradingRules.class);
        SupportedSports.addSport(SupportedSportType.ICE_HOCKEY, "Ice hockey", IcehockeyMatchEngine.class,
                        IceHockeyTradingRules.class);
        SupportedSports.addSport(SupportedSportType.SOCCER, "Football", FootballMatchEngine.class,
                        FootballTradingRules.class);
        SupportedSports.addSport(SupportedSportType.HANDBALL, "Handball", HandballMatchEngine.class, null);
        SupportedSports.addSport(SupportedSportType.FLOORBALL, "Floorball", FloorballMatchEngine.class, null);
        SupportedSports.addSport(SupportedSportType.VOLLEYBALL, "Volleyball", VolleyballMatchEngine.class,
                        VolleyballTradingRules.class);
        SupportedSports.addSport(SupportedSportType.SNOOKER, "Snooker", SnookerMatchEngine.class,
                        SnookerTradingRules.class);
        SupportedSports.addSport(SupportedSportType.FIELD_HOCKEY, "Field hockey", FieldhockeyMatchEngine.class, null);
        SupportedSports.addSport(SupportedSportType.TABLE_TENNIS, "Tabletennis", TabletennisMatchEngine.class, null);
        SupportedSports.addSport(SupportedSportType.BADMINTON, "Badminton", BadmintonMatchEngine.class,
                        BadmintonTradingRules.class);
        SupportedSports.addSport(SupportedSportType.BASKETBALL, "Basketball", BasketballMatchEngine.class,
                        BasketballTradingRules.class);
        SupportedSports.addSport(SupportedSportType.BEACH_VOLLEYBALL, "Beach Volleyball",
                        BeachVolleyballMatchEngine.class, BeachVolleyballTradingRules.class);
        SupportedSports.addSport(SupportedSportType.SQUASH, "Squash", SquashMatchEngine.class,
                        SquashTradingRules.class);
        SupportedSports.addSport(SupportedSportType.BOWLS, "Bowls", BowlsMatchEngine.class, BowlsTradingRules.class);
        SupportedSports.addSport(SupportedSportType.CRICKET, "Cricket", CricketMatchEngine.class,
                        CricketTradingRules.class);
        SupportedSports.addSport(SupportedSportType.RUGBY_UNION, "RugbyUnion", RugbyUnionMatchEngine.class, null);
        SupportedSports.addSport(SupportedSportType.RUGBY_LEAGUE, "RugbyLeague", RugbyLeagueMatchEngine.class, null);
        SupportedSports.addSport(SupportedSportType.AMERICAN_FOOTBALL, "Americanfootball",
                        AmericanfootballMatchEngine.class, AmericanFootballTradingRules.class);
        SupportedSports.addSport(SupportedSportType.TESTCRICKET, "Test Cricket", TestCricketMatchEngine.class, null);

        SupportedSports.addSport(SupportedSportType.BASEBALL, "Baseball", BaseballMatchEngine.class,
                        BaseballTradingRules.class);
        SupportedSports.addSport(SupportedSportType.FUTSAL, "Futsal", FutsalMatchEngine.class, null);
        SupportedSports.addSport(SupportedSportType.UFC, "Ufc", UfcMatchEngine.class, null);
        SupportedSports.addSport(SupportedSportType.AUSSIE_RULES, "AFL", AflMatchEngine.class, null);
        SupportedSports.addSport(SupportedSportType.TEST_SPORT, "Test model for use with unit tests",
                        TestSportMatchEngine.class, null);
        SupportedSports.addSport(SupportedSportType.FANTASY_EXAMPLE_SPORT, "Example of a simple fantasy sport",
                        TestSportMatchEngine.class, null);
        SupportedSports.addSport(SupportedSportType.OUTRIGHTS, "Outrights", OutrightsMatchEngine.class, null);
    }

}
