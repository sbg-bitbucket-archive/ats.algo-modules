package ats.algo.sport.tennis;

import java.util.LinkedHashMap;
import java.util.Map;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchFormatOptions;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.dto.util.DtoJsonUtil;
import ats.algo.core.matchresult.MatchResulter;

/**
 * Defines the static properties of the match Current double tennis match format includes: 1. Match conditions (Gender,
 * Surface, Tournament level) 2. Sets number (best of 3/5) 3. Tie break in final set (yes/no) 4. No advantage for all
 * games (yes/no)
 * 
 * parameter defined now for future use: 5. isDoubleTennisMatch (yes/no), as we will integrate double and single tennis
 * in the future.
 * 
 * Tie break for final set formal will be choosing from three different formats: advantage, tie break and Championship
 * (only for tennis double)
 * 
 * @author Geoff & Jin
 * 
 */

public class TennisMatchFormat extends MatchFormat {
    private static final long serialVersionUID = 1L;

    /**
     * defines the surface to be played on
     * 
     * @author Geoff
     * 
     */

    public enum Surface {
        GRASS,
        HARD,
        CLAY,
        IHARD,
        CARPET
    }

    public enum Sex {
        MEN,
        WOMEN,
        MIXED
    }

    public enum FinalSetType {
        NORMAL_WITH_TIE_BREAK,
        ADVANTAGE_SET,
        CHAMPIONSHIP_TIE_BREAK,
    }

    /**
     * defines the tournament standard
     * 
     * @author Geoff
     * 
     */
    public enum TournamentLevel {
        ATP_NEXTGEN(2),
        CHALLENGER(1),
        CHALLENGER_QUALIFIER(1),
        ATP(2),
        ATP_QUALIFIER(2),
        MASTERS(3),
        GRANDSLAM(4),
        ITF(1),
        ITF_QUALIFIER(1),
        WTA(2),
        WTA_QUALIFIER(2),
        PREMIER(3),
        IPTL(1);

        private int tLevel;

        private TournamentLevel(int tLevel) {
            this.tLevel = tLevel;
        }

        public int getLevel() {
            return tLevel;
        }
    }

    private boolean doublesMatch;
    private Sex sex;
    private Surface surface;
    private TournamentLevel tournamentLevel;
    private int setsPerMatch;
    private FinalSetType finalSetType;
    private boolean noAdvantageGameFormat;
    private boolean noAdvantageTieBreakFormat;

    private int teamAPlayerRank;
    private int teamBPlayerRank;
    private String teamAPlayerId;
    private String teamBPlayerId;
    private String teamAPlayerName;
    private String teamBPlayerName;

    @Override
    public void applyFormat(Map<String, Object> format) {
        doublesMatch = DtoJsonUtil.toBoolean(format.get("doublesMatch"), false);
        sex = Sex.valueOf(DtoJsonUtil.toString(format.get("sex")));
        surface = Surface.valueOf(DtoJsonUtil.toString(format.get("surface")));
        tournamentLevel = TournamentLevel.valueOf(DtoJsonUtil.toString(format.get("tournamentLevel")));
        setsPerMatch = DtoJsonUtil.toInt(format.get("setsPerMatch"));
        finalSetType = FinalSetType.valueOf(DtoJsonUtil.toString(format.get("finalSetType")));
        noAdvantageGameFormat = DtoJsonUtil.toBoolean(format.get("noAdvantageGameFormat"), false);
        noAdvantageTieBreakFormat = DtoJsonUtil.toBoolean(format.get("noAdvantageTieBreakFormat"), false);
        teamAPlayerRank =
                        DtoJsonUtil.toInt(format.get("teamAPlayerRank") != null ? format.get("teamAPlayerRank") : "0");
        teamBPlayerRank =
                        DtoJsonUtil.toInt(format.get("teamBPlayerRank") != null ? format.get("teamBPlayerRank") : "0");
        teamAPlayerId = DtoJsonUtil.toString(format.get("teamAPlayerId"), "Unknown");
        teamBPlayerId = DtoJsonUtil.toString(format.get("teamBPlayerId"), "Unknown");
        teamAPlayerName = DtoJsonUtil.toString(format.get("teamAPlayerName"), "Unknown");
        teamBPlayerName = DtoJsonUtil.toString(format.get("teamBPlayerName"), "Unknown");
        /*** if it is atp_next_generation, no combination is allowed */

    }

