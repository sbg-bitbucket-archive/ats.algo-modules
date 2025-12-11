package ats.algo.genericsupportfunctions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import ats.core.util.json.JsonUtil;

public class FileReadWrite {

    /**
     * Saves a string to file
     * 
     * @param fname
     * @param content
     * @throws IOException
     */
    public static void writeString(String fname, String content) throws IOException {
        Files.write(Paths.get(fname), content.getBytes());
    }

    /**
     * Saves an object to file in JsonFormat
     * 
     * @param fname
     * @param object
     * @throws IOException
     */
    public static void writeJson(String fname, Object object) throws IOException {
        writeString(fname, JsonUtil.marshalJson(object));
    }


    public static <T> T readJson(String fileName, Class<T> targetClass) throws IOException {
        T instance = null;
        BufferedReader in = null;
        FileReader fr = new FileReader(fileName);
        in = new BufferedReader(fr);
        Stream<String> lines = in.lines();
        StringBuilder data = new StringBuilder();
        lines.forEach(line -> data.append(line).append("\n"));
        lines.close();
        String json = data.toString();
        instance = JsonUtil.unmarshalJson(json, targetClass);
        if (in != null)
            in.close();
        return instance;
    }

}
