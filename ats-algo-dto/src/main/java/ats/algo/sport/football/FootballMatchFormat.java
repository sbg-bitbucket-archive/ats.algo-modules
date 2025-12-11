package ats.algo.sport.football;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchFormatOptions;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.dto.util.DtoJsonUtil;
import ats.algo.core.matchresult.MatchResulter;

public class FootballMatchFormat extends MatchFormat {
    private static final long serialVersionUID = 1L;
    private int normalTimeMinutes;
    private int extraTimeMinutes;
    private boolean penaltiesPossible;
    private boolean matchInSecondLeg;
    private boolean matchTwoLegs;
    private String lastScore;
    private int matchLevel;
    private boolean awayGoalDouble;
    private boolean shootOutNewFormat;
    private int leagueID;


    public FootballMatchFormat() {
        super(SupportedSportType.SOCCER);
        normalTimeMinutes = 90;
        extraTimeMinutes = 0;
        penaltiesPossible = false;
        matchInSecondLeg = false;
        matchTwoLegs = false;
        lastScore = "0:0";
        matchLevel = 3;
        awayGoalDouble = true;
        shootOutNewFormat = true;
        leagueID = -1;
        matchFormatOk = matchFormatCheckingOk(this);
    }

    @Override
    public void applyFormat(Map<String, Object> format) {
        normalTimeMinutes = DtoJsonUtil.toInt(format.get("normalTimeMinutes"));
        extraTimeMinutes = DtoJsonUtil.toInt(format.get("extraTimeMinutes"));
        penaltiesPossible = DtoJsonUtil.toBoolean(format.get("penaltiesPossible"), false);
        matchInSecondLeg = DtoJsonUtil.toBoolean(format.get("matchinSecondLeg"), false);
        matchTwoLegs = DtoJsonUtil.toBoolean(format.get("matchTwoLegs"), false);
        lastScore = DtoJsonUtil.toString(format.get("lastScore"), "0:0");
        matchLevel = DtoJsonUtil.toInt(format.get("matchLevel") != null ? format.get("matchLevel") : "3");
        awayGoalDouble = DtoJsonUtil.toBoolean(format.get("awayGoalDouble"), true);
        shootOutNewFormat = DtoJsonUtil.toBoolean(format.get("shootOutNewFormat"), true);
        leagueID = DtoJsonUtil.toInt(format.get("leagueID"));
        matchFormatOk = matchFormatCheckingOk(this);
    }

    public FootballMatchFormat(Map<String, Object> format) {
        super(SupportedSportType.SOCCER);
        applyFormat(format);
    }

    public boolean isShootOutNewFormat() {
        return shootOutNewFormat;
    }

    public void setShootOutNewFormat(boolean shootOutNewFormat) {
        this.shootOutNewFormat = shootOutNewFormat;
    }

    public int getNormalTimeMinutes() {
        return normalTimeMinutes;
    }

    @JsonIgnore
    public boolean is80MinMatch() {
        return normalTimeMinutes == 80;
    }

    public boolean isMatchInSecondLeg() {
        return matchInSecondLeg;
    }

    public void setMatchinSecondLeg(boolean matchInSecondLeg) {
        this.matchInSecondLeg = matchInSecondLeg;
    }

    public String getLastScore() {
        return lastScore;
    }

    public void setLastScore(String lastScore) {
        this.lastScore = lastScore;
    }

    public void setNormalTimeMinutes(int normalTimeMinutes) {
        this.normalTimeMinutes = normalTimeMinutes;
    }

    public int getMatchLevel() {
        return matchLevel;
    }

    public void setMatchLevel(int matchLevel) {
        this.matchLevel = matchLevel;
    }

    public int getExtraTimeMinutes() {
        return extraTimeMinutes;
    }

    public void setExtraTimeMinutes(int extraTimeMinutes) {
        this.extraTimeMinutes = extraTimeMinutes;
    }

