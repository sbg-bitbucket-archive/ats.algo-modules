package ats.algo.core.baseclasses;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * container to hold the list of possible values that each property in the associated MatchFormat class is allowed to
 * take. The purpose is to to provide the info required by a gUI to present the user with a drop down list of choices
 * for that property.
 * 
 * @author Geoff
 *
 */
public abstract class MatchFormatOptions implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Map<String, Set<String>> propertyOptionSets;

    public MatchFormatOptions() {
        propertyOptionSets = new HashMap<String, Set<String>>();
    }

    /**
     * for json use only gets the list of all the possible options that can be set for each of the properties of the
     * Matchformat object
     * 
     * @return
     */
    public Map<String, Set<String>> getPropertyOptionSets() {
        return propertyOptionSets;
    }

    /**
     * for json use only gets the list of all the possible options that can be set for each of the properties of the
     * Matchformat object
     *
     * @param formatEnums
     */
    public void setPropertyOptionSets(Map<String, Set<String>> formatEnums) {
        this.propertyOptionSets = formatEnums;
    }

    /**
     * defines the possible values that the specified property can take
     * 
     * @param property should be exactly the same name as is used for this property in the corresponding
     *        xxxMatchFormatClass
     * @param propertyOptions a set of strings listing all of the possible values that this property is allowed to take
     */
    public void addPropertyOptions(String property, Set<String> propertyOptions) {
        propertyOptionSets.put(property, propertyOptions);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((propertyOptionSets == null) ? 0 : propertyOptionSets.hashCode());
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
        MatchFormatOptions other = (MatchFormatOptions) obj;
        if (propertyOptionSets == null) {
            if (other.propertyOptionSets != null)
                return false;
        } else if (!propertyOptionSets.equals(other.propertyOptionSets))
            return false;
        return true;
    }



}