    public TennisMatchFormat(Map<String, Object> format) {
        super(SupportedSportType.TENNIS);
        applyFormat(format);
    }

    /**
     * Sets all parameters to sensible defaults.
     * 
     */
    public TennisMatchFormat() {
        super(SupportedSportType.TENNIS);
        this.doublesMatch = false;
        this.sex = Sex.MEN;
        this.surface = Surface.HARD;
        this.tournamentLevel = TournamentLevel.ATP;
        this.setsPerMatch = 3;
        this.finalSetType = FinalSetType.NORMAL_WITH_TIE_BREAK;
        this.noAdvantageGameFormat = false;
        this.noAdvantageTieBreakFormat = false;
        this.teamAPlayerRank = 0;
        this.teamBPlayerRank = 0;
        this.teamAPlayerId = "Unknown";
        this.teamBPlayerId = "Unknown";
        this.teamAPlayerName = "Unknown";
        this.teamBPlayerName = "Unknown";
    }

    /**
     * sets the main parameters explicitly
     * 
     * @param setsPerMatch
     * @param finalSetType
     * @param noAdvantageGameFormat
     * @param isDoublesMatch
     */
    public TennisMatchFormat(int setsPerMatch, int gamesPerSet, FinalSetType finalSetType,
                    boolean noAdvantageGameFormat, boolean isDoublesMatch) {
        super(SupportedSportType.TENNIS);
        this.doublesMatch = isDoublesMatch;
        this.setsPerMatch = setsPerMatch;
        this.finalSetType = finalSetType;
        /*
         * default properties
         */
        this.sex = Sex.MEN;
        this.surface = Surface.HARD;
        this.tournamentLevel = TournamentLevel.ATP;
        this.noAdvantageGameFormat = false;
        this.noAdvantageTieBreakFormat = false;
        this.teamAPlayerRank = 0;
        this.teamBPlayerRank = 0;
        this.teamAPlayerId = "Unknown";
        this.teamBPlayerId = "Unknown";
        this.teamAPlayerName = "Unknown";
        this.teamBPlayerName = "Unknown";
    }

    /*
     * Default constructor
     */
    public TennisMatchFormat(int setsPerMatch, FinalSetType finalSetType, boolean noAdvantageGameFormat,
                    boolean isDoublesMatch) {
        super(SupportedSportType.TENNIS);
        this.doublesMatch = isDoublesMatch;
        this.setsPerMatch = setsPerMatch;
        this.finalSetType = finalSetType;
        /*
         * default properties
         */
        this.sex = Sex.MEN;
        this.surface = Surface.HARD;
        this.tournamentLevel = TournamentLevel.ATP;
        this.noAdvantageGameFormat = false;
        this.noAdvantageTieBreakFormat = false;
        this.teamAPlayerRank = 0;
        this.teamBPlayerRank = 0;
        this.teamAPlayerId = "Unknown";
        this.teamBPlayerId = "Unknown";
        this.teamAPlayerName = "Unknown";
        this.teamBPlayerName = "Unknown";
    }


    public TennisMatchFormat(Boolean doubleTennis, Sex sex, Surface surface, TournamentLevel tLevel, int setsPerMatch,
                    int gamesPerSet, FinalSetType finalSetType, boolean noAdvantageGameFormat) {
        super(SupportedSportType.TENNIS);
        this.doublesMatch = doubleTennis;
        this.sex = sex;
        this.surface = surface;
        this.tournamentLevel = tLevel;
        this.setsPerMatch = setsPerMatch;
        this.finalSetType = finalSetType;
        this.noAdvantageGameFormat = noAdvantageGameFormat;
        this.noAdvantageTieBreakFormat = false;
        this.teamAPlayerRank = 0;
        this.teamBPlayerRank = 0;
        this.teamAPlayerId = "Unknown";
        this.teamBPlayerId = "Unknown";
        this.teamAPlayerName = "Unknown";
        this.teamBPlayerName = "Unknown";
    }

