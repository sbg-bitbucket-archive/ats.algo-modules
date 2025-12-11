package ats.algo.core.json;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import ats.algo.core.baseclasses.MatchParams;

/**
 * This class is not in use - but keep source code in case useful as an example in future
 * 
 * @author Geoff
 *
 */
public class MatchParamsDeserializer extends JsonDeserializer<MatchParams> {

    @Override
    public MatchParams deserialize(JsonParser jp, DeserializationContext ctxt)
                    throws IOException, JsonProcessingException {
        // String className = jp.getCurrentName();
        // /*
        // * instantiate a copy of xxxMatchParams. This assumes that at least one instance of the class in question has
        // * already been instantiated so that the class appears in the instantiatedDerivedClasses list
        // */
        // MatchParams matchParams = null;
        // try {
        // Class<? extends MatchParams> matchParamsClass = MatchParams.getInstantiatedDerivedClasses().get(className);
        // Constructor<?> constructor = matchParamsClass.getConstructor();
        // matchParams = (MatchParams) constructor.newInstance();
        // } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
        // | IllegalArgumentException | InvocationTargetException e) {
        // throw new IllegalArgumentException(
        // String.format("Error getting or constructing xxxMatchParams class : %s", className));
        // }
        // /*
        // *
        // */
        // LinkedHashMap <String, MatchParam > map = new LinkedHashMap<String, MatchParam> ();
        // JsonToken currentToken;
        // while ((currentToken = jp.nextValue()) != JsonToken.END_OBJECT) {
        // String propertyName = jp.getCurrentName();
        // //System.out.println((propertyName) + " " + currentToken);
        // switch (propertyName) {
        // case "eventId":
        // matchParams.setEventId(jp.getValueAsLong());
        // break;
        // case "jsonVersionUID":
        // long jsonVersionUID = jp.getValueAsLong();
        // if (jsonVersionUID != 2)
        // throw new IllegalArgumentException(
        // String.format("jsonVersionUID incorrect. Expecting: 2. But is: %d", jsonVersionUID));
        // break;
        // case "baseClass":
        // String baseClass = jp.getValueAsString();
        // if (!baseClass.equals("MatchParams"))
        // throw new IllegalArgumentException(
        // String.format("Wrong baseClass. Expecting: MatchParams. But is: %s", baseClass));
        // // do nothing
        // break;
        // default:
        // if (currentToken != JsonToken.START_OBJECT)
        // throw new IllegalArgumentException(String
        // .format("Expecting START_OBJECT at beginning of MatchParam but is: %s", currentToken));
        // map.put(propertyName, parseJsonMatchParam(jp));
        // break;
        // }
        // }
        // matchParams.setFromMap(map);
        // return matchParams;
        return null;
    }
    //
    // private MatchParam parseJsonMatchParam(JsonParser jp) throws JsonParseException, IOException {
    // MatchParam matchParam = new MatchParam();
    // Gaussian gaussian = matchParam.getParam();
    // while ((jp.nextValue()) != JsonToken.END_OBJECT) {
    // String propertyName = jp.getCurrentName();
    // //System.out.println((" " + propertyName) + " " + currentToken);
    // switch (propertyName) {
    // case "mean":
    // gaussian.setMean(jp.getDoubleValue());
    // break;
    // case "stdDevn":
    // gaussian.setStdDevn(jp.getDoubleValue());
    // break;
    // case "bias":
    // gaussian.setBias(jp.getDoubleValue());
    // break;
    // case "minAllowedValue":
    // case "maxAllowedValue":
    // /*
    // * do nothing
    // */
    // break;
    //
    // default:
    // throw new IllegalArgumentException(String.format("Unrecognised property name: %s", propertyName));
    // }
    // }
    // return matchParam;
    // }
    //
    //

}
