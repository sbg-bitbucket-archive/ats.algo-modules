package ats.algo.sport.outrights.server.scrapers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ats.algo.genericsupportfunctions.PairOfDoubles;
import ats.algo.sport.outrights.calcengine.core.Teams;
import ats.algo.sport.outrights.server.api.TargetPointsEntry;

/**
 * utility method for scraping data from SI
 * 
 * @author jin
 *
 */
public class SportingIndexScraper {

    /**
     * scrapes the points for the season at which SI is offering to buy and sell each team
     * 
     * @param competitionId either "premier-league" or "championship-league". Anything else returns null in this version
     * @param teams Each team includes a property "sportingIndexName" which can be used to match the scraped website
     *        content
     * @return Map of the scraped data. The keys are the team Id's, the data content are the scraped points values for
     *         that team
     */
    public static Map<String, TargetPointsEntry> scrape(String competitionId, Teams teams) throws IOException {
        String url = getUrlForCompetition(competitionId);
        String url2 = getSubUrlForCompetition(competitionId);//
        if (url == null || url2 == null)
            return null;
        else {
            Map<String, TargetPointsEntry> items = new HashMap<>(teams.getTeams().size());

            Map<String, PairOfDoubles> keyPriceMap = new HashMap<>();

            Document doc = Jsoup.connect(url).get();
            Elements tableElements = doc.getElementsByClass("prices");
            Document doc2 = Jsoup.connect(url2).ignoreContentType(true).get();
            Elements tableElements2 = doc2.getAllElements();
            String separateBy = "\"Markets\"";
            String[] contents = tableElements2.get(3).text().split(separateBy);
            String[] contents2 = contents[1].split("},");
            fillPriceMap(contents2, keyPriceMap);
            Elements tableRowElements = tableElements.select("ul");
            Element tableRowElement = tableRowElements.get(2);
            Elements nameRows = tableRowElement.select("li");

            for (int i = 0; i < nameRows.size(); i++) {
                String[] nameAndKey = nameRows.get(i).toString().split("\"");
                String[] keys = nameAndKey[3].split("-");
                String key = keys[keys.length - 1];
                PairOfDoubles prices = keyPriceMap.get(key);
                if (prices == null)
                    throw new IllegalArgumentException("Missing price");
                String name = nameAndKey[14].substring(1, nameAndKey[14].indexOf(" Points"));

                String itemKey = teams.getFromSportingIndexName(name).getTeamID();
                if (itemKey == null)
                    throw new IllegalArgumentException("Missing team key");
                items.put(itemKey, new TargetPointsEntry(itemKey, (prices.A + prices.B) / 2));
            }
            return items;
        }
    }



    private static void fillPriceMap(String[] contents2, Map<String, PairOfDoubles> keyPriceMap) {
        for (String line : contents2) {

            String[] detailsInLine = line.split(",");
            String[] buyPriceLine = detailsInLine[0].split("\"Buy\":");
            double buy = Double.parseDouble(buyPriceLine[1]);
            String[] sellPriceLine = detailsInLine[3].split("\"Sell\":");
            double sell = Double.parseDouble(sellPriceLine[1]);
            String[] keyLine = detailsInLine[2].split("-");
            String key = keyLine[keyLine.length - 1].replaceAll(",", "").replaceAll("\"", "");
            keyPriceMap.put(key, new PairOfDoubles(buy, sell));
        }

    }

    /**
     * fixed list of the urls for sporting index
     * 
     * @param competitionName
     * @return
     */
    private static String getUrlForCompetition(String competitionName) {
        String url;
        switch (competitionName) {
            case "premier-league":
                url = "https://www.sportingindex.com/spread-betting/football/domestic-premier-league/group_a.5c9dabe2-322d-40c7-b04d-925da5f6175a/premier-league-points-2018-19";
                break;
            case "championship":
                url = "https://www.sportingindex.com/spread-betting/football/domestic-championship/group_a.ef2ccbd1-46ad-41be-99c7-c4a5e9bb250b/championship-points-2018-19";
                break;
            case "la-liga":
                url = "https://www.sportingindex.com/spread-betting/football/european-long-term-markets/group_c.62362e9e-aefd-4c60-8a7d-4a426968019c/la-liga-points-2018-19";
                break;
            default:
                url = null;
        }
        return url;
    }

    private static String getSubUrlForCompetition(String competitionName) {
        String url;
        switch (competitionName) {
            case "premier-league":
                url = "https://livepricing.sportingindex.com/LivePricing.svc/jsonp/GetLivePricesByMeeting?meetingKey=group_a.5c9dabe2-322d-40c7-b04d-925da5f6175a&checksum=0&callback=_jqjsp&_1538746934851=";
                break;
            case "championship":
                url = "https://livepricing.sportingindex.com/LivePricing.svc/jsonp/GetLivePricesByMeeting?meetingKey=group_a.ef2ccbd1-46ad-41be-99c7-c4a5e9bb250b&checksum=0&callback=_jqjsp&_1539010111641= HTTP/1.1";
                break;
            case "la-liga":
                url = "https://livepricing.sportingindex.com/LivePricing.svc/jsonp/GetLivePricesByMeeting?meetingKey=group_c.62362e9e-aefd-4c60-8a7d-4a426968019c&checksum=0&callback=_jqjsp&_1539746934851=";
                break;
            default:
                url = null;
        }
        return url;
    }


}
