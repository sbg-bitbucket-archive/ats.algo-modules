/**
 * 
 */
package ats.algo.sport.basketball;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.core.matchresult.MatchResultElement;
import ats.algo.core.matchresult.MatchResultEntryType;
import ats.algo.core.matchresult.MatchResultMap;

/**
 * @author Robert
 *
 */
public class BasketballSimpleMatchState extends SimpleMatchState {


    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private int pointsA;
    private int pointsB;
    private int firstQuarterPointsA;
    private int firstHalfPointsA;
    private int secondQuarterPointsA;
    private int secondHalfPointsA;
    private int thirdQuarterPointsA;
    private int fourthQuarterPointsA;

    private int firstQuarterPointsB;
    private int firstHalfPointsB;
    private int secondQuarterPointsB;
    private int secondHalfPointsB;
    private int thirdQuarterPointsB;
    private int fourthQuarterPointsB;

    private BasketballMatchPeriod matchPeriod;
    private int elapsedTimeSeconds;

    /**
     * 
     * @param preMatch
     * @param matchCompleted
     * @param pointsA
     * @param pointsB
     * @param firstQuarterPointsA
     * @param firstQuarterPointsB
     * @param secondQuarterPointsA
     * @param secondQuarterPointsB
     * @param thirdQuarterPointsA
     * @param thirdQuarterPointsB
     * @param fourthQuarterPointsA
     * @param fourthQuarterPointsB
     * @param firstHalfPointsA
     * @param firstHalfPointsB
     * @param secondHalfPointsA
     * @param secondHalfPointsB
     */
    @JsonCreator
    public BasketballSimpleMatchState(@JsonProperty("preMatch") boolean preMatch,
                    @JsonProperty("matchCompleted") boolean matchCompleted,
                    @JsonProperty("matchPeriod") BasketballMatchPeriod matchPeriod,
                    @JsonProperty("elapsedTimeSeconds") int elapsedTimeSeconds, @JsonProperty("pointsA") int pointsA,
                    @JsonProperty("pointsB") int pointsB, @JsonProperty("firstQuarterPointsA") int firstQuarterPointsA,
                    @JsonProperty("secondQuarterPointsA") int secondQuarterPointsA,
                    @JsonProperty("thirdQuarterPointsA") int thirdQuarterPointsA,
                    @JsonProperty("fourthQuarterPointsA") int fourthQuarterPointsA,
                    @JsonProperty("firstHalfPointsA") int firstHalfPointsA,
                    @JsonProperty("secondHalfPointsA") int secondHalfPointsA,
                    @JsonProperty("firstQuarterPointsB") int firstQuarterPointsB,
                    @JsonProperty("secondQuarterPointsB") int secondQuarterPointsB,
                    @JsonProperty("thirdQuarterPointsB") int thirdQuarterPointsB,
                    @JsonProperty("fourthQuarterPointsB") int fourthQuarterPointsB,
                    @JsonProperty("firstHalfPointsB") int firstHalfPointsB,
                    @JsonProperty("secondHalfPointsB") int secondHalfPointsB

    ) {
        super(preMatch, matchCompleted);
        this.matchPeriod = matchPeriod;
        this.elapsedTimeSeconds = elapsedTimeSeconds;
        this.pointsA = pointsA;
        this.pointsB = pointsB;
        this.firstQuarterPointsA = firstQuarterPointsA;
        this.firstQuarterPointsB = firstQuarterPointsB;
        this.secondQuarterPointsA = secondQuarterPointsA;
        this.secondQuarterPointsB = secondQuarterPointsB;
        this.thirdQuarterPointsA = thirdQuarterPointsA;
        this.thirdQuarterPointsB = thirdQuarterPointsB;
        this.fourthQuarterPointsA = fourthQuarterPointsA;
        this.fourthQuarterPointsB = fourthQuarterPointsB;
        this.firstHalfPointsA = firstHalfPointsA;
        this.firstHalfPointsB = firstHalfPointsB;
        this.secondHalfPointsA = secondHalfPointsA;
        this.secondHalfPointsB = secondHalfPointsB;
    }

    public BasketballSimpleMatchState() {
        super();
        matchPeriod = BasketballMatchPeriod.PREMATCH;
    }