    public boolean isPenaltiesPossible() {
        return penaltiesPossible;
    }

    public boolean isMatchTwoLegs() {
        return matchTwoLegs;
    }

    public void setMatchTwoLegs(boolean matchTwoLegs) {
        this.matchTwoLegs = matchTwoLegs;
    }

    public void setPenaltiesPossible(boolean penaltiesPossible) {
        this.penaltiesPossible = penaltiesPossible;
    }

    public boolean isAwayGoalDouble() {
        return awayGoalDouble;
    }

    public void setAwayGoalDouble(boolean awayGoalDouble) {
        this.awayGoalDouble = awayGoalDouble;
    }

    public int getLeagueID() {
        return leagueID;
    }

    public void setLeagueID(int leagueID) {
        this.leagueID = leagueID;
    }

    @Override
    public MatchFormatOptions matchFormatOptions() {
        return new FootballMatchFormatOptions();
    }

    private static final String normalTimeMinutesKey = "normalTimeMinutes";
    private static final String extraTimeMinutesKey = "extraTimeMinutes";
    private static final String penaltiesPossibleKey = "penaltiesPossible";
    private static final String matchInSecondLegKey = "matchInSecondLeg";
    private static final String matchTwoLegsKey = "matchTwoLegs";
    private static final String lastScoreKey = "lastScore";// "First Leg final
                                                           // score (0:0)";
    private static final String matchLevelKey = "matchLevel";// "Match booking
                                                             // Level 1-5 (1
                                                             // is the
                                                             // highest)";

    private static final String matchFormatOkKey = "matchFormatOk";
    private static final String awayGoalDoubleKey = "awayGoalDouble";
    private static final String shootOutNewFormatKey = "shootOutNewFormat";
    private static final String leagueIDKey = "LeagueID";

