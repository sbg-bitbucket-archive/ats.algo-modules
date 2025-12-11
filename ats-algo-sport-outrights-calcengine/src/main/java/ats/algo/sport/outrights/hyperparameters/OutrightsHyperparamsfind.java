package ats.algo.sport.outrights.hyperparameters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import ats.algo.genericsupportfunctions.CsvFile;
import ats.algo.genericsupportfunctions.FileReadWrite;
import ats.algo.margining.Margining;
import ats.algo.sport.outrights.CompetitionType;
import ats.algo.sport.outrights.server.api.HistoryMap;
import ats.algo.sport.outrights.server.api.TeamDataUpdate;



/*
 * File source : http://www.football-data.co.uk/englandm.php This class load historic data from csv file
 **/

public class OutrightsHyperparamsfind {
    static final ZoneId zoneId = ZoneId.systemDefault();
    static CompetitionType leagueType;

    @SuppressWarnings("resource")
    public static Map<String, HistoryMatchInfos> loadingMatchHistory(String leagueName)
                    throws IOException, ParseException {
        BufferedReader br = new BufferedReader(new FileReader(leagueName + "-matchhistory.csv"));

        String line = null;
        Map<String, HistoryMatchInfos> map = new LinkedHashMap<String, HistoryMatchInfos>();
        // current save key as data-home-away
        br.readLine(); // consume first line and ignore
        while ((line = br.readLine()) != null) {
            String str[] = line.split(",");
            for (int i = 0; i < str.length; i++) {

                String key = str[1] + "-" + str[2] + "-" + str[3];
                Date date = new SimpleDateFormat("dd/MM/yyyy").parse(str[1]);

                double[] bbavMR = {1.0, 2.0, 3.0};
                double bbavO25 = Double.parseDouble(str[53]);
                double bbavU25 = Double.parseDouble(str[55]);

                double bbavLine = Double.parseDouble(str[57]);
                double bbavAHCPHome = Double.parseDouble(str[59]);
                double bbavAHCPAway = Double.parseDouble(str[61]);

                int ftHomeGoal = Integer.parseInt(str[4]);
                int ftAwayGoal = Integer.parseInt(str[5]);
                int homeCorners = Integer.parseInt(str[17]);
                int awayCorners = Integer.parseInt(str[18]);
                int homeYellows = Integer.parseInt(str[19]);
                int awayYellows = Integer.parseInt(str[20]);
                int homeReds = Integer.parseInt(str[21]);
                int awayReds = Integer.parseInt(str[22]);

                double[] prices = new double[3];
                prices[0] = Double.parseDouble(str[23]); // B365H
                prices[1] = Double.parseDouble(str[24]); // B365D
                prices[2] = Double.parseDouble(str[25]); // B365A
                Margining margining = new Margining();
                margining.setMarginingAlgoToEntropy();
                double[] probs = margining.removeMargin(prices, 1.0);

                HistoryMatchInfos matchInfos = new HistoryMatchInfos(str[2], str[3], date, ftHomeGoal, ftAwayGoal,
                                homeCorners, awayCorners, homeYellows, awayYellows, homeReds, awayReds, bbavMR, bbavO25,
                                bbavU25, bbavLine, bbavAHCPHome, bbavAHCPAway, probs[0], probs[1], probs[2]);
                // System.out.println(matchInfos.toString());
                map.put(key, matchInfos);
            }
        }
        System.out.println("Total Matches Number: " + map.size());
        return map;
    }


