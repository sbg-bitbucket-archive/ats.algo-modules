// package ats.algo.sport.outrights.calcengine.core;
//
// import ats.algo.genericsupportfunctions.MethodName;
// import ats.algo.sport.outrights.calcengine.core.AbstractFormat;
// import ats.algo.sport.outrights.calcengine.core.AbstractMarketsFactory;
// import ats.algo.sport.outrights.calcengine.core.AbstractParams;
// import ats.algo.sport.outrights.calcengine.core.AbstractState;
// import ats.algo.sport.outrights.calcengine.core.Competition;
// import ats.algo.sport.outrights.calcengine.core.MatchProbsCache;
// import ats.algo.sport.outrights.calcengine.core.OutrightsCompetition;
// import ats.algo.sport.outrights.competitionsdata.TestCompetition;
//
// public class MatchProbsCacheTest {
//
// @Test
// public void matchProbsCachetest() {
// MethodName.print();
// Competition competition = TestCompetition.generateBasicSample();
// AbstractFormat matchFormat = CompetitionProperties.competitionMatchFormatInstance(competition);
// AbstractParams matchParams = new AbstractParams();
// AbstractState competitionState = CompetitionProperties.competitionStateInstance(competition);
//
// AbstractMarketsFactory marketsFactory = CompetitionProperties.competitionMarketsFactoryInstance(competition);
// OutrightsCompetition match = new OutrightsCompetition(matchFormat, competitionState, matchParams, null,
// marketsFactory);
// match.initialiseProbsCache(competition);
//
// MatchProbsCache matchProbsCache = new MatchProbsCache(competition,
// competition.getHomeAdvantageRatingAdjustment(),false);
// }
//
// }
