package ats.algo.sport.americanfootball;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.core.matchresult.MatchResultElement;
import ats.algo.core.matchresult.MatchResultEntryType;
import ats.algo.core.matchresult.MatchResultMap;

public class AmericanfootballSimpleMatchState extends SimpleMatchState {

    private static final long serialVersionUID = 1L;
    private int totalPointsA;
    private int totalPointsB;

    private int firstQuarterTDA;
    private int secondQuarterTDA;
    private int thirdQuarterTDA;
    private int fourthQuarterTDA;
    private int extraTimeQuarterTDA;

    private int firstQuarterTDB;
    private int secondQuarterTDB;
    private int thirdQuarterTDB;
    private int fourthQuarterTDB;
    private int extraTimeQuarterTDB;

    private int firstQuarterPointsA;
    private int secondQuarterPointsA;
    private int thirdQuarterPointsA;
    private int fourthQuarterPointsA;
    private int extraTimeQuarterPointsA;

    private int firstQuarterPointsB;
    private int secondQuarterPointsB;
    private int thirdQuarterPointsB;
    private int fourthQuarterPointsB;
    private int extraTimeQuarterPointsB;

    private AmericanfootballMatchPeriod matchPeriod;
    private int elapsedTimeSeconds;

    @JsonCreator
    public AmericanfootballSimpleMatchState(@JsonProperty("preMatch") boolean preMatch,
                    @JsonProperty("matchCompleted") boolean matchCompleted,
                    @JsonProperty("matchPeriod") AmericanfootballMatchPeriod matchPeriod,
                    @JsonProperty("elapsedTimeSeconds") int elapsedTimeSeconds,
                    @JsonProperty("totalPointsA") int totalPointsA, @JsonProperty("totalPointsB") int totalPointsB,
                    @JsonProperty("firstQuarterTDA") int firstQuarterTDA,
                    @JsonProperty("secondQuarterTDA") int secondQuarterTDA,
                    @JsonProperty("thirdQuarterTDA") int thirdQuarterTDA,
                    @JsonProperty("fourthQuarterTDA") int fourthQuarterTDA,
                    @JsonProperty("extraTimeQuarterTDA") int extraTimeQuarterTDA,
                    @JsonProperty("firstQuarterTDB") int firstQuarterTDB,
                    @JsonProperty("secondQuarterTDB") int secondQuarterTDB,
                    @JsonProperty("thirdQuarterTDB") int thirdQuarterTDB,
                    @JsonProperty("fourthQuarterTDB") int fourthQuarterTDB,
                    @JsonProperty("extraTimeQuarterTDB") int extraTimeQuarterTDB,
                    @JsonProperty("firstQuarterPointsA") int firstQuarterPointsA,
                    @JsonProperty("secondQuarterPointsA") int secondQuarterPointsA,
                    @JsonProperty("thirdQuarterPointsA") int thirdQuarterPointsA,
                    @JsonProperty("fourthQuarterPointsA") int fourthQuarterPointsA,
                    @JsonProperty("extraTimeQuarterPointsA") int extraTimeQuarterPointsA,
                    @JsonProperty("firstQuarterPointsB") int firstQuarterPointsB,
                    @JsonProperty("secondQuarterPointsB") int secondQuarterPointsB,
                    @JsonProperty("thirdQuarterPointsB") int thirdQuarterPointsB,
                    @JsonProperty("fourthQuarterPointsB") int fourthQuarterPointsB,
                    @JsonProperty("extraTimeQuarterPointsB") int extraTimeQuarterPointsB)