    public TennisMatchFormat(Boolean doubleTennis, Sex sex, Surface surface, TournamentLevel tLevel, int setsPerMatch,
                    int gamesPerSet, FinalSetType finalSetType, boolean noAdvantageGameFormat,
                    boolean noAdvantageTieBreakFormat) {
        super(SupportedSportType.TENNIS);
        this.doublesMatch = doubleTennis;
        this.sex = sex;
        this.surface = surface;
        this.tournamentLevel = tLevel;
        this.setsPerMatch = setsPerMatch;
        this.finalSetType = finalSetType;
        this.noAdvantageGameFormat = noAdvantageGameFormat;
        this.noAdvantageTieBreakFormat = noAdvantageTieBreakFormat;
        this.teamAPlayerRank = 0;
        this.teamBPlayerRank = 0;
        this.teamAPlayerId = "Unknown";
        this.teamBPlayerId = "Unknown";
        this.teamAPlayerName = "Unknown";
        this.teamBPlayerName = "Unknown";
    }

    public TennisMatchFormat(Boolean doubleTennis, Sex sex, Surface surface, TournamentLevel tLevel, int setsPerMatch,
                    FinalSetType finalSetType, boolean noAdvantageGameFormat) {
        super(SupportedSportType.TENNIS);
        this.doublesMatch = doubleTennis;
        this.sex = sex;
        this.surface = surface;
        this.tournamentLevel = tLevel;
        this.setsPerMatch = setsPerMatch;
        this.finalSetType = finalSetType;
        this.noAdvantageGameFormat = noAdvantageGameFormat;
        this.noAdvantageTieBreakFormat = false;
        this.teamAPlayerRank = 0;
        this.teamBPlayerRank = 0;
        this.teamAPlayerId = "Unknown";
        this.teamBPlayerId = "Unknown";
        this.teamAPlayerName = "Unknown";
        this.teamBPlayerName = "Unknown";
    }

    public TennisMatchFormat(boolean doublesMatch, Sex sex, Surface surface, TournamentLevel tournamentLevel,
                    int setsPerMatch, FinalSetType finalSetType, boolean noAdvantageGameFormat,
                    boolean noAdvantageTieBreakFormat, int teamAPlayerRank, int teamBPlayerRank, String teamAPlayerId,
                    String teamBPlayerId, String teamAPlayerName, String teamBPlayerName) {
        super(SupportedSportType.TENNIS);
        this.doublesMatch = doublesMatch;
        this.sex = sex;
        this.surface = surface;
        this.tournamentLevel = tournamentLevel;
        this.setsPerMatch = setsPerMatch;
        this.finalSetType = finalSetType;
        this.noAdvantageGameFormat = noAdvantageGameFormat;
        this.noAdvantageTieBreakFormat = noAdvantageTieBreakFormat;
        this.teamAPlayerRank = teamAPlayerRank;
        this.teamBPlayerRank = teamBPlayerRank;
        this.teamAPlayerId = teamAPlayerId;
        this.teamBPlayerId = teamBPlayerId;
        this.teamAPlayerName = teamAPlayerName;
        this.teamBPlayerName = teamBPlayerName;
    }

    public boolean isDoublesMatch() {
        return doublesMatch;
    }

    public void setDoublesMatch(boolean doublesMatch) {
        this.doublesMatch = doublesMatch;
    }

    public Sex getSex() {
        return sex;
    }

    public Surface getSurface() {
        return surface;
    }

    public TournamentLevel getTournamentLevel() {
        return tournamentLevel;
    }

    public int getSetsPerMatch() {
        return setsPerMatch;
    }


