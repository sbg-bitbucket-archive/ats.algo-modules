package ats.algo.sport.cricket;

import ats.algo.genericsupportfunctions.Gaussian;
import ats.algo.core.baseclasses.MatchIncidentResult;
import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchParam;
import ats.algo.core.baseclasses.MatchParamType;
import ats.algo.core.baseclasses.AlgoMatchParams;
import ats.algo.core.common.TeamId;

/**
 * 
 * @author Robert
 *
 */
public class CricketMatchParams extends AlgoMatchParams {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private MatchParam teamAScoreRate;
    private MatchParam teamBScoreRate;
    private MatchParam teamARuns4Boost;
    private MatchParam teamBRuns4Boost;
    private MatchParam teamARuns6Boost;
    private MatchParam teamBRuns6Boost;

    public CricketMatchParams() {
        super();
        setDefaultParamsT20();
    }

    /*
     * getters and setters come in two flavours - setting and returning the matchParam or the underlying gaussian
     */
    void setTeamAScoreRate(Gaussian skill) {
        this.teamAScoreRate.setGaussian(skill);
    }

    void setTeamBScoreRate(Gaussian skill) {
        this.teamBScoreRate.setGaussian(skill);
    }

    Gaussian getTeamAScoreRate() {
        return teamAScoreRate.getGaussian();
    }

    Gaussian getTeamBScoreRate() {
        return teamBScoreRate.getGaussian();
    }

    void setTeamAScoreRate(double p, double stdDevn) {
        this.teamAScoreRate.updateGaussian(p, stdDevn);
    }

    void setTeamBScoreRate(double p, double stdDevn) {
        this.teamBScoreRate.updateGaussian(p, stdDevn);
    }

    Gaussian getTeamARuns4Boost() {
        return teamARuns4Boost.getGaussian();
    }

    public void setTeamARuns4Boost(MatchParam teamARuns4Boost) {
        this.teamARuns4Boost = teamARuns4Boost;
    }

    Gaussian getTeamBRuns4Boost() {
        return teamBRuns4Boost.getGaussian();
    }

    public void setTeamBRuns4Boost(MatchParam teamBRuns4Boost) {
        this.teamBRuns4Boost = teamBRuns4Boost;
    }

    Gaussian getTeamARuns6Boost() {
        return teamARuns6Boost.getGaussian();
    }

    public void setTeamARuns6Boost(MatchParam teamARuns6Boost) {
        this.teamARuns6Boost = teamARuns6Boost;
    }

    Gaussian getTeamBRuns6Boost() {
        return teamBRuns6Boost.getGaussian();
    }

    public void setTeamBRuns6Boost(MatchParam teamBRuns6Boost) {
        this.teamBRuns6Boost = teamBRuns6Boost;
    }

    @Override
    public void setDefaultParams(MatchFormat matchFormat) {
        if (((CricketMatchFormat) matchFormat).getnOversinMatch() == 20) {
            setDefaultParamsT20();
        } else if (((CricketMatchFormat) matchFormat).getnOversinMatch() == 50) {
            setDefaultParamsOneDay();
        }
    }

    protected void updateParamMap() {
        super.updateParamMap();
        super.paramMap.put("teamAScoreRate", teamAScoreRate);
        super.paramMap.put("teamBScoreRate", teamBScoreRate);
        super.paramMap.put("teamARuns4Boost", teamARuns4Boost);
        super.paramMap.put("teamBRuns4Boost", teamBRuns4Boost);
        super.paramMap.put("teamARuns6Boost", teamARuns6Boost);
        super.paramMap.put("teamBRuns6Boost", teamBRuns6Boost);
    }

