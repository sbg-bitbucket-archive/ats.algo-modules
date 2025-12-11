package ats.algo.sport.football;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.baseclasses.MatchEngineSavedState;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncidentResult;
import ats.algo.core.baseclasses.MatchParam;
import ats.algo.core.baseclasses.MatchParamType;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.common.PlayerInfo;
import ats.algo.core.common.TeamId;
import ats.algo.core.common.TeamSheet;
import ats.algo.genericsupportfunctions.Gaussian;

public class FootballMatchParams extends MatchParams {

    private static final long serialVersionUID = 1L;
    private MatchParam goalTotal;
    private MatchParam goalSupremacy;
    private MatchParam homeLoseBoost;
    private MatchParam awayLoseBoost;
    private MatchParam targetGoalBoost;
    private MatchParam goalsDevn;
    private MatchParam cornerTotal;
    private MatchParam cornerSupremacy;
    private MatchParam cardTotal;
    private MatchParam cardSupremacy;
    private MatchParam redCardProb;
    private Map<String, MatchParam> individualParams;
    private Map<String, MatchParam> penaltyParams;

    /*
     * static data for sorting out which params belong to which eventTiers
     */
    private static final int DEFAULT_EVENT_TIER = 4;
    private static final String[] tier4And5 =
                    {"goalTotal", "goalSupremacy", "homeLoseBoost", "awayLoseBoost", "targetGoalBoost", "goalsDevn"};
    private static final String[] tier3 = {"goalTotal", "goalSupremacy", "homeLoseBoost", "awayLoseBoost",
            "targetGoalBoost", "goalsDevn", "cornerTotal", "cornerSupremacy"};
    private static final String[] tier1And2 =
                    {"goalTotal", "goalSupremacy", "homeLoseBoost", "awayLoseBoost", "targetGoalBoost", "goalsDevn",
                            "cornerTotal", "cornerSupremacy", "cardTotal", "cardSupremacy", "redCardProb"};

    /*
     * add a new tier 6 that includes individuals, for testing purpose. expect to restore to tier 1 at some point
     */
    private static final int nTiers = 6;
    private static String[][] defaultParamsInTier = {tier1And2, tier1And2, tier3, tier4And5, tier4And5, tier1And2};
    private static boolean[] defaultIndividualsInTier = {false, false, false, false, false, true};

    public FootballMatchParams() {
        if (defaultParamsInTier.length != nTiers || defaultIndividualsInTier.length != nTiers)
            throw new IllegalStateException("inconsistent lengths of static tier arrays");
        setDefaultParams();
        initialiseParamMapForDefaultEventTier();
    }

    public FootballMatchParams(MatchFormat matchFormat) {
        this();
        updateParamMapForPenaltiesPossible(((FootballMatchFormat) matchFormat).isPenaltiesPossible());
    }

    /**
     * returns true if we expect individual matchParams for goalscorer etc to be in this tier
     * 
     * @param eventTier
     * @return
     */
    public static boolean individualsInTier(int eventTier) {
        if (eventTier >= 1 && eventTier <= nTiers)
            return defaultIndividualsInTier[eventTier - 1];
        else
            return false;
    }

    /*
     * getters and setters come in two flavours - setting and returning the matchParam or the underlying gaussian
     */

    void setHomeLoseBoost(Gaussian skill) {
        this.homeLoseBoost.setGaussian(skill);
    }

    Gaussian getGoalTotal() {
        return goalTotal.getGaussian();
    }

    void setGoalTotal(Gaussian skill) {
        this.goalTotal.setGaussian(skill);
    }

    Gaussian getTargetGoalBoost() {
        return targetGoalBoost.getGaussian();
    }

    Gaussian getGoalsDevn() {
        return goalsDevn.getGaussian();
    }

    @JsonIgnore
    public void setGoalTotal(double skill, double stdDevn) {
        this.goalTotal.updateGaussian(skill, stdDevn);
    }

    Gaussian getGoalSupremacy() {
        return goalSupremacy.getGaussian();
    }

    void setGoalSupremacy(Gaussian skill) {
        this.goalSupremacy.setGaussian(skill);
    }

    @JsonIgnore
    public void setGoalSupremacy(double skill, double stdDevn) {
        this.goalSupremacy.updateGaussian(skill, stdDevn);
    }