    public static Map<String, HistoryMatchInfos> loadDataForMatchPredictionTestUse(String toScraping)
                    throws IOException, ParseException {
        // FIXME: BETTER LOGIC TO BE IMPLEMENTED IN THE FUTURE
        if (toScraping.contains("Champ"))
            leagueType = CompetitionType.CHAMPIONSHIP_LEAGUE;
        else if (toScraping.contains("premier"))
            leagueType = CompetitionType.PREMIER_LEAGUE;


        Map<String, HistoryMatchInfos> historyMap = loadingMatchHistory(toScraping);
        FileInputStream fi = new FileInputStream(new File(toScraping.concat("-data")));
        ObjectInputStream oi = new ObjectInputStream(fi);
        HistoryMap pr1 = null;
        try {
            pr1 = (HistoryMap) oi.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        oi.close();
        fi.close();
        bindingODrateToMatchHistoryMap(historyMap, pr1);
        return historyMap;
    }

    public static void main(String[] args) throws IOException, ParseException {
        String toScraping = "premier-league";
        Map<String, HistoryMatchInfos> historyMap = loadDataForMatchPredictionTestUse(toScraping);
        saveData(historyMap);

        // for (Map.Entry<String, HistoryMatchInfos> match : historyMap.entrySet())
        // System.out.println("home goal:"+match.getValue().getFtHomeGoal()+" away
        // goal"+match.getValue().getFtAwayGoal()+" " + match.getValue().getD_rate_home() + " " +
        // match.getValue().getO_rate_home() + " "
        // + match.getValue().getD_rate_away() + " " + match.getValue().getO_rate_away());
        //
        hyperParamsFinder gdForHyperParams = new hyperParamsFinder(historyMap);
        gdForHyperParams.calcLeastSquareLinearAlgebraMethod();
        gdForHyperParams.calc1();
        gdForHyperParams.costG();
        gdForHyperParams.cost4Params();

        /**
         * Param find follows Geoff's suggestions, where the cost function is formulated by
         * 
         * (f(home_o, away_d) - bet365_probs)^2
         * 
         */

    }

    /*
     * save the data to a csv file
     */

    private static final String csvFile = "c:\\aatmp\\matchData.csv";
    private static final String jsonfile = "C:\\aatmp\\matchData.json";

    private static void saveData(Map<String, HistoryMatchInfos> historyMap) {
        List<BasicMatchData> basicContentList = new ArrayList<>(historyMap.size());
        for (HistoryMatchInfos h : historyMap.values()) {
            basicContentList.add(BasicMatchData.generate(h));
        }
        try {
            CsvFile.writeCsvfile(csvFile, basicContentList);
            FileReadWrite.writeJson(jsonfile, basicContentList);
        } catch (Exception e) {
            System.out.println("File io error");
            e.printStackTrace();
        }


    }


    /**
     * This method looks through historyMap and find the closest update for the match date (od rate updated date < match
     * date)
     */
    private static void bindingODrateToMatchHistoryMap(Map<String, HistoryMatchInfos> historyMap, HistoryMap pr1) {
        Map<LocalDateTime, HashMap<String, TeamDataUpdate>> lookUpMap = pr1.getHistoryMap();
        LocalDateTime matchDate = null;
        for (Map.Entry<String, HistoryMatchInfos> match : historyMap.entrySet()) {
            String matchDateandTeams[] = match.getKey().split("-");
            String pattern = "dd/MM/yy HH:mm";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern).withZone(zoneId);

            matchDate = LocalDateTime.parse(matchDateandTeams[0] + " 00:00", formatter);
            // note: to confirm, always find at least 1 day difference from update date to the match date
            LocalDateTime bestMatch = findClosetDateUptoMatchDate(lookUpMap, matchDate);
            fillTeamRates(bestMatch, lookUpMap, match);
            // System.out.println("Best Match:"+bestMatch+" Match Date: "+matchDate);
        }

    }

