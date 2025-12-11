package ats.algo.genericsupportfunctions;

public class JsonProperty {

    /**
     * extracts the first property with given name from a jason string. expected to be of the form "property" : "value"
     * 
     * @param json
     * @param property
     * @return
     */
    static public String getStringProperty(String json, String property) {
        int i = json.indexOf(property);
        if (i == -1)
            return null;
        int j = json.indexOf("\"", i + property.length() + 2);
        int beginIndex = j + 1;
        int endIndex = json.indexOf("\"", beginIndex);
        if (endIndex == -1)
            throw new IllegalArgumentException();
        String value = json.substring(beginIndex, endIndex);
        return value;
    }
}
