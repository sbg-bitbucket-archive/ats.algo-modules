package ats.algo.springbridge;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.reflect.Constructor;

import org.springframework.context.ApplicationContext;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.matchengineframework.MatchEngine;
import ats.algo.matchengineframework.SpringMatchEngine;
import ats.core.util.log.Logger;
import ats.core.util.log.LoggerFactory;

public class GetInstancesFromProperties {

    private static final Logger log = LoggerFactory.getLogger(GetInstancesFromProperties.class);

    private static final String MATCH_ENGINE_BEAN_PREFIX = "bean:";

    @SuppressWarnings("unchecked")
    public static MatchEngine getMatchEngine(Class<? extends MatchEngine> defaultMatchEngineClass,
                    SupportedSportType supportedSportType, MatchFormat matchFormat) {
        Class<? extends MatchEngine> matchEngineClass = defaultMatchEngineClass;
        String sport = supportedSportType.toString().toLowerCase();
        String matchEngineProperty = "algo." + sport + ".engine.class";
        String className = System.getProperty(matchEngineProperty);
        String matchEngineBeanName = null;
        if (className != null) {
            try {
                if (className.startsWith(MATCH_ENGINE_BEAN_PREFIX)) {
                    /*
                     * The external model has been configured via spring so need to look up the relevant bean from the
                     * context. This is done lazily as the constructor will be executed before spring finishes
                     * initialising. N.B. the bean must be prototype scope so that a new instance is created each time
                     * it is retrieved from the spring application context
                     */
                    matchEngineBeanName = className.replace(MATCH_ENGINE_BEAN_PREFIX, "");
                } else {
                    matchEngineClass = (Class<? extends MatchEngine>) Class.forName(className);
                    log.info("Match engine class %s overriden by class %s (obtained via System.getProperty())\n",
                                    matchEngineClass.toString(), className);
                }
            } catch (ClassNotFoundException e) {
                matchEngineClass = defaultMatchEngineClass;
                log.info("Class %s not found or does not extend MatchEngine\n", className);
            }
        }

        MatchEngine matchEngine = null;
        if (matchEngineBeanName != null) {
            log.info("spring bean '%s' will used for %s\n", matchEngineBeanName, supportedSportType);
            ApplicationContext applicationContext = SpringContextBridge.getApplicationContext();
            checkNotNull(applicationContext, "Spring application context is missing");
            // N.B. this bean should be "prototype" scope so a new instance
            // beans
            matchEngine = (MatchEngine) applicationContext.getBean(matchEngineBeanName);
            checkNotNull(matchEngine, "Could not find bean named '%s'", matchEngineBeanName);
            try {
                /*
                 * if MatchEngine extends SpringMatchEngine then need to set the matchFormat explicitly
                 */
                SpringMatchEngine springMatchEngine = (SpringMatchEngine) matchEngine;
                if (springMatchEngine != null)
                    springMatchEngine.setMatchFormat(matchFormat);
            } catch (Exception e) {
                /*
                 * do nothing if not extended from SpringMatchEngine
                 */
            }
        } else {

            try {
                Constructor<?> constructor = matchEngineClass.getConstructor(MatchFormat.class);
                matchEngine = (MatchEngine) constructor.newInstance(matchFormat);
            } catch (Exception e) {
                log.error("Invalid constructor for %s match engine\n", supportedSportType);
                log.error(e);
            }
        }
        return matchEngine;
    }

    @SuppressWarnings("unchecked")
    public static TradingRules getTradingRules(Class<? extends TradingRules> defaultTradingRulesClass,
                    SupportedSportType supportedSportType) {
        Class<? extends TradingRules> tradingRulesClass = defaultTradingRulesClass;
        String sport = supportedSportType.toString().toLowerCase();
        String tradingRulesProperty = "algo." + sport + ".tradingrules.class";
        String className = System.getProperty(tradingRulesProperty);
        if (className != null) {
            try {

                tradingRulesClass = (Class<? extends TradingRules>) Class.forName(className);
                log.info("trading rules class %s overriden by class %s (obtained via System.getProperty())\n",
                                tradingRulesClass.toString(), className);

            } catch (ClassNotFoundException e) {
                tradingRulesClass = defaultTradingRulesClass;
                log.info("Class %s not found or does not extend TradingRules\n", className);
            }
        }

        TradingRules tradingRules = null;
        try {
            Constructor<?> constructor = tradingRulesClass.getConstructor();
            tradingRules = (TradingRules) constructor.newInstance();
        } catch (Exception e) {
            log.error("Invalid constructor for %s match engine\n", supportedSportType);
            log.error(e);
        }

        return tradingRules;
    }

}
