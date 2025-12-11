package ats.algo.core;

import java.util.Map;

import com.google.common.collect.Maps;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.SupportedSportType;
import ats.core.util.ExceptionUtil;

/**
 * Expected to be for Algo Framework use only. Defines the list of sports supported by the Framework.
 * 
 * @author
 *
 */
public class SportMetaData {

    private static Map<String, SportMetaData> sportsMetaMap;

    static {
        sportsMetaMap = Maps.newHashMap();
        register(SupportedSportType.BADMINTON, "badminton", "Badminton");
        register(SupportedSportType.BASEBALL, "baseball", "Baseball");
        register(SupportedSportType.BASKETBALL, "basketball", "Basketball");
        register(SupportedSportType.BANDY, "bandy", "Bandy");
        register(SupportedSportType.BEACH_VOLLEYBALL, "beachvolleyball", "BeachVolleyball");
        register(SupportedSportType.BOWLS, "bowls", "Bowls");
        register(SupportedSportType.CRICKET, "cricket", "Cricket");
        register(SupportedSportType.DARTS, "darts", "Dart");
        register(SupportedSportType.FIELD_HOCKEY, "fieldhockey", "Fieldhockey");
        register(SupportedSportType.FLOORBALL, "floorball", "Floorball");
        register(SupportedSportType.FUTSAL, "futsal", "Futsal");
        register(SupportedSportType.HANDBALL, "handball", "Handball");
        register(SupportedSportType.ICE_HOCKEY, "icehockey", "Icehockey");
        register(SupportedSportType.ROLLER_HOCKEY, "rollerhockey", "Rollerhockey");
        register(SupportedSportType.RUGBY_UNION, "rugbyunion", "RugbyUnion");
        register(SupportedSportType.RUGBY_LEAGUE, "rugbyleague", "RugbyLeague");
        register(SupportedSportType.AMERICAN_FOOTBALL, "americanfootball", "Americanfootball");
        register(SupportedSportType.SNOOKER, "snooker", "Snooker");
        register(SupportedSportType.SOCCER, "football", "Football");
        register(SupportedSportType.SQUASH, "squash", "Squash");
        register(SupportedSportType.TENNIS, "tennis", "Tennis");
        register(SupportedSportType.TESTCRICKET, "testcricket", "TestCricket");
        register(SupportedSportType.TABLE_TENNIS, "tabletennis", "Tabletennis");
        register(SupportedSportType.VOLLEYBALL, "volleyball", "Volleyball");
        register(SupportedSportType.AUSSIE_RULES, "afl", "Afl");
    }

    private static void register(SupportedSportType sport, String sportSpecificPackage, String classPrefix) {
        // ats.algo.sport.bandy.BandyMatchState;
        // lastPackage - bandy
        // classPrefix - Bandy
        String nameForPackageAndClassBeginingWith = "ats.algo.sport." + sportSpecificPackage + "." + classPrefix;
        String matchParamsFullyQualifiedName = nameForPackageAndClassBeginingWith + "MatchParams";

        Class<? extends MatchFormat> matchFormat = resolveClass(nameForPackageAndClassBeginingWith + "MatchFormat");
        Class<? extends MatchParams> matchParams = resolveClass(matchParamsFullyQualifiedName);
        Class<? extends MatchState> matchState = resolveClass(nameForPackageAndClassBeginingWith + "MatchState");
        Class<? extends MatchIncident> matchIncident =
                        resolveClass(nameForPackageAndClassBeginingWith + "MatchIncident");

        register(sport, matchFormat, matchParams, matchState, matchIncident);
    }

    @SuppressWarnings("unchecked")
    private static <T> T resolveClass(String name) {
        try {
            return (T) Class.forName(name, true, SportMetaData.class.getClassLoader());
        } catch (ClassNotFoundException e) {
            throw ExceptionUtil.unexpectedException(e);
        }
    }

    private static void register(SupportedSportType sport, Class<? extends MatchFormat> matchFormat,
                    Class<? extends MatchParams> matchParams, Class<? extends MatchState> matchState,
                    Class<? extends MatchIncident> matchIncident) {
        // logger.info("Registering Metadata for %s", sport.name());
        sportsMetaMap.put(sport.name(), new SportMetaData(sport, matchFormat, matchParams, matchState, matchIncident));
    }

    private Class<? extends MatchFormat> matchFormat;
    private Class<? extends MatchParams> matchParams;
    private Class<? extends MatchState> matchState;
    private Class<? extends MatchIncident> matchIncident;
    private SupportedSportType sport;

    SportMetaData(SupportedSportType sport, Class<? extends MatchFormat> matchFormat,
                    Class<? extends MatchParams> matchParams, Class<? extends MatchState> matchState,
                    Class<? extends MatchIncident> matchIncident) {
        super();
        this.sport = sport;
        this.matchFormat = matchFormat;
        this.matchParams = matchParams;
        this.matchState = matchState;
        this.matchIncident = matchIncident;
    }


    // public static void customiseMatchParams(SupportedSportType sport, Class<? extends MatchParams> matchParams) {
    // logger.info("Customising %s MatchParams with %s", sport, matchParams.getName());
    // sportsMetaMap.get(sport).matchParams= matchParams;
    // }

    /**
     * Expected to be for Algo Framework use only. Gets the metadata for the specified sport
     * 
     * @param type
     * @return
     */
    public static SportMetaData forSportType(SupportedSportType type) {
        return forSportName(type.name());
    }


    public static SportMetaData forSportName(String name) {
        return sportsMetaMap.get(name);
    }

    public Class<? extends MatchFormat> getMatchFormat() {
        return matchFormat;
    }

    public Class<? extends MatchParams> getMatchParams() {
        return matchParams;
    }

    public Class<? extends MatchState> getMatchState() {
        return matchState;
    }

    public Class<? extends MatchIncident> getMatchIncident() {
        return matchIncident;
    }

    public SupportedSportType getSport() {
        return sport;
    }
}
