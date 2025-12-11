package ats.algo.genericsupportfunctions;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Stream;

import ats.core.util.json.JsonUtil;

public class ReadResourceFile {

    /**
     * read object stored as a json string
     * 
     * @param fileName
     * @param originatingClass Name of the class from which this method is invoked. Needed to identify where the
     *        resource file is located.
     * @param targetClass Type of object to be returned
     * @return object of type targetClass
     */
    public static <T> T readObject(String fileName, Class<?> originatingClass, Class<T> targetClass) {
        BufferedReader in = getReader(fileName, originatingClass);
        Stream<String> lines = in.lines();
        StringBuilder data = new StringBuilder();
        lines.forEach(line -> data.append(line).append("\n"));
        lines.close();
        String json = data.toString();
        T instance = JsonUtil.unmarshalJson(json, targetClass);
        return instance;
    }



    /**
     * gets a reader for a file stored within project in src/main/resources folder. Deals with the differences in
     * behaviour between Eclipse and jar file when trying to locate these files
     * 
     * @param fileName
     * @param originatingClass
     * @return
     */
    public static BufferedReader getReader(String fileName, Class<?> originatingClass) {
        InputStream stream;
        stream = originatingClass.getResourceAsStream("/" + fileName);
        if (stream == null)
            stream = originatingClass.getResourceAsStream("/resources/" + fileName);
        if (stream == null)
            return null;
        InputStreamReader isReader = new InputStreamReader(stream);
        return new BufferedReader(isReader);
    }

}
