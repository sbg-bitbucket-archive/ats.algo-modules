package ats.algo.sport.tennisG;

import java.util.LinkedHashMap;
import java.util.Map;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchFormatOptions;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.dto.util.DtoJsonUtil;

/**
 * defines the static properties of the match
 * 
 * @author Geoff
 * 
 */

public class TennisGMatchFormat extends MatchFormat {
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
        WOMEN
    }

    /**
     * defines the tournament standard
     * 
     * @author Geoff
     * 
     */
    public enum TournamentLevel {
        CHALLENGER(1),
        ATP(2),
        MASTERS(3),
        GRANDSLAM(4),
        ITF(1),
        WTA(2),
        PREMIER(3);

        private int tLevel;

        private TournamentLevel(int tLevel) {
            this.tLevel = tLevel;
        }

        public int getLevel() {
            return tLevel;
        }
    }

    private int setsPerMatch;
    private boolean tieBreakInFinalSet;
    private Surface surface;
    private TournamentLevel tournamentLevel;
    private Sex sex;

    @Override
    public void applyFormat(Map<String, Object> format) {
        setsPerMatch = DtoJsonUtil.toInt(format.get("setsPerMatch"));
        tieBreakInFinalSet = DtoJsonUtil.toBoolean(format.get("tieBreakInFinalSet"), true);
        surface = Surface.valueOf(DtoJsonUtil.toString(format.get("surface")));
        sex = Sex.valueOf(DtoJsonUtil.toString(format.get("sex")));
        tournamentLevel = TournamentLevel.valueOf(DtoJsonUtil.toString(format.get("tournamentLevel")));
    }


    public TennisGMatchFormat(Map<String, Object> format) {
        super(SupportedSportType.TEST_TENNISG);
        applyFormat(format);
    }

    /**
     * Sets all parameters to sensible defaults.
     * 
     */
    public TennisGMatchFormat() {
        super(SupportedSportType.TENNIS);
        this.setsPerMatch = 3;
        this.tieBreakInFinalSet = true;
        this.surface = Surface.HARD;
        this.tournamentLevel = TournamentLevel.ATP;
        this.sex = Sex.MEN;
    }

    /**
     * Sets default values for sex,surface, tournament level
     * 
     * @param setsPerMatch
     * @param tieBreakInFinalSet
     */
    public TennisGMatchFormat(int setsPerMatch, boolean tieBreakInFinalSet) {
        super(SupportedSportType.TENNIS);
        this.setsPerMatch = setsPerMatch;
        this.tieBreakInFinalSet = tieBreakInFinalSet;
        this.surface = Surface.HARD;
        this.tournamentLevel = TournamentLevel.ATP;
        this.sex = Sex.MEN;
    }

    /**
     * all parameters explicitly set
     * 
     * @param sex
     * @param surface
     * @param tLevel
     * @param setsPerMatch
     * @param tieBreakInFinalSet
     */
    public TennisGMatchFormat(Sex sex, Surface surface, TournamentLevel tLevel, int setsPerMatch,
                    boolean tieBreakInFinalSet) {
        super(SupportedSportType.TENNIS);
        this.sex = sex;
        this.surface = surface;
        this.tournamentLevel = tLevel;
        this.setsPerMatch = setsPerMatch;
        this.tieBreakInFinalSet = tieBreakInFinalSet;
    }

    public int getSetsPerMatch() {
        return setsPerMatch;
    }

    public void setSetsPerMatch(int setsPerMatch) {
        this.setsPerMatch = setsPerMatch;
    }

    public boolean isTieBreakInFinalSet() {
        return tieBreakInFinalSet;
    }

    public void setTieBreakInFinalSet(boolean tieBreakInFinalSet) {
        this.tieBreakInFinalSet = tieBreakInFinalSet;
    }

    public Surface getSurface() {
        return surface;
    }

    public void setSurface(Surface surface) {
        this.surface = surface;
    }

    public TournamentLevel getTournamentLevel() {
        return tournamentLevel;
    }

    public void setTournamentLevel(TournamentLevel tournamentLevel) {
        this.tournamentLevel = tournamentLevel;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    private final static String sexKey = "Men/Women";
    private final static String surfaceKey = "Surface(Hard, Grass, Clay, Carpet, IHard)";
    private final static String tLevelKey = "Tournament level (Challenger, ATP, Masters, GrandSlam, ITF, WTA, Premier)";
    private final static String nSetsKey = "Sets per match (3 or 5)";
    private final static String tbInFinalSetKey = "Tie break in final set (Yes/No)?";

    @Override
    public LinkedHashMap<String, String> getAsMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

        map.put(sexKey, sex.toString());
        map.put(surfaceKey, surface.toString());
        map.put(tLevelKey, tournamentLevel.toString());
        map.put(nSetsKey, String.format("%d", setsPerMatch));
        String tb;
        if (tieBreakInFinalSet)
            tb = "Yes";
        else
            tb = "No";
        map.put(tbInFinalSetKey, tb);
        return map;
    }

    @Override
    public String setFromMap(Map<String, String> map) {
        String errMsg = "Unknown error";
        String newValue;
        Sex sex;
        Surface surface;
        TournamentLevel tLevel;
        int setsPerMatch;
        boolean tieBreakInFinalSet;
        try {
            newValue = map.get(sexKey);
            errMsg = String.format("Invalid value: %s", newValue);
            switch (newValue.toUpperCase()) {
                case "MEN":
                    sex = Sex.MEN;
                    break;
                case "WOMEN":
                    sex = Sex.WOMEN;
                    break;
                default:
                    throw new Exception();
            }
            newValue = map.get(surfaceKey);
            errMsg = String.format("Invalid value: %s", newValue);
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
            errMsg = String.format("Invalid value: %s", newValue);
            switch (newValue.toUpperCase()) {
                case "ATP":
                    tLevel = TournamentLevel.ATP;
                    break;
                case "CHALLENGER":
                    tLevel = TournamentLevel.CHALLENGER;
                    break;
                case "GRANDSLAM":
                    tLevel = TournamentLevel.GRANDSLAM;
                    break;
                case "ITF":
                    tLevel = TournamentLevel.ITF;
                    break;
                case "MASTERS":
                    tLevel = TournamentLevel.MASTERS;
                    break;
                case "PREMIER":
                    tLevel = TournamentLevel.PREMIER;
                    break;
                case "WTA":
                    tLevel = TournamentLevel.WTA;
                    break;
                default:
                    throw new Exception();
            }

            newValue = map.get(nSetsKey);
            errMsg = String.format("Invalid value: %s", newValue);
            setsPerMatch = Integer.parseInt(newValue);
            if ((setsPerMatch != 3) && (setsPerMatch != 5))
                throw new Exception();
            newValue = map.get(tbInFinalSetKey);
            errMsg = String.format("Invalid value: %s", newValue);
            switch (newValue.toUpperCase()) {
                case "NO":
                    tieBreakInFinalSet = false;
                    break;
                case "YES":
                    tieBreakInFinalSet = true;
                    break;
                default:
                    throw new Exception();
            }
        } catch (Exception e) {
            return errMsg;
        }
        this.setsPerMatch = setsPerMatch;
        this.tieBreakInFinalSet = tieBreakInFinalSet;
        this.surface = surface;
        this.tournamentLevel = tLevel;
        this.sex = sex;
        return null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + setsPerMatch;
        result = prime * result + ((sex == null) ? 0 : sex.hashCode());
        result = prime * result + ((surface == null) ? 0 : surface.hashCode());
        result = prime * result + (tieBreakInFinalSet ? 1231 : 1237);
        result = prime * result + ((tournamentLevel == null) ? 0 : tournamentLevel.hashCode());
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
        TennisGMatchFormat other = (TennisGMatchFormat) obj;
        if (setsPerMatch != other.setsPerMatch)
            return false;
        if (sex != other.sex)
            return false;
        if (surface != other.surface)
            return false;
        if (tieBreakInFinalSet != other.tieBreakInFinalSet)
            return false;
        if (tournamentLevel != other.tournamentLevel)
            return false;
        return true;
    }


    @Override
    public MatchFormatOptions matchFormatOptions() {
        throw new IllegalArgumentException("Not yet implemented for TennisG");
    }


    @Override
    public MatchFormat generateDefaultMatchFormat() {
        return new TennisGMatchFormat();
    }
}
