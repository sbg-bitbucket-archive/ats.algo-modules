package ats.algo.executeRequests;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import ats.algo.core.request.PriceCalcResponse;
import ats.algo.genericsupportfunctions.ConsoleInput;
import ats.core.util.json.JsonUtil;
import ats.core.util.log.Logger;
import ats.core.util.log.LoggerFactory;

public class ReadPriceCalcResponse {

    private static final Logger log = LoggerFactory.getLogger(ReadPriceCalcResponse.class);

    public static void main(String[] args) {
        // LogUtil.initConsoleLogging(Level.TRACE);
        System.out.println("Utility to execute priceCalcResponse from file");
        String reqStr = null;

        do {
            String fileName = ConsoleInput.readString("Enter fileName:", "C:\\AAtmp\\response.json");

            try {
                reqStr = readFile(fileName, Charset.defaultCharset());
            } catch (IOException e) {
                System.out.println("File read error");
                e.printStackTrace();
            }
        } while (reqStr == null);

        @SuppressWarnings("unused")
        PriceCalcResponse response = JsonUtil.unmarshalJson(reqStr, PriceCalcResponse.class);

        log.info("Successfully unmarshalled response");

    }


    static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

}