    private static void fillTeamRates(LocalDateTime bestMatch,
                    Map<LocalDateTime, HashMap<String, TeamDataUpdate>> lookUpMap,
                    Entry<String, HistoryMatchInfos> match) {
        String homeName = match.getValue().getHomeTeamName();
        String awayName = match.getValue().getAwayTeamName();

        HashMap<String, TeamDataUpdate> dataForDate = lookUpMap.get(bestMatch);
        // FIXME: TIDY UP , put in a map
        if (leagueType == CompetitionType.PREMIER_LEAGUE) {
            if (homeName.equals("Brighton")) {
                homeName = "BrightonandHoveAlbion";
            } else if (homeName.equals("Man City")) {
                homeName = "ManchesterCity";
            } else if (homeName.equals("Crystal Palace")) {
                homeName = "CrystalPalace";
            } else if (homeName.equals("Huddersfield")) {
                homeName = "HuddersfieldTown";
            } else if (homeName.equals("West Brom")) {
                homeName = "WestBromwichAlbion";
            } else if (homeName.equals("Bournemouth")) {
                homeName = "AFCBournemouth";
            } else if (homeName.equals("Man United")) {
                homeName = "ManchesterUnited";
            } else if (homeName.equals("West Ham")) {
                homeName = "WestHamUnited";
            } else if (homeName.equals("Tottenham")) {
                homeName = "TottenhamHotspur";
            } else if (homeName.equals("Leicester")) {
                homeName = "LeicesterCity";
            } else if (homeName.equals("Stoke")) {
                homeName = "StokeCity";
            } else if (homeName.equals("Swansea")) {
                homeName = "SwanseaCity";
            }

            if (awayName.equals("Brighton")) {
                awayName = "BrightonandHoveAlbion";
            } else if (awayName.equals("Man City")) {
                awayName = "ManchesterCity";
            } else if (awayName.equals("Crystal Palace")) {
                awayName = "CrystalPalace";
            } else if (awayName.equals("Huddersfield")) {
                awayName = "HuddersfieldTown";
            } else if (awayName.equals("West Brom")) {
                awayName = "WestBromwichAlbion";
            } else if (awayName.equals("Bournemouth")) {
                awayName = "AFCBournemouth";
            } else if (awayName.equals("Man United")) {
                awayName = "ManchesterUnited";
            } else if (awayName.equals("West Ham")) {
                awayName = "WestHamUnited";
            } else if (awayName.equals("Tottenham")) {
                awayName = "TottenhamHotspur";
            } else if (awayName.equals("Leicester")) {
                awayName = "LeicesterCity";
            } else if (awayName.equals("Stoke")) {
                awayName = "StokeCity";
            } else if (awayName.equals("Swansea")) {
                awayName = "SwanseaCity";
            }
        } else if (leagueType == CompetitionType.CHAMPIONSHIP_LEAGUE) {
            if (homeName.equals("Nott'm Forest")) {
                homeName = "NottinghamForest";
            } else if (homeName.equals("Aston Villa")) {
                homeName = "AstonVilla";
            } else if (homeName.equals("Bristol City")) {
                homeName = "BristolCity";
            } else if (homeName.equals("Burton")) {
                homeName = "BurtonAlbion";
            } else if (homeName.equals("Ipswich")) {
                homeName = "IpswichTown";
            } else if (homeName.equals("Preston")) {
                homeName = "PrestonNorthEnd";
            } else if (homeName.equals("QPR")) {
                homeName = "QueensParkRangers";
            } else if (homeName.equals("Sheffield United")) {
                homeName = "SheffieldUnited";
            } else if (homeName.equals("Sheffield Weds")) {
                homeName = "SheffieldWednesday";
            } else if (homeName.equals("Wolves")) {
                homeName = "Wolverhampton";
            } else if (homeName.equals("Cardiff")) {
                homeName = "CardiffCity";
            } else if (homeName.equals("Hull")) {
                homeName = "HullCity";
            } else if (homeName.equals("Leeds")) {
                homeName = "LeedsUnited";
            } else if (homeName.equals("Derby")) {
                homeName = "DerbyCounty";
            } else if (homeName.equals("Norwich")) {
                homeName = "NorwichCity";
            }

            if (awayName.equals("Nott'm Forest")) {
                awayName = "NottinghamForest";
            } else if (awayName.equals("Aston Villa")) {
                awayName = "AstonVilla";
            } else if (awayName.equals("Bristol City")) {
                awayName = "BristolCity";
            } else if (awayName.equals("Burton")) {
                awayName = "BurtonAlbion";
            } else if (awayName.equals("Ipswich")) {
                awayName = "IpswichTown";
            } else if (awayName.equals("Preston")) {
                awayName = "PrestonNorthEnd";
            } else if (awayName.equals("QPR")) {
                awayName = "QueensParkRangers";
            } else if (awayName.equals("Sheffield United")) {
                awayName = "SheffieldUnited";
            } else if (awayName.equals("Sheffield Weds")) {
                awayName = "SheffieldWednesday";
            } else if (awayName.equals("Wolves")) {
                awayName = "Wolverhampton";
            } else if (awayName.equals("Cardiff")) {
                awayName = "CardiffCity";
            } else if (awayName.equals("Hull")) {
                awayName = "HullCity";
            } else if (awayName.equals("Leeds")) {
                awayName = "LeedsUnited";
            } else if (awayName.equals("Derby")) {
                awayName = "DerbyCounty";
            } else if (awayName.equals("Norwich")) {
                awayName = "NorwichCity";
            }
        }
        TeamDataUpdate homeData = dataForDate.get(homeName);
        TeamDataUpdate awayData = dataForDate.get(awayName);
        if (homeData == null || awayData == null)
            throw new IllegalArgumentException("Error!");
        // System.out.println("Found Data: home-" + homeData + " away-" + awayData);
        match.getValue().setORateHome(homeData.getRatingOffense());
        match.getValue().setDRateHome(homeData.getRatingDefense());

        match.getValue().setORateAway(awayData.getRatingOffense());
        match.getValue().setDRateAway(awayData.getRatingDefense());
    }

    private static LocalDateTime findClosetDateUptoMatchDate(
                    Map<LocalDateTime, HashMap<String, TeamDataUpdate>> lookUpMap, LocalDateTime matchDate) {

        long minDiff = Long.MAX_VALUE, absMin = Long.MAX_VALUE, currentTime = matchDate.atZone(zoneId).toEpochSecond();
        LocalDateTime minDate = null;
        LocalDateTime closestDate = null;
        for (Map.Entry<LocalDateTime, HashMap<String, TeamDataUpdate>> lookUpEntry : lookUpMap.entrySet()) {
            LocalDateTime date = lookUpEntry.getKey();
            long dateTime = date.atZone(zoneId).toEpochSecond();
            long diff = currentTime - dateTime;
            long absDiff = Math.abs(currentTime - dateTime);

            // if(matchDate.equals(date))
            // System.out.println("Same Day");
            if ((diff > 1) && (diff < minDiff)) { // needs to be negative indicates the date before
                minDiff = diff;
                minDate = date;
            }
            if (absDiff < absMin) { // needs to be negative indicates the date before
                absMin = absDiff;
                closestDate = date;
            }
        }

        if (minDate == null) {
            System.out.println("Warning:" + "Can not find ratings for the match date" + matchDate.toString());
            minDate = closestDate;
        }

        return minDate;
    }

}
