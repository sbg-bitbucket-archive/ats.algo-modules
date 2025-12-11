// package ats.algo.sport.tennis;
//
// import ats.algo.genericsupportfunctions.MethodName;
// import static org.junit.Assert.*;
//
// import org.junit.Test;
//
// import ats.algo.algomanager.AlgoManagerSharedMemoryTestBase;
// import ats.algo.core.baseclasses.GenericMatchParams;
// import ats.algo.core.baseclasses.MatchFormat;
// import ats.algo.core.common.SupportedSportType;
// import ats.algo.core.common.TeamId;
// import ats.algo.genericsupportfunctions.Sleep;
// import ats.algo.sport.tennis.TennisMatchFormat.FinalSetType;
// import ats.algo.sport.tennis.TennisMatchFormat.Sex;
// import ats.algo.sport.tennis.TennisMatchFormat.Surface;
// import ats.algo.sport.tennis.TennisMatchFormat.TournamentLevel;
// import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;
//
// public class TennisUndoProblemTest extends AlgoManagerSharedMemoryTestBase {
//
// @Test
// public void testIncidentJumpUndo() {
// MethodName.print();
// long eventId = 23L;
// MatchFormat matchFormat = new TennisMatchFormat(false, Sex.WOMEN, Surface.HARD, TournamentLevel.ATP, 3,
// FinalSetType.NORMAL_WITH_TIE_BREAK, false);
// publishedMatchParams = null;
// algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, matchFormat, 2);
// waitForPublishedMatchParams();
// GenericMatchParams genericMatchParams = publishedMatchParams.generateGenericMatchParams();
// genericMatchParams.setEventId(eventId);
// publishedMatchParams = null;
// algoManager.handleSetMatchParams(genericMatchParams);
// waitForPublishedMatchParams();
// algoManager.handleMatchIncident(getIncident(eventId, "START", TennisMatchIncidentType.MATCH_STARTING, TeamId.A),
// true);
// algoManager.handleMatchIncident(getIncident(eventId, "15-0", TennisMatchIncidentType.POINT_WON, TeamId.A), true);
// /*
// * wait for these two incidents to get processed
// */
// Sleep.sleep(3);
// algoManager.handleMatchIncident(getIncident(eventId, "30-0", TennisMatchIncidentType.POINT_WON, TeamId.A), true);
// algoManager.handleMatchIncident(getIncident(eventId, "Challenge", TennisMatchIncidentType.CHALLENGER_BALLMARK,
// TeamId.A), true);
// algoManager.handleMatchIncident(getIncident(eventId, "Challenge1", TennisMatchIncidentType.SERVING_ORDER, TeamId.A),
// true);
// algoManager.handleMatchIncident(getIncident(eventId, "Challenge2", TennisMatchIncidentType.CHALLENGE, TeamId.A),
// true);
//
// Sleep.sleep(5);
// TennisSimpleMatchState simpleMatchState = (TennisSimpleMatchState) publishedMatchState;
// // System.out.println(simpleMatchState.formattedGeneralState());
// // System.out.println(simpleMatchState.formattedGeneralState());
// algoManager.handleUndoMatchIncident(eventId, getIncident(eventId, "30-0", TennisMatchIncidentType.POINT_WON,
// TeamId.A));
// Sleep.sleep(5);
// simpleMatchState = (TennisSimpleMatchState) publishedMatchState;
// // System.out.println(simpleMatchState.formattedGeneralState());
// algoManager.handleMatchIncident(getIncident(eventId, "15-15", TennisMatchIncidentType.POINT_WON, TeamId.B), true);
// Sleep.sleep(5);
// simpleMatchState = (TennisSimpleMatchState) publishedMatchState;
// // System.out.println(simpleMatchState.formattedGeneralState());
// /*
// * wait for all these incidents to get processed
// */
// Sleep.sleep(3);
// simpleMatchState = (TennisSimpleMatchState) publishedMatchState;
// // System.out.println(simpleMatchState.formattedGeneralState());
//
// assertEquals(1, simpleMatchState.getPointsA());
// assertEquals(1, simpleMatchState.getPointsB());
// }
//
// @Test
// public void test() {
// MethodName.print();
// long eventId = 23L;
// MatchFormat matchFormat = new TennisMatchFormat(false, Sex.WOMEN, Surface.HARD, TournamentLevel.ATP, 3,
// FinalSetType.NORMAL_WITH_TIE_BREAK, false);
// publishedMatchParams = null;
// algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, matchFormat, 2);
// waitForPublishedMatchParams();
// GenericMatchParams genericMatchParams = publishedMatchParams.generateGenericMatchParams();
// genericMatchParams.setEventId(eventId);
// publishedMatchParams = null;
// algoManager.handleSetMatchParams(genericMatchParams);
// waitForPublishedMatchParams();
// algoManager.handleMatchIncident(getIncident(eventId, "START", TennisMatchIncidentType.MATCH_STARTING, TeamId.A),
// true);
// algoManager.handleMatchIncident(getIncident(eventId, "I1", TennisMatchIncidentType.POINT_WON, TeamId.A), true);
// /*
// * wait for these two incidents to get processed
// */
// Sleep.sleep(3);
// algoManager.handleMatchIncident(getIncident(eventId, "I2", TennisMatchIncidentType.POINT_WON, TeamId.A), true);
// algoManager.handleMatchIncident(getIncident(eventId, "I3", TennisMatchIncidentType.POINT_WON, TeamId.A), true);
// algoManager.handleUndoLastMatchIncident(eventId);
// algoManager.handleMatchIncident(getIncident(eventId, "I4", TennisMatchIncidentType.POINT_WON, TeamId.A), true);
// /*
// * wait for all these incidents to get processed
// */
// Sleep.sleep(3);
// TennisSimpleMatchState simpleMatchState = (TennisSimpleMatchState) publishedMatchState;
// // System.out.println(simpleMatchState.formattedGeneralState());
// assertEquals(3, simpleMatchState.getPointsA());
// assertEquals(0, simpleMatchState.getPointsB());
//
// }
//
// private void waitForPublishedMatchParams() {
// while (publishedMatchParams == null)
// Sleep.sleepMs(200);
// }
//
//
// private TennisMatchIncident getIncident(long eventId, String incidentId, TennisMatchIncidentType type,
// TeamId teamId) {
// TennisMatchIncident incident = new TennisMatchIncident(0, type, teamId);
// incident.setEventId(eventId);
// incident.setIncidentId(incidentId);
// return incident;
//
// }
// }
