package ats.algo.algomanager;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.reflect.Constructor;
import java.util.EnumMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.matchengineframework.MatchEngine;
import ats.algo.springbridge.SpringContextBridge;
import ats.core.util.log.Logger;
import ats.core.util.log.LoggerFactory;

/**
 * 
 * @author Geoff
 * 
 */

public class SupportedSports {
    private static final Logger log = LoggerFactory.getLogger(SupportedSports.class);
    public static final EnumMap<SupportedSportType, SportProperties> sportsList;

    /**
     * private constructor to prevent class being instantiated
     */
    private SupportedSports() {

    }

    /**
     * Initialises sportsList. Need to have a call to the addSport method for every supported sport
     */
    static {
        EnumMap<SupportedSportType, SportProperties> s =
                        new EnumMap<SupportedSportType, SportProperties>(SupportedSportType.class);
        sportsList = s;
    }

    /**
     * adds a sport to the supported list
     * 
     * @param s
     * @param supportedSport
     * @param sportName
     * @param matchEngineClass
     */
    public static void addSport(SupportedSportType supportedSport, String sportName,
                    Class<? extends MatchEngine> matchEngineClass, Class<? extends TradingRules> tradingRulesSetClass) {
        sportsList.put(supportedSport,
                        new SportProperties(supportedSport, sportName, matchEngineClass, tradingRulesSetClass));
    }

    /**
     * gets the text string associated with this sport
     * 
     * @param supportedSport
     * @return
     */
    public static String getSportName(SupportedSportType supportedSport) {
        SportProperties algoAlgoSportDetails = sportsList.get(supportedSport);
        if (algoAlgoSportDetails == null)
            throw new IllegalArgumentException(
                            String.format("Sport %s is not listed in AlgoSportsList", supportedSport.toString()));
        return algoAlgoSportDetails.getSportName();
    }

    /**
     * gets a new instance of the MatchEngine associated with this sport and the supplied matchFormat. Throws an
     * exception if either can't find the sport or can't create the instance. May return a customer specific matchEngine
     * 
     * @param supportedSport
     * @param matchFormat
     * @return
     */
    public static MatchEngine getActiveMatchEngine(SupportedSportType supportedSport, MatchFormat matchFormat) {
        SportProperties sportProperties = sportsList.get(supportedSport);
        if (sportProperties == null) {
            throw new IllegalArgumentException(
                            String.format("Sport %s is not listed in AlgoSportsList", supportedSport.toString()));
        }
        String matchEngineBean = sportProperties.getActiveMatchEngineBeanName();
        MatchEngine matchEngine = null;
        if (matchEngineBean != null) {
            log.info("spring bean '%s' will used for %s", matchEngineBean, supportedSport);
            ApplicationContext applicationContext = SpringContextBridge.getApplicationContext();
            checkNotNull(applicationContext, "Spring application context is missing");
            // N.B. this bean should be "prototype" scope so a new instance beans
            matchEngine = (MatchEngine) applicationContext.getBean(matchEngineBean);
            if (matchEngine != null)
                matchEngine.setMatchFormat(matchFormat);
            checkNotNull(matchEngine, "Could not find bean named '%s'", matchEngineBean);
        } else {
            Class<? extends MatchEngine> matchEngineClass = sportProperties.activeMatchEngineClass();
            try {

                Constructor<?> constructor = matchEngineClass.getConstructor(MatchFormat.class);

                matchEngine = (MatchEngine) constructor.newInstance(matchFormat);

            } catch (Exception e) {
                log.error(String.format("Invalid constructor for %s match engine", supportedSport.toString()));
                log.error(e);
            }
        }
        return matchEngine;
    }

    /**
     * gets a new instance of the MatchEngine associated with this sport and the supplied matchFormat. Throws an
     * exception if either can't find the sport or can't create the instance. Always returns the Amelco (i.e. default)
     * matchEngine
     * 
     * @param supportedSport
     * @param matchFormat
     * @return
     */
    public static MatchEngine getAmelcoMatchEngine(SupportedSportType supportedSport, MatchFormat matchFormat) {
        SportProperties algoAlgoSportDetails = sportsList.get(supportedSport);
        if (algoAlgoSportDetails == null)
            throw new IllegalArgumentException(
                            String.format("Sport %s is not listed in AlgoSportsList", supportedSport.toString()));
        Class<? extends MatchEngine> matchEngineClass = algoAlgoSportDetails.amelcoMatchEngineClass();
        MatchEngine matchEngine = null;
        try {
            Constructor<?> constructor = matchEngineClass.getConstructor(MatchFormat.class);
            matchEngine = (MatchEngine) constructor.newInstance(matchFormat);
        } catch (Exception e) {
            log.error(String.format("Invalid constructor for %s match engine", supportedSport.toString()));
            log.error(e);
        }
        return matchEngine;
    }

