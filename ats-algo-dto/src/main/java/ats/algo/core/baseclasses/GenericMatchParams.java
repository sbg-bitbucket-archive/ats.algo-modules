package ats.algo.core.baseclasses;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ats.algo.core.common.TeamSheet;

/**
 * This is the container that is used to send matchParam data to/from ATS via Json serialization. All other
 * xxxMatchPrams classes can be populated from the contents of this container by calling the method
 * 
 * @author Geoff
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GenericMatchParams extends MatchParams {

    private static final long serialVersionUID = 1L;

    private String originatingClassName;

    public GenericMatchParams() {
        super();
    }

    @Override
    public GenericMatchParams generateGenericMatchParams() {
        return this;
    }

    public String getOriginatingClassName() {
        return originatingClassName;
    }

    public void setOriginatingClassName(String originatingClassName) {
        this.originatingClassName = originatingClassName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (eventId ^ (eventId >>> 32));
        result = prime * result + ((paramMap == null) ? 0 : paramMap.hashCode());
        result = prime * result + ((originatingClassName == null) ? 0 : originatingClassName.hashCode());
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
        GenericMatchParams other = (GenericMatchParams) obj;
        if (eventId != other.eventId)
            return false;
        if (paramMap == null) {
            if (other.paramMap != null)
                return false;
        } else if (!paramMap.equals(other.paramMap))
            return false;
        if (originatingClassName == null) {
            if (other.originatingClassName != null)
                return false;
        } else if (!originatingClassName.equals(other.originatingClassName))
            return false;
        return true;
    }

    /**
     * generates a new instance of the xxxMatchParams class that generated this GenericMatchParams class.
     * 
     * @return
     */
    public MatchParams generateXxxMatchParams() {
        MatchParams matchParams;
        try {
            @SuppressWarnings("unchecked")
            Class<MatchParams> cls = (Class<MatchParams>) Class.forName(this.originatingClassName);
            Constructor<MatchParams> cx = cls.getConstructor();
            matchParams = (MatchParams) cx.newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
                        | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            return null;
        }
        if (matchParams != null)
            matchParams.setFromGenericMatchParams(this);
        return matchParams;
    }

    /**
     * if the appropriate method exists for the sport in question then it is invoked to update the param map. If not, no
     * action is taken
     * 
     * @param eventTier
     */
    public boolean updateParamMapForEventTier(long eventTier) {
        try {
            @SuppressWarnings("unchecked")
            Class<MatchParams> cls = (Class<MatchParams>) Class.forName(this.originatingClassName);
            Method updateParamMapForEventTierMethod =
                            cls.getMethod("updateParamMapForEventTier", long.class, GenericMatchParams.class);
            /*
             * if we get this far then the required method exists so invoke it to update the param map
             */
            updateParamMapForEventTierMethod.invoke(null, eventTier, this);
            return true;
        } catch (ClassNotFoundException | SecurityException | IllegalArgumentException | NoSuchMethodException
                        | IllegalAccessException | InvocationTargetException e) {
            return false;
        }
    }

    public void updatePlayerMatchParams(TeamSheet teamSheet) {
        try {
            @SuppressWarnings("unchecked")
            Class<MatchParams> cls = (Class<MatchParams>) Class.forName(this.originatingClassName);
            Method updatePlayerMatchParamsMethod =
                            cls.getMethod("updatePlayerMatchParams", TeamSheet.class, GenericMatchParams.class);
            /*
             * if we get this far then the required method exists so invoke it
             */
            updatePlayerMatchParamsMethod.invoke(null, teamSheet, this);
        } catch (ClassNotFoundException | SecurityException | IllegalArgumentException | NoSuchMethodException
                        | IllegalAccessException | InvocationTargetException e) {
            return;
        }
    }

    @Override
    public void setDefaultParams(MatchFormat matchFormat) {}

    @Override
    public MatchParams copy() {
        GenericMatchParams cc = (GenericMatchParams) super.copy();
        cc.setOriginatingClassName(originatingClassName);
        return cc;
    }

    /**
     * updates the MatchParamObject from the contents of the map
     * 
     * @param map
     */
    @Override
    public void setFromMap(Map<String, MatchParam> map) {
        LinkedHashMap<String, MatchParam> newMap = new LinkedHashMap<String, MatchParam>(map.size());
        for (Entry<String, MatchParam> e : map.entrySet()) {
            MatchParam matchParam = e.getValue().copy();
            newMap.put(e.getKey(), matchParam);
        }
        this.paramMap = newMap;

    }

}
