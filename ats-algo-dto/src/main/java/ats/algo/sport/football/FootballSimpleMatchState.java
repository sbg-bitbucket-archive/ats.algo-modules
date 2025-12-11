package ats.algo.sport.football;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.core.common.TeamId;
import ats.algo.core.common.TeamSheet;
import ats.algo.core.matchresult.MatchResultElement;
import ats.algo.core.matchresult.MatchResultEntryType;
import ats.algo.core.matchresult.MatchResultMap;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FootballSimpleMatchState extends SimpleMatchState {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private int goalsA;
    private int goalsB;
    private int cornersA;
    private int cornersB;
    private int yellowCardsA;
    private int yellowCardsB;
    private int redCardsA;
    private int redCardsB;

    private int firstHalfGoalsA;
    private int firstHalfGoalsB;
    private int secondHalfGoalsA;
    private int secondHalfGoalsB;

    private FootballMatchPeriod matchPeriod;
    private int elapsedTimeSeconds;

    private boolean clockRunningNow;

    private Map<String, String> goalScorerNames;
    private boolean varReferralInProgress;

    private TeamSheet teamSheet;

    private FootballShootoutInfo shootoutInfo;// = new FootballShootoutInfo(0, 0, TeamId.UNKNOWN, 0, true);


    @JsonCreator
    /**
     * 
     * @param preMatch
     * @param matchCompleted
     * @param clockRunningNow
     * @param matchPeriod
     * @param elapsedTimeSeconds
     * @param goalsA
     * @param goalsB
     * @param cornersA
     * @param cornersB
     * @param yellowCardsA
     * @param yellowCardsB
     * @param redCardsA
     * @param redCardsB
     * @param firstHalfGoalA
     * @param firstHalfGoalB
     * @param secondHalfGoalA
     * @param secondHalfGoalB
     * @param goalScorerNames
     * @param teamSheet
     * @param shootoutInfo
     */
    public FootballSimpleMatchState(@JsonProperty("preMatch") boolean preMatch,
                    @JsonProperty("matchCompleted") boolean matchCompleted,
                    @JsonProperty("clockRunningNow") boolean clockRunningNow,
                    @JsonProperty("matchPeriod") FootballMatchPeriod matchPeriod,
                    @JsonProperty("elapsedTimeSeconds") int elapsedTimeSeconds, @JsonProperty("goalsA") int goalsA,
                    @JsonProperty("goalsB") int goalsB, @JsonProperty("cornersA") int cornersA,
                    @JsonProperty("cornersB") int cornersB, @JsonProperty("yellowCardsA") int yellowCardsA,
                    @JsonProperty("yellowCardsB") int yellowCardsB, @JsonProperty("redCardsA") int redCardsA,
                    @JsonProperty("redCardsB") int redCardsB, @JsonProperty("firstHalfGoalsA") int firstHalfGoalA,
                    @JsonProperty("firstHalfGoalsB") int firstHalfGoalB,
                    @JsonProperty("secondHalfGoalsA") int secondHalfGoalA,
                    @JsonProperty("secondHalfGoalsB") int secondHalfGoalB,
                    @JsonProperty("goalScorers") Map<String, String> goalScorerNames,
                    @JsonProperty("teamSheet") TeamSheet teamSheet,
                    @JsonProperty("shootoutInfo") FootballShootoutInfo shootoutInfo,
                    @JsonProperty("varReferralInProgress") boolean varReferralInProgress) {
        super(preMatch, matchCompleted);
        this.varReferralInProgress = varReferralInProgress;
        this.matchPeriod = matchPeriod;
        this.elapsedTimeSeconds = elapsedTimeSeconds;
        this.clockRunningNow = clockRunningNow;
        this.goalsA = goalsA;
        this.goalsB = goalsB;
        this.cornersA = cornersA;
        this.cornersB = cornersB;
        this.yellowCardsA = yellowCardsA;
        this.yellowCardsB = yellowCardsB;
        this.redCardsA = redCardsA;
        this.redCardsB = redCardsB;
        this.firstHalfGoalsA = firstHalfGoalA;
        this.firstHalfGoalsB = firstHalfGoalB;
        this.secondHalfGoalsA = secondHalfGoalA;
        this.secondHalfGoalsB = secondHalfGoalB;
        this.goalScorerNames = goalScorerNames;
        this.teamSheet = teamSheet;
        this.shootoutInfo = shootoutInfo;
    }



    public FootballSimpleMatchState() {
        super();
        matchPeriod = FootballMatchPeriod.PREMATCH;
    }

    public FootballShootoutInfo getShootoutInfo() {
        return shootoutInfo;
    }

    public void setShootoutInfo(FootballShootoutInfo shootoutInfo) {
        this.shootoutInfo = shootoutInfo;
    }

    public boolean isClockRunningNow() {
        return clockRunningNow;
    }

    public void setClockRunningNow(boolean clockRunningNow) {
        this.clockRunningNow = clockRunningNow;
    }

    public int getGoalsA() {
        return goalsA;
    }

    public int getGoalsB() {
        return goalsB;
    }

    public int getCornersA() {
        return cornersA;
    }

    public int getCornersB() {
        return cornersB;
    }

    public int getYellowCardsA() {
        return yellowCardsA;
    }

    public int getYellowCardsB() {
        return yellowCardsB;
    }

    public int getRedCardsA() {
        return redCardsA;
    }

    public int getRedCardsB() {
        return redCardsB;
    }

    public FootballMatchPeriod getMatchPeriod() {
        return matchPeriod;
    }

    public int getElapsedTimeSeconds() {
        return elapsedTimeSeconds;
    }

    @Override
    public int elapsedTime() {
        return elapsedTimeSeconds;
    }

    public int getFirstHalfGoalsA() {
        return firstHalfGoalsA;
    }

    public int getFirstHalfGoalsB() {
        return firstHalfGoalsB;
    }

    public int getSecondHalfGoalsA() {
        return secondHalfGoalsA;
    }

    public int getSecondHalfGoalsB() {
        return secondHalfGoalsB;
    }


    public Map<String, String> getGoalScorerNames() {
        return goalScorerNames;
    }

    public boolean isVarReferralInProgress() {
        return varReferralInProgress;
    }

    public void setVarReferralInProgress(boolean varReferralInProgress) {
        this.varReferralInProgress = varReferralInProgress;
    }



    @Override
    public TeamSheet getTeamSheet() {
        return teamSheet;
    }

    public void setGoalsA(int goalsA) {
        this.goalsA = goalsA;
    }

    public void setGoalsB(int goalsB) {
        this.goalsB = goalsB;
    }

    public void setCornersA(int cornersA) {
        this.cornersA = cornersA;
    }

    public void setCornersB(int cornersB) {
        this.cornersB = cornersB;
    }

    public void setYellowCardsA(int yellowCardsA) {
        this.yellowCardsA = yellowCardsA;
    }

    public void setYellowCardsB(int yellowCardsB) {
        this.yellowCardsB = yellowCardsB;
    }

    public void setRedCardsA(int redCardsA) {
        this.redCardsA = redCardsA;
    }

    public void setRedCardsB(int redCardsB) {
        this.redCardsB = redCardsB;
    }

    public void setFirstHalfGoalsA(int firstHalfGoalsA) {
        this.firstHalfGoalsA = firstHalfGoalsA;
    }

    public void setFirstHalfGoalsB(int firstHalfGoalsB) {
        this.firstHalfGoalsB = firstHalfGoalsB;
    }

    public void setSecondHalfGoalsA(int secondHalfGoalsA) {
        this.secondHalfGoalsA = secondHalfGoalsA;
    }

    public void setSecondHalfGoalsB(int secondHalfGoalsB) {
        this.secondHalfGoalsB = secondHalfGoalsB;
    }

    public void setMatchPeriod(FootballMatchPeriod matchPeriod) {
        this.matchPeriod = matchPeriod;
    }

    public void setElapsedTimeSeconds(int elapsedTimeSeconds) {
        this.elapsedTimeSeconds = elapsedTimeSeconds;
    }

    public void setGoalScorerNames(Map<String, String> goalScorerNames) {
        this.goalScorerNames = goalScorerNames;
    }

    public void setTeamSheet(TeamSheet teamSheet) {
        this.teamSheet = teamSheet;
    }

    @Override
    public Map<String, String> playerStateAsMap() {
        if (teamSheet == null)
            return super.playerStateAsMap();
        return teamSheet.playerStatusMap();
    }

    @Override
    public MatchResultMap generateResultMapFromSimpleMatchState(MatchFormat format) {
        MatchResultMap resultMap = new MatchResultMap();
        // FootballMatchFormat footballFormat = (FootballMatchFormat) format;

        if ((getFirstHalfGoalsA() > (-1)) && (getFirstHalfGoalsB() > (-1))) {
            String goalsAFH = Integer.toString(getFirstHalfGoalsA());
            String goalsBFH = Integer.toString(getFirstHalfGoalsB());
            resultMap.put("firstHalfGoals", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50,
                            goalsAFH + "-" + goalsBFH));
        }

        if ((getSecondHalfGoalsA() > (-1)) && (getSecondHalfGoalsB() > (-1))) {
            String goalsASH = Integer.toString(getSecondHalfGoalsA());
            String goalsBSH = Integer.toString(getSecondHalfGoalsB());
            resultMap.put("secondHalfGoals", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50,
                            goalsASH + "-" + goalsBSH));
        }

        if ((getGoalsA() > (-1)) && (getGoalsB() > (-1))) {
            String goalsA = Integer.toString(getGoalsA());
            String goalsB = Integer.toString(getGoalsB());
            resultMap.put("totalGoals",
                            new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50, goalsA + "-" + goalsB));
        }

        if ((getCornersA() > (-1)) && (getCornersB() > (-1))) {
            String cornersA = Integer.toString(getCornersA());
            String cornersB = Integer.toString(getCornersB());
            resultMap.put("totalCorners", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50,
                            cornersA + "-" + cornersB));
        }

        if ((getYellowCardsA() > (-1)) && (getYellowCardsB() > (-1))) {
            String yellowCardsA = Integer.toString(getYellowCardsA());
            String yellowCardsB = Integer.toString(getYellowCardsB());
            resultMap.put("totalYellowCards", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50,
                            yellowCardsA + "-" + yellowCardsB));
        }

        if ((getRedCardsA() > (-1)) && (getRedCardsB() > (-1))) {
            String redCardsA = Integer.toString(getRedCardsA());
            String redCardsB = Integer.toString(getRedCardsB());
            resultMap.put("totalRedCards", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50,
                            redCardsA + "-" + redCardsB));
        }

        return resultMap;
    }

    @Override
    public FootballSimpleMatchState generateMatchStateFromMatchResultMap(MatchResultMap result,
                    MatchFormat matchFormat) {
        FootballMatchState fms = new FootballMatchState(matchFormat);
        FootballMatchFormat fmf = (FootballMatchFormat) matchFormat;

        Map<String, MatchResultElement> map = result.getMap();

        boolean fHalfGoals = map.get("firstHalfGoals") != null;
        boolean sHalfGoals = map.get("secondHalfGoals") != null;
        boolean totGoals = map.get("totalGoals") != null;

        boolean fHalfCorners = map.get("firstHalfCorners") != null;
        boolean sHalfCorners = map.get("secondHalfCorners") != null;
        boolean totCorners = map.get("totalCorners") != null;

        boolean fHalfYellowCards = map.get("firstHalfYellowCards") != null;
        boolean sHalfYellowCards = map.get("secondHalfYellowCards") != null;
        boolean totYellowCards = map.get("totalYellowCards") != null;

        boolean fHalfRedCards = map.get("firstHalfRedCards") != null;
        boolean sHalfRedCards = map.get("secondHalfRedCards") != null;
        boolean totRedCards = map.get("totalRedCards") != null;

        if (fHalfGoals && sHalfGoals) {
            if (map.get("firstHalfGoals").valueAsPairOfIntegersCheckingNegatives() != null
                            && map.get("secondHalfGoals").valueAsPairOfIntegersCheckingNegatives() != null) {
                int firstHalfGoalsA = map.get("firstHalfGoals").valueAsPairOfIntegersCheckingNegatives().A;
                int firstHalfGoalsB = map.get("firstHalfGoals").valueAsPairOfIntegersCheckingNegatives().B;

                int secondHalfGoalsA = map.get("secondHalfGoals").valueAsPairOfIntegersCheckingNegatives().A;
                int secondHalfGoalsB = map.get("secondHalfGoals").valueAsPairOfIntegersCheckingNegatives().B;

                fms.setGoalsA(firstHalfGoalsA + secondHalfGoalsA);
                fms.setGoalsB(firstHalfGoalsB + secondHalfGoalsB);

                for (int i = 0; i < firstHalfGoalsA; i++) {
                    fms.getFirstHalfGoals().add(TeamId.A);
                }
                for (int i = 0; i < firstHalfGoalsB; i++) {
                    fms.getFirstHalfGoals().add(TeamId.B);
                }
                for (int i = 0; i < secondHalfGoalsA; i++) {
                    fms.getSecondHalfGoals().add(TeamId.A);
                }
                for (int i = 0; i < secondHalfGoalsB; i++) {
                    fms.getSecondHalfGoals().add(TeamId.B);
                }

                if (fmf.getExtraTimeMinutes() > 0) {
                    if (map.get("extraTimeFirstHalfGoals").valueAsPairOfIntegersCheckingNegatives() != null && map
                                    .get("extraTimeFirstHalfGoals").valueAsPairOfIntegersCheckingNegatives() != null) {
                        fms.setGoalsA(fms.getGoalsA()
                                        + map.get("extraTimeFirstHalfGoals").valueAsPairOfIntegersCheckingNegatives().A
                                        + map.get("extraTimeSecondHalfGoals")
                                                        .valueAsPairOfIntegersCheckingNegatives().A);

                        fms.setGoalsB(fms.getGoalsB()
                                        + map.get("extraTimeFirstHalfGoals").valueAsPairOfIntegersCheckingNegatives().B
                                        + map.get("extraTimeSecondHalfGoals")
                                                        .valueAsPairOfIntegersCheckingNegatives().B);
                    }
                }
            } else if (totGoals) {
                if (map.get("totalGoals").valueAsPairOfIntegersCheckingNegatives() != null) {
                    fms.setGoalsA(map.get("totalGoals").valueAsPairOfIntegersCheckingNegatives().A);
                    fms.setGoalsB(map.get("totalGoals").valueAsPairOfIntegersCheckingNegatives().B);
                }
            }
        }

        if (fHalfCorners && sHalfCorners) {
            if (map.get("firstHalfCorners").valueAsPairOfIntegersCheckingNegatives() != null
                            && map.get("secondHalfCorners").valueAsPairOfIntegersCheckingNegatives() != null) {

                fms.setCornersA(map.get("firstHalfCorners").valueAsPairOfIntegersCheckingNegatives().A
                                + map.get("secondHalfCorners").valueAsPairOfIntegersCheckingNegatives().A);

                fms.setCornersB(map.get("firstHalfCorners").valueAsPairOfIntegersCheckingNegatives().B
                                + map.get("secondHalfCorners").valueAsPairOfIntegersCheckingNegatives().B);

                if (fmf.getExtraTimeMinutes() > 0) {
                    if (map.get("extraTimeFirstHalfCorners").valueAsPairOfIntegersCheckingNegatives() != null
                                    && map.get("extraTimeFirstHalfCorners")
                                                    .valueAsPairOfIntegersCheckingNegatives() != null) {
                        fms.setCornersA(fms.getCornersA()
                                        + map.get("extraTimeFirstHalfCorners")
                                                        .valueAsPairOfIntegersCheckingNegatives().A
                                        + map.get("extraTimeSecondHalfCorners")
                                                        .valueAsPairOfIntegersCheckingNegatives().A);

                        fms.setCornersB(fms.getCornersB()
                                        + map.get("extraTimeFirstHalfCorners")
                                                        .valueAsPairOfIntegersCheckingNegatives().B
                                        + map.get("extraTimeSecondHalfCorners")
                                                        .valueAsPairOfIntegersCheckingNegatives().B);
                    }
                }
            }
        } else if (totCorners) {
            if (map.get("totalCorners").valueAsPairOfIntegersCheckingNegatives() != null) {
                fms.setCornersA(map.get("totalCorners").valueAsPairOfIntegersCheckingNegatives().A);
                fms.setCornersB(map.get("totalCorners").valueAsPairOfIntegersCheckingNegatives().B);
            }
        }

        if (fHalfYellowCards && sHalfYellowCards) {
            if (map.get("firstHalfYellowCards").valueAsPairOfIntegersCheckingNegatives() != null
                            && map.get("secondHalfYellowCards").valueAsPairOfIntegersCheckingNegatives() != null) {

                fms.setYellowCardsA(map.get("firstHalfYellowCards").valueAsPairOfIntegersCheckingNegatives().A
                                + map.get("secondHalfYellowCards").valueAsPairOfIntegersCheckingNegatives().A);

                fms.setYellowCardsB(map.get("firstHalfYellowCards").valueAsPairOfIntegersCheckingNegatives().B
                                + map.get("secondHalfYellowCards").valueAsPairOfIntegersCheckingNegatives().B);

                if (fmf.getExtraTimeMinutes() > 0) {
                    if (map.get("extraTimeFirstHalfYellowCards").valueAsPairOfIntegersCheckingNegatives() != null
                                    && map.get("extraTimeFirstHalfYellowCards")
                                                    .valueAsPairOfIntegersCheckingNegatives() != null) {
                        fms.setYellowCardsA(fms.getYellowCardsA()
                                        + map.get("extraTimeFirstHalfYellowCards")
                                                        .valueAsPairOfIntegersCheckingNegatives().A
                                        + map.get("extraTimeSecondHalfYellowCards")
                                                        .valueAsPairOfIntegersCheckingNegatives().A);

                        fms.setYellowCardsB(fms.getYellowCardsB()
                                        + map.get("extraTimeFirstHalfYellowCards")
                                                        .valueAsPairOfIntegersCheckingNegatives().B
                                        + map.get("extraTimeSecondHalfYellowCards")
                                                        .valueAsPairOfIntegersCheckingNegatives().B);
                    }
                }
            }
        } else if (totYellowCards) {
            if (map.get("totalYellowCards").valueAsPairOfIntegersCheckingNegatives() != null) {
                fms.setYellowCardsA(map.get("totalYellowCards").valueAsPairOfIntegersCheckingNegatives().A);
                fms.setYellowCardsB(map.get("totalYellowCards").valueAsPairOfIntegersCheckingNegatives().B);
            }
        }

        if (fHalfRedCards && sHalfRedCards) {
            if (map.get("firstHalfRedCards").valueAsPairOfIntegersCheckingNegatives() != null
                            && map.get("secondHalfRedCards").valueAsPairOfIntegersCheckingNegatives() != null) {

                fms.setRedCardsA(map.get("firstHalfRedCards").valueAsPairOfIntegersCheckingNegatives().A
                                + map.get("secondHalfRedCards").valueAsPairOfIntegersCheckingNegatives().A);

                fms.setRedCardsB(map.get("firstHalfRedCards").valueAsPairOfIntegersCheckingNegatives().B
                                + map.get("secondHalfRedCards").valueAsPairOfIntegersCheckingNegatives().B);

                if (fmf.getExtraTimeMinutes() > 0) {
                    if (map.get("extraTimeFirstHalfRedCards").valueAsPairOfIntegersCheckingNegatives() != null
                                    && map.get("extraTimeFirstHalfRedCards")
                                                    .valueAsPairOfIntegersCheckingNegatives() != null) {
                        fms.setRedCardsA(fms.getRedCardsA()
                                        + map.get("extraTimeFirstHalfRedCards")
                                                        .valueAsPairOfIntegersCheckingNegatives().A
                                        + map.get("extraTimeSecondHalfRedCards")
                                                        .valueAsPairOfIntegersCheckingNegatives().A);

                        fms.setRedCardsB(fms.getRedCardsB()
                                        + map.get("extraTimeFirstHalfRedCards")
                                                        .valueAsPairOfIntegersCheckingNegatives().B
                                        + map.get("extraTimeSecondHalfRedCards")
                                                        .valueAsPairOfIntegersCheckingNegatives().B);
                    }
                }
            }
        } else if (totRedCards) {
            if (map.get("totalRedCards").valueAsPairOfIntegersCheckingNegatives() != null) {
                fms.setRedCardsA(map.get("totalRedCards").valueAsPairOfIntegersCheckingNegatives().A);
                fms.setRedCardsB(map.get("totalRedCards").valueAsPairOfIntegersCheckingNegatives().B);
            }
        }

        fms.setMatchPeriod(FootballMatchPeriod.MATCH_COMPLETED);
        FootballSimpleMatchState sms = fms.generateSimpleMatchState();
        return sms;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + cornersA;
        result = prime * result + cornersB;
        result = prime * result + elapsedTimeSeconds;
        result = prime * result + firstHalfGoalsA;
        result = prime * result + firstHalfGoalsB;
        result = prime * result + ((goalScorerNames == null) ? 0 : goalScorerNames.hashCode());
        result = prime * result + goalsA;
        result = prime * result + goalsB;
        result = prime * result + ((matchPeriod == null) ? 0 : matchPeriod.hashCode());
        result = prime * result + redCardsA;
        result = prime * result + redCardsB;
        result = prime * result + secondHalfGoalsA;
        result = prime * result + secondHalfGoalsB;
        result = prime * result + ((shootoutInfo == null) ? 0 : shootoutInfo.hashCode());
        result = prime * result + ((teamSheet == null) ? 0 : teamSheet.hashCode());
        result = prime * result + yellowCardsA;
        result = prime * result + yellowCardsB;
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
        FootballSimpleMatchState other = (FootballSimpleMatchState) obj;
        if (cornersA != other.cornersA)
            return false;
        if (cornersB != other.cornersB)
            return false;
        if (elapsedTimeSeconds != other.elapsedTimeSeconds)
            return false;
        if (firstHalfGoalsA != other.firstHalfGoalsA)
            return false;
        if (firstHalfGoalsB != other.firstHalfGoalsB)
            return false;
        if (goalScorerNames == null) {
            if (other.goalScorerNames != null)
                return false;
        } else if (!goalScorerNames.equals(other.goalScorerNames))
            return false;
        if (goalsA != other.goalsA)
            return false;
        if (goalsB != other.goalsB)
            return false;
        if (matchPeriod != other.matchPeriod)
            return false;
        if (redCardsA != other.redCardsA)
            return false;
        if (redCardsB != other.redCardsB)
            return false;
        if (secondHalfGoalsA != other.secondHalfGoalsA)
            return false;
        if (secondHalfGoalsB != other.secondHalfGoalsB)
            return false;
        if (shootoutInfo == null) {
            if (other.shootoutInfo != null)
                return false;
        } else if (!shootoutInfo.equals(other.shootoutInfo))
            return false;
        if (teamSheet == null) {
            if (other.teamSheet != null)
                return false;
        } else if (!teamSheet.equals(other.teamSheet))
            return false;
        if (yellowCardsA != other.yellowCardsA)
            return false;
        if (yellowCardsB != other.yellowCardsB)
            return false;
        return true;
    }


}
