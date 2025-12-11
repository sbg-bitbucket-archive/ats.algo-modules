package ats.algo.sport.tennisG;


import java.util.Map;
import java.util.TreeMap;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncidentResult;
import ats.algo.core.baseclasses.MatchParam;
import ats.algo.core.baseclasses.MatchParamType;
import ats.algo.core.baseclasses.AlgoMatchParams;
import ats.algo.genericsupportfunctions.Gaussian;
import ats.algo.momentum.Bayes;
import ats.algo.sport.tennisG.TennisGMatchFormat.Sex;
import ats.algo.sport.tennisG.TennisGMatchFormat.Surface;
import ats.algo.sport.tennisG.TennisGMatchFormat.TournamentLevel;
import ats.algo.sport.tennisG.TennisGMatchIncidentResult.TennisMatchIncidentResultType;

/**
 * holds the parameters associated with this match
 * 
 * @author Geoff
 * 
 */
public class TennisGMatchParams extends AlgoMatchParams {
    private static final long serialVersionUID = 1L;

    private MatchParam onServePctA;
    private MatchParam onServePctB;

    private static final TreeMap<Sex, Map<Surface, Map<TournamentLevel, Gaussian>>> defaultParams =
                    new TreeMap<Sex, Map<Surface, Map<TournamentLevel, Gaussian>>>();

    static {
        initialiseTennisDefaultParameters();
    }

