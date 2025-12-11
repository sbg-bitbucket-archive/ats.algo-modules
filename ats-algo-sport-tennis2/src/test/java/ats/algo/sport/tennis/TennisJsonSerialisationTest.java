package ats.algo.sport.tennis;

import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.JsonSerializer;
import ats.algo.genericsupportfunctions.MethodName;
import ats.algo.sport.tennis.TennisMatchFormat.FinalSetType;
import ats.algo.sport.tennis.TennisMatchFormat.Sex;
import ats.algo.sport.tennis.TennisMatchFormat.Surface;
import ats.algo.sport.tennis.TennisMatchFormat.TournamentLevel;
import ats.core.util.json.JsonUtil;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TennisJsonSerialisationTest {

    @Test
    public void testMatchParams() {
        MethodName.log();
        TennisMatchParams matchParams1 = new TennisMatchParams();
        matchParams1.setDoublesMatch(true);
        matchParams1.setEventId(1111);
        matchParams1.setOnServePctA1(.3, .03);
        matchParams1.setOnServePctA2(.4, .04);
        matchParams1.setOnServePctB1(.5, .05);
        matchParams1.setOnServePctB2(.6, .06);
        String json = JsonUtil.marshalJson(matchParams1, true);
        // System.out.print("Doubles match json: \n");
        // System.out.println(json);
        TennisMatchParams matchParams2 = JsonUtil.unmarshalJson(json, TennisMatchParams.class);
        // System.out.print(matchParams1.toString() + "\n");
        // System.out.print(matchParams2.toString() + "\n");
        assertEquals(matchParams1, matchParams2);
        TennisMatchParams matchParams3 = new TennisMatchParams();
        matchParams3.setDoublesMatch(false);
        matchParams3.setOnServePctA1(.3, .03);
        matchParams3.setOnServePctB1(.7, .07);
        String json2 = JsonUtil.marshalJson(matchParams3, true);
        // System.out.print("\nSingles match json: \n");
        // System.out.println(json2);
        TennisMatchParams matchParams4 = JsonUtil.unmarshalJson(json2, TennisMatchParams.class);
        assertEquals(matchParams3, matchParams4);
    }

    @Test
    public void testMatchParams2() {
        MethodName.log();
        TennisMatchParams matchParams1 = new TennisMatchParams();
        matchParams1.setDoublesMatch(false);
        matchParams1.setEventId(1111);
        matchParams1.setUserId("Fred");
        matchParams1.setOnServePctA1(.3, .03);
        matchParams1.setOnServePctB1(.5, .05);
        GenericMatchParams gm1 = matchParams1.generateGenericMatchParams();
        String json = JsonUtil.marshalJson(gm1, false);
        GenericMatchParams gm2 = JsonUtil.unmarshalJson(json, GenericMatchParams.class);
        // System.out.print(gm1.toString() + "\n");
        // System.out.print(gm2.toString() + "\n");
        assertEquals(gm1, gm2);
        MatchParams matchParams2 = gm2.generateXxxMatchParams();
        assertEquals(matchParams1, matchParams2);

    }

    @Test
    public void testMatchState() {
        MethodName.log();
        TennisMatchFormat matchFormat = new TennisMatchFormat(true, Sex.WOMEN, Surface.CLAY, TournamentLevel.CHALLENGER,
                        5, FinalSetType.NORMAL_WITH_TIE_BREAK, false);;
        TennisMatchState matchState1 = new TennisMatchState(matchFormat);
        matchState1.setScore(1, 0, 1, 2, TeamId.A, 1, 1);

        String json = JsonUtil.marshalJson(matchState1, true);
        // System.out.println(json);
        TennisMatchState matchState2 = JsonUtil.unmarshalJson(json, TennisMatchState.class);
        assertEquals(matchState1, matchState2);
    }

    @Test
    public void testMatchState2() {
        MethodName.log();
        TennisMatchFormat matchFormat = new TennisMatchFormat(true, Sex.WOMEN, Surface.CLAY, TournamentLevel.CHALLENGER,
                        5, FinalSetType.NORMAL_WITH_TIE_BREAK, false);;
        TennisMatchState matchState1 = new TennisMatchState(matchFormat);
        matchState1.setScore(1, 0, 1, 2, TeamId.A, 1, 1);

        String json = JsonSerializer.serialize(matchState1, true);
        // System.out.println(json);
        TennisMatchState matchState2 = JsonSerializer.deserialize(json, TennisMatchState.class);
        assertEquals(matchState1, matchState2);
    }
}
