package ats.algo.sport.snooker;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncidentResult;
import ats.algo.core.baseclasses.MatchParam;
import ats.algo.core.baseclasses.MatchParamType;
import ats.algo.core.baseclasses.AlgoMatchParams;
import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.Gaussian;
import ats.algo.momentum.Bayes;

/**
 * 
 * @author Robert
 *
 */
public class SnookerMatchParams extends AlgoMatchParams {

    private static final long serialVersionUID = 1L;
    private MatchParam onFramePctA;
    private MatchParam onCurrentFramePctA;
    private MatchParam break50A;
    private MatchParam break50B;
    private MatchParam break100A;
    private MatchParam break100B;

    public SnookerMatchParams() {
        super();
        setDefaultParams();
    }

    Gaussian getOnCurrentFramePctA() {
        return onCurrentFramePctA.getGaussian();
    }

    Gaussian getOnFramePctA() {
        return onFramePctA.getGaussian();
    }

    void setOnCurrentFramePctA(Gaussian skill) {
        this.onCurrentFramePctA.setGaussian(skill);
    }

    void setOnFramePctA(Gaussian skill) {
        this.onFramePctA.setGaussian(skill);
    }

    private boolean updateNeeded;

    /**
     * this function is passed to the momentum updating logic in . In this case it is a very simple function, since x[0]
     * is the prob that A wins a Leg
     * 
     * @param x
     * @return
     */
    private double probAWinsLegGivenX(double[] x) {
        return x[0];
    }

    public void updateParamsGivenMatchIncidentResult(MatchIncidentResult matchIncidentResult) {
        boolean playerAWonLeg = true;
        updateNeeded = false;
        TeamId teamID = ((SnookerMatchIncidentResult) matchIncidentResult).getTeamId();
        switch (((SnookerMatchIncidentResult) matchIncidentResult).getSnookerMatchIncidentResultType()) {
            case SERVEFIRST:
                onCurrentFramePctA.setGaussian(onFramePctA.getGaussian());
                return;
            case FRAMEWON:
                if (TeamId.A == teamID) {
                    playerAWonLeg = true;

                } else
                    playerAWonLeg = false;
                updateNeeded = true;
                break;
            case PREMATCH:
            case MATCHWON:
            case DRAW:
                return; // don't apply momentum logic unless a leg has been played
            default:
                break;
        }
        Bayes bayes = new Bayes(1, (double[] z) -> probAWinsLegGivenX(z));
        Gaussian[] params = new Gaussian[1];
        params[0] = onFramePctA.getGaussian();
        bayes.setPriorParams(params);
        bayes.updateSkills(playerAWonLeg);
        params = bayes.getPosteriorParams();
        if (updateNeeded) {
            onCurrentFramePctA.setGaussian(params[0]);
            onFramePctA.setGaussian(params[0]);
            params[0] = new Gaussian(1 - onCurrentFramePctA.getGaussian().getMean(),
                            onCurrentFramePctA.getGaussian().getStdDevn());
        }
    }

    public void setDefaultParams() {
        onFramePctA = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, .5, 0.02, 0, 1, true);
        onCurrentFramePctA = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED,
                        onFramePctA.getGaussian().getMean(), onFramePctA.getGaussian().getStdDevn(), 0, 1, true);
        break50A = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, .5, 0.02, 0, 1, true);
        break50B = new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, .5, 0.02, 0, 1, true);
        break100A = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, .1, 0.02, 0, 1, true);
        break100B = new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, .1, 0.02, 0, 1, true);
        updateParamMap();
    }

    @Override
    public void setDefaultParams(MatchFormat matchFormat) {
        onFramePctA = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, .5, 0.02, 0, 1, true);
        onCurrentFramePctA = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED,
                        onFramePctA.getGaussian().getMean(), onFramePctA.getGaussian().getStdDevn(), 0, 1, true);
        break50A = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, .5, 0.02, 0, 1, true);
        break50B = new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, .5, 0.02, 0, 1, true);
        break100A = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, .1, 0.02, 0, 1, true);
        break100B = new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, .1, 0.02, 0, 1, true);
        updateParamMap();

    }

    protected void updateParamMap() {
        super.updateParamMap();
        super.paramMap.put("onFramePctA", onFramePctA);
        super.paramMap.put("onCurrentFramePctA", onCurrentFramePctA);
        super.paramMap.put("break50A", break50A);
        super.paramMap.put("break50B", break50B);
        super.paramMap.put("break100A", break100A);
        super.paramMap.put("break100B", break100B);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((break100A == null) ? 0 : break100A.hashCode());
        result = prime * result + ((break100B == null) ? 0 : break100B.hashCode());
        result = prime * result + ((break50A == null) ? 0 : break50A.hashCode());
        result = prime * result + ((break50B == null) ? 0 : break50B.hashCode());
        result = prime * result + ((onCurrentFramePctA == null) ? 0 : onCurrentFramePctA.hashCode());
        result = prime * result + ((onFramePctA == null) ? 0 : onFramePctA.hashCode());
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
        SnookerMatchParams other = (SnookerMatchParams) obj;
        if (break100A == null) {
            if (other.break100A != null)
                return false;
        } else if (!break100A.equals(other.break100A))
            return false;
        if (break100B == null) {
            if (other.break100B != null)
                return false;
        } else if (!break100B.equals(other.break100B))
            return false;
        if (break50A == null) {
            if (other.break50A != null)
                return false;
        } else if (!break50A.equals(other.break50A))
            return false;
        if (break50B == null) {
            if (other.break50B != null)
                return false;
        } else if (!break50B.equals(other.break50B))
            return false;
        if (onCurrentFramePctA == null) {
            if (other.onCurrentFramePctA != null)
                return false;
        } else if (!onCurrentFramePctA.equals(other.onCurrentFramePctA))
            return false;
        if (onFramePctA == null) {
            if (other.onFramePctA != null)
                return false;
        } else if (!onFramePctA.equals(other.onFramePctA))
            return false;
        return true;
    }

    public MatchParam getBreak50A() {
        return break50A;
    }

    public void setBreak50A(MatchParam break50a) {
        break50A = break50a;
    }

    public MatchParam getBreak50B() {
        return break50B;
    }

    public void setBreak50B(MatchParam break50b) {
        break50B = break50b;
    }

    public MatchParam getBreak100A() {
        return break100A;
    }

    public void setBreak100A(MatchParam break100a) {
        break100A = break100a;
    }

    public MatchParam getBreak100B() {
        return break100B;
    }

    public void setBreak100B(MatchParam break100b) {
        break100B = break100b;
    }

}
