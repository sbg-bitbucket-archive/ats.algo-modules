package ats.algo.sport.darts;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncidentResult;
import ats.algo.core.baseclasses.MatchParam;
import ats.algo.core.baseclasses.MatchParamType;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.genericsupportfunctions.Gaussian;
import ats.algo.momentum.Bayes;

public class DartMatchParams extends MatchParams {

    private static final long serialVersionUID = 1L;
    private MatchParam skillA;
    private MatchParam skillB;
    private MatchParam triplesVsDoublesA;
    private MatchParam triplesVsDoublesB;

    @JsonIgnore
    boolean doubleReqdToStartLeg;

    public DartMatchParams() {
        skillA = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, 1, 0.05, 0.5, 1.5, false);
        skillB = new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, 1, 0.05, 0.5, 1.5, false);
        triplesVsDoublesA = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, 1, 0.05, 0.5, 1.5, false);
        triplesVsDoublesB = new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, 1, 0.05, 0.5, 1.5, false);
        doubleReqdToStartLeg = false;
        updateParamMap();
    }

    public DartMatchParams(MatchFormat matchFormat) {
        this();
        doubleReqdToStartLeg = ((DartMatchFormat) matchFormat).isDoubleReqdToStart();

    }

    @JsonCreator
    private DartMatchParams(Map<String, Object> delegate) {
        super(delegate);
        applyParams(delegate);
    }

    Gaussian getSkillA() {
        return skillA.getGaussian();
    }

    void setSkillA(double skill, double stdDevn) {
        skillA.updateGaussian(skill, stdDevn);
    }

    void setSkillA(Gaussian skill) {
        skillA.setGaussian(skill);
    }

    Gaussian getSkillB() {
        return skillB.getGaussian();
    }

    void setSkillB(double skill, double stdDevn) {
        skillB.updateGaussian(skill, stdDevn);
    }

    void setSkillB(Gaussian skill) {
        skillB.setGaussian(skill);
    }

    Gaussian getTriplesVsDoublesA() {
        return triplesVsDoublesA.getGaussian();
    }

    void setTriplesVsDoublesA(double triplesVsDoubles, double stdDevn) {
        triplesVsDoublesA.updateGaussian(triplesVsDoubles, stdDevn);
    }

    void setTriplesVsDoublesA(Gaussian triplesVsDoubles) {
        triplesVsDoublesA.setGaussian(triplesVsDoubles);
    }

    Gaussian getTriplesVsDoublesB() {
        return triplesVsDoublesB.getGaussian();
    }

    void setTriplesVsDoublesB(double triplesVsDoubles, double stdDevn) {
        triplesVsDoublesB.updateGaussian(triplesVsDoubles, stdDevn);
    }

    void setTriplesVsDoublesB(Gaussian triplesVsDoubles) {
        triplesVsDoublesB.setGaussian(triplesVsDoubles);
    }

    @Override
    public void setEqualTo(MatchParams other) {
        super.setEqualTo(other);
        this.doubleReqdToStartLeg = ((DartMatchParams) other).doubleReqdToStartLeg;
    }

    private boolean startedLegAtOcheA;

    /**
     * returns the probability of playerA winning leg given the value of startedLegAtOcheA and given the supplied skills
     * array[]
     * 
     * @param x x[0] skillA, x[1] - skillB
     * @return
     */
    private double probAWinsLeg(double[] x) {
        if (x[0] < LegProbTables.minSkill || x[1] < LegProbTables.minSkill || x[0] > LegProbTables.maxSkill
                        || x[1] > LegProbTables.maxSkill)
            return 0.5;
        LegProbTablesRow legProbs;
        if (startedLegAtOcheA)
            legProbs = LegProbTables.getLegProbs(x[0], x[1], doubleReqdToStartLeg);
        else {
            legProbs = LegProbTables.getLegProbs(x[1], x[0], doubleReqdToStartLeg);
            legProbs.switchProbsAToB();
        }
        return legProbs.ProbAWins;
    }

    /**
     * private class to implement the pdf function required for the Bayesian skills update
     * 
     * @author Geoff
     * 
     */

    public void updateParamsGivenMatchIncidentResult(MatchIncidentResult matchIncidentResult) {
        /*
         * do nothing if the event is just moving from pre-match to in-play otherwise set the startedLegAtOcheA and
         * legWonByA values used by the probEventOccurs method
         */
        DartMatchIncidentResult result = (DartMatchIncidentResult) matchIncidentResult;
        boolean legWonByA;
        switch (result.getDartMatchEventOutcome()) {
            case BEGININPLAY:
            case WITHINLEG:
            case MATCHDRAWN:
            default:
                return; // do nothing - only update params at end of a leg
            case LEGWONA:
            case SETWONA:
            case MATCHWONA:
                legWonByA = true;
                break;
            case LEGWONB:
            case SETWONB:
            case MATCHWONB:
                legWonByA = false;
                break;
        }
        startedLegAtOcheA = result.isPlayerAStartedAtOche();
        /*
         * update parameter estimate using the Bayes momentum logic
         */
        Bayes bayes = new Bayes(2, (double[] z) -> probAWinsLeg(z));
        Gaussian[] params = new Gaussian[2];
        params[0] = skillA.getGaussian();
        params[1] = skillB.getGaussian();
        bayes.setPriorParams(params);
        bayes.updateSkills(legWonByA);
        params = bayes.getPosteriorParams();
        skillA.setGaussian(params[0]);
        skillB.setGaussian(params[1]);
    }

    @Override
    public void setDefaultParams(MatchFormat matchFormat) {
        Gaussian d = new Gaussian(1.0, 0.05);
        skillA.updateGaussian(d.getMean(), d.getStdDevn());
        skillB.updateGaussian(d.getMean(), d.getStdDevn());
        triplesVsDoublesA.updateGaussian(d.getMean(), d.getStdDevn());
        triplesVsDoublesB.updateGaussian(d.getMean(), d.getStdDevn());
        doubleReqdToStartLeg = false;
        updateParamMap();
    }

    protected void updateParamMap() {
        super.updateParamMap();
        super.paramMap.put("skillA", skillA);
        super.paramMap.put("skillB", skillB);
        super.paramMap.put("triplesVsDoublesA", triplesVsDoublesA);
        super.paramMap.put("triplesVsDoublesB", triplesVsDoublesB);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void applyParams(Map<String, Object> delegate2) {
        super.applyParams(delegate2);
        Map<String, Object> delegate = (Map<String, Object>) delegate2.get("paramMap");
        skillA = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, delegate.get("skillA"));
        skillB = new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, delegate.get("skillB"));
        triplesVsDoublesA =
                        new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, delegate.get("triplesVsDoublesA"));
        triplesVsDoublesB =
                        new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, delegate.get("triplesVsDoublesB"));
        updateParamMap();
        doubleReqdToStartLeg = false;
    }
}