    /**
     * gets the trading rules set associated with this sport. this will be the default set defined for this sport unless
     * overridden via a system property of the type "algo.<sport>.tradingrules.class" at the time SportProperties was
     * initialised
     * 
     * @param supportedSport
     * @return
     */
    public static TradingRules getTradingRulesSet(SupportedSportType supportedSport) {
        SportProperties sportProperties = sportsList.get(supportedSport);
        if (sportProperties == null)
            throw new IllegalArgumentException(
                            String.format("Sport %s is not listed in AlgoSportsList", supportedSport.toString()));
        Class<? extends TradingRules> tradingRuleSetClass = sportProperties.tradingRulesSetClass();
        if (tradingRuleSetClass == null)
            return null;
        TradingRules tradingRuleSet = null;
        try {
            Constructor<?> constructor = tradingRuleSetClass.getConstructor();
            tradingRuleSet = (TradingRules) constructor.newInstance();
        } catch (Exception e) {
            log.error(String.format("Invalid constructor for %s tradingRules", supportedSport.toString()));
            log.error(e);
        }
        return tradingRuleSet;
    }

    public static Class<? extends AbstractPriceCalculator> getPriceCalculatorClass(SupportedSportType supportedSport) {
        SportProperties sportProperties = sportsList.get(supportedSport);
        if (sportProperties == null)
            throw new IllegalArgumentException(
                            String.format("Sport %s is not listed in AlgoSportsList", supportedSport.toString()));
        return sportProperties.priceCalculatorClass();
    }

    public static Class<? extends AbstractParamFinder> getParamFinderClass(SupportedSportType supportedSport) {
        SportProperties sportProperties = sportsList.get(supportedSport);
        if (sportProperties == null)
            throw new IllegalArgumentException(
                            String.format("Sport %s is not listed in AlgoSportsList", supportedSport.toString()));
        return sportProperties.paramFinderClass();
    }

    public static int getEventStateHistorySize(SupportedSportType supportedSport) {
        return sportsList.get(supportedSport).getUndoEventStateHistorySize();
    }

    public static long getDelayBeforePublishingResultedMarkets(SupportedSportType supportedSport) {
        return ((long) sportsList.get(supportedSport).getDelayBeforePublishingResultedMarketsSecs()) * 1000;
    }

    public static boolean isUseClientResulting(SupportedSportType supportedSport) {
        return sportsList.get(supportedSport).isUseClientResulting();
    }

    public static boolean isUseClientParamFinder(SupportedSportType supportedSport) {
        return sportsList.get(supportedSport).isUseClientParamFinder();
    }

    public static boolean isUseClientTradingRules(SupportedSportType supportedSport) {
        return sportsList.get(supportedSport).isUseClientTradingRules();
    }

    public static boolean isUseExternalModel(SupportedSportType supportedSport) {
        return sportsList.get(supportedSport).isUseExternalModel();
    }


    public static void setUseClientResulting(SupportedSportType supportedSport, boolean b) {
        sportsList.get(supportedSport).setUseClientResulting(b);
    }

    public static void setUseClientParamFinder(SupportedSportType supportedSport, boolean b) {
        sportsList.get(supportedSport).setUseClientParamFinder(b);
    }

    public static void setUseClientTradingRules(SupportedSportType supportedSport, boolean b) {
        sportsList.get(supportedSport).setUseClientTradingRules(b);
    }

    public static void setUseExternalModel(SupportedSportType supportedSport, boolean b) {
        sportsList.get(supportedSport).setUseExternalModel(b);
    }

    public static void setDelayBeforePublishingResultedMarkets(SupportedSportType supportedSport, int secs) {
        sportsList.get(supportedSport).setDelayBeforePublishingResultedMarketsSecs(secs);
    }

    public static Map<String, String> getPropertiesAsMap(SupportedSportType supportedSport) {
        return sportsList.get(supportedSport).propertiesAsMap();
    }


}
