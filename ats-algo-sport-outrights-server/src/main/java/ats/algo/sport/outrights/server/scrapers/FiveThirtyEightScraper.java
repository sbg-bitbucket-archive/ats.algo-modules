package ats.algo.sport.outrights.server.scrapers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ats.algo.sport.outrights.hyperparameters.HistoryMap;
import ats.algo.sport.outrights.server.api.TeamDataUpdate;
import ats.org.json.JSONException;

public class FiveThirtyEightScraper {
    static final ZoneId zoneId = ZoneId.systemDefault();
    private final static String FIVE_THIRTY_EIGHT_SITE = "https://projects.fivethirtyeight.com/soccer-predictions/";
    private final static String FIVE_THIRTY_EIGHT_SITE_JSON_HEAD =
                    "https://projects.fivethirtyeight.com/soccer-predictions/forecasts/2017_";
    private final static String FIVE_THIRTY_EIGHT_SITE_JSON_TAIL = "_forecast.json";

    public static Map<String, TeamDataUpdate> getTeamData(String leagueName, boolean normalLeague) throws IOException {

        Map<String, TeamDataUpdate> teamMap = new HashMap<>();
        String searchUrl = FIVE_THIRTY_EIGHT_SITE.concat(leagueName);
        Document doc = Jsoup.connect(searchUrl).get();
        Elements tableElements = doc.getElementsByClass("forecast-table");
        Elements tableRowElements = tableElements.select(":not(thead) tr");
        for (int i = 0; i < tableRowElements.size(); i++) { // rows
            Element row = tableRowElements.get(i);
            // System.out.println("row");
            Elements rowItems = row.select("td");
            row.getElementsByClass("name").select("span").remove();
            // System.out.println("Removing Span" +
            // row.getElementsByClass("name").select("span").remove());
            String team = "";
            double off = 0.0;
            double def = 0.0;
            // double pts = 0.0;
            int goalDiff = 0;
            int w = 0;
            int l = 0;
            int d = 0;

            for (int j = 0; j < rowItems.size(); j++) { // cols
                if (j == 0)
                    team = rowItems.get(j).text();
                // else if(j==1)
                // pts = Double.parseDouble(rowItems.get(j).text());
                else if (j == 2)
                    off = Double.parseDouble(rowItems.get(j).text());
                else if (j == 3)
                    def = Double.parseDouble(rowItems.get(j).text());
                else if (normalLeague && j == 4)
                    w = (int) Double.parseDouble(rowItems.get(j).text());
                else if (normalLeague && j == 5)
                    d = (int) Double.parseDouble(rowItems.get(j).text());
                else if (normalLeague && j == 6)
                    l = (int) Double.parseDouble(rowItems.get(j).text());
                else if (normalLeague && j == 7)
                    goalDiff = (int) Double.parseDouble(rowItems.get(j).text());
                // else if (j == 8)
                // pts = Double.parseDouble(rowItems.get(j).text());
            }
            teamMap.put(team, new TeamDataUpdate(off, def, w, d, l, goalDiff));
        }
        return teamMap;
    }

    /**
     * This method retrive the history team ratings from Jsons we got back.
     * https://projects.fivethirtyeight.com/soccer-predictions/forecasts/2017_ return a Map of String (Date) with Value
     * of TeamDateUpdate (Ratings for all teams)
     */
    public static Map<LocalDateTime, HashMap<String, TeamDataUpdate>> getTeamDataHistory(String leagueName)
                    throws IOException, JSONException {

        Map<LocalDateTime, HashMap<String, TeamDataUpdate>> updateMap = new HashMap<>();

        String searchUrl = FIVE_THIRTY_EIGHT_SITE_JSON_HEAD.concat(leagueName).concat(FIVE_THIRTY_EIGHT_SITE_JSON_TAIL);
        String InboxJson = Jsoup.connect(searchUrl).ignoreContentType(true).execute().body();

        System.out.println(InboxJson);

        String wordToFind = "\"forecasts\":";
        Pattern word = Pattern.compile(wordToFind);
        Matcher match = word.matcher(InboxJson);
        String usefulInfos = "";
        while (match.find()) {
            System.out.println("Found forecasts at index " + match.start() + " - " + (match.end() - 1));
            int index1 = match.end();
            int index2 = InboxJson.length() - 1;
            usefulInfos = InboxJson.substring(index1, index2);
        }
        /**
         * Firstly split the string into different updates Secondly split each updates to teams
         */
        String[] updatesArrays = usefulInfos.split("\"last_updated\":");
        for (String update : updatesArrays)
            collectThisUpdateTeamInfos(updateMap, update);

        return updateMap;
    }

    private static void collectThisUpdateTeamInfos(Map<LocalDateTime, HashMap<String, TeamDataUpdate>> updateMap,
                    String update) {

        if (!update.contains("teams")) {
            return;
        }
        String tempSubString = update.substring(0, update.indexOf(","));
        String datestring = tempSubString.substring(1, tempSubString.indexOf("T")) + " "
                        + tempSubString.substring(tempSubString.indexOf("T") + 1, tempSubString.indexOf("T") + 6);

        String[] teams = update.replace("teams\":[", "").split("},");

        String pattern = "yyyy-MM-dd HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern).withZone(zoneId);

        LocalDateTime updateDate = LocalDateTime.parse(datestring, formatter);


        // System.out.println("This update date is: " + updateDate.toString());

        HashMap<String, TeamDataUpdate> teamData = new HashMap<String, TeamDataUpdate>();

        for (String teamStr : teams) {
            if (teamStr.contains("\"name\":")) {// only process those entries where is got a team name
                String cleanedTeam = cleanTeamString(teamStr);
                String[] infos = cleanedTeam.split(",");
                fillingTeamData(infos, teamData);
                // System.out.println(cleanedTeam);
            }
        }

        updateMap.put(updateDate, teamData);

    }



    private static void fillingTeamData(String[] infos, HashMap<String, TeamDataUpdate> teamData) {
        String teamName = null;
        double orating = 0.0;
        double drating = 0.0;
        for (String infoString : infos) {
            if (infoString.contains("\"name\":\""))
                teamName = infoString.replace("\"name\":\"", "").replace("\",", "").replace("\"", "");

            if (infoString.contains("\"d_rating\":"))
                drating = Double.parseDouble(infoString.replace("\"d_rating\":", "").replace(",", ""));

            if (infoString.contains("\"o_rating\":"))
                orating = Double.parseDouble(infoString.replace("\"o_rating\":", "").replace(",", ""));
        }

        if (teamName == null)
            System.out.println("");
        teamData.put(teamName, new TeamDataUpdate(orating, drating, 0, 0, 0, 0));
    }


    private static String cleanTeamString(String teamStr) {
        return teamStr.replace("{", "").replace("}", "").replace("]", "").replace("\n", "").replace("\r", "")
                        .replace(" ", "");
    }


    public static void main(String[] args) {
        String toScraping = "championship";// premier-league
        Map<LocalDateTime, HashMap<String, TeamDataUpdate>> historyMap = null;
        try {
            historyMap = FiveThirtyEightScraper.getTeamDataHistory(toScraping);
        } catch (IOException e2) {
            e2.printStackTrace();
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        File f = new File(toScraping.concat("-data"));
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(f));

            HistoryMap map = new HistoryMap(historyMap);
            oos.writeObject(map);
            oos.flush();
            oos.close();
            /*
             * Load information from saved txt
             */
            FileInputStream fi = new FileInputStream(new File(toScraping.concat("-data")));
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



}
