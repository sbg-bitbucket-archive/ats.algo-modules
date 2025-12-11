package ats.algo.sport.tennis;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.core.baseclasses.AlgoMatchParams;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.dto.util.DtoJsonUtil;
import ats.algo.matchrunner.BetsyncProxy;
import ats.core.util.json.JsonUtil;

public class TennisBetSyncProxyTest {

    @Test
    public void testMatchParams() {
        MethodName.log();
        AlgoMatchParams matchParams = new TennisMatchParams();
        try {
            BetsyncProxy.checkSerializationOk(matchParams.generateGenericMatchParams());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testMatchState() {
        MethodName.log();
        AlgoMatchState matchState = new TennisMatchState();
        try {
            BetsyncProxy.checkSerializationOk(matchState.generateSimpleMatchState());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testMatchIncident() {
        MethodName.log();
        MatchIncident matchIncident = new TennisMatchIncident();
        try {
            BetsyncProxy.checkSerializationOk(matchIncident);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
	
    @Test
    public void testMatchParams2() {
        MethodName.log();
        AlgoMatchParams matchParams = new TennisMatchParams();
        GenericMatchParams g = matchParams.generateGenericMatchParams();
        String json = JsonUtil.marshalJson(g);
        GenericMatchParams g2 = DtoJsonUtil.unmarshallMatchParams(json);
        assertEquals(g,g2);             
    }
     
    @Test
    public void testMatchIncident2() {
        MethodName.log();
        MatchIncident i = new TennisMatchIncident();
        String json = JsonUtil.marshalJson(i);
        MatchIncident i2 = DtoJsonUtil.unmarshallMatchIncident(json);
        assertEquals(i,i2);             
    }
}
