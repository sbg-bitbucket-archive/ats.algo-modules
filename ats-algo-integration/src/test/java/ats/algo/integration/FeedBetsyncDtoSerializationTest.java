package ats.algo.integration;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.core.common.TeamId;
import ats.algo.sport.football.FootballMatchFormat;
import ats.algo.sport.football.FootballMatchParams;
import ats.algo.sport.football.FootballMatchState;
import ats.algo.sport.football.FootballShootoutInfo;
import ats.algo.sport.football.FootballSimpleMatchState;
import ats.algo.sport.football.FootballMatchIncident.FootballMatchIncidentType;
import ats.betting.betsync.dto.FeedBetsyncDto;
import ats.core.util.json.JsonUtil;

@SuppressWarnings("unused")
public class FeedBetsyncDtoSerializationTest {

    /*
     * for some reason these tetss only work if run on their own - fail if run along with all the other unit testts
     */
    @Test
    public void testSimpleMatchState() {
        FeedBetsyncDto dto = new FeedBetsyncDto();
        List<FootballMatchIncidentType> shootoutListStatus = new ArrayList<>();
        shootoutListStatus.add(FootballMatchIncidentType.GOAL);
        List<FootballMatchIncidentType> shootoutListStatus2 = new ArrayList<>();
        shootoutListStatus2.add(FootballMatchIncidentType.SHOOTOUT_MISS);
        FootballShootoutInfo shootoutInfo =
                        new FootballShootoutInfo(1, 0, TeamId.A, 2, false, shootoutListStatus, shootoutListStatus2);
        SimpleMatchState simpleMatchState = new FootballSimpleMatchState(false, false, true, null, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, null, null, shootoutInfo, false);
        dto.setSimpleMatchState(simpleMatchState);
        String json = JsonUtil.marshalJson(dto, true);
        System.out.println(json);
        FeedBetsyncDto dto2 = JsonUtil.unmarshalJson(json, FeedBetsyncDto.class);
        assertEquals(dto.getSimpleMatchState(), dto2.getSimpleMatchState());
    }

    // @Test
    public void testMatchState() {
        FeedBetsyncDto dto = new FeedBetsyncDto();
        MatchState matchState = new FootballMatchState(new FootballMatchFormat());
        dto.setMatchState(matchState);
        String json = JsonUtil.marshalJson(dto, true);
        System.out.println(json);
        FeedBetsyncDto dto2 = JsonUtil.unmarshalJson(json, FeedBetsyncDto.class);
        assertEquals(dto.getMatchState(), dto2.getMatchState());
    }



}
