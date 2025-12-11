package ats.algo.scraping.scrapers;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * gets league data from the fivethirtyeight website
 * 
 * @author gicha
 *
 */
public class FiveThirtyEightScraper {

    private final static String FIVE_THIRTY_EIGHT_SITE = "https://projects.fivethirtyeight.com/soccer-predictions/";

    public Map<String, FiveThirtyEightTeamData> getTeamData(String leagueName) {

        Map<String, FiveThirtyEightTeamData> teamMap = new LinkedHashMap<String, FiveThirtyEightTeamData>();
        try {
            String searchUrl = FIVE_THIRTY_EIGHT_SITE.concat(leagueName);

            Document doc = Jsoup.connect(searchUrl).get();
            Elements tableElements = doc.getElementsByClass("forecast-table");

            // Elements tableHeaderEles = tableElements.select("thead tr th");
            // System.out.println("headers");
            // for (int i = 0; i < tableHeaderEles.size(); i++) {
            // System.out.println(tableHeaderEles.get(i).text());
            // }

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
                    else if (j == 4)
                        w = (int) Double.parseDouble(rowItems.get(j).text());
                    else if (j == 5)
                        d = (int) Double.parseDouble(rowItems.get(j).text());
                    else if (j == 6)
                        l = (int) Double.parseDouble(rowItems.get(j).text());
                    else if (j == 7)
                        goalDiff = (int) Double.parseDouble(rowItems.get(j).text());
                    // else if (j == 8)
                    // pts = Double.parseDouble(rowItems.get(j).text());
                }
                teamMap.put(team, new FiveThirtyEightTeamData(off, def, w, d, l, goalDiff, team));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return teamMap;
    }

    @SuppressWarnings("unused")
    private void writeToCSV(Map<String, FiveThirtyEightTeamData> teamMap2) {
        PrintWriter pw;
        try {
            String filename = "teams.csv";
            pw = new PrintWriter(new File(filename));
            StringBuilder sb = new StringBuilder();
            for (Entry<String, FiveThirtyEightTeamData> entry : teamMap2.entrySet()) {
                sb.append(entry.getKey()).append(',').append(entry.getValue().toString()).append('\n');
            }
            pw.write(sb.toString());
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws FileNotFoundException {
        FiveThirtyEightScraper scraper = new FiveThirtyEightScraper();
        scraper.getTeamData("premier-league");
        System.out.println("-----------------------------------");
        scraper.getTeamData("bundesliga");
        System.out.println("-----------------------------------");
        scraper.getTeamData("eliteserien");
    }
}
