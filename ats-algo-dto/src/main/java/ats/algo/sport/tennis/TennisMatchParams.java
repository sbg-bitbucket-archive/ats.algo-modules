package ats.algo.sport.tennis;

import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchEngineSavedState;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncidentResult;
import ats.algo.core.baseclasses.MatchParam;
import ats.algo.core.baseclasses.MatchParamType;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.genericsupportfunctions.Gaussian;
import ats.algo.sport.tennis.TennisMatchFormat.Sex;
import ats.algo.sport.tennis.TennisMatchFormat.Surface;
import ats.algo.sport.tennis.TennisMatchFormat.TournamentLevel;
import ats.algo.sport.tennis.TennisMatchIncidentResult.TennisMatchIncidentResultType;

/**
 * holds the parameters associated with this match
 *
 * Default setting allocates parameters for four possible players (Tennis Double). For tennis single case, Player A2 and
 * Player B2 will not play their parts in calculations, Player A1 and Player B1 are assumed to serve through out the
 * whole match.
 *
 * @author Geoff
 *
 */

public class TennisMatchParams extends MatchParams {
    private static final long serialVersionUID = 1L;

    private MatchParam onServePctA1;
    private MatchParam onServePctB1;
    private MatchParam onServePctA2;
    private MatchParam onServePctB2;


    private static final TreeMap<Sex, Map<Surface, Map<TournamentLevel, Gaussian>>> defaultParams =
                    new TreeMap<Sex, Map<Surface, Map<TournamentLevel, Gaussian>>>();

    static {
        initialiseTennisDefaultParameters();
    }

