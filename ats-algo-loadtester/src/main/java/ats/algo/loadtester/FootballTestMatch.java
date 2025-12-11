package ats.algo.loadtester;

import ats.algo.core.baseclasses.AlgoMatchParams;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.comparetomarket.MarketPrice;
import ats.algo.core.comparetomarket.MarketPrices;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.core.markets.MarketCategory;
import ats.algo.sport.football.FootballMatchFormat;
import ats.algo.sport.football.FootballMatchParams;

public class FootballTestMatch extends TestMatch {

    FootballMatchFormat matchFormat;
    FootballMatchParams matchParams1;
    FootballMatchParams matchParams2;
    MarketPricesList marketPricesList1;
    MarketPricesList marketPricesList2;
    boolean lastOutputWas1;
    int nParamFinds;
    int count;

    public FootballTestMatch(long eventId) {
        super(eventId, "FootballMatch");
        matchFormat = new FootballMatchFormat();
        matchParams1 = new FootballMatchParams(matchFormat);
        matchParams2 = new FootballMatchParams(matchFormat);
        matchParams1.setEventId(eventId);
        matchParams2.setEventId(eventId);
        // System.out.print(matchParams1.toString());
        matchParams2.setGoalTotal(1.2, 0.05);
        matchParams2.setGoalSupremacy(0, 0.05);
        /*
         * initialise marketPricesLists
         */
        marketPricesList1 = new MarketPricesList();
        marketPricesList1.put("Source1", this.getTestMarketPrices1());
        marketPricesList2 = new MarketPricesList();
        marketPricesList2.put("Source1", this.getTestMarketPrices2());
        lastOutputWas1 = false;
        nParamFinds = 3;
        count = 0;
    }

    @Override
    MatchIncident getNextIncident() {
        noIncidentsGenerated++;
        return null;
    }

    @Override
    SupportedSportType getSupportedSport() {
        return SupportedSportType.SOCCER;
    }

    @Override
    MatchFormat getMatchFormat() {
        return matchFormat;
    }

    @Override
    AlgoMatchParams getNextMatchParams() {
        noIncidentsGenerated++;
        AlgoMatchParams matchParams;
        if (lastOutputWas1)
            matchParams = matchParams2;
        else
            matchParams = matchParams1;
        count++;
        lastOutputWas1 = !lastOutputWas1;
        if (count == 50)
            this.setMatchCompleted(true);

        return matchParams;
    }

    @Override
    MarketPricesList getNextMarketPricesList() {
        noIncidentsGenerated++;
        MarketPricesList marketPricesList;;
        if (lastOutputWas1)
            marketPricesList = marketPricesList2;
        else
            marketPricesList = marketPricesList1;
        count++;
        lastOutputWas1 = !lastOutputWas1;
        if (count == nParamFinds)
            this.setMatchCompleted(true);
        return marketPricesList;
    }

    private MarketPrices getTestMarketPrices1() {
        MarketPrices m = new MarketPrices();
        m.setSourceWeight(1);

        MarketPrice ab = new MarketPrice("FT:AXB", "Match winner", MarketCategory.GENERAL, null, "M");
        ab.put("A", 2.11);
        ab.put("B", 3.14);
        ab.put("Draw", 3.2);

        m.addMarketPrice(ab);

        MarketPrice tmtg = new MarketPrice("FT:OU", "Total goals", MarketCategory.OVUN, "2.5", "M");
        tmtg.put("Over", 1.78);
        tmtg.put("Under", 1.97);
        m.addMarketPrice(tmtg);
        return m;
    }

    private MarketPrices getTestMarketPrices2() {
        MarketPrices m = new MarketPrices();
        m.setSourceWeight(1);

        MarketPrice ab = new MarketPrice("FT:AXB", "Match winner", MarketCategory.GENERAL, null, "M");
        ab.put("A", 2.39);
        ab.put("B", 2.81);
        ab.put("Draw", 3.03);
        m.addMarketPrice(ab);

        MarketPrice tmtg = new MarketPrice("FT:OU", "Total goals", MarketCategory.OVUN, "2.5", "M");
        tmtg.put("Over", 1.95);
        tmtg.put("Under", 1.8);
        m.addMarketPrice(tmtg);
        return m;
    }

    @Override
    TestMatch newInstance(long eventId) {
        return new FootballTestMatch(eventId);
    }



}
