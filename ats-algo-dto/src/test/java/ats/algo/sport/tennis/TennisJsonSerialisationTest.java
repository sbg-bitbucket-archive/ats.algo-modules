package ats.algo.sport.tennis;

import static com.google.common.collect.Maps.newHashMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.JsonSerializer;
import ats.algo.sport.tennis.TennisMatchFormat.FinalSetType;
import ats.algo.sport.tennis.TennisMatchFormat.Sex;
import ats.algo.sport.tennis.TennisMatchFormat.Surface;
import ats.algo.sport.tennis.TennisMatchFormat.TournamentLevel;
import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;
import ats.algo.sport.tennis.TennisMatchIncident.TennisPointResult;
import ats.algo.sport.tennis.TennisMatchIncidentResult.TennisMatchIncidentResultType;
import ats.core.util.json.JsonUtil;

public class TennisJsonSerialisationTest {

    private Map<String, String> parseFormat(String format) {
        Map<String, String> formatSettingsMap = newHashMap();
        for (String pair : format.split(";")) {
            String[] split = pair.split("=");
            formatSettingsMap.put(split[0], split[1]);
        }
        return formatSettingsMap;
    }

    @Test
    public void testMatchFormatFromATS() {
        Map<String, String> formatSettingsMap = parseFormat(
                        " teamAPlayerId=AMELCO-3900004;teamAPlayerName=AMELCO-Jamie Murray;surface=GRASS;teamBPlayerId=AMELCO-3900013;"
                                        + "sex=WOMEN;teamBPlayerName=AMELCO-Lucie Safarova;setsPerMatch=3;finalSetType=TieBreak;doublesMatch=False;"
                                        + "tournamentLevel=ITF;noAdvantageGameFormat=False");
        TennisMatchFormat matchFormat = new TennisMatchFormat();
        matchFormat.setFromMap(formatSettingsMap);
        System.out.println(matchFormat);
    }

    @Test
    public void testMatchFormat() {
        TennisMatchFormat matchFormat1 = new TennisMatchFormat(false, Sex.WOMEN, Surface.CLAY,
                        TournamentLevel.CHALLENGER, 3, FinalSetType.NORMAL_WITH_TIE_BREAK, false);
        String json = JsonUtil.marshalJson(matchFormat1, true);
        System.out.println(json);
        TennisMatchFormat matchFormat2 = JsonUtil.unmarshalJson(json, TennisMatchFormat.class);
        assertEquals(matchFormat1, matchFormat2);
    }

    @Test
    public void testMatchFormatOptions() {
        TennisMatchFormatOptions o1 = new TennisMatchFormatOptions();
        String json = JsonUtil.marshalJson(o1, true);
        System.out.println(json);
        assertTrue(json.contains("CARPET"));
    }

    @Test
    public void testMatchParams() {
        TennisMatchParams matchParams1 = new TennisMatchParams();
        matchParams1.setDoublesMatch(true);
        matchParams1.setEventId(1111);
        matchParams1.setOnServePctA1(.3, .03);
        matchParams1.setOnServePctA2(.4, .04);
        matchParams1.setOnServePctB1(.5, .05);
        matchParams1.setOnServePctB2(.6, .06);
        String json = JsonUtil.marshalJson(matchParams1, true);
        System.out.print("Doubles match json: \n");
        System.out.println(json);
        TennisMatchParams matchParams2 = JsonUtil.unmarshalJson(json, TennisMatchParams.class);
        System.out.print(matchParams1.toString() + "\n");
        System.out.print(matchParams2.toString() + "\n");
        assertEquals(matchParams1, matchParams2);
        TennisMatchParams matchParams3 = new TennisMatchParams();
        matchParams3.setDoublesMatch(false);
        matchParams3.setOnServePctA1(.3, .03);
        matchParams3.setOnServePctB1(.7, .07);
        String json2 = JsonUtil.marshalJson(matchParams3, true);
        System.out.print("\nSingles match json: \n");
        System.out.println(json2);
        TennisMatchParams matchParams4 = JsonUtil.unmarshalJson(json2, TennisMatchParams.class);
        assertEquals(matchParams3, matchParams4);
    }

    @Test
    public void testMatchParams2() {
        TennisMatchParams matchParams1 = new TennisMatchParams();
        matchParams1.setDoublesMatch(false);
        matchParams1.setEventId(1111);
        matchParams1.setUserId("Fred");
        matchParams1.setOnServePctA1(.3, .03);
        matchParams1.setOnServePctB1(.5, .05);
        GenericMatchParams gm1 = matchParams1.generateGenericMatchParams();
        String json = JsonUtil.marshalJson(gm1, false);
        GenericMatchParams gm2 = JsonUtil.unmarshalJson(json, GenericMatchParams.class);
        System.out.print(gm1.toString() + "\n");
        System.out.print(gm2.toString() + "\n");
        assertEquals(gm1, gm2);
        MatchParams matchParams2 = gm2.generateXxxMatchParams();
        assertEquals(matchParams1, matchParams2);

    }



