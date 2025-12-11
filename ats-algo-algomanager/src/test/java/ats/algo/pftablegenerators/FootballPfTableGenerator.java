package ats.algo.pftablegenerators;

import ats.algo.algomanager.AlgoManagerSimpleTestBase;
import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.baseclasses.MatchParam;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.Markets;
import ats.algo.sport.football.FootballMatchFormat;
import ats.core.util.log.Level;
import ats.core.util.log.LogUtil;

public class FootballPfTableGenerator extends AlgoManagerSimpleTestBase {


    public FootballPfTableGenerator() {
        super.beforeEachTest();
    }

    public static void main(String[] args) {
        generateTable("goalTotal", "goalSupremacy", "FT:OU", "FT:SPRD", 1);
        generateTable("cornerTotal", "cornerSupremacy", "FT:COU", "FT:CSPRD", 5);
        System.out.println("Finished");
        System.exit(0);
    }

    private static void generateTable(String totalParamName, String supremacyParamName, String ouMktType,
                    String sprdMktType, int interval) {
        FootballPfTableGenerator generator = new FootballPfTableGenerator();
        LogUtil.initConsoleLogging(Level.ERROR);
        generator.algoManager.handleNewEventCreation(SupportedSportType.SOCCER, 123L, new FootballMatchFormat());
        GenericMatchParams params = generator.publishedMatchParams.generateGenericMatchParams();
        MatchParam totalParam = params.getParamMap().get(totalParamName);
        MatchParam supremacyParam = params.getParamMap().get(supremacyParamName);
        double delta = 1.0;
        for (double total = totalParam.getMinAllowedParamValue() + delta; total <= totalParam.getMaxAllowedParamValue()
                        - delta; total += interval) {
            double minSupremacy = Math.max(-total, supremacyParam.getMinAllowedParamValue()) + delta;
            double maxSupremacy = Math.min(total, supremacyParam.getMaxAllowedParamValue()) - delta;
            for (double supremacy = minSupremacy; supremacy <= maxSupremacy; supremacy += interval) {
                totalParam.getGaussian().setMean(total);
                supremacyParam.getGaussian().setMean(supremacy);
                generator.algoManager.handleSetMatchParams(params);
                String s = extractProbs(generator.publishedMarkets, ouMktType, sprdMktType);
                System.out.printf("totalParam: %.3f, supremacyParam: %.3f, %s\n", total, supremacy, s);
            }
        }
    }

    private static String extractProbs(Markets markets, String ouMktType, String sprdMktType) {
        Market ou = markets.getMarketForBalancedLine(ouMktType, "M");
        Market sprd = markets.getMarketForBalancedLine(sprdMktType, "M");
        return String.format(" %s lineId: %s, probOver: %.3f, %s lineId: %s, probA: %.3f", ouMktType, ou.getLineId(),
                        ou.getSelectionsProbs().get("Over"), sprdMktType, sprd.getLineId(),
                        sprd.getSelectionsProbs().get("AH"));
    }

}
