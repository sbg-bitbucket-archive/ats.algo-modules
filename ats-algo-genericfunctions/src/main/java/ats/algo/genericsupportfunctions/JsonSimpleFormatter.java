package ats.algo.genericsupportfunctions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
public class JsonSimpleFormatter {

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
                write("<empty>", indent);
            } else {
                writeNewLine();
                for (int i = 0; i < length; i++) {
                    visit(array.get(i), indent);
                    writeNewLine();
                }
                writeNewLine();
            }

        }

        @SuppressWarnings("unchecked")
        private void visit(JSONObject obj, int indent) throws JSONException {
            final int length = obj.length();

            if (length == 0) {
                write("<empty>", indent);
            } else {
                if (indent > 0)
                    writeNewLine();
                List<String> sortedKeys = getSortedKeys(obj.keys(), obj.length());
                for (String key : sortedKeys) {
                    Object value = obj.get(key);
                    if (getJSONObjectType(value) == JSONObjectType.JSON_VALUE) {
                        write(key + ": " + value.toString(), indent);
                        writeNewLine();
                    } else {
                        write(key + ":", indent);
                        writeNewLine();
                        visit(value, indent + 1);
                    }
                }
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
            // System.out.printf("indent: %d, class: %s, object: %s\n", indent,
            // object.getClass().getName(),
            // object.toString());
            JSONObjectType type = getJSONObjectType(object);
            switch (type) {
                case JSON_ARRAY:
                    visit((JSONArray) object, indent);
                    break;
                case JSON_OBJECT:
                    visit((JSONObject) object, indent);
                    break;
                case JSON_VALUE:
                    write(String.valueOf(object), indent);
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
            for (int i = 0; i < (indent * indentationSize); i++) {
                builder.append(indentationChar);
            }
            builder.append(data);
            lastWriteWasNewLine = false;
        }

        boolean lastWriteWasNewLine = false;

        private void writeNewLine() {
            if (!lastWriteWasNewLine) {
                lastWriteWasNewLine = true;
                builder.append("\n");
            }
        }

        @Override
        public String toString() {
            return builder.toString();
        }

    }

}