    public TennisMatchParams() {
        super();
        eventId = 0L;

        onServePctA1 = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, 0.65, 0.05, 0.40, 0.99, true);
        onServePctB1 = new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, 0.65, 0.05, 0.40, 0.99, true);
        onServePctA2 = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, 0.65, 0.05, 0.40, 0.99, true);
        onServePctB2 = new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, 0.65, 0.05, 0.40, 0.99, true);
        updateParamMapForMatchFormat(true);

    }

    protected void updateParamMapForMatchFormat(boolean doubles) {
        /*
         * remove any old params from the map then put the correct set
         */
        super.updateParamMap();
        if (doubles) {
            super.paramMap.put("onServePctA1", onServePctA1);
            super.paramMap.put("onServePctA2", onServePctA2);
            super.paramMap.put("onServePctB1", onServePctB1);
            super.paramMap.put("onServePctB2", onServePctB2);
        } else {
            super.paramMap.put("onServePctA", onServePctA1);
            super.paramMap.put("onServePctB", onServePctB1);
        }

    }


    @JsonCreator
    public TennisMatchParams(Map<String, Object> delegate) {
        super(delegate);
        applyParams(delegate);

    }

    @JsonIgnore
    public Gaussian getOnServePct(PlayerId playerId) {
        Gaussian onServePct = null;
        switch (playerId) {
            case A1:
                onServePct = getOnServePctA1();
                break;
            case A2:
                onServePct = getOnServePctA2();
                break;
            case B1:
                onServePct = getOnServePctB1();
                break;
            case B2:
                onServePct = getOnServePctB2();
                break;
            default:
                break;
        }
        return onServePct;

    }

    @JsonIgnore
    public void setDoublesMatch(boolean isDoublesMatch) {
        updateParamMapForMatchFormat(isDoublesMatch);
    }

    @JsonIgnore
    public Gaussian getOnServePctA1() {
        return onServePctA1.getGaussian();
    }

    @JsonIgnore
    public void setOnServePctA1(double onServePct, double stdDevn) {
        this.onServePctA1.updateGaussian(onServePct, stdDevn);
    }

    @JsonIgnore
    public void setOnServePctA1(Gaussian skill) {
        this.onServePctA1.setGaussian(skill);
    }

    @JsonIgnore
    public Gaussian getOnServePctB1() {
        return onServePctB1.getGaussian();
    }

    @JsonIgnore
    public void setOnServePctB1(double onServePct, double stdDevn) {
        this.onServePctB1.updateGaussian(onServePct, stdDevn);
    }

    @JsonIgnore
    public void setOnServePctB1(Gaussian skill) {
        this.onServePctB1.setGaussian(skill);
    }

    @JsonIgnore
    public Gaussian getOnServePctA2() {
        return onServePctA2.getGaussian();
    }

    @JsonIgnore
    public void setOnServePctA2(double onServePct, double stdDevn) {
        this.onServePctA2.updateGaussian(onServePct, stdDevn);
    }

    @JsonIgnore
    public void setOnServePctA2(Gaussian skill) {
        this.onServePctA2.setGaussian(skill);
    }

    @JsonIgnore
    public Gaussian getOnServePctB2() {
        return onServePctB2.getGaussian();
    }

    @JsonIgnore
    public void setOnServePctB2(double onServePct, double stdDevn) {
        this.onServePctB2.updateGaussian(onServePct, stdDevn);
    }

    @JsonIgnore
    public void setOnServePctB2(Gaussian skill) {
        this.onServePctB2.setGaussian(skill);
    }

    @Override
    public void setEqualTo(MatchParams matchParams) {
        boolean doubles = !matchParams.getParamMap().containsKey("onServePctA");
        this.updateParamMapForMatchFormat(doubles);
        super.setEqualTo(matchParams);
    }

    public void applyMomentumLogic(MatchState matchState, MatchIncidentResult matchIncidentResult,
                    MatchEngineSavedState matchEngineSavedState) {

        TennisMatchIncidentResult incidentResult = (TennisMatchIncidentResult) matchIncidentResult;
        TennisMatchIncidentResultType resultType = incidentResult.getTennisMatchIncidentResultType();
        TennisMatchParams lastSeenMatchParams =
                        ((TennisMatchEngineSavedState) matchEngineSavedState).getLastSeenTennisMatchParams();
        TennisMatchParams intraGameMatchParams =
                        ((TennisMatchEngineSavedState) matchEngineSavedState).getIntraGameUpdatedTennisMatchParams();
        if (!this.equals(lastSeenMatchParams)) {
            /*
             * the params have been updated via either a param find or manual param change, so throw away pending state
             */
            intraGameMatchParams.setEqualTo(this);
            lastSeenMatchParams.setEqualTo(this);
        }

        /*
         * do nothing if the incident is just moving from pre-match to in-play
         */
        if ((resultType == TennisMatchIncidentResultType.FIRSTSERVERINMATCHSET)) {
            intraGameMatchParams.setEqualTo(this);
            lastSeenMatchParams.setEqualTo(this);
            return;
        }
        boolean onServeTeamA = incidentResult.isTeamAServedPoint();
        boolean onServePlayer1 = incidentResult.getPlayerIdServedPoint() == 1;
        boolean teamAWonPoint = incidentResult.isTeamAWonPoint();

        int pointA = ((TennisMatchState) matchState).getPointsA();
        int pointB = ((TennisMatchState) matchState).getPointsB();
        int gamesA = ((TennisMatchState) matchState).getGamesA();
        int gamesB = ((TennisMatchState) matchState).getGamesB();
        int setA = ((TennisMatchState) matchState).getSetsA();
        int setB = ((TennisMatchState) matchState).getSetsB();

        boolean keyPoint = ((pointA - pointB == 1) && (teamAWonPoint) && (pointB != 0))
                        || ((pointB - pointA == 1) && (!teamAWonPoint) && (pointA != 0));
        keyPoint = (keyPoint) || ((pointA == pointB) && (pointA != 0));
        keyPoint = (keyPoint) || ((pointA == pointB) && (pointA == 0) && (setA + setB + gamesA + gamesB != 0));

        keyPoint = keyPoint && (pointA + pointB > 4); // only after 5th point
        if ((onServeTeamA) && (onServePlayer1)) {
            updateParam(intraGameMatchParams.getOnServePctA1(), teamAWonPoint, keyPoint);
        }
        if ((onServeTeamA) && (!onServePlayer1)) {
            updateParam(intraGameMatchParams.getOnServePctA2(), teamAWonPoint, keyPoint);
        }
        if ((!onServeTeamA) && (onServePlayer1)) {
            updateParam(intraGameMatchParams.getOnServePctB1(), !teamAWonPoint, keyPoint);
        }
        if ((!onServeTeamA) && (!onServePlayer1)) {
            updateParam(intraGameMatchParams.getOnServePctB2(), !teamAWonPoint, keyPoint);
        }

        setEqualTo(intraGameMatchParams);
        lastSeenMatchParams.setEqualTo(this);

        // /*
        // * update parameter estimate for the player currently on serve using the Bayes momentum logic
        // */
        // if (onServeTeamA && onServePlayer1)
        // updateParam(intraGameMatchParams.getOnServePctA1(), teamAWonPoint);
        // if (onServeTeamA && !onServePlayer1)
        // updateParam(intraGameMatchParams.getOnServePctA2(), teamAWonPoint);
        // if (!onServeTeamA && onServePlayer1)
        // updateParam(intraGameMatchParams.getOnServePctB1(), !teamAWonPoint);
        // if (!onServeTeamA && !onServePlayer1)
        // updateParam(intraGameMatchParams.getOnServePctB2(), !teamAWonPoint);
        //
        // /*
        // * if end of a game then update the onServePcts with the accumulated changes
        // */
        // if (resultType == TennisMatchIncidentResultType.GAMEWONA || resultType ==
        // TennisMatchIncidentResultType.GAMEWONB
        // || resultType == TennisMatchIncidentResultType.SETWON) {
        // this.setEqualTo(intraGameMatchParams);
        // lastSeenMatchParams.setEqualTo(this);
        // }

    }

    @Override
    public void setFromMap(Map<String, MatchParam> map) {
        /*
         * need to set singles or doubles before setting from the map
         */
        boolean isDoubles = (map.get("onServePctA1") != null);
        this.setDoublesMatch(isDoubles);
        super.setFromMap(map);
    }

    /**
     * updates the skill estimate using the bayesian momentum logic. Only the mean is changed, not the sd
     *
     * @param onSrvPct - updated following execution of the method
     * @param playerWonPoint - true if player on serve won the point
     */
    // private void updateParam(Gaussian gaussian, boolean playerWonPoint) {
    // double m = gaussian.getMean();
    // double sd = gaussian.getStdDevn();
    // sd = 0.05;
    // if (playerWonPoint)
    // m += sd * sd / m;
    // else
    // m -= sd * sd / (1 - m);
    // gaussian.setMean(m);
    //
    // }
    private void updateParam(Gaussian gaussian, boolean playerWonPoint, boolean keyPoint) {
        double m = gaussian.getMean();
        double sd = gaussian.getStdDevn();
        if (keyPoint) {
            sd = 0.05D;
            if (playerWonPoint) {
                m += sd * sd / m;
            } else {
                m -= sd * sd / (1.0D - m);
            }
        }
        gaussian.setMean(m);
    }

    private void setDefaultParams(Gaussian d, boolean doublesMatch) {
        onServePctA1.updateGaussian(d.getMean(), d.getStdDevn());
        onServePctB1.updateGaussian(d.getMean(), d.getStdDevn());
        onServePctA2.updateGaussian(d.getMean(), d.getStdDevn());
        onServePctB2.updateGaussian(d.getMean(), d.getStdDevn());
        this.updateParamMapForMatchFormat(doublesMatch);
    }

    @Override
    /**
     * sets the default parameters
     */
    public void setDefaultParams(MatchFormat matchFormat) {
        TennisMatchFormat f = (TennisMatchFormat) matchFormat;
        Gaussian d = getDefaultParams(f.getSex(), f.getSurface(), f.getTournamentLevel());
        setDefaultParams(d, f.isDoublesMatch());
        // setTraderControls(f);
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
        addElement(Sex.MEN, Surface.CLAY, TournamentLevel.ATP, 0.65, 0.05);
        addElement(Sex.MEN, Surface.CLAY, TournamentLevel.CHALLENGER, 0.60, 0.075);
        addElement(Sex.MEN, Surface.CLAY, TournamentLevel.GRANDSLAM, 0.65, 0.04);
        addElement(Sex.MEN, Surface.CLAY, TournamentLevel.MASTERS, 0.65, 0.05);
        addElement(Sex.MEN, Surface.CLAY, TournamentLevel.ITF, 0.57, 0.1);
        addElement(Sex.MEN, Surface.GRASS, TournamentLevel.ATP, 0.65, 0.05);
        addElement(Sex.MEN, Surface.GRASS, TournamentLevel.CHALLENGER, 0.60, 0.075);
        addElement(Sex.MEN, Surface.GRASS, TournamentLevel.GRANDSLAM, 0.65, 0.04);
        addElement(Sex.MEN, Surface.GRASS, TournamentLevel.MASTERS, 0.65, 0.05);
        addElement(Sex.MEN, Surface.GRASS, TournamentLevel.ITF, 0.57, 0.1);
        addElement(Sex.MEN, Surface.HARD, TournamentLevel.ATP, 0.65, 0.05);
        addElement(Sex.MEN, Surface.HARD, TournamentLevel.CHALLENGER, 0.60, 0.075);
        addElement(Sex.MEN, Surface.HARD, TournamentLevel.GRANDSLAM, 0.65, 0.04);
        addElement(Sex.MEN, Surface.HARD, TournamentLevel.MASTERS, 0.65, 0.05);
        addElement(Sex.MEN, Surface.HARD, TournamentLevel.ITF, 0.57, 0.1);
        addElement(Sex.MEN, Surface.IHARD, TournamentLevel.ATP, 0.65, 0.05);
        addElement(Sex.MEN, Surface.IHARD, TournamentLevel.GRANDSLAM, 0.65, 0.04);
        addElement(Sex.MEN, Surface.IHARD, TournamentLevel.CHALLENGER, 0.60, 0.075);
        addElement(Sex.MEN, Surface.IHARD, TournamentLevel.MASTERS, 0.65, 0.05);
        addElement(Sex.MEN, Surface.IHARD, TournamentLevel.ITF, 0.57, 0.1);
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
        addElement(Sex.MEN, Surface.CARPET, TournamentLevel.ATP, 0.65, 0.05);
        addElement(Sex.MEN, Surface.CARPET, TournamentLevel.GRANDSLAM, 0.65, 0.04);
        addElement(Sex.MEN, Surface.CARPET, TournamentLevel.CHALLENGER, 0.60, 0.075);
        addElement(Sex.MEN, Surface.CARPET, TournamentLevel.MASTERS, 0.65, 0.05);
        addElement(Sex.MEN, Surface.CARPET, TournamentLevel.ITF, 0.57, 0.1);
        addElement(Sex.WOMEN, Surface.CARPET, TournamentLevel.WTA, 0.586, 0.06);
        addElement(Sex.WOMEN, Surface.CARPET, TournamentLevel.GRANDSLAM, 0.586, 0.05);
        addElement(Sex.WOMEN, Surface.CARPET, TournamentLevel.CHALLENGER, 0.57, 0.08);
        addElement(Sex.WOMEN, Surface.CARPET, TournamentLevel.PREMIER, 0.586, 0.06);
        addElement(Sex.WOMEN, Surface.CARPET, TournamentLevel.ITF, 0.556, 0.11);


        addElement(Sex.MEN, Surface.CLAY, TournamentLevel.ATP_QUALIFIER, 0.65, 0.05);
        addElement(Sex.MEN, Surface.CLAY, TournamentLevel.CHALLENGER_QUALIFIER, 0.60, 0.075);
        addElement(Sex.MEN, Surface.CLAY, TournamentLevel.ITF_QUALIFIER, 0.57, 0.1);
        addElement(Sex.MEN, Surface.GRASS, TournamentLevel.ATP_QUALIFIER, 0.65, 0.05);
        addElement(Sex.MEN, Surface.GRASS, TournamentLevel.CHALLENGER_QUALIFIER, 0.60, 0.075);
        addElement(Sex.MEN, Surface.GRASS, TournamentLevel.ITF_QUALIFIER, 0.57, 0.1);
        addElement(Sex.MEN, Surface.HARD, TournamentLevel.ATP_QUALIFIER, 0.65, 0.05);
        addElement(Sex.MEN, Surface.HARD, TournamentLevel.CHALLENGER_QUALIFIER, 0.60, 0.075);
        addElement(Sex.MEN, Surface.HARD, TournamentLevel.ITF_QUALIFIER, 0.57, 0.1);
        addElement(Sex.MEN, Surface.IHARD, TournamentLevel.ATP_QUALIFIER, 0.65, 0.05);
        addElement(Sex.MEN, Surface.IHARD, TournamentLevel.CHALLENGER_QUALIFIER, 0.60, 0.075);
        addElement(Sex.MEN, Surface.IHARD, TournamentLevel.ITF_QUALIFIER, 0.57, 0.1);
        addElement(Sex.WOMEN, Surface.CLAY, TournamentLevel.ITF_QUALIFIER, 0.533, 0.11);
        addElement(Sex.WOMEN, Surface.CLAY, TournamentLevel.WTA_QUALIFIER, 0.55, 0.06);
        addElement(Sex.WOMEN, Surface.CLAY, TournamentLevel.CHALLENGER_QUALIFIER, 0.541, 0.08);
        addElement(Sex.WOMEN, Surface.GRASS, TournamentLevel.ITF_QUALIFIER, 0.569, 0.11);
        addElement(Sex.WOMEN, Surface.GRASS, TournamentLevel.WTA_QUALIFIER, 0.586, 0.06);
        addElement(Sex.WOMEN, Surface.GRASS, TournamentLevel.CHALLENGER_QUALIFIER, 0.574, 0.08);
        addElement(Sex.WOMEN, Surface.HARD, TournamentLevel.ITF_QUALIFIER, 0.547, 0.11);
        addElement(Sex.WOMEN, Surface.HARD, TournamentLevel.WTA_QUALIFIER, 0.555, 0.06);
        addElement(Sex.WOMEN, Surface.HARD, TournamentLevel.CHALLENGER_QUALIFIER, 0.551, 0.08);
        addElement(Sex.WOMEN, Surface.IHARD, TournamentLevel.ITF_QUALIFIER, 0.553, 0.11);
        addElement(Sex.WOMEN, Surface.IHARD, TournamentLevel.WTA_QUALIFIER, 0.563, 0.06);
        addElement(Sex.WOMEN, Surface.IHARD, TournamentLevel.CHALLENGER_QUALIFIER, 0.558, 0.08);
        addElement(Sex.MEN, Surface.CARPET, TournamentLevel.ATP_QUALIFIER, 0.65, 0.05);
        addElement(Sex.MEN, Surface.CARPET, TournamentLevel.CHALLENGER_QUALIFIER, 0.60, 0.075);
        addElement(Sex.MEN, Surface.CARPET, TournamentLevel.ITF_QUALIFIER, 0.57, 0.1);
        addElement(Sex.WOMEN, Surface.CARPET, TournamentLevel.WTA_QUALIFIER, 0.586, 0.06);
        addElement(Sex.WOMEN, Surface.CARPET, TournamentLevel.CHALLENGER_QUALIFIER, 0.57, 0.08);
        addElement(Sex.WOMEN, Surface.CARPET, TournamentLevel.ITF_QUALIFIER, 0.556, 0.11);
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


    @SuppressWarnings("unchecked")
    @Override
    public void applyParams(Map<String, Object> delegate) {
        super.applyParams(delegate);
        boolean doubles = true;

        Map<String, Object> delegate2 = (Map<String, Object>) delegate.get("paramMap");
        if (delegate2.containsKey("onServePctA")) {
            doubles = false;
            onServePctA1 = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, delegate2.get("onServePctA"));
            onServePctB1 = new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, delegate2.get("onServePctB"));
            /*
             * still create the other two params so they are not null - set to default values
             */
            onServePctA2 = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, 0.65, 0.05, 0.2, 0.8, true);
            onServePctB2 = new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, 0.65, 0.05, 0.2, 0.8, true);

        } else {
            doubles = true;
            onServePctA1 = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, delegate2.get("onServePctA1"));
            onServePctA2 = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, delegate2.get("onServePctA2"));
            onServePctB1 = new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, delegate2.get("onServePctB1"));
            onServePctB2 = new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, delegate2.get("onServePctB2"));
        }
        updateParamMapForMatchFormat(doubles);
    }
}
