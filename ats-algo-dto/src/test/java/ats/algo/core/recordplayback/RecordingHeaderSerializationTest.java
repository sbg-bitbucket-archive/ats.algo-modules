package ats.algo.core.recordplayback;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import ats.algo.core.common.SupportedSportType;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.core.util.json.JsonUtil;

public class RecordingHeaderSerializationTest {

    @Test
    public void test() {
        RecordingHeader hdr = new RecordingHeader(SupportedSportType.TENNIS, 123L, 123456789, "competitionName",
                        "teamAName", "teamBName", 3, new TennisMatchFormat());
        String json = JsonUtil.marshalJson(hdr);
        System.out.println(json);
        @SuppressWarnings("unused")
        RecordingHeader hdr2 = JsonUtil.unmarshalJson(json, RecordingHeader.class);
        ObjectMapper mapper = new ObjectMapper();
        String json2;
        try {
            json2 = mapper.writeValueAsString(hdr);
            System.out.println(json2);
            @SuppressWarnings("unused")
            RecordingHeader hdr3 = mapper.readValue(json2, RecordingHeader.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