    {
        super(preMatch, matchCompleted);
        this.matchPeriod = matchPeriod;
        this.elapsedTimeSeconds = elapsedTimeSeconds;
        this.totalPointsA = totalPointsA;
        this.totalPointsB = totalPointsB;
        this.firstQuarterTDA = firstQuarterTDA;
        this.secondQuarterTDA = secondQuarterTDA;
        this.thirdQuarterTDA = thirdQuarterTDA;
        this.fourthQuarterTDA = fourthQuarterTDA;
        this.extraTimeQuarterTDA = extraTimeQuarterTDA;
        this.firstQuarterTDB = firstQuarterTDB;
        this.secondQuarterTDB = secondQuarterTDB;
        this.thirdQuarterTDB = thirdQuarterTDB;
        this.fourthQuarterTDB = fourthQuarterTDB;
        this.extraTimeQuarterTDB = extraTimeQuarterTDB;
        this.firstQuarterPointsA = firstQuarterPointsA;
        this.secondQuarterPointsA = secondQuarterPointsA;
        this.thirdQuarterPointsA = thirdQuarterPointsA;
        this.fourthQuarterPointsA = fourthQuarterPointsA;
        this.extraTimeQuarterPointsA = extraTimeQuarterPointsA;
        this.firstQuarterPointsB = firstQuarterPointsB;
        this.secondQuarterPointsB = secondQuarterPointsB;
        this.thirdQuarterPointsB = thirdQuarterPointsB;
        this.fourthQuarterPointsB = fourthQuarterPointsB;
        this.extraTimeQuarterPointsB = extraTimeQuarterPointsB;
    }

    @Override
    public MatchResultMap generateResultMapFromSimpleMatchState(MatchFormat format) {
        MatchResultMap resultMap = new MatchResultMap();
        int validNOofQuarters = 0;

        if ((getFirstQuarterPointsA() > (-1)) && (getFirstQuarterPointsB() > (-1))) {
            String goalsAFP = Integer.toString(getFirstQuarterPointsA());
            String goalsBFP = Integer.toString(getFirstQuarterPointsB());
            resultMap.put("firstQuarterPoints", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50,
                            goalsAFP + "-" + goalsBFP));
            validNOofQuarters++;
        }

