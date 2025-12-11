package ats.algo.sport.buildabet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BuildABet {
    private Map<String, BuildBetSelectionHandler> buildBetMap;
    private double[][] scoreGridOne = new double[30][30];

    public BuildABet() {
        /*
         * Place to add methods to build a bet
         */
        buildBetMap = new HashMap<String, BuildBetSelectionHandler>();
        buildBetMap.put("FT:AXB-A", (int a, int b, double line) -> {
            return a > b;
        });

        buildBetMap.put("FT:AXB-X", (int a, int b, double line) -> {
            return a == b;
        });
        buildBetMap.put("FT:AXB-B", (int a, int b, double line) -> {
            return a < b;
        });

        buildBetMap.put("FT:OU-Over", (int a, int b, double line) -> {
            return (double) (a + b) > line;
        });
        buildBetMap.put("FT:OU-Under", (int a, int b, double line) -> {
            return (double) (a + b) < line;
        });

        buildBetMap.put("FT:A:OU-Over", (int a, int b, double line) -> {
            return (double) (a) > line;
        });
        buildBetMap.put("FT:A:OU-Under", (int a, int b, double line) -> {
            return (double) (a) < line;
        });

        buildBetMap.put("FT:B:OU-Over", (int a, int b, double line) -> {
            return (double) (b) > line;
        });
        buildBetMap.put("FT:B:OU-Under", (int a, int b, double line) -> {
            return (double) (b) < line;
        });

        buildBetMap.put("FT:AHCP-AH", (int a, int b, double line) -> {
            return (double) (a - b) > line;
        });
        buildBetMap.put("FT:AHCP-BH", (int a, int b, double line) -> {
            return (double) (a - b) < line;
        });

        buildBetMap.put("FT:SPRD-AH", (int a, int b, double line) -> {
            return (double) (a - b) > line;
        });
        buildBetMap.put("FT:SPRD-BH", (int a, int b, double line) -> {
            return (double) (a - b) < line;
        });

        buildBetMap.put("FT:3HCP-AH", (int a, int b, double line) -> {
            return (double) (a - b) > line;
        });
        buildBetMap.put("FT:3HCP-BH", (int a, int b, double line) -> {
            return (double) (a - b) < line;
        });

        buildBetMap.put("FT:3HCP-DH", (int a, int b, double line) -> {
            return (double) (a - b) == line;
        });

        buildBetMap.put("FT:OE-Odd", (int a, int b, double line) -> {
            return (a + b) % 2 == 1;
        });

        buildBetMap.put("FT:OE-Even", (int a, int b, double line) -> {
            return (a + b) % 2 == 0;
        });

        buildBetMap.put("FT:BTS-Yes", (int a, int b, double line) -> {
            return (a > 0 && b > 0);
        });

        buildBetMap.put("FT:BTS-No", (int a, int b, double line) -> {
            return !(a > 0 && b > 0);
        });

    }

    public void setScoreGridOne(double[][] scoreGridOne) {
        this.scoreGridOne = scoreGridOne;
    }

    public void setScoreGridOne(Map<String, Double> csGrid) {
        double[][] scoreGrid = new double[30][30];
        Double normTemp = 0.0;
        // normalising

        for (Map.Entry<String, Double> entry : csGrid.entrySet())
            normTemp += entry.getValue();
        for (Map.Entry<String, Double> entry : csGrid.entrySet()) {
            String scores = entry.getKey();
            String[] tempScores = scores.split("-");
            int scoreA = Integer.parseInt(tempScores[0]);
            int scoreB = Integer.parseInt(tempScores[1]);
            if (!(normTemp >= 0 && normTemp <= 0))
                scoreGrid[scoreA][scoreB] = entry.getValue() / normTemp;
        }
        this.scoreGridOne = scoreGrid;
    }

    public void init() {

    }

    private double generateProbForBuildABet(List<BuildBetSelectionHandlerWithLine> check) {
        double probs = 0;
        for (int home = 0; home < scoreGridOne.length; home++) {
            for (int away = 0; away < scoreGridOne.length; away++) {
                boolean add = true;
                for (BuildBetSelectionHandlerWithLine checking : check) {
                    add = add && (checking.getHandler().handleCorrectScore(home, away, checking.line));
                    System.out.println("local debug : " + add + " "
                                    + checking.getHandler().handleCorrectScore(home, away, checking.line) + " home "
                                    + home + " away " + away);
                }

                if (add)
                    probs += scoreGridOne[home][away];
            }
        }
        return probs;
    }


    public double generateBuildABetProbs(List<String> combineSelections) {
        List<BuildBetSelectionHandlerWithLine> check = new ArrayList<BuildBetSelectionHandlerWithLine>();

        for (String s : combineSelections) {
            String[] requirements = s.split("-");
            String key = requirements[0] + "-" + requirements[1];
            BuildBetSelectionHandler handler = null;
            double line = -1;
            if (requirements.length == 4)// includes lines
            {
                line = 0 - Double.parseDouble(requirements[3]);
                handler = this.buildBetMap.get(key);
            } else if (requirements.length == 3)// includes lines
            {
                line = Double.parseDouble(requirements[2]);
                handler = this.buildBetMap.get(key);
            } else if (requirements.length == 2)// not includes lines
            {
                handler = this.buildBetMap.get(key);
            }

            BuildBetSelectionHandlerWithLine handlerWithLine = new BuildBetSelectionHandlerWithLine(handler, line);
            check.add(handlerWithLine);
        }
        // System.out.println(check);
        double probs = generateProbForBuildABet(check);
        return probs;
    }


    public Map<String, BuildBetSelectionHandler> getBuildBetMap() {
        return buildBetMap;
    }

    public void setBuildBetMap(Map<String, BuildBetSelectionHandler> buildBetMap) {
        this.buildBetMap = buildBetMap;
    }

    public boolean isMarketValidForBuildABet(String market) {
        return buildBetMap.containsKey(market);
    }

}



class BuildBetSelectionHandlerWithLine {
    double line = -1;
    BuildBetSelectionHandler handler;

    public BuildBetSelectionHandlerWithLine(BuildBetSelectionHandler handler, double line) {
        this.line = line;
        this.handler = handler;
    }

    public double getLine() {
        return line;
    }

    public BuildBetSelectionHandler getHandler() {
        return handler;
    }
}