    public FinalSetType getFinalSetType() {
        return finalSetType;
    }

    public boolean isNoAdvantageGameFormat() {
        return noAdvantageGameFormat;
    }

    public boolean isNoAdvantageTieBreakFormat() {
        return noAdvantageTieBreakFormat;
    }

    public int getTeamAPlayerRank() {
        return teamAPlayerRank;
    }

    public int getTeamBPlayerRank() {
        return teamBPlayerRank;
    }

    public String getTeamAPlayerId() {
        return teamAPlayerId;
    }

    public String getTeamBPlayerId() {
        return teamBPlayerId;
    }

    public String getTeamAPlayerName() {
        return teamAPlayerName;
    }

    public String getTeamBPlayerName() {
        return teamBPlayerName;
    }

    @Override
    public MatchFormatOptions matchFormatOptions() {
        return new TennisMatchFormatOptions();
    }

    private final static String doublesKey = "doublesMatch";
    private final static String sexKey = "sex";
    private final static String surfaceKey = "surface";
    private final static String tLevelKey = "tournamentLevel";// Tournament
                                                              // level:
                                                              // Grandslam,
                                                              // Masters, ATP,
                                                              // Challenger,
                                                              // Premier, WTA,
                                                              // ITF
    private final static String nSetsKey = "setsPerMatch";// 3.5
    private final static String finalSetTypeKey = "finalSetType";// Final set
                                                                 // type
                                                                 // (N)ormal
                                                                 // with tie
                                                                 // break,
                                                                 // (A)dvantage,
                                                                 // (C)hamp
                                                                 // tie break
    private final static String noAdvantageGameFormatKey = "noAdvantageGameFormat";
    private final static String noAdvantageTieBreakFormatKey = "noAdvantageTieBreakFormat";
    private final static String teamAPlayerRankKey = "teamAPlayerRank";
    private final static String teamBPlayerRankKey = "teamBPlayerRank";
    private final static String teamAPlayerIdKey = "teamAPlayerId";
    private final static String teamBPlayerIdKey = "teamBPlayerId";
    private final static String teamAPlayerNameKey = "teamAPlayerName";
    private final static String teamBPlayerNameKey = "teamBPlayerName";



    @Override
    public LinkedHashMap<String, String> getAsMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        String doublesStr;
        if (doublesMatch)
            doublesStr = "true";
        else
            doublesStr = "false";

        map.put(doublesKey, doublesStr);