    @Override
    public LinkedHashMap<String, String> getAsMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put(normalTimeMinutesKey, String.format("%d", normalTimeMinutes));
        map.put(extraTimeMinutesKey, String.format("%d", extraTimeMinutes));
        map.put(leagueIDKey, String.format("%d", leagueID));
        String s;
        if (penaltiesPossible)
            s = "True";
        else
            s = "False";
        map.put(penaltiesPossibleKey, s);
        if (isMatchTwoLegs())
            s = "True";
        else
            s = "False";
        map.put(matchTwoLegsKey, s);
        if (isShootOutNewFormat())
            s = "True";
        else
            s = "False";
        map.put(shootOutNewFormatKey, s);
        if (isMatchInSecondLeg())
            s = "True";
        else
            s = "False";
        map.put(matchInSecondLegKey, s);
        map.put(lastScoreKey, lastScore);
        map.put(matchLevelKey, String.format("%d", matchLevel));
        if (isAwayGoalDouble())
            s = "True";
        else
            s = "False";
        map.put(awayGoalDoubleKey, s);
        matchFormatCheckingOk(this);
        // if (isMatchFormatOk())
        // s = "True";
        // else
        // s = "False";
        // map.put(matchFormatOkKey, s);
        if (isUseMarginChart())
            map.put(getMarginchartnamekey(), String.valueOf(getMarginChart()));
        return map;
    }

    @Override
    public boolean matchFormatCheckingOk(MatchFormat matchFormat) {

        boolean ETP = (((FootballMatchFormat) matchFormat).getExtraTimeMinutes() > 0); // Extra Time Possible
        boolean PP = (((FootballMatchFormat) matchFormat).isPenaltiesPossible()); // Penalties possible
        boolean MTL = (((FootballMatchFormat) matchFormat).isMatchTwoLegs()); // Two Legs
        boolean MISL = (((FootballMatchFormat) matchFormat).isMatchInSecondLeg()); // 2nd Leg

        if (ETP && PP && MTL && !MISL) {
            return false;
        }
        if (ETP && PP && !MTL && MISL) {
            return false;
        }
        if (ETP && !PP && MTL && MISL) {
            return false;
        }
        if (ETP && !PP && MTL && !MISL) {
            return false;
        }
        if (ETP && !PP && !MTL && MISL) {
            return false;
        }
        if (!ETP && PP && MTL && !MISL) {
            return false;
        }
        if (!ETP && PP && !MTL && MISL) {
            return false;
        }
        if (!ETP && !PP && MTL && MISL) {
            return false;
        }
        if (!ETP && !PP && !MTL && MISL) {
            return false;
        }

        return true;
    }

    // FIXME: CHECK MATCH FORMAT HERE
    @Override
    public String setFromMap(Map<String, String> map) {
        String newValue = null;
        String errorMessage = null;
        int marginChart;
        try {
            /*
             * margin chart
             */

            newValue = map.get(getMarginchartnamekey());
            errorMessage = String.format("Invalid value: %s.  Allowed integer values 1 to 10 ", newValue);
            marginChart = Integer.parseInt(newValue);
            if ((marginChart > 10) || (marginChart < 1))
                throw new Exception();
            setMarginChart(marginChart);

            newValue = map.get(normalTimeMinutesKey);
            int n = Integer.parseInt(newValue);
            if (n != 80 && (n != 90))
                throw new Exception();
            setNormalTimeMinutes(n);
            newValue = map.get(lastScoreKey);
            setLastScore(newValue);
            newValue = map.get(leagueIDKey);
            n = Integer.parseInt(newValue);
            if (n != -1 && (n < 0))
                throw new Exception();
            setLeagueID(n);
            newValue = map.get(extraTimeMinutesKey);
            n = Integer.parseInt(newValue);
            if (n != 0 && (n != 30))
                throw new Exception();
            setExtraTimeMinutes(n);
            newValue = map.get(matchLevelKey);
            n = Integer.parseInt(newValue);
            if (n < 0 && (n > 5))
                throw new Exception();
            setMatchLevel(n);
            newValue = map.get(matchTwoLegsKey);
            switch (newValue.toUpperCase()) {
                case "TRUE":
                    matchTwoLegs = true;
                    break;
                case "FALSE":
                    matchTwoLegs = false;
                    break;
                default:
                    throw new Exception();
            }

            newValue = map.get(shootOutNewFormatKey);
            switch (newValue.toUpperCase()) {
                case "TRUE":
                    shootOutNewFormat = true;
                    break;
                case "FALSE":
                    shootOutNewFormat = false;
                    break;
                default:
                    throw new Exception();
            }

            newValue = map.get(awayGoalDoubleKey);
            switch (newValue.toUpperCase()) {
                case "TRUE":
                    awayGoalDouble = true;
                    break;
                case "FALSE":
                    awayGoalDouble = false;
                    break;
                default:
                    throw new Exception();
            }

            newValue = map.get(matchInSecondLegKey);
            switch (newValue.toUpperCase()) {
                case "TRUE":
                    matchInSecondLeg = true;
                    break;
                case "FALSE":
                    matchInSecondLeg = false;
                    break;
                default:
                    throw new Exception();
            }
            newValue = map.get(penaltiesPossibleKey);
            switch (newValue.toUpperCase()) {
                case "TRUE":
                    penaltiesPossible = true;
                    break;
                case "FALSE":
                    penaltiesPossible = false;
                    break;
                default:
                    throw new Exception();
            }
            matchFormatOk = this.matchFormatCheckingOk(this);
            map.put(matchFormatOkKey, Boolean.toString(matchFormatOk));

        } catch (Exception e) {
            errorMessage = String.format("Invalid input: %s.", newValue);
            return errorMessage;
        }
        return null;
    }

    @Override
    public MatchResulter generateMatchResulter() {
        MatchResulter matchResulter = new FootballMatchResulter();
        return matchResulter;
    }

    @Override
    public MatchFormat generateDefaultMatchFormat() {
        return new FootballMatchFormat();
    }

}
