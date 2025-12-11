package ats.algo.core.baseclasses;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import ats.algo.core.GamePeriod;
import ats.algo.core.common.DatafeedMatchIncident.DatafeedMatchIncidentType;
import ats.algo.core.common.ElapsedTimeMatchIncident.ElapsedTimeMatchIncidentType;
import ats.algo.core.markets.DataToGenerateStaticMarkets;
import ats.algo.core.matchresult.MatchResultMap;
import ats.algo.genericsupportfunctions.JsonSimpleFormatter;
import ats.core.util.json.JsonUtil;
import ats.org.json.JSONException;
import ats.org.json.JSONObject;

/**
 * container for holding a simplified representation of matchState suitable for serialising to Json and sending to
 * external consumers. Sub classes are expected to be immutable.
 * 
 * All the standard MatchState methods are overriden and marked as @JsonIgnore to prevent them being serialised
 * 
 * @author gicha
 *
 */
public abstract class SimpleMatchState extends MatchState {

    private static final long serialVersionUID = 1L;

    private Source source;
    private String sourceFeedCode;
    private String sourceFeedEventId;
    private boolean preMatch;
    private boolean matchCompleted;

    @JsonCreator
    public SimpleMatchState(@JsonProperty("source") Source source, @JsonProperty("preMatch") boolean preMatch,
                    @JsonProperty("matchCompleted") boolean matchCompleted) {
        this(preMatch, matchCompleted);
        this.source = source;

    }

    public SimpleMatchState(boolean preMatch, boolean matchCompleted) {
        super();
        this.preMatch = preMatch;
        this.matchCompleted = matchCompleted;
    }

    public SimpleMatchState() {
        super();
        preMatch = true;
    }

    /**
     * should be overridden by sports that support elapsed time to return the current elapsed time
     * 
     * @return
     */
    public int elapsedTime() {
        return 0;
    }

    @Override
    public boolean preMatch() {
        return preMatch;
    }

    public boolean isPreMatch() {
        return preMatch;
    }

