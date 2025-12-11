package ats.algo.sport.outrights.server.awsscraper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ats.algo.genericsupportfunctions.CsvFile;
import ats.algo.genericsupportfunctions.FileReadWrite;
import ats.algo.sport.outrights.CompetitionType;
import ats.algo.sport.outrights.hyperparameters.BasicMatchData;
import ats.algo.sport.outrights.hyperparameters.HistoryMap;
import ats.algo.sport.outrights.hyperparameters.HistoryMatchInfos;
import ats.algo.sport.outrights.hyperparameters.OutrightsHyperparamsfind;
import ats.algo.sport.outrights.server.api.TeamDataUpdate;
import ats.algo.sport.outrights.server.scrapers.FiveThirtyEightScraper;
import ats.org.json.JSONException;

public class ConnectingMGDB {

    public void updateDB(String toScraping) {
        Map<LocalDateTime, HashMap<String, TeamDataUpdate>> historyMap = null;
        try {
            historyMap = FiveThirtyEightScraper.getTeamDataHistory(toScraping);
        } catch (IOException e2) {
            e2.printStackTrace();
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        File f = new File(toScraping.concat("-rating-data"));
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(f));
            /*
             * Write this map as a object to local folder
             */
            HistoryMap map = new HistoryMap(historyMap);
            oos.writeObject(map);
            oos.flush();
            oos.close();
            /**
             * Underneath code is optional
             */
            /*
             * Load information from saved object, print out the saved history object
             */
            FileInputStream fi = new FileInputStream(new File(toScraping.concat("-rating-data")));
            ObjectInputStream oi = new ObjectInputStream(fi);
            HistoryMap pr1 = null;
            try {
                pr1 = (HistoryMap) oi.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            System.out.println("Contents in txt");
            System.out.println(pr1.toString());
            oi.close();
            fi.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }


    }


    public void bindDatas(String toScraping) {
        Map<String, HistoryMatchInfos> historyMap = null;
        try {
            if (toScraping.toUpperCase().contains("CHAMP"))
                OutrightsHyperparamsfind.setLeagueType(CompetitionType.CHAMPIONSHIP_LEAGUE);
            else if (toScraping.toUpperCase().contains("PREMIER"))
                OutrightsHyperparamsfind.setLeagueType(CompetitionType.PREMIER_LEAGUE);
            else {
                throw new IllegalArgumentException("UNKNOWN COMPETION TYPE");
            }
            historyMap = OutrightsHyperparamsfind.loadDataForMatchPredictionTestUse(toScraping, false);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("Saving loaded history data...");
        saveData(toScraping, historyMap);
    }

    private static final String csvFile = "matchData.csv";
    private static final String jsonfile = "matchData.json";

    /**
     * Combined map and ratings are saved into a csv file, also save a copy in json
     **/
    private static void saveData(String scraped, Map<String, HistoryMatchInfos> historyMap) {
        List<BasicMatchData> basicContentList = new ArrayList<>(historyMap.size());
        for (HistoryMatchInfos h : historyMap.values()) {
            basicContentList.add(BasicMatchData.generate(h));
        }
        try {
            CsvFile.writeCsvfile(scraped + csvFile, basicContentList);
            FileReadWrite.writeJson(scraped + jsonfile, basicContentList);
        } catch (Exception e) {
            System.out.println("File io error");
            e.printStackTrace();
        }


    }

    public void dropDBCollection() {

    }

    public void shutdownConnection() {

    }

}