    public TennisGMatchParams() {
        super();
        onServePctA = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, .6, 0.0, .25, .75, true);
        onServePctB = new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, .6, 0.0, .25, .75, true);
        super.paramMap.put("onServePctA", onServePctA);
        super.paramMap.put("onServePctB", onServePctB);
    }


    Gaussian getOnServePctA() {
        return onServePctA.getGaussian();
    }

    void setOnServePctA(double onServePct, double stdDevn) {
        onServePctA.updateGaussian(onServePct, stdDevn);
    }

    void setOnServePctA(Gaussian skill) {
        onServePctA.setGaussian(skill);
    }

    Gaussian getOnServePctB() {
        return onServePctB.getGaussian();
    }

    void setOnServePctB(double onServePct, double stdDevn) {
        onServePctB.updateGaussian(onServePct, stdDevn);
    }

    void setOnServePctB(Gaussian skill) {
        onServePctB.setGaussian(skill);
    }



    private boolean onServeA;

    /**
     * returns the probability that A wins a point give the supplied skills
     * 
     * @param x x[0] - prob A wins point if A serving, x[1] - prob A wins point if B serving
     * @return
     */
    private double probAWinsPoint(double[] x) {
        if (onServeA) {
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
        /*
         * do nothing if the event is just moving from pre-match to in-play
         */
        if (((TennisGMatchIncidentResult) matchIncidentResult)
                        .getTennisMatchIncidentResultType() == TennisMatchIncidentResultType.FIRSTSERVERINMATCHSET)
            return;
        /*
         * update parameter estimate using the Bayes momentum logic
         */
        Bayes bayes = new Bayes(2, (double[] z) -> probAWinsPoint(z));
        onServeA = ((TennisGMatchIncidentResult) matchIncidentResult).playerAServedPoint();
        boolean playerAWonPoint = ((TennisGMatchIncidentResult) matchIncidentResult).playerAwonPoint();
        Gaussian[] params = new Gaussian[2];
        params[0] = onServePctA.getGaussian();
        params[1] = onServePctB.getGaussian();
        bayes.setPriorParams(params);
        bayes.updateSkills(playerAWonPoint);
        params = bayes.getPosteriorParams();
        onServePctA.setGaussian(params[0]);
        onServePctB.setGaussian(params[1]);
    }

    @Override
    /**
     * sets the default parameters
     */
    public void setDefaultParams(MatchFormat matchFormat) {
        TennisGMatchFormat f = (TennisGMatchFormat) matchFormat;
        Gaussian d = getDefaultParams(f.getSex(), f.getSurface(), f.getTournamentLevel());
        onServePctA.updateGaussian(d.getMean(), d.getStdDevn());
        onServePctB.updateGaussian(d.getMean(), d.getStdDevn());
    }

    private static void initialiseTennisDefaultParameters() {
        for (Sex sex : Sex.values()) {
            Map<Surface, Map<TournamentLevel, Gaussian>> mSurface =
                            new TreeMap<Surface, Map<TournamentLevel, Gaussian>>();
            for (Surface surface : Surface.values()) {
                TreeMap<TournamentLevel, Gaussian> mLevel = new TreeMap<TournamentLevel, Gaussian>();
                mSurface.put(surface, mLevel);
            }
            defaultParams.put(sex, mSurface);
        }
        addElement(Sex.MEN, Surface.CLAY, TournamentLevel.ATP, 0.614, 0.05);
        addElement(Sex.MEN, Surface.CLAY, TournamentLevel.CHALLENGER, 0.597, 0.075);
        addElement(Sex.MEN, Surface.CLAY, TournamentLevel.GRANDSLAM, 0.614, 0.04);
        addElement(Sex.MEN, Surface.CLAY, TournamentLevel.MASTERS, 0.614, 0.05);
        addElement(Sex.MEN, Surface.CLAY, TournamentLevel.ITF, 0.58, 0.1);
        addElement(Sex.MEN, Surface.GRASS, TournamentLevel.ATP, 0.659, 0.05);
        addElement(Sex.MEN, Surface.GRASS, TournamentLevel.CHALLENGER, 0.644, 0.075);
        addElement(Sex.MEN, Surface.GRASS, TournamentLevel.GRANDSLAM, 0.659, 0.04);
        addElement(Sex.MEN, Surface.GRASS, TournamentLevel.MASTERS, 0.659, 0.05);
        addElement(Sex.MEN, Surface.GRASS, TournamentLevel.ITF, 0.629, 0.1);
        addElement(Sex.MEN, Surface.HARD, TournamentLevel.ATP, 0.628, 0.05);
        addElement(Sex.MEN, Surface.HARD, TournamentLevel.CHALLENGER, 0.617, 0.075);
        addElement(Sex.MEN, Surface.HARD, TournamentLevel.GRANDSLAM, 0.628, 0.04);
        addElement(Sex.MEN, Surface.HARD, TournamentLevel.MASTERS, 0.628, 0.05);
        addElement(Sex.MEN, Surface.HARD, TournamentLevel.ITF, 0.604, 0.1);
        addElement(Sex.MEN, Surface.IHARD, TournamentLevel.ATP, 0.643, 0.05);
        addElement(Sex.MEN, Surface.IHARD, TournamentLevel.GRANDSLAM, 0.643, 0.04);
        addElement(Sex.MEN, Surface.IHARD, TournamentLevel.CHALLENGER, 0.638, 0.075);
        addElement(Sex.MEN, Surface.IHARD, TournamentLevel.MASTERS, 0.643, 0.05);
        addElement(Sex.MEN, Surface.IHARD, TournamentLevel.ITF, 0.633, 0.1);
        addElement(Sex.WOMEN, Surface.CLAY, TournamentLevel.GRANDSLAM, 0.55, 0.05);
        addElement(Sex.WOMEN, Surface.CLAY, TournamentLevel.ITF, 0.533, 0.11);
        addElement(Sex.WOMEN, Surface.CLAY, TournamentLevel.PREMIER, 0.55, 0.06);
        addElement(Sex.WOMEN, Surface.CLAY, TournamentLevel.WTA, 0.55, 0.06);
        addElement(Sex.WOMEN, Surface.CLAY, TournamentLevel.CHALLENGER, 0.541, 0.08);
        addElement(Sex.WOMEN, Surface.GRASS, TournamentLevel.GRANDSLAM, 0.586, 0.05);
        addElement(Sex.WOMEN, Surface.GRASS, TournamentLevel.ITF, 0.569, 0.11);
        addElement(Sex.WOMEN, Surface.GRASS, TournamentLevel.WTA, 0.586, 0.06);
        addElement(Sex.WOMEN, Surface.GRASS, TournamentLevel.PREMIER, 0.586, 0.06);
        addElement(Sex.WOMEN, Surface.GRASS, TournamentLevel.CHALLENGER, 0.574, 0.08);
        addElement(Sex.WOMEN, Surface.HARD, TournamentLevel.GRANDSLAM, 0.554, 0.05);
        addElement(Sex.WOMEN, Surface.HARD, TournamentLevel.ITF, 0.547, 0.11);
        addElement(Sex.WOMEN, Surface.HARD, TournamentLevel.PREMIER, 0.555, 0.06);
        addElement(Sex.WOMEN, Surface.HARD, TournamentLevel.WTA, 0.555, 0.06);
        addElement(Sex.WOMEN, Surface.HARD, TournamentLevel.CHALLENGER, 0.551, 0.08);
        addElement(Sex.WOMEN, Surface.IHARD, TournamentLevel.ITF, 0.553, 0.11);
        addElement(Sex.WOMEN, Surface.IHARD, TournamentLevel.PREMIER, 0.563, 0.06);
        addElement(Sex.WOMEN, Surface.IHARD, TournamentLevel.WTA, 0.563, 0.06);
        addElement(Sex.WOMEN, Surface.IHARD, TournamentLevel.GRANDSLAM, 0.563, 0.05);
        addElement(Sex.WOMEN, Surface.IHARD, TournamentLevel.CHALLENGER, 0.558, 0.08);
        addElement(Sex.MEN, Surface.CARPET, TournamentLevel.ATP, 0.668, 0.05);
        addElement(Sex.MEN, Surface.CARPET, TournamentLevel.GRANDSLAM, 0.668, 0.04);
        addElement(Sex.MEN, Surface.CARPET, TournamentLevel.CHALLENGER, 0.653, 0.075);
        addElement(Sex.MEN, Surface.CARPET, TournamentLevel.MASTERS, 0.668, 0.05);
        addElement(Sex.MEN, Surface.CARPET, TournamentLevel.ITF, 0.638, 0.1);
        addElement(Sex.WOMEN, Surface.CARPET, TournamentLevel.WTA, 0.586, 0.06);
        addElement(Sex.WOMEN, Surface.CARPET, TournamentLevel.GRANDSLAM, 0.586, 0.05);
        addElement(Sex.WOMEN, Surface.CARPET, TournamentLevel.CHALLENGER, 0.57, 0.08);
        addElement(Sex.WOMEN, Surface.CARPET, TournamentLevel.PREMIER, 0.586, 0.06);
        addElement(Sex.WOMEN, Surface.CARPET, TournamentLevel.ITF, 0.556, 0.11);
    }

    private static void addElement(Sex sex, Surface surface, TournamentLevel level, double skill, double stdDevn) {
        Gaussian element = new Gaussian(skill, stdDevn);
        defaultParams.get(sex).get(surface).put(level, element);
    }

    /**
     * returns the default params - always returns something even if supplied params are not consistent
     * 
     * @param sex
     * @param surface
     * @param level
     * @return
     */
    public Gaussian getDefaultParams(Sex sex, Surface surface, TournamentLevel level) {
        Gaussian params;
        params = defaultParams.get(sex).get(surface).get(level);
        if (params == null) {
            if (sex == Sex.MEN)
                params = defaultParams.get(Sex.MEN).get(Surface.HARD).get(TournamentLevel.CHALLENGER);
            else
                params = defaultParams.get(Sex.WOMEN).get(Surface.HARD).get(TournamentLevel.ITF);
        }
        return params;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (onServeA ? 1231 : 1237);
        result = prime * result + ((onServePctA == null) ? 0 : onServePctA.hashCode());
        result = prime * result + ((onServePctB == null) ? 0 : onServePctB.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TennisGMatchParams other = (TennisGMatchParams) obj;
        if (onServeA != other.onServeA)
            return false;
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


}
