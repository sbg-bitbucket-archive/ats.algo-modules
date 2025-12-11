package ats.algo.sport.volleyball;

import java.util.Map;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncidentResult;
import ats.algo.core.baseclasses.MatchParam;
import ats.algo.core.baseclasses.MatchParamType;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.Gaussian;
import ats.algo.momentum.Bayes;

public class VolleyballMatchParams extends MatchParams {

    private static final long serialVersionUID = 1L;

    private MatchParam onServePctA;
    private MatchParam onServePctB;

    public VolleyballMatchParams() {
        setDefaultParams();
    }

    @JsonCreator
    private VolleyballMatchParams(Map<String, Object> delegate) {
        super(delegate);
        applyParams(delegate);
    }

    @JsonIgnore
    public Gaussian getOnServePctA() {
        return onServePctA.getGaussian();
    }

    @JsonIgnore
    public Gaussian getOnServePctB() {
        return onServePctB.getGaussian();
    }

    @JsonIgnore
    public void setOnServePctA(Gaussian skill) {
        this.onServePctA.setGaussian(skill);
    }

    @JsonIgnore
    public void setOnServePctB(Gaussian skill) {
        this.onServePctB.setGaussian(skill);
    }

    @JsonIgnore
    public void setOnServePctA(double p, double stdDevn) {
        this.onServePctA.updateGaussian(p, stdDevn);
    }

    @JsonIgnore
    public void setOnServePctB(double p, double stdDevn) {
        this.onServePctB.updateGaussian(p, stdDevn);
    }

    private boolean serveA;

    /**
     * this function is passed to the momentum updating logic in . In this case it is a very simple function, since x[0]
     * is the prob that A wins a Leg
     * 
     * @param x
     * @return
     */
    private double probAWinsLegGivenX(double[] x) {

        if (serveA) {
            if (x[0] < 0)
                return 0;
            if (x[0] > 1)
                return 1;
            return x[0];
        } else {
            if (x[1] < 0)
                return 1;
            if (x[1] > 1)
                return 0;
            return 1 - x[1];
        }
    }

    public void updateParamsGivenMatchIncidentResult(MatchIncidentResult matchIncidentResult) {
        boolean playerAWonLeg = true;
        TeamId teamId = ((VolleyballMatchIncidentResult) matchIncidentResult).getTeamId();
        switch (((VolleyballMatchIncidentResult) matchIncidentResult).getVolleyballMatchIncidentResultType()) {
            case POINTWON:
                if (TeamId.A == teamId)
                    playerAWonLeg = true;
                else
                    playerAWonLeg = false;
                break;
            case PREMATCH:
            case SERVE:
            case SERVEFINALSET:
            case MATCHWON:
                return; // don't apply momentum logic unless a leg has been played
            default:
                break;
        }
        serveA = ((VolleyballMatchIncidentResult) matchIncidentResult).isPlayerAServedPoint();
        Bayes bayes = new Bayes(2, (double[] z) -> probAWinsLegGivenX(z));
        Gaussian[] params = new Gaussian[2];
        params[0] = onServePctA.getGaussian();
        params[1] = onServePctB.getGaussian();

        bayes.setPriorParams(params);
        bayes.updateSkills(playerAWonLeg);
        params = bayes.getPosteriorParams();
        onServePctA.setGaussian(params[0]);
        onServePctB.setGaussian(params[1]);
    }

    @Override
    public void setDefaultParams(MatchFormat matchFormat) {
        onServePctA = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, .35, 0.04, 0, 1, true);
        onServePctB = new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, .35, 0.04, 0, 1, true);
        updateParamMap();
    }

    public void setDefaultParams() {
        onServePctA = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, .35, 0.04, 0, 1, true);
        onServePctB = new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, .35, 0.04, 0, 1, true);
        updateParamMap();
    }

    protected void updateParamMap() {
        super.updateParamMap();
        super.paramMap.put("onServePctA", onServePctA);
        super.paramMap.put("onServePctB", onServePctB);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((onServePctA == null) ? 0 : onServePctA.hashCode());
        result = prime * result + ((onServePctB == null) ? 0 : onServePctB.hashCode());
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
        VolleyballMatchParams other = (VolleyballMatchParams) obj;
        if (onServePctA == null) {
            if (other.onServePctA != null)
                return false;
        } else if (!onServePctA.equals(other.onServePctA))
            return false;
        if (onServePctB == null) {
            if (other.onServePctB != null)
                return false;
        } else if (!onServePctB.equals(other.onServePctB))
            return false;
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void applyParams(Map<String, Object> delegate2) {
        super.applyParams(delegate2);
        Map<String, Object> delegate = (Map<String, Object>) delegate2.get("paramMap");
        onServePctA = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, delegate.get("onServePctA"));
        onServePctB = new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, delegate.get("onServePctB"));
        updateParamMap();
    }

    @Override
    public String toString() {
        return "VolleyballMatchParams [onServePctA=" + onServePctA + ", onServePctB=" + onServePctB + "]";
    }

}
