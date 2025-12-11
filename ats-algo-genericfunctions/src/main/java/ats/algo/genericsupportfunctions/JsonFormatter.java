package ats.algo.genericsupportfunctions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ats.org.json.JSONArray;
import ats.org.json.JSONException;
import ats.org.json.JSONObject;

/**
 * Format a json string for easy viewing
 * 
 * c.f. https://stackoverflow.com/questions/8596161/json-string-tidy-formatter-for-java for ideas behind this
 * 
 * @author gicha
 *
 */
public class JsonFormatter {

    /**
     * formats the contents of the string in an easily viewable format
     * 
     * @param json String containing the json representation
     * @return
     * @throws JSONException
     */
    public static String format(String json) throws JSONException {
        final JSONObject object = new JSONObject(json);
        return format(object, json);
    }

    /**
     * returns the contents of the supplied object into an easily viewable format
     * 
     * @param object the object to be formatted
     * @param json a json string which contains the object (perhaps as a subObject within the string)
     * @return
     * @throws JSONException
     */
    public static String format(JSONObject object, String json) throws JSONException {
        final JsonVisitor visitor = new JsonVisitor(2, ' ');
        visitor.setSourceString(json);
        visitor.visit(object, 0);
        return visitor.toString();
    }

    private static class JsonVisitor {

        private final StringBuilder builder = new StringBuilder();
        private final int indentationSize;
        private final char indentationChar;

        private String json;

        public JsonVisitor(int indentationSize, char indentationChar) {
            this.indentationSize = indentationSize;
            this.indentationChar = indentationChar;
        }

        public void setSourceString(String json) {
            this.json = json;
        }

        private void visit(JSONArray array, int indent) throws JSONException {
            final int length = array.length();
            if (length == 0) {
                write("[]", indent);
            } else {
                append("[");
                for (int i = 0; i < length; i++) {
                    visit(array.get(i), indent + 1);
                }
                write("]", indent);
            }
        }

        @SuppressWarnings("unchecked")
        private void visit(JSONObject obj, int indent) throws JSONException {
            final int length = obj.length();
            if (length == 0) {
                append("{}");
            } else {
                append("{");
                List<String> sortedKeys = getSortedKeys(obj.keys(), obj.length());
                Iterator<String> iterator = sortedKeys.iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    Object value = obj.get(key);
                    write("\"" + key + "\" : ", indent);
                    visit(value, indent + 1);
                    if (iterator.hasNext())
                        append(",");
                }
                write("}", indent);
            }
        }

        private List<String> getSortedKeys(Iterator<String> keys, int length) {
            Map<String, Integer> map = new HashMap<String, Integer>(length);
            while (keys.hasNext()) {
                String key = keys.next();
                map.put(key, json.indexOf(key));
            }
            return MapSort.sortKeys(map);
        }

        private void visit(Object object, int indent) throws JSONException {
            JSONObjectType type = getJSONObjectType(object);
            switch (type) {
                case JSON_ARRAY:
                    visit((JSONArray) object, indent);
                    break;
                case JSON_OBJECT:
                    visit((JSONObject) object, indent);
                    break;
                case JSON_VALUE:
                    if (object instanceof String) {
                        append("\"" + (String) object + "\"");
                    } else {
                        append(String.valueOf(object));
                    }
                    break;
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

        private void write(String data, int indent) {
            builder.append("\n");
            for (int i = 0; i < (indent * indentationSize); i++) {
                builder.append(indentationChar);
            }
            builder.append(data);
        }

        private void append(String data) {
            builder.append(data);
        }

        @Override
        public String toString() {
            return builder.toString();
        }

    }

}