    @Override
    public MatchResultMap generateResultMapFromSimpleMatchState(MatchFormat format) {
        MatchResultMap resultMap = new MatchResultMap();
        BasketballMatchFormat basketballMatchFormat = (BasketballMatchFormat) format;

        if (!basketballMatchFormat.isTwoHalvesFormat()) {

            if ((getFirstQuarterPointsA() > (-1)) && (getFirstQuarterPointsB() > (-1))) {
                String goalsAFP = Integer.toString(getFirstQuarterPointsA());
                String goalsBFP = Integer.toString(getFirstQuarterPointsB());
                resultMap.put("quarter1Score", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50,
                                goalsAFP + "-" + goalsBFP));
            }

            if ((getSecondQuarterPointsA() > (-1)) && (getSecondQuarterPointsB() > (-1))) {
                String goalsAFP = Integer.toString(getSecondQuarterPointsA());
                String goalsBFP = Integer.toString(getSecondQuarterPointsB());
                resultMap.put("quarter2Score", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50,
                                goalsAFP + "-" + goalsBFP));
            }

            if ((getThirdQuarterPointsA() > (-1)) && (getThirdQuarterPointsB() > (-1))) {
                String goalsAFP = Integer.toString(getThirdQuarterPointsA());
                String goalsBFP = Integer.toString(getThirdQuarterPointsB());
                resultMap.put("quarter3Score", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50,
                                goalsAFP + "-" + goalsBFP));
            }

            if ((getFourthQuarterPointsA() > (-1)) && (getFourthQuarterPointsB() > (-1))) {
                String goalsAFP = Integer.toString(getFourthQuarterPointsA());
                String goalsBFP = Integer.toString(getFourthQuarterPointsB());
                resultMap.put("quarter4Score", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50,
                                goalsAFP + "-" + goalsBFP));
            }
            int totalPointsFromQuartersA = getFirstQuarterPointsA() + getSecondQuarterPointsA()
                            + getThirdQuarterPointsA() + getFourthQuarterPointsA();
            int totalPointsFromQuartersB = getFirstQuarterPointsB() + getSecondQuarterPointsB()
                            + getThirdQuarterPointsB() + getFourthQuarterPointsB();

            boolean overTimeA = false;
            boolean overTimeB = false;
            if (totalPointsFromQuartersA < getPointsA()) {
                String goalsAFP = Integer.toString(getPointsA() - totalPointsFromQuartersA);
                overTimeA = true;
                if (totalPointsFromQuartersB < getPointsB()) {
                    String goalsBFP = Integer.toString(getPointsB() - totalPointsFromQuartersB);
                    overTimeB = true;

                    if (overTimeA && overTimeB) {
                        resultMap.put("extraTimeScore", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0,
                                        50, goalsAFP + "-" + goalsBFP));
                    } else {
                        resultMap.put("extraTimeScore",
                                        new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50, "-1-0"));
                    }
                } else {
                    resultMap.put("extraTimeScore",
                                    new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50, "-1-0"));
                }
            } else {
                resultMap.put("extraTimeScore",
                                new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50, "-1-0"));
            }



        } else {

            if ((getFirstHalfPointsA() > (-1)) && (getFirstHalfPointsB() > (-1))) {
                String goalsAFP = Integer.toString(getFirstHalfPointsA());
                String goalsBFP = Integer.toString(getFirstHalfPointsB());
                resultMap.put("half1Score", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50,
                                goalsAFP + "-" + goalsBFP));
            }

            if ((getSecondHalfPointsA() > (-1)) && (getSecondHalfPointsB() > (-1))) {
                String goalsAFP = Integer.toString(getSecondHalfPointsA());
                String goalsBFP = Integer.toString(getSecondHalfPointsB());
                resultMap.put("half2Score", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50,
                                goalsAFP + "-" + goalsBFP));
            }

            int totalPointsFromHalvesA = getFirstHalfPointsA() + getSecondHalfPointsA();
            int totalPointsFromHalvesB = getFirstHalfPointsB() + getSecondHalfPointsB();

            boolean overTimeA = false;
            boolean overTimeB = false;
            if (totalPointsFromHalvesA < getPointsA()) {
                String goalsAFP = Integer.toString(getPointsA() - totalPointsFromHalvesA);
                overTimeA = true;
                if (totalPointsFromHalvesB < getPointsB()) {
                    String goalsBFP = Integer.toString(getPointsB() - totalPointsFromHalvesB);
                    overTimeB = true;

                    if (overTimeA && overTimeB) {
                        resultMap.put("extraTimeScore", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0,
                                        50, goalsAFP + "-" + goalsBFP));
                    } else {
                        resultMap.put("extraTimeScore",
                                        new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50, "-1-0"));
                    }
                } else {
                    resultMap.put("extraTimeScore",
                                    new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50, "-1-0"));
                }
            } else {
                resultMap.put("extraTimeScore",
                                new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50, "-1-0"));
            }

        }


        return resultMap;
    }

    @Override
    public BasketballSimpleMatchState generateMatchStateFromMatchResultMap(MatchResultMap result,
                    MatchFormat matchFormat) {
        BasketballMatchState bms = new BasketballMatchState(matchFormat);
        BasketballMatchFormat bmf = (BasketballMatchFormat) matchFormat;

        Map<String, MatchResultElement> map = result.getMap();
        if (bmf.isTwoHalvesFormat()) {
            boolean fPeriodPoints = map.get("half1Score") != null;
            boolean sPeriodPoints = map.get("half2Score") != null;

            int firstHalfPointsA = -1;
            int firstHalfPointsB = -1;
            int secondHalfPointsA = -1;
            int secondHalfPointsB = -1;

            if (fPeriodPoints && sPeriodPoints) {
                firstHalfPointsA = map.get("half1Score").valueAsPairOfIntegersCheckingNegatives().A;
                firstHalfPointsB = map.get("half1Score").valueAsPairOfIntegersCheckingNegatives().B;

                secondHalfPointsA = map.get("half2Score").valueAsPairOfIntegersCheckingNegatives().A;
                secondHalfPointsB = map.get("half2Score").valueAsPairOfIntegersCheckingNegatives().B;
            }

            bms.setNormalTimeGoalsA((fPeriodPoints && sPeriodPoints) ? firstHalfPointsA + firstHalfPointsA : -1);
            bms.setNormalTimeGoalsB((fPeriodPoints && sPeriodPoints) ? firstHalfPointsB + firstHalfPointsB : -1);

            bms.setFirstHalfPointsA(firstHalfPointsA);
            bms.setSecondHalfPointsA(secondHalfPointsA);
            bms.setFirstHalfPointsB(firstHalfPointsB);
            bms.setSecondHalfPointsB(secondHalfPointsB);

            if (bms.getExtraPeriodSecs() > 0
                            && map.get("extraTimeScore").valueAsPairOfIntegersCheckingNegatives() != null) {
                bms.setPointsA(map.get("extraTimeScore").valueAsPairOfIntegersCheckingNegatives().A + firstHalfPointsA
                                + secondHalfPointsA);
                bms.setPointsB(map.get("extraTimeScore").valueAsPairOfIntegersCheckingNegatives().B + firstHalfPointsB
                                + secondHalfPointsB);
            } else {
                bms.setPointsA((fPeriodPoints && sPeriodPoints) ? firstHalfPointsA + firstHalfPointsA : -1);
                bms.setPointsB((fPeriodPoints && sPeriodPoints) ? firstHalfPointsB + firstHalfPointsB : -1);
            }
        } else {

            boolean firstPeriodPoints = map.get("quarter1Score") != null;
            boolean secondPeriodPoints = map.get("quarter2Score") != null;
            boolean thirdPeriodPoints = map.get("quarter3Score") != null;
            boolean fourthPeriodPoints = map.get("quarter4Score") != null;

            int firstQuarterPointsA = -1;
            int secondQuarterPointsA = -1;
            int thirdQuarterPointsA = -1;
            int fourthQuarterPointsA = -1;

            int firstQuarterPointsB = -1;
            int secondQuarterPointsB = -1;
            int thirdQuarterPointsB = -1;
            int fourthQuarterPointsB = -1;

            if (firstPeriodPoints && secondPeriodPoints && thirdPeriodPoints && fourthPeriodPoints) {
                firstQuarterPointsA = map.get("quarter1Score").valueAsPairOfIntegersCheckingNegatives().A;
                firstQuarterPointsB = map.get("quarter1Score").valueAsPairOfIntegersCheckingNegatives().B;

                secondQuarterPointsA = map.get("quarter2Score").valueAsPairOfIntegersCheckingNegatives().A;
                secondQuarterPointsB = map.get("quarter2Score").valueAsPairOfIntegersCheckingNegatives().B;

                thirdQuarterPointsA = map.get("quarter3Score").valueAsPairOfIntegersCheckingNegatives().A;
                thirdQuarterPointsB = map.get("quarter3Score").valueAsPairOfIntegersCheckingNegatives().B;

                fourthQuarterPointsA = map.get("quarter4Score").valueAsPairOfIntegersCheckingNegatives().A;
                fourthQuarterPointsB = map.get("quarter4Score").valueAsPairOfIntegersCheckingNegatives().B;
            }

            bms.setNormalTimeGoalsA((firstPeriodPoints && secondPeriodPoints && thirdPeriodPoints && fourthPeriodPoints)
                            ? firstQuarterPointsA + secondQuarterPointsA + thirdQuarterPointsA + fourthQuarterPointsA
                            : -1);
            bms.setNormalTimeGoalsB((firstPeriodPoints && secondPeriodPoints && thirdPeriodPoints && fourthPeriodPoints)
                            ? firstQuarterPointsB + secondQuarterPointsB + thirdQuarterPointsB + fourthQuarterPointsB
                            : -1);

            bms.setFirstQuarterPointsA(firstQuarterPointsA);
            bms.setFirstQuarterPointsB(firstQuarterPointsB);
            bms.setSecondQuarterPointsA(secondQuarterPointsA);
            bms.setSecondQuarterPointsB(secondQuarterPointsB);
            bms.setThirdQuarterPointsA(thirdQuarterPointsA);
            bms.setThirdQuarterPointsB(thirdQuarterPointsB);
            bms.setFourthQuarterPointsA(fourthQuarterPointsA);
            bms.setFourthQuarterPointsB(fourthQuarterPointsB);

            if (bms.getExtraPeriodSecs() > 0
                            && map.get("extraTimeScore").valueAsPairOfIntegersCheckingNegatives() != null) {
                bms.setPointsA(map.get("extraTimeScore").valueAsPairOfIntegersCheckingNegatives().A
                                + firstQuarterPointsA + secondQuarterPointsA + thirdQuarterPointsA
                                + fourthQuarterPointsA);
                bms.setPointsB(map.get("extraTimeScore").valueAsPairOfIntegersCheckingNegatives().B
                                + firstQuarterPointsB + secondQuarterPointsB + thirdQuarterPointsB
                                + fourthQuarterPointsB);
            } else {
                bms.setPointsA((firstPeriodPoints && secondPeriodPoints && thirdPeriodPoints && fourthPeriodPoints)
                                ? firstQuarterPointsA + secondQuarterPointsA + thirdQuarterPointsA
                                                + fourthQuarterPointsA
                                : -1);
                bms.setPointsB((firstPeriodPoints && secondPeriodPoints && thirdPeriodPoints && fourthPeriodPoints)
                                ? firstQuarterPointsB + secondQuarterPointsB + thirdQuarterPointsB
                                                + fourthQuarterPointsB
                                : -1);
            }

        }

        bms.setMatchPeriod(BasketballMatchPeriod.MATCH_COMPLETED);
        BasketballSimpleMatchState sms = bms.generateSimpleMatchState();
        return sms;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + elapsedTimeSeconds;
        result = prime * result + firstHalfPointsA;
        result = prime * result + firstHalfPointsB;
        result = prime * result + firstQuarterPointsA;
        result = prime * result + firstQuarterPointsB;
        result = prime * result + fourthQuarterPointsA;
        result = prime * result + fourthQuarterPointsB;
        result = prime * result + ((matchPeriod == null) ? 0 : matchPeriod.hashCode());
        result = prime * result + pointsA;
        result = prime * result + pointsB;
        result = prime * result + secondHalfPointsA;
        result = prime * result + secondHalfPointsB;
        result = prime * result + secondQuarterPointsA;
        result = prime * result + secondQuarterPointsB;
        result = prime * result + thirdQuarterPointsA;
        result = prime * result + thirdQuarterPointsB;
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
        BasketballSimpleMatchState other = (BasketballSimpleMatchState) obj;
        if (elapsedTimeSeconds != other.elapsedTimeSeconds)
            return false;
        if (firstHalfPointsA != other.firstHalfPointsA)
            return false;
        if (firstHalfPointsB != other.firstHalfPointsB)
            return false;
        if (firstQuarterPointsA != other.firstQuarterPointsA)
            return false;
        if (firstQuarterPointsB != other.firstQuarterPointsB)
            return false;
        if (fourthQuarterPointsA != other.fourthQuarterPointsA)
            return false;
        if (fourthQuarterPointsB != other.fourthQuarterPointsB)
            return false;
        if (matchPeriod != other.matchPeriod)
            return false;
        if (pointsA != other.pointsA)
            return false;
        if (pointsB != other.pointsB)
            return false;
        if (secondHalfPointsA != other.secondHalfPointsA)
            return false;
        if (secondHalfPointsB != other.secondHalfPointsB)
            return false;
        if (secondQuarterPointsA != other.secondQuarterPointsA)
            return false;
        if (secondQuarterPointsB != other.secondQuarterPointsB)
            return false;
        if (thirdQuarterPointsA != other.thirdQuarterPointsA)
            return false;
        if (thirdQuarterPointsB != other.thirdQuarterPointsB)
            return false;
        return true;
    }

    public void setPointsA(int pointsA) {
        this.pointsA = pointsA;
    }

    public void setPointsB(int pointsB) {
        this.pointsB = pointsB;
    }

    public void setFirstQuarterPointsA(int firstQuarterPointsA) {
        this.firstQuarterPointsA = firstQuarterPointsA;
    }

    public void setFirstHalfPointsA(int firstHalfPointsA) {
        this.firstHalfPointsA = firstHalfPointsA;
    }

    public void setSecondQuarterPointsA(int secondQuarterPointsA) {
        this.secondQuarterPointsA = secondQuarterPointsA;
    }

    public void setSecondHalfPointsA(int secondHalfPointsA) {
        this.secondHalfPointsA = secondHalfPointsA;
    }

    public void setThirdQuarterPointsA(int thirdQuarterPointsA) {
        this.thirdQuarterPointsA = thirdQuarterPointsA;
    }

    public void setFourthQuarterPointsA(int fourthQuarterPointsA) {
        this.fourthQuarterPointsA = fourthQuarterPointsA;
    }

    public void setFirstQuarterPointsB(int firstQuarterPointsB) {
        this.firstQuarterPointsB = firstQuarterPointsB;
    }

    public void setFirstHalfPointsB(int firstHalfPointsB) {
        this.firstHalfPointsB = firstHalfPointsB;
    }

    public void setSecondQuarterPointsB(int secondQuarterPointsB) {
        this.secondQuarterPointsB = secondQuarterPointsB;
    }

    public void setSecondHalfPointsB(int secondHalfPointsB) {
        this.secondHalfPointsB = secondHalfPointsB;
    }

    public void setThirdQuarterPointsB(int thirdQuarterPointsB) {
        this.thirdQuarterPointsB = thirdQuarterPointsB;
    }

    public void setFourthQuarterPointsB(int fourthQuarterPointsB) {
        this.fourthQuarterPointsB = fourthQuarterPointsB;
    }

    public void setMatchPeriod(BasketballMatchPeriod matchPeriod) {
        this.matchPeriod = matchPeriod;
    }

    public void setElapsedTimeSeconds(int elapsedTimeSeconds) {
        this.elapsedTimeSeconds = elapsedTimeSeconds;
    }


    public int getPointsA() {
        return pointsA;
    }

    public int getPointsB() {
        return pointsB;
    }

    public int getFirstQuarterPointsA() {
        return firstQuarterPointsA;
    }

    public int getFirstHalfPointsA() {
        return firstHalfPointsA;
    }

    public int getSecondQuarterPointsA() {
        return secondQuarterPointsA;
    }

    public int getSecondHalfPointsA() {
        return secondHalfPointsA;
    }

    public int getThirdQuarterPointsA() {
        return thirdQuarterPointsA;
    }

    public int getFourthQuarterPointsA() {
        return fourthQuarterPointsA;
    }

    public int getFirstQuarterPointsB() {
        return firstQuarterPointsB;
    }

    public int getFirstHalfPointsB() {
        return firstHalfPointsB;
    }

    public int getSecondQuarterPointsB() {
        return secondQuarterPointsB;
    }

    public int getSecondHalfPointsB() {
        return secondHalfPointsB;
    }

    public int getThirdQuarterPointsB() {
        return thirdQuarterPointsB;
    }

    public int getFourthQuarterPointsB() {
        return fourthQuarterPointsB;
    }

    public BasketballMatchPeriod getMatchPeriod() {
        return matchPeriod;
    }

    public int getElapsedTimeSeconds() {
        return elapsedTimeSeconds;
    }
}

