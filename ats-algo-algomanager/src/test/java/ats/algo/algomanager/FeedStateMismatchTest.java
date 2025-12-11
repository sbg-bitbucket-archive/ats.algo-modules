package ats.algo.algomanager;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.ElapsedTimeMatchIncident;
import ats.algo.core.common.ElapsedTimeMatchIncident.ElapsedTimeMatchIncidentType;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.common.TeamId;
import ats.algo.core.recordplayback.RecordedItem;
import ats.algo.core.traderalert.TraderAlert.TraderAlertType;
import ats.algo.sport.football.FootballMatchFormat;
import ats.algo.sport.football.FootballMatchIncident;
import ats.algo.sport.football.FootballMatchIncident.FootballMatchIncidentType;
import ats.algo.sport.football.FootballMatchPeriod;
import ats.algo.sport.football.FootballMatchStateFromFeed;
import ats.algo.sport.football.FootballSimpleMatchState;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchFormat.FinalSetType;
import ats.algo.sport.tennis.TennisMatchFormat.Sex;
import ats.algo.sport.tennis.TennisMatchFormat.Surface;
import ats.algo.sport.tennis.TennisMatchFormat.TournamentLevel;
import ats.algo.sport.tennis.TennisMatchIncident;
import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;
import ats.algo.sport.tennis.TennisMatchIncident.TennisPointResult;
import ats.core.util.json.JsonUtil;
import ats.algo.sport.tennis.TennisMatchState;
import ats.algo.sport.tennis.TennisMatchStateFromFeed;
import ats.algo.sport.tennis.TennisSimpleMatchState;

public class FeedStateMismatchTest extends AlgoManagerSimpleTestBase {

    @Test
    @Ignore
    public void testChampionshipTiebreakBug() {
        MethodName.log();

        long eventId = 4568529;
        TennisMatchFormat tennisMatchFormat = new TennisMatchFormat(true, Sex.MEN, Surface.HARD, TournamentLevel.ITF, 3,
                        FinalSetType.CHAMPIONSHIP_TIE_BREAK, true);
        algoManager.autoSyncWithMatchFeed(false);
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, tennisMatchFormat);

        String state = "{\"TennisMatchState\":{\"subClass\":\"TennisMatchState\",\"eventId\":0,\"incidentId\":\"1314169267\",\"clockTimeOfLastPriceCalc\":1519393331238,\"clockTimeOfLastElapsedTimeFromIncident\":0,\"dataFeedStatus\":\"BET_START\",\"clocktimeOfLastDatafeedMatchIncident\":0,\"noSuccessfulInPlayParamFindsExecuted\":0,\"datafeedStateMismatch\":false,\"dataToGenerateStaticMarkets\":{\"initialisationCompleted\":true,\"map\":{\"FT:SPRD\":\"-1\"}},\"genderMap\":{\"A1\":\"MALE\",\"B1\":\"MALE\"},\"suspensionStatus\":{\"delaySuspension\":false,\"timeStampOfIncident\":-1},\"faultFlag\":false,\"matchFormat\":{\"subClass\":\"TennisMatchFormat\",\"sportType\":\"TENNIS\",\"matchFormatOk\":true,\"doublesMatch\":true,\"sex\":\"MEN\",\"surface\":\"IHARD\",\"tournamentLevel\":\"ITF\",\"setsPerMatch\":3,\"finalSetType\":\"CHAMPIONSHIP_TIE_BREAK\",\"noAdvantageGameFormat\":true,\"noAdvantageTieBreakFormat\":false,\"teamAPlayerRank\":0,\"teamBPlayerRank\":0,\"teamAPlayerId\":\"Unknown\",\"teamBPlayerId\":\"Unknown\"},\"matchWinningSetScore\":2,\"setsA\":0,\"setsB\":1,\"gamesA\":6,\"gamesB\":6,\"tieBreaksCounter\":1,\"firstServingBreakInSetN\":[3,3,0],\"firstServingBreakAInSetN\":[0,4,0],\"firstServingBreakBInSetN\":[3,3,0],\"gameScoreInSetN\":[{\"A\":4,\"B\":6},{\"A\":0,\"B\":0},{\"A\":0,\"B\":0}],\"inTieBreak\":true,\"inSuperTieBreak\":false,\"breakPoint\":false,\"gamePoint\":false,\"dangerPoint\":false,\"inFinalSet\":false,\"nSetsInMatch\":3,\"nGamesInMatch\":6,\"onServePlayerInFirstGameOfSet\":\"A1\",\"onServePlayerInSecondGameOfSet\":\"B1\",\"startOfMatchServer\":\"A1\",\"finalSetSuperTBNo\":10,\"tieBreakinFinalSet\":true,\"tieBreakinRegularSet\":true,\"finalSetSuperTieBreak\":true,\"newSetSwitchServePlayer\":false,\"doublesMatch\":true,\"game\":{\"pointsA\":0,\"pointsB\":4,\"pointNo\":4,\"onServeSideNow\":\"B\",\"onServePlayerTeamANow\":1,\"onServePlayerTeamBNow\":1,\"noAdvantageGameFormat\":true,\"powerPointCurrentPoint\":false,\"onServeNow\":\"B\"},\"tieBreak\":{\"pointsA\":6,\"pointsB\":5,\"onServeSideNow\":\"A\",\"onServePlayerTeamANow\":2,\"onServePlayerTeamBNow\":2,\"onServeAtStartOfTieBreak\":\"A\",\"onServePlayerTeamAAtStartOfTieBreak\":2,\"onServePlayerTeamBAtStartOfTieBreak\":2,\"noAdvantageTiebreakFormat\":false,\"doubles\":true,\"onServeNow\":\"A\"},\"superTieBreak\":{\"pointsA\":0,\"pointsB\":0,\"onServeSideNow\":\"UNKNOWN\",\"onServePlayerTeamANow\":0,\"onServePlayerTeamBNow\":0,\"onServeAtStartOfSuperTieBreak\":\"UNKNOWN\",\"onServePlayerTeamAAtStartOfSuperTieBreak\":0,\"onServePlayerTeamBAtStartOfSuperTieBreak\":0,\"doubles\":true},\"lastPointPlayedOutcome\":\"B\",\"lastGamePlayedOutcome\":\"B\",\"teamBreakFirst\":\"B\",\"doubletennisMatchIncident\":{\"subClass\":\"TennisMatchIncident\",\"undo\":false,\"elapsedTimeSecs\":4434,\"eventId\":4568529,\"incidentId\":\"1314169267\",\"sourceSystem\":\"BETRADAR\",\"timeStamp\":1519393333050,\"matchStateFromFeed\":{\"subClass\":\"TennisMatchStateFromFeed\",\"setsA\":0,\"setsB\":1,\"gamesA\":6,\"gamesB\":6,\"pointsA\":6,\"pointsB\":5,\"teamServingNow\":\"UNKNOWN\",\"playerServingNow\":\"B1\"},\"incidentSubType\":\"POINT_WON\",\"serverSideAtStartOfMatch\":\"B\",\"serverPlayerAtStartOfMatch\":0,\"pointWinner\":\"B\",\"pointResult\":\"UNKNOWN\"},\"lastIncidentDetails\":{\"lastIncident\":{\"subClass\":\"TennisMatchIncident\",\"undo\":false,\"elapsedTimeSecs\":4434,\"eventId\":4568529,\"incidentId\":\"1314169267\",\"sourceSystem\":\"BETRADAR\",\"timeStamp\":1519393333050,\"matchStateFromFeed\":{\"subClass\":\"TennisMatchStateFromFeed\",\"setsA\":0,\"setsB\":1,\"gamesA\":6,\"gamesB\":6,\"pointsA\":6,\"pointsB\":5,\"teamServingNow\":\"UNKNOWN\",\"playerServingNow\":\"B1\"},\"incidentSubType\":\"POINT_WON\",\"serverSideAtStartOfMatch\":\"B\",\"serverPlayerAtStartOfMatch\":0,\"pointWinner\":\"B\",\"pointResult\":\"UNKNOWN\"},\"servingPlayer\":\"B1\",\"servingTeam\":\"B\",\"powerPoint\":false,\"pointNo\":11,\"serveNo\":1,\"pointResult\":\"UNKNOWN\"},\"serveNumber\":1,\"previousGameScore\":{\"A\":0,\"B\":4},\"requestedFirstPlayerToServe\":true,\"sequenceIdForMatch\":\"M\",\"gameNo\":13,\"noAdvantageGameFormat\":true,\"powerPointCurrentPoint\":false,\"onServeTeamInFirstGameOfSet\":\"A\",\"onServeTeamInSecondGameOfSet\":\"B\",\"noSetsInMatch\":3,\"onServeTeamNow\":\"A\",\"onServeNow\":\"A\",\"breakPointStillPossible\":false,\"nextSetFinalSet\":true,\"totalGamesA\":4,\"totalGamesB\":6,\"preMatch\":false,\"matchCompleted\":false,\"nextPrompt\":{\"matchOver\":false,\"prompt\":\"Next point winner Ar/Br, r=R(ally), A(ce), D(ouble fault), U(nknown): \"},\"doubleMatch\":true,\"onServePlayerNow\":\"A2\",\"pointsA\":6,\"pointsB\":5,\"setNo\":2,\"pointNo\":12,\"clockRunning\":false}}";
        TennisMatchState matchState = JsonUtil.unmarshalJson(state, TennisMatchState.class);
        // System.out.println(matchState);

        algoManager.getEventDetails(eventId).getEventState().setMatchState(matchState);

        // System.out.println(algoManager.getEventDetails(eventId).getEventState().getMatchState());

        String serving = "{\"TennisMatchIncident\":{\"subClass\":\"TennisMatchIncident\",\"undo\":false,\"elapsedTimeSecs\":4462,\"eventId\":4568529,\"incidentId\":\"-2273-2-13-7\",\"sourceSystem\":\"BETRADAR\",\"timeStamp\":1519393333050,\"incidentSubType\":\"SERVING_ORDER\",\"serverSideAtStartOfMatch\":\"A\",\"serverPlayerAtStartOfMatch\":1,\"pointWinner\":\"UNKNOWN\"}}";
        TennisMatchIncident incidentServing = JsonUtil.unmarshalJson(serving, TennisMatchIncident.class);
        algoManager.handleMatchIncident(incidentServing, true);

        // System.out.println(this.publishedMatchState);

