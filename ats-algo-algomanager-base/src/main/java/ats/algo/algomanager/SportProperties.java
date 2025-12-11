package ats.algo.algomanager;

import java.util.HashMap;
import java.util.Map;

import ats.algo.core.common.SupportedSportType;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.matchengineframework.MatchEngine;
import ats.core.AtsBean;

/**
 * Holds the details associated with each sport
 * 
 * @author Geoff
 *
 */

public class SportProperties extends AtsBean {

    private static final String MATCH_ENGINE_BEAN_PREFIX = "bean:";
    private String sportName;
    private Class<? extends MatchEngine> activeMatchEngineClass;
    private Class<? extends MatchEngine> amelcoMatchEngineClass;
    private Class<? extends TradingRules> tradingRulesClass;
    private Class<? extends AbstractPriceCalculator> priceCalculatorClass;
    private Class<? extends AbstractParamFinder> paramFinderClass;
    private String activeMatchEngineBeanName;
    private int undoEventStateHistorySize;
    private int delayBeforePublishingResultedMarketsSecs;
    private boolean useExternalModel;
    private boolean useClientParamFinder;
    private boolean useClientResulting;
    private boolean useClientTradingRules;

    /**
     * 
     * @param sportId the enum for this sport e.g. "SupportedSport.TENNIS"
     * @param sportName Text string, e.g. "Tennis"
     * @param matchEngineClass the class for the match engine e.g. "TennisMatchEngine.class"
     */
    @SuppressWarnings("unchecked")
    public SportProperties(SupportedSportType sportId, String sportName, Class<? extends MatchEngine> matchEngineClass,
                    Class<? extends TradingRules> tradingRulesClass) {
        this.sportName = sportName;
        this.amelcoMatchEngineClass = matchEngineClass;
        this.tradingRulesClass = tradingRulesClass;
        this.priceCalculatorClass = (Class<? extends AbstractPriceCalculator>) this.getClassFromSystemProperty(sportId,
                        "priceCalculator.class", null);
        this.paramFinderClass = (Class<? extends AbstractParamFinder>) this.getClassFromSystemProperty(sportId,
                        "paramFinder.class", null);

        /*
         * matchEngineProperty is e.g. "algo.tennis.engine.class"
         */
        String sportStr = sportId.toString().toLowerCase();
        String matchEngineProperty = "algo." + sportStr + ".engine.class";
        String matchEngineClassName = System.getProperty(matchEngineProperty);
        if (matchEngineClassName == null) {
            this.activeMatchEngineClass = matchEngineClass;
        } else {
            try {
                if (matchEngineClassName.startsWith(MATCH_ENGINE_BEAN_PREFIX)) {
                    /*
                     * The external model has been configured via spring so need to look up the relevant bean from the
                     * context. This is done lazily as the constructor will be executed before spring finishes
                     * initialising. N.B. the bean must be prototype scope so that a new instance is created each time
                     * it is retrieved from the spring application context
                     */
                    activeMatchEngineBeanName = matchEngineClassName.replace(MATCH_ENGINE_BEAN_PREFIX, "");
                } else {
                    this.activeMatchEngineClass = ((Class<? extends MatchEngine>) Class.forName(matchEngineClassName));
                    info("Match engine class %s overriden by class %s (obtained via System.getProperty())",
                                    matchEngineClass.toString(), matchEngineClassName);
                }
            } catch (ClassNotFoundException e) {
                this.activeMatchEngineClass = matchEngineClass;
                error(String.format("Class %s not found or does not extend MatchEngine", matchEngineClassName));
            }
        }
        String tradingRulesProperty = "algo." + sportStr + ".tradingrules.class";
        String tradingRulesClassName = System.getProperty(tradingRulesProperty);
        if (tradingRulesClassName == null) {
            this.tradingRulesClass = tradingRulesClass;
        } else {
            try {
                this.tradingRulesClass = (Class<? extends TradingRules>) Class.forName(tradingRulesClassName);
                info("Tradingrules class overriden by class %s (obtained via System.getProperty())",
                                tradingRulesClassName);
            } catch (ClassNotFoundException e) {
                this.tradingRulesClass = tradingRulesClass;
                error(String.format("Class %s not found or does not extend TradingRules", tradingRulesClassName));
            }
        }
        String trRulesStr = "null";
        if (this.tradingRulesClass != null)
            trRulesStr = this.tradingRulesClass.getName();
        String matchEngineStr;
        if (this.activeMatchEngineClass != null)
            matchEngineStr = activeMatchEngineClass.getName();
        else
            matchEngineStr = matchEngineClassName;
        info("%s using matchEngine: %s and tradingrules: %s", this.sportName, matchEngineStr, trRulesStr);

        String undoEventStateHistorySizeProperty = "algo." + sportStr + ".undoEventStateHistorySize";

        if (System.getProperty(undoEventStateHistorySizeProperty) == null) {
            this.undoEventStateHistorySize = 5;
        } else {
            this.undoEventStateHistorySize = Integer.parseInt(System.getProperty(undoEventStateHistorySizeProperty));
            if (this.undoEventStateHistorySize > 8 || this.undoEventStateHistorySize < 1) {
                this.undoEventStateHistorySize = 5;
            }
        }

        String delayBeforePublishingResultedMarketsSecsProperty =
                        "algo." + sportStr + ".delayBeforePublishingResultedMarketsSecs";

        if (System.getProperty(delayBeforePublishingResultedMarketsSecsProperty) == null) {
            this.delayBeforePublishingResultedMarketsSecs = 240;
        } else {
            this.delayBeforePublishingResultedMarketsSecs =
                            Integer.parseInt(System.getProperty(delayBeforePublishingResultedMarketsSecsProperty));

            if (this.delayBeforePublishingResultedMarketsSecs > 2400
                            || this.delayBeforePublishingResultedMarketsSecs < 30) {
                this.delayBeforePublishingResultedMarketsSecs = 240;
            }
        }

        String externalModelProperty = System.getProperty("algo." + sportStr + ".externalModel");
        if (externalModelProperty != null)
            useExternalModel = externalModelProperty.equals("true");
        else
            useExternalModel = false;
        String clientResultingKey = "algo." + sportStr + ".clientResulting";
        String clientResultingProperty = System.getProperty(clientResultingKey);
        if (clientResultingProperty != null)
            useClientResulting = clientResultingProperty.equals("true");
        else
            useClientResulting = false;
        if (useClientResulting)
            info("%s = %b", clientResultingKey, useClientResulting);

        String clientTradingRulesKey = "algo." + sportStr + ".clientTradingRules";

        String clientTradingRulesProperty = System.getProperty(clientTradingRulesKey);
        if (clientTradingRulesProperty != null)
            useClientTradingRules = clientTradingRulesProperty.equals("true");
        else
            useClientTradingRules = false;
        if (useClientTradingRules)
            info("%s = %b", clientTradingRulesKey, useClientTradingRules);

        String clientParamFinderKey = "algo." + sportStr + ".clientParamFinding";
        String clientParamFinderProperty = System.getProperty(clientParamFinderKey);
        if (clientParamFinderProperty != null)
            useClientParamFinder = clientParamFinderProperty.equals("true");
        else
            useClientParamFinder = false;
        if (useClientParamFinder)
            info("%s = %b", clientParamFinderKey, useClientParamFinder);
    }

