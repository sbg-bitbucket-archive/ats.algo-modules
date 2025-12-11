package ats.algo.algomanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ats.algo.core.common.SupportedSportType;
import ats.algo.integration.AlgoMgrBetsyncCoordinator;
import ats.core.util.log.Logger;
import ats.core.util.log.LoggerFactory;

/**
 * Controller to expose HTTP APIs which will be used to query and possible manage aspects of the Algo MGR in production
 */
@Controller
public class AlgoMgrController {
    private Logger log = LoggerFactory.getLogger(getClass());

    private AlgoMgrBetsyncCoordinator algoMgrBetsyncCoordinator;

    @Autowired
    public void setAlgoMgrBetsyncCoordinator(AlgoMgrBetsyncCoordinator algoMgrBetsyncCoordinator) {
        this.algoMgrBetsyncCoordinator = algoMgrBetsyncCoordinator;
    }

    @RequestMapping(value = "/stats/detailed", method = RequestMethod.GET)
    public @ResponseBody String getAlgoDetailedStatistics() {
        AlgoManagerStatistics algoStatistics = getAlgoStats();
        return algoStatistics.toString();
    }

    @RequestMapping(value = "/stats/summary", method = RequestMethod.GET)
    public @ResponseBody String getAlgoSummaryStatistics() {
        AlgoManagerStatistics algoStatistics = getAlgoStats();
        return algoStatistics.getSummaryStats().toString();
    }

    @RequestMapping(value = "/stats/event/{eventId}", method = RequestMethod.GET)
    public @ResponseBody String getAlgoEventStatistics(@PathVariable Long eventId) {
        AlgoManagerStatistics algoStatistics = getAlgoStats();
        return algoStatistics.getEventStats(eventId).toString();
    }

    @RequestMapping(value = "/stats/sport/{sportCode}", method = RequestMethod.GET)
    public @ResponseBody String getAlgoSportStatistics(@PathVariable String sportCode) {
        AlgoManagerStatistics algoStatistics = getAlgoStats();
        return algoStatistics.getSportStats(SupportedSportType.valueOf(sportCode.toUpperCase())).toString();
    }

    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public @ResponseBody String sayHi() {
        log.info("in ping");
        return "pong";
    }

    private AlgoManagerStatistics getAlgoStats() {
        AlgoManagerStatistics algoStatistics = algoMgrBetsyncCoordinator.getAlgoStatistics();
        return algoStatistics;
    }
}