        map.put(sexKey, sex.toString());
        map.put(surfaceKey, surface.toString());
        map.put(tLevelKey, tournamentLevel.toString());
        map.put(nSetsKey, String.format("%d", setsPerMatch));
        String finalSetTypeStr = "";
        switch (finalSetType) {
            case ADVANTAGE_SET:
                finalSetTypeStr = "Advantage";
                break;
            case CHAMPIONSHIP_TIE_BREAK:
                finalSetTypeStr = "ChampTieBreak";
                break;
            case NORMAL_WITH_TIE_BREAK:
                finalSetTypeStr = "TieBreak";
                break;
        }
        map.put(finalSetTypeKey, finalSetTypeStr);
        String gameFormatStr;
        if (noAdvantageGameFormat)
            gameFormatStr = "True";
        else
            gameFormatStr = "False";
        map.put(noAdvantageGameFormatKey, gameFormatStr);
        String tiebreakFormatStr;
        if (noAdvantageTieBreakFormat)
            tiebreakFormatStr = "True";
        else
            tiebreakFormatStr = "False";
        map.put(noAdvantageTieBreakFormatKey, tiebreakFormatStr);
        map.put(teamAPlayerRankKey, String.format("%d", teamAPlayerRank));
        map.put(teamBPlayerRankKey, String.format("%d", teamBPlayerRank));
        map.put(teamAPlayerIdKey, teamAPlayerId);
        map.put(teamBPlayerIdKey, teamBPlayerId);
        map.put(teamAPlayerNameKey, teamAPlayerName);
        map.put(teamBPlayerNameKey, teamBPlayerName);
        if (isUseMarginChart())
            map.put(getMarginchartnamekey(), String.valueOf(getMarginChart()));
        return map;
    }

    @Override
    public String setFromMap(Map<String, String> map) {
        String errMsg = "Unknown error";
        String newValue;
        boolean doublesMatch;
        Sex sex;
        Surface surface;
        TournamentLevel tournamentLevel;
        int setsPerMatch;
        FinalSetType finalSetType;
        boolean noAdvantageGameFormat;
        boolean noAdvantageTieBreakFormat;
        int teamAPlayerRank;
        int teamBPlayerRank;
        String teamAPlayerId;
        String teamBPlayerId;
        String teamAPlayerName;
        String teamBPlayerName;
        int marginChart;

        try {
            newValue = map.get(teamAPlayerNameKey);
            errMsg = String.format("Invalid value: %s.  Allowed values String", newValue);
            if (newValue == null)
                newValue = "Unknown";
            teamAPlayerName = newValue;
            newValue = map.get(teamBPlayerNameKey);
            errMsg = String.format("Invalid value: %s.  Allowed values String", newValue);
            if (newValue == null)
                newValue = "Unknown";
            teamBPlayerName = newValue;

            newValue = map.get(doublesKey).toUpperCase();
            errMsg = String.format("Invalid value: %s.  Allowed values: 'true', 'false'", newValue);
            // map.put(doublesKey, newValue);
            switch (newValue) {
                case "YES":
                case "TRUE":
                    doublesMatch = true;
                    break;
                case "NO":
                case "FALSE":
                    doublesMatch = false;
                    break;
                default:
                    throw new Exception();
            }
            newValue = map.get(sexKey);
            errMsg = String.format("Invalid value: %s.  Allowed values: 'men', 'women', 'mixed'", newValue);
            switch (newValue.toUpperCase()) {
                case "MEN":
                    sex = Sex.MEN;
                    break;
                case "WOMEN":
                    sex = Sex.WOMEN;
                    break;
                case "MIXED":
                    sex = Sex.MIXED;
                    break;
                default:
                    throw new Exception();
            }
            newValue = map.get(surfaceKey);
            errMsg = String.format("Invalid value: %s.  Allowed values: 'grass', 'hard', 'clay', 'carpet', 'ihard'",
                            newValue);
            switch (newValue.toUpperCase()) {
                case "GRASS":
                    surface = Surface.GRASS;
                    break;
                case "HARD":
                    surface = Surface.HARD;
                    break;
                case "CLAY":
                    surface = Surface.CLAY;
                    break;
                case "CARPET":
                    surface = Surface.CARPET;
                    break;
                case "IHARD":
                    surface = Surface.IHARD;
                    break;
                default:
                    throw new Exception();
            }
            newValue = map.get(tLevelKey);
            errMsg = String.format(
                            "Invalid value: %s.  Allowed values 'ATP', 'challenger', 'grandslam', 'ITF', 'masters', 'premier', WTA'",
                            newValue);
            switch (newValue.toUpperCase()) {
                case "ATP":
                case "ATP(2)":
                    tournamentLevel = TournamentLevel.ATP;
                    break;
                case "ATP_NEXTGEN":
                    tournamentLevel = TournamentLevel.ATP_NEXTGEN;
                    break;
                case "CHALLENGER":
                    tournamentLevel = TournamentLevel.CHALLENGER;
                    break;
                case "ATP_QUALIFIER":
                    tournamentLevel = TournamentLevel.ATP_QUALIFIER;
                    break;
                case "CHALLENGER_QUALIFIER":
                    tournamentLevel = TournamentLevel.CHALLENGER_QUALIFIER;
                    break;
                case "GRANDSLAM":
                    tournamentLevel = TournamentLevel.GRANDSLAM;
                    break;
                case "ITF_QUALIFIER":
                    tournamentLevel = TournamentLevel.ITF_QUALIFIER;
                    break;
                case "ITF":
                    tournamentLevel = TournamentLevel.ITF;
                    break;
                case "MASTERS":
                    tournamentLevel = TournamentLevel.MASTERS;
                    break;
                case "PREMIER":
                    tournamentLevel = TournamentLevel.PREMIER;
                    break;
                case "WTA_QUALIFIER":
                    tournamentLevel = TournamentLevel.WTA_QUALIFIER;
                    break;
                case "WTA":
                    tournamentLevel = TournamentLevel.WTA;
                    break;
                case "IPTL":
                    tournamentLevel = TournamentLevel.IPTL;
                    break;
                default:
                    throw new Exception();
            }

            newValue = map.get(nSetsKey);
            errMsg = String.format("Invalid value: %s.  Allowed values '3' or '5'", newValue);
            setsPerMatch = Integer.parseInt(newValue);
            if ((setsPerMatch != 3) && (setsPerMatch != 5)
                            && (!(tournamentLevel.equals(TournamentLevel.IPTL) && setsPerMatch == 1)))
                throw new Exception();


            newValue = map.get(finalSetTypeKey);
            errMsg = String.format("Invalid value: %s.  Allowed values: 'advantage', 'tiebreak', 'champtiebreak'",
                            newValue);
            switch (newValue.toUpperCase()) {
                case "A":
                case "ADVANTAGE_SET":
                case "ADVANTAGE":
                    finalSetType = FinalSetType.ADVANTAGE_SET;
                    break;
                case "C":
                case "CHAMPIONSHIP_TIE_BREAK":
                case "CHAMPTIEBREAK":
                    finalSetType = FinalSetType.CHAMPIONSHIP_TIE_BREAK;
                    break;
                case "N":
                case "NORMAL_WITH_TIE_BREAK":
                case "TIEBREAK":
                    finalSetType = FinalSetType.NORMAL_WITH_TIE_BREAK;
                    break;
                default:
                    throw new Exception();
            }
            newValue = map.get(noAdvantageGameFormatKey);
            errMsg = String.format("Invalid value: %s.  Allowed values 'true', 'false'", newValue);
            switch (newValue.toUpperCase()) {
                case "NO":
                case "FALSE":
                    noAdvantageGameFormat = false;
                    break;
                case "YES":
                case "TRUE":
                    noAdvantageGameFormat = true;
                    break;
                default:
                    throw new Exception();
            }

            newValue = map.get(noAdvantageTieBreakFormatKey);
            errMsg = String.format("Invalid value: %s.  Allowed values 'true', 'false'", newValue);
            if (newValue == null)
                newValue = "FALSE";
            switch (newValue.toUpperCase()) {
                case "NO":
                case "FALSE":
                    noAdvantageTieBreakFormat = false;
                    break;
                case "YES":
                case "TRUE":
                    noAdvantageTieBreakFormat = true;
                    break;
                default:
                    throw new Exception();
            }
            newValue = map.get(teamAPlayerRankKey);
            errMsg = String.format("Invalid value: %s.  Allowed values Numbers", newValue);
            if (newValue == null)
                newValue = "0";
            teamAPlayerRank = Integer.parseInt(newValue);
            newValue = map.get(teamBPlayerRankKey);
            errMsg = String.format("Invalid value: %s.  Allowed values Numbers", newValue);
            if (newValue == null)
                newValue = "0";
            teamBPlayerRank = Integer.parseInt(newValue);
            newValue = map.get(teamAPlayerIdKey);
            errMsg = String.format("Invalid value: %s.  Allowed values String", newValue);
            if (newValue == null)
                newValue = "Unknown";
            teamAPlayerId = newValue;
            newValue = map.get(teamBPlayerIdKey);
            errMsg = String.format("Invalid value: %s.  Allowed values String", newValue);
            if (newValue == null)
                newValue = "Unknown";
            teamBPlayerId = newValue;

            newValue = map.get(getMarginchartnamekey());
            errMsg = String.format("Invalid value: %s.  Allowed integer values 1 to 10 ", newValue);
            marginChart = Integer.parseInt(newValue);
            if ((marginChart > 10) || (marginChart < 1))
                throw new Exception();

        } catch (Exception e) {
            return errMsg;
        }

        setMarginChart(marginChart);
        this.doublesMatch = doublesMatch;
        this.sex = sex;
        this.surface = surface;
        this.tournamentLevel = tournamentLevel;
        this.setsPerMatch = setsPerMatch;
        this.finalSetType = finalSetType;
        this.noAdvantageGameFormat = noAdvantageGameFormat;
        this.noAdvantageTieBreakFormat = noAdvantageTieBreakFormat;
        this.teamAPlayerId = teamAPlayerId;
        this.teamBPlayerId = teamBPlayerId;
        this.teamAPlayerRank = teamAPlayerRank;
        this.teamBPlayerRank = teamBPlayerRank;
        this.teamAPlayerName = teamAPlayerName;
        this.teamBPlayerName = teamBPlayerName;
        return null;
    }

    public void setSurface(Surface surface) {
        this.surface = surface;
    }

    @Override
    public MatchResulter generateMatchResulter() {
        MatchResulter matchResulter = new TennisMatchResulter();
        return matchResulter;
    }

    @Override
    public MatchFormat generateDefaultMatchFormat() {
        return new TennisMatchFormat();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (doublesMatch ? 1231 : 1237);
        result = prime * result + ((finalSetType == null) ? 0 : finalSetType.hashCode());
        result = prime * result + (noAdvantageGameFormat ? 1231 : 1237);
        result = prime * result + (noAdvantageTieBreakFormat ? 1231 : 1237);
        result = prime * result + setsPerMatch;
        result = prime * result + ((sex == null) ? 0 : sex.hashCode());
        result = prime * result + ((surface == null) ? 0 : surface.hashCode());
        result = prime * result + ((teamAPlayerId == null) ? 0 : teamAPlayerId.hashCode());
        result = prime * result + ((teamAPlayerName == null) ? 0 : teamAPlayerName.hashCode());
        result = prime * result + teamAPlayerRank;
        result = prime * result + ((teamBPlayerId == null) ? 0 : teamBPlayerId.hashCode());
        result = prime * result + ((teamBPlayerName == null) ? 0 : teamBPlayerName.hashCode());
        result = prime * result + teamBPlayerRank;
        result = prime * result + ((tournamentLevel == null) ? 0 : tournamentLevel.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        TennisMatchFormat other = (TennisMatchFormat) obj;
        if (doublesMatch != other.doublesMatch)
            return false;
        if (finalSetType != other.finalSetType)
            return false;
        if (noAdvantageGameFormat != other.noAdvantageGameFormat)
            return false;
        if (noAdvantageTieBreakFormat != other.noAdvantageTieBreakFormat)
            return false;
        if (setsPerMatch != other.setsPerMatch)
            return false;
        if (sex != other.sex)
            return false;
        if (surface != other.surface)
            return false;
        if (teamAPlayerId == null) {
            if (other.teamAPlayerId != null)
                return false;
        } else if (!teamAPlayerId.equals(other.teamAPlayerId))
            return false;
        if (teamAPlayerName == null) {
            if (other.teamAPlayerName != null)
                return false;
        } else if (!teamAPlayerName.equals(other.teamAPlayerName))
            return false;
        if (teamAPlayerRank != other.teamAPlayerRank)
            return false;
        if (teamBPlayerId == null) {
            if (other.teamBPlayerId != null)
                return false;
        } else if (!teamBPlayerId.equals(other.teamBPlayerId))
            return false;
        if (teamBPlayerName == null) {
            if (other.teamBPlayerName != null)
                return false;
        } else if (!teamBPlayerName.equals(other.teamBPlayerName))
            return false;
        if (teamBPlayerRank != other.teamBPlayerRank)
            return false;
        if (tournamentLevel != other.tournamentLevel)
            return false;
        return true;
    }

}
