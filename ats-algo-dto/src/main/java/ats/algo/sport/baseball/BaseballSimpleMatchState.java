package ats.algo.sport.baseball;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.core.common.TeamId;
import ats.algo.core.matchresult.MatchResultElement;
import ats.algo.core.matchresult.MatchResultEntryType;
import ats.algo.core.matchresult.MatchResultMap;
import ats.algo.sport.baseball.BaseballMatchIncidentResult.BaseballMatchPeriod;

@JsonPropertyOrder({"preMatch", "matchCompleted", "hitsA", "hitsB", "hit", "out", "inning", "bat", "batFirst", "runsA",
        "runsB", "strike", "ball", "adjustScore", "base1", "base2", "base3", "firstHalf", "pitcherOneA", "pitcherOneB",
        "extraInnings"})

public class BaseballSimpleMatchState extends SimpleMatchState {

    private static final long serialVersionUID = 1L;
    private String info;

    private BaseballMatchFormat matchFormat;
    private int hitsA;
    private int hitsB;
    private int hit;
    private int out;
    private int inning;
    private TeamId bat;
    private TeamId batFirst;
    private int runsA;
    private int runsB;
    private int strike;
    private int ball;
    // private int[][] runsInInningsN;
    // private int[][] hitsInInningsN;
    @JsonIgnore
    private int adjustScore;
    private boolean base1;
    private boolean base2;
    private boolean base3;
    private boolean firstHalf;
    private boolean pitcherOneA;
    private boolean pitcherOneB;
    private int extraInnings;

    @JsonCreator
    public BaseballSimpleMatchState(@JsonProperty("preMatch") boolean preMatch,
                    @JsonProperty("matchCompleted") boolean matchCompleted, @JsonProperty("hitsA") int hitsA,
                    @JsonProperty("hitsB") int hitsB, @JsonProperty("hit") int hit, @JsonProperty("out") int out,
                    @JsonProperty("inning") int inning, @JsonProperty("bat") TeamId bat,
                    @JsonProperty("batFirst") TeamId batFirst, @JsonProperty("runsA") int runsA,
                    @JsonProperty("runsB") int runsB, @JsonProperty("strike") int strike,
                    @JsonProperty("ball") int ball, @JsonProperty("base1") boolean base1,
                    @JsonProperty("base2") boolean base2, @JsonProperty("base3") boolean base3,
                    @JsonProperty("firstHalf") boolean firstHalf, @JsonProperty("pitcherOneA") boolean pitcherOneA,
                    @JsonProperty("pitcherOneB") boolean pitcherOneB, @JsonProperty("extraInnings") int extraInnings)

    {
        super(preMatch, matchCompleted);
        info = "simpleMatchState not yet implemented for" + this.getClass().getSimpleName();
        this.hitsA = hitsA;
        this.hitsB = hitsB;
        this.out = out;
        this.inning = inning;
        this.bat = bat;
        this.batFirst = batFirst;
        this.runsA = runsA;
        this.runsB = runsB;
        this.strike = strike;
        this.ball = ball;
        this.base1 = base1;
        this.base2 = base2;
        this.base3 = base3;
        this.firstHalf = firstHalf;
        this.pitcherOneA = pitcherOneA;
        this.pitcherOneB = pitcherOneB;
        this.extraInnings = extraInnings;
        // TODO - replace info property by the properties needed for this sport.
        // c.f. e.g. TennisSimpleMatchState
    }

