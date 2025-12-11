package ats.algo.sport.outrights.server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ats.algo.core.markets.Market;
import ats.algo.core.markets.Markets;
import ats.algo.sport.outrights.calcengine.core.Competition;
import ats.algo.sport.outrights.server.api.MarketListEntry;
import ats.algo.sport.outrights.server.api.MarketsList;
import ats.core.util.json.JsonUtil;

@Controller
public class MarketsController {


    @GetMapping("/markets")
    @ResponseBody
    public String getMarkets(@RequestParam(name = "eventID", required = false,
                    defaultValue = OutrightsServer.DEFAULT_EVENT_ID) String eventIdStr) {
        long eventID = OutrightsServer.convertEventIdStr(eventIdStr);
        Competition competition = OutrightsServer.getFromEventId(eventID);
        if (competition == null)
            return null;
        OutrightsServer.outrights.calculate(competition);
        Markets markets = competition.getMarkets();
        MarketsList marketsList = new MarketsList(competition.getEventID());
        for (Market market : markets) {
            marketsList.add(new MarketListEntry(market));
        }
        return JsonUtil.marshalJson(marketsList);
    }

    @GetMapping("/market")
    @ResponseBody
    public String getMarket(
                    @RequestParam(name = "eventID", required = false,
                                    defaultValue = OutrightsServer.DEFAULT_EVENT_ID) String eventIdStr,
                    @RequestParam(name = "id", required = true) String id) {
        long eventID = OutrightsServer.convertEventIdStr(eventIdStr);
        Competition competition = OutrightsServer.getFromEventId(eventID);
        if (competition == null)
            return null;
        Market market = competition.getMarkets().getMarketForFullKey(id);
        return JsonUtil.marshalJson(market);
    }
}