    Gaussian getCornerTotal() {
        return cornerTotal.getGaussian();
    }

    void setCornerTotal(Gaussian skill) {
        this.cornerTotal.setGaussian(skill);
    }

    Gaussian getCornerSupremacy() {
        return cornerSupremacy.getGaussian();
    }

    void setCornerSupremacy(Gaussian skill) {
        this.cornerSupremacy.setGaussian(skill);
    }

    Gaussian getCardTotal() {
        return cardTotal.getGaussian();
    }

    void setCardTotal(Gaussian skill) {
        this.cardTotal.setGaussian(skill);
    }

    Gaussian getCardSupremacy() {
        return cardSupremacy.getGaussian();
    }

    void setCardSupremacy(Gaussian skill) {
        this.cardSupremacy.setGaussian(skill);
    }

    void setAwayLoseBoost(Gaussian skill) {
        this.awayLoseBoost.setGaussian(skill);
    }

    public void setHomeLoseBoost(double skill, double stdDevn) {
        this.homeLoseBoost.updateGaussian(skill, stdDevn);
    }

    public void setAwayLoseBoost(double skill, double stdDevn) {
        this.awayLoseBoost.updateGaussian(skill, stdDevn);
    }

    Gaussian getRedCardProb() {
        return redCardProb.getGaussian();
    }

    void setRedCardProb(Gaussian skill) {
        this.redCardProb.setGaussian(skill);
    }

    Gaussian getHomeLoseBoost() {
        return homeLoseBoost.getGaussian();
    }

    Gaussian getAwayLoseBoost() {
        return awayLoseBoost.getGaussian();
    }

    public Map<String, MatchParam> getIndividualParams() {
        return individualParams;
    }

    public void setIndividualParams(Map<String, MatchParam> individualParams) {
        this.individualParams = individualParams;
    }

    public Map<String, MatchParam> getPenaltyParams() {
        return penaltyParams;
    }

    public void setPenaltyParams(Map<String, MatchParam> penaltyParams) {
        this.penaltyParams = penaltyParams;
    }

    @Override
    public void setDefaultParams(MatchFormat matchFormat) {
        setDefaultParams();
        initialiseParamMapForDefaultEventTier();
    }

    private void setDefaultParams() {
        goalTotal = goalTotalParam();
        goalSupremacy = goalSupremacyParam();
        homeLoseBoost = homeLoseBoostParam();
        awayLoseBoost = awayLoseBoostParam();
        targetGoalBoost = targetGoalBoostParam();
        goalsDevn = goalsDevnParam();
        cornerTotal = cornerTotalParam();
        cornerSupremacy = cornerSupremacyParam();
        cardTotal = cardTotalParam();
        cardSupremacy = cardSupremacyParam();
        redCardProb = redCardProbParam();
        individualParams = generateDefaultIndividualParams();
        penaltyParams = generateDefaultPenaltyParams();
    }

    private void initialiseParamMapForDefaultEventTier() {
        String[] paramNames = defaultParamsInTier[DEFAULT_EVENT_TIER - 1];
        for (String paramName : paramNames) {
            switch (paramName) {
                case "goalTotal":
                    paramMap.put("goalTotal", goalTotal);
                    break;
                case "goalSupremacy":
                    paramMap.put("goalSupremacy", goalSupremacy);
                    break;
                case "homeLoseBoost":
                    paramMap.put("homeLoseBoost", homeLoseBoost);
                    break;
                case "awayLoseBoost":
                    paramMap.put("awayLoseBoost", awayLoseBoost);
                    break;
                case "targetGoalBoost":
                    paramMap.put("targetGoalBoost", targetGoalBoost);
                    break;
                case "goalsDevn":
                    paramMap.put("goalsDevn", goalsDevn);
                    break;
                case "cornerTotal":
                    paramMap.put("cornerTotal", cornerTotal);
                    break;
                case "cornerSupremacy":
                    paramMap.put("cornerSupremacy", cornerSupremacy);
                    break;
                case "cardTotal":
                    paramMap.put("cardTotal", cardTotal);
                    break;
                case "cardSupremacy":
                    paramMap.put("cardSupremacy", cardSupremacy);
                    break;
                case "redCardProb":
                    paramMap.put("redCardProb", redCardProb);
                    break;
            }
        }
    }

