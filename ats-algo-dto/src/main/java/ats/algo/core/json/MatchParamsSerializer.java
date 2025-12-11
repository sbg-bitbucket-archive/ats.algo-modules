package ats.algo.core.json;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import ats.algo.core.baseclasses.MatchParams;


/*
 * this class is no longer used but keep source in case useful as example in future
 */


/**
 * custom serializer for the xxxMatchParams class
 * 
 * @author Geoff
 * 
 *         Key features:
 * 
 *         - Only serialises the set of MatchParam objects returned by the getAsMap() method
 * 
 *         - Does not serialise the entire MatchParam object, only those properties needed by the ATS client
 */

public class MatchParamsSerializer extends JsonSerializer<MatchParams> {

    @Override
    public void serialize(MatchParams tc, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonProcessingException {
        // jgen.writeStartObject();
        // jgen.writeNumberField("eventId", tc.getEventId());
        // jgen.writeNumberField("jsonVersionUID", 2);
        // jgen.writeStringField("baseClass", tc.getBaseClass());
        // LinkedHashMap<String, MatchParam> map = tc.getAsMap();
        // for (Entry<String, MatchParam> entry : map.entrySet()) {
        // String key = entry.getKey();
        // MatchParam matchParam = entry.getValue();
        // jgen.writeObjectFieldStart(key);
        // jgen.writeNumberField("mean", matchParam.getParam().getMean());
        // jgen.writeNumberField("stdDevn", matchParam.getParam().getStdDevn());
        // jgen.writeNumberField("bias", matchParam.getParam().getBias());
        // jgen.writeNumberField("minAllowedValue", matchParam.getMinAllowedParamValue());
        // jgen.writeNumberField("maxAllowedValue", matchParam.getMaxAllowedParamValue());
        // jgen.writeEndObject();
        // }
        // jgen.writeEndObject();
    }

}
