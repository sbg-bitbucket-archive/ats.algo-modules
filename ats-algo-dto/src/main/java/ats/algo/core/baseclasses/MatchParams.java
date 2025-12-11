package ats.algo.core.baseclasses;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ats.algo.core.MarketGroup;
import ats.algo.core.dto.util.DtoJsonUtil;
import ats.algo.genericsupportfunctions.GCMath;
import ats.algo.genericsupportfunctions.Gaussian;
import ats.core.util.json.JsonUtil;

/**
 * defines the set of skill related parameters that drive the model for a particular sport
 *
 * @author Geoff
 *
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class MatchParams implements Serializable {

    private static final long serialVersionUID = 2L;

    protected long eventId;
    protected String userId;


    protected LinkedHashMap<String, MatchParam> paramMap;

    /**
     * Constructor for normal use
     */
    public MatchParams() {
        paramMap = new LinkedHashMap<String, MatchParam>();
    }

    /**
     * Constructor used by json deserialization
     * 
     * @param jsonMap
     */
    public MatchParams(Map<String, Object> jsonMap) {
        this();
        Object token1 = jsonMap.get("eventId");
        if (token1 != null)
            eventId = DtoJsonUtil.toLong(token1);

    }

    /**
     * Gets the eventId for the match these params are associated with
     * 
     * @return
     */
    public long getEventId() {
        return eventId;
    }

    /**
     * Sets the eventId for the match these params are associated with
     *
     * @param eventId
     */
    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * gets the set of matchParams as a Map
     * 
     * @return
     */
    public final LinkedHashMap<String, MatchParam> getParamMap() {
        return paramMap;
    }

    /**
     * sets the set of matchParams as a Map
     * 
     * @return
     */
    public void setParamMap(LinkedHashMap<String, MatchParam> paramMap) {
        this.paramMap = paramMap;
    }

    /**
     * creates a new object which contains the paramMap, suitable for serializing via json. All subclasses of
     * MatchParams can be converted into a GenericMatchParams object
     * 
     * @return
     */
    public GenericMatchParams generateGenericMatchParams() {
        GenericMatchParams genericMatchParams = new GenericMatchParams();
        genericMatchParams.setEventId(eventId);
        genericMatchParams.setUserId(userId);
        genericMatchParams.setOriginatingClassName(this.getClass().getCanonicalName());
        genericMatchParams.setParamMap(paramMap);
        return genericMatchParams;
    }

    /**
     * sets the properties of this xxxMatchParam object from the properties contained in the GenericMatchParams object
     */
    @JsonIgnore
    public void setFromGenericMatchParams(GenericMatchParams genericMatchParams) {
        this.eventId = genericMatchParams.getEventId();
        this.userId = genericMatchParams.getUserId();
        this.setFromMap(genericMatchParams.getParamMap());
    }

    /**
     * makes a copy of this object, replicating all the params to the new instance of the class
     *
     * @return
     */
    public MatchParams copy() {
        /*
         * create a new instance of the correct class
         */
        Class<? extends MatchParams> matchParamsClass = this.getClass();
        Constructor<?> constructor;
        MatchParams cc = null;
        try {
            constructor = matchParamsClass.getConstructor();
            cc = (MatchParams) constructor.newInstance();
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                        | IllegalArgumentException | InvocationTargetException e) {
            throw new IllegalArgumentException(
                            String.format("Can't construct new instance of %s", matchParamsClass.getSimpleName()));
        }
        /*
         * set each of the matchParams to be the same state
         */
        cc.setEqualTo(this);
        return cc;
    }

    /**
     * sets all the state for this instance to be the same as that for the specified object
     *
     * @param other the object who's state is to be copied to this
     */
    public void setEqualTo(MatchParams other) {
        this.setEventId(other.getEventId());
        this.setUserId(other.getUserId());
        for (Entry<String, MatchParam> entry : other.getParamMap().entrySet()) {
            String key = entry.getKey();
            MatchParam otherMatchParam = entry.getValue();
            MatchParam thisMatchParam = this.paramMap.get(key);
            if (thisMatchParam == null) {
                thisMatchParam = otherMatchParam.copy();
                paramMap.put(key, thisMatchParam);
            }
            thisMatchParam.setEqualTo(otherMatchParam);
        }
    }

    /**
     * sets the default values of each matchParam. The defaults may depend on the MatchFormat, so e.g. in tennis ATP
     * players will have higher average onServePcts than WTA players
     */
    public abstract void setDefaultParams(MatchFormat matchFormat);


    /**
     * initialises each of the parameters from the contents of the json map
     * 
     * @param delegate the map supplied by Jackson when converting from json to object
     */
    public void applyParams(Map<String, Object> delegate) {
        /*
         * do nothing in the base clase
         */
    }


    private MatchParam[] matchParamArray;

    /**
     * Used to hold the results of the getArray method
     *
     * @author Geoff
     *
     */
    public class ParamArray {
        public double[] x; // holds the values of the parameter
        public double[] delta; // holds the default initial delta to use when
                               // param finding

        ParamArray(int n) {
            x = new double[n];
            delta = new double[n];
        }

        public int size() {
            return x.length;
        }

        public String toString() {
            String s = "";
            for (int i = 0; i < x.length; i++) {
                s += String.format("i: %d, x: %.3f, delta: %.4f\n", i, x[i], delta[i]);
            }
            return s;
        }
    }

    private boolean includeStdDevns;
    private boolean rotateSearchAxes;
    private boolean nParamsEven;

    private static boolean transformParams = true;

    @JsonIgnore
    public static void setTransformParams(boolean transformParams) {
        MatchParams.transformParams = transformParams;
    }

    public class TransformedParam {
        double value;
        double delta;
    }

    /**
     * converts the parameters into an array, for use by the generic param finding algos
     *
     * @param marketGroups if null all parameters are selected; otherwise just those belonging to the specified market
     *        groups
     * @param includeStdDevns if true then stdDevns are added to the list of params to be optimised
     * @param rotateSearchAxes if true then rotate axes of search by 45 degrees. Works best if successive pairs of
     *        params in the map are related - e.g. onSrvPctA, onSrvPctB
     *
     * @return
     */
    @JsonIgnore
    public ParamArray getAsArray(Collection<MarketGroup> marketGroups, boolean includeStdDevns,
                    boolean rotateSearchAxes) {
        /*
         * get as Map then convert to array. Only include those params which belong to the correct market group and for
         * which stdDevn >0
         */
        this.includeStdDevns = includeStdDevns;
        this.rotateSearchAxes = rotateSearchAxes;
        ArrayList<MatchParam> paramList = new ArrayList<MatchParam>();
        for (Entry<String, MatchParam> entry : paramMap.entrySet()) {
            MatchParam matchParam = entry.getValue();
            if (inMarketGroups(marketGroups, matchParam) && (matchParam.getGaussian().getStdDevn() != 0)
                            && (matchParam.getMatchParameterType() != MatchParamType.TRADER_CONTROL)) {
                paramList.add(matchParam);
            }
        }
        /*
         * convert collection to array, rotating axes if required
         */
        matchParamArray = paramList.toArray(new MatchParam[paramList.size()]);
        int n = matchParamArray.length;
        int arraySize;
        if (includeStdDevns)
            arraySize = 2 * n;
        else
            arraySize = n;
        ParamArray paramArray = new ParamArray(arraySize);
        int m;
        /*
         * when converting to array treat params two at a time, rotating the axis if reqd
         */
        nParamsEven = 2 * (n / 2) == n;
        if (nParamsEven)
            m = n / 2;
        else
            m = (n - 1) / 2;
        for (int i = 0; i < m; i++) {
            int j = 2 * i;
            double mean1;
            double delta1;
            double mean2;
            double delta2;
            if (transformParams) {
                TransformedParam p1 = transformParam(matchParamArray[j]);
                TransformedParam p2 = transformParam(matchParamArray[j + 1]);
                mean1 = p1.value;
                delta1 = p1.delta;
                mean2 = p2.value;
                delta2 = p2.delta;
            } else {
                mean1 = matchParamArray[j].getGaussian().getMean();
                delta1 = matchParamArray[j].getGaussian().getStdDevn();
                mean2 = matchParamArray[j + 1].getGaussian().getMean();
                delta2 = matchParamArray[j + 1].getGaussian().getStdDevn();
            }
            if (rotateSearchAxes) {
                double delta = Math.sqrt(delta1 * delta1 + delta2 * delta2) / 2;
                paramArray.x[j] = (mean1 + mean2) / 2;
                paramArray.delta[j] = delta;
                paramArray.x[j + 1] = (mean1 - mean2) / 2;
                paramArray.delta[j + 1] = delta;
            } else {
                paramArray.x[j] = mean1;
                paramArray.delta[j] = delta1;
                paramArray.x[j + 1] = mean2;
                paramArray.delta[j + 1] = delta2;
            }
            if (includeStdDevns) {
                double stdDevn1 = matchParamArray[j].getGaussian().getStdDevn();
                double stdDevn2 = matchParamArray[j + 1].getGaussian().getStdDevn();
                paramArray.x[n + j] = stdDevn1;
                paramArray.delta[n + j] = stdDevn1 / 10.0;
                paramArray.x[n + j + 1] = stdDevn2;
                paramArray.delta[n + j + 1] = stdDevn2 / 10.0;
            }
        }
        if (!nParamsEven) {
            /*
             * add the odd one at the end of the ParamMap
             */
            double mean;
            double delta;
            if (transformParams) {
                TransformedParam p = transformParam(matchParamArray[n - 1]);
                mean = p.value;
                delta = p.delta;
            } else {
                mean = matchParamArray[n - 1].getGaussian().getMean();
                delta = matchParamArray[n - 1].getGaussian().getStdDevn();
            }
            paramArray.x[n - 1] = mean;
            paramArray.delta[n - 1] = delta;
            if (includeStdDevns) {
                double stdDevn = matchParamArray[n - 1].getGaussian().getStdDevn();
                paramArray.x[2 * n - 1] = stdDevn;
                paramArray.delta[2 * n - 1] = stdDevn / 10.0;
            }
        }
        return paramArray;
    }

    TransformedParam transformParam(MatchParam matchParam) {
        double mean = matchParam.getGaussian().getMean();
        double d = matchParam.getGaussian().getStdDevn() / 2;
        double lBound = matchParam.getMinAllowedParamValue();
        double uBound = matchParam.getMaxAllowedParamValue();
        TransformedParam p = new TransformedParam();
        p.value = Math.log((mean - lBound) / (uBound - mean));
        /*
         * Find a distance measure in the transformed space
         */
        double u;
        double v;
        if (mean + d >= uBound) {
            u = mean;
            v = mean - 2 * d;
        } else if (mean - d <= lBound) {
            u = mean + 2 * d;
            v = mean;
        } else {
            u = mean + d;
            v = mean - d;
        }
        if (u >= uBound || v <= lBound) {
            throw new IllegalArgumentException("Can't transform matchParam: " + matchParam.toString());
        }

        p.delta = Math.log(((u - lBound) * (uBound - v)) / ((uBound - u) * (v - lBound)));
        return p;
    }

    /**
     * determine whether matchParam belongs to one of the specified market groups
     *
     * @param marketGroups if null then always returns true
     * @param matchParam
     * @return true if marketGroups is null or matchParam group is in marketGroups or matchParam group is unspecified
     */
    public static boolean inMarketGroups(Collection<MarketGroup> marketGroups, MatchParam matchParam) {
        MarketGroup paramMarketGroup = matchParam.getMarketGroup();
        if (marketGroups == null || paramMarketGroup == MarketGroup.NOT_SPECIFIED)
            return true;
        for (MarketGroup marketGroup : marketGroups) {
            if (paramMarketGroup == marketGroup)
                return true;
        }
        return false;
    }

    /**
     * sets the value of the parameters from an array. Used following completion of the generic param finding algo the
     * getAsArray method must previously have been called
     *
     * @param x
     */
    public void setFromArray(double[] x) {
        /*
         * The array was initialised by getAsArray to point at each of the matchParams
         */

        if (matchParamArray == null)
            throw new IllegalArgumentException("call to setFromArray must be preceded by call to convertToArray");
        int n = matchParamArray.length;
        int m;
        if (nParamsEven)
            m = n / 2;
        else
            m = (n - 1) / 2;
        for (int i = 0; i < m; i++) {
            int j = 2 * i;
            Gaussian gaussian1 = matchParamArray[j].getGaussian();
            Gaussian gaussian2 = matchParamArray[j + 1].getGaussian();
            double m1 = x[j];
            double m2 = x[j + 1];
            if (rotateSearchAxes) {
                m1 = x[j] + x[j + 1];
                m2 = x[j] - x[j + 1];
            } else {
                m1 = x[j];
                m2 = x[j + 1];
            }
            if (transformParams) {
                double a1 = matchParamArray[j].getMinAllowedParamValue();
                double b1 = matchParamArray[j].getMaxAllowedParamValue();
                double a2 = matchParamArray[j + 1].getMinAllowedParamValue();
                double b2 = matchParamArray[j + 1].getMaxAllowedParamValue();
                m1 = a1 + (b1 - a1) / (1 + Math.exp(-m1));
                m2 = a2 + (b2 - a2) / (1 + Math.exp(-m2));
            }
            gaussian1.setMean(GCMath.round(m1, 8));
            gaussian2.setMean(GCMath.round(m2, 8));

            if (this.includeStdDevns) {
                gaussian1.setStdDevn(x[n + j]);
                gaussian2.setStdDevn(x[n + j + 1]);
            }
        }
        if (!nParamsEven) {
            Gaussian gaussian = matchParamArray[n - 1].getGaussian();
            double m1 = x[n - 1];
            if (transformParams) {
                double a = matchParamArray[n - 1].getMinAllowedParamValue();
                double b = matchParamArray[n - 1].getMaxAllowedParamValue();
                m1 = a + (b - a) / (1 + Math.exp(-m1));
            }
            gaussian.setMean(GCMath.round(m1, 8));
            if (includeStdDevns) {
                gaussian.setStdDevn(x[2 * n - 1]);
            }
        }

    }

    /**
     * updates the MatchParams object from the contents of the map
     * 
     * @param map
     */
    public void setFromMap(Map<String, MatchParam> map) {
        for (Entry<String, MatchParam> e : map.entrySet()) {
            MatchParam matchParam = paramMap.get(e.getKey());
            if (matchParam != null)
                matchParam.setEqualTo(e.getValue());
        }
    }

    /**
     * rounds matchParams to avoid excessive nos of digits following a param find. Means are rounded to 3 decimal
     * places, sd's to 4
     */
    public void roundParamValues() {
        for (MatchParam matchParam : paramMap.values()) {
            Gaussian gaussian = matchParam.getGaussian();
            gaussian.setMean(GCMath.round(gaussian.getMean(), 3));
            gaussian.setStdDevn(GCMath.round(gaussian.getStdDevn(), 4));
        }

    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (eventId ^ (eventId >>> 32));
        result = prime * result + ((paramMap == null) ? 0 : paramMap.hashCode());
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MatchParams other = (MatchParams) obj;
        if (eventId != other.eventId)
            return false;
        if (paramMap == null) {
            if (other.paramMap != null)
                return false;
        } else if (!paramMap.equals(other.paramMap))
            return false;
        if (userId == null) {
            if (other.userId != null)
                return false;
        } else if (!userId.equals(other.userId))
            return false;
        return true;
    }

    /**
     * specifies whether the bias should be applied to MatchParams or not when calling the nextRandom() of
     * getBiasAdustedMean() methods
     *
     * @param applyBias
     */
    @JsonIgnore
    public void setApplyBias(boolean applyBias) {
        for (MatchParam matchParam : getParamMap().values()) {
            matchParam.getGaussian().setApplyBias(applyBias);
        }
    }

    @JsonIgnore
    public boolean isBiased() {
        for (MatchParam matchParam : getParamMap().values()) {
            if (matchParam.getGaussian().getBias() != 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * resets to zero the bias for each param
     * 
     * @return true if any bias was non zero and was changed
     */
    public boolean resetBias() {
        boolean biasWasReset = false;
        for (MatchParam matchParam : getParamMap().values()) {
            Gaussian gaussian = matchParam.getGaussian();
            if (gaussian.getBias() != 0) {
                gaussian.setBias(0);
                biasWasReset = true;
            }
        }
        return biasWasReset;
    }

    /**
     * converts the matchParamsMap to a string
     * 
     * @param inputMatchParamsMap
     * @return
     */
    public static String paramsMapToString(LinkedHashMap<String, MatchParam> matchParamsMap) {
        String s = "";
        for (Entry<String, MatchParam> e : matchParamsMap.entrySet()) {
            s += e.getKey() + "= " + e.getValue().toString() + "\n";
        }
        return s;
    }

    public String toString() {
        return JsonUtil.marshalJson(this);
    }

    protected void updateParamMap() {
        Set<String> oldKeys = paramMap.keySet();
        paramMap.keySet().removeAll(oldKeys);
    }



}
