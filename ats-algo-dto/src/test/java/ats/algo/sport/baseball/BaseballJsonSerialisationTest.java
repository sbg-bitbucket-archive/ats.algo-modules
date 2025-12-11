package ats.algo.sport.baseball;

import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.JsonSerializer;
import ats.algo.sport.baseball.BaseballMatchFormat;
import ats.algo.sport.baseball.BaseballMatchIncident;
import ats.algo.sport.baseball.BaseballMatchParams;
import ats.algo.sport.baseball.BaseballMatchState;
import ats.algo.sport.baseball.BaseballMatchIncident.BaseballMatchIncidentType;
import ats.core.util.json.JsonUtil;

public class BaseballJsonSerialisationTest {
    @Test
    public void testMatchFormat() {
        BaseballMatchFormat matchFormat1 = new BaseballMatchFormat();
        String json = JsonUtil.marshalJson(matchFormat1, true);
        BaseballMatchFormat matchFormat2 = JsonUtil.unmarshalJson(json, BaseballMatchFormat.class);
        System.out.println(json);
        assertEquals(matchFormat1, matchFormat2);
    }

    @Test
    public void testMatchParams() {
        BaseballMatchParams baseballMatchParams = new BaseballMatchParams();
        String json = JsonUtil.marshalJson(baseballMatchParams, true);
        System.out.println("-----------------------------------------");
        System.out.println(json);
        BaseballMatchParams vpMatchParams = JsonUtil.unmarshalJson(json, BaseballMatchParams.class);
        System.out.println("-----------------------------------------");
        System.out.println(vpMatchParams);
        assertEquals(baseballMatchParams, vpMatchParams);
    }

    @Test
    public void testMatchIncident() {
        BaseballMatchIncident BaseballMatchIncident = new BaseballMatchIncident();
        BaseballMatchIncident.setIncidentSubType(BaseballMatchIncidentType.BALL);
        String json = JsonUtil.marshalJson(BaseballMatchIncident, true);
        BaseballMatchIncident BaseballMatchIncident2 = JsonUtil.unmarshalJson(json, BaseballMatchIncident.class);
        System.out.println(json);
        assertEquals(BaseballMatchIncident, BaseballMatchIncident2);
    }

    @Test
    public void testMatchFormatOptions() {
        BaseballMatchFormatOptions o1 = new BaseballMatchFormatOptions();
        String json = JsonUtil.marshalJson(o1, true);
        System.out.println(json);
        assertTrue(json.contains("3"));
    }

    @Test
    public void testMatchState() {
        BaseballMatchFormat matchFormat = new BaseballMatchFormat();
        BaseballMatchState BaseballMatchState = new BaseballMatchState(matchFormat);
        String json = JsonUtil.marshalJson(BaseballMatchState, true);
        BaseballMatchState voMatchState = JsonUtil.unmarshalJson(json, BaseballMatchState.class);
        System.out.println(json);
        assertEquals(BaseballMatchState, voMatchState);

    }

    @Test
    public void testSimpleMatchState() {
        BaseballMatchFormat matchFormat = new BaseballMatchFormat();
        BaseballMatchState BaseballMatchState = new BaseballMatchState(matchFormat);
        String json = JsonSerializer.serialize(BaseballMatchState.generateSimpleMatchState(), true);
        System.out.println(json);
        BaseballMatchState.setBase2(false);
        BaseballMatchState.setBall(10);
        BaseballMatchState.setBat(TeamId.B);

        MatchState sms = BaseballMatchState.generateSimpleMatchState();
        // System.out.println(((TennisSimpleMatchState) sms).getScoreInSet1().A+"-"+((TennisSimpleMatchState)
        // sms).getScoreInSet1().B);
        json = JsonSerializer.serialize(sms, true);
        System.out.println(json);
        MatchState sms2 = JsonSerializer.deserialize(json, BaseballSimpleMatchState.class);
        assertEquals(sms, sms2);
    }
}
