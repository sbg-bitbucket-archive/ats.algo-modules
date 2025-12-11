package ats.algo.sport.outrights;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ats.core.util.json.JsonUtil;

public class OutrightsMatchIncidentTest {

    @Test
    public void test() {
        OutrightsMatchIncident incident = new OutrightsMatchIncident();
        String json = JsonUtil.marshalJson(incident);
        OutrightsMatchIncident incident2 = JsonUtil.unmarshalJson(json, OutrightsMatchIncident.class);
        assertEquals(incident, incident2);
    }
}