        String pointToWinSet =
                        "{\"TennisMatchIncident\":{\"subClass\":\"TennisMatchIncident\",\"undo\":false,\"elapsedTimeSecs\":4462,\"eventId\":4568529,\"incidentId\":\"1314170221\",\"sourceSystem\":\"BETRADAR\",\"timeStamp\":1519393336069,\"matchStateFromFeed\":{\"subClass\":\"TennisMatchStateFromFeed\",\"setsA\":1,\"setsB\":1,\"gamesA\":7,\"gamesB\":6,\"pointsA\":7,\"pointsB\":5,\"teamServingNow\":\"UNKNOWN\",\"playerServingNow\":\"B1\"},\"incidentSubType\":\"POINT_WON\",\"serverSideAtStartOfMatch\":\"B\",\"serverPlayerAtStartOfMatch\":0,\"pointWinner\":\"A\",\"pointResult\":\"UNKNOWN\"}}";
        TennisMatchIncident pointToWinSetIncident = JsonUtil.unmarshalJson(pointToWinSet, TennisMatchIncident.class);
        algoManager.handleMatchIncident(pointToWinSetIncident, true);

        // System.out.println(this.publishedMatchState);
        TennisMatchState stateCheck =
                        (TennisMatchState) algoManager.getEventDetails(eventId).getEventState().getMatchState();
        // System.out.println(stateCheck);
        assertEquals(stateCheck.isInTieBreak(), true);
        assertEquals(stateCheck.isInSuperTieBreak(), true);

        String s = "{\"TennisMatchIncident\":{\"subClass\":\"TennisMatchIncident\",\"undo\":false,\"elapsedTimeSecs\":4492,\"eventId\":4568529,\"incidentId\":\"-2273-3-1\",\"sourceSystem\":\"BETRADAR\",\"timeStamp\":1519393336069,\"incidentSubType\":\"SERVING_ORDER\",\"serverSideAtStartOfMatch\":\"B\",\"serverPlayerAtStartOfMatch\":1,\"pointWinner\":\"UNKNOWN\"}}";
        TennisMatchIncident i1 = JsonUtil.unmarshalJson(s, TennisMatchIncident.class);
        algoManager.handleMatchIncident(i1, true);

        s = "{\"TennisMatchIncident\":{\"subClass\":\"TennisMatchIncident\",\"undo\":false,\"elapsedTimeSecs\":4492,\"eventId\":4568529,\"incidentId\":\"1314172325\",\"sourceSystem\":\"BETRADAR\",\"timeStamp\":1519393343112,\"incidentSubType\":\"MATCH_STARTING\",\"serverSideAtStartOfMatch\":\"B\",\"serverPlayerAtStartOfMatch\":1}}";
        i1 = JsonUtil.unmarshalJson(s, TennisMatchIncident.class);
        algoManager.handleMatchIncident(i1, true);

        s = "{\"TennisMatchIncident\":{\"subClass\":\"TennisMatchIncident\",\"undo\":false,\"elapsedTimeSecs\":4566,\"eventId\":4568529,\"incidentId\":\"1314172551\",\"sourceSystem\":\"BETRADAR\",\"timeStamp\":1519393344121,\"matchStateFromFeed\":{\"subClass\":\"TennisMatchStateFromFeed\",\"setsA\":1,\"setsB\":1,\"gamesA\":0,\"gamesB\":0,\"pointsA\":0,\"pointsB\":1,\"teamServingNow\":\"UNKNOWN\",\"playerServingNow\":\"B1\"},\"incidentSubType\":\"POINT_WON\",\"serverSideAtStartOfMatch\":\"B\",\"serverPlayerAtStartOfMatch\":0,\"pointWinner\":\"B\",\"pointResult\":\"UNKNOWN\"}}";
        i1 = JsonUtil.unmarshalJson(s, TennisMatchIncident.class);
        algoManager.handleMatchIncident(i1, true);

        s = "{\"TennisMatchIncident\":{\"subClass\":\"TennisMatchIncident\",\"undo\":false,\"elapsedTimeSecs\":4576,\"eventId\":4568529,\"incidentId\":\"1314173283\",\"sourceSystem\":\"BETRADAR\",\"timeStamp\":1519393347143,\"incidentSubType\":\"MATCH_STARTING\",\"serverSideAtStartOfMatch\":\"A\",\"serverPlayerAtStartOfMatch\":1}}";
        i1 = JsonUtil.unmarshalJson(s, TennisMatchIncident.class);
        algoManager.handleMatchIncident(i1, true);

        s = "{\"TennisMatchIncident\":{\"subClass\":\"TennisMatchIncident\",\"undo\":false,\"elapsedTimeSecs\":4604,\"eventId\":4568529,\"incidentId\":\"1314173431\",\"sourceSystem\":\"BETRADAR\",\"timeStamp\":1519393347155,\"incidentSubType\":\"FAULT\",\"serverPlayerAtStartOfMatch\":0}}";
        i1 = JsonUtil.unmarshalJson(s, TennisMatchIncident.class);
        algoManager.handleMatchIncident(i1, true);

        s = "{\"TennisMatchIncident\":{\"subClass\":\"TennisMatchIncident\",\"undo\":false,\"elapsedTimeSecs\":4619,\"eventId\":4568529,\"incidentId\":\"1314173703\",\"sourceSystem\":\"BETRADAR\",\"timeStamp\":1519393348154,\"matchStateFromFeed\":{\"subClass\":\"TennisMatchStateFromFeed\",\"setsA\":1,\"setsB\":1,\"gamesA\":0,\"gamesB\":0,\"pointsA\":0,\"pointsB\":2,\"teamServingNow\":\"UNKNOWN\",\"playerServingNow\":\"B1\"},\"incidentSubType\":\"POINT_WON\",\"serverSideAtStartOfMatch\":\"A\",\"serverPlayerAtStartOfMatch\":0,\"pointWinner\":\"B\",\"pointResult\":\"DOUBLE_FAULT\"}}";
        i1 = JsonUtil.unmarshalJson(s, TennisMatchIncident.class);
        algoManager.handleMatchIncident(i1, true);

