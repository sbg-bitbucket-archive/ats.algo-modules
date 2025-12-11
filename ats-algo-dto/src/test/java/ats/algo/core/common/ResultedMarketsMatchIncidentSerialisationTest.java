package ats.algo.core.common;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.markets.ResultedMarket;
import ats.algo.core.markets.ResultedMarkets;
import ats.core.util.json.JsonUtil;

public class ResultedMarketsMatchIncidentSerialisationTest {

    @Test
    public void test() {
        MatchIncident incident = generateExampleResultedMarketsIncident();
        String json1 = JsonUtil.marshalJson(incident, true);
        System.out.println(json1);
        /*
         * polymorphic classes have to be wrapped in some kind of other object for deserialisation to work. Seems to be
         * a bug in Jackson. Not a problem in practice since Match Incidents always sent within some larger object, e.g.
         * PriceCalcRequest
         */
        IncidentWrapperForTest wrapper = new IncidentWrapperForTest();
        wrapper.setMatchIncident(incident);
        String json = JsonUtil.marshalJson(wrapper, true);
        System.out.println(json);
        IncidentWrapperForTest wrapper2 = JsonUtil.unmarshalJson(json, IncidentWrapperForTest.class);
        assertEquals(wrapper, wrapper2);
    }


    private MatchIncident generateExampleResultedMarketsIncident() {
        ResultedMarkets resultedMarkets = new ResultedMarkets();
        MatchIncident incident = new ResultedMarketsMatchIncident(resultedMarkets);
        ResultedMarket resultedMarket = new ResultedMarket();
        resultedMarket.setType("FT:AGSC");
        resultedMarket.setMarketDescription("Anytime goal scorer");
        resultedMarkets.addMarket(resultedMarket);
        List<String> winningSelections = new ArrayList<>();
        List<String> losingSelections = new ArrayList<>();
        List<String> stakeBackSelections = new ArrayList<>();
        resultedMarket.setWinningSelections(winningSelections);
        resultedMarket.setLosingSelections(losingSelections);
        resultedMarket.setStakeBack(stakeBackSelections);
        losingSelections.add("A.Adam Lallana");
        losingSelections.add("A.Andrew Robertson");
        losingSelections.add("A.Dejan Lovren");
        losingSelections.add("A.Emre Can");
        losingSelections.add("A.Georginio Wijnaldum");
        losingSelections.add("A.James Milner");
        losingSelections.add("A.Joel Matip");
        losingSelections.add("A.Joseph Gomez");
        losingSelections.add("A.Ragnar Klavan");
        losingSelections.add("B.Danilo");
        losingSelections.add("B.Fabian Delph");
        losingSelections.add("B.Fernandinho");
        losingSelections.add("B.John Stones");
        losingSelections.add("B.Kevin De Bruyne");
        losingSelections.add("B.Kyle Walker");
        losingSelections.add("B.Nicolás Otamendi");
        losingSelections.add("B.Raheem Sterling");
        losingSelections.add("B.Sergio Agüero");
        stakeBackSelections.add("A.Alberto Moreno");
        stakeBackSelections.add("A.Daniel Sturridge");
        stakeBackSelections.add("A.Danny Ings");
        stakeBackSelections.add("A.Dominic Solanke");
        stakeBackSelections.add("A.Jordan Henderson");
        stakeBackSelections.add("A.Marko Grujic");
        stakeBackSelections.add("A.Trent Alexander-Arnold");
        stakeBackSelections.add("A.Virgil van Dijk");
        stakeBackSelections.add("B.Brahim Diaz");
        stakeBackSelections.add("B.David Silva");
        stakeBackSelections.add("B.Eliaquim Mangala");
        stakeBackSelections.add("B.Oleksandr Zinchenko");
        stakeBackSelections.add("B.Tosin Adarabioyo");
        stakeBackSelections.add("B.Vincent Kompany");
        stakeBackSelections.add("B.Yaya Touré");
        winningSelections.add("A.Alex Oxlade-Chamberlain");
        winningSelections.add("A.Mohamed Salah");
        winningSelections.add("A.Roberto Firmino");
        winningSelections.add("A.Sadio Mané");
        winningSelections.add("B.Bernardo Silva");
        winningSelections.add("B.Ilkay Gündogan");
        winningSelections.add("B.Leroy Sané");
        incident.setIncidentId("32975312.1");
        incident.setEventId(569321);
        incident.setSourceSystem("ABELSON");
        return incident;
    }

}
