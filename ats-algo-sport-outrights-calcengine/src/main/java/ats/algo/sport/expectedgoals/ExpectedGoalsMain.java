package ats.algo.sport.expectedgoals;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import ats.algo.genericsupportfunctions.PairOfDoubles;

public class ExpectedGoalsMain {

    public static void main(String[] args) {
        // String inputFile = "C:\\aatmp\\prices.csv";
        // String outputFile = "C:\\aatmp\\pricesWithExpGoals.csv";
        // LinkedHashMap<String, List<String>> input = CsvFile.readCsvFile(inputFile);
        // LinkedHashMap<String, List<String>> output = calcExpectedGoals(input);
        // boolean success = CsvFile.writeCsvfile(outputFile, output);
        // if (success)
        // System.out.println ("output csv file produced successfully");
        // else
        // System.out.println ("error generating output file");
    }



    @SuppressWarnings("unused")
    private static LinkedHashMap<String, List<String>> calcExpectedGoals(LinkedHashMap<String, List<String>> input) {
        List<String> pricesHome = input.get("B365H");
        List<String> pricesAway = input.get("B365A");
        List<String> pricesDraw = input.get("B365D");
        int n = pricesHome.size();
        List<Price> prices = new ArrayList<Price>(n);
        for (int i = 0; i < n; i++) {
            double priceHome = Double.parseDouble(pricesHome.get(i));
            double priceAway = Double.parseDouble(pricesAway.get(i));
            double priceDraw = Double.parseDouble(pricesDraw.get(i));
            Price price = new Price(priceHome, priceAway, priceDraw);
            prices.add(price);
        }
        List<String> expGoalsHome = new ArrayList<>(n);
        List<String> expGoalsAway = new ArrayList<>(n);
        ExpectedGoals expectedGoals = new ExpectedGoals();
        for (Price price : prices) {
            PairOfDoubles expGoals = expectedGoals.calcExpectedGoals(price.priceHome, price.priceAway, price.priceDraw);
            price.expGoalsHome = expGoals.A;
            price.expGoalsAway = expGoals.B;
        }
        for (int i = 0; i < n; i++) {
            Price price = prices.get(i);
            expGoalsHome.add(String.format("%.2f", price.expGoalsHome));
            expGoalsAway.add(String.format("%.2f", price.expGoalsAway));
        }
        LinkedHashMap<String, List<String>> output = new LinkedHashMap<>(5);
        output.put("B365H", pricesHome);
        output.put("B365D", pricesDraw);
        output.put("B365A", pricesAway);
        output.put("expGoalsHome", expGoalsHome);
        output.put("expGoalsAway", expGoalsAway);
        return output;
    }

}