        // System.out.println(this.publishedMatchState);
        stateCheck = null;
        stateCheck = (TennisMatchState) algoManager.getEventDetails(eventId).getEventState().getMatchState();
        // System.out.println(stateCheck);
        assertEquals(stateCheck.isInTieBreak(), true);
        assertEquals(stateCheck.isInSuperTieBreak(), true);

    }

    @Test
    public void testChampionshipTiebreakGameEnd() {
        MethodName.log();

        long eventId = 12345L;
        TennisMatchFormat tennisMatchFormat = new TennisMatchFormat(true, Sex.MEN, Surface.HARD, TournamentLevel.ITF, 3,
                        FinalSetType.CHAMPIONSHIP_TIE_BREAK, true);
        algoManager.autoSyncWithMatchFeed(false);
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, tennisMatchFormat);

        TennisMatchIncident startingTennisMatchIncident = new TennisMatchIncident();
        startingTennisMatchIncident.setIncidentSubType(TennisMatchIncidentType.MATCH_STARTING);
        startingTennisMatchIncident.setServerSideAtStartOfMatch(TeamId.B);
        startingTennisMatchIncident.setServerPlayerAtStartOfMatch(1);
        startingTennisMatchIncident.setPointWinner(TeamId.UNKNOWN);
        startingTennisMatchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(startingTennisMatchIncident, true);

        startingTennisMatchIncident = null;

        for (int i = 0; i < 4; i++) {
            TennisMatchIncident tennisMatchIncident =
                            new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.B);
            tennisMatchIncident.setEventId(eventId);
            algoManager.handleMatchIncident(tennisMatchIncident, true);

        }

        startingTennisMatchIncident = new TennisMatchIncident();
        startingTennisMatchIncident.setIncidentSubType(TennisMatchIncidentType.MATCH_STARTING);
        startingTennisMatchIncident.setServerSideAtStartOfMatch(TeamId.A);
        startingTennisMatchIncident.setServerPlayerAtStartOfMatch(1);
        startingTennisMatchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(startingTennisMatchIncident, true);

        for (int i = 0; i < 20; i++) {
            TennisMatchIncident tennisMatchIncident =
                            new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.B);
            tennisMatchIncident.setEventId(eventId);
            algoManager.handleMatchIncident(tennisMatchIncident, true);

        }

        // System.out.println(this.publishedMatchState);

        startingTennisMatchIncident = new TennisMatchIncident();
        startingTennisMatchIncident.setIncidentSubType(TennisMatchIncidentType.MATCH_STARTING);
        startingTennisMatchIncident.setServerSideAtStartOfMatch(TeamId.B);
        startingTennisMatchIncident.setServerPlayerAtStartOfMatch(1);
        startingTennisMatchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(startingTennisMatchIncident, true);

        startingTennisMatchIncident = null;

        for (int i = 0; i < 4; i++) {
            TennisMatchIncident tennisMatchIncident =
                            new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A);
            tennisMatchIncident.setEventId(eventId);
            algoManager.handleMatchIncident(tennisMatchIncident, true);

        }

        startingTennisMatchIncident = new TennisMatchIncident();
        startingTennisMatchIncident.setIncidentSubType(TennisMatchIncidentType.MATCH_STARTING);
        startingTennisMatchIncident.setServerSideAtStartOfMatch(TeamId.A);
        startingTennisMatchIncident.setServerPlayerAtStartOfMatch(1);
        startingTennisMatchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(startingTennisMatchIncident, true);

        startingTennisMatchIncident = null;

        for (int i = 0; i < 16; i++) {
            TennisMatchIncident tennisMatchIncident =
                            new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A);
            tennisMatchIncident.setEventId(eventId);
            algoManager.handleMatchIncident(tennisMatchIncident, true);

        }

        for (int i = 0; i < 20; i++) {
            TennisMatchIncident tennisMatchIncident =
                            new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.B);
            tennisMatchIncident.setEventId(eventId);
            algoManager.handleMatchIncident(tennisMatchIncident, true);

        }

        for (int i = 0; i < 4; i++) {
            TennisMatchIncident tennisMatchIncident =
                            new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A);
            tennisMatchIncident.setEventId(eventId);
            algoManager.handleMatchIncident(tennisMatchIncident, true);

        }

        for (int i = 0; i < 4; i++) {
            TennisMatchIncident tennisMatchIncident =
                            new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.B);
            tennisMatchIncident.setEventId(eventId);
            algoManager.handleMatchIncident(tennisMatchIncident, true);

        }

        // System.out.println(this.publishedMatchState);

        for (int i = 0; i < 6; i++) {
            TennisMatchIncident tennisMatchIncident =
                            new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A);
            tennisMatchIncident.setEventId(eventId);
            algoManager.handleMatchIncident(tennisMatchIncident, true);

        }

        // System.out.println(this.publishedMatchState);

        TennisMatchIncident tennisMatchIncident = new TennisMatchIncident();
        tennisMatchIncident.setServerPlayerAtStartOfMatch(1);
        tennisMatchIncident.setIncidentSubType(TennisMatchIncidentType.POINT_WON);
        tennisMatchIncident.setPointWinner(TeamId.A);
        tennisMatchIncident.setServerSideAtStartOfMatch(TeamId.A);
        tennisMatchIncident.setPointResult(TennisPointResult.UNKNOWN);
        tennisMatchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(tennisMatchIncident, true);

        tennisMatchIncident = null;

        // System.out.println(this.publishedMatchState);

        startingTennisMatchIncident = new TennisMatchIncident();
        startingTennisMatchIncident.setIncidentSubType(TennisMatchIncidentType.MATCH_STARTING);
        startingTennisMatchIncident.setServerSideAtStartOfMatch(TeamId.A);
        startingTennisMatchIncident.setServerPlayerAtStartOfMatch(1);
        startingTennisMatchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(startingTennisMatchIncident, true);

        startingTennisMatchIncident = null;

        tennisMatchIncident = new TennisMatchIncident();
        tennisMatchIncident.setServerPlayerAtStartOfMatch(1);
        tennisMatchIncident.setIncidentSubType(TennisMatchIncidentType.POINT_WON);
        tennisMatchIncident.setPointWinner(TeamId.A);
        tennisMatchIncident.setServerSideAtStartOfMatch(TeamId.A);
        tennisMatchIncident.setPointResult(TennisPointResult.UNKNOWN);
        tennisMatchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(tennisMatchIncident, true);

        tennisMatchIncident = null;

        startingTennisMatchIncident = new TennisMatchIncident();
        startingTennisMatchIncident.setIncidentSubType(TennisMatchIncidentType.MATCH_STARTING);
        startingTennisMatchIncident.setServerSideAtStartOfMatch(TeamId.B);
        startingTennisMatchIncident.setServerPlayerAtStartOfMatch(1);
        startingTennisMatchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(startingTennisMatchIncident, true);

        startingTennisMatchIncident = null;

        tennisMatchIncident = new TennisMatchIncident();
        tennisMatchIncident.setServerPlayerAtStartOfMatch(1);
        tennisMatchIncident.setIncidentSubType(TennisMatchIncidentType.POINT_WON);
        tennisMatchIncident.setPointWinner(TeamId.A);
        tennisMatchIncident.setServerSideAtStartOfMatch(TeamId.B);
        tennisMatchIncident.setPointResult(TennisPointResult.UNKNOWN);
        tennisMatchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(tennisMatchIncident, true);

        tennisMatchIncident = null;

        tennisMatchIncident = new TennisMatchIncident();
        tennisMatchIncident.setServerPlayerAtStartOfMatch(1);
        tennisMatchIncident.setIncidentSubType(TennisMatchIncidentType.POINT_WON);
        tennisMatchIncident.setPointWinner(TeamId.B);
        tennisMatchIncident.setServerSideAtStartOfMatch(TeamId.B);
        tennisMatchIncident.setPointResult(TennisPointResult.UNKNOWN);
        tennisMatchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(tennisMatchIncident, true);

        tennisMatchIncident = null;

        tennisMatchIncident = new TennisMatchIncident();
        tennisMatchIncident.setServerPlayerAtStartOfMatch(1);
        tennisMatchIncident.setIncidentSubType(TennisMatchIncidentType.POINT_WON);
        tennisMatchIncident.setPointWinner(TeamId.A);
        tennisMatchIncident.setServerSideAtStartOfMatch(TeamId.B);
        tennisMatchIncident.setPointResult(TennisPointResult.UNKNOWN);
        tennisMatchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(tennisMatchIncident, true);

        tennisMatchIncident = null;

        tennisMatchIncident = new TennisMatchIncident();
        tennisMatchIncident.setServerPlayerAtStartOfMatch(1);
        tennisMatchIncident.setIncidentSubType(TennisMatchIncidentType.POINT_WON);
        tennisMatchIncident.setPointWinner(TeamId.B);
        tennisMatchIncident.setServerSideAtStartOfMatch(TeamId.B);
        tennisMatchIncident.setPointResult(TennisPointResult.UNKNOWN);
        tennisMatchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(tennisMatchIncident, true);

        tennisMatchIncident = null;

        // System.out.println(this.publishedMatchState);

        TennisMatchStateFromFeed tennisMatchStateFromFeed =
                        new TennisMatchStateFromFeed(1, 1, 0, 0, 4, 2, false, TeamId.A);

        tennisMatchIncident = new TennisMatchIncident();
        tennisMatchIncident.setServerPlayerAtStartOfMatch(1);
        tennisMatchIncident.setIncidentSubType(TennisMatchIncidentType.POINT_WON);
        tennisMatchIncident.setPointWinner(TeamId.A);
        tennisMatchIncident.setServerSideAtStartOfMatch(TeamId.B);
        tennisMatchIncident.setPointResult(TennisPointResult.UNKNOWN);
        tennisMatchIncident.setEventId(eventId);
        tennisMatchIncident.setTennisMatchStateFromFeed(tennisMatchStateFromFeed);
        algoManager.handleMatchIncident(tennisMatchIncident, true);

        tennisMatchIncident = null;

        TennisMatchState tennisMatchState =
                        (TennisMatchState) this.algoManager.getEventDetails(eventId).getEventState().getMatchState();

        // System.out.println(this.publishedMatchState);
        // System.out.println(tennisMatchState);

        assertEquals(tennisMatchState.isDatafeedStateMismatch(), false);

    }

    @Test
    public void testServeIssueMatchState() {
        MethodName.log();

        long eventId = 12345L;
        TennisMatchFormat tennisMatchFormat = new TennisMatchFormat(true, Sex.MEN, Surface.HARD, TournamentLevel.ITF, 3,
                        FinalSetType.CHAMPIONSHIP_TIE_BREAK, true);
        algoManager.autoSyncWithMatchFeed(false);
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, tennisMatchFormat);

        TennisMatchIncident startingTennisMatchIncident = new TennisMatchIncident();
        startingTennisMatchIncident.setIncidentSubType(TennisMatchIncidentType.MATCH_STARTING);
        startingTennisMatchIncident.setServerSideAtStartOfMatch(TeamId.A);
        startingTennisMatchIncident.setServerPlayerAtStartOfMatch(1);
        startingTennisMatchIncident.setPointWinner(TeamId.UNKNOWN);
        startingTennisMatchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(startingTennisMatchIncident, true);

        startingTennisMatchIncident = null;

        startingTennisMatchIncident = null;

        for (int i = 0; i < 4; i++) {
            TennisMatchIncident tennisMatchIncident =
                            new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A);
            tennisMatchIncident.setEventId(eventId);
            algoManager.handleMatchIncident(tennisMatchIncident, true);

        }

        startingTennisMatchIncident = new TennisMatchIncident();
        startingTennisMatchIncident.setIncidentSubType(TennisMatchIncidentType.MATCH_STARTING);
        startingTennisMatchIncident.setServerSideAtStartOfMatch(TeamId.B);
        startingTennisMatchIncident.setServerPlayerAtStartOfMatch(1);
        startingTennisMatchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(startingTennisMatchIncident, true);

        for (int i = 0; i < 16; i++) {
            TennisMatchIncident tennisMatchIncident =
                            new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A);
            tennisMatchIncident.setEventId(eventId);
            algoManager.handleMatchIncident(tennisMatchIncident, true);

        }

        for (int i = 0; i < 12; i++) {
            TennisMatchIncident tennisMatchIncident =
                            new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.B);
            tennisMatchIncident.setEventId(eventId);
            algoManager.handleMatchIncident(tennisMatchIncident, true);

        }

        for (int i = 0; i < 3; i++) {
            TennisMatchIncident tennisMatchIncident =
                            new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A);
            tennisMatchIncident.setEventId(eventId);
            algoManager.handleMatchIncident(tennisMatchIncident, true);

        }

        for (int i = 0; i < 2; i++) {
            TennisMatchIncident tennisMatchIncident =
                            new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.B);
            tennisMatchIncident.setEventId(eventId);
            algoManager.handleMatchIncident(tennisMatchIncident, true);

        }

        TennisMatchIncident tennisMatchIncident = new TennisMatchIncident();
        tennisMatchIncident.setServerPlayerAtStartOfMatch(1);
        tennisMatchIncident.setIncidentSubType(TennisMatchIncidentType.POINT_WON);
        tennisMatchIncident.setPointWinner(TeamId.A);
        tennisMatchIncident.setServerSideAtStartOfMatch(TeamId.B);
        tennisMatchIncident.setPointResult(TennisPointResult.UNKNOWN);
        tennisMatchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(tennisMatchIncident, true);

        tennisMatchIncident = null;

        startingTennisMatchIncident = new TennisMatchIncident();
        startingTennisMatchIncident.setIncidentSubType(TennisMatchIncidentType.MATCH_STARTING);
        startingTennisMatchIncident.setServerSideAtStartOfMatch(TeamId.B);
        startingTennisMatchIncident.setServerPlayerAtStartOfMatch(1);
        startingTennisMatchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(startingTennisMatchIncident, true);

        startingTennisMatchIncident = null;

        tennisMatchIncident = new TennisMatchIncident();
        tennisMatchIncident.setIncidentSubType(TennisMatchIncidentType.POINT_WON);
        tennisMatchIncident.setServerSideAtStartOfMatch(TeamId.B);
        tennisMatchIncident.setServerPlayerAtStartOfMatch(0);
        tennisMatchIncident.setPointWinner(TeamId.B);
        tennisMatchIncident.setPointResult(TennisPointResult.UNKNOWN);
        tennisMatchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(tennisMatchIncident, true);
        tennisMatchIncident = null;

        tennisMatchIncident = new TennisMatchIncident();
        tennisMatchIncident.setIncidentSubType(TennisMatchIncidentType.POINT_WON);
        tennisMatchIncident.setServerSideAtStartOfMatch(TeamId.B);
        tennisMatchIncident.setServerPlayerAtStartOfMatch(0);
        tennisMatchIncident.setPointWinner(TeamId.B);
        tennisMatchIncident.setPointResult(TennisPointResult.UNKNOWN);
        tennisMatchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(tennisMatchIncident, true);
        tennisMatchIncident = null;

        TennisMatchStateFromFeed tennisMatchStateFromFeed =
                        new TennisMatchStateFromFeed(1, 0, 0, 0, 0, 3, false, TeamId.B);

        tennisMatchIncident = new TennisMatchIncident();
        tennisMatchIncident.setIncidentSubType(TennisMatchIncidentType.POINT_WON);
        tennisMatchIncident.setServerSideAtStartOfMatch(TeamId.B);
        tennisMatchIncident.setServerPlayerAtStartOfMatch(0);
        tennisMatchIncident.setPointWinner(TeamId.B);
        tennisMatchIncident.setPointResult(TennisPointResult.UNKNOWN);
        tennisMatchIncident.setEventId(eventId);
        tennisMatchIncident.setTennisMatchStateFromFeed(tennisMatchStateFromFeed);
        algoManager.handleMatchIncident(tennisMatchIncident, true);
        tennisMatchIncident = null;
        tennisMatchStateFromFeed = null;

        // System.out.println(this.publishedMatchState);

        tennisMatchStateFromFeed = new TennisMatchStateFromFeed(1, 0, 0, 1, 0, 4, false, TeamId.B);

        tennisMatchIncident = new TennisMatchIncident();
        tennisMatchIncident.setIncidentSubType(TennisMatchIncidentType.POINT_WON);
        tennisMatchIncident.setServerSideAtStartOfMatch(TeamId.B);
        tennisMatchIncident.setServerPlayerAtStartOfMatch(0);
        tennisMatchIncident.setPointWinner(TeamId.B);
        tennisMatchIncident.setPointResult(TennisPointResult.UNKNOWN);
        tennisMatchIncident.setEventId(eventId);
        tennisMatchIncident.setTennisMatchStateFromFeed(tennisMatchStateFromFeed);
        algoManager.handleMatchIncident(tennisMatchIncident, true);
        tennisMatchIncident = null;
        tennisMatchStateFromFeed = null;

        // System.out.println(this.publishedMatchState);

        startingTennisMatchIncident = new TennisMatchIncident();
        startingTennisMatchIncident.setIncidentSubType(TennisMatchIncidentType.MATCH_STARTING);
        startingTennisMatchIncident.setServerSideAtStartOfMatch(TeamId.A);
        startingTennisMatchIncident.setServerPlayerAtStartOfMatch(0);
        startingTennisMatchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(startingTennisMatchIncident, true);

        // System.out.println(this.publishedMatchState);

        startingTennisMatchIncident = null;

        tennisMatchIncident = new TennisMatchIncident();
        tennisMatchIncident.setIncidentSubType(TennisMatchIncidentType.FAULT);
        tennisMatchIncident.setServerPlayerAtStartOfMatch(0);
        tennisMatchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(tennisMatchIncident, true);
        tennisMatchIncident = null;
        tennisMatchStateFromFeed = null;

        // System.out.println(this.publishedMatchState);

        // tennisMatchStateFromFeed = new TennisMatchStateFromFeed(1, 0, 0, 1,
        // 1, 0, TeamId.A);
        tennisMatchIncident = new TennisMatchIncident();
        tennisMatchIncident.setIncidentSubType(TennisMatchIncidentType.POINT_WON);
        tennisMatchIncident.setServerSideAtStartOfMatch(TeamId.A);
        tennisMatchIncident.setServerPlayerAtStartOfMatch(0);
        tennisMatchIncident.setPointWinner(TeamId.A);
        tennisMatchIncident.setPointResult(TennisPointResult.UNKNOWN);
        tennisMatchIncident.setEventId(eventId);
        // tennisMatchIncident.setTennisMatchStateFromFeed(tennisMatchStateFromFeed);
        algoManager.handleMatchIncident(tennisMatchIncident, true);
        tennisMatchIncident = null;
        tennisMatchStateFromFeed = null;

        // System.out.println(this.publishedMatchState);

        TennisMatchState tennisMatchState =
                        (TennisMatchState) this.algoManager.getEventDetails(eventId).getEventState().getMatchState();

        // System.out.println(this.publishedMatchState);
        // System.out.println(tennisMatchState);

        assertEquals(tennisMatchState.getOnServeNow(), TeamId.A);
        assertEquals(tennisMatchState.getOnServeTeamInFirstGameOfSet(), TeamId.B);

    }

    @Test
    public void testTiebreakIssueMatchState() {
        MethodName.log();

        long eventId = 12345L;
        TennisMatchFormat tennisMatchFormat = new TennisMatchFormat(true, Sex.MEN, Surface.HARD, TournamentLevel.ITF, 3,
                        FinalSetType.CHAMPIONSHIP_TIE_BREAK, true);
        algoManager.autoSyncWithMatchFeed(false);
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, tennisMatchFormat);

        TennisMatchIncident startingTennisMatchIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A, 1);
        startingTennisMatchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(startingTennisMatchIncident, true);

        for (int i = 0; i < 4; i++) {
            TennisMatchIncident tennisMatchIncident =
                            new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A);
            tennisMatchIncident.setEventId(eventId);
            algoManager.handleMatchIncident(tennisMatchIncident, true);

        }

        TennisMatchIncident servingTennisMatchIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.B, 1);
        servingTennisMatchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(servingTennisMatchIncident, true);

        for (int i = 0; i < 16; i++) {
            TennisMatchIncident tennisMatchIncident =
                            new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A);
            tennisMatchIncident.setEventId(eventId);
            algoManager.handleMatchIncident(tennisMatchIncident, true);

        }

        for (int i = 0; i < 20; i++) {
            TennisMatchIncident tennisMatchIncident =
                            new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.B);
            tennisMatchIncident.setEventId(eventId);
            algoManager.handleMatchIncident(tennisMatchIncident, true);

        }

        for (int i = 0; i < 4; i++) {
            TennisMatchIncident tennisMatchIncident =
                            new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A);
            tennisMatchIncident.setEventId(eventId);
            algoManager.handleMatchIncident(tennisMatchIncident, true);

        }

        for (int i = 0; i < 3; i++) {
            TennisMatchIncident tennisMatchIncident =
                            new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.B);
            tennisMatchIncident.setEventId(eventId);
            algoManager.handleMatchIncident(tennisMatchIncident, true);

        }

        for (int i = 0; i < 3; i++) {
            TennisMatchIncident tennisMatchIncident =
                            new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A);
            tennisMatchIncident.setEventId(eventId);
            algoManager.handleMatchIncident(tennisMatchIncident, true);

        }

        for (int i = 0; i < 1; i++) {
            TennisMatchIncident tennisMatchIncident =
                            new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.B);
            tennisMatchIncident.setEventId(eventId);
            algoManager.handleMatchIncident(tennisMatchIncident, true);

        }

        algoManager.handleMatchIncident(getMatchIncident(null, eventId), true);
        algoManager.handleMatchIncident(getMatchIncident(TeamId.B, eventId), true);
        algoManager.handleMatchIncident(getMatchIncident(null, eventId), true);
        algoManager.handleMatchIncident(getMatchIncident(TeamId.B, eventId), true);
        algoManager.handleMatchIncident(getMatchIncident(null, eventId), true);
        algoManager.handleMatchIncident(getMatchIncident(TeamId.A, eventId), true);
        algoManager.handleMatchIncident(getMatchIncident(null, eventId), true);
        algoManager.handleMatchIncident(getMatchIncident(TeamId.A, eventId), true);
        algoManager.handleMatchIncident(getMatchIncident(null, eventId), true);
        algoManager.handleMatchIncident(getMatchIncident(TeamId.B, eventId), true);
        algoManager.handleMatchIncident(getMatchIncident(TeamId.B, eventId), true);
        algoManager.handleMatchIncident(getMatchIncident(TeamId.B, eventId), true);
        algoManager.handleMatchIncident(getMatchIncident(null, eventId), true);
        algoManager.handleMatchIncident(getMatchIncident(TeamId.B, eventId), true);
        algoManager.handleMatchIncident(getMatchIncident(TeamId.A, eventId), true);

        // System.out.println(this.publishedMatchState);

        TennisMatchStateFromFeed tennisMatchStateFromFeed =
                        new TennisMatchStateFromFeed(0, 1, 6, 7, 3, 7, false, TeamId.B);
        TennisMatchIncident tennisMatchIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.B);
        tennisMatchIncident.setMatchStateFromFeed(tennisMatchStateFromFeed);
        tennisMatchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(tennisMatchIncident, true);

        tennisMatchIncident = null;
        servingTennisMatchIncident = null;
        startingTennisMatchIncident = null;
        tennisMatchStateFromFeed = null;

        servingTennisMatchIncident = new TennisMatchIncident();
        servingTennisMatchIncident.setIncidentSubType(TennisMatchIncidentType.MATCH_STARTING);
        servingTennisMatchIncident.setServerSideAtStartOfMatch(TeamId.B);
        servingTennisMatchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(servingTennisMatchIncident, true);

        // System.out.println(this.publishedMatchState);

        tennisMatchStateFromFeed = new TennisMatchStateFromFeed(0, 1, 0, 0, 1, 0, false, TeamId.B);
        tennisMatchIncident = new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A);
        tennisMatchIncident.setMatchStateFromFeed(tennisMatchStateFromFeed);
        tennisMatchIncident.setEventId(eventId);
        // System.out.println(tennisMatchIncident);
        algoManager.handleMatchIncident(tennisMatchIncident, true);

        TennisMatchState tennisMatchState =
                        (TennisMatchState) this.algoManager.getEventDetails(eventId).getEventState().getMatchState();

        assertEquals(tennisMatchState.getOnServeNow(), TeamId.B);
        assertEquals(tennisMatchState.getOnServeTeamInFirstGameOfSet(), TeamId.B);

        // algoManager.handleMatchIncident(getMatchIncident(TeamId.B, eventId),
        // true);

        // System.out.println(this.publishedMatchState);

    }

    @Test
    public void testSecondSetTiebreakMismatch() {
        MethodName.log();

        long eventId = 12345L;
        TennisMatchFormat tennisMatchFormat = new TennisMatchFormat(true, Sex.MEN, Surface.IHARD, TournamentLevel.ITF,
                        3, FinalSetType.CHAMPIONSHIP_TIE_BREAK, true);
        algoManager.autoSyncWithMatchFeed(false);
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, tennisMatchFormat);

        TennisMatchIncident startingTennisMatchIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.B);
        startingTennisMatchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(startingTennisMatchIncident, true);

        for (int i = 0; i < 4; i++) {
            TennisMatchIncident tennisMatchIncident =
                            new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A);
            tennisMatchIncident.setEventId(eventId);
            algoManager.handleMatchIncident(tennisMatchIncident, true);

        }

        TennisMatchIncident servingTennisMatchIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.SERVING_ORDER, TeamId.A);
        servingTennisMatchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(servingTennisMatchIncident, true);

        for (int i = 0; i < 16; i++) {
            TennisMatchIncident tennisMatchIncident =
                            new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A);
            tennisMatchIncident.setEventId(eventId);
            algoManager.handleMatchIncident(tennisMatchIncident, true);

        }
        for (int i = 0; i < 4; i++) {
            TennisMatchIncident tennisMatchIncident =
                            new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.B);
            tennisMatchIncident.setEventId(eventId);
            algoManager.handleMatchIncident(tennisMatchIncident, true);

        }

        for (int i = 0; i < 4; i++) {
            TennisMatchIncident tennisMatchIncident =
                            new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A);
            tennisMatchIncident.setEventId(eventId);
            algoManager.handleMatchIncident(tennisMatchIncident, true);

        }

        // System.out.println(this.publishedMatchState);

        servingTennisMatchIncident = new TennisMatchIncident(0, TennisMatchIncidentType.SERVING_ORDER, TeamId.A);
        servingTennisMatchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(servingTennisMatchIncident, true);

        for (int i = 0; i < 4; i++) {
            TennisMatchIncident tennisMatchIncident =
                            new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A);
            tennisMatchIncident.setEventId(eventId);
            algoManager.handleMatchIncident(tennisMatchIncident, true);

        }

        servingTennisMatchIncident = new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.B);
        servingTennisMatchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(servingTennisMatchIncident, true);

        for (int i = 0; i < 16; i++) {
            TennisMatchIncident tennisMatchIncident =
                            new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A);
            tennisMatchIncident.setEventId(eventId);
            algoManager.handleMatchIncident(tennisMatchIncident, true);

        }

        for (int i = 0; i < 24; i++) {
            TennisMatchIncident tennisMatchIncident =
                            new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.B);
            tennisMatchIncident.setEventId(eventId);
            algoManager.handleMatchIncident(tennisMatchIncident, true);

        }

        for (int i = 0; i < 4; i++) {
            TennisMatchIncident tennisMatchIncident =
                            new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A);
            tennisMatchIncident.setEventId(eventId);
            algoManager.handleMatchIncident(tennisMatchIncident, true);

        }

        TennisMatchIncident tennisMatchIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A);
        tennisMatchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(tennisMatchIncident, true);

        tennisMatchIncident = new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.B);
        tennisMatchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(tennisMatchIncident, true);

        tennisMatchIncident = new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A);
        tennisMatchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(tennisMatchIncident, true);

        tennisMatchIncident = new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A);
        tennisMatchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(tennisMatchIncident, true);

        tennisMatchIncident = new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A);
        tennisMatchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(tennisMatchIncident, true);

        tennisMatchIncident = new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.B);
        tennisMatchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(tennisMatchIncident, true);

        tennisMatchIncident = new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.B);
        tennisMatchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(tennisMatchIncident, true);

        tennisMatchIncident = new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.B);
        tennisMatchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(tennisMatchIncident, true);

        tennisMatchIncident = new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.B);
        tennisMatchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(tennisMatchIncident, true);

        TennisMatchStateFromFeed tennisMatchStateFromFeed =
                        new TennisMatchStateFromFeed(1, 0, 6, 6, 4, 6, false, TeamId.B);
        tennisMatchIncident = new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.B);
        tennisMatchIncident.setEventId(eventId);
        tennisMatchIncident.setTennisMatchStateFromFeed(tennisMatchStateFromFeed);
        algoManager.handleMatchIncident(tennisMatchIncident, true);

        // System.out.println(this.publishedMatchState);

        tennisMatchStateFromFeed = new TennisMatchStateFromFeed(1, 1, 6, 7, 4, 7, false, TeamId.B);
        tennisMatchIncident = new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.B);
        tennisMatchIncident.setEventId(eventId);
        tennisMatchIncident.setTennisMatchStateFromFeed(tennisMatchStateFromFeed);
        algoManager.handleMatchIncident(tennisMatchIncident, true);
        assertFalse(this.algoManager.getEventDetails(eventId).getEventState().getMatchState()
                        .isDatafeedStateMismatch());
        // System.out.println(this.publishedMatchState);

    }

    @Test
    public void testUndoForPPBF() {
        MethodName.log();
        long eventId = 12345L;
        TennisMatchFormat tennisMatchFormat = new TennisMatchFormat();
        algoManager.autoSyncWithMatchFeed(false);
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, tennisMatchFormat);

        TennisMatchIncident startingTennisMatchIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.B);
        startingTennisMatchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(startingTennisMatchIncident, true);

        for (int i = 1; i < 5; i++) {
            algoManager.handleMatchIncident(getPointWonIncident(eventId, "R" + i, TeamId.A), true);
        }

        for (int i = 5; i < 9; i++) {
            algoManager.handleMatchIncident(getPointWonIncident(eventId, "R" + i, TeamId.B), true);
        }

        // System.out.println(super.publishedMatchState);

        traderAlert = null;

        TennisMatchStateFromFeed tennisMatchStateFromFeed =
                        new TennisMatchStateFromFeed(0, 0, 1, 1, 0, 1, false, TeamId.B);
        TennisMatchIncident tennisMatchIncident9 = getPointWonIncident(eventId, "1091564546", TeamId.B);
        tennisMatchIncident9.setTennisMatchStateFromFeed(tennisMatchStateFromFeed);
        algoManager.handleMatchIncident(tennisMatchIncident9, true);

        assertTrue(traderAlert == null);

        tennisMatchStateFromFeed = new TennisMatchStateFromFeed(0, 0, 1, 1, 0, 2, false, TeamId.B);
        TennisMatchIncident tennisMatchIncident10 = getPointWonIncident(eventId, "1091565774", TeamId.B);
        tennisMatchIncident10.setTennisMatchStateFromFeed(tennisMatchStateFromFeed);
        algoManager.handleMatchIncident(tennisMatchIncident10, true);

        algoManager.handleUndoLastMatchIncident(eventId);

        tennisMatchStateFromFeed = new TennisMatchStateFromFeed(0, 0, 1, 1, 0, 2, false, TeamId.B);
        TennisMatchIncident tennisMatchIncident11 = getPointWonIncident(eventId, "1091565872", TeamId.B);
        tennisMatchIncident11.setTennisMatchStateFromFeed(tennisMatchStateFromFeed);
        algoManager.handleMatchIncident(tennisMatchIncident11, true);
        assertTrue(traderAlert == null);

        // System.out.println(super.publishedMatchState);
        // System.out.println();

    }

    @Test
    public void testChampionshipTieBreak() {
        MethodName.log();

        long eventId = 12345L;
        TennisMatchFormat tennisMatchFormat = new TennisMatchFormat(true, Sex.MEN, Surface.CLAY, TournamentLevel.ITF, 3,
                        FinalSetType.CHAMPIONSHIP_TIE_BREAK, true);
        algoManager.autoSyncWithMatchFeed(false);
        algoManager.usingParamFindTradingRules(false);
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, tennisMatchFormat);
        TennisMatchIncident startingTennisMatchIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A);
        startingTennisMatchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(startingTennisMatchIncident, true);

        int incidentCount = 1;

        for (int i = 1; i < 5; i++) {
            algoManager.handleMatchIncident(getPointWonIncident(eventId, "R" + incidentCount, TeamId.B), true);
            incidentCount++;
        }

        TennisMatchIncident serverIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.SERVING_ORDER, TeamId.B);
        serverIncident.setEventId(eventId);
        algoManager.handleMatchIncident(serverIncident, true);

        for (int i = 5; i < 25; i++) {
            algoManager.handleMatchIncident(getPointWonIncident(eventId, "R" + incidentCount, TeamId.B), true);
            incidentCount++;
        }

        // System.out.println(this.publishedMatchState);
        // System.out.println(this.publishedMatchState);

        serverIncident = new TennisMatchIncident(0, TennisMatchIncidentType.SERVING_ORDER, TeamId.A);
        serverIncident.setEventId(eventId);
        algoManager.handleMatchIncident(serverIncident, true);

        for (int j = 1; j < 5; j++) {
            algoManager.handleMatchIncident(getPointWonIncident(eventId, "R" + incidentCount, TeamId.A), true);
            incidentCount++;
        }

        serverIncident = new TennisMatchIncident(0, TennisMatchIncidentType.SERVING_ORDER, TeamId.B);
        serverIncident.setEventId(eventId);
        algoManager.handleMatchIncident(serverIncident, true);

        for (int j = 5; j < 24; j++) {
            algoManager.handleMatchIncident(getPointWonIncident(eventId, "R" + incidentCount, TeamId.A), true);
            incidentCount++;
        }
        // System.out.println(incidentCount);
        // System.out.println(this.publishedMatchState);

        for (int k = 1; k < 21; k++) {
            algoManager.handleMatchIncident(getPointWonIncident(eventId, "R" + incidentCount, TeamId.B), true);
            incidentCount++;
        }
        // System.out.println(this.publishedMatchState);
        // System.out.println();

        for (int k = 22; k < 25; k++) {
            algoManager.handleMatchIncident(getPointWonIncident(eventId, "R" + incidentCount, TeamId.A), true);
            incidentCount++;
            // System.out.println(this.publishedMatchState);
            // System.out.println();
        }

        // System.out.println(this.publishedMatchState);

        TennisMatchIncident tennisIncident = new TennisMatchIncident(5200, TennisMatchIncidentType.POINT_WON, TeamId.A);
        tennisIncident.setEventId(eventId);

        TennisMatchStateFromFeed tennisMatchStateFromFeed =
                        new TennisMatchStateFromFeed(1, 1, 6, 4, 4, 3, false, TeamId.B);
        tennisIncident.setTennisMatchStateFromFeed(tennisMatchStateFromFeed);

        algoManager.handleMatchIncident(tennisIncident, true);

        // System.out.println(this.publishedMatchState);
        // System.out.println();

        serverIncident = null;
        serverIncident = new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.B);
        serverIncident.setEventId(eventId);
        algoManager.handleMatchIncident(serverIncident, true);

        // System.out.println(this.publishedMatchState);
        // System.out.println();

        algoManager.handleMatchIncident(getPointWonIncident(eventId, "R" + incidentCount, TeamId.A), true);
        incidentCount++;
        // System.out.println(this.publishedMatchState);
        // System.out.println();

        serverIncident = null;
        serverIncident = new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A);
        serverIncident.setEventId(eventId);
        algoManager.handleMatchIncident(serverIncident, true);

        // System.out.println(this.publishedMatchState);
        // System.out.println();

        algoManager.handleMatchIncident(getPointWonIncident(eventId, "R" + incidentCount, TeamId.A), true);
        incidentCount++;
        // System.out.println(this.publishedMatchState);
        // System.out.println();

        algoManager.handleMatchIncident(getPointWonIncident(eventId, "R" + incidentCount, TeamId.A), true);
        incidentCount++;
        // System.out.println(this.publishedMatchState);
        // System.out.println();

        algoManager.handleMatchIncident(getPointWonIncident(eventId, "R" + incidentCount, TeamId.B), true);
        incidentCount++;
        // System.out.println(this.publishedMatchState);
        // System.out.println();

        algoManager.handleMatchIncident(getPointWonIncident(eventId, "R" + incidentCount, TeamId.B), true);
        incidentCount++;
        // System.out.println(this.publishedMatchState);
        // System.out.println();

        algoManager.handleMatchIncident(getPointWonIncident(eventId, "R" + incidentCount, TeamId.A), true);
        incidentCount++;
        // System.out.println(this.publishedMatchState);
        // System.out.println();

        assertTrue(algoManager.getEventDetails(eventId).getEventState().getMatchState()
                        .isDataFeedStateMismatchCleared());
        assertFalse(algoManager.getEventDetails(eventId).getEventState().getMatchState().isDatafeedStateMismatch());
        assertTrue(!this.publishedMatchState.isMatchCompleted());

    }

    @Test
    public void testEndMatch2() {
        MethodName.log();
        long eventId = 12345L;
        TennisMatchFormat tennisMatchFormat = new TennisMatchFormat();
        algoManager.autoSyncWithMatchFeed(true);
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, tennisMatchFormat);
        TennisMatchIncident startingTennisMatchIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A);
        startingTennisMatchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(startingTennisMatchIncident, true);

        algoManager.handleMatchIncident(getPointWonIncident(eventId, "R1", TeamId.A), true);
        algoManager.handleMatchIncident(getPointWonIncident(eventId, "R2", TeamId.A), true);
        assertNull(traderAlert);
        assertFalse(algoManager.getMatchState(eventId).isDatafeedStateMismatch());
        TennisMatchStateFromFeed tennisMatchStateFromFeed =
                        new TennisMatchStateFromFeed(0, 0, 5, 6, 0, 0, false, TeamId.A);

        TennisMatchIncident tennisMatchIncident1 = getPointWonIncident(eventId, "R3", TeamId.A);
        tennisMatchIncident1.setTennisMatchStateFromFeed(tennisMatchStateFromFeed);
        // System.out.println(publishedMarkets);

        publishedMarkets = null;
        algoManager.handleMatchIncident(tennisMatchIncident1, true);
        // System.out.println(super.publishedMatchState);
        assertFalse(algoManager.getMatchState(eventId).isDatafeedStateMismatch());
        assertFalse(((TennisSimpleMatchState) super.publishedMatchState).isTieBreak());

        /**
         * Create end match incident
         */
        tennisMatchStateFromFeed = new TennisMatchStateFromFeed(1, 2, 6, 7, 4, 7, false, TeamId.B);
        tennisMatchIncident1 = getPointWonIncident(eventId, "R3", TeamId.B);
        tennisMatchIncident1.setTennisMatchStateFromFeed(tennisMatchStateFromFeed);
        algoManager.handleMatchIncident(tennisMatchIncident1, true);
        assertFalse(algoManager.getMatchState(eventId).isDatafeedStateMismatch());
        assertFalse(((TennisSimpleMatchState) super.publishedMatchState).isTieBreak());
        assertTrue(((TennisSimpleMatchState) super.publishedMatchState).isMatchCompleted());
        // System.out.println(this.publishedMarkets);

    }

    @Test
    public void testEndMatch() {
        MethodName.log();
        long eventId = 12345L;
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, new TennisMatchFormat());
        TennisMatchIncident startingTennisMatchIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A);
        startingTennisMatchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(startingTennisMatchIncident, true);

        algoManager.handleMatchIncident(getPointWonIncident(eventId, "R1", TeamId.A), true);
        algoManager.handleMatchIncident(getPointWonIncident(eventId, "R2", TeamId.A), true);
        assertNull(traderAlert);
        assertFalse(algoManager.getMatchState(eventId).isDatafeedStateMismatch());
        TennisMatchStateFromFeed tennisMatchStateFromFeed =
                        new TennisMatchStateFromFeed(0, 0, 5, 6, 0, 0, false, TeamId.A);

        TennisMatchIncident tennisMatchIncident1 = getPointWonIncident(eventId, "R3", TeamId.A);
        tennisMatchIncident1.setTennisMatchStateFromFeed(tennisMatchStateFromFeed);
        // System.out.println(publishedMarkets);

        publishedMarkets = null;
        algoManager.handleMatchIncident(tennisMatchIncident1, true);
        assertFalse(algoManager.getMatchState(eventId).isDatafeedStateMismatch());
        assertFalse(((TennisSimpleMatchState) super.publishedMatchState).isTieBreak());

        /**
         * Create end match incident
         */
        tennisMatchStateFromFeed = new TennisMatchStateFromFeed(0, 2, 0, 0, 0, 0, false, TeamId.A);
        tennisMatchIncident1 = getEndMatchIncident(eventId, "R3", TeamId.B);
        tennisMatchIncident1.setTennisMatchStateFromFeed(tennisMatchStateFromFeed);
        algoManager.handleMatchIncident(tennisMatchIncident1, true);
        assertFalse(algoManager.getMatchState(eventId).isDatafeedStateMismatch());
        assertFalse(((TennisSimpleMatchState) super.publishedMatchState).isTieBreak());
        assertTrue(((TennisSimpleMatchState) super.publishedMatchState).isMatchCompleted());
        // System.out.println(this.publishedMarkets);

    }

    @Test
    public void test1() {
        MethodName.log();
        long eventId = 3679L;
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, new TennisMatchFormat());
        TennisMatchIncident startingTennisMatchIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A);
        startingTennisMatchIncident.setEventId(eventId);
        startingTennisMatchIncident.setIncidentId("MATCH_STARTING");
        algoManager.handleMatchIncident(startingTennisMatchIncident, true);

        algoManager.handleMatchIncident(getPointWonIncident(eventId, "R1", TeamId.A), true);
        algoManager.handleMatchIncident(getPointWonIncident(eventId, "R2", TeamId.A), true);
        assertNull(traderAlert);
        // // System.out.println(publishedMatchState);
        assertFalse(algoManager.getMatchState(eventId).isDatafeedStateMismatch());
        /*
         * Score should be 30-0, send in incident that takes score to 40-0 but saying feed thinks it is 30-40
         */
        TennisMatchStateFromFeed tennisMatchStateFromFeed =
                        new TennisMatchStateFromFeed(0, 0, 0, 0, 2, 3, false, TeamId.A);
        TennisMatchIncident tennisMatchIncident1 = getPointWonIncident(eventId, "R3", TeamId.A);
        tennisMatchIncident1.setTennisMatchStateFromFeed(tennisMatchStateFromFeed);
        // System.out.println(publishedMarkets);

        publishedMarkets = null;
        algoManager.handleMatchIncident(tennisMatchIncident1, true);
        // miss match setted to false in the TennisMatchState
        assertFalse(algoManager.getMatchState(eventId).isDatafeedStateMismatch());
        // // System.out.println(traderAlert);
        assertNull(traderAlert);
        // assertEquals(TraderAlertType.MATCH_STATE_MISMATCH,
        // traderAlert.getTraderAlertType());
        // // System.out.println(recordedItem);

        /*
         * undo the score from 40-0 back to to 30-0. Should send mismatch cleared alert
         */
        // System.out.println(publishedMatchState);
        traderAlert = null;
        publishedMarkets = null;
        publishedMatchState = null;
        algoManager.handleUndoLastMatchIncident(eventId);
        // // System.out.println(publishedMatchState);
        // assertEquals(TraderAlertType.MATCH_STATE_MISMATCH_CLEARED,
        // traderAlert.getTraderAlertType());
        assertFalse(publishedMarkets != null);
        // // System.out.println(recordedItem);
        // // System.out.println(super.publishedMatchState);
        /*
         * Simulate manual recovery by trader by issuing two incidents with null TennisMatchStateFromFeed objects to
         * take the score back to 30-0,
         */
        traderAlert = null;
        algoManager.handleMatchIncident(getPointWonIncident(eventId, "R4", TeamId.B), true);
        // System.out.println(publishedMatchState);
        assertEquals(null, traderAlert);
        traderAlert = null;
        algoManager.handleMatchIncident(getPointWonIncident(eventId, "R5", TeamId.B), true);
        assertEquals(null, traderAlert);
        // System.out.println(publishedMatchState);
        /*
         * Issue incident to take score to 30-40. with tennisMatchStateFromFeed set to 30-40 also. Should be in sync so
         * no traderAlert expected.
         */
        TennisMatchIncident tennisMatchIncident2 = getPointWonIncident(eventId, "R6", TeamId.B);
        tennisMatchIncident2.setTennisMatchStateFromFeed(tennisMatchStateFromFeed);
        algoManager.handleMatchIncident(tennisMatchIncident2, true);
        assertEquals(null, traderAlert);
        /*
         * fire in another incident to take score to 0-1 0-0, but leave score per feed at 0-0 15-40
         */
        traderAlert = null;
        publishedMarkets = null;
        TennisMatchIncident tennisMatchIncident3 = getPointWonIncident(eventId, "R7", TeamId.B);
        tennisMatchIncident3.setTennisMatchStateFromFeed(tennisMatchStateFromFeed);
        algoManager.handleMatchIncident(tennisMatchIncident3, true);
        // assertEquals(TraderAlertType.MATCH_STATE_MISMATCH,
        // traderAlert.getTraderAlertType());

        /*
         * fire in another incident to take score to 0-1 0-15, this time with correct score per feed
         */
        traderAlert = null;
        TennisMatchIncident tennisMatchIncident4 = getPointWonIncident(eventId, "R8", TeamId.B);
        tennisMatchIncident4
                        .setTennisMatchStateFromFeed(new TennisMatchStateFromFeed(0, 0, 0, 1, 0, 1, false, TeamId.A));
        algoManager.handleMatchIncident(tennisMatchIncident4, true);
        // System.out.println(super.publishedMatchState);
        // assertEquals(TraderAlertType.MATCH_STATE_MISMATCH_CLEARED,
        // traderAlert.getTraderAlertType());

        /*
         * forward the score to 0-2 and leave points score non-zero as is actually done by the feed.
         */
        traderAlert = null;
        TennisMatchIncident tennisMatchIncident5 = getPointWonIncident(eventId, "R9", TeamId.B);
        tennisMatchIncident5
                        .setTennisMatchStateFromFeed(new TennisMatchStateFromFeed(0, 0, 0, 1, 0, 2, false, TeamId.A));
        algoManager.handleMatchIncident(tennisMatchIncident5, true);
        assertEquals(null, traderAlert);
        TennisMatchIncident tennisMatchIncident6 = getPointWonIncident(eventId, "R10", TeamId.B);
        tennisMatchIncident6
                        .setTennisMatchStateFromFeed(new TennisMatchStateFromFeed(0, 0, 0, 1, 0, 3, false, TeamId.A));
        algoManager.handleMatchIncident(tennisMatchIncident6, true);
        assertEquals(null, traderAlert);
        TennisMatchIncident tennisMatchIncident7 = getPointWonIncident(eventId, "R11", TeamId.B);
        tennisMatchIncident7
                        .setTennisMatchStateFromFeed(new TennisMatchStateFromFeed(0, 0, 0, 2, 0, 4, false, TeamId.A));
        algoManager.handleMatchIncident(tennisMatchIncident7, true);
        assertEquals(null, traderAlert);
    }

    @Test
    public void test2() {
        MethodName.log();
        long eventId = 12345L;
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, new TennisMatchFormat());
        TennisMatchIncident startingTennisMatchIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A);
        startingTennisMatchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(startingTennisMatchIncident, true);

        algoManager.handleMatchIncident(getPointWonIncident(eventId, "R1", TeamId.A), true);
        algoManager.handleMatchIncident(getPointWonIncident(eventId, "R2", TeamId.A), true);
        assertNull(traderAlert);
        // // System.out.println(publishedMatchState);
        assertFalse(algoManager.getMatchState(eventId).isDatafeedStateMismatch());
        /*
         * Try reproduce the tie break error seen in the logs
         */
        TennisMatchStateFromFeed tennisMatchStateFromFeed =
                        new TennisMatchStateFromFeed(0, 0, 5, 6, 0, 0, false, TeamId.A);

        TennisMatchIncident tennisMatchIncident1 = getPointWonIncident(eventId, "R3", TeamId.A);
        tennisMatchIncident1.setTennisMatchStateFromFeed(tennisMatchStateFromFeed);
        // System.out.println(publishedMarkets);

        publishedMarkets = null;
        algoManager.handleMatchIncident(tennisMatchIncident1, true);
        // miss match setted to false in the TennisMatchState
        assertFalse(algoManager.getMatchState(eventId).isDatafeedStateMismatch());
        // assertTrue(((TennisMatchState)super.publishedMatchState).isInTieBreak());
        // Should not be in tie break
        assertFalse(((TennisSimpleMatchState) super.publishedMatchState).isTieBreak());
    }

    // Ignore below test, after we force miss match alert to be false.
    @Test
    @Ignore
    public void test() {
        MethodName.log();
        long eventId = 12345L;
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, new TennisMatchFormat());
        TennisMatchIncident startingTennisMatchIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A);
        startingTennisMatchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(startingTennisMatchIncident, true);

        algoManager.handleMatchIncident(getPointWonIncident(eventId, "R1", TeamId.A), true);
        algoManager.handleMatchIncident(getPointWonIncident(eventId, "R2", TeamId.A), true);
        // // System.out.println(publishedMatchState);
        assertFalse(algoManager.getMatchState(eventId).isDatafeedStateMismatch());
        /*
         * Score should be 30-0, send in incident that takes score to 40-0 but saying feed thinks it is 30-40
         */
        TennisMatchStateFromFeed tennisMatchStateFromFeed =
                        new TennisMatchStateFromFeed(0, 0, 0, 0, 2, 3, false, TeamId.A);
        TennisMatchIncident tennisMatchIncident1 = getPointWonIncident(eventId, "R3", TeamId.A);
        tennisMatchIncident1.setTennisMatchStateFromFeed(tennisMatchStateFromFeed);
        publishedMarkets = null;
        algoManager.handleMatchIncident(tennisMatchIncident1, true);
        assertTrue(algoManager.getMatchState(eventId).isDatafeedStateMismatch());
        // // System.out.println(traderAlert);
        assertEquals(TraderAlertType.MATCH_STATE_MISMATCH, traderAlert.getTraderAlertType());
        @SuppressWarnings("unused")
        RecordedItem recordedItem = recording.get(recording.size() - 1);
        // // System.out.println(recordedItem);

        /*
         * undo the score from 40-0 back to to 30-0. Should send mismatch cleared alert
         */
        // System.out.println(publishedMatchState);
        traderAlert = null;
        publishedMarkets = null;
        publishedMatchState = null;
        algoManager.handleUndoLastMatchIncident(eventId);
        // // System.out.println(publishedMatchState);
        assertEquals(TraderAlertType.MATCH_STATE_MISMATCH_CLEARED, traderAlert.getTraderAlertType());
        recordedItem = recording.get(recording.size() - 1);
        assertFalse(publishedMarkets == null);
        // // System.out.println(super.publishedMatchState);
        /*
         * Simulate manual recovery by trader by issuing two incidents with null TennisMatchStateFromFeed objects to
         * take the score back to 30-30,
         */
        traderAlert = null;
        algoManager.handleMatchIncident(getPointWonIncident(eventId, "R4", TeamId.B), true);
        // System.out.println(publishedMatchState);
        assertEquals(null, traderAlert);
        traderAlert = null;
        algoManager.handleMatchIncident(getPointWonIncident(eventId, "R5", TeamId.B), true);
        assertEquals(null, traderAlert);
        // System.out.println(publishedMatchState);
        /*
         * Issue incident to take score to 30-40. with tennisMatchStateFromFeed set to 30-40 also. Should be in sync so
         * no traderAlert expected.
         */
        TennisMatchIncident tennisMatchIncident2 = getPointWonIncident(eventId, "R6", TeamId.B);
        tennisMatchIncident2.setTennisMatchStateFromFeed(tennisMatchStateFromFeed);
        algoManager.handleMatchIncident(tennisMatchIncident2, true);
        assertEquals(null, traderAlert);
        /*
         * fire in another incident to take score to 0-1 0-0, but leave score per feed at 0-0 15-40
         */
        traderAlert = null;
        publishedMarkets = null;
        TennisMatchIncident tennisMatchIncident3 = getPointWonIncident(eventId, "R7", TeamId.B);
        tennisMatchIncident3.setTennisMatchStateFromFeed(tennisMatchStateFromFeed);
        algoManager.handleMatchIncident(tennisMatchIncident3, true);
        assertEquals(TraderAlertType.MATCH_STATE_MISMATCH, traderAlert.getTraderAlertType());
        /*
         * fire in another incident to take score to 0-1 0-15, this time with correct score per feed
         */
        traderAlert = null;
        TennisMatchIncident tennisMatchIncident4 = getPointWonIncident(eventId, "R8", TeamId.B);
        tennisMatchIncident4
                        .setTennisMatchStateFromFeed(new TennisMatchStateFromFeed(0, 0, 0, 1, 0, 1, false, TeamId.A));
        algoManager.handleMatchIncident(tennisMatchIncident4, true);
        // System.out.println(super.publishedMatchState);
        assertEquals(TraderAlertType.MATCH_STATE_MISMATCH_CLEARED, traderAlert.getTraderAlertType());
        recordedItem = recording.get(recording.size() - 1);
        // System.out.println(recordedItem);
        /*
         * forward the score to 0-2 and leave points score non-zero as is actually done by the feed.
         */
        traderAlert = null;
        TennisMatchIncident tennisMatchIncident5 = getPointWonIncident(eventId, "R9", TeamId.B);
        tennisMatchIncident5
                        .setTennisMatchStateFromFeed(new TennisMatchStateFromFeed(0, 0, 0, 1, 0, 2, false, TeamId.A));
        algoManager.handleMatchIncident(tennisMatchIncident5, true);
        assertEquals(null, traderAlert);
        TennisMatchIncident tennisMatchIncident6 = getPointWonIncident(eventId, "R10", TeamId.B);
        tennisMatchIncident6
                        .setTennisMatchStateFromFeed(new TennisMatchStateFromFeed(0, 0, 0, 1, 0, 3, false, TeamId.A));
        algoManager.handleMatchIncident(tennisMatchIncident6, true);
        assertEquals(null, traderAlert);
        TennisMatchIncident tennisMatchIncident7 = getPointWonIncident(eventId, "R11", TeamId.B);
        tennisMatchIncident7
                        .setTennisMatchStateFromFeed(new TennisMatchStateFromFeed(0, 0, 0, 2, 0, 4, false, TeamId.A));
        algoManager.handleMatchIncident(tennisMatchIncident7, true);
        assertEquals(null, traderAlert);
    }

    TennisMatchIncident getPointWonIncident(long eventId, String incidentId, TeamId teamId) {
        TennisMatchIncident matchIncident = new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, teamId);
        matchIncident.setEventId(eventId);
        matchIncident.setIncidentId(incidentId);
        return matchIncident;
    }

    TennisMatchIncident getEndMatchIncident(long eventId, String incidentId, TeamId teamId) {
        TennisMatchIncident matchIncident = new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_END, teamId);
        matchIncident.setEventId(eventId);
        matchIncident.setIncidentId(incidentId);
        return matchIncident;
    }

    @Test
    public void testFootballSyncWithStateTest06092017() {
        MethodName.log();
        algoManager.autoSyncWithMatchFeed(true);
        long eventId = 4983896;
        algoManager.handleNewEventCreation(SupportedSportType.SOCCER, eventId, new FootballMatchFormat());

        FootballMatchStateFromFeed footballMatchStateFromFeed =
                        new FootballMatchStateFromFeed(1, 2, 3, 4, 5, 6, 7, 8, FootballMatchPeriod.PREMATCH);

        ElapsedTimeMatchIncident footballMatchIncident1 =
                        new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);

        footballMatchIncident1.setEventId(eventId);
        algoManager.handleMatchIncident(footballMatchIncident1, true);

        sleep(1);

        ElapsedTimeMatchIncident footballMatchIncident2 =
                        new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 0);

        footballMatchIncident2.setEventId(eventId);
        algoManager.handleMatchIncident(footballMatchIncident2, true);

        sleep(1);

        ElapsedTimeMatchIncident footballMatchIncident3 =
                        new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 4500);

        footballMatchIncident3.setEventId(eventId);
        algoManager.handleMatchIncident(footballMatchIncident3, true);

        FootballMatchIncident footballMatchIncident4 =
                        new FootballMatchIncident(FootballMatchIncidentType.GOAL, 2940, TeamId.A);
        footballMatchIncident4.setEventId(eventId);
        algoManager.handleMatchIncident(footballMatchIncident4, true);

        sleep(1);

        ElapsedTimeMatchIncident footballMatchIncident9 =
                        new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 3000);

        footballMatchIncident9.setEventId(eventId);
        algoManager.handleMatchIncident(footballMatchIncident9, true);

        FootballMatchIncident footballMatchIncident5 =
                        new FootballMatchIncident(FootballMatchIncidentType.GOAL, 3000, TeamId.A);
        footballMatchIncident5.setEventId(eventId);
        footballMatchIncident5.setFootballMatchStateFromFeed(footballMatchStateFromFeed);
        algoManager.handleMatchIncident(footballMatchIncident5, true);

        sleep(1);

        FootballMatchIncident footballMatchIncident10 =
                        new FootballMatchIncident(FootballMatchIncidentType.GOAL, 3000, TeamId.A);
        footballMatchIncident10.setEventId(eventId);
        footballMatchIncident10.setFootballMatchStateFromFeed(footballMatchStateFromFeed);
        algoManager.handleMatchIncident(footballMatchIncident10, true);

        sleep(1);

        FootballMatchIncident footballMatchIncident11 =
                        new FootballMatchIncident(FootballMatchIncidentType.GOAL, 3000, TeamId.A);
        footballMatchIncident11.setEventId(eventId);
        footballMatchIncident11.setFootballMatchStateFromFeed(footballMatchStateFromFeed);
        algoManager.handleMatchIncident(footballMatchIncident11, true);

        sleep(1);

        FootballMatchIncident footballMatchIncident12 =
                        new FootballMatchIncident(FootballMatchIncidentType.GOAL, 3000, TeamId.A);
        footballMatchIncident12.setEventId(eventId);
        footballMatchIncident12.setFootballMatchStateFromFeed(footballMatchStateFromFeed);
        algoManager.handleMatchIncident(footballMatchIncident12, true);

        sleep(1);

        FootballMatchIncident footballMatchIncident6 =
                        new FootballMatchIncident(FootballMatchIncidentType.GOAL, 4500, TeamId.A);

        footballMatchIncident6.setIncidentId("aa6dc8df-f39b-4f65-b0f3-2abcf2a2bc3d");
        footballMatchIncident6.setElapsedTimeSecs(4500);
        footballMatchIncident6.setExternalEventId("699959788");
        footballMatchIncident6.setEventId(eventId);
        footballMatchIncident6.setTimeStamp(1504704778535L);

        footballMatchIncident6.setFootballMatchStateFromFeed(footballMatchStateFromFeed);

        // System.out.println(footballMatchIncident6);
        // System.out.println();

        algoManager.handleMatchIncident(footballMatchIncident6, true);

        // System.out.println(footballMatchIncident4);

        // System.out.println(super.publishedMatchState);

        assertEquals(((FootballSimpleMatchState) super.publishedMatchState).getGoalsA(), 1);
        assertEquals(((FootballSimpleMatchState) super.publishedMatchState).getGoalsB(), 2);
        assertEquals(((FootballSimpleMatchState) super.publishedMatchState).getCornersA(), 3);
        assertEquals(((FootballSimpleMatchState) super.publishedMatchState).getCornersB(), 4);
        assertEquals(((FootballSimpleMatchState) super.publishedMatchState).getYellowCardsA(), 5);
        assertEquals(((FootballSimpleMatchState) super.publishedMatchState).getYellowCardsB(), 6);
        assertEquals(((FootballSimpleMatchState) super.publishedMatchState).getRedCardsA(), 7);
        assertEquals(((FootballSimpleMatchState) super.publishedMatchState).getRedCardsB(), 8);

        sleep(1);

        ElapsedTimeMatchIncident footballMatchIncident7 =
                        new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_MATCH_CLOCK, 4500);

        footballMatchIncident7.setEventId(eventId);
        algoManager.handleMatchIncident(footballMatchIncident7, true);

        sleep(1);

        FootballMatchIncident footballMatchIncident8 =
                        new FootballMatchIncident(FootballMatchIncidentType.GOAL, 4500, TeamId.A);
        footballMatchIncident8.setEventId(eventId);
        footballMatchIncident8.setFootballMatchStateFromFeed(footballMatchStateFromFeed);
        algoManager.handleMatchIncident(footballMatchIncident8, true);

        assertEquals(((FootballSimpleMatchState) super.publishedMatchState).getGoalsA(), 1);
        assertEquals(((FootballSimpleMatchState) super.publishedMatchState).getGoalsB(), 2);
        assertEquals(((FootballSimpleMatchState) super.publishedMatchState).getCornersA(), 3);
        assertEquals(((FootballSimpleMatchState) super.publishedMatchState).getCornersB(), 4);
        assertEquals(((FootballSimpleMatchState) super.publishedMatchState).getYellowCardsA(), 5);
        assertEquals(((FootballSimpleMatchState) super.publishedMatchState).getYellowCardsB(), 6);
        assertEquals(((FootballSimpleMatchState) super.publishedMatchState).getRedCardsA(), 7);
        assertEquals(((FootballSimpleMatchState) super.publishedMatchState).getRedCardsB(), 8);

    }

    @Test
    public void testFootballSyncWithState() {
        MethodName.log();
        long eventId = 12345L;
        algoManager.handleNewEventCreation(SupportedSportType.SOCCER, eventId, new FootballMatchFormat());

        ElapsedTimeMatchIncident footballMatchIncident1 =
                        new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);

        footballMatchIncident1.setEventId(eventId);
        algoManager.handleMatchIncident(footballMatchIncident1, true);

        sleep(15);

        // System.out.println(publishedMatchState);

        ElapsedTimeMatchIncident footballMatchIncident2 =
                        new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_MATCH_CLOCK, 15 * 60);
        footballMatchIncident2.setEventId(eventId);
        algoManager.handleMatchIncident(footballMatchIncident2, true);

        // System.out.println(publishedMatchState);

        FootballMatchIncident footballMatchIncident3 =
                        new FootballMatchIncident(FootballMatchIncidentType.GOAL, 16 * 60, TeamId.A);

        /*
         * Adding a goal. However the match state from feed will have an extra corner for A, goal for B a elapsed time
         * will be 20 minutes. Will check that we are aligned with the state.
         */

        FootballMatchStateFromFeed footballMatchStateFromFeed =
                        new FootballMatchStateFromFeed(1, 2, 3, 4, 5, 6, 7, 8, FootballMatchPeriod.IN_SECOND_HALF);

        footballMatchIncident3.setEventId(eventId);
        footballMatchIncident3.setFootballMatchStateFromFeed(footballMatchStateFromFeed);
        algoManager.handleMatchIncident(footballMatchIncident3, true);

        assertEquals(((FootballSimpleMatchState) super.publishedMatchState).getGoalsA(), 1);
        assertEquals(((FootballSimpleMatchState) super.publishedMatchState).getGoalsB(), 2);
        assertEquals(((FootballSimpleMatchState) super.publishedMatchState).getCornersA(), 3);
        assertEquals(((FootballSimpleMatchState) super.publishedMatchState).getCornersB(), 4);
        assertEquals(((FootballSimpleMatchState) super.publishedMatchState).getYellowCardsA(), 5);
        assertEquals(((FootballSimpleMatchState) super.publishedMatchState).getYellowCardsB(), 6);
        assertEquals(((FootballSimpleMatchState) super.publishedMatchState).getRedCardsA(), 7);
        assertEquals(((FootballSimpleMatchState) super.publishedMatchState).getRedCardsB(), 8);
        assertEquals(((FootballSimpleMatchState) super.publishedMatchState).getMatchPeriod(),
                        FootballMatchPeriod.IN_SECOND_HALF);

        // System.out.println(publishedMatchState);
        // System.out.println("Test passed");
        // System.out.println("");

    }

    private static void sleep(int nSecs) {
        for (int i = 0; i < nSecs; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // System.out.printf("Waiting... %d secs (of %d)\n", i + 1, nSecs);
        }
    }

    ElapsedTimeMatchIncident setClock(long eventId, String incidentId, int elapsedTime) {
        ElapsedTimeMatchIncident matchIncident =
                        new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_MATCH_CLOCK, elapsedTime);
        matchIncident.setEventId(eventId);
        matchIncident.setIncidentId(incidentId);
        return matchIncident;
    }

    private MatchIncident getMatchIncident(TeamId a, long eventId) {
        if (a != null) {
            TennisMatchIncident matchIncident = new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, a);
            matchIncident.setEventId(eventId);
            return matchIncident;
        } else {
            TennisMatchIncident matchIncident = new TennisMatchIncident(0, TennisMatchIncidentType.FAULT, a);
            matchIncident.setEventId(eventId);
            return matchIncident;
        }
    }

}
