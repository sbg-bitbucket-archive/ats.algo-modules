// package ats.algo.sport.floorball;
//
// import static org.junit.Assert.assertEquals;
//
// import org.junit.Test;
//
// import ats.algo.markets.Markets;
// import ats.algo.sport.floorball.FloorballMatchEngine;
// import ats.algo.sport.floorball.FloorballMatchFormat;
// import ats.algo.sport.floorball.FloorballMatchParams;
//
// public class FloorballMatchEngineTest {
//
// @Test
// public void test() {
// FloorballMatchFormat matchFormat = new FloorballMatchFormat();
// FloorballMatchEngine matchEngine = new FloorballMatchEngine(matchFormat);
// FloorballMatchParams matchParams = (FloorballMatchParams) matchEngine.getMatchParams();
// matchParams.setHomeScoreRate(2, 0);
// matchParams.setAwayScoreRate(2, 0);
// matchParams.setHomeLoseBoost(0.0, 0);
// matchParams.setAwayLoseBoost(0.0, 0);
// matchEngine.setMatchParams(matchParams);
// Markets markets = matchEngine.calculate();
// System.out.print(markets.toString());
// assertEquals(0.4, markets.get("AB").get("Home"), 0.01);
// assertEquals(0.2, markets.get("AB").get("Draw"), 0.01);
// assertEquals(0.4, markets.get("AB").get("Away"), 0.01);
//
// matchParams.setHomeScoreRate(1.7847, 0);
// matchParams.setAwayScoreRate(0.80939, 0);
// matchParams.setHomeLoseBoost(0.2393, 0);
// matchParams.setAwayLoseBoost(0.11043, 0);
// matchEngine.setMatchParams(matchParams);
// markets = matchEngine.calculate();
// System.out.print(markets.toString());
// assertEquals(0.605, markets.get("AB").get("Home"), 0.005);
// assertEquals(0.249, markets.get("AB").get("Draw"), 0.005);
// assertEquals(0.146, markets.get("AB").get("Away"), 0.005);
//
// }
// }
