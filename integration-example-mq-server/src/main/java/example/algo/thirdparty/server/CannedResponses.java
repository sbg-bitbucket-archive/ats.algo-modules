package example.algo.thirdparty.server;

import java.util.ArrayList;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.baseclasses.MatchEngineSavedState;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.comparetomarket.ParamFindResults;
import ats.algo.core.comparetomarket.ParamFindResultsDescription;
import ats.algo.core.comparetomarket.ParamFindResultsStatus;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketCategory;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.request.ParamFindResponse;
import ats.algo.core.request.PriceCalcResponse;
import ats.algo.sport.football.FootballMatchParams;
import ats.algo.sport.tennis.TennisMatchParams;

public class CannedResponses {
    public static PriceCalcResponse getCannedFootballPriceCalcResponse() {
        Markets markets = new Markets();
        Market market1 = new Market(MarketCategory.GENERAL, "FT:AXB", "M", "Match Winner");
        market1.setLineId(null);
        market1.setIsValid(true);
        market1.setMarketGroup(MarketGroup.GOALS);
        market1.setSequenceId("M");
        market1.put("A", 0.83);
        market1.put("B", 0.17);
        markets.addMarketWithFullKey(market1);
        Market market2 = new Market(MarketCategory.GENERAL, "FT:OUR", "M", "Total goal range");
        market2.setLineId(null);
        market2.setIsValid(true);
        market2.setMarketGroup(MarketGroup.GOALS);
        market2.setSequenceId("M");
        market2.put("0-1", 0.242);
        market2.put("2-3", 0.460);
        market2.put("4+", 0.298);
        markets.addMarketWithFullKey(market2);
        FootballMatchParams f = new FootballMatchParams();
        f.setGoalTotal(2.5, 0.1); // set to something non-default
        GenericMatchParams matchParams = f.generateGenericMatchParams();
        ResultedMarkets resultedMarkets = new ResultedMarkets();
        MatchEngineSavedState savedState = new MatchEngineSavedState();
        savedState.setSavedState("Your content goes here");
        PriceCalcResponse response = new PriceCalcResponse(null, markets, matchParams, savedState, resultedMarkets);
        return response;
    }


    public static ParamFindResponse getCannedFootballParamFindResponse() {
        ParamFindResults results = new ParamFindResults();
        results.addResultSummaryInfo(true, ParamFindResultsStatus.GREEN,
                        "This is the canned response summary result status");
        results.addResultDetailRow(false, "Bet365", "FT:AXB", "A", 1.0, 0.6, 0.55, 0.59);
        results.addResultDetailRow(false, "Pinnacle", "FT:AXB", "A", 2.0, 0.595, 0.55, 0.59);
        MatchParams matchParams = new FootballMatchParams();
        ParamFindResponse response = new ParamFindResponse(null, results, matchParams.generateGenericMatchParams());
        return response;
    }


    public static PriceCalcResponse getCannedTennisPriceCalcResponse() {
        Markets markets = new Markets();
        Market market1 = new Market(MarketCategory.GENERAL, "FT:ML", "M", "Match Winner");
        market1.setLineId(null);
        market1.setIsValid(true);
        market1.setSequenceId("M");
        market1.put("A", 0.512);
        market1.put("B", 0.488);
        markets.addMarketWithFullKey(market1);
        Market market2 = new Market(MarketCategory.GENERAL, "FT:TBIM", "M", "Tie break in match");
        market2.setLineId(null);
        market2.setIsValid(true);
        market2.setSequenceId("M");
        market2.put("No", 0.540);
        market2.put("Yes", 0.460);
        markets.addMarketWithFullKey(market2);
        GenericMatchParams matchParams = (new TennisMatchParams()).generateGenericMatchParams();
        ResultedMarkets resultedMarkets = new ResultedMarkets();
        MatchEngineSavedState savedState = new MatchEngineSavedState();
        savedState.setSavedState("Your content goes here");
        PriceCalcResponse response = new PriceCalcResponse(null, markets, matchParams, savedState, resultedMarkets);
        return response;
    }



    public static ParamFindResponse getCannedTennisParamFindResponse() {
        ParamFindResults results = new ParamFindResults();
        results.setFatalPfError(false);
        results.setFunctionValueAtMinimum(.03);
        results.setMinFunctionValueAchievable(.06);
        results.setnIterations(24);
        ParamFindResultsDescription resultsDescription = new ParamFindResultsDescription();
        resultsDescription.setResultSummary("This string will be displayed to the trader");
        ArrayList<String> resultDetail = new ArrayList<String>();

        resultsDescription.setResultDetail(resultDetail);
        results.setParamFindResultsDescription(resultsDescription);
        results.setParamFindResultsStatus(ParamFindResultsStatus.GREEN);
        results.setShouldSuspendMarkets(false);
        ParamFindResultsStatus summaryStatus = null;
        results.setParamFindResultsStatus(summaryStatus);

        results.setDetailedLog("Any detailed logging can go here");
        MatchParams matchParams = new TennisMatchParams();
        ParamFindResponse response = new ParamFindResponse(null, results, matchParams.generateGenericMatchParams());
        return response;
    }

}