    private void setDefaultParamsT20() {
        teamAScoreRate = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, 170, 15, 0, 400, false);
        teamBScoreRate = new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, 170, 15, 0, 400, false);
        teamARuns4Boost = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, 0, 1, -50, 50, false);
        teamBRuns4Boost = new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, 0, 1, -50, 50, false);
        teamARuns6Boost = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, 0, 1, -20, 20, false);
        teamBRuns6Boost = new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, 0, 1, -20, 20, false);
        updateParamMap();
    }

    private void setDefaultParamsOneDay() {
        teamAScoreRate = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, 280, 25, 0, 600, false);
        teamBScoreRate = new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, 280, 25, 0, 600, false);
        teamARuns4Boost = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, 0, 1, -50, 50, false);
        teamBRuns4Boost = new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, 0, 1, -50, 50, false);
        teamARuns6Boost = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, 0, 1, -20, 20, false);
        teamBRuns6Boost = new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, 0, 1, -20, 20, false);
        updateParamMap();
    }

    public void updateParamsGivenMatchIncidentResult(MatchIncidentResult matchIncidentResult) {
        CricketMatchIncidentResult cmir = (CricketMatchIncidentResult) matchIncidentResult;
        TeamId teamId = cmir.getTeamId();
        int runs = 0;
        int wicket = 0;
        double[] per = {0.8, 0.2};
        boolean wicketA = false;
        boolean wicketB = false;
        switch (cmir.getcricketMatchIncidentResultType()) {
            case WICKET_BOWLED:
            case WICKET_CAUGHT:
            case WICKET_LBW:
            case WICKET_OTHER:
            case WICKET_RUN_OUT:
            case WICKET_RUN_OUT_AND_RUN:
            case WICKET_STUMPED:
                if (TeamId.A == teamId) {
                    wicketA = true;
                }
                if (TeamId.B == teamId) {
                    wicketB = true;
                }
                runs = cmir.getRuns();
                wicket = cmir.getWicket();
                break;
            case PREMATCH:
            case MATCHWON:
                return; // don't apply momentum logic unless a leg has been played
            default:
                break;
        }
        if (wicketA) {
            double teamA = teamAScoreRate.getGaussian().getMean();
            if (wicket < 8)
                teamA = teamA - teamA * per[0] / 7 + runs;
            else
                teamA = teamA - teamA * per[1] / 3 + runs;
            teamAScoreRate.updateGaussian(teamA, teamAScoreRate.getGaussian().getStdDevn());
        }
        if (wicketB) {
            double teamB = teamBScoreRate.getGaussian().getMean();
            if (wicket < 8)
                teamB = teamB - teamB * per[0] / 7 + runs;
            else
                teamB = teamB - teamB * per[1] / 3 + runs;
            teamBScoreRate.updateGaussian(teamB, teamBScoreRate.getGaussian().getStdDevn());
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((teamARuns4Boost == null) ? 0 : teamARuns4Boost.hashCode());
        result = prime * result + ((teamARuns6Boost == null) ? 0 : teamARuns6Boost.hashCode());
        result = prime * result + ((teamAScoreRate == null) ? 0 : teamAScoreRate.hashCode());
        result = prime * result + ((teamBRuns4Boost == null) ? 0 : teamBRuns4Boost.hashCode());
        result = prime * result + ((teamBRuns6Boost == null) ? 0 : teamBRuns6Boost.hashCode());
        result = prime * result + ((teamBScoreRate == null) ? 0 : teamBScoreRate.hashCode());
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
        CricketMatchParams other = (CricketMatchParams) obj;
        if (teamARuns4Boost == null) {
            if (other.teamARuns4Boost != null)
                return false;
        } else if (!teamARuns4Boost.equals(other.teamARuns4Boost))
            return false;
        if (teamARuns6Boost == null) {
            if (other.teamARuns6Boost != null)
                return false;
        } else if (!teamARuns6Boost.equals(other.teamARuns6Boost))
            return false;
        if (teamAScoreRate == null) {
            if (other.teamAScoreRate != null)
                return false;
        } else if (!teamAScoreRate.equals(other.teamAScoreRate))
            return false;
        if (teamBRuns4Boost == null) {
            if (other.teamBRuns4Boost != null)
                return false;
        } else if (!teamBRuns4Boost.equals(other.teamBRuns4Boost))
            return false;
        if (teamBRuns6Boost == null) {
            if (other.teamBRuns6Boost != null)
                return false;
        } else if (!teamBRuns6Boost.equals(other.teamBRuns6Boost))
            return false;
        if (teamBScoreRate == null) {
            if (other.teamBScoreRate != null)
                return false;
        } else if (!teamBScoreRate.equals(other.teamBScoreRate))
            return false;
        return true;
    }

}
