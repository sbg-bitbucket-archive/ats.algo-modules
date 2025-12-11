package ats.algo.sport.outrights.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ats.algo.sport.outrights.calcengine.core.Outrights;
import ats.algo.sport.outrights.calcengine.core.Competition;
import ats.algo.sport.outrights.calcengine.core.Competitions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class OutrightsServer {

    private static final Logger logger = LoggerFactory.getLogger(OutrightsServer.class);

    static Outrights outrights;

    public static void main(String[] args) {

        logger.info("Outrights starting");
        outrights = new Outrights();
        outrights.loadPersistedData();
        // outrights.calculateAll();
        /*
         * start the http handler
         */
        SpringApplication.run(OutrightsServer.class, args);
        logger.info("http server initialised");
        /*
         * start the MQ handler
         */
        PriceCalcRequestHandler pcrHandler = new PriceCalcRequestHandler(outrights);
        pcrHandler.listenAndServe();

        logger.info("Outrights initialised");
    }

    static long convertEventIdStr(String eventIdStr) {
        long eventID = 0;
        try {
            eventID = Long.parseLong(eventIdStr);
        } catch (NumberFormatException e) {
            // DO nothing
        }
        return eventID;
    }

    static Competition getFromEventId(long eventId) {
        Competition competition = OutrightsServer.outrights.getCompetitions().get(eventId);
        return competition;
    }

    static Competitions getCompetitions() {
        return OutrightsServer.outrights.getCompetitions();
    }

}
