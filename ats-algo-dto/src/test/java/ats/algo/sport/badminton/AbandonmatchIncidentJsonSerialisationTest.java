package ats.algo.sport.badminton;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ats.algo.core.common.AbandonMatchIncident;
import ats.algo.core.common.TeamId;
import ats.algo.core.common.AbandonMatchIncident.AbandonMatchIncidentType;
import ats.core.util.json.JsonUtil;

public class AbandonmatchIncidentJsonSerialisationTest {

    @Test
    public void testMatchIncident() {
        TeamId teamId = TeamId.A;
        AbandonMatchIncident abandonMatchIncidentnew =
                        new AbandonMatchIncident(AbandonMatchIncidentType.WALKOVER, teamId);
        String json = JsonUtil.marshalJson(abandonMatchIncidentnew, true);
        AbandonMatchIncident abandonMatchIncidentnew2 = JsonUtil.unmarshalJson(json, AbandonMatchIncident.class);
        System.out.println(abandonMatchIncidentnew2);
        assertEquals(abandonMatchIncidentnew, abandonMatchIncidentnew2);
    }
}
