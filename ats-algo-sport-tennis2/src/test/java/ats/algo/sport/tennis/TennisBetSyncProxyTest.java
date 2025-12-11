package ats.algo.sport.tennis;

import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.matchrunner.BetsyncProxy;

public class TennisBetSyncProxyTest {


    @Test
    public void testMatchParams() {
        MatchParams matchParams = new TennisMatchParams();
        try {
            BetsyncProxy.checkSerializationOk(matchParams.generateGenericMatchParams());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testMatchState() {
        MatchState matchState = new TennisMatchState();
        try {
            BetsyncProxy.checkSerializationOk(matchState);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