    private Class<?> getClassFromSystemProperty(SupportedSportType sport, String property, Class<?> defaultClass) {
        Class<?> clazz = defaultClass;
        String propertyName = "algo." + sport.toString().toLowerCase() + "." + property;
        String clazzName = System.getProperty(propertyName);
        if (clazzName != null) {
            try {
                clazz = (Class<?>) Class.forName(clazzName);
                info("%s.  Default class %s overriden by  %s (obtained via System.getProperty())", propertyName,
                                defaultClass, clazz);
            } catch (ClassNotFoundException e) {
                error(String.format("Class %s intended to override %s not found", clazzName, defaultClass));
            }
        }
        return clazz;
    }

    public Map<String, String> propertiesAsMap() {
        Map<String, String> map = new HashMap<>(13);
        map.put("sportName", sportName);
        map.put("activeMatchEngineClass", getClassName(activeMatchEngineClass));
        map.put("amelcoMatchEngineClass", getClassName(amelcoMatchEngineClass));
        map.put("tradingRulesClass", getClassName(tradingRulesClass));
        map.put("priceCalculatorClass", getClassName(priceCalculatorClass));
        map.put("paramFinderClass", getClassName(paramFinderClass));
        map.put("activeMatchEngineBeanName", activeMatchEngineBeanName);
        map.put("undoEventStateHistorySize", String.valueOf(undoEventStateHistorySize));
        map.put("delayBeforePublishingResultedMarketsSecs", String.valueOf(delayBeforePublishingResultedMarketsSecs));
        map.put("useExternalModel", String.valueOf(useExternalModel));
        map.put("useClientParamFinder", String.valueOf(useClientParamFinder));
        map.put("useClientResulting", String.valueOf(useClientResulting));
        map.put("useClientTradingRules", String.valueOf(useClientTradingRules));
        return map;
    }

    private String getClassName(Class<?> clazz) {
        if (clazz == null)
            return "not set - will use default";
        else
            return clazz.getName();
    }


    /**
     * gets the matchEngineClass to use - may be a customer specific one that has overriden ours
     * 
     * @return
     */

    public Class<? extends MatchEngine> activeMatchEngineClass() {
        return activeMatchEngineClass;
    }


    public Class<? extends MatchEngine> amelcoMatchEngineClass() {
        return amelcoMatchEngineClass;
    }

    public Class<? extends TradingRules> tradingRulesSetClass() {
        return tradingRulesClass;
    }


    public Class<? extends AbstractPriceCalculator> priceCalculatorClass() {
        return priceCalculatorClass;
    }

    public Class<? extends AbstractParamFinder> paramFinderClass() {
        return paramFinderClass;
    }



    public String getSportName() {
        return sportName;
    }

    public String getActiveMatchEngineBeanName() {
        return activeMatchEngineBeanName;
    }

    public int getUndoEventStateHistorySize() {
        return undoEventStateHistorySize;
    }

    public int getDelayBeforePublishingResultedMarketsSecs() {
        return delayBeforePublishingResultedMarketsSecs;
    }

    public void setDelayBeforePublishingResultedMarketsSecs(int delayBeforePublishingResultedMarketsSecs) {
        this.delayBeforePublishingResultedMarketsSecs = delayBeforePublishingResultedMarketsSecs;
    }

    public boolean isUseExternalModel() {
        return useExternalModel;
    }

    public void setUseExternalModel(boolean useExternalModel) {
        this.useExternalModel = useExternalModel;
    }

    public boolean isUseClientParamFinder() {
        return useClientParamFinder;
    }

    public void setUseClientParamFinder(boolean useClientParamFinder) {
        this.useClientParamFinder = useClientParamFinder;
    }

    public boolean isUseClientResulting() {
        return useClientResulting;
    }

    public void setUseClientResulting(boolean useClientResulting) {
        this.useClientResulting = useClientResulting;
    }

    public boolean isUseClientTradingRules() {
        return useClientTradingRules;
    }

    public void setUseClientTradingRules(boolean useClientTradingRules) {
        this.useClientTradingRules = useClientTradingRules;
    }

}
