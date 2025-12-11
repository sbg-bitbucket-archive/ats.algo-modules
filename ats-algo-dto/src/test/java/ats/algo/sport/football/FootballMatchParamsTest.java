package ats.algo.sport.football;

import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.baseclasses.MatchParam;
import ats.algo.core.common.TeamSheet;
import ats.algo.genericsupportfunctions.JsonFormatter;
import ats.org.json.JSONException;

public class FootballMatchParamsTest {

    @Test
    public void testNoPenalties() {
        FootballMatchFormat format = new FootballMatchFormat();
        test1(format, 6);
    }

    @Test
    public void testWithPenalties() {
        FootballMatchFormat format = new FootballMatchFormat();
        format.setPenaltiesPossible(true);
        test1(format, 18);
    }

    public void test1(FootballMatchFormat format, int expNoParamsForDefaultTier) {
        FootballMatchParams params = new FootballMatchParams(format);
        params.getGoalSupremacy().setProperties(4, 1, 0);
        params.getCornerSupremacy().setProperties(5, 1, 0);
        /*
         * generic matchParams should by default only contain the goal related params
         */
        GenericMatchParams generic = params.generateGenericMatchParams();
        System.out.println(generic);
        assertEquals(expNoParamsForDefaultTier, generic.getParamMap().size());
        FootballMatchParams params2 = (FootballMatchParams) generic.generateXxxMatchParams();
        /*
         * verify that goal param gets updated but corner one doesn't
         */
        assertEquals(params.getGoalSupremacy(), params2.getGoalSupremacy());
        assertEquals(1.0, params2.getCornerSupremacy().getMean(), 0.0001);
        /*
         * change the event tier to add in corners and cards.
         */
        generic.updateParamMapForEventTier(3);
        generic.getParamMap().get("cornerTotal").getGaussian().setProperties(8, 3, 1);
        FootballMatchParams params3 = (FootballMatchParams) generic.generateXxxMatchParams();
        assertEquals(4.0, params3.getGoalSupremacy().getMean(), 0.0001);
        assertEquals(1.0, params3.getCornerSupremacy().getMean(), 0.0001);
        assertEquals(8.0, params3.getCornerTotal().getMean(), 0.0001);
        /*
         * go back down to tier 4 - only 4 params. Corners should go back to default values
         */
        generic.updateParamMapForEventTier(4);
        FootballMatchParams params4 = (FootballMatchParams) generic.generateXxxMatchParams();
        assertEquals(expNoParamsForDefaultTier, generic.getParamMap().size());
        assertEquals(4.0, params4.getGoalSupremacy().getMean(), 0.0001);
        assertEquals(11.0, params4.getCornerTotal().getMean(), 0.0001);
    }

    @Test
    public void testIndividualParams() {
        FootballMatchFormat format = new FootballMatchFormat();
        FootballMatchParams params = new FootballMatchParams(format);
        GenericMatchParams generic = params.generateGenericMatchParams();
        generic.updateParamMapForEventTier(6);
        try {
            System.out.println(JsonFormatter.format(generic.toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        assertEquals(45, generic.getParamMap().size());
        MatchParam p1 = generic.getParamMap().get("A.Player3");
        assertFalse(p1 == null);
        TeamSheet teamSheet = ExampleTeamSheets.getExampleTeamSheet();
        generic.updatePlayerMatchParams(teamSheet);
        System.out.println(generic);
        FootballMatchParams params2 = (FootballMatchParams) generic.generateXxxMatchParams();
        assertEquals(45, params2.getParamMap().size());
        p1 = params2.getIndividualParams().get("A.Andre Gray");
        assertFalse(p1 == null);
    }

}