        if ((getSecondQuarterPointsA() > (-1)) && (getSecondQuarterPointsB() > (-1))) {
            String goalsASP = Integer.toString(getSecondQuarterPointsA());
            String goalsBSP = Integer.toString(getSecondQuarterPointsB());
            resultMap.put("secondQuarterPoints", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50,
                            goalsASP + "-" + goalsBSP));
            validNOofQuarters++;
        }

        if ((getThirdQuarterPointsA() > (-1)) && (getThirdQuarterPointsB() > (-1))) {
            String goalsATP = Integer.toString(getThirdQuarterPointsA());
            String goalsBTP = Integer.toString(getThirdQuarterPointsB());
            resultMap.put("thirdQuarterPoints", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50,
                            goalsATP + "-" + goalsBTP));
            validNOofQuarters++;
        }

        if ((getFourthQuarterPointsA() > (-1)) && (getFourthQuarterPointsB() > (-1))) {
            String goalsATP = Integer.toString(getFourthQuarterPointsA());
            String goalsBTP = Integer.toString(getFourthQuarterPointsB());
            resultMap.put("fourthQuarterPoints", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50,
                            goalsATP + "-" + goalsBTP));
            validNOofQuarters++;
        }

        if (validNOofQuarters == 4) {
            String goalsATP = Integer.toString(getFirstQuarterPointsA() + getSecondQuarterPointsA()
                            + getThirdQuarterPointsA() + getFourthQuarterPointsA());
            String goalsBTP = Integer.toString(getFirstQuarterPointsB() + getSecondQuarterPointsB()
                            + getThirdQuarterPointsB() + getFourthQuarterPointsB());
            resultMap.put("totalPoints", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50,
                            goalsATP + "-" + goalsBTP));
        }

        if ((getExtraTimeQuarterPointsA() > (-1)) && (getExtraTimeQuarterPointsB() > (-1))) {
            String goalsATP = Integer.toString(getExtraTimeQuarterPointsA());
            String goalsBTP = Integer.toString(getExtraTimeQuarterPointsB());
            resultMap.put("extraTimeQuarterPoints", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50,
                            goalsATP + "-" + goalsBTP));
        }

        return resultMap;
    }

    @Override
    public AmericanfootballSimpleMatchState generateMatchStateFromMatchResultMap(MatchResultMap result,
                    MatchFormat matchFormat) {
        AmericanfootballMatchState fms = new AmericanfootballMatchState(matchFormat);
        AmericanfootballMatchFormat fmf = (AmericanfootballMatchFormat) matchFormat;

        Map<String, MatchResultElement> map = result.getMap();

        boolean quarterPoints1 = map.get("firstQuarterPoints") != null;
        boolean quarterPoints2 = map.get("secondQuarterPoints") != null;
        boolean quarterPoints3 = map.get("thirdQuarterPoints") != null;
        boolean quarterPoints4 = map.get("fourthQuarterPoints") != null;
        boolean extraTimeQuarterPoints = map.get("extraTimeQuarterPoints") != null;

        int quarterPoints1A = -1;
        int quarterPoints1B = -1;
        int quarterPoints2A = -1;
        int quarterPoints2B = -1;
        int quarterPoints3A = -1;
        int quarterPoints3B = -1;
        int quarterPoints4A = -1;
        int quarterPoints4B = -1;

        if (quarterPoints1 && quarterPoints2 && quarterPoints3 && quarterPoints4) {
            quarterPoints1A = map.get("firstQuarterPoints").valueAsPairOfIntegersCheckingNegatives().A;
            quarterPoints1B = map.get("firstQuarterPoints").valueAsPairOfIntegersCheckingNegatives().B;

            quarterPoints2A = map.get("secondQuarterPoints").valueAsPairOfIntegersCheckingNegatives().A;
            quarterPoints2B = map.get("secondQuarterPoints").valueAsPairOfIntegersCheckingNegatives().B;

            quarterPoints3A = map.get("thirdQuarterPoints").valueAsPairOfIntegersCheckingNegatives().A;
            quarterPoints3B = map.get("thirdQuarterPoints").valueAsPairOfIntegersCheckingNegatives().B;

            quarterPoints4A = map.get("fourthQuarterPoints").valueAsPairOfIntegersCheckingNegatives().A;
            quarterPoints4B = map.get("fourthQuarterPoints").valueAsPairOfIntegersCheckingNegatives().B;
        }


        fms.setFirstQuarterPointsA(quarterPoints1A);
        fms.setFirstQuarterPointsB(quarterPoints1B);
        fms.setSecondQuarterPointsA(quarterPoints2A);
        fms.setSecondQuarterPointsB(quarterPoints2B);
        fms.setThirdQuarterPointsA(quarterPoints3A);
        fms.setThirdQuarterPointsB(quarterPoints3B);
        fms.setFourthQuarterPointsA(quarterPoints4A);
        fms.setFourthQuarterPointsB(quarterPoints4B);

        fms.setTotalPointsA(quarterPoints1A + quarterPoints2A + quarterPoints3A + quarterPoints4A);
        fms.setTotalPointsB(quarterPoints1B + quarterPoints2B + quarterPoints3B + quarterPoints4B);


        if (fmf.getExtraTimeMinutes() > 0 && extraTimeQuarterPoints) {
            fms.setExtraTimeQuarterPointsA(
                            map.get("extraTimeQuarterPoints").valueAsPairOfIntegersCheckingNegatives().A);
            fms.setExtraTimeQuarterPointsB(
                            map.get("extraTimeQuarterPoints").valueAsPairOfIntegersCheckingNegatives().B);
        }

        fms.setMatchPeriod(AmericanfootballMatchPeriod.MATCH_COMPLETED);
        AmericanfootballSimpleMatchState sms = (AmericanfootballSimpleMatchState) fms.generateSimpleMatchState();
        return sms;
    }

    public AmericanfootballSimpleMatchState() {
        super();
        matchPeriod = AmericanfootballMatchPeriod.PREMATCH;
    }

    public int getTotalPointsA() {
        return totalPointsA;
    }

    public void setTotalPointsA(int totalPointsA) {
        this.totalPointsA = totalPointsA;
    }

    public int getTotalPointsB() {
        return totalPointsB;
    }

    public void setTotalPointsB(int totalPointsB) {
        this.totalPointsB = totalPointsB;
    }

    public int getFirstQuarterTDA() {
        return firstQuarterTDA;
    }

    public void setFirstQuarterTDA(int firstQuarterTDA) {
        this.firstQuarterTDA = firstQuarterTDA;
    }

    public int getSecondQuarterTDA() {
        return secondQuarterTDA;
    }

    public void setSecondQuarterTDA(int secondQuarterTDA) {
        this.secondQuarterTDA = secondQuarterTDA;
    }

    public int getThirdQuarterTDA() {
        return thirdQuarterTDA;
    }

    public void setThirdQuarterTDA(int thirdQuarterTDA) {
        this.thirdQuarterTDA = thirdQuarterTDA;
    }

    public int getFourthQuarterTDA() {
        return fourthQuarterTDA;
    }

    public void setFourthQuarterTDA(int fourthQuarterTDA) {
        this.fourthQuarterTDA = fourthQuarterTDA;
    }

    public int getExtraTimeQuarterTDA() {
        return extraTimeQuarterTDA;
    }

    public void setExtraTimeQuarterTDA(int extraTimeQuarterTDA) {
        this.extraTimeQuarterTDA = extraTimeQuarterTDA;
    }

    public int getFirstQuarterTDB() {
        return firstQuarterTDB;
    }

    public void setFirstQuarterTDB(int firstQuarterTDB) {
        this.firstQuarterTDB = firstQuarterTDB;
    }

    public int getSecondQuarterTDB() {
        return secondQuarterTDB;
    }

    public void setSecondQuarterTDB(int secondQuarterTDB) {
        this.secondQuarterTDB = secondQuarterTDB;
    }

    public int getThirdQuarterTDB() {
        return thirdQuarterTDB;
    }

    public void setThirdQuarterTDB(int thirdQuarterTDB) {
        this.thirdQuarterTDB = thirdQuarterTDB;
    }

    public int getFourthQuarterTDB() {
        return fourthQuarterTDB;
    }

    public void setFourthQuarterTDB(int fourthQuarterTDB) {
        this.fourthQuarterTDB = fourthQuarterTDB;
    }

    public int getExtraTimeQuarterTDB() {
        return extraTimeQuarterTDB;
    }

    public void setExtraTimeQuarterTDB(int extraTimeQuarterTDB) {
        this.extraTimeQuarterTDB = extraTimeQuarterTDB;
    }

    public int getFirstQuarterPointsA() {
        return firstQuarterPointsA;
    }

    public void setFirstQuarterPointsA(int firstQuarterPointsA) {
        this.firstQuarterPointsA = firstQuarterPointsA;
    }

    public int getSecondQuarterPointsA() {
        return secondQuarterPointsA;
    }

    public void setSecondQuarterPointsA(int secondQuarterPointsA) {
        this.secondQuarterPointsA = secondQuarterPointsA;
    }

    public int getThirdQuarterPointsA() {
        return thirdQuarterPointsA;
    }

    public void setThirdQuarterPointsA(int thirdQuarterPointsA) {
        this.thirdQuarterPointsA = thirdQuarterPointsA;
    }

    public int getFourthQuarterPointsA() {
        return fourthQuarterPointsA;
    }

    public void setFourthQuarterPointsA(int fourthQuarterPointsA) {
        this.fourthQuarterPointsA = fourthQuarterPointsA;
    }

    public int getExtraTimeQuarterPointsA() {
        return extraTimeQuarterPointsA;
    }

    public void setExtraTimeQuarterPointsA(int extraTimeQuarterPointsA) {
        this.extraTimeQuarterPointsA = extraTimeQuarterPointsA;
    }

    public int getFirstQuarterPointsB() {
        return firstQuarterPointsB;
    }

    public void setFirstQuarterPointsB(int firstQuarterPointsB) {
        this.firstQuarterPointsB = firstQuarterPointsB;
    }

    public int getSecondQuarterPointsB() {
        return secondQuarterPointsB;
    }

    public void setSecondQuarterPointsB(int secondQuarterPointsB) {
        this.secondQuarterPointsB = secondQuarterPointsB;
    }

    public int getThirdQuarterPointsB() {
        return thirdQuarterPointsB;
    }

    public void setThirdQuarterPointsB(int thirdQuarterPointsB) {
        this.thirdQuarterPointsB = thirdQuarterPointsB;
    }

    public int getFourthQuarterPointsB() {
        return fourthQuarterPointsB;
    }

    public void setFourthQuarterPointsB(int fourthQuarterPointsB) {
        this.fourthQuarterPointsB = fourthQuarterPointsB;
    }

    public int getExtraTimeQuarterPointsB() {
        return extraTimeQuarterPointsB;
    }

    public void setExtraTimeQuarterPointsB(int extraTimeQuarterPointsB) {
        this.extraTimeQuarterPointsB = extraTimeQuarterPointsB;
    }

    public AmericanfootballMatchPeriod getMatchPeriod() {
        return matchPeriod;
    }

    public void setMatchPeriod(AmericanfootballMatchPeriod matchPeriod) {
        this.matchPeriod = matchPeriod;
    }

    public int getElapsedTimeSeconds() {
        return elapsedTimeSeconds;
    }

    public void setElapsedTimeSeconds(int elapsedTimeSeconds) {
        this.elapsedTimeSeconds = elapsedTimeSeconds;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + elapsedTimeSeconds;
        result = prime * result + extraTimeQuarterPointsA;
        result = prime * result + extraTimeQuarterPointsB;
        result = prime * result + extraTimeQuarterTDA;
        result = prime * result + extraTimeQuarterTDB;
        result = prime * result + firstQuarterPointsA;
        result = prime * result + firstQuarterPointsB;
        result = prime * result + firstQuarterTDA;
        result = prime * result + firstQuarterTDB;
        result = prime * result + fourthQuarterPointsA;
        result = prime * result + fourthQuarterPointsB;
        result = prime * result + fourthQuarterTDA;
        result = prime * result + fourthQuarterTDB;
        result = prime * result + ((matchPeriod == null) ? 0 : matchPeriod.hashCode());
        result = prime * result + secondQuarterPointsA;
        result = prime * result + secondQuarterPointsB;
        result = prime * result + secondQuarterTDA;
        result = prime * result + secondQuarterTDB;
        result = prime * result + thirdQuarterPointsA;
        result = prime * result + thirdQuarterPointsB;
        result = prime * result + thirdQuarterTDA;
        result = prime * result + thirdQuarterTDB;
        result = prime * result + totalPointsA;
        result = prime * result + totalPointsB;
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
        AmericanfootballSimpleMatchState other = (AmericanfootballSimpleMatchState) obj;
        if (elapsedTimeSeconds != other.elapsedTimeSeconds)
            return false;
        if (extraTimeQuarterPointsA != other.extraTimeQuarterPointsA)
            return false;
        if (extraTimeQuarterPointsB != other.extraTimeQuarterPointsB)
            return false;
        if (extraTimeQuarterTDA != other.extraTimeQuarterTDA)
            return false;
        if (extraTimeQuarterTDB != other.extraTimeQuarterTDB)
            return false;
        if (firstQuarterPointsA != other.firstQuarterPointsA)
            return false;
        if (firstQuarterPointsB != other.firstQuarterPointsB)
            return false;
        if (firstQuarterTDA != other.firstQuarterTDA)
            return false;
        if (firstQuarterTDB != other.firstQuarterTDB)
            return false;
        if (fourthQuarterPointsA != other.fourthQuarterPointsA)
            return false;
        if (fourthQuarterPointsB != other.fourthQuarterPointsB)
            return false;
        if (fourthQuarterTDA != other.fourthQuarterTDA)
            return false;
        if (fourthQuarterTDB != other.fourthQuarterTDB)
            return false;
        if (matchPeriod != other.matchPeriod)
            return false;
        if (secondQuarterPointsA != other.secondQuarterPointsA)
            return false;
        if (secondQuarterPointsB != other.secondQuarterPointsB)
            return false;
        if (secondQuarterTDA != other.secondQuarterTDA)
            return false;
        if (secondQuarterTDB != other.secondQuarterTDB)
            return false;
        if (thirdQuarterPointsA != other.thirdQuarterPointsA)
            return false;
        if (thirdQuarterPointsB != other.thirdQuarterPointsB)
            return false;
        if (thirdQuarterTDA != other.thirdQuarterTDA)
            return false;
        if (thirdQuarterTDB != other.thirdQuarterTDB)
            return false;
        if (totalPointsA != other.totalPointsA)
            return false;
        if (totalPointsB != other.totalPointsB)
            return false;
        return true;
    }


}
