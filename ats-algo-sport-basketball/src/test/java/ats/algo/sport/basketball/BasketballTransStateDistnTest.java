package ats.algo.sport.basketball;



import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Test;

public class BasketballTransStateDistnTest {

    @Test
    public void testPreMatch() throws ParseException {
        BasketballMatchFormat matchFormat = new BasketballMatchFormat();
        matchFormat.setExtraTimeMinutes(5);
        matchFormat.setNormalTimeMinutes(48);
        matchFormat.setTwoHalvesFormat(false);
        BasketballMatchState matchState = new BasketballMatchState(matchFormat);
        BasketballMatchParams matchParams = new BasketballMatchParams();
        matchParams.setDefaultParams(matchFormat);
        BasketballMatch match = new BasketballMatch(matchFormat, (BasketballMatchState) matchState,
                        (BasketballMatchParams) matchParams);

        JSONObject modelParams = match.getModelData();
        double[][] coef_ = match.getModelCoefFromJson(modelParams);
        double[] int_ = match.getModelInterceptFromJson(modelParams);

        double[] transStateDistn = match.getModelProbabilities(coef_, int_,
                        new double[] {600, 3, 195.5, -7.5, 1.0, 0.0, 0.0, 0.0});
        double sum = 0.0;
        System.out.println(matchState.getMatchPeriod() + " Test: time is " + matchState.getElapsedTimeSecs()
                        + " secs point is " + matchState.getPointsA() + "-" + matchState.getPointsB());
        for (int m = 0; m < transStateDistn.length; m++) {
            sum += transStateDistn[m];
            System.out.println("transStateDistn [" + m + "] is " + m + transStateDistn[m]);
        }
        System.out.println("sum is " + sum);
        // assertEquals(1.0, sum, 0.005);
    }

