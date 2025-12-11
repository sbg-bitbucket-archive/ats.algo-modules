package ats.algo.extractdatafromlog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import ats.algo.genericsupportfunctions.ConsoleInput;


public class ExtractDataFromLog {



    static Map<String, String> filters;

    private static void initFilters() {
        filters = new HashMap<String, String>();
        filters.put("EventCreation", "handleNewEventCreation");
        filters.put("PriceCalcRequest", "PriceCalcRequest json (1 of");
        filters.put("PriceCalcResponse", "PriceCalcResponse json (1 of");
        filters.put("ParamFindRequest", "ParamFindRequest json (1 of");
        filters.put("ParamFindResponse", "ParamFindResponse json (1 of");
        filters.put("PriceCalcError", "Fatal price calc error");
        filters.put("ParamFindError", "Fatal param find error");
        filters.put("PublishMarkets", "Publishing markets");
        filters.put("ResultedMarkets", "Publishing resulted markets");
        filters.put("SupplyMarketPrices", "processSupplyMarketPrices");
        filters.put("DequeuingPendingWork", "Dequeuing pending work");
        filters.put("Publishing", " Publishing ");
        filters.put("WarnWaiting", "has been waiting for");
        filters.put("SettingSourceWeight", "Setting source weight");
        filters.put("ProcessSetMatchParams", "processSetMatchParams");
        filters.put("UnmatchedMarket", "is not matched by any market");

    }

    private static String extractLogLineType(String line) {
        if (line.length() < 42)
            return null;
        String src = line.substring(29, 42);
        if (!src.equals(" AlgoManager "))
            return null;
        if (line.contains(" DEBUG "))
            return null;
        String type = null;
        for (Entry<String, String> e : filters.entrySet()) {
            if (line.contains(e.getValue())) {
                type = e.getKey();
                if (type.equals("PublishMarkets")) {
                    /*
                     * fix for current error in logging
                     */
                    if (line.contains("PublishResultedMarkets"))
                        type = "ResultedMarkets";
                    if (line.contains("ParamFindResults"))
                        type = "PublishParamFindResults";
                }
                break;
            } else {
                if (line.contains("json"))
                    type = null;
                else
                    type = "other";
            }
        }
        return type;
    }

    public static void main(String[] args) {
        System.out.println("Utility to extract log data from files in folder");
        initFilters();

        /*
         * get folder name
         */
        File folder = null;
        do {
            String folderName = ConsoleInput.readString("Enter path:", "C:\\AAtmp\\atsLogs");
            folder = new File(folderName);
        } while (folder == null);
        /*
         * open output file
         */
        FileWriter fw = null;
        try {
            fw = new FileWriter("C:\\AAtmp\\extractedData.txt");
        } catch (IOException e1) {
            e1.printStackTrace();
            System.exit(1);
        }
        /*
         * process each file
         */
        File[] files = folder.listFiles();
        try {
            for (File file : files) {
                String fname = file.getAbsolutePath();
                System.out.println("Processing file: " + fname);
                FileReader fr = new FileReader(fname);
                BufferedReader bufr = new BufferedReader(fr);
                String line;
                line = bufr.readLine();
                while (line != null) {
                    String type = extractLogLineType(line);
                    if (type != null) {
                        LogLineData lld = new LogLineData(type, line);
                        fw.write(lld.toString());
                    }
                    line = bufr.readLine();
                }
                bufr.close();
            }
            fw.close();
            System.out.println("Finished");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.exit(0);
    }


}
