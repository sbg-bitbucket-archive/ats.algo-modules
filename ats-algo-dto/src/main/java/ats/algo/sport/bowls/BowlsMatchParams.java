package ats.algo.sport.bowls;

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

public class BowlsMatchParams extends MatchParams {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private MatchParam onEndPctA;
    private MatchParam onEndPctB;
    private MatchParam onCurrentEndPctA;
    private MatchParam onCurrentEndPctB;

    public BowlsMatchParams() {
        setDefaultParams();
    }

    @JsonCreator
    private BowlsMatchParams(Map<String, Object> delegate) {
        super(delegate);
        applyParams(delegate);
    }

    Gaussian getOnEndPctA() {
        return onEndPctA.getGaussian();
    }

    Gaussian getOnEndPctB() {
        return onEndPctB.getGaussian();
    }

    Gaussian getOnCurrentEndPctA() {
        return onCurrentEndPctA.getGaussian();
    }

    Gaussian getOnCurrentEndPctB() {
        return onCurrentEndPctB.getGaussian();
    }

    void setOnEndPctA(Gaussian skill) {
        this.onEndPctA.setGaussian(skill);
    }

    void setOnEndPctB(Gaussian skill) {
        this.onEndPctB.setGaussian(skill);
    }

    void setOnCurrentEndPctA(Gaussian skill) {
        this.onCurrentEndPctA.setGaussian(skill);
    }

    void setOnCurrentEndPctB(Gaussian skill) {
        this.onCurrentEndPctB.setGaussian(skill);
    }

    void setOnEndPctA(double p, double stdDevn) {
        this.onEndPctA.updateGaussian(p, stdDevn);
    }

    void setOnEndPctB(double p, double stdDevn) {
        this.onEndPctB.updateGaussian(p, stdDevn);
    }

    void setOnCurrentEndPctA(double p, double stdDevn) {
        this.onCurrentEndPctA.updateGaussian(p, stdDevn);
    }

    void setOnCurrentEndPctB(double p, double stdDevn) {
        this.onCurrentEndPctB.updateGaussian(p, stdDevn);
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

        if (!serveA) {
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
        TeamId teamId = ((BowlsMatchIncidentResult) matchIncidentResult).getTeamId();
        switch (((BowlsMatchIncidentResult) matchIncidentResult).getBowlsMatchIncidentResultType()) {
            case ENDWON1:
            case ENDWON2:
            case ENDWON3:
            case ENDWON4:
                if (TeamId.A == teamId)
                    playerAWonLeg = true;
                else
                    playerAWonLeg = false;
                break;

            case SERVEFIRST:
                if (TeamId.A == teamId) {
                    onCurrentEndPctA = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED,
                                    onEndPctA.getGaussian().getMean(), 0.0, 0, 1, true);
                    onCurrentEndPctB = new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED,
                                    1 - onEndPctA.getGaussian().getMean(), 0.0, 0, 1, true);
                } else {
                    onCurrentEndPctA = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED,
                                    1 - onEndPctB.getGaussian().getMean(), 0.0, 0, 1, true);
                    onCurrentEndPctB = new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED,
                                    onEndPctB.getGaussian().getMean(), 0.0, 0, 1, true);
                }
                return;
            case PREMATCH:
            case SERVENOW:
            case MATCHWON:
                return; // don't apply momentum logic unless a leg has been played
            default:
                break;
        }
        serveA = ((BowlsMatchIncidentResult) matchIncidentResult).isPlayerAServedEnd();
        Bayes bayes = new Bayes(2, (double[] z) -> probAWinsLegGivenX(z));
        Gaussian[] params = new Gaussian[2];
        params[0] = onEndPctA.getGaussian();
        params[1] = onEndPctB.getGaussian();

        bayes.setPriorParams(params);
        bayes.updateSkills(playerAWonLeg);
        params = bayes.getPosteriorParams();
        onEndPctA.setGaussian(params[0]);
        onEndPctB.setGaussian(params[1]);
        if (serveA) {
            onCurrentEndPctA = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, params[0].getMean(), 0.0, 0,
                            1, true);
            onCurrentEndPctB = new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, 1 - params[0].getMean(), 0.0,
                            0, 1, true);
        } else {
            onCurrentEndPctA = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, 1 - params[1].getMean(), 0.0,
                            0, 1, true);
            onCurrentEndPctB = new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, params[1].getMean(), 0.0, 0,
                            1, true);
        }
    }

    @Override
    public void setDefaultParams(MatchFormat matchFormat) {
        onEndPctA = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, .55, 0.05, 0, 1, true);
        onCurrentEndPctA = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, .55, 0.05, 0, 1, true);
        onEndPctB = new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, .45, 0.05, 0, 1, true);
        onCurrentEndPctB = new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, .45, 0.05, 0, 1, true);
        updateParamMap();
    }

    public void setDefaultParams() {
        onEndPctA = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, .55, 0.05, 0, 1, true);
        onCurrentEndPctA = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, .55, 0.05, 0, 1, true);
        onEndPctB = new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, .45, 0.05, 0, 1, true);
        onCurrentEndPctB = new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, .45, 0.05, 0, 1, true);
        updateParamMap();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((onCurrentEndPctA == null) ? 0 : onCurrentEndPctA.hashCode());
        result = prime * result + ((onCurrentEndPctB == null) ? 0 : onCurrentEndPctB.hashCode());
        result = prime * result + ((onEndPctA == null) ? 0 : onEndPctA.hashCode());
        result = prime * result + ((onEndPctB == null) ? 0 : onEndPctB.hashCode());
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
        BowlsMatchParams other = (BowlsMatchParams) obj;
        if (onCurrentEndPctA == null) {
            if (other.onCurrentEndPctA != null)
                return false;
        } else if (!onCurrentEndPctA.equals(other.onCurrentEndPctA))
            return false;
        if (onCurrentEndPctB == null) {
            if (other.onCurrentEndPctB != null)
                return false;
        } else if (!onCurrentEndPctB.equals(other.onCurrentEndPctB))
            return false;
        if (onEndPctA == null) {
            if (other.onEndPctA != null)
                return false;
        } else if (!onEndPctA.equals(other.onEndPctA))
            return false;
        if (onEndPctB == null) {
            if (other.onEndPctB != null)
                return false;
        } else if (!onEndPctB.equals(other.onEndPctB))
            return false;
        return true;
    }

    protected void updateParamMap() {
        super.updateParamMap();
        super.paramMap.put("onEndPctA", onEndPctA);
        super.paramMap.put("onEndPctB", onEndPctB);
        super.paramMap.put("onCurrentEndPctA", onCurrentEndPctA);
        super.paramMap.put("onCurrentEndPctB", onCurrentEndPctB);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void applyParams(Map<String, Object> delegate2) {
        super.applyParams(delegate2);
        Map<String, Object> delegate = (Map<String, Object>) delegate2.get("paramMap");
        onEndPctA = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, delegate.get("onEndPctA"));
        onEndPctB = new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, delegate.get("onEndPctB"));
        onCurrentEndPctA =
                        new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, delegate.get("onCurrentEndPctA"));
        onCurrentEndPctB =
                        new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, delegate.get("onCurrentEndPctB"));
        updateParamMap();
    }
}
