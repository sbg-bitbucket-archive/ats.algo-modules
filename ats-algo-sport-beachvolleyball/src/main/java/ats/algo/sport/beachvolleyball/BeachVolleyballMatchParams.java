package ats.algo.sport.beachvolleyball;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncidentResult;
import ats.algo.core.baseclasses.MatchParam;
import ats.algo.core.baseclasses.MatchParamType;
import ats.algo.core.baseclasses.AlgoMatchParams;
import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.Gaussian;
import ats.algo.momentum.Bayes;

public class BeachVolleyballMatchParams extends AlgoMatchParams {

    private static final long serialVersionUID = 1L;
    private MatchParam onServePctA;
    private MatchParam onServePctB;

    public BeachVolleyballMatchParams() {
        super();
        setDefaultParams();
    }

    Gaussian getOnServePctA() {
        return onServePctA.getGaussian();
    }

    Gaussian getOnServePctB() {
        return onServePctB.getGaussian();
    }

    void setOnServePctA(Gaussian skill) {
        this.onServePctA.setGaussian(skill);
    }

    void setOnServePctB(Gaussian skill) {
        this.onServePctB.setGaussian(skill);
    }

    void setOnServePctA(double p, double stdDevn) {
        this.onServePctA.updateGaussian(p, stdDevn);
    }

    void setOnServePctB(double p, double stdDevn) {
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
        TeamId teamId = ((BeachVolleyballMatchIncidentResult) matchIncidentResult).getTeamId();
        switch (((BeachVolleyballMatchIncidentResult) matchIncidentResult).getVolleyballMatchIncidentResultType()) {
            case POINTWON:
                if (TeamId.A == teamId)
                    playerAWonLeg = true;
                else
                    playerAWonLeg = false;
                break;
            case PREMATCH:
            case SERVEFIRST:
            case MATCHWON:
                return; // don't apply momentum logic unless a leg has been played
            default:
                break;
        }
        serveA = ((BeachVolleyballMatchIncidentResult) matchIncidentResult).isPlayerAServedPoint();
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

}