    static final double GOAL_TOTAL_DEFAULT_VALUE = 2.7;
    static final double GOAL_SUPREMACY_DEFAULT_VALUE = 0.3;
    static final double CORNER_TOTAL_DEFAULT_VALUE = 11.0;
    static final double CORNER_SUPREMACY_DEFAULT_VALUE = 1.0;
    static final double CARD_TOTAL_DEFAULT_VALUE = 4.0;
    static final double CARD_SUPREMACY_DEFAULT_VALUE = 0.5;

    private static MatchParam goalTotalParam() {
        return new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.GOALS, GOAL_TOTAL_DEFAULT_VALUE, 1.0, 0, 20,
                        false);
    }

    private static MatchParam goalSupremacyParam() {
        return new MatchParam(MatchParamType.BOTHDIFFERENCE, MarketGroup.GOALS, GOAL_SUPREMACY_DEFAULT_VALUE, 1.0, -8,
                        8, false);
    }

    private static MatchParam homeLoseBoostParam() {
        return new MatchParam(MatchParamType.A, MarketGroup.GOALS, 0.12, 0.0, 0, 2, false);
    }

    private static MatchParam awayLoseBoostParam() {
        return new MatchParam(MatchParamType.B, MarketGroup.GOALS, 0.08, 0.0, 0, 2, false);
    }

    private static MatchParam targetGoalBoostParam() {
        return new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.GOALS, 0.4, 0.1, 0.0, 1.0, false);
    }

    private static MatchParam goalsDevnParam() {
        return new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.GOALS, 1.0, 0.25, 0.0, 2.5, false);
    }

    private static MatchParam cornerTotalParam() {
        return new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.CORNERS, CORNER_TOTAL_DEFAULT_VALUE, 1.0, 5, 50,
                        false);
    }

    private static MatchParam cornerSupremacyParam() {
        return new MatchParam(MatchParamType.BOTHDIFFERENCE, MarketGroup.CORNERS, CORNER_SUPREMACY_DEFAULT_VALUE, 1.0,
                        -20, 20, false);
    }

    private static MatchParam cardTotalParam() {
        return new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.BOOKINGS, CARD_TOTAL_DEFAULT_VALUE, 1.0, 0, 20,
                        false);
    }

    private static MatchParam cardSupremacyParam() {
        return new MatchParam(MatchParamType.BOTHDIFFERENCE, MarketGroup.BOOKINGS, CARD_SUPREMACY_DEFAULT_VALUE, 1.0,
                        -10, 10, false);
    }

    private static MatchParam redCardProbParam() {
        return new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.BOOKINGS, 0.08, 0.008, 0, 1, true);
    }

    private static double[] defaultPenaltyProbs = {.866, .817, .793, .725, .80, .643};
    private static String[] defaultPenaltyParamNames =
                    {"1st kick", "2nd kick", "3rd kick", "4th kick", "5th kick", "Sudden death kicks"};

    private static Map<String, MatchParam> generateDefaultPenaltyParams() {
        Map<String, MatchParam> penaltyParams = new LinkedHashMap<String, MatchParam>(12);
        for (int i = 1; i <= 6; i++)
            addPenaltyParam(penaltyParams, TeamId.A, defaultPenaltyParamNames[i - 1], defaultPenaltyProbs[i - 1]);
        for (int i = 1; i <= 6; i++)
            addPenaltyParam(penaltyParams, TeamId.B, defaultPenaltyParamNames[i - 1], defaultPenaltyProbs[i - 1]);
        return penaltyParams;
    }

    private static void addPenaltyParam(Map<String, MatchParam> penaltyParams, TeamId teamId, String name,
                    double prob) {
        String paramName = teamId.toString() + "." + name;
        MatchParamType type;
        if (teamId == TeamId.A)
            type = MatchParamType.A;
        else
            type = MatchParamType.B;
        MatchParam matchParam = new MatchParam(type, MarketGroup.PENALTY, prob, 0, 0.2, 0.9, true);
        penaltyParams.put(paramName, matchParam);
    }

    private static Map<String, MatchParam> generateDefaultIndividualParams() {
        TeamSheet teamSheet = TeamSheet.generateDefaultTeamSheet();
        Map<String, MatchParam> individualParams =
                        new LinkedHashMap<String, MatchParam>(teamSheet.getTeamSheetMap().size());
        for (Entry<String, PlayerInfo> e : teamSheet.getTeamSheetMap().entrySet()) {
            String playerId = e.getKey();
            PlayerInfo playerInfo = e.getValue();
            MatchParamType matchParamType;
            if (playerInfo.getTeamId().equals(TeamId.A))
                matchParamType = MatchParamType.A;
            else
                matchParamType = MatchParamType.B;
            MatchParam mp = new MatchParam(matchParamType, MarketGroup.INDIVIDUAL, 6, .6, 0, 10, false);
            mp.setDescription(playerId);
            individualParams.put(playerId, mp);
        }
        return individualParams;
    }

    /**
     * updates the player params with the names and playerIds from the team sheet
     * 
     * @param teamId
     * @param teamSheet
     */
    public static void updatePlayerMatchParams(TeamSheet teamSheet, GenericMatchParams matchParams) {
        Map<String, MatchParam> paramMap = matchParams.getParamMap();
        /*
         * remove any old params of type INDIVIDUAL
         */
        Iterator<Map.Entry<String, MatchParam>> iter = paramMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, MatchParam> e = iter.next();
            if (e.getValue().getMarketGroup().equals(MarketGroup.INDIVIDUAL))
                iter.remove();
        }
        /*
         * add in the new params
         */
        for (Entry<String, PlayerInfo> e : teamSheet.getTeamSheetMap().entrySet()) {
            String playerId = e.getKey();
            PlayerInfo playerInfo = e.getValue();
            MatchParamType matchParamType;
            if (playerInfo.getTeamId().equals(TeamId.A))
                matchParamType = MatchParamType.A;
            else
                matchParamType = MatchParamType.B;
            MatchParam mp = new MatchParam(matchParamType, MarketGroup.INDIVIDUAL, 6, .6, 0, 10, false);
            mp.setDescription(playerId);
            paramMap.put(playerId, mp);
        }
    }

    /**
     * 
     * @param eventTierL
     * @param matchParams
     */
    public static void updateParamMapForEventTier(long eventTierL, GenericMatchParams matchParams) {
        updateParamMapForEventTier(eventTierL, matchParams, defaultParamsInTier, defaultIndividualsInTier);
    }

    /**
     * 
     * @param eventTier
     * @param matchParams
     */
    protected static void updateParamMapForEventTier(long eventTierL, GenericMatchParams matchParams,
                    String[][] paramsInTier, boolean[] individualsInTier) {
        int eventTier = (int) eventTierL;
        if (eventTier < 1 || eventTier > nTiers)
            throw new IllegalArgumentException("EventTier not in range 1 to " + nTiers);
        /*
         * remove all the params not associated with this tier
         */
        Map<String, MatchParam> paramMap = matchParams.getParamMap();
        boolean individualParamsAlreadyInMap = false;
        Iterator<Map.Entry<String, MatchParam>> iter = paramMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, MatchParam> e = iter.next();
            String paramName = e.getKey();
            MarketGroup marketGroup = e.getValue().getMarketGroup();
            if (!inThisTier((int) eventTier, paramName, marketGroup, paramsInTier, individualsInTier)) {
                iter.remove();
            } else
                individualParamsAlreadyInMap |= marketGroup.equals(MarketGroup.INDIVIDUAL);
        }
        /*
         * add any params associated with this tier that are not already in the map
         */
        String[] paramNames = paramsInTier[eventTier - 1];
        for (String paramName : paramNames) {
            if (paramMap.get(paramName) == null) {
                MatchParam matchParam = getParamFromName(paramName);
                if (matchParam != null)
                    paramMap.put(paramName, matchParam);
            }
        }
        if (individualsInTier[eventTier - 1] && !individualParamsAlreadyInMap) {
            addIndividualParamsToMap(paramMap);
        }
    }

    private static boolean inThisTier(int eventTier, String paramName, MarketGroup marketGroup, String[][] paramsInTier,
                    boolean[] individualsInTier) {
        if (marketGroup.equals(MarketGroup.PENALTY)) {
            /*
             * leave any penalty type params untouched - not affected by tiers
             */
            return true;
        }
        if (marketGroup.equals(MarketGroup.INDIVIDUAL)) {
            return individualsInTier[eventTier - 1];
        }
        String[] paramsInThisTier = paramsInTier[eventTier - 1];
        for (String paramNameInThisTier : paramsInThisTier) {
            if (paramNameInThisTier.equals(paramName))
                return true;
        }
        return false;
    }

    private static MatchParam getParamFromName(String paramName) {
        MatchParam matchParam = null;
        switch (paramName) {
            case "goalTotal":
                matchParam = goalTotalParam();
                break;
            case "goalSupremacy":
                matchParam = goalSupremacyParam();
                break;
            case "homeLoseBoost":
                matchParam = homeLoseBoostParam();
                break;
            case "awayLoseBoost":
                matchParam = awayLoseBoostParam();
                break;
            case "targetGoalBoost":
                matchParam = targetGoalBoostParam();
                break;
            case "goalsDevn":
                matchParam = goalsDevnParam();
                break;
            case "cornerTotal":
                matchParam = cornerTotalParam();
                break;
            case "cornerSupremacy":
                matchParam = cornerSupremacyParam();
                break;
            case "cardTotal":
                matchParam = cardTotalParam();
                break;
            case "cardSupremacy":
                matchParam = cardSupremacyParam();
                break;
            case "redCardProb":
                matchParam = redCardProbParam();
                break;
        }
        return matchParam;
    }

    private static void addIndividualParamsToMap(Map<String, MatchParam> paramMap) {
        Map<String, MatchParam> individualParams = generateDefaultIndividualParams();
        for (Entry<String, MatchParam> e : individualParams.entrySet())
            paramMap.put(e.getKey(), e.getValue());
    }

    private void updateParamMapForPenaltiesPossible(boolean penaltiesPossible) {
        if (penaltiesPossible)
            for (Entry<String, MatchParam> e : penaltyParams.entrySet())
                paramMap.put(e.getKey(), e.getValue());
    }

    @Override
    public void setFromMap(Map<String, MatchParam> genericMatchParamsMap) {
        boolean mapContainsIndividualParams = false;
        boolean mapContainsPenaltyParams = false;
        for (MatchParam matchParam : genericMatchParamsMap.values()) {
            mapContainsIndividualParams |= matchParam.getMarketGroup().equals(MarketGroup.INDIVIDUAL);
            mapContainsPenaltyParams |= matchParam.getMarketGroup().equals(MarketGroup.PENALTY);
        }
        /*
         * if either of these types of params are present in the map then clear out completely our current set and
         * replace by the new ones
         */
        if (mapContainsIndividualParams)
            individualParams.clear();
        if (mapContainsPenaltyParams)
            penaltyParams.clear();
        paramMap.clear();
        for (Entry<String, MatchParam> e : genericMatchParamsMap.entrySet()) {
            MatchParam matchParam = e.getValue();
            String paramName = e.getKey();
            switch (matchParam.getMarketGroup()) {
                case INDIVIDUAL:
                    individualParams.put(e.getKey(), matchParam);
                    break;
                case PENALTY:
                    penaltyParams.put(e.getKey(), matchParam);
                    break;
                default:
                    setParamFromName(paramName, matchParam);
                    break;
            }
            paramMap.put(paramName, matchParam);
        }
    }

    private void setParamFromName(String paramName, MatchParam matchParam) {
        switch (paramName) {
            case "goalTotal":
                goalTotal = matchParam;
                break;
            case "goalSupremacy":
                goalSupremacy = matchParam;
                break;
            case "homeLoseBoost":
                homeLoseBoost = matchParam;
                break;
            case "awayLoseBoost":
                awayLoseBoost = matchParam;
                break;
            case "targetGoalBoost":
                targetGoalBoost = matchParam;
                break;
            case "goalsDevn":
                goalsDevn = matchParam;
                break;
            case "cornerTotal":
                cornerTotal = matchParam;
                break;
            case "cornerSupremacy":
                cornerSupremacy = matchParam;
                break;
            case "cardTotal":
                cardTotal = matchParam;
                break;
            case "cardSupremacy":
                cardSupremacy = matchParam;
                break;
            case "redCardProb":
                redCardProb = matchParam;
                break;
            default:
                break;
        }
    }

    @Override
    public void setEqualTo(MatchParams matchParams) {
        FootballMatchParams other = (FootballMatchParams) matchParams;
        goalTotal.setEqualTo(other.goalTotal);
        goalSupremacy.setEqualTo(other.goalSupremacy);
        homeLoseBoost.setEqualTo(other.homeLoseBoost);
        awayLoseBoost.setEqualTo(other.awayLoseBoost);
        targetGoalBoost.setEqualTo(other.targetGoalBoost);
        goalsDevn.setEqualTo(other.goalsDevn);
        cornerTotal.setEqualTo(other.cornerTotal);
        cornerSupremacy.setEqualTo(other.cornerSupremacy);
        cardTotal.setEqualTo(other.cardTotal);
        cardSupremacy.setEqualTo(other.cardSupremacy);
        redCardProb.setEqualTo(other.redCardProb);
        this.setIndividualParams(other.individualParams);
        this.setPenaltyParams(other.penaltyParams);
        updateParamMap();
        super.setEqualTo(matchParams);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((awayLoseBoost == null) ? 0 : awayLoseBoost.hashCode());
        result = prime * result + ((cardSupremacy == null) ? 0 : cardSupremacy.hashCode());
        result = prime * result + ((cardTotal == null) ? 0 : cardTotal.hashCode());
        result = prime * result + ((cornerSupremacy == null) ? 0 : cornerSupremacy.hashCode());
        result = prime * result + ((cornerTotal == null) ? 0 : cornerTotal.hashCode());
        result = prime * result + ((goalSupremacy == null) ? 0 : goalSupremacy.hashCode());
        result = prime * result + ((goalTotal == null) ? 0 : goalTotal.hashCode());
        result = prime * result + ((targetGoalBoost == null) ? 0 : targetGoalBoost.hashCode());
        result = prime * result + ((goalsDevn == null) ? 0 : goalsDevn.hashCode());
        result = prime * result + ((homeLoseBoost == null) ? 0 : homeLoseBoost.hashCode());
        result = prime * result + ((individualParams == null) ? 0 : individualParams.hashCode());
        result = prime * result + ((penaltyParams == null) ? 0 : penaltyParams.hashCode());
        result = prime * result + ((redCardProb == null) ? 0 : redCardProb.hashCode());
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
        FootballMatchParams other = (FootballMatchParams) obj;
        if (awayLoseBoost == null) {
            if (other.awayLoseBoost != null)
                return false;
        } else if (!awayLoseBoost.equals(other.awayLoseBoost))
            return false;
        if (cardSupremacy == null) {
            if (other.cardSupremacy != null)
                return false;
        } else if (!cardSupremacy.equals(other.cardSupremacy))
            return false;
        if (cardTotal == null) {
            if (other.cardTotal != null)
                return false;
        } else if (!cardTotal.equals(other.cardTotal))
            return false;
        if (cornerSupremacy == null) {
            if (other.cornerSupremacy != null)
                return false;
        } else if (!cornerSupremacy.equals(other.cornerSupremacy))
            return false;
        if (cornerTotal == null) {
            if (other.cornerTotal != null)
                return false;
        } else if (!cornerTotal.equals(other.cornerTotal))
            return false;
        if (goalSupremacy == null) {
            if (other.goalSupremacy != null)
                return false;
        } else if (!goalSupremacy.equals(other.goalSupremacy))
            return false;
        if (goalTotal == null) {
            if (other.goalTotal != null)
                return false;
        } else if (!goalTotal.equals(other.goalTotal))
            return false;
        if (targetGoalBoost == null) {
            if (other.targetGoalBoost != null)
                return false;
        } else if (!targetGoalBoost.equals(other.targetGoalBoost))
            return false;
        if (goalsDevn == null) {
            if (other.goalsDevn != null)
                return false;
        } else if (!goalsDevn.equals(other.goalsDevn))
            return false;
        if (homeLoseBoost == null) {
            if (other.homeLoseBoost != null)
                return false;
        } else if (!homeLoseBoost.equals(other.homeLoseBoost))
            return false;
        if (individualParams == null) {
            if (other.individualParams != null)
                return false;
        } else if (!individualParams.equals(other.individualParams))
            return false;
        if (penaltyParams == null) {
            if (other.penaltyParams != null)
                return false;
        } else if (!penaltyParams.equals(other.penaltyParams))
            return false;
        if (redCardProb == null) {
            if (other.redCardProb != null)
                return false;
        } else if (!redCardProb.equals(other.redCardProb))
            return false;
        return true;
    }

    public void applyMomentumLogic(MatchIncidentResult matchIncidentResult,
                    MatchEngineSavedState matchEngineSavedState) {
        /*
         * no momentum logic for football
         */

    }
}
