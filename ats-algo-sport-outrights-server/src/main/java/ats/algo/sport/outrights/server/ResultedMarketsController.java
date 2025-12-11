package ats.algo.sport.outrights.server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ats.algo.core.markets.ResultedMarket;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.sport.outrights.calcengine.core.Competition;
import ats.algo.sport.outrights.server.api.ResultedMarketListEntry;
import ats.algo.sport.outrights.server.api.ResultedMarketsList;
import ats.core.util.json.JsonUtil;

@Controller
public class ResultedMarketsController {

    @GetMapping("/resultedmarkets")
    @ResponseBody
    public String getMarkets(@RequestParam(name = "eventID", required = false,
                    defaultValue = OutrightsServer.DEFAULT_EVENT_ID) String eventIdStr) {
        long eventID = OutrightsServer.convertEventIdStr(eventIdStr);
        Competition competition = OutrightsServer.getFromEventId(eventID);
        if (competition == null)
            return null;
        OutrightsServer.outrights.calculate(competition);
        ResultedMarkets markets = competition.getNewResultedMarkets();
        ResultedMarketsList marketsList = new ResultedMarketsList(competition.getEventID());
        for (ResultedMarket market : markets) {
            marketsList.add(new ResultedMarketListEntry(market));
        }
        return JsonUtil.marshalJson(marketsList);
    }

    @GetMapping("/resultedmarket")
    @ResponseBody
    public String getMarket(
                    @RequestParam(name = "eventID", required = false,
                                    defaultValue = OutrightsServer.DEFAULT_EVENT_ID) String eventIdStr,
                    @RequestParam(name = "id", required = true) String id) {
        long eventID = OutrightsServer.convertEventIdStr(eventIdStr);
        Competition competition = OutrightsServer.getFromEventId(eventID);
        if (competition == null)
            return null;
        ResultedMarket market = competition.getNewResultedMarkets().get(id);
        return JsonUtil.marshalJson(market);
    }
}