    @Test
    public void testInplay() {

        // BasketballMatchFormat matchFormat = new BasketballMatchFormat();
        // matchFormat.setExtraTimeMinutes(5);
        // matchFormat.setNormalTimeMinutes(48);
        // matchFormat.setTwoHalvesFormat(false);
        // BasketballMatchState matchState = new BasketballMatchState(matchFormat);
        // BasketballMatchParams matchParams = new BasketballMatchParams();
        // matchParams.setDefaultParams(matchFormat);
        // ElapsedTimeMatchIncident elapsedTimeMatchIncident =
        // new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
        // BasketballMatchPeriod outcome = (BasketballMatchPeriod) matchState
        // .updateStateForIncident((MatchIncident) elapsedTimeMatchIncident, false);
        // assertEquals(BasketballMatchPeriod.IN_FIRST_QUARTER, outcome);
        // assertEquals(0, matchState.getPointsA());
        // assertEquals(0, matchState.getPointsB());
        // BasketballMatchIncident basketballMatchIncident = new BasketballMatchIncident();
        // basketballMatchIncident.set(BasketballMatchIncidentType.TWO_POINTS_SCORED, 30, TeamId.A);
        // outcome = (BasketballMatchPeriod) matchState.updateStateForIncident((MatchIncident) basketballMatchIncident,
        // false);
        //
        // assertEquals(2, matchState.getPointsA());
        // assertEquals(0, matchState.getPointsB());
        // matchState.setElapsedTimeSecs(100);
        // matchState.setPointsA(10);
        // matchState.setPointsB(15);
        // BasketballMatch match = new BasketballMatch(matchFormat, (BasketballMatchState) matchState,
        // (BasketballMatchParams) matchParams);
        //
        // //match.getData();
        // double[] transStateDistn = match.getModelProbabilites(matchState.getElapsedTimeSecs(),
        // matchState.getPointsA() - matchState.getPointsB());
        // double sum = 0.0;
        // System.out.println(matchState.getMatchPeriod() + " 1sr Test: time is " + matchState.getElapsedTimeSecs()
        // + " secs point is " + matchState.getPointsA() + "-" + matchState.getPointsB());
        // for (int m = 0; m < transStateDistn.length; m++) {
        // sum += transStateDistn[m];
        // System.out.println("transStateDistn [" + m + "] is " + m + transStateDistn[m]);
        // }
        // System.out.println("sum is " + sum);
        // assertEquals(1.0, sum, 0.005);
        // sum = 0.0;
        // matchState.setElapsedTimeSecs(200);
        // matchState.setPointsA(18);
        // matchState.setPointsB(15);
        // System.out.println(matchState.getMatchPeriod() + " 2nd Test: time is " + matchState.getElapsedTimeSecs()
        // + " secs point is " + matchState.getPointsA() + "-" + matchState.getPointsB());
        //
        // transStateDistn = match.getModelProbabilites(matchState.getElapsedTimeSecs(),
        // matchState.getPointsA() - matchState.getPointsB());
        // for (int m = 0; m < transStateDistn.length; m++) {
        // sum += transStateDistn[m];
        // System.out.println("transStateDistn [" + m + "] is " + m + transStateDistn[m]);
        // }
        // System.out.println("sum is " + sum);
        // assertEquals(1.0, sum, 0.005);
        //
        // sum = 0.0;
        // matchState.setMatchPeriod(BasketballMatchPeriod.IN_SECOND_QUARTER);
        // matchState.setElapsedTimeSecs(1000);
        // matchState.setPointsA(38);
        // matchState.setPointsB(25);
        // System.out.println(matchState.getMatchPeriod() + " 1st Test: time is " + matchState.getElapsedTimeSecs()
        // + " secs point is " + matchState.getPointsA() + "-" + matchState.getPointsB());
        //
        //
        // transStateDistn = match.getModelProbabilites(matchState.getElapsedTimeSecs(),
        // matchState.getPointsA() - matchState.getPointsB());
        // for (int m = 0; m < transStateDistn.length; m++) {
        // sum += transStateDistn[m];
        // System.out.println("transStateDistn [" + m + "] is " + m + transStateDistn[m]);
        // }
        // System.out.println("sum is " + sum);
        // assertEquals(1.0, sum, 0.005);
        // sum = 0.0;
        // matchState.setMatchPeriod(BasketballMatchPeriod.IN_SECOND_QUARTER);
        // matchState.setElapsedTimeSecs(1100);
        // matchState.setPointsA(38);
        // matchState.setPointsB(55);
        // System.out.println(matchState.getMatchPeriod() + " 2nd Test: time is " + matchState.getElapsedTimeSecs()
        // + " secs point is " + matchState.getPointsA() + "-" + matchState.getPointsB());
        //
        //
        // transStateDistn = match.getModelProbabilites(matchState.getElapsedTimeSecs(),
        // matchState.getPointsA() - matchState.getPointsB());
        // for (int m = 0; m < transStateDistn.length; m++) {
        // sum += transStateDistn[m];
        // System.out.println("transStateDistn [" + m + "] is " + m + transStateDistn[m]);
        // }
        // System.out.println("sum is " + sum);
        // assertEquals(1.0, sum, 0.005);
        // sum = 0.0;
        // matchState.setMatchPeriod(BasketballMatchPeriod.IN_THIRD_QUARTER);
        // matchState.setElapsedTimeSecs(1800);
        // matchState.setPointsA(60);
        // matchState.setPointsB(60);
        // System.out.println(matchState.getMatchPeriod() + " 1st Test: time is " + matchState.getElapsedTimeSecs()
        // + " secs point is " + matchState.getPointsA() + "-" + matchState.getPointsB());
        //
        //
        // transStateDistn = match.getModelProbabilites(matchState.getElapsedTimeSecs(),
        // matchState.getPointsA() - matchState.getPointsB());
        // for (int m = 0; m < transStateDistn.length; m++) {
        // sum += transStateDistn[m];
        // System.out.println("transStateDistn [" + m + "] is " + m + transStateDistn[m]);
        // }
        // System.out.println("sum is " + sum);
        // assertEquals(1.0, sum, 0.005);
        // sum = 0.0;
        // matchState.setMatchPeriod(BasketballMatchPeriod.IN_THIRD_QUARTER);
        // matchState.setElapsedTimeSecs(2000);
        // matchState.setPointsA(70);
        // matchState.setPointsB(60);
        // System.out.println(matchState.getMatchPeriod() + " 2nd Test: time is " + matchState.getElapsedTimeSecs()
        // + " secs point is " + matchState.getPointsA() + "-" + matchState.getPointsB());
        //
        //
        // transStateDistn = match.getModelProbabilites(matchState.getElapsedTimeSecs(),
        // matchState.getPointsA() - matchState.getPointsB());
        // for (int m = 0; m < transStateDistn.length; m++) {
        // sum += transStateDistn[m];
        // System.out.println("transStateDistn [" + m + "] is " + m + transStateDistn[m]);
        // }
        // System.out.println("sum is " + sum);
        // assertEquals(1.0, sum, 0.005);
        //
        // sum = 0.0;
        // matchState.setMatchPeriod(BasketballMatchPeriod.IN_FOURTH_QUARTER);
        // matchState.setElapsedTimeSecs(2500);
        // matchState.setPointsA(80);
        // matchState.setPointsB(81);
        // System.out.println(matchState.getMatchPeriod() + " 1st Test: time is " + matchState.getElapsedTimeSecs()
        // + " secs point is " + matchState.getPointsA() + "-" + matchState.getPointsB());
        //
        //
        // transStateDistn = match.getModelProbabilites(matchState.getElapsedTimeSecs(),
        // matchState.getPointsA() - matchState.getPointsB());
        // for (int m = 0; m < transStateDistn.length; m++) {
        // sum += transStateDistn[m];
        // System.out.println("transStateDistn [" + m + "] is " + m + transStateDistn[m]);
        // }
        // System.out.println("sum is " + sum);
        // assertEquals(1.0, sum, 0.005);
        //
        // sum = 0.0;
        // matchState.setMatchPeriod(BasketballMatchPeriod.IN_FOURTH_QUARTER);
        // matchState.setElapsedTimeSecs(2800);
        // matchState.setPointsA(95);
        // matchState.setPointsB(93);
        // System.out.println(matchState.getMatchPeriod() + " 2nd Test: time is " + matchState.getElapsedTimeSecs()
        // + " secs point is " + matchState.getPointsA() + "-" + matchState.getPointsB());
        //
        //
        // transStateDistn = match.getModelProbabilites(matchState.getElapsedTimeSecs(),
        // matchState.getPointsA() - matchState.getPointsB());
        // for (int m = 0; m < transStateDistn.length; m++) {
        // sum += transStateDistn[m];
        // System.out.println("transStateDistn [" + m + "] is " + m + transStateDistn[m]);
        // }
        // System.out.println("sum is " + sum);
        // assertEquals(1.0, sum, 0.005);
    }

}