    @Test
    public void testTennisMatchParamsSerialisation() {
        TennisMatchParams matchParams = new TennisMatchParams();
        matchParams.setDoublesMatch(false);
        String json = JsonSerializer.serialize(matchParams.generateGenericMatchParams(), true);
        System.out.println(json);
        GenericMatchParams tmp = JsonSerializer.deserialize(json, GenericMatchParams.class);
        TennisMatchParams matchParams2 = (TennisMatchParams) tmp.generateXxxMatchParams();
        assertEquals(matchParams, matchParams2);
        System.out.println(matchParams2);
    }

    @Test
    public void testMatchIncident() {
        TennisMatchIncident incident1 = new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A);
        incident1.setPointResult(TennisPointResult.DOUBLE_FAULT);
        incident1.setEventId(235L);
        String json = JsonUtil.marshalJson(incident1);
        System.out.println(json);
        TennisMatchIncident incident2 = JsonUtil.unmarshalJson(json, TennisMatchIncident.class);
        assertEquals(incident1, incident2);
        /*
         * test jsonIgnoreProperties works ok - change property eventId so it is not recognised.
         */
        String s = "{\"TennisMatchIncident\":{\"subClass\":\"TennisMatchIncident\",\"elapsedTimeSecs\":0,\"eventIdXXX\":0,\"timeStamp\":0,\"incidentSubType\":\"POINT_WON\",\"serverSideAtStartOfMatch\":\"UNKNOWN\",\"serverPlayerAtStartOfMatch\":0,\"pointWinner\":\"A\",\"pointResult\":\"DOUBLE_FAULT\"}}";
        TennisMatchIncident incident3 = JsonUtil.unmarshalJson(s, TennisMatchIncident.class);
        System.out.println(incident3);
        assertEquals(235L, incident1.getEventId());
        assertEquals(0, incident3.getEventId());
    }

    @Test
    public void testMatchIncidentResult() {
        TennisMatchIncidentResult result =
                        new TennisMatchIncidentResult(true, true, 1, TennisMatchIncidentResultType.GAMEWONA);
        String json = JsonUtil.marshalJson(result, true);
        System.out.println(json);
        TennisMatchIncidentResult result2 = JsonUtil.unmarshalJson(json, TennisMatchIncidentResult.class);
        assertEquals(result, result2);
    }

    @Test
    public void testMatchState() {
        TennisMatchFormat matchFormat = new TennisMatchFormat(true, Sex.WOMEN, Surface.CLAY, TournamentLevel.CHALLENGER,
                        5, FinalSetType.NORMAL_WITH_TIE_BREAK, false);;
        TennisMatchState matchState1 = new TennisMatchState(matchFormat);
        matchState1.setScore(1, 0, 1, 2, TeamId.A, 1, 1);

        String json = JsonUtil.marshalJson(matchState1, true);
        System.out.println(json);
        TennisMatchState matchState2 = JsonUtil.unmarshalJson(json, TennisMatchState.class);
        assertEquals(matchState1, matchState2);
    }

    @Test
    public void testMatchState2() {
        TennisMatchFormat matchFormat = new TennisMatchFormat(true, Sex.WOMEN, Surface.CLAY, TournamentLevel.CHALLENGER,
                        5, FinalSetType.NORMAL_WITH_TIE_BREAK, false);;
        TennisMatchState matchState1 = new TennisMatchState(matchFormat);
        matchState1.setScore(1, 0, 1, 2, TeamId.A, 1, 1);

        String json = JsonSerializer.serialize(matchState1, true);
        System.out.println(json);
        TennisMatchState matchState2 = JsonSerializer.deserialize(json, TennisMatchState.class);
        assertEquals(matchState1, matchState2);
    }

    @Test
    public void testSimpleMatchState() {
        TennisMatchFormat matchFormat = new TennisMatchFormat(true, Sex.WOMEN, Surface.CLAY, TournamentLevel.CHALLENGER,
                        5, FinalSetType.NORMAL_WITH_TIE_BREAK, false);;
        TennisMatchState matchState = new TennisMatchState(matchFormat);
        String json = JsonSerializer.serialize(matchState.generateSimpleMatchState(), true);
        System.out.println(json);
        matchState.setScore(1, 0, 1, 2, TeamId.A, 1, 1);
        matchState.setGameScoreInSetN(0, 6, 2);
        System.out.println((matchState).getGameScoreInSetN(0).A + "-" + (matchState).getGameScoreInSetN(0).B);

        MatchState sms = matchState.generateSimpleMatchState();
        // System.out.println(((TennisSimpleMatchState)
        // sms).getScoreInSet1().A+"-"+((TennisSimpleMatchState)
        // sms).getScoreInSet1().B);
        json = JsonSerializer.serialize(sms, true);
        System.out.println(json);
        MatchState sms2 = JsonSerializer.deserialize(json, TennisSimpleMatchState.class);
        assertEquals(sms, sms2);

        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("currentSetNo", 3);
        map.put("subClass", TennisSimpleMatchState.class.getSimpleName());

        ObjectMapper om = new ObjectMapper();
        TennisSimpleMatchState mapToSMS = om.convertValue(map, TennisSimpleMatchState.class);
        System.out.println(mapToSMS);
        assertEquals(mapToSMS.getCurrentSetNo(), 3);
    }
}
