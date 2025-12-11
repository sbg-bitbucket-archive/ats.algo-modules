package ats.algo.sport.snooker;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncidentResult;
import ats.algo.core.baseclasses.MatchParam;
import ats.algo.core.baseclasses.MatchParamType;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.Gaussian;
import ats.algo.momentum.Bayes;

/**
 * 
 * @author Robert
 *
 */
public class SnookerMatchParams extends MatchParams {

    private static final long serialVersionUID = 1L;
    private MatchParam onFramePctA;
    private MatchParam onCurrentFramePctA;

    public SnookerMatchParams() {
        setDefaultParams();
    }

    @JsonCreator
    private SnookerMatchParams(Map<String, Object> delegate) {
        super(delegate);
        applyParams(delegate);
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
        updateParamMap();
    }

    @Override
    public void setDefaultParams(MatchFormat matchFormat) {
        onFramePctA = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, .5, 0.02, 0, 1, true);
        onCurrentFramePctA = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED,
                        onFramePctA.getGaussian().getMean(), onFramePctA.getGaussian().getStdDevn(), 0, 1, true);
        updateParamMap();

    }

    protected void updateParamMap() {
        super.updateParamMap();
        super.paramMap.put("onFramePctA", onFramePctA);
        super.paramMap.put("onCurrentFramePctA", onCurrentFramePctA);

    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
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

    @SuppressWarnings("unchecked")
    @Override
    public void applyParams(Map<String, Object> delegate2) {
        super.applyParams(delegate2);
        Map<String, Object> delegate = (Map<String, Object>) delegate2.get("paramMap");
        onFramePctA = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, delegate.get("onFramePctA"));
        onCurrentFramePctA =
                        new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, delegate.get("onCurrentFramePctA"));
        updateParamMap();
    }
}
