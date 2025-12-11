package ats.algo.genericsupportfunctions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ats.org.json.JSONArray;
import ats.org.json.JSONException;
import ats.org.json.JSONObject;

/**
 * Format a json string for a non technical viewer
 * 
 * c.f. https://stackoverflow.com/questions/8596161/json-string-tidy-formatter-for-java for ideas behind this
 * 
 * @author gicha
 *
 */
public class JsonExtractor {

    /**
     * extracts any properties and return as array
     * 
     * @param json String containing the json representation
     * @return
     * @throws JSONException
     */
    public static List<String> extractListProperty(String json, String property) throws JSONException {
        final JSONObject object = new JSONObject(json);
        return extract(object, json, property);
    }

    /**
     * returns the contents of the supplied object into an easily viewable format
     * 
     * @param object the object to be formatted
     * @param json a json string which contains the object (perhaps as a subObject within the string)
     * @return
     * @throws JSONException
     */
    private static List<String> extract(JSONObject object, String json, String property) throws JSONException {
        final JsonVisitor visitor = new JsonVisitor(2, ' ');
        visitor.visit(object, 0, property);
        return visitor.getList();
    }

    private static class JsonVisitor {

        private List<String> list;

        List<String> getList() {
            return list;
        }

        public JsonVisitor(int indentationSize, char indentationChar) {
            list = new ArrayList<>();
        }


        private void visit(JSONArray array, int indent, String property) throws JSONException {
            final int length = array.length();
            if (length == 0) {

            } else {
                for (int i = 0; i < length; i++) {
                    // System.out.println("Array #" + i);
                    visit(array.get(i), indent, property);
                }
            }
        }

        private void visit(JSONObject obj, int indent, String property) throws JSONException {
            final int length = obj.length();
            // System.out.println("Visit object");
            if (length == 0) {
            } else {
                Iterator<?> keys = obj.keys();
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    // System.out.println(" " + key);
                    Object value = obj.get(key);
                    if (getJSONObjectType(value) == JSONObjectType.JSON_VALUE) {
                        if (key.equals(property))
                            list.add(value.toString());
                    } else {
                        visit(value, indent + 1, property);
                    }
                }
            }
        }


        private void visit(Object object, int indent, String property) throws JSONException {

            JSONObjectType type = getJSONObjectType(object);
            switch (type) {
                case JSON_ARRAY:
                    visit((JSONArray) object, indent, property);
                    break;
                case JSON_OBJECT:
                    visit((JSONObject) object, indent, property);
                    break;
                case JSON_VALUE:
                    throw new IllegalArgumentException("ERROR!");
            }
        }

        enum JSONObjectType {
            JSON_ARRAY,
            JSON_OBJECT,
            JSON_VALUE
        }

        private static JSONObjectType getJSONObjectType(Object object) {
            if (object instanceof JSONArray) {
                return JSONObjectType.JSON_ARRAY;
            } else if (object instanceof JSONObject) {
                return JSONObjectType.JSON_OBJECT;
            } else {
                return JSONObjectType.JSON_VALUE;
            }
        }
    }

}
