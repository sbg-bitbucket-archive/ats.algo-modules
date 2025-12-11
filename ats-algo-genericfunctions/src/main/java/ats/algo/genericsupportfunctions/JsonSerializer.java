package ats.algo.genericsupportfunctions;


import ats.core.util.json.JsonUtil;

public class JsonSerializer {



    private JsonSerializer() {}

    public static String serialize(Object o, boolean prettyOutput) {
        return JsonUtil.marshalJson(o, prettyOutput);
    }

    public static String serialize(Object o, Class<?> baseClass, boolean prettyOutput) {
        String json = JsonUtil.marshalJson(o, prettyOutput);
        json = json.replaceFirst(o.getClass().getSimpleName(), baseClass.getSimpleName());
        return json;
    }

    public static <T> T deserialize(String json, Class<T> T) {
        return JsonUtil.unmarshalJson(json, T);
    }

    public static void print(Object o) {
        System.out.println(JsonUtil.marshalJson(o, true));
    }



}
