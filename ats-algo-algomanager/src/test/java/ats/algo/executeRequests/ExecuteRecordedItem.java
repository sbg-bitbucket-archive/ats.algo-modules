package ats.algo.executeRequests;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.ObjectMapper;

import ats.algo.core.recordplayback.RecordedItem;
import ats.algo.core.request.PriceCalcResponse;

import ats.core.util.log.Level;
import ats.core.util.log.LogUtil;
import ats.core.util.log.Logger;
import ats.core.util.log.LoggerFactory;

public class ExecuteRecordedItem {

    private static final Logger log = LoggerFactory.getLogger(ExecuteRecordedItem.class);

    public static void main(String[] args) {
        LogUtil.initConsoleLogging(Level.TRACE);
        System.out.println("Utility to read and execute recordedItem from file");
        String reqStr = null;
        String fileName = "C:\\AAtmp\\testMatch\\R-8f6cd56d-7f61-48ab-af8a-ac181ef20088.json";
        try {
            reqStr = readFile(fileName, Charset.defaultCharset());
        } catch (IOException e) {
            System.out.println("File read error");
            e.printStackTrace();
        }
        ObjectMapper mapperIn = new ObjectMapper();
        @SuppressWarnings("unused")
        RecordedItem recordedItem = null;
        boolean readOk = false;
        try {
            recordedItem = mapperIn.readValue(reqStr, RecordedItem.class);
            readOk = true;
        } catch (Exception e1) {
            /*
             * do nothing
             */
        }
        if (!readOk) {
            mapperIn.enableDefaultTyping();
            mapperIn.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
            try {
                recordedItem = mapperIn.readValue(reqStr, RecordedItem.class);
                readOk = true;
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    // public static void main(String[] args) {
    // LogUtil.initConsoleLogging(Level.TRACE);
    // System.out.println("Utility to read and execute recordedItem from file");
    // String reqStr = null;
    // String fileName = "C:\\AAtmp\\testMatch\\recording-header.json";
    // try {
    // reqStr = readFile(fileName, Charset.defaultCharset());
    // } catch (IOException e) {
    // System.out.println("File read error");
    // e.printStackTrace();
    // }
    // ObjectMapper mapperIn = new ObjectMapper();
    // RecordingHeader header = null;
    // boolean readOk = false;
    // try {
    // header = mapperIn.readValue(reqStr, RecordingHeader.class);
    // readOk = true;
    // } catch (Exception e1) {
    // /*
    // * do nothing
    // */
    // }
    // if (!readOk) {
    // mapperIn.enableDefaultTyping();
    // mapperIn.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
    // try {
    // header = mapperIn.readValue(reqStr, RecordingHeader.class);
    // readOk = true;
    // } catch (Exception e1) {
    // e1.printStackTrace();
    // }
    // }
    // System.out.println("Read ok: " + readOk);
    // }

    static void handlePriceCalcResponse(long eventId, PriceCalcResponse response) {
        log.info("Response received:" + eventId);
        log.info(response);
    }

    static void handlePriceCalcError(long eventId, String requestId) {
        log.info("Fatal priceCalcError received:" + eventId);
    }

    static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

}
