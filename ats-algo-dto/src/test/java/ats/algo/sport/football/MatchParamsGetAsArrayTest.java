package ats.algo.sport.football;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.junit.Test;
import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.baseclasses.MatchParam;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.baseclasses.MatchParams.ParamArray;
import ats.algo.genericsupportfunctions.Gaussian;
import ats.algo.sport.football.FootballMatchParams;

public class MatchParamsGetAsArrayTest {

    @Test
    public void test1() {
        MatchParams.setTransformParams(false);
        test1Body();
        MatchParams.setTransformParams(true);
        test1Body();

    }

    private void test1Body() {
        FootballMatchParams matchParams = getTestMatchParams();

        matchParams.setGoalTotal(5, .4);
        matchParams.getRedCardProb().setMean(.3);
        matchParams.getRedCardProb().setStdDevn(.05);
        FootballMatchParams matchParams2 = (FootballMatchParams) matchParams.copy();
        assertEquals(matchParams, matchParams2);
        // System.out.println(matchParams.toString());
        ParamArray paramArray = matchParams.getAsArray(null, true, false);
        assertEquals(90, paramArray.x.length);
        ArrayList<MarketGroup> matchGroups = new ArrayList<MarketGroup>();
        matchGroups.add(MarketGroup.GOALS);
        paramArray = matchParams.getAsArray(matchGroups, true, false);
        assertEquals(12, paramArray.x.length);
        matchGroups.add(MarketGroup.CORNERS);
        paramArray = matchParams.getAsArray(matchGroups, true, false);
        assertEquals(16, paramArray.x.length);
        matchGroups.add(MarketGroup.BOOKINGS);
        paramArray = matchParams.getAsArray(matchGroups, true, false);
        assertEquals(22, paramArray.x.length);

        matchParams.setGoalTotal(3, .3);
        matchParams.getRedCardProb().setMean(.5);
        matchParams.getRedCardProb().setStdDevn(.07);
        matchParams.setFromArray(paramArray.x);
        checkMatchParams(matchParams, matchParams2);
        assertEquals(matchParams, matchParams2);
    }

    @Test
    public void test2() {
        MatchParams.setTransformParams(false);
        test2Body();
        MatchParams.setTransformParams(true);
        test2Body();

    }

    private void test2Body() {
        FootballMatchParams matchParams = getTestMatchParams();
        matchParams.setGoalTotal(5, .4);
        matchParams.setGoalSupremacy(3, 0); // should therefore exclude
                                            // supremacy from the param find
        matchParams.getRedCardProb().setMean(.3);
        matchParams.getRedCardProb().setStdDevn(.05);
        FootballMatchParams matchParams2 = (FootballMatchParams) matchParams.copy();
        System.out.println(matchParams);
        ParamArray paramArray = matchParams.getAsArray(null, true, false);
        System.out.println(paramArray);

        matchParams.setGoalTotal(4, .2);
        matchParams.getRedCardProb().setMean(.6);
        matchParams.getRedCardProb().setStdDevn(.07);
        matchParams.setFromArray(paramArray.x);
        checkMatchParams(matchParams, matchParams2);
        System.out.println(matchParams2.toString());
        System.out.println(matchParams.toString());
        assertEquals(88, paramArray.x.length);
        assertEquals(matchParams, matchParams2);

    }

    @Test
    public void test3() {
        MatchParams.setTransformParams(false);
        test3Body();
        MatchParams.setTransformParams(true);
        test3Body();
    }

    /**
     * test rotating the axes of the params
     */
    private void test3Body() {
        FootballMatchParams matchParams = getTestMatchParams();
        matchParams.setGoalTotal(6, .4);
        FootballMatchParams matchParams2 = (FootballMatchParams) matchParams.copy();
        // System.out.println(matchParams);
        ParamArray paramArray = matchParams.getAsArray(null, true, true);
        // System.out.println(paramArray);
        matchParams.setGoalTotal(4, .2);
        matchParams.getRedCardProb().setMean(.6);
        matchParams.getRedCardProb().setStdDevn(.07);
        matchParams.setFromArray(paramArray.x);
        System.out.println(matchParams.toString());
        System.out.println(matchParams2.toString());

        checkMatchParams(matchParams, matchParams2);
        assertEquals(matchParams, matchParams2);
    }

    @Test
    public void test4() {
        MatchParams.setTransformParams(false);
        test4Body();
        MatchParams.setTransformParams(true);
        test4Body();
    }

    private void test4Body() {
        FootballMatchParams matchParams = getTestMatchParams();
        matchParams.setGoalTotal(5, .4);
        matchParams.setGoalSupremacy(3, 0); // should therefore exclude
                                            // supremacy from the param find
        FootballMatchParams matchParams2 = (FootballMatchParams) matchParams.copy();
        System.out.println(matchParams);
        ParamArray paramArray = matchParams.getAsArray(null, false, true);
        // System.out.println(paramArray);
        matchParams.setGoalTotal(4, .4);
        matchParams.setFromArray(paramArray.x);
        checkMatchParams(matchParams, matchParams2);
    }

    @Test
    public void test5() {
        MatchParams.setTransformParams(false);
        test5Body();
        MatchParams.setTransformParams(true);
        test5Body();
    }

    private void test5Body() {
        MatchParams.setTransformParams(false);
        FootballMatchParams matchParams = getTestMatchParams();
        matchParams.setGoalTotal(5, .4);
        matchParams.setGoalSupremacy(3, .3);
        FootballMatchParams matchParams2 = (FootballMatchParams) matchParams.copy();
        System.out.println(matchParams);
        ParamArray paramArray = matchParams.getAsArray(null, false, true);
        System.out.println(paramArray);
        matchParams.setGoalTotal(4, .4);
        matchParams.setFromArray(paramArray.x);
        checkMatchParams(matchParams, matchParams2);
    }

    private void checkMatchParams(FootballMatchParams matchParams, FootballMatchParams matchParams2) {
        for (Entry<String, MatchParam> e : matchParams2.getParamMap().entrySet()) {
            MatchParam p1 = e.getValue();
            MatchParam p2 = matchParams.getParamMap().get(e.getKey());
            System.out.println(e.getKey() + ": " + p1);
            System.out.println(e.getKey() + ": " + p2.toString());
            assertEquals(p1, p2);
        }
    }

    private FootballMatchParams getTestMatchParams() {
        /*
         * set up the params to contain all possible football related params
         */
        FootballMatchFormat format = new FootballMatchFormat();
        format.setPenaltiesPossible(true);
        FootballMatchParams params = new FootballMatchParams(format);
        GenericMatchParams genericMatchParams = params.generateGenericMatchParams();
        genericMatchParams.updateParamMapForEventTier(6);
        params = (FootballMatchParams) genericMatchParams.generateXxxMatchParams();
        params.setGoalTotal(new Gaussian(7, .7));
        params.setGoalSupremacy(new Gaussian(2.5, .25));
        params.setHomeLoseBoost(new Gaussian(.8, .08));
        params.setAwayLoseBoost(.5, .05);
        params.setCornerTotal(new Gaussian(10, 1));
        params.setCornerSupremacy(new Gaussian(4, .4));
        params.setCardTotal(new Gaussian(15, .15));
        params.setCardSupremacy(new Gaussian(-3, .3));
        params.setRedCardProb(new Gaussian(.4, .04));
        return params;
    }

}