    @Override
    public boolean isMatchCompleted() {
        return matchCompleted;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String getSourceFeedCode() {
        return sourceFeedCode;
    }

    public void setSourceFeedCode(String sourceFeedCode) {
        this.sourceFeedCode = sourceFeedCode;
    }

    public String getSourceFeedEventId() {
        return sourceFeedEventId;
    }

    public void setSourceFeedEventId(String sourceFeedEventId) {
        this.sourceFeedEventId = sourceFeedEventId;
    }

    public void setPreMatch(boolean preMatch) {
        this.preMatch = preMatch;
    }

    public void setMatchCompleted(boolean matchCompleted) {
        this.matchCompleted = matchCompleted;
    }



    @Override
    public MatchState copy() {
        /*
         * immutable class so can return this
         */
        return this;
    }

    @Override
    @JsonIgnore
    public MatchIncidentPrompt getNextPrompt() {
        return null;
    }

    @Override
    @JsonIgnore
    public MatchIncident getMatchIncident(String response) {
        return null;
    }

    @Override
    @JsonIgnore
    public LinkedHashMap<String, String> getAsMap() {
        return null;
    }

    @Override
    public String setFromMap(Map<String, String> map) {
        return null;
    }

    @Override
    @JsonIgnore
    public MatchFormat getMatchFormat() {
        return null;
    }

    @Override
    @JsonIgnore
    public int secsLeftInCurrentPeriod() {
        return 0;
    }

    @Override
    @JsonIgnore
    public ElapsedTimeMatchIncidentType getClockStatus() {
        return null;
    }

    @Override
    public boolean isClockRunning() {
        return false;
    }

    @Override
    @JsonIgnore
    public void setClockStatus(ElapsedTimeMatchIncidentType clockStatus) {}

    @Override
    @JsonIgnore
    public long getClockTimeOfLastElapsedTimeFromIncident() {
        return 0;
    }

    @Override
    @JsonIgnore
    public void setClockTimeOfLastElapsedTimeFromIncident(long clockTimeOfLastElapsedTimeFromIncident) {}


    @Override
    @JsonIgnore
    public DatafeedMatchIncidentType getDataFeedStatus() {
        return null;
    }

    @Override
    @JsonIgnore
    public void setDataFeedStatus(DatafeedMatchIncidentType dataFeedStatus) {

    }

    @Override
    @JsonIgnore
    public long getClocktimeOfLastDatafeedMatchIncident() {
        return 0;
    }

    @Override
    @JsonIgnore
    public void setClocktimeOfLastDatafeedMatchIncident(long clocktimeOfLastDatafeedMatchIncident) {}

    @Override
    @JsonIgnore
    public int getNoSuccessfulInPlayParamFindsExecuted() {
        return 0;
    }

    @Override
    @JsonIgnore
    public void setNoSuccessfulInPlayParamFindsExecuted(int noSuccessfulInPlayParamFindsExecuted) {}


    @Override
    @JsonIgnore
    public DataToGenerateStaticMarkets getDataToGenerateStaticMarkets() {
        return null;
    }

    @Override
    @JsonIgnore
    public void setDataToGenerateStaticMarkets(DataToGenerateStaticMarkets dataToGenerateStaticMarkets) {}

    @Override
    @JsonIgnore
    public boolean isDatafeedStateMismatch() {
        return false;
    }

    @Override
    @JsonIgnore
    public boolean updateElapsedTime() {
        return false;
    }

    @Override
    @JsonIgnore
    protected int getSecsSinceLastPriceRecalc() {
        return 0;
    }

    @Override
    @JsonIgnore
    public void setPriceCalcTime() {}

    @Override
    @JsonIgnore
    protected void setClockTimeOfLastElapsedTimeFromIncident() {
        clockTimeOfLastElapsedTimeFromIncident = System.currentTimeMillis();
    }

    @Override
    @JsonIgnore
    public GamePeriod getGamePeriod() {
        return null;
    }

    @Override
    @JsonIgnore
    public long getClockTimeOfLastPriceCalc() {
        return 0;
    }

    @Override
    @JsonIgnore
    public void setClockTimeOfLastPriceCalc(long clockTimeOfLastPriceCalc) {}

    @Override
    public MatchState generateSimpleMatchState() {
        return this;
    }

    @Override
    public String toString() {
        return JsonUtil.marshalJson(this);
    }

    public MatchResultMap generateResultMapFromSimpleMatchState(MatchFormat format) {
        MatchResultMap matchResultMap = new MatchResultMap();
        return matchResultMap;
    }

    public SimpleMatchState generateMatchStateFromMatchResultMap(MatchResultMap result, MatchFormat matchFormat) {
        return this;
    }

    private JSONObject extractJSONObject(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        String className = this.getClass().getSimpleName();
        jsonObject = jsonObject.getJSONObject(className);
        return jsonObject;
    }

    public String formattedGeneralState() {
        String jsonString = JsonUtil.marshalJson(this);
        String result;
        try {
            JSONObject jsonObject = extractJSONObject(jsonString);
            jsonObject.remove("teamSheet");
            jsonObject.remove("subClass");
            jsonObject.remove("eventId");
            jsonObject.remove("requestId");
            result = JsonSimpleFormatter.format(jsonObject, jsonString);
        } catch (JSONException e) {
            result = "SimpleMatchState: error parsing json";
        }
        return result;
    }



    public String formattedPenaltiesState() {
        String jsonString = JsonUtil.marshalJson(this);
        String result;
        try {
            JSONObject jsonObject = extractJSONObject(jsonString);
            jsonObject = jsonObject.getJSONObject("penalties");

            result = JsonSimpleFormatter.format(jsonObject, jsonString);

        } catch (JSONException e) {
            result = "<empty>";
        }
        return result;

    }

    public enum Source {
        ALGO_MANAGER,
        ATS,
        FEED
    }

    public Map<String, String> generalStateAsMap() {
        return convertJsontoMap(this.formattedGeneralState());
    }

    /**
     * default if not overriden by sport specific class
     * 
     * @return
     */
    public Map<String, String> playerStateAsMap() {
        Map<String, String> map = new LinkedHashMap<>(1);
        map.put("Empty", "");
        return map;
    }


    private Map<String, String> convertJsontoMap(String json) {
        /*
         * Hard code to convert to map
         */
        json = json.replaceFirst("scoreInSet1:", "");
        json = json.replaceFirst("scoreInSet2:", "");
        json = json.replaceFirst("scoreInSet3:", "");
        json = json.replaceFirst("scoreInSet4:", "");
        json = json.replaceFirst("scoreInSet5:", "");
        json = json.replaceFirst(" A:", "scoreInSet1.A:");
        json = json.replaceFirst(" A:", "scoreInSet2.A:");
        json = json.replaceFirst(" A:", "scoreInSet3.A:");
        json = json.replaceFirst(" A:", "scoreInSet4.A:");
        json = json.replaceFirst(" A:", "scoreInSet5.A:");
        json = json.replaceFirst(" B:", "scoreInSet1.B:");
        json = json.replaceFirst(" B:", "scoreInSet2.B:");
        json = json.replaceFirst(" B:", "scoreInSet3.B:");
        json = json.replaceFirst(" B:", "scoreInSet4.B:");
        json = json.replaceFirst(" B:", "scoreInSet5.B:");
        Map<String, String> x = Arrays.stream(json.split("\n")).collect(new ToMapCollector());
        return x;
    }



    class ToMapCollector implements Collector<String, LinkedHashMap<String, String>, LinkedHashMap<String, String>> {

        @Override
        public BiConsumer<LinkedHashMap<String, String>, String> accumulator() {
            return (map, s) -> {
                String[] sp = s.split(": ");
                String value = sp.length > 1 ? sp[1] : "";
                map.put(sp[0], value);
            };
        }

        @Override
        public Set<java.util.stream.Collector.Characteristics> characteristics() {
            return Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH));
        }

        @Override
        public BinaryOperator<LinkedHashMap<String, String>> combiner() {
            return null;
        }

        @Override
        public Function<LinkedHashMap<String, String>, LinkedHashMap<String, String>> finisher() {
            return Function.identity();
        }

        @Override
        public Supplier<LinkedHashMap<String, String>> supplier() {
            return LinkedHashMap<String, String>::new;
        }


    }
}
