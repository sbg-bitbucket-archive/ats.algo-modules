package ats.algo.sport.outrights.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ats.algo.sport.outrights.calcengine.core.Competition;
import ats.algo.sport.outrights.calcengine.core.Competitions;
import ats.algo.sport.outrights.calcengine.core.Outrights;
import ats.algo.sport.outrights.server.api.Alerts;

@SpringBootApplication
public class OutrightsServer implements CommandLineRunner {

    static final Logger logger = LoggerFactory.getLogger(OutrightsServer.class);

    static Outrights outrights;

    static OutrightsConfiguration outrightsConfiguration;

    static final String DEFAULT_EVENT_ID = "5537098"; // PremierLeague1819

    public static void main(String... args) {
        SpringApplication.run(OutrightsServer.class, args);
    }

    @Autowired
    public OutrightsServer(OutrightsConfiguration configuration) {
        outrightsConfiguration = configuration;
        logger.info("Outrights starting");
        outrights = new Outrights(outrightsConfiguration.getEnv());
        logger.info("Setting persisted data folder = " + outrightsConfiguration.getPersistedDataFolder());
        outrights.setPersistedDataFolder(outrightsConfiguration.getPersistedDataFolder());
        if (outrightsConfiguration.isLoadPersistedData())
            outrights.loadPersistedData();
        else
            logger.info("loadPersistedData set to false.  Competitions data file not loaded");
        outrights.updateDerivedData();

        logger.info("http server initialised");

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

    static OutrightsConfiguration getOutrightsConfiguration() {
        return outrightsConfiguration;
    }

    @Override
    public void run(String... strings) throws Exception {
        /*
         * start the MQ handler
         */
        logger.info("Configurations from properties are = " + outrightsConfiguration);
        String brokerUrl = outrightsConfiguration.getBrokerUrl();
        PriceCalcRequestHandler pcrHandler = new PriceCalcRequestHandler(outrights, brokerUrl);
        pcrHandler.listenAndServe();
        logger.info("Outrights initialised");
    }

    public static Alerts getAlerts() {
        return outrights.getAlerts();
    }

}