    @Override
    public MatchResultMap generateResultMapFromSimpleMatchState(MatchFormat format) {
        MatchResultMap resultMap = new MatchResultMap();
        if ((getRunsA() > (-1)) && (getRunsB() > (-1))) {
            String runsA = Integer.toString(getRunsA());
            String runsB = Integer.toString(getRunsB());
            resultMap.put("runs",
                            new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50, runsA + "-" + runsB));
        }
        return resultMap;
    }

    @Override
    public BaseballSimpleMatchState generateMatchStateFromMatchResultMap(MatchResultMap result,
                    MatchFormat matchFormat) {
        BaseballMatchState fms = new BaseballMatchState(matchFormat);

        Map<String, MatchResultElement> map = result.getMap();

        boolean runs = map.get("runs") != null;

        int runsA = -1;
        int runsB = -1;

        if (runs) {
            runsA = map.get("runs").valueAsPairOfIntegersCheckingNegatives().A;
            runsB = map.get("runs").valueAsPairOfIntegersCheckingNegatives().B;
        }

        fms.setRunsA(runsA);
        fms.setRunsB(runsB);

        TeamId wonTeam = TeamId.UNKNOWN;
        if (runsA > runsB)
            wonTeam = TeamId.A;
        else if (runsA < runsB)
            wonTeam = TeamId.B;

        fms.setCurrentMatchState(new BaseballMatchIncidentResult(BaseballMatchPeriod.MATCH_COMPLETED, wonTeam));
        BaseballSimpleMatchState sms = (BaseballSimpleMatchState) fms.generateSimpleMatchState();
        return sms;
    }


    public BaseballSimpleMatchState() {
        super();

    }

    public BaseballMatchFormat getMatchFormat() {
        return matchFormat;
    }



    public void setMatchFormat(BaseballMatchFormat matchFormat) {
        this.matchFormat = matchFormat;
    }



    public int getHitsA() {
        return hitsA;
    }



    public void setHitsA(int hitsA) {
        this.hitsA = hitsA;
    }



    public int getHitsB() {
        return hitsB;
    }



    public void setHitsB(int hitsB) {
        this.hitsB = hitsB;
    }



    public int getHit() {
        return hit;
    }



    public void setHit(int hit) {
        this.hit = hit;
    }



    public int getOut() {
        return out;
    }



    public void setOut(int out) {
        this.out = out;
    }



    public int getInning() {
        return inning;
    }



    public void setInning(int inning) {
        this.inning = inning;
    }



    public TeamId getBat() {
        return bat;
    }



    public void setBat(TeamId bat) {
        this.bat = bat;
    }

    public TeamId getBatFirst() {
        return batFirst;
    }

    public void setBatFirst(TeamId batFirst) {
        this.batFirst = batFirst;
    }

    public int getRunsA() {
        return runsA;
    }

    public void setRunsA(int runsA) {
        this.runsA = runsA;
    }

    public int getRunsB() {
        return runsB;
    }



    public void setRunsB(int runsB) {
        this.runsB = runsB;
    }



    public int getStrike() {
        return strike;
    }



    public void setStrike(int strike) {
        this.strike = strike;
    }



    public int getBall() {
        return ball;
    }



    public void setBall(int ball) {
        this.ball = ball;
    }



    public int getAdjustScore() {
        return adjustScore;
    }



    public void setAdjustScore(int adjustScore) {
        this.adjustScore = adjustScore;
    }



    public boolean isBase1() {
        return base1;
    }



    public void setBase1(boolean base1) {
        this.base1 = base1;
    }



    public boolean isBase2() {
        return base2;
    }



    public void setBase2(boolean base2) {
        this.base2 = base2;
    }



    public boolean isBase3() {
        return base3;
    }



    public void setBase3(boolean base3) {
        this.base3 = base3;
    }



    public boolean isFirstHalf() {
        return firstHalf;
    }



    public void setFirstHalf(boolean firstHalf) {
        this.firstHalf = firstHalf;
    }



    public boolean isPitcherOneA() {
        return pitcherOneA;
    }



    public void setPitcherOneA(boolean pitcherOneA) {
        this.pitcherOneA = pitcherOneA;
    }



    public boolean isPitcherOneB() {
        return pitcherOneB;
    }



    public void setPitcherOneB(boolean pitcherOneB) {
        this.pitcherOneB = pitcherOneB;
    }



    public int getExtraInnings() {
        return extraInnings;
    }



    public void setExtraInnings(int extraInnings) {
        this.extraInnings = extraInnings;
    }



    public void setInfo(String info) {
        this.info = info;
    }



    public String getInfo() {
        return info;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + adjustScore;
        result = prime * result + ball;
        result = prime * result + (base1 ? 1231 : 1237);
        result = prime * result + (base2 ? 1231 : 1237);
        result = prime * result + (base3 ? 1231 : 1237);
        result = prime * result + ((bat == null) ? 0 : bat.hashCode());
        result = prime * result + ((batFirst == null) ? 0 : batFirst.hashCode());
        result = prime * result + extraInnings;
        result = prime * result + (firstHalf ? 1231 : 1237);
        result = prime * result + hit;
        result = prime * result + hitsA;
        result = prime * result + hitsB;
        result = prime * result + ((info == null) ? 0 : info.hashCode());
        result = prime * result + inning;
        result = prime * result + ((matchFormat == null) ? 0 : matchFormat.hashCode());
        result = prime * result + out;
        result = prime * result + (pitcherOneA ? 1231 : 1237);
        result = prime * result + (pitcherOneB ? 1231 : 1237);
        result = prime * result + runsA;
        result = prime * result + runsB;
        result = prime * result + strike;
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
        BaseballSimpleMatchState other = (BaseballSimpleMatchState) obj;
        if (adjustScore != other.adjustScore)
            return false;
        if (ball != other.ball)
            return false;
        if (base1 != other.base1)
            return false;
        if (base2 != other.base2)
            return false;
        if (base3 != other.base3)
            return false;
        if (bat != other.bat)
            return false;
        if (batFirst != other.batFirst)
            return false;
        if (extraInnings != other.extraInnings)
            return false;
        if (firstHalf != other.firstHalf)
            return false;
        if (hit != other.hit)
            return false;
        if (hitsA != other.hitsA)
            return false;
        if (hitsB != other.hitsB)
            return false;
        if (info == null) {
            if (other.info != null)
                return false;
        } else if (!info.equals(other.info))
            return false;
        if (inning != other.inning)
            return false;
        if (matchFormat == null) {
            if (other.matchFormat != null)
                return false;
        } else if (!matchFormat.equals(other.matchFormat))
            return false;
        if (out != other.out)
            return false;
        if (pitcherOneA != other.pitcherOneA)
            return false;
        if (pitcherOneB != other.pitcherOneB)
            return false;
        if (runsA != other.runsA)
            return false;
        if (runsB != other.runsB)
            return false;
        if (strike != other.strike)
            return false;
        return true;
    }



    @Override
    public String toString() {
        return "[info=" + info + "]";
    }
}

