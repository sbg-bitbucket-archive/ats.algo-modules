package ats.algo.betstars.soccer;


import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchParam;
import ats.algo.core.baseclasses.MatchParamType;
import ats.algo.core.baseclasses.MatchParams;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * @author Gabriele Cacchioni on 30/10/2016.
 */
public class BsSoccerMatchParams extends MatchParams {

    /**
     * versionUID required since class is Serializable
     */
    private static final long serialVersionUID = 1L;


    // Team A Expectancy
    private MatchParam ScoreRateTeamA;

    // Team B Expectancy
    private MatchParam ScoreRateTeamB;

    // Team A Lose Boost
    private MatchParam LoseBoostTeamA;

    // Team B Lose Boost
    private MatchParam LoseBoostTeamB;

    // Win boost
    private MatchParam WinBoost;

    // Time Boost
    private MatchParam TimeBoost;


    /**
     * Class constructor - must initialise any new parameters to sensible default values
     */
    public BsSoccerMatchParams() {
        setDefaultParams();
    }

    @JsonCreator
    /**
     * no need to amend this
     * 
     * @param delegate
     */
    public BsSoccerMatchParams(Map<String, Object> delegate) {
        super(delegate);
        applyParams(delegate);
    }

    // Getters and Setters
    @JsonIgnore
    public MatchParam getScoreRateTeamA() {
        return ScoreRateTeamA;
    }

    @JsonIgnore
    public void setScoreRateTeamA(MatchParam argExpectancyTeamA) {
        this.ScoreRateTeamA = argExpectancyTeamA;
        updateParamMap();
    }

    @JsonIgnore
    public MatchParam getScoreRateTeamB() {
        return ScoreRateTeamB;
    }

    @JsonIgnore
    public void setScoreRateTeamB(MatchParam argExpectancyTeamB) {
        this.ScoreRateTeamB = argExpectancyTeamB;
        updateParamMap();
    }

    @JsonIgnore
    public MatchParam getLoseBoostTeamA() {
        return LoseBoostTeamA;
    }

    @JsonIgnore
    public void setLoseBoostTeamA(MatchParam loseBoostTeamA) {
        LoseBoostTeamA = loseBoostTeamA;
        updateParamMap();
    }

    @JsonIgnore
    public MatchParam getLoseBoostTeamB() {
        return LoseBoostTeamB;
    }

    @JsonIgnore
    public void setLoseBoostTeamB(MatchParam loseBoostTeamB) {
        LoseBoostTeamB = loseBoostTeamB;
        updateParamMap();
    }

    @JsonIgnore
    public MatchParam getWinBoost() {
        return WinBoost;
    }

    @JsonIgnore
    public void setWinBoost(MatchParam winBoost) {
        WinBoost = winBoost;
        updateParamMap();
    }

    @JsonIgnore
    public MatchParam getTimeBoost() {
        return TimeBoost;
    }

    @JsonIgnore
    public void setTimeBoost(MatchParam timeBoost) {
        TimeBoost = timeBoost;
        updateParamMap();
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((LoseBoostTeamA == null) ? 0 : LoseBoostTeamA.hashCode());
        result = prime * result + ((LoseBoostTeamB == null) ? 0 : LoseBoostTeamB.hashCode());
        result = prime * result + ((ScoreRateTeamA == null) ? 0 : ScoreRateTeamA.hashCode());
        result = prime * result + ((ScoreRateTeamB == null) ? 0 : ScoreRateTeamB.hashCode());
        result = prime * result + ((TimeBoost == null) ? 0 : TimeBoost.hashCode());
        result = prime * result + ((WinBoost == null) ? 0 : WinBoost.hashCode());
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
        BsSoccerMatchParams other = (BsSoccerMatchParams) obj;
        if (LoseBoostTeamA == null) {
            if (other.LoseBoostTeamA != null)
                return false;
        } else if (!LoseBoostTeamA.equals(other.LoseBoostTeamA))
            return false;
        if (LoseBoostTeamB == null) {
            if (other.LoseBoostTeamB != null)
                return false;
        } else if (!LoseBoostTeamB.equals(other.LoseBoostTeamB))
            return false;
        if (ScoreRateTeamA == null) {
            if (other.ScoreRateTeamA != null)
                return false;
        } else if (!ScoreRateTeamA.equals(other.ScoreRateTeamA))
            return false;
        if (ScoreRateTeamB == null) {
            if (other.ScoreRateTeamB != null)
                return false;
        } else if (!ScoreRateTeamB.equals(other.ScoreRateTeamB))
            return false;
        if (TimeBoost == null) {
            if (other.TimeBoost != null)
                return false;
        } else if (!TimeBoost.equals(other.TimeBoost))
            return false;
        if (WinBoost == null) {
            if (other.WinBoost != null)
                return false;
        } else if (!WinBoost.equals(other.WinBoost))
            return false;
        return true;
    }



    /**
     * Sets the default parameters
     *
     * @param matchFormat Actually this method don't use match format, it's here only to comply to the parent method's
     *        signature
     */

    @Override
    public void setDefaultParams(MatchFormat matchFormat) {
        setDefaultParams();
    }

    private void setDefaultParams() {
        ScoreRateTeamA = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, 1, 0, -1000, 1000, false);
        ScoreRateTeamB = new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, 1, 0, -1000, 1000, false);
        LoseBoostTeamA = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, 0, 0, -1000, 1000, false);
        LoseBoostTeamB = new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, 0, 0, -1000, 1000, false);
        WinBoost = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.NOT_SPECIFIED, 0, 0, -1000, 1000, false);
        TimeBoost = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.NOT_SPECIFIED, 0.5, 0, -1000, 1000, false);
        updateParamMap();
    }


    /**
     * (After the introduction of the concept of ParamMap)
     * <p/>
     * Update internally the paramMap. Removes unneeded parameters from the matchrunner
     */
    protected void updateParamMap() {
        super.updateParamMap();
        super.paramMap.put("ScoreRateTeamA", ScoreRateTeamA);
        super.paramMap.put("ScoreRateTeamB", ScoreRateTeamB);
        super.paramMap.put("LoseBoostTeamA", LoseBoostTeamA);
        super.paramMap.put("LoseBoostTeamB", LoseBoostTeamB);
        super.paramMap.put("WinBoost", WinBoost);
        super.paramMap.put("TimeBoost", TimeBoost);

    }

    @SuppressWarnings("unchecked")
    @Override
    public void applyParams(Map<String, Object> delegate) {
        super.applyParams(delegate);
        Map<String, Object> delegate2 = (Map<String, Object>) delegate.get("paramMap");
        this.ScoreRateTeamA =
                        new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, delegate2.get("ScoreRateTeamA"));
        this.ScoreRateTeamB =
                        new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, delegate2.get("ScoreRateTeamB"));
        this.LoseBoostTeamA =
                        new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, delegate2.get("LoseBoostTeamA"));
        this.LoseBoostTeamB =
                        new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, delegate2.get("LoseBoostTeamB"));
        this.WinBoost = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.NOT_SPECIFIED,
                        delegate2.get("WinBoost"));
        this.TimeBoost = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.NOT_SPECIFIED,
                        delegate2.get("TimeBoost"));
        updateParamMap();
    }
}

